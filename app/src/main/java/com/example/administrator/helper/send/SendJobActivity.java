package com.example.administrator.helper.send;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.example.administrator.helper.utils.TimestampTypeAdapter;
import com.example.administrator.helper.utils.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SendJobActivity extends AppCompatActivity {

    @InjectView(R.id.et_xuqiu_job)
    EditText etXuqiuJob;
    @InjectView(R.id.tv_show_time_job1)
    TextView tvShowTimeJob1;
    @InjectView(R.id.tv_show_time_job2)
    TextView tvShowTimeJob2;
    @InjectView(R.id.tv_renwudizhi_job)
    TextView tvRenwudizhiJob;
    @InjectView(R.id.rl_map_all_job)
    RelativeLayout rlMapAllJob;
    @InjectView(R.id.et_phone_job)
    EditText etPhoneJob;
    @InjectView(R.id.tv_buy_job)
    TextView tvBuyJob;
    @InjectView(R.id.rl_buy_job)
    RelativeLayout rlBuyJob;
    @InjectView(R.id.et_money_job)
    EditText etMoneyJob;
    @InjectView(R.id.but_send_job)
    Button butSendJob;
    @InjectView(R.id.tv_tixing_job)
    TextView tvTixingJob;
    @InjectView(R.id.v1111)
    View v1111;

    GetTimePicker getTimePicker;

    //地址选择请求码
    public static final int MAP_REQUEST = 12;


    //用户
    User user = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_job);
        ButterKnife.inject(this);
        user = ((MyApplication) getApplication()).getUser();

    }

    //请求返回界面回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_REQUEST && resultCode == RESULT_OK) {
            //地图界面返回
            String address = data.getStringExtra("address");
            tvRenwudizhiJob.setText(address);
        }

    }

    @OnClick({R.id.tv_show_time_job1, R.id.tv_show_time_job2, R.id.rl_map_all_job, R.id.rl_buy_job, R.id.but_send_job})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_show_time_job1:
                //选择时间
                if (getTimePicker==null){
                    getTimePicker = new GetTimePicker(tvShowTimeJob1,v1111,this,this);
                }
                getTimePicker.showBottoPopupWindow();

//                final Calendar calendar = Calendar.getInstance();//获取当前时间
//                //弹出日期选择
//                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
//
//                        //弹出时间选择
//                        new TimePickerDialog(SendJobActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                Calendar calendar1 = Calendar.getInstance();
//                                calendar1.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);
//                                Timestamp timestamp = new Timestamp(calendar1.getTimeInMillis());
////                                Date date =calendar1.getTime();
////                                DateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH/mm/ss");
////                                String timeStr = sdf.format(date);
////                                Timestamp timestamp;
////                                timestamp = Timestamp.valueOf(timeStr);
//                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                tvShowTimeJob1.setText(format.format(timestamp));
//                            }
//                        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
//                    }
//
//
//                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.tv_show_time_job2:
                //选择时间
                if (getTimePicker==null){
                    getTimePicker = new GetTimePicker(tvShowTimeJob2,v1111,this,this);
                }
                getTimePicker.showBottoPopupWindow();
//                final Calendar calendar2 = Calendar.getInstance();//获取当前时间
//                //弹出日期选择
//                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, final int year, final int monthOfYear, final int dayOfMonth) {
//
//                        //弹出时间选择
//                        new TimePickerDialog(SendJobActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                            @Override
//                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                Calendar calendar1 = Calendar.getInstance();
//                                calendar1.set(year, monthOfYear, dayOfMonth, hourOfDay, minute);
//                                Timestamp timestamp = new Timestamp(calendar1.getTimeInMillis());
////                                Date date =calendar1.getTime();
////                                DateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH/mm/ss");
////                                String timeStr = sdf.format(date);
////                                Timestamp timestamp;
////                                timestamp = Timestamp.valueOf(timeStr);
//                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                tvShowTimeJob2.setText(format.format(timestamp));
//                            }
//                        }, calendar2.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.MINUTE), true).show();
//                    }
//
//
//                }, calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.rl_map_all_job:
                //选择地址
                Intent intent = new Intent(this, getMap.class);
                startActivityForResult(intent, MAP_REQUEST);
                break;
            case R.id.rl_buy_job:
                //支付方式
                CharSequence[] item = {"微信支付", "支付宝"};
                new AlertDialog.Builder(this).setTitle("选择支付方式")
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        tvBuyJob.setText("微信支付");
                                        break;
                                    case 1:
                                        tvBuyJob.setText("支付宝");
                                        break;
                                }
                            }
                        }).create().show();
                break;
            case R.id.but_send_job:
                //获取用户输入信息
                String xuqiu = null;//需求
                Timestamp creatTime = null;//开始时间
                Timestamp time = null;//任务要求时间
                String makePlace = null;//任务地址
                String phone = null;//联系电话
                boolean buyway = true;//付款方式
                Integer money = null;//任务赏金

                //需求
                if (etXuqiuJob.getText().toString() == null || "".equals(etXuqiuJob.getText().toString())) {
                    tvTixingJob.setText("请输入具体需求");
                    return;
                } else {
                    xuqiu = etXuqiuJob.getText().toString();
                }
                //开始时间
                if (tvShowTimeJob1.getText().toString() == null || "".equals(tvShowTimeJob1.getText().toString())) {
                    tvTixingJob.setText("请选择兼职开始时间");
                    return;
                } else {
                    String timeStr = tvShowTimeJob1.getText().toString();
                    creatTime = Timestamp.valueOf(timeStr);
                }
                //任务地址
                if (tvRenwudizhiJob.getText().toString() == null || "".equals(tvRenwudizhiJob.getText().toString() == null)) {
                    tvTixingJob.setText("请选择任务地址");
                    return;
                } else {
                    makePlace = tvRenwudizhiJob.getText().toString();
                }
                //联系电话
                if (etPhoneJob.getText().toString() == null || "".equals(etPhoneJob.getText().toString())) {
                    tvTixingJob.setText("请输入联系电话");
                    return;
                } else {
                    phone = etPhoneJob.getText().toString();
                }
                //赏金
                if (etMoneyJob.getText().toString() == null) {
                    tvTixingJob.setText("请输入你预期的赏金");
                    return;
                } else if (Integer.parseInt(etMoneyJob.getText().toString()) < 8) {
                    tvTixingJob.setText("亲,赏金至少为8元哦~");
                    return;
                } else {
                    money = Integer.parseInt(etMoneyJob.getText().toString());
                }

                //限定时间
                String timeStr = tvShowTimeJob1.getText().toString();
                if ("".equals(timeStr)) {
                    time = null;
                } else {
                    time = Timestamp.valueOf(timeStr);
                }
                //支付方式
                String buyWayStr = tvBuyJob.getText().toString();
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
                Task task = new Task(user, creatTime, time, city, makePlace, null, phone, new TaskType(4, "兼职"), xuqiu, money, 1);
                final String taskJson = toJson(task);
                //订单
                Orders order = new Orders(null, task, new Coupon(-1, null, 0, null, null, null), money, buyway, new Timestamp(System.currentTimeMillis()), null, new OrderStaus(1, "待付款"), null);
                final String orderJson = toJson(order);
                InsertOrderBean insertOrderBean = new InsertOrderBean(-1, money, buyway, 1);
                String insertOrderBeanJson = toJson(insertOrderBean);

                //网络访问
                String url = UrlUtils.MYURL + "SendServlet";
                RequestParams params = new RequestParams(url);
                params.addBodyParameter("task", taskJson);
                params.addBodyParameter("insertOrderBean", insertOrderBeanJson);

                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Toast.makeText(SendJobActivity.this, "恭喜您，发布成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                        final Intent intent = new Intent(SendJobActivity.this, GoPayActivity.class);
                        intent.putExtra("task", taskJson);
                        intent.putExtra("order", orderJson);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        Toast.makeText(SendJobActivity.this, "抱歉，创建任务失败", Toast.LENGTH_SHORT).show();
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

    public String toJson(Object object) {
        GsonBuilder gb = new GsonBuilder();
        gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
        gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
        Gson gson = gb.create();
        String json = gson.toJson(object);
        return json;
    }
}
