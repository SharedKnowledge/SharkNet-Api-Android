package net.sharksystem.api.impl;

import net.sharkfw.asip.ASIPInformation;
import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.engine.ASIPConnection;
import net.sharkfw.asip.engine.ASIPInMessage;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.ports.KnowledgePort;
import net.sharkfw.system.L;
import net.sharksystem.api.interfaces.SharkNet;
import net.sharksystem.api.shark.protocols.nfc.NfcMessageStub;

import java.util.Iterator;

/**
 * Created by j4rvis on 2/4/17.
 */

public class NFCContactPort extends KnowledgePort {

    private final SharkNet.NFCContentListener contentListener;

    public NFCContactPort(SharkEngine se, SharkNet.NFCContentListener contentListener) {
        super(se);
        this.contentListener = contentListener;
    }

    @Override
    protected void handleInsert(ASIPInMessage message, ASIPConnection asipConnection, ASIPKnowledge asipKnowledge) {
        // check if ASIPInMessage is of type 'NFCMessage'
        if(!SharkCSAlgebra.identical(message.getType(), NfcMessageStub.NFC_MESSAGE_TYPE)){
            return;
        }

        try {
            ASIPInterest inMemoASIPInterest = InMemoSharkKB.createInMemoASIPInterest();
            Iterator<ASIPInformation> information = asipKnowledge.getInformation(inMemoASIPInterest);
            while (information.hasNext()) {
                L.d("Received message: " + information.next().getContentAsString(), this);
            }

        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        // okay so we have received an ASIPMessage using NFC.
        // Now split the knowledge to the sending Contact and the anchored contacts
        // than call the NFCContentListener
        try {
            this.contentListener.onNewContact(null, null);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleExpose(ASIPInMessage message, ASIPConnection asipConnection, ASIPInterest interest) throws SharkKBException {
        // not used
    }
}
