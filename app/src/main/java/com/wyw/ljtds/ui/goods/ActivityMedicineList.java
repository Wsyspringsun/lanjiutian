package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.CategoryBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.GoodsModel;
import com.wyw.ljtds.model.MedicineListModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.category.ActivityScan;
import com.wyw.ljtds.ui.home.ActivitySearch;
import com.wyw.ljtds.ui.user.ActivityMessage;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.SpaceItemDecoration;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

@ContentView(R.layout.activity_goods_list)
public class ActivityMedicineList extends BaseActivity {
    public static final String ARG_MTD_FIND = "1";
    public static final String ARG_MTD_GET = "0";
    public static final String TAG_LIST_FROM = "com.wyw.ljtds.ui.goods.ActivityMedicineList.tag_list_from";
    public static final String TAG_PARAM_CLASSID = "com.wyw.ljtds.ui.goods.ActivityMedicineList.TAG_PARAM_CLASSID";
    public static final String TAG_PARAM_TYPE = "com.wyw.ljtds.ui.goods.ActivityMedicineList.TAG_PARAM_TYPE";
    public static final String TAG_PARAM_TOPFLG = "com.wyw.ljtds.ui.goods.ActivityMedicineList.TAG_PARAM_TOPFLG";
    public static final String TAG_PARAM_KEYWORD = "com.wyw.ljtds.ui.goods.ActivityMedicineList.TAG_PARAM_KEYWORD";


    @ViewInject(R.id.activity_goods_list_sr)
    SwipeRefreshLayout srf;

    @ViewInject(R.id.recyclerView)
    private RecyclerView recyclerView; //药品列表
    @ViewInject(R.id.back)
    private ImageView back;
    @ViewInject(R.id.zxing)
    private LinearLayout zxing;
    @ViewInject(R.id.ll_message)
    private LinearLayout llMessage;
    @ViewInject(R.id.edHeader)
    private TextView search;
    @ViewInject(R.id.paixu1_tv)
    private TextView paixu1_tv;
    @ViewInject(R.id.paixu1_iv)
    private ImageView paixu1_iv;
    @ViewInject(R.id.paixu2_tv)
    private TextView paixu2_tv;
    @ViewInject(R.id.paixu3_tv)
    private TextView paixu3_tv;
    @ViewInject(R.id.paixu3_iv)
    private ImageView paixu3_iv;
    @ViewInject(R.id.paixu4_tv)
    private TextView paixu4_tv;
    @ViewInject(R.id.main_header_location)
    TextView tvLocation;


    //无数据时的界面
    private View noData;
    private boolean isRise = true;
    private List<MedicineListModel> list;
    private MyAdapter adapter;
    private boolean end = false;


    /*8
    params for load data
     */
    String lat = "", lng = "";
    private String mtdtype = "";
    private String keyword = "";//搜索的关键字
    private String classId = "";//分类的typeid
    private String orderby = "";
    private int pageIndex = 1;
    private String topFlg = "";
    private CategoryBiz categoryBiz;

    @Event(value = {R.id.back, R.id.zxing, R.id.edHeader, R.id.paixu1, R.id.paixu2, R.id.paixu3, R.id.paixu4})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.zxing:
                startActivity(new Intent(this, ActivityScan.class));
                break;
            case R.id.edHeader:
                startActivity(ActivitySearch.getIntent(this, 0, search.getText().toString()));
                break;

            case R.id.paixu1:
                reset();
                paixu1_tv.setTextColor(getResources().getColor(R.color.base_bar));
                paixu1_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_1));
                orderby = "";
                sortData();
                break;
            case R.id.paixu2:
                reset();
                paixu2_tv.setTextColor(getResources().getColor(R.color.base_bar));
                orderby = "1";
                sortData();
                break;
            case R.id.paixu3:
                reset();
                if (isRise) {
                    paixu3_tv.setTextColor(getResources().getColor(R.color.base_bar));
                    paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_4));
                    isRise = false;
                    orderby = "2";
                } else {
                    paixu3_tv.setTextColor(getResources().getColor(R.color.base_bar));
                    paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_5));
                    isRise = true;
                    orderby = "3";
                }
                sortData();
                break;
            case R.id.paixu4:
                reset();
                paixu4_tv.setTextColor(getResources().getColor(R.color.base_bar));
                orderby = "5";
                sortData();
                break;
        }
    }

    private void sortData() {
        pageIndex = 1;
        getlist();
    }

    /**
     * @param context
     * @param type    0:获取一对多关系 ，1 ： 获取多对多关系列表
     * @param topFlg  活动标识
     * @param classId 分类
     * @param keyword 查询关键字
     * @return
     */
    public static Intent getIntent(Context context, String type, String topFlg, String classId, String keyword) {
        Intent it = new Intent(context, ActivityMedicineList.class);
//        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        it.putExtra(TAG_PARAM_TYPE, type);
        it.putExtra(TAG_PARAM_TOPFLG, topFlg);
        it.putExtra(TAG_PARAM_CLASSID, classId);
        it.putExtra(TAG_PARAM_KEYWORD, keyword);
        return it;
    }

    void initParams() {
        Intent it = getIntent();
        lat = "" + SingleCurrentUser.defaultLat;
        lng = "" + SingleCurrentUser.defaultLng;
        if (SingleCurrentUser.location != null) {
            lat = "" + SingleCurrentUser.location.getLatitude();
            lng = "" + SingleCurrentUser.location.getLongitude();
        }
        mtdtype = it.getStringExtra(TAG_PARAM_TYPE);
        topFlg = it.getStringExtra(TAG_PARAM_TOPFLG);
        classId = it.getStringExtra(TAG_PARAM_CLASSID);
        keyword = it.getStringExtra(TAG_PARAM_KEYWORD);
        if (!StringUtils.isEmpty(keyword)) {
            search.setText(keyword);
        }
        pageIndex = 1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categoryBiz = CategoryBiz.getInstance(this);

        back.setVisibility(View.VISIBLE);
        llMessage.setVisibility(View.GONE);
        zxing.setVisibility(View.GONE);

//        final GridLayoutManager glm = new GridLayoutManager(this, 1);
        final LinearLayoutManager glm = new LinearLayoutManager(this);
        setLocation();
        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getlist();
                srf.setRefreshing(false);
            }
        });

        recyclerView.setLayoutManager(glm);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                MedicineListModel detail = adapter.getData().get(i);
                Intent it = ActivityMedicinesInfo.getIntent(ActivityMedicineList.this, detail.getWAREID(), detail.getLOGISTICS_COMPANY_ID());
                startActivity(it);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int cnt = adapter.getItemCount();
//                int totalItemCount = glm.getItemCount();
                int lastVisibleItem = glm.findLastVisibleItemPosition();
                if (!end && !loading && (lastVisibleItem + 1) >= cnt) {
                    pageIndex = pageIndex + 1;
                    getlist();
                }
            }
        });
        adapter = new MyAdapter();
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
        adapter.setEmptyView(noData);
        recyclerView.setAdapter(adapter);


        initParams();
        getlist();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void getlist() {
        setLoding(this, false);
        new BizDataAsyncTask<List<MedicineListModel>>(this) {
            @Override
            protected List<MedicineListModel> doExecute() throws ZYException, BizFailure {
                return categoryBiz.getMedicineList(mtdtype, topFlg, classId, orderby, keyword, pageIndex + "", AppConfig.DEFAULT_PAGE_COUNT + "", lat, lng);
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineListModel> medicineListModel) {
                closeLoding();
                if (medicineListModel == null || medicineListModel.size() <= 0) {
                    end = true;
                }

                list = medicineListModel;
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
                adapter.setEmptyView(noData);
            }
        }.execute();
    }

    private void bindData2View() {
        if (pageIndex == 1) {
            adapter.setNewData(list);
        } else {
            adapter.addData(list);
        }
        adapter.notifyDataSetChanged();
    }


    private class MyAdapter extends BaseQuickAdapter<MedicineListModel> {

        public MyAdapter() {
            super(R.layout.item_goods_onecol, list);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, MedicineListModel medicineListModel) {
            String wareName = StringUtils.deletaFirst(medicineListModel.getWARENAME()),
                    brand = medicineListModel.getCOMMODITY_BRAND(),
                    detailFlg = StringUtils.isEmpty(medicineListModel.getCOMMODITY_PARAMETER()) ? "" : "[" + medicineListModel.getCOMMODITY_PARAMETER() + "]",
                    size = medicineListModel.getWARESPEC(),
                    shopname = medicineListModel.getLOGISTICS_COMPANY(),
                    distanceText = medicineListModel.getDISTANCE_TEXT() + "|",
                    durationText = medicineListModel.getDURATION_TEXT() + "   ",
                    qisong = "￥" + medicineListModel.getQISONG() + "配送|",
                    baoyou = "￥" + medicineListModel.getBAOYOU() + "包邮",
                    postage = "   配送费￥" + medicineListModel.getPOSTAGE() + "\n",
                    price = "￥" + medicineListModel.getSALEPRICE() + "";


            StringBuilder sbName = new StringBuilder().append(detailFlg).append(brand + " ").append(wareName).append(size);
            baseViewHolder.setText(R.id.item_goods_omecol_goods_name, sbName);

            StringBuilder sb = new StringBuilder()
                    .append(shopname).append(postage)
                    .append(distanceText)
                    .append(durationText)
                    .append(qisong).append(baoyou);
            int shopnameStart = sb.indexOf(shopname), shopnameEnd = shopnameStart + shopname.length();

            SpannableString sbs = new SpannableString(sb.toString());
//            sbs.setSpan(new ForegroundColorSpan(Color.RED), 0, detailFlg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbs.setSpan(new RelativeSizeSpan(0.9f), shopnameEnd, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbs.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(ActivityMedicineList.this, R.color.font_1)), shopnameStart, shopnameEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            baseViewHolder.setText(R.id.item_goods_omecol_goods_info, sbs);
//                    .setText(R.id.money, medicineListModel.getSALEPRICE());

            ImageView goodsImg = baseViewHolder.getView(R.id.item_goods_omecol_goods_img);
            if (!StringUtils.isEmpty(medicineListModel.getIMG_PATH())) {
                Picasso.with(ActivityMedicineList.this).load(Uri.parse(medicineListModel.getIMG_PATH())).into(goodsImg);
            }
            TextView goodsBizStat = baseViewHolder.getView(R.id.item_goods_omecol_goods_bizstat);
            if ("0".equals(medicineListModel.getBUSAVLID_FLG())) {
                goodsBizStat.setText(getString(R.string.biz_stat_on));
                goodsBizStat.setVisibility(View.GONE);
            } else if ("1".equals(medicineListModel.getBUSAVLID_FLG())) {
                goodsBizStat.setText(getString(R.string.biz_stat_off));
                goodsBizStat.setVisibility(View.VISIBLE);
            }

            /**
             * 活动小图标
             */
            baseViewHolder.setVisible(R.id.item_goods_huodong_te, false);
            baseViewHolder.setVisible(R.id.item_goods_huodong_zeng, false);

            baseViewHolder.setText(R.id.item_goods_omecol_goods_money, price);
            baseViewHolder.setVisible(R.id.item_goods_omecol_goods_money_old, false);
            //活动状况显示
            String tpF = medicineListModel.getTOP_FLG();
            if (GoodsModel.HUODONG_TEJIA.equals(tpF)) {
                baseViewHolder.setVisible(R.id.item_goods_huodong_te, true);
                baseViewHolder.setText(R.id.item_goods_omecol_goods_money, medicineListModel.getPROMPRICE());
                baseViewHolder.setText(R.id.item_goods_omecol_goods_money_old, price);
                baseViewHolder.setVisible(R.id.item_goods_omecol_goods_money_old, true);
            } else if (GoodsModel.HUODONG_JIFEN.equals(tpF)) {
                baseViewHolder.setVisible(R.id.item_goods_huodong_jifen, true);
                baseViewHolder.setText(R.id.item_goods_omecol_goods_money, "￥" + Utils.formatFee(medicineListModel.getSALEPRICE() + "") + " + " + Utils.formatFee(medicineListModel.getCOST_POINT()) + "积分");
                baseViewHolder.setText(R.id.item_goods_omecol_goods_money_old, price);
            } else if (Arrays.asList(GoodsModel.HUODONG_MANZENG).contains(medicineListModel.getTOP_FLG())) {
                baseViewHolder.setVisible(R.id.item_goods_huodong_zeng, true);
            }

        }
    }


    private void reset() {
        paixu1_tv.setTextColor(getResources().getColor(R.color.font_black2));
        paixu1_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_2));
        paixu2_tv.setTextColor(getResources().getColor(R.color.font_black2));
        paixu3_tv.setTextColor(getResources().getColor(R.color.font_black2));
        paixu3_iv.setImageDrawable(getResources().getDrawable(R.mipmap.paixu_3));
        paixu4_tv.setTextColor(getResources().getColor(R.color.font_black2));
//        isRise = true;
    }

    private void setLocation() {
        if (SingleCurrentUser.location != null) {
            tvLocation.setText(SingleCurrentUser.location.getAddrStr());
        }
    }
}
