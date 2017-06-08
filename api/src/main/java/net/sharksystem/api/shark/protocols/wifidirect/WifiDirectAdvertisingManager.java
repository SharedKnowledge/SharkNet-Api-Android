package net.sharksystem.api.shark.protocols.wifidirect;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Handler;
import android.os.Looper;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.protocols.Protocols;
import net.sharkfw.system.L;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by j4rvis on 22.07.16.
 */
public class WifiDirectAdvertisingManager implements WifiP2pManager.DnsSdTxtRecordListener, Runnable {

    public static final String TYPE_SI = "si:radar";
    private static final String TYPE_NAME = "RADAR";
    public static final SemanticTag TYPE_TAG = InMemoSharkKB.createInMemoSemanticTag(TYPE_NAME, TYPE_SI);
    private final AndroidSharkEngine mEngine;
    private final String mBluetoothAddress;
    private final WifiP2pManager mManager;
    private final WifiP2pManager.Channel mChannel;
    private boolean mIsDiscovering = false;
    private WifiP2pDnsSdServiceInfo mServiceInfo;
    private int mDiscoveryInterval = 10000;
    private Handler mHandler = new Handler();

    public WifiDirectAdvertisingManager(Context context, AndroidSharkEngine engine) {
        mEngine = engine;

        mBluetoothAddress = android.provider.Settings.Secure.getString(context.getContentResolver(), "bluetooth_address");

        mManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
//        Looper.prepare();
        mChannel = mManager.initialize(context, context.getMainLooper(), null);
        mManager.setDnsSdResponseListeners(mChannel, null, this);
    }

    public void setDiscoveryInterval(int interval) {
        this.mDiscoveryInterval = interval;
    }

    public boolean isDiscovering() {
        return mIsDiscovering;
    }

    public void startAdvertising() {

        PeerSemanticTag sender = mEngine.getOwner();
        ASIPInterest interest = null;

        String[] addresses = sender.getAddresses();
        List<String> strings = Arrays.asList(addresses);

        if(!strings.contains(Protocols.BLUETOOTH_PREFIX + mBluetoothAddress)) {
            sender.addAddress(Protocols.BLUETOOTH_PREFIX + mBluetoothAddress);
        }

        try {
            STSet typeSTSet = InMemoSharkKB.createInMemoSTSet();
            typeSTSet.merge(TYPE_TAG);
            interest = InMemoSharkKB.createInMemoASIPInterest(null, typeSTSet, sender, null, null, null, null, ASIPSpace.DIRECTION_INOUT);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        if (!mIsDiscovering) {
            mManager.clearLocalServices(mChannel, new WifiActionListener("Clear LocalServices"));
            try {
                L.d(L.asipSpace2String(interest), this);
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
            HashMap<String, String> map = WifiDirectUtil.interest2RecordMap(interest);
            mServiceInfo = WifiP2pDnsSdServiceInfo.newInstance("_sbc", "_presence._tcp", map);
            mManager.addLocalService(mChannel, mServiceInfo, new WifiActionListener("Add LocalService"));

            mManager.clearServiceRequests(mChannel, new WifiActionListener("Clear ServiceRequests"));
            WifiP2pDnsSdServiceRequest wifiP2pDnsSdServiceRequest = WifiP2pDnsSdServiceRequest.newInstance();
            mManager.addServiceRequest(mChannel, wifiP2pDnsSdServiceRequest, new WifiActionListener("Add ServiceRequest"));

            mHandler.post(this);

            mIsDiscovering = true;
        }
    }

    public void stopAdvertising() {
        if (mIsDiscovering) {
            mHandler.removeCallbacks(this);
            mManager.clearServiceRequests(mChannel, new WifiActionListener("Clear ServiceRequests"));
            mManager.removeLocalService(mChannel, mServiceInfo, new WifiActionListener("Remove LocalService"));
            mManager.clearLocalServices(mChannel, new WifiActionListener("Clear LocalServices"));

            mIsDiscovering = false;
        }
    }

    // called to trigger the discovery on a certain basis
    @Override
    public void run() {
        mManager.discoverServices(mChannel, new WifiActionListener("Discover Services"));
        mHandler.postDelayed(this, mDiscoveryInterval);
    }

    @Override
    public void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice) {


        if (srcDevice == null || txtRecordMap.isEmpty()) return;
        try {
            ASIPInterest interest = WifiDirectUtil.recordMap2Interest(txtRecordMap);
            mEngine.handleASIPInterest(interest);
        } catch (SharkKBException e) {
            e.printStackTrace();
        }
    }

    private class WifiActionListener implements WifiP2pManager.ActionListener {

        private String title = "";

        public WifiActionListener(String title) {
            this.title = title;
        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailure(int reason) {
            L.e(this.title + " failed. ReasonCode: " + reason, this);
        }
    }
}
