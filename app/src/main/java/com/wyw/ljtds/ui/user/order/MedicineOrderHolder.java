package com.wyw.ljtds.ui.user.order;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.MedicineOrder;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

/**
 * Created by wsy on 18-3-12.
 */
public class MedicineOrderHolder extends RecyclerView.ViewHolder {
    private final TextView tvSize;
    private final TextView tvTitle;
    private final TextView tvMoney;
    private final TextView tvNumber;
    private final SimpleDraweeView sdvGoods;
    public MedicineOrder data;

    public MedicineOrderHolder(View itemView) {
        super(itemView);
        tvSize = (TextView) itemView.findViewById(R.id.size);
        tvTitle = (TextView) itemView.findViewById(R.id.title);
        tvMoney = (TextView) itemView.findViewById(R.id.money);
        tvNumber = (TextView) itemView.findViewById(R.id.number);
        sdvGoods = (SimpleDraweeView) itemView.findViewById(R.id.item_orderinfo_goods_pic);
    }

    public void bindData2View() {
        if (data == null) return;

        if (StringUtils.isEmpty(data.getCommodityColor())) {
            tvSize.setText(" 规格：" + data.getCommoditySize());
        } else {
            tvSize.setText("产地：" + data.getCommodityColor() + " ;规格：" + data.getCommoditySize());
        }
        tvTitle.setText(StringUtils.deletaFirst(data.getCommodityName()));
        tvMoney.setText("￥" + Utils.formatFee("" + data.getCostMoney()));
        tvNumber.setText("X" + data.getExchangeQuanlity());

        if (!StringUtils.isEmpty(data.getImgPath())) {
            sdvGoods.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + data.getImgPath()));
        }
    }
}
