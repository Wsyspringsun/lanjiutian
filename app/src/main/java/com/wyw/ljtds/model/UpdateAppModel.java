package com.wyw.ljtds.model;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class UpdateAppModel extends BaseModel {
    private String android;
    private String android_update_message;
    private String android_download_link;
    private String android_force_update;

    public String getAndroid() {
        return android;
    }

    public void setAndroid(String android) {
        this.android = android;
    }

    public String getAndroid_update_message() {
        return android_update_message;
    }

    public void setAndroid_update_message(String android_update_message) {
        this.android_update_message = android_update_message;
    }

    public String getAndroid_download_link() {
        return android_download_link;
    }

    public void setAndroid_download_link(String android_download_link) {
        this.android_download_link = android_download_link;
    }

    public String getAndroid_force_update() {
        return android_force_update;
    }

    public void setAndroid_force_update(String android_force_update) {
        this.android_force_update = android_force_update;
    }
}
