package com.wyw.ljtds.adapter.commodity;

import android.net.Uri;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityListModel;
import com.wyw.ljtds.model.GoodsModel;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import java.util.Arrays;
import java.util.List;

import static com.wyw.ljtds.adapter.goodsinfo.MedicineItemAdapter1.RESIZE;

/**
 * Created by wsy on 18-4-15.
 */
public class CommodityAdapter extends BaseQuickAdapter<CommodityListModel> {

    public CommodityAdapter(List<CommodityListModel> list) {
        super(R.layout.item_goods_grid, list);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CommodityListModel commodityListModel) {
        baseViewHolder.setText(R.id.goods_title, StringUtils.deletaFirst(commodityListModel.getTitle()))
                .setText(R.id.item_goods_grid_money, "￥" + Utils.formatFee(commodityListModel.getCostMoney() + ""));

        ImageView goods_img = baseViewHolder.getView(R.id.item_goods_grid_sdv);
        if (StringUtils.isEmpty(commodityListModel.getImgPath())) {
        } else {
            Picasso.with(mContext).load(Uri.parse(AppConfig.IMAGE_PATH_LJT + commodityListModel.getImgPath())).resize(RESIZE, RESIZE).placeholder(R.mipmap.zhanweitu).into(goods_img);
        }

        baseViewHolder.setVisible(R.id.item_goods_huodong_te, false);
        baseViewHolder.setVisible(R.id.item_goods_huodong_zeng, false);
        if (GoodsModel.HUODONG_TEJIA.equals(commodityListModel.getTopFlg())) {
            baseViewHolder.setVisible(R.id.item_goods_huodong_te, true);
            baseViewHolder.setText(R.id.item_goods_grid_money_old, "￥" + Utils.formatFee(commodityListModel.getMarketPrice() + ""));
            baseViewHolder.setVisible(R.id.item_goods_grid_money_old, true);
        } else if (Arrays.asList(GoodsModel.HUODONG_MANZENG).contains(commodityListModel.getTopFlg())) {
            baseViewHolder.setVisible(R.id.item_goods_huodong_zeng, true);
        }

    }
}
