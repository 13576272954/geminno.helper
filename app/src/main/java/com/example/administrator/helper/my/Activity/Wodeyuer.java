package com.example.administrator.helper.my.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.administrator.helper.Fragment.HomePageFragment;
import com.example.administrator.helper.R;


public class Wodeyuer extends AppCompatActivity {
    Toolbar toolbar;
    Button button_tixian;
    Button button_chongzhi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wodeyuer);
        toolbar= (Toolbar) findViewById(R.id.toobar00);
        toolbar.setNavigationIcon(R.mipmap.a2);
        button_tixian= (Button) findViewById(R.id.button_tixian);
        button_chongzhi= (Button) findViewById(R.id.button_chongzhi);

        //设置toolbar的导航图标点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyToolBarActivity", "NavigationOnClickListener: onClick");
               finish();
            }
        });

          button_tixian.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent=new Intent(Wodeyuer.this,YuerTixian.class);
                  startActivity(intent);
              }
          });
   button_chongzhi.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           Intent intent=new Intent(Wodeyuer.this,YuerChongzhi.class);
           startActivity(intent);

       }
   });

    }
}
