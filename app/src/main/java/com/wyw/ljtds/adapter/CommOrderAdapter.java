package com.wyw.ljtds.adapter;

import android.net.Uri;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
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

        String title = StringUtils.deletaFirst(goods.getCOMMODITY_NAME());
        if (!"".equals(goods.getGIVEAWAY())) {
            title += "(" + goods.getGIVEAWAY() + ")";
        }

        baseViewHolder.setText(R.id.item_order_submit_goods_title, title)
                .setText(R.id.item_order_submit_goods_number, "X" + goods.getEXCHANGE_QUANLITY());
        String costPoint = goods.getCOST_POINT();
        String price = "￥" + goods.getCOST_MONEY();
        if (costPoint != null) {
            if ("0".compareTo(costPoint) < 0) {
                price += " + " + costPoint + "积分";
            }
        }
        baseViewHolder.setText(R.id.item_order_submit_goods_money, price);

        SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.item_order_submit_goods_pic);
        if (!StringUtils.isEmpty(goods.getIMG_PATH())) {
            Uri imgurl = Uri.parse(goods.getIMG_PATH());
            // 清除Fresco对这条验证码的缓存
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.evictFromMemoryCache(imgurl);
            imagePipeline.evictFromDiskCache(imgurl);
            // combines above two lines
            simpleDraweeView.setImageURI(imgurl);
        }
    }
}
