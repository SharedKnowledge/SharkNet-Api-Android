package net.sharksystem.api.shark.protocols.wifidirect;

import android.content.Context;

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
public class WifiDirectStreamStub implements StreamStub {

    private final AndroidSharkEngine mEngine;
    private final WifiDirectAdvertisingManager mWifiDirectAdvertisingManager;
    private boolean mIsStarted = false;

    public WifiDirectStreamStub(Context context, AndroidSharkEngine engine) {
        mEngine = engine;
        mWifiDirectAdvertisingManager = new WifiDirectAdvertisingManager(context, mEngine);
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
    }

    @Override
    public void start() throws IOException {
        if(!mIsStarted){
            mWifiDirectAdvertisingManager.startAdvertising(mEngine.getSpace());
            mIsStarted = true;
        }
    }

    @Override
    public void stop() {
        if(mIsStarted){
            mWifiDirectAdvertisingManager.stopAdvertising();
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
        mWifiDirectAdvertisingManager.startAdvertising(asipSpace);
        mIsStarted = true;
    }

    @Override
    public void offer(Knowledge knowledge) throws SharkNotSupportedException {
        // TODO not yet implemented
    }
}
