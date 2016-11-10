package net.sharksystem.api.shark.protocols.wifidirect_obsolete;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import net.sharkfw.system.L;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by j4rvis on 28.01.16.
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class WifiDirectListener
        implements WifiP2pManager.DnsSdTxtRecordListener,
        WifiDirectStatus,
            WifiP2pManager.PeerListListener,
            WifiP2pManager.DnsSdServiceResponseListener{

    public interface WifiDirectPeerListener{
        public ArrayList<WifiDirectPeer> onPeersAvailable();
    }

    public final static String NEW_PEERS_ACTION = "net.sharksystem.android.wifi.p2p.NEW_PEERS";
    public final static String NEW_RECORDS_ACTION = "net.sharksystem.android.wifi.p2p.NEW_RECORDS";
    public final static String CONNECTION_ESTABLISHED_ACTION = "net.sharksystem.android.wifi.p2p.CONNECTION_ESTABLISHED";

    private Context _context = null;
    private LocalBroadcastManager _lbm;

    private ArrayList<WifiDirectPeer> _peers = new ArrayList<>();

    protected WifiDirectListener(Context context) {
        _context = context;
        _lbm = LocalBroadcastManager.getInstance(_context);
    }

    public ArrayList<WifiDirectPeer> getPeers() {
        return _peers;
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {
        sendBroadcast(NEW_PEERS_ACTION);
    }

    @Override
    public void onDnsSdTxtRecordAvailable(String fullDomainName, Map<String, String> txtRecordMap, WifiP2pDevice srcDevice) {

        WifiDirectPeer newPeer = new WifiDirectPeer(srcDevice, txtRecordMap);
        if (this._peers.contains(newPeer)) {
            WifiDirectPeer peer = this._peers.get(this._peers.indexOf(newPeer));
            if (peer.getLastUpdated() < newPeer.getLastUpdated()) {
                this._peers.remove(peer);
                this._peers.add(newPeer);
            }
        } else {
            this._peers.add(newPeer);
        }
        if (!_peers.isEmpty())
            sendBroadcast(NEW_RECORDS_ACTION);
    }

    private void sendBroadcast(String action) {
        Intent intent = new Intent(action);
        intent.putParcelableArrayListExtra("WifiDirectPeers", _peers);
        _lbm.sendBroadcast(intent);
    }

    public void sendConnectionBroadcast(WifiP2pGroup group, WifiP2pInfo info){
        Intent intent = new Intent(CONNECTION_ESTABLISHED_ACTION);
        intent.putExtra("WifiP2PGroup", group);
        intent.putExtra("WifiP2PInfo", info);
        _lbm.sendBroadcast(intent);
    }

    @Override
    public void onStatusChanged(int status) {
        String toastText = "";
        switch (status) {
            case WifiDirectStatus.DISCOVERING:
                toastText = "DISCOVERING";
                break;
            case WifiDirectStatus.CONNECTED:
                toastText = "CONNECTED";
                break;
            case WifiDirectStatus.DISCONNECTED:
                toastText = "DISCONNECTED";
                break;
            case WifiDirectStatus.STOPPED:
                toastText = "STOPPED";
                break;
            case WifiDirectStatus.INITIATED:
                toastText = "INITIATED";
                break;
        }

        L.d("onStatusChanged: "+ toastText, this);
        if (_context != null)
            Toast.makeText(this._context, toastText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDnsSdServiceAvailable(String instanceName, String registrationType, WifiP2pDevice srcDevice) {

    }
}
