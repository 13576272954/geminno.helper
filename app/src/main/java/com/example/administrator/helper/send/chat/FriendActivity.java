package com.example.administrator.helper.send.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.RefreshListView;
import com.example.administrator.helper.utils.ViewHolder;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FriendActivity extends AppCompatActivity {
    List<User> users;//好友集合
    User my;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.rl_talk_toolbar)
    RelativeLayout rlTalkToolbar;
    @InjectView(R.id.list_friend)
    RefreshListView listFriend;
    List<String> usernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.inject(this);
        loginHuanxin("12345","111");

        //获取当前用户
        my = ((MyApplication) getApplication()).getUser();
        //获取当前用户好友集合
        getFriend(my.getId());
    }

    private List<User> getFriend(int UserId) {
        return null;
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        Log.i("11111111", "onClick:  点击");
//        loginHuanxin("12345","111");

    }

    /**
     * 自定义适配器
     */
    class friendAdapter extends CommonAdapter<User> {

        public friendAdapter(Context context, List lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, User user, int position) {
            //找控件赋值

        }
    }

    public void loginHuanxin(String name, String psd) {
        EMClient.getInstance().login(name, psd, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                Log.i("1111111", "onSuccess:  环信登陆成功回调");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 加载所有会话到内存


                        try {
                            EMClient.getInstance().chatManager().loadAllConversations();
                            Log.i("1111111", "run:  环信登陆成功");
                            usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();

                            Log.i("11111111", "run:  1123"+usernames);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            Log.i("1111111", "getFriend:  错误:" + e.getErrorCode()+"  "+ e.getMessage());
                        }
                        // 加载所有群组到内存，如果使用了群组的话
//                         EMClient.getInstance().groupManager().loadAllGroups();
                        getFriend(12345);
                    }
                });
            }

            /**
             * 登陆错误的回调
             *
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("1111111", "登录失败 Error code:" + i + ", message:" + s);

                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }
}
