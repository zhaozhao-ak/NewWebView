package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

public class GetIMSI extends BaseImpl{


	public GetIMSI(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("HardwareIds")
	@RequiresPermission(Manifest.permission.READ_PHONE_STATE)
	@Deprecated
	public void getIMSI(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);
		TelephonyManager mTelephonyMgr = (TelephonyManager) getmActitity().getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = mTelephonyMgr.getSubscriberId();
		
		JSONObject outputs = new JSONObject();
		try {
			outputs.put("imsi",imsi);
			successCallback(FCDeviceApi.ACTION_GETIMSI,outputs);
		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(FCDeviceApi.ACTION_GETIMSI,e.getMessage());
		} 
	}
}
