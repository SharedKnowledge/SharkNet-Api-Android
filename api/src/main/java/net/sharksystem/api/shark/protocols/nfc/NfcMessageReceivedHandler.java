package net.sharksystem.api.shark.protocols.nfc;

import android.nfc.Tag;

import net.sharkfw.asip.serialization.ASIPSerializerException;
import net.sharkfw.protocols.RequestHandler;
import net.sharkfw.system.L;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by mn-io on 25.01.2016.
 */
public class NfcMessageReceivedHandler implements OnMessageReceived {
    private final NfcMessageStub.NFCMessageListener nfcMessageListener;
    private RequestHandler handler;
    private NfcMessageStub nfcMessageStub;
    private byte[] byteBuffer;

    public NfcMessageReceivedHandler(NfcMessageStub nfcMessageStub, NfcMessageStub.NFCMessageListener listener) {
        this.nfcMessageStub = nfcMessageStub;
        this.nfcMessageListener = listener;
    }

    @Override
    public void onMessage(byte[] message) {
        if (byteBuffer == null) {
            byteBuffer = message;
        } else {
            byteBuffer = concat(byteBuffer, message);
        }
//        L.d("onMessage: " + byteBuffer, this);
        this.nfcMessageListener.onMessageReceived();
    }

    public static byte[] concat(byte[] first, byte[] second) {
        final int newLength = first.length + second.length;
        byte[] result = Arrays.copyOf(first, newLength);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    @Override
    public void onError(Exception exception) {
        L.d("onError", this);
    }

    @Override
    public void newTag(Tag tag) {

    }

    @Override
    public void tagLost() {
        if (byteBuffer != null) {
            // Pass to the handler for the ports
            try {
                handler.handleMessage(byteBuffer, nfcMessageStub);
            } catch (ASIPSerializerException e) {
                e.printStackTrace();
                this.nfcMessageListener.onExchangeFailure("Message can't be serialized to ASIP.");
            }
            byteBuffer = null;
        }
    }

    public void setHandler(RequestHandler handler) {
        this.handler = handler;
    }
}
