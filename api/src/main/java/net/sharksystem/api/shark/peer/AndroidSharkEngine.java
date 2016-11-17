package net.sharksystem.api.shark.peer;

import android.app.Activity;
import android.content.Context;

import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.SharkStub;
import net.sharkfw.kep.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.peer.J2SEAndroidSharkEngine;
import net.sharkfw.protocols.RequestHandler;
import net.sharkfw.protocols.Stub;
import net.sharksystem.api.shark.protocols.bluetooth.BluetoothStreamStub;
import net.sharksystem.api.shark.protocols.nfc.NfcMessageStub;
import net.sharksystem.api.shark.protocols.wifidirect.WifiDirectManager;
import net.sharksystem.api.shark.protocols.wifidirect.WifiDirectStreamStub;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AndroidSharkEngine extends J2SEAndroidSharkEngine
        implements WifiDirectManager.WifiDirectPeerListener {

    private Context mContext;
    private WeakReference<Activity> activityRef;
    private Stub currentStub;
    private ASIPSpace mSpace;

    private HashMap<ASIPSpace, Long> mNearbyPeers = new HashMap<>();
    private ArrayList<NearbyPeersListener> mNearbyPeersListeners = new ArrayList<>();

    public AndroidSharkEngine(Context context) {
        super();
        mContext = context;
    }

    public AndroidSharkEngine(Activity activity) {
        super();
        mContext = activity.getApplicationContext();
        activityRef = new WeakReference<>(activity);
    }

    public void setSpace(ASIPSpace space) {
        if(space==null){
            try {
                mSpace = InMemoSharkKB.createInMemoASIPInterest(null, null, getOwner(), null, null, null, null, ASIPSpace.DIRECTION_INOUT);
            } catch (SharkKBException e) {
                e.printStackTrace();
            }
        } else {
            mSpace = space;
        }
    }

    public ASIPSpace getSpace() {
        return mSpace;
    }

    /*
     * Wifi Direct methods
     * @see net.sharkfw.peer.SharkEngine#createWifiDirectStreamStub(net.sharkfw.kep.KEPStub)
     */
    protected Stub createWifiDirectStreamStub(SharkStub stub) throws SharkProtocolNotSupportedException {
        if (currentStub == null) {
            currentStub = new WifiDirectStreamStub(mContext, this);
            currentStub.setHandler((RequestHandler) stub);
        }
        return currentStub;
    }

    @Override
    public void startWifiDirect() throws SharkProtocolNotSupportedException, IOException {
        this.createWifiDirectStreamStub(this.getAsipStub()).start();
    }

    public void stopWifiDirect() throws SharkProtocolNotSupportedException {
        currentStub.stop();
    }

    @Override
    protected Stub createNfcStreamStub(SharkStub stub) throws SharkProtocolNotSupportedException {
        if (currentStub == null) {
            currentStub = new NfcMessageStub(mContext, activityRef);
            currentStub.setHandler((RequestHandler) stub);
        }
        return currentStub;
    }

    @Override
    public void startNfc() throws SharkProtocolNotSupportedException, IOException {
        this.createNfcStreamStub(this.getAsipStub()).start();
    }

    @Override
    public void stopNfc() throws SharkProtocolNotSupportedException {
        this.createNfcStreamStub(this.getAsipStub()).stop();
    }

    @Override
    protected Stub createBluetoothStreamStub(SharkStub kepStub) throws SharkProtocolNotSupportedException {
        if(currentStub==null){
            currentStub = new BluetoothStreamStub();
        }
        return currentStub;
    }

    @Override
    public void startBluetooth() throws SharkProtocolNotSupportedException, IOException {
        this.createBluetoothStreamStub(this.getAsipStub()).start();
    }

    @Override
    public void stopBluetooth() throws SharkProtocolNotSupportedException {
        currentStub.stop();
    }

    @Override
    public Stub getProtocolStub(int type) throws SharkProtocolNotSupportedException {
        return super.getProtocolStub(type);
        //TODO this function is called by the parent but the parent function itself looks likes a big mess
    }

    @Override
    public void onNewNearbyPeer(ASIPSpace interest) {
        if (interest.getSender() != null) {

            Iterator<Map.Entry<ASIPSpace, Long>> iterator = mNearbyPeers.entrySet().iterator();
            while (iterator.hasNext()){
//                L.d(entry.getKey().getSender().getName() + " " + Arrays.toString(entry.getKey().getSender().getAddresses()), this);
                Map.Entry<ASIPSpace, Long> next = iterator.next();
                if (next.getKey().getSender().getName().equals(interest.getSender().getName())) {
                    iterator.remove();
                }
            }
            this.mNearbyPeers.put(interest, System.currentTimeMillis());

            for (NearbyPeersListener listener : mNearbyPeersListeners) {
                listener.onNearbyPeerDetected(mNearbyPeers);
            }
        }
    }

    // Wifi Radar

    public interface NearbyPeersListener {
        void onNearbyPeerDetected(HashMap<ASIPSpace, Long> nearbyPeersMap);
    }

    public void addNearbyPeersListener(NearbyPeersListener listener) {
        if (!mNearbyPeersListeners.contains(listener)) {
            mNearbyPeersListeners.add(listener);
        }
    }

    public void removeNearbyPeersListener(NearbyPeersListener listener) {
        mNearbyPeersListeners.remove(listener);
    }

    public List<ASIPSpace> getNearbyPeersAsList(long millis) {
        long currentTime = System.currentTimeMillis();

        ArrayList<ASIPSpace> tags = new ArrayList<>();

        for (Map.Entry<ASIPSpace, Long> entry : this.mNearbyPeers.entrySet()) {
            if (currentTime - entry.getValue() <= millis) {
                tags.add(entry.getKey());
            }
        }

        return tags;
    }

    public List<ASIPSpace> getNearbyPeersAsList() {
        return getNearbyPeersAsList(1000 * 60);
    }


    public HashMap<ASIPSpace, Long> getNearbyPeers(long millis) {
        long currentTime = System.currentTimeMillis();

        HashMap<ASIPSpace, Long> temp = new HashMap<>();

        for (Map.Entry<ASIPSpace, Long> entry : this.mNearbyPeers.entrySet()) {
            if (currentTime - entry.getValue() <= millis) {
                temp.put(entry.getKey(), entry.getValue());
            }
        }

        return temp;
    }

    public HashMap<ASIPSpace, Long> getNearbyPeers() {
        return getNearbyPeers(1000 * 60);
    }


}
