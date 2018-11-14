package com.wyw.ljtds.ui.user.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.model.UserWaitterModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

@ContentView(R.layout.activity_waitter)
public class ActivityWaitter extends BaseActivity {
    private static String TAG_REFEREEID = "com.wyw.ljtds.ui.user.manage.ActivityWaitter.TAG_REFEREEID";
    @ViewInject(R.id.fragment_toolbar_common_white_title)
    private TextView title;

    @ViewInject(R.id.activity_waitter_tv_userid)
    private TextView tvUserid;//工号
    @ViewInject(R.id.activity_waitter_tv_username)
    private TextView tvUsername;
    @ViewInject(R.id.activity_waitter_tv_tel)
    private TextView tvTel; //客服电话
    @ViewInject(R.id.activity_waitter_tv_logistics_company)
    private TextView tvLogisticsCompany;
    @ViewInject(R.id.activity_waitter_tv_org_principal)
    private TextView tvOrgPrincipal;//门店联系人
    @ViewInject(R.id.activity_waitter_tv_org_mobile)
    private TextView tvOrgMobile;
    @ViewInject(R.id.activity_waitter_tv_org_address)
    private TextView tvOrgAddress;
    @ViewInject(R.id.activity_waitter_tv_customer_mobile)
    private TextView tvCustomerMobile;

    private UserBiz userBiz;
    public UserWaitterModel userWaitter;

    @Event(value = {R.id.fragment_toolbar_common_white_return})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_toolbar_common_white_return:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.user_waitter);

        userBiz = UserBiz.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.log("getCustomerService..........");
        getCustomerService();
    }

    private void updView() {
        tvUserid.setText(userWaitter.getUSERID() + "");//工号
        tvUsername.setText(userWaitter.getUSERNAME() + "");
        tvTel.setText(userWaitter.getTEL() + ""); //客服电话
        tvLogisticsCompany.setText(userWaitter.getLOGISTICS_COMPANY() + "");
        tvOrgPrincipal.setText(userWaitter.getORG_PRINCIPAL() + "");//门店联系人
        tvOrgMobile.setText(userWaitter.getORG_MOBILE() + "");
        tvOrgAddress.setText(userWaitter.getORG_ADDRESS() + "");
        tvCustomerMobile.setText(userWaitter.getCUSTOMER_MOBILE() + "");
    }

    public static Intent getIntent(Context ctx, String refereeId) {
        Intent it = new Intent(ctx, ActivityWaitter.class);
        it.putExtra(TAG_REFEREEID, refereeId);
        return it;
    }

    public void getCustomerService() {
        setLoding(this, false);
        new BizDataAsyncTask<UserWaitterModel>() {

            @Override
            protected UserWaitterModel doExecute() throws ZYException, BizFailure {
                Utils.log("getCustomerService...request.....");
                String refereeId = getIntent().getStringExtra(TAG_REFEREEID);
                return userBiz.getCustomerService(refereeId);
            }

            @Override
            protected void onExecuteSucceeded(UserWaitterModel data) {
                closeLoding();
                Utils.log("getCustomerService...response.....");
                userWaitter = data;
                updView();
            }

            @Override
            protected void OnExecuteFailed() {
                Utils.log("getCustomerService...error.....");
                closeLoding();
            }
        }.execute();
    }
}
