package com.example.administrator.helper.receive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.helper.MainActivity;
import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Task;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.send.chat.TalkingActivity;
import com.example.administrator.helper.utils.TimestampTypeAdapter;
import com.example.administrator.helper.utils.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.sql.Timestamp;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class TaskDetilsActivity extends AppCompatActivity {

    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.iv_xiaoxi)
    ImageView ivXiaoxi;
    @InjectView(R.id.tv_shippingCost)
    TextView tvShippingCost;
    @InjectView(R.id.tv_deliveryPlace)
    TextView tvDeliveryPlace;
    @InjectView(R.id.tv_receivingLand)
    TextView tvReceivingLand;
    @InjectView(R.id.tv_textExplain)
    TextView tvTextExplain;
    @InjectView(R.id.tv_jieDan)
    TextView tvJieDan;
    Task task;
    int taskId;

    User user;
    MyApplication myApplication = (MyApplication) getApplication();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detils);
        ButterKnife.inject(this);
        user=((MyApplication)getApplication()).getUser();

        initView();
        initData();
        initEvent();

    }

    public void initView() {

    }

    public void initEvent() {


    }

    public void updateOrder(int taskId) {

        String url = UrlUtils.MYURL + "UpdateOrdersServlet";
        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("orderId", taskId + "");
        requestParams.addQueryStringParameter("receiveUserId", ((MyApplication) getApplication()).getUser().getId() + "");
        Log.i("TaskDetilsActivity", "updateOrder:  " + ((MyApplication) getApplication()).getUser().getId());
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.i("OrderAllFragment", "onSuccess: ");
                //   getOrderData();
                Intent intent = new Intent();
                startActivity(intent);
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

    public void initData() {
        //获取传过来的ProductInfo
        Intent intent = getIntent();
        task = intent.getParcelableExtra("productInfo");
        if (task != null) {
            //商品名称
            tvShippingCost.setText(task.getMoney() + "");
            tvDeliveryPlace.setText(task.getMakePlace());
            Log.i("TaskDetilsActivity", "initData:  " + task.getMakePlace());
            tvReceivingLand.setText(task.getSubmitPlace());
            tvTextExplain.setText(task.getTaskDemand());
            taskId = task.getId();
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_xiaoxi, R.id.tv_jieDan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                Intent intent2=new Intent(this,DetilsActivity.class);
                finish();
                break;
            case R.id.iv_xiaoxi:
                Intent intent1=new Intent(this, TalkingActivity.class);
                GsonBuilder gb=new GsonBuilder();
                gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
                gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter());
                Gson gson = gb.create();
                String userStr=gson.toJson(task.getSendUser());
                intent1.putExtra("user",userStr);
                startActivity(intent1);
                break;
            case R.id.tv_jieDan:
                Intent intent = new Intent(TaskDetilsActivity.this, MainActivity.class);
                taskId = task.getId();
                updateOrder(taskId);
                startActivity(intent);
                break;
        }
    }
}
