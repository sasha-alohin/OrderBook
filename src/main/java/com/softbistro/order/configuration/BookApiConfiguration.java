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
	private String urlForBook;
	private String urlForRent;
	public String getUrlForCatalog() {
		return urlForCatalog;
	}
	public void setUrlForCatalog(String urlForCatalog) {
		this.urlForCatalog = urlForCatalog;
	}
	public String getUrlForBook() {
		return urlForBook;
	}
	public void setUrlForBook(String urlForBook) {
		this.urlForBook = urlForBook;
	}
	public String getUrlForRent() {
		return urlForRent;
	}
	public void setUrlForRent(String urlForRent) {
		this.urlForRent = urlForRent;
	}
	
}
