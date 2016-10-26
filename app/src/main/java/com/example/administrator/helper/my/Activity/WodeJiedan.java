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
import com.example.administrator.helper.my.Fragment.JiedaiDaipingjia;
import com.example.administrator.helper.my.Fragment.JiedanQuanbu;
import com.example.administrator.helper.my.Fragment.JiedanWancheng;
import com.example.administrator.helper.my.Fragment.Jiedanjinxingzhong;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WodeJiedan extends AppCompatActivity {

    @InjectView(R.id.all_jiedan_order_goback)
    ImageView allOrderGoback;
    @InjectView(R.id.order_jiedan_fragment_tab1)
    TextView orderJiedanFragmentTab1;
    @InjectView(R.id.order_jiedan_fragment_tab2)
    TextView orderJiedanFragmentTab2;
    @InjectView(R.id.order_jiedan_fragment_tab3)
    TextView orderJiedanFragmentTab3;
    @InjectView(R.id.order_jiedan_fragment_tab4)
    TextView orderJiedanFragmentTab4;
    @InjectView(R.id.jiedan_huadong)
    LinearLayout jiedanHuadong;
    @InjectView(R.id.order_jiedan_fragment_viewpager)
    ViewPager orderJiedanFragmentViewpager;
    //创建fragment (碎片)的集合
    List<BaseFragment> lists=new ArrayList<BaseFragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wode_jiedan);
        ButterKnife.inject(this);
        allOrderGoback= (ImageView) findViewById(R.id.all_jiedan_order_goback);
        allOrderGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        initData();//调用
    }

    //viewpage显示的fragment的数据源
    public  void initData() {
        //往碎片集合里加东西
        lists.add(new JiedanQuanbu());
        lists.add(new Jiedanjinxingzhong());
        lists.add(new JiedaiDaipingjia());
        lists.add(new JiedanWancheng());


        orderJiedanFragmentViewpager.setOffscreenPageLimit(4);

        orderJiedanFragmentViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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


        orderJiedanFragmentViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("AllOrdersActivity", "onPageSelected: " + position);
                TextView textView;

                for (int i = 0; i < lists.size(); i++) {
                    textView = (TextView) jiedanHuadong.getChildAt(i);
                    textView.setSelected(false);
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                textView = (TextView) jiedanHuadong.getChildAt(position);
                textView.setSelected(true);
                textView.setTextColor(Color.RED);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @OnClick({R.id.order_jiedan_fragment_tab1, R.id.order_jiedan_fragment_tab2, R.id.order_jiedan_fragment_tab3, R.id.order_jiedan_fragment_tab4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_jiedan_fragment_tab1:
                orderJiedanFragmentViewpager.setCurrentItem(1);
                break;
            case R.id.order_jiedan_fragment_tab2:
                orderJiedanFragmentViewpager.setCurrentItem(1);
                break;
            case R.id.order_jiedan_fragment_tab3:
                orderJiedanFragmentViewpager.setCurrentItem(2);
                break;
            case R.id.order_jiedan_fragment_tab4:
                orderJiedanFragmentViewpager.setCurrentItem(3);
                break;



        }

    }
}
