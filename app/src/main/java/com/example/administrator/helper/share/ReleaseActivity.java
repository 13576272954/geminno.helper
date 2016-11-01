package com.example.administrator.helper.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Share;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.MyGridView;
import com.example.administrator.helper.utils.ViewHolder;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class ReleaseActivity extends AppCompatActivity {
    Share share =new Share();
    TextView tvqx;
    TextView tvfb;
    EditText edtnr;
    ImageView imtupian;
    MyGridView gridrelase;
    public static final int MYREQUESECODE = 1;
    Integer count=0;
    List<String> fileList=new ArrayList<>();
    CommonAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_release);
        super.onCreate(savedInstanceState);

        initDate();


        //点击事件
        //取消
        tvqx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        imtupian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReleaseActivity.this,ImageListActivity.class);
                Log.i("TianJiaTieziActivit", "onClick: 跳转到添加图片");
                startActivityForResult(intent,MYREQUESECODE);

            }
        });

        //发表
        tvfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String share1=edtnr.getText().toString();//内容
                Log.i("ReleaseActivity", "onClick: share1"+"--"+share1);
                share.setShare(share1);
                MyApplication myApplication=new MyApplication();
                int userID=myApplication.getUser().getId();
                Log.i("ReleaseActivity", "onClick: userID"+"--"+userID);
                share.setUserID(userID);//用户id
                Timestamp sentTime = new Timestamp(System.currentTimeMillis());//创建时间
                Log.i("ReleaseActivity", "onClick: sentTime"+"--"+sentTime);
                share.setSendTim(sentTime);
                share.setCount(count);
                String url = "http://192.168.23.1:8080/Helper/InsertShareServlet";
                RequestParams requestParams = new RequestParams(url);
                Gson gson = new Gson();
                String sharejson = gson.toJson(share);
                requestParams.addBodyParameter("share", sharejson);
                requestParams.setMultipart(true);
                Log.i("ReleaseActivity", "onClick: requestParams=="+requestParams);
                x.http().post(requestParams, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.i("ReleaseActivity", "onClick: --------" + result);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Log.i("ReleaseActivity", "onError: -----------------.." + ex);
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        Log.i("ReleaseActivity", "onFinished: ");
                    }
                });
            }
        });

    }
    //shipeiqi
    public void gridviewSetadapter(){
        if (adapter==null){
            adapter=new CommonAdapter<String>(this,fileList, R.layout.gridview_item) {
                @Override
                public void convert(ViewHolder viewHolder, String s, int position) {
                    ImageView imtupian=viewHolder.getViewById(R.id.image);
                    x.image().bind(imtupian,s);
                }
            };
            gridrelase.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }


//找控件
    public void initDate(){
        tvqx= (TextView) findViewById(R.id.tv_quxiao);
        tvfb= (TextView) findViewById(R.id.tv_fabiao);
        edtnr= (EditText) findViewById(R.id.edt_nr);
        imtupian= (ImageView) findViewById(R.id.im_tupian1);
        gridrelase= (MyGridView) findViewById(R.id.grideview_release);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==MYREQUESECODE&&resultCode==RESULT_OK){
        List<String> newlist=new ArrayList<>();
            newlist=data.getStringArrayListExtra("image");
            fileList.clear();
            fileList.addAll(newlist);
            if (fileList!=null){
                gridviewSetadapter();
            }
        }
    }
}
