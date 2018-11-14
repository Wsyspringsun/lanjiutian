package com.wyw.ljtds.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class CreatOrderModel extends OrderTradeDto implements Serializable, Parcelable {
    private String PREFERENTIAL_ID; //优惠券id
    private String INVOICE_FLG = "0";
    private String INVOICE_TYPE;
    private String INVOICE_ID;
    private String INVOICE_TAX;
    private String INVOICE_ORG;
    private String INVOICE_TITLE;
    //发票种类 0：明细  1办公  2：家居   3：药品   4：耗材
    private String INVOICE_CONTENT;

    private String DISTRIBUTION_MODE = "0";

    protected CreatOrderModel(Parcel in) {
        INVOICE_FLG = in.readString();
        INVOICE_TYPE = in.readString();
        INVOICE_ID = in.readString();
        INVOICE_TAX = in.readString();
        INVOICE_ORG = in.readString();
        INVOICE_TITLE = in.readString();
        INVOICE_CONTENT = in.readString();
        DISTRIBUTION_MODE = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(INVOICE_FLG);
        dest.writeString(INVOICE_TYPE);
        dest.writeString(INVOICE_ID);
        dest.writeString(INVOICE_TAX);
        dest.writeString(INVOICE_ORG);
        dest.writeString(INVOICE_TITLE);
        dest.writeString(INVOICE_CONTENT);
        dest.writeString(DISTRIBUTION_MODE);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CreatOrderModel> CREATOR = new Creator<CreatOrderModel>() {
        @Override
        public CreatOrderModel createFromParcel(Parcel in) {
            return new CreatOrderModel(in);
        }

        @Override
        public CreatOrderModel[] newArray(int size) {
            return new CreatOrderModel[size];
        }
    };

    public String getINVOICE_FLG() {
        return INVOICE_FLG;
    }

    public void setINVOICE_FLG(String INVOICE_FLG) {
        this.INVOICE_FLG = INVOICE_FLG;
    }

    public String getINVOICE_TYPE() {
        return INVOICE_TYPE;
    }

    public void setINVOICE_TYPE(String INVOICE_TYPE) {
        this.INVOICE_TYPE = INVOICE_TYPE;
    }

    public String getINVOICE_ID() {
        return INVOICE_ID;
    }

    public void setINVOICE_ID(String INVOICE_ID) {
        this.INVOICE_ID = INVOICE_ID;
    }

    public String getINVOICE_TAX() {
        return INVOICE_TAX;
    }

    public void setINVOICE_TAX(String INVOICE_TAX) {
        this.INVOICE_TAX = INVOICE_TAX;
    }

    public String getINVOICE_TITLE() {
        return INVOICE_TITLE;
    }

    public void setINVOICE_TITLE(String INVOICE_TITLE) {
        this.INVOICE_TITLE = INVOICE_TITLE;
    }

    public String getINVOICE_CONTENT() {
        return INVOICE_CONTENT;
    }

    public void setINVOICE_CONTENT(String INVOICE_CONTENT) {
        this.INVOICE_CONTENT = INVOICE_CONTENT;
    }

    public String getDISTRIBUTION_MODE() {
        return DISTRIBUTION_MODE;
    }

    public void setDISTRIBUTION_MODE(String DISTRIBUTION_MODE) {
        this.DISTRIBUTION_MODE = DISTRIBUTION_MODE;
    }

    public String getINVOICE_ORG() {
        return INVOICE_ORG;
    }

    public void setINVOICE_ORG(String INVOICE_ORG) {
        this.INVOICE_ORG = INVOICE_ORG;
    }

    public String getPREFERENTIAL_ID() {
        return PREFERENTIAL_ID;
    }

    public void setPREFERENTIAL_ID(String PREFERENTIAL_ID) {
        this.PREFERENTIAL_ID = PREFERENTIAL_ID;
    }

    public class USER_ADDRESS {
        private String ADDRESS_DETAIL;
        private int ADDRESS_ID;
        private String CONSIGNEE_MOBILE;
        private String CONSIGNEE_NAME;
        private String CITY;
        private String PROVINCE;
        private String CONSIGNEE_ADDRESS;
        private int CONSIGNEE_PROVINCE;
        private int CONSIGNEE_CITY;

        public String getCITY() {
            return CITY;
        }

        public void setCITY(String CITY) {
            this.CITY = CITY;
        }

        public String getPROVINCE() {
            return PROVINCE;
        }

        public void setPROVINCE(String PROVINCE) {
            this.PROVINCE = PROVINCE;
        }

        public String getCONSIGNEE_ADDRESS() {
            return CONSIGNEE_ADDRESS;
        }

        public void setCONSIGNEE_ADDRESS(String CONSIGNEE_ADDRESS) {
            this.CONSIGNEE_ADDRESS = CONSIGNEE_ADDRESS;
        }

        public String getADDRESS_DETAIL() {
            return ADDRESS_DETAIL;
        }

        public void setADDRESS_DETAIL(String ADDRESS_DETAIL) {
            this.ADDRESS_DETAIL = ADDRESS_DETAIL;
        }

        public int getADDRESS_ID() {
            return ADDRESS_ID;
        }

        public void setADDRESS_ID(int ADDRESS_ID) {
            this.ADDRESS_ID = ADDRESS_ID;
        }

        public String getCONSIGNEE_MOBILE() {
            return CONSIGNEE_MOBILE;
        }

        public void setCONSIGNEE_MOBILE(String CONSIGNEE_MOBILE) {
            this.CONSIGNEE_MOBILE = CONSIGNEE_MOBILE;
        }

        public String getCONSIGNEE_NAME() {
            return CONSIGNEE_NAME;
        }

        public void setCONSIGNEE_NAME(String CONSIGNEE_NAME) {
            this.CONSIGNEE_NAME = CONSIGNEE_NAME;
        }

        public int getCONSIGNEE_PROVINCE() {
            return CONSIGNEE_PROVINCE;
        }

        public void setCONSIGNEE_PROVINCE(int CONSIGNEE_PROVINCE) {
            this.CONSIGNEE_PROVINCE = CONSIGNEE_PROVINCE;
        }

        public int getCONSIGNEE_CITY() {
            return CONSIGNEE_CITY;
        }

        public void setCONSIGNEE_CITY(int CONSIGNEE_CITY) {
            this.CONSIGNEE_CITY = CONSIGNEE_CITY;
        }


    }
}
