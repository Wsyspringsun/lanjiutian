package com.wyw.ljtds.ui.user.manage;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.AppManager;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.address.ActivityAddress;
import com.wyw.ljtds.utils.StringUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/13 0013.
 */

@ContentView(R.layout.activity_manage)
public class ActivityManage extends BaseActivity {
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.sdv_item_head_img)
    private SimpleDraweeView sdv_item_head_img;
    @ViewInject(R.id.name)
    private TextView name;
    @ViewInject(R.id.nicheng)
    private TextView nicheng;
    @ViewInject( R.id.shiming_xin )
    private TextView shiming_xin;


    private UserModel user;


    @Event(value = {R.id.address, R.id.shiming, R.id.touxiang, R.id.header_return, R.id.guanyu, R.id.qiehuan, R.id.anquan, R.id.sdv_item_head_img})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.header_return:
                finish();
                break;

            case R.id.address:
                it = new Intent( ActivityManage.this, ActivityAddress.class );
                startActivity( it );
                break;

            case R.id.shiming:
                if (StringUtils.isEmpty( user.getID_CARD() ) || StringUtils.isEmpty( user.getUSER_NAME() )) {
                    it = new Intent( ActivityManage.this, ActivityRealName.class );
                    startActivityForResult( it, 2 );
                }else {

                }
                break;

            case R.id.touxiang:
                it = new Intent( ActivityManage.this, ActivityInformation.class );
                it.putExtra( "user", getIntent().getParcelableExtra( "user" ) );
                startActivityForResult( it, 1 );
                break;

            case R.id.guanyu:
                startActivity( new Intent( ActivityManage.this, ActivityUpdate.class ) );
                break;

            case R.id.sdv_item_head_img:
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add( AppConfig.IMAGE_PATH + user.getUSER_ICON_FILE_ID() );
                it = new Intent( ActivityManage.this, ActivityBigImage.class );
                it.putStringArrayListExtra( "imgs", arrayList );
                startActivity( it );
                break;

            case R.id.qiehuan:
                AlertDialog alert = new AlertDialog.Builder( this ).create();
                alert.setMessage( getResources().getString( R.string.exit_account ) );
                alert.setButton( DialogInterface.BUTTON_POSITIVE, getResources().getString( R.string.alert_queding ), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        PreferenceCache.putToken( "" );
                        PreferenceCache.putUsername( "" );
                        PreferenceCache.putPhoneNum( "" );
                        Intent it = new Intent( AppConfig.AppAction.ACTION_TOKEN_EXPIRE );
                        MyApplication.getAppContext().sendBroadcast( it );
                    }
                } );
                alert.setButton( DialogInterface.BUTTON_NEGATIVE, getResources().getString( R.string.alert_quxiao ), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                } );
                alert.show();

                Button button1 = alert.getButton( DialogInterface.BUTTON_POSITIVE );
                Button button2 = alert.getButton( DialogInterface.BUTTON_NEGATIVE );
                button1.setTextColor( getResources().getColor( R.color.base_bar ) );
                button2.setTextColor( getResources().getColor( R.color.base_bar ) );

                break;

            case R.id.anquan:
                AppManager.addDestoryActivity( this, "222" );
                startActivity( new Intent( ActivityManage.this, ActivitySecurity.class ) );
                break;

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        title.setText( R.string.user_zhanghu );
        user = getIntent().getParcelableExtra( "user" );
        name.setText( "用户名：" + user.getMOBILE() );
        sdv_item_head_img.setImageURI( Uri.parse( AppConfig.IMAGE_PATH + user.getUSER_ICON_FILE_ID() ) );

        if (StringUtils.isEmpty( user.getNICKNAME() )) {
            nicheng.setText( "昵称：保密" );
        } else {
            nicheng.setText( "昵称：" + user.getNICKNAME() );
        }

        if (StringUtils.isEmpty( user.getID_CARD() ) || StringUtils.isEmpty( user.getUSER_NAME() )) {
            shiming_xin.setText( "" );
        }else {
            shiming_xin.setText( "以实名认证" );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == 1 && resultCode == AppConfig.IntentExtraKey.RESULT_OK) {
            user = data.getParcelableExtra( "user" );
            name.setText( "用户名：" + user.getMOBILE().substring( 0, user.getMOBILE().length() - (user.getMOBILE().substring( 3 )).length() ) + "****" + user.getMOBILE().substring( 7 ) );
            sdv_item_head_img.setImageURI( Uri.parse( AppConfig.IMAGE_PATH + user.getUSER_ICON_FILE_ID() ) );
            if (StringUtils.isEmpty( user.getNICKNAME() )) {
                nicheng.setText( "昵称：保密" );
            } else {
                nicheng.setText( "昵称：" + user.getNICKNAME() );
            }
        }
    }
}
