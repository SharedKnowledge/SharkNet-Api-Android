package net.sharksystem.api.dao_impl;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import net.sharkfw.asip.ASIPInformationSpace;
import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.engine.ASIPOutMessage;
import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.knowledgeBase.sync.manager.SyncComponent;
import net.sharkfw.security.PkiStorage;
import net.sharkfw.security.SharkPkiStorage;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkNotSupportedException;
import net.sharksystem.api.dao_interfaces.ContactDao;
import net.sharksystem.api.dao_interfaces.SharkNetApi;
import net.sharksystem.api.models.Chat;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.models.Settings;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;
import net.sharksystem.api.shark.peer.NearbyPeerManager;
import net.sharksystem.api.shark.ports.NfcPkiPort;
import net.sharksystem.api.shark.ports.NfcPkiPortEventListener;
import net.sharksystem.api.shark.ports.NfcPkiPortListener;

import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by j4rvis on 3/26/17.
 */

public class SharkNetApiImpl implements SharkNetApi {

    private final Context mContext;
    private SharkKB mRootKb = new InMemoSharkKB();
    private AndroidSharkEngine mEngine;
    private ChatDao mChatDao;
    private ContactDao mContactDao;
    private SettingsDao mSettingsDao;
    private Contact mAccount;
    private boolean mIsDiscovering = false;

    public SharkNetApiImpl(Context context) {
        mEngine = new AndroidSharkEngine(context);
        mEngine.getSyncManager().addSyncMergeListener(this);
        mContext = context;
        try {
            mContactDao = new CachedContactDaoImpl(new InMemoSharkKB(InMemoSharkKB.createInMemoSemanticNet(), InMemoSharkKB.createInMemoSemanticNet(), mRootKb.getPeersAsTaxonomy(), InMemoSharkKB.createInMemoSpatialSTSet(), InMemoSharkKB.createInMemoTimeSTSet()));
            mChatDao = new ChatDao(mEngine, mRootKb, mContactDao);
            mSettingsDao = new SettingsDao(mRootKb);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
        if(getSettings().getAccountTag()!=null){
            mAccount = mContactDao.get(getSettings().getAccountTag());
        }
    }

    public AndroidSharkEngine getSharkEngine() {
        return mEngine;
    }

    // Account Methods

    public Contact getAccount() {
        return mAccount;
    }

    public void setAccount(Contact contact) {
        if(mAccount==null){
            mContactDao.add(contact);
            mEngine.setEngineOwnerPeer(contact.getTag());
        } else {
            mContactDao.update(contact);
        }

        mAccount = contact;
        // Update Account in Settings
        Settings settings = getSettings();
        settings.setAccountTag(contact.getTag());
        setSettings(settings);

        if(settings.getMailSmtpServer() != null && !settings.getMailSmtpServer().isEmpty()
                || settings.getMailUsername() != null && !settings.getMailUsername().isEmpty()
                || settings.getMailPassword() != null && !settings.getMailPassword().isEmpty()
                || settings.getMailPopServer() != null && !settings.getMailPopServer().isEmpty()
                || settings.getMailAddress() != null && !settings.getMailAddress().isEmpty()){

            // TODO
            mEngine.setBasicMailConfiguration(
                    settings.getMailSmtpServer(),
                    settings.getMailUsername(),
                    settings.getMailPassword(),
                    settings.getMailPopServer(),
                    settings.getMailAddress());
            try {
                mEngine.startMail();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setSettings(Settings settings) {
        mSettingsDao.setSettings(settings);
    }

    @Override
    public Settings getSettings() {
        return mSettingsDao.getSettings();
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
    public void pingMailServer(final SemanticTag type, final PeerSemanticTag receiver) {
        try {
            mEngine.startMail();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ASIPOutMessage message = mEngine.createASIPOutMessage(receiver.getAddresses(), mEngine.getOwner(), receiver, null, null, null, type, 1);
                try {
                    message.expose(InMemoSharkKB.createInMemoASIPInterest());
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    // Shark methods

    public void addRadarListener(NearbyPeerManager.NearbyPeerListener peerListener) {
        mEngine.addNearbyPeerListener(peerListener);
    }

    @Override
    public void allowSyncInvitation(boolean allow) {
        mEngine.getSyncManager().allowInvitation(allow);
    }

    public void startRadar() {
        if (!mIsDiscovering) {
            mEngine.startDiscovery();
            mIsDiscovering = true;
        }
    }

    public void stopRadar() {
        if (mIsDiscovering) {
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
//            ASIPKnowledgeConverter asipKnowledgeConverter = new ASIPKnowledgeConverter(knowledge);
//            L.d("Initial length: " + (asipKnowledgeConverter.getContent().length + asipKnowledgeConverter.getSerializedKnowledge().length()), this);
//            ContactDaoImpl contactDao = new ContactDaoImpl((SharkKB) knowledge);
//            for (SharkCertificate sharkCertificate : sharkCertificatesBySigner) {
//                contactDao.add(getContact(sharkCertificate.getOwner()));
//            }
//            contactDao.add(mAccount);

//            ASIPKnowledgeConverter asipKnowledgeConverter2 = new ASIPKnowledgeConverter(knowledge);
//            L.d("contact added length: " + (asipKnowledgeConverter2.getContent().length + asipKnowledgeConverter2.getSerializedKnowledge().length()), this);
//            L.d("Image length: " + asipKnowledgeConverter2.getContent().length, this);

            NfcPkiPort nfcPkiPort = new NfcPkiPort(mEngine, this, (NfcPkiPortListener) activity);
            mEngine.setupNfc(activity, nfcPkiPort);
            mEngine.stopNfc();
            mEngine.offer(knowledge);
            return nfcPkiPort;
        } catch (SharkProtocolNotSupportedException | SharkNotSupportedException e) {
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

    @Override
    public void initPki() {
        SharkPkiStorage pkiStorage = (SharkPkiStorage) getSharkEngine().getPKIStorage();

        try {
            pkiStorage.setPkiStorageOwner(getAccount().getTag());
            pkiStorage.generateNewKeyPair(1000 * 60 * 60 * 24 * 365);
        } catch (SharkKBException | NoSuchAlgorithmException | IOException e) {
            L.e(e.getMessage());
        }
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewMerge(SyncComponent component, SharkKB changes) {
        changesHaveContacts(changes);

        int numberOfMessages = 0;
        try {
            Iterator<ASIPInformationSpace> iterator = changes.getAllInformationSpaces();
            while (iterator.hasNext()) {
                ASIPInformationSpace next = iterator.next();
                if (SharkCSAlgebra.isIn(next.getASIPSpace().getTypes(), MessageDao.MESSAGE_TYPE)) {
                    numberOfMessages++;
                }
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(android.R.drawable.ic_menu_compass);

        Chat chat = getChat(component.getUniqueName());
        if(chat != null){
            String chatTitle = "";
            if (chat.getTitle() != null) {
                List<Contact> contacts = chat.getContacts();
                contacts.remove(mAccount);
                if (contacts.size() == 1) chatTitle = "mit " + contacts.get(0).getName();
            } else {
                chatTitle = chat.getTitle();
            }
            String messageHeader = (numberOfMessages > 1 ? "Nachrichten" : "Nachricht");
            mBuilder.setContentTitle("Neue " + messageHeader);
            mBuilder.setContentText("Du hast " + numberOfMessages + " neue " + messageHeader + " in dem Chat " + chatTitle);
        } else {
            mBuilder.setContentTitle("Neuer Chat").setContentText(" Du hast einen neuen Chat.");
        }

        NotificationManager mNotifyMgr = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and   issues it.
        mNotifyMgr.notify(001, mBuilder.build());
    }

    private void changesHaveContacts(SharkKB changes){
        ContactDaoImpl contactDao = new ContactDaoImpl(changes);
        List<Contact> all = contactDao.getAll();
        if(all == null || all.isEmpty()) return;

        for (Contact contact : all){
            updateContact(contact);
        }
    }
}
