package com.wyw.ljtds.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.AbstractListViewAdapter;
import com.wyw.ljtds.adapter.AbstractViewHolder;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;
import com.wyw.ljtds.ui.goods.ActivityMedicineList;
import com.wyw.ljtds.utils.InputMethodUtils;
import com.wyw.ljtds.widget.MyGridView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/27 0027.
 */

@ContentView(R.layout.activity_search)
public class ActivitySearch extends BaseActivity {
    @ViewInject(R.id.gridview)
    private MyGridView gridView;
    @ViewInject(R.id.back)
    private ImageView back;
    @ViewInject(R.id.edHeader)
    private EditText edHeader;

    @Event(value = {R.id.back, R.id.search_img, R.id.search_tv})
    private void onclick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.back:
                InputMethodUtils.closeSoftKeyboard( this );
                it = new Intent( this, MainActivity.class );
                it.putExtra( AppConfig.IntentExtraKey.BOTTOM_MENU_INDEX, AppConfig.currSel );
                finish();
                startActivity( it );

                break;

            case R.id.search_img:
            case R.id.search_tv:
                if (getIntent().getIntExtra( "from", 0 ) == 1) {
                    it = new Intent( ActivitySearch.this, ActivityGoodsList.class );
                    it.putExtra( "search", edHeader.getText().toString().trim() );
                    it.putExtra( "typeid", "" );
                    startActivity( it );
                } else {
                    it = new Intent( ActivitySearch.this, ActivityMedicineList.class );
                    it.putExtra( "search", edHeader.getText().toString().trim() );
                    it.putExtra( "typeid", "" );
                    startActivity( it );
                }
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        List<String> list = new ArrayList<>();
        list.add( "雀巢咖啡" );
        list.add( "女式拖鞋" );
        list.add( "红富士" );
        list.add( "防嗮霜" );
        list.add( "乔丹篮球" );
        list.add( "毛衣" );
        list.add( "雀巢咖啡" );

        AbstractListViewAdapter<String> adapter = new AbstractListViewAdapter<String>( this, R.layout.item_search_tuijian ) {
            @Override
            public void initListViewItem(AbstractViewHolder viewHolder, String item) {
                TextView tv = viewHolder.getView( R.id.text_tuijian );
                tv.setText( item );
            }
        };
        adapter.addItems( list );
        gridView.setAdapter( adapter );


        edHeader.setImeOptions( EditorInfo.IME_ACTION_SEARCH );//设置搜索按钮
        edHeader.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (getIntent().getIntExtra( "from", 0 ) == 1) {
                        Intent it = new Intent( ActivitySearch.this, ActivityGoodsList.class );
                        it.putExtra( "search", edHeader.getText().toString().trim() );
                        it.putExtra( "typeid", "" );
                        startActivity( it );
                    } else {
                        Intent it = new Intent( ActivitySearch.this, ActivityMedicineList.class );
                        it.putExtra( "search", edHeader.getText().toString().trim() );
                        it.putExtra( "typeid", "" );
                        startActivity( it );
                    }
                }
                return false;
            }
        } );

    }

}
