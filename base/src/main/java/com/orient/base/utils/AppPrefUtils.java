package com.orient.base.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * SharedPreferences的辅助工具类
 * <p>
 * Author WangJie
 * Created on 2019/8/5.
 */
public class AppPrefUtils {
    private static AppPrefUtils instance;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private AppPrefUtils(String name,Context context) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static AppPrefUtils getInstance(String name,Context context) {
        if (instance == null) {
            instance = new AppPrefUtils(name,context);
        }
        return instance;
    }

    /**
     * 存入Boolean
     */
    public void putBoolean(String key, Boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 获取boolean
     */
    public Boolean getBoolean(String key) {
        return sp.getBoolean(key, true);
    }

    /**
     * 存入字符串
     */
    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 获取字符串
     */
    public String getString(String key) {
        return sp.getString(key, "");
    }

    /**
     * 存入Int类型的数据
     */
    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 获取Int类型的数据
     */
    public int getInt(String key) {
        return sp.getInt(key, 0);
    }

    /**
     * 存入Long
     */
    public void putLong(String key, Long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * 获取Long类型的数据
     */
    public long getLong(String key) {
        return sp.getLong(key, 0);
    }

    /**
     * 存入Set<String>
     */
    public void putStringSet(String key, Set<String> set) {
        editor.putStringSet(key, set);
        editor.commit();
    }

    /**
     * 获取Set<String>
     */
    public Set<String> getStringSet(String key) {
        Set<String> set = new HashSet<>();
        return sp.getStringSet(key, set);
    }

    /**
     * 删除数据
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }
}
