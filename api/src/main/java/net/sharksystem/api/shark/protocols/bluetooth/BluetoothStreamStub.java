package net.sharksystem.api.shark.protocols.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.kep.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.Knowledge;
import net.sharkfw.protocols.Protocols;
import net.sharkfw.protocols.RequestHandler;
import net.sharkfw.protocols.StreamConnection;
import net.sharkfw.protocols.StreamStub;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkNotSupportedException;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;

import java.io.IOException;
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

        return new BluetoothConnection(remoteDevice, this.getLocalAddress());
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
    public void offer(Knowledge knowledge) throws SharkNotSupportedException {

    }
}
