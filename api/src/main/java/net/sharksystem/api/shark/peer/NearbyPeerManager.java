package net.sharksystem.api.shark.peer;

import android.os.Handler;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.knowledgeBase.sync.manager.SyncManager;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkTaskExecutor;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by j4rvis on 11/22/16.
 */

public class NearbyPeerManager {

    private SharkEngine mEngine;

    public interface NearbyPeerListener {
        void onNearbyPeersFound(ArrayList<NearbyPeer> peers);
        void onNearbyPeerFound(NearbyPeer peer);
    }

    private ArrayList<NearbyPeer> mPeers = new ArrayList<>();
    private ArrayList<NearbyPeerListener> mListeners = new ArrayList<>();

    public NearbyPeerManager(SharkEngine engine) {
        mEngine = engine;
    }

    public void addNearbyPeerListener(NearbyPeerListener listener) {
        mListeners.add(listener);
    }

    public void removeNearbyPeerListener(NearbyPeerListener listener){
        mListeners.remove(listener);
    }

    public void addPeer(ASIPInterest interest){
        NearbyPeer peer = new NearbyPeer(interest);

        if(mPeers.contains(peer)) {
            int indexOf = mPeers.indexOf(peer);
            NearbyPeer nearbyPeer = mPeers.get(indexOf);
            nearbyPeer.merge(peer);
        } else {
            mPeers.add(peer);
        }
        Collections.sort(mPeers);

        L.d("Found Peer: " + peer.getSender().getName(), this);

        mEngine.getSyncManager().doInviteOrSync(peer.getSender());

        for (NearbyPeerListener listener : mListeners){
            listener.onNearbyPeersFound(mPeers);
            listener.onNearbyPeerFound(peer);
        }
    }


}
