package com.example.administrator.helper;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.send.GoPayActivity;
import com.example.administrator.helper.send.chat.FriendActivity;
import com.example.administrator.helper.utils.TimestampTypeAdapter;
import com.example.administrator.helper.utils.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Timestamp;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @InjectView(R.id.iv_user)
    ImageView ivUser;
    @InjectView(R.id.et_name)
    EditText etName;
    @InjectView(R.id.et_psd)
    EditText etPsd;
    @InjectView(R.id.bt_login)
    Button btLogin;
    @InjectView(R.id.bt_forget)
    Button btForget;
    @InjectView(R.id.bt_creat)
    Button btCreat;
    @InjectView(R.id.bt_else)
    Button btElse;

    private SharedPreferences sp;//保存密码的存储类

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp=getSharedPreferences("userInfo", Context.MODE_PRIVATE);//第一个参数，xml文件名；第二个，文件读写权限

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);



    }
    public void choseAutoLogin(String name ,String psd ,SharedPreferences sp,AppCompatActivity context ){
            Log.i("LoginActivity", "choseAutoLogin:  aaaaaaa"+Context.MODE_PRIVATE);
            this.sp = sp;
        getUser(name, psd,context);
    }

    @OnClick({R.id.bt_login, R.id.bt_forget, R.id.bt_creat, R.id.bt_else})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
               String name = etName.getText().toString();
               String psd = etPsd.getText().toString();
                getUser(name,psd,this);
                break;
            case R.id.bt_forget:
                break;
            case R.id.bt_creat:
                break;
            case R.id.bt_else:
                break;
        }
    }

    private void getUser(final String name, final String psd, final AppCompatActivity context){
        String url = UrlUtils.MYURL+"LoginServlet";
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("userName",name);
        params.addQueryStringParameter("userPsd",psd);
        Log.i("dingwei", "getUser:  aaaaaaa"+sp);
        final SharedPreferences.Editor editor = sp.edit();
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                GsonBuilder gb=new GsonBuilder();
                gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
                gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
                Gson gson = gb.create();
                User user=gson.fromJson(result,User.class);
                if (user!=null){
                    Log.i("1111111", "onSuccess:  user:"+user);

                    MyApplication myApplication= (MyApplication) context.getApplication();
                    myApplication.setUser(user);
                    Log.i("LoginActivity", "onSuccess:  登陆账户"+name);
                    editor.putString("userName",name);
                    editor.putString("userPsd",psd);
                    editor.putBoolean("autoLogin",true);
                    editor.commit();

                    loginHuanxin(user.getPhoneNumber(),user.getPassword(),context);

                }else {
                    Toast.makeText(context, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context,LoginActivity.class);
                    context.finish();
                    context.startActivity(intent);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("LoginActivity", "LoginActivity:onError失败"+ex.getMessage());
                Toast.makeText(context, "网络访问失败", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,LoginActivity.class);
                context.finish();
                context.startActivity(intent);
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.i("LoginActivity", "LoginActivity:onCancelled 取消");
            }

            @Override
            public void onFinished() {
                Log.i("LoginActivity", "LoginActivity:onFinished 完成");
            }
        });
    }

    public void loginHuanxin(String name , String psd, final AppCompatActivity context){
        EMClient.getInstance().login(name, psd, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 加载所有会话到内存

                        EMClient.getInstance().chatManager().loadAllConversations();
                        Intent intent=new Intent(context,MainActivity.class);
                        context.finish();
                        context.startActivity(intent);
                        Log.i("1111111", "run:  环信登陆成功");

                        // 加载所有群组到内存，如果使用了群组的话
//                         EMClient.getInstance().groupManager().loadAllGroups();
                    }
                });
            }

            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("1111111", "登录失败 Error code:" + i + ", message:" + s);
                        /**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         */
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(context, "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(context, "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(context, "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(context, "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(context, "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(context, "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(context, "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(context, "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(context, "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(context, "ml_sign_in_failed code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                        }
                        Intent intent = new Intent(context,LoginActivity.class);
                        context.finish();
                        context.startActivity(intent);
                    }
                });
            }
            @Override
            public void onProgress(int i, String s) {
            }
        });
    }
}
