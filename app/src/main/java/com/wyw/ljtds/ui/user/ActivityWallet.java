package com.wyw.ljtds.ui.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.model.WalletModel;
import com.wyw.ljtds.ui.base.BaseActivity;

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

    @Event(value = {R.id.header_return})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.header_return:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("我的钱包");

        setLoding(this,false);
        getPoin();
    }

    BizDataAsyncTask<List<WalletModel>> pointTask;
    private void getPoin(){
        pointTask=new BizDataAsyncTask<List<WalletModel>>() {
            @Override
            protected List<WalletModel> doExecute() throws ZYException, BizFailure {
                return UserBiz.getWallet();
            }

            @Override
            protected void onExecuteSucceeded(List<WalletModel> model) {
                for (int i=0;i<model.size();i++){
                    jifen.setText(model.get(i).getUSABLE_POINT()+"");
                    money.setText("￥"+model.get(i).getUSABLE_AMOUNT()+"");
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
