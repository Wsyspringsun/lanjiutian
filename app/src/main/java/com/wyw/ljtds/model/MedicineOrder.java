package com.wyw.ljtds.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MedicineOrder {
    public static Map<String, String> StatData = new HashMap<>();

    static {
        // "申请换退货"
        StatData.put("9", "申请换退货");
        // 同意退换货
        StatData.put("10", "同意退换货");
        // 拒绝退换货
        StatData.put("11", "拒绝退换货");
        // 已退货
        StatData.put("12", "已退货");
        // 已退款
        StatData.put("13", "已退款");
    }


    private String commodityOrderId;
    private String oidUserId; // 用户帐号
    private String commodityId; // 商品id
    private String commodityName;// 商品名称
    private String imgPath;
    private String orderGroupId; // 主订单id
    private String commodityColor;// 商品颜色
    private String commoditySize;// 商品规格
    private Integer exchangeQuanlity;// 交易数量
    private BigDecimal costMoney; // 单价
    private BigDecimal costMoneyAll;// 总价
    private String orderStatus; // 订单状态
    private String delFlg;
    private String insUserId;
    private String updUserId;
    private String insDate;
    private String updDate;

    public void setCommodityOrderId(String commodityOrderId) {
        this.commodityOrderId = commodityOrderId;
    }

    public void setOidUserId(String oidUserId) {
        this.oidUserId = oidUserId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId;
    }

    public void setOrderGroupId(String orderGroupId) {
        this.orderGroupId = orderGroupId;
    }

    public void setCommodityColor(String commodityColor) {
        this.commodityColor = commodityColor;
    }

    public void setCommoditySize(String commoditySize) {
        this.commoditySize = commoditySize;
    }

    public void setExchangeQuanlity(Integer exchangeQuanlity) {
        this.exchangeQuanlity = exchangeQuanlity;
    }

    public void setCostMoney(BigDecimal costMoney) {
        this.costMoney = costMoney;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setDelFlg(String delFlg) {
        this.delFlg = delFlg;
    }

    public void setInsUserId(String insUserId) {
        this.insUserId = insUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    public void setInsDate(String insDate) {
        this.insDate = insDate;
    }

    public void setUpdDate(String updDate) {
        this.updDate = updDate;
    }

    public String getCommodityOrderId() {
        return this.commodityOrderId;
    }

    public String getOidUserId() {
        return this.oidUserId;
    }

    public String getCommodityId() {
        return this.commodityId;
    }

    public String getOrderGroupId() {
        return this.orderGroupId;
    }

    /**
     * @return the commodityName
     */
    public String getCommodityName() {
        return commodityName;
    }

    /**
     * @param commodityName the commodityName to set
     */
    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getCommodityColor() {
        return this.commodityColor;
    }

    public String getCommoditySize() {
        return this.commoditySize;
    }

    public Integer getExchangeQuanlity() {
        return this.exchangeQuanlity;
    }

    public BigDecimal getCostMoney() {
        return this.costMoney;
    }

    public BigDecimal getCostMoneyAll() {
        if (this.getCostMoney() != null && this.getExchangeQuanlity() != null)
            return this.getCostMoney().multiply(new BigDecimal(this.exchangeQuanlity));
        return this.costMoneyAll;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public String getDelFlg() {
        return this.delFlg;
    }

    public String getInsUserId() {
        return this.insUserId;
    }

    public String getUpdUserId() {
        return this.updUserId;
    }

    public String getInsDate() {
        return this.insDate;
    }

    public String getUpdDate() {
        return this.updDate;
    }

    public static MedicineOrder fromMap(Map<String, Object> kv) {
        MedicineOrder dto = new MedicineOrder();
        dto.setCommodityOrderId((String) kv.get("COMMODITY_ORDER_ID"));
        dto.setOidUserId((String) kv.get("OID_USER_ID"));
        dto.setCommodityId((String) kv.get("COMMODITY_ID"));
        dto.setCommodityName((String) kv.get("COMMODITY_NAME"));
        dto.setOrderGroupId((String) kv.get("ORDER_GROUP_ID"));
        dto.setCommodityColor((String) kv.get("COMMODITY_COLOR"));
        dto.setCommoditySize((String) kv.get("COMMODITY_SIZE"));
        dto.setExchangeQuanlity((Integer) kv.get("EXCHANGE_QUANLITY"));
        dto.setCostMoney((BigDecimal) kv.get("COST_MONEY"));
        dto.setOrderStatus((String) kv.get("ORDER_STATUS"));
        dto.setDelFlg((String) kv.get("DEL_FLG"));
        dto.setInsUserId((String) kv.get("INS_USER_ID"));
        dto.setUpdUserId((String) kv.get("UPD_USER_ID"));
        dto.setInsDate((String) kv.get("INS_DATE"));
        dto.setUpdDate((String) kv.get("UPD_DATE"));
        return dto;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> kv = new HashMap<String, Object>();
        kv.put("COMMODITY_ORDER_ID", this.getCommodityOrderId());
        kv.put("OID_USER_ID", this.getOidUserId());
        kv.put("COMMODITY_ID", this.getCommodityId());
        kv.put("ORDER_GROUP_ID", this.getOrderGroupId());
        kv.put("COMMODITY_COLOR", this.getCommodityColor());
        kv.put("COMMODITY_SIZE", this.getCommoditySize());
        kv.put("EXCHANGE_QUANLITY", this.getExchangeQuanlity());
        kv.put("COST_MONEY", this.getCostMoney());
        kv.put("COST_MONEY_ALL", this.getCostMoneyAll());
        kv.put("ORDER_STATUS", this.getOrderStatus());
        kv.put("DEL_FLG", this.getDelFlg());
        kv.put("INS_USER_ID", this.getInsUserId());
        kv.put("UPD_USER_ID", this.getUpdUserId());
        kv.put("INS_DATE", this.getInsDate());
        kv.put("UPD_DATE", this.getUpdDate());

        return kv;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
