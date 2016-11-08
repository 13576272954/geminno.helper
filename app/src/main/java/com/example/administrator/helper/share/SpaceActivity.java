package com.example.administrator.helper.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.ClickLike;
import com.example.administrator.helper.entity.ShareEntity;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.MyGridView;
import com.example.administrator.helper.utils.RefreshListView;
import com.example.administrator.helper.utils.UrlUtils;
import com.example.administrator.helper.utils.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SpaceActivity extends AppCompatActivity {
    ImageView imfh;
    TextView tvmin;
    RefreshListView respace;
    ShareEntity shareEntity1;

    int shareId;
    Timestamp sentTime;
    ClickLike clickLike;

    int orderFlag = 0;
    int pageNo = 1;
    int pageSize = 5;
    List<ShareEntity> shareEntitiess = new ArrayList<>();
    SpaceAdapter spaceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space);
        //获得跳转数据
        Intent intent=getIntent();
        Gson gson=new Gson();
        shareEntity1=gson.fromJson(intent.getStringExtra("shareEntity"),ShareEntity.class);
        //控件
        imfh= (ImageView) findViewById(R.id.im_fanhi1);
        tvmin= (TextView) findViewById(R.id.min);
        respace= (RefreshListView) findViewById(R.id.view1);

        initData();
        initEven();
    }
    public void initData(){
        getData();
    }
    public void initEven(){

    }
    //获取网络数据
    public void getData(){
        //界面初始化数据：listview显示数据
        //xutils获取网络数据
        String url = UrlUtils.MYURL+"QuerySpaceServlet";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("orderFlag", orderFlag + "");//排序标记
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        MyApplication myApplication= (MyApplication) getApplication();
        requestParams.addQueryStringParameter("thisuser",myApplication.getUser().getId()+"");
        //当前头像用户的id
        int touuserId=shareEntity1.getDynamic().getUserId();
        Log.i("SpaceActivity", "getData: 7777777----"+touuserId);
        requestParams.addQueryStringParameter("touuserId",touuserId+"");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type type = new TypeToken<List<ShareEntity>>(){}.getType();
                List<ShareEntity> newshareEntities=gson.fromJson(result,type);
                //shareEntities.clear();
                shareEntitiess.addAll(newshareEntities);
                if (spaceAdapter==null){
                    spaceAdapter=new SpaceAdapter(SpaceActivity.this,shareEntitiess, R.layout.share_item);
                    respace.setAdapter(spaceAdapter);
                }else {
                    spaceAdapter.notifyDataSetChanged();
                }

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
    class SpaceAdapter extends CommonAdapter<ShareEntity> {

        public SpaceAdapter(Context context, List<ShareEntity> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, final ShareEntity shareEntity, final int position) {
            final List<String> images=new ArrayList<>();
            String[] pictures= shareEntity.getDynamic().getPicture().split(",");
            Log.i("SshaeAdapter", "convert111: ----"+shareEntity.getDynamic().getPicture().split(",").toString());
            //遍历数组加到集合中
            for (int i=0;i<pictures.length;i++){
                images.add(pictures[i]);
                Log.i("SshaeAdapter", "convert: --1111"+pictures[i]);
            }
            Log.i("SshaeAdapter", "convert: "+images.size());
            TextView tvmc = viewHolder.getViewById(R.id.tv_mncheng);
            tvmc.setText(shareEntity.getDynamic().getUser().getName());
            TextView tvsj = viewHolder.getViewById(R.id.tv_shijian);
            tvsj.setText(shareEntity.getDynamic().getSentTime().toString());
            TextView tvdizi=viewHolder.getViewById(R.id.tv_address);
            tvdizi.setText(shareEntity.getDynamic().getAddress());
            TextView tvnr = viewHolder.getViewById(R.id.tv_neirong);
            tvnr.setText(shareEntity.getDynamic().getShare()+        +shareEntity.getDynamic().getId());
            final TextView tvcount = viewHolder.getViewById(R.id.tv_count);
            tvcount.setText(shareEntity.getDynamic().getCount()+ "");
            ImageView imtouxian=viewHolder.getViewById(R.id.im_touxiang);
            x.image().bind(imtouxian, UrlUtils.MYURL + "/" +shareEntity.getDynamic().getUser().getImage() );
            //显示图片
            final MyGridView gridduotu=viewHolder.getViewById(R.id.grideview_share);
            gridduotu.setTag(position);
            gridduotu.setAdapter(new CommonAdapter<String>(SpaceActivity.this,images, R.layout.gridview_item) {
                @Override
                public void convert(ViewHolder viewHolder, String s, final int position) {
                    Log.i("SshaeAdapter", "convert: "+images.get(position)+"------"+position);
                    ImageView imxiance=viewHolder.getViewById(R.id.image);
                    x.image().bind(imxiance, UrlUtils.MYURL+s);
                }
            });
            final RadioButton imz = viewHolder.getViewById(R.id.im_zan);
            imz.setTag(position);//加标记，保证每个checkbox的tag不一样
            imz.setChecked(shareEntity.isCheck());
            //点赞的点击事件
            imz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int count= shareEntity.getDynamic().getCount();
                    if ((int)imz.getTag()==position && shareEntity.isCheck()){
                        Log.i("SshaeAdapter", "onClick:isCheck "+position);
                        imz.setChecked(false);
                        shareEntity.setCheck(false);
                        shareId=shareEntity.getDynamic().getId();
                        sentTime=shareEntity.getDynamic().getSentTime();
                        count--;
                        shareEntity.getDynamic().setCount(count);;
                        tvcount.setText(count +"");
                        deleteThumb();
                    }else if ((int)imz.getTag()==position && !shareEntity.isCheck()){
                        Log.i("SshaeAdapter", "onClick: !isCheck"+position);
                        imz.setChecked(true);
                        shareEntity.setCheck(true);
                        shareId=shareEntity.getDynamic().getId();
                        sentTime=shareEntity.getDynamic().getSentTime();
                        count++;
                        shareEntity.getDynamic().setCount(count);
                        tvcount.setText(count +"");
                        insertThumb();
                    }

                }
            });
        }
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
