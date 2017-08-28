package com.clickatell.chatsample.touchsample;

import android.util.Log;

import com.clickatell.chatsecure.androidsdk.sdk2.TouchSdk;
import com.clickatell.chatsecure.androidsdk.utils.MapUtils;
import com.clickatell.chatsecure.androidsdk.utils.PushNotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by Alexey Sidorenko on 31.07.2017.
 */

public class AppPushService extends FirebaseMessagingService {
    public static final String TAG = AppPushService.class.getName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> notificationData = remoteMessage.getData();
        if (notificationData == null) {
            Log.e(TAG, "onMessageReceived: data is null, skip");
            return;
        }

        if (TouchSdk.LOG) {
            Log.d(TAG, "onMessageReceived: " + MapUtils.logMap(notificationData));
        }

        if (PushNotificationHelper.isClickatellPush(notificationData)){
            PushNotificationHelper.handlePush(getApplicationContext(), notificationData);
        } else {
            Log.v(TAG, "sample message " + notificationData.get("title"));
        }
    }

}
