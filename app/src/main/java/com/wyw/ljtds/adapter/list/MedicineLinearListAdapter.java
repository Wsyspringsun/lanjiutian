package com.wyw.ljtds.adapter.list;

import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.model.MedicineListModel;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import java.util.List;

/**
 * Created by wsy on 2018/9/7.
 */

public class MedicineLinearListAdapter extends BaseQuickAdapter<MedicineListModel> {
    List<MedicineListModel> list;
    Context context;

    public MedicineLinearListAdapter(Context context, List<MedicineListModel> list) {
        super(R.layout.item_shopgoods, list);
        this.context = context;
        this.list = list;
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, MedicineListModel medicineListModel) {
        String wareName = StringUtils.deletaFirst(medicineListModel.getWARENAME()),
                brand = medicineListModel.getCOMMODITY_BRAND(),
                detailFlg = StringUtils.isEmpty(medicineListModel.getCOMMODITY_PARAMETER()) ? "" : "[" + medicineListModel.getCOMMODITY_PARAMETER() + "]",
                size = medicineListModel.getWARESPEC(),
                price = "￥" + medicineListModel.getSALEPRICE() + "",
                costPoint = "+" + Utils.formatFee(medicineListModel.getCOST_POINT()) + "积分";
        StringBuilder sbName = new StringBuilder().append(detailFlg).append(brand + " ").append(wareName).append(size);
        baseViewHolder.setText(R.id.item_shopgoods_tv_title, sbName);

        baseViewHolder.setText(R.id.item_shopgoods_tv_money, price + costPoint);

        ImageView goodsImg = baseViewHolder.getView(R.id.item_shopgoods_iv_thumb);
        if (!StringUtils.isEmpty(medicineListModel.getIMG_PATH())) {
            Picasso.with(context).load(Uri.parse(medicineListModel.getIMG_PATH())).placeholder(R.mipmap.zhanweitu).into(goodsImg);
        }


    }
}
