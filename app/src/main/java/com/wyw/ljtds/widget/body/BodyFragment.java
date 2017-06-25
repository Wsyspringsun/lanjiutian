package com.wyw.ljtds.widget.body;

import android.app.Activity;
import android.app.Fragment;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.wyw.ljtds.ui.find.ActivityBody;


/**
 * Created by angelo on 2015/2/15.
 */
public abstract class BodyFragment extends Fragment implements ActivityBody.MyTouchListener {
    public ImageView bodyImageView;
    private boolean isMan = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ActivityBody)activity).registerTouchListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setShowImage(isMan);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
            //逻辑处理
        }
    }

    public void setMan(boolean isMan) {
        this.isMan = isMan;
    }

    public abstract void setShowImage(Boolean isMan);
}
