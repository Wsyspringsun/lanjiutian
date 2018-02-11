package com.wyw.ljtds.ui.user.address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.core.PoiInfo;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.user.ActivityLogin;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.ToolbarManager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by Administrator on 2017/1/6 0006.
 */

@ContentView(R.layout.activity_address_change)
public class AddressSelActivity extends BaseActivity {
    public static final String TAG_SELECTED_ADDRESS = "com.wyw.ljtds.ui.user.address.AddressSelActivity.tag_selected_address";
    private static final int REQUEST_SEARCH_ADDRESS = 1;

    @ViewInject(R.id.activity_address_change_rv_addrlist)
    private RecyclerView recyclerView;
    @ViewInject(R.id.activity_address_change_tv_tianjia)
    private TextView add;
    @ViewInject(R.id.activity_address_change_tv_currentloc)
    private TextView tvCurrentLoc;
    @ViewInject(R.id.activity_address_change_rl_location)
    private RelativeLayout rlLocation;


    //    @ViewInject(R.id.activity_address_change_tv_location)
//    private TextView tvLocation;
    //无数据时的界面
    private View noData;
    private List<AddressModel> list;
    private AddressAdapter adapter;
    private MyLocation locationItem;

    @Event(value = {R.id.activity_address_change_tv_location, R.id.activity_address_change_img_location, R.id.activity_address_change_tv_tianjia, R.id.activity_address_change_rl_location, R.id.header_return})
    private void onClick(View view) {
        Intent it;
        switch (view.getId()) {
            case R.id.activity_address_change_tv_location:
            case R.id.activity_address_change_img_location:
                //location
                if (SingleCurrentUser.bdLocation != null) {
                    BDLocation location = SingleCurrentUser.bdLocation;
                    updateLocationItem(MyLocation.newInstance(location.getLatitude(), location.getLongitude(), location.getAddrStr()));
                } else {
                    ToastUtil.show(AddressSelActivity.this, AddressSelActivity.this.getString(R.string.err_location));
                }
                break;
            case R.id.activity_address_change_rl_location:
                //select location
                SingleCurrentUser.updateLocation(locationItem);
                setResult(Activity.RESULT_OK);
                finish();
                break;

            case R.id.activity_address_change_tv_tianjia:
                it = new Intent(this, ActivityAddressEdit.class);
                it.putExtra(AppConfig.IntentExtraKey.ADDRESS_FROM, 1);
                startActivity(it);
                break;
        }

    }

    private void updateLocationItem(MyLocation loc) {
        locationItem = loc;
        tvCurrentLoc.setText(locationItem.getAddrStr());
    }

    public static Intent getIntent(Context ctx, Boolean isSel) {
        Intent it = new Intent(ctx, AddressSelActivity.class);
        it.putExtra(TAG_SELECTED_ADDRESS, isSel);
        return it;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ToolbarManager.initialToolbar(this, new ToolbarManager.IconBtnManager() {
            @Override
            public void initIconBtn(View v, int position) {
                switch (position) {
                    case 4:
                        //search address
                        v.setVisibility(View.VISIBLE);
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent it = AddressSearchActivity.getIntent(AddressSelActivity.this);
                                startActivityForResult(it, REQUEST_SEARCH_ADDRESS);
                            }
                        });
                        //go to map search
                        break;
                }
            }
        });

        updateLocationItem(SingleCurrentUser.location);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//必须有 linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);//设置方向滑动 recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        noData = getLayoutInflater().inflate(R.layout.main_empty_view, (ViewGroup) recyclerView.getParent(), false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (UserBiz.isLogined()) {
            getAddress();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        switch (requestCode) {
            case REQUEST_SEARCH_ADDRESS:
                //返回搜索地址结果
                String searchRltJson = data.getStringExtra(AddressSearchActivity.TAG_SELECT_ADDR);
                PoiInfo searchRlt = GsonUtils.Json2Bean(searchRltJson, PoiInfo.class);

                MyLocation location = MyLocation.newInstance(searchRlt.location.latitude, searchRlt.location.longitude, searchRlt.address);
                updateLocationItem(location);
                //移动地图中心
                break;
        }
    }


    private void getAddress() {
        setLoding(this, false);
        new BizDataAsyncTask<List<AddressModel>>() {
            @Override
            protected List<AddressModel> doExecute() throws ZYException, BizFailure {
                return UserBiz.selectUserAddress();
            }

            @Override
            protected void onExecuteSucceeded(List<AddressModel> addressModels) {
                closeLoding();
                list = addressModels;
                bindData2View();
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void bindData2View() {
        adapter = new AddressAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    class AddressHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AddressModel data;
        TextView tvName;
        TextView tvPhone;
        TextView tvXiangxi;

        public AddressModel getData() {
            return data;
        }

        public void setData(AddressModel data) {
            this.data = data;
        }

        public AddressHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.item_address_sel_name);
            tvPhone = (TextView) itemView.findViewById(R.id.item_address_sel_phone);
            tvXiangxi = (TextView) itemView.findViewById(R.id.item_address_sel_xiangxi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent it;
            switch (v.getId()) {
                case R.id.item_address_sel_item_address:
                    Boolean isSel = AddressSelActivity.this.getIntent().getBooleanExtra(TAG_SELECTED_ADDRESS, false);
                    if (!isSel)
                        return;
                    it = new Intent();
                    it.putExtra(TAG_SELECTED_ADDRESS, data);
                    AddressSelActivity.this.setResult(Activity.RESULT_OK, it);
                    finish();
                    break;
            }
        }
    }

    class AddressAdapter extends RecyclerView.Adapter<AddressHolder> {
        List<AddressModel> data;

        public AddressAdapter(List<AddressModel> data) {
            this.data = data;
        }

        @Override
        public AddressHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(AddressSelActivity.this);
            View view = inflater.inflate(R.layout.item_address_sel, parent, false);
            AddressHolder holder = new AddressHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(AddressHolder holder, int position) {
            AddressModel itemData = data.get(position);
            holder.setData(itemData);
            holder.tvName.setText(itemData.getCONSIGNEE_NAME());
            holder.tvPhone.setText(itemData.getCONSIGNEE_MOBILE());
            holder.tvXiangxi.setText(itemData.getLOCATION() + " " + itemData.getCONSIGNEE_ADDRESS());
        }

        @Override
        public int getItemCount() {
            if (data == null) return 0;
            return data.size();
        }
    }

}
