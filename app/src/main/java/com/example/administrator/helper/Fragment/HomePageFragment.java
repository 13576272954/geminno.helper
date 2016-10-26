package com.example.administrator.helper.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.administrator.helper.R;
import com.example.administrator.helper.my.Activity.MainActivity2;
import com.example.administrator.helper.my.Activity.WodeJiedan;
import com.example.administrator.helper.my.Activity.Wodefadan;
import com.example.administrator.helper.my.Activity.Wodeyuer;
import com.example.administrator.helper.my.Activity.shezhi;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * Created by bin on 2016/9/19.
 */
public class HomePageFragment extends Fragment {
    public static final int REQUECT = 1;
    @InjectView(R.id.imageView3)
    ImageView imageView3;
    @InjectView(R.id.tv_imageButton_geren)
    ImageButton tvImageButtonGeren;
    @InjectView(R.id.textView2)
    TextView textView2;
    @InjectView(R.id.relatlayout)
    RelativeLayout relatlayout;
    @InjectView(R.id.tv_shenqingpaotui)
    TextView tvShenqingpaotui;
    @InjectView(R.id.tv_wodeyuer)
    TextView tvWodeyuer;
    @InjectView(R.id.tv_wodefadan)
    TextView tvWodefadan;
    @InjectView(R.id.tv_wodejiedan)
    TextView tvWodejiedan;
    @InjectView(R.id.tv_yijianfankui)
    TextView tvYijianfankui;
    @InjectView(R.id.v_changjianwenti)
    TextView vChangjianwenti;
    @InjectView(R.id.tv_wodejifen)
    TextView tvWodejifen;
    @InjectView(R.id.tv_shezhi)
    TextView tvShezhi;
    @InjectView(R.id.scrollView)
    ScrollView scrollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_zongbuju, null);

        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.tv_imageButton_geren, R.id.tv_shenqingpaotui, R.id.tv_wodeyuer, R.id.tv_wodefadan, R.id.tv_wodejiedan, R.id.tv_yijianfankui, R.id.v_changjianwenti, R.id.tv_wodejifen, R.id.tv_shezhi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_imageButton_geren:
                Intent intent =new Intent(getActivity(), MainActivity2.class);
                getActivity().startActivityForResult(intent,REQUECT);
                Log.i("HomePageFragment", "onClick: 000");

                break;
            case R.id.tv_shenqingpaotui:
                break;
            case R.id.tv_wodeyuer:
                Intent intent2=new Intent(getActivity(), Wodeyuer.class);
                getActivity().startActivityForResult(intent2,REQUECT);
                Log.i("HomePageFragment", "onClick: 1111");
                break;
            case R.id.tv_wodefadan:
                Intent intent3=new Intent(getActivity(), Wodefadan.class);
                getActivity().startActivityForResult(intent3,REQUECT);
                Log.i("HomePageFragment", "onClick: 222");
                break;
            case R.id.tv_wodejiedan:
                Intent intent4=new Intent(getActivity(), WodeJiedan.class);
                getActivity().startActivityForResult(intent4,REQUECT);
                Log.i("HomePageFragment", "onClick: 3333");
                break;
            case R.id.tv_yijianfankui:
                break;
            case R.id.v_changjianwenti:
                break;
            case R.id.tv_wodejifen:
                break;
            case R.id.tv_shezhi:
                Intent intent5=new Intent(getActivity(),shezhi.class);
                getActivity().startActivityForResult(intent5,REQUECT);
                Log.i("HomePageFragment", "onClick: 4444");
                break;
        }
    }
}