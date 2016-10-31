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
	private Integer father;//父级评论id
	private Integer share;//分享
	private String cotent;//评论内容
	private Timestamp sendTime;//评论时间
	private boolean isLast;//是否最后一级评论
	
	
	public Comment() {
		super();
	}
	
	

	public Comment(User publishUser, Integer father, Integer share, String cotent, Timestamp sendTime, boolean isLast) {
		super();
		this.publishUser = publishUser;
		this.father = father;
		this.share = share;
		this.cotent = cotent;
		this.sendTime = sendTime;
		this.isLast = isLast;
	}



	public Comment(Integer id, User publishUser, Integer father, Integer share, String cotent, Timestamp sendTime,
			boolean isLast) {
		super();
		this.id = id;
		this.publishUser = publishUser;
		this.father = father;
		this.share = share;
		this.cotent = cotent;
		this.sendTime = sendTime;
		this.isLast = isLast;
	}

	public boolean isLast() {
		return isLast;
	}
	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}
	public void setCotent(String cotent) {
		this.cotent = cotent;
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
	public Integer getFather() {
		return father;
	}
	public void setFather(Integer father) {
		this.father = father;
	}
	public Integer getShare() {
		return share;
	}
	public void setShare(Integer share) {
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
		return "Comment [id=" + id + ", publishUser=" + publishUser + ", father=" + father + ", share=" + share
				+ ", cotent=" + cotent + ", sendTime=" + sendTime + ", isLast=" + isLast + "]";
	}
	
	
}
