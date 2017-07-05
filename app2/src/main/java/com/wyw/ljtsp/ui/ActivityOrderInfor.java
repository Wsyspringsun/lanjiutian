package com.wyw.ljtsp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.wyw.ljtsp.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

@ContentView(R.layout.activity_order_info)
public class ActivityOrderInfor extends BaseActivitry {
    @ViewInject(R.id.recycler)
    private RecyclerView recyclerView;
    @ViewInject(R.id.header_title)
    private TextView title;

    private MyAdapter adapter;
    //无数据时的界面
    private View noData;
    private List<Integer> list ;


    @Event(value = {R.id.button1,R.id.button2,R.id.header_return})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:

                break;

            case R.id.button2:

                break;

            case R.id.header_return:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();

        title.setText(R.string.order_details);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( this);//必须有
        linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );//设置水平方向滑动
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager( linearLayoutManager );
        list=new ArrayList<>();
        for (int i=0;i<2;i++){
            list.add(i);
        }
        adapter=new MyAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    //商品adapter
    private class MyAdapter extends BaseQuickAdapter<Integer> {
        public MyAdapter(List<Integer> lists) {
            super( R.layout.item_oder_goods, lists );
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
