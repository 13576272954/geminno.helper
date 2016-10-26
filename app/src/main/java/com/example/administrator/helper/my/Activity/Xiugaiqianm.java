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



public class Xiugaiqianm extends AppCompatActivity {
    Toolbar toolbar;
    EditText et;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Xiugaiqianm", "onCreate: 111");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiugaiqianm);
        toolbar= (Toolbar) findViewById(R.id.toobar2);
        toolbar.setNavigationIcon(R.mipmap.a2);
        button= (Button) findViewById(R.id.button2);
        et= (EditText) findViewById(R.id.editText_qianming);


        //设置toolbar的导航图标点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyToolBarActivity", "NavigationOnClickListener: onClick");
                Intent intent=new Intent(Xiugaiqianm.this,MainActivity2.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url= UrlUtils.MYURL+"UserSevlet";//访问网络的url
                RequestParams requestParams=new RequestParams(url);
                requestParams.addQueryStringParameter("userId",((MyApplication)getApplication()).getUser().getId()+"");
                Log.i("Xiugaiqianm", "onClick: 222");
                requestParams.addQueryStringParameter("signName",et.getText().toString());
                Log.i("Xiugaiqianm", "onClick: 333"+et.getText().toString());

                x.http().get(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i("Xiugaiqianm", "onSuccess: 4444");
                        Gson gson = new Gson();
                        User user=gson.fromJson(result,User.class);
                        Log.i("Xiugaiqianm", "onSuccess: user"+user);
                        if (user!=null) {
                            Intent intent = new Intent(Xiugaiqianm.this, MainActivity2.class);
                            intent.putExtra("signName", user.getSign());
                            Log.i("Xiugaiqianm", "onSuccess: 555  " + user.getSign());
                            startActivity(intent);
                            onResume();
                        }
                        finish();
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
        });
    }
}
