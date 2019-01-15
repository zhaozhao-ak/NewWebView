package com.rjyx.webviewdemo.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rjyx.webviewdemo.R;
import com.rjyx.webviewdemo.api.FCDeviceApi;
import com.rjyx.webviewdemo.impi.AlbumImpl;
import com.rjyx.webviewdemo.jsbridge.BridgeHandler;
import com.rjyx.webviewdemo.jsbridge.BridgeWebView;
import com.rjyx.webviewdemo.jsbridge.CallBackFunction;
import com.rjyx.webviewdemo.jsbridge.DefaultHandler;
import com.rjyx.webviewdemo.util.NetworkUtils;

/**
 * Created by rjyx on 2018/12/27.
 */

public class BridgeWebViewActivity extends AppCompatActivity {

    private static final String TAG = "BridgeWebViewActivity";//日志打印
    private static final String APP_CACAHE_DIRNAME = "/rjyx_webcache";//缓存目录

    private BridgeWebView webView;//浏览器控件
    private ProgressBar progressBar;//进度条的加载

    //私有变量
    //private String url="file:////android_asset/index.html";//首页网页链接
    private String url="http://192.168.8.10:8083/hdis/tablet/layout";//首页网页链接

    int RESULT_CODE = 0;//浏览器控件的变量
    ValueCallback<Uri> mUploadMessage;//通过webview调用Android的图片或文件选择
    private FCDeviceApi fcDeviceApi;//安卓设备封装的功能
    private Uri result;//资源标识
    private AlbumImpl mAlbumImpl;//相册功能

    /* private SwipeRefreshLayout swipeLayout; */
    public AlbumImpl getmAlbumImpl() {
        return mAlbumImpl;
    }


    public void setmAlbumImpl(AlbumImpl mAlbumImpl) {
        this.mAlbumImpl = mAlbumImpl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_webview);

        //初始化控件
        intView();
        //创建安卓设备封装的功能
        fcDeviceApi = new FCDeviceApi(this);
        //缓存模式设置
        setWebView();
    }

    @Override
    protected void onResume() {

        // 打开网页
        openWebPage();

        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage) {
                return;
            }
            result = intent == null || resultCode != RESULT_OK ? null : intent
                    .getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }

    }

    private void openWebPage(){
        if (NetworkUtils.isNetworkAvailable(this)) {
            webView.loadUrl(url);
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "当前网络未连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void intView() {
        webView = (BridgeWebView) findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        //The_title.setText();
        progressBar.setMax(100);
    }

    /** 加载缓存*/

    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private void setWebView() {

        WebSettings websettings = webView.getSettings();
        //支持脚本，关闭为false
        websettings.setJavaScriptEnabled(true);
        //提高渲染的优先级
        websettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        //设置 缓存模式
        websettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 开启 DOM storage API 功能
        websettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        websettings.setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME;
        //      String cacheDirPath = getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        Log.i(TAG, "cacheDirPath="+cacheDirPath);
        //设置数据库缓存路径
        websettings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        websettings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        websettings.setAppCacheEnabled(true);
        // 设置可以支持缩放
        websettings.setSupportZoom(false);
        // 设置出现缩放工具
        websettings.setBuiltInZoomControls(false);
        // 扩大比例的缩放
        websettings.setUseWideViewPort(false);
        // 自适应屏幕
        websettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        websettings.setLoadWithOverviewMode(true);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.requestFocus();
        webView.setDefaultHandler(new DefaultHandler());

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.i(TAG, "加载进度 ：" + newProgress);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {

                    progressBar.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                // TODO Auto-generated method stub
                super.onReceivedTitle(view, title);
                Log.d("ANDROID_LAB", "TITLE=" + title);
                //The_title.setText(title);
            }
        });

        webView.registerHandler("FCDeviceAPI", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "传入的数据：" + data);
                fcDeviceApi.manage(data, function);
            }
        });

    }

    public void refreMyWebView() {
        if(NetworkUtils.isNetworkAvailable(this)){
            progressBar.setVisibility(View.VISIBLE);
            webView.loadUrl(url);
        }else{
            showToast("网络未连接");
        }

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
