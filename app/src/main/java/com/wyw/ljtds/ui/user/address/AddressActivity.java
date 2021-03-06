package com.wyw.ljtds.ui.user.address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6 0006.
 */

@ContentView(R.layout.activity_address_list)
public class AddressActivity extends BaseActivity {
    public static final String TAG_CMD = "com.wyw.ljtds.ui.user.address.ActivityAddress.TAG_CMD";
    public static final String TAG_SELECTED_ADDRESS = "com.wyw.ljtds.ui.user.address.ActivityAddress.tag_selected_address";
    public static final String CMD_CREATE = "com.wyw.ljtds.ui.user.address.ActivityAddress.CMD_CREATE";


    @ViewInject(R.id.activity_address_list_rv_addrlist)
    private RecyclerView recyclerView;
    @ViewInject(R.id.activity_address_list_tv_tianjia)
    private TextView add;

    //无数据时的界面
    private View noData;
    private List<AddressModel> list;
    private AddressAdapter adapter;

    UserBiz bizUser;

    private void initIconBtnStat() {
        LinearLayout btnBack = (LinearLayout) findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressActivity.this.finish();
            }
        });
        TextView tvTitle = (TextView) findViewById(R.id.activity_fragment_title);
        tvTitle.setText(getTitle());
    }

    @Event(value = {R.id.activity_address_list_tv_tianjia, R.id.header_return})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.activity_address_list_tv_tianjia:
                Boolean isSel = AddressActivity.this.getIntent().getBooleanExtra(TAG_SELECTED_ADDRESS, false);
                if (isSel) {
                    it = new Intent();
                    it.putExtra(TAG_CMD, CMD_CREATE);
                    setResult(Activity.RESULT_OK, it);
                    finish();
                } else {
                    it = new Intent(this, ActivityAddressEdit.class);
                    it.putExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, 1);
                    startActivity(it);
                }
                break;
        }
    }

    public static Intent getIntent(Context ctx, Boolean isSel) {
        Intent it = new Intent(ctx, AddressActivity.class);
        it.putExtra(TAG_SELECTED_ADDRESS, isSel);
        return it;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bizUser = UserBiz.getInstance(this);
        initIconBtnStat();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有 linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动 recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!UserBiz.isLogined()) {
            startActivity(ActivityLogin.getIntent(this));
            finish();
            return;
        }
        getAddress();
    }

    private void getAddress() {
        setLoding(this, false);
        new BizDataAsyncTask<List<AddressModel>>() {
            @Override
            protected List<AddressModel> doExecute() throws ZYException, BizFailure {
                return bizUser.selectUserAddress();
            }

            @Override
            protected void onExecuteSucceeded(List<AddressModel> addressModels) {
                closeLoding();
                list = addressModels;
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void bindData2View() {
        adapter = new AddressAdapter(list);
        recyclerView.setAdapter(adapter);
    }


    private void delete(final String str) {
        setLoding(this, false);
        new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return bizUser.deleteUserAddress(str);
            }

            @Override
            protected void onExecuteSucceeded(Integer rlt) {
                closeLoding();
                if (1 == rlt) {
                    //删除成功
                    SingleCurrentUser.location = null;
                    ToastUtil.show(AddressActivity.this, getResources().getString(R.string.delete_succeed));
                    getAddress();
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }


    private void changeDefaultAddress(final String addrId) {
        setLoding(this, false);
        new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                return bizUser.changeDefaultAddress(addrId);
            }

            @Override
            protected void onExecuteSucceeded(Integer integer) {
                closeLoding();
                ToastUtil.show(AddressActivity.this, getResources().getString(R.string.update_succeed));
                getAddress();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    class AddressHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AddressModel data;
        TextView tvName;
        TextView tvPhone;
        TextView tvXiangxi;
        RadioButton chkIsDefault;
        LinearLayout llytIsCheck;
        LinearLayout llytBianji;
        LinearLayout llytShanchu;
        RelativeLayout rlOp;

        public AddressModel getData() {
            return data;
        }

        public void setData(AddressModel data) {
            this.data = data;
        }

        public AddressHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.name);
            tvPhone = (TextView) itemView.findViewById(R.id.phone);
            tvXiangxi = (TextView) itemView.findViewById(R.id.xiangxi);
            chkIsDefault = (RadioButton) itemView.findViewById(R.id.item_address_chk_ischeck);
            chkIsDefault.setEnabled(false);
            llytIsCheck = (LinearLayout) itemView.findViewById(R.id.item_address_llyt_ischeck);
            llytBianji = (LinearLayout) itemView.findViewById(R.id.bianji);
            llytShanchu = (LinearLayout) itemView.findViewById(R.id.shanchu);

            rlOp = (RelativeLayout) itemView.findViewById(R.id.item_address_rl_op);
            Boolean isSel = AddressActivity.this.getIntent().getBooleanExtra(TAG_SELECTED_ADDRESS, false);
            /*if (isSel) {
                rlOp.setVisibility(View.GONE);
            } else {
                rlOp.setVisibility(View.VISIBLE);
            }*/

            itemView.setOnClickListener(this);
            llytIsCheck.setOnClickListener(this);
            llytBianji.setOnClickListener(this);
            llytShanchu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent it;
            switch (v.getId()) {
                case R.id.item_address:
                    Boolean isSel = AddressActivity.this.getIntent().getBooleanExtra(TAG_SELECTED_ADDRESS, false);
                    if (!isSel)
                        return;
                    it = new Intent();
                    it.putExtra(TAG_SELECTED_ADDRESS, data);
                    AddressActivity.this.setResult(Activity.RESULT_OK, it);
                    finish();
                    break;
                case R.id.bianji:
                    it = ActivityAddressEdit.getIntent(AddressActivity.this, data);
                    startActivity(it);
                    break;
                case R.id.shanchu:
                    delete(data.getADDRESS_ID() + "");
                    break;
                case R.id.item_address_llyt_ischeck:
                    if (!chkIsDefault.isChecked()) {
                        changeDefaultAddress(data.getADDRESS_ID() + "");
                    }
                    break;
            }
        }
    }

    class AddressAdapter extends RecyclerView.Adapter<AddressHolder> {
        List<AddressModel> data;

        public AddressAdapter(List<AddressModel> data) {
            this.data = data;
        }

        @Override
        public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(AddressActivity.this);
            View view = inflater.inflate(R.layout.item_address, parent, false);
            AddressHolder holder = new AddressHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(AddressHolder holder, int position) {
            AddressModel itemData = data.get(position);
            holder.setData(itemData);
            holder.tvName.setText(itemData.getCONSIGNEE_NAME());
            holder.tvPhone.setText(itemData.getCONSIGNEE_MOBILE());
            holder.tvXiangxi.setText(itemData.getLOCATION() + " " + itemData.getCONSIGNEE_ADDRESS());
            holder.chkIsDefault.setChecked("0".equals(itemData.getDEFAULT_FLG()));
        }

        @Override
        public int getItemCount() {
            if (data == null) return 0;
            return data.size();
        }
    }

}
