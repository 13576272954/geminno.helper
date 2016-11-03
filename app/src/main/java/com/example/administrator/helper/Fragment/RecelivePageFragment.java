package com.example.administrator.helper.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.receive.DetilsActivity;
import com.example.administrator.helper.receive.TaskDetilsActivity;

import com.example.administrator.helper.utils.ImageLoader;
import com.example.administrator.helper.utils.UrlUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//import com.baidu.mapapi.SDKInitializer;
//import android.Manifest;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.location.Location;
//import android.location.LocationListener;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.MyLocationData;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.map.TextOptions;
//import com.baidu.mapapi.model.LatLng;
//import java.util.ArrayList;


public class RecelivePageFragment extends Fragment  {

//    @InjectView(R.id.ib_detils)
//    ImageButton ibDetils;
//    @InjectView(R.id.abc)
//    RadarView abc;
//    @InjectView(R.id.radar)
//    RadarViewGroup radar;
//    @InjectView(R.id.vp)
//    CustomViewPager vp;
//    @InjectView(R.id.textView1)
//    TextView textView1;
//
//
//    private CustomViewPager viewPager;
//    private RelativeLayout ryContainer;
//    private RadarViewGroup radarViewGroup;
//    List<User> list = new ArrayList<User>();
//
//    private int mPosition;
//    ViewpagerAdapter mAdapter;
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        locationService.stop();
//    }
//
//    private FixedSpeedScroller scroller;
//    private SparseArray<User> mDatas = new SparseArray<>();
//    String url2;
//    int tasktypeid = 0;
//    Integer pageNo = null;
//    Integer pageSize = null;
//    String city;
//    String taskDemand = null;
    View view;
//    ImageLoader myImageLoader;
//    private final int SDK_PERMISSION_REQUEST = 127;
//    private LocationService locationService;
//    private TextView LocationResult;
//    private Button startLocation;
//    private String permissionInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recelive_fragment, null);
//        LocationResult = (TextView) view.findViewById(R.id.textView1);
//        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
//        startLocation = (Button) view.findViewById(R.id.addfence);

//        onStart();
//        initView();
//        initData();
//        getPersimmions();
//        mAdapter = new ViewpagerAdapter();
//        viewPager.setAdapter(mAdapter);
//        //设置缓存数为展示的数目
//        viewPager.setOffscreenPageLimit(list.size());
//        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
//        //设置切换动画
//        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
//        viewPager.addOnPageChangeListener(this);
//        setViewPagerSpeed(250);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                radarViewGroup.setDatas(mDatas);
//            }
//        }, 1500);
//
//        radarViewGroup.setiRadarClickListener(this);
//
//        ButterKnife.inject(this, view);
//        onStop();
        return view;
    }

//    @TargetApi(23)
//    private void getPersimmions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ArrayList<String> permissions = new ArrayList<String>();
//            /***
//             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
//             */
//            // 定位精确位置
//            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//            }
//            /*
//			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
//			 */
//            // 读写权限
//            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
//            }
//            // 读取电话状态权限
//            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
//                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
//            }
//
//            if (permissions.size() > 0) {
//                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
//            }
//        }
//    }
//
//    @TargetApi(23)
//    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
//        if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
//            if (shouldShowRequestPermissionRationale(permission)) {
//                return true;
//            } else {
//                permissionsList.add(permission);
//                Log.i("RecelivePageFragment", "addPermission: " + permission);
//                return false;
//            }
//
//        } else {
//            return true;
//        }
//    }
//
//    @TargetApi(23)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        // TODO Auto-generated method stub
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//    }
//
//    private void initData() {
//        for (int i = 0; i < list.size(); i++) {
//            final User user = new User();
//            String url = UrlUtils.MYURL + "ReceiveServlet";//访问网络的url
//            RequestParams requestParams = new RequestParams(url);
//            requestParams.addQueryStringParameter("city", city + "");
//            requestParams.addQueryStringParameter("tasktypeid", tasktypeid + "");
//            requestParams.addQueryStringParameter("taskDemand", taskDemand + "");
//            requestParams.addQueryStringParameter("pageNo", pageNo + "");
//            requestParams.addQueryStringParameter("pageSize", pageSize + "");
//            x.http().get(requestParams, new Callback.CacheCallback<String>() {
//                @Override
//                public boolean onCache(String result) {
//                    return false;
//                }
//
//                @Override
//                public void onSuccess(String result) {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<List<User>>() {
//                    }.getType();
//                    List<User> newTasks = new ArrayList<User>();
//                    newTasks = gson.fromJson(result, type);
//                    list.clear();//清空原来的数据
//                    list.addAll(newTasks);
//                    viewPager.setAdapter(mAdapter);
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//
//                }
//
//                @Override
//                public void onFinished() {
//
//                }
//            });
//            mDatas.put(i, user);
//        }
//    }
//
//    private void initView() {
//        viewPager = (CustomViewPager) view.findViewById(R.id.vp);
//        radarViewGroup = (RadarViewGroup) view.findViewById(R.id.radar);
//        ryContainer = (RelativeLayout) view.findViewById(R.id.ry_container);
//    }
//
//    /**
//     * 设置ViewPager切换速度
//     *
//     * @param duration
//     */
//    private void setViewPagerSpeed(int duration) {
//        try {
//            Field field = ViewPager.class.getDeclaredField("mScroller");
//            field.setAccessible(true);
//            scroller = new FixedSpeedScroller(getActivity(), new AccelerateInterpolator());
//            field.set(viewPager, scroller);
//            scroller.setmDuration(duration);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        mPosition = position;
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        radarViewGroup.setCurrentShowItem(position);
//        LogUtil.m("当前位置 " + mPosition);
//        LogUtil.m("速度 " + viewPager.getSpeed());
//        //当手指左滑速度大于2000时viewpager右滑（注意是item+2）
//        if (viewPager.getSpeed() < -1800) {
//
//            viewPager.setCurrentItem(mPosition + 2);
//            LogUtil.m("位置 " + mPosition);
//            viewPager.setSpeed(0);
//        } else if (viewPager.getSpeed() > 1800 && mPosition > 0) {
//            //当手指右滑速度大于2000时viewpager左滑（注意item-1即可）
//            viewPager.setCurrentItem(mPosition - 1);
//            LogUtil.m("位置 " + mPosition);
//            viewPager.setSpeed(0);
//        }
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
//
//    @Override
//    public void onRadarItemClick(int position) {
//        viewPager.setCurrentItem(position);
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//        locationService.stop();
//    }
//
//    @OnClick(R.id.ib_detils)
//    public void onClick() {
//        Intent intent = new Intent(getActivity(), DetilsActivity.class);
//
//        startActivity(intent);
//
//    }
//
//
//    class ViewpagerAdapter extends PagerAdapter {
//        @Override
//        public Object instantiateItem(ViewGroup container, final int position) {
//            final User user = mDatas.get(position);
//            //设置一大堆演示用的数据，麻里麻烦~~
//            final View view = LayoutInflater.from(getActivity()).inflate(R.layout.viewpager_layout, null);
//            ImageView image = (ImageView) view.findViewById(R.id.iv);
//            url2 = user.getImage();
//            myImageLoader = new ImageLoader(getActivity());
//            myImageLoader.showImageByUrl(url2, image);
//            ImageView ivSex = (ImageView) view.findViewById(R.id.iv_sex);
//            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
//            TextView tvDistance = (TextView) view.findViewById(R.id.tv_distance);
//            tvName.setText(user.getName());
////            tvDistance.setText(user.getDistance() + "km");
//
//            view.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return viewPager.dispatchTouchEvent(event);
//                }
//            });
//
//
//            if (user.getSex() == null) {
//                ivSex.setImageResource(R.drawable.girl);
//            } else {
//                ivSex.setImageResource(R.drawable.boy);
//            }
//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), TaskDetilsActivity.class);
//                    intent.putExtra("dd", (Parcelable) list.get(position));
//                    startActivityForResult(intent, 111);
//                    Toast.makeText(getActivity(), "这是 " + user.getName() + " >.<", Toast.LENGTH_SHORT).show();
//
//                }
//            });
//            container.addView(view);
//            return view;
//        }
//
//        @Override
//        public int getCount() {
//            return list.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            View view = (View) object;
//            container.removeView(view);
//        }
//
//    }
//
//    /**
//     * 显示请求字符串
//     *
//     * @param str
//     */
//    public void logMsg(String str) {
//        try {
//            if (LocationResult != null)
//                LocationResult.setText(str);
//            Log.i("RecelivePageFragment", "onReceiveLocation1111: " + str);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i("RecelivePageFragment", "onReceiveLocation2222: " + str);
//        }
//    }
//
//
//    /***
//     * Stop location service
//     */
//    @Override
//    public void onStop() {
//        // TODO Auto-generated method stub
//        locationService.unregisterListener(mListener); //注销掉监听
//        locationService.stop(); //停止定位服务
//        Log.i("RecelivePageFragment", "onStop222: " + mListener);
//        super.onStop();
//    }
//
//    @Override
//    public void onStart() {
//        // TODO Auto-generated method stub
//        super.onStart();
//        // -----------location config ------------
//        locationService = ((MyApplication) getActivity().getApplication()).locationService;
//        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
//        locationService.registerListener(mListener);
//        //注册监听
////        int type = getActivity().getIntent().getIntExtra("from", 0);
////        if (type == 0) {
//        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
////        }
//        Log.i("LocationService", "getDefaultLocationClientOption44444: " + mListener);
////        else if (type == 1) {
////            locationService.setLocationOption(locationService.getOption());
////        }
////        startLocation.setOnClickListener(new View.OnClickListener() {
//
////            @Override
////            public void onClick(View v) {
////                if (startLocation.getText().toString().equals(getString(R.string.startlocation))) {
//
//        locationService.start();// 定位SDK
//
//        Log.i("RecelivePageFragment", "onStart8888888888: ");
//
//        // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
////                    startLocation.setText(getString(R.string.stoplocation));
////                } else {
//
////                    startLocation.setText(getString(R.string.startlocation));
////                }
////            }
////        });/
//    }

//    private BDLocationListener mListener = new BDLocationListener() {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            // TODO Auto-generated method stub
//
//            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
//                StringBuffer sb = new StringBuffer(256);
//                sb.append("time : ");
////                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
////                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
////                 */
//                sb.append(location.getTime());
//                sb.append("\nlocType : ");// 定位类型
//                sb.append(location.getLocType());
//                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
//                sb.append(location.getLocTypeDescription());
//                sb.append("\nlatitude : ");// 纬度
//                sb.append(location.getLatitude());
//                sb.append("\nlontitude : ");// 经度
//                sb.append(location.getLongitude());
//                sb.append("\nradius : ");// 半径
//                sb.append(location.getRadius());
//                sb.append("\nCountryCode : ");// 国家码
//                sb.append(location.getCountryCode());
//                sb.append("\nCountry : ");// 国家名称
//                sb.append(location.getCountry());
//                sb.append("\ncitycode : ");// 城市编码
//                Log.i("RecelivePageFragment", "onReceiveLocation55555: " + location.getCountry());
//                sb.append(location.getCityCode());
//                sb.append("\ncity : ");// 城市
//                Log.i("RecelivePageFragment", "onReceiveLocation55555: " + location.getCityCode());
//                sb.append(location.getCity());
//                sb.append("\nDistrict : ");// 区
//                sb.append(location.getDistrict());
//                sb.append("\nStreet : ");// 街道
//                sb.append(location.getStreet());
//                sb.append("\naddr : ");// 地址信息
//                sb.append(location.getAddrStr());
//                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
//                sb.append(location.getUserIndoorState());
//                sb.append("\nDirection(not all devices have value): ");
//                sb.append(location.getDirection());// 方向
//                sb.append("\nlocationdescribe: ");
//                sb.append(location.getLocationDescribe());// 位置语义化信息
//                sb.append("\nPoi: ");// POI信息
//
//                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
//                    for (int i = 0; i < location.getPoiList().size(); i++) {
//                        Poi poi = (Poi) location.getPoiList().get(i);
//                        sb.append(poi.getName() + ";");
//                    }
//                }
//                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
//                    Log.i("RecelivePageFragment", "onReceiveLocation: " + location.getLocType());
//                    sb.append("\nspeed : ");
//                    sb.append(location.getSpeed());// 速度 单位：km/h
//                    sb.append("\nsatellite : ");
//                    sb.append(location.getSatelliteNumber());// 卫星数目
//                    sb.append("\nheight : ");
//                    sb.append(location.getAltitude());// 海拔高度 单位：米
//                    sb.append("\ngps status : ");
//                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
//                    sb.append("\ndescribe : ");
//                    Log.i("RecelivePageFragment", "onReceiveLocation77777777: " + location.getLocType());
//                    sb.append("gps定位成功");
//                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
////                     运营商信息
//                    if (location.hasAltitude()) {// *****如果有海拔高度*****
//                        sb.append("\nheight : ");
//                        sb.append(location.getAltitude());// 单位：米
//                    }
//                    sb.append("\noperationers : ");// 运营商信息
//                    sb.append(location.getOperators());
//                    sb.append("\ndescribe : ");
//                    Log.i("RecelivePageFragment", "onReceiveLocatio6666666666: " + location.getLocType());
//                    sb.append("网络定位成功");
//                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                    sb.append("\ndescribe : ");
//                    sb.append("离线定位成功，离线定位结果也是有效的");
//                } else if (location.getLocType() == BDLocation.TypeServerError) {
//                    sb.append("\ndescribe : ");
//                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                    sb.append("\ndescribe : ");
//                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
//                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                    sb.append("\ndescribe : ");
//                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//                }
////                logMsg(sb.toString());
//
//            }
//        }
//
//    };
}


//    @OnClick({ R.id.ib_detils})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.ib_detils:
//                Intent intent = new Intent(getActivity(), DetilsActivity.class);
//                startActivity(intent);
//                break;
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }


//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
////        SDKInitializer.initialize(getActivity().getApplicationContext());
//        View view = inflater.inflate(R.layout.recelive_fragment, null);
//        SDKInitializer.initialize(getActivity().getApplicationContext());
//
//        //获取id信息
//        Intent intent = getActivity().getIntent();
//        user_id = intent.getIntExtra("user_id", 0);
//
//        ButterKnife.inject(getActivity());
////        init();
//        //获取地图对象
//        bdMap = mMapView.getMap();
//        bdMap.setMyLocationEnabled(true);
//        //获取LocationManager的实例，getSystemService（）方法用来确定获取系统的那个服务
//        //注似乎位置提供器服务必须由用户自己开启，用app没有方法可以直接调用权限的的样子
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//        //获取所有定位服务
//        providerList = locationManager.getProviders(true);
//        //判断是否开启了gps
//        Log.d("aaa", "" + providerList + "GPS_PROVIDER" + providerList.contains(LocationManager.GPS_PROVIDER));
//        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
//            Toast.makeText(getActivity(), "您的gps功能未开启，或者卫星链接过少，自动帮您启用网络服务定位，可能导致定位偏差较大", Toast.LENGTH_LONG).show();
//            provider = LocationManager.NETWORK_PROVIDER;
//        } else if (providerList.contains(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(getActivity(), "正在使用gps服务定位", Toast.LENGTH_SHORT).show();
//            provider = LocationManager.GPS_PROVIDER;
//            //判断是否开启了网络定位
//        } else
//
//        {
//            Toast.makeText(getActivity(), "您还未开启定位服务，请您先在设置中开启定位服务功能，谢谢", Toast.LENGTH_SHORT).show();
//
//        }
//
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//        }
//
//        Location location = locationManager.getLastKnownLocation(provider);
////        if (location != null) {
//            //调用方法，将获取的数据取出并装到LatLng中
////            navigateTo(location);
////        }
//        //监听器，时间每5秒监听一次，距离没1m监听一次
////        locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
//
//  /*
//         //在定点上添加一个覆盖物 先生成一个bitmap图作为icon然后设置位置，最后设置是可拖动（长按图标可拖动）
//        MarkerOptions marker2 = new MarkerOptions();
//        BitmapDescriptor bitmap2 = BitmapDescriptorFactory
//                .fromResource(R.drawable.zh);
//        marker2.icon(bitmap2).position(new LatLng(23.0544980000, 113.413000000)).draggable(false);
//        bdMap.addOverlay(marker2);*/
//        //获取覆盖物对象
////        setMarkerInfo();
//        ButterKnife.inject(this, view);
//        return view;
//    }


//        private void navigateTo(Location location) {
//            if (isFirstLocate) {
//                //将将获取的数据取出并装到LatLng中(确定要定定位的位置)
//                // LatLng a = new LatLng(23, 113);
//                j = location.getLongitude();
//                w = location.getLatitude();
//                //  j=113.0;
//                //  w=24.0;
//                LatLng a = new LatLng(w, j);
//                Log.d("ppp", w + "  " + j);
//                //将LatLng对象转为MapStatusUpdate对象
//                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(a);
//                //将位置信息最后转化为BaiduMap的属性
//                bdMap.animateMapStatus(update);
//                //设置缩放等级3~19
//                update = MapStatusUpdateFactory.zoomTo(19f);
//                bdMap.animateMapStatus(update);
//                //将标签修改
//                isFirstLocate = false;
//            }
//            MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
//            locationBuilder.latitude(location.getLatitude());
//            locationBuilder.longitude(location.getLongitude());
//            MyLocationData locationData = locationBuilder.build();
//            bdMap.setMyLocationData(locationData);
//        }
//
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                //更新位置
//                if (location != null) {
//                    navigateTo(location);
//                }
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//            }
//        };
//
//        private void init() {
//            mMapView = (MapView) getActivity().findViewById(R.id.bmapsView);
//        }
//
//        @Override
//        public void onResume() {
//            super.onResume();
//            mMapView.onResume();
//        }
//
//        @Override
//        public void onPause() {
//            super.onPause();
//            mMapView.onPause();
//        }
//
//        @Override
//        public void onDestroy() {
//            super.onDestroy();
//            bdMap.setMyLocationEnabled(false);
//            mMapView.onDestroy();
//            if (locationManager != null) {
//                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                //退出时关闭监听器
//                locationManager.removeUpdates(locationListener);
//            }
//        }
//
//        //初始化覆盖物
//        private void newVenuess(double a, double b, String name) {
//
//            MarkerOptions marker = new MarkerOptions();
//            BitmapDescriptor bitmap = BitmapDescriptorFactory
//                    .fromResource(R.mipmap.weizi);
//            marker.icon(bitmap).position(new LatLng(a, b)).draggable(false);
//            bdMap.addOverlay(marker);
//
//         /*
//         * 创建文字覆盖物
//         */
//            TextOptions text = new TextOptions();
//            text.bgColor(Color.GRAY).fontColor(Color.GREEN).text(name)
//                    .position(new LatLng(a, b))
//                    .fontSize(25);
//            bdMap.addOverlay(text);
//            //响应事件
//            bdMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    Toast.makeText(getActivity(), "登录成功，欢迎您", Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            });
//        }
//
//        private void setMarkerInfo() {
//
//            infos = new ArrayList<Task>();
//            infos1 = new ArrayList<Task>();
//            infos2 = new ArrayList<Task>();
//            infos3 = new ArrayList<Task>();
//            infos4 = new ArrayList<Task>();
//            infos5 = new ArrayList<Task>();
//            infos6 = new ArrayList<Task>();
//
//            //从网络获取数据
//            RequestParams params = new RequestParams("http://172.27.35.1:8080/kdc/MaplistServlet");
//            params.addBodyParameter("userId", user_id + "");//post方法的传值
//            //  Toast.makeText(MaplistActivity.this, "正在登录中，请稍等。。。", Toast.LENGTH_SHORT).show();
//            x.http().post(params, new Callback.CommonCallback<String>() {//post的方式网络通讯
//                @Override
//                public void onSuccess(String result) {
//                    Gson gson = new Gson();
//                    Log.e("aaaa", "场地信息返回成功" + result);
//                    List<Task> list = gson.fromJson(result,
//                            new TypeToken<List<Task>>() {
//                            }.getType());
//
//                    for (Task lis : list) {
//                        infos.add(lis);
//                        Log.e("aaaaa", "venuesList：" + infos);
//                    }
//                    //将场馆按类型分类
//                    for (Task info : infos) {
//                        if (info.getTaskType() == 1) {
//                            infos1.add(info);
//                        } else if (info.getTaskType() == 2) {
//                            infos2.add(info);
//                        } else if((info.getTaskType() == 3){
//                            infos3.add(info);
//                        }
//                        else if (info.getTaskType() == 4) {
//                            infos4.add(info);
//                        } else if (info.getTaskType() ==5) {
//                            infos5.add(info);
//                        } else  {
//                            infos6.add(info);
//                        }
//                    }
//                    //显示覆盖物
//                    addOverlay(infos);
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//                    Log.e("aaaaa", "链接失败");
//                    //    Toast.makeText(LoginActivity.this, "您的账号/密码错误，请区分大小写或者重新输入", Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//                    Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onFinished() {
//                }
//            });
//
//            Log.e("aaaaa", "venuesList2222：" + infos);
//        }
//
//        //显示marker
//        private void addOverlay(List<Task> infos) {
//            //清空地图
//            bdMap.clear();
//            //创建marker的显示图标
//            BitmapDescriptor bitmap;
//            LatLng latLng = null;
//            Marker marker;
//            OverlayOptions options;
//            for (Task info : infos) {
//                //按类型不同显示不同的覆盖物
//                if (info.getTaskType() == 1) {
//                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.back);
//                } else if (info.getTaskType() == 2) {
//                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.tab_bg);
//                } else if(info.getTaskType()==3){
//                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.nav_back);
//                }else if(info.getMakePlace()==4){
//                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.nav_back);
//                }
//                else if(info.getMakePlace()==5){
//                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.arrow);
//                }else if(info.getMakePlace()==6){
//                    bitmap = BitmapDescriptorFactory.fromResource(R.drawable.bg_default_face);
//                }
//                //获取经纬度
//                latLng = new LatLng(info.getLatitude(), info.getLongitude());
//                //设置marker
//                options = new MarkerOptions()
//                        .position(latLng)//设置位置
//                        .icon(bitmap)//设置图标样式
//                        .zIndex(9) // 设置marker所在层级
//                ; // 设置手势拖拽;
//                //添加marker
//                marker = (Marker) bdMap.addOverlay(options);
//                //添加文本
//                TextOptions text = new TextOptions();
//                text.bgColor(Color.GRAY).fontColor(Color.GREEN).text(info.getTaskType())
//                        .position(new LatLng(info.getLatitude(), info.getLongitude()))
//                        .fontSize(25);
//                bdMap.addOverlay(text);
//                //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
//                Bundle bundle = new Bundle();
//                //info必须实现序列化接口
//                bundle.putSerializable("info", info);
//                marker.setExtraInfo(bundle);
//            }
//            //将地图显示在最后一个marker的位置
//            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
//            bdMap.setMapStatus(msu);
//            //添加marker点击事件的监听
//            bdMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    //从marker中获取info信息
//                    Bundle bundle = marker.getExtraInfo();
//                    Task infoUtil = (Task) bundle.getSerializable("info");
//
//跳转到详情表
//                    Intent intent = new Intent(getActivity(), DetilsActivity.class);
//                    intent.putExtra("user_id", 2);
//                    Bundle bundle2 = new Bundle();
//                    bundle2.putSerializable("venues_tbl", infoUtil);//序列化
//                    intent.putExtras(bundle2);//发送数据
//                    startActivity(intent);
//                    return true;
//                }
//            });
//        }
//
////        //菜单按钮
////        @OnClick({R.id., R.id.b_up, R.id.b_xx,R.id.button})
////        public void onClick(View view) {
////            switch (view.getId()) {
////                case R.id.b_show:
////
////                    Intent intent = new Intent(getActivity(), DetilsActivity.class);
////                    intent.putExtra("user_id", user_id);
////                    startActivity(intent);
////
////                    break;
////                case R.id.b_up:
////                    Intent intent2 = new Intent(MapActivity.this, UploadActivity.class);
////                    intent2.putExtra("user_id", user_id);
////                    intent2.putExtra("w", w);
////                    intent2.putExtra("j", j);
////                    startActivity(intent2);
////                    break;
////                case R.id.button:
////                    new  AlertDialog.Builder(getActivity())
////                            .setTitle("请选择定位方式" )
////                            .setIcon(android.R.drawable.ic_dialog_info)
////                            .setSingleChoiceItems(new  String[] {"gps定位", "网络定位" },flag,
////                                    new  DialogInterface.OnClickListener() {
////                                        public   void  onClick(DialogInterface dialog,  int  which) {
////                                            if(which==0){
////                                                flag=0;
////                                            }else{
////                                                flag=1;
////                                            }
////                                        }
////                                    }
////                            )
////                            .setNegativeButton("确定" , new DialogInterface.OnClickListener() {
////                                @Override
////                                public void onClick(DialogInterface dialog, int which) {
////                                    if (flag==0){
////                                        provider = LocationManager.GPS_PROVIDER;
////                                    }else{
////                                        provider = LocationManager.NETWORK_PROVIDER;
////                                    }
////                                }
////                            })
////                            .show();
////                    break;
////                case R.id.b_xx:
////                    final String[] hobbies = {"显示支持预约的付费场地", "显示不支持预约的付费场地", "显示免费的野场地"};
////                    new AlertDialog.Builder(getActivity())
////                            .setTitle("选择显示的场地类型（多选）")
////                            .setMultiChoiceItems(hobbies, new boolean[]{flag1, flag2, flag3,flag4,flag5,flag6}, new DialogInterface.OnMultiChoiceClickListener() {
////                                @Override
////                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
////                                    if (which == 0) {
////                                        flag1 = !flag1;
////                                    } else if (which == 1) {
////                                        flag2 = !flag2;
////                                    } else if (which == 2) {
////                                        flag3 = !flag3;
////                                } else if (which == 3) {
////                                    flag4= !flag4;
////                                } else if (which == 4) {
////                                    flag5 = !flag5;
////                                }else{
////                                    flag6=!flag6
////                                }
////                                }
////                            })
////                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
////                                @Override
////                                public void onClick(DialogInterface dialog, int which) {
////                                    infos.clear();
////                                    if (flag1) {
////                                        infos.addAll(infos1);
////                                    }
////                                    if (flag2) {
////                                        infos.addAll(infos2);
////                                    }
////                                    if (flag3) {
////                                        infos.addAll(infos3);
////                                    }
////                                    addOverlay(infos);
////                                    flag11 = flag1;
////                                    flag22 = flag2;
////                                    flag33 = flag3;
////                                }
////                            })
////                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
////                                @Override
////                                public void onClick(DialogInterface dialog, int which) {
////                                    flag1 = flag11;
////                                    flag2 = flag22;
////                                    flag3 = flag33;
////                                }
////                            })
////                            .show();
////                    break;
////            }
////        }
////    }
//
//
//    @OnClick({R.id.ib_message, R.id.ib_detils, R.id.et_myw, R.id.et_myd})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.ib_message:
//                break;
//            case R.id.ib_detils:
//                Intent intent = new Intent(getActivity(), DetilsActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.et_myw:
//                break;
//            case R.id.et_myd:
//                break;
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.reset(this);
//    }
//    }}