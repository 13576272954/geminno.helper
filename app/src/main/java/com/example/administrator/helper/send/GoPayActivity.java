package com.example.administrator.helper.send;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Orders;
import com.example.administrator.helper.entity.Task;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.utils.UrlUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import c.b.BP;
import c.b.PListener;
import c.b.QListener;

public class GoPayActivity extends AppCompatActivity {

    @InjectView(R.id.gopay_prodmoney)
    TextView gopayProdmoney;
    @InjectView(R.id.gopay_youhuimoney)
    TextView gopayYouhuimoney;
    @InjectView(R.id.gopay_shifumoney)
    TextView gopayShifumoney;
    @InjectView(R.id.gopay_order_info)
    LinearLayout gopayOrderInfo;
    @InjectView(R.id.gopay_pay)
    Button gopayPay;

    Task task;
    User user;
    Orders order;
    String trade_no;//Bmob订单id

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_pay);
        ButterKnife.inject(this);

        //支付时要先初始化
        BP.init(this, "381e8949cca2851afa738898139f924a");

        user=((MyApplication)getApplication()).getUser();

        Intent intent =getIntent();
        String taskStr=intent.getStringExtra("task");
        String orderStr=intent.getStringExtra("order");
        Gson gson=new Gson();
        task=gson.fromJson(taskStr,Task.class);
        order=gson.fromJson(orderStr,Orders.class);

        Log.i("GoPayActivity", "onCreate:  "+order.getCoupon());
        gopayProdmoney.setText("￥"+(double)task.getMoney());
        gopayYouhuimoney.setText("￥"+order.getCoupon().getReduce());
        gopayShifumoney.setText("￥"+order.getPrice());

    }

    @OnClick(R.id.gopay_pay)
    public void onClick() {
        Log.i("GoPayActivity", "onClick:  点击了");
        showDialog("正在获取订单...");
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport",
                    "com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            this.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (order.getPrice()==0){
            Toast.makeText(this,"恭喜您发布成功",Toast.LENGTH_SHORT);
            setData();
            finish();
            return;
        }

//        BP.pay("发布任务", task.getTaskDemand()+"  任务id"+task.getId(), order.getPrice(), order.getBuyWay(), new PListener() {
        BP.pay("发布任务","测试", 0.02, false, new PListener() {
            @Override
            public void orderId(String s) {
                Log.i("GoPayActivity", "orderId:  订单id："+s);
                trade_no=s;
                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            @Override
            public void succeed() {

                Log.i("GoPayActivity", "succeed:  支付成功");
                //修改状态
                setData();
                hideDialog();
                Toast.makeText(GoPayActivity.this, "支付成功!", Toast.LENGTH_SHORT)
                        .show();
                finish();
            }

            @Override
            public void fail(int i, String s) {
                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
                Log.i("GoPayActivity", "fail:  失败");
                if (i == -3) {
                    Log.i("GoPayActivity", "fail:  -3");
                    Toast.makeText(
                            GoPayActivity.this,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
                    installBmobPayPlugin("bp.db");
                } else {
                    Log.i("GoPayActivity", "fail:  其他");
//                    Toast.makeText(GoPayActivity.this, "支付中断!", Toast.LENGTH_SHORT)
//                            .show();
                }
                hideDialog();
            }

            @Override
            public void unknow() {
                Log.i("GoPayActivity", "unknow:  未知");
                Toast.makeText(GoPayActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
                hideDialog();
            }
        });
    }


    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            Log.i("GoPayActivity", "showDialog:  显示");
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                Log.i("GoPayActivity", "hideDialog:  隐藏");
                dialog.dismiss();
            } catch (Exception e) {
            }
    }

    /**
     * 装插件
     * @param fileName
     */
    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 付款成功访问数据库
     */
    void setData(){
        String url = UrlUtils.MYURL+"PayServlet";
        RequestParams params= new RequestParams(url);
        params.addQueryStringParameter("trade_no",trade_no);
//        params.addQueryStringParameter("orderId",order.getId()+"");
        params.addQueryStringParameter("orderId",20+"");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result=="success") {
                    Toast.makeText(GoPayActivity.this, "付款成功", Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(GoPayActivity.this, "出现位置异常", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //订单状态修改失败
                Log.i("GoPayActivity", "onError:  修改订单访问网络失败");
                Toast.makeText(GoPayActivity.this,"订单状态修改失败，系统将于48小时内将付款退回",Toast.LENGTH_LONG);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                Log.i("GoPayActivity", "onFinished:  访问完成");
            }
        });
    }
}
