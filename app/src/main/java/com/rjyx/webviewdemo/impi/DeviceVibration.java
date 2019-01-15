package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;



import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;
import android.app.Service;
import android.os.Vibrator;

public class DeviceVibration extends BaseImpl{
	private Vibrator vibrator;
	public DeviceVibration(Activity activity) {
		super(activity);
		
	}
	public void deviceVibration(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);
		vibrator = ( Vibrator ) this.getmActitity().getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate( new long[]{100,10,100,1000},-1);
		
		// 返回模拟数据
				JSONObject outputs = new JSONObject();
				try {
					outputs.put("qrcode", "手机震动");
					successCallback(FCDeviceApi.ACTION_SCANQRCODE,outputs);
				} catch (JSONException e) {
					e.printStackTrace();
					faileCallback(FCDeviceApi.ACTION_SCANQRCODE,e.getMessage());
				}
			}
		
	

}
