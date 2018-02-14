package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.MedicineDetailsEvaluateModel;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.RecycleViewDivider;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * Created by Administrator on 2017/3/12 0012.
 */

@ContentView(R.layout.fragment_goods_evaluate)
public class FragmentGoodsPagerEvaluate extends BaseFragment {
    private static final String ARG_COMMID = "ARG_COMMID";
    @ViewInject(R.id.fragment_goods_evaluate_data)
    private RecyclerView rylvData;

    private int pageIndex = 1;
    private boolean end = false;
    private MyAdapter adapter;
    private List<MedicineDetailsEvaluateModel> list_eva;
    private View noData;

    @ViewInject(R.id.tv_evaluate_cnt)
    TextView tvGoodComment;

    public static FragmentGoodsPagerEvaluate newInstance(String commId) {
        FragmentGoodsPagerEvaluate frag = new FragmentGoodsPagerEvaluate();
        Bundle args = new Bundle();
        args.putString(ARG_COMMID, commId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            String commid = getArguments().getString(ARG_COMMID);
            geteva(false, true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvGoodComment.setText("0");

        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) rylvData.getParent(), false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        rylvData.setLayoutManager(linearLayoutManager);
        rylvData.setItemAnimator(new DefaultItemAnimator());
        rylvData.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.font_black2)));
    }

    public void bindData2View(List<MedicineDetailsEvaluateModel> medicineDetailsEvaluateModels) {
        if (medicineDetailsEvaluateModels == null) return;
        tvGoodComment.setText("(" + medicineDetailsEvaluateModels.size() + ")");
        if (adapter == null) {
            adapter = new MyAdapter();
            adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    geteva(true, false);
                }
            });
            rylvData.setAdapter(adapter);
        }
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        adapter.addData(medicineDetailsEvaluateModels);
        adapter.notifyDataSetChanged();
    }

    /*public void bindData2View(final CommodityDetailsModel model) {
//        tvGoodComment.setText("" + model.getEVALUATE_CNT());
//        geteva(model.getCommodityId(), true, true);
        adapter = new MyAdapter();
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                geteva(true, false);
            }
        });
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rylvData.setAdapter(adapter);
    }*/

    private void geteva(final boolean loadmore, final boolean refresh) {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<List<MedicineDetailsEvaluateModel>>() {
            @Override
            protected List<MedicineDetailsEvaluateModel> doExecute() throws ZYException, BizFailure {
                String classid = getArguments().getString(ARG_COMMID);
                if (refresh) {
                    return GoodsBiz.getEvaluate(classid, "0", AppConfig.DEFAULT_PAGE_COUNT + "");
                } else {
                    return GoodsBiz.getEvaluate(classid, pageIndex + "", AppConfig.DEFAULT_PAGE_COUNT + "");
                }
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineDetailsEvaluateModel> medicineDetailsEvaluateModels) {
                closeLoding();
                if (medicineDetailsEvaluateModels.size() < AppConfig.DEFAULT_PAGE_COUNT) {
                    end = true;
                    //可以加入emptyview
                    if (loadmore && medicineDetailsEvaluateModels.size() == 0) {
                        adapter.setEmptyView(noData);
                    }
                } else {
                    end = false;

                }

                list_eva = medicineDetailsEvaluateModels;
                if (loadmore) {
                    adapter.addData(list_eva);
                    adapter.notifyDataSetChanged();
                }
                if (refresh) {
                    adapter.setNewData(list_eva);
                    adapter.notifyDataSetChanged();
                }


                if (end) {
                    adapter.addFooterView(getFooterView());
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.removeAllFooterView();
                    adapter.notifyDataSetChanged();
                }


                pageIndex++;

                bindData2View(medicineDetailsEvaluateModels);
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private View getFooterView() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.main_end_view, (ViewGroup) rylvData.getParent(), false);
        return view;
    }

    private class MyAdapter extends BaseQuickAdapter<MedicineDetailsEvaluateModel> {

        public MyAdapter() {
            super(R.layout.item_goods_evaluate, list_eva);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, MedicineDetailsEvaluateModel models) {
            String str = models.getMOBILE();
            baseViewHolder.setText(R.id.time, DateUtils.parseTime(models.getINS_DATE() + ""))
                    .setText(R.id.name, str.substring(0, str.length() - (str.substring(3)).length()) + "****" + str.substring(7))
                    .setText(R.id.context, models.getEVALUATE_CONTGENT());

            SimpleDraweeView user_img = baseViewHolder.getView(R.id.user_img);
            if (StringUtils.isEmpty(models.getUSER_ICON_FILE_ID())) {
                user_img.setImageURI(Uri.parse(""));
            } else {
//                Utils.log(AppConfig.IMAGE_PATH_LJT + models.getUSER_ICON_FILE_ID());
                user_img.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + models.getUSER_ICON_FILE_ID()));
            }


            ArrayList arrayList = new ArrayList();
            if (!StringUtils.isEmpty(models.getIMG_PATH1())) {
                arrayList.add(AppConfig.IMAGE_PATH_LJT + models.getIMG_PATH1());
            }
            if (!StringUtils.isEmpty(models.getIMG_PATH2())) {
                arrayList.add(AppConfig.IMAGE_PATH_LJT + models.getIMG_PATH2());
            }
            if (!StringUtils.isEmpty(models.getIMG_PATH3())) {
                arrayList.add(AppConfig.IMAGE_PATH_LJT + models.getIMG_PATH3());
            }
            if (!StringUtils.isEmpty(models.getIMG_PATH4())) {
                arrayList.add(AppConfig.IMAGE_PATH_LJT + models.getIMG_PATH4());
            }
            if (!StringUtils.isEmpty(models.getIMG_PATH5())) {
                arrayList.add(AppConfig.IMAGE_PATH_LJT + models.getIMG_PATH5());
            }
            final BGANinePhotoLayout mCurrentClickNpl = baseViewHolder.getView(R.id.item_moment_photos);
            mCurrentClickNpl.setData(arrayList);
            mCurrentClickNpl.setDelegate(new BGANinePhotoLayout.Delegate() {
                @Override
                public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                    if (mCurrentClickNpl.getItemCount() == 1) {
                        // 预览单张图片
                        startActivity(BGAPhotoPreviewActivity.newIntent(getActivity(), null, mCurrentClickNpl.getCurrentClickItem()));
                    } else if (mCurrentClickNpl.getItemCount() > 1) {
                        // 预览多张图片
                        startActivity(BGAPhotoPreviewActivity.newIntent(getActivity(), null, mCurrentClickNpl.getData(), mCurrentClickNpl.getCurrentClickItemPosition()));
                    }
                }
            });

        }
    }
}
