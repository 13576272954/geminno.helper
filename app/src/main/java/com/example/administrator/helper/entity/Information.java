package com.example.administrator.helper.entity;

import java.sql.Timestamp;

/**
 * 实体类
 * 消息
 * @author Administrator
 *
 */
public class Information {
	private Integer id;//消息id
	private Integer sendUserId;//发送用户
	private Integer reveiveUserId;//接收用户
	private String value;//消息内容
	private Timestamp sendTime;//发送时间
	
	
	public Information() {
		super();
	}
	
	
	public Information(Integer id, Integer sendUserId,Integer reveiveUserId, String value, Timestamp sendTime) {
		super();
		this.id = id;
		this.sendUserId = sendUserId;
		this.reveiveUserId = reveiveUserId;
		this.value = value;
		this.sendTime = sendTime;
	}
	

	public Information(Integer sendUserId, Integer reveiveUserId, String value, Timestamp sendTime) {
		super();
		this.sendUserId = sendUserId;
		this.reveiveUserId = reveiveUserId;
		this.value = value;
		this.sendTime = sendTime;
	}


	public Integer getSendUser() {
		return sendUserId;
	}
	public void setSendUser(Integer sendUserId) {
		this.sendUserId = sendUserId;
	}
	public Integer getReveiveUser() {
		return reveiveUserId;
	}
	public void setReveiveUser(Integer reveiveUserId) {
		this.reveiveUserId = reveiveUserId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getId() {
		return id;
	}
	@Override
	public String toString() {
		return "Information [id=" + id + ", sendUserId=" + sendUserId + ", reveiveUserId=" + reveiveUserId + ", value=" + value
				+ ", sendTime=" + sendTime + "]";
	}
	
	
	
}
