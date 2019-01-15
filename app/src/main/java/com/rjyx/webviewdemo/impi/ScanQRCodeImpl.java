package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;


import com.dtr.zxing.activity.CaptureActivity;
import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;
import com.rjyx.webviewdemo.MainActivity;

/**
 * 条码功能类
 * @author Administrator
 *
 */
public class ScanQRCodeImpl extends BaseImpl{
	
	

	public ScanQRCodeImpl(Activity activity) {
		super(activity);
	}

	public void scanQRCode(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);
		
		    	Intent intent=new Intent(this.getmActitity(), CaptureActivity.class);
		    	this.getmActitity().startActivity(intent);
		    
		
		// 返回模拟数据
		JSONObject outputs = new JSONObject();
		try {
			outputs.put("qrcode", "二维码！");
			successCallback(FCDeviceApi.ACTION_SCANQRCODE,outputs);
		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(FCDeviceApi.ACTION_SCANQRCODE,e.getMessage());
		}
	}

}
