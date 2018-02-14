package com.wyw.ljtds.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.IntegerRes;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.flexbox.FlexboxLayout;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.DataListAdapter;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.MedicineShop;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;
import com.wyw.ljtds.widget.NumberButton;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.Set;

/**
 * Created by wsy on 17-12-26.
 */

public class LifeGoodsSelectDialog extends Dialog {
    MyCallback callback;

    public void setCallback(MyCallback callback) {
        this.callback = callback;
    }

    public CommodityDetailsModel.ColorList seledColor = null;
    public CommodityDetailsModel.SizeList seledSize = null;

    private Context context;
    private final ImageView btnQuxiao;
    private final Button btnConfirm;
    SimpleDraweeView imgGoods;
    TextView tvMoney;
    TextView tvStore;
    public TextView tvSeled;
    NumberButton nbGoods;
    RadioGroup rgColorSel;
    RadioGroup rgSizeSel;
    FlexboxLayout rgSizeSelFlxLayout;
    FlexboxLayout rgColorSelFlxLayout;

    public LifeGoodsSelectDialog(@NonNull final Context context, @StyleRes int themeResId) {
        super(context, themeResId);

        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_lifegoods_sel, null);

        btnConfirm = (Button) view.findViewById(R.id.dialog_lifegoods_sel_btn_submit);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    if (seledColor == null || seledSize == null) {
                        ToastUtil.show(context, tvSeled.getText().toString());
                        return;
                    }
                    callback.callback(seledColor, seledSize, nbGoods.getNumber());
                }
            }
        });
        ;
        btnQuxiao = (ImageView) view.findViewById(R.id.dialog_lifegoods_sel_cancel);
        btnQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LifeGoodsSelectDialog.this.dismiss();
            }
        });

        imgGoods = (SimpleDraweeView) view.findViewById(R.id.dialog_lifegoods_sel_img);
        tvMoney = (TextView) view.findViewById(R.id.dialog_lifegoods_sel_money);
        tvStore = (TextView) view.findViewById(R.id.dialog_lifegoods_sel_store);
        tvSeled = (TextView) view.findViewById(R.id.dialog_lifegoods_sel_tv_seled);
        nbGoods = (NumberButton) view.findViewById(R.id.dialog_lifegoods_sel_num);
        rgColorSel = (RadioGroup) view.findViewById(R.id.dialog_lifegoods_sel_tfl_color);
        rgColorSelFlxLayout = (FlexboxLayout) view.findViewById(R.id.dialog_lifegoods_sel_fbl_color);
        rgSizeSel = (RadioGroup) view.findViewById(R.id.dialog_lifegoods_sel_tfl_size);
        rgSizeSelFlxLayout = (FlexboxLayout) view.findViewById(R.id.dialog_lifegoods_sel_fbl_size);

        this.setContentView(view);
        this.setCancelable(true);
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        //显示的坐标
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //内容 透明度
        //        lp.alpha = 0.2f;
        lp.width = MyApplication.screenWidth;
        lp.height = MyApplication.screenHeight * 3 / 4;
        //遮罩 透明度
//        lp.dimAmount = 0.2f;
        dialogWindow.setAttributes(lp);
    }

    public void bindData2View(final CommodityDetailsModel model) {
        if (model == null) return;
        if (model.getColorList() == null) return;
        final View.OnClickListener sizeSelChangeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton) v;
                if (seledColor == null) return;
                if (seledColor.getSizeList() == null) return;
                if (seledColor.getSizeList().size() <= 0) return;
                clearOtherChecked(rgSizeSelFlxLayout, rb);
                int idx = rgSizeSelFlxLayout.indexOfChild(rb);
                if (seledSize == seledColor.getSizeList().get(idx)) return;
                seledSize = seledColor.getSizeList().get(idx);
                setSeledText();
            }

        };

        View.OnClickListener colorSelChangeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rbColor = (RadioButton) v;
                if (rbColor.isChecked()) {
                    clearOtherChecked(rgColorSelFlxLayout, rbColor);
                    int idx = rgColorSelFlxLayout.indexOfChild(rbColor);
                    if (seledColor == model.getColorList().get(idx)) return;
                    seledColor = model.getColorList().get(idx);
                    rgSizeSelFlxLayout.removeAllViews();
                    for (int i = 0; i < seledColor.getSizeList().size(); i++) {
                        CommodityDetailsModel.SizeList sizeItem = seledColor.getSizeList().get(i);
                        RadioButton rb = (RadioButton) LayoutInflater.from(context).inflate(R.layout.item_radiobutton, rgSizeSel, false);
                        rb.setText(sizeItem.getCommoditySize());
                        rb.setOnClickListener(sizeSelChangeListener);
                        rgSizeSelFlxLayout.addView(rb);
                        if(i==0){
                            rb.setChecked(true);
                            sizeSelChangeListener.onClick(rb);
                        }
                    }
                    setSeledText();
                }
            }
        };
        for (int i = 0; i < model.getColorList().size(); i++) {
            CommodityDetailsModel.ColorList colorItem = model.getColorList().get(i);
            RadioButton rb = (RadioButton) LayoutInflater.from(context).inflate(R.layout.item_radiobutton, rgColorSel, false);
            rb.setText(colorItem.getColorName());
            rb.setOnClickListener(colorSelChangeListener);
            rgColorSelFlxLayout.addView(rb);
            if (i == 0) {
                rb.setChecked(true);
                colorSelChangeListener.onClick(rb);
            }
        }
//        RadioGrouptflColorSel = (TagFlowLayout) view.findViewById(R.id.dialog_lifegoods_sel_tfl_color);
//        ~ = (TagFlowLayout) view.findViewById(R.id.dialog_lifegoods_sel_tfl_color);
        /*colorListAdapter = new TagAdapter<CommodityDetailsModel.ColorList>(model.getColorList()) {

            @Override
            public View getView(FlowLayout parent, int position, CommodityDetailsModel.ColorList colorList) {
                TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.tag_item, parent, false);
                textView.setText(colorList.getColorName());
                return textView;
            }
        };

        rgColorSel.setAdapter(colorListAdapter);

        rgSizeSel.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                if (selectPosSet == null) {
                    rgSizeSel.removeAllViews();
                }
                if (sizeAdapter == null) return;
                Integer pos = selectPosSet.iterator().next();
                seledSize = sizeAdapter.getItem(pos);
                setSeledText();
            }
        });

        rgColorSel.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
//                clearSizeList();
                if (selectPosSet == null) {
                    rgSizeSel.removeAllViews();
                }
                Integer pos = selectPosSet.iterator().next();
                CommodityDetailsModel.ColorList colorItem = colorListAdapter.getItem(pos);
                seledColor = colorItem;
                sizeAdapter = new TagAdapter<CommodityDetailsModel.SizeList>(colorItem.getSizeList()) {
                    @Override
                    public View getView(FlowLayout parent, int position, CommodityDetailsModel.SizeList sizeList) {
                        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.tag_item, parent, false);
                        textView.setText(sizeList.getCommoditySize());
                        return textView;
                    }
                };
                rrg.setAdapter(sizeAdapter);
            }
        });*/
    }

    private void clearOtherChecked(FlexboxLayout rgColorSelFlxLayout, RadioButton rbMe) {
        if (rgColorSelFlxLayout == null) return;
        for (int i = 0; i < rgColorSelFlxLayout.getChildCount(); i++) {
            RadioButton rb = (RadioButton) rgColorSelFlxLayout.getChildAt(i);
            if (rb == rbMe) {
                continue;
            }
            if (rb.isChecked()) {
                rb.setChecked(false);
            }
        }
    }

    private void setSeledText() {
        String selColor = "颜色", selSize = "规格";
        if (seledSize != null) {
            selSize = seledSize.getCommoditySize();
            tvMoney.setText("￥" + seledSize.getCostMoney());
            tvStore.setText("库存:" + seledSize.getQuanlityUsable());
            imgGoods.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + seledSize.getImgPath()));
            Utils.log("getImgPath:" + AppConfig.IMAGE_PATH_LJT + seledSize.getImgPath());
        }
        if (seledColor != null) {
            selColor = seledColor.getColorName();
        }
        Utils.log("选择:" + selSize + "," + selColor + ",getImgPath:");
        tvSeled.setText("选择:" + selSize + "," + selColor);
    }
}
