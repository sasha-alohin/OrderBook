package com.softbistro.order.component;

public class OrderItem {

	private Integer quatity;
	private String catalogItemSource;
	private String pricingId;
	private String catalogItemId;

	@Override
	public String toString() {
		return "OrderItem [quatity=" + quatity + ", catalogItemSource=" + catalogItemSource + ", pricingId=" + pricingId
				+ ", catalogItemId=" + catalogItemId + "]";
	}

	public OrderItem() {
	}

	public OrderItem(Integer quatity, String catalogItemSource, String pricingId, String catalogItemId) {
		this.quatity = quatity;
		this.catalogItemSource = catalogItemSource;
		this.pricingId = pricingId;
		this.catalogItemId = catalogItemId;
	}

	public Integer getQuatity() {
		return quatity;
	}

	public void setQuatity(Integer quatity) {
		this.quatity = quatity;
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

}
