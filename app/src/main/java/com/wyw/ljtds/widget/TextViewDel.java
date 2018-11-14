package com.wyw.ljtds.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wyw.ljtds.R;

/**
 * Created by wsy on 2018/5/25.
 */

public class TextViewDel extends TextView {

    private int delLineColor;
    private boolean flag = true;

    public TextViewDel(Context context, AttributeSet attrs) {

        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextViewDel);
//        "http://schemas.android.com/apk/res-auto","delLineColor"
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int itemId = ta.getIndex(i);
            switch (itemId) {
                case R.styleable.TextViewDel_delLineColor:
                    delLineColor = ta.getColor(itemId, R.color.white);
                    break;
            }
        }

    }

    @Override

    public void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (flag) {

            Paint paint = new Paint();

            // 设置直线的颜色

            paint.setColor(delLineColor);

            // 设置直线没有锯齿

            paint.setAntiAlias(true);

            // 设置线宽

            paint.setStrokeWidth((float) 3.0);

            // 设置直线位置

            canvas.drawLine(0, this.getHeight() / 2, this.getWidth(),

                    this.getHeight() / 2, paint);

        }

    }

    /**
     * true显示删除线     false不显示删除线
     *
     * @param flag
     * @return flag
     */

    public boolean setTv(boolean flag) {

        this.flag = flag;

        return flag;

    }

}
