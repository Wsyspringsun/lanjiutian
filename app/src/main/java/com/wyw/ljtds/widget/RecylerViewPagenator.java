package com.wyw.ljtds.widget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by wsy on 2018/11/20.
 * 给RecylerView 添加分页功能
 */

public class RecylerViewPagenator {
    /**
     * 分页后处理
     */
    public interface NextPageListner {
        public void nextPage();
    }

    RecyclerView ryData;
    NextPageListner nextpageListner;

    private boolean end = false; //是否到达页尾

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    private boolean loading = false; //是否正在加载
    private int page = 1; //页码

    public RecylerViewPagenator(RecyclerView rycv, NextPageListner np) {
        this.ryData = rycv;
        this.nextpageListner = np;

        ryData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter == null) return;
                LinearLayoutManager ll = (LinearLayoutManager) recyclerView.getLayoutManager();
                int cnt = adapter.getItemCount() - 1;
                int lastVisibleItem = ll.findLastVisibleItemPosition();
                if (!end && !loading && (lastVisibleItem >= cnt)) {
                    page = page + 1;
                    if (nextpageListner != null)
                        nextpageListner.nextPage();
                }
            }
        });
    }
}
