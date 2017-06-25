package com.wyw.ljtds.widget.picker;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间选择器
 */
public class TimePickerDialog extends BaseDialog {
    private TextView  cancel;
    private TextView  ok;
    private WheelView wheel_y;
    private WheelView wheel_m;
    private WheelView wheel_d;
    private WheelView wheel_h;
    private WheelView wheel_mi;
    private int  year   = 0;
    private int  month  = 0;
    private int  day    = 0;
//    private int  hour   = 0;
//    private int  minter = 0;
    private long time   = 0;
    private OnClickCallback callback;

    public TimePickerDialog(Context context) {
        super(context);
        dialog.setContentView( R.layout.dialog_time_picker);
        cancel = (TextView) dialog.findViewById(R.id.quxiao);
        ok = (TextView) dialog.findViewById(R.id.baocun);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        wheel_y = (WheelView) dialog.findViewById(R.id.wheel_y);
        wheel_m = (WheelView) dialog.findViewById(R.id.wheel_m);
        wheel_d = (WheelView) dialog.findViewById(R.id.wheel_d);
//        wheel_h = (WheelView) dialog.findViewById(R.id.wheel_h);
//        wheel_mi = (WheelView) dialog.findViewById(R.id.wheel_mi);
        wheel_y.setData( DateUtils.getItemList(DateUtils.TYPE_YEAR));
        wheel_y.setOnSelectListener(new WheelView.SelectListener() {
            @Override
            public void onSelect(int index, String text) {
                year = index + DateUtils.MIN_YEAR;

            }
        });
        wheel_m.setData(DateUtils.getItemList(DateUtils.TYPE_MONTH));
        wheel_m.setOnSelectListener(new WheelView.SelectListener() {
            @Override
            public void onSelect(int index, String text) {
                month = index + 1;
                wheel_d.setData(DateUtils.createdDay(year, month));
            }
        });
        wheel_d.setData(DateUtils.getItemList(DateUtils.TYPE_DAY));
//        wheel_h.setData(TimeUtils.getItemList(TimeUtils.TYPE_HOUR));
//        wheel_mi.setData(TimeUtils.getItemList(TimeUtils.TYPE_MINUTE));

        Date date = new Date(System.currentTimeMillis());
        Log.e("aa", date.getYear() + ">>" + date.getMonth() + ">>" + date.getDate() + ">>");
        wheel_y.setCurrentItem(date.getYear() - 70);
        wheel_m.setCurrentItem(date.getMonth());
        wheel_d.setCurrentItem(date.getDate() - 1);
//        wheel_h.setCurrentItem(date.getHours());
//        wheel_mi.setCurrentItem(date.getMinutes());

        setDialogLocation(mContext, dialog);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.baocun:
                if (callback != null) {
                    int year = wheel_y.getCurrentItem();
                    int month = wheel_m.getCurrentItem();
                    int day = wheel_d.getCurrentItem();
                    int daySize = wheel_d.getItemCount();
                    if (day > daySize) {
                        day = day - daySize;
                    }
//                    int hour = wheel_h.getCurrentItem() - 12;
//                    int min = wheel_mi.getCurrentItem();


                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year + 1900 + 70);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DATE, day + 1);
//                    calendar.set(Calendar.HOUR, hour);
//                    calendar.set(Calendar.MINUTE, min);

                    long setTime = calendar.getTimeInMillis();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String date = dateFormat.format(setTime);
                    Log.e("****",date+"");
                    callback.onSure(year, month, day,setTime);
                }
                break;
            case R.id.quxiao:
                if (callback != null) {
                    callback.onCancel();
                }
                break;
        }
    }

    public void setCallback(OnClickCallback callback) {
        this.callback = callback;
    }

    public interface OnClickCallback {
        void onCancel();

        /**
         *
         * @param year 用于界面显示  下同
         * @param month
         * @param day
         * @param time 用于给服务器传值
         */
        void onSure(int year, int month, int day, long time);
    }
}
