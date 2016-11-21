package net.sharksystem.api.shark.protocols.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import net.sharkfw.protocols.RequestHandler;
import net.sharkfw.protocols.tcp.SharkServer;
import net.sharkfw.system.L;

import java.io.IOException;

/**
 * Created by j4rvis on 11/21/16.
 */

public class BluetoothServer implements SharkServer {

    private final BluetoothAdapter mBluetoothAdapter;
    private final String mLocalAddress;
    private boolean mActive;
    private BluetoothServerSocket mSocket = null;
    private RequestHandler mRequestHandler;

    public BluetoothServer(BluetoothAdapter bluetoothAdapter, RequestHandler requestHandler, String localAddress) throws IOException {
        mBluetoothAdapter = bluetoothAdapter;
        mRequestHandler = requestHandler;
        mLocalAddress = localAddress;

        mSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(BluetoothStreamStub.BT_NAME, BluetoothStreamStub.BT_UUID);

        mActive = true;
    }

    @Override
    public void hold() {
        mActive = false;
        try {
            mSocket.close();
        } catch (IOException e) {
            L.e("BluetoothServer hold failed. Reason: " + e.getMessage(), this);
        }
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public void setHandler(RequestHandler handler) {
        mRequestHandler = handler;
    }

    @Override
    public void run() {
        try {
            while (mActive){
                BluetoothSocket bluetoothSocket = mSocket.accept();
                BluetoothConnection con = new BluetoothConnection(bluetoothSocket, mLocalAddress);
                mRequestHandler.handleStream(con);
            }
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
