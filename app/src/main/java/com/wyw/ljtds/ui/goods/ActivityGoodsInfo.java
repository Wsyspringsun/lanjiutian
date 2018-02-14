package com.wyw.ljtds.ui.goods;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.weixin.uikit.MMAlert;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.goodsinfo.ItemTitlePagerAdapter;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.GoodsModel;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.cart.CartActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.goodsinfo.NoScrollViewPager;

import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/12/26 0026.
 */
//@ContentView(R.layout.activity_goods_info)
public abstract class ActivityGoodsInfo extends BaseActivity {
    /**
     * 微信分享使用
     **/
    private IWXAPI wxApi;
    private static final int MMAlertSelect1 = 0; //选择分享的渠道
    private static final int MMAlertSelect2 = 1;
    private static final int MMAlertSelect3 = 2;

    public static String VAL_INFO_SOURCE = "ActivityMedicinesInfo";
    @ViewInject(R.id.vp_content)
    public ViewPager nsvpContent;
    //    public NoScrollViewPager nsvpContent;
    @ViewInject(R.id.tv_title)
    public TextView tv_title;
    @ViewInject(R.id.activity_goods_info_animator_img)
    protected ImageView imgAnimator;
    @ViewInject(R.id.activity_goods_info_tv_addcart)
    protected TextView tvAddcart;
    @ViewInject(R.id.activity_goods_info_tv_goumai)
    protected TextView tvGoumai;
    @ViewInject(R.id.activity_goods_info_tv_shop)
    private TextView tvShop;
    @ViewInject(R.id.activity_goods_info_tv_kefu)
    private TextView tvKefu;
    @ViewInject(R.id.activity_goods_info_tv_shoucang)
    private TextView tvShoucang;
    @ViewInject(R.id.activity_goods_info_img_shoucang)
    private ImageView imgShoucang;
    @ViewInject(R.id.activity_goods_info_tb_main)
    private Toolbar tbMain;


    AbsoluteSizeSpan ass = new AbsoluteSizeSpan(20);
    private static final int THUMB_SIZE = 150;
    //    public MedicineDetailsModel model;
    private List<Fragment> fragmentList = new ArrayList<>();
    private Dialog dialogConsult;

    protected FragmentGoodsPagerDetails fragmentGoodsDetail;
    protected FragmentGoodsPagerEvaluate fragmentGoodsEvaluate;
    private boolean toolbarStat;
    private View cartIcon;


    @Override
    public void onBackPressed() {
        //不是详情页面时组织后退
        if (nsvpContent.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            nsvpContent.setCurrentItem(0);
        }
    }

    /**
     * 将商品放入
     *
     * @return
     */
    protected void initFragmentList(String id) {
        fragmentGoodsDetail = new FragmentGoodsPagerDetails();
        fragmentGoodsEvaluate = FragmentGoodsPagerEvaluate.newInstance(id);
        fragmentList.add(getMainFragment());
        fragmentList.add(fragmentGoodsDetail);
        fragmentList.add(fragmentGoodsEvaluate);

        nsvpContent.setAdapter(new ItemTitlePagerAdapter(getSupportFragmentManager(),
                fragmentList, new String[]{"商品", "详情", "评价"}));
        nsvpContent.setOffscreenPageLimit(3);
    }

    protected abstract OrderTradeDto info2Order();

    protected abstract Fragment getMainFragment();

    protected abstract void handleClickEvent(View v);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxApi = ((MyApplication) getApplication()).wxApi;

        initIconBtnStat();


        //设置图标
//        int i = 0, j = 2;//i:起始 j:字数
//        Utils.setIconText(this, tvShop, "\ue623\n店铺");
//        SpannableString sbs1 = new SpannableString(tvShop.getText());
//        sbs1.setSpan(ass, i, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tvShop.setText(sbs1);
//
//        Utils.setIconText(this, tvKefu, "\ue60e\n客服");
//        SpannableString sbs2 = new SpannableString(tvKefu.getText());
//        sbs2.setSpan(ass, i, j, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tvKefu.setText(sbs2);
//
        setShoucangText("0");

//        setToolMainEnable(false);
        setToolMainEnable(true);
        imgAnimator.setVisibility(View.GONE);
    }

    protected void setToolMainEnable(boolean stat) {
        toolbarStat = stat;
        for (int i = 0; i < tbMain.getChildCount(); i++) {
            View tv = tbMain.getChildAt(i);
            tv.setEnabled(stat);
        }
        tbMain.setEnabled(stat);
    }

    protected void wechatShare(final String title, final String description, final String imgUrl, final String url) {
        MMAlert.showAlert(ActivityGoodsInfo.this, "分享", ActivityGoodsInfo.this.getResources().getStringArray(R.array.send_webpage_item),
                null, new MMAlert.OnAlertSelectId() {
                    @Override
                    public void onClick(int whichButton) {
                        WXMediaMessage msg = null;
                        SendMessageToWX.Req req = null;

                        WXWebpageObject webpage = new WXWebpageObject();
                        webpage.webpageUrl = url;
                        msg = new WXMediaMessage(webpage);
                        msg.title = title;
                        msg.description = description;
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.send_music_thumb);
                        Bitmap.CompressFormat cprsFormat = Bitmap.CompressFormat.PNG;
                        if (!StringUtils.isEmpty(imgUrl)) {
                            int fixIdx = imgUrl.lastIndexOf('.');
                            if (fixIdx > 0) {
                                String fix = imgUrl.substring(fixIdx);
                                if ("jpg".equals(fix.toLowerCase())) {
                                    cprsFormat = Bitmap.CompressFormat.JPEG;
                                }
                                try {
                                    bmp = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                        if (bmp != null) {
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
                            bmp.recycle();
                            msg.thumbData = Utils.bmpToByteArray(cprsFormat, thumbBmp, true);
                        } else {
                        }

                        req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;

                        switch (whichButton) {
                            case MMAlertSelect1:
                                req.scene = SendMessageToWX.Req.WXSceneSession;
                                break;
                            case MMAlertSelect2:
                                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                break;
                            case MMAlertSelect3:
                                req.scene = SendMessageToWX.Req.WXSceneFavorite;
                                break;
                        }

                        wxApi.sendReq(req);
                    }
                });
    }

    //显示咨询的模窗口
    public void showConsultImtes(int[] point) {
        if (dialogConsult == null) {
            dialogConsult = new Dialog(this, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater = this.getLayoutInflater();

            View layout = inflater.inflate(R.layout.fragment_consult_items, (ViewGroup) findViewById(R.id.vp_content), false);
            View itemKefu = layout.findViewById(R.id.fragment_consult_ll_kefu);
            itemKefu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleClickEvent(v);
                    dialogConsult.dismiss();
                }
            });
            View itemTel = layout.findViewById(R.id.fragment_consult_ll_tel);
            itemTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleClickEvent(v);
                    dialogConsult.dismiss();
                }
            });

            dialogConsult.setContentView(layout);
            dialogConsult.setCancelable(true);


            Window dialogWindow = dialogConsult.getWindow();
//        dialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialogWindow.setGravity(Gravity.BOTTOM | Gravity.LEFT);
            //显示的坐标
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            if (point != null && point.length > 0) {
                lp.x = point[0];
                lp.y = 80;
            }
            Log.e(AppConfig.ERR_TAG, "lp.x" + point[0]);
            Log.e(AppConfig.ERR_TAG, "lp.y" + point[1]);
            //内容 透明度
//        lp.alpha = 0.2f;
            //遮罩 透明度
            lp.dimAmount = 0.2f;
//        //dialog的大小
//            lp.width = width;
//            lp.verticalMargin = 20f;
            dialogWindow.setAttributes(lp);

        }
        dialogConsult.show();

    }

    private void initIconBtnStat() {
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.back);
        TextView tvTitle = (TextView) findViewById(R.id.activity_fragment_title);
        tvTitle.setText(getTitle());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nsvpContent.getCurrentItem() == 0) {
                    ActivityGoodsInfo.this.finish();
                } else {
                    nsvpContent.setCurrentItem(0);
                }
//                ActivityGoodsInfo.this.finish();
            }
        });

        LinearLayout llIconBtnlist = (LinearLayout) findViewById(R.id.ll_icon_btnlist);
        for (int i = 0; i < llIconBtnlist.getChildCount(); i++) {
            View v = llIconBtnlist.getChildAt(i);
            switch (i) {
                case 2:
                    //进入购物车
                    v.setVisibility(View.VISIBLE);
                    cartIcon = v;
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent it = new Intent(ActivityGoodsInfo.this, CartActivity.class);
                            startActivity(it);
                        }
                    });
                    break;
            }
        }
    }


    private void animateAddCartOk(View v) {
        int[] endPoint = new int[2];
        cartIcon.getLocationOnScreen(endPoint);
        int lEnd = endPoint[0], tEnd = endPoint[1];
//        float lEnd = cartIcon.getX(), tEnd = cartIcon.getY();
        int[] startPoint = new int[2];
        tvAddcart.getLocationOnScreen(startPoint);
        int lStart = startPoint[0], tStart = startPoint[1];
//        float lStart = btnAddCart.getX(), tStart = btnAddCart.getY();
        imgAnimator.setVisibility(View.VISIBLE);
        int duration = 1500;
        final ObjectAnimator yAnimator = ObjectAnimator.ofFloat(v, "y", tStart, tEnd).setDuration(duration);
        final ObjectAnimator xAnimator = ObjectAnimator.ofFloat(v, "x", lStart, lEnd).setDuration(duration);
        yAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                imgAnimator.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        yAnimator.start();
        xAnimator.start();
    }

    //添加购物车
    public void addCart(final String str) {
        setLoding(this, false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return GoodsBiz.shoppingCart(str, "create");
//                return "";
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();
                ToastUtil.show(ActivityGoodsInfo.this, "添加购物车成功！");

                animateAddCartOk(imgAnimator);
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    //收藏
    protected void doFavorite(final String id, final GoodsModel goods) {
        new BizDataAsyncTask<Boolean>() {
            @Override
            protected Boolean doExecute() throws ZYException, BizFailure {
                Log.e(AppConfig.ERR_TAG, "doFavorite:" + goods.getFavorited());
                String type = goods instanceof MedicineDetailsModel ? "1" : "0";
                if ("0".equals(goods.getFavorited())) {
                    //add to Favorite
                    return UserBiz.addFavoritesGoods(id, type);
                } else {
                    //remove from Favorite
                    return UserBiz.deleteFavoritesGoods(id);
                }
            }

            @Override
            protected void onExecuteSucceeded(Boolean aBoolean) {
                closeLoding();
                if (aBoolean) {
                    if ("0".equals(goods.getFavorited())) {
                        //add to Favorite
                        goods.setFavorited("1");
                    } else {
                        //remove from Favorite
                        goods.setFavorited("0");
                    }
                    setShoucangText(goods.getFavorited());
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    protected void setShoucangText(String flg) {
        String txt = "";
        if ("1".equals(flg)) {
            //add to Favorite
            imgShoucang.setImageDrawable(ActivityCompat.getDrawable(ActivityGoodsInfo.this, R.drawable.ic_shoucangxuan));
            txt = "\ue631\n收藏";
        } else {
            //remove from Favorite
            imgShoucang.setImageDrawable(ActivityCompat.getDrawable(ActivityGoodsInfo.this, R.drawable.ic_shoucang));
            txt = "\ue62e\n收藏";
        }
//        Utils.setIconText(this, tvShoucang, txt);
//        SpannableString sbs3 = new SpannableString(tvShoucang.getText());
//        sbs3.setSpan(ass, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tvShoucang.setText(sbs3);
    }

    public boolean isToolbarStat() {
        return toolbarStat;
    }
}
