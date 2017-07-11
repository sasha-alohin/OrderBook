package com.softbistro.order.component;

public class BookForOrder {

	private String catalogItemId;
	private String pricingId;
	private Integer orderId;

	public BookForOrder(String catalogItemId, String pricingId) {
		this.catalogItemId = catalogItemId;
		this.pricingId = pricingId;
	}

	public BookForOrder(String catalogItemId) {
		this.catalogItemId = catalogItemId;
	}

	public BookForOrder(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public BookForOrder() {
	}

	public String getCatalogItemId() {
		return catalogItemId;
	}

	public void setCatalogItemId(String catalogItemId) {
		this.catalogItemId = catalogItemId;
	}

	public String getPricingId() {
		return pricingId;
	}

	public void setPricingId(String pricingId) {
		this.pricingId = pricingId;
	}

}
