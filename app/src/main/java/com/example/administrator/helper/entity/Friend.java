package com.example.administrator.helper.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Friend implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private User user1;
	private User user2;
	private Timestamp creatTime;
	
	
	public Friend() {
		super();
	}


	

	public Friend(int id, User user1, User user2, Timestamp creatTime) {
		super();
		this.id = id;
		this.user1 = user1;
		this.user2 = user2;
		this.creatTime = creatTime;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public User getUser1() {
		return user1;
	}


	public void setUser1(User user1) {
		this.user1 = user1;
	}


	public User getUser2() {
		return user2;
	}


	public void setUser2(User user2) {
		this.user2 = user2;
	}

	public Timestamp getCreatTime() {
		return creatTime;
	}


	public void setCreatTime(Timestamp creatTime) {
		this.creatTime = creatTime;
	}


	@Override
	public String toString() {
		return "Friend [id=" + id + ", user1=" + user1 + ", user2=" + user2 + "]";
	}
	
}
