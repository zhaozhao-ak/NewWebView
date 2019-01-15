package com.rjyx.webviewdemo.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rjyx.webviewdemo.R;
import com.rjyx.webviewdemo.util.NetworkUtils;
import com.rjyx.webviewdemo.widget.MyWebView;

/**
 * Created by rjyx on 2018/12/27.
 */

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";//日志打印
    private static final String APP_CACAHE_DIRNAME = "/rjyx_webcache";//缓存目录

    private MyWebView webView;//浏览器控件
    private ProgressBar progressBar;//进度条的加载

    //私有变量
    //private String url="file:////android_asset/index.html";//首页网页链接
    private String url="http://192.168.8.10:8083/hdis/tablet/layout";//首页网页链接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        //初始化控件
        intView();
        //缓存模式设置
        setWebView();
    }

    @Override
    protected void onResume() {

        // 打开网页
        openWebPage();

        super.onResume();
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
        webView = (MyWebView) findViewById(R.id.webView);
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