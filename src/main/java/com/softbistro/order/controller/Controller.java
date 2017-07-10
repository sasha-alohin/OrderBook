package com.softbistro.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softbistro.order.component.Book;
import com.softbistro.order.service.OrderService;

@RestController
public class Controller {

	@Autowired
	private OrderService service;
	
	@RequestMapping(value="/catalog")
	public List<Book> getCatalog(){
		return service.getCatalog();
	}
}
