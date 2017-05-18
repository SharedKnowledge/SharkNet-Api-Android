package net.sharksystem.api.shark.protocols.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import net.sharkfw.protocols.ConnectionListenerManager;
import net.sharkfw.protocols.StreamConnection;
import net.sharkfw.system.L;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by j4rvis on 11/21/16.
 */

public class BluetoothConnection extends ConnectionListenerManager implements StreamConnection{

    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private String mLocalAddress;
    private String mRemoteAddress;
    private BluetoothSocket mSocket;
    private BluetoothStreamStub mStreamStub;

    public BluetoothConnection(BluetoothStreamStub stub, BluetoothDevice device, String localAddress) throws IOException {
        try {
            mStreamStub = stub;
            mSocket = device.createInsecureRfcommSocketToServiceRecord(BluetoothStreamStub.BT_UUID);
            mSocket.connect();
            mInputStream = mSocket.getInputStream();
            mOutputStream = mSocket.getOutputStream();
            mRemoteAddress = mSocket.getRemoteDevice().getAddress();
            mLocalAddress = localAddress;

        } catch (IOException e) {
            L.e("Can not create a Connection to the device. Reason: " + e.getMessage(), this);
            throw new IOException(e.getMessage(), e.getCause());
        }
    }

    public BluetoothConnection(BluetoothSocket bluetoothSocket, String localAddress) throws IOException {
        mSocket = bluetoothSocket;
        mInputStream = mSocket.getInputStream();
        mOutputStream = mSocket.getOutputStream();

        mLocalAddress = localAddress;
        mRemoteAddress = mSocket.getRemoteDevice().getAddress();
    }

    @Override
    public InputStream getInputStream() {
        return mInputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return mOutputStream;
    }

    @Override
    public void sendMessage(byte[] msg) throws IOException {

    }

    @Override
    public String getReplyAddressString() {
        return mLocalAddress;
    }

    @Override
    public String getReceiverAddressString() {
        return mRemoteAddress;
    }

    @Override
    public String getLocalAddressString() {
        return mLocalAddress;
    }

    @Override
    public void setLocalAddressString(String localAddress) {
        mLocalAddress = localAddress;
    }

    public void close(boolean notify) {
        L.d("Closing Bluetooth-Connection from: " + this.getReplyAddressString() + " to: " + this.getReceiverAddressString(), this);
        try {
            mSocket.close();
            if(notify) notifyClose();
        } catch (IOException e) {
            L.e("Socket can not be closed. Reason: " + e.getMessage(), this);
        }
    }

    @Override
    public void close() {
        this.close(true);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.notifyClose();
    }

    public void notifyClose(){
        if(this.mStreamStub != null) this.mStreamStub.streamClosed(this);
    }
}
