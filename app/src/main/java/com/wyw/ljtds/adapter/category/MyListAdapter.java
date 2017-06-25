package com.wyw.ljtds.adapter.category;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.wyw.ljtds.R;
import com.wyw.ljtds.model.CommodityTypeFirstModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 昭昭 on 2016/8/4.
 */

public class MyListAdapter extends BaseAdapter {
    private List<CommodityTypeFirstModel> data = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mInflater;
    private int curPositon;

    public void setCurPositon(int curPositon) {
        this.curPositon = curPositon;
    }

    public int getCurPositon() {
        return curPositon;
    }

    public MyListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void addItems(List<CommodityTypeFirstModel> list) {
        this.data.addAll(list);
    }

    public void removeAll() {
        if (data != null && data.size() > 0) {
            for (int i = data.size() - 1; i >= 0; i--) {
                data.remove(i);
            }
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.category_left_item,null);
            vh = new ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.left_item_btn);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        vh.tv.setText(data.get(position).getName());
        if (position == curPositon) {
            convertView.setBackgroundColor(mContext.getResources().getColor( R.color.whitesmoke ));
            vh.tv.setTextColor(mContext.getResources().getColor( R.color.base_bar ));
        } else{
            vh.tv.setTextColor(mContext.getResources().getColor( R.color.font_black2 ));
            convertView.setBackgroundColor(Color.WHITE);
        }
        return convertView;
    }
    class ViewHolder {
        TextView tv;
    }
}
