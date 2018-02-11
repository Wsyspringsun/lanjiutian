package com.wyw.ljtds.model;

import android.support.annotation.IntegerRes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/10 0010.
 */

public class UserIndexModel extends BaseModel {
    private UserDataModel userOrderNumberModel; //report order amount
    private UserModel userModel;
    private List<RecommendModel> recommendModels;
    private List<IconText> orderIcons;
    private List<IconText> extraIcons;

    public List<IconText> getOrderIcons() {
        return orderIcons;
    }

    public void setOrderIcons(List<IconText> orderIcons) {
        this.orderIcons = orderIcons;
    }

    public List<IconText> getExtraIcons() {
        return extraIcons;
    }

    public void setExtraIcons(List<IconText> extraIcons) {
        this.extraIcons = extraIcons;
    }

    public UserDataModel getUserOrderNumberModel() {
        return userOrderNumberModel;
    }

    public void setUserOrderNumberModel(UserDataModel userOrderNumberModel) {
        this.userOrderNumberModel = userOrderNumberModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public List<RecommendModel> getRecommendModels() {
        return recommendModels;
    }

    public void setRecommendModels(List<RecommendModel> recommendModels) {
        this.recommendModels = recommendModels;
    }
}
