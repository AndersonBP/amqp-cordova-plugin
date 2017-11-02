package org.amqp.notification;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

public class Push extends CordovaPlugin {
	
	private static CallbackContext clbContext;

	public static final String TAG = "Push";

	public static final String ACTION_INITIALIZE = "initialize";

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		try {
			clbContext = callbackContext;

			if(action.equals("initialize")){
				Activity activity = cordova.getActivity();

				Intent intent = new Intent( activity, NotificationService.class);
				activity.startService(intent);

				clbContext.success();
			}
		} catch (Exception e) {
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