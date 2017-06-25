package com.wyw.ljtds.ui.user.help;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
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

        title.setText("帮助中心");
    }
}
