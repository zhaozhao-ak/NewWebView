package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;



/**
 * 相册功能类
 * @author Administrator
 *
 */
public class AlbumImpl extends BaseImpl{
	
	
	public AlbumImpl(Activity activity) {
		super(activity);
		
		if(activity.getIntent().
				getComponent().
				getClassName().
				equals("CeHuaActivity")){
			//MainActivity tempActivity = (MainActivity)this.getmActitity();
			//tempActivity.setmAlbumImpl(this);
		}
		
	}
    
	@SuppressLint("NewApi")
	public void openAlbum(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);
		
		Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
	    intent.addCategory(Intent.CATEGORY_OPENABLE);
	    intent.setType("image/*");
	    intent.putExtra("return-data", true);
		this.getmActitity().startActivityForResult(intent, 11111);
	}
	
	public void ResultCallback(String imgdata)
	{
		JSONObject outputs = new JSONObject();
		try {
			outputs.put("imgdata",imgdata);
			successCallback(FCDeviceApi.ACTION_OPENALBUM,outputs);
		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(FCDeviceApi.ACTION_OPENALBUM,e.getMessage());
		}
	}
	
}
