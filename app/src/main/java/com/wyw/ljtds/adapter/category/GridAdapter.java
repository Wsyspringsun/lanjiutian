package com.wyw.ljtds.adapter.category;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityTypeSecondModel;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;

import java.util.List;

/**
 * 作者：郭传沛
 * 时间：2017/3/5:21:00
 * 邮箱：gcpzdl@mail.com
 * 说明：
 */


public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private List<CommodityTypeSecondModel.Children> mChildrenBeanXes;

    public GridAdapter(Context context, List<CommodityTypeSecondModel.Children> childrenBeanXes) {
        mContext = context;
        mChildrenBeanXes = childrenBeanXes;
    }

    @Override
    public int getCount() {
        return mChildrenBeanXes.size();
    }

    @Override
    public Object getItem(int position) {
        return mChildrenBeanXes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mChildrenBeanXes.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.category_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (SimpleDraweeView) convertView.findViewById(R.id.item_head_img);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.grid_item_text);
            viewHolder.item = (LinearLayout) convertView.findViewById(R.id.item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(mChildrenBeanXes.get(position).getName());
        viewHolder.mImageView.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT_ECOMERCE + mChildrenBeanXes.get(position).getImgPath()));

        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = ActivityGoodsList.getIntent(mContext, mChildrenBeanXes.get(position).getCommodityTypeId());
                mContext.startActivity(it);
            }
        });

        return convertView;
    }

    class ViewHolder {
        SimpleDraweeView mImageView;
        TextView textView;
        LinearLayout item;
    }
}
