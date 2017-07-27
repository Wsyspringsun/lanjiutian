package com.wyw.ljtds.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商铺详情
 * 
 * @author wsy
 *
 */
public class GroupOrder {
	private List<MedicineOrder> details;
	private int groupExchangeQuanlity;
	private String orderGroupId;
	private String orderTradeId;
	private String oidGroupId;
	private String oidGroupName;
	private String postage;
	private String groupMoneyAll;
	private String groupStatus;
	private String invoiceFlg;
	private String invoiceType;
	private String invoiceId;
	private String logisticsCompany;
	private String logisticsCompanyId;
	private String logisticsOrderId;
	private String distributionMode;
	private String distributionDate;
	private String delFlg;
	private String insUserId;
	private String updUserId;
	private String insDate;
	private String updDate;

	public void setOrderGroupId(String orderGroupId) {
		this.orderGroupId = orderGroupId;
	}

	public void setOrderTradeId(String orderTradeId) {
		this.orderTradeId = orderTradeId;
	}

	public void setOidGroupId(String oidGroupId) {
		this.oidGroupId = oidGroupId;
	}

	public void setOidGroupName(String oidGroupName) {
		this.oidGroupName = oidGroupName;
	}

	public void setPostage(String postage) {
		this.postage = postage;
	}

	public void setGroupMoneyAll(String groupMoneyAll) {
		this.groupMoneyAll = groupMoneyAll;
	}

	public void setGroupStatus(String groupStatus) {
		this.groupStatus = groupStatus;
	}

	public void setInvoiceFlg(String invoiceFlg) {
		this.invoiceFlg = invoiceFlg;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}

	public void setLogisticsCompanyId(String logisticsCompanyId) {
		this.logisticsCompanyId = logisticsCompanyId;
	}

	public void setLogisticsOrderId(String logisticsOrderId) {
		this.logisticsOrderId = logisticsOrderId;
	}

	public void setDistributionMode(String distributionMode) {
		this.distributionMode = distributionMode;
	}

	public void setDistributionDate(String distributionDate) {
		this.distributionDate = distributionDate;
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

	public String getOrderGroupId() {
		return this.orderGroupId;
	}

	public String getOrderTradeId() {
		return this.orderTradeId;
	}

	public String getOidGroupId() {
		return this.oidGroupId;
	}

	public String getOidGroupName() {
		return this.oidGroupName;
	}

	public String getPostage() {
		return this.postage;
	}

	public String getGroupMoneyAll() {
		return this.groupMoneyAll;
	}

	public String getGroupStatus() {
		return this.groupStatus;
	}

	public String getInvoiceFlg() {
		return this.invoiceFlg;
	}

	public String getInvoiceType() {
		return this.invoiceType;
	}

	public String getInvoiceId() {
		return this.invoiceId;
	}

	public String getLogisticsCompany() {
		return this.logisticsCompany;
	}

	public String getLogisticsCompanyId() {
		return this.logisticsCompanyId;
	}

	public String getLogisticsOrderId() {
		return this.logisticsOrderId;
	}

	public String getDistributionMode() {
		return this.distributionMode;
	}

	public String getDistributionDate() {
		return this.distributionDate;
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

	public static GroupOrder fromMap(Map<String, Object> kv) {
		GroupOrder dto = new GroupOrder();
		dto.setOrderGroupId((String) kv.get("ORDER_GROUP_ID"));
		dto.setOrderTradeId((String) kv.get("ORDER_TRADE_ID"));
		dto.setOidGroupId((String) kv.get("OID_GROUP_ID"));
		dto.setOidGroupName((String) kv.get("OID_GROUP_NAME"));
		dto.setPostage((String) kv.get("POSTAGE"));
		dto.setGroupMoneyAll(String.valueOf(kv.get("GROUP_MONEY_ALL")));
		dto.setGroupStatus((String) kv.get("GROUP_STATUS"));
		dto.setInvoiceFlg((String) kv.get("INVOICE_FLG"));
		dto.setInvoiceType((String) kv.get("INVOICE_TYPE"));
		dto.setInvoiceId((String) kv.get("INVOICE_ID"));
		dto.setLogisticsCompany((String) kv.get("LOGISTICS_COMPANY"));
		dto.setLogisticsCompanyId((String) kv.get("LOGISTICS_COMPANY_ID"));
		dto.setLogisticsOrderId((String) kv.get("LOGISTICS_ORDER_ID"));
		dto.setDistributionMode((String) kv.get("DISTRIBUTION_MODE"));
		dto.setDistributionDate((String) kv.get("DISTRIBUTION_DATE"));
		dto.setDelFlg((String) kv.get("DEL_FLG"));
		dto.setInsUserId((String) kv.get("INS_USER_ID"));
		dto.setUpdUserId((String) kv.get("UPD_USER_ID"));
		dto.setInsDate((String) kv.get("INS_DATE"));
		dto.setUpdDate((String) kv.get("UPD_DATE"));
		return dto;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> kv = new HashMap<String, Object>();
		kv.put("ORDER_GROUP_ID", this.getOrderGroupId());
		kv.put("ORDER_TRADE_ID", this.getOrderTradeId());
		kv.put("OID_GROUP_ID", this.getOidGroupId());
		kv.put("OID_GROUP_NAME", this.getOidGroupName());
		kv.put("POSTAGE", this.getPostage());
		kv.put("GROUP_MONEY_ALL", this.getGroupMoneyAll());
		kv.put("GROUP_STATUS", this.getGroupStatus());
		kv.put("INVOICE_FLG", this.getInvoiceFlg());
		kv.put("INVOICE_TYPE", this.getInvoiceType());
		kv.put("INVOICE_ID", this.getInvoiceId());
		kv.put("LOGISTICS_COMPANY", this.getLogisticsCompany());
		kv.put("LOGISTICS_COMPANY_ID", this.getLogisticsCompanyId());
		kv.put("LOGISTICS_ORDER_ID", this.getLogisticsOrderId());
		kv.put("DISTRIBUTION_MODE", this.getDistributionMode());
		kv.put("DISTRIBUTION_DATE", this.getDistributionDate());
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
	public List<MedicineOrder> getDetails() {
		return details;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(List<MedicineOrder> details) {
		this.details = details;
	}

	/**
	 * @return the groupExchangeQuanlity
	 */
	public int getGroupExchangeQuanlity() {
		return groupExchangeQuanlity;
	}

	/**
	 * @param groupExchangeQuanlity the groupExchangeQuanlity to set
	 */
	public void setGroupExchangeQuanlity(int groupExchangeQuanlity) {
		this.groupExchangeQuanlity = groupExchangeQuanlity;
	}
}
