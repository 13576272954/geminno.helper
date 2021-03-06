package com.example.administrator.helper.send.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Information;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.utils.TimestampTypeAdapter;
import com.example.administrator.helper.utils.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TalkingActivity extends AppCompatActivity  implements EMMessageListener {

    // 聊天信息输入框
    private EditText inputText;
    // 发送按钮
    private Button send;
    //标题栏
    ImageView back;
    TextView otherUserName;


    // 消息监听器
    private EMMessageListener mMessageListener;
    // 当前聊天的 ID
    private String mChatId;
    // 当前会话对象
    private EMConversation mConversation;


    private ListView msgListView;
    private MsgAdapter adapter;

//    存放信息的集合
    private List<Msg> msgList = new ArrayList<Msg>();

    User thisUser;
    User otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talking);
        // 获取当前会话的username(如果是群聊就是群id)
        String str = getIntent().getStringExtra("user");
         GsonBuilder gb=new GsonBuilder();
         gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
         gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
         Gson gson = gb.create();
        otherUser=gson.fromJson(str,User.class);
        thisUser=((MyApplication)getApplication()).getUser();
//        thisUser =new User(3,"333","33",null,"男",22,null,null,null,null,null,null,new Timestamp(System.currentTimeMillis()),"333","http://pic.qqtn.com/up/2016-9/2016091811555278855.jpg");
//        otherUser =new User(1,"11","111",11111,"男",20,null,null,null,null,null,null,new Timestamp(System.currentTimeMillis()),"12345","http://pic.qqtn.com/up/2016-9/2016091811555278855.jpg");

        Log.i("TalkingActivity", "onCreate:  "+EMClient.getInstance());

        mChatId = otherUser.getPhoneNumber();

        mMessageListener = this;


        initView();
        initConversation();

        initMsgs();

    }

    /**
     * 初始化界面
     */
    private void initView() {
        otherUserName = (TextView) findViewById(R.id.tv_other_user);
        otherUserName.setText(otherUser.getName());
        back = (ImageView) findViewById(R.id.iv_fanhui);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter = new MsgAdapter(TalkingActivity.this, R.layout.qipao, msgList);
        inputText = (EditText)findViewById(R.id.input_text);
        send = (Button)findViewById(R.id.send);
        msgListView = (ListView)findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
        // 设置发送按钮的点击事件
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    inputText.setText("");
                    // 创建一条新消息，第一个参数为消息内容，第二个为接受者username
                    EMMessage message = EMMessage.createTxtSendMessage(content, mChatId);
                    // 将新的消息内容和时间加入到下边
                    Msg msg = null;
                    if(!"".equals(content)) {
                        msg= new Msg(content, Msg.TYPE_SEND,thisUser);
                        msgList.add(msg);
                        adapter.notifyDataSetChanged();
                        msgListView.setSelection(msgList.size());
                        inputText.setText("");
                    }
                    // 调用发送消息的方法
                    EMClient.getInstance().chatManager().sendMessage(message);
                    // 为消息设置回调
                    final Msg mMsg = msg;
                    message.setMessageStatusCallback(new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            // 消息发送成功，打印下日志，正常操作应该去刷新ui
                            Log.i("lzan13", "send message on success");
                            //向数据库插入聊天信息
                            Information information = new Information(thisUser.getId(),otherUser.getId(),mMsg.getContent(),new Timestamp(System.currentTimeMillis()));
                            saveMsg(information);
                        }

                        @Override
                        public void onError(int i, String s) {
                            // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                            Log.i("lzan13", "send message on error " + i + " - " + s);
                            msgList.remove(mMsg);
                            adapter.notifyDataSetChanged();
                            msgListView.setSelection(msgList.size());
                            Toast.makeText(TalkingActivity.this,"消息发送失败",Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onProgress(int i, String s) {
                            // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
                        }
                    });
                }
            }
        });
    }


    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {

        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是绘画类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EMClient.getInstance().chatManager().getConversation(mChatId, null, true);
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mChatId);
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
        }

    }

    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    // 将新的消息内容和时间加入到下边
                    msgList.add(new Msg(body.getMessage(),Msg.TYPE_RECEIVED,otherUser));
                    adapter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());
                    break;
            }
        }
    };


    public void saveMsg(Information information){
        GsonBuilder gb=new GsonBuilder();
        gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
        gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
        Gson gson = gb.create();
        String informationStr=gson.toJson(information);
        String url = UrlUtils.MYURL+"SaveInformationServlet";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("information",informationStr);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initMsgs() {
        String url = UrlUtils.MYURL+"GetInformationServlet";
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("thisUserId",thisUser.getId()+"");
        params.addQueryStringParameter("friendUserId",otherUser.getId()+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("TalkingActivity", "onSuccess:  "+result);
                GsonBuilder gb=new GsonBuilder();
                gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
                gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
                Gson gson = gb.create();
                List<Information> informations = gson.fromJson(result, new TypeToken<List<Information>>(){}.getType());
                Log.i("TalkingActivity", "onSuccess:  111111");
                for (Information information : informations ) {
                    Log.i("TalkingActivity", "onSuccess:  22222"+information.getId());
                    if (information.getSendUser()==thisUser.getId()){
                        msgList.add(new Msg(information.getValue(),Msg.TYPE_SEND,thisUser));
                    }else if (information.getReveiveUser()==thisUser.getId()){
                        msgList.add(new Msg(information.getValue(),Msg.TYPE_RECEIVED,otherUser));
                    }
                }
                adapter.notifyDataSetChanged();
                msgListView.setSelection(msgList.size());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("TalkingActivity", "onError:  失败:"+ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.i("TalkingActivity", "onFinished:  ");
            }
        });
    }


    /**
     * 收到的消息处理
     * @param list
     */
    @Override
    public void onMessageReceived(List<EMMessage> list) {
        // 循环遍历当前收到的消息
        for (EMMessage message : list) {
            if (message.getFrom().equals(mChatId)) {
                // 设置消息为已读
                mConversation.markMessageAsRead(message.getMsgId());

                // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                Message msg = mHandler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                mHandler.sendMessage(msg);
            } else {
                // 如果消息不是当前会话的消息发送通知栏通知
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
    }
}
