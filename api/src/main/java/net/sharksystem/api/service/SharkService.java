package net.sharksystem.api.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import net.sharkfw.system.L;
import net.sharksystem.api.dao_impl.SharkNetApiImpl;
import net.sharksystem.api.dao_impl.ThreadedSharkNetApiImpl;
import net.sharksystem.api.dao_interfaces.SharkNetApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by j4rvis on 4/5/17.
 */

public class SharkService extends Service implements Runnable {

    private LocalBinder mBinder = new LocalBinder();
    private ExecutorService mExecutor;
    private ThreadedSharkNetApiImpl mApi;

    @Override
    public void onCreate() {
        super.onCreate();
        L.d("Service created.", this);
        mExecutor = Executors.newSingleThreadExecutor();
//        mExecutor.execute(this);
        mApi = new ThreadedSharkNetApiImpl(mExecutor);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.d("Service startCommand.", this);
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public SharkNetApi getApi() {
        return SharkNetApiImpl.getInstance();
    }

    @Override
    public void run() {
        try {
            L.d("Tick", this);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mExecutor.execute(this);
    }

    public class LocalBinder extends Binder {
        public SharkService getService() {
            return SharkService.this;
        }
    }
}
