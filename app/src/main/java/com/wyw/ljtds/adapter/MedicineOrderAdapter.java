package com.wyw.ljtds.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wyw.ljtds.R;
import com.wyw.ljtds.model.MedicineOrder;
import com.wyw.ljtds.ui.user.order.MedicineOrderHolder;

import java.util.List;

/**
 * Created by wsy on 18-3-12.
 */
public class MedicineOrderAdapter extends RecyclerView.Adapter<MedicineOrderHolder> {
    private List<MedicineOrder> data;
    private final Context context;

    public MedicineOrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MedicineOrderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_orderinfor_goods, parent, false);
        return new MedicineOrderHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MedicineOrderHolder holder, int position) {
        if (data == null || data.isEmpty()) return;
        MedicineOrder itemData = data.get(position);
        holder.data = itemData;
        holder.bindData2View();
    }

    @Override
    public int getItemCount() {
        if (data == null || data.isEmpty()) return 0;
        return data.size();
    }

    public List<MedicineOrder> getData() {
        return data;
    }

    public void setData(List<MedicineOrder> data) {
        this.data = data;
    }
}
