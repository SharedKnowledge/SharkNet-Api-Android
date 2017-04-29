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
import net.sharkfw.system.L;
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
        getProtocolStub(Protocols.NFC).offer(knowledge);
        L.d("NFC Knowledge offered.", this);
    }

    public void setupNfc(Activity activity, NfcMessageStub.NFCMessageListener listener) {
        this.activity = activity;
        this.nfcMessageListener = listener;
    }

    @Override
    public void startNfc() throws SharkProtocolNotSupportedException, IOException {
        getProtocolStub(Protocols.NFC).start();
        L.d("NFC started", this);
    }

    @Override
    public void startBluetooth() throws SharkProtocolNotSupportedException, IOException {
        getProtocolStub(Protocols.BLUETOOTH).start();
    }

    @Override
    public void stopNfc() throws SharkProtocolNotSupportedException {
        getProtocolStub(Protocols.NFC).stop();
        L.d("NFC prepared", this);
    }

    @Override
    public void stopBluetooth() throws SharkProtocolNotSupportedException {
        getProtocolStub(Protocols.BLUETOOTH).stop();
    }

    @Override
    protected Stub createNFCMessageStub(SharkStub stub) throws SharkProtocolNotSupportedException {
        NfcMessageStub nfcMessageStub = new NfcMessageStub(this, activity.getApplicationContext(), activity, nfcMessageListener);
        nfcMessageStub.setHandler((RequestHandler) stub);
        return nfcMessageStub;
    }

    @Override
    protected Stub createBluetoothStreamStub(ASIPStub stub) throws SharkProtocolNotSupportedException {
        return new BluetoothStreamStub(this, stub);
    }
}
