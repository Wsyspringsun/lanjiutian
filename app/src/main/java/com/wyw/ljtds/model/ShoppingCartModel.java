package com.wyw.ljtds.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class ShoppingCartModel {
    private List<ShoppingCartGroupModel> DETAILS;

    public String cartCostMoneyAll = "0";

    public List<ShoppingCartGroupModel> getDETAILS() {
        return DETAILS;
    }

    public void setDETAILS(List<ShoppingCartGroupModel> DETAILS) {
        this.DETAILS = DETAILS;
    }


}
