package net.sharksystem.api.shark.protocols.bluetooth.callbacks;

import android.bluetooth.BluetoothSocket;

/**
 * Created by j4rvis on 11/21/16.
 */

public interface ConnectionCallback {
    void onNewConnection(BluetoothSocket socket);
    void onCreatingSocketFailed(String reason);
    void onListeningFailed(String reason);
}
