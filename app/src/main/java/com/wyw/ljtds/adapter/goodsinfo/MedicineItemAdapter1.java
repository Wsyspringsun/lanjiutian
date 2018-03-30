package com.wyw.ljtds.adapter.goodsinfo;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.MedicineListModel;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import java.util.List;

/**
 * Created by wsy on 18-3-23.
 */
public class MedicineItemAdapter1 extends BaseQuickAdapter<MedicineListModel> {
    public static final int RESIZE = 200;

    public MedicineItemAdapter1(Context context, List<MedicineListModel> medicineList) {
        super(R.layout.item_goods_grid, medicineList);
        this.mContext = context;
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, MedicineListModel recommendModel) {
        baseViewHolder.setText(R.id.goods_title, StringUtils.deletaFirst(recommendModel.getWARENAME()))
                .setText(R.id.money, "￥" + recommendModel.getSALEPRICE() + "");

        ImageView goods_img = baseViewHolder.getView(R.id.item_goods_grid_sdv);
        Picasso.with(mContext).load(Uri.parse(recommendModel.getIMG_PATH())).resize(RESIZE, RESIZE).into(goods_img);
        Utils.log("recommendModel.getIMG_PATH():" + recommendModel.getIMG_PATH());
    }
}
