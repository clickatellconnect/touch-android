package com.clickatell.chatsample.touchsample;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.clickatell.chatsecure.androidsdk.sdk2.TouchSdk;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by Alexey Sidorenko on 24.07.2017.
 */

public class App extends Application {
    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        TouchSdk.LOG = true;
        TouchSdk.install(this, getString(R.string.clickatekll_token));

        String token = null;
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "private token - " + token);

    }
}
