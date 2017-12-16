package com.wyw.ljtsp.biz.task;

import android.os.AsyncTask;
import android.widget.Toast;

import com.wyw.ljtsp.R;
import com.wyw.ljtsp.biz.exception.BizFailure;
import com.wyw.ljtsp.biz.exception.NetWorkNotAvailableException;
import com.wyw.ljtsp.biz.exception.OperationTimeOutException;
import com.wyw.ljtsp.biz.exception.ZYException;
import com.wyw.ljtsp.config.MyApplication;


/**
 * 业务数据访问处理线程(隐式的任务)
 * 
 * @author lilingxu
 * 
 * @param <Result>
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
//				onExecuteFailure(mFailure);
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
		Toast.makeText(MyApplication.getAppContext(),R.string.msg_opreation_timeout,
				Toast.LENGTH_SHORT).show();
		OnExecuteFailed();
	}

//	protected void onExecuteFailure(BizFailure failure) {
//		Log.e( "****",failure.getMessage());
//		if (mFailure.getMessage().contains("新旧Token不一致")||mFailure.getMessage().contains("用户名密码")) {// token认证失败
//			Toast.makeText(MyApplication.getAppContext(), R.string.auth_expire,
//					Toast.LENGTH_LONG).show();
//			Intent i = new Intent(AppConfig.AppAction.ACTION_TOKEN_EXPIRE);
//			MyApplication.getAppContext().sendBroadcast(i);
//			PreferenceCache.putToken("");
//			PreferenceCache.putUsername( "" );
//			PreferenceCache.putPhoneNum( "" );
//		} else if (mFailure.getMessage().contains( "地址为空" )||mFailure.getMessage().contains( "获取地址为空" )){//没有地址
//			Intent i = new Intent(AppConfig.AppAction.ACTION_ADDRESS_EXPIRE);
//			MyApplication.getAppContext().sendBroadcast(i);
//		} else if (mFailure.getMessage().contains("获取消息失败")){
//
//		} else {
//			Toast.makeText(MyApplication.getAppContext(), failure.getMessage(),
//					Toast.LENGTH_LONG).show();
//		}
//		OnExecuteFailed();
//	}

	protected void onOperationTimeout() {
		Toast.makeText(MyApplication.getAppContext(), R.string.msg_opreation_timeout,
				Toast.LENGTH_SHORT).show();
		OnExecuteFailed();
	}
}
