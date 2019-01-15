package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

public class GetOSVersion extends BaseImpl{

	public GetOSVersion(Activity activity) {
		super(activity);
		
	}
	public void getOSVersion(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);
		TelephonyManager telephonyManager = (TelephonyManager) getmActitity().getSystemService(Context.TELEPHONY_SERVICE);
		  String newtype = android.os.Build.VERSION.RELEASE; 
		  
		  JSONObject outputs = new JSONObject();
			try {
				outputs.put("osversion",newtype);
				successCallback(FCDeviceApi.ACTION_REACHABILITY,outputs);
			} catch (JSONException e) {
				e.printStackTrace();
				faileCallback(FCDeviceApi.ACTION_REACHABILITY,e.getMessage());
			}

		}
	

}
