package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;

public class IsIpad extends BaseImpl{

	public IsIpad(Activity activity) {
		super(activity);
		
	}
	public void isIpad(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);

		JSONObject outputs = new JSONObject();
		try {
			outputs.put("result","NO");
			successCallback(FCDeviceApi.ACTION_ISIPAD,outputs);
		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(FCDeviceApi.ACTION_ISIPAD,e.getMessage());
		}

	}

}
