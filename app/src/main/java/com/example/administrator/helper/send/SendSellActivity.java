package com.example.administrator.helper.send;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class SendSellActivity extends AppCompatActivity {

    @InjectView(R.id.et_xuqiu_sell)
    EditText etXuqiuSell;
    @InjectView(R.id.tv_show_time_sell)
    TextView tvShowTimeSell;
    @InjectView(R.id.et_phone_sell)
    EditText etPhoneSell;
    @InjectView(R.id.tv_buy_sell)
    TextView tvBuySell;
    @InjectView(R.id.rl_buy_sell)
    RelativeLayout rlBuySell;
    @InjectView(R.id.tv_youhuiquan_sell)
    TextView tvYouhuiquanSell;
    @InjectView(R.id.rl_youhui_sell)
    RelativeLayout rlYouhuiSell;
    @InjectView(R.id.et_money_sell)
    EditText etMoneySell;
    @InjectView(R.id.but_send_study_sell)
    Button butSendStudySell;
    @InjectView(R.id.tv_tixing_sell)
    TextView tvTixingSell;
    @InjectView(R.id.v1111)
    View v1111;

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
        setContentView(R.layout.activity_send_sell);
        ButterKnife.inject(this);
        user = ((MyApplication) getApplication()).getUser();
        if (coupon == null) {
            coupon = new Coupon(-1, null, 0, null, null, null);
        }

        //解析优惠券需要用到的listView的布局和控件
        youhuiListView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.listview_youhuiquan, null);
        youhuiList = (ListView) youhuiListView.findViewById(R.id.list_youhuiquan);

    }

    //请求返回界面回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @OnClick({R.id.rl_city_sell, R.id.rl_buy_sell, R.id.rl_youhui_sell, R.id.but_send_study_sell})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_city_sell:
                //选择时间
                if (getTimePicker == null) {
                    getTimePicker = new GetTimePicker(tvShowTimeSell, v1111, this, this);
                }
                getTimePicker.showBottoPopupWindow();

//                final Calendar calendar =Calendar.getInstance();//获取当前时间
//                //弹出日期选择
//                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
//
//                        //弹出时间选择
//                        new TimePickerDialog(SendSellActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                Calendar calendar1 = Calendar.getInstance();
//                                calendar1.set(year,monthOfYear,dayOfMonth,hourOfDay,minute);
//                                Timestamp timestamp = new Timestamp(calendar1.getTimeInMillis());
////                                Date date =calendar1.getTime();
////                                DateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH/mm/ss");
////                                String timeStr = sdf.format(date);
////                                Timestamp timestamp;
////                                timestamp = Timestamp.valueOf(timeStr);
//                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                tvShowTimeSell.setText(format.format(timestamp));
//                            }
//                        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show();
//                    }
//
//
//                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.rl_buy_sell:
                //支付方式
                CharSequence[] item = {"微信支付", "支付宝"};
                new AlertDialog.Builder(this).setTitle("选择支付方式")
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        tvBuySell.setText("微信支付");
                                        break;
                                    case 1:
                                        tvBuySell.setText("支付宝");
                                        break;
                                }
                            }
                        }).create().show();
                break;
            case R.id.rl_youhui_sell:
                //优惠券
                if (dialog == null) {
                    initListview();
                    dialog = new AlertDialog.Builder(this).setTitle("请选择优惠券").setView(youhuiListView)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialo, int which) {
                                    coupon = new Coupon(-1, null, 0, null, null, null);
                                    tvYouhuiquanSell.setText("未选择优惠券");
                                    dialog.cancel();
                                }
                            }).create();
                    dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
                    dialog.show();
                } else {
                    dialog.show();
                }
                break;
            case R.id.but_send_study_sell:
                String xuqiu = null;//需求
                Timestamp creatTime = new Timestamp(System.currentTimeMillis());//创建日期
                Timestamp time = null;//任务要求时间
                String phone = null;//联系电话
                boolean buyway = true;//付款方式
                Integer money = null;//任务赏金
                //获取用户输入的信息
                String buyWayStr = tvBuySell.getText().toString();
                //需求
                if (etXuqiuSell.getText().toString() == null || "".equals(etXuqiuSell.getText().toString())) {
                    tvTixingSell.setText("请输入具体需求");
                    return;
                } else {
                    xuqiu = etXuqiuSell.getText().toString();
                }
                //时间
                if (tvShowTimeSell.getText().toString() == null || "".equals(tvShowTimeSell.getText().toString())) {
                    tvTixingSell.setText("请选择任务时限");
                    return;
                } else {
                    String timeStr = tvShowTimeSell.getText().toString();
                    time = Timestamp.valueOf(timeStr);
                }
                //联系电话
                if (etPhoneSell.getText().toString() == null || "".equals((etPhoneSell.getText().toString()))) {
                    tvTixingSell.setText("请输入联系电话");
                    return;
                } else {
                    phone = etPhoneSell.getText().toString();
                }
                //支付方式
                switch (buyWayStr) {
                    case "支付宝":
                        buyway = true;
                        break;
                    case "微信支付":
                        buyway = false;
                        break;
                }
                /**
                 * 优惠券
                 */
                //赏金
                if (etMoneySell.getText().toString() == null && "".equals(etMoneySell.getText().toString())) {
                    tvTixingSell.setText("请输入你预期的赏金");
                    return;
                } else if (Integer.parseInt(etMoneySell.getText().toString()) < 8) {
                    tvTixingSell.setText("亲,赏金至少为8元哦~");
                    return;
                } else {
                    money = Integer.parseInt(etMoneySell.getText().toString());
                }
                int price = money - coupon.getReduce();
                if (price <= 0) {
                    price = 0;
                }
                /**
                 封装对象
                 */
                //任务
                Task task = new Task(user, null, time, null, null, null, phone, new TaskType(5, "销售"), xuqiu, money, 1);
                final String taskJson = toJson(task);
                //订单
                Orders order = new Orders(null, task, coupon, price, buyway, creatTime, null, new OrderStaus(1, "待付款"), null);
                final String orderJson = toJson(order);
                InsertOrderBean insertOrderBean = new InsertOrderBean(coupon.getId(), price, buyway, 1);
                String insertOrderBeanJson = toJson(insertOrderBean);
                //网络访问
                String url = UrlUtils.MYURL + "SendServlet";
                RequestParams params = new RequestParams(url);
                params.addBodyParameter("task", taskJson);
                params.addBodyParameter("insertOrderBean", insertOrderBeanJson);
                ;

                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(SendSellActivity.this, "恭喜您，发布成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        final Intent intent = new Intent(SendSellActivity.this, GoPayActivity.class);
                        intent.putExtra("task", taskJson);
                        intent.putExtra("order", orderJson);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(SendSellActivity.this, "抱歉，创建任务失败", Toast.LENGTH_SHORT).show();
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
        Log.i("SendStudyActivity", "initListview:  " + user.getId());
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
                    couponAdapter = new CommonAdapter<Coupon>(SendSellActivity.this, coupons, R.layout.item_youhui) {
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
                        tvYouhuiquanSell.setText("优惠 " + coupon.getReduce() + " 元");
                        dialog.cancel();
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SendSellActivity.this, "抱歉，网络访问失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(SendSellActivity.this, "已取消网络访问", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
                Log.i("SendStudyActivity", "onFinished:  网络访问完成");
            }
        });


    }

    public String toJson(Object object) {
        GsonBuilder gb = new GsonBuilder();
        gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
        gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
        Gson gson = gb.create();
        String json = gson.toJson(object);
        return json;
    }
}
