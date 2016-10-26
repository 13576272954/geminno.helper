package com.example.administrator.helper.entity;

import java.sql.Timestamp;

/**
 * 实体类
 * 积分
 * @author Administrator
 *
 */
public class Accumulate {
	private Integer id;//积分交易id
	private Integer userID;//用户id
	private Integer amount;//交易数额
	private Timestamp businessTime;//交易时间
	private String businessContent;//交易说明
	
	
	public Accumulate() {
		super();
	}
	
	
	public Accumulate(Integer userID, Integer amount, Timestamp businessTime, String businessContent) {
		super();
		this.userID = userID;
		this.amount = amount;
		this.businessTime = businessTime;
		this.businessContent = businessContent;
	}
	

	public Accumulate(Integer id, Integer userID, Integer amount, Timestamp businessTime, String businessContent) {
		super();
		this.id = id;
		this.userID = userID;
		this.amount = amount;
		this.businessTime = businessTime;
		this.businessContent = businessContent;
	}


	public Integer getId() {
		return id;
	}
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Timestamp getBusinessTime() {
		return businessTime;
	}
	public void setBusinessTime(Timestamp businessTime) {
		this.businessTime = businessTime;
	}
	public String getBusinessContent() {
		return businessContent;
	}
	public void setBusinessContent(String businessContent) {
		this.businessContent = businessContent;
	}


	@Override
	public String toString() {
		return "Accumulate [id=" + id + ", userID=" + userID + ", amount=" + amount + ", businessTime=" + businessTime
				+ ", businessContent=" + businessContent + "]";
	}
	
}
