package com.wyw.ljtds.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.ui.base.BaseActivityFragment;
import com.wyw.ljtds.ui.category.FragmentLife;

/**
 * Created by wsy on 17-12-20.
 */

/**
 * 统一管理工具栏
 */
public class ToolbarManager {
    public interface IconBtnManager {
        void initIconBtn(View v, int position);
    }

    public static void initialToolbar(final Activity context, IconBtnManager mgr) {
        TextView tvTitle = (TextView) context.findViewById(R.id.activity_fragment_title);
        tvTitle.setText(context.getTitle());
        //set back button
        LinearLayout btnBack = (LinearLayout) context.findViewById(R.id.back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.finish();
            }
        });
        //set icon click event
        LinearLayout llIconBtnlist = (LinearLayout) context.findViewById(R.id.ll_icon_btnlist);
        for (int i = 0; i < llIconBtnlist.getChildCount(); i++) {
            View v = llIconBtnlist.getChildAt(i);
            //  icon button 's status,visible stat and click event,need fragment instance interface IconBtnManager
            if (mgr != null) {
                mgr.initIconBtn(v, i);
            }
        }
    }
}
