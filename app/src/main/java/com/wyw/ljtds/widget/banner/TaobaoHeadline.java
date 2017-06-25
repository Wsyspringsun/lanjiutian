package com.wyw.ljtds.widget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wyw.ljtds.R;
import com.wyw.ljtds.model.HeadlineBean;

import java.util.List;

/**
 * Created by wuming on 16/10/16.
 */

public class TaobaoHeadline extends RelativeLayout {

    private static final String TAG = TaobaoHeadline.class.getSimpleName();
    private HeadlineClickListener listener;
    private ViewSwitcher viewSwitcher;
    private List<HeadlineBean> data;
    private RelativeLayout subView1, subView2;
    private int currentPosition = 0;
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            currentPosition++;
            final ViewHolder holder = (ViewHolder) ((currentPosition % 2) == 0 ? subView1.getTag() : subView2.getTag());
            holder.title_tv.setText(data.get(currentPosition % data.size()).getTitle());
            viewSwitcher.setDisplayedChild(currentPosition % 2);
            postDelayed(runnable, 4000);
        }
    };
    private OnClickListener headlineItemClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onHeadlineClick(data.get(currentPosition % data.size()));
            }
        }
    };

    public TaobaoHeadline(Context context) {
        this(context, null);
    }

    public TaobaoHeadline(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.broadcast_headline_layout_home, this, true);
        viewSwitcher = (ViewSwitcher) rootView.findViewById(R.id.taobao_headline_viewswitcher);
        if (subView1 == null) {
            subView1 = (RelativeLayout) viewSwitcher.findViewById(R.id.subView1);
            final ViewHolder holder = new ViewHolder();
            holder.title_tv = (TextView) subView1.findViewById(R.id.headline_title_tv);
            holder.title_tv.setText(data.get(0).getContent());
            subView1.setTag(holder);
            subView1.setOnClickListener(headlineItemClickListener);
        }
        if (subView2 == null) {
            subView2 = (RelativeLayout) viewSwitcher.findViewById(R.id.subView2);
            final ViewHolder holder = new ViewHolder();
            holder.title_tv = (TextView) subView2.findViewById(R.id.headline_title_tv);
            subView2.setTag(holder);
            subView2.setOnClickListener(headlineItemClickListener);
        }
        findViewById(R.id.taobao_headline_more_tv).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onMoreClick();
                }
            }
        });
        viewSwitcher.setDisplayedChild(0);
        //进入动画
        viewSwitcher.setInAnimation(getContext(), R.anim.headline_in);
        //退出动画
        viewSwitcher.setOutAnimation(getContext(), R.anim.headline_out);
        if (data.size() != 1) {
            post(runnable);
        }
    }

    private void initView1() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.broadcast_headline_layout_find, this, true);
        viewSwitcher = (ViewSwitcher) rootView.findViewById(R.id.taobao_headline_viewswitcher);
        if (subView1 == null) {
            subView1 = (RelativeLayout) viewSwitcher.findViewById(R.id.subView1);
            final ViewHolder holder = new ViewHolder();
            holder.title_tv = (TextView) subView1.findViewById(R.id.headline_title_tv);
            holder.title_tv.setText(data.get(0).getTitle());
            holder.title_tv.setTextColor(getResources().getColor(R.color.font_black2));
//            holder.content_iv= (ImageView) subView1.findViewById(R.id.headline_title_iv);
//            holder.content_iv.setImageDrawable(getResources().getDrawable(R.mipmap.jiantou_you_hui));
            subView1.setTag(holder);
            subView1.setOnClickListener(headlineItemClickListener);
        }
        if (subView2 == null) {
            subView2 = (RelativeLayout) viewSwitcher.findViewById(R.id.subView2);
            final ViewHolder holder = new ViewHolder();
            holder.title_tv = (TextView) subView2.findViewById(R.id.headline_title_tv);
            holder.title_tv.setTextColor(getResources().getColor(R.color.font_black2));
//            holder.content_iv= (ImageView) subView2.findViewById(R.id.headline_title_iv);
//            holder.content_iv.setImageDrawable(getResources().getDrawable(R.mipmap.jiantou_you_hui));
            subView2.setTag(holder);
            subView2.setOnClickListener(headlineItemClickListener);
        }
//        findViewById(R.id.taobao_headline_more_tv).setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onMoreClick();
//                }
//            }
//        });
        viewSwitcher.setDisplayedChild(0);
        //进入动画
        viewSwitcher.setInAnimation(getContext(), R.anim.headline_in);
        //退出动画
        viewSwitcher.setOutAnimation(getContext(), R.anim.headline_out);
        if (data.size() != 1) {
            post(runnable);
        }
    }

    //配置滚动的数据   type是判断首页还是医药馆
    public void setData(List<HeadlineBean> data,int type) {
        this.data = data;
        if (type==0){
            initView();
        }else if(type==2){
            initView1();
        }
    }

    public void setHeadlineClickListener(HeadlineClickListener listener) {
        this.listener = listener;
    }

    public interface HeadlineClickListener {
        void onHeadlineClick(HeadlineBean bean);

        void onMoreClick();
    }

    private class ViewHolder {
        TextView title_tv;
        ImageView content_iv;
    }
}
