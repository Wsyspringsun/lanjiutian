package com.wyw.ljtsp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyw.ljtsp.R;
import com.wyw.ljtsp.utils.InputMethodUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

@ContentView(R.layout.activity_login)
public class ActivityLogin extends BaseActivitry {
    @ViewInject(R.id.guanbi)
    private ImageView close;
    @ViewInject(R.id.ed_phone)
    private EditText ed_phone;
    @ViewInject(R.id.ed_pass)
    private EditText ed_pass;

    @Event(value = {R.id.guanbi,R.id.next})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.guanbi:
                InputMethodUtils.closeSoftKeyboard( this );
                finish();
                it = new Intent( this, MainActivity.class );
//                AppConfig.currSel = 0;
//                it.putExtra( AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel );
                startActivity( it );
                break;

            case R.id.next:
                setLoding( this, false );
//                doLogin();
                break;


        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        x.view().inject( this );//xutils初始化视图
        ed_phone.setImeOptions( EditorInfo.IME_ACTION_NEXT );
        ed_pass.setImeOptions( EditorInfo.IME_ACTION_DONE );

        ed_phone.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i==EditorInfo.IME_ACTION_NEXT){
                    ed_pass.requestFocus();
                }
                return false;
            }
        } );
        ed_pass.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i==EditorInfo.IME_ACTION_DONE){
                    InputMethodUtils.closeSoftKeyboard( ActivityLogin.this ) ;
                }
                return false;
            }
        } );

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            InputMethodUtils.closeSoftKeyboard( this );
            finish();
            Intent it = new Intent( this, MainActivity.class );
//            AppConfig.currSel = 0;
//            it.putExtra( AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel );
            startActivity( it );
        }
        return super.onKeyDown( keyCode, event );
    }
}
