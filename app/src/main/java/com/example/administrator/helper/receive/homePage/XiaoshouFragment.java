package com.example.administrator.helper.receive.homePage;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.helper.BaseFragment;
import com.example.administrator.helper.MyApplication;
import com.example.administrator.helper.R;
import com.example.administrator.helper.entity.Task;
import com.example.administrator.helper.receive.TaskDetilsActivity;
import com.example.administrator.helper.utils.CommonAdapter;
import com.example.administrator.helper.utils.ImageLoader;
import com.example.administrator.helper.utils.RefreshListView;
import com.example.administrator.helper.utils.UrlUtils;
import com.example.administrator.helper.utils.ViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by bin on 2016/10/17.
 */

public class XiaoshouFragment extends BaseFragment implements  RefreshListView.OnRefreshUploadChangeListener{
//    @InjectView(R.id.search_clear)
//    ImageButton searchClear;
//    @InjectView(R.id.tv_search)
//    TextView tvSearch;


    android.os.Handler handler2 = new android.os.Handler();

    Handler handler = new Handler() {
        @Override
        public void close() {
        }

        @Override
        public void flush() {
        }

        @Override
        public void publish(LogRecord record) {
        }
    };

    Boolean flag1 = false, flag2 = true;

    //商品名称
    String taskDemand = "null";
    int tasktypeid = 5;
    int pageNo = 1;
    int pageSize = 5;
    String city;
    String url2;
    ImageLoader myImageLoader;
    CommonAdapter<Task> goodsAdapter;
    List<String> popContents = new ArrayList<String>();
    List<Task> tasks = new ArrayList<Task>();//存放商品信息

    @InjectView(R.id.lv_goods)
    RefreshListView lvGoods;
//    @InjectView(R.id.query)
//    EditText query;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.xuexi_fragment,null);
        Log.i("XiaoshouFragment", "onCreateView: "+((MyApplication)getActivity().getApplication()).getCity());
        city=((MyApplication)getActivity().getApplication()).getCity();
        Log.i("XiaoshouFragment", "onCreateView: "+city);
        ButterKnife.inject(this, v);
        return v;



    }


    public void initView() {
    }

    public void initEvent() {
        lvGoods.setOnRefreshUploadChangeListener(this);
        //lvGoods的item点击事件
        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("agssdfv", "" + lvGoods.isFlag());

                if (!lvGoods.isFlag()) {
                    //跳转到商品详情页面
                    Intent intent = new Intent(getActivity(), TaskDetilsActivity.class);
                    //点击item的商品信息
                    intent.putExtra("productInfo", (Parcelable) tasks.get(position - 1));
                    startActivityForResult(intent, 123);

                }
            }
        });

//        //设置query的文本改变事件
//        query.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // TODO Auto-generated method stub
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // TODO Auto-generated method stub
//                //输入文本后，imagebutton显示；清空，则不显示
//                if (s.length() > 0) {
//                    //输入框内容：
//                    searchClear.setVisibility(View.VISIBLE);//设置显示
//                } else {
//                    //没有输入内容，不显示按钮
//                    searchClear.setVisibility(View.INVISIBLE);//设置不显示
//                }
//            }
//        });
//        //清除按钮点击事件：清空输入框内容，隐藏软键盘,并重新获取数据
//        searchClear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                taskDemand = "";
//                getData();
//                query.getText().clear();
//            }
//        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.i("setUserVisibleHint", "setUserVisibleHint: "+isVisibleToUser);
        if (isVisibleToUser){
            getData();
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    public void initData() {
        getData();//获取网络数据，显示在listview上
    }

    //获取网络数据
    public void getData() {
        //界面初始化数据：listview显示数据
        //xutils获取网络数据
        // goodsAdapter = null;
        String url = UrlUtils.MYURL + "ReceiveServlet";//访问网络的url
        RequestParams requestParams = new RequestParams(url);
        Log.i("XiaoshouFragment", "onCreateView: "+city);
        requestParams.addQueryStringParameter("city",city);
        requestParams.addQueryStringParameter("taskDemand",taskDemand);
        requestParams.addQueryStringParameter("tasktypeid", tasktypeid + "");//排序标记
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");

        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //  Log.i("MainpageFragment", "onSuccess: " + result);
                //json转换成List<Product>
                Gson gson = new Gson();
                Type type = new TypeToken<List<Task>>() {
                }.getType();
                List<Task> newTasks = new ArrayList<Task>();
                newTasks = gson.fromJson(result, type);//解析成List<Product>
                tasks.clear();//清空原来的数据
                tasks.addAll(newTasks);
                //设置listview的apter
                if (goodsAdapter == null) {
                    goodsAdapter = new CommonAdapter<Task>(getActivity(), tasks, R.layout.prod_list_item) {
                        @Override
                        public void convert(ViewHolder viewHolder, Task task, int position) {
                            //取出控件，赋值
                            TextView tv = viewHolder.getViewById(R.id.prod_list_item_tv);
                            tv.setText("发布时间："+ task.getBeginTime());//商品名称
                            TextView tvPrice = viewHolder.getViewById(R.id.prod_list_item_tv2);
                            tvPrice.setText("￥" + task.getMoney());
                            TextView a = viewHolder.getViewById(R.id.prod_list_item_tv3);
                            a.setText("地点：" + task.getMakePlace());
                            //其他控件赋值

                            ImageView imageView = viewHolder.getViewById(R.id.prod_list_item_iv);
                            url2 = task.getSendUser().getImage();
                            myImageLoader = new ImageLoader(getActivity());
                            myImageLoader.showImageByUrl(url2, imageView);
                        }
                    };
                    lvGoods.setAdapter(goodsAdapter);
                    lvGoods.setOnRefreshUploadChangeListener((RefreshListView.OnRefreshUploadChangeListener)getActivity());
                } else {
                    goodsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
    }


//    @OnClick({R.id.tv_search, R.id.search_clear})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.tv_search:
//                //搜索：按姓名搜索(去掉前后的空格)
//                taskDemand = query.getText().toString().trim();
//                getData();
//                break;
//            case R.id.search_clear:
//                query.setText("");
//                break;
//        }
//    }


    @Override
    public void onRefresh() {
        Log.d("ssdfgvc", "111111");
        pageNo = 1;
        getData();

        handler2.postDelayed(new Runnable() {
            public void run() {
                lvGoods.completeRefresh();
            }
        }, 1000);


    }

    @Override
    public void onPull() {

        pageNo++;
        lvGoods.changeFootState();

        String url = UrlUtils.MYURL+ "ReceiveServlet";//访问网络的url
        RequestParams requestParams = new RequestParams(url);
        requestParams.addQueryStringParameter("city",city);
        requestParams.addQueryStringParameter("taskDemand",  taskDemand);
        requestParams.addQueryStringParameter("tasktypeid",tasktypeid + "");//排序标记
        requestParams.addQueryStringParameter("pageNo", pageNo + "");
        requestParams.addQueryStringParameter("pageSize", pageSize + "");
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //  Log.i("MainpageFragment", "onSuccess: " + result);
                //json转换成List<Product>
                Gson gson = new Gson();
                Type type = new TypeToken<List<Task>>() {
                }.getType();
                List<Task> newTasks = new ArrayList<Task>();
                newTasks = gson.fromJson(result, type);//解析成List<Product>


                if (newTasks.size() == 0) {
                    Log.d("pageNo", "newProducts.size()" + newTasks.size());
                    Toast.makeText(getActivity(), "没有数据", Toast.LENGTH_SHORT).show();
                    pageNo--;
                    return;
                }
                tasks.addAll(newTasks);
                //设置listview的apter
                if (goodsAdapter == null) {

                    goodsAdapter = new CommonAdapter<Task>(getActivity(), tasks, R.layout.prod_list_item) {
                        @Override
                        public void convert(ViewHolder viewHolder, Task task, int position) {
                            //取出控件，赋值
                            TextView tv = viewHolder.getViewById(R.id.prod_list_item_tv);
                            tv.setText("地点：" + task.getMakePlace());//商品名称
                            TextView tvPrice = viewHolder.getViewById(R.id.prod_list_item_tv2);
                            tvPrice.setText("￥" + task.getMoney());
                            TextView a = viewHolder.getViewById(R.id.prod_list_item_tv3);
                            a.setText( task.getBeginTime().toString());
                            //其他控件赋值

                        }
                    };

                    lvGoods.setAdapter(goodsAdapter);
                    lvGoods.setOnRefreshUploadChangeListener((RefreshListView.OnRefreshUploadChangeListener)getActivity());
                } else {
                    goodsAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            }
        });
        // handler

        handler2.postDelayed(new Runnable() {
            public void run() {
                lvGoods.completeLoad();
            }
        }, 1000);
    }
}
