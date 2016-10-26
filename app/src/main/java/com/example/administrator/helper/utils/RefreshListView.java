package com.example.administrator.helper.utils;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.administrator.helper.R;

import java.text.SimpleDateFormat;
import java.util.Date;


public class RefreshListView extends ListView implements AbsListView.OnScrollListener{
    private int headHeight;//headview的高度
    View headView;//头部的view
    int  headState;//头部的状态
    int firstVisibleItem;
    OnRefreshUploadChangeListener onRefreshUploadChangeListener;//接口

    public static final int INIT=0;//初始状态（头部不显示）
    public static final int PREPAREREFRESHING=1;//准备刷新（头部全部显示）
    public static final int ISREFRESHING=2;//正在刷新
    private float downY;//按下去时,Y轴坐标
    private float moveY;//手滑动时，滑动点坐标
    private RotateAnimation upAnimation;
    private RotateAnimation downAnimation;
    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView tvRefreshState;
    private TextView tvRefreshTime;
    private View footView;//底部的view


    private ProgressBar footPb;
    private TextView footTv;


    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    boolean flag=false;//表示是否在下拉

    boolean isLoading=false;


    public RefreshListView(Context context) {
        this(context,null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initHead(context);
        initAnimation(context);//初始化动画
        initFoot(context);
        this.setOnScrollListener(this);//bug2:
        Log.i("RefreshListView", "RefreshListView: ");

    }





    //初始话head
    public void initHead(Context context){
        Log.i("RefreshListView", "RefreshListView: initHead");
        //解析布局文件

        headView= LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_head,null);

        //初始化头部控件
        imageView= (ImageView) headView.findViewById(R.id.iv_refresher);
        progressBar=(ProgressBar) headView.findViewById(R.id.pb_refresher);
        tvRefreshState = (TextView) headView.findViewById(R.id.tv_refreshertext);
        tvRefreshTime = (TextView) headView.findViewById(R.id.tv_refreshtime);
        //获取headview的高度
        headView.measure(0,0);
        headHeight=headView.getMeasuredHeight();
        //设置头部不显示
        headView.setPadding(0,-headHeight,0,0);
        addHeaderView(headView);//listview添加头部
    }
    //action_down,action_move,action_up
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //手按下去时候，记录初始值
                downY=ev.getY();
                flag=false;//还原成默认状态
                break;
            case MotionEvent.ACTION_MOVE:
                //  如果正在刷新，停止执行
                if(headState==ISREFRESHING){
                    return false;
                }

                moveY=ev.getY();//滑动点Y坐标

                Log.i("RefreshListView", "onTouchEvent: move:"+moveY+",downY:"+downY);

                //判断第一条记录可见&&下拉；跟着头部改变
                //如果是向下拉，第一条可见；不执行Item点击事件
                if(firstVisibleItem==0&&moveY-downY>0)
                {
                    flag=true;//是在下拉


                    float  headPadding=-headHeight+(moveY-downY);//拉过之后，头部的padding

                    //下拉过程中，状态改变：头部全部显示出来==>准备刷新
                    if(headPadding>=0&&headState==INIT){
                        headState=PREPAREREFRESHING;//状态变成准备刷新
                        changeState();//改变界面控件
                    }
                    //下拉过程中，状态改变：头部不全部显示出来==>Init
                    if(headPadding<0&&headState==PREPAREREFRESHING){
                        headState=INIT;
                        changeState();//改变界面控件
                    }

                    headView.setPadding(0, (int) headPadding,0,0);
                    return true;//bug1:如果是向下拉，return
                }

                break;


            case MotionEvent.ACTION_UP:
                //准备状态-》正在刷新；
                if(headState==PREPAREREFRESHING){
                    headState=ISREFRESHING;
                    changeState();
                    //更新数据源
                    if(onRefreshUploadChangeListener!=null){
                        onRefreshUploadChangeListener.onRefresh();
                    }

                    //headview的padding变成0
                    headView.setPadding(0,0,0,0);

                }else if(headState==INIT){
                    //初始状态，松手后，头部不显示
                    headView.setPadding(0,-headHeight,0,0);
                }


                break;
        }



        return super.onTouchEvent(ev);
    }

    public void initAnimation(Context context){
        //0-》180：选择中心点在自身的原点
        upAnimation=new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setFillAfter(true);
        upAnimation.setDuration(1000);
        downAnimation=new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setFillAfter(true);
        downAnimation.setDuration(1000);
    }


    //headview不同状态，控件效果
    public void changeState(){
        switch(headState){
            case INIT:
                progressBar.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                //给imageView设置动画
                imageView.startAnimation(downAnimation);//设置箭头朝下转
                tvRefreshState.setText("下拉刷新");
                tvRefreshTime.setVisibility(View.INVISIBLE);
                break;

            case PREPAREREFRESHING:
                //准备刷新
                progressBar.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                imageView.startAnimation(upAnimation);////设置箭头朝下
                tvRefreshState.setText("释放刷新");
                tvRefreshTime.setVisibility(View.INVISIBLE);
                break;
            case ISREFRESHING:
                //正在刷新

                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                imageView.clearAnimation();//清除imagview的动画
                tvRefreshState.setText("正在刷新");
                tvRefreshTime.setVisibility(View.VISIBLE);
                tvRefreshTime.setText(getTime());//刷新时间
                break;
        }
    }

    //格式化当前时间
    public String getTime(){

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time=	format.format(new Date());

        return time;
    }



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //滚动状态改变
        Log.i("RefreshListView", "onScrollStateChanged: "+scrollState);
        //最后一条记录可见&&当前没有在刷新&&开始||结束
//
        if(getLastVisiblePosition()==getCount()-1&&isLoading==false) {  //  OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
            if(scrollState==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL||scrollState==OnScrollListener.SCROLL_STATE_IDLE){
                //界面改变&&加载数据&&界面改变
                //改变状态
                isLoading=true;//正在加载
                //改变界面
                changeFootState();
                if(onRefreshUploadChangeListener!=null){
                    onRefreshUploadChangeListener.onPull();//加载更多数据
                }
            }

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.i("RefreshListView", "onScroll: ");
        this.firstVisibleItem=firstVisibleItem;
    }

    //定义接口：下拉刷新，上拉加载；
    public interface  OnRefreshUploadChangeListener{
        //下拉刷新
        void onRefresh();
        void onPull();//上拉加载
    }

    public void setOnRefreshUploadChangeListener(OnRefreshUploadChangeListener onRefreshUploadChangeListener){
        this.onRefreshUploadChangeListener=onRefreshUploadChangeListener;
    }


    //刷新完成
    public void completeRefresh(){
        headState=INIT;
        //padding返回去
        //   Log.d("pageNo",headHeight+"");
        headView.setPadding(0,-headHeight*2,0,0);
        //状态改变：正在刷新-INIT
        changeState();//控件初始化
    }

    //加载
    public void initFoot(Context context) {
        Log.i("RefreshListView", "initFoot: ");
        footView = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_footer, null);
        footPb= (ProgressBar) footView.findViewById(R.id.footer_progressbar);
        footTv= (TextView) footView.findViewById(R.id.footer_hint_textview);
        addFooterView(footView);//添加footview

    }

    //完成加载
    public void completeLoad(){

        isLoading=false;//加载完成
        footPb.setVisibility(GONE);
        footTv.setVisibility(VISIBLE);

    }

    //根据状态，判断底部哪个控件显示
    public void changeFootState(){
        if(isLoading){
            footPb.setVisibility(VISIBLE);
            footTv.setVisibility(GONE);

        }else{
            //加载完成：文本显示
            footPb.setVisibility(GONE);
            footTv.setVisibility(VISIBLE);
        }
    }



}