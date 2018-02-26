package com.wyw.ljtds.ui.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.model.FavoriteModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.goods.ActivityGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityLifeGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.goods.FragmentGoodsDetails;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/2/12 0012.
 */

@ContentView(R.layout.activity_collect)
public class ActivityCollect extends BaseActivity {
    public static String TAG_MY = "com.wyw.ljtds.ui.user.ActivityCollect.tag_my";
    public static String TAG_MY_SHOUCANG = "tag_shoucang";
    public static String TAG_MY_ZUJI = "tag_zuji";

    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.jiantou)
    private ImageView jianTou;
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.header_edit)
    private TextView header_edit;
    @ViewInject(R.id.footer)
    private LinearLayout footer;

    private MyAdapter adapter;
    private List<FavoriteModel> list;
    private int index = 0;
    private List<String> date;

    private String tagMy;
    private View noData;

    @Event(value = {R.id.header_edit, R.id.header_return, R.id.shanchu, R.id.add_car})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_edit:
                if (index == 0) {
                    jianTou.setVisibility(View.GONE);
                    title.setText("编辑");
                    header_edit.setText("完成");
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) header_edit.getLayoutParams();
//                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);//0为true,1为维false
//                    header_edit.setLayoutParams(params);
                    footer.setVisibility(View.VISIBLE);
                    index = 1;
                } else {
                    jianTou.setVisibility(View.VISIBLE);
                    title.setText(R.string.user_shoucang);
                    header_edit.setText(R.string.bianji);
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) header_edit.getLayoutParams();
//                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
//                    params.addRule(RelativeLayout.LEFT_OF, R.id.header_image_right);//相当于xml中的to_left_of
//                    header_edit.setLayoutParams(params);
                    footer.setVisibility(View.GONE);
                    index = 0;
                }
                adapter.notifyDataSetChanged();
                break;

            case R.id.header_return:
                AppManager.getAppManager().finishActivity();
                break;

            case R.id.shanchu:
                date = new ArrayList<>();

                for (int i = 0; i < adapter.getData().size(); i++) {
                    if (adapter.getData().get(i).isCheck()) {
                        date.add(adapter.getData().get(i).getFavoritesGoodsId());
                    }
                }

                String[] ids = new String[date.size()];
                for (int j = 0; j < date.size(); j++) {
                    ids[j] = date.get(j);
                }
                deleteall d = new deleteall();
                d.setIds(ids);
                delete(GsonUtils.Bean2Json(d));
                break;

            case R.id.add_car:

                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        tagMy = intent.getStringExtra(TAG_MY);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);


        adapter = new MyAdapter();
        noData = this.getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
        adapter.setEmptyView(noData);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (index == 0) {
                    FavoriteModel dataItem = adapter.getItem(i);
                    Intent it;
                    if (dataItem.getGoodsFlg().equals("0")) {
//                            it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, adapter.getItem(i).getCommodityId());
                        it = ActivityMedicinesInfo.getIntent(ActivityCollect.this, dataItem.getCommodityId(),dataItem.getOidGroupId() );
                    } else {
                        it = ActivityLifeGoodsInfo.getIntent(ActivityCollect.this, dataItem.getCommodityId());
                    }
                    startActivity(it);

                    /*if (TAG_MY_ZUJI.equals(tagMy)) {

                    } else if (TAG_MY_SHOUCANG.equals(tagMy)) {
                        if (adapter.getItem(i).getGoodsFlg().equals("1")) {
                            Intent it = new Intent(ActivityCollect.this, ActivityMedicinesInfo.class);
//                            it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, adapter.getItem(i).getCommodityId());
                            startActivity(it);
                        } else {
                            Intent it = new Intent(ActivityCollect.this, ActivityGoodsInfo.class);
                            startActivity(it);
                        }
                    }*/


                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshData();
    }

    private void refreshData() {
        if (TAG_MY_SHOUCANG.equals(tagMy)) {
            header_edit.setVisibility(View.VISIBLE);
            title.setText(getResources().getString(R.string.user_shoucang));
            getFavorite();
        } else if (TAG_MY_ZUJI.equals(tagMy)) {
            header_edit.setVisibility(View.GONE);
            title.setText(getResources().getString(R.string.user_zuji));
            getHistory();
        }
    }

    BizDataAsyncTask<List<FavoriteModel>> favoriteTask;

    private void getFavorite() {
        setLoding(this, false);
        favoriteTask = new BizDataAsyncTask<List<FavoriteModel>>() {
            @Override
            protected List<FavoriteModel> doExecute() throws ZYException, BizFailure {
                return UserBiz.getFavorite();
            }

            @Override
            protected void onExecuteSucceeded(List<FavoriteModel> favoriteModels) {
                list = favoriteModels;
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCheck(false);
                }
                adapter.setNewData(list);
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };

        favoriteTask.execute();
    }

    private void getHistory() {
        setLoding(this, false);
        new BizDataAsyncTask<List<FavoriteModel>>() {
            @Override
            protected List<FavoriteModel> doExecute() throws ZYException, BizFailure {
                return UserBiz.getHistory(1);
            }

            @Override
            protected void onExecuteSucceeded(List<FavoriteModel> favoriteModels) {
                list = favoriteModels;
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setCheck(false);
                }
                adapter.setNewData(list);
                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }


    BizDataAsyncTask<Boolean> deleteTask;

    private void delete(final String ids) {
        deleteTask = new BizDataAsyncTask<Boolean>() {
            @Override
            protected Boolean doExecute() throws ZYException, BizFailure {
                Log.e("*****", ids);
                return UserBiz.mulDelFavoritesGoods(ids);
            }

            @Override
            protected void onExecuteSucceeded(Boolean isfl) {
                if (isfl) {
//                    finish();
//                    startActivity(new Intent(ActivityCollect.this, ActivityCollect.class));
                    refreshData();
                }
            }

            @Override
            protected void OnExecuteFailed() {
            }
        };
        deleteTask.execute();
    }


    private class MyAdapter extends BaseQuickAdapter<FavoriteModel> {
        public MyAdapter() {
            super(R.layout.item_user_collect, list);
        }

        @Override
        protected void convert(final BaseViewHolder baseViewHolder, final FavoriteModel messageModel) {
//            final CheckBox checkBox = baseViewHolder.getView( R.id.check );
//            adapter.getData().get( baseViewHolder.getPosition() ).setCheck( checkBox.isChecked() );
            if (index == 0) {
                baseViewHolder.setVisible(R.id.check, false)
                        .setChecked(R.id.check, false);
                for (int i = 0; i < adapter.getData().size(); i++) {
                    adapter.getData().get(i).setCheck(false);
                }
            } else {

                baseViewHolder.setVisible(R.id.check, true);
            }


            //加载图片
            SimpleDraweeView imageView = baseViewHolder.getView(R.id.goods_img);
            imageView.setImageURI(Uri.parse(messageModel.getImgPath()));

            String title = messageModel.getTitle();
            if (!StringUtils.isEmpty(title)) {
                if ("y".equalsIgnoreCase("" + title.charAt(0))) {
                    title = title.substring(1);
                }
            }
            baseViewHolder.setText(R.id.goods_title, title)
                    .setText(R.id.money, "￥" + Utils.formatFee(messageModel.getCostMoney() + ""))
                    .setOnCheckedChangeListener(R.id.check, new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            adapter.getData().get(baseViewHolder.getPosition()).setCheck(b);
                        }
                    });
        }
    }

    private class deleteall {
        private String[] ids;

        public String[] getIds() {
            return ids;
        }

        public void setIds(String[] ids) {
            this.ids = ids;
        }
    }

    public static Intent getIntent(Context context, String tag) {
        Intent it = new Intent(context, ActivityCollect.class);
        it.putExtra(TAG_MY, tag);
        return it;
    }
}
