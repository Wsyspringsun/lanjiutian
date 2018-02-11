package com.wyw.ljtds.widget.commodity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.NumberButton;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class CheckInchPopWindow implements OnDismissListener, OnClickListener {

    public interface OnSelectedCompleteLinstener {
        /**
         * 商品详情被选中回调
         */
        public abstract void onComplete(CheckInchModel result);
    }

    private OnSelectedCompleteLinstener onSelectedCompleteListener;

    public void setOnSelectedCompleteListener(
            OnSelectedCompleteLinstener selectedCompleteListener) {
        this.onSelectedCompleteListener = selectedCompleteListener;
    }

    private ImageView pop_del;

    private PopupWindow popupWindow;
    private OnItemClickListener listener;
    private Context context;
    private Activity activity;
    private List<CommodityDetailsModel.ColorList> color;
    private List<CommodityDetailsModel.ColorList> color1 = new ArrayList<>();
    private List<CommodityDetailsModel.SizeList> size;


    //    private FlowTagLayout flow_layout1;
//    private FlowTagLayout flow_layout2;
    private TagFlowLayout flowLayout1;
    private TagFlowLayout flowLayout2;
    private TextView inventory_tv;
    private TextView real_price_tv;
    private SimpleDraweeView iv_adapter_grid_pic;
    private Button ok_tv, add_car;
    private TextView has_choose_tv;
    private NumberButton number_button;

    LayoutInflater mInflater;
    private int sum = 0;


    private ArrayList<String> mColorListStr;
    private ArrayList<String> mSizeListStr;
    private com.zhy.view.flowlayout.TagAdapter adapter1;
    private com.zhy.view.flowlayout.TagAdapter adapter2;
    private CheckInchModel reslut = new CheckInchModel();


    public CheckInchPopWindow(final Context context, CommodityDetailsModel commodityDetailsModel) {
        this.context = context;
        this.color = commodityDetailsModel.getColorList();
        this.activity = (Activity) context;
        this.mInflater = LayoutInflater.from(context);


        ViewGroup view = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.adapter_popwindow, null, true);


//        flow_layout1 = (FlowTagLayout) view.findViewById( R.id.flow_layout1 );
//        flow_layout2 = (FlowTagLayout) view.findViewById( R.id.flow_layout2 );
        flowLayout1 = (TagFlowLayout) view.findViewById(R.id.flow_layout1);
        flowLayout2 = (TagFlowLayout) view.findViewById(R.id.flow_layout2);
        ok_tv = (Button) view.findViewById(R.id.ok_tv);
        add_car = (Button) view.findViewById(R.id.add_car);

        pop_del = (ImageView) view.findViewById(R.id.pop_del);
        inventory_tv = (TextView) view.findViewById(R.id.inventory_tv);
        real_price_tv = (TextView) view.findViewById(R.id.real_price_tv);
        has_choose_tv = (TextView) view.findViewById(R.id.has_choose_tv);
        iv_adapter_grid_pic = (SimpleDraweeView) view.findViewById(R.id.iv_adapter_grid_pic);
        number_button = (NumberButton) view.findViewById(R.id.number_button);

        pop_del.setOnClickListener(this);
        ok_tv.setOnClickListener(this);
        add_car.setOnClickListener(this);

        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.popWindow_anim_style);
        ColorDrawable colorDrawable = new ColorDrawable(Color.WHITE);
        popupWindow.setBackgroundDrawable(colorDrawable);
        backgroundAlpha(activity, 0.5f);//0.0-1.0
        popupWindow.setOnDismissListener(this);

        if (color != null && !color.isEmpty()) {
            for (int i = 0; i < color.size(); i++) {
                if (color.get(i).getSizeList() != null && !color.get(i).getSizeList().isEmpty()) {
                    color1.add(color.get(i));
                }
            }
        }

        mColorListStr = new ArrayList<>();
        for (int i = 0; i < color1.size(); i++) {
            mColorListStr.add(color1.get(i).getColorName());
        }


        adapter1 = new com.zhy.view.flowlayout.TagAdapter<String>(mColorListStr) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.tag_item, flowLayout1, false);
                textView.setText(o);
                return textView;
            }
        };
        flowLayout1.setAdapter(adapter1);
        flowLayout1.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                adapter2.clearAndAddAll(getsize(position));
                reslut.setCololr(adapter1.getItem(position).toString());
                adapter2.setSelectedList(0);
                reslut.setColor_option(position);//设置默认的选中项
                reslut.setColor_id(color1.get(position).getCommodityColorId());

                if (mSizeListStr != null && !mSizeListStr.isEmpty()) {
                    reslut.setSize(mSizeListStr.get(0));//设置默认的选中项
                    reslut.setSize_option(0);//设置默认的选中项
                    reslut.setSize_id(size.get(0).getCommoditySizeId());
                    update(0);
                }
                getResult2Show(reslut);
                return true;
            }
        });
        adapter1.setSelectedList(0);
        reslut.setCololr(adapter1.getItem(0).toString());//设置默认的选中项
        reslut.setColor_option(0);//设置默认的选中项
        reslut.setColor_id(color1.get(0).getCommodityColorId());

        adapter2 = new com.zhy.view.flowlayout.TagAdapter<String>(getsize(0)) {
            @Override
            public View getView(FlowLayout parent, int position, String o) {
                TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.tag_item, flowLayout1, false);
                textView.setText(o);
                return textView;
            }
        };
        flowLayout2.setAdapter(adapter2);
        flowLayout2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                reslut.setSize(adapter2.getItem(position).toString());
                reslut.setSize_option(position);//设置默认的选中项
                reslut.setSize_id(size.get(position).getCommoditySizeId());
                update(position);
                getResult2Show(reslut);
                return true;
            }
        });
        adapter2.setSelectedList(0);
        reslut.setSize(adapter2.getItem(0).toString());//设置默认的选中项
        reslut.setSize_option(0);//设置默认的选中项
        reslut.setSize_id(size.get(0).getCommoditySizeId());
        update(0);

        reslut.setNum(1);
        number_button.setBuyMax(999).setInventory(reslut.getUsable()).setCurrentNumber(1)
                .setOnWarnListener(new NumberButton.OnWarnListener() {

                    @Override
                    public void onWarningForInventory(int inventory) {//超过库存
                        ToastUtil.show(context, "您最多只能购买这么多了");
                    }

                    @Override
                    public void onWarningForBuyMax(int max) {//超过最大可购买数量
                        ToastUtil.show(context, "您最多只能购买这么多了");
                    }
                })
                .setOnNumberListener(new NumberButton.OnNumberListener() {
                    @Override
                    public void OnNumberChange(int num) {
                        reslut.setNum(num);
                    }
                });

//            adapter1 = new TagAdapter<String>( context );
//            flow_layout1.setAdapter( adapter1 );
//
//
//            flow_layout1.setTagCheckedMode( FlowTagLayout.FLOW_TAG_CHECKED_SINGLE );
//            flow_layout1.setOnTagClickListener( new OnTagClickListener() {
//                @Override
//                public void onItemClick(FlowTagLayout parent, View view, int position) {
//                    Log.e( "***", position + "" );
//                }
//            } );
//
//            flow_layout1.setOnTagSelectListener( new OnTagSelectListener() {
//                @Override
//                public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
//                    adapter2.clearAndAddAll( getsize( Integer.parseInt( StringUtils.getNumber( selectedList.toString() ) ) ) );
//                    String str = parent.getAdapter().getItem( Integer.parseInt( StringUtils.getNumber( selectedList.toString() ) ) ).toString();
//                    reslut.setClolr( str );
//                    if (mSizeListStr != null && !mSizeListStr.isEmpty()) {
//                        reslut.setSize( mSizeListStr.get( 0 ) );//设置默认的选中项
//                        update( 0 );
//                    }
//
//                    getResult2Show( reslut );
//
//                }
//            } );
//
//            adapter1.onlyAddAll( mColorListStr );
//            reslut.setClolr( mColorListStr.get( 0 ) );//设置默认的选中项
//
//
//            adapter2 = new TagAdapter<String>( context );
//            flow_layout2.setAdapter( adapter2 );
//            flow_layout2.setTagCheckedMode( FlowTagLayout.FLOW_TAG_CHECKED_SINGLE );
//            flow_layout2.setOnTagClickListener( new OnTagClickListener() {
//                @Override
//                public void onItemClick(FlowTagLayout parent, View view, int position) {
//                    Log.e( "***", position + "" );
//                }
//            } );
//
//            flow_layout2.setOnTagSelectListener( new OnTagSelectListener() {
//                @Override
//                public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
//                    String str = parent.getAdapter().getItem( Integer.parseInt( StringUtils.getNumber( selectedList.toString() ) ) ).toString();
//                    reslut.setSize( str );
//                    update( Integer.parseInt( StringUtils.getNumber( selectedList.toString() ) ) );
//                    getResult2Show( reslut );
//                }
//            } );
//
//            adapter2.clearAndAddAll( getsize( 0 ) );
//            reslut.setSize( mSizeListStr.get( 0 ) );//设置默认的选中项
//            update( 0 );

    }

    /**
     * 显示popwindow
     *
     * @param parent
     */

    public void showAsDropDown(View parent) {
        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        getResult2Show(reslut);
        backgroundAlpha(activity, 1f);
    }

    public void dissmiss() {

        if (popupWindow != null && popupWindow.isShowing()) {
            getResult2Show(reslut);
            popupWindow.dismiss();
        }
    }

    private List<String> getsize(int index) {
        mSizeListStr = new ArrayList<>();
        mSizeListStr.clear();
        size = color1.get(index).getSizeList();

        for (int j = 0; j < size.size(); j++) {
            mSizeListStr.add(size.get(j).getCommoditySize());
//            Log.e( "*********",mSizeListStr.size()+"" );
        }

//        if (!mSizeListStr.isEmpty()&&mSizeListStr!=null){
//            reslut.setSize( mSizeListStr.get( 0 ) );
//        }else {
//            reslut.setSize( "no_size" );
//        }
        return mSizeListStr;
    }

    private void update(int index) {
        if (size != null && !size.isEmpty()) {
            reslut.setNew_money(size.get(index).getCostMoney());
            reslut.setOld_money(size.get(index).getMarketPrice());
            reslut.setUsable(size.get(index).getQuanlityUsable());
            String img[] = new String[6];
            if (!StringUtils.isEmpty(size.get(index).getImgPath())) {
                img[0] = "http://www.lanjiutian.com/upload/images" + size.get(index).getImgPath();
            } else {
                img[0] = "";
            }

            if (!StringUtils.isEmpty(size.get(index).getImgPath2())) {
                img[1] = "http://www.lanjiutian.com/upload/images" + size.get(index).getImgPath2();
            } else {
                img[1] = "";
            }

            if (!StringUtils.isEmpty(size.get(index).getImgPath3())) {
                img[2] = "http://www.lanjiutian.com/upload/images" + size.get(index).getImgPath3();
            } else {
                img[2] = "";
            }

            if (!StringUtils.isEmpty(size.get(index).getImgPath4())) {
                img[3] = "http://www.lanjiutian.com/upload/images" + size.get(index).getImgPath4();
            } else {
                img[3] = "";
            }

            if (!StringUtils.isEmpty(size.get(index).getImgPath5())) {
                img[4] = "http://www.lanjiutian.com/upload/images" + size.get(index).getImgPath5();
            } else {
                img[4] = "";
            }

            if (!StringUtils.isEmpty(size.get(index).getImgPath6())) {
                img[5] = "http://www.lanjiutian.com/upload/images" + size.get(index).getImgPath6();
            } else {
                img[5] = "";
            }
            reslut.setImage(img);
            getResult2Show(reslut);
        }
    }


    private void getResult2Show(final CheckInchModel result) {
        if (result == null) {

            Toast.makeText(context, "选择的对象为空", Toast.LENGTH_SHORT).show();

        } else {
            if (onSelectedCompleteListener != null) {
                onSelectedCompleteListener.onComplete(result);
            }


            real_price_tv.setText("￥" + result.getNew_money());
            inventory_tv.setText("库存" + result.getUsable());
            has_choose_tv.setText("已选择：" + "\"" + result.getCololr() + "\"  \"" + result.getSize() + "\"");
            iv_adapter_grid_pic.setImageURI(Uri.parse(result.getImage()[0]));
        }
    }


    public interface OnItemClickListener {

        public void onClickAdd2Car(CheckInchModel result);

        public void onClickBuyNow(CheckInchModel result);
    }

    public void setSelect(CheckInchModel checkInchModel) {
        adapter1.setSelectedList(checkInchModel.getColor_option());
        adapter2.setSelectedList(checkInchModel.getSize_option());
        adapter2.clearAndAddAll(getsize(checkInchModel.getColor_option()));
        reslut.setCololr(adapter1.getItem(checkInchModel.getColor_option()).toString());
        reslut.setSize(adapter2.getItem(checkInchModel.getSize_option()).toString());
        reslut.setColor_option(checkInchModel.getColor_option());
        reslut.setSize_option(checkInchModel.getSize_option());
        reslut.setColor_id(checkInchModel.getColor_id());
        reslut.setSize_id(checkInchModel.getSize_id());
        number_button.setCurrentNumber(checkInchModel.getNum());
        reslut.setNum(checkInchModel.getNum());
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 关闭popwindow
            case R.id.pop_del:

                dissmiss();
                break;

            //购买
            case R.id.ok_tv:
                listener.onClickBuyNow(reslut);
                break;

            //加入购物车
            case R.id.add_car:
                listener.onClickAdd2Car(reslut);
                break;
        }
    }


}






