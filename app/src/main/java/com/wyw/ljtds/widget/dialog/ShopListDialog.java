package com.wyw.ljtds.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mylhyl.circledialog.params.DialogParams;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.DataListAdapter;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.MedicineShop;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.MyCallback;

import java.util.List;

/**
 * Created by wsy on 17-12-26.
 */

public class ShopListDialog extends Dialog {
    MyCallback callback;

    public void setCallback(MyCallback callback) {
        this.callback = callback;
    }

    private final MedicineShopAdapter ryvDataAdapter;
    GridLayoutManager glm;
    private Context context;
    private final ImageView btnQuxiao;
    private final RecyclerView ryvData;
    private final Button btnConfirm;

    public ShopListDialog(@NonNull final Context context, @StyleRes int themeResId) {
        super(context, themeResId);

        this.context = context;
        View vShopList = LayoutInflater.from(context).inflate(R.layout.fragment_addr_medicine_shop_list, null);

        ryvDataAdapter = new MedicineShopAdapter(context, R.layout.item_addr_medicine_shop);
        glm = new GridLayoutManager(context, 2);
        ryvData = (RecyclerView) vShopList.findViewById(R.id.fragment_addr_medicine_shop_list_ryv_data);
        ryvData.setLayoutManager(glm);
        ryvData.setAdapter(ryvDataAdapter);

        btnConfirm = (Button) vShopList.findViewById(R.id.fragment_addr_medicine_shop_list_btn_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    if (ryvDataAdapter.selectedItem != null)
                        callback.callback(v, ryvDataAdapter.selectedItem);
                    else
                        ToastUtil.show(context, "提示：必须选择一个店铺!");
                }
            }
        });
        btnQuxiao = (ImageView) vShopList.findViewById(R.id.fragment_addr_medicine_shop_list_btn_quxiao);
        btnQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopListDialog.this.dismiss();
            }
        });

        this.setContentView(vShopList);
        this.setCancelable(true);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //显示的坐标
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //内容 透明度
        //        lp.alpha = 0.2f;
        lp.width = MyApplication.screenWidth;
        lp.height = MyApplication.screenHeight / 2;
        //遮罩 透明度
//        lp.dimAmount = 0.2f;
        dialogWindow.setAttributes(lp);
    }

    public void bindData2View(List<MedicineShop> shopList) {
        if (ryvDataAdapter == null) return;
        ryvDataAdapter.list = shopList;
        ryvDataAdapter.notifyDataSetChanged();
    }

    class MedicineShopViewHolder extends RecyclerView.ViewHolder {
        public CheckedTextView tvName;

        public MedicineShopViewHolder(View itemView) {
            super(itemView);
            tvName = (CheckedTextView) itemView.findViewById(R.id.item_addr_medicine_shop_tv_name);
        }
    }

    class MedicineShopAdapter extends DataListAdapter<MedicineShop, MedicineShopViewHolder> {
        private final Context context;
        private int layout;
        public MedicineShop selectedItem;

        public MedicineShopAdapter(Context context, @LayoutRes int layout) {
            this.context = context;
            this.layout = layout;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = null;
            if (viewType == TYPE_EMPTY) {
                v = inflater.inflate(R.layout.main_empty_view, parent, false);
                return new EmptyViewHolder(v);
            }
            v = inflater.inflate(layout, parent, false);
            return new MedicineShopViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (list == null || list.size() <= 0) return;
            if (holder instanceof MedicineShopViewHolder) {
                final MedicineShop itemData = list.get(position);

                MedicineShopViewHolder mvh = (MedicineShopViewHolder) holder;
                mvh.tvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selectedItem != null) {
                            selectedItem.isChecked = false;
                        }
                        itemData.isChecked = true;
                        selectedItem = itemData;
                        MedicineShopAdapter.this.notifyDataSetChanged();
//                        ((CheckedTextView) v).setChecked(true);
                    }
                });
                mvh.tvName.setText(itemData.getLOGISTICS_COMPANY());
                mvh.tvName.setChecked(itemData.isChecked);
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int type = getItemViewType(position);
                        switch (type) {
                            case TYPE_EMPTY:
                                return gridManager.getSpanCount();
                        }
                        return 1;
                    }
                });
            }
        }
    }
}
