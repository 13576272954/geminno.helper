package com.example.administrator.helper.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.helper.R;

/**
 * Created by ASUS on 2016/11/6.
 */
public class RefreshListViewa extends ListView implements AbsListView.OnScrollListener{

    OnRefreshLoadChangeListener onRefreshLoadChangeListener; //自定义的接口

    View headView; //头部布局
    View footView; //底部布局
    boolean flag = false; //表示是否在下拉（默认没有下拉）
    public ImageView imageView;
    public TextView tv_xg;

    private ProgressBar footPb;
    TextView footTv;
    boolean isLoading = false; //是否处于加载状态

    public RefreshListViewa(Context context) {
        this(context,null);
    }
    public RefreshListViewa(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public RefreshListViewa(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initHead(context);
        initFoot(context);
        this.setOnScrollListener(this);//这里如果不写的话，是不会监听的，即onScrollStateChanged和onScroll不会执行
    }

    //初始化头部
    void initHead(Context context){
        headView = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_head2,null);

        imageView = (ImageView) headView.findViewById(R.id.imageView);
        tv_xg = (TextView) headView.findViewById(R.id.tv_xg);
        //ListView添加头部
        addHeaderView(headView);
    }

    //初始化底部
    void initFoot(Context context){
        footView = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_footer1,null);
        footPb= (ProgressBar) footView.findViewById(R.id.footer_progressbar);
        footTv= (TextView) footView.findViewById(R.id.footer_hint_textview);
        addFooterView(footView);//添加footView
    }

    //实现AbsListView.OnScrollListener接口需要重写的两个方法
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //滚动状态改变会执行
        //Log.i("RefreshListView", "onScrollStateChanged: "+scrollState);
        if(getLastVisiblePosition()==getCount()-1&&isLoading==false){//表示最后一个Item可见，且没有在加载状态
            if(scrollState== OnScrollListener.SCROLL_STATE_TOUCH_SCROLL||scrollState== OnScrollListener.SCROLL_STATE_IDLE){
                //界面改变（开始加载）--》完成刷新--》界面改变（完成加载）
                isLoading = true; //改变加载状态
                changeFootState(); //改变底部界面的状态
                if(onRefreshLoadChangeListener!=null){
                    onRefreshLoadChangeListener.onLoad();//加载更多数据
                }
            }
        }
    }
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //Log.i("RefreshListView", "onScroll: ");
    }

    //footView处于不同状态，改变不同的控件状态
    public void changeFootState(){
        if(isLoading){
            //正在加载：ProgressBar显示
            footPb.setVisibility(VISIBLE);
            footTv.setVisibility(GONE);
        }else{
            //没有在加载,进图条隐藏，文本显示
            footPb.setVisibility(GONE);
            footTv.setVisibility(VISIBLE);
        }
    }

    public boolean isFlag() {
        return flag;
    }

    //加载完成
    public void completeLoad(){
        isLoading = false; //加载完成
        changeFootState();
    }

    //定义接口：下拉刷新，上拉加载
    public interface OnRefreshLoadChangeListener{
        void onLoad();  //上拉加载
    }

    //供其他类实现该接口
    public void setOnRefreshUploadChangeListener(OnRefreshLoadChangeListener onRefreshLoadChangeListener){
        this.onRefreshLoadChangeListener = onRefreshLoadChangeListener;
    }

    //去掉footView
    public void setFootGONE(){
        removeFooterView(footView);
    }
    public void setHeadGONE(){
        removeHeaderView(headView);
    }

}
