package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;
import com.rjyx.webviewdemo.util.Constant;
import com.rjyx.webviewdemo.util.SharedPrefsUtil;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

public class PopLoginView extends BaseImpl{
	
	private String id;
	private String username;
	private String usertype;
	private String token;
	private String firstname;
	private String lastname;
	private String idcard;
	private String email;
	public PopLoginView(Activity activity) {
		super(activity);
		
	}
	public void popLoginView(CallBackFunction responseCallback){
		   this.setResponseCallback(responseCallback);
		  if(SharedPrefsUtil.getBooleanValue(this.getmActitity(), Constant.LOGIN_CONFIG, "isLogin", true)){
			  showToast("已经登录状态");
			    id=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "ID", "");
				  username=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "Username", "");
				  usertype=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "UserType", "");
				  token=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "Token", "");
				  firstname=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "FirstName", "");
				  lastname=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "LastName", "");
				  idcard=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "IDCard", "");
				  email=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "Email", "");
			 
			  
		   }else{
//			   Intent intent=new Intent(this.getmActitity(), LoginActivity.class);
//	 	         this.getmActitity().startActivity(intent);
		   }
		    
		    JSONObject outputs = new JSONObject();
			try {
				outputs.put("userID",id);
				outputs.put("userName",username);
				outputs.put("userType",usertype);
				outputs.put("token",token);
				outputs.put("firstName",firstname);
				outputs.put("lastName",lastname);
				outputs.put("idCard",idcard);
				outputs.put("Email",email);
				successCallback(FCDeviceApi.ACTION_POPLOGINVIEW,outputs);
			} catch (JSONException e) {
				e.printStackTrace();
				faileCallback(FCDeviceApi.ACTION_POPLOGINVIEW,e.getMessage());
			}

		}
	 private void showToast(String msg) {
			Toast.makeText(this.getmActitity(), msg, Toast.LENGTH_SHORT).show();
		}
}
