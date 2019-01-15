package com.rjyx.webviewdemo.util;

import android.content.Context;

public class SharedPrefsUtil {

    /**
     * 存储数据(Long)
     */
    public static void putLongValue(Context context,String file_name, String key, long value) {
        context.getSharedPreferences(file_name, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    /**
     * 存储数据(Int)
     */
    public static void putIntValue(Context context,String file_name, String key, int value) {
        context.getSharedPreferences(file_name, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    /**
     * 存储数据(String)
     */
    public static void putStringValue(Context context,String file_name, String key, String value) {
        context.getSharedPreferences(file_name, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    /**
     * 存储数据(boolean)
     */
    public static void putBooleanValue(Context context,String file_name, String key,
                                       boolean value) {
        context.getSharedPreferences(file_name, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    /**
     * 取出数据(Long)
     */
    public static long getLongValue(Context context,String file_name, String key, long defValue) {
        return context.getSharedPreferences(file_name,Context.MODE_PRIVATE).getLong(key, defValue);
    }

    /**
     * 取出数据(int)
     */
    public static int getIntValue(Context context,String file_name, String key, int defValue) {
        return context.getSharedPreferences(file_name,Context.MODE_PRIVATE).getInt(key, defValue);
    }

    /**
     * 取出数据(boolean)
     */
    public static boolean getBooleanValue(Context context,String file_name, String key,
                                          boolean defValue) {
        return context.getSharedPreferences(file_name,Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    /**
     * 取出数据(String)
     */
    public static String getStringValue(Context context,String file_name, String key,
                                        String defValue) {
        return context.getSharedPreferences(file_name,Context.MODE_PRIVATE).getString(key, defValue);
    }

    /**
     * 清空所有数据
     */
    public static void clear(Context context,String file_name) {
        context.getSharedPreferences(file_name, Context.MODE_PRIVATE).edit().clear().commit();
    }

    /**
     * 移除指定数据
     */
    public static void remove(Context context,String file_name, String key) {
        context.getSharedPreferences(file_name, Context.MODE_PRIVATE).edit().remove(key).commit();
    }
}
