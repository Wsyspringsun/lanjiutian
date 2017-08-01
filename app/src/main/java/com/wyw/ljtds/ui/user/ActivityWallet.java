package com.wyw.ljtds.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.WalletModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.wallet.BalanceActivity;
import com.wyw.ljtds.ui.user.wallet.PointRecordActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Administrator on 2017/2/12 0012.
 */

@ContentView(R.layout.activity_wallet)
public class ActivityWallet extends BaseActivity {
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.jifen)
    private TextView jifen;
    @ViewInject(R.id.money)
    private TextView money;

    @Event(value = {R.id.header_return, R.id.sel_wallet_jifen, R.id.sel_wallet_youhuiquan, R.id.sel_wallet_yue})
    private void onClick(View view) {
        Intent it = null;
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;
            case R.id.sel_wallet_jifen:
                it = new Intent(this, PointRecordActivity.class);
                startActivity(it);
                break;
            case R.id.sel_wallet_youhuiquan:
                break;
            case R.id.sel_wallet_yue:
                it = new Intent(this, BalanceActivity.class);
                startActivity(it);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("我的钱包");

//        getPoin();
    }

    private void getPoin() {
        setLoding(this, false);
        BizDataAsyncTask<List<WalletModel>> pointTask = new BizDataAsyncTask<List<WalletModel>>() {
            @Override
            protected List<WalletModel> doExecute() throws ZYException, BizFailure {
                return UserBiz.getWallet();
            }

            @Override
            protected void onExecuteSucceeded(List<WalletModel> model) {
                for (int i = 0; i < model.size(); i++) {
                    jifen.setText(model.get(i).getUSABLE_POINT() + "");
                    money.setText("￥" + model.get(i).getUSABLE_AMOUNT() + "");
                }

                closeLoding();
            }

            @Override
            protected void OnExecuteFailed() {

//                closeLoding();
            }
        };
        pointTask.execute();
    }
}
