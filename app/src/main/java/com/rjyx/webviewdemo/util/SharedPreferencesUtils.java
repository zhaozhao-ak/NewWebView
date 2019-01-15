package com.rjyx.webviewdemo.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Base64;
import android.util.Log;

/**
 * SharedPreferences工具类
 *
 * @author bobby
 *
 */
public class SharedPreferencesUtils {

    private static final String TAG = SharedPreferencesUtils.class
            .getSimpleName();
    Context context;
    String name;

    public SharedPreferencesUtils(Context context, String name) {
        this.context = context;
        this.name = name;
    }

    /**
     * 根据key和预期的value类型获取value的值
     *
     * @param key
     * @param clazz
     * @return
     */
    public <T> T getValue(String key, Class<T> clazz) {
        if (context == null) {
            throw new RuntimeException("请先调用带有context，name参数的构造！");
        }
        SharedPreferences sp = this.context.getSharedPreferences(this.name,
                Context.MODE_PRIVATE);
        return getValue(key, clazz, sp);
    }

    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     */
    @SuppressLint("NewApi")
    public void setObject(String key, Object object) {
        SharedPreferences sp = this.context.getSharedPreferences(this.name,
                Context.MODE_PRIVATE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {

            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(),
                    Base64.DEFAULT));
            Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi")
    public <T> T getObject(String key, Class<T> clazz) {
        SharedPreferences sp = this.context.getSharedPreferences(this.name,
                Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 对于外部不可见的过渡方法
     *
     * @param key
     * @param clazz
     * @param sp
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T getValue(String key, Class<T> clazz, SharedPreferences sp) {

        if (clazz.isAssignableFrom(Integer.class)) {
            return (T) Integer.valueOf(sp.getInt(key, 0));
        } else if (clazz.isAssignableFrom(String.class)) {
            return (T) sp.getString(key, null);
        } else if (clazz.isAssignableFrom(Boolean.class)) {
            return (T) Boolean.valueOf(sp.getBoolean(key, false));
        } else if (clazz.isAssignableFrom(Long.class)) {
            return (T) Long.valueOf(sp.getLong(key, 0L));
        } else if (clazz.isAssignableFrom(Float.class)) {
            return (T) Float.valueOf(sp.getFloat(key, 0L));
        }
        Log.i("TAG", "无法找到" + key + "对应的值");
        return null;
    }

    public <T extends Object> boolean setValue(String key, T value) {
        SharedPreferences sp = this.context.getSharedPreferences(this.name,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (value.getClass().isAssignableFrom(Integer.class)) {
            editor.putInt(key, (Integer) value);
            Log.i(TAG, "传入值的类型为：Integer");
        } else if (value.getClass().isAssignableFrom(String.class)) {
            editor.putString(key, (String) value);
            Log.i(TAG, "传入值的类型为：String");
        } else if (value.getClass().isAssignableFrom(Boolean.class)) {
            editor.putBoolean(key, (Boolean) value);
            Log.i(TAG, "传入值的类型为：Boolean");
        } else if (value.getClass().isAssignableFrom(Long.class)) {
            editor.putLong(key, (Long) value);
            Log.i(TAG, "传入值的类型为：Long");
        } else if (value.getClass().isAssignableFrom(Float.class)) {
            editor.putFloat(key, (Float) value);
            Log.i(TAG, "传入值的类型为：Float");
        }
        return editor.commit();

    }

    public void delete(String key) {
        SharedPreferences sp = this.context.getSharedPreferences(this.name,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

}