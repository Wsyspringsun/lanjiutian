package com.wyw.ljtds.ui.cart;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.DataListAdapter;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.model.OrderTradeDto;
import com.wyw.ljtds.model.ShoppingCartGroupModel;
import com.wyw.ljtds.model.ShoppingCartMedicineModel;
import com.wyw.ljtds.model.ShoppingCartModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.ui.goods.ActivityGoodsSubmit;
import com.wyw.ljtds.ui.goods.ActivityLifeGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.goods.LifeShopActivity;
import com.wyw.ljtds.ui.goods.ShopActivity;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.NumberButton;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

@ContentView(R.layout.fragment_shopping_cart)
public class FragmentCart extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @ViewInject(R.id.fragment_cart_hader)
    RelativeLayout rlCartHeader;
    @ViewInject(R.id.exListView)
    private RecyclerView recyclerView;
    //    @ViewInject(R.id.all_chekbox)
//    private CheckBox cb_check_all;
    @ViewInject(R.id.tv_total_price)
    private TextView tv_total_price;
    @ViewInject(R.id.tv_go_to_pay)
    private TextView tv_go_to_pay;
    //    @ViewInject(R.id.header_back_img)
//    private ImageView back;
//    @ViewInject(R.id.header_title)
//    private TextView title;
    @ViewInject(R.id.fragment_user_nologin)
    private LinearLayout layoutNologin;
    @ViewInject(R.id.fragment_user_logined)
    LinearLayout layoutlogin;
    @ViewInject(R.id.fragment_cart_srf)
    private SwipeRefreshLayout srf;

    private int totalCount = 0;// 购买的商品总数量
    //无数据时的界面
    private View noData;

    private ShoppingcartGroupAdapter adapter;
    private int index = 0;
    //    private int x = 0;//购物车商品数量
    private ShoppingCartModel cartModel;
    private LinearLayoutManager llm;


    @Event(value = { /**R.id.all_chekbox,*/R.id.tv_go_to_pay, R.id.tv_delete})
    private void onclick(View view) {
        switch (view.getId()) {
//            case R.id.all_chekbox:
//                doCheckAll();
//                break;

            case R.id.tv_go_to_pay:
                doPay();
                break;

            case R.id.tv_delete:
                doDelete();
                break;
        }
    }

    /**
     * gorder page
     */
    private void doPay() {
        StringBuilder err = new StringBuilder("");
        OrderTradeDto order = info2Order(err);
        if (err.length() > 0) {
            ToastUtil.show(getActivity(), err.toString());
            return;
        }
        if (order == null) {
            ToastUtil.show(getActivity(), "生成订单失败");
            return;
        }
        Intent it = ActivityGoodsSubmit.getIntent(getActivity(), order);
        startActivity(it);
    }

    protected OrderTradeDto info2Order(StringBuilder err) {
        if (cartModel == null) return null;
        if (cartModel.getDETAILS() == null || cartModel.getDETAILS().size() <= 0) return null;

        //log first commodity is medicine or life goods, if medicine: first = sxljt
        String first = "";
        boolean checkno = false;
        List<ShoppingCartGroupModel> shops = cartModel.getDETAILS();
        List<OrderGroupDto> ogList = new ArrayList<>();
        for (ShoppingCartGroupModel shopItem : shops) {
            List<ShoppingCartMedicineModel> comms = shopItem.getDETAILS();
            if (comms == null || comms.size() <= 0) continue;
            List<OrderCommDto> oList = new ArrayList<>();
            for (ShoppingCartMedicineModel commItem : comms) {
                if (!commItem.checed) continue;
                if ("".equals(first)) {
                    first = commItem.getINS_USER_ID();
                } else {
                    if (!first.equals(commItem.getINS_USER_ID())) {
                        //cross life & medicine , return err;
                        err.append("请将生活馆和医药馆商品分开结算");
                        return null;
                    }
                }

                checkno = true;
                OrderCommDto o = new OrderCommDto();
                o.setCOMMODITY_ORDER_ID(commItem.getCOMMODITY_ORDER_ID());
                o.setEXCHANGE_QUANLITY(commItem.getEXCHANGE_QUANLITY());
                o.setCOMMODITY_COLOR(commItem.getCOMMODITY_COLOR());
                o.setCOMMODITY_SIZE(commItem.getCOMMODITY_SIZE());
                o.setCOMMODITY_ID(commItem.getCOMMODITY_ID());
                o.setCOMMODITY_NAME(commItem.getCOMMODITY_NAME());
                oList.add(o);
            }
            if (oList.size() <= 0) {
                //没有选中，几许遍历
                continue;
            }
            OrderGroupDto og = new OrderGroupDto();
            og.setDETAILS(oList);
            og.setLOGISTICS_COMPANY_ID(shopItem.getBUSNO());
            og.setLOGISTICS_COMPANY(shopItem.getBUSNAME());

            ogList.add(og);
        }
        if (!checkno) {
            err.append(getString(R.string.chk_valid_empty));
            return null;
        }

        OrderTradeDto ot = new OrderTradeDto();
        ot.setDETAILS(ogList);
        ot.setINS_USER_ID(first);
        ot.setLAT(SingleCurrentUser.location.getLatitude() + "");
        ot.setLNG(SingleCurrentUser.location.getLongitude() + "");
        String addrId = "";
        if (!StringUtils.isEmpty(SingleCurrentUser.location.getADDRESS_ID())) {
            addrId = SingleCurrentUser.location.getADDRESS_ID();
        }
        ot.setADDRESS_ID(addrId);

        return ot;
    }

    /**
     * 验证是否存在跨生活馆和医药馆
     *
     * @return
     */
    private String validCheck() {
        if (cartModel == null) return "";
        if (cartModel.getDETAILS() == null || cartModel.getDETAILS().size() <= 0) return "";
        boolean checkno = false;
        List<ShoppingCartGroupModel> shops = cartModel.getDETAILS();
        for (ShoppingCartGroupModel shopItem : shops) {
            List<ShoppingCartMedicineModel> comms = shopItem.getDETAILS();
            if (comms == null || comms.size() <= 0) continue;
            for (ShoppingCartMedicineModel commItem : comms) {
                if (commItem.checed) {
                    checkno = true;
                }
            }

        }
        if (checkno) return "";
        return getString(R.string.chk_valid_empty);
    }


    public static FragmentCart newInstance(String param1, String param2) {
        FragmentCart fragment = new FragmentCart();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() instanceof CartActivity) {
            rlCartHeader.setVisibility(View.GONE);
        } else {
            rlCartHeader.setVisibility(View.VISIBLE);
        }
        srf.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showCart();
                srf.setRefreshing(false);
            }
        });
        //添加登录事件
        layoutNologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityLogin.goLogin(getActivity());
            }
        });
        llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        noData = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
        adapter = new ShoppingcartGroupAdapter();
//        adapter.setEmptyView(noData);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        //判断是否登录
        if (!UserBiz.isLogined()) {
            layoutlogin.setVisibility(View.GONE);
            layoutNologin.setVisibility(View.VISIBLE);
            return;
        }
        layoutlogin.setVisibility(View.VISIBLE);
        layoutNologin.setVisibility(View.GONE);

        showCart();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //购物车隐藏时   清除数据   显现时重新加载数据

        if (hidden) {
        } else {
            if (AppConfig.currSel == 3) {
                //没有登录跳入登录界面
                if (!UserBiz.isLogined()) {
                    layoutlogin.setVisibility(View.GONE);
                    layoutNologin.setVisibility(View.VISIBLE);
                    return;
                }
                layoutlogin.setVisibility(View.VISIBLE);
                layoutNologin.setVisibility(View.GONE);
                showCart();
            }
        }
    }

    private class ShoppingcartGroupAdapter extends DataListAdapter<ShoppingCartGroupModel, ShoppingcartGroupHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_EMPTY) {
                return new EmptyViewHolder(noData);
            }
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_shopcart_group, parent, false);
            return new ShoppingcartGroupHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ShoppingcartGroupHolder) {
                ShoppingCartGroupModel group = list.get(position);
                ShoppingcartGroupHolder h = (ShoppingcartGroupHolder) holder;

                ImageView imgGroup = (ImageView) h.itemView.findViewById(R.id.iv_source_image);
                if (AppConfig.GROUP_LJT.equals(group.getINS_USER_ID())) {
                    imgGroup.setImageDrawable(ActivityCompat.getDrawable(getActivity(), R.drawable.ic_biaozhi));
                }
                h.group = group;
                h.tvName.setText(group.getBUSNAME());
                h.tvBaoyou.setText("满" + group.getBAOYOU() + "包邮");
                List<ShoppingCartMedicineModel> children = group.getDETAILS();
                if (children != null) {
                    boolean chk = true;
                    for (ShoppingCartMedicineModel goodsItem : children) {
                        if (!goodsItem.checed) {
                            //exist one not checked ,shop is not checked
                            chk = false;
                            break;
                        }
                    }
                    h.checkBox.setChecked(chk);

                }
                RecyclerView goods = h.rvGoods;
                goods.setLayoutManager(new LinearLayoutManager(getActivity()));
                goods.setItemAnimator(new DefaultItemAnimator());
                goods.setAdapter(new ShoppingcartCommAdapter(children));
            }

        }
    }

    private class ShoppingcartGroupHolder extends RecyclerView.ViewHolder {
        private ShoppingCartGroupModel group;
        public TextView tvName;
        public TextView tvBaoyou;
        public RecyclerView rvGoods;
        public final CheckBox checkBox;

        public ShoppingcartGroupHolder(View itemView) {
            super(itemView);

            Button btnAddMore = (Button) itemView.findViewById(R.id.item_shpcart_group_btn_buymore);
            btnAddMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = null;
                    if (AppConfig.GROUP_LJT.equals(group.getINS_USER_ID())) {
                        it = ShopActivity.getIntent(getActivity(), group.getBUSNO());
                    } else {
                        it = LifeShopActivity.getIntent(getActivity(), group.getBUSNO());
                    }
                    startActivity(it);
                }
            });
            checkBox = (CheckBox) itemView.findViewById(R.id.item_shopcart_group_chk);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check all children
                    List<ShoppingCartMedicineModel> children = group.getDETAILS();
                    if (children != null && children.size() > 0) {
                        for (ShoppingCartMedicineModel goodsItem : children) {
                            goodsItem.checed = checkBox.isChecked();
                        }
                        //refreata and view
                        calculate();
                    }
                }
            });


//            Picasso.with(getActivity()).load(Uri.parse(g))
            tvName = (TextView) itemView.findViewById(R.id.tv_source_name);
            tvBaoyou = (TextView) itemView.findViewById(R.id.item_shpcart_group_postage);
            rvGoods = (RecyclerView) itemView.findViewById(R.id.reclcyer);
//
        }
    }

    private class MyAdapter extends BaseQuickAdapter<ShoppingCartGroupModel> {

        public MyAdapter() {
            super(R.layout.item_shopcart_group, null);
        }

        @Override
        protected void convert(final BaseViewHolder baseViewHolder, final ShoppingCartGroupModel group) {
            Button btnAddMore = baseViewHolder.getView(R.id.item_shpcart_group_btn_buymore);
            btnAddMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(AppConfig.ERR_TAG, "group.getBUSNO():.........." + group.getBUSNO());
                    Intent it = ShopActivity.getIntent(getActivity(), group.getBUSNO());
                    startActivity(it);
                }
            });
            final CheckBox checkBox = baseViewHolder.getView(R.id.item_shopcart_group_chk);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check all children
                    List<ShoppingCartMedicineModel> children = group.getDETAILS();
                    if (children != null && children.size() > 0) {
                        for (ShoppingCartMedicineModel goodsItem : children) {
                            goodsItem.checed = checkBox.isChecked();
                        }
                        //refreata and view
                        calculate();
                    }
                }
            });


            ImageView imgGroup = baseViewHolder.getView(R.id.iv_source_image);
            imgGroup.setImageDrawable(ActivityCompat.getDrawable(getActivity(), R.drawable.ic_launcher_sm));
//            Picasso.with(getActivity()).load(Uri.parse(g))
            baseViewHolder.setText(R.id.tv_source_name, group.getBUSNAME());
            baseViewHolder.setText(R.id.item_shpcart_group_postage, "满" + group.getBAOYOU() + "包邮");
            List<ShoppingCartMedicineModel> children = group.getDETAILS();
            if (children != null && children.size() > 0) {
                boolean chk = true;
                for (ShoppingCartMedicineModel goodsItem : children) {
                    if (!goodsItem.checed) {
                        //exist one not checked ,shop is not checked
                        chk = false;
                        break;
                    }
                }
                checkBox.setChecked(chk);
                RecyclerView goods = baseViewHolder.getView(R.id.reclcyer);
                goods.setLayoutManager(new LinearLayoutManager(getActivity()));
                goods.setItemAnimator(new DefaultItemAnimator());
                goods.setAdapter(new ShoppingcartCommAdapter(children));
            }
        }
    }

    public class CartProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener, NumberButton.OnNumberListener {
        private final NumberButton btnNum;
        private final TextView tvNum;
        ShoppingCartMedicineModel itemData;
        final TextView tvInfo;
        final CheckBox checkBox;
        final Button btnSave;
        final ImageView btnEdit;
        final LinearLayout rlEdit;
        final LinearLayout rlShow;
        ImageView simpleDraweeView;

        public CartProductHolder(View itemView) {
            super(itemView);

            checkBox = (CheckBox) itemView.findViewById(R.id.item_shopcart_product_chk);
            tvInfo = (TextView) itemView.findViewById(R.id.item_shopcart_product_tv_info);
            tvNum = (TextView) itemView.findViewById(R.id.item_shopcart_product_tv_num);
            simpleDraweeView = (ImageView) itemView.findViewById(R.id.iv_adapter_list_pic);
            btnEdit = (ImageView) itemView.findViewById(R.id.item_shopcart_product_btn_edit);
            btnSave = (Button) itemView.findViewById(R.id.item_shopcart_product_btn_save);
            rlEdit = (LinearLayout) itemView.findViewById(R.id.item_shopcart_product_rl_edit);
            rlShow = (LinearLayout) itemView.findViewById(R.id.item_shopcart_product_rl_show);
            rlShow.setVisibility(View.VISIBLE);
            rlEdit.setVisibility(View.GONE);

            btnNum = (NumberButton) itemView.findViewById(R.id.item_shopcart_product_btn_num);
            btnNum.setOnNumberListener(this);

            btnEdit.setOnClickListener(this);
            btnSave.setOnClickListener(this);
            checkBox.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_shopcart_product_btn_edit:
                    rlEdit.setVisibility(View.VISIBLE);
                    rlShow.setVisibility(View.GONE);
                    break;
                case R.id.item_shopcart_product_btn_save:
                    rlEdit.setVisibility(View.GONE);
                    rlShow.setVisibility(View.VISIBLE);

                    if (btnNum.getNumber() != itemData.getEXCHANGE_QUANLITY()) {
                        itemData.setEXCHANGE_QUANLITY(btnNum.getNumber());
                        calculate();
                        doUpdate();
                    }
                    break;
                case R.id.item_shopcart_product_chk:
                    itemData.checed = checkBox.isChecked();
                    calculate();
                    break;
                default:
                    if (rlShow.getVisibility() == View.VISIBLE) {
                        if (AppConfig.GROUP_LJT.equals(itemData.getINS_USER_ID())) {
                            Intent it = ActivityMedicinesInfo.getIntent(getActivity(), itemData.getCOMMODITY_ID(), itemData.getBUSNO());
                            startActivity(it);
                        } else {
                            Intent it = ActivityLifeGoodsInfo.getIntent(getActivity(), itemData.getCOMMODITY_ID());
                            startActivity(it);
                        }
                    }
                    break;
            }
        }

        @Override
        public void OnNumberChange(int num) {
//            itemData.setEXCHANGE_QUANLITY(num);
        }
    }

    private void doUpdate() {
        if (cartModel == null) return;
        if (cartModel.getDETAILS() == null || cartModel.getDETAILS().size() <= 0) return;

        List<ShoppingCartMedicineModel> checkedList = new ArrayList<>();

        List<ShoppingCartGroupModel> shops = cartModel.getDETAILS();
        for (ShoppingCartGroupModel shopItem : shops) {
            List<ShoppingCartMedicineModel> comms = shopItem.getDETAILS();
            if (comms == null || comms.size() <= 0) continue;
            for (ShoppingCartMedicineModel commItem : comms) {
                if (commItem != null)
                    checkedList.add(commItem);
            }
        }
        updateCart(GsonUtils.List2Json(checkedList), "update");
    }

    private class ShoppingcartCommAdapter extends DataListAdapter<ShoppingCartMedicineModel, CartProductHolder> {
        public ShoppingcartCommAdapter(List<ShoppingCartMedicineModel> data) {
            this.list = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_EMPTY) return new EmptyViewHolder(noData);
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.item_shopcart_product, parent, false);
            return new CartProductHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CartProductHolder) {
                if (list == null || list.size() <= 0) return;
                CartProductHolder vh = (CartProductHolder) holder;
                ShoppingCartMedicineModel goods = list.get(position);

                vh.itemData = goods;

                String name = StringUtils.deletaFirst(goods.getWARENAME()) + "\n\n",
                        price = "￥" + Utils.formatFee(goods.getCOST_MONEY());

                String spec = "规格：" + goods.getCOMMODITY_SIZE() + "\n";
                if (!AppConfig.GROUP_LJT.equals(goods.getINS_USER_ID())) {
                    spec = getString(R.string.title_cat) + "：" + goods.getCOMMODITY_COLOR() + "," + spec;
                }

                StringBuilder sb = new StringBuilder().append(name).append(spec).append(price);
                String[] strArr = new String[]{name, spec, price};
                int start = 0;
                SpannableString sbs = new SpannableString(sb.toString());
                for (int i = 0; i < strArr.length; i++) {
                    if (i == 0) {
                        sbs.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getActivity(), R.color.font_1)), start, start + strArr[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        sbs.setSpan(new ForegroundColorSpan(ActivityCompat.getColor(getActivity(), R.color.font_3)), start, start + strArr[i].length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    start += strArr[i].length();
                }
                vh.tvInfo.setText(sbs);
                Picasso.with(getActivity()).load(Uri.parse(goods.getIMG_PATH())).into(vh.simpleDraweeView);
                vh.checkBox.setChecked(goods.checed);
                vh.btnNum.setCurrentNumber(goods.getEXCHANGE_QUANLITY());
                vh.tvNum.setText("x" + goods.getEXCHANGE_QUANLITY());
            }
        }
    }

    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中
     */
    public void doDelete() {
        AlertDialog alert;
        String err = validCheck();
        if (!"".equals(err)) {
            ToastUtil.show(getActivity(), err);
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
                if (cartModel == null) return;
                List<String> toBeDeleteProducts = new ArrayList<>();// 待删除的子元素列表
                List<ShoppingCartGroupModel> shops = cartModel.getDETAILS();
                for (ShoppingCartGroupModel shopItem : shops) {
                    List<ShoppingCartMedicineModel> comms = shopItem.getDETAILS();
                    if (comms == null || comms.size() <= 0) continue;
                    for (ShoppingCartMedicineModel commItem : comms) {
                        if (commItem != null && commItem.checed)
                            toBeDeleteProducts.add(commItem.getCOMMODITY_ORDER_ID());
                    }
                }
                delete(toBeDeleteProducts);
            }
        });
        alert.show();
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        if (cartModel == null) return;
        DecimalFormat df = new DecimalFormat("#.#");
        //cul total price
        BigDecimal totalPrice = new BigDecimal(0);
        List<ShoppingCartGroupModel> shops = cartModel.getDETAILS();
        if (shops != null) {
            for (int i = 0; i < shops.size(); i++) {
                ShoppingCartGroupModel shopItem = shops.get(i);
                List<ShoppingCartMedicineModel> goods = shopItem.getDETAILS();
                if (goods == null || goods.size() <= 0) continue;
                BigDecimal shopTotal = new BigDecimal(0);
                for (int j = 0; j < goods.size(); j++) {
                    ShoppingCartMedicineModel goodsItem = goods.get(j);
                    if (goodsItem == null) continue;

                    if (goodsItem.checed) {
                        BigDecimal goodsItemPrice = new BigDecimal(goodsItem.getCOST_MONEY());
                        BigDecimal goodsItemCount = new BigDecimal(goodsItem.getEXCHANGE_QUANLITY());
                        BigDecimal goodsItemCostMoneyAll = goodsItemPrice.multiply(goodsItemCount);
                        goodsItem.setCOST_MONEY_ALL(df.format(goodsItemCostMoneyAll));
                        shopTotal = shopTotal.add(goodsItemCostMoneyAll);
                    }
                }
                shopItem.shopCostMoneyAll = df.format(shopTotal);
                totalPrice = totalPrice.add(shopTotal);
            }
        } else {
            totalPrice = new BigDecimal(0);
        }
        cartModel.cartCostMoneyAll = df.format(totalPrice);

        bindData2View();
    }

    /**
     * 全选与反选
     */
    private void doCheckAll() {
/*        for (int i = 0; i < adapter.getItemCount(); i++) {
//            list.get( i ).setChoosed( cb_check_all.isChecked() );
            Group group = adapter.getItem(i);
            List<ShoppingCartMedicineModel> childs = group.getGoodses();
            for (int j = 0; j < childs.size(); j++) {
//                childs.get( j ).setChoosed( cb_check_all.isChecked() );
            }
        }
        adapter.notifyDataSetChanged();
        calculate();*/
    }

    private void rest() {
/*        for (int i = 0; i < adapter.getItemCount(); i++) {
            adapter.getData().get(i).setChoosed(false);
            Group group = adapter.getItem(i);
            List<ShoppingCartMedicineModel> childs = group.getGoodses();
            for (int j = 0; j < childs.size(); j++) {
                childs.get(j).setChoosed(false);
            }
        }*/

    }

    private void showCart() {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<ShoppingCartModel>() {

            @Override
            protected ShoppingCartModel doExecute() throws ZYException, BizFailure {
                Map<String, String> data = new HashMap<>();
                data.put("LAT", SingleCurrentUser.location.getLatitude() + "");
                data.put("LNG", SingleCurrentUser.location.getLongitude() + "");
                return GoodsBiz.showCart(GsonUtils.Bean2Json(data), "getShoppingCartList");
            }

            @Override
            protected void onExecuteSucceeded(ShoppingCartModel shoppingCartModel) {
                closeLoding();
                cartModel = shoppingCartModel;
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void bindData2View() {
        if (cartModel == null) {
            tv_total_price.setText("￥" + 0);
            adapter.list = null;
            adapter.notifyDataSetChanged();
            return;
        }
        tv_total_price.setText("￥" + cartModel.cartCostMoneyAll);
        adapter.list = cartModel.getDETAILS();
        adapter.notifyDataSetChanged();
    }

    private void delete(final List<String> toBeDeleteProducts) {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                String str = GsonUtils.Bean2Json(toBeDeleteProducts);
                Log.e(AppConfig.ERR_TAG, "delete str:" + str);
                return GoodsBiz.shoppingCart(str, "delete");
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();
                if ("ok".equals(s)) {
                    showCart();
                    //delete from client
                    /*List<ShoppingCartGroupModel> shops = cartModel.getDETAILS();
                    for (int i = shops.size() - 1; i >= 0; i--) {
                        ShoppingCartGroupModel shopItem = shops.get(i);
                        List<ShoppingCartMedicineModel> comms = shopItem.getDETAILS();
                        if (comms == null) {
                            shops.remove(shopItem);
                            continue;
                        }
                        for (int j = comms.size() - 1; j >= 0; j--) {
                            ShoppingCartMedicineModel commItem = comms.get(j);
//                            comms.remove(commItem);
                            if (toBeDeleteProducts.contains(commItem.getCOMMODITY_ORDER_ID())) {
                                comms.remove(commItem);
                            }
                        }
                        if (comms.size() <= 0) {
                            shops.remove(shopItem);
                        }
                    }
                    if (shops.size() <= 0) cartModel.setDETAILS(null);

                    calculate();
                    bindData2View();*/
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void updateCart(final String str, final String op) {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return GoodsBiz.shoppingCart(str, op);
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();
                if ("delete".equals(op) && s.equals("ok")) {
                    showCart();
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }
}

