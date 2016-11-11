package net.sharksystem.api.shark;

import android.content.Context;

/**
 * Created by j4rvis on 18.05.16.
 */
public class Application extends android.app.Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
