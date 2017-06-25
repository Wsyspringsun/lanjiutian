package com.wyw.ljtds.adapter.category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.model.CommodityTypeSecondModel;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：郭传沛
 * 时间：2017/3/5:19:42
 * 邮箱：gcpzdl@mail.com
 * 说明：
 */


public class Recycler_rightAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    List<CommodityTypeSecondModel> mChildrenBeanXes;

    public Recycler_rightAdapter(Context context, List<CommodityTypeSecondModel> childrenBeanXes) {
        mContext = context;
        mChildrenBeanXes = childrenBeanXes;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder( LayoutInflater.from( mContext ).inflate( R.layout.category_right_item, parent, false ) );
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.mTextView.setText( mChildrenBeanXes.get( position ).getName() );

        GridAdapter gridAdapter = new GridAdapter( mContext, mChildrenBeanXes.get( position ).getChildren() );
        myViewHolder.mGridview.setAdapter( gridAdapter );

    }

    @Override
    public int getItemCount() {
        return mChildrenBeanXes.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {


        private final TextView mTextView;
        private final MyGridView mGridview;

        public MyViewHolder(View itemView) {
            super( itemView );
            mGridview = (MyGridView) itemView.findViewById( R.id.right_item_grid );
            mTextView = (TextView) itemView.findViewById( R.id.right_item_text );
        }
    }

}
