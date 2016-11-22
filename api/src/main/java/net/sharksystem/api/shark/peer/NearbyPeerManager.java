package net.sharksystem.api.shark.peer;

import net.sharkfw.asip.ASIPInterest;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by j4rvis on 11/22/16.
 */

public class NearbyPeerManager {

    public interface NearbyPeerListener {
        void onNearbyPeerFound(ArrayList<NearbyPeer> peers);
    }

    private ArrayList<NearbyPeer> mPeers = new ArrayList<>();
    private ArrayList<NearbyPeerListener> mListeners = new ArrayList<>();

    public static NearbyPeerManager mInstance = null;

    public static NearbyPeerManager getInstance(){
        if(mInstance == null){
            mInstance = new NearbyPeerManager();
        }
        return mInstance;
    }

    private NearbyPeerManager() {
    }

    public void addNearbyPeerListener(NearbyPeerListener listener) {
        mListeners.add(listener);
    }

    public void removeNearbyPeerListener(NearbyPeerListener listener){
        mListeners.remove(listener);
    }

    public void addPeer(ASIPInterest interest){
        // TODO convert interest to Peer!
        NearbyPeer peer = new NearbyPeer(interest);

        if(mPeers.contains(peer)) {
            int indexOf = mPeers.indexOf(peer);
            NearbyPeer nearbyPeer = mPeers.get(indexOf);
            nearbyPeer.merge(peer);
        } else {
            mPeers.add(peer);
        }
        Collections.sort(mPeers);

        for (NearbyPeerListener listener : mListeners){
            listener.onNearbyPeerFound(mPeers);
        }
    }
}
