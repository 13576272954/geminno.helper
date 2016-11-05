package com.example.administrator.helper.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helper.BaseFragment;
import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.View.NoScrollListview;
import com.example.administrator.helper.View.NoTouchLinearLayout;
import com.example.administrator.helper.entity.ClickLike;
import com.example.administrator.helper.entity.Comment;
import com.example.administrator.helper.entity.ShareEntity;
import com.example.administrator.helper.share.DetailsActivity;
import com.example.administrator.helper.share.ReleaseActivity;
import com.example.administrator.helper.share.ShowImageActivity;
import com.example.administrator.helper.share.SpaceActivity;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.MyGridView;
import com.example.administrator.helper.utils.RefreshListView;
import com.example.administrator.helper.utils.TimestampTypeAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;


/**
 * Created by bin on 2016/9/19.
 */
public class SharePageFragment extends BaseFragment {
    private static int FABU=123;
    ImageView imtianjia;
    RefreshListView lvshare;
    int orderFlag = 0;
    int pageNo = 1;
    int pageSize = 5;
    List<ShareEntity> shareEntities = new ArrayList<>();
    SshaeAdapter shareAdapter;
    CommonAdapter<String> tupianadapter;
    private Button mSendBut;//评论发送按钮
    private NoTouchLinearLayout mLytEdittextVG;//发表评论框
    private EditText mCommentEdittext;//评论输入框
    private boolean isReply;            //发表还是回复评论，true代表回复
    private String comment = "";        //记录评论输入对话框中的内容

    Map<Integer , List<Comment>> comments = new HashMap<Integer , List<Comment>>(); //评论的集合    key:分享id，value:评论集合

    int shareId;
    Timestamp sentTime;
    ClickLike clickLike;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share_page, null);
        imtianjia = (ImageView) v.findViewById(R.id.im_tianjia);
        lvshare = (RefreshListView) v.findViewById(R.id.view);
        mLytEdittextVG = (NoTouchLinearLayout) v.findViewById(R.id.edit_vg_lyt);
        mCommentEdittext = (EditText) v.findViewById(R.id.edit_comment);
        mSendBut = (Button) v.findViewById(R.id.but_comment_send);
        ButterKnife.inject(this, v);
        return v;

    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        imtianjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReleaseActivity.class);
                getActivity().startActivityForResult(intent,FABU);
            }
        });
        lvshare.setOnRefreshUploadChangeListener(new RefreshListView.OnRefreshUploadChangeListener() {
            @Override
            public void onRefresh() {
               pageNo = 1;
               shareEntities.clear();
               getData();

            }

            @Override
            public void onPull() {
                pageNo++;
           getData();
            }
        });
    }

    @Override
    public void initData() {
        getData();
    }

    public void getData() {
        //界面初始化数据：listview显示数据
        //xutils获取网络数据
        String url = UrlUtils.MYURL+"QueryDynamicServlet";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("orderFlag", orderFlag + "");//排序标记
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        Log.i("SharePageFragment", "getData:  user---:"+((MyApplication)getActivity().getApplication()).getUser());
        requestParams.addQueryStringParameter("thisuser",((MyApplication)getActivity().getApplication()).getUser().getId()+"");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("------->", "onSuccess: result" + result);
                //gson解析list<Dynamic>
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type type = new TypeToken<List<ShareEntity>>(){}.getType();
               // shareEntities=gson.fromJson(result,type);
                List<ShareEntity> newshareEntities=gson.fromJson(result,type);
                //shareEntities.clear();
                shareEntities.addAll(newshareEntities);
                for (ShareEntity shareEntity:shareEntities) {
                    Log.i("aaaaaaaaaa", "onSuccess:  "+shareEntity.getDynamic().getId());
                    getComment(shareEntity.getDynamic().getId());
                }
                Log.i("SharePageFragment", "onSuccess: shareEntities" + "--" + shareEntities);
                if (shareAdapter == null) {
                    shareAdapter = new SshaeAdapter(getActivity(), shareEntities, R.layout.share_item);
                    lvshare.setAdapter(shareAdapter);
                } else {
                    shareAdapter.notifyDataSetChanged();
                }
                lvshare.completeRefresh();
                lvshare.completeLoad();
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

    class SshaeAdapter extends CommonAdapter<ShareEntity> {

        public Map<Integer, Integer> getNumbers() {
            return numbers;
        }

        //每个位置的显示数量:key:position,value:分享表中的数量
        Map<Integer,Integer> numbers=new HashMap<>();

        public SshaeAdapter(Context context, List<ShareEntity> lists, int layoutId) {
            super(context, lists, layoutId);

        }

        @Override
        public void convert(final ViewHolder viewHolder, final ShareEntity shareEntity, final int position) {
            //遍历数组把所有地址加入集合中
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
            //内容布局点击事件跳转到详情界面
            RelativeLayout relxq= viewHolder.getViewById(R.id.rel_neirong);
            Log.i("SshaeAdapter", "convert:  "+relxq);
            relxq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson =new Gson();
                    String shareEntityjson=gson.toJson(shareEntity);
                    String imagejson=gson.toJson(images);
                    Intent intent=new Intent(getActivity(),DetailsActivity.class);
                    intent.putExtra("shareEntity",shareEntityjson ) ;
                    intent.putExtra("images",imagejson);
                    startActivity(intent);
                }
            });
            //头像
            ImageView imtouxian=viewHolder.getViewById(R.id.im_touxiang);
            x.image().bind(imtouxian, UrlUtils.MYURL + "/" +shareEntity.getDynamic().getUser().getImage() );
            //头像点击事件,跳转到空间详情界面
            imtouxian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson =new Gson();
                    String shareEntityjson=gson.toJson(shareEntity);
                    Intent intent1=new Intent(getActivity(), SpaceActivity.class);
                    intent1.putExtra("shareEntity",shareEntityjson );
                    startActivity(intent1);
                }
            });


            //显示图片
            final MyGridView gridduotu=viewHolder.getViewById(R.id.grideview_share);
            gridduotu.setTag(position);
            gridduotu.setAdapter(new CommonAdapter<String>(getActivity(),images, R.layout.gridview_item) {
               @Override
               public void convert(ViewHolder viewHolder, String s, final int position) {
                   Log.i("SshaeAdapter", "convert: "+images.get(position)+"------"+position);
                ImageView imxiance=viewHolder.getViewById(R.id.image);
                x.image().bind(imxiance, UrlUtils.MYURL+s);
                   //图片点击事件
                   imxiance.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                          Intent intent=new Intent(getActivity(), ShowImageActivity.class);
                           intent.putExtra("image",images.get(position));
                           Log.i("SshaeAdapter", "66666: "+images.get(position));
                           startActivity(intent);
                       }
                   });
                }
            });
            NoScrollListview noScrollListview = viewHolder.getViewById(R.id.list_comment);
            noScrollListview.setDividerHeight(0);
            noScrollListview.setTag(position);
            CommentAdapter commentAdapter = null;//子listView适配器
            if (commentAdapter==null){
                commentAdapter=new CommentAdapter(getActivity(),comments.get(shareEntity.getDynamic().getId()),R.layout.item_comment);
                noScrollListview.setAdapter(commentAdapter);
            }else {
                commentAdapter.notifyDataSetChanged();
            }
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
                        shareId=shareEntities.get(position).getDynamic().getId();
                        sentTime=shareEntities.get(position).getDynamic().getSentTime();
                        count--;
                        shareEntity.getDynamic().setCount(count);;
                        tvcount.setText(count +"");
                        deleteThumb();
                    }else if ((int)imz.getTag()==position && !shareEntity.isCheck()){
                        Log.i("SshaeAdapter", "onClick: !isCheck"+position);
                        imz.setChecked(true);
                        shareEntity.setCheck(true);
                        shareId=shareEntities.get(position).getDynamic().getId();
                        sentTime=shareEntities.get(position).getDynamic().getSentTime();
                        count++;
                        shareEntity.getDynamic().setCount(count);
                        tvcount.setText(count +"");
                        insertThumb();
                    }

                }
            });
            ImageView imp=viewHolder.getViewById(R.id.im_pin);
            imp.setTag(position);//加标记

        }
    }
   //点赞增加插入数据库

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
     * 获取评论
     * @param shareId
     */
    private void getComment(final int shareId){
        String url = UrlUtils.MYURL+"GetCommentServlet";
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("shareId",shareId+"");
        Log.i("SharePageFragment", "getComment:  shareId"+shareId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            List<Comment> comm = new ArrayList<Comment>();
            @Override
            public void onSuccess(String result) {
                Log.i("SharePageFragment", "onSuccess:  comment"+result);
                if (result!=null) {
                    GsonBuilder gb = new GsonBuilder();
                    gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
                    gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
                    Gson gson = gb.create();
                    Type type = new TypeToken<List<Comment>>() {
                    }.getType();
                    comm = gson.fromJson(result, type);
                    if (comm.size()>0) {
                        comments.put(shareId, comm);
                    }else {
                        comments.put(shareId,null);
                    }
                    if (comments.size()==shareEntities.size()){

                        if (shareAdapter == null) {
                            Log.i("SharePageFragment", "onSuccess:  shareAdapter == null");
                            shareAdapter = new SshaeAdapter(getActivity(), shareEntities, R.layout.share_item);
                            lvshare.setAdapter(shareAdapter);
                        } else {
                            Log.i("SharePageFragment", "onSuccess:  shareAdapter != null");
                            shareAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("SharePageFragment", "onError:  评论获取失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.i("SharePageFragment", "onFinished:  ");
            }
        });
    }

    public void insertThumb() {
        String url = UrlUtils.MYURL+"InsertThumbServlet";
        RequestParams requestParams1 = new RequestParams(url);
        clickLike=new ClickLike(((MyApplication)getActivity().getApplication()).getUser().getId(),shareId,sentTime);
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
        clickLike=new ClickLike(((MyApplication)getActivity().getApplication()).getUser().getId(),shareId,sentTime);
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
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        mCommentEdittext.getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    mCommentEdittext.requestFocus();//获取焦点
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(mCommentEdittext.getWindowToken(), 0);
                }
            }
        }, 100);
    }
    /**
     * 判断对话框中是否输入内容
     */
    private boolean isEditEmply() {
        comment = mCommentEdittext.getText().toString().trim();
        if (comment.equals("")) {
            Toast.makeText(getActivity(), "评论不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        mCommentEdittext.setText("");
        return true;
    }
}