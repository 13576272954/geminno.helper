package com.example.administrator.helper.entity;

import java.sql.Timestamp;

/**
 * 实体类
 * 点赞
 * @author Administrator
 *
 */
public class ClickLike {
	private Integer id;//点赞id
	private Integer userID;//点赞人id
	private Integer shareID;//分享ID
	private Timestamp sendTime;//点赞时间
	
	
	public ClickLike() {
		super();
	}
	
	
	public ClickLike(Integer id, Integer userID, Integer shareID, Timestamp sendTime) {
		super();
		this.id = id;
		this.userID = userID;
		this.shareID = shareID;
		this.sendTime = sendTime;
	}
	

	public ClickLike(Integer userID, Integer shareID, Timestamp sendTime) {
		super();
		this.userID = userID;
		this.shareID = shareID;
		this.sendTime = sendTime;
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
	public Integer getShareID() {
		return shareID;
	}
	public void setShareID(Integer shareID) {
		this.shareID = shareID;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}


	@Override
	public String toString() {
		return "ClickLike [id=" + id + ", userID=" + userID + ", shareID=" + shareID + ", sendTime=" + sendTime + "]";
	}
	
	
}
