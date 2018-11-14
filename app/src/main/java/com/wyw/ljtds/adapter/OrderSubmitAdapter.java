package com.wyw.ljtds.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.CreatOrderModel;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.OrderGroupDto;
import com.wyw.ljtds.model.OrderTrade;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;
import com.wyw.ljtds.widget.MyCallback;
import com.wyw.ljtds.widget.dialog.DiscountDialog;

import java.util.Arrays;
import java.util.List;

/**
 * 将订单数据绑定到订单界面视图
 * Created by wsy on 17-12-9.
 */
public class OrderSubmitAdapter extends RecyclerView.Adapter {
    private static final int UNKNOW = -1;
    private final Context context;
    private View.OnClickListener onClickListener;
    private MyCallback completeDiscountCallback;
    private LayoutInflater inflater;

    private CreatOrderModel orderModel; //订单数据

    private static final int ADDRESSSEL = 0; //选择地址
    private static final int GROUPORDER = 1; //订单数据
    private static final int FOOTER = 2; //底部派送方式和支付方式数据

    List<ItemTypeInfo> itemTypeList;

    public OrderSubmitAdapter(Context mContext, CreatOrderModel orderModel) {
        inflater = LayoutInflater.from(mContext);
        this.context = mContext;
        this.orderModel = orderModel;
    }

    @Override
    public int getItemCount() {
        //选择地址
        ItemTypeInfo addressselView = new ItemTypeInfo(ADDRESSSEL, 1);
        int orderSize = 0;
        if (orderModel != null && orderModel.getDETAILS() != null) {
            orderSize = orderModel.getDETAILS().size();
        }
//        订单数量
        ItemTypeInfo groupOrderView = new ItemTypeInfo(GROUPORDER, orderSize);
//        配送方式和支付方式
        ItemTypeInfo footerView = new ItemTypeInfo(FOOTER, 1);

        //各类型视图的有序列表
        ItemTypeInfo[] viArr = {addressselView, groupOrderView, footerView};
        itemTypeList = Arrays.asList(viArr);
        //计算每个类型视图起始位置
        for (int i = 0; i < itemTypeList.size(); i++) {
            ItemTypeInfo vi = itemTypeList.get(i);
            if (i == 0) {
                vi.setItemStartPos(0);
            } else {
                ItemTypeInfo preItem = itemTypeList.get(i - 1);
                vi.setItemStartPos(preItem.getItemStartPos() + preItem.getItemCount());
            }
        }


        ItemTypeInfo lastItem = itemTypeList.get(itemTypeList.size() - 1);
        int cnt = lastItem.itemStartPos + lastItem.itemCount;
        return cnt;
    }

    @Override
    public int getItemViewType(int position) {
        ItemTypeInfo itemTypeInfo = getItemTypeInfo(position);
        if (itemTypeInfo.itemType != UNKNOW)
            return itemTypeInfo.itemType;
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ADDRESSSEL:
                view = inflater.inflate(R.layout.item_order_submit_address, parent, false);
                return new AddressSelHolder(view);
            case GROUPORDER:
                view = inflater.inflate(R.layout.item_order_submit_group, parent, false);
                return new GroupOrderHolder(view);
            case FOOTER:
                view = inflater.inflate(R.layout.item_order_submit_bottom, parent, false);
                return new FooterHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemTypeInfo typeInfo = getItemTypeInfo(position);
        //数据在数据列表中实际索引
        int dataPos = position - typeInfo.itemStartPos;
//        Log.e(AppConfig.ERR_TAG, "onBindViewHolder:" + position);

        if (holder instanceof AddressSelHolder) {
            AddressSelHolder addrSelHolder = (AddressSelHolder) holder;
            AddressModel addr = orderModel.getUSER_ADDRESS();
            if (addr != null && addr.getADDRESS_ID() > 0) {
                StringBuilder err = new StringBuilder();
                MyLocation location = AddressModel.parseLocation(err, addr.getADDRESS_LOCATION());
                String addrLocation = "未知";
                if (err.length() <= 0) {
                    addrLocation = location.getAddrStr();
                }
                addrSelHolder.tvAddrInfo.setText(addr.getCONSIGNEE_NAME() + "      " + addr.getCONSIGNEE_MOBILE() + "\n" + addrLocation + addr.getCONSIGNEE_ADDRESS());
            } else {
                addrSelHolder.tvAddrInfo.setText("请选择地址");
            }
        } else if (holder instanceof GroupOrderHolder) {
            GroupOrderHolder groupOrderHolder = (GroupOrderHolder) holder;
            OrderGroupDto orderGroup = orderModel.getDETAILS().get(dataPos);
            if (AppConfig.GROUP_LJT.equals(orderGroup.getINS_USER_ID())) {
                groupOrderHolder.groupLogo.setImageDrawable(ActivityCompat.getDrawable(context, R.drawable.ic_biaozhi));
            } else {
                groupOrderHolder.groupLogo.setImageDrawable(ActivityCompat.getDrawable(context, R.drawable.ic_dianpu));
            }

            groupOrderHolder.data = orderGroup;
            groupOrderHolder.edRemark.setText(orderGroup.getGROUP_REMARKS());
            groupOrderHolder.tvGroupName.setText(orderGroup.getLOGISTICS_COMPANY());
            groupOrderHolder.ryGoods.setAdapter(new CommOrderAdapter(orderGroup.getDETAILS()));
            String xiaoji = Utils.formatFee(orderGroup.getGROUP_MONEY_ALL());
            if (orderGroup.getCOST_POINT() > 0) {
                xiaoji += " + " + orderGroup.getCOST_POINT() + "积分";
            }
            groupOrderHolder.tvGroupCost.setText("共" + orderGroup.getGROUP_EXCHANGE_QUANLITY() + "件商品  小计:￥" + xiaoji);
            groupOrderHolder.tvGroupArriveDate.setText(orderGroup.getDISTRIBUTION_DATE());
            //显示优惠状况
            /*if ("1".equals(orderGroup.getPOSTAGE_FLG()) || !StringUtils.isEmpty(orderGroup.getGROUP_DEDUCTIBLE_ALL_MONEY())) {
                String youhui = "已优惠:";
                if ("1".equals(orderGroup.getPOSTAGE_FLG())) {
                    youhui += "抵免邮费;";
                }
                if (!StringUtils.isEmpty(orderGroup.getGROUP_DEDUCTIBLE_ALL_MONEY())) {
                    youhui += orderGroup.getGROUP_DEDUCTIBLE_ALL_MONEY() + "元;";
                }
            }*/
            String youhui = orderGroup.getGROUP_REDUCTION_MONEY() ;
//            + " 随机减：" + orderGroup.getRAND_SUB_MONEY()

            groupOrderHolder.tvGroupDiscountContent.setText(youhui);
        } else if (holder instanceof FooterHolder) {
            FooterHolder footerHolder = (FooterHolder) holder;

            String strrlZhifu = "";
            if ("1".equals(orderModel.getDISTRIBUTION_MODE())) {
                strrlZhifu = "门店自取";
            } else {
                strrlZhifu = "送货上门";
            }

            if (OrderTrade.PAYMTD_MONEY.equals(orderModel.getPAYMENT_METHOD())) {
                strrlZhifu += "\n货到付款";
            } else {
                strrlZhifu += "\n在线支付";
            }
            footerHolder.tvZhifuContent.setText(strrlZhifu);

            if ("1".equals(orderModel.getINVOICE_FLG())) {
                footerHolder.tvInvoiceContent.setText(orderModel.getINVOICE_TITLE());
            } else {
                footerHolder.tvInvoiceContent.setText("不开发票");
            }

        }
    }

    private ItemTypeInfo getItemTypeInfo(int position) {
        for (int i = 0; i < itemTypeList.size(); i++) {
            ItemTypeInfo vti = itemTypeList.get(i);
            if (position >= vti.itemStartPos && position < (vti.itemStartPos + vti.itemCount)) {
                return vti;
            }
        }
        return new ItemTypeInfo(UNKNOW, 0);
    }


    public void setCompleteDiscountCallback(MyCallback completeDiscountCallback) {
        this.completeDiscountCallback = completeDiscountCallback;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 地址显示区
     */
    public class AddressSelHolder extends RecyclerView.ViewHolder {
        private TextView tvAddrInfo;

        public AddressSelHolder(View itemView) {
            super(itemView);
            tvAddrInfo = (TextView) itemView.findViewById(R.id.item_order_submit_address_info);
            itemView.setOnClickListener(onClickListener);
        }
    }

    /**
     * 商铺订单数据视图
     */
    public class GroupOrderHolder extends RecyclerView.ViewHolder {
        OrderGroupDto data = null; //店铺订单数据
        TextView tvGroupName = null;
        TextView tvGroupCost = null;
        TextView tvGroupArriveDate = null;//预计送达时间
        TextView tvGroupDiscountContent = null;//优惠内容
        RelativeLayout groupDiscount = null; //优惠栏
        ImageView groupLogo = null;

        RecyclerView ryGoods = null;//商品列表
        EditText edRemark = null; //买家留言

        public GroupOrderHolder(View itemView) {
            super(itemView);
            tvGroupName = (TextView) itemView.findViewById(R.id.item_order_submit_group_groupname);
            groupLogo = (ImageView) itemView.findViewById(R.id.item_order_submit_group_logo);
            tvGroupCost = (TextView) itemView.findViewById(R.id.item_order_submit_group_tv_cost);
            tvGroupArriveDate = (TextView) itemView.findViewById(R.id.item_order_submit_group_arrivedate);
            tvGroupDiscountContent = (TextView) itemView.findViewById(R.id.item_order_submit_group_discount_content);
            groupDiscount = (RelativeLayout) itemView.findViewById(R.id.item_order_submit_group_discount);
            groupDiscount.setOnClickListener(new View.OnClickListener() {
                //优惠项目弹出框
                public DiscountDialog dialog;

                private void showDiscountDialog() {
                    if (dialog == null) {
                        dialog = new DiscountDialog(context, data);
                        dialog.setCallback(completeDiscountCallback);
                    }
                    dialog.show();
                }

                @Override
                public void onClick(View v) {
                    showDiscountDialog();
                }
            });


            ryGoods = (RecyclerView) itemView.findViewById(R.id.item_order_submit_group_goods);
            ryGoods.setLayoutManager(new LinearLayoutManager(context));
            ryGoods.setItemAnimator(new DefaultItemAnimator());


            edRemark = (EditText) itemView.findViewById(R.id.item_order_submit_group_edremark);
            edRemark.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    data.setGROUP_REMARKS(s.toString());
                }
            });

        }
    }


    /**
     * 底部派送方式和支付方式数据
     */
    public class FooterHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rlInvoice = null;//发票信息栏
        private TextView tvInvoiceContent = null;
        private RelativeLayout rlZhifu = null;//配送方式和支付方式栏位
        private TextView tvZhifuContent = null;

        public FooterHolder(View itemView) {
            super(itemView);

            rlInvoice = (RelativeLayout) itemView.findViewById(R.id.item_order_submit_bottom_invoice);
            rlInvoice.setOnClickListener(onClickListener);
            tvInvoiceContent = (TextView) itemView.findViewById(R.id.item_order_submit_bottom_invoice_content);

            rlZhifu = (RelativeLayout) itemView.findViewById(R.id.item_order_submit_bottom_paymethod);
            rlZhifu.setOnClickListener(onClickListener);
            tvZhifuContent = (TextView) itemView.findViewById(R.id.item_order_submit_bottom_paymethod_content);

        }
    }

    class ItemTypeInfo {
        int itemType;
        int itemStartPos;
        int itemCount;

        private ItemTypeInfo() {
        }

        public ItemTypeInfo(int itemType, int itemCount) {
            this.itemType = itemType;
            this.itemCount = itemCount;
        }

        public int getItemStartPos() {
            return itemStartPos;
        }

        public void setItemStartPos(int itemStartPos) {
            this.itemStartPos = itemStartPos;
        }

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }


        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }
    }

}
