package com.wyw.ljtds.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.utils.StringUtils;

/**
 * Created by Administrator on 2017/3/12 0012.
 */

public class AddressModel extends BaseModel implements Parcelable {
    //地址的唯一id
    private int ADDRESS_ID;
    //姓名
    private String CONSIGNEE_NAME;
    //手机号
    private String CONSIGNEE_MOBILE;
    //邮编
    private String CONSIGNEE_ZIP_CODE = "048000";
    //详细地址
    private String CONSIGNEE_ADDRESS;
    //默认标识
    private String DEFAULT_FLG;
    //删除标识
    private String DEL_FLG;
    private String PROVINCE;
    private String CONSIGNEE_PROVINCE;
    //市
    private String CITY;
    private String CONSIGNEE_CITY;

    private String CONSIGNEE_COUNTY;
    private String COUNTY = "";
    // 地理位置 格式：市辖区文景苑
    private String LOCATION;
    // 地理位置 格式：市辖区文景苑|112.84387|35.489365
    private String ADDRESS_LOCATION;
    private String LAT;
    private String LNG;

    public String getLAT() {
        return LAT;
    }

    public void setLAT(String LAT) {
        this.LAT = LAT;
    }

    public String getLNG() {
        return LNG;
    }

    public void setLNG(String LNG) {
        this.LNG = LNG;
    }

    public String getADDRESS_LOCATION() {
        return ADDRESS_LOCATION;
    }

    public void setADDRESS_LOCATION(String ADDRESS_LOCATION) {
        this.ADDRESS_LOCATION = ADDRESS_LOCATION;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }


    public int getADDRESS_ID() {
        return ADDRESS_ID;
    }

    public void setADDRESS_ID(int ADDRESS_ID) {
        this.ADDRESS_ID = ADDRESS_ID;
    }

    public String getCONSIGNEE_NAME() {
        return CONSIGNEE_NAME;
    }

    public void setCONSIGNEE_NAME(String CONSIGNEE_NAME) {
        this.CONSIGNEE_NAME = CONSIGNEE_NAME;
    }

    public String getCONSIGNEE_MOBILE() {
        return CONSIGNEE_MOBILE;
    }

    public void setCONSIGNEE_MOBILE(String CONSIGNEE_MOBILE) {
        this.CONSIGNEE_MOBILE = CONSIGNEE_MOBILE;
    }

    public String getCONSIGNEE_ZIP_CODE() {
        return CONSIGNEE_ZIP_CODE;
    }

    public void setCONSIGNEE_ZIP_CODE(String CONSIGNEE_ZIP_CODE) {
        this.CONSIGNEE_ZIP_CODE = CONSIGNEE_ZIP_CODE;
    }

    public String getCONSIGNEE_ADDRESS() {
        return CONSIGNEE_ADDRESS;
    }

    public void setCONSIGNEE_ADDRESS(String CONSIGNEE_ADDRESS) {
        this.CONSIGNEE_ADDRESS = CONSIGNEE_ADDRESS;
    }

    public String getDEFAULT_FLG() {
        return DEFAULT_FLG;
    }

    public void setDEFAULT_FLG(String DEFAULT_FLG) {
        this.DEFAULT_FLG = DEFAULT_FLG;
    }

    public String getDEL_FLG() {
        return DEL_FLG;
    }

    public void setDEL_FLG(String DEL_FLG) {
        this.DEL_FLG = DEL_FLG;
    }

    public String getPROVINCE() {
        return PROVINCE;
    }

    public void setPROVINCE(String PROVINCE) {
        this.PROVINCE = PROVINCE;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ADDRESS_ID);
        dest.writeString(this.CONSIGNEE_NAME);
        dest.writeString(this.CONSIGNEE_MOBILE);
        dest.writeString(this.CONSIGNEE_ZIP_CODE);
        dest.writeString(this.CONSIGNEE_ADDRESS);
        dest.writeString(this.DEFAULT_FLG);
        dest.writeString(this.DEL_FLG);
        dest.writeString(this.PROVINCE);
        dest.writeString(this.CITY);
        dest.writeString(this.LOCATION);
        dest.writeString(this.ADDRESS_LOCATION);
    }

    public AddressModel() {
    }

    protected AddressModel(Parcel in) {
        this.ADDRESS_ID = in.readInt();
        this.CONSIGNEE_NAME = in.readString();
        this.CONSIGNEE_MOBILE = in.readString();
        this.CONSIGNEE_ZIP_CODE = in.readString();
        this.CONSIGNEE_ADDRESS = in.readString();
        this.DEFAULT_FLG = in.readString();
        this.DEL_FLG = in.readString();
        this.PROVINCE = in.readString();
        this.CITY = in.readString();
        this.LOCATION = in.readString();
        this.ADDRESS_LOCATION = in.readString();
    }

    public static final Parcelable.Creator<AddressModel> CREATOR = new Parcelable.Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel source) {
            return new AddressModel(source);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

    public String getCONSIGNEE_PROVINCE() {
        return CONSIGNEE_PROVINCE;
    }

    public void setCONSIGNEE_PROVINCE(String CONSIGNEE_PROVINCE) {
        this.CONSIGNEE_PROVINCE = CONSIGNEE_PROVINCE;
    }

    public String getCONSIGNEE_CITY() {
        return CONSIGNEE_CITY;
    }

    public void setCONSIGNEE_CITY(String CONSIGNEE_CITY) {
        this.CONSIGNEE_CITY = CONSIGNEE_CITY;
    }

    public String getCONSIGNEE_COUNTY() {
        return CONSIGNEE_COUNTY;
    }

    public void setCONSIGNEE_COUNTY(String CONSIGNEE_COUNTY) {
        this.CONSIGNEE_COUNTY = CONSIGNEE_COUNTY;
    }

    public String getCOUNTY() {
        return COUNTY;
    }

    public void setCOUNTY(String COUNTY) {
        this.COUNTY = COUNTY;
    }

    public static MyLocation parseLocation(StringBuilder err, String sLoc) {
//        Log.e(AppConfig.ERR_TAG, "BDLocation parseLocation:" + sLoc);
        if (StringUtils.isEmpty(sLoc)) {
            err.append("空的地址");
            return null;
        }
        String[] ssLoc = sLoc.split(AppConfig.ADDRESS_LOCATION_SEP);
        if (ssLoc == null || ssLoc.length < 3) {
            err.append("错误的地址格式, 需要 xx|lng|lat");
            return null;
        }

        double lat, lng;
        try {
            lat = Double.parseDouble(ssLoc[2]);
            lng = Double.parseDouble(ssLoc[1]);
        } catch (Exception ex) {
            err.append("错误的地址格式, 需要 xx|lat|lng");
            return null;
        }

        MyLocation loc = new MyLocation();
        loc.setAddrStr(ssLoc[0]);
        loc.setLatitude(lat);
        loc.setLongitude(lng);
        return loc;
    }
}
