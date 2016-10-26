package com.example.administrator.helper.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.helper.R;

import java.util.List;


public class MenuListAdapter extends BaseAdapter {

//    Context context;
//    List<T> lists;
//    int layoutId;
//    public CommonAdapter(Context context, List<T> lists, int layoutId){
//
//        this.context=context;
//        this.lists=lists;
//        this.layoutId=layoutId;
//
//    }
//
//
//    @Override
//    public int getCount() {
//        return (lists!=null)?lists.size():0;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return lists.get(position);//每个item的数据源
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    //找到控件，赋值
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        ViewHolder viewHolder=ViewHolder.get(context,layoutId,convertView,parent);
//        convert(viewHolder,lists.get(position),position);
//        return viewHolder.getConvertView();
//    }
//
//    //取出控件，赋值
//    public abstract void  convert(ViewHolder viewHolder,T t,int position);
//}

    private Context context;

    private ViewHolder viewHolder;

    private List<String> list;

    public MenuListAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.act_item_popu_list, null);
        }

        viewHolder.text1 = (TextView) convertView.findViewById(R.id.textname);
        viewHolder.text1.setText(list.get(position));

        return convertView;
    }

    private class ViewHolder {
        private TextView text1;
    }
}
