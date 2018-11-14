package com.wyw.ljtmgr.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.biz.SimpleCommonCallback;
import com.wyw.ljtmgr.biz.UserBiz;
import com.wyw.ljtmgr.model.AwardListResponse;
import com.wyw.ljtmgr.model.AwardModel;
import com.wyw.ljtmgr.model.OrderDetailModel;
import com.wyw.ljtmgr.model.ServerResponse;

import org.w3c.dom.Text;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.zip.InflaterOutputStream;

import utils.DateUtils;

import static com.wyw.ljtmgr.R.id.orderId;

@ContentView(R.layout.activity_awardlist)
public class AwardListlActivity extends BaseActivity {
    private static final String TAG_OIDUSERID = "com.wyw.ljtmgr.ui.AwardListlActivity.TAG_OIDUSERID";
    @ViewInject(R.id.activity_awardlist_ryv_data)
    RecyclerView ryvData;
    RecyclerView.Adapter adpAwards;
    List<AwardModel> lsAwards;

    UserBiz userBiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

        userBiz = UserBiz.getInstance(this);
    }


    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ryvData.setLayoutManager(layoutManager);

        initToolbar();
    }

    protected void initToolbar() {
        TextView title = (TextView) findViewById(R.id.fragment_toolbar_common_title);
        title.setText(getTitle());
        LinearLayout llLeft = (LinearLayout) findViewById(R.id.fragment_toolbar_common_left);
//        LinearLayout llRight = (LinearLayout) findViewById(R.id.fragment_toolbar_common_right);
        for (int i = 0; i < llLeft.getChildCount(); i++) {
            ImageView imgItem = (ImageView) llLeft.getChildAt(i);
            imgItem.setOnClickListener(toolbarListener);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    /**
     * 加载要领取的奖品列表
     */
    private void loadData() {
        setLoding();
        String oidUserId = getIntent().getStringExtra(TAG_OIDUSERID);
        userBiz.loadAwards(oidUserId, new SimpleCommonCallback<AwardListResponse>(this) {

            @Override
            protected void handleResult(AwardListResponse result) {
                lsAwards = result.getListRet();
                bindData2View();
            }
        });
    }

    /**
     * 领取
     *
     * @param integralDrawLogId 数据的id
     */
    private void doGainAward(String integralDrawLogId) {
        setLoding();
        userBiz.doGainAward(integralDrawLogId, new SimpleCommonCallback<ServerResponse>(this) {
            @Override
            protected void handleResult(ServerResponse result) {
                Toast.makeText(AwardListlActivity.this, "领取成功", Toast.LENGTH_LONG).show();
                loadData();
            }
        });
    }

    private void bindData2View() {
        if (lsAwards == null) return;
        if (adpAwards == null) {
            adpAwards = new RecyclerView.Adapter<AwardItemHolder>() {

                @Override
                public AwardItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new AwardItemHolder(LayoutInflater.from(AwardListlActivity.this).inflate(R.layout.item_award, parent, false));
                }

                @Override
                public void onBindViewHolder(AwardItemHolder holder, int position) {
                    AwardModel item = lsAwards.get(position);
                    holder.itemData = item;

                    holder.tvName.setText("奖品：" + item.getAwardName());
                    holder.tvUserInfo.setText("中奖人：" + item.getUserName() + "  " + item.getUserMobile());
                    holder.tvBusName.setText("领奖门店：" + item.getBusname());
                    holder.tvInsDate.setText("抽奖时间：" + DateUtils.parseTime(item.getInsDate()));
                    holder.tvUpdDate.setText("领奖时间：" + DateUtils.parseTime(item.getUpdDate()));
                    switch (item.getValidFlg()) {
                        case "0":
                            holder.btnStat.setEnabled(true);
                            holder.btnStat.setText("兑换");
                            break;
                        case "1":
                            holder.btnStat.setEnabled(false);
                            holder.btnStat.setText("已领奖");
                            break;
                        case "2":
                            holder.btnStat.setEnabled(false);
                            holder.btnStat.setText("已过期");
                            break;

                    }
                }

                @Override
                public int getItemCount() {
                    return lsAwards.size();
                }
            };
            ryvData.setAdapter(adpAwards);
        }else{
            adpAwards.notifyDataSetChanged();
        }

    }

    public static Intent getIntent(Activity context, String oidUserId) {
        Intent it = new Intent(context, AwardListlActivity.class);
        it.putExtra(TAG_OIDUSERID, oidUserId);
        return it;
    }

    class AwardItemHolder extends RecyclerView.ViewHolder {

        public AwardModel itemData;
        TextView tvUserInfo;
        TextView tvName;
        TextView tvBusName;
        TextView tvInsDate;
        TextView tvUpdDate;
        Button btnStat;


        View.OnClickListener clickListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_award_stat:
                        if (v.isEnabled()) {
                            doGainAward(itemData.getIntegralDrawLogId());
                        }
                        break;
                }
            }
        };

        public AwardItemHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.item_award_name);
            tvUserInfo = (TextView) itemView.findViewById(R.id.item_award_userinfo);
            tvBusName = (TextView) itemView.findViewById(R.id.item_award_busname);
            tvInsDate = (TextView) itemView.findViewById(R.id.item_award_insdate);
            tvUpdDate = (TextView) itemView.findViewById(R.id.item_award_upddate);
            btnStat = (Button) itemView.findViewById(R.id.item_award_stat);
            btnStat.setOnClickListener(clickListner);

        }
    }
}
