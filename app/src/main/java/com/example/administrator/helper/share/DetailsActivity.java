package com.example.administrator.helper.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.ClickLike;
import com.example.administrator.helper.entity.Details;
import com.example.administrator.helper.entity.ShareEntity;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.MyGridView;
import com.example.administrator.helper.utils.UrlUtils;
import com.example.administrator.helper.utils.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    List<Details> detailss=new ArrayList<>();
    List<String> images;
    ShareEntity shareEntity;
    ImageView imtx;
    TextView tvmc;
    TextView tvsj;
    TextView tvnr;
    TextView tvdz;
    TextView tvshu;
    TextView tvming;
    MyGridView mygrid;
    RadioButton rdzan;

    int shareId;
    Timestamp sentTime;
    ClickLike clickLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent=getIntent();
        Gson gson=new Gson();
        shareEntity=gson.fromJson(intent.getStringExtra("shareEntity"),ShareEntity.class);
        Type type = new TypeToken<List<String>>(){}.getType();
        images=gson.fromJson(intent.getStringExtra("images"),type);
        imtx= (ImageView) findViewById(R.id.im_touxiang1);
        tvmc= (TextView) findViewById(R.id.tv_mncheng1);
        tvsj= (TextView) findViewById(R.id.tv_shijian1);
        tvnr= (TextView) findViewById(R.id.tv_neirong1);
        tvdz= (TextView) findViewById(R.id.tv_address);
        tvshu= (TextView) findViewById(R.id.tv_count);
        tvming= (TextView) findViewById(R.id.tv_uname);
        mygrid= (MyGridView) findViewById(R.id.grideview_share1);
        rdzan= (RadioButton) findViewById(R.id.im_zan);
        initData();
        initEven();
        x.image().bind(imtx, UrlUtils.MYURL + "/" +shareEntity.getDynamic().getUser().getImage());
        tvmc.setText(shareEntity.getDynamic().getUser().getName());
        tvsj.setText(shareEntity.getDynamic().getSentTime().toString());
        tvnr.setText(shareEntity.getDynamic().getShare());
        tvdz.setText(shareEntity.getDynamic().getAddress());
        tvshu.setText(shareEntity.getDynamic().getCount()+ "");
        rdzan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count= shareEntity.getDynamic().getCount();
                if ( shareEntity.isCheck()){
                    rdzan.setChecked(false);
                    shareEntity.setCheck(false);
                    shareId=shareEntity.getDynamic().getId();
                    sentTime=shareEntity.getDynamic().getSentTime();
                    count--;
                    shareEntity.getDynamic().setCount(count);;
                    tvshu.setText(count +"");
                    deleteThumb();
                }else if (!shareEntity.isCheck()){
                    rdzan.setChecked(true);
                    shareEntity.setCheck(true);
                    shareId=shareEntity.getDynamic().getId();
                    sentTime=shareEntity.getDynamic().getSentTime();
                    count++;
                    shareEntity.getDynamic().setCount(count);
                    tvshu.setText(count +"");
                    insertThumb();
                }

            }
        });

    }

    public void initData(){
        getdata();
    }
    public void initEven(){

    }
    //获取网络数据
    public void getdata(){
        String url= UrlUtils.MYURL+"SelectClicklikeServlet";
        RequestParams requestParams= new RequestParams(url);
        requestParams.addBodyParameter("shareId",shareEntity.getDynamic().getId()+"");
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson=new Gson();
                Type type = new TypeToken<List<Details>>(){}.getType();
                detailss=gson.fromJson(result,type);
                String uname="";
                //遍历集合中的用户名称
                for (Details details :detailss){
                    uname=uname+details.getUser().getName()+"、";
                }
                tvming.setText(uname);
                mygrid.setAdapter(new CommonAdapter<String>(DetailsActivity.this,images, R.layout.gridview_item) {
                    @Override
                    public void convert(ViewHolder viewHolder, String s, int position) {

                        ImageView imtu=viewHolder.getViewById(R.id.image);
                        x.image().bind(imtu, UrlUtils.MYURL+s);
                    }
                });
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

    //点赞增加插入数据库
    public void insertThumb() {
        String url = UrlUtils.MYURL+"InsertThumbServlet";
        RequestParams requestParams1 = new RequestParams(url);
        MyApplication myApplication=new MyApplication();
        clickLike=new ClickLike(myApplication.getUser().getId(),shareId,sentTime);
        Gson gson = new Gson();
        String clickLikeJson = gson.toJson(clickLike);
        requestParams1.addBodyParameter("clickLikeJson", clickLikeJson);
        x.http().post(requestParams1, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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
    //取消点赞删除数据库
    public void deleteThumb(){
        String url= UrlUtils.MYURL+"DeleteThumbServlet";
        RequestParams requestParams2 = new RequestParams(url);
        MyApplication myApplication=new MyApplication();
        clickLike=new ClickLike(myApplication.getUser().getId(),shareId,sentTime);
        Gson gson = new Gson();
        String clickLikeJson = gson.toJson(clickLike);
        requestParams2.addBodyParameter("clickLikeJson", clickLikeJson);
        x.http().post(requestParams2, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

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

}
