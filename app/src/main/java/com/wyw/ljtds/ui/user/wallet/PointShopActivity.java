package com.wyw.ljtds.ui.user.wallet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.Ticket;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.ActivityMessage;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;

import org.json.JSONException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * create by wsy on 2017-07-28
 */
@ContentView(R.layout.activity_point_shop)
public class PointShopActivity extends BaseActivity {
    @ViewInject(R.id.activity_fragment_title)
    private TextView tvTitle;
    @ViewInject(R.id.activity_point_shop_pointnum)
    private TextView tvPointnum;

    @ViewInject(R.id.activity_point_shop_goods)
    private RecyclerView ryvShopGoods;


    private RecyclerView.Adapter ryvShopGoodsAdapter;


    UserBiz userBiz;

    List<Ticket> couponListList;
    String pointStr;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Event(value = {R.id.back})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userBiz = UserBiz.getInstance(this);

        tvTitle.setText(getTitle());


        LinearLayoutManager lineartayoutManger = new LinearLayoutManager(this);
        ryvShopGoods.setLayoutManager(lineartayoutManger);
        ryvShopGoods.setItemAnimator(new DefaultItemAnimator());

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void loadExchangeIndex() {
        setLoding(this, false);
        new BizDataAsyncTask<String>() {

            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return userBiz.loadExchangeIndex();
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();
                try {
                    Utils.log("loadExchangeIndex:" + s);
                    JsonElement el = Utils.parseResponse(s);
                    JsonObject json = el.getAsJsonObject();

                    JsonElement couponListEl = json.get("couponList");

                    Gson gson = new GsonBuilder().create();
                    TypeToken<List<Ticket>> tt = new TypeToken<List<Ticket>>() {
                    };
                    couponListList = gson.fromJson(couponListEl, tt.getType());

                    JsonElement pointEl = json.get("point");
                    pointStr = pointEl.getAsString();

                    bindData2View();
                } catch (BizFailure e) {
                    e.printStackTrace();
                    ToastUtil.show(PointShopActivity.this, e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.show(PointShopActivity.this, e.getMessage());
                }

            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    public void doExchangeCoupon(final String couponId, final String tempId, final String couponName) {
        setLoding(this, false);
        new BizDataAsyncTask<String>() {

            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return userBiz.exchangeCoupon(couponId, tempId, couponName);
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                closeLoding();
                try {
                    Utils.log("doExchangeCoupon:" + s);
                    Utils.parseResponse(s);
                    ToastUtil.show(PointShopActivity.this, "恭喜您，兑换成功！");
                    loadExchangeIndex();
                } catch (BizFailure e) {
                    Utils.log("doExchangeCoupon failure");
                    e.printStackTrace();
                    ToastUtil.show(PointShopActivity.this, e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void bindData2View() {
        if (couponListList == null) return;
        tvPointnum.setText("我的积分\n" + pointStr);
        if (ryvShopGoodsAdapter == null) {
            ryvShopGoodsAdapter = new RecyclerView.Adapter<PointShopGoodsItemHolder>() {
                @Override
                public PointShopGoodsItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = getLayoutInflater().inflate(R.layout.item_pointshopgoods, parent, false);
                    return new PointShopGoodsItemHolder(view);
                }

                @Override
                public void onBindViewHolder(PointShopGoodsItemHolder holder, int position) {
                    Ticket itemData = couponListList.get(position);
                    holder.itemData = itemData;
                    Picasso.with(PointShopActivity.this).load(AppConfig.IMAGE_PATH_LJT + itemData.getIMG_PATH()).placeholder(R.drawable.img_adv_zhanwei).into(holder.ivItem);
                }

                @Override
                public int getItemCount() {
                    if (couponListList == null)
                        return 0;
                    return couponListList.size();
                }
            };

            ryvShopGoods.setAdapter(ryvShopGoodsAdapter);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PointShop Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        loadExchangeIndex();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    private class PointShopGoodsItemHolder extends RecyclerView.ViewHolder {
//        TextView tvTitle;
//        TextView tvType;
//        TextView tvDesc;
//        TextView tvScop;
//        TextView tvGain;

        ImageView ivItem;

        Ticket itemData;

        View.OnClickListener onGainListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doExchangeCoupon(itemData.getRED_PACKET_ID(), itemData.getRED_PACKET_TEMPLET_ID(), itemData.getRED_PACKET_TEMPLET_NAME());
            }
        };

        public PointShopGoodsItemHolder(View itemView) {
            super(itemView);
            ivItem = (ImageView) itemView.findViewById(R.id.item_pointshopgoods_imgitem);
//            tvTitle = (TextView) itemView.findViewById(R.id.item_pointshopgoods_title);
//            tvType = (TextView) itemView.findViewById(R.id.item_pointshopgoods_type);
//            tvDesc = (TextView) itemView.findViewById(R.id.item_pointshopgoods_exchangedesc);
//            tvScop = (TextView) itemView.findViewById(R.id.item_pointshopgoods_scop);
//            tvGain = (TextView) itemView.findViewById(R.id.item_pointshopgoods_gain);

            itemView.setOnClickListener(onGainListener);
        }
    }

    public static Intent getIntent(Context ctx) {
        Intent it = new Intent(ctx, PointShopActivity.class);
        return it;
    }
}
