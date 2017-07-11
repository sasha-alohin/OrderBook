package com.softbistro.order.component;

public class BookForOrder {

	private String catalogItemId;
	private String pricingId;

	public BookForOrder(String catalogItemId, String pricingId) {
		this.catalogItemId = catalogItemId;
		this.pricingId = pricingId;
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
