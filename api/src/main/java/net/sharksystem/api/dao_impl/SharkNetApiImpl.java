package net.sharksystem.api.dao_impl;

import android.app.Activity;
import android.content.Context;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.engine.ASIPOutMessage;
import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.asip.serialization.ASIPKnowledgeConverter;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.security.PkiStorage;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkNotSupportedException;
import net.sharkfw.system.SharkSecurityException;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.api.shark.ports.NfcPkiPort;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;
import net.sharksystem.api.shark.ports.NfcPkiPortListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by j4rvis on 3/26/17.
 */

public class SharkNetApiImpl implements SharkNetApi {

    private ChatDao mChatDao;
    private ContactDao mContactDao;
    private SharkKB mRootKb = new InMemoSharkKB();
    private AndroidSharkEngine mEngine;
    private Contact mAccount;
    private boolean mIsDiscovering = false;


    public SharkNetApiImpl() {
        try {
            mContactDao = new ContactDao(new InMemoSharkKB(InMemoSharkKB.createInMemoSemanticNet(), InMemoSharkKB.createInMemoSemanticNet(), mRootKb.getPeersAsTaxonomy(), InMemoSharkKB.createInMemoSpatialSTSet(), InMemoSharkKB.createInMemoTimeSTSet()));
            mChatDao = new ChatDao(mRootKb, mContactDao);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    public void initSharkEngine(Context context) {
        mEngine = new AndroidSharkEngine(context);
//        mEngine.setEngineOwnerPeer(mAccount.getTag());
    }

    public AndroidSharkEngine getSharkEngine() {
        return mEngine;
    }

    // Account Methods

    public Contact getAccount() {
        return mAccount;
    }

    public void setAccount(Contact contact) {
        mContactDao.add(contact);
        mAccount = contact;
        mEngine.setEngineOwnerPeer(mAccount.getTag());
    }

    // DAO Methods

    public List<Chat> getChats() {
        return mChatDao.getAll();
    }

    public Chat getChat(SemanticTag tag) {
        return mChatDao.get(tag);
    }

    public void addChat(Chat chat) {
        mChatDao.add(chat);
    }

    public void updateChat(Chat chat) {
        mChatDao.update(chat);
    }

    public void removeChat(Chat chat) {
        mChatDao.remove(chat);
    }

    public int numberOfChats() {
        return mChatDao.size();
    }

    public List<Contact> getContacts() {
        return mContactDao.getAll();
    }

    public Contact getContact(PeerSemanticTag tag) {
        return mContactDao.get(tag);
    }

    public void addContact(Contact contact) {
        mContactDao.add(contact);
    }

    public void updateContact(Contact contact) {
        mContactDao.update(contact);
    }

    public void removeContact(Contact contact) {
        mContactDao.remove(contact);
    }

    public int numberOfContacts() {
        return mContactDao.size();
    }

    @Override
    public void pingMailServer(SemanticTag type, PeerSemanticTag receiver) {
        try {
            mEngine.startMail();
            ASIPOutMessage message = mEngine.createASIPOutMessage(receiver.getAddresses(), mEngine.getOwner(), receiver, null, null, null, type, 1);
            message.expose(InMemoSharkKB.createInMemoASIPInterest());
        } catch (SharkKBException | IOException e) {
            e.printStackTrace();
        }
    }

    // Shark methods

    public void addRadarListener(NearbyPeerManager.NearbyPeerListener peerListener) {
        mEngine.addNearbyPeerListener(peerListener);
    }

    public void startRadar() {
        if(!mIsDiscovering){
            mEngine.startDiscovery();
            mIsDiscovering = true;
        }
    }

    public void stopRadar() {
        if(mIsDiscovering){
            mEngine.stopDiscovery();
            mIsDiscovering = false;
        }
    }

    public NfcPkiPortEventListener initNFC(Activity activity) {
        PkiStorage pkiStorage = mEngine.getPKIStorage();
        try {
//            List<SharkCertificate> sharkCertificatesBySigner =
//                    pkiStorage.getSharkCertificatesBySigner(mAccount.getTag());
            ASIPKnowledge knowledge = pkiStorage.getPublicKeyAsKnowledge(true);

            // TODO remove logs

            ASIPKnowledgeConverter asipKnowledgeConverter = new ASIPKnowledgeConverter(knowledge);
            L.d("Initial length: " + (asipKnowledgeConverter.getContent().length + asipKnowledgeConverter.getSerializedKnowledge().length()), this);
            ContactDao contactDao = new ContactDao((SharkKB) knowledge);
//            for (SharkCertificate sharkCertificate : sharkCertificatesBySigner) {
//                contactDao.add(getContact(sharkCertificate.getOwner()));
//            }
            contactDao.add(mAccount);

            ASIPKnowledgeConverter asipKnowledgeConverter2 = new ASIPKnowledgeConverter(knowledge);
            L.d("contact added length: " + (asipKnowledgeConverter2.getContent().length + asipKnowledgeConverter2.getSerializedKnowledge().length()), this);
//            L.d("Merged contact data into keys", this);
            L.d(L.kb2String((SharkKB) knowledge), this);

            // TODO remove logs

            NfcPkiPort nfcPkiPort = new NfcPkiPort(mEngine, this, (NfcPkiPortListener) activity);
            mEngine.setupNfc(activity, nfcPkiPort);
            mEngine.stopNfc();
            mEngine.offer(knowledge);
            return nfcPkiPort;
        } catch (SharkKBException | SharkProtocolNotSupportedException | SharkNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void startNFC() {
        try {
            mEngine.startNfc();
        } catch (SharkProtocolNotSupportedException | IOException e) {
            e.printStackTrace();
        }

    }

    public void stopNFC() {
        try {
            mEngine.stopNfc();
        } catch (SharkProtocolNotSupportedException e) {
            e.printStackTrace();
        }
    }


}
