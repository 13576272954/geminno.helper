package com.example.administrator.helper.send.chat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.ViewHolder;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class FriendActivity extends AppCompatActivity {
    List<User> users ;//好友集合
    User my;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        //获取当前用户
        my=((MyApplication)getApplication()).getUser();
        //获取当前用户好友集合
        getFriend(my.getId());
    }

    private List<User> getFriend(int UserId ){
        Log.i("1111111", "getFriend:  2324234234");
        try {
            List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
            Log.i("1111111", "getFriend:  "+usernames);
        } catch (HyphenateException e) {
            e.printStackTrace();
            Log.i("1111111", "getFriend:  "+e);
        }
        return  null;
    }

    /**
     * 自定义适配器
     */
    class friendAdapter extends CommonAdapter<User>{

        public friendAdapter(Context context, List lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, User user, int position) {
            //找控件赋值

        }
    }
}
