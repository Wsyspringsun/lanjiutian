package com.wyw.ljtds.adapter;

import android.net.Uri;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
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
        if (StringUtils.isEmpty(goods.getCOMMODITY_COLOR())) {
            baseViewHolder.setText(R.id.item_order_submit_goods_size, " 规格：" + goods.getCOMMODITY_SIZE());
        } else {
            baseViewHolder.setText(R.id.item_order_submit_goods_size, "产地：" + goods.getCOMMODITY_COLOR() + " ;规格：" + goods.getCOMMODITY_SIZE());
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
