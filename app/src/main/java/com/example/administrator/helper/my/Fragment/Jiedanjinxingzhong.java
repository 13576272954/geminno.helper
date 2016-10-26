package com.example.administrator.helper.my.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helper.BaseFragment;
import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Orders;
import com.example.administrator.helper.my.Activity.XianShiGerenXinxi;
import com.example.administrator.helper.my.util.CommonAdapter;
import com.example.administrator.helper.my.util.ViewHolder;
import com.example.administrator.helper.utils.RefreshListView;
import com.example.administrator.helper.utils.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class Jiedanjinxingzhong extends BaseFragment implements RefreshListView.OnRefreshUploadChangeListener{
    @InjectView(R.id.lv_goods)
    RefreshListView lvGoods;
    List<Orders> orders=new ArrayList<>();//从服务器获取的订单信息
    CommonAdapter<Orders> orderApater;//适配器
    Handler handler=new Handler();
    public static final int UNPAY=1;//代付款
    public static final int UNACTIVITY=2;//待开始
    public static final int ACTIVITY=3;//进行中
    public static final int ACCORD=4;//待评价
    public static final int CANCEL=5;//已完成
    public static final int  CLOSE=7;//已关闭
    public static final int  REFUND=6;//退款中
    //订单分页
    int orderFlag=0;
    int pageNo=1;
    int pageSize=5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_quanbu, null);

        ButterKnife.inject(this, v);

        return v;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initEvent() {
        lvGoods.setOnRefreshUploadChangeListener(this);
    }

    @Override
    public void initData() {
        getOrderData();
    }

    //获取网络数据
    public void getOrderData(){
        Log.i("JiedanQuanbu", "getOrderData: 000");
        RequestParams requestParams=new RequestParams(UrlUtils.MYURL+"JieDanQueryServlet");
        //传参数：user_id,order_id
        //传参数：user_id,order_id

        requestParams.addQueryStringParameter("userId",((MyApplication)getActivity().getApplication()).getUser().getId()+"");

         requestParams.addQueryStringParameter("orderStatusId",3+"");
        requestParams.addQueryStringParameter("orderFlag",orderFlag+"");//排序标记
        requestParams.addQueryStringParameter("pageNo",pageNo+"");
        requestParams.addQueryStringParameter("pageSize",pageSize+"");
        Log.i("QuanBuFragment", "getOrderData: fenye  "+orders+"  " +pageNo+"  "+pageSize);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // Log.i("QuanBuFragment", "onSuccess: "+result);
                Gson gson=new Gson();
                Type type=new TypeToken<List<Orders>>(){}.getType();
                List<Orders> newOrders=gson.fromJson(result,type);

                orders.clear();
                orders.addAll(newOrders);
                Log.i("QuanBuFragment", "onSuccess: ygef "+"  "+orders);
                //设置listview的adapter
                if(orderApater==null){
                    orderApater=new CommonAdapter<Orders>(getActivity(),orders,R.layout.frag_allorders_item) {
                        @Override
                        public void convert(ViewHolder viewHolder, Orders order, int position) {

                            //设置item中控件的取值
                            initItemView(viewHolder,order,position);

                        }
                    };
                   lvGoods.setAdapter(orderApater);
                }else{
                    orderApater.notifyDataSetChanged();
                    Log.i("QuanBuFragment", "onSuccess: 控件值更新");
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Log.i("QuanBuFragment", "onError:错误 "+ ex);
                if(ex instanceof HttpException){  //网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    //...
                }else{
                    //...其他错误
                }
                Log.i("QuanBuFragment", "onError: "+ex.getMessage());
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.i("QuanBuFragment", "onFinished: 结束00");
            }
        });
    }
    public void initItemView(ViewHolder viewHolder, Orders order, int position){
        TextView tvrenwuTime=viewHolder.getViewById(R.id.frag_allorders_item_time);//显示任务订单时间
        TextView tvOrderState=viewHolder.getViewById(R.id. frag_allorders_item_trade);//显示任务订单状态
        ImageView imageViewtouxiang=viewHolder.getViewById(R.id.order_item_iv_left);//显示发任务用户头像
        TextView  tvrenwuyaoqiu=viewHolder.getViewById(R.id.order_item_info_yaoqiu);//显示任务要求
        TextView tvrenwuleixing=viewHolder.getViewById(R.id.order_renwuleixing);//显示任务类型
        TextView tvTotalMoney=viewHolder.getViewById(R.id.order_item_info_price);//显示任务总价
        Button btnLian=viewHolder.getViewById(R.id.btn_item_left_lianxi);
        Button btnLeft=viewHolder.getViewById(R.id.btn_item_left);
        Button btnRight=viewHolder.getViewById(R.id.btn_item_right);

        // NoScrollListview noScrollListview=viewHolder.getViewById(R.id.frag_allorders_item_listView);

        //控件赋值

            tvrenwuTime.setText("订单时间：" + order.getCreateDate());
            // Log.i("QuanBuFragment", "initItemView: "+order.getCreateDate());
            Log.i("QuanBuFragment", "initItemView: " + tvOrderState + "      " + order);
            tvOrderState.setText(order.getOrderStaus().getOrderStaus());
            Log.i("QuanBuFragment", "initItemView: kj " + order.getOrderStaus().getOrderStaus());
            x.image().bind(imageViewtouxiang, UrlUtils.MYURL + "image/" + order.getTask().getSendUser().getImage());
            tvrenwuyaoqiu.setText(order.getTask().getTaskDemand());
            // Log.i("QuanBuFragment", "initItemView: 气我呀"+order.getTask().getTaskDemand());
            tvTotalMoney.setText("￥" + order.getTask().getMoney());
            // Log.i("QuanBuFragment", "initItemView: 就和地方"+order.getTask().getMoney());
            tvrenwuleixing.setText(order.getTask().getTaskType().getTaskType());
            //  Log.i("QuanBuFragment", "initItemView: 请问"+order.getTask().getTaskType().getTaskType());
            //设置按钮的初始显示内容
            btnShow(order.getOrderStaus().getId(),btnLian, btnLeft, btnRight);

            //按钮点击事件
            btnClick(order, position,btnLian, btnLeft, btnRight);

    }


    //根据订单状态，判断按钮是否显示，按钮的文本，按钮的点击事件
    public void btnShow(int orderStateId,Button btnLian,Button btnLeft,Button btnRight){
        Log.i("QuanBuFragment", "btnShow: 显示"+orderStateId);
        switch (orderStateId){
            case UNPAY:
                btnLeft.setVisibility(View.GONE);//左边按钮消失
                btnRight.setVisibility(View.GONE);
                break;

            case   UNACTIVITY:
                btnLeft.setVisibility(View.GONE);//左边按钮消失
                btnRight.setVisibility(View.GONE);
                break;
            case ACTIVITY:
                //进行中:
                btnLian.setVisibility(View.VISIBLE);
                btnLeft.setVisibility(View.GONE);//左边按钮消失
                btnRight.setVisibility(View.GONE);
                btnLian.setText("联系发单人");
                btnLeft.setText("举报");
                btnRight.setText("待评价");
                //待评价表示接单者已经做完
                break;
            case  ACCORD:
                //待评价
                btnLeft.setVisibility(View.GONE);//左边按钮消失
                btnRight.setVisibility(View.GONE);
                btnLeft.setText("举报");
                btnRight.setText("去评价");
                break;
            case  CANCEL:
                //已完成
                btnLeft.setVisibility(View.GONE);//左边按钮消失
                btnRight.setVisibility(View.VISIBLE);
                // btnLeft.setText("删除订单");
                btnRight.setText("删除订单");
                Log.i("JiedanQuanbu", "btnShow: 删除此单单的显示");

                break;
            case  CLOSE:
                btnLeft.setVisibility(View.GONE);//左边按钮消失
                btnRight.setVisibility(View.GONE);
                // btnLeft.setText("删除订单");
                btnRight.setText("删除订单");
                break;
            case REFUND:
                btnLeft.setVisibility(View.GONE);//左边按钮消失
                btnRight.setVisibility(View.GONE);
                break;


        }

    }

    //按钮点击事件
    public void btnClick(final Orders order, final int position,Button btnLian,Button btnLeft, Button btnRight){



        btnLian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (order.getOrderStaus().getId()){
                    case ACTIVITY:
                        Intent intent=new Intent(getActivity(), XianShiGerenXinxi.class);
                        Log.i("QuanBuFragment", "onClick: 传输局"+order.getTask().getSendUser().getId());
                        intent.putExtra("JiedanzheId",order.getTask().getSendUser().getId());

                        startActivityForResult(intent,1);
                        break;
                    case  ACCORD:
                        Intent intent2=new Intent(getActivity(), XianShiGerenXinxi.class);
                        intent2.putExtra("JiedanzheId",order.getTask().getSendUser().getId());

                        startActivityForResult(intent2,1);
                        break;

                }

            }
        });


        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("QuanBuFragment", "onClick: yweb"+order.getOrderStaus().getId());
                //判断订单状态
                switch (order.getOrderStaus().getId()){

                    case ACTIVITY:

                        break;
                    case ACCORD:
                        break;
                }
            }


        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (order.getOrderStaus().getId()){
                    case ACTIVITY:
                        break;
                    case  ACCORD:
                        break;
                    case CANCEL:
                        //已完成 点击按钮删除此订单的显示
                        RequestParams requestParams2=new RequestParams(UrlUtils.MYURL+"OrderDeleteServlet");
                        Log.i("JiedanQuanbu", "onClick: hijk"+order.getId());
                        requestParams2.addBodyParameter("orderId",order.getId()+"");
                        //更新订单，更新界面
                        x.http().post(requestParams2, new Callback.CommonCallback<String>() {

                            @Override
                            public void onSuccess(String result) {

                                //更新界面
                                //  orders.get(position).
                                getOrderData();
                                Log.i("JiedanQuanbu", "onSuccess: 按钮后更新页面");

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



                        break;
                    case CLOSE:

                        break;


                }
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
}
    @Override
    public void onRefresh() {
        //更新数据
        pageNo=1;
        //重新请求服务器
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("MainpageFragment", "runonRefresh()  ");
                getOrderData();;//停留1秒
                lvGoods.completeRefresh();//更新数据后重新回到初始状态
            }
        },1000);
    }

    @Override
    public void onPull() {
        //加载数据
        pageNo++;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("MainpageFragment", "runonPull()  ");
                getPullDate();//原来基础上加载数据
            }
        },1000);

    }
    public void getPullDate(){
        //xutils获取网络数据
        Log.i("JiedanQuanbu", "getOrderData: 000");
        RequestParams requestParams=new RequestParams(UrlUtils.MYURL+"JieDanQueryServlet");
        //传参数：user_id,order_id
        //传参数：user_id,order_id

        requestParams.addQueryStringParameter("userId",((MyApplication)getActivity().getApplication()).getUser().getId()+"");

        requestParams.addQueryStringParameter("orderStatusId",3+"");
        requestParams.addQueryStringParameter("orderFlag",orderFlag+"");//排序标记
        requestParams.addQueryStringParameter("pageNo",pageNo+"");
        requestParams.addQueryStringParameter("pageSize",pageSize+"");
        Log.i("QuanBuFragment", "getOrderData: fenye  "+orders+"  " +pageNo+"  "+pageSize);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // Log.i("QuanBuFragment", "onSuccess: "+result);
                Gson gson=new Gson();
                Type type=new TypeToken<List<Orders>>(){}.getType();
                List<Orders> newOrders=gson.fromJson(result,type);
                if (newOrders.size()==0){
                    pageNo--;
                    Toast.makeText(getActivity(),"已经到底了",Toast.LENGTH_SHORT).show();
                    lvGoods.completeLoad();//加载数据后更新界面
                    return;
                }

                orders.addAll(newOrders);
                Log.i("QuanBuFragment", "onSuccess: ygef "+"  "+orders);
                //设置listview的adapter
                if(orderApater==null){
                    orderApater=new CommonAdapter<Orders>(getActivity(),orders,R.layout.frag_allorders_item) {
                        @Override
                        public void convert(ViewHolder viewHolder, Orders order, int position) {

                            //设置item中控件的取值
                            initItemView(viewHolder,order,position);

                        }
                    };
                    lvGoods.setAdapter(orderApater);
                }else{
                    orderApater.notifyDataSetChanged();
                    Log.i("QuanBuFragment", "onSuccess: 控件值更新");
                }
                lvGoods.completeLoad();//加载数据后更新界面
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Log.i("QuanBuFragment", "onError:错误 "+ ex);
                if(ex instanceof HttpException){  //网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    //...
                }else{
                    //...其他错误
                }
                Log.i("QuanBuFragment", "onError: "+ex.getMessage());
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.i("QuanBuFragment", "onFinished: 结束00");
            }
        });

    }
}
