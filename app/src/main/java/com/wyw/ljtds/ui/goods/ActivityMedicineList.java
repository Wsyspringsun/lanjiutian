package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
    public static final String TAG_PARAM_KEYWORD = "com.wyw.ljtds.ui.goods.ActivityMedicineList.TAG_PARAM_KEYWORD";
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
    @ViewInject(R.id.xiaoxi)
    private ImageView image;
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

    @Event(value = {R.id.back, R.id.zxing, R.id.edHeader, R.id.xiaoxi, R.id.paixu1, R.id.paixu2, R.id.paixu3, R.id.paixu4})
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
                it = new Intent(this, ActivitySearch.class);
                it.putExtra("from", 0);
                it.putExtra(ActivitySearch.TAG_INIT_KEYWORD, search.getText().toString());
                finish();
                startActivity(it);
                break;

            case R.id.xiaoxi:
                it = new Intent(this, ActivityMessage.class);
                startActivity(it);
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

    public static Intent getIntent(Context context, String type, String topFlg, String classId, String keyword) {
        Intent it = new Intent(context, ActivityMedicineList.class);
        it.putExtra(TAG_PARAM_TYPE, type);
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

        back.setVisibility(View.VISIBLE);
        llMessage.setVisibility(View.GONE);
        zxing.setVisibility(View.GONE);

//        final GridLayoutManager glm = new GridLayoutManager(this, 1);
        final LinearLayoutManager glm = new LinearLayoutManager(this);
        setLocation();
        recyclerView.setLayoutManager(glm);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new SpaceItemDecoration(10));
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

    }

    @Override
    protected void onResume() {
        super.onResume();

        initParams();
        getlist();
    }

    private void getlist() {
        setLoding(this, false);
        new BizDataAsyncTask<List<MedicineListModel>>(this) {
            @Override
            protected List<MedicineListModel> doExecute() throws ZYException, BizFailure {
//                String lat = "" + SingleCurrentUser.defaultLat, lng = "" + SingleCurrentUser.defaultLng;
                Utils.log("getMedicineList:" + mtdtype + "-" + topFlg + "-" + classId + "-" + orderby + "-" + keyword + "-" + pageIndex + "" + "-" + AppConfig.DEFAULT_PAGE_COUNT + "" + "-" + lat + "-" + lng);
                return CategoryBiz.getMedicineList(mtdtype, topFlg, classId, orderby, keyword, pageIndex + "", AppConfig.DEFAULT_PAGE_COUNT + "", lat, lng);
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineListModel> medicineListModel) {
                Log.e(AppConfig.ERR_TAG, "get data success");
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


    private void findlist() {
        setLoding(this, false);
        new BizDataAsyncTask<List<MedicineListModel>>() {
            @Override
            protected List<MedicineListModel> doExecute() throws ZYException, BizFailure {
                Log.e(AppConfig.ERR_TAG, classId + "/" + orderby + pageIndex);
//                return CategoryBiz.findMedicineList(classId, orderby, pageIndex + "", "" + AppConfig.DEFAULT_PAGE_COUNT);
                return GsonUtils.Json2ArrayList("[ { \"WAREID\": \"005968\", \"WARENAME\": \"Y螺旋藻片（百合康）\", \"WARETYPE\": \"031001\", \"PRESCRIPTION_FLG\": \"3\", \"WARESPEC\": \"250mg*2000片\", \"WAREUNIT\": \"瓶\", \"PROD_ADD\": \"                                                            \", \"PRODUCER\": \"荣成百合生物技术有限公司\", \"SALEPRICE\": 318, \"MEMPRICE\": 0, \"PARITYWARE\": \",\", \"CLICK_NUM\": 630, \"SALE_NUM\": 0, \"VALID_FLG\": \"1\", \"UPD_DATE\": 1508801493000, \"PROMPRICE\": 74.4, \"MEMPROMPRICE\": 318, \"BEGINTIME\": 1505404800000, \"ENDTIME\": 1505491199000, \"TOP_FLG\": \"0\", \"COMMODITY_SORT\": 0, \"PROFIT_MARGIN\": 0.77, \"RECOMMEND_SORT\": 0, \"ADMIN_USER_NAME\": \"邱守哲\", \"SUMQTY\": 1, \"OID_GROUP_ID\": \"sxljt\", \"GROUP_NAME\": \"山西蓝九天药业连锁有限公司\", \"LOGISTICS_COMPANY\": \"沁水中村店\", \"LOGISTICS_COMPANY_ID\": \"021\", \"ORG_PRINCIPAL\": \"姚书庭\", \"ORG_MOBILE\": \"15713563517\", \"AREA\": \"6\", \"LNG\": 111.998017, \"LAT\": 35.570456, \"BUSAVLID_FLG\": \"0\", \"DEL_FLG\": \"0\", \"DISTANCE\": 11993438, \"DISTANCE_TEXT\": \"106.3公里\", \"DURATION_TEXT\": \"1444分钟\", \"QISONG\": \"￥58起送\", \"BAOYOU\": \"￥108包邮\", \"POSTAGE\": \"配送费￥0\" } ]", MedicineListModel.class);
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineListModel> medicineListModel) {
                Log.e(AppConfig.ERR_TAG, "find data success");
                closeLoding();
                Log.e(AppConfig.ERR_TAG, GsonUtils.Bean2Json(medicineListModel));
                if (medicineListModel == null || medicineListModel.size() <= 0) {
                    end = true;
                    adapter.setEmptyView(noData);
                }
                adapter.addData(medicineListModel);
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void OnExecuteFailed() {
                adapter.setEmptyView(noData);
                closeLoding();
            }
        }.execute();
    }

    private View getFooterView() {
        View view = getLayoutInflater().inflate(R.layout.main_end_view, (ViewGroup) recyclerView.getParent(), false);
        return view;
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
                    price = "￥" + medicineListModel.getSALEPRICE() + "\n";


            StringBuilder sbName = new StringBuilder().append(detailFlg).append(brand + " ").append(wareName).append(size);
            baseViewHolder.setText(R.id.item_goods_omecol_goods_name, sbName);

            StringBuilder sb = new StringBuilder()
                    .append(price).append(shopname).append(postage)
                    .append(distanceText)
                    .append(durationText)
                    .append(qisong).append(baoyou);
            int priceStart = sb.indexOf(price);
            int priceEnd = priceStart + price.length();
            int shopnameStart = sb.indexOf(shopname), shopnameEnd = shopnameStart + shopname.length();

            SpannableString sbs = new SpannableString(sb.toString());
//            sbs.setSpan(new ForegroundColorSpan(Color.RED), 0, detailFlg.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbs.setSpan(new RelativeSizeSpan(1.2f), priceStart, priceEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbs.setSpan(new RelativeSizeSpan(0.9f), shopnameEnd, sb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            sbs.setSpan(new ForegroundColorSpan(Color.RED), priceStart, priceEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
