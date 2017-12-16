package com.wyw.ljtds.biz.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.NetWorkNotAvailableException;
import com.wyw.ljtds.biz.exception.OperationTimeOutException;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.ui.user.address.ActivityAddressEdit;


/**
 * 业务数据访问处理线程(隐式的任务)
 *
 * @param <Result>
 * @author lilingxu
 */
public abstract class BizDataAsyncTask<Result> extends
        AsyncTask<Void, Void, Boolean> {
    private Result mResult;

    private ZYException mException;

    private BizFailure mFailure;


    public BizDataAsyncTask() {
        super();
    }




    @Override
    protected final Boolean doInBackground(Void... params) {
        Log.e(AppConfig.ERR_TAG, "task Instance:" + this.getClass().getName());
        try {
            mResult = doExecute();
            return true;
        } catch (ZYException e) {
            mException = e;
            return false;
        } catch (BizFailure f) {
            mFailure = f;
            return false;
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (isCancelled()) {
            return;
        }
        if (result) {
            onExecuteSucceeded(mResult);
        } else {
            if (mException instanceof NetWorkNotAvailableException) {
                onNetworkNotAvailable();
            } else if (mException instanceof OperationTimeOutException) {
                onOperationTimeout();
            } else if (mException != null) {
                onExecuteException(mException);
            }

            if (mFailure != null) {
                onExecuteFailure(mFailure);
            }
        }
    }

    protected abstract Result doExecute() throws ZYException, BizFailure;

    protected abstract void onExecuteSucceeded(Result result);

    protected abstract void OnExecuteFailed();

    protected void onNetworkNotAvailable() {
        Toast.makeText(MyApplication.getAppContext(), R.string.network_not_available,
                Toast.LENGTH_SHORT).show();
        OnExecuteFailed();
    }

    protected void onExecuteException(ZYException exception) {
        Toast.makeText(MyApplication.getAppContext(), R.string.msg_opreation_timeout,
                Toast.LENGTH_SHORT).show();
        OnExecuteFailed();
    }

    protected void onExecuteFailure(BizFailure failure) {
        Log.e(AppConfig.ERR_TAG, this.getClass().getName() + " BizTask onExecuteFailure.......:" + failure.getMessage());
        OnExecuteFailed();
        if (mFailure.getMessage().contains("新旧Token不一致") || mFailure.getMessage().contains("用户名密码") || mFailure.getMessage().contains("Token不一致") || mFailure.getMessage().contains("Token为空")) {// token认证失败
            Toast.makeText(MyApplication.getAppContext(), R.string.auth_expire,
                    Toast.LENGTH_LONG).show();
            PreferenceCache.putToken("");
            PreferenceCache.putUsername("");
            PreferenceCache.putPhoneNum("");
            Intent i = new Intent(AppConfig.AppAction.ACTION_TOKEN_EXPIRE);
            MyApplication.getAppContext().sendBroadcast(i);
        } else if (mFailure.getMessage().contains("地址为空") || mFailure.getMessage().contains("获取地址为空")) {//没有地址
            Log.e(AppConfig.ERR_TAG, "地址为空:" + AppConfig.AppAction.ACTION_ADDRESS_EXPIRE);
            Intent i = new Intent(AppConfig.AppAction.ACTION_ADDRESS_EXPIRE);
            MyApplication.getAppContext().sendBroadcast(i);
//            Intent it = new Intent(MyApplication.getAppContext(), ActivityAddressEdit.class);
//            it.putExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, 4);
//            MyApplication.getAppContext().startActivity(it);

            Toast.makeText(MyApplication.getAppContext(), "请在我的账户中维护您的送货地址",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MyApplication.getAppContext(), failure.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    protected void onOperationTimeout() {
        Toast.makeText(MyApplication.getAppContext(), R.string.msg_opreation_timeout,
                Toast.LENGTH_SHORT).show();
        OnExecuteFailed();
    }
}
