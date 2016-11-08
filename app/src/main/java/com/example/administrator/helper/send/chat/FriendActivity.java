package com.example.administrator.helper.send.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.View.CircularImageView;
import com.example.administrator.helper.entity.Friend;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.RefreshListView;
import com.example.administrator.helper.utils.TimestampTypeAdapter;
import com.example.administrator.helper.utils.UrlUtils;
import com.example.administrator.helper.utils.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class FriendActivity extends AppCompatActivity {
    List<Friend> friends;//好友关系集合
    List<User> users = new ArrayList<User>(); //好友集合
    User my;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.rl_talk_toolbar)
    LinearLayout rlTalkToolbar;
    @InjectView(R.id.list_friend)
    ListView listFriend;
    friendAdapter friendAdapter;
    @InjectView(R.id.tv_add_friend)
    ImageView tvAddFriend;

    List<String> usernames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        ButterKnife.inject(this);

        //获取当前用户
        my = ((MyApplication) getApplication()).getUser();
        //获取当前用户好友集合
        getFriend();
        initEvent();
    }

    private void getFriend() {
        String url = UrlUtils.MYURL + "SelectFriendServlet";
        RequestParams params = new RequestParams(url);
        GsonBuilder gb = new GsonBuilder();
        gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
        gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
        final Gson gson = gb.create();
        String userStr = gson.toJson(my);
        params.addQueryStringParameter("user", userStr);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Type type = new TypeToken<List<Friend>>() {
                }.getType();
                friends = gson.fromJson(result, type);
                Log.i("FriendActivity", "onSuccess:  好友关系"+friends  );
                for (Friend f: friends) {
                    if (f.getUser1().getId()==my.getId()){
                        users.add(f.getUser2());
                    }else if (f.getUser2().getId()==my.getId()){
                        users.add(f.getUser1());
                    }
                }
                Log.i("FriendActivity", "onSuccess:  "+users);
                if (friendAdapter == null) {
                    friendAdapter = new friendAdapter(FriendActivity.this, users, R.layout.item_friend);
                    listFriend.setAdapter(friendAdapter);
                } else {
                    friendAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("FriendActivity", "onError:  获取好友" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initEvent() {
        listFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FriendActivity.this, TalkingActivity.class);
                if (users.size()>0) {
                    User user = users.get(i - 1);
                    GsonBuilder gb = new GsonBuilder();
                    gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
                    gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
                    Gson gson = gb.create();
                    String userStr = gson.toJson(user);
                    intent.putExtra("user", userStr);
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_add_friend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_friend:
                Intent intent = new Intent(FriendActivity.this,AddFriendActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 自定义适配器
     */
    class friendAdapter extends CommonAdapter<User> {

        public friendAdapter(Context context, List<User> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, User user, int position) {
            //找控件赋值
            CircularImageView image = viewHolder.getViewById(R.id.iv_item_user_head);
            TextView nameText = viewHolder.getViewById(R.id.item_user_name);
            TextView signNameText = viewHolder.getViewById(R.id.item_last_talk);
            nameText.setText(user.getName());
            if (user.getSign() != null && "".equals(user.getSign())) {
                signNameText.setText("这个人很懒，什么都没留下");
            } else {
                signNameText.setText(user.getSign());
            }
            x.image().bind(image, user.getImage());
        }
    }

}
