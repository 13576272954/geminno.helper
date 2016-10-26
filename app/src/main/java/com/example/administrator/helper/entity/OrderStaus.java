package com.example.administrator.helper.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderStaus implements Parcelable {
	private Integer id;
	private String orderStaus;
	public OrderStaus(Integer id, String orderStaus) {
		super();
		this.id = id;
		this.orderStaus = orderStaus;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getOrderStaus() {
		return orderStaus;
	}
	public void setOrderStaus(String orderStaus) {
		this.orderStaus = orderStaus;
	}
	@Override
	public String toString() {
		return "OrderStaus [id=" + id + ", orderStaus=" + orderStaus + "]";
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.orderStaus);
	}

	protected OrderStaus(Parcel in) {
		this.id = (Integer) in.readValue(Integer.class.getClassLoader());
		this.orderStaus = in.readString();
	}

	public static final Parcelable.Creator<OrderStaus> CREATOR = new Parcelable.Creator<OrderStaus>() {
		@Override
		public OrderStaus createFromParcel(Parcel source) {
			return new OrderStaus(source);
		}

		@Override
		public OrderStaus[] newArray(int size) {
			return new OrderStaus[size];
		}
	};
}
