package net.sharksystem.api.shark.protocols.nfc;

import android.nfc.Tag;

/**
 * Created by mn-io on 22.01.16.
 */
public interface OnMessageReceived {

    void onMessage(byte[] message);

    void onError(Exception exception);

    void newTag(Tag tag);

    void tagLost();
}
