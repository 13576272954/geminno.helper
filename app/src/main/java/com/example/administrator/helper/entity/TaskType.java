package com.example.administrator.helper.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskType implements Parcelable {
	private Integer id;
	private String taskType;
	public TaskType(Integer id, String taskType) {
		super();
		this.id = id;
		this.taskType = taskType;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	@Override
	public String toString() {
		return "TaskType [id=" + id + ", taskType=" + taskType + "]";
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeValue(this.id);
		dest.writeString(this.taskType);
	}

	protected TaskType(Parcel in) {
		this.id = (Integer) in.readValue(Integer.class.getClassLoader());
		this.taskType = in.readString();
	}

	public static final Parcelable.Creator<TaskType> CREATOR = new Parcelable.Creator<TaskType>() {
		@Override
		public TaskType createFromParcel(Parcel source) {
			return new TaskType(source);
		}

		@Override
		public TaskType[] newArray(int size) {
			return new TaskType[size];
		}
	};
}
