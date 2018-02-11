package com.wyw.ljtds.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

/**
 * Created by wsy on 17-12-15.
 * wsy
 * 有空数据视图的适配器
 */

public abstract class DataListAdapter<D, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    protected final int TYPE_EMPTY = 1;
    public List<D> list;

    @Override
    public int getItemViewType(int position) {
        if (list == null || list.size() <= 0) {
            return TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (list == null || list.size() <= 0) {
            return 1;
        }
        return list.size();
    }

    protected class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}

