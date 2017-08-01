package com.wyw.ljtds.ui.cart;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.mylhyl.circledialog.CircleDialog;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.BaseModelResult;
import com.wyw.ljtds.model.GoodSubmitModel1;
import com.wyw.ljtds.model.GoodSubmitModel2;
import com.wyw.ljtds.model.GoodSubmitModel3;
import com.wyw.ljtds.model.ShoppingCartModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.goods.ActivityGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityGoodsSubmit;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

@ContentView(R.layout.fragment_shopping_cart)
public class FragmentCart extends BaseFragment {
    @ViewInject(R.id.exListView)
    private RecyclerView recyclerView;
    //    @ViewInject(R.id.all_chekbox)
//    private CheckBox cb_check_all;
    @ViewInject(R.id.tv_total_price)
    private TextView tv_total_price;
    @ViewInject(R.id.tv_go_to_pay)
    private TextView tv_go_to_pay;
    @ViewInject(R.id.header_back_img)
    private ImageView back;
    @ViewInject(R.id.header_title)
    private TextView title;

    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    //无数据时的界面
    private View noData;

    private List<Group> list;
    private MyAdapter adapter;
    private int index = 0;
    //    private int x = 0;//购物车商品数量
    private List<Goods> select = new ArrayList<>();


    @Event(value = { /**R.id.all_chekbox,*/R.id.tv_go_to_pay, R.id.tv_delete})
    private void onclick(View view) {
        AlertDialog alert;
        switch (view.getId()) {
//            case R.id.all_chekbox:
//                doCheckAll();
//                break;

            case R.id.tv_go_to_pay:
                if (totalCount <= 0) {
                    ToastUtil.show(getActivity(), "请选择要支付的商品");
                    return;
                }

                GoodSubmitModel1 goodSubmitModel = new GoodSubmitModel1();
                List<GoodSubmitModel2> groupList = new ArrayList<>();

                for (int i = 0; i < adapter.getItemCount(); i++) {
                    Group group = adapter.getItem(i);
                    GoodSubmitModel2 goodSubmitModel2 = new GoodSubmitModel2();
                    List<Goods> childs = group.getGoodses();
                    List<GoodSubmitModel3> goodList = new ArrayList<>();
                    for (int j = 0; j < childs.size(); j++) {
                        Goods goods1 = childs.get(j);
                        GoodSubmitModel3 goods = new GoodSubmitModel3();
                        if (goods1.isChoosed()) {
                            goods.setEXCHANGE_QUANLITY(goods1.getNumber());
                            goods.setCOMMODITY_COLOR(goods1.getColor());
                            goods.setCOMMODITY_ID(goods1.getId());
                            goods.setCOMMODITY_NAME(goods1.getName());
                            goods.setCOMMODITY_SIZE(goods1.getSize());
                            goods.setCOMMODITY_ORDER_ID(goods1.getOrder_id());
//                            goods.setCOST_MONEY( goods1.getMoney()+"" );

                            goodList.add(goods);

                        }
                    }
                    goodSubmitModel2.setDETAILS(goodList);
                    goodSubmitModel2.setOID_GROUP_ID(group.getId());
                    goodSubmitModel2.setOID_GROUP_NAME(group.getName());
                    if (!goodSubmitModel2.getDETAILS().isEmpty()) {
                        groupList.add(goodSubmitModel2);
                    }


                }

                if (groupList.size() > 1) {
                    alert = new AlertDialog.Builder(getActivity()).create();
                    alert.setTitle(getResources().getString(R.string.alert_tishi));
                    alert.setMessage("暂不支持多店铺合并结算");
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                } else {
                    goodSubmitModel.setDETAILS(groupList);
                    Intent it = new Intent(getActivity(), ActivityGoodsSubmit.class);
                    it.putExtra("data", GsonUtils.Bean2Json(goodSubmitModel));
                    startActivity(it);
                }

                Log.e("*********", GsonUtils.Bean2Json(goodSubmitModel));


                break;

            case R.id.tv_delete:
                calculate();
                if (totalCount <= 0) {
                    ToastUtil.show(getActivity(), "请选择要移除的商品");
                    return;
                }
                alert = new AlertDialog.Builder(getActivity()).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDelete();
                    }
                });
                alert.show();
                break;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        x = 0;
//        back.setVisibility(View.GONE);
//        title.setText( "购物车（" + x + "）" );
        title.setText("购物车");

        showCart();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
//        recyclerView.addOnItemTouchListener( new OnItemChildClickListener() {
//            @Override
//            public void SimpleOnItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
//                switch (view.getId()){
//                    case R.id.determine_chekbox:
//                        CheckBox checkBox= (CheckBox) view.findViewById( R.id.determine_chekbox );
//                        adapter.getItem( i ).setChoosed( checkBox.isChecked() );
//                        Group group=adapter.getItem( i );
//                        for (int j = 0; j < adapter.getItem( i ).getGoodses().size(); j++) {
//                            group.getGoodses().get( j ).setChoosed( group.isChoosed );
//                        }
//                        Log.e( "******asdasd",group.isChoosed()+"" );
//                        adapter.notifyDataSetChanged();
//                        calculate();
//
//                        break;
//                }
//            }
//        } );
        adapter.setEmptyView(noData);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //购物车隐藏时   清除数据   显现时重新加载数据
        if (hidden) {
            adapter.getData().clear();
//            x = 0;
//            cb_check_all.setChecked( false );
            tv_total_price.setText("￥0.00");
            tv_go_to_pay.setText("结算(0)");
        } else {
            if (AppConfig.currSel == 3) {
                showCart();
            }
        }
    }

    private class MyAdapter extends BaseQuickAdapter<Group> {

        public MyAdapter() {
            super(R.layout.item_shopcart_group, list);
        }

        @Override
        protected void convert(final BaseViewHolder baseViewHolder, final Group group) {
            final CheckBox checkBox = baseViewHolder.getView(R.id.determine_chekbox);
            baseViewHolder.setText(R.id.tv_source_name, group.getName())
                    .setChecked(R.id.determine_chekbox, group.isChoosed())
                    .setOnClickListener(R.id.determine_chekbox, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            rest();
                            group.setChoosed(checkBox.isChecked());
                            for (int j = 0; j < group.getGoodses().size(); j++) {
                                group.getGoodses().get(j).setChoosed(group.isChoosed);
                            }
                            adapter.notifyDataSetChanged();
                            calculate();
                        }
                    });

            RecyclerView goods = baseViewHolder.getView(R.id.reclcyer);
            goods.setLayoutManager(new LinearLayoutManager(getActivity()));
            goods.setItemAnimator(new DefaultItemAnimator());
            goods.setAdapter(new MyAdapter1(group.getGoodses(), baseViewHolder.getPosition()));

        }
    }

    private class MyAdapter1 extends BaseQuickAdapter<Goods> {
        public MyAdapter1(List<Goods> list, int index) {
            super(R.layout.item_shopcart_product, list);
        }

        @Override
        protected void convert(final BaseViewHolder baseViewHolder, final Goods goods) {
            final CheckBox checkBox = baseViewHolder.getView(R.id.check_box);
            if (goods.getIns_user_id().equals("sxljt")) {
                baseViewHolder.setText(R.id.size, "规格：" + goods.getSize())
                        .setText(R.id.size1, "规格：" + goods.getSize());
            } else {
                baseViewHolder.setText(R.id.size, "颜色：" + goods.getColor() + "   规格：" + goods.getSize())
                        .setText(R.id.size1, "颜色：" + goods.getColor() + "   规格：" + goods.getSize());
            }
            baseViewHolder.setText(R.id.tv_intro, StringUtils.deletaFirst(goods.getName()))
                    .setText(R.id.tv_price, "￥" + goods.getMoney() + "")
                    .setText(R.id.tv_num, goods.getNumber() + "")
                    .setChecked(R.id.check_box, goods.isChoosed())
                    .setOnClickListener(R.id.check_box, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goods.setChoosed(checkBox.isChecked());
                            Log.e("inddddddd", adapter.getItem(index).isChoosed() + "");
                            adapter.notifyDataSetChanged();
                            calculate();
                        }
                    })
                    .setOnClickListener(R.id.all_ll, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!goods.isChoosed()) {
                                if (goods.getIns_user_id().equals("sxljt")) {
                                    Intent it = new Intent(getActivity(), ActivityMedicinesInfo.class);
                                    it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, goods.getId());
                                    startActivity(it);
                                } else {
                                    Intent it = new Intent(getActivity(), ActivityGoodsInfo.class);
                                    it.putExtra(AppConfig.IntentExtraKey.MEDICINE_INFO_ID, goods.getId());
                                    startActivity(it);
                                }
                            }
                        }
                    });

            //判断是否为处方药
            if (goods.getType().equals("1")) {
                baseViewHolder.setVisible(R.id.tv_add, false)
                        .setVisible(R.id.tv_reduce, false);
            } else {
                baseViewHolder.setOnClickListener(R.id.tv_add, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int num = goods.getNumber();
                        if (num < 999) {
                            num++;
                        } else {
                            num = 999;
                        }
                        goods.setNumber(num);
                        List<UpdateCart> list = new ArrayList<UpdateCart>();
                        UpdateCart updateCart = new UpdateCart(goods.getOrder_id(), String.valueOf(goods.getNumber()), new BigDecimal(goods.getMoney()));
                        list.add(updateCart);
                        updateCart(GsonUtils.List2Json(list), "update");

                        calculate();
                    }
                })
                        .setOnClickListener(R.id.tv_reduce, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int num = goods.getNumber();
                                if (num > 1) {
                                    num--;
                                } else {
                                    num = 1;
                                }

                                goods.setNumber(num);
                                List<UpdateCart> list = new ArrayList<UpdateCart>();
                                UpdateCart updateCart = new UpdateCart(goods.getOrder_id(), String.valueOf(goods.getNumber()), new BigDecimal(goods.getMoney()));
                                list.add(updateCart);
                                updateCart(GsonUtils.List2Json(list), "update");

                                calculate();
                            }
                        });
            }

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.iv_adapter_list_pic);
            if (StringUtils.isEmpty(goods.getImg())) {
                simpleDraweeView.setImageURI(Uri.parse(""));
            } else {
                simpleDraweeView.setImageURI(Uri.parse(goods.getImg()));
            }

        }
    }

    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中
     */
    public void doDelete() {
        List<String> toBeDeleteProducts = new ArrayList<String>();// 待删除的子元素列表
//        if (cb_check_all.isChecked()) {
//
//        } else {
//
//        }

        for (int i = 0; i < adapter.getItemCount(); i++) {
            Group group = adapter.getItem(i);
            List<Goods> childs = group.getGoodses();
            for (int j = 0; j < childs.size(); j++) {
                Goods goods = childs.get(j);
                if (goods.isChoosed()) {
                    toBeDeleteProducts.add(goods.getOrder_id());
                }
            }

        }
        String str = GsonUtils.List2Json(toBeDeleteProducts);
        updateCart(str, "delete");

        calculate();
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        DecimalFormat df = new DecimalFormat("######0.00");
        Log.e(AppConfig.ERR_TAG, "adapter.getItemCount:" + adapter.getItemCount());
        if (adapter.getData() == null)
            return;
        for (int i = 0; i < adapter.getData().size(); i++) {
            Log.e(AppConfig.ERR_TAG, "item:" + i + "/" + adapter.getData().size());
            Group group = adapter.getItem(i);
            List<Goods> childs = group.getGoodses();
            for (int j = 0; j < childs.size(); j++) {
                Goods goods = childs.get(j);
                if (goods.isChoosed()) {
                    totalCount++;
                    totalPrice += goods.getNumber() * goods.getMoney();
                }
            }
        }
        tv_total_price.setText("￥" + df.format(totalPrice));
        tv_go_to_pay.setText("结算(" + totalCount + ")");
    }

    /**
     * 全选与反选
     */
    private void doCheckAll() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
//            list.get( i ).setChoosed( cb_check_all.isChecked() );
            Group group = adapter.getItem(i);
            List<Goods> childs = group.getGoodses();
            for (int j = 0; j < childs.size(); j++) {
//                childs.get( j ).setChoosed( cb_check_all.isChecked() );
            }
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    private void rest() {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            adapter.getData().get(i).setChoosed(false);
            Group group = adapter.getItem(i);
            List<Goods> childs = group.getGoodses();
            for (int j = 0; j < childs.size(); j++) {
                childs.get(j).setChoosed(false);
            }
        }

    }

    private BizDataAsyncTask<String> showCartTask;

    private void showCart() {
        showCartTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                Page page = new Page("0", AppConfig.DEFAULT_PAGE_COUNT + "");
                String data = GsonUtils.Bean2Json(page);
                return GoodsBiz.showCart(data, "read");
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();

                if (s.contains("用户名密码") || s.contains("Token不一致")) {//未登陆跳转
                    Toast.makeText(MyApplication.getAppContext(), R.string.auth_expire,
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AppConfig.AppAction.ACTION_TOKEN_EXPIRE);
                    MyApplication.getAppContext().sendBroadcast(i);
                    PreferenceCache.putToken("");
                    PreferenceCache.putUsername("");
                    PreferenceCache.putPhoneNum("");
                }

                if (s.length() < 80) {//没有数据
                    adapter.getData().removeAll(list);
                    adapter.getEmptyView().setVisibility(View.VISIBLE);
//                    cb_check_all.setEnabled( false );//全选不可点击
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.getEmptyView().setVisibility(View.GONE);

//                    cb_check_all.setEnabled( true );

                    BaseModelResult baseModelResult = GsonUtils.Json2Bean(s, BaseModelResult.class);
                    ShoppingCartModel shoppingCartModel = GsonUtils.Json2Bean(baseModelResult.getResult().toString(), ShoppingCartModel.class);

                    list = new ArrayList<>();
                    List<Goods> itme;
                    for (int i = 0; i < shoppingCartModel.getDETAILS().size(); i++) {
                        Group group = new Group();
                        group.setName(shoppingCartModel.getDETAILS().get(i).getOID_GROUP_NAME());
                        group.setId(shoppingCartModel.getDETAILS().get(i).getOID_GROUP_ID());
                        group.setChoosed(false);
                        itme = new ArrayList<>();
                        List<ShoppingCartModel.Goods> lists = shoppingCartModel.getDETAILS().get(i).getDETAILS();
                        for (int j = 0; j < lists.size(); j++) {
                            Goods goods = new Goods();
                            goods.setNumber(lists.get(j).getEXCHANGE_QUANLITY());
                            goods.setId(lists.get(j).getCOMMODITY_ID());
                            goods.setSize(lists.get(j).getCOMMODITY_SIZE());
                            goods.setColor(lists.get(j).getCOMMODITY_COLOR());
                            goods.setMoney(lists.get(j).getCOST_MONEY());
                            goods.setName(lists.get(j).getCOMMODITY_NAME());
                            goods.setOrder_id(lists.get(j).getCOMMODITY_ORDER_ID());
                            goods.setImg(lists.get(j).getIMG_PATH());
                            goods.setType(lists.get(j).getPRESCRIPTION_FLG());
                            goods.setIns_user_id(lists.get(j).getINS_USER_ID());
                            goods.setGroup_name(lists.get(j).getGROUPNAME());
                            goods.setChoosed(false);
                            itme.add(goods);

//                            x++;
                        }

                        group.setGoodses(itme);
                        list.add(group);

                    }

                    adapter.setNewData(list);
//                    title.setText( "购物车（" + x + "）" );
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        setLoding(getActivity(), false);
        showCartTask.execute();
    }


    private BizDataAsyncTask<String> updateTask;

    private void updateCart(final String str, final String op) {
        updateTask = new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                Log.e("data", str);
                return GoodsBiz.shoppingCart(str, op);
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();
                if (s.equals("ok")) {
//                    x = 0;
//                    cb_check_all.setChecked( false );
                    tv_total_price.setText("￥0.00");
                    tv_go_to_pay.setText("结算(0)");
                    showCart();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        };
        setLoding(getActivity(), false);
        updateTask.execute();
    }


    private class Group {
        private boolean isChoosed;
        private String name;
        private String id;
        private List<Goods> goodses;

        public boolean isChoosed() {
            return isChoosed;
        }

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Goods> getGoodses() {
            return goodses;
        }

        public void setGoodses(List<Goods> goodses) {
            this.goodses = goodses;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    private class Goods {
        private String img;
        private boolean isChoosed;
        private String id;
        private String order_id;
        private String name;
        private String size;
        private String color;
        private double money;
        private int number;
        private String type;
        //判断医药馆还是生活馆
        private String Ins_user_id;
        private String group_name;

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getIns_user_id() {
            return Ins_user_id;
        }

        public void setIns_user_id(String ins_user_id) {
            Ins_user_id = ins_user_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public boolean isChoosed() {
            return isChoosed;
        }

        public void setChoosed(boolean choosed) {
            isChoosed = choosed;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }


    /**
     * 分页  购物车现在不用分页
     */
    private class Page {
        private String PAGEINDEX;
        private String PAGESIZE;

        public Page() {
        }

        public Page(String index, String size) {
            this.PAGEINDEX = index;
            this.PAGESIZE = size;
        }
    }


    /**
     * 更新购物车  传输指定格式的json
     */
    private class UpdateCart {
        private String COMMODITY_ORDER_ID;
        private String EXCHANGE_QUANLITY;
        private BigDecimal COST_MONEY;

        public UpdateCart() {

        }

        public UpdateCart(String id, String number, BigDecimal money) {
            this.COMMODITY_ORDER_ID = id;
            this.EXCHANGE_QUANLITY = number;
            this.COST_MONEY = money;
        }
    }
}
