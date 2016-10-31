package com.example.administrator.helper.send.map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.administrator.helper.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class getMap extends AppCompatActivity implements OnGetGeoCoderResultListener {

    /**
     * MapView 是地图主控件
     */
    MapView mapView;
    BaiduMap baiduMap;
    GeoCoder mSearch; // 搜索模块，也可去掉地图模块独立使用
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    @InjectView(R.id.chat_publish_complete_cancle)
    TextView chatPublishCompleteCancle;
    @InjectView(R.id.chat_publish_complete_publish)
    Button chatPublishCompletePublish;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;

    TextView tvJingwei;
    Button requestLocButton;

    boolean isFirstLoc = true; // 是否首次定位
    /**
     * 当前的点击点
     */
    private LatLng currentPt;

    //当前点的点的地址描述
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_get_map);
        ButterKnife.inject(this);
        mapView = (MapView) findViewById(R.id.bmapView);
        tvJingwei = (TextView) findViewById(R.id.tv_jinwei);
        requestLocButton = (Button) findViewById(R.id.button1);
        baiduMap = mapView.getMap();

        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;

        requestLocButton.setText("普通");

        // 初始化搜索模块，注册事件监听   定位
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        //按钮点击事件
        requestLocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mCurrentMode) {
                    case NORMAL:
                        requestLocButton.setText("跟随");
                        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        baiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case COMPASS:
                        requestLocButton.setText("普通");
                        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                        baiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    case FOLLOWING:
                        requestLocButton.setText("罗盘");
                        mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                        baiduMap
                                .setMyLocationConfigeration(new MyLocationConfiguration(
                                        mCurrentMode, true, mCurrentMarker));
                        break;
                    default:
                        break;
                }
            }
        });


        initListener();


    }

    /**
     * Geo搜索监听
     *
     * @param geoCodeResult
     */
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    /**
     * 反Geo搜索监听
     *
     * @param reverseGeoCodeResult
     */
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getMap.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        //清空地图
        baiduMap.clear();
        baiduMap.clear();
        //定位当前位置
        baiduMap.addOverlay(new MarkerOptions().position(reverseGeoCodeResult.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka)));
        //设置地图中心点
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(reverseGeoCodeResult
                .getLocation()));
        Toast.makeText(getMap.this, reverseGeoCodeResult.getAddress(),
                Toast.LENGTH_LONG).show();
        address=reverseGeoCodeResult.getAddress();
        tvJingwei.setText(address);
    }

    private void initListener() {
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //点击地图
                currentPt = latLng;
                updateTextView();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                //点击poi点
                currentPt = mapPoi.getPosition();
                updateTextView();
                return false;
            }
        });
    }

    private void updateTextView() {
        if (tvJingwei == null) {
            return;
        }
        String state = "";
        if (currentPt == null) {
            state = "点击、长按、双击地图以获取经纬度和地图状态";
        } else {
            state = String.format("当前经度： %f 当前纬度：%f",
                    currentPt.longitude, currentPt.latitude);
            LatLng ptCenter = new LatLng(currentPt.latitude, currentPt.longitude);
            //反Geo搜索
            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
        }
//        tvJingwei.setText(state);
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();

    }

    @OnClick({R.id.chat_publish_complete_cancle, R.id.chat_publish_complete_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_publish_complete_cancle:
                //取消
                finish();
                break;
            case R.id.chat_publish_complete_publish:
                //完成
                Intent intent =new Intent();
                intent.putExtra("address",address);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }


    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//             map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                //反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
