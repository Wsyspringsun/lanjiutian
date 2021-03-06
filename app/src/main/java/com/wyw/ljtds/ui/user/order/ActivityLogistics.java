package com.wyw.ljtds.ui.user.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.LogisticsModel;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.OrderCommDto;
import com.wyw.ljtds.model.OrderTrade;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.DateUtils;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.model.LogisticsData;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.logistics.StepView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2017/1/24 0024.
 */

@ContentView(R.layout.activity_logistics)
public class ActivityLogistics extends BaseActivity {
    @ViewInject(R.id.header_return_text)
    private TextView title;
    @ViewInject(R.id.stepView)
    private StepView stepView;
    @ViewInject(R.id.zhuangtai)
    private TextView style;
    @ViewInject(R.id.wuliu)
    private TextView wuliu;
    @ViewInject(R.id.dingdan)
    private TextView dingdan;
    @ViewInject(R.id.reclcyer)
    private RecyclerView recyclerView;
    @ViewInject(R.id.activity_logistic_info)
    private RelativeLayout logisticInfo;


    private List<OrderCommDto> list;
    List<LogisticsData> logisticsDataList;
    private MyAdapter adapter;
    private String phones = "";


    private static final String PATTERN_PHONE = "((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}";
    private static final String SCHEME_TEL = "tel:";
    private LogisticsModel logiModel;


    @Event(value = {R.id.header_return})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("物流信息");

        setLoding(this, false);
        getLogis(getIntent().getStringExtra("order_id"));


        adapter = new MyAdapter();
        View noData = this.getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
        adapter.setEmptyView(noData);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        // 设置view的绑定监听
        stepView.setBindViewListener(new StepView.BindViewListener() {
            @Override
            public void onBindView(TextView itemMsg, TextView itemDate, Object data) {
                LogisticsData sid = (LogisticsData) data;
                itemMsg.setText(formatPhoneNumber(itemMsg, sid.getContext()));
                itemDate.setText(sid.getTime());
            }
        });


    }

    BizDataAsyncTask<LogisticsModel> logisticsTask;

    private void getLogis(final String data) {
        logisticsTask = new BizDataAsyncTask<LogisticsModel>() {
            @Override
            protected LogisticsModel doExecute() throws ZYException, BizFailure {
                return GoodsBiz.getLogistics(data, "orderLogistic");
            }

            @Override
            protected void onExecuteSucceeded(final LogisticsModel logisticsModel) {
                closeLoding();
                if (logisticsModel == null) return;
                logiModel = logisticsModel;
                if (!StringUtils.isEmpty(logisticsModel.getCOURIER())) {
                    logisticInfo.setVisibility(View.VISIBLE);
                    TextView tvCurior = (TextView) logisticInfo.findViewById(R.id.activity_logistic_tv_curior);
                    tvCurior.setText("快递员\n" + logisticsModel.getCOURIER());

                    LinearLayout telRegion = (LinearLayout) findViewById(R.id.activity_logistic_tel);

                    CuriorListener cl = new CuriorListener(logisticsModel.getCOURIER_MOBILE());
                    telRegion.setOnClickListener(cl);
                    LinearLayout traceRegion = (LinearLayout) findViewById(R.id.activity_logistic_trace);
                    traceRegion.setOnClickListener(cl);


                } else {
                    logisticInfo.setVisibility(View.GONE);
                }
                if (logisticsModel.getORDER() == null) {
//                    ToastUtil.show(ActivityLogistics.this,getResources().getString(R.string.logistic_null));
//                    finish();
                    return;
                }
                LinearLayout traceRegion = (LinearLayout) findViewById(R.id.activity_logistic_trace);
                if (traceRegion != null)
                    traceRegion.setVisibility(View.GONE);
                if ("C".equals(logisticsModel.getORDER().getSTATUS())) {
                    style.setText("物流状态：已发货");
                    if (traceRegion != null)
                        traceRegion.setVisibility(View.VISIBLE);
                } else if ("D".equals(logisticsModel.getORDER().getSTATUS()) || "S".equals(logisticsModel.getORDER().getSTATUS())) {
                    style.setText("物流状态：已送达");
                }
                wuliu.setText("物流单号：" + logisticsModel.getLOGISTICS_ORDER_ID());
                dingdan.setText("订单编号：" + logisticsModel.getCOMMODITY_ORDER_ID());

                logisticsDataList = new ArrayList<>();
                if (logisticsModel.getDETAILS() != null && !logisticsModel.getDETAILS().isEmpty()) {
                    for (int i = 0; i < logisticsModel.getDETAILS().size(); i++) {
                        String str = "";
                        LogisticsData logisticsData = new LogisticsData();
                        logisticsData.setTime(DateUtils.parseTime(logisticsModel.getDETAILS().get(i).getINS_DATE() + ""));
                        if (!StringUtils.isEmpty(logisticsModel.getDETAILS().get(i).getCOURIER())) {
                            str = "配送门店：" + logisticsModel.getDETAILS().get(i).getLOGISTICS_COMPANY() + "——配送员：" + logisticsModel.getDETAILS().get(i)
                                    .getCOURIER() + " " + logisticsModel.getDETAILS().get(i).getCOURIER_MOBILE() + " " + logisticsModel.getDETAILS().get(i)
                                    .getLOGISTICS_DETAIL();
                        } else {
                            str = "配送门店：" + logisticsModel.getDETAILS().get(i).getLOGISTICS_COMPANY() + "——" + logisticsModel.getDETAILS().get(i)
                                    .getLOGISTICS_DETAIL();
                        }

                        logisticsData.setContext(str);
                        logisticsDataList.add(logisticsData);
                    }

                    Log.e("logisticsDataList", logisticsDataList.size() + "");
                    stepView.setDatas(logisticsDataList);

                    list = logisticsModel.getORDER().getDETAILS();
                    adapter.addData(list);
                    adapter.notifyDataSetChanged();

                    phones = logisticsModel.getDETAILS().get(0).getCOURIER_MOBILE();
                }


            }

            @Override
            protected void OnExecuteFailed() {
                logisticsDataList = null;
                closeLoding();
            }
        };
        logisticsTask.execute();
    }

    //商品adapter
    private class MyAdapter extends BaseQuickAdapter<OrderCommDto> {
        public MyAdapter() {
            super(R.layout.item_order_submit_goods, list);
        }

        @Override
        protected void convert(BaseViewHolder baseViewHolder, OrderCommDto goods) {
            if (StringUtils.isEmpty(goods.getCOMMODITY_COLOR())) {
                baseViewHolder.setText(R.id.item_order_submit_goods_size, " 规格：" + goods.getCOMMODITY_SIZE());
            } else {
                baseViewHolder.setText(R.id.item_order_submit_goods_size, "产地：" + goods.getCOMMODITY_COLOR() + " ;规格：" + goods.getCOMMODITY_SIZE());
            }
            baseViewHolder.setText(R.id.item_order_submit_goods_title, StringUtils.deletaFirst(goods.getCOMMODITY_NAME()))
                    .setText(R.id.item_order_submit_goods_money, "￥" + goods.getCOST_MONEY())
                    .setText(R.id.item_order_submit_goods_number, "X" + goods.getEXCHANGE_QUANLITY());

            SimpleDraweeView simpleDraweeView = baseViewHolder.getView(R.id.item_order_submit_goods_pic);
            if (!StringUtils.isEmpty(goods.getIMG_PATH())) {
                simpleDraweeView.setImageURI(Uri.parse(goods.getIMG_PATH()));
            }
        }
    }


    /**
     * 格式化TextView的显示格式，识别手机号
     *
     * @param textView
     * @param source
     * @return
     */
    private SpannableStringBuilder formatPhoneNumber(TextView textView, String source) {
        // 若要部分 SpannableString 可点击，需要如下设置
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        // 将要格式化的 String 构建成一个 SpannableStringBuilder
        SpannableStringBuilder value = new SpannableStringBuilder(source);

        // 使用正则表达式匹配电话
        Linkify.addLinks(value, Pattern.compile(PATTERN_PHONE), SCHEME_TEL);

        // 获取上面到所有 addLinks 后的匹配部分(这里一个匹配项被封装成了一个 URLSpan 对象)
        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        for (final URLSpan urlSpan : urlSpans) {
            if (urlSpan.getURL().startsWith(SCHEME_TEL)) {
                int start = value.getSpanStart(urlSpan);
                int end = value.getSpanEnd(urlSpan);
                value.removeSpan(urlSpan);
                value.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        String phone = urlSpan.getURL().replace(SCHEME_TEL, "");
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLogistics.this);
                        builder.setMessage("是否拨打电话：" + phone);
                        builder.setNegativeButton("取消", null);
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phones));
                                //开启系统拨号器
                                startActivity(intent);
                            }
                        });
                        builder.create().show();
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.parseColor("#3f8de2"));
                        ds.setUnderlineText(true);
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return value;
    }

    class CuriorListener implements View.OnClickListener {
        private String mobile;

        public CuriorListener(String mobile) {
            this.mobile = mobile;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_logistic_tel:
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobile));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case R.id.activity_logistic_trace:
                    if (logiModel == null) return;
                    String stat = logiModel.getORDER().getGROUP_STATUS();
                    if (!OrderTrade.SHIPPED.equals(stat)) {
                    }
                    StringBuilder err = new StringBuilder();
                    MyLocation loc = AddressModel.parseLocation(err, logiModel.getORDER().getUSER_ADDRESS_LOCATION());
                    if (err.length() > 0) {
                        ToastUtil.show(ActivityLogistics.this, "目标地址格式错误");
                        return;
                    }
                    startActivity(LogisticTraceActivity.getIntent(ActivityLogistics.this, mobile, loc.getLatitude(), loc.getLongitude()));
                    break;
            }
        }
    }
}
