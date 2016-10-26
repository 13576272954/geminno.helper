package com.example.administrator.helper.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.helper.R;
import com.example.administrator.helper.send.SendBorrowActivity;
import com.example.administrator.helper.send.SendJobActivity;
import com.example.administrator.helper.send.SendLifeActivity;
import com.example.administrator.helper.send.SendOtherActivity;
import com.example.administrator.helper.send.SendSellActivity;
import com.example.administrator.helper.send.SendStudyActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by bin on 2016/9/19.
 */
public class SendPageFragment extends Fragment {
    public static final int REQUECT = 1;
    @InjectView(R.id.but_xuexi)
    Button butXuexi;
    @InjectView(R.id.but_shenghuo)
    Button butShenghuo;
    @InjectView(R.id.but_jieyong)
    Button butJieyong;
    @InjectView(R.id.but_xiaoshou)
    Button butXiaoshou;
    @InjectView(R.id.but_jianzhi)
    Button butJianzhi;
    @InjectView(R.id.but_qita)
    Button butQita;
    @InjectView(R.id.vp)
    ViewPager mViewPaper;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.dot_0)
    View dot0;
    @InjectView(R.id.dot_1)
    View dot1;
    @InjectView(R.id.dot_2)
    View dot2;
    @InjectView(R.id.frameLayout)
    FrameLayout frameLayout;

    private List<ImageView> images;
    private List<View> dots;
    private int currentItem;
    //记录上一次点的位置
    private int oldPosition = 0;
    //存放图片的id
    private int[] imageIds = new int[]{
            R.mipmap.send_a,
            R.mipmap.send_b,
            R.mipmap.send_c,
    };

    private ViewPagerAdapter adapter;
    private ScheduledExecutorService scheduledExecutorService;
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_send_page, null);
        ButterKnife.inject(this, v);
        getPicture();
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.but_xuexi, R.id.but_shenghuo, R.id.but_jieyong, R.id.but_xiaoshou, R.id.but_jianzhi, R.id.but_qita})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_xuexi:
                Intent intent1 = new Intent(getActivity(), SendStudyActivity.class);
                getActivity().startActivityForResult(intent1, REQUECT);
                break;
            case R.id.but_shenghuo:
                Intent intent2 = new Intent(getActivity(), SendLifeActivity.class);
                getActivity().startActivityForResult(intent2, REQUECT);
                break;
            case R.id.but_jieyong:
                Intent intent3 = new Intent(getActivity(), SendBorrowActivity.class);
                getActivity().startActivityForResult(intent3, REQUECT);
                break;
            case R.id.but_xiaoshou:
                Intent intent4 = new Intent(getActivity(), SendSellActivity.class);
                getActivity().startActivityForResult(intent4, REQUECT);
                break;
            case R.id.but_jianzhi:
                Intent intent5 = new Intent(getActivity(), SendJobActivity.class);
                getActivity().startActivityForResult(intent5, REQUECT);
                break;
            case R.id.but_qita:
                Intent intent6 = new Intent(getActivity(), SendOtherActivity.class);
                getActivity().startActivityForResult(intent6, REQUECT);
                break;
        }
    }

    public void getPicture() {
        //显示的图片
        images = new ArrayList<ImageView>();
        for (int i = 0; i < imageIds.length; i++) {
            ImageView imageView = new ImageView(v.getContext());
            imageView.setBackgroundResource(imageIds[i]);
            images.add(imageView);
        }
        //显示的小点
        dots = new ArrayList<View>();
        dots.add(dot0);
        dots.add(dot1);
        dots.add(dot2);
        adapter = new ViewPagerAdapter();
        Log.i("RecommendFragment", "getPicture:  "+adapter);
        mViewPaper.setAdapter(adapter);
        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                dots.get(position).setBackgroundResource(R.color.colorAccent);
                dots.get(oldPosition).setBackgroundResource(R.color.black);

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 自定义Adapter
     */
    private class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            // TODO Auto-generated method stub

            view.removeView(images.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            // TODO Auto-generated method stub
            view.addView(images.get(position));
            return images.get(position);
        }

    }

    /**
     * 利用线程池定时执行动画轮播
     */
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(
                new ViewPageTask(),
                2,
                2,
                TimeUnit.SECONDS);
    }

    /**
     * 图片轮播任务
     */
    private class ViewPageTask implements Runnable {

        @Override
        public void run() {
            currentItem = (currentItem + 1) % imageIds.length;
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 接收子线程传递过来的数据
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            mViewPaper.setCurrentItem(currentItem);
        }

    };

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }
}
