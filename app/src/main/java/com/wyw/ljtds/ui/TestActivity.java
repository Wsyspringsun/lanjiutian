package com.wyw.ljtds.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.Ticket;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;

import java.util.List;

@ContentView(R.layout.activity_fragment)
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    protected void onResume() {
        super.onResume();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_send_ticket, (ViewGroup) findViewById(R.id.fragment_con));
//        final Dialog dialog = new AlertDialog.Builder(this)).setView(layout).create();
        Dialog dialog = new Dialog(TestActivity.this, R.style.Theme_AppCompat_Dialog);
//        dialog.setContentView(R.layout.fragment_send_ticket);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.gongxi);

//        Window dialogWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
//        lp.alpha = 0.9f; // 透明度
        dialog.show();

        View view = layout.findViewById(R.id.btn_sent_ticket);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //申请领取优惠券
                doGetTicketTask();
                Log.e(AppConfig.ERR_TAG, "恭喜您");
//                dialog.dismiss();
                Intent it = new Intent(TestActivity.this, ActivityLogin.class);
                startActivity(it);
            }
        });
        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         *
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
//        lp.width = 300; // 宽度
//        lp.height = 800; // 高度
    }

    private void doGetTicketTask() {
        new BizDataAsyncTask<List<Ticket>>() {
            @Override
            protected List<Ticket> doExecute() throws ZYException, BizFailure {
                return null;
            }

            @Override
            protected void onExecuteSucceeded(List<Ticket> tickets) {

            }

            @Override
            protected void OnExecuteFailed() {

            }
        }.execute();
    }
}
