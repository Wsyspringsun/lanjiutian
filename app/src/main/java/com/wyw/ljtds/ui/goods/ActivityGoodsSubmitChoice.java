package com.wyw.ljtds.ui.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/4/10 0010.
 */

@ContentView(R.layout.dialog_pay_choice)
public class ActivityGoodsSubmitChoice extends BaseActivity {
    @ViewInject(R.id.header_title)
    private TextView title;
    @ViewInject(R.id.time1)
    private Spinner spinner1;
    @ViewInject(R.id.time2)
    private Spinner spinner2;
    @ViewInject( R.id.radio1 )
    private RadioGroup radioGroup1;
    @ViewInject( R.id.radio2 )
    private RadioGroup radioGroup2;
    @ViewInject( R.id.jifen_rb1 )
    private RadioButton jifen_rb1;
    @ViewInject( R.id.jifen_rb2 )
    private RadioButton jifen_rb2;
    @ViewInject( R.id.jifen_rb3 )
    private RadioButton jifen_rb3;
    @ViewInject( R.id.shuoming )
    private TextView shuoming;


    private ArrayAdapter adapter1;
    private String zhifu_s = "";
    private String jifen_money = "";
    private String jifen;


    @Event( value = {R.id.queding,R.id.header_return})
    private void onClick(View v){
        Intent mIntent = new Intent();

        if (radioGroup1.getCheckedRadioButtonId()==R.id.zhifu_rb1){
            mIntent.putExtra("pay", "在线支付");
        }else {
            mIntent.putExtra("pay", "货到付款");
        }

        if (radioGroup2.getCheckedRadioButtonId()==R.id.jifen_rb1){
            mIntent.putExtra("jifen_money", "0");
            mIntent.putExtra("jifen", "0");
        }else if (radioGroup2.getCheckedRadioButtonId()==R.id.jifen_rb2){
            mIntent.putExtra("jifen_money", "1");
            mIntent.putExtra("jifen", "300");
        }else {
            mIntent.putExtra("jifen_money", "2");
            mIntent.putExtra("jifen", "600");
        }

        setResult( AppConfig.IntentExtraKey.RESULT_OK ,mIntent);
        finish();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        title.setText( "设置页面" );

        adapter1 = ArrayAdapter.createFromResource( this, R.array.time, android.R.layout.simple_spinner_item );
        adapter1.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner1.setAdapter( adapter1 );
        spinner2.setAdapter( adapter1 );

        spinner1.setSelection( 0, true );
        spinner2.setSelection( 5, true );

        zhifu_s=getIntent().getStringExtra( "pay" );
        jifen_money=getIntent().getStringExtra( "jifen_money" );
        jifen=getIntent().getStringExtra( "jifen");
        BigDecimal dou1= Utils.DoubleFormat( jifen,1 );

        Log.e( "qqqq",zhifu_s+"; "+jifen_money +"; "+jifen );

        if (zhifu_s.equals( "在线支付" )){
            radioGroup1.check( R.id.zhifu_rb1 );
        }else {
            radioGroup1.check( R.id.zhifu_rb2 );
        }


        if ((dou1.compareTo( new BigDecimal( "599.99" ) ))==1){
            radioGroup2.check( R.id.jifen_rb3 );
            jifen_rb1.setEnabled( true );
            jifen_rb2.setEnabled( true );
            jifen_rb3.setEnabled( true );
        }else if ((dou1.compareTo( new BigDecimal( "300.00" ) )==-1)){
            radioGroup2.check( R.id.jifen_rb1 );
            jifen_rb1.setEnabled( true );
            jifen_rb2.setEnabled( false );
            jifen_rb3.setEnabled( false );
        }else {
            radioGroup2.check( R.id.jifen_rb2 );
            jifen_rb1.setEnabled( true );
            jifen_rb2.setEnabled( true );
            jifen_rb3.setEnabled( false );
        }

        shuoming.setText( "当前积分："+jifen+"。市区30元以上包邮，30元以下邮费3元。县城50元以上包邮，50元以下邮费5元。\\n消费1元送1积分，300积分抵1元，消费可用600积分。" );
//        spinner1.getSelectedItem().toString(); spinner选中值

    }
}
