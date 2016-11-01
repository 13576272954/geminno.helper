package com.example.administrator.helper.my.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.administrator.helper.entity.OrderStaus;
import com.example.administrator.helper.entity.Orders;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.RefreshListView;
import com.example.administrator.helper.utils.UrlUtils;
import com.example.administrator.helper.utils.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/10/15 0015.
 */
public class DaiFuKuanFragment extends BaseFragment implements RefreshListView.OnRefreshUploadChangeListener{

    @InjectView(R.id.lv_goods)
    RefreshListView lvGoods;
    List<Orders> orders=new ArrayList<>();//从服务器获取的订单信息
    CommonAdapter<Orders> orderApater;//适配器

    public static final int UNPAY=1;//代付款
    public static final int UNACTIVITY=2;//待开始
    public static final int ACTIVITY=3;//进行中
    public static final int ACCORD=4;//待评价
    public static final int CANCEL=5;//已完成
    public static final int  CLOSE=7;//已关闭
    public static final int  REFUND=6;//退款中
    Handler handler=new Handler();
    //订单分页
    int orderFlag=0;
    int pageNo=1;
    int pageSize=5;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_quanbu, null);
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

        RequestParams requestParams=new RequestParams(UrlUtils.MYURL+"OrderQueryServlet");
        //传参数：user_id,order_id
        //传参数：user_id,order_id
        requestParams.addQueryStringParameter("userId",((MyApplication)getActivity().getApplication()).getUser().getId()+"");
        Log.i("DaiFuKuanFragment", "getOrderData: "+((MyApplication)getActivity().getApplication()).getUser().getId());
         requestParams.addQueryStringParameter("orderStatusId",1+"");
        requestParams.addQueryStringParameter("orderFlag",orderFlag+"");//排序标记
        requestParams.addQueryStringParameter("pageNo",pageNo+"");
        requestParams.addQueryStringParameter("pageSize",pageSize+"");
        Log.i("DaiFuKuanFragment", "getOrderData: fenye  "+orders+"  " +pageNo+"  "+pageSize);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                // Log.i("QuanBuFragment", "onSuccess: "+result);
                Gson gson=new Gson();
                Type type=new TypeToken<List<Orders>>(){}.getType();
                List<Orders> newOrders=gson.fromJson(result,type);

                orders.clear();
                orders.addAll(newOrders);
                Log.i("DaiFuKuanFragment", "onSuccess: gf "+orders);
                //设置listview的adapter
                if(orderApater==null){
                    orderApater=new CommonAdapter<Orders>(getActivity(),orders,R.layout.frag_allorders_item) {
                        @Override
                        public void convert(ViewHolder viewHolder, Orders order, int position) {

                            //设置item中控件的取值
                        initItemView(viewHolder,order,position);

                            Log.i("DaiFuKuanFragment", "convert:控件赋值 ");
                        }
                    };
                   lvGoods.setAdapter(orderApater);
                }else{
                    orderApater.notifyDataSetChanged();

                    Log.i("DaiFuKuanFragment", "onSuccess:控件值更新 ");
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Log.i("DaiFuKuanFragment", "onError: "+ex);
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
        tvrenwuTime.setText("订单时间："+order.getCreateDate());
        // Log.i("QuanBuFragment", "initItemView: "+order.getCreateDate());
        Log.i("QuanBuFragment", "initItemView: "+tvOrderState+"      "+order);
        tvOrderState.setText(order.getOrderStaus().getOrderStaus());
        Log.i("QuanBuFragment", "initItemView: kj "+order.getOrderStaus().getOrderStaus());
        Log.i("DaiFuKuanFragment", "initItemView: kj"+order.getOrderStaus().getOrderStaus());
        ImageOptions imageOptions1=new ImageOptions.Builder().setAutoRotate(true).build();
        x.image().bind(imageViewtouxiang,order.getTask().getSendUser().getImage(),imageOptions1);
//        x.image().bind(imageViewtouxiang, UrlUtils.MYURL + "image/" + order.getTask().getSendUser().getImage());
        tvrenwuyaoqiu.setText(order.getTask().getTaskDemand());
        // Log.i("QuanBuFragment", "initItemView: 气我呀"+order.getTask().getTaskDemand());
        tvTotalMoney.setText("￥"+order.getTask().getMoney());
         Log.i("QuanBuFragment", "initItemView: 就和地方"+tvrenwuleixing);
        tvrenwuleixing.setText(order.getTask().getTaskType().getTaskType());
        //  Log.i("QuanBuFragment", "initItemView: 请问"+order.getTask().getTaskType().getTaskType());
        //设置按钮的初始显示内容
      btnShow(order.getOrderStaus().getId(),btnLian,btnLeft,btnRight);

        //按钮点击事件
        btnClick(order,position,btnLian,btnLeft,btnRight);

    }
    //根据订单状态，判断按钮是否显示，按钮的文本，按钮的点击事件
    public void btnShow(int orderStateId,Button btnLian,Button btnLeft,Button btnRight){

        switch (orderStateId){
            case UNPAY:
                //代付款
                btnLian.setVisibility(View.GONE);
                btnLeft.setVisibility(View.VISIBLE);
                btnRight.setVisibility(View.VISIBLE);
                btnLeft.setText("删除任务");
                btnRight.setText("去付款");
                break;
            case UNACTIVITY:
                //待开始:（取消订单、付款）
                btnLeft.setVisibility(View.GONE);
                btnRight.setVisibility(View.VISIBLE);
                // btnLeft.setText("取消订单");
                btnRight.setText("取消订单");

                break;
            case ACTIVITY:
                //进行中:
                btnLian.setVisibility(View.VISIBLE);
                btnLeft.setVisibility(View.VISIBLE);//左边按钮消失
                btnRight.setVisibility(View.VISIBLE);
                btnLeft.setText("举报");
                btnRight.setText("待评价");
                //待评价表示接单者已经做完
                break;
            case  ACCORD:
                //待评价
                btnLeft.setVisibility(View.VISIBLE);//左边按钮消失
                btnRight.setVisibility(View.VISIBLE);
                btnLeft.setText("举报");
                btnRight.setText("去评价");
                break;
            case  CANCEL:
                //已完成
                btnLeft.setVisibility(View.GONE);//左边按钮消失
                btnRight.setVisibility(View.VISIBLE);
                // btnLeft.setText("删除订单");
                btnRight.setText("确定完成");

                break;
            case  CLOSE:
                btnLeft.setVisibility(View.GONE);//左边按钮消失
                btnRight.setVisibility(View.VISIBLE);
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
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DaiFuKuanFragment", "onClick:fg er  "+order.getOrderStaus().getId());
                //判断订单状态
                switch (order.getOrderStaus().getId()){
                    case UNPAY:

//                        changeState(order.getTaskStatus().getId(),DELETE,"删除订单",position);
//                        Log.i("QuanBuFragment", "onClick: "+ DELETE);
                        order.getId();
                        RequestParams requestParams2=new RequestParams(UrlUtils.MYURL+"OrderDeleteServlet");
                        requestParams2.addBodyParameter("orderId",order.getId()+"");
                        //更新订单，更新界面
                        x.http().post(requestParams2, new Callback.CommonCallback<String>() {

                            @Override
                            public void onSuccess(String result) {

                                //更新界面
                                //  orders.get(position).
                                getOrderData();
                                Log.i("DaiFuKuanFragment", "onSuccess: 重新赋值745");

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

                    case ACTIVITY:
                        final CharSequence[] items = {"没按时完成", "突然说不干了"};//CharSequence是接口，String实现


                        new AlertDialog.Builder(getActivity()).setTitle("举报类型").
                                setItems(items, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(getContext(),"举报成功",Toast.LENGTH_SHORT).show();
                                    }
                                }).create().show();
                        break;
                    case ACCORD:
                        final CharSequence[] items2 = {"没按任务要求做", "时间拖时","言语谩骂发单者"};//CharSequence是接口，String实现


                        new AlertDialog.Builder(getActivity()).setTitle("举报类型").
                                setItems(items2, new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.i("DaiFuKuanFragment", "onClick: kj gn");
                                        Toast.makeText(getContext(),"举报成功",Toast.LENGTH_SHORT);
                                    }
                                }).create().show();
                        break;
                }



            }


        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (order.getOrderStaus().getId()){
                    case UNPAY:
                        //去付款变成待开始
                        changeState(order.getId(),UNACTIVITY,"待开始",position);


                        break;
                    case UNACTIVITY:

                        //订单变成退款中
                        changeState(order.getId(),REFUND,"退款中",position);
                        break;
                    case ACTIVITY:
                        //待评价就相当于认为任务完成
                        changeState(order.getId(),ACCORD,"待评价",position);

                        break;
                    case  ACCORD:
                        //已完成 这个单子的流程走完
                        changeState(order.getId(),CANCEL,"确认完成",position);

                        break;
                    case CANCEL:
                        //已完成
                        changeState(order.getId(),CLOSE,"关闭订单",position);


                        break;
                    case CLOSE:

                        RequestParams requestParams2=new RequestParams(UrlUtils.MYURL+"OrderDeleteServlet");
                        requestParams2.addBodyParameter("orderId",order.getId()+"");
                        //更新订单，更新界面
                        x.http().post(requestParams2, new Callback.CommonCallback<String>() {

                            @Override
                            public void onSuccess(String result) {

                                //更新界面
                                //  orders.get(position).
                                getOrderData();

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


                }
            }
        });

    }



    //更新订单状态，更新界面
    public void changeState(int orderId, final int newStateId, final String newStateName,final int position){

        RequestParams requestParams=new RequestParams(UrlUtils.MYURL+"OrderUpdateServlet");
        requestParams.addBodyParameter("orderId",orderId+"");
        requestParams.addBodyParameter("newStateId",newStateId+"");
        Log.i("DaiFuKuanFragment", "changeState: t yuo"+orderId+"  "+newStateId);

        //更新订单，更新界面
        x.http().post(requestParams, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                //更新界面
                orders.get(position).setOrderStaus(new OrderStaus(newStateId,newStateName));
                orderApater.notifyDataSetChanged();//更新界面
                getOrderData();

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);


        Log.i("setUserVisibleHint", "setUserVisibleHint: "+isVisibleToUser);
        if (isVisibleToUser){
            getOrderData();
        }
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

        RequestParams requestParams=new RequestParams(UrlUtils.MYURL+"OrderQueryServlet");
        //传参数：user_id,order_id
        //传参数：user_id,order_id
        requestParams.addQueryStringParameter("userId",((MyApplication)getActivity().getApplication()).getUser().getId()+"");
        Log.i("DaiFuKuanFragment", "getOrderData: "+((MyApplication)getActivity().getApplication()).getUser().getId());
        requestParams.addQueryStringParameter("orderStatusId",1+"");
        requestParams.addQueryStringParameter("orderFlag",orderFlag+"");//排序标记
        requestParams.addQueryStringParameter("pageNo",pageNo+"");
        requestParams.addQueryStringParameter("pageSize",pageSize+"");
        Log.i("DaiFuKuanFragment", "getOrderData: fenye  "+orders+"  " +pageNo+"  "+pageSize);
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
                Log.i("DaiFuKuanFragment", "onSuccess: gf "+orders);
                //设置listview的adapter
                if(orderApater==null){
                    orderApater=new CommonAdapter<Orders>(getActivity(),orders,R.layout.frag_allorders_item) {
                        @Override
                        public void convert(ViewHolder viewHolder, Orders order, int position) {

                            //设置item中控件的取值
                            initItemView(viewHolder,order,position);

                            Log.i("DaiFuKuanFragment", "convert:控件赋值 ");
                        }
                    };
                    lvGoods.setAdapter(orderApater);
                }else{
                    orderApater.notifyDataSetChanged();

                    Log.i("DaiFuKuanFragment", "onSuccess:控件值更新 ");
                }
                lvGoods.completeLoad();//加载数据后更新界面
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                Log.i("DaiFuKuanFragment", "onError: "+ex);
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
