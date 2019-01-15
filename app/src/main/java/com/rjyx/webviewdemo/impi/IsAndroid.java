package com.rjyx.webviewdemo.impi;


import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;

public class IsAndroid extends BaseImpl{

	public IsAndroid(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
   public void isAndroid(CallBackFunction responseCallback){
	   this.setResponseCallback(responseCallback);
	   
	   
	   JSONObject outputs = new JSONObject();
		try {
			outputs.put("result","Yes");
			successCallback(FCDeviceApi.ACTION_ISANDROID,outputs);
		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(FCDeviceApi.ACTION_ISANDROID,e.getMessage());
		}

	}
	   
   
}
