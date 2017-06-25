package com.wyw.ljtds.ui.user.manage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.ui.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/6/4 0004.
 */

@ContentView(R.layout.activity_security)
public class ActivitySecurity extends BaseActivity {
    @ViewInject(R.id.header_title)
    private TextView title;


    @Event(value = { R.id.header_return,R.id.mima})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.mima:
                AppManager.addDestoryActivity( this,"111" );
                startActivity( new Intent( this,ActivityAmendPassword.class ) );
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        title.setText( "修改密码");
    }
}
