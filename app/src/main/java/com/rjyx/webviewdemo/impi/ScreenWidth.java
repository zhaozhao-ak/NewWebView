package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ScreenWidth extends BaseImpl{

	public ScreenWidth(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	public void screenWidth(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);
		 DisplayMetrics  dm = new DisplayMetrics(); 
		  getmActitity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		  int screenWidth = dm.widthPixels; 
		  
		  JSONObject outputs = new JSONObject();
			try {
				outputs.put("screenwidth",screenWidth);
				successCallback(FCDeviceApi.ACTION_SCREENWIDTH,outputs);
			} catch (JSONException e) {
				e.printStackTrace();
				faileCallback(FCDeviceApi.ACTION_SCREENWIDTH,e.getMessage());
			} 
	}

}
