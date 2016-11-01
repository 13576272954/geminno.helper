package com.example.administrator.helper.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helper.R;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.MyGridView;
import com.example.administrator.helper.utils.ViewHolder;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ImageActivity extends AppCompatActivity {
    TextView tvqux;
    TextView qued;
    MyGridView mygridview;
    List<String> imageList;
    CommonAdapter<String> adapter;
    List<String> fileList=new ArrayList<>();
    Map<Integer,Boolean> checkmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        tvqux= (TextView) findViewById(R.id.tv_quxiao2);
        qued= (TextView) findViewById(R.id.tv_quedin1);
        mygridview= (MyGridView) findViewById(R.id.gride_image);
        initDate();
        initEven();

        //确定点击事件
        qued.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Set<Map.Entry<Integer,Boolean>> set=checkmap.entrySet();
                for (Map.Entry<Integer,Boolean> m: set){
                    if (m.getValue()==true){
                        fileList.add(imageList.get(m.getKey()));
                    }
                }
                if (fileList.size()==0){
                    Toast.makeText(ImageActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    Intent intent =new Intent();
                    intent.putStringArrayListExtra("image", (ArrayList<String>) fileList);
                    Log.i("ImageActivity", "onClick: ---"+"--"+fileList.size());
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });

    }
    public void initDate(){
        Intent intent = getIntent();
        imageList = intent.getStringArrayListExtra("imagelist");
        checkmap=new HashMap<Integer, Boolean>();
        for (int i=0;i<imageList.size();i++){
            checkmap.put(i,false);
        }
    }
    public void initEven(){
        tvqux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        GrildviewSetAdapter();
    }

    public void GrildviewSetAdapter(){
        if (adapter==null){
            adapter=new CommonAdapter<String>(this,imageList, R.layout.image_item) {
                @Override
                public void convert(ViewHolder viewHolder, String s, int position) {
                    ImageView xiangce=viewHolder.getViewById(R.id.image1);
                    CheckBox cb=viewHolder.getViewById(R.id.checkBox);
                    x.image().bind(xiangce,s);
                    if (checkmap.get(position)){
                        cb.setChecked(true);
                    }else{
                        cb.setChecked(false);
                    }
                    //CheckBox的点击事件
                    cb.setTag(position);//标记
                    cb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          int t=(int) view.getTag();
                            if (checkmap.get(t)){
                                checkmap.put(t,false);
                            }else {
                                checkmap.put(t,true);
                            }
                        }
                    });
                }
            };
            mygridview.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }

}
