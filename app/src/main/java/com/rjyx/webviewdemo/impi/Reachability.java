package com.rjyx.webviewdemo.impi;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class Reachability extends BaseImpl{


	public Reachability(Activity activity) {
		super(activity);
		// TODO Auto-generated constructor stub
	}
	public void reachability(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);
		String networkType = ""; 
	    ConnectivityManager cm = (ConnectivityManager) getmActitity().getSystemService(Context.CONNECTIVITY_SERVICE); 
	    NetworkInfo info = cm.getActiveNetworkInfo();
	    if (info == null) { 
	    	networkType = "null"; 
	    } else if (info.getType() == ConnectivityManager.TYPE_WIFI) { 
	    	networkType = "wifi"; 
	    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) { 
	        int subType = info.getSubtype(); 
	        if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS 
	           || subType == TelephonyManager.NETWORK_TYPE_EDGE) { 
	        	networkType = "2g"; 
	        } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA 
	                || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0 
	                || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) { 
	        	networkType = "3g"; 
	        } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准 
	        	networkType = "4g"; 
	        } 
	        
	    } 

	    JSONObject outputs = new JSONObject();
		try {
			outputs.put("networkstatus",networkType);
			successCallback(FCDeviceApi.ACTION_REACHABILITY,outputs);
		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(FCDeviceApi.ACTION_REACHABILITY,e.getMessage());
		}

	}
	
}


