package com.example.administrator.helper.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.helper.R;
import com.example.administrator.helper.share.ReleaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by bin on 2016/9/19.
 */
public class SharePageFragment extends Fragment {


    @InjectView(R.id.im_fanhi)
    ImageView imFanhi;
    @InjectView(R.id.im_tianjia)
    ImageView imTianjia;
    @InjectView(R.id.listView)
    ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_share_page, null);


        ButterKnife.inject(this, v);
        return v;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.im_tianjia)
    public void onClick() {
        Intent intent =new Intent(getActivity(), ReleaseActivity.class);
        startActivity(intent);
    }
}
