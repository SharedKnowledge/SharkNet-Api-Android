package net.sharksystem.api.shark.protocols.nfc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.os.Build;

import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.ASIPOutMessage;
import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.Knowledge;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.protocols.MessageStub;
import net.sharkfw.protocols.RequestHandler;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkNotSupportedException;

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by mn-io on 22.01.16.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class NfcMessageStub implements MessageStub {

    public static String NFC_MESSAGE_TYPE_NAME = "NFC_MESSAGE";
    public static String NFC_MESSAGE_TYPE_SI = "NFC_MESSAGE_SUBJECT_IDENTIFIER";
    public static SemanticTag NFC_MESSAGE_TYPE = InMemoSharkKB.createInMemoSemanticTag(NFC_MESSAGE_TYPE_NAME, NFC_MESSAGE_TYPE_SI);
    private final SharkEngine sharkEngine;

    public interface NFCMessageListener{
        void onMessageReceived(String message);
        void onExchangeComplete(String message);
        void onExchangeFailure(String failure);
    }

    public static final String SMART_CARD_IDENTIFIER = "SHARK NFC";

    private final NfcAdapter nfcAdapter;
    private final Activity activity;
    private final NfcMessageReceivedHandler receivedRequestHandler;
    private final NfcMessageSendHandler sendRequestHandler;
    private boolean isStarted = false;

    public NfcMessageStub(SharkEngine engine, Context context, Activity activity, NFCMessageListener listener) throws SharkProtocolNotSupportedException {
        this.activity = activity;
        this.sharkEngine = engine;
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (this.nfcAdapter == null) {
            throw new SharkProtocolNotSupportedException("NFC is not supported");
        }

        receivedRequestHandler = new NfcMessageReceivedHandler(this, listener);
        sendRequestHandler = new NfcMessageSendHandler();
    }

    @Override
    public void setHandler(RequestHandler handler) {
        receivedRequestHandler.setHandler(handler);
    }

    @Override
    public void stop() {
        NfcAdapterHelper.actAsSmartCard(SMART_CARD_IDENTIFIER, activity, sendRequestHandler, receivedRequestHandler);
        isStarted = false;
    }

    @Override
    public void start() {
        NfcAdapterHelper.actAsNfcReaderWriter(SMART_CARD_IDENTIFIER, activity, sendRequestHandler, receivedRequestHandler);
        isStarted = true;
    }

    @Override
    public boolean started() {
        return isStarted;
    }

    @Override
    public void setReplyAddressString(String addr) {

    }

    @Override
    public void sendMessage(byte[] msg, String recAddress) throws IOException {
        L.d("sendMessage called", this);
        sendRequestHandler.setData(msg);
        sendRequestHandler.setMaxSize(1024);
    }

    @Override
    public void offer(ASIPSpace interest) throws SharkNotSupportedException {
    }

    /**
     * Will be used to set the content of the NFC exchange
     * @param knowledge
     */
    @Override
    public void offer(Knowledge knowledge) {
        L.d("Offer called", this);
        ASIPOutMessage outMessage = this.sharkEngine.createASIPOutMessage(new String[]{"nfc://"},
                this.sharkEngine.getOwner(),
                null,
                null,
                null,
                null,
                NfcMessageStub.NFC_MESSAGE_TYPE,
                1);

        outMessage.insert(knowledge);
        // Should send the whole message as byte[] to this.sendMessage()
    }

    @Override
    public String getReplyAddressString() {
        return null;
    }
}
