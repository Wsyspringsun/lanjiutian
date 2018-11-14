package com.wyw.ljtds.ui.user.address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.InputMethodUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import static com.wyw.ljtds.R.id.recyclerView;
import static com.wyw.ljtds.R.id.tv_nearest_keywords;

/**
 * Created by wsy on 2017/1/6 0006.
 * search address
 */
@ContentView(R.layout.activity_search_result)
public class AddressSearchActivity extends BaseActivity {
    public static final String TAG_SELECT_ADDR = "com.wyw.ljtds.ui.user.address.AddressSearchActivity.TAG_SELECT_ADDR";
    private static final String TAG_INITKEYWORD = "com.wyw.ljtds.ui.user.address.AddressSearchActivity.TAG_INITKEYWORD";
    @ViewInject(R.id.activity_search_result_tv_city)
    TextView tvCity;
    @ViewInject(R.id.activity_search_result_tv_cancel)
    TextView tvCancel;
    @ViewInject(R.id.activity_search_result_et_keyword)
    EditText etKeyword;
    @ViewInject(R.id.activity_search_result_ryv_content)
    RecyclerView ryvContent;

    private PoiSearch mPoiSearch;
    private SuggestionSearch mSuggestionSearch;


    private List<PoiInfo> rltData;

    //        2 创建在线建议查询监听者；
    OnGetSuggestionResultListener sugListener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {
//                Utils.log("no result");
                return;
                //未找到相关结果
            }

            List<SuggestionResult.SuggestionInfo> list = res.getAllSuggestions();
            if (list == null || list.size() <= 0) {
                return;
            }
            SuggestionResult.SuggestionInfo sInfo = list.get(0);

            laodData(sInfo.key);
            //获取在线建议检索结果
//            Utils.log("SuggestionResult:" + GsonUtils.Bean2Json(res));
        }
    };
    private View noData;
    private PoiDataAdapter adapter;

    @Event(value = {R.id.activity_search_result_tv_cancel})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.activity_search_result_tv_cancel:
                finish();
                break;
        }
    }

    public void sugLoadData() {
//        1 创建在线建议查询实例；
        mSuggestionSearch = SuggestionSearch.newInstance();

//        3 设置在线建议查询监听者；
        mSuggestionSearch.setOnGetSuggestionResultListener(sugListener);
        //        4 发起在线建议查询；
        // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(etKeyword.getText().toString())
                .city(tvCity.getText().toString()).citylimit(true));
//        5 释放在线建议查询实例；
        mSuggestionSearch.destroy();
    }

    private void laodData(String keyword) {
        //        1 创建POI检索实例
        mPoiSearch = PoiSearch.newInstance();
//        3 设置POI检索监听者；
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
//        4 发起检索请求；
        mPoiSearch.searchInCity((new PoiCitySearchOption())
                .city(tvCity.getText().toString())
                .keyword(keyword)
                .pageCapacity(20)
                .pageNum(0));
//        5 释放POI检索实例；
//        mPoiSearch.destroy();
//        POI周边检索
//        周边搜索是一个圆形范围，适用于以某个位置为中心点，自定义检索半径值，搜索某个位置附近的POI。
//        搜索方式如下：
//        mPoiSearch.searchNearby(new PoiNearbySearchOption()
//                .keyword("餐厅")
//                .sortType(PoiSortType.distance_from_near_to_far)
//                .location(center)
//                .radius(radius)
//                .pageNum(10));
//        POI区域检索（即矩形范围检索）
//        POI区域检索以“用户指定的左下角和右上角坐标的长方形区域”为检索范围的poi检索。
//        LatLng southwest = new LatLng(39.92235, 116.380338);
//        LatLng northeast = new LatLng(39.947246, 116.414977);
//        LatLngBounds searchbound = new LatLngBounds.Builder()
//                .include(southwest).include(northeast)
//                .build();
//
//        mPoiSearch.searchInBound(new PoiBoundSearchOption().bound(searchbound)
//                .keyword("餐厅"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        ryvContent.setLayoutManager(linearLayoutManager);
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) ryvContent.getParent(), false);

        adapter = new PoiDataAdapter();
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.setEmptyView(noData);
        ryvContent.setAdapter(adapter);
        ryvContent.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                PoiInfo itemData = adapter.getItem(i);
                Intent it = new Intent();
                it.putExtra(TAG_SELECT_ADDR, GsonUtils.Bean2Json(itemData));
                Log.e(AppConfig.ERR_TAG, "..Click...PoiInfo index-" + i + ":" + GsonUtils.Bean2Json(itemData));
                setResult(Activity.RESULT_OK, it);
                finish();
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });
        ryvContent.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodUtils.closeSoftKeyboard(AddressSearchActivity.this);
                return false;
            }
        });

        //文字改变
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sugLoadData();
            }
        });


        //默认数据
        String initKey = getIntent().getStringExtra(TAG_INITKEYWORD);
        if (!StringUtils.isEmpty(initKey)) {
            laodData(initKey);
        }
    }

    //        2 创建POI检索监听者；
    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {

        @Override
        public void onGetPoiResult(PoiResult poiResult) {
            rltData = poiResult.getAllPoi();
            bindData2Adapter();
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
//            Log.e(AppConfig.ERR_TAG, "poiDetailResult:" + GsonUtils.Bean2Json(poiDetailResult));
        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            //Log.e(AppConfig.ERR_TAG, "poiIndoorResult:" + GsonUtils.Bean2Json(poiIndoorResult));
        }
    };

    private void bindData2Adapter() {
        if (adapter == null) return;
        adapter.setNewData(rltData);
        adapter.notifyDataSetChanged();
    }

    public static Intent getIntent(Context context, String initkeyword) {
        Intent it = new Intent(context, AddressSearchActivity.class);
        it.putExtra(TAG_INITKEYWORD, initkeyword);
        return it;
    }

    private class PoiDataAdapter extends BaseQuickAdapter<PoiInfo> {
        public PoiDataAdapter() {
            super(R.layout.item_address_search, null);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, PoiInfo poiInfo) {
            String rlt = poiInfo.name + "\n" + poiInfo.address;
            String key = poiInfo.name;
//
            SpannableString spanRlt = new SpannableString(rlt);
            int startIdx = rlt.indexOf(key), len = key.length();
            if (startIdx < 0 || startIdx > rlt.length()) startIdx = 0;
            if (StringUtils.isEmpty(rlt) || StringUtils.isEmpty(key)) len = 0;
            spanRlt.setSpan(new ForegroundColorSpan(Color.RED), startIdx, startIdx + len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            baseViewHolder.setText(R.id.item_address_search_tv_content, spanRlt);
        }
    }
}
