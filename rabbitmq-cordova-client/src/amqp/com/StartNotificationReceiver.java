package org.amqp.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;

import org.amqp.notification.NotificationService;

public class StartNotificationReceiver extends BroadcastReceiver {

   public static final String CUSTOM_INTENT = "intent.action.START_SERVICE";
    
    private static final String TAG = "BOOT_RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent locationServiceIntent = new Intent(context, NotificationService.class);
        locationServiceIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);

        context.startService(locationServiceIntent);
     }
}