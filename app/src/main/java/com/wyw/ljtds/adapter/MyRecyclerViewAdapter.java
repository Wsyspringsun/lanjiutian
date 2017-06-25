package com.wyw.ljtds.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016-10-05.
 */
public abstract class MyRecyclerViewAdapter<T> extends RecyclerView.Adapter<MyRecyclerViewHolder>{
    protected Context mContext;
    protected List<T> mDatas;
    protected int mItemLayoutId;
    protected LayoutInflater mInflater;

    public MyRecyclerViewAdapter(Context context, List<T> mDatas, int itemLayoutId) {
        this.mContext = context;
        this.mDatas = mDatas;
        this.mItemLayoutId = itemLayoutId;
        this.mInflater=LayoutInflater.from(context);
    }

    public void removeAll() {
        if (mDatas != null && mDatas.size() > 0) {
            for (int i = mDatas.size() - 1; i >= 0; i--) {
                mDatas.remove(i);
            }
        }
    }

    public void updateData(List<T> data) {
        mDatas.clear();
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        mDatas.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public MyRecyclerViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        MyRecyclerViewHolder holder = MyRecyclerViewHolder.get(mContext,parent,mItemLayoutId);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyRecyclerViewHolder holder,int position) {
        convert(holder, mDatas.get(position));
    }

    public abstract void convert(MyRecyclerViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}
