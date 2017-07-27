package com.wyw.ljtds.ui.user.help;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.ActivityWebView;
import com.wyw.ljtds.ui.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/2/18 0018.
 */

@ContentView(R.layout.activity_help)
public class ActivityHelp extends BaseActivity {
    @ViewInject(R.id.header_title)
    private TextView title;

    @Event(value = {R.id.header_return,R.id.bangzhu3})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.header_return:
                finish();
                break;
            case R.id.bangzhu3:
                Log.e(AppConfig.ERR_TAG,"bangzhu3");
                Intent it = new Intent(ActivityHelp.this, ActivityWebView.class);
                startActivity(it);
                break;
            default:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText("帮助中心");
    }
}
