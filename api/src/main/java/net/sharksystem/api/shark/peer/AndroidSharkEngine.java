package net.sharksystem.api.shark.peer;

import android.app.Activity;
import android.content.Context;

import net.sharkfw.asip.ASIPKnowledge;
import net.sharkfw.asip.ASIPStub;
import net.sharkfw.asip.SharkStub;
import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.peer.J2SEAndroidSharkEngine;
import net.sharkfw.protocols.Protocols;
import net.sharkfw.protocols.RequestHandler;
import net.sharkfw.protocols.Stub;
import net.sharkfw.system.SharkNotSupportedException;
import net.sharksystem.api.shark.ports.RadarDiscoveryPort;
import net.sharksystem.api.shark.protocols.bluetooth.BluetoothStreamStub;
import net.sharksystem.api.shark.protocols.nfc.NfcMessageStub;
import net.sharksystem.api.shark.protocols.wifidirect.WifiDirectAdvertisingManager;

import java.io.IOException;

public class AndroidSharkEngine extends J2SEAndroidSharkEngine {

    private final NearbyPeerManager mPeerManager;
    private Context mContext;
    private WifiDirectAdvertisingManager mAdvertisingManager;
    private Activity activity;
    private NfcMessageStub.NFCMessageListener nfcMessageListener;

    public AndroidSharkEngine(Context context) {
        super();
        mContext = context;
        mPeerManager = new NearbyPeerManager();
        mPeerManager.setEngine(this);
    }

    public Context getContext() {
        return mContext;
    }

    public void addNearbyPeerListener(NearbyPeerManager.NearbyPeerListener listener) {
        mPeerManager.addNearbyPeerListener(listener);
    }

    public void startDiscovery() {
        new RadarDiscoveryPort(this, mPeerManager);
        mAdvertisingManager = new WifiDirectAdvertisingManager(mContext, this);
        mAdvertisingManager.startAdvertising();
    }

    public void stopDiscovery() {
        mAdvertisingManager.stopAdvertising();
    }

    public void offer(ASIPKnowledge knowledge) throws SharkProtocolNotSupportedException, SharkNotSupportedException {
        this.createNFCMessageStub(this.getAsipStub()).offer(knowledge);
    }

    public void setupNfc(Activity activity, NfcMessageStub.NFCMessageListener listener) {
        this.activity = activity;
        this.nfcMessageListener = listener;
    }

    @Override
    public void startNfc() throws SharkProtocolNotSupportedException, IOException {
        this.createNFCMessageStub(this.getAsipStub()).start();
    }

    @Override
    public void startBluetooth() throws SharkProtocolNotSupportedException, IOException {
        this.createBluetoothStreamStub(this.getAsipStub()).start();
    }

    @Override
    public void stopNfc() throws SharkProtocolNotSupportedException {
        this.createNFCMessageStub(this.getAsipStub()).stop();
    }

    @Override
    public void stopBluetooth() throws SharkProtocolNotSupportedException {
        this.createBluetoothStreamStub(this.getAsipStub()).stop();
    }

    @Override
    protected Stub createNFCMessageStub(SharkStub stub) throws SharkProtocolNotSupportedException {
        NfcMessageStub nfcMessageStub = new NfcMessageStub(this, mContext, activity, nfcMessageListener);
        nfcMessageStub.setHandler((RequestHandler) stub);
        setProtocolStub(nfcMessageStub, Protocols.NFC);
        return nfcMessageStub;
    }

    @Override
    protected Stub createBluetoothStreamStub(ASIPStub stub) throws SharkProtocolNotSupportedException {
        BluetoothStreamStub bluetoothStreamStub = new BluetoothStreamStub(this, stub);
        setProtocolStub(bluetoothStreamStub, Protocols.BLUETOOTH);
        return bluetoothStreamStub;
    }
}
