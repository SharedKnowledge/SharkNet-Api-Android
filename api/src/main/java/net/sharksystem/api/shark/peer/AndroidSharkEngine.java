package net.sharksystem.api.shark.peer;

import android.app.Activity;
import android.content.Context;

import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.asip.ASIPStub;
import net.sharkfw.asip.SharkStub;
import net.sharkfw.asip.engine.serializer.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.STSet;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.SharkKBException;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.peer.J2SEAndroidSharkEngine;
import net.sharkfw.protocols.RequestHandler;
import net.sharkfw.protocols.Stub;
import net.sharksystem.api.shark.ports.RadarDiscoveryPort;
import net.sharksystem.api.shark.protocols.bluetooth.BluetoothStreamStub;
import net.sharksystem.api.shark.protocols.nfc.NfcMessageStub;
import net.sharksystem.api.shark.protocols.wifidirect.WifiDirectAdvertisingManager;

import java.io.IOException;

public class AndroidSharkEngine extends J2SEAndroidSharkEngine {

    private Context mContext;
    private Activity activity;
    private Stub currentStub;
    private ASIPSpace mSpace;

    private WifiDirectAdvertisingManager mAdvertisingManager;

    public final static String DISCOVERY_TOPIC = "DISCOVERY_INTEREST";
    public final static String DISCOVERY_SI = "www.sharksystem.net/discovery";
    private NfcMessageStub.NFCMessageListener nfcMessageListener;

    public AndroidSharkEngine(Context context) {
        super();
        mContext = context;
        NearbyPeerManager.getInstance().setEngine(this);
    }

    public AndroidSharkEngine(Context context, SharkKB sharkKB){
        super(sharkKB);
        mContext = context;
        NearbyPeerManager.getInstance().setEngine(this);
    }

    public Context getContext(){
        return mContext;
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public void setSpace(ASIPSpace space) throws SharkKBException {

        STSet topicSet = InMemoSharkKB.createInMemoSTSet();
        topicSet.createSemanticTag(DISCOVERY_TOPIC, DISCOVERY_SI);

        if(space==null){
            mSpace = InMemoSharkKB.createInMemoASIPInterest(topicSet, null, getOwner(), null, null, null, null, ASIPSpace.DIRECTION_INOUT);
        } else {
            mSpace = space;
            mSpace.getSender().merge(getOwner());
            mSpace.getTopics().merge(topicSet);
        }
    }

    public ASIPSpace getSpace() {
        return mSpace;
    }

    public void addNearbyPeerListener(NearbyPeerManager.NearbyPeerListener listener){
        NearbyPeerManager.getInstance().addNearbyPeerListener(listener);
    }

    public void startDiscovery(){
        new RadarDiscoveryPort(this);
        mAdvertisingManager = new WifiDirectAdvertisingManager(mContext, this);
        mAdvertisingManager.startAdvertising(mSpace);
    }

    public void stopDiscovery(){
        mAdvertisingManager.stopAdvertising();
    }

    @Override
    protected Stub createNfcStreamStub(SharkStub stub) throws SharkProtocolNotSupportedException {
        if (currentStub == null) {
            currentStub = new NfcMessageStub(mContext, activity, nfcMessageListener);
            currentStub.setHandler((RequestHandler) stub);
        }
        return currentStub;
    }

    public void setNFCMessageListener(NfcMessageStub.NFCMessageListener listener){
        nfcMessageListener = listener;
    }

    @Override
    public void startNfc() throws SharkProtocolNotSupportedException, IOException {
        this.createNfcStreamStub(this.getAsipStub()).start();
    }

    @Override
    public void stopNfc() throws SharkProtocolNotSupportedException {
        this.createNfcStreamStub(this.getAsipStub()).stop();
    }

    public void sendNFCMessage(String message) throws IOException {
        ((NfcMessageStub) this.currentStub).sendMessage(message.getBytes(), "");
    }

    @Override
    protected Stub createBluetoothStreamStub(ASIPStub stub) throws SharkProtocolNotSupportedException {
        if(currentStub==null){
            currentStub = new BluetoothStreamStub(this, stub);
        }
        return currentStub;
    }

    @Override
    public void startBluetooth() throws SharkProtocolNotSupportedException, IOException {
        this.createBluetoothStreamStub(this.getAsipStub()).start();
    }

    @Override
    public void stopBluetooth() throws SharkProtocolNotSupportedException {
        this.createBluetoothStreamStub(this.getAsipStub()).stop();
    }

    @Override
    public Stub getProtocolStub(int type) throws SharkProtocolNotSupportedException {
        return super.getProtocolStub(type);
        //TODO this function is called by the parent but the parent function itself looks likes a big mess
    }
}
