package com.example.administrator.helper.my.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helper.LoginActivity;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Orders;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.my.util.CommonAdapter;
import com.example.administrator.helper.my.util.ViewHolder;
import com.example.administrator.helper.utils.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.ButterKnife;

public class XianShiGerenXinxi extends AppCompatActivity {
  Toolbar toolbar;
    ImageView imageView_touxiang;
    TextView   tv_sginName;
    TextView  tv_nickname;
    TextView    tv_phoneNumber;
    TextView jifen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xian_shi_geren_xinxi);

     tv_phoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
        imageView_touxiang= (ImageView) findViewById(R.id.imageView_touxiang);
     tv_nickname = (TextView) findViewById(R.id.tv_nickname);
      tv_sginName = (TextView) findViewById(R.id.tv_signName);
        jifen= (TextView) findViewById(R.id.jifen);



            Intent intent = getIntent();
       int  userId=  intent.getIntExtra("JiedanzheId",-1);

        Log.i("XianShiGerenXinxi", "onCreate: intent="+userId);

        RequestParams  requestParams=new RequestParams(UrlUtils.MYURL+"UserSelectServlet");
        requestParams.addQueryStringParameter("userId",userId+"");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("MainActivity", "onSuccess: 222");
                Gson gson = new Gson();
                User user = gson.fromJson(result, User.class);
                if (user != null) {
                    tv_sginName.setText(user.getSign());
                    tv_nickname.setText(user.getName());
                    tv_phoneNumber.setText(user.getPhoneNumber());
                    jifen.setText(user.getPoints());
                    Log.i("MainActivity2", "onSuccess: 图片"+user.getImage());
                    x.image().bind(imageView_touxiang,UrlUtils.MYURL+"image/"+user.getImage());


                }





            };





            @Override
            public void onError(Throwable ex, boolean isOnCallback) {


            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.i("QuanBuFragment", "onFinished: 结束00");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

