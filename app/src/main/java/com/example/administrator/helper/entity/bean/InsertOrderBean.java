package com.example.administrator.helper.entity.bean;

public class InsertOrderBean {
	private Integer taskId;//任务id
	private Integer couponId;//优惠券id
	private Integer price;//订单价格
	//buyWay  task_status_id  trade_no
	private boolean buyWay;//支付方式
	private Integer taskStatusId;//订单状态id
	
	public InsertOrderBean(Integer taskId, Integer couponId, Integer price, boolean buyWay, Integer taskStatusId) {
		super();
		this.taskId = taskId;
		this.couponId = couponId;
		this.price = price;
		this.buyWay = buyWay;
		this.taskStatusId = taskStatusId;
	}


	public InsertOrderBean(Integer couponId, Integer price, boolean buyWay, Integer taskStatusId) {
		super();
		this.couponId = couponId;
		this.price = price;
		this.buyWay = buyWay;
		this.taskStatusId = taskStatusId;
	}


	public boolean getBuyWay() {
		return buyWay;
	}


	public void setBuyWay(boolean buyWay) {
		this.buyWay = buyWay;
	}


	public Integer getTaskStatusId() {
		return taskStatusId;
	}


	public void setTaskStatusId(Integer taskStatusId) {
		this.taskStatusId = taskStatusId;
	}


	


	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	public Integer getCouponId() {
		return couponId;
	}
	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}


	@Override
	public String toString() {
		return "InsertOrderBean [taskId=" + taskId + ", couponId=" + couponId + ", price=" + price + ", buyWay="
				+ buyWay + ", taskStatusId=" + taskStatusId + "]";
	}
	
}
