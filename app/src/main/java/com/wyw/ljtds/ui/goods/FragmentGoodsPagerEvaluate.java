package com.wyw.ljtds.ui.goods;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.wyw.ljtds.model.MedicineDetailsEvaluateModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.RecycleViewDivider;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
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

    private boolean end = false;
    private EvaluateAdapter adapter;
    private List<MedicineDetailsEvaluateModel> list_eva;
    private View noData;

    @ViewInject(R.id.tv_evaluate_cnt)
    TextView tvGoodComment;
    private int pageIdx = 0;

    public static FragmentGoodsPagerEvaluate newInstance(String commId) {
        FragmentGoodsPagerEvaluate frag = new FragmentGoodsPagerEvaluate();
        Bundle args = new Bundle();
        args.putString(ARG_COMMID, commId);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onResume() {
        super.onResume();

        refreshData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            refreshData();
        }
    }

    private void refreshData() {
        adapter = null;
        pageIdx = 0;
        end = false;
        geteva();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvGoodComment.setText("0");

        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) rylvData.getParent(), false);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动
        rylvData.setLayoutManager(linearLayoutManager);
        rylvData.setItemAnimator(new DefaultItemAnimator());
        rylvData.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.font_black2)));

        rylvData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (0 == newState) {
                    //0标识到达了
                    int fp = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    //完成滑动
                    if (fp == 1 && !end) {
                        pageIdx = pageIdx + 1;
                        geteva();
                    }
//                    Log.e(AppConfig.TAG_ERR, "newState:" + newState + " fp" + fp);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }

    private void bindData2View() {
        if (adapter == null) {
            adapter = new EvaluateAdapter(getActivity(), list_eva);
            rylvData.setAdapter(adapter);
        }
        if (list_eva == null) {
            end = true;
        }
        if (pageIdx <= 0) {
            adapter.setEvaList(list_eva);
        } else {
            adapter.getEvaList().addAll(list_eva);
        }
        adapter.notifyDataSetChanged();
        Log.e(AppConfig.ERR_TAG, "pageIdx:" + pageIdx + ",end:" + end + ",adapter:" + adapter);
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

    private void geteva() {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<List<MedicineDetailsEvaluateModel>>() {
            @Override
            protected List<MedicineDetailsEvaluateModel> doExecute() throws ZYException, BizFailure {
                String commId = getArguments().getString(ARG_COMMID);
                return GoodsBiz.getEvaluate(commId, pageIdx + "", AppConfig.DEFAULT_PAGE_COUNT + "");
            }

            @Override
            protected void onExecuteSucceeded(List<MedicineDetailsEvaluateModel> medicineDetailsEvaluateModels) {
                closeLoding();
                list_eva = medicineDetailsEvaluateModels;
                bindData2View();
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


    private class EvaluateViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTime;
        private TextView tvName;
        private TextView tvContent;
        SimpleDraweeView userImg;
        BGANinePhotoLayout mCurrentClickNpl;

        public EvaluateViewHolder(View itemView) {
            super(itemView);

            mCurrentClickNpl = (BGANinePhotoLayout) itemView.findViewById(R.id.item_moment_photos);
            tvTime = (TextView) itemView.findViewById(R.id.time);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvContent = (TextView) itemView.findViewById(R.id.context);
            userImg = (SimpleDraweeView) itemView.findViewById(R.id.user_img);

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

    private class EvaluateAdapter extends RecyclerView.Adapter<EvaluateViewHolder> {
        private Context context;
        private List<MedicineDetailsEvaluateModel> evaList;

        public List<MedicineDetailsEvaluateModel> getEvaList() {
            return evaList;
        }

        public void setEvaList(List<MedicineDetailsEvaluateModel> evaList) {
            this.evaList = evaList;
        }

        public EvaluateAdapter(Context context, List<MedicineDetailsEvaluateModel> evaList) {
            this.context = context;
            this.evaList = evaList;
        }

        @Override
        public EvaluateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View viewitem = LayoutInflater.from(context).inflate(R.layout.item_goods_evaluate, parent, false);
            return new EvaluateViewHolder(viewitem);
        }

        @Override
        public void onBindViewHolder(EvaluateViewHolder holder, int position) {
            MedicineDetailsEvaluateModel models = evaList.get(position);
            holder.tvTime.setText(models.getINS_DATE());
            String str = models.getMOBILE();
            holder.tvName.setText(str.substring(0, str.length() - (str.substring(3)).length()) + "****" + str.substring(7));
            holder.tvContent.setText(models.getEVALUATE_CONTGENT());
            holder.userImg.setImageURI(Uri.parse(models.getUSER_ICON_FILE_ID()));
            String[] imgStrs = models.getIMG();
            ArrayList<String> ls = new ArrayList<>(Arrays.asList(imgStrs));
            holder.mCurrentClickNpl.setData(ls);

        }

        @Override
        public int getItemCount() {
            if (evaList == null)
                return 0;
            return evaList.size();
        }
    }

    /*private class MyAdapter extends BaseQuickAdapter<MedicineDetailsEvaluateModel> {

        public MyAdapter() {
            super(R.layout.item_goods_evaluate, list_eva);
        }


        @Override
        protected void convert(BaseViewHolder baseViewHolder, MedicineDetailsEvaluateModel models) {
            String str = models.getMOBILE();
            Log.e(AppConfig.ERR_TAG, "str:" + str);

        }
    }*/
}
