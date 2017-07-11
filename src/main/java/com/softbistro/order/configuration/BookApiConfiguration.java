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
	private String urlForFirstCheckout;
	private String urlForZeroCheckout;
	private String urlForFirstEvaluateCheckout;

	public String getUrlForFirstEvaluateCheckout() {
		return urlForFirstEvaluateCheckout;
	}

	public void setUrlForFirstEvaluateCheckout(String urlForFirstEvaluateCheckout) {
		this.urlForFirstEvaluateCheckout = urlForFirstEvaluateCheckout;
	}

	public String getUrlForZeroCheckout() {
		return urlForZeroCheckout;
	}

	public void setUrlForZeroCheckout(String urlForZeroCheckout) {
		this.urlForZeroCheckout = urlForZeroCheckout;
	}

	public String getUrlForFirstCheckout() {
		return urlForFirstCheckout;
	}

	public void setUrlForFirstCheckout(String urlForFirstCheckout) {
		this.urlForFirstCheckout = urlForFirstCheckout;
	}

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