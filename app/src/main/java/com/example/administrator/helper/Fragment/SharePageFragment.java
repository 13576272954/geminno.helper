package com.example.administrator.helper.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.helper.BaseFragment;
import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.View.NoScrollListview;
import com.example.administrator.helper.entity.ClickLike;
import com.example.administrator.helper.entity.Comment;
import com.example.administrator.helper.entity.ShareEntity;
import com.example.administrator.helper.share.ReleaseActivity;
import com.example.administrator.helper.utils.CommonAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

;


/**
 * Created by bin on 2016/9/19.
 */
public class SharePageFragment extends BaseFragment {
    ImageView imtianjia;
    ListView lvshare;
    int orderFlag = 0;
    int pageNo = 1;
    int pageSize = 5;
    List<ShareEntity> shareEntities = new ArrayList<>();
    SshaeAdapter shareAdapter;


    int shareId;
    Timestamp sentTime;
    ClickLike clickLike;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share_page, null);
        imtianjia = (ImageView) v.findViewById(R.id.im_tianjia);
        lvshare = (ListView) v.findViewById(R.id.listView);
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
                startActivity(intent);
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
        Log.i("SharePageFragment", "getData:  ");
        String url = UrlUtils.MYURL+"QueryDynamicServlet";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("orderFlag", orderFlag + "");//排序标记
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        requestParams.addQueryStringParameter("thisuser",((MyApplication)getActivity().getApplication()).getUser().getId()+"");
        Log.i("SharePageFragment", "getData:  访问网络");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("SharePageFragment", "onSuccess: result" + result);
                //gson解析list<Dynamic>

                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type type = new TypeToken<List<ShareEntity>>() {}.getType();

                shareEntities = gson.fromJson(result, type);
                Log.i("SharePageFragment", "onSuccess: shareEntities" + "--" + shareEntities);
                if (shareAdapter == null) {
                    Log.i("SharePageFragment", "onSuccess:  shareAdapter == null");
                    shareAdapter = new SshaeAdapter(getActivity(), shareEntities, R.layout.share_item);
                    lvshare.setAdapter(shareAdapter);
                } else {
                    Log.i("SharePageFragment", "onSuccess:  shareAdapter != null");
                    shareAdapter.notifyDataSetChanged();
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
        public void convert(ViewHolder viewHolder, final ShareEntity shareEntity, final int position) {
            Log.i("SharePageFragment", "convert:  share");
            TextView tvmc = viewHolder.getViewById(R.id.tv_mncheng);
            tvmc.setText(shareEntity.getDynamic().getUser().getName());
            TextView tvsj = viewHolder.getViewById(R.id.tv_shijian);
            tvsj.setText(shareEntity.getDynamic().getSentTime().toString());
            TextView tvnr = viewHolder.getViewById(R.id.tv_neirong);
            tvnr.setText(shareEntity.getDynamic().getShare()+        +shareEntity.getDynamic().getId());
            final TextView tvcount = viewHolder.getViewById(R.id.tv_count);
            tvcount.setText(shareEntity.getDynamic().getCount()+ "");
            final RadioButton imz = viewHolder.getViewById(R.id.im_zan);
            imz.setTag(position);//加标记，保证每个imageview的tag不一样
            imz.setChecked(shareEntity.isCheck());
            CommentAdapter commentAdapter = null;//子listView适配器
            List<Comment> comments = new ArrayList<Comment>();
            NoScrollListview noScrollListview = viewHolder.getViewById(R.id.list_comment);
            noScrollListview.setTag(position);
            getComment(shareEntity.getDynamic().getId(),comments);//获取评论信息
            if ((int)noScrollListview.getTag()==position&&commentAdapter==null){
                commentAdapter=new CommentAdapter(getActivity(),comments,R.layout.item_comment);
                noScrollListview.setAdapter(commentAdapter);
            }else if ((int)noScrollListview.getTag()==position&&commentAdapter!=null){
                commentAdapter.notifyDataSetChanged();
            }
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
                        shareEntity.getDynamic().setCount(count);
                        tvcount.setText(count +"");
                        deleteThumb();
                    }else if ((int)imz.getTag()==position && !shareEntity.isCheck()){
                        Log.i("SharePageFragment", "onClick: !isCheck"+position);
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

        }
    }

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
    private void getComment(int shareId, final List<Comment> comments){
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
                    comments.clear();
                    Log.i("SharePageFragment", "onSuccess:  size "+comm.size());
                    comments.addAll(comm);
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
    public void deleteThumb(){
        String url=UrlUtils.MYURL+"DeleteThumbServlet";
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
}