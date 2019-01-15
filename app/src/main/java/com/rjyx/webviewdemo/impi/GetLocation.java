package com.rjyx.webviewdemo.impi;



import org.json.JSONException;
import org.json.JSONObject;

import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;


public class GetLocation extends BaseImpl {

	private double latitude;
	private double longitude;
	public GetLocation(Activity activity) {
		super(activity);

	}
	@SuppressLint("HardwareIds")
	@RequiresPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
	@Deprecated
	public void getLocation(CallBackFunction responseCallback){
		this.setResponseCallback(responseCallback);

		LocationManager locationManager =
				(LocationManager) getmActitity().getSystemService(Context.LOCATION_SERVICE);
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location != null)
			{
				latitude = location.getLatitude();
				longitude = location.getLongitude();

			} else 
			{
				LocationListener locationListener = new LocationListener() 
				{

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
					}

					@Override
					public void onProviderEnabled(String provider) {
					}

					@Override
					public void onProviderDisabled(String provider) {
					}

					@Override
					public void onLocationChanged(Location location) {
						if (location != null) {
							latitude = location.getLatitude(); 
							longitude = location.getLongitude(); 

						}
					}

				};
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 1000, 0,
						locationListener);
				Location location1 = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (location1 != null) {
					latitude = location1.getLatitude(); 
					longitude = location1.getLongitude();
					
				}
			}
		}else{
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		}
            JSONObject outputs = new JSONObject();
		try {
			outputs.put("longitude",longitude);
			outputs.put("latitude", latitude);
			successCallback(FCDeviceApi.ACTION_GETLOCATION,outputs);
		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(FCDeviceApi.ACTION_GETLOCATION,e.getMessage());
		} 
	}






}
