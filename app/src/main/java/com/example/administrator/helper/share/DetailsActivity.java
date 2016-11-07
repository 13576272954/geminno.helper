package com.example.administrator.helper.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.View.NoScrollListview;
import com.example.administrator.helper.entity.ClickLike;
import com.example.administrator.helper.entity.Comment;
import com.example.administrator.helper.entity.Details;
import com.example.administrator.helper.entity.ShareEntity;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.MyGridView;
import com.example.administrator.helper.utils.TimestampTypeAdapter;
import com.example.administrator.helper.utils.UrlUtils;
import com.example.administrator.helper.utils.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import cn.sharesdk.framework.ShareSDK;
//import cn.sharesdk.onekeyshare.OnekeyShare;

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
    TextView tvfh;
    MyGridView mygrid;
    RadioButton rdzan;
    ImageView rdpin;
    ImageView imfx;
    int shareId;
    Timestamp sentTime;
    ClickLike clickLike;
    ListView commentList;
    Map<Comment , List<Comment>> comments=new HashMap<Comment , List<Comment>>();

    FatherAdapter fatherAdapter;
    Comment sendComment;//要发表的评论
    private MyPopWindowBottom myPopWindowBottom;//屏幕底部popwindow
    private String comment = "";        //记录评论输入对话框中的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent=getIntent();
        Gson gson=new Gson();
        shareEntity=gson.fromJson(intent.getStringExtra("shareEntity"),ShareEntity.class);
        Type type = new TypeToken<List<String>>(){}.getType();
        images=gson.fromJson(intent.getStringExtra("images"),type);
        shareId=shareEntity.getDynamic().getId();

        imtx= (ImageView) findViewById(R.id.im_touxiang1);
        imfx= (ImageView) findViewById(R.id.im_fx);
        tvmc= (TextView) findViewById(R.id.tv_mncheng1);
        tvsj= (TextView) findViewById(R.id.tv_shijian1);
        tvnr= (TextView) findViewById(R.id.tv_neirong1);
        tvdz= (TextView) findViewById(R.id.tv_address);
        tvshu= (TextView) findViewById(R.id.tv_count);
        tvming= (TextView) findViewById(R.id.tv_uname);
        tvfh= (TextView) findViewById(R.id.tv_fhui);
        mygrid= (MyGridView) findViewById(R.id.grideview_share1);
        rdzan= (RadioButton) findViewById(R.id.im_zan);
        rdpin = (ImageView) findViewById(R.id.im_pin);
        commentList = (ListView) findViewById(R.id.list_data);
        myPopWindowBottom = new MyPopWindowBottom(this, new PoPListener());
        rdzan.setChecked(shareEntity.isCheck());
        initData();
        initEven();

        x.image().bind(imtx, UrlUtils.MYURL + "/" +shareEntity.getDynamic().getUser().getImage());
        tvmc.setText(shareEntity.getDynamic().getUser().getName());
        tvsj.setText(shareEntity.getDynamic().getSentTime().toString());
        tvnr.setText(shareEntity.getDynamic().getShare());
        tvdz.setText(shareEntity.getDynamic().getAddress());
        tvshu.setText(shareEntity.getDynamic().getCount()+ "");

        //返回
        tvfh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //点赞
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
        //评论
        rdpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myPopWindowBottom==null) {
                    myPopWindowBottom = new MyPopWindowBottom(DetailsActivity.this, new PoPListener());
                }
                sendComment=new Comment();
                sendComment.setFather(null);
                sendComment.setPublishUser(((MyApplication)getApplication()).getUser());
                sendComment.setShare(shareId);
                myPopWindowBottom.showAtLocation(findViewById(R.id.xiangqing), Gravity.BOTTOM, 0, 0);//这种方式无论有虚拟按键还是没有都可完全显示，因为它显示的在整个父布局中
                myPopWindowBottom.onFocusChange(true,DetailsActivity.this);
            }
        });
        //分享点击事件
        imfx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**showShare();*/
            }
        });

        //listView赋值
    }

    public void initData(){
        getdata();
        getComments();//获取评论的map集合
    }
    public void initEven(){
        myPopWindowBottom.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                myPopWindowBottom.onFocusChange(false,DetailsActivity.this);
                sendComment=null;
            }
        });
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
        clickLike=new ClickLike(((MyApplication)getApplication()).getUser().getId(),shareId,sentTime);
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
        clickLike=new ClickLike(((MyApplication)getApplication()).getUser().getId(),shareId,sentTime);
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

    /**
     * 获取评论集合
     */
    private void getComments(){
        String url = UrlUtils.MYURL+"GetCommentServlet";
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("shareId",shareId+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                GsonBuilder gb=new GsonBuilder();
                gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
                gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
                Gson gson = gb.create();
                Type type = new TypeToken<List<Comment>>() {
                }.getType();
                List<Comment> comments1 = gson.fromJson(result,type);
                Comment comment = null;
                for (Comment comm: comments1) {
                    if (comm.getFather()==null){
                        comment = comm;
                        comments.put(comment,null);
                    }else if (comm.getFather()!=null&&comment!=null){
                        if (comments.get(comment)==null) {
                            List<Comment> c = new ArrayList<Comment>();
                            c.add(comm);
                            comments.put(comment,c);
                        }else {
                            comments.get(comment).add(comm);
                        }
                    }
                }
                if (fatherAdapter == null) {
                    List<Comment>  list = new ArrayList<Comment>();
                    for (Comment c:comments.keySet()) {
                        list.add(c);
                    }
                    Log.i("SharePageFragment", "onSuccess:  shareAdapter == null");
                    fatherAdapter = new FatherAdapter(DetailsActivity.this, list, R.layout.comment_item_list);
                    commentList.setAdapter(fatherAdapter);
                } else {
                    Log.i("SharePageFragment", "onSuccess:  shareAdapter != null");
                    fatherAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(DetailsActivity.this,"获取评论失败",Toast.LENGTH_SHORT);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
//    //分享
//    private void showShare() {
//        ShareSDK.initSDK(this);
//        OnekeyShare oks = new OnekeyShare();
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//
//// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
//        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle("标题");
//        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText("我是分享文本");
//        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用
//        oks.setUrl("http://sharesdk.cn");
//        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
//        // site是分享此内容的网站名称，仅在QQ空间使用
//        oks.setSite("ShareSDK");
//        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//        oks.setSiteUrl("http://sharesdk.cn");
//
//// 启动分享GUI
//        oks.show(this);
//    }

    class FatherAdapter extends CommonAdapter<Comment>{

        public FatherAdapter(Context context, List<Comment> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, final Comment comment, int position) {
            TextView Name = viewHolder.getViewById(R.id.comment_name);
            TextView Time = viewHolder.getViewById(R.id.comment_time);
            TextView Content = viewHolder.getViewById(R.id.comment_content);
            Button Reply = viewHolder.getViewById(R.id.but_comment_reply);
            NoScrollListview listReply = viewHolder.getViewById(R.id.no_scroll_list_reply);
            Log.i("FatherAdapter", "convert:  1111"+comment.getPublishUser());
            Log.i("FatherAdapter", "convert:  2222"+Name);
            Name.setText(comment.getPublishUser().getName());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            String timeStr = df.format(comment.getSendTime());
            Time.setText(timeStr);
            Content.setText(comment.getCotent());
            Reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //发表评论
                    if (myPopWindowBottom==null) {
                        myPopWindowBottom = new MyPopWindowBottom(DetailsActivity.this, new PoPListener());
                    }
                    sendComment=new Comment();
                    sendComment.setFather(comment);
                    sendComment.setPublishUser(((MyApplication)getApplication()).getUser());
                    sendComment.setShare(shareId);
                    myPopWindowBottom.showAtLocation(findViewById(R.id.xiangqing), Gravity.BOTTOM, 0, 0);//这种方式无论有虚拟按键还是没有都可完全显示，因为它显示的在整个父布局中
                    myPopWindowBottom.onFocusChange(true,DetailsActivity.this);
                }
            });

            listReply.setDividerHeight(0);
            listReply.setTag(position);
            CommentAdapter commentAdapter = null;//子listView适配器
            if (commentAdapter==null){
                commentAdapter=new CommentAdapter(DetailsActivity.this,comments.get(comment),R.layout.item_comment);
                listReply.setAdapter(commentAdapter);
            }else {
                commentAdapter.notifyDataSetChanged();
            }
            listReply.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (myPopWindowBottom==null) {
                        myPopWindowBottom = new MyPopWindowBottom(DetailsActivity.this, new PoPListener());
                    }
                    sendComment=new Comment();
                    sendComment.setPublishUser(((MyApplication)getApplication()).getUser());
                    sendComment.setShare(shareId);
                    sendComment.setFather(comments.get(comment).get(i));
                    myPopWindowBottom.showAtLocation(findViewById(R.id.xiangqing), Gravity.BOTTOM, 0, 0);//这种方式无论有虚拟按键还是没有都可完全显示，因为它显示的在整个父布局中
                    myPopWindowBottom.onFocusChange(true,DetailsActivity.this);
                }
            });

        }
    }

    /**
     * 不可滑动的listView的适配器
     */
    class CommentAdapter extends CommonAdapter<Comment>{

        public CommentAdapter(Context context, List<Comment> lists, int layoutId) {
            super(context, lists, layoutId);
        }

        @Override
        public void convert(ViewHolder viewHolder, Comment comment, int position) {
            //找控件赋值
            TextView sendUser = viewHolder.getViewById(R.id.tv_user_send);
            TextView huiFu = viewHolder.getViewById(R.id.tv_huifu);
            TextView receiveUser = viewHolder.getViewById(R.id.tv_user_receive);
            TextView content = viewHolder.getViewById(R.id.tv_comment_content);
            content.setText(comment.getCotent());
            if (comment.getFather()!=null){
                sendUser.setText(comment.getPublishUser().getName());
                huiFu.setText("回复");
                receiveUser.setText(comment.getFather().getPublishUser().getName()+":");
            }else {
                sendUser.setText(comment.getPublishUser().getName()+":");
                huiFu.setText("");
                receiveUser.setText("");
            }
        }
    }

    /**
     * PopWindow按钮点击事件
     */
    class PoPListener implements MyPopWindowListener {
        @Override
        public void firstItem(EditText editText) {
            //封装对象
            comment = editText.getText().toString();
            sendComment.setCotent(comment);
            comment="";
            editText.setText("");
            sendComment.setSendTime(new Timestamp(System.currentTimeMillis()));
//            //刷新界面
//            if (sendComment.getFather()==null) {
//                comments.get(shareId).add(sendComment);
//                shareAdapter.notifyDataSetChanged();
//            }
            //网络访问
            String url = UrlUtils.MYURL+"SendCommentServlet";
            RequestParams params = new RequestParams(url);
            GsonBuilder gb = new GsonBuilder();
            gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
            gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
            Gson gson = gb.create();
            String commentStr = gson.toJson(sendComment);
            params.addBodyParameter("comment",commentStr);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if (result.equals("success")){
                        Log.i("PoPListener", "onSuccess:  发表成功");
                        sendComment=null;
                        myPopWindowBottom.onFocusChange(false,DetailsActivity.this);
                        myPopWindowBottom.dismiss();

                    }else{
                        Log.i("PoPListener", "onSuccess:  asjbfakjhsf");
                        Toast.makeText(DetailsActivity.this,"发表评论失败",Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("PoPListener", "onError:  "+ex);
                    Toast.makeText(DetailsActivity.this,"发表评论失败",Toast.LENGTH_LONG);
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



    //隐藏PopWindow
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN){
                View v = getCurrentFocus();
                myPopWindowBottom.onFocusChange(false,DetailsActivity.this);
                myPopWindowBottom.dismiss();
            }
            if (getWindow().superDispatchTouchEvent(ev)) {
                return true;
            }
        return super.onTouchEvent(ev);
    }

}
