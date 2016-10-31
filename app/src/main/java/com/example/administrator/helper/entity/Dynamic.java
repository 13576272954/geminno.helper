package com.example.administrator.helper.entity;

import java.sql.Timestamp;

/**
 * Created by ASUS on 2016/10/26.
 */
public class Dynamic {
    private int id;
    private int UserId;
    private String share;
    private String picture;
    private int count;
    private Timestamp sentTime;
    private User user;


    public Dynamic(int id, int userId, String share, String picture, int count, Timestamp sentTime, User user) {
        super();
        this.id = id;
        UserId = userId;
        this.share = share;
        this.picture = picture;
        this.count = count;
        this.sentTime = sentTime;
        this.user = user;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return UserId;
    }
    public void setUserId(int userId) {
        UserId = userId;
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
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    public Timestamp getSentTime() {
        return sentTime;
    }
    public void setSentTime(Timestamp sentTime) {
        this.sentTime = sentTime;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    @Override
    public String toString() {
        return "Dynamic [id=" + id + ", UserId=" + UserId + ", share=" + share + ", picture=" + picture + ", count="
                + count + ", sentTime=" + sentTime + ", user=" + user + "]";
    }

}
