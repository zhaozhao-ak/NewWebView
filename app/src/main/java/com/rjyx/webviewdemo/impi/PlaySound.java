package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class PlaySound extends BaseImpl {

	public PlaySound(Activity activity) {
		super(activity);
		
	}
	public void playSound(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);
		 Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);  
	     Ringtone r = RingtoneManager.getRingtone(getmActitity().getApplicationContext(), notification);
	     r.play(); 
	     //返回参数
	     JSONObject outputs = new JSONObject();
			try {
				outputs.put("playSound", "播放声音");
				successCallback(FCDeviceApi.ACTION_PLAYSOUND,outputs);
			} catch (JSONException e) {
				e.printStackTrace();
				faileCallback(FCDeviceApi.ACTION_PLAYSOUND,e.getMessage());
			}   
	}

}
