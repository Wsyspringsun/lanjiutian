package com.wyw.ljtds.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.ui.base.BaseActivityFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * create by wsy on 2017-07-28
 */
@ContentView(R.layout.activity_fragment)
public class HuoDongActivity extends BaseActivityFragment {
    public static final String FLG_HUODONG_LIST = "0";
    public static final String FLG_HUODONG_CHOJIANG = "1";
    public static final String FLG_HUODONG_MANZENG = "2";
    public static final String FLG_HUODONG_TEJIA = "3";
    public static final String FLG_HUODONG_MIAOSHA = "4";
    public static final String FLG_HUODONG_KANJIA = "5";
    public static final String FLG_HUODONG_DIANZIBI = "6";
    public static final String FLG_HUODONG_LINGYUANGOU = "7";
    public static final String FLG_HUODONG_JIFENDUIHUAN = "8";
    public static final String FLG_HUODONG_JIFENSHANGCHENG = "9";

    private static final String TAG_HUODONG_FLG = "com.wyw.ljtds.ui.home.HuoDongActivity";
    @ViewInject(R.id.activity_fragment_title)
    private TextView tvTitle;

    @Event(value = {R.id.back})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        String flg = getIntent().getStringExtra(TAG_HUODONG_FLG);
        return HuoDongFragment.newInstance(flg);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String flg = getIntent().getStringExtra(TAG_HUODONG_FLG);

        switch (flg) {
            case FLG_HUODONG_LIST:
                tvTitle.setText(getString(R.string.huodong_list));
                break;
            case FLG_HUODONG_CHOJIANG:
                tvTitle.setText(getString(R.string.huodong_choujiang));
                break;
            case FLG_HUODONG_MANZENG:
                tvTitle.setText(getString(R.string.huodong_maisong));
                break;
            case FLG_HUODONG_TEJIA:
                tvTitle.setText(getString(R.string.huodong_tejia));
                break;
            case FLG_HUODONG_MIAOSHA:
                tvTitle.setText(getString(R.string.huodong_xianshiqiang));
                break;
            case FLG_HUODONG_KANJIA:
                tvTitle.setText(getString(R.string.huodong_kanjia));
                break;
            case FLG_HUODONG_DIANZIBI:
                tvTitle.setText(getString(R.string.huodong_dianzibi));
                break;
            case FLG_HUODONG_LINGYUANGOU:
                tvTitle.setText(getString(R.string.huodong_lingyuangou));
                break;
            case FLG_HUODONG_JIFENDUIHUAN:
                tvTitle.setText(getString(R.string.huodong_jifenduihuan));
                break;
            case FLG_HUODONG_JIFENSHANGCHENG:
                tvTitle.setText("积分商城");
                break;
            default:
                break;
        }
    }

    public static Intent getIntent(Context ctx, String flg) {
        Intent it = new Intent(ctx, HuoDongActivity.class);
        it.putExtra(TAG_HUODONG_FLG, flg);
        return it;
    }

}
