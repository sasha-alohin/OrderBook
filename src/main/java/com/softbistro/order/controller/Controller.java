package com.softbistro.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.messenger4j.send.templates.GenericTemplate;
import com.softbistro.order.component.Book;
import com.softbistro.order.component.BookForOrder;
import com.softbistro.order.component.CatalogItem;
import com.softbistro.order.service.OrderService;

@RestController
public class Controller {

	@Autowired
	private OrderService service;

	@RequestMapping(value = "/lastCheckout", method = RequestMethod.POST, produces = "application/json")
	public void lastCheckout(@RequestBody BookForOrder book) throws JsonProcessingException {
		service.lastEvaluateCheckout(book);
	}
	
	@RequestMapping(value = "/zeroCheckout", method = RequestMethod.POST, produces = "application/json")
	public void zeroCheckout(@RequestBody BookForOrder book) throws JsonProcessingException {
		service.zeroCheckout(book);
	}

	@RequestMapping(value = "/firstCheckout", method = RequestMethod.POST, produces = "application/json")
	public void firstCheckout(@RequestBody BookForOrder book) throws JsonProcessingException {
		service.firstCheckout(book);
	}

	@RequestMapping(value = "/firstEvaluateCheckout", method = RequestMethod.POST, produces = "application/json")
	public void firstEvaluateCheckout(@RequestBody BookForOrder book) throws JsonProcessingException {
		 service.firstEvaluateCheckout(book);
	}
	
	@RequestMapping(value = "/setShippingOpt", method = RequestMethod.POST, produces = "application/json")
	public void setShippingOptions(@RequestBody BookForOrder book) throws JsonProcessingException {
		service.setShippingOptions(book);
	}

	@RequestMapping(value = "/catalog/{searchQuery}", method = RequestMethod.GET, produces = "application/json")
	public List<Book> getCatalog(@PathVariable("searchQuery") String searchQuery) {
		return service.getCatalog(searchQuery);
	}

	@RequestMapping(value = "/createOrder", method = RequestMethod.POST, produces = "application/json")
	public Integer createOrder(@RequestBody BookForOrder book) throws JsonProcessingException {
		return service.createOrder(book);
	}
	
	@RequestMapping(value = "/addItem", method = RequestMethod.POST, produces = "application/json")
	public void addItem(@RequestBody BookForOrder book) throws JsonProcessingException {
		 service.addItem(book);
	}

	@RequestMapping(value = "/prices/{catalogItemId}", method = RequestMethod.GET, produces = "application/json")
	public CatalogItem getPrices(@PathVariable("catalogItemId") String catalogItemId) {
		return service.getCatalogItem(catalogItemId);
	}

	@RequestMapping(value = "/template/{catalogItemId}", method = RequestMethod.GET, produces = "application/json")
	public GenericTemplate getTemplate(@PathVariable("searchQuery") String searchQuery) {
		return service.createTemplate(getCatalog(searchQuery));
	}

}
