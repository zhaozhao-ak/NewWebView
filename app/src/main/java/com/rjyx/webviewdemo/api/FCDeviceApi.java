package com.rjyx.webviewdemo.api;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


import com.rjyx.webviewdemo.impi.AlbumImpl;
import com.rjyx.webviewdemo.impi.DeviceVibration;
import com.rjyx.webviewdemo.impi.GetCityName;
import com.rjyx.webviewdemo.impi.GetDeviceName;

import com.rjyx.webviewdemo.impi.GetIMSI;
import com.rjyx.webviewdemo.impi.GetLocation;
import com.rjyx.webviewdemo.impi.GetOSVersion;
import com.rjyx.webviewdemo.impi.GetUserInfo;
import com.rjyx.webviewdemo.impi.IsAndroid;
import com.rjyx.webviewdemo.impi.IsIpad;
import com.rjyx.webviewdemo.impi.IsIphone;

import com.rjyx.webviewdemo.impi.PlaySound;
import com.rjyx.webviewdemo.impi.PopLoginView;
import com.rjyx.webviewdemo.impi.Reachability;
import com.rjyx.webviewdemo.impi.ScanQRCodeImpl;
import com.rjyx.webviewdemo.impi.ScreenHeight;
import com.rjyx.webviewdemo.impi.ScreenWidth;
import com.rjyx.webviewdemo.impi.TakePhotoImpl;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;

import android.app.Activity;



/**
 * 安卓设备封装的功能类
 * @author Administrator
 *
 */
public class FCDeviceApi {

	// Android与Javascript通信的参数名称
	public static final String ACTION = "action";
	public static final String INPUTS = "inputs";
	public static final String OUTPUTS   = "outputs";

	// Android定义的功能名称
	public static final String ACTION_TAKEPHOTE = "takePhoto";
	public static final String ACTION_SCANQRCODE = "scanQRcode";
	public static final String ACTION_OPENALBUM = "openAlbum";
	public static final String ACTION_GETLOCATION  = "getLocation";
	public static final String ACTION_GETCITYNAME  = "getCityName";
	public static final String ACTION_HTTP_GET_IOS = "http_get_ios";
	public static final String ACTION_HTTP_POST_IOS = "http_post_ios";
	public static final String ACTION_ENCRYPT   = "encrypt";
	public static final String ACTION_DECRYPT   = "decrypt";
	public static final String ACTION_DEVICEVIBRATION = "deviceVibration";
	public static final String ACTION_NOTIFICATION = "notification";
	public static final String ACTION_PLAYSOUND = "playSound";
	public static final String ACTION_REACHABILITY = "Reachability";
	public static final String ACTION_GETOSVERSION = "getOSVersion";
	public static final String ACTION_ISANDROID = "IsAndroid";
	public static final String ACTION_ISIPHONE  = "IsIphone";
	public static final String ACTION_ISIPAD = "IsIpad";
	public static final String ACTION_GETIMSI   = "getIMSI";
	public static final String ACTION_GETDEVICENAME = "getDeviceName";
	public static final String ACTION_GETIDFV   = "getIDFV";
	public static final String ACTION_SCREENHEIGHT = "screenHeight";
	public static final String ACTION_SCREENWIDTH  = "screenWidth";
	public static final String ACTION_OPENMAPKIT = "openMapKit";
	public static final String ACTION_OPENMAPKIT_FORANNOTATION = "openMapKitForAnnotation";
	public static final String ACTION_DOWNOFFLINEMAP  = "downOfflineMap";
	public static final String ACTION_SWITCHLANGUAGES = "switchLanguages";
    public static final String ACTION_GETUSERINFO="getUserInfo";
    public static final String ACTION_POPLOGINVIEW="popLoginView";
	// 用于传递Activity上下文
	private Activity mActivity;
	public Activity getmActivity() {
		return mActivity;
	}

	public void setmActivity(Activity activity) {
		this.mActivity = activity;
	}

	// 存放功能处理器
	Map<String, ApiHandler> messageHandlers = new HashMap<String, ApiHandler>();
	// 存放处理完成后的回调方法
	Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();

	// 构造函数，传入主界面并且注册所有已经实现的功能
	public FCDeviceApi(Activity activity){
		this.setmActivity(activity);

		this.setupAPIHandlers();
	}



	// 注册所有已经实现的处理器
	private void setupAPIHandlers(){
		// 1.拍照
		messageHandlers.put(FCDeviceApi.ACTION_TAKEPHOTE, new ApiHandler(){

			@Override
			public void handler(JSONObject inputs) {
				TakePhotoImpl takePhotoImpl=new TakePhotoImpl(mActivity);
				takePhotoImpl.takePhoto(responseCallbacks.get(ACTION_TAKEPHOTE));
			}

		});
		// 2.扫描条码
		messageHandlers.put(FCDeviceApi.ACTION_SCANQRCODE, new ApiHandler(){

			@Override
			public void handler(JSONObject inputs) {
				ScanQRCodeImpl scanQRCodeImpl = new ScanQRCodeImpl(mActivity);
				scanQRCodeImpl.scanQRCode(responseCallbacks.get(ACTION_SCANQRCODE));
			}

		});
		// 3.打开相册
		messageHandlers.put(FCDeviceApi.ACTION_OPENALBUM, new ApiHandler(){

			@Override
			public void handler(JSONObject inputs) {
				AlbumImpl albumImpl=new AlbumImpl(mActivity);
				albumImpl.openAlbum(responseCallbacks.get(ACTION_OPENALBUM));
			}

		});
		//手机震动
		messageHandlers.put(FCDeviceApi.ACTION_DEVICEVIBRATION, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				DeviceVibration deviceVibration=new DeviceVibration(mActivity);
				deviceVibration.deviceVibration(responseCallbacks.get(ACTION_DEVICEVIBRATION));
			}
		});
		/*//消息通知栏
		messageHandlers.put(FCDeviceApi.ACTION_NOTIFICATION, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				Ntotification ntotification=new Ntotification(mActivity);
				ntotification.notification(responseCallbacks.get(ACTION_NOTIFICATION));

			}
		});*/
		//播放声音
		messageHandlers.put(FCDeviceApi.ACTION_PLAYSOUND, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				PlaySound playSound=new PlaySound(mActivity);
				playSound.playSound(responseCallbacks.get(ACTION_PLAYSOUND));

			}
		});
		//获取网络状态
		messageHandlers.put(FCDeviceApi.ACTION_REACHABILITY, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				Reachability reachability=new Reachability(mActivity);
				reachability.reachability(responseCallbacks.get(ACTION_REACHABILITY));

			}
		});
		//手机设备版本号
		messageHandlers.put(FCDeviceApi.ACTION_GETOSVERSION, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				GetOSVersion getOSVersion=new GetOSVersion(mActivity);
				getOSVersion.getOSVersion(responseCallbacks.get(ACTION_GETOSVERSION));

			}
		});
		//是否安卓设备
		messageHandlers.put(FCDeviceApi.ACTION_ISANDROID, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				IsAndroid isAndroid=new IsAndroid(mActivity);
				isAndroid.isAndroid(responseCallbacks.get(ACTION_ISANDROID));

			}
		});
		//是否Iphone设备
		messageHandlers.put(FCDeviceApi.ACTION_ISIPHONE, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				IsIphone isIphone=new IsIphone(mActivity);
				isIphone.isIphone(responseCallbacks.get(ACTION_ISIPHONE));

			}
		});
		//是否Ipad设备
		messageHandlers.put(FCDeviceApi.ACTION_ISIPAD, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				IsIpad isIpad=new IsIpad(mActivity);
				isIpad.isIpad(responseCallbacks.get(ACTION_ISIPAD));

			}
		});
		//获取IMSI编码
		messageHandlers.put(FCDeviceApi.ACTION_GETIMSI, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				GetIMSI getIMSI=new GetIMSI(mActivity);
				getIMSI.getIMSI(responseCallbacks.get(ACTION_GETIMSI));

			}
		});
		//获取设备名称
		messageHandlers.put(FCDeviceApi.ACTION_GETDEVICENAME, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				GetDeviceName getDeviceName=new GetDeviceName(mActivity);
				getDeviceName.getDeviceName(responseCallbacks.get(ACTION_GETDEVICENAME));

			}
		});
		//屏幕的分辨率高度
		messageHandlers.put(FCDeviceApi.ACTION_SCREENHEIGHT, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				ScreenHeight screenHeight=new ScreenHeight(mActivity);
				screenHeight.screenHeight(responseCallbacks.get(ACTION_SCREENHEIGHT));

			}
		});
		//屏幕的分辨率宽度
		messageHandlers.put(FCDeviceApi.ACTION_SCREENWIDTH, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				ScreenWidth screenWidth=new ScreenWidth(mActivity);
				screenWidth.screenWidth(responseCallbacks.get(ACTION_SCREENWIDTH));

			}
		});
		//获取经纬度
		messageHandlers.put(FCDeviceApi.ACTION_GETLOCATION, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				GetLocation getLocation=new GetLocation(mActivity);
				getLocation.getLocation(responseCallbacks.get(ACTION_GETLOCATION));

			}
		});
		//获取当前城市
		messageHandlers.put(FCDeviceApi.ACTION_GETCITYNAME, new ApiHandler() {

			@Override
			public void handler(JSONObject inputs) {
				GetCityName getCityName=new GetCityName(mActivity);
				getCityName.getCityName(responseCallbacks.get(ACTION_GETCITYNAME));

			}
		});
		messageHandlers.put(FCDeviceApi.ACTION_GETUSERINFO, new ApiHandler() {
			
			@Override
			public void handler(JSONObject inputs) {
				GetUserInfo getuserinfo = new GetUserInfo(mActivity);
				getuserinfo.getUserInfo(responseCallbacks.get(ACTION_GETUSERINFO));
				
			}
		});
         messageHandlers.put(FCDeviceApi.ACTION_POPLOGINVIEW, new ApiHandler() {
			
			@Override
			public void handler(JSONObject inputs) {
				PopLoginView popLoginView = new PopLoginView(mActivity);
				popLoginView.popLoginView(responseCallbacks.get(ACTION_POPLOGINVIEW));
				
			}
		});
		
	}

	// 对外公开的方法，在这里根据action调用不同的功能
	public void manage(String data,CallBackFunction function){
		ApiHandler handler;//处理器
		String actionName = "";//功能名称
		JSONObject inputsParams;//传入处理器的参数
		try {
			JSONObject jsonObject = new JSONObject(data);//将Javascript发送的消息序列化为 JSONObject的对象
			actionName = jsonObject.getString("action");//从Javascript发送的消息中取出action
			inputsParams = jsonObject.getJSONObject("inputs");//从Javascript发送的消息中取出inputs

			handler = messageHandlers.get(actionName);
			responseCallbacks.put(actionName, function);//存放回调函数
			if(handler !=null){
				//如果存在处理器，则调用相应功能
				handler.handler(inputsParams);
			}else
			{
				//如果不存在处理器，则返回错误提示
				faileCallback(actionName,"此功能 "+actionName+" 未完成！");
			}

		} catch (JSONException e) {
			e.printStackTrace();
			faileCallback(actionName,e.getMessage());
		}
	}

	// 返回成功消息
	private void successCallback(String action,JSONObject outputs){
		if(responseCallbacks.get(action) == null)return;

		JSONObject responseDict = new JSONObject();
		try {
			responseDict.put("responseCode", "1");// 1表示调用成功
			responseDict.put("errorMessage", "");// 调用成功，无错误消息
			responseDict.put("action", action);// 功能名称
			responseDict.put("outputs", outputs);// 输出参数

			String responseData = responseDict.toString();
			responseCallbacks.get(action).onCallBack(responseData);
		} catch (JSONException e) {
			e.printStackTrace();
			// 发送异常消息
			faileCallback(action,e.getMessage());
		}
	}

	// 返回失败消息
	private void faileCallback(String action,String errorMsg){
		if(responseCallbacks.get(action) == null)return;

		JSONObject responseDict = new JSONObject();
		try {
			responseDict.put("responseCode", "0");// 0表示调用失败
			responseDict.put("errorMessage", errorMsg);// 返回错误消息
			responseDict.put("action", action);// 功能名称
			responseDict.put("outputs", "");// 输出结果

			String responseData = responseDict.toString();
			responseCallbacks.get(action).onCallBack(responseData);
		} catch (JSONException e) {
			e.printStackTrace();
			// 异常时返回默认的消息
			responseCallbacks.get(action).onCallBack("{\"responseCode\":\"0\",\"errorMessage\":"
					+e.getMessage()+",\"action\":"
					+action+",\"outputs\":\"\"}");
		}
	}
}
