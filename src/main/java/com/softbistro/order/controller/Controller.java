package com.softbistro.order.controller;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.github.messenger4j.send.templates.GenericTemplate;
import com.softbistro.order.component.Book;
import com.softbistro.order.component.CatalogItem;
import com.softbistro.order.component.Order;
import com.softbistro.order.service.OrderService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

@RestController
public class Controller {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

	@Autowired
	private OrderService service;

	@RequestMapping(value = "/catalog")
	public List<Book> getCatalog() {
		return service.getCatalog();
	}
	
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	public void createOrder(@RequestBody Order order) {
		service.createOrder();
	}
	@RequestMapping(value = "/prices")
	public CatalogItem getPrices() {
		return service.getCatalogItem();
	}
	@RequestMapping(value = "/template", method = RequestMethod.GET, produces = "application/json")
	public GenericTemplate getTemplate() {
		return service.createTemplate(getCatalog());
	}
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	public String get() {
		/*GenericTemplate genericTemplate = Client.create().resource("http://localhost:8080/template").get(new GenericType<GenericTemplate>() {
		});*/
		
		ClientResponse response = null;
		try {
			LOGGER.info("Calling getAllVCPENetworks service");
			String url = "http://localhost:8080/template";
			Client client = Client.create();
			WebResource webResource = client.resource(url);
			response = webResource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			LOGGER.info("List of VCPENetwork recovered");
		} catch (ClientHandlerException e) {
			LOGGER.error(e.getMessage());
			throw e;
		}
		return response.getEntity(new GenericType<String>() {
		});
	}

}
