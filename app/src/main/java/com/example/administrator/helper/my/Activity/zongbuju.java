package com.example.administrator.helper.my.Activity;//package com.example.administrator.helper.my.Activity;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import com.example.administrator.helper.R;
//
//
//public class zongbuju extends AppCompatActivity {
//    ImageButton imageButton;
//    TextView tv_yuer;
//     TextView tv_wodefadan;
//    TextView tv_wodejiedan;
//    TextView tv_shezhi;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_zongbuju);
//
//
//
//        imageButton= (ImageButton) findViewById(R.id.imageButton);
//
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(zongbuju.this,MainActivity2.class);
//                startActivity(intent);
//
//            }
//        });
//        tv_yuer= (TextView) findViewById(R.id.tv_yuer);
//        tv_yuer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(zongbuju.this,Wodeyuer.class);
//                startActivity(intent);
//
//                Log.i("zongbuju", "onClick: 跳转1");
//                finish();
//                Log.i("zongbuju", "onClick: 跳转2");
//            }
//        });
//        tv_wodefadan= (TextView) findViewById(R.id.tv_wodefadan);
//        tv_wodefadan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(zongbuju.this,Wodefadan.class);
//                startActivity(intent);
//
//            }
//        });
//        tv_wodejiedan= (TextView) findViewById(R.id.tv_wodejiedan);
//        tv_wodejiedan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(zongbuju.this,WodeJiedan.class);
//                startActivity(intent);
//
//            }
//        });
//        tv_shezhi= (TextView) findViewById(R.id.tv_shezhi);
//        tv_shezhi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(zongbuju.this,shezhi.class);
//                startActivity(intent);
//
//            }
//        });
//
//
//    }
//}
