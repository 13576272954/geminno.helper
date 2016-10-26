package com.example.administrator.helper.my.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.administrator.helper.R;


public class YuerChongzhi extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuer_chongzhi);
        toolbar= (Toolbar) findViewById(R.id.toobar002);
        toolbar.setNavigationIcon(R.mipmap.a2);
        //设置toolbar的导航图标点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyToolBarActivity", "NavigationOnClickListener: onClick");
                Intent intent=new Intent(YuerChongzhi.this,Wodeyuer.class);
                startActivity(intent);
            }
        });

    }
}
