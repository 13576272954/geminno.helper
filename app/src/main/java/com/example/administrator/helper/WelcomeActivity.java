package com.example.administrator.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.example.administrator.helper.utils.LocationService;

public class WelcomeActivity extends AppCompatActivity {

    private SharedPreferences sp;//保存密码的存储类
    private String userNameValue,passwordValue;//用户名和密码
    boolean choseAutoLogin;//是否自动登陆
    LoginActivity loginActivity;

    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initData();
//        isLogin();

    }
    private void initData (){
        sp=getSharedPreferences("userInfo", Context.MODE_PRIVATE);//第一个参数，xml文件名；第二个，文件读写权限
        choseAutoLogin = sp.getBoolean("autoLogin", false);//false表示默认返回值为false
        userNameValue = sp.getString("userName",null);
        passwordValue = sp.getString("userPsd",null);
        Log.i("dingwei", "initData:  "+choseAutoLogin+"    "+userNameValue+"    "+passwordValue);
        loginActivity = new LoginActivity();
    }
    private void isLogin(){
        if (choseAutoLogin){
            loginActivity.choseAutoLogin(userNameValue,passwordValue,sp,this);
        }else {
            Intent intent = new Intent(this,LoginActivity.class);
            finish();
            startActivity(intent);
        }
    }

    private void logMsg(String city){
        initData();
        isLogin();
        ((MyApplication)getApplication()).setCity(city);
    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }
////
    @Override
    protected void onStart() {
        super.onStart();
        locationService = ((MyApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        //注册监听
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();// 定位SDK
        Log.i("dingwei", "onReceiveLocation:  开始定位");
        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }

    /**
     * 定位结果回调，重写onReceiveLocation方法
     */
    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.i("dingwei", "onReceiveLocation:  定位回调");
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                Log.i("dingwei", "onReceiveLocation:  Time:"+location.getTime());
                Log.i("dingwei", "onReceiveLocation:  Type:"+location.getLocType());
                Log.i("dingwei", "onReceiveLocation:  city:"+location.getCity());
                logMsg(location.getCity());
            }

        }
    };
}
