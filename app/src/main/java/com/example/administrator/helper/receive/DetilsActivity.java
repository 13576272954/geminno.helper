package com.example.administrator.helper.receive;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.administrator.helper.BaseFragment;
import com.example.administrator.helper.R;
import com.example.administrator.helper.receive.homePage.JianzhiFragment;
import com.example.administrator.helper.receive.homePage.JieyongFragment;
import com.example.administrator.helper.receive.homePage.QitaFragment;
import com.example.administrator.helper.receive.homePage.ShenghuoFragment;
import com.example.administrator.helper.receive.homePage.XiaoshouFragment;
import com.example.administrator.helper.receive.homePage.XuexiFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

//import com.example.myapplication.homePage.XuexiFragment;


public class DetilsActivity extends AppCompatActivity {


    List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
    @InjectView(R.id.xuexi_fragment)
    Button orderFragmentTab1;
    @InjectView(R.id.shenghuo_fragment)
    Button orderFragmentTab2;
    @InjectView(R.id.jieyong_fragment)
    Button orderFragmentTab3;
    @InjectView(R.id.jianzhi_fragment)
    Button orderFragmentTab4;
    @InjectView(R.id.xiaoshou_fragment)
    Button orderFragmentTab5;
    @InjectView(R.id.qita_fragment)
    Button orderFragmentTab6;
    @InjectView(R.id.fragment_viewpager)
    ViewPager orderFragmentViewpager;
    int flag = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detils);
        ButterKnife.inject(this);
        initView();
    }

    public void initView() {

        fragmentList.add(new XuexiFragment());
        fragmentList.add(new ShenghuoFragment());
       fragmentList.add(new JieyongFragment());
        fragmentList.add(new JianzhiFragment());
        fragmentList.add(new XiaoshouFragment());
        fragmentList.add(new QitaFragment());


        orderFragmentViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {

                //return fragments[position];
                Log.d("jj","position"+position);
                return fragmentList.get(position);
            }
            @Override
            public int getCount() {
                orderFragmentViewpager.getCurrentItem();
                color(orderFragmentViewpager.getCurrentItem());
                return fragmentList.size();
            }
        });
    }

    @OnClick({R.id.xuexi_fragment, R.id.shenghuo_fragment, R.id.jieyong_fragment, R.id.jianzhi_fragment, R.id.xiaoshou_fragment, R.id.qita_fragment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.xuexi_fragment:
                flag = 0;
                break;
            case R.id.shenghuo_fragment:
                flag = 1;
                break;
            case R.id.jieyong_fragment:
                flag = 2;
                break;
            case R.id.jianzhi_fragment:
                flag = 3;
                break;
            case R.id.xiaoshou_fragment:
                flag = 4;
                break;
            case R.id.qita_fragment:
                flag = 5;
                break;
        }
        orderFragmentViewpager.setOffscreenPageLimit(6);
        orderFragmentViewpager.setCurrentItem(flag);
    }

    public void color( int flag) {
        orderFragmentTab1.setBackgroundColor(0xffffffff);
        orderFragmentTab2.setBackgroundColor(0xffffffff);
        orderFragmentTab3.setBackgroundColor(0xffffffff);
        orderFragmentTab4.setBackgroundColor(0xffffffff);
        orderFragmentTab5.setBackgroundColor(0xffffffff);
        orderFragmentTab6.setBackgroundColor(0xffffffff);
        switch (flag) {
            case 0:
                orderFragmentTab1.setBackgroundColor(0xffBDBDBD);
                break;
            case 1:
                orderFragmentTab2.setBackgroundColor(0xffBDBDBD);
                break;
            case 2:
                orderFragmentTab3.setBackgroundColor(0xffBDBDBD);
                break;
            case 3:
                orderFragmentTab4.setBackgroundColor(0xffBDBDBD);
                break;
            case 4:
                orderFragmentTab5.setBackgroundColor(0xffBDBDBD);
                break;
            case 5:
                orderFragmentTab6.setBackgroundColor(0xffBDBDBD);
                break;


        }

    }
}
