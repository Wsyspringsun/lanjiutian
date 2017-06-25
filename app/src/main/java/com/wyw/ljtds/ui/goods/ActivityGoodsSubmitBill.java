package com.wyw.ljtds.ui.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.widget.dialog.BottomDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

@ContentView(R.layout.dialog_pay_bill)
public class ActivityGoodsSubmitBill extends BaseActivity {
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.peisong_rg)
    private RadioGroup radioGroup1;
    @ViewInject(R.id.fapiao_rg)
    private RadioGroup radioGroup2;
    @ViewInject(R.id.fapiao_tt_rg)
    private RadioGroup radioGroup3;
    @ViewInject(R.id.fapiao_rb2)
    private RadioButton tv1;
    @ViewInject(R.id.xiangqing)
    private LinearLayout xiangqing;
    @ViewInject(R.id.edit_taitou)
    private EditText editText;


    private String peisong = "";
    private String fapiao1 = "";
    private String fapiao2 = "";
    private String fapiao3 = "";
    private String fapiao4 = "";
    private String lll = "0";

    @Event(value = {R.id.queding, R.id.header_return, R.id.fapiao_rb2, R.id.fapiao_rb1})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_return:
            case R.id.queding:
                Intent mIntent = new Intent();
                mIntent.putExtra( "possition", getIntent().getIntExtra( "possition", 0 ) );

                if (radioGroup1.getCheckedRadioButtonId() == R.id.peisong_rb1) {
                    mIntent.putExtra( "peisong", "0" );
                } else {
                    mIntent.putExtra( "peisong", "1" );
                }

                if (radioGroup2.getCheckedRadioButtonId() == R.id.fapiao_rb1) {
                    mIntent.putExtra( "fapiao_flg1", "0" );
                    setResult( AppConfig.IntentExtraKey.RESULT_OK, mIntent );
                    finish();
                } else {
                    mIntent.putExtra( "fapiao_flg1", "1" );
                    mIntent.putExtra( "fapiao_flg2","0" );
                    mIntent.putExtra( "fapiao_flg4",fapiao4 );
                    mIntent.putExtra( "fapiao_flg3",editText.getText().toString().trim() );

                    if (StringUtils.isEmpty( editText.getText().toString().trim() )) {
                        ToastUtil.show( this, "请填写发票抬头信息" );
                    } else {
                        setResult( AppConfig.IntentExtraKey.RESULT_OK, mIntent );
                        finish();
                    }
                }

                break;

            case R.id.fapiao_rb1:
                tv1.setText( R.string.fapiao_yes );
                xiangqing.setVisibility( View.INVISIBLE );
                break;

            case R.id.fapiao_rb2:
                View view = LayoutInflater.from( ActivityGoodsSubmitBill.this ).inflate( R.layout.dialog_pay_bill_select, null );
                final BottomDialog dialog = new BottomDialog( ActivityGoodsSubmitBill.this, view ).setCancelable( false ).show();
                final RadioGroup radioGroup2 = (RadioGroup) view.findViewById( R.id.fapiao_rg2 );
                final RadioGroup radioGroup1 = (RadioGroup) view.findViewById( R.id.fapiao_rg1 );
                RadioButton rb1 = (RadioButton) view.findViewById( R.id.fapiao_rb1 );
                RadioButton rb2 = (RadioButton) view.findViewById( R.id.fapiao_rb2 );
                RadioButton rb3 = (RadioButton) view.findViewById( R.id.fapiao_rb3 );
                RadioButton rb4 = (RadioButton) view.findViewById( R.id.fapiao_rb4 );
                RadioButton rb5 = (RadioButton) view.findViewById( R.id.fapiao_rb5 );

                ImageView quxiao = (ImageView) view.findViewById( R.id.quxiao );
                quxiao.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dissmiss();
                        String str = "";
                        if (fapiao4.equals( "0" )) {
                            str = getResources().getString( R.string.fapiao_mx );
                        } else if (fapiao4.equals( "1" )) {
                            str = getResources().getString( R.string.fapiao_bg );
                        } else if (fapiao4.equals( "2" )) {
                            str = getResources().getString( R.string.fapiao_jj );
                        } else if (fapiao4.equals( "3" )) {
                            str = getResources().getString( R.string.fapiao_yy );
                        } else if (fapiao4.equals( "4" )) {
                            str = getResources().getString( R.string.fapiao_hc );
                        }
                        tv1.setText( str );
                    }
                } );
                if (fapiao4.equals( "0" )) {
                    radioGroup1.check( R.id.fapiao_rb1 );
                    radioGroup2.clearCheck();
                } else if (fapiao4.equals( "1" )) {
                    radioGroup1.check( R.id.fapiao_rb2 );
                    radioGroup2.clearCheck();
                } else if (fapiao4.equals( "2" )) {
                    radioGroup1.check( R.id.fapiao_rb3 );
                    radioGroup2.clearCheck();
                } else if (fapiao4.equals( "3" )) {
                    radioGroup2.check( R.id.fapiao_rb4 );
                    radioGroup1.clearCheck();
                } else if (fapiao4.equals( "4" )) {
                    radioGroup2.check( R.id.fapiao_rb5 );
                    radioGroup1.clearCheck();
                }

                rb1.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            radioGroup2.clearCheck();
                            fapiao4 = "0";
//                            tv1.setText( R.string.fapiao_mx );
                        }
                    }
                } );
                rb2.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            radioGroup2.clearCheck();
                            fapiao4 = "1";
//                            tv1.setText( R.string.fapiao_bg );
                        }
                    }
                } );
                rb3.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            radioGroup2.clearCheck();
                            fapiao4 = "2";
//                            tv1.setText( R.string.fapiao_jj );
                        }
                    }
                } );
                rb4.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            radioGroup1.clearCheck();
                            fapiao4 = "3";
                        }
                    }
                } );
                rb5.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            radioGroup1.clearCheck();
                            fapiao4 = "4";
//                            tv1.setText( R.string.fapiao_yy );
                        }
                    }
                } );
                Button button = (Button) view.findViewById( R.id.queding );
                button.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dissmiss();
                        String str = "";
                        if (fapiao4.equals( "0" )) {
                            str = getResources().getString( R.string.fapiao_mx );
                        } else if (fapiao4.equals( "1" )) {
                            str = getResources().getString( R.string.fapiao_bg );
                        } else if (fapiao4.equals( "2" )) {
                            str = getResources().getString( R.string.fapiao_jj );
                        } else if (fapiao4.equals( "3" )) {
                            str = getResources().getString( R.string.fapiao_yy );
                        } else if (fapiao4.equals( "4" )) {
                            str = getResources().getString( R.string.fapiao_hc );
                        }
                        tv1.setText( str );
                    }
                } );

                xiangqing.setVisibility( View.VISIBLE );

                break;
        }


//        if (radioGroup3.getCheckedRadioButtonId()==R.id.fapiao_lx_rb1){
//            mIntent.putExtra("fapiao_flg2", "纸质");
//        }else {
//            mIntent.putExtra("fapiao_flg2", "电子");
//        }
//
//        if (radioGroup4.getCheckedRadioButtonId()==R.id.gren){
//            mIntent.putExtra("fapiao_flg3", "个人");
//        }else {
//            mIntent.putExtra("fapiao_flg3", "公司");
//        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        title.setText( "选择配送方式及发票" );

        fapiao1 = getIntent().getStringExtra( "fapiao_flg1" );
        fapiao2 = getIntent().getStringExtra( "fapiao_flg2" );
        fapiao3 = getIntent().getStringExtra( "fapiao_flg3" );
        fapiao4 = getIntent().getStringExtra( "fapiao_flg4" );
        peisong = getIntent().getStringExtra( "peisong" );

        Log.e( "ppppp", fapiao1 + "; " +fapiao2+"; "+fapiao3+"; "+fapiao4+"; "+ peisong );

        if (peisong.equals( "0" )) {
            radioGroup1.check( R.id.peisong_rb1 );
        } else {
            radioGroup1.check( R.id.peisong_rb2 );
        }

        if (fapiao1.equals( "0" )) {
            xiangqing.setVisibility( View.INVISIBLE );
            radioGroup2.check( R.id.fapiao_rb1 );
        } else {
            xiangqing.setVisibility( View.VISIBLE );
            radioGroup2.check( R.id.fapiao_rb2 );
            if (fapiao4.equals( "0" )){
                tv1.setText( R.string.fapiao_mx );
            }else if (fapiao4.equals( "1" )){
                tv1.setText( R.string.fapiao_bg );
            }else if (fapiao4.equals( "2" )){
                tv1.setText( R.string.fapiao_jj );
            }else if (fapiao4.equals( "3" )){
                tv1.setText( R.string.fapiao_yy );
            }else if (fapiao4.equals( "4" )){
                tv1.setText( R.string.fapiao_hc );
            }

            if (!StringUtils.isEmpty( fapiao3 )){
                editText.setText( fapiao3 );
            }

        }

    }
}
