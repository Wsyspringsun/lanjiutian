package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/1/4 0004.
 */

@ContentView(R.layout.fragment_goods_info_parameter)
public class FragmentGoodsParameter extends BaseFragment {
    @ViewInject(R.id.canshuxiangqing)
    public TextView tv;

    private String str;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv.setText(str);
    }

    public void bindData2View(MedicineDetailsModel model) {
        if (model == null) return;
        str = "商品名称：" + model.getWARENAME() + "\n规格：" + model.getWARESPEC() + "\n计量单位：" +
                model.getWAREUNIT() + "\n生产厂商：" + model.getPRODUCER() + "\n批准文号：" + model.getFILENO();
//        tv.setText(str);
    }

    public void bindData2View(CommodityDetailsModel model) {
        str = "商品名称：" + model.getTitle() + "\n规格：" + model.getCommodityParameter() + "\n商品信息：" + model.getDesc();
//        tv.setText(str);
    }


}
