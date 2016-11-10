package net.sharksystem.api.shark.protocols.nfc.androidService;

import android.annotation.TargetApi;
import android.nfc.cardemulation.HostApduService;
import android.os.Build;
import android.os.Bundle;

import net.sharksystem.api.shark.protocols.nfc.OnMessageReceived;
import net.sharksystem.api.shark.protocols.nfc.OnMessageSend;
import net.sharksystem.api.shark.protocols.nfc.readerWriterMode.IsoDepTransceiver;

import java.util.Arrays;

/**
 * Created by Mario Neises (mn-io) on 22.01.16.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class SmartCardEmulationService extends HostApduService {

    public static final byte[] KEEP_CHANNEL_OPEN_SIGNAL_PASSIVE = {(byte) 0xFE, (byte) 0xFF, (byte) 0xFE, (byte) 0xFF, (byte) 0xFE, (byte) 0xFF, (byte) 0xFE, (byte) 0xFF, (byte) 0xFE, (byte) 0xFF, (byte) 0xFE, (byte) 0xFF, (byte) 0xFC,};
    public static final byte[] EMPTY_MSG = new byte[0];

    private static OnMessageSend src;
    private static OnMessageReceived sink;
    private static byte[] handshakeIdentifier;
    private static boolean isValidReader;

    @Override
    public void onDeactivated(int reason) {
        if (src != null) {
            src.onDeactivated(reason);
        }

        if (sink != null) {
            sink.handleTagLost();
        }

        isValidReader = false;
    }

    @Override
    public byte[] processCommandApdu(byte[] data, Bundle extras) {
        if (src == null) {
            return null;
        }

        if (Arrays.equals(IsoDepTransceiver.AID_APDU, data)) {
            isValidReader = true;
            return handshakeIdentifier;
        }

        if (!isValidReader) {
            return null;
        }

        if (sink != null) {
            if (!Arrays.equals(IsoDepTransceiver.KEEP_CHANNEL_OPEN_SIGNAL_ACTIVE, data)) {
                sink.handleMessageReceived(data);
            } else {
                sink.handleMessageReceived(EMPTY_MSG);
            }
        }

        byte[] nextMessage = src.getNextMessage();
        if (nextMessage == null) {
            nextMessage = KEEP_CHANNEL_OPEN_SIGNAL_PASSIVE;
        }

        return nextMessage;
    }

    public static void setInitialHandshakeResponse(String identifier) {
        handshakeIdentifier = identifier.getBytes();
        isValidReader = false;
    }

    public static void setSource(OnMessageSend src) {
        SmartCardEmulationService.src = src;
    }

    public static void setSink(OnMessageReceived sink) {
        SmartCardEmulationService.sink = sink;
    }
}