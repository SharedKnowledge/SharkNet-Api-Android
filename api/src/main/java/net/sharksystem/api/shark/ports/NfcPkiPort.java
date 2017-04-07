package net.sharksystem.api.shark.ports;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.engine.ASIPConnection;
import net.sharkfw.asip.engine.ASIPInMessage;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.ports.KnowledgePort;
import net.sharkfw.security.PkiStorage;
import net.sharkfw.security.SharkCertificate;
import net.sharkfw.security.SharkPkiStorage;
import net.sharkfw.security.SharkPublicKey;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkException;
import net.sharksystem.api.dao_impl.ContactDao;
import net.sharksystem.api.dao_impl.SharkNetApiImpl;
import net.sharksystem.api.models.Contact;
import net.sharksystem.api.shark.protocols.nfc.NfcMessageStub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 2/4/17.
 */

public class NfcPkiPort extends KnowledgePort implements NfcPkiPortEventListener, NfcMessageStub.NFCMessageListener{

    private final NfcPkiPortListener mPortListener;
    private final PkiStorage mPkiStorage;
    private SharkPkiStorage mTempPkiStorage;
    private SharkPublicKey mPublicKey;
    private List<SharkCertificate> mCertificates;
    private ASIPKnowledge mKnowledge;
    private List<Contact> mContacts;

    public NfcPkiPort(SharkEngine se, NfcPkiPortListener portListener) {
        super(se);
        this.mPortListener = portListener;
        this.mPkiStorage = this.se.getPKIStorage();
    }

    @Override
    protected void handleInsert(ASIPInMessage message, ASIPConnection asipConnection, ASIPKnowledge asipKnowledge) {
//         check if ASIPInMessage is of type 'NFCMessage'
        if(!SharkCSAlgebra.identical(message.getType(), NfcMessageStub.NFC_MESSAGE_TYPE)){
            return;
        }

        // Now retrieve the key and the mCertificates out of the mKnowledge
        // best way should be to create a new PkiStorage and use the mKnowledge as inMemokb.

        mKnowledge = asipKnowledge;

        mTempPkiStorage = new SharkPkiStorage((SharkKB) asipKnowledge);
        List<SharkPublicKey> unsignedPublicKeys = new ArrayList<>();
        try {
            unsignedPublicKeys = mTempPkiStorage.getUnsignedPublicKeys();
        } catch (SharkKBException e) {
            // shouldn't throw any errors
            e.printStackTrace();
        }
        // should consist of one key!
        if(!unsignedPublicKeys.isEmpty()){
            if(unsignedPublicKeys.size() > 1){
                L.d("Something is really weird. We should only have one key", this);
            } else {
                this.mPublicKey = unsignedPublicKeys.get(0);
                this.mPortListener.onPublicKeyReceived(this.mPublicKey.getOwner());
            }
        }
    }

    @Override
    protected void handleExpose(ASIPInMessage message, ASIPConnection asipConnection, ASIPInterest interest) throws SharkKBException {

    }
    @Override
    public void onPublicKeyDecision(boolean accepted) {
        if(accepted){
            try {
                mPkiStorage.sign(mPublicKey);
                mCertificates = mTempPkiStorage.getAllSharkCertificates();
                ContactDao contactDao = new ContactDao((SharkKB) mKnowledge);
                mContacts = contactDao.getAll();
                mPortListener.onCertificatesReceived(mCertificates, mContacts);
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        } else {
            // TODO we reached the end of the exchange
        }
    }

    @Override
    public void onCertificatesDecision(boolean accepted) {
        if(accepted){
            L.d("mCertificates: " + mCertificates.size(), this);
            for (SharkCertificate certificate : mCertificates) {
                L.d("Add certificate from " + certificate.getOwner().getName()
                        + " and signed by " + certificate.getSigner().getName(), this);
                try {
                    mPkiStorage.addSharkCertificate(certificate);
                    for (Contact contact : mContacts) {
                        SharkNetApiImpl.getInstance().addContact(contact);
                    }
                } catch (SharkKBException e) {
                    e.printStackTrace();
                }
            }
        } else {
            // TODO we reached the end of the exchange
        }
    }

    public void removePort(){
        try {
            this.se.removePersistedPort(this);
        } catch (SharkException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived() {
        this.mPortListener.onMessageReceived();
    }

    @Override
    public void onExchangeFailure(String reason) {
        this.mPortListener.onExchangeFailed(reason);
    }
}
