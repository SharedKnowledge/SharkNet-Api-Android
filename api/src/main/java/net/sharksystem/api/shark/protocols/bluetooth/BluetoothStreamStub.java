package net.sharksystem.api.shark.protocols.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.Knowledge;
import net.sharkfw.protocols.Protocols;
import net.sharkfw.protocols.RequestHandler;
import net.sharkfw.protocols.StreamConnection;
import net.sharkfw.protocols.StreamStub;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkNotSupportedException;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by j4rvis on 11/17/16.
 */

public class BluetoothStreamStub implements StreamStub {

    public static UUID BT_UUID = UUID.fromString("ED01E22D-19DD-4B79-81BA-444E9B6D89BF");
    public static String BT_NAME = "SHARK_BT";
    private final BluetoothAdapter mBluetoothAdapter;
    private final AndroidSharkEngine mEngine;
    private String mLocalAddress = null;

    private RequestHandler mRequestHandler;
    private BluetoothServer mBluetoothServer;
    private BluetoothConnection mBluetoothConnection;
    private List<Thread> mWaitThreads = new ArrayList<>();
    private long mLastCall;


    public BluetoothStreamStub(AndroidSharkEngine engine, RequestHandler requestHandler) {
        mEngine = engine;
        mLocalAddress = android.provider.Settings.Secure.getString(engine.getContext().getContentResolver(), "bluetooth_address");
        mRequestHandler = requestHandler;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public StreamConnection createStreamConnection(String addressString) throws IOException {

        String address = null;
        try {
            address = Protocols.removeProtocolPrefix(addressString);
        } catch (SharkProtocolNotSupportedException e) {
            L.e("Protocol not supported. Reason: " + e.getMessage(), this);
        }
        BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(address);

        L.d("trying to create an outgoing connection!", this);

        if(mBluetoothConnection==null){
            mBluetoothConnection = new BluetoothConnection(this, remoteDevice, this.getLocalAddress());
        } else {
            Thread waitThreadTemp = Thread.currentThread();
            mWaitThreads.add(waitThreadTemp);
            try {
                waitThreadTemp.wait(60*60*1000);
                this.killConnection();
                return this.createStreamConnection(addressString);
            } catch (InterruptedException e) {
                return this.createStreamConnection(addressString);
            }
        }

        return mBluetoothConnection;
    }

    private synchronized void killConnection() {
        long now = System.currentTimeMillis();

        if(mLastCall == 0 || now - mLastCall >= 60*60*1000){
            // Do not notify the streamStub.streamClosed method
            mBluetoothConnection.close(false);
            mBluetoothConnection = null;
            mLastCall = now;
        }
    }

    @Override
    public String getLocalAddress() {
        return mLocalAddress;
    }

    @Override
    public void setHandler(RequestHandler handler) {
        mRequestHandler = handler;
    }

    @Override
    public void stop() {
        if(this.started()){
            mBluetoothServer.hold();
            mBluetoothServer = null;
        }
    }

    @Override
    public void start() throws IOException {
        if(!this.started()){
            try {
                mBluetoothServer = new BluetoothServer(mBluetoothAdapter, mRequestHandler, this.getLocalAddress());
                new Thread(mBluetoothServer).start();
            } catch (IOException e){
                L.e("Can not create BluetoothServer. Reason: " + e.getMessage(), this);
                throw e;
            }
        }
    }

    @Override
    public boolean started() {
        return mBluetoothServer != null;
    }

    @Override
    public void offer(ASIPSpace interest) throws SharkNotSupportedException {

    }

    @Override
    public void offer(ASIPKnowledge knowledge) throws SharkNotSupportedException {

    }

    public void streamClosed(BluetoothConnection bluetoothConnection) {
        // Check if bluetoothConnection is identical
        mBluetoothConnection = null;
        Thread thread = mWaitThreads.remove(0);
        if(thread!=null){
            thread.interrupt();
        }
        try {
            Thread.sleep(10);
            for (Thread waitThread : mWaitThreads) {
                waitThread.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
