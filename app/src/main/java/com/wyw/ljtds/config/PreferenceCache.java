package com.wyw.ljtds.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * 缓存应用程序的配置信息和必要的业务数据
 *
 * @author lenovo
 */
public class PreferenceCache {

    public static final String PF_TOKEN = "token"; // token
    public static final String PF_GUIDE_PAGE = "guide_page";//引导页
    public static final String PF_VERSION = "";//版本号
    public static final String PF_AUTO_LOGIN = "auton_login"; // 自动登录
    public static final String PF_PHONE_NUM = "phone_number"; // 手机号
    public static final String PF_USERNAME = "username";// 保存上次登录的用户名
    public static final String PF_ADDRESS_PHONE = "address_phone";//地址 手机号
    public static final String PF_ADDRESS_NAME = "address_name";//地址 名字
    public static final String PF_ADDRESS_DETAIL = "address_detail";//地址 详细
    public static final String PF_ADDRESS_ID = "address_id";//地址 id

    private static SharedPreferences getSharedPreferences() {
        MyApplication app = (MyApplication) MyApplication.getAppContext();
        return app.getSharedPreferences( "LJT", Context.MODE_PRIVATE );
    }

    public static void putToken(String token) {
        SharedPreferences pref = getSharedPreferences();

        Editor editor = pref.edit();
        editor.putString( PF_TOKEN, token );
        editor.commit();
    }

    public static String getToken() {
        return getSharedPreferences().getString( PF_TOKEN, "" );
    }

    public static void putUsername(String username) {
        SharedPreferences pref = getSharedPreferences();

        Editor editor = pref.edit();
        editor.putString( PF_USERNAME, username );
        editor.commit();
    }

    public static String getUsername() {
        return getSharedPreferences().getString( PF_USERNAME, "" );
    }

    public static void putAutoLogin(boolean isAutonLogin) {
        SharedPreferences pref = getSharedPreferences();

        Editor editor = pref.edit();
        editor.putBoolean( PF_AUTO_LOGIN, isAutonLogin );
        editor.commit();
    }

    public static boolean isAutoLogin() {
        return getSharedPreferences().getBoolean( PF_AUTO_LOGIN, false );
    }

    public static void putPhoneNum(String phoneNum) {
        SharedPreferences pref = getSharedPreferences();
        Editor editor = pref.edit();
        editor.putString( PF_PHONE_NUM, phoneNum );
        editor.commit();
    }

    public static String getPhoneNum() {
        return getSharedPreferences().getString( PF_PHONE_NUM, "" );
    }

    public static void putGuidePage(boolean guidePage) {
        SharedPreferences pref = getSharedPreferences();
        Editor editor = pref.edit();
        editor.putBoolean( PF_GUIDE_PAGE, guidePage );
        editor.commit();
    }

    public static boolean getGuidePage() {
        return getSharedPreferences().getBoolean( PF_GUIDE_PAGE, false );
    }

    public static void putVersion(String version) {
        SharedPreferences pref = getSharedPreferences();
        Editor editor = pref.edit();
        editor.putString( PF_VERSION, version );
        editor.commit();
    }

    public static String getVersion() {
        return getSharedPreferences().getString( PF_VERSION, "" );
    }

    public static void putAddressName(String address_name) {
        SharedPreferences pref = getSharedPreferences();
        Editor editor = pref.edit();
        editor.putString( PF_ADDRESS_NAME, address_name );
        editor.commit();
    }

    public static String getAddressName() {
        return getSharedPreferences().getString( PF_ADDRESS_NAME, "" );
    }

    public static void putAddressPhone(String address_phone) {
        SharedPreferences pref = getSharedPreferences();
        Editor editor = pref.edit();
        editor.putString( PF_ADDRESS_PHONE, address_phone );
        editor.commit();
    }

    public static String getAddressPhone() {
        return getSharedPreferences().getString( PF_ADDRESS_PHONE, "" );
    }

    public static void putAddressDetail(String address_detail) {
        SharedPreferences pref = getSharedPreferences();
        Editor editor = pref.edit();
        editor.putString( PF_ADDRESS_DETAIL, address_detail );
        editor.commit();
    }

    public static String getAddressDetail() {
        return getSharedPreferences().getString( PF_ADDRESS_DETAIL, "" );
    }

    public static void putAddressId(int id) {
        SharedPreferences pref = getSharedPreferences();
        Editor editor = pref.edit();
        editor.putInt( PF_ADDRESS_ID, id );
        editor.commit();
    }

    public static int getAddressId() {
        return getSharedPreferences().getInt( PF_ADDRESS_ID, 0 );
    }
}
