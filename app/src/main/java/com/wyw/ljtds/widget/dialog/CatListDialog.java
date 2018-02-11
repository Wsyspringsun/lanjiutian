package com.wyw.ljtds.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.DataListAdapter;
import com.wyw.ljtds.biz.biz.CategoryBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.MedicineShop;
import com.wyw.ljtds.model.MedicineTypeFirstModel;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.MyCallback;

import java.util.List;

/**
 * Created by wsy on 17-12-26.
 */

public class CatListDialog extends Dialog {
    MyCallback callback;

    public void setCallback(MyCallback callback) {
        this.callback = callback;
    }

    private final MedicineTypeAdapter ryvDataAdapter;
    GridLayoutManager glm;
    private Context context;
    private final RecyclerView ryvData;

    public CatListDialog(@NonNull final Context context, int x, int y, @StyleRes int themeResId) {
        super(context, themeResId);

        this.context = context;
        View vCatList = LayoutInflater.from(context).inflate(R.layout.fragment_medicine_type_list, null);

        ryvDataAdapter = new MedicineTypeAdapter(context, R.layout.item_addr_medicine_shop);
        glm = new GridLayoutManager(context, 2);
        ryvData = (RecyclerView) vCatList.findViewById(R.id.fragment_medicine_type_list_ryv_data);
        ryvData.setLayoutManager(glm);
        ryvData.setAdapter(ryvDataAdapter);

        this.setContentView(vCatList);
        this.setCancelable(true);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.TOP | Gravity.RIGHT);
        //显示的坐标
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //内容 透明度
        //        lp.alpha = 0.2f;
        lp.width = MyApplication.screenWidth / 2 + 100;
        lp.height = MyApplication.screenHeight / 2 + 150;
        lp.y = y + 10;
        lp.x = -(x + 5);
        //遮罩 透明度
//        lp.dimAmount = 0.2f;
        dialogWindow.setAttributes(lp);
        loadData();
    }


    void loadData() {
        new BizDataAsyncTask<List<MedicineTypeFirstModel>>() {
            @Override
            protected List<MedicineTypeFirstModel> doExecute() throws ZYException, BizFailure {
                return CategoryBiz.getMedicineType();
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineTypeFirstModel> medicineTypeFirstModels) {
                bindData2View(medicineTypeFirstModels);
            }

            @Override
            protected void OnExecuteFailed() {

            }
        }.execute();
    }


    public void bindData2View(List<MedicineTypeFirstModel> shopList) {
        if (ryvDataAdapter == null) return;
        MedicineTypeFirstModel all = new MedicineTypeFirstModel();
        all.setCLASSCODE("");
        all.setCLASSNAME("全部");
        ryvDataAdapter.list.add(all);
        ryvDataAdapter.list.addAll(shopList);
        ryvDataAdapter.notifyDataSetChanged();
    }

    class MedicineTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        MedicineTypeFirstModel itemData;
        public CheckedTextView tvName;

        public MedicineTypeViewHolder(View itemView) {
            super(itemView);
            tvName = (CheckedTextView) itemView.findViewById(R.id.item_addr_medicine_shop_tv_name);
            tvName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (callback != null) {
                callback.callback(itemData);
            }
        }
    }

    class MedicineTypeAdapter extends DataListAdapter<MedicineTypeFirstModel, MedicineTypeViewHolder> {
        private final Context context;
        private int layout;
        public MedicineShop selectedItem;

        public MedicineTypeAdapter(Context context, @LayoutRes int layout) {
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
            return new MedicineTypeViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MedicineTypeViewHolder) {
                if (list == null || list.size() <= 0) return;
                MedicineTypeFirstModel itemData = list.get(position);
                MedicineTypeViewHolder h = (MedicineTypeViewHolder) holder;
                h.itemData = itemData;
                h.tvName.setText(itemData.getCLASSNAME());
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
