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

public class SendLifeActivity extends AppCompatActivity {

    @InjectView(R.id.et_xuqiu_life)
    EditText etXuqiuLife;
    @InjectView(R.id.tv_renwudizhi_life1)
    TextView tvRenwudizhiLife1;
    @InjectView(R.id.rl_map_all_life1)
    RelativeLayout rlMapAllLife1;
    @InjectView(R.id.tv_renwudizhi_life)
    TextView tvRenwudizhiLife;
    @InjectView(R.id.rl_map_all_life)
    RelativeLayout rlMapAllLife;
    @InjectView(R.id.et_phone_life)
    EditText etPhoneLife;
    @InjectView(R.id.tv_buy_life)
    TextView tvBuyLife;
    @InjectView(R.id.rl_buy_life)
    RelativeLayout rlBuyLife;
    @InjectView(R.id.tv_youhuiquan_life)
    TextView tvYouhuiquanLife;
    @InjectView(R.id.rl_youhui_life)
    RelativeLayout rlYouhuiLife;
    @InjectView(R.id.et_money_life)
    EditText etMoneyLife;
    @InjectView(R.id.but_send_study_life)
    Button butSendStudyLife;
    @InjectView(R.id.tv_tixing_life)
    TextView tvTixingLife;
    @InjectView(R.id.v1111)
    View v1111;
    @InjectView(R.id.but_jian)
    Button butJian;
    @InjectView(R.id.but_jia)
    Button butJia;
    @InjectView(R.id.tv_quxiao)
    TextView tvQuXiao;;

    //地址选择请求码
    public static final int MAP_REQUEST1 = 11;
    public static final int MAP_REQUEST2 = 12;


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
        setContentView(R.layout.activity_send_life);
        ButterKnife.inject(this);


        user = ((MyApplication) getApplication()).getUser();
        if (coupon == null) {
            coupon = new Coupon(-1, null, 0, null, null, null);
        }

        //解析优惠券需要用到的listView的布局和控件
        youhuiListView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.listview_youhuiquan, null);
        youhuiList = (ListView) youhuiListView.findViewById(R.id.list_youhuiquan);

        etMoneyLife.addTextChangedListener(changeMoney);
    }

    //请求返回界面回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST1 && resultCode == RESULT_OK) {
            //地图界面返回1
            String address = data.getStringExtra("address");
            tvRenwudizhiLife1.setText(address);
        } else if (requestCode == MAP_REQUEST2 && resultCode == RESULT_OK) {
            //地图界面返回2
            String address = data.getStringExtra("address");
            tvRenwudizhiLife.setText(address);
        }

    }

    @OnClick({R.id.rl_map_all_life1, R.id.rl_map_all_life, R.id.rl_buy_life, R.id.rl_youhui_life,R.id.tv_quxiao, R.id.but_send_study_life,R.id.but_jian, R.id.but_jia})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_map_all_life1:
                //选择地址
                Intent intent = new Intent(this, getMap.class);
                startActivityForResult(intent, MAP_REQUEST1);
                break;
            case R.id.rl_map_all_life:
                //选择地址
                Intent intent2 = new Intent(this, getMap.class);
                startActivityForResult(intent2, MAP_REQUEST2);
                break;
            case R.id.rl_buy_life:
                //支付方式
                CharSequence[] item = {"微信支付", "支付宝"};
                new AlertDialog.Builder(this).setTitle("选择支付方式")
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        tvBuyLife.setText("微信支付");
                                        break;
                                    case 1:
                                        tvBuyLife.setText("支付宝");
                                        break;
                                }
                            }
                        }).create().show();
                break;
            case R.id.rl_youhui_life:
                //优惠券
                if (dialog == null) {
                    initListview();
                    dialog = new AlertDialog.Builder(this).setTitle("请选择优惠券").setView(youhuiListView)
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialo, int which) {
                                    coupon = new Coupon(-1, null, 0, null, null, null);
                                    tvYouhuiquanLife.setText("未选择优惠券");
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
                if (Integer.parseInt(etMoneyLife.getText().toString())<=8){
                    return;
                }else if (Integer.parseInt(etMoneyLife.getText().toString())>8){
                    etMoneyLife.setText(Integer.parseInt(etMoneyLife.getText().toString())-1+"");
                }
                break;
            case R.id.but_jia:
                etMoneyLife.setText(Integer.parseInt(etMoneyLife.getText().toString())+1+"");
                break;
            case R.id.tv_quxiao:
                finish();
                break;
            case R.id.but_send_study_life:
                //获取用户输入信息
                String xuqiu = null;//需求
                Timestamp creatTime = new Timestamp(System.currentTimeMillis());//创建日期
                String makePlace = null;//任务地址
                String submitPlace = null;//提交地址
                String phone = null;//联系电话
                boolean buyway = true;//付款方式
                Integer money = null;//任务赏金
                //需求
                if (etXuqiuLife.getText().toString() == null || "".equals(etXuqiuLife.getText().toString())) {
                    tvTixingLife.setText("请输入具体需求");
                    return;
                } else {
                    xuqiu = etXuqiuLife.getText().toString();
                }
                //任务地址
                if (tvRenwudizhiLife1.getText().toString() == null || "".equals(tvRenwudizhiLife1.getText().toString())) {
                    tvTixingLife.setText("请选择任务地址");
                    return;
                } else {
                    makePlace = tvRenwudizhiLife1.getText().toString();
                }
                //联系电话
                if (etPhoneLife.getText().toString() == null || "".equals(etPhoneLife.getText().toString())) {
                    tvTixingLife.setText("请输入联系电话");
                    return;
                } else {
                    phone = etPhoneLife.getText().toString();
                }
                //赏金
                if (etMoneyLife.getText().toString() == null || "".equals(etMoneyLife.getText().toString())) {
                    tvTixingLife.setText("请输入你预期的赏金");
                    return;
                } else if (Integer.parseInt(etMoneyLife.getText().toString()) < 8) {
                    tvTixingLife.setText("亲,赏金至少为8元哦~");
                    return;
                } else {
                    money = Integer.parseInt(etMoneyLife.getText().toString());
                }
                if (coupon == null)
                    coupon = new Coupon(-1, null, 0, null, null, null);
                int price = money - coupon.getReduce();
                if (price <= 0) {
                    price = 0;
                }

                //提交地址
                submitPlace = tvRenwudizhiLife.getText().toString();
                if ("".equals(submitPlace))
                    submitPlace = null;

                //支付方式
                String buyWayStr = tvBuyLife.getText().toString();
                switch (buyWayStr) {
                    case "支付宝":
                        buyway = true;
                        break;
                    case "微信支付":
                        buyway = false;
                        break;
                }
                String[] c = (makePlace.split("省"))[1].split("市");
                String city = c[0] + "市";
                /**
                 封装对象
                 */
                //任务
                Task task = new Task(user, creatTime, null, city, makePlace, submitPlace, phone, new TaskType(2, "生活"), xuqiu, money, 1);
                final String taskJson = toJson(task);
                //订单
                Orders order = new Orders(null, task, coupon, price, buyway, creatTime, null, new OrderStaus(1, "待付款"), null);
                final String orderJson = toJson(order);
                InsertOrderBean insertOrderBean = new InsertOrderBean(coupon.getId(), price, buyway, 1);
                String insertOrderBeanJson = toJson(insertOrderBean);

                /**
                 * 网络访问
                 */
                String url = UrlUtils.MYURL + "SendServlet";
                RequestParams params = new RequestParams(url);
                params.addBodyParameter("task", taskJson);
                params.addBodyParameter("insertOrderBean", insertOrderBeanJson);

                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(SendLifeActivity.this, "恭喜您，发布成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        final Intent intent = new Intent(SendLifeActivity.this, GoPayActivity.class);
                        intent.putExtra("task", taskJson);
                        intent.putExtra("order", orderJson);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(SendLifeActivity.this, "抱歉，创建任务失败", Toast.LENGTH_SHORT).show();
                        Log.i("SendLifeActivity", "onError:  " + ex.getMessage());
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
                    couponAdapter = new CommonAdapter<Coupon>(SendLifeActivity.this, coupons, R.layout.item_youhui) {
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
                        tvYouhuiquanLife.setText("优惠 " + coupon.getReduce() + " 元");
                        dialog.cancel();
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SendLifeActivity.this, "抱歉，网络访问失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(SendLifeActivity.this, "已取消网络访问", Toast.LENGTH_SHORT).show();
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
