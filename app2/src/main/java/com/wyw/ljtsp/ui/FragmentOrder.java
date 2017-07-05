package com.wyw.ljtsp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wyw.ljtsp.R;
import com.wyw.ljtsp.weidget.LazyLoadFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

@ContentView(R.layout.fragment_order)
public class FragmentOrder extends LazyLoadFragment {
    @ViewInject(R.id.recycler)
    private RecyclerView recyclerView;

    private MyAdapter adapter;
    //无数据时的界面
    private View noData;
    private List<Integer> list;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void lazyLoad() {
        recyclerView.setVisibility(View.VISIBLE);
        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());//必须有
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置水平方向滑动
        recyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        adapter = new MyAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent it=new Intent(getActivity(),ActivityOrderInfor.class);
                startActivity(it);
            }
        });
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
        list = null;
        recyclerView.setVisibility(View.GONE);
    }

    //商品adapter
    private class MyAdapter extends BaseQuickAdapter<Integer> {
        public MyAdapter(List<Integer> lists) {
            super(R.layout.item_oder, lists);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, Integer order) {
//            if (StringUtils.isEmpty( goods.getCOMMODITY_COLOR() )) {
//                baseViewHolder.setText( R.id.size, " 规格：" + goods.getCOMMODITY_SIZE() );
//            } else {
//                baseViewHolder.setText( R.id.size, "产地：" + goods.getCOMMODITY_COLOR() + " ;规格：" + goods.getCOMMODITY_SIZE() );
//            }
//            baseViewHolder.setText( R.id.title, StringUtils.deletaFirst( goods.getCOMMODITY_NAME() ) )
//                    .setText( R.id.money, "￥" + goods.getCOST_MONEY() )
//                    .setText( R.id.number, "X" + goods.getEXCHANGE_QUANLITY() );

//            SimpleDraweeView simpleDraweeView = baseViewHolder.getView( R.id.iv_adapter_list_pic );
//            if (!StringUtils.isEmpty( goods.getIMG_PATH() )) {
//                simpleDraweeView.setImageURI( Uri.parse( goods.getIMG_PATH() ) );
//            }
        }
    }
}
