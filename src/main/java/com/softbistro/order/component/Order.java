package com.softbistro.order.component;

import java.util.List;

public class Order {
	private List<OrderItem> items;
	private Integer userId;


	public Order(List<OrderItem> items, Integer userId) {
		this.items = items;
		this.userId = userId;
	}

	public Order() {
	}

	@Override
	public String toString() {
		return "Order [items=" + items + ", userId=" + userId + "]";
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
