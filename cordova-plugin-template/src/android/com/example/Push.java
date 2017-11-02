package org.amqp.notification;

import java.util.ArrayList;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import org.amqp.notification.PushNotification;
import org.amqp.notification.PushManager;

public class Push extends CordovaPlugin {
	
	private static CallbackContext clbContext;
	private PushManager manager;
	
	public static final String TAG = "Push";

	public static final String ACTION_INITIALIZE = "initialize";

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
		try {
			clbContext = callbackContext;

			this.manager = new PushManager(cordova.getActivity(), this);

			clbContext.success("Iniciou");
			return true;

		} catch (Exception e) {
			clbContext.error(e.getMessage());
		} finally {
			return false;
		}
	}

	public static void proceedNotification(PushNotification extras) {
		try {
			clbContext.success(extras.toString());
		} catch (Exception e) {
			clbContext.error(e.getMessage());
		}
	}
}