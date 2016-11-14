package net.sharksystem.api.shark.protocols.wifidirect;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Handler;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by j4rvis on 22.07.16.
 */
public class WifiDirectManager
        extends BroadcastReceiver
        implements WifiP2pManager.DnsSdTxtRecordListener,
        WifiP2pManager.ConnectionInfoListener, Runnable {

    private final AndroidSharkEngine mEngine;

    public enum WIFI_STATE {
        INIT,
        DISCOVERING,
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED,
        NONE
    }

    private boolean mIsReceiverRegistered = false;
    private boolean mIsDiscovering = false;

    private WifiP2pDnsSdServiceInfo mServiceInfo;

    private WIFI_STATE mState = WIFI_STATE.NONE;

    private final WifiP2pManager mManager;
    private final WifiP2pManager.Channel mChannel;

    private Context mContext = null;

    private int mDiscoveryInterval = 10000;

    // Interfaces
    //
    //

    public interface WifiDirectPeerListener {
        void onNewNearbyPeer(ASIPSpace space);
    }
    public interface WifiDirectNetworkListener {
        void onNetworkCreated(WifiP2pInfo info, WifiP2pGroup group);
        void onNetworkDestroyed();
    }

    public WifiDirectManager(Context context, AndroidSharkEngine engine) {
        mEngine = engine;
        addPeerListener(mEngine);
        mContext = context;

        mManager = (WifiP2pManager) mContext.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(mContext, mContext.getMainLooper(), null);
        mManager.setDnsSdResponseListeners(mChannel, null, this);
    }

    // Setter
    //
    //

    private List<WifiDirectPeerListener> mPeerListeners = new ArrayList<>();
    private List<WifiDirectNetworkListener> mNetworkListeners = new ArrayList<>();

    public void addPeerListener(WifiDirectPeerListener listener){
        if(!mPeerListeners.contains(listener)){
            mPeerListeners.add(listener);
        }
    }

    public void addNetworkListener(WifiDirectNetworkListener listener){
        if(!mNetworkListeners.contains(listener)){
            mNetworkListeners.add(listener);
        }
    }

    public void setDiscoveryInterval(int interval){
        this.mDiscoveryInterval = interval;
    }

    // Getter
    //
    //

    public WIFI_STATE getState(){ return mState; }

    // Private methods
    //
    //



    // Public methods
    //
    //

    public void startAdvertising(ASIPSpace space){

        if(!mIsDiscovering){

            if(!mIsReceiverRegistered){
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
                intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
                intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
                intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
                intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
                mContext.registerReceiver(this, intentFilter);
                mIsReceiverRegistered = true;
            }

            mManager.clearLocalServices(mChannel, new WifiActionListener("Clear LocalServices"));
//            HashMap<String, String> map = WifiDirectUtil.interest2RecordMap(space);
            HashMap<String, String> map = WifiDirectUtil.interest2RecordMap((ASIPInterest) space);
            mServiceInfo =
                    WifiP2pDnsSdServiceInfo.newInstance("_sbc", "_presence._tcp", map);
            mManager.addLocalService(mChannel, mServiceInfo,
                    new WifiActionListener("Add LocalService"));

            mManager.clearServiceRequests(mChannel,
                    new WifiActionListener("Clear ServiceRequests"));
            WifiP2pDnsSdServiceRequest wifiP2pDnsSdServiceRequest = WifiP2pDnsSdServiceRequest.newInstance();
            mManager.addServiceRequest(mChannel, wifiP2pDnsSdServiceRequest,
                    new WifiActionListener("Add ServiceRequest"));

            mHandler.post(this);

            mState = WIFI_STATE.DISCOVERING;
            mIsDiscovering = true;
        }

    }

    public void stopAdvertising(){
        if(mIsDiscovering){
            mHandler.removeCallbacks(this);
            mManager.clearServiceRequests(mChannel,
                    new WifiActionListener("Clear ServiceRequests"));
            mManager.removeLocalService(mChannel, mServiceInfo,
                    new WifiActionListener("Remove LocalService"));
            mManager.clearLocalServices(mChannel,
                    new WifiActionListener("Clear LocalServices"));

            if(mIsReceiverRegistered){
                mContext.unregisterReceiver(this);
                mIsReceiverRegistered = false;
            }

            mState = WIFI_STATE.INIT;
            mIsDiscovering = false;
        }

        if(mIsReceiverRegistered){
            mContext.unregisterReceiver(this);
            mIsReceiverRegistered = false;
        }

    }

    public void connect(PeerSemanticTag peer) throws IllegalArgumentException{
        final WifiP2pConfig config = new WifiP2pConfig();
        for(String addr : peer.getAddresses()){
            if(addr.startsWith("WIFI://")){
                config.deviceAddress = addr;
            }
        }
        if(config.deviceAddress == null || config.deviceAddress.isEmpty()){
            throw new IllegalArgumentException("Peer does not have a WIFI address");
        }

        config.wps.setup = WpsInfo.PBC;
        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int reason) {
            }
        });
        mState = WIFI_STATE.CONNECTING;
    }


    // Implemented methods
    //
    //

    private Handler mHandler = new Handler();

    // called to trigger the discovery on a certain basis
    @Override
    public void run() {
        mManager.discoverServices(mChannel, new WifiActionListener("Discover Services"));
        mHandler.postDelayed(this, mDiscoveryInterval);
    }

    @Override
    public void onDnsSdTxtRecordAvailable(String fullDomainName,
                                          Map<String, String> txtRecordMap,
                                          WifiP2pDevice srcDevice) {

        if(srcDevice == null || txtRecordMap.isEmpty()) return;

        String addr = "wifi://" + srcDevice.deviceAddress;

//        L.d(txtRecordMap.toString(), this);

        ASIPInterest interest = null;
        try {
            interest = WifiDirectUtil.recordMap2Interest(txtRecordMap);
            if (interest != null) {
                PeerSemanticTag sender = interest.getSender();
                sender.addAddress(addr);
            }
        } catch (SharkKBException e) {
            e.printStackTrace();
        }

        // Inform Listener
        for(WifiDirectPeerListener listener : mPeerListeners){
            listener.onNewNearbyPeer(interest);
        }

    }

    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
        mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                for(WifiDirectNetworkListener listener : mNetworkListeners){
                    listener.onNetworkCreated(info, group);
                }
            }
        });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(mManager == null)
            return;

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
            } else {
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if(networkInfo.isConnected()){
                mState = WIFI_STATE.CONNECTED;
                mManager.requestConnectionInfo(mChannel, this);
            } else {
                mState = WIFI_STATE.DISCOVERING;
            }
        }
    }
}
