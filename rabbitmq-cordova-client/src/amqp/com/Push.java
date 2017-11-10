package org.amqp.notification;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.cordova.CordovaInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.util.Random;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.app.PendingIntent;
import android.app.Service;
import org.amqp.notification.StartNotificationReceiver;
import org.amqp.notification.Configuration;
import android.widget.Toast;

import amqp.com.ConfigurationFileService;

public class Push extends CordovaPlugin {

  private static CallbackContext clbContext;

  private static Context context;

  public static final String TAG = "Push";

  public static final String ACTION_INITIALIZE = "initialize";


  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
    try {
      clbContext = callbackContext;
      context = cordova.getActivity().getApplicationContext();
      ConfigurationFileService configurationFileService = new ConfigurationFileService(context);
      JSONObject conf = args.getJSONObject(0);

      if(action.equals("initialize")){
        if(configurationFileService.existeConfigurationFile()) {
          Activity activity = cordova.getActivity();

          Intent intent = new Intent();

          intent.setAction(StartNotificationReceiver.CUSTOM_INTENT);
          cordova.getActivity().getApplicationContext().sendBroadcast(intent);
        }
        clbContext.success("success");
      }else if(action.equals("saveConfigs")){
        configurationFileService.saveConfiguration(conf.toString());
      }else if(action.equals("clearConfigs")){
        configurationFileService.clearConfigurations();
      }

    } catch (Exception e) {
      Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
      clbContext.error(e.getMessage());
    }

    return true;
  }

  public static void proceedNotification(String message) {
    try {
      clbContext.success(message);
    } catch (Exception e) {
      clbContext.error(e.getMessage());
    }
  }

  public static void proceedError(String error){
    try {
      clbContext.error(error);
    } catch (Exception e) {
      clbContext.error(e.getMessage());
    }
  }

}
