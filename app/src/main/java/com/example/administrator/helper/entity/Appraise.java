package com.example.administrator.helper.entity;

import java.sql.Timestamp;

/**
 * 实体类
 * 评价
 * @author Administrator
 *
 */
public class Appraise {
	private Integer id;//评价id
	private Integer orderID;//订单id
	private Integer satisfied;//满意度
	private String appraise;//评价
	private String picture;//图片
	private Timestamp creatTime;//评论时间
	
	public Appraise() {
		// TODO Auto-generated constructor stub
	}
	
	
	public Appraise(Integer id, Integer orderID, Integer satisfied, String appraise, String picture, Timestamp creatTime) {
		super();
		this.id = id;
		this.orderID = orderID;
		this.satisfied = satisfied;
		this.appraise = appraise;
		this.picture = picture;
		this.creatTime = creatTime;
	}
	

	public Appraise(Integer orderID, Integer satisfied, String appraise, String picture, Timestamp creatTime) {
		super();
		this.orderID = orderID;
		this.satisfied = satisfied;
		this.appraise = appraise;
		this.picture = picture;
		this.creatTime = creatTime;
	}


	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getOrderID() {
		return orderID;
	}


	public void setOrderID(Integer orderID) {
		this.orderID = orderID;
	}


	public Integer getSatisfied() {
		return satisfied;
	}


	public void setSatisfied(Integer satisfied) {
		this.satisfied = satisfied;
	}


	public String getAppraise() {
		return appraise;
	}


	public void setAppraise(String appraise) {
		this.appraise = appraise;
	}


	public Timestamp getCreatTime() {
		return creatTime;
	}


	public void setCreatTime(Timestamp creatTime) {
		this.creatTime = creatTime;
	}


	public Integer getId() {
		return id;
	}


	@Override
	public String toString() {
		return "Appraise [id=" + id + ", orderID=" + orderID + ", satisfied=" + satisfied + ", appraise=" + appraise
				+ ", picture=" + picture + ", creatTime=" + creatTime + "]";
	}

	
	
}
