package net.sharksystem.api.shark.protocols.nfc;

import android.nfc.Tag;

/**
 * Created by Mario Neises (mn-io) on 22.01.16.
 */
public interface OnMessageReceived {

    void handleMessageReceived(byte[] msg);

    void handleError(Exception exception);

    void handleTagLost();

    void handleNewTag(Tag tag);
}
