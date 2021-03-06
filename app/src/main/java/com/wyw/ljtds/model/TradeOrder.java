package com.wyw.ljtds.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 映射交易单
 * @author wsy
 *
 */
public class TradeOrder {
	private List<GroupOrder> details;
	private String orderTradeId;
	private String oidUserId;
	private String userAddressId;
	private String costPoint;
	private String pointMoney;
	private String tradeMoneyAll;
	private String payAmount;
	private String paymentMethod;
	private String tradeStatus;
	private String delFlg;
	private String insUserId;
	private String updUserId;
	private String insDate;
	private String updDate;

	public void setOrderTradeId(String orderTradeId) {
		this.orderTradeId = orderTradeId;
	}

	public void setOidUserId(String oidUserId) {
		this.oidUserId = oidUserId;
	}

	public void setUserAddressId(String userAddressId) {
		this.userAddressId = userAddressId;
	}

	public void setCostPoint(String costPoint) {
		this.costPoint = costPoint;
	}

	public void setPointMoney(String pointMoney) {
		this.pointMoney = pointMoney;
	}

	public void setTradeMoneyAll(String tradeMoneyAll) {
		this.tradeMoneyAll = tradeMoneyAll;
	}

	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
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

	public String getOrderTradeId() {
		return this.orderTradeId;
	}

	public String getOidUserId() {
		return this.oidUserId;
	}

	public String getUserAddressId() {
		return this.userAddressId;
	}

	public String getCostPoint() {
		return this.costPoint;
	}

	public String getPointMoney() {
		return this.pointMoney;
	}

	public String getTradeMoneyAll() {
		return this.tradeMoneyAll;
	}

	public String getPayAmount() {
		return this.payAmount;
	}

	public String getPaymentMethod() {
		return this.paymentMethod;
	}

	public String getTradeStatus() {
		return this.tradeStatus;
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

	public static TradeOrder fromMap(Map<String, Object> kv) {
		TradeOrder dto = new TradeOrder();
		dto.setOrderTradeId((String) kv.get("ORDER_TRADE_ID"));
		dto.setOidUserId((String) kv.get("OID_USER_ID"));
		dto.setUserAddressId((String) kv.get("USER_ADDRESS_ID"));
		dto.setCostPoint((String) kv.get("COST_POINT"));
		dto.setPointMoney(String.valueOf(kv.get("POINT_MONEY")));
		dto.setTradeMoneyAll(String.valueOf(kv.get("TRADE_MONEY_ALL")));
		dto.setPayAmount(String.valueOf(kv.get("PAY_AMOUNT")));
		dto.setPaymentMethod((String) kv.get("PAYMENT_METHOD"));
		dto.setTradeStatus((String) kv.get("TRADE_STATUS"));
		dto.setDelFlg((String) kv.get("DEL_FLG"));
		dto.setInsUserId((String) kv.get("INS_USER_ID"));
		dto.setUpdUserId((String) kv.get("UPD_USER_ID"));
		dto.setInsDate((String) kv.get("INS_DATE"));
		dto.setUpdDate((String) kv.get("UPD_DATE"));
		return dto;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> kv = new HashMap<String, Object>();
		kv.put("ORDER_TRADE_ID", this.getOrderTradeId());
		kv.put("OID_USER_ID", this.getOidUserId());
		kv.put("USER_ADDRESS_ID", this.getUserAddressId());
		kv.put("COST_POINT", this.getCostPoint());
		kv.put("POINT_MONEY", this.getPointMoney());
		kv.put("TRADE_MONEY_ALL", this.getTradeMoneyAll());
		kv.put("PAY_AMOUNT", this.getPayAmount());
		kv.put("PAYMENT_METHOD", this.getPaymentMethod());
		kv.put("TRADE_STATUS", this.getTradeStatus());
		kv.put("DEL_FLG", this.getDelFlg());
		kv.put("INS_USER_ID", this.getInsUserId());
		kv.put("UPD_USER_ID", this.getUpdUserId());
		kv.put("INS_DATE", this.getInsDate());
		kv.put("UPD_DATE", this.getUpdDate());

		return kv;
	}

	/**
	 * @return the details
	 */
	public List<GroupOrder> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<GroupOrder> details) {
		this.details = details;
	}
}
