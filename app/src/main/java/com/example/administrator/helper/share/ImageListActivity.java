package com.example.administrator.helper.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.helper.R;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.FileTraversal;
import com.example.administrator.helper.utils.ImageUtil;
import com.example.administrator.helper.utils.ViewHolder;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {
    TextView tvqx;
    ListView lvimage;
    List<FileTraversal> list;
    ImageUtil util;
    CommonAdapter<FileTraversal> adapter;
    public static final int MYREQUESECODC = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        tvqx= (TextView) findViewById(R.id.tv_quxiao1);
        lvimage= (ListView) findViewById(R.id.lv_image);
        //点击取消返回事件
        tvqx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initDate();
        initEven();
    }
    public void initDate(){
        util = new ImageUtil(this);
        list = util.LocalImgFileList();
    }
    public void initEven(){
        if (adapter==null){
            adapter=new CommonAdapter<FileTraversal>(this,list, R.layout.imagelist_item) {
                @Override
                public void convert(ViewHolder viewHolder, FileTraversal fileTraversal, int position) {
                    ImageView imagehead=viewHolder.getViewById(R.id.im_head);
                    TextView tvname=viewHolder.getViewById(R.id.tv_imagename);
                    TextView tvnumb=viewHolder.getViewById(R.id.tv_imagenumb);
                    if (fileTraversal.getFilecontent().get(0)!=null){
                        x.image().bind(imagehead,fileTraversal.getFilecontent().get(0));
                    }
                    tvname.setText(fileTraversal.getFilename());
                    tvnumb.setText("("+fileTraversal.getFilecontent().size()+")");
                }
            };
            lvimage.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
        //item点击事件，跳转选择照片
        lvimage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent=new Intent(ImageListActivity.this,ImageActivity.class);
                intent.putStringArrayListExtra("imagelist", (ArrayList<String>) list.get(position).getFilecontent());
                startActivityForResult(intent,MYREQUESECODC);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==MYREQUESECODC&&resultCode==RESULT_OK){
            List<String> list=data.getStringArrayListExtra("image");
            Log.i("ImageListActivity", "onActivityResult: ----"+"---"+list.size());
            Intent intent=new Intent();
            intent.putStringArrayListExtra("image", (ArrayList<String>) list);
            setResult(RESULT_OK,intent);
        }
            finish();
    }
}
