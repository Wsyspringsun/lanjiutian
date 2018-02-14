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
import com.wyw.ljtds.model.MedicineTypeSecondModel;
import com.wyw.ljtds.ui.goods.ActivityMedicineList;

import java.util.List;

/**
 * 作者：郭传沛
 * 时间：2017/3/5:21:00
 * 邮箱：gcpzdl@mail.com
 * 说明：
 */


public class GridAdapter2 extends BaseAdapter {
    private Context mContext;
    private List<MedicineTypeSecondModel.CHILDREN> mChildrenBeanXes;

    public GridAdapter2(Context context, List<MedicineTypeSecondModel.CHILDREN> childrenBeanXes) {
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
        viewHolder.textView.setText(mChildrenBeanXes.get(position).getCLASSNAME());
        viewHolder.mImageView.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + mChildrenBeanXes.get(position).getIMG_PATH()));

        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = ActivityMedicineList.getIntent(mContext, ActivityMedicineList.ARG_MTD_GET, "", mChildrenBeanXes.get(position).getCLASSCODE(), "");
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
