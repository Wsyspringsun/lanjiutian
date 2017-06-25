package com.wyw.ljtds.ui.user.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/1/11 0011.
 */

@ContentView(R.layout.activity_real_name)
public class ActivityRealName extends BaseActivity {
    @ViewInject(R.id.ed_name)
    private EditText ed_name;
    @ViewInject(R.id.ed_card)
    private EditText ed_card;
    @ViewInject( R.id.header_title )
    private TextView header_title;


    @Event(value = {R.id.next,R.id.header_return})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.header_return:
                finish();
                break;

            case R.id.next:
                if (StringUtils.isEmpty(ed_name.getText().toString().trim())){
                    ToastUtil.show(this,getResources().getString(R.string.realname_error1));
                }else if(StringUtils.isEmpty(ed_card.getText().toString().trim())||ed_card.length()!=18){
                    ToastUtil.show(this,getResources().getString(R.string.realname_error2));
                }else {
                    getReal();
                }
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        header_title.setText( "实名认证" );
    }

    BizDataAsyncTask<Integer> realName;
    private void getReal(){
        realName=new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                Log.e( "*********",ed_name.getText().toString().trim()+" ; "+ed_card.getText().toString().trim() );
                return UserBiz.realNameAuth(ed_name.getText().toString().trim(),ed_card.getText().toString().trim());
            }

            @Override
            protected void onExecuteSucceeded(Integer o) {
               if (o==1){
                   finish();
               }
            }

            @Override
            protected void OnExecuteFailed() {

            }


        };
        realName.execute();
    }
}
