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
import net.sharksystem.api.shark.protocols.nfc.NfcMessageStub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 2/4/17.
 */

public class NfcPkiPort extends KnowledgePort implements NfcPkiPortEventListener, NfcMessageStub.NFCMessageListener{

    private final NfcPkiPortListener portListener;
    private final PkiStorage myPkiStorage;
    private SharkPkiStorage tempPkiStorage;
    private SharkPublicKey publicKey;
    private List<SharkCertificate> certificates;

    public NfcPkiPort(SharkEngine se, NfcPkiPortListener portListener) {
        super(se);
        this.portListener = portListener;
        this.myPkiStorage = this.se.getPKIStorage();
    }

    @Override
    protected void handleInsert(ASIPInMessage message, ASIPConnection asipConnection, ASIPKnowledge asipKnowledge) {
//         check if ASIPInMessage is of type 'NFCMessage'
        if(!SharkCSAlgebra.identical(message.getType(), NfcMessageStub.NFC_MESSAGE_TYPE)){
            return;
        }

        // Now retrieve the key and the certificates out of the knowledge
        // best way should be to create a new PkiStorage and use the knowledge as inMemokb.

        tempPkiStorage = new SharkPkiStorage((SharkKB) asipKnowledge);
        List<SharkPublicKey> unsignedPublicKeys = new ArrayList<>();
        try {
            unsignedPublicKeys = tempPkiStorage.getUnsignedPublicKeys();
        } catch (SharkKBException e) {
            // shouldn't throw any errors
            e.printStackTrace();
        }
        // should consist of one key!
        if(!unsignedPublicKeys.isEmpty()){
            if(unsignedPublicKeys.size() > 1){
                L.d("Something is really weird. We should only have one key", this);
            } else {
                this.publicKey = unsignedPublicKeys.get(0);
                this.portListener.onPublicKeyReceived(this.publicKey.getOwner());
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
                this.myPkiStorage.sign(this.publicKey);
                this.certificates = this.tempPkiStorage.getAllSharkCertificates();
                this.portListener.onCertificatesReceived(this.certificates);
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
            L.d("certificates: " + certificates.size(), this);
            for (SharkCertificate certificate : this.certificates) {
                L.d("Add certificate from " + certificate.getOwner().getName()
                        + " and signed by " + certificate.getSigner().getName(), this);
                try {
                    this.myPkiStorage.addSharkCertificate(certificate);
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
        this.portListener.onMessageReceived();
    }

    @Override
    public void onExchangeFailure(String reason) {
        this.portListener.onExchangeFailed(reason);
    }
}
