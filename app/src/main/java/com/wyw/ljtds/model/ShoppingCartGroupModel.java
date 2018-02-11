package com.wyw.ljtds.model;

import java.util.List;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class ShoppingCartGroupModel {
    public String shopCostMoneyAll;
    private List<ShoppingCartMedicineModel> DETAILS;
    private String LOGISTICS_COMPANY_ID; //店铺编号
    private String LOGISTICS_COMPANY; //店铺名称
    public boolean checked = false;
    private String BAOYOU;

    public String getBAOYOU() {
        return BAOYOU;
    }

    public void setBAOYOU(String BAOYOU) {
        this.BAOYOU = BAOYOU;
    }

    public List<ShoppingCartMedicineModel> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<ShoppingCartMedicineModel> DETAILS) {
        this.DETAILS = DETAILS;
    }

    public String getBUSNO() {
        return LOGISTICS_COMPANY_ID;
    }

    public void setBUSNO(String LOGISTICS_COMPANY_ID) {
        this.LOGISTICS_COMPANY_ID = LOGISTICS_COMPANY_ID;
    }

    public String getBUSNAME() {
        return LOGISTICS_COMPANY;
    }

    public void setBUSNAME(String LOGISTICS_COMPANY) {
        this.LOGISTICS_COMPANY = LOGISTICS_COMPANY;
    }
}
