package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;
import com.rjyx.webviewdemo.util.Constant;
import com.rjyx.webviewdemo.util.SharedPrefsUtil;

import android.app.Activity;
import android.widget.Toast;

public class GetUserInfo extends BaseImpl {
    
	
	private String username;
	private String id;
	private String token;
	private String usertype;
	@SuppressWarnings("unused")
	private String realNameCertificateStatus;
	private String firstname;
	private String lastname;
	private String idcard;
	private String email;
	public GetUserInfo(Activity activity) {
		super(activity);
		
		
	}
	 public void getUserInfo(CallBackFunction responseCallback){
		   this.setResponseCallback(responseCallback);
		   if(SharedPrefsUtil.getBooleanValue(this.getmActitity(), Constant.LOGIN_CONFIG, "isLogin", true)){
		    id=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "ID", "");
		   token=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "Token", "");
		   usertype=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "UserType", "");
		    username=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "Username", "");
		    realNameCertificateStatus=SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG,
				   "", "");
		    firstname = SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "FirstName", "");
		    lastname = SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "LastName", "");
		    idcard = SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "IDCard", "");
		    email = SharedPrefsUtil.getStringValue(this.getmActitity(), Constant.LOGIN_CONFIG, "Email", "");
		   }else{
			   showToast("当前未登陆帐号");
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
				
				successCallback(FCDeviceApi.ACTION_GETUSERINFO,outputs);
			} catch (JSONException e) {
				e.printStackTrace();
				faileCallback(FCDeviceApi.ACTION_GETUSERINFO,e.getMessage());
			}

		}
	 private void showToast(String msg) {
			Toast.makeText(this.getmActitity(), msg, Toast.LENGTH_SHORT).show();
		}
  
}
