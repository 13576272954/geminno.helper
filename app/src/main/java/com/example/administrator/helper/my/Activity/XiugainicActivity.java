package com.example.administrator.helper.my.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.utils.UrlUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;



public class XiugainicActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button button;


    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("XiugainicActivity", "onCreate: 1111");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiugainic);
        button= (Button) findViewById(R.id.button);
        toolbar= (Toolbar) findViewById(R.id.toobar1);
        toolbar.setNavigationIcon(R.mipmap.a2);

      //  View view = LayoutInflater.from(this).inflate(R.layout.xiugai, null);
        et= (EditText)findViewById(R.id.EditText_name);

        //设置toolbar的导航图标点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(XiugainicActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String url= UrlUtils.MYURL+"UserSevlet";//访问网络的url
                RequestParams requestParams=new RequestParams(url);
                requestParams.addQueryStringParameter("userId",((MyApplication)getApplication()).getUser().getId()+"");
                Log.i("XiugainicActivity", "onClick: 22222");
                requestParams.addQueryStringParameter("nickname",et.getText().toString());




                Log.i("XiugainicActivity", "onClick: 333:"+et.getText().toString());
                x.http().get(requestParams ,new Callback.CommonCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        Log.i("XiugainicActivity", "onSuccess: 44444");
                            Gson gson = new Gson();
                        Log.i("XiugainicActivity", "onSuccess: 12345"+gson.fromJson(result,User.class));
                            User user=gson.fromJson(result,User.class);
                            Log.i("XiugainicActivity", "onSuccess: user="+user);
                            if (user!=null){
                                Intent intent=new Intent(XiugainicActivity.this,MainActivity2.class);
                                intent.putExtra("nickName", user.getName());
                                startActivity(intent);
                                onResume();
                                Log.i("XiugainicActivity", "onSuccess: 55555");
                            }
                           // User.setNickname(et.getText().toString());
                            finish();
                        }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.i("XiugainicActivity", "onError: 66666"+ex);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        Log.i("aaa", "onClick: 1155");
                    }
                });




//                Intent intent=new Intent(XiugainicActivity.this,MainActivity.class);
//                startActivity(intent);


            }
        });

    }
}
