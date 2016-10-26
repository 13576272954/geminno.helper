package com.example.administrator.helper.entity;

import java.sql.Timestamp;

/**
 * 实体类
 * 优惠券
 * @author Administrator
 *
 */
public class Coupon {
	private Integer id;//优惠券id
	private User user;//所属用户
	private Integer reduce;//减免金额
	private Integer status;//状态
	//1、未使用2、已使用3、已过期
	private Timestamp gainTime;//获得时间
	private Timestamp outTime;//过期时间
	
	
	public Coupon(Integer id, Integer reduce, Integer status, Timestamp gainTime, Timestamp outTime) {
		super();
		this.id = id;
		this.reduce = reduce;
		this.status = status;
		this.gainTime = gainTime;
		this.outTime = outTime;
	}

	public Coupon(Integer id, User user, Integer reduce, Integer status, Timestamp gainTime, Timestamp outTime) {
		super();
		this.id = id;
		this.user = user;
		this.reduce = reduce;
		this.status = status;
		this.gainTime = gainTime;
		this.outTime = outTime;
	}

	public Coupon() {
		// TODO Auto-generated constructor stub
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getReduce() {
		return reduce;
	}

	public void setReduce(Integer reduce) {
		this.reduce = reduce;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getGainTime() {
		return gainTime;
	}

	public void setGainTime(Timestamp gainTime) {
		this.gainTime = gainTime;
	}

	public Timestamp getOutTime() {
		return outTime;
	}

	public void setOutTime(Timestamp outTime) {
		this.outTime = outTime;
	}

	public Integer getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Coupon [id=" + id + ", user=" + user + ", reduce=" + reduce + ", status=" + status + ", gainTime="
				+ gainTime + ", outTime=" + outTime + "]";
	}
	
}
