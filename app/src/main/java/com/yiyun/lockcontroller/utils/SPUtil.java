package com.yiyun.lockcontroller.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.yiyun.lockcontroller.App;
import com.yiyun.lockcontroller.R;

/**
 * Created by layo on 2017/5/5.
 */

public class SPUtil {
    // TODO: 2018-2-1 设置加载密文和读取密文的功能 
    private final SharedPreferences mPreferences;

    private SPUtil() {
        Context context = App.getAppContext();
        String spName = context.getResources().getString(R.string.app_name);
        mPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
    }

    public static SPUtil getInstance() {
        return SingletonHolder.instance;
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void putInt(String key, int value) {
        mPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return mPreferences.getInt(key, defaultValue);
    }

    public int getInt(String key) {
        return mPreferences.getInt(key, 0);
    }

    /**
     * SP中读取String,和存String
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code ""}
     */
    public String getString(@NonNull final String key) {
        return getString(key, "");
    }

    public void putString(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    /**
     * SP中读取String
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public String getString(@NonNull final String key, @NonNull final String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }

    /**
     * SP中读取long
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public long getLong(@NonNull final String key) {
        return getLong(key, -1L);
    }

    /**
     * SP中读取long
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public long getLong(@NonNull final String key, final long defaultValue) {
        return mPreferences.getLong(key, defaultValue);
    }

    /**
     * SP中读取float
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值-1
     */
    public float getFloat(@NonNull final String key) {
        return getFloat(key, -1f);
    }

    /**
     * SP中读取float
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public float getFloat(@NonNull final String key, final float defaultValue) {
        return mPreferences.getFloat(key, defaultValue);
    }

    /**
     * SP中读取boolean
     *
     * @param key 键
     * @return 存在返回对应值，不存在返回默认值{@code false}
     */
    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }

    public void putBoolean(@NonNull String key, boolean value) {
        mPreferences.edit().putBoolean(key, value).apply();
    }

    /**
     * SP中读取boolean
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 存在返回对应值，不存在返回默认值{@code defaultValue}
     */
    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    /**
     * SP中移除该key
     *
     * @param key 键
     */
    public void remove(@NonNull final String key) {
        mPreferences.edit().remove(key).apply();
    }

    /**
     * SP中清除所有数据
     */
    public void clear() {
        mPreferences.edit().clear().apply();
    }

    private static class SingletonHolder {
        private static final SPUtil instance = new SPUtil();
    }
}
