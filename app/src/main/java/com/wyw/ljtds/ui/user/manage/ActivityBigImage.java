package com.wyw.ljtds.ui.user.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.ui.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;

import cn.bingoogolapple.photopicker.adapter.BGAPhotoPageAdapter;
import cn.bingoogolapple.photopicker.widget.BGAHackyViewPager;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 看大图
 * Created by Administrator on 2017/5/21 0021.
 */
@ContentView(R.layout.activity_photoview_viewpager)
public class ActivityBigImage extends BaseActivity implements PhotoViewAttacher.OnViewTapListener {
    @ViewInject(R.id.bga_viewpager)
    private BGAHackyViewPager viewPager;
    @ViewInject(R.id.number)
    private TextView mTitleTv;

    private BGAPhotoPageAdapter mPhotoPageAdapter;

    @Event(value = {R.id.bga_viewpager, R.id.fanhui})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.bga_viewpager:

                finish();
                break;

            case R.id.fanhui:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPhotoPageAdapter = new BGAPhotoPageAdapter(ActivityBigImage.this, ActivityBigImage.this, getIntent().getStringArrayListExtra("imgs"));
        viewPager.setAdapter(mPhotoPageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onViewTap(View view, float x, float y) {

    }

    /**
     * @param ctx
     * @param imgs 图片路径
     * @return
     */
    public static Intent getIntent(Context ctx, String... imgs) {
        Intent it = new Intent(ctx, ActivityBigImage.class);
        ArrayList<String> imgList = new ArrayList<>();
        for(String img:imgs){
            imgList.add(img);
        }
        it.putStringArrayListExtra("imgs", imgList);
        return it;
    }
}
