package com.rjyx.webviewdemo.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.rjyx.webviewdemo.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suweiming on 2017/12/28.
 * 一般通用工具
 */

public class CommonUtils {

    // 申请权限
    public static final String[] REQUEST_BASIC_PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.CAMERA,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    /**
     * 一般的回调口
     */
    public interface CommonCallBack {
        void callBack(boolean isSuccess, String msg);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideInput(Activity activity) {
        try {

            //获取当屏幕内容的高度
            int screenHeight = activity.getWindow().getDecorView().getHeight();
            //获取View可见区域的bottom
            Rect rect = new Rect();
            //DecorView即为activity的顶级view
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
            //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
            //选取screenHeight*2/3进行判断
            if ((screenHeight * 2 / 3 > rect.bottom) == false) {
                return;
            }

            InputMethodManager inputmanager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
    }

    /**
     * 生成APP的快捷方式
     */
    public static void createAppShortCut(final Context context, final String iconUrl, final CommonCallBack callBack) {
        // 必须是子线程，放在主线程会崩
        new Thread(new Runnable() {
            @Override
            public void run() {
                // String iconUrl = "http://10.0.0.35:5050/download/icon_test_change.png";
                InputStream ins = null;

                try {
                    ins = new java.net.URL(iconUrl).openStream();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    callBack.callBack(false, e.toString());
                    if (ins != null) {
                        try {
                            ins.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    return;

                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.callBack(false, e.toString());
                    if (ins != null) {
                        try {
                            ins.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    return;
                }

                // 生成bitmap对象
                Bitmap orgBitmap = BitmapFactory.decodeStream(ins);
                OutputStream outStream = null;
                final String iconTempName = "RJYXAppIconTemp.png";

                try {
                    outStream = context.openFileOutput(iconTempName, Context.MODE_PRIVATE);
                    orgBitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.callBack(false, e.toString());
                    return;

                } finally {
                    // 最后都要关闭和回收
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (ins != null) {
                        try {
                            ins.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (orgBitmap != null && !orgBitmap.isRecycled()) {
                        orgBitmap.recycle();
                        orgBitmap = null;
                    }
                }

                // 生成的bitmap
                Bitmap newCreateBitmap = null;
                try {
                    newCreateBitmap = BitmapFactory.decodeStream(context.openFileInput(iconTempName));

                    Intent shortcutIntent = new Intent();
                    shortcutIntent.setClassName(context, context.getClass().getName());
                    shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    Intent addIntent = new Intent();
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
                    // app快捷方式名称
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getResources().getString(R.string.app_name));
                    // app的Icon
                    addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, newCreateBitmap);
                    // 不允许重复创建
                    addIntent.putExtra("duplicate", false);

                    addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
                    context.sendBroadcast(addIntent);

                } catch (Exception e) {
                    e.printStackTrace();
                    // 出错要回收
                    if (newCreateBitmap != null && !newCreateBitmap.isRecycled()) {
                        newCreateBitmap.recycle();
                        newCreateBitmap = null;
                    }
                    callBack.callBack(false, e.toString());
                    return;
                }
            }
        }).start();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}