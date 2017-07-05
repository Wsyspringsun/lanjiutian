package com.wyw.ljtsp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtsp.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/7/4 0004.
 */

@ContentView(R.layout.activity_setting)
public class ActivitySetting extends BaseActivitry {
    @ViewInject(R.id.header_title)
    private TextView title;


    @Event(value = {R.id.header_return,R.id.sign_out})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.header_return:
                finish();
                break;

            case R.id.sign_out:

                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.setting);
    }
}
