package com.example.administrator.helper.send.chat;

import com.example.administrator.helper.entity.User;

/**
 * Created by Administrator on 2016/10/24.
 */
public class Msg {
    public static final int TYPE_RECEIVED = 0;//收到的消息
    public static final int TYPE_SEND = 1;//发送的消息

    private User sendUser;//发送用户
    private String content;//内容
    private int type;//类型

    public Msg(String content, int type,User sendUser) {
        this.content = content;
        this.type = type;
        this.sendUser = sendUser;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public User getSendUser() {
        return sendUser;
    }
}