package com.wyw.ljtds.ui.find;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

@ContentView(R.layout.activity_medicine_category)
public class ActivityFindCategort extends BaseActivity {
    @ViewInject(R.id.header_title)
    private TextView title;

    @Event(value = {R.id.header_return, R.id.buwei, R.id.fenlei})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.buwei:
                startActivity(new Intent(this, ActivityBody.class));
                break;

            case R.id.fenlei:
//                Intent it = new Intent(this, MainActivity.class);
//                startActivity(it);
                AppConfig.currSel = 1;
                finish();
                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title.setText(R.string.find_button1);
    }

}
