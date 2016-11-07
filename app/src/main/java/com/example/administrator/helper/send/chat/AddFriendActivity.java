package com.example.administrator.helper.send.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Friend;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.utils.CommonAdapter;
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
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddFriendActivity extends AppCompatActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.tv_add_friend)
    View tvAddFriend;
    @InjectView(R.id.rl_add_toolbar)
    LinearLayout rlAddToolbar;
    @InjectView(R.id.but_sousuo)
    Button butSousuo;
    @InjectView(R.id.ll111)
    LinearLayout ll111;
    @InjectView(R.id.list_jieguo)
    ListView listJieguo;
    @InjectView(R.id.et_who)
    EditText etWho;

    List<User> users;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.iv_back, R.id.but_sousuo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.but_sousuo:
                String name = etWho.getText().toString();
                if (name.equals("")){
                    Toast.makeText(AddFriendActivity.this,"您没有任何输入哦~",Toast.LENGTH_SHORT);
                    return;
                }else {
                    getUsers(name);
                }
                break;
        }
    }

    //查找用户
    public void getUsers(String name){
        String url = UrlUtils.MYURL+"FindUserByNameServlet";
        RequestParams params =new RequestParams(url);
        params.addQueryStringParameter("name",name);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                GsonBuilder gb = new GsonBuilder();
                gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
                gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
                Gson gson = gb.create();
                Type type = new TypeToken<List<User>>() {
                }.getType();
                users = gson.fromJson(result,type);
                if (!(users.size()>0)){
                    Toast.makeText(AddFriendActivity.this,"未找到用户",Toast.LENGTH_SHORT);
                }else {
                    //设置适配器
                    if (userAdapter==null){
                        userAdapter=new UserAdapter(AddFriendActivity.this,users,R.layout.item_sousuo_friend);
                        listJieguo.setAdapter(userAdapter);
                    }else {
                        userAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("AddFriendActivity", "onError:  c查找用户失败"+ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    class UserAdapter extends CommonAdapter<User>{

        public UserAdapter(Context context, List<User> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, final User user, int position) {
            //找控件赋值
            ImageView userImage = viewHolder.getViewById(R.id.img_user_friend);
            TextView userName = viewHolder.getViewById(R.id.tv_name_friend);
            Button addFriend = viewHolder.getViewById(R.id.but_add_friend);
            userName.setText(user.getName());
            x.image().bind(userImage,user.getImage());
            addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = UrlUtils.MYURL+"InsertFriendServlet";
                    RequestParams params = new RequestParams(url);
                    Friend friend =new Friend(0,((MyApplication)getApplication()).getUser(),user,new Timestamp(System.currentTimeMillis()));
                    GsonBuilder gb=new GsonBuilder();
                    gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
                    gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
                    Gson gson = gb.create();
                    String friendStr = gson.toJson(friend);
                    params.addBodyParameter("friend",friendStr);
                    x.http().post(params, new Callback.CommonCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            if (result.equals("success")){
                                Toast.makeText(AddFriendActivity.this,"添加成功!",Toast.LENGTH_SHORT);
                            }else if (result.equals("fail")){
                                Toast.makeText(AddFriendActivity.this,"抱歉，网络异常了!",Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onError(Throwable ex, boolean isOnCallback) {
                            Log.i("UserAdapter", "onError:  添加好友失败"+ex);
                            Toast.makeText(AddFriendActivity.this,"抱歉，网络异常了!",Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onCancelled(CancelledException cex) {

                        }

                        @Override
                        public void onFinished() {

                        }
                    });
                }
            });
        }
    }
}
