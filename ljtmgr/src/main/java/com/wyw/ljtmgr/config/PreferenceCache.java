package com.wyw.ljtmgr.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 缓存应用程序的配置信息和必要的业务数据
 *
 * @author lenovo
 */
public class PreferenceCache {

    public static final String PF_ACTIVE = "active"; // token
    public static final String PF_TOKEN = "token"; // token
    public static final String PF_VERSION = "";//版本号
    public static final String PF_USER = "user";// 保存上次登录的用户名
    public static final String PF_COURIORDATA = "PF_COURIORDATA";

    private static SharedPreferences getSharedPreferences() {
        MyApplication app = (MyApplication) MyApplication.getAppContext();
        return app.getSharedPreferences("LJT", Context.MODE_PRIVATE);
    }

    public static void putToken(String token) {
        SharedPreferences pref = getSharedPreferences();

        Editor editor = pref.edit();
        editor.putString(PF_TOKEN, token);
        editor.commit();
    }

    public static String getToken() {
        return getSharedPreferences().getString(PF_TOKEN, "");
    }

    public static void putUser(String userJson) {
        SharedPreferences pref = getSharedPreferences();
        Editor editor = pref.edit();
        editor.putString(PF_USER, userJson);
        editor.commit();
    }

    public static String getUser() {
        return getSharedPreferences().getString(PF_USER, "");
    }


    public static String getVersion() {
        return getSharedPreferences().getString(PF_VERSION, "");
    }

    /*
    存放快递信息
     */
    public static void putCouriorData(String couriorData) {
        SharedPreferences pref = getSharedPreferences();
        Editor editor = pref.edit();
        editor.putString(PF_COURIORDATA, couriorData);
        editor.commit();
    }

    /*
    获取快递信息
     */
    public static String getCouriorData() {
        return getSharedPreferences().getString(PF_COURIORDATA, "");
    }

    public static void putActived(Boolean isActive) {
        SharedPreferences pref = getSharedPreferences();

        Editor editor = pref.edit();
        editor.putBoolean(PF_ACTIVE, isActive);
        editor.commit();
    }

    public static Boolean getActived() {
        return getSharedPreferences().getBoolean(PF_ACTIVE, false);
    }
}
