package com.rjyx.webviewdemo.jsbridge;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import java.net.URLEncoder;
import android.webkit.WebViewClient;

import com.rjyx.webviewdemo.util.FileUtils;
import com.rjyx.webviewdemo.util.HttpUtil;
import com.rjyx.webviewdemo.util.MD5Util;
import com.rjyx.webviewdemo.util.WebCacheManager;

@SuppressLint("SetJavaScriptEnabled")
public class BridgeWebView extends WebView implements WebViewJavascriptBridge{

	private final String TAG = "BridgeWebView";

	public static final String toLoadJs = "WebViewJavascriptBridge.js";
	Map<String, CallBackFunction> responseCallbacks = new HashMap<String, CallBackFunction>();
	Map<String, BridgeHandler> messageHandlers = new HashMap<String, BridgeHandler>();
	BridgeHandler defaultHandler = new DefaultHandler();

	private List<Message> startupMessage = new ArrayList<Message>();

	public List<Message> getStartupMessage() {
		return startupMessage;
	}

	public void setStartupMessage(List<Message> startupMessage) {
		this.startupMessage = startupMessage;
	}

	private long uniqueId = 0;
	
	private OnScrollChangedCallback mOnScrollChangedCallback;
	
	private WebCacheManager webCacheManager = null;
	private FileUtils fileTools = new FileUtils();

	public BridgeWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		webCacheManager = new WebCacheManager(context);
		init();
	}

	public BridgeWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		webCacheManager = new WebCacheManager(context);
		init();
	}

	public BridgeWebView(Context context) {
		super(context);
		webCacheManager = new WebCacheManager(context);
		init();
	}

	/**
	 *
	 * @param handler
	 *            default handler,handle messages send by js without assigned handler name,
     *            if js message has handler name, it will be handled by named handlers registered by native
	 */
	public void setDefaultHandler(BridgeHandler handler) {
       this.defaultHandler = handler;
	}

    private void init() {
		this.setVerticalScrollBarEnabled(false);
		this.setHorizontalScrollBarEnabled(false);
		this.getSettings().setJavaScriptEnabled(true);
		/*
		 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		 * WebView.setWebContentsDebuggingEnabled(true); }
		 */
		this.setWebViewClient(new BridgeWebViewClient());
	}

    /**
     * 获取到CallBackFunction data执行调用并且从数据集移除
     * @param url
     */
	void handlerReturnData(String url) {
		String functionName = BridgeUtil.getFunctionFromReturnUrl(url);
		CallBackFunction f = responseCallbacks.get(functionName);
		String data = BridgeUtil.getDataFromReturnUrl(url);
		if (f != null) {
			f.onCallBack(data);
			responseCallbacks.remove(functionName);
			return;
		}
	}

	class BridgeWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			try {
				url = URLDecoder.decode(url, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();

				Log.e("BridgeWebViewClient", e.getMessage());
			}
			if (url.startsWith(BridgeUtil.YY_RETURN_DATA)) { // 如果是返回数据
				handlerReturnData(url);
				return true;
			} else if (url.startsWith(BridgeUtil.YY_OVERRIDE_SCHEMA)) { //
				flushMessageQueue();
				return true;
			} else {
				return super.shouldOverrideUrlLoading(view, url);
			}
		}

		// TODO shouldInterceptRequest
		@SuppressLint("NewApi")
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			//优先从assets资源目录读取文件
			//其次检查文件是否缓存到了fcyd_cache目录
			WebResourceResponse resourceResponse = webCacheManager.readWebResourceResponse(url);
			if (resourceResponse != null) {
				return resourceResponse;
			}
			
			//如果第一次请求未读取到本地，则自主请求网络并保存到sd卡，同时返回给浏览器
			if (webCacheManager.thisURLRequestlsFile(url)) {
				int startIndex = url.lastIndexOf("/");
				String unsureTarget = url.substring(startIndex + 1);
				String[] items = unsureTarget.split("\\?");
				//请求网络得到byte数组
				byte[] data = HttpUtil.doGetBytes(url);
				if (data != null && data.length>0) {
					//将url按“？”拆分,左侧是文件链接
					String[] urlItems = url.split("\\?");
					int startIndex2 = items[0].lastIndexOf(".");
					//得到文件的扩展名
					String extension = items[0].substring(startIndex2 + 1);
					//得到文件的MIME类型
					String mimeType = webCacheManager.getMimeTypes().get(extension);
					//将文件的url去除参数后，进行MD5加密。得到32位长度的名称
					String cacheFile = MD5Util.MD5(urlItems[0]) + "." + extension;
					Log.i("WebCacheManager", items[0] + "写入sd卡:" + cacheFile);
					//保存数据
					fileTools.write2SD(webCacheManager.DEFAULT_SD_PATH, cacheFile, data);
					//将数据返回浏览器
					InputStream inputStream = fileTools.streamFromSD(webCacheManager.DEFAULT_SD_PATH, cacheFile);				
					return new WebResourceResponse(mimeType, "utf-8", inputStream);
				}
			}
			//如果请求不是文件类型，让浏览器自行处理
			Log.i("shouldInterceptRequest url", "请求网络:" + url);
			return super.shouldInterceptRequest(view, url);
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
			String url = request.getUrl().toString();
			//优先从assets资源目录读取文件
			//其次检查文件是否缓存到了fcyd_cache目录
			WebResourceResponse resourceResponse = webCacheManager.readWebResourceResponse(url);
			if (resourceResponse != null) {
				return resourceResponse;
			}

			//如果第一次请求未读取到本地，则自主请求网络并保存到sd卡，同时返回给浏览器
			if (webCacheManager.thisURLRequestlsFile(url)) {
				int startIndex = url.lastIndexOf("/");
				String unsureTarget = url.substring(startIndex + 1);
				String[] items = unsureTarget.split("\\?");
				//请求网络得到byte数组
				byte[] data = HttpUtil.doGetBytes(url);
				if (data != null && data.length>0) {
					//将url按“？”拆分,左侧是文件链接
					String[] urlItems = url.split("\\?");
					int startIndex2 = items[0].lastIndexOf(".");
					//得到文件的扩展名
					String extension = items[0].substring(startIndex2 + 1);
					//得到文件的MIME类型
					String mimeType = webCacheManager.getMimeTypes().get(extension);
					//将文件的url去除参数后，进行MD5加密。得到32位长度的名称
					String cacheFile = MD5Util.MD5(urlItems[0]) + "." + extension;
					Log.i("WebCacheManager", items[0] + "写入sd卡:" + cacheFile);
					//保存数据
					fileTools.write2SD(webCacheManager.DEFAULT_SD_PATH, cacheFile, data);
					//将数据返回浏览器
					InputStream inputStream = fileTools.streamFromSD(webCacheManager.DEFAULT_SD_PATH, cacheFile);
					return new WebResourceResponse(mimeType, "utf-8", inputStream);
				}
			}
			//如果请求不是文件类型，让浏览器自行处理
			Log.i("shouldInterceptRequest request", "请求网络:" + url);

			return super.shouldInterceptRequest(view, request);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

			if (toLoadJs != null) {
				BridgeUtil.webViewLoadLocalJs(view, toLoadJs);
			}

			//
			if (startupMessage != null) {
				for (Message m : startupMessage) {
					dispatchMessage(m);
				}
				startupMessage = null;
			}
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
	}

	@Override
	public void send(String data) {
		send(data, null);
	}

	@Override
	public void send(String data, CallBackFunction responseCallback) {
		doSend(null, data, responseCallback);
	}

    /**
     * 保存message到消息队列
     * @param handlerName handlerName
     * @param data data
     * @param responseCallback CallBackFunction
     */
	private void doSend(String handlerName, String data, CallBackFunction responseCallback) {
		Message m = new Message();
		if (!TextUtils.isEmpty(data)) {
			m.setData(data);
		}
		if (responseCallback != null) {
			String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT, ++uniqueId + (BridgeUtil.UNDERLINE_STR + SystemClock.currentThreadTimeMillis()));
			responseCallbacks.put(callbackStr, responseCallback);
			m.setCallbackId(callbackStr);
		}
		if (!TextUtils.isEmpty(handlerName)) {
			m.setHandlerName(handlerName);
		}
		queueMessage(m);
	}

    /**
     * list<message> != null 添加到消息集合否则分发消息
     * @param m Message
     */
	private void queueMessage(Message m) {
		if (startupMessage != null) {
			startupMessage.add(m);
		} else {
			dispatchMessage(m);
		}
	}

    /**
     * 分发message 必须在主线程才分发成功
     * @param m Message
     */
	void dispatchMessage(Message m) {
        String messageJson = m.toJson();
        //escape special characters for json string  为json字符串转义特殊字符
        messageJson = messageJson.replaceAll("(\\\\)([^utrn])", "\\\\\\\\$1$2");
        messageJson = messageJson.replaceAll("(?<=[^\\\\])(\")", "\\\\\"");
		messageJson = messageJson.replaceAll("(?<=[^\\\\])(\')", "\\\\\'");
		messageJson = messageJson.replaceAll("%7B", URLEncoder.encode("%7B"));
		messageJson = messageJson.replaceAll("%7D", URLEncoder.encode("%7D"));
		messageJson = messageJson.replaceAll("%22", URLEncoder.encode("%22"));
        String javascriptCommand = String.format(BridgeUtil.JS_HANDLE_MESSAGE_FROM_JAVA, messageJson);
        // 必须要找主线程才会将数据传递出去 --- 划重点
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            this.loadUrl(javascriptCommand);
        }
    }

    /**
     * 刷新消息队列
     */
	void flushMessageQueue() {
		if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
			loadUrl(BridgeUtil.JS_FETCH_QUEUE_FROM_JAVA, new CallBackFunction() {

				@Override
				public void onCallBack(String data) {
					// deserializeMessage 反序列化消息
					List<Message> list = null;
					try {
						list = Message.toArrayList(data);
					} catch (Exception e) {
                        e.printStackTrace();
						return;
					}
					if (list == null || list.size() == 0) {
						return;
					}
					for (int i = 0; i < list.size(); i++) {
						Message m = list.get(i);
						String responseId = m.getResponseId();
						// 是否是response  CallBackFunction
						if (!TextUtils.isEmpty(responseId)) {
							CallBackFunction function = responseCallbacks.get(responseId);
							String responseData = m.getResponseData();
							function.onCallBack(responseData);
							responseCallbacks.remove(responseId);
						} else {
							CallBackFunction responseFunction = null;
							// if had callbackId 如果有回调Id
							final String callbackId = m.getCallbackId();
							if (!TextUtils.isEmpty(callbackId)) {
								responseFunction = new CallBackFunction() {
									@Override
									public void onCallBack(String data) {
										Message responseMsg = new Message();
										responseMsg.setResponseId(callbackId);
										responseMsg.setResponseData(data);
										queueMessage(responseMsg);
									}
								};
							} else {
								responseFunction = new CallBackFunction() {
									@Override
									public void onCallBack(String data) {
										// do nothing
									}
								};
							}
							// BridgeHandler执行
							BridgeHandler handler;
							if (!TextUtils.isEmpty(m.getHandlerName())) {
								handler = messageHandlers.get(m.getHandlerName());
							} else {
								handler = defaultHandler;
							}
							if (handler != null){
								handler.handler(m.getData(), responseFunction);
							}
						}
					}
				}
			});
		}
	}


	public void loadUrl(String jsUrl, CallBackFunction returnCallback) {
		this.loadUrl(jsUrl);
        // 添加至 Map<String, CallBackFunction>
		responseCallbacks.put(BridgeUtil.parseFunctionName(jsUrl), returnCallback);
	}

	/**
	 * register handler,so that javascript can call it
	 * 注册处理程序,以便javascript调用它
	 * @param handlerName handlerName
	 * @param handler BridgeHandler
	 */
	public void registerHandler(String handlerName, BridgeHandler handler) {
		if (handler != null) {
            // 添加至 Map<String, BridgeHandler>
			messageHandlers.put(handlerName, handler);
		}
	}

	/**
	 * unregister handler
	 *
	 * @param handlerName
	 */
	public void unregisterHandler(String handlerName) {
		if (handlerName != null) {
			messageHandlers.remove(handlerName);
		}
	}
	public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
		mOnScrollChangedCallback = onScrollChangedCallback;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollChangedCallback != null) {
			mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
		}
	}

	public static interface OnScrollChangedCallback {
		public void onScroll(int dx, int dy);
	}

	/**
	 * call javascript registered handler
	 * 调用javascript处理程序注册
     * @param handlerName handlerName
	 * @param data data
	 * @param callBack CallBackFunction
	 */
	public void callHandler(String handlerName, String data, CallBackFunction callBack) {
        doSend(handlerName, data, callBack);
	}

}
