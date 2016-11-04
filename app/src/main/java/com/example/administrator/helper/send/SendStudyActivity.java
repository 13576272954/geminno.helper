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
import com.example.administrator.helper.send.map.getMap;
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

public class SendStudyActivity extends AppCompatActivity {

    @InjectView(R.id.et_xuqiu)
    EditText etXuqiu;
    @InjectView(R.id.tv_renwudizhi)
    TextView tvRenwudizhi;
    @InjectView(R.id.et_phone)
    EditText etPhone;
    @InjectView(R.id.tv_buy)
    TextView tvBuy;
    @InjectView(R.id.et_money)
    EditText etMoney;
    @InjectView(R.id.but_send_study)
    Button butSendStudy;
    @InjectView(R.id.rl_city)
    RelativeLayout rlCity;
    @InjectView(R.id.rl_map_all)
    RelativeLayout rlMapAll;
    @InjectView(R.id.rl_buy)
    RelativeLayout rlBuy;
    @InjectView(R.id.tv_show_time)
    TextView tvShowTime;
    @InjectView(R.id.tv_youhuiquan)
    TextView tvYouhuiquan;
    @InjectView(R.id.tv_tixing_study)
    TextView tvTixingStudy;
    @InjectView(R.id.v1111)
    View v1111;
    @InjectView(R.id.but_jian)
    Button butJian;
    @InjectView(R.id.but_jia)
    Button butJia;
    @InjectView(R.id.tv_quxiao)
    TextView tvQuXiao;

    //地址选择请求码
    public static final int MAP_REQUEST = 12;
    //优惠券
    Coupon coupon = null;

    GetTimePicker getTimePicker;

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
        setContentView(R.layout.activity_send_study);
        ButterKnife.inject(this);

        user = ((MyApplication) getApplication()).getUser();
        if (coupon == null) {
            coupon = new Coupon(-1, null, 0, null, null, null);
        }

        //解析优惠券需要用到的listView的布局和控件
        youhuiListView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.listview_youhuiquan, null);
        youhuiList = (ListView) youhuiListView.findViewById(R.id.list_youhuiquan);

        etMoney.addTextChangedListener(changeMoney);
    }


    @OnClick(R.id.but_send_study)
    public void onClick() {
        //获取任务的详情
        //需求
        String xiuqiu = null;
        Log.i("SendStudyActivity", "onClick:  " + (etXuqiu.getText().toString() == null) + ("".equals(etPhone.getText().toString())));
        if (etXuqiu.getText().toString() == null || "".equals(etXuqiu.getText().toString())) {
            tvTixingStudy.setText("请输入具体需求");
            return;
        } else {
            xiuqiu = etXuqiu.getText().toString();
        }
        //时间
        Timestamp lastTime = null;
        if (tvShowTime.getText().toString() == null || "".equals(tvShowTime.getText().toString())) {
            tvTixingStudy.setText("请选择任务时限");
            return;
        } else {
            String timeStr = tvShowTime.getText().toString();
            lastTime = Timestamp.valueOf(timeStr);
        }
        //联系电话
        String phone = null;
        if (etPhone.getText().toString() == null || "".equals(etPhone.getText().toString())) {
            tvTixingStudy.setText("请输入联系电话");
            Log.i("SendStudyActivity", "onClick:  ");
            return;
        } else {
            phone = etPhone.getText().toString();
        }
        String makePlace = null;
        if (tvRenwudizhi.getText().toString() != null && !"".equals(tvRenwudizhi.getText().toString())) {
            makePlace = tvRenwudizhi.getText().toString();//任务地址
        }
        Log.i("SendStudyActivity", "onClick:  makePlace:" + makePlace);

        String buyWay = tvBuy.getText().toString();//支付方式
        //赏金
        Integer money = null;
        if ("".equals(etMoney.getText().toString())) {
            tvTixingStudy.setText("请输入你预期的赏金");
            Log.i("SendStudyActivity", "onClick:  请输入你预期的赏金");
            return;
        } else if (Integer.parseInt(etMoney.getText().toString()) < 8) {
            tvTixingStudy.setText("亲,赏金至少为8元哦~");
            return;
        } else {
            money = Integer.parseInt(etMoney.getText().toString());
        }
        if (coupon == null)
            coupon = new Coupon(-1, null, 0, null, null, null);
        int price = money - coupon.getReduce();
        if (price <= 0) {
            price = 0;
        }

        boolean buyway = true;
        switch (buyWay) {
            case "支付宝":
                buyway = true;
                break;
            case "微信支付":
                buyway = false;
                break;
        }
        String[] c = (makePlace.split("省"))[1].split("市");
        String city = c[0] + "市";
        /*
        将赏金、优惠券、支付方式封装成Orders类对象
         */
        Task task = new Task(user, null, lastTime, city, makePlace, null, phone, new TaskType(1, "学习"), xiuqiu, money, 1);
        final String taskJson = toJson(task);
        Orders order = new Orders(null, task, coupon, price, buyway, new Timestamp(System.currentTimeMillis()), null, new OrderStaus(1, "待付款"), null);
        final String orderJson = toJson(order);
        InsertOrderBean insertOrderBean = new InsertOrderBean(coupon.getId(), price, buyway, 1);
        String insertOrderBeanJson = toJson(insertOrderBean);

        //网络访问
        String url = UrlUtils.MYURL + "SendServlet";
        RequestParams params = new RequestParams(url);
        params.addBodyParameter("task", taskJson);
        params.addBodyParameter("insertOrderBean", insertOrderBeanJson);


        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("SendStudyActivity", "onSuccess:  发布成功");
                Toast.makeText(SendStudyActivity.this, "恭喜您，发布成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
                final Intent intent = new Intent(SendStudyActivity.this, GoPayActivity.class);
                intent.putExtra("task", taskJson);
                intent.putExtra("order", orderJson);
                startActivity(intent);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SendStudyActivity.this, "抱歉，创建任务失败", Toast.LENGTH_SHORT).show();
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
    }

    @OnClick({R.id.rl_city, R.id.rl_map_all, R.id.rl_buy, R.id.rl_youhui,R.id.but_jian, R.id.but_jia,R.id.tv_quxiao})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_city:
                //选择时间
                if (getTimePicker == null) {
                    getTimePicker = new GetTimePicker(tvShowTime, v1111, this, this);
                }
                getTimePicker.showBottoPopupWindow();

                break;
            case R.id.rl_map_all:
                //选择地址
                Intent intent = new Intent(this, getMap.class);
                startActivityForResult(intent, MAP_REQUEST);
                break;
            case R.id.rl_buy:
                //支付方式
                CharSequence[] item = {"微信支付", "支付宝"};
                new AlertDialog.Builder(this).setTitle("选择支付方式")
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        tvBuy.setText("微信支付");
                                        break;
                                    case 1:
                                        tvBuy.setText("支付宝");
                                        break;
                                }
                            }
                        }).create().show();
                break;
            case R.id.rl_youhui:
                //优惠券
                if (dialog == null) {
                    initListview();
                    dialog = new AlertDialog.Builder(this).setTitle("请选择优惠券").setView(youhuiListView)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialo, int which) {
                                    coupon = new Coupon(-1, null, 0, null, null, null);
                                    tvYouhuiquan.setText("未选择优惠券");
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
                if (Integer.parseInt(etMoney.getText().toString())<=8){
                    return;
                }else if (Integer.parseInt(etMoney.getText().toString())>8){
                    etMoney.setText(Integer.parseInt(etMoney.getText().toString())-1+"");
                }
                break;
            case R.id.but_jia:
                etMoney.setText(Integer.parseInt(etMoney.getText().toString())+1+"");
                break;
            case R.id.tv_quxiao:
                finish();
                break;
            default:
                break;
        }
    }

    //请求返回界面回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST && resultCode == RESULT_OK) {
            //地图界面返回
            String address = data.getStringExtra("address");
            tvRenwudizhi.setText(address);
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
                    couponAdapter = new CommonAdapter<Coupon>(SendStudyActivity.this, coupons, R.layout.item_youhui) {
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
                        tvYouhuiquan.setText("优惠 " + coupon.getReduce() + " 元");
                        dialog.cancel();
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SendStudyActivity.this, "抱歉，网络访问失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(SendStudyActivity.this, "已取消网络访问", Toast.LENGTH_SHORT).show();
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
