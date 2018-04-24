package com.wyw.ljtds.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.AbstractListViewAdapter;
import com.wyw.ljtds.adapter.AbstractViewHolder;
import com.wyw.ljtds.biz.biz.KeywordsStore;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;
import com.wyw.ljtds.ui.goods.ActivityMedicineList;
import com.wyw.ljtds.utils.InputMethodUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.widget.MyGridView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

@ContentView(R.layout.activity_search)
public class ActivitySearch extends BaseActivity {
    public static final String TAG_INIT_KEYWORD = "com.wyw.ljtds.ui.home.ActivitySearch.tag_init_keyword";
    @ViewInject(R.id.gridview)
    private MyGridView gridView;
    @ViewInject(R.id.mgv_nearest_keywords)
    private MyGridView gridViewNearestKey;
    @ViewInject(R.id.back)
    private ImageView back;
    @ViewInject(R.id.edHeader)
    private EditText edHeader;

    @Event(value = {R.id.back, R.id.search_img, R.id.search_tv})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.back:
                InputMethodUtils.closeSoftKeyboard(this);
                it = new Intent(this, MainActivity.class);
                it.putExtra(AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel);
                finish();
                startActivity(it);

                break;

            case R.id.search_img:
            case R.id.search_tv:
                goSearchRlt();

                break;
        }
    }

    private void goSearchRlt() {
        String keyword = edHeader.getText().toString().trim();
        KeywordsStore.add(keyword);
        Intent it = null;
        if (getIntent().getIntExtra("from", 0) == 1) {
            //搜索 商品
            it = new Intent(ActivitySearch.this, ActivityGoodsList.class);
            it.putExtra("search", keyword);
            it.putExtra("typeid", "");
        } else {
            //搜索 药品
            it = ActivityMedicineList.getIntent(ActivitySearch.this, "0", "", "", keyword);
        }
        startActivity(it);
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edHeader.setSelection(edHeader.getText().length());

        List<String> list = new ArrayList<>();
        if (getIntent().getIntExtra("from", 0) == 1) {
            list.add("女式拖鞋");
            list.add("蔬菜水果");
            list.add("防嗮霜");
            list.add("篮球");
            list.add("毛衣");
        } else {
            list.add("头痛");
            list.add("感冒");
            list.add("体质虚弱");
        }

        final AbstractListViewAdapter<String> adapter = new AbstractListViewAdapter<String>(this, R.layout.item_search_tuijian) {
            @Override
            public void initListViewItem(AbstractViewHolder viewHolder, String item) {
                TextView tv = viewHolder.getView(R.id.text_tuijian);
                tv.setText(item);
            }
        };
        adapter.addItems(list);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String k = adapter.getItem(position);
                edHeader.setText(k);
                goSearchRlt();
            }
        });
        gridView.setAdapter(adapter);


        if (getIntent().getIntExtra("from", 0) == 1) {
            KeywordsStore.initData(AppConfig.MEDICINE);
        } else {
            KeywordsStore.initData(AppConfig.LIFE);
        }
        List<String> listKeys = KeywordsStore.getList();

        String keyword = getIntent().getStringExtra(TAG_INIT_KEYWORD);
        if (!StringUtils.isEmpty(keyword)) {
            edHeader.setText(keyword);
            edHeader.setSelection(edHeader.getText().length());
        }
        final AbstractListViewAdapter<String> adapter2 = new AbstractListViewAdapter<String>(this, R.
                layout.item_search_tuijian) {
            @Override
            public void initListViewItem(AbstractViewHolder viewHolder, String item) {
                TextView tv = viewHolder.getView(R.id.text_tuijian);
                tv.setText(item);
            }
        };

        adapter2.addItems(listKeys);
        gridViewNearestKey.setAdapter(adapter2);
        View noData = findViewById(R.id.tv_nearest_keywords);
        if (listKeys == null || listKeys.size() <= 0) {
            noData.setVisibility(View.VISIBLE);
        } else {
            noData.setVisibility(View.GONE);
        }
        gridViewNearestKey.setEmptyView(noData);
        gridViewNearestKey.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String k = adapter2.getItem(position);
                edHeader.setText(k);
                edHeader.setSelection(edHeader.getText().length());
                goSearchRlt();
            }
        });

        edHeader.setImeOptions(EditorInfo.IME_ACTION_SEARCH);//设置搜索按钮
        edHeader.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
/*                    if (getIntent().getIntExtra("from", 0) == 1) {
                        Intent it = new Intent(ActivitySearch.this, ActivityGoodsList.class);
                        it.putExtra("search", edHeader.getText().toString().trim());
                        it.putExtra("typeid", "");
                        startActivity(it);
                    } else {
                        Intent it = new Intent(ActivitySearch.this, ActivityMedicineList.class);
                        it.putExtra("search", edHeader.getText().toString().trim());
                        it.putExtra("typeid", "");
                        startActivity(it);
                    }*/
                    goSearchRlt();
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //持久化查询记录
        KeywordsStore.doStore();
    }

    /**
     * @param context
     * @param src activity来源
     * @param initKeyword 初始的关键字
     * @return
     */
    public static Intent getIntent(Context context, int src, String initKeyword) {
        Intent it = new Intent(context, ActivitySearch.class);
//        it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        it.putExtra("from", src);
        it.putExtra(TAG_INIT_KEYWORD, initKeyword);
        return it;
    }


}
