package com.example.administrator.helper.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 实体类
 * 分享
 * @author Administrator
 *
 */
public class Share implements Serializable {
	private Integer id;//分享id
	private Integer userID;//用户id
	private String share;//分享内容
	private String picture;//图片
	private Integer count;//访问人数
	private Timestamp sendTim;//发布时间
	private String address;//定位地址

	public Share() {
		super();
	}


	public Share(Integer id, Integer userID, String share, Integer count, Timestamp sendTim, String address) {
		super();
		this.id = id;
		this.userID = userID;
		this.share = share;
		this.count = count;
		this.sendTim = sendTim;
		this.address = address;
	}


	public Share(Integer id, Integer userID, String share, String picture, Integer count, Timestamp sendTim,
				 String address) {
		super();
		this.id = id;
		this.userID = userID;
		this.share = share;
		this.picture = picture;
		this.count = count;
		this.sendTim = sendTim;
		this.address = address;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getShare() {
		return share;
	}

	public void setShare(String share) {
		this.share = share;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Timestamp getSendTim() {
		return sendTim;
	}

	public void setSendTim(Timestamp sendTim) {
		this.sendTim = sendTim;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Share [id=" + id + ", userID=" + userID + ", share=" + share + ", picture=" + picture + ", count="
				+ count + ", sendTim=" + sendTim + ", address=" + address + "]";
	}
	
	
}
