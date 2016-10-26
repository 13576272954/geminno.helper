package com.example.administrator.helper.my.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.administrator.helper.BaseFragment;
import com.example.administrator.helper.R;
import com.example.administrator.helper.my.Fragment.DaiFuKuanFragment;
import com.example.administrator.helper.my.Fragment.DaiPingJiaFragment;
import com.example.administrator.helper.my.Fragment.JinXingZhongFragment;
import com.example.administrator.helper.my.Fragment.DaiKaiShiFragment;
import com.example.administrator.helper.my.Fragment.QuanBuFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class Wodefadan extends AppCompatActivity {

    @InjectView(R.id.all_order_goback)
    ImageView allOrderGoback;
    @InjectView(R.id.order_fragment_tab1)
    TextView orderFragmentTab1;
    @InjectView(R.id.order_fragment_tab2)
    TextView orderFragmentTab2;
    @InjectView(R.id.rder_fragment_tab3)
    TextView rderFragmentTab3;
    @InjectView(R.id.rder_fragment_tab4)
    TextView rderFragmentTab4;
    @InjectView(R.id.rder_fragment_tab5)
    TextView rderFragmentTab5;
    @InjectView(R.id.id_Linearly)
    LinearLayout idLinearly;
    @InjectView(R.id.order_fragment_viewpager)
    ViewPager orderFragmentViewpager;
    //创建fragment (碎片)的集合
    List<BaseFragment> lists=new ArrayList<BaseFragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Wodefadan", "onCreate: 调到了");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wodefadan);
        ButterKnife.inject(this);

        initData();//调用
        allOrderGoback= (ImageView) findViewById(R.id.all_order_goback);
        allOrderGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }


    //viewpage显示的fragment的数据源
    public  void initData() {
        //往碎片集合里加东西
        lists.add(new QuanBuFragment());
        lists.add(new DaiFuKuanFragment());
        lists.add(new DaiKaiShiFragment());
        lists.add(new JinXingZhongFragment());
        lists.add(new DaiPingJiaFragment());

        orderFragmentViewpager.setOffscreenPageLimit(5);

   orderFragmentViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
      public Fragment getItem(int position) {
                Log.i("Wodefadan", "getItem: 0000");
                return lists.get(position);
            }

            @Override
       public int getCount() {
                //返回listView的大小 有几个Fragment。
                //数据写在 fragment 。
      return lists.size();
            }
        });


        orderFragmentViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("AllOrdersActivity", "onPageSelected: " + position);
                TextView textView;

                for (int i = 0; i < lists.size(); i++) {
                    textView = (TextView) idLinearly.getChildAt(i);
                    textView.setSelected(false);
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                textView = (TextView) idLinearly.getChildAt(position);
                textView.setSelected(true);
                textView.setTextColor(Color.RED);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @OnClick({R.id.order_fragment_tab1, R.id.order_fragment_tab2, R.id.rder_fragment_tab3, R.id.rder_fragment_tab4, R.id.rder_fragment_tab5})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_fragment_tab1:
                orderFragmentViewpager.setCurrentItem(0);
                break;
            case R.id.order_fragment_tab2:
                orderFragmentViewpager.setCurrentItem(1);
                break;
            case R.id.rder_fragment_tab3:
                orderFragmentViewpager.setCurrentItem(2);
                break;
            case R.id.rder_fragment_tab4:
                orderFragmentViewpager.setCurrentItem(3);
                break;
            case R.id.rder_fragment_tab5:
                orderFragmentViewpager.setCurrentItem(4);
                break;



    }

    }
}



