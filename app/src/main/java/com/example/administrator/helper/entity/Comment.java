package com.example.administrator.helper.entity;

import java.sql.Timestamp;

/**
 * 实体类
 * 评论
 * @author Administrator
 *
 */
public class Comment {
	private Integer id;//评论id
	private User publishUser;//发表用户
	private User receiveUser;//接收用户
	private Share share;//分享
	private String cotent;//评论内容
	private Timestamp sendTime;//评论时间
	
	
	public Comment() {
		super();
	}
	
	
	public Comment(Integer id, User publishUser, User receiveUser, Share share, String cotent, Timestamp sendTime) {
		super();
		this.id = id;
		this.publishUser = publishUser;
		this.receiveUser = receiveUser;
		this.share = share;
		this.cotent = cotent;
		this.sendTime = sendTime;
	}
	

	public Comment(User publishUser, User receiveUser, Share share, String cotent, Timestamp sendTime) {
		super();
		this.publishUser = publishUser;
		this.receiveUser = receiveUser;
		this.share = share;
		this.cotent = cotent;
		this.sendTime = sendTime;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public User getPublishUser() {
		return publishUser;
	}
	public void setPublishUser(User publishUser) {
		this.publishUser = publishUser;
	}
	public User getReceiveUser() {
		return receiveUser;
	}
	public void setReceiveUser(User receiveUser) {
		this.receiveUser = receiveUser;
	}
	public Share getShare() {
		return share;
	}
	public void setShare(Share share) {
		this.share = share;
	}
	public Timestamp getSendTime() {
		return sendTime;
	}
	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}
	public String getCotent() {
		return cotent;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", publishUser=" + publishUser + ", receiveUser=" + receiveUser + ", share="
				+ share + ", cotent=" + cotent + ", sendTime=" + sendTime + "]";
	}
	
	
}
