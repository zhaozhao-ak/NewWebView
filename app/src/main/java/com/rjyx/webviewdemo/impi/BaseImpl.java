package com.rjyx.webviewdemo.impi;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;



/**
 * 执行安卓功能的基类
 * @author Administrator
 *
 */
public class BaseImpl {
	private static final int REQUEST_CODE_PICK_IMAGE = 0;
	private static final int REQUEST_CODE_CAPTURE_CAMEIA = 0;
	private Activity mActitity; 
	public Activity getmActitity() {
		return mActitity;
	}

	public void setmActitity(Activity mActitity) {
		this.mActitity = mActitity;
	}

	private CallBackFunction responseCallback;
	
	public CallBackFunction getResponseCallback() {
		return responseCallback;
	}

	public void setResponseCallback(CallBackFunction responseCallback) {
		this.responseCallback = responseCallback;
	}
	
	public BaseImpl(Activity activity){
		this.mActitity = activity;
	}

	// 返回成功消息
	public  void successCallback(String action,JSONObject outputs){
		JSONObject responseDict = new JSONObject();
		try {
			responseDict.put("responseCode", "1");// 1表示调用成功
			responseDict.put("errorMessage", "");// 调用成功，无错误消息
			responseDict.put("action", action);// 功能名称
			responseDict.put("outputs", outputs);// 输出参数
			
			String responseData = responseDict.toString();
			this.responseCallback.onCallBack(responseData);
		} catch (JSONException e) {
			e.printStackTrace();
			// 发送异常消息
			faileCallback(action,e.getMessage());
		}
	}
	
	// 返回失败消息
	public void faileCallback(String action,String errorMsg){
		JSONObject responseDict = new JSONObject();
		try {
			responseDict.put("responseCode", "0");// 0表示调用失败
			responseDict.put("errorMessage", errorMsg);// 返回错误消息
			responseDict.put("action", action);// 功能名称
			responseDict.put("outputs", "");// 输出结果
			
			String responseData = responseDict.toString();
			this.responseCallback.onCallBack(responseData);
		} catch (JSONException e) {
			e.printStackTrace();
			// 异常时返回默认的消息
			this.responseCallback.onCallBack("{\"responseCode\":\"0\",\"errorMessage\":"
											+e.getMessage()+",\"action\":"
											+action+",\"outputs\":\"\"}");
		}
	}
	
}
