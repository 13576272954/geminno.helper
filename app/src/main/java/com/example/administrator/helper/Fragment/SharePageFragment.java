package com.example.administrator.helper.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helper.BaseFragment;
import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.ClickLike;
import com.example.administrator.helper.entity.ShareEntity;
import com.example.administrator.helper.share.DetailsActivity;
import com.example.administrator.helper.share.ReleaseActivity;
import com.example.administrator.helper.share.ShowImageActivity;
import com.example.administrator.helper.share.SpaceActivity;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.MyGridView;
import com.example.administrator.helper.utils.RefreshListViewa;
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

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;


/**
 * Created by bin on 2016/9/19.
 */
public class SharePageFragment extends BaseFragment implements RefreshListViewa.OnRefreshLoadChangeListener {
    ImageView imtianjia;
    RefreshListViewa lvshare;
    ImageView imxing;
    TextView tvxg;
    int orderFlag = 0;
    int pageNo = 1;
    int pageSize = 5;
    List<ShareEntity> shareEntities = new ArrayList<>();
    SshaeAdapter shareAdapter;
    int shareId;
    Timestamp sentTime;
    ClickLike clickLike;
    private PtrClassicFrameLayout ptrFrame;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share_page, null);
        imtianjia = (ImageView) v.findViewById(R.id.im_tianjia);
        lvshare = (RefreshListViewa) v.findViewById(R.id.view);
        ptrFrame = (PtrClassicFrameLayout) v.findViewById(R.id.ultra_ptr_frame);
//        ButterKnife.inject(this, v);
        return v;

    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
            ptrFrame.setLastUpdateTimeRelateObject(this);
            //下拉刷新的阻力，下拉时，下拉距离和显示头部的距离比例，值越大，则越不容易滑动
            ptrFrame.setRatioOfHeaderHeightToRefresh(1.2f);

            ptrFrame.setDurationToClose(200);//返回到刷新的位置（暂未找到）

            ptrFrame.setDurationToCloseHeader(1000);//关闭头部的时间 // default is false

            ptrFrame.setPullToRefresh(false);//当下拉到一定距离时，自动刷新（true），显示释放以刷新（false）

            ptrFrame.setKeepHeaderWhenRefresh(true);//见名只意
            //数据刷新的接口回调
            ptrFrame.setPtrHandler(new PtrHandler() {
                //是否能够刷新
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame,
                                                 View content, View header) {
                    return PtrDefaultHandler.checkContentCanBePulledDown(frame,
                            content, header);
                }
                //开始刷新的回调
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    //数据刷新的回调
                    ptrFrame.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pageNo = 1;
                            getData();
                            ptrFrame.refreshComplete();   //完成刷新后，页面恢复
                        }
                    }, 1500);
                }
            });

            //UI更新接口的回调
            ptrFrame.addPtrUIHandler(new PtrUIHandler() {
                //刷新完成之后，UI消失之后的接口回调
                @Override
                public void onUIReset(PtrFrameLayout frame) {
                }
                //开始下拉之前的接口回调
                @Override
                public void onUIRefreshPrepare(PtrFrameLayout frame) {
                }
                //开始刷新的接口回调
                @Override
                public void onUIRefreshBegin(PtrFrameLayout frame) {
                }
                //刷新完成的接口回调
                @Override
                public void onUIRefreshComplete(PtrFrameLayout frame) {
                }
                //下拉滑动的接口回调，多次调用
                @Override
                public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
                    /**
                     * isUnderTouch ：手指是否触摸
                     * status：状态值
                     * ptrIndicator：滑动偏移量等值的封装对象。
                     */
                }
            });


        imtianjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReleaseActivity.class);
                startActivity(intent);
            }
        });

        lvshare.setOnRefreshUploadChangeListener(this);


        lvshare.tv_xg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "111", Toast.LENGTH_SHORT).show();
            }
        });
        lvshare.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "222", Toast.LENGTH_SHORT).show();
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
        requestParams.addQueryStringParameter("thisuser",((MyApplication)getActivity().getApplication()).getUser().getId()+"");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("SharePageFragment", "onSuccess: result" + result);
                //gson解析list<Dynamic>
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type type = new TypeToken<List<ShareEntity>>(){}.getType();
               // shareEntities=gson.fromJson(result,type);
                List<ShareEntity> newshareEntities=gson.fromJson(result,type);
                shareEntities.clear();
                shareEntities.addAll(newshareEntities);
                Log.i("SharePageFragment", "onSuccess: shareEntities" + "--" + shareEntities);
                if (shareAdapter == null) {
                    shareAdapter = new SshaeAdapter(getActivity(), shareEntities, R.layout.share_item);
                    lvshare.setAdapter(shareAdapter);
                } else {
                    shareAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("SharePageFragment", "onError: "+ex);
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    public void loadMoreData() {
        //界面初始化数据：listview显示数据
        //xutils获取网络数据
        String url = UrlUtils.MYURL+"QueryDynamicServlet";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("orderFlag", orderFlag + "");//排序标记
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        requestParams.addQueryStringParameter("thisuser",((MyApplication)getActivity().getApplication()).getUser().getId()+"");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("SharePageFragment", "onSuccess: result" + result);
                //gson解析list<Dynamic>
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                Type type = new TypeToken<List<ShareEntity>>(){}.getType();
                // shareEntities=gson.fromJson(result,type);
                List<ShareEntity> newshareEntities=gson.fromJson(result,type);
                if(newshareEntities.size()==0){
                    pageNo--;
                }
                shareEntities.addAll(newshareEntities);
                Log.i("SharePageFragment", "onSuccess: shareEntities" + "--" + shareEntities);
                if (shareAdapter == null) {
                    shareAdapter = new SshaeAdapter(getActivity(), shareEntities, R.layout.share_item);
                    lvshare.setAdapter(shareAdapter);
                } else {
                    shareAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.i("SharePageFragment", "onError: "+ex);
            }
            @Override
            public void onCancelled(CancelledException cex) {
            }
            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void onLoad() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pageNo++;
                loadMoreData();
                lvshare.completeLoad();
            }
        },1000);
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
            if (shareEntity.getDynamic().getPicture()!=null){
                String[] pictures= shareEntity.getDynamic().getPicture().split(",");
                //遍历数组加到集合中
                for (int i=0;i<pictures.length;i++){
                    images.add(pictures[i]);
                    Log.i("SshaeAdapter", "convert: --1111"+pictures[i]);
                }
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
                    String shareEntityjson=gson.toJson(shareEntities.get(position));
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
              Log.i("SshaeAdapter", "convert: 333333"+ UrlUtils.MYURL+s);
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

        }
    }
   //点赞增加插入数据库
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


}