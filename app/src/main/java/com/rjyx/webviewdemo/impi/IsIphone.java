package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;

public class IsIphone extends BaseImpl{

	public IsIphone(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	public void isIphone(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);

		JSONObject outputs = new JSONObject();
		try {
			outputs.put("result","NO");
			successCallback(FCDeviceApi.ACTION_ISIPHONE,outputs);
		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(FCDeviceApi.ACTION_ISIPHONE,e.getMessage());
		}

	}


}
