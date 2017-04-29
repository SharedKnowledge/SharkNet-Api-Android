package net.sharksystem.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.ThreadedSharkNetApiImpl;
import net.sharksystem.api.dao_interfaces.SharkNetApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by j4rvis on 4/5/17.
 */

public class SharkService extends Service {

    private LocalBinder mBinder = new LocalBinder();
    private ExecutorService mExecutor;
    private ThreadedSharkNetApiImpl mApi;
    private int mBoundClients = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        L.d("Service created.", this);
        mExecutor = Executors.newSingleThreadExecutor();
        mApi = new ThreadedSharkNetApiImpl(this, mExecutor);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.d("Service startCommand.", this);
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mBoundClients++;
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if(--mBoundClients == 0){
            L.d("Stopping the service.", this);
            stopSelf();
        }
        return super.onUnbind(intent);
    }

    public SharkNetApi getApi() {
        return mApi;
    }

    public class LocalBinder extends Binder {
        public SharkService getService() {
            return SharkService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApi.stopRadar();
    }
}