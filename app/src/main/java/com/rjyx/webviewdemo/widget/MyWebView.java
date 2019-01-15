package com.rjyx.webviewdemo.widget;

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
import android.webkit.WebViewClient;

import com.rjyx.webviewdemo.util.WebCacheManager;
import com.rjyx.webviewdemo.util.FileUtils;
import com.rjyx.webviewdemo.util.HttpUtil;
import com.rjyx.webviewdemo.util.MD5Util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rjyx on 2018/12/27.
 */

public class MyWebView extends WebView {

    private final String TAG = "MyWebView";


    private WebCacheManager webCacheManager = null;
    private FileUtils fileTools = new FileUtils();

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        webCacheManager = new WebCacheManager(context);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        webCacheManager = new WebCacheManager(context);
        init();
    }

    public MyWebView(Context context) {
        super(context);
        webCacheManager = new WebCacheManager(context);
        init();
    }


    private void init() {
        this.setVerticalScrollBarEnabled(false);
        this.setHorizontalScrollBarEnabled(false);
        this.getSettings().setJavaScriptEnabled(true);
		/*
		 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		 * WebView.setWebContentsDebuggingEnabled(true); }
		 */
        this.setWebViewClient(new MyWebView.MyWebViewClient());
    }

    class MyWebViewClient extends WebViewClient {
        /**
         * 防止加载网页时调起系统浏览器
         */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
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
                    Log.i(TAG, "WebCacheManager" + items[0] + "写入sd卡:" + cacheFile);
                    //保存数据
                    fileTools.write2SD(webCacheManager.DEFAULT_SD_PATH, cacheFile, data);
                    //将数据返回浏览器
                    InputStream inputStream = fileTools.streamFromSD(webCacheManager.DEFAULT_SD_PATH, cacheFile);
                    return new WebResourceResponse(mimeType, "utf-8", inputStream);
                }
            }
            //如果请求不是文件类型，让浏览器自行处理
            Log.i(TAG, "请求网络:" + url);
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
                    Log.i(TAG, "WebCacheManager" + items[0] + "写入sd卡:" + cacheFile);
                    //保存数据
                    fileTools.write2SD(webCacheManager.DEFAULT_SD_PATH, cacheFile, data);
                    //将数据返回浏览器
                    InputStream inputStream = fileTools.streamFromSD(webCacheManager.DEFAULT_SD_PATH, cacheFile);
                    return new WebResourceResponse(mimeType, "utf-8", inputStream);
                }
            }
            //如果请求不是文件类型，让浏览器自行处理
            Log.i(TAG, "请求网络:" + url);

            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }
}