package com.example.administrator.helper.entity;

public class Details {
  private int userId;
  private User user;
  
  
public Details(int userId, User user) {
	super();
	this.userId = userId;
	this.user = user;
}
public int getUserId() {
	return userId;
}
public void setUserId(int userId) {
	this.userId = userId;
}
public User getUser() {
	return user;
}
public void setUser(User user) {
	this.user = user;
}
@Override
public String toString() {
	return "Details [userId=" + userId + ", user=" + user + "]";
}
  
}
