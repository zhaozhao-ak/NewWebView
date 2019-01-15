package com.rjyx.webviewdemo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebStorage;

@SuppressLint("NewApi")
public class WebCacheManager {
	private final String TAG = "WebCacheManager";
	public final String DEFAULT_SD_PATH = "rjyx_cache";//sd卡目录
	public final String APP_CACAHE_DIRNAME = "/rjyx_webcache";//缓存目录

	private ArrayList<String> extensionArray = null;//扩展名数组
	private ArrayList<String> assetsArray = null;//assets资源目录文件列表
	private HashMap<String, String> mimeTypes = null;//MIME类型数组

	private Context mContext;//应用上下文
	private FileUtils fileTools = new FileUtils();//文件工具

	public WebCacheManager(Context context) {
		setContext(context);
		// assets目录文件
		assetsArray = new ArrayList<String>();
		String assetsFiles[] = { "angular.js" };
		for (String filename : assetsFiles) {
			assetsArray.add(filename);
		}
		// 文件扩展名
		String extensions[] = { "js", "css", "eot", "svg", "ttf", "woff", "woff2", "map", "png", "jpeg", "gif",
				"html","ico" };
		extensionArray = new ArrayList<String>();
		for (String extension : extensions) {
			extensionArray.add(extension);
		}
		// 文件类型字典
		mimeTypes = new HashMap<String, String>();
		// 前端脚本
		mimeTypes.put("txt", "text/plain");
		mimeTypes.put("html", "text/html");
		mimeTypes.put("css", "text/css");
		mimeTypes.put("js", "text/javascript");
		mimeTypes.put("map", "text/plain");
		// 字体文件
		mimeTypes.put("woff", "application/font-woff");
		mimeTypes.put("woff2", "application/font-woff2");
		mimeTypes.put("ttf", "application/x-font-ttf");
		mimeTypes.put("eot", "application/vnd.ms-fontobject");
		mimeTypes.put("svg", "image/svg+xml");

		// 图像
		mimeTypes.put("png", "image/png");
		mimeTypes.put("jpeg", "image/jpeg");
		mimeTypes.put("gif", "image/gif");
		mimeTypes.put("ico", "image/x-icon");
	}

	/**
	 * 应用上下文
	 * @return
	 */
	public Context getContext() {
		return mContext;
	}

	public void setContext(Context mContext) {
		this.mContext = mContext;
	}
	
	/**
	 * 返回MIME类型数组
	 * @return
	 */
	public HashMap<String, String> getMimeTypes() {
		return mimeTypes;
	}

	public WebResourceResponse readWebResourceResponse(String url) {

		if (thisURLRequestlsFile(url)) {
			try {
				String fileName = thisURLRequestIsAssetsFile(url);

				if (!fileName.equals("")) {
					InputStream inputStream = this.getResources().getAssets().open(fileName);
					int subing = fileName.lastIndexOf(".");
					String extension = fileName.substring(subing + 1);
					String mimeType = mimeTypes.get(extension);

					return new WebResourceResponse(mimeType, "utf-8", inputStream);
				} else {

					return cachedLocationForURLRequest(url);
				}

			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return null;
	}

	private Resources getResources() {
		return this.getContext().getResources();
	}

	/**
	 * 判断 unsureTarget 是否文件名称
	 *
	 * @return
	 * @return
	 */
	private boolean thisIsFileName(String unsureTarget) {
		int startIndex = unsureTarget.lastIndexOf(".");
		String extension = unsureTarget.substring(startIndex + 1);
		if (extensionArray.contains(extension)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前url是否包含文件名称 将？号左右的值进行拆分 如果没有？号直接返回/后的字符串 如果有将？左右值进行拆分
	 * 
	 * @param url
	 * @return
	 * @return
	 */
	public boolean thisURLRequestlsFile(String url) {
		int startIndex = url.lastIndexOf("/");
		String unsureTarget = url.substring(startIndex + 1);
		String[] items = unsureTarget.split("\\?");
		boolean result = false;
		if (thisIsFileName(items[0])) {
			Log.i(TAG, items[0] + "是文件");
			result = true;
		} else {
			Log.i(TAG, items[0] + "不是文件");
			result = false;
		}
		return result;
	}

	/**
	 * 获取url包含的本地资源文件名称 首先检查url是否为文件路径，如果不是则返回 “” 如果文件名称与assets目录文件匹配，则返回文件名称
	 * 否则返回 “”
	 * 
	 * @param url
	 * @return
	 */
	public String thisURLRequestIsAssetsFile(String url) {
		int startIndex = url.lastIndexOf("/");
		String unsureTarget = url.substring(startIndex + 1);
		String[] items = unsureTarget.split("\\?");
		if (thisIsFileName(items[0]) && assetsArray.contains(items[0])) {
			Log.i(TAG, items[0] + "是assets文件");
			return items[0];
		} else {
			Log.i(TAG, items[0] + "不是assets文件");
			return "";
		}
	}

	// 获取url包含的远程文件名称
	// 首先检查url是否为文件路径，
	// 如果不是则返回 “”
	// 如果是文件名称，判断是否在assets目录，
	// 如果存在返回“”，
	// 如果不存在则返回url的md5值，然后拼接成存储路径 例如“/data/sdcard/fcyd_cache/文件名称”
	public WebResourceResponse cachedLocationForURLRequest(String url) {
		int startIndex = url.lastIndexOf("/");
		String unsureTarget = url.substring(startIndex + 1);
		String[] items = unsureTarget.split("\\?");
		//如果uil是文件类型 并且不再assets目录，则尝试从 fcyd_cache 读取
		if (thisIsFileName(items[0]) && !assetsArray.contains(items[0])) {
			//将url按“？”拆分,左侧是文件链接
			String[] urlItems = url.split("\\?");
			int startIndex2 = items[0].lastIndexOf(".");
			//得到文件的扩展名
			String extension = items[0].substring(startIndex2 + 1);
			//得到文件的MIME类型
			String mimeType = mimeTypes.get(extension);
			//将文件的url去除参数后，进行MD5加密。得到32位长度的名称
			String cacheFile = MD5Util.MD5(urlItems[0]) + "." + extension;
			if (fileTools.isFileExist(DEFAULT_SD_PATH, cacheFile)) {
				Log.i(TAG, items[0] + "从sd卡读取:" + cacheFile);
				InputStream inputStream = fileTools.streamFromSD(DEFAULT_SD_PATH, cacheFile);				

				return new WebResourceResponse(mimeType, "utf-8", inputStream);
			}
		}
		return null;
	}

	public void ClearCaches(){
		//清空所有Cookie
		CookieSyncManager.createInstance(this.getContext().getApplicationContext());
		CookieManager cookieManager = CookieManager.getInstance();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			cookieManager.removeSessionCookies(null);
			cookieManager.removeAllCookie();
			cookieManager.flush();
		} else {
			cookieManager.removeSessionCookies(null);
			cookieManager.removeAllCookie();
			CookieSyncManager.getInstance().sync();
		}
		WebStorage.getInstance().deleteAllData(); //清空WebView的localStorage

		new DeleteFileTask().execute(fileTools.getSDCardRoot(), DEFAULT_SD_PATH);
		new DeleteFileTask().execute(fileTools.getSDCardRoot(), APP_CACAHE_DIRNAME);
	}
}
