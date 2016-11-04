package com.example.administrator.helper.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.administrator.helper.R;
import com.example.administrator.helper.utils.UrlUtils;

import org.xutils.x;

public class ShowImageActivity extends AppCompatActivity {
    ImageView imchuantu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        imchuantu= (ImageView) findViewById(R.id.im_chuantu);
        Intent intent=getIntent();
        String image=intent.getStringExtra("image");
        x.image().bind(imchuantu, UrlUtils.MYURL+image);
    }
}
