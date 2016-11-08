package com.example.eli.a24dian.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.eli.a24dian.R;

import java.util.List;

/**
 * Created by Eli on 2016/10/27.
 */

public class CalculateAdapter extends BaseAdapter {

    private List<String> list;
    private LayoutInflater layoutInflater;
    private Context mContext;
    Typeface face;
    public CalculateAdapter(Context context, List<String> list) {
        this.list = list;
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        face= Typeface.createFromAsset(mContext.getAssets(), "fonts/Skranji_Bold.ttf");
    }

    /**
     * 数据总数
     */
    @Override
    public int getCount() {

        return list.size();
    }

    /**
     * 获取当前数据
     */
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

        View view = null;
        if (layoutInflater != null) {
            view = layoutInflater
                    .inflate(R.layout.item_calculate, null);
            TextView textView = (TextView) view.findViewById(R.id.txt_item);
            textView.setTypeface(face);
            //获取自定义的类实例
            textView.setText(list.get(position));
        }
        return view;
    }

}