package com.wyw.ljtds.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.imagepipeline.cache.NoOpImageCacheStatsTracker;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;

import java.util.List;

/**
 * Created by wsy on 17-12-15.
 * wsy
 * 有空数据视图的适配器
 */

public abstract class DataListAdapter<D, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    private final int TYPE_EMPTY = 1;
    private Context context;
    private int layout;
    private List<D> list;

    public DataListAdapter(Context context, @LayoutRes int layout, List<D> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (list == null || list.size() <= 0) {
            return TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = null;
        switch (viewType) {
            case TYPE_EMPTY:
                v = inflater.inflate(R.layout.main_empty_view, parent, false);
                return new EmptyViewHolder(v);
        }
        v = inflater.inflate(layout, parent, false);
        return getViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindData2View(holder, position);
    }

    protected abstract V getViewHolder(View v);

    protected abstract void bindData2View(RecyclerView.ViewHolder holder, int position);

    @Override
    public int getItemCount() {
        if (list == null || list.size() <= 0) {
            return 1;
        }
        return list.size();
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}

