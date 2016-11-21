package net.sharksystem.api.shark.protocols.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import net.sharkfw.protocols.ConnectionListenerManager;
import net.sharkfw.protocols.SharkInputStream;
import net.sharkfw.protocols.SharkOutputStream;
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

    public BluetoothConnection(BluetoothDevice device, String localAddress) throws IOException {
        try {
            mSocket = device.createInsecureRfcommSocketToServiceRecord(BluetoothStreamStub.BT_UUID);
            mSocket.connect();
            mInputStream = mSocket.getInputStream();
            mOutputStream = mSocket.getOutputStream();
            mRemoteAddress = mSocket.getRemoteDevice().getAddress();
            mLocalAddress = localAddress;
        } catch (IOException e) {
            L.e("Can not create a Connection to the device. Reason: " + e.getMessage(), this);
            throw e;
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
    public SharkInputStream getSharkInputStream() {
        return null;
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
    public SharkOutputStream getSharkOutputStream() {
        return null;
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

    @Override
    public void close() {
        try {
            mSocket.close();
        } catch (IOException e) {
            L.e("Socket can not be closed. Reason: " + e.getMessage(), this);
        }
    }
}
