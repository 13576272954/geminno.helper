package com.example.administrator.helper.entity;

public class OrderStaus {
	private Integer id;
	private String orderStaus;
	public OrderStaus(Integer id, String orderStaus) {
		super();
		this.id = id;
		this.orderStaus = orderStaus;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderStaus() {
		return orderStaus;
	}
	public void setOrderStaus(String orderStaus) {
		this.orderStaus = orderStaus;
	}
	@Override
	public String toString() {
		return "OrderStaus [id=" + id + ", orderStaus=" + orderStaus + "]";
	}
	

}
