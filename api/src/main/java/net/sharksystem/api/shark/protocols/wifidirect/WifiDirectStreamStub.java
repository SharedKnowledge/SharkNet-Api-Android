package net.sharksystem.api.shark.protocols.wifidirect;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;

import net.sharkfw.asip.ASIPSpace;
import net.sharkfw.knowledgeBase.Knowledge;
import net.sharkfw.protocols.RequestHandler;
import net.sharkfw.protocols.StreamConnection;
import net.sharkfw.protocols.StreamStub;
import net.sharkfw.system.SharkNotSupportedException;
import net.sharksystem.api.shark.peer.AndroidSharkEngine;

import java.io.IOException;

/**
 * Created by j4rvis on 22.07.16.
 */
public class WifiDirectStreamStub implements StreamStub, WifiDirectManager.WifiDirectNetworkListener {

    private final AndroidSharkEngine mEngine;
    private final WifiDirectManager mWifiDirectManager;
    private boolean mIsStarted = false;
    private RequestHandler mRequestHandler;

    public WifiDirectStreamStub(Context context, AndroidSharkEngine engine) {
        mEngine = engine;
        mWifiDirectManager = new WifiDirectManager(context, mEngine);
    }

    @Override
    public StreamConnection createStreamConnection(String address) throws IOException {
        return null;
    }

    @Override
    public String getLocalAddress() {
        // TODO how to get the own Wifi ip address?
        return null;
    }

    @Override
    public void setHandler(RequestHandler requestHandler) {
        mRequestHandler = requestHandler;
    }

    @Override
    public void start() throws IOException {
        if(!mIsStarted){
            mWifiDirectManager.startAdvertising(mEngine.getSpace());
            mIsStarted = true;
        }
    }

    @Override
    public void stop() {
        if(mIsStarted){
            mWifiDirectManager.stopAdvertising();
            mIsStarted = false;
        }
    }

    @Override
    public boolean started() {
        return mIsStarted;
    }

    @Override
    public void offer(ASIPSpace asipSpace) throws SharkNotSupportedException {
        stop();
        mWifiDirectManager.startAdvertising(asipSpace);
        mIsStarted = true;
    }

    @Override
    public void offer(Knowledge knowledge) throws SharkNotSupportedException {
        // TODO not yet implemented
    }

    @Override
    public void onNetworkCreated(WifiP2pInfo info, WifiP2pGroup group) {
    }

    @Override
    public void onNetworkDestroyed() {

    }
}
