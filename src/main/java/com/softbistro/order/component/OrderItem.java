package com.softbistro.order.component;

public class OrderItem {

	private Integer quantity;
	private String catalogItemSource;
	private String pricingId;
	private String catalogItemId;

	public OrderItem() {
	}

	public OrderItem(Integer quantity, String catalogItemSource, String pricingId, String catalogItemId) {
		this.setQuantity(quantity);
		this.catalogItemSource = catalogItemSource;
		this.pricingId = pricingId;
		this.catalogItemId = catalogItemId;
	}

	public String getCatalogItemSource() {
		return catalogItemSource;
	}

	public void setCatalogItemSource(String catalogItemSource) {
		this.catalogItemSource = catalogItemSource;
	}

	public String getPricingId() {
		return pricingId;
	}

	public void setPricingId(String pricingId) {
		this.pricingId = pricingId;
	}

	public String getCatalogItemId() {
		return catalogItemId;
	}

	public void setCatalogItemId(String catalogItemId) {
		this.catalogItemId = catalogItemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
