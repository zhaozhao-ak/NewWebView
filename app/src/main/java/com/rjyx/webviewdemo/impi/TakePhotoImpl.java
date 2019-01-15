package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;



import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;



/**
 * 摄像头功能类
 * @author Administrator
 *
 */
public class TakePhotoImpl extends BaseImpl{
	private static final int CAMERA_RESULT = 0;
	private String imageFilePath;

	public TakePhotoImpl(Activity activity) {
		super(activity);
	}

	public void takePhoto(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);
		imageFilePath = Environment.getExternalStorageDirectory()  
				.getAbsolutePath() + "/mypicture.jpg"; 
    	Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFilePath); 
		this.getmActitity().startActivityForResult(i, CAMERA_RESULT);
	}
		
		// 返回模拟数据
		public void WeCallback(String imgdata)
		{
			JSONObject outputs = new JSONObject();
			try {
				outputs.put("imgdata",imgdata);
				successCallback(FCDeviceApi.ACTION_TAKEPHOTE,outputs);
			} catch (JSONException e) {
				e.printStackTrace();
				faileCallback(FCDeviceApi.ACTION_TAKEPHOTE,e.getMessage());
			}
		}
		
	}
