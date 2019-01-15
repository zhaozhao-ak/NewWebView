package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

public class GetDeviceName extends BaseImpl{

	public GetDeviceName(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
    public void getDeviceName(CallBackFunction responseCallback){
    	this.setResponseCallback(responseCallback);
    	TelephonyManager telephonyManager = (TelephonyManager) getmActitity().getSystemService(Context.TELEPHONY_SERVICE);
  	    String Homtype = android.os.Build.MODEL; 
  	    
  	  JSONObject outputs = new JSONObject();
		try {
			outputs.put("deviceName",Homtype);
			successCallback(FCDeviceApi.ACTION_GETDEVICENAME,outputs);
		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(FCDeviceApi.ACTION_GETDEVICENAME,e.getMessage());
		} 
    }
}
