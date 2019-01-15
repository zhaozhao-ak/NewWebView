package com.rjyx.webviewdemo.impi;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GetCityName extends BaseImpl {
	public  String cityName ;  //城市名    

	private  Geocoder geocoder;   //此对象能通过经纬度来获取相应的城市等信息 

	private Activity context;
	public GetCityName(Activity activity) {
		super(activity);

	}
	public void getCityName(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);
		geocoder = new Geocoder(context);    
		//用于获取Location对象，以及其他    
		LocationManager locationManager;     
		String serviceName = Context.LOCATION_SERVICE;    
		//实例化一个LocationManager对象    
		locationManager = (LocationManager)context.getSystemService(serviceName);    
		//provider的类型    
		String provider = LocationManager.NETWORK_PROVIDER;    
		Criteria criteria = new Criteria();    
		criteria.setAccuracy(Criteria.ACCURACY_FINE);   //高精度    
		criteria.setAltitudeRequired(false);    //不要求海拔    
		criteria.setBearingRequired(false); //不要求方位    
		criteria.setCostAllowed(false); //不允许有话费    
		criteria.setPowerRequirement(Criteria.POWER_LOW);   //低功耗    

		//通过最后一次的地理位置来获得Location对象    
		Location location = locationManager.getLastKnownLocation(provider);    

		String queryed_name = updateWithNewLocation(location);    
		if((queryed_name != null) && (0 != queryed_name.length())){    

			cityName = queryed_name;    
		}    
            locationManager.requestLocationUpdates(provider,30000,50,locationListener);    
		    locationManager.removeUpdates(locationListener); 
}    
	private   LocationListener locationListener = new LocationListener() {    
		String tempCityName;    
		public void onLocationChanged(Location location) {    

			tempCityName = updateWithNewLocation(location);    
			if((tempCityName != null) && (tempCityName.length() != 0)){    

				cityName = tempCityName;    
			}    
		}    
		public void onProviderDisabled(String provider) {    
			tempCityName = updateWithNewLocation(null);    
			if ((tempCityName != null) && (tempCityName.length() != 0)) {    

				cityName = tempCityName;    
			}    
		}    

		public void onProviderEnabled(String provider) {    
		}    

		public void onStatusChanged(String provider, int status, Bundle extras) {    
		}    
	};    
	
	private  String updateWithNewLocation(Location location) {    
		String mcityName = "";   
		double lat = 0;    
		double lng = 0;    
		List<Address> addList = null;    
		if (location != null) {    
			lat = location.getLatitude();    
			lng = location.getLongitude();    
		} else {    

			System.out.println("无法获取地理信息");    
		}    

		try {    

			addList = geocoder.getFromLocation(lat, lng, 1);    //解析经纬度    

		} catch (IOException e) {    
			// TODO Auto-generated catch block    
			e.printStackTrace();    
		}    
		if (addList != null && addList.size() > 0) {    
			for (int i = 0; i < addList.size(); i++) {    
				Address add = addList.get(i);    
				mcityName += add.getLocality();    
			}    
		}    
		if(mcityName.length()!=0){    

			return mcityName.substring(0, (mcityName.length()-1)); 

		}
		JSONObject outputs = new JSONObject();
		try {
			outputs.put("cityname",cityName );
			successCallback(FCDeviceApi.ACTION_GETCITYNAME,outputs);
		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(FCDeviceApi.ACTION_GETCITYNAME,e.getMessage());
		}
         
        return mcityName;


	}    




}








