package com.wyw.ljtds.ui.home;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.CommodityListModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.GsonUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wsy on 17-7-28.
 */

@ContentView(R.layout.fragment_xianshiqiang)
public class XianShiQiangFragment extends BaseFragment {
    private static final String ARG_CHANGCI = "ARG_CHANGCI";
    @ViewInject(R.id.fragment_xianshiqiang_tv_timershow)
    private TextView tvTimerShow;
    @ViewInject(R.id.fragment_xianshiqiang_rv_goods)
    private RecyclerView rvGoods;

    public static XianShiQiangFragment newInstance(int changci) {
        XianShiQiangFragment fragment = new XianShiQiangFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHANGCI, changci);
        fragment.setArguments(args);
        return fragment;
    }

    /*
     * 毫秒转化时分秒毫秒
     */
    public static String formatTime(Long ms) {
        Log.e(AppConfig.ERR_TAG, ".................." + ms);
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        }
        if (second > 0) {
            sb.append(second + "秒");
        }
//        if (milliSecond > 0) {
//            sb.append(milliSecond + "毫秒");
//        }
        return sb.toString();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        long span = 33685000;
        new CountDownTimer(span, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimerShow.setText("倒计时：" + formatTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                tvTimerShow.setEnabled(true);
                tvTimerShow.setText("开抢吧!!!");
            }
        }.start();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initRecyclerView() {
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 2);
        rvGoods.setLayoutManager(glm);

        List<CommodityListModel> lsGoods = new ArrayList<>();
        lsGoods.add(new CommodityListModel());
        lsGoods.add(new CommodityListModel());
        lsGoods.add(new CommodityListModel());
        lsGoods.add(new CommodityListModel());
        rvGoods.setAdapter(new XianShiQiangGoodsAdapter(lsGoods));
    }


    class XianShiQiangGoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CommodityListModel itemData;

        public XianShiQiangGoodsViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {
        }

        public CommodityListModel getItemData() {
            return itemData;
        }

        public void setItemData(CommodityListModel itemData) {
            this.itemData = itemData;
        }
    }

    class XianShiQiangGoodsAdapter extends RecyclerView.Adapter<XianShiQiangGoodsViewHolder> {
        private List<CommodityListModel> data;

        public XianShiQiangGoodsAdapter(List<CommodityListModel> data) {
            this.data = data;
        }

        @Override
        public XianShiQiangGoodsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.item_xianshiqiang_info, parent, false);
            XianShiQiangGoodsViewHolder vh = new XianShiQiangGoodsViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(XianShiQiangGoodsViewHolder holder, int position) {
            CommodityListModel itemData = data.get(position);
            holder.setItemData(itemData);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
