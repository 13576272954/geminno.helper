package com.example.administrator.helper.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.bumptech.glide.Glide;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Task;
import com.example.administrator.helper.entity.User;
import com.example.administrator.helper.receive.DetilsActivity;
import com.example.administrator.helper.receive.TaskDetilsActivity;

import com.example.administrator.helper.receive.custom.CustomViewPager;
import com.example.administrator.helper.receive.custom.RadarView;
import com.example.administrator.helper.receive.custom.RadarViewGroup;
import com.example.administrator.helper.receive.homePage.XuexiFragment;
import com.example.administrator.helper.receive.utils.CropCircleTransformation;
import com.example.administrator.helper.receive.utils.FixedSpeedScroller;
import com.example.administrator.helper.receive.utils.LogUtil;
import com.example.administrator.helper.receive.utils.ZoomOutPageTransformer;
import com.example.administrator.helper.utils.ImageLoader;
import com.example.administrator.helper.utils.LocationService;
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
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class RecelivePageFragment extends Fragment implements ViewPager.OnPageChangeListener, RadarViewGroup.IRadarClickListener {

    @InjectView(R.id.ib_detils)
    ImageButton ibDetils;
    @InjectView(R.id.abc)
    RadarView abc;
    @InjectView(R.id.radar)
    RadarViewGroup radar;
    @InjectView(R.id.vp)
    CustomViewPager vp;
    @InjectView(R.id.textView1)
    TextView textView1;
    private CustomViewPager viewPager;
    private RelativeLayout ryContainer;
    private RadarViewGroup radarViewGroup;
    List<Task> list = new ArrayList<Task>();

    private int mPosition;

    List<GeoCoder> mSearchList = new ArrayList<>();
    GeoCoder mSearch = null;
    @Override
    public void onDestroy() {
        super.onDestroy();
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
    }
    private FixedSpeedScroller scroller;
    private SparseArray<Task> mDatas = new SparseArray<>();
    String url2;
    int tasktypeid = -1;
    Integer pageNo = null;
    Integer pageSize = null;
    String city = null;
    String myaddress = null ;
    String taskDemand = null;
     View view;
    int i=0;
    ImageLoader myImageLoader;
    private final int SDK_PERMISSION_REQUEST = 127;
    private LocationService locationService;
//    private TextView LocationResult;
//    private Button startLocation;
    private String permissionInfo;
//    String strInfo;

    LatLng minePoint = null;
    LatLng newPoint = null ;

    OnGetGeoCoderResultListener listener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recelive_fragment, null);

        getPersimmions();
        initView();


        ryContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });
        Log.i("RecelivePageFragment", "onCreateViewxvvvvvvvvvvv: "+list.size());

        ButterKnife.inject(this, view);
//        onStop();
        return view;
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                Log.i("RecelivePageFragment", "addPermission: " + permission);
                return false;
            }

        } else {
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
   float newDestain;
    private void initData() {

            String url = UrlUtils.MYURL + "ReceiveServlet";//访问网络的url

            RequestParams requestParams = new RequestParams(url);
            requestParams.addQueryStringParameter("city",city);
            requestParams.addQueryStringParameter("tasktypeid", tasktypeid + "");
            requestParams.addQueryStringParameter("taskDemand", taskDemand + "");
            requestParams.addQueryStringParameter("pageNo", pageNo + "");
            requestParams.addQueryStringParameter("pageSize", pageSize + "");

            x.http().get(requestParams, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Task>>() {
                    }.getType();
                    List<Task> newTasks = new ArrayList<Task>();
                    newTasks = gson.fromJson(result, type);
                    Log.i("RecelivePageFragment", "initData8888885555555555: " + newTasks);
                    list.clear();//清空原来的数据
                    list.addAll(newTasks);
                    geocodeDiGui(list.size()-1);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("RecelivePageFragment", "initDataxxxxxxxxxxb: "+ex);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    Log.i("RecelivePageFragment", "initDatasssssssssss: ");
                }
            });

    }

    void geocodeDiGui(final int size){

        if( size < 0  ){
            ViewpagerAdapter  mAdapter = new ViewpagerAdapter();
//            Log.i("RecelivePageFragment", "geocodeDiGui: "+list.get(size-1));
            viewPager.setAdapter(mAdapter);
            //设置缓存数为展示的数目
            Log.i("RecelivePageFragment", "onCreateView22: "+mAdapter);
            viewPager.setOffscreenPageLimit(list.size());
            Log.i("RecelivePageFragment", "onCreateView: "+list.size());
            viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
            //设置切换动画
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
            viewPager.addOnPageChangeListener(this);
            Log.i("RecelivePageFragment", "onCreateView000000000000: "+mDatas);
            setViewPagerSpeed(250);

            Log.i("RecelivePageFragment", "onCreateView1111111111: "+mDatas);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i("RecelivePageFragment", "run22222222222: "+mDatas);
                    radarViewGroup.setDatas(mDatas);
                }
            }, 1500);
            radarViewGroup.setiRadarClickListener(this);
            Log.i("RecelivePageFragment", "geocodeDiGui:-----------111 "+mDatas);
            return;
        }
        Log.i("RecelivePageFragment", "geocodeDiGui:1111111111 "+list.size());
        GeoCodeOption GeoOption = new GeoCodeOption().city(list.get(size).getMakePlace()).address(list.get(size).getCity());

        Log.i("RecelivePageFragment", "geocodeDiGui:222222222222 "+list.size());
        mSearch = GeoCoder.newInstance();
        mSearch.geocode(GeoOption);

        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                newPoint=geoCodeResult.getLocation();
                User user = new User();
                newDestain = user.calDistance(minePoint,newPoint);
                newDestain = newDestain/1000;
                final String image= list.get(size).getSendUser().getImage();
                list.get(size).getSendUser().setImage(image);
                final  String name=list.get(size).getSendUser().getName();
                list.get(size).getSendUser().setName(name);
                list.get(size).getSendUser().setDistance(newDestain);
                final String sex=list.get(size).getSendUser().getSex();
                list.get(size).getSendUser().setSex(sex);
               mDatas.put(size, list.get(size));
                Log.i("RecelivePageFragment", "onGetGeoCodeResult: "+list.get(size));
               geocodeDiGui(size - 1);
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

            }});

    }

    private void initView() {
        viewPager = (CustomViewPager) view.findViewById(R.id.vp);
        radarViewGroup = (RadarViewGroup) view.findViewById(R.id.radar);
        ryContainer = (RelativeLayout) view.findViewById(R.id.ry_container);
        startService();
    }

    private void setViewPagerSpeed(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            scroller = new FixedSpeedScroller(getActivity(), new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(duration);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPosition = position;
    }

    @Override
    public void onPageSelected(int position) {
        radarViewGroup.setCurrentShowItem(position);
        LogUtil.m("当前位置 " + mPosition);
        LogUtil.m("速度 " + viewPager.getSpeed());
        //当手指左滑速度大于2000时viewpager右滑（注意是item+2）
        if (viewPager.getSpeed() < -1800) {
            viewPager.setCurrentItem(mPosition + 2);
            LogUtil.m("位置 " + mPosition);
            viewPager.setSpeed(0);
        } else if (viewPager.getSpeed() > 1800 && mPosition > 0) {
            //当手指右滑速度大于2000时viewpager左滑（注意item-1即可）
            viewPager.setCurrentItem(mPosition - 1);
            LogUtil.m("位置 " + mPosition);
            viewPager.setSpeed(0);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRadarItemClick(int position) {
        viewPager.setCurrentItem(position);
    }

    @OnClick(R.id.ib_detils)
    public void onClick() {
        Intent intent = new Intent(getActivity(),XuexiFragment.class);
        intent.putExtra("xiangxi", (Parcelable)list.get(i));
        startActivityForResult(intent, 222);
    }
    class ViewpagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Log.i("RecelivePageFragment", "ofgfgfgfdgdfgd: "+container+"            "+position+"             "+mDatas);
            final Task task = mDatas.get(position);

            final View view = LayoutInflater.from(getActivity()).inflate(R.layout.viewpager_layout, null);
            ImageView image = (ImageView) view.findViewById(R.id.iv);
            url2=task.getSendUser().getImage();
            myImageLoader = new ImageLoader(getActivity());
            myImageLoader.showImageByUrl(url2, image);
            Glide.with(getActivity()).load(url2).bitmapTransform(new CropCircleTransformation(getActivity())).crossFade(900).into(image);
            ImageView ivSex = (ImageView) view.findViewById(R.id.iv_sex);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name1);
            TextView tvDistance = (TextView) view.findViewById(R.id.tv_distance);
             tvName.setText(task.getSendUser().getName());
             tvDistance.setText(newDestain+"km");
         if (task.getSendUser().getSex().equals("女")) {
                ivSex.setImageResource(R.drawable.girl);
            } else if(task.getSendUser().getSex().equals("男")){
                ivSex.setImageResource(R.drawable.boy);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TaskDetilsActivity.class);
                    intent.putExtra("productInfo", (Parcelable)list.get(position) );
                    startActivityForResult(intent, 123);
                    Toast.makeText(getActivity(), "快来帮助我吧！", Toast.LENGTH_SHORT).show();

                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }

    public void logMsg(String city,String myaddress , LatLng point) {
            this.city=city;
            this.myaddress=myaddress;
            this.minePoint = point;
             initData();

    }


    public void startService(){
        locationService = ((MyApplication) getActivity().getApplication()).locationService;
         locationService.registerListener(mListener);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
       locationService.start();// 定位SDK
    }

    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub

            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                String address=location.getDistrict()+location.getStreet();
                locationService.unregisterListener(mListener);
                locationService.stop();
                logMsg(location.getCity(),address, new LatLng(location.getLatitude() , location.getLongitude())) ;

            }
        }

    };
}
