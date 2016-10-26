package com.example.administrator.helper.send.chat;

/**
 * Created by Administrator on 2016/10/24.
 */
public class Msg {
    public static final int TYPE_RECEIVED = 0;//收到的消息
    public static final int TYPE_SEND = 1;//发送的消息

    private String content;//内容
    private int type;//类型

    public Msg(String content, int type) {
        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}