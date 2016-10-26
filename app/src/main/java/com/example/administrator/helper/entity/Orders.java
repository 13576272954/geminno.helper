package com.example.administrator.helper.entity;

import java.sql.Timestamp;

/**
 * 实体类 订单
 * 
 * @author Administrator
 *
 */
public class Orders {
	private Integer id;// 订单id
	private User receiveUser;// 接单用户
	private Task task;// 任务
	private Coupon coupon;// 优惠券
	private Integer price;//订单价格
	private boolean buyWay;// 支付方式
	//false为微信，true为支付宝
	private Timestamp createDate;// 创建日期
	private Timestamp finishTime;// 完成时间
	private OrderStaus orderStaus;// 订单状态   
	//1、待付款2、待开始2、进行中3、待评价5、已完成6、退款中7、已取消
	private Appraise order;// 评价

	public Orders() {
		super();
	}
	
	
	public Orders(User receiveUser, Task task, Coupon coupon, Integer price, boolean buyWay, Timestamp createDate,
			Timestamp finishTime, OrderStaus orderStaus, Appraise order) {
		super();
		this.receiveUser = receiveUser;
		this.task = task;
		this.coupon = coupon;
		this.price = price;
		this.buyWay = buyWay;
		this.createDate = createDate;
		this.finishTime = finishTime;
		this.orderStaus = orderStaus;
		this.order = order;
	}


	public Orders(Integer id, User receiveUser, Task task, Coupon coupon, Integer price, boolean buyWay, Timestamp createDate,
			Timestamp finishTime, OrderStaus orderStaus, Appraise order) {
		super();
		this.id = id;
		this.receiveUser = receiveUser;
		this.task = task;
		this.coupon = coupon;
		this.price = price;
		this.buyWay = buyWay;
		this.createDate = createDate;
		this.finishTime = finishTime;
		this.orderStaus = orderStaus;
		this.order = order;
	}


	public Integer getPrice() {
		return price;
	}


	public void setPrice(Integer price) {
		this.price = price;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public void setOrderStaus(OrderStaus orderStaus) {
		this.orderStaus = orderStaus;
	}


	public User getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(User receiveUser) {
		this.receiveUser = receiveUser;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Coupon getCoupon() {
		return coupon;
	}

	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;
	}

	public boolean getBuyWay() {
		return buyWay;
	}

	public void setBuyWay(boolean buyWay) {
		this.buyWay = buyWay;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Timestamp getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}

	public OrderStaus getOrderStaus() {
		return orderStaus;
	}

	public void setTaskStatus(OrderStaus orderStaus) {
		this.orderStaus = orderStaus;
	}

	public Appraise getOrder() {
		return order;
	}

	public void setOrder(Appraise order) {
		this.order = order;
	}

	public Integer getId() {
		return id;
	}


	@Override
	public String toString() {
		return "Orders [id=" + id + ", receiveUser=" + receiveUser + ", task=" + task + ", coupon=" + coupon
				+ ", price=" + price + ", buyWay=" + buyWay + ", createDate=" + createDate + ", finishTime="
				+ finishTime + ", taskStatus=" + orderStaus + ", order=" + order + "]";
	}

	

	
}
