package net.sharksystem.api.shark.peer;

import net.sharkfw.asip.ASIPInterest;
import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;

import java.util.ArrayList;

/**
 * Created by j4rvis on 11/22/16.
 */

public class NearbyPeer implements Comparable {

    private ASIPInterest mInterest;
    private final PeerSemanticTag mSender;
    private long mLastSeen = 0;
    private ArrayList<Long> mPeerSeenList = new ArrayList<>();
    private boolean mBusy = false;

    private long mLastSynced = 0;
    private ArrayList<Long> mSyncedList = new ArrayList<>();

    public NearbyPeer(ASIPInterest interest) {
        mInterest = interest;
        mSender = interest.getSender();
        this.updateLastSeen();
    }

    public synchronized boolean isSyncing(){
        return mBusy;
    }

    public synchronized void setSyncing(boolean value){
        mBusy = value;
    }

    public ASIPInterest getInterest() {
        return mInterest;
    }

    public PeerSemanticTag getSender() {
        return mSender;
    }

    public long getLastSeen() {
        return mLastSeen;
    }

    public int getTimesSeen(){
        return mPeerSeenList.size();
    }

    private void updateLastSeen(){
        mLastSeen = System.currentTimeMillis();
        mPeerSeenList.add(mLastSeen);
    }

    public long getLastSynced() {
        return mLastSynced;
    }

    public int getTimesSynced(){
        return mSyncedList.size();
    }

    public void updateSynced(){
        mLastSynced = System.currentTimeMillis();
        mSyncedList.add(mLastSynced);
    }

    public void merge(NearbyPeer peer){
        mInterest = peer.getInterest();
        this.updateLastSeen();
    }

    @Override
    public boolean equals(Object o) {
        NearbyPeer peer = (NearbyPeer) o;
        return SharkCSAlgebra.identical(this.getSender(), peer.getSender());
    }

    @Override
    public int compareTo(Object o) {
        NearbyPeer peer = (NearbyPeer) o;
        int result = 0;
        if(this.getLastSeen() > peer.getLastSeen()){
            result = 1;
        } else if(this.getLastSeen() < peer.getLastSeen()){
            result = -1;
        }
        return result;
    }

}
