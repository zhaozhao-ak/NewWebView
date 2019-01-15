package com.rjyx.webviewdemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rjyx.webviewdemo.R;
import com.rjyx.webviewdemo.util.NetworkUtils;
import com.rjyx.webviewdemo.x5.X5WebView;
import com.rjyx.webviewdemo.x5.X5WebViewPool;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by rjyx on 2018/12/27.
 */

public class X5WebViewActivity extends AppCompatActivity {

    private static final String TAG = "X5WebViewActivity";//日志打印

    protected FrameLayout mWebviewRoot;

    private X5WebView mWebView;//浏览器控件
    private ProgressBar progressBar;//进度条的加载

    //私有变量
    //private String url="file:////android_asset/index.html";//首页网页链接
    private String url="http://192.168.8.241:8083/hdis/tablet/layout";//首页网页链接

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfive_webview);

        //初始化控件
        intView();
    }

    @Override
    protected void onResume() {

        // 打开网页
        openWebPage();

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        X5WebViewPool.getInstance().removeWebView(mWebviewRoot,mWebView);
    }

    private void openWebPage(){
        if (NetworkUtils.isNetworkAvailable(this)) {

            mWebView.loadUrl(url);

        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "当前网络未连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void intView() {
        mWebviewRoot = (FrameLayout) findViewById(R.id.webview_root_fl);
        mWebView = X5WebViewPool.getInstance().getWebView();
        mWebviewRoot.addView(mWebView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        //The_title.setText();
        progressBar.setMax(100);

        // 设置浏览器
        setWebView();
    }

    /** 设置浏览器 */
    private void setWebView() {

        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.requestFocus();
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
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
            mWebView.loadUrl(url);
        }else{
            showToast("网络未连接");
        }

    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
