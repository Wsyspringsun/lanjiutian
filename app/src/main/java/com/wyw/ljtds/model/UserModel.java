package com.wyw.ljtds.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class UserModel extends BaseModel implements Parcelable {
    //唯一标识
    private String OID_USER_ID;
    //昵称
    private String NICKNAME;
    //头像文件id
    private String USER_ICON_FILE_ID;
    //真实姓名
    private String USER_NAME;
    //身份证号
    private String ID_CARD;
    //用户积分
    private BigDecimal USER_POINT;
    //用户说明
    private String USER_COMMENT;
    //性别
    private String SEX;
    //手机号
    private String MOBILE;
    //银行卡号
    private int CARD_NO;
    //待付款数量
    private int PAYMENT;
    //待发货数量
    private int COMMODITY_SENT;
    //待收货数量
    private int COMMODITY_RECEIVED;
    //待评价数量
    private int COMMODITY_EVALUATE;
    //退货数量
    private int REFUND;
    //地址信息
    private Address DEFAULT_ADDRESS;
    //生日
    private Long BIRTHDAY = null;

    public Long getBIRTHDAY() {
        return BIRTHDAY;
    }

    public void setBIRTHDAY(Long BIRTHDAY) {
        this.BIRTHDAY = BIRTHDAY;
    }

    public String getNICKNAME() {
        return NICKNAME;
    }

    public void setNICKNAME(String NICKNAME) {
        this.NICKNAME = NICKNAME;
    }

    public String getUSER_ICON_FILE_ID() {
        return USER_ICON_FILE_ID;
    }

    public void setUSER_ICON_FILE_ID(String USER_ICON_FILE_ID) {
        this.USER_ICON_FILE_ID = USER_ICON_FILE_ID;
    }

    public String getUSER_NAME() {
        return USER_NAME;
    }

    public void setUSER_NAME(String USER_NAME) {
        this.USER_NAME = USER_NAME;
    }

    public String getID_CARD() {
        return ID_CARD;
    }

    public void setID_CARD(String ID_CARD) {
        this.ID_CARD = ID_CARD;
    }

    public BigDecimal getUSER_POINT() {
        return USER_POINT;
    }

    public void setUSER_POINT(BigDecimal USER_POINT) {
        this.USER_POINT = USER_POINT;
    }

    public String getUSER_COMMENT() {
        return USER_COMMENT;
    }

    public void setUSER_COMMENT(String USER_COMMENT) {
        this.USER_COMMENT = USER_COMMENT;
    }

    public String getSEX() {
        return SEX;
    }

    public void setSEX(String SEX) {
        this.SEX = SEX;
    }

    public String getMOBILE() {
        return MOBILE;
    }

    public void setMOBILE(String MOBILE) {
        this.MOBILE = MOBILE;
    }

    public int getCARD_NO() {
        return CARD_NO;
    }

    public void setCARD_NO(int CARD_NO) {
        this.CARD_NO = CARD_NO;
    }

    public int getPAYMENT() {
        return PAYMENT;
    }

    public void setPAYMENT(int PAYMENT) {
        this.PAYMENT = PAYMENT;
    }

    public int getCOMMODITY_SENT() {
        return COMMODITY_SENT;
    }

    public void setCOMMODITY_SENT(int COMMODITY_SENT) {
        this.COMMODITY_SENT = COMMODITY_SENT;
    }

    public int getCOMMODITY_RECEIVED() {
        return COMMODITY_RECEIVED;
    }

    public void setCOMMODITY_RECEIVED(int COMMODITY_RECEIVED) {
        this.COMMODITY_RECEIVED = COMMODITY_RECEIVED;
    }

    public int getCOMMODITY_EVALUATE() {
        return COMMODITY_EVALUATE;
    }

    public void setCOMMODITY_EVALUATE(int COMMODITY_EVALUATE) {
        this.COMMODITY_EVALUATE = COMMODITY_EVALUATE;
    }

    public int getREFUND() {
        return REFUND;
    }

    public void setREFUND(int REFUND) {
        this.REFUND = REFUND;
    }

    public Address getDEFAULT_ADDRESS() {
        return DEFAULT_ADDRESS;
    }

    public void setDEFAULT_ADDRESS(Address DEFAULT_ADDRESS) {
        this.DEFAULT_ADDRESS = DEFAULT_ADDRESS;
    }

    public String getOID_USER_ID() {
        return OID_USER_ID;
    }

    public void setOID_USER_ID(String OID_USER_ID) {
        this.OID_USER_ID = OID_USER_ID;
    }

    public class Address {
        //地址的唯一id
        private int ADDRESS_ID;
        //姓名
        private String CONSIGNEE_NAME;
        //手机号
        private String CONSIGNEE_MOBILE;
        //邮编
        private String CONSIGNEE_ZIP_CODE;
        //详细地址
        private String CONSIGNEE_ADDRESS;
        //省
        private String PROVINCE;
        //市
        private String CITY;
        //全部详细地址
        private String ADDRESS_DETAIL;

        public String getCONSIGNEE_MOBILE() {
            return CONSIGNEE_MOBILE;
        }

        public void setCONSIGNEE_MOBILE(String CONSIGNEE_MOBILE) {
            this.CONSIGNEE_MOBILE = CONSIGNEE_MOBILE;
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

        public String getADDRESS_DETAIL() {
            return ADDRESS_DETAIL;
        }

        public void setADDRESS_DETAIL(String ADDRESS_DETAIL) {
            this.ADDRESS_DETAIL = ADDRESS_DETAIL;
        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.OID_USER_ID);
        dest.writeString(this.NICKNAME);
        dest.writeString(this.USER_ICON_FILE_ID);
        dest.writeString(this.USER_NAME);
        dest.writeString(this.ID_CARD);
        dest.writeString(this.SEX);
        dest.writeString(this.MOBILE);
        dest.writeInt(this.CARD_NO);
        if (this.BIRTHDAY != null)
            dest.writeLong(this.BIRTHDAY);
    }

    public UserModel() {
    }

    protected UserModel(Parcel in) {
        this.OID_USER_ID = in.readString();
        this.NICKNAME = in.readString();
        this.USER_ICON_FILE_ID = in.readString();
        this.USER_NAME = in.readString();
        this.ID_CARD = in.readString();
        this.SEX = in.readString();
        this.MOBILE = in.readString();
        this.CARD_NO = in.readInt();
        this.BIRTHDAY = in.readLong();
    }

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel source) {
            return new UserModel(source);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
}
