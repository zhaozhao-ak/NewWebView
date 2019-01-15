package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ScreenHeight extends BaseImpl{

	public ScreenHeight(Activity activity) {
		super(activity);
		
	}
   public void screenHeight(CallBackFunction responseCallback){
	   this.setResponseCallback(responseCallback);
	   DisplayMetrics metric = new DisplayMetrics();  
	   getmActitity().getWindowManager().getDefaultDisplay().getMetrics(metric);
	   int height = metric.heightPixels;  
	   
	   JSONObject outputs = new JSONObject();
		try {
			outputs.put("screenheight",height);
			successCallback(FCDeviceApi.ACTION_SCREENHEIGHT,outputs);
		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(FCDeviceApi.ACTION_SCREENHEIGHT,e.getMessage());
		} 
   }
}
