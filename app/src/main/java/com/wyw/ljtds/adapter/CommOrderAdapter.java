package com.wyw.ljtds.adapter;

import android.net.Uri;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.utils.StringUtils;

import java.util.List;

/**
 * Created by wsy on 18-1-12.
 */

public class CommOrderAdapter extends BaseQuickAdapter<OrderCommDto> {
    public CommOrderAdapter(List<OrderCommDto> lists) {
        super(R.layout.item_order_submit_goods, lists);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, OrderCommDto goods) {
        if (AppConfig.GROUP_LJT.equals(goods.getINS_USER_ID())) {
            //医药
            baseViewHolder.setText(R.id.item_order_submit_goods_size, "产地：" + goods.getCOMMODITY_COLOR() + " ;" + mContext.getString(R.string.title_cat_val, goods.getCOMMODITY_SIZE()));
        } else {
            baseViewHolder.setText(R.id.item_order_submit_goods_size, mContext.getString(R.string.title_cat_val, goods.getCOMMODITY_COLOR()) + " ;" + mContext.getString(R.string.title_size_val, goods.getCOMMODITY_SIZE()));
        }

        baseViewHolder.setText(R.id.item_order_submit_goods_title, StringUtils.deletaFirst(goods.getCOMMODITY_NAME()))
                .setText(R.id.item_order_submit_goods_money, "￥" + goods.getCOST_MONEY())
                .setText(R.id.item_order_submit_goods_number, "X" + goods.getEXCHANGE_QUANLITY());

        SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.item_order_submit_goods_pic);
        if (!StringUtils.isEmpty(goods.getIMG_PATH())) {
            simpleDraweeView.setImageURI(Uri.parse(goods.getIMG_PATH()));
        }
    }
}
