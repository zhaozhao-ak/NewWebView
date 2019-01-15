package com.rjyx.webviewdemo;

import android.app.Application;
import android.util.Log;

import com.rjyx.webviewdemo.x5.X5WebViewPool;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by rjyx on 2018/12/27.
 */

public class H5Application extends Application {

    private static H5Application mInstance = null;// 单例

    /**
     * 单例获取
     */
    public static synchronized H5Application getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        synchronized (this) {
            if (mInstance == null) {
                mInstance = this;
            }
        }

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);

        X5WebViewPool.getInstance().init();
    }

}