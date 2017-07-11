package com.softbistro.order.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for Api
 * 
 * @author alex_alokhin
 *
 */
@Configuration
@ConfigurationProperties("orders")
public class BookApiConfiguration {

	private String urlForCatalog;
	private String urlForPrices;
	private String urlForCreateOrder;

	public String getUrlForCatalog() {
		return urlForCatalog;
	}

	public void setUrlForCatalog(String urlForCatalog) {
		this.urlForCatalog = urlForCatalog;
	}

	public String getUrlForPrices() {
		return urlForPrices;
	}

	public void setUrlForPrices(String urlForPrices) {
		this.urlForPrices = urlForPrices;
	}

	public String getUrlForCreateOrder() {
		return urlForCreateOrder;
	}

	public void setUrlForCreateOrder(String urlForCreateOrder) {
		this.urlForCreateOrder = urlForCreateOrder;
	}

}