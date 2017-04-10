package net.sharksystem.api.shark.peer;

import android.os.Handler;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.system.L;
import net.sharkfw.system.SharkTaskExecutor;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by j4rvis on 11/22/16.
 */

public class NearbyPeerManager {

    private final SharkTaskExecutor mTaskExecutor;
    private long mSyncInterval = 1000 * 60; // five minutes
    private long mPeerTimeout = 1000 * 60 ; // one minute
    private SharkEngine mEngine;

    public interface NearbyPeerListener {
        void onNearbyPeerFound(ArrayList<NearbyPeer> peers);
    }

    private ArrayList<NearbyPeer> mPeers = new ArrayList<>();
    private ArrayList<NearbyPeerListener> mListeners = new ArrayList<>();


    public NearbyPeerManager() {
        mTaskExecutor = SharkTaskExecutor.getInstance();
    }

    /**
     *
     * @param syncInterval - describes the interval when a peer should be synced again
     * @param peerTimeout - describes the timeout when a peer is described as not available
     */
    public void configureManager(long syncInterval, long peerTimeout){
        mSyncInterval = syncInterval;
        mPeerTimeout = peerTimeout;
    }

    public void setEngine(SharkEngine engine){
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

        // TODO implement syncronization
//        mHandler.post(this);

        for (NearbyPeerListener listener : mListeners){
            listener.onNearbyPeerFound(mPeers);
        }
    }


}
