package com.rjyx.webviewdemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rjyx.webviewdemo.ui.BridgeWebViewActivity;
import com.rjyx.webviewdemo.ui.WebViewActivity;
import com.rjyx.webviewdemo.ui.X5BridgeWebViewActivity;
import com.rjyx.webviewdemo.ui.X5WebViewActivity;
import com.rjyx.webviewdemo.util.CommonUtils;
import com.rjyx.webviewdemo.util.DeleteFileTask;
import com.rjyx.webviewdemo.util.FileUtils;
import com.rjyx.webviewdemo.util.RuntimePermissionUtil;
import com.rjyx.webviewdemo.util.WebCacheManager;

public class MainActivity extends AppCompatActivity {

    private boolean hasPermission = true;// 是否有权限
    private final int PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数
    private final int PERMISSIONS_GRANTED = 0; // 权限授权
    private final int PERMISSIONS_DENIED = 1; // 权限拒绝

    private long exitTime = 0;//退出二次时间

    private Button btn_webview;
    private Button btn_bridge_webview;
    private Button btn_xfive_webview;
    private Button btn_xfive_bridge_webview;
    private Button bit_clearcache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化控件
        intView();

        // 请求权限
        requestPermission();
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        // 申请权限
        RuntimePermissionUtil permissionUtil = new RuntimePermissionUtil(this);
        hasPermission = true;
        if (permissionUtil.lacksPermissions(CommonUtils.REQUEST_BASIC_PERMISSIONS)) {
            if (hasPermission) {
                // 请求权限
                ActivityCompat.requestPermissions(this, CommonUtils.REQUEST_BASIC_PERMISSIONS, PERMISSION_REQUEST_CODE);
            } else {
                hasPermission = true;
            }

        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && hasAllPermissionsGranted(grantResults)) {
            hasPermission = true;
            allPermissionsGranted();
        } else {
            hasPermission = false;
        }
    }

    /**
     * 含有全部的权限
     */
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 全部权限均已获取
     */
    private void allPermissionsGranted() {
        setResult(PERMISSIONS_GRANTED);
    }


    private void intView() {

        btn_webview = (Button) findViewById(R.id.btn_webview);
        btn_bridge_webview = (Button) findViewById(R.id.btn_bridge_webview);
        btn_xfive_webview = (Button) findViewById(R.id.btn_xfive_webview);
        btn_xfive_bridge_webview = (Button)findViewById(R.id.btn_xfive_bridge_webview);
        bit_clearcache = (Button) findViewById(R.id.bit_clearcache);

        btn_webview.setOnClickListener(buttonOnClickListener);
        btn_bridge_webview.setOnClickListener(buttonOnClickListener);
        btn_xfive_webview.setOnClickListener(buttonOnClickListener);
        btn_xfive_bridge_webview.setOnClickListener(buttonOnClickListener);
        bit_clearcache.setOnClickListener(buttonOnClickListener);
    }

    private View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btn_webview: {
                    intent = new Intent(MainActivity.this, WebViewActivity.class);
                    startActivity(intent);
                }
                break;
                case R.id.btn_bridge_webview: {
                    intent = new Intent(MainActivity.this, BridgeWebViewActivity.class);
                    startActivity(intent);
                }
                break;
                case R.id.btn_xfive_bridge_webview: {
                    intent = new Intent(MainActivity.this, X5BridgeWebViewActivity.class);
                    startActivity(intent);
                }
                break;
                case R.id.btn_xfive_webview: {
                    intent = new Intent(MainActivity.this, X5WebViewActivity.class);
                    startActivity(intent);
                }
                break;
                default:{
                    WebCacheManager webCacheManager = new WebCacheManager(MainActivity.this);
                    webCacheManager.ClearCaches();
                }
            }
        }
    };

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            exit();

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出APP",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }
}
