package net.sharksystem.api.shark.protocols.nfc;

/**
 * Created by Mario Neises (mn-io) on 22.01.16.
 */
public interface OnMessageSend {

    byte[] getNextMessage();

    void onDeactivated(int reason);

    void setMaxSize(int size);
}
