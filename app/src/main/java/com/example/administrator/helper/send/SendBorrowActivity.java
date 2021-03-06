package com.example.administrator.helper.send;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Coupon;
import com.example.administrator.helper.entity.OrderStaus;
import com.example.administrator.helper.entity.Orders;
import com.example.administrator.helper.entity.Task;
import com.example.administrator.helper.entity.TaskType;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.entity.bean.InsertOrderBean;
import com.example.administrator.helper.send.TimePicker.GetTimePicker;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 发布
 * 借用
 */
public class SendBorrowActivity extends AppCompatActivity {
    @InjectView(R.id.et_xuqiu_borrow)
    EditText etXuqiuBorrow;
    @InjectView(R.id.tv_show_time_borrow1)
    TextView tvShowTimeBorrow1;
    @InjectView(R.id.tv_show_time_borrow2)
    TextView tvShowTimeBorrow2;
    @InjectView(R.id.et_phone_borrow)
    EditText etPhoneBorrow;
    @InjectView(R.id.tv_buy_borrow)
    TextView tvBuyBorrow;
    @InjectView(R.id.rl_buy_borrow)
    RelativeLayout rlBuyBorrow;
    @InjectView(R.id.tv_youhuiquan_borrow)
    TextView tvYouhuiquanBorrow;
    @InjectView(R.id.rl_youhui_borrow)
    RelativeLayout rlYouhuiBorrow;
    @InjectView(R.id.et_money_borrow)
    EditText etMoneyBorrow;
    @InjectView(R.id.but_send_study_borrow)
    Button butSendStudyBorrow;
    @InjectView(R.id.tv_tixing_borrow)
    TextView tvTixingBorrow;
    @InjectView(R.id.v1111)
    View v1111;
    @InjectView(R.id.but_jian)
    Button butJian;
    @InjectView(R.id.but_jia)
    Button butJia;
    @InjectView(R.id.tv_quxiao)
    TextView tvQuXiao;

    GetTimePicker getTimePicker;

    //优惠券
    Coupon coupon = null;
    //优惠券listView布局
    LinearLayout youhuiListView;
    ListView youhuiList;
    //优惠券dialog
    AlertDialog dialog;
    //优惠券集合
    List<Coupon> coupons = new ArrayList<Coupon>();
    CommonAdapter<Coupon> couponAdapter;

    //用户
    User user = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_borrow);
        ButterKnife.inject(this);

        user = ((MyApplication) getApplication()).getUser();
        if (coupon == null) {
            coupon = new Coupon(-1, null, 0, null, null, null);
        }

        //解析优惠券需要用到的listView的布局和控件
        youhuiListView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.listview_youhuiquan, null);
        youhuiList = (ListView) youhuiListView.findViewById(R.id.list_youhuiquan);

        etMoneyBorrow.addTextChangedListener(changeMoney);
    }


    @OnClick({R.id.tv_show_time_borrow1, R.id.tv_show_time_borrow2, R.id.rl_buy_borrow, R.id.rl_youhui_borrow, R.id.but_send_study_borrow,R.id.but_jian, R.id.but_jia,R.id.tv_quxiao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_show_time_borrow1:
                //选择时间
                if (getTimePicker == null) {
                    getTimePicker = new GetTimePicker(tvShowTimeBorrow1, v1111, this, this);
                }
                getTimePicker.showBottoPopupWindow();

                break;
            case R.id.tv_show_time_borrow2:
                //选择时间
                if (getTimePicker == null) {
                    getTimePicker = new GetTimePicker(tvShowTimeBorrow2, v1111, this, this);
                }
                getTimePicker.showBottoPopupWindow();
                break;
            case R.id.rl_buy_borrow:
                //支付方式
                CharSequence[] item = {"微信支付", "支付宝"};
                new AlertDialog.Builder(this).setTitle("选择支付方式")
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        tvBuyBorrow.setText("微信支付");
                                        break;
                                    case 1:
                                        tvBuyBorrow.setText("支付宝");
                                        break;
                                }
                            }
                        }).create().show();
                break;
            case R.id.rl_youhui_borrow:
                //优惠券
                if (dialog == null) {
                    initListview();
                    dialog = new AlertDialog.Builder(this).setTitle("请选择优惠券").setView(youhuiListView)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialo, int which) {
                                    coupon = new Coupon(-1, null, 0, null, null, null);
                                    tvYouhuiquanBorrow.setText("未选择优惠券");
                                    dialog.cancel();
                                }
                            }).create();
                    dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
                    dialog.show();
                } else {
                    dialog.show();
                }
                break;
            case R.id.but_jian:
                if (Integer.parseInt(etMoneyBorrow.getText().toString())<=8){
                    return;
                }else if (Integer.parseInt(etMoneyBorrow.getText().toString())>8){
                    etMoneyBorrow.setText(Integer.parseInt(etMoneyBorrow.getText().toString())-1+"");
                }
                break;
            case R.id.but_jia:
                etMoneyBorrow.setText(Integer.parseInt(etMoneyBorrow.getText().toString())+1+"");
                break;
            case R.id.tv_quxiao:
                finish();
                break;
            case R.id.but_send_study_borrow:
                //获取用户输入信息
                String xuqiu = null;//需求
                Timestamp creatTime = null;//开始时间
                Timestamp time = null;//任务要求时间
                String phone = null;//联系电话
                boolean buyway = true;//付款方式
                Integer money = null;//任务赏金

                //需求
                if ("".equals(etXuqiuBorrow.getText().toString())) {
                    tvTixingBorrow.setText("请输入具体需求");
                    return;
                } else {
                    xuqiu = etXuqiuBorrow.getText().toString();
                }
                //借用时间
                if ("".equals(tvShowTimeBorrow1.getText().toString())) {
                    tvTixingBorrow.setText("请选择兼职开始时间");
                    return;
                } else {
                    String timeStr = tvShowTimeBorrow1.getText().toString();
                    creatTime = Timestamp.valueOf(timeStr);
                }
                //联系电话
                if ("".equals(etPhoneBorrow.getText().toString())) {
                    tvTixingBorrow.setText("请输入联系电话");
                    return;
                } else if (Integer.parseInt(etMoneyBorrow.getText().toString()) < 8) {
                    tvTixingBorrow.setText("亲,赏金至少为8元哦~");
                    return;
                } else {
                    phone = etPhoneBorrow.getText().toString();
                }
//                if (coupon==null)
//                    coupon=new Coupon(-1, null, 0, null, null, null);
                //赏金
                if ("".equals(etMoneyBorrow.getText().toString())) {
                    tvTixingBorrow.setText("请输入你预期的赏金");
                    return;
                } else if (Integer.parseInt(etMoneyBorrow.getText().toString()) < 8) {
                    tvTixingBorrow.setText("亲,赏金至少为8元哦~");
                    return;
                } else {
                    money = Integer.parseInt(etMoneyBorrow.getText().toString());
                }
                int price = money - coupon.getReduce();
                if (price <= 0) {
                    price = 0;
                }


                //归还时间
                String timeStr = tvShowTimeBorrow2.getText().toString();
                Log.i("SendBorrowActivity", "onClick:  11111" + timeStr);
                if (timeStr.equals("")) {
                    time = null;
                } else {
                    time = Timestamp.valueOf(timeStr);
                }
                //支付方式
                String buyWayStr = tvBuyBorrow.getText().toString();
                switch (buyWayStr) {
                    case "支付宝":
                        buyway = true;
                        break;
                    case "微信支付":
                        buyway = false;
                        break;
                }
                /**
                 封装对象
                 */
                //任务
                Task task = new Task(user, creatTime, time, null, null, null, phone, new TaskType(3, "借用"), xuqiu, money, 1);
                final String taskJson = toJson(task);
                Log.i("SendBorrowActivity", "onSuccess:  11111" + task);
                //订单
                Orders order = new Orders(null, task, coupon, price, buyway, new Timestamp(System.currentTimeMillis()), null, new OrderStaus(1, "待付款"), null);
                final String orderJson = toJson(order);

                InsertOrderBean insertOrderBean = new InsertOrderBean(coupon.getId(), price, buyway, 1);
                String insertOrderBeanJson = toJson(insertOrderBean);

                //网络访问
                String url = UrlUtils.MYURL + "SendServlet";
                RequestParams params = new RequestParams(url);
                params.addBodyParameter("task", taskJson);
                params.addBodyParameter("insertOrderBean", insertOrderBeanJson);

                final Intent intent = new Intent(this, GoPayActivity.class);
                intent.putExtra("task", taskJson);
                intent.putExtra("order", orderJson);
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(SendBorrowActivity.this, "恭喜您，发布成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        final Intent intent = new Intent(SendBorrowActivity.this, GoPayActivity.class);
                        intent.putExtra("task", taskJson);
                        intent.putExtra("order", orderJson);
                        Log.i("SendBorrowActivity", "onSuccess:  11111" + taskJson);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(SendBorrowActivity.this, "抱歉，创建任务失败", Toast.LENGTH_SHORT).show();
                        Log.i("SendStudyActivity", "onError:  " + ex.getMessage());
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {
                        Log.i("SendStudyActivity", "onFinished" + "网络访问完成");
                    }
                });

                break;
        }
    }

    /**
     * 初始化优惠券listView
     */
    void initListview() {
        //listview 赋值
//        获取网络数据
        String url = UrlUtils.MYURL + "GetCouponServlet";
        Log.i("SendStudyActivity", "initListview:       " + url);
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("userId", user.getId() + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                GsonBuilder gb = new GsonBuilder();
                gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
                gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
                Gson gson = gb.create();
                List<Coupon> list = gson.fromJson(result, new TypeToken<List<Coupon>>() {
                }.getType());
                coupons.clear();
                coupons.addAll(list);
                if (couponAdapter == null) {
                    couponAdapter = new CommonAdapter<Coupon>(SendBorrowActivity.this, coupons, R.layout.item_youhui) {
                        @Override
                        public void convert(ViewHolder viewHolder, Coupon coupon, int position) {
                            //找控件赋值
                            TextView tvreduce = viewHolder.getViewById(R.id.tv_reduce);
                            TextView outTime = viewHolder.getViewById(R.id.out_time);
                            tvreduce.setText(coupon.getReduce() + "");
                            if (coupon.getOutTime() != null) {
                                outTime.setText(coupon.getOutTime().toString() + " 过期");
                            } else {
                                outTime.setTextColor(Color.rgb(225, 126, 0));
                                outTime.setText("不限期");
                            }
                        }
                    };
                    youhuiList.setAdapter(couponAdapter);
                } else {
                    couponAdapter.notifyDataSetChanged();
                }
                youhuiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        coupon = coupons.get(position);
                        tvYouhuiquanBorrow.setText("优惠 " + coupon.getReduce() + " 元");
                        dialog.cancel();
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SendBorrowActivity.this, "抱歉，网络访问失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(SendBorrowActivity.this, "已取消网络访问", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
                Log.i("SendStudyActivity", "onFinished:  网络访问完成");
            }
        });


    }

    /**
     * 赏金输入框内容监听
     */
    TextWatcher changeMoney = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable!=null) {
                if (Integer.parseInt(editable.toString()) <= 8) {
                    Drawable drawable = getResources().getDrawable(R.drawable.shape_left_no);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    butJian.setBackgroundDrawable(drawable);
                } else if (Integer.parseInt(editable.toString()) > 8) {
                    Drawable drawable = getResources().getDrawable(R.drawable.shape_blue);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    butJian.setBackgroundDrawable(drawable);
                }
            }
        }
    };

    public String toJson(Object object) {
        GsonBuilder gb = new GsonBuilder();
        gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
        gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
        Gson gson = gb.create();
        String json = gson.toJson(object);
        return json;
    }


}
