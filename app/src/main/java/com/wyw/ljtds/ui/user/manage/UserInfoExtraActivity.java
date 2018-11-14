package com.wyw.ljtds.ui.user.manage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.wyw.ljtds.MainActivity;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.OrderSubmitAdapter;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.MedicineTypeFirstModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by Administrator on 2017/1/13 0013.
 */

@ContentView(R.layout.activity_userinfo)
public class UserInfoExtraActivity extends BaseActivity {
    private static final String CARTEVITAL_FLG_YES = "1";
    private static final String CARTEVITAL_FLG_NO = "0";
    @ViewInject(R.id.activity_userinfo_ed_realname)
    private EditText edRealname;//真实姓名
    @ViewInject(R.id.activity_userinfo_ed_cardId)
    private EditText edCardId;//身份证号
    @ViewInject(R.id.activity_userinfo_ed_cartevitalflg)
    private AppCompatSpinner spnrCartevitalFlg;//医保卡标识   0：无 ，1：有
    @ViewInject(R.id.activity_userinfo_ryv_alwaysbuydrug)
    private RecyclerView ryvAlwaysBuyDrug;
    @ViewInject(R.id.activity_userinfo_btn_sure)
    private Button btnSure;
    @ViewInject(R.id.activity_fragment_title)
    private TextView tvTitle;

    private ArrayAdapter<String> spinerArrayAdapter;
    private RecyclerView.Adapter ryvBuyDrugAdapter;

    GoodsBiz goodsBiz = null;
    UserBiz userBiz = null;

    private boolean needSend = false;

    private List<MedicineTypeFirstModel> medicineTypes;

    //固定数据
    private String[] carteVitalFlgItems; //有/无医保卡
    private Map<String, String> carteVitalFlgItemsMap = new HashMap<>();
    //1:有 0：吴
    private Map<String, String> carteVitalFlgItemsMapRev = new HashMap<>();


    String realName;
    String cardId;
    String carteVitalFlg;
    String alwaysBuyDrug;
    Set<String> alwaysBuyDrugSet = new HashSet<>();

    @Event(value = {R.id.back})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    private View.OnClickListener onViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_userinfo_btn_sure:
                    String err = validData();
                    if (err.length() > 0) {
                        Utils.showErrMsg(UserInfoExtraActivity.this, err);
                        return;
                    }
                    submitRealNameAuth();
                    break;

            }
        }
    };

    /**
     * 验证数据
     *
     * @return
     */
    private String validData() {
        StringBuilder err = new StringBuilder();
        realName = edRealname.getText().toString().trim();
        if (realName.length() > 20 || realName.length() <= 0) {
            err.append("姓名在1~20个字符之间");
        }
        cardId = edCardId.getText().toString().trim().toLowerCase();
        if (cardId.length() != 18) {
            err.append("不是真实的身份证号");
        }
        String seledSpnrCartevitalFlg = (String) spnrCartevitalFlg.getSelectedItem();
        carteVitalFlg = carteVitalFlgItemsMap.get(seledSpnrCartevitalFlg);
        alwaysBuyDrug = "";
        for (String drug : alwaysBuyDrugSet) {
            alwaysBuyDrug += "," + drug;
        }
        if (alwaysBuyDrug.indexOf(",") == 0) {
            alwaysBuyDrug = alwaysBuyDrug.substring(1, alwaysBuyDrug.length());
        }
        if (alwaysBuyDrug.length() <= 0) {
            err.append("请您至少选择一项");
        }

        return err.toString();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.log("UserInfoExtraActivity........");

        tvTitle.setText(getTitle());

        goodsBiz = GoodsBiz.getInstance(this);
        userBiz = UserBiz.getInstance(this);

        carteVitalFlgItemsMap.put("有", CARTEVITAL_FLG_YES);
        carteVitalFlgItemsMap.put("无", CARTEVITAL_FLG_NO);

        carteVitalFlgItemsMapRev.put(CARTEVITAL_FLG_YES, "有");
        carteVitalFlgItemsMapRev.put(CARTEVITAL_FLG_NO, "无");

        carteVitalFlgItems = new String[carteVitalFlgItemsMap.keySet().size()];
        carteVitalFlgItems = carteVitalFlgItemsMap.keySet().toArray(carteVitalFlgItems);


        spinerArrayAdapter = new ArrayAdapter<String>(UserInfoExtraActivity.this, R.layout.item_textview, carteVitalFlgItems);
        spnrCartevitalFlg.setAdapter(spinerArrayAdapter);
        spinerArrayAdapter.setDropDownViewResource(R.layout.item_textview_dropdown);


        GridLayoutManager layoutManger = new GridLayoutManager(this, 4);
        ryvAlwaysBuyDrug.setLayoutManager(layoutManger);
        ryvAlwaysBuyDrug.setItemAnimator(new DefaultItemAnimator());

        btnSure.setOnClickListener(onViewClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadAlwaysBuyDrug();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    /**
     * 加载常用药
     */
    private void loadAlwaysBuyDrug() {
        setLoding(this, false);
        new BizDataAsyncTask<String>() {
            @Override
            protected String doExecute() throws ZYException, BizFailure {
                return goodsBiz.alwaysBuyDrug();
            }

            @Override
            protected void onExecuteSucceeded(String data) {
                try {
                    JsonElement el = Utils.parseResponse(data);
                    Gson gson = new GsonBuilder().create();
                    TypeToken<List<MedicineTypeFirstModel>> tt = new TypeToken<List<MedicineTypeFirstModel>>() {
                    };
                    medicineTypes = gson.fromJson(el, tt.getType());
                    getUser();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ToastUtil.show(UserInfoExtraActivity.this, "获取服务器数据失败");
                } finally {
                    closeLoding();
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void submitRealNameAuth() {
        setLoding(this, false);
        new BizDataAsyncTask<Integer>() {
            @Override
            protected Integer doExecute() throws ZYException, BizFailure {
                Utils.log("params:realName-" + realName + ",cardId-" + cardId + ",carteVitalFlg-" + carteVitalFlg + ",alwaysBuyDrug-" + alwaysBuyDrug);
                return userBiz.realNameAuth(realName, cardId, carteVitalFlg, alwaysBuyDrug);
            }

            @Override
            protected void onExecuteSucceeded(Integer o) {
                closeLoding();
                if (o == 1) {
                    if (needSend) {
//                        ToastUtil.show(UserInfoExtraActivity.this, "完善成功!优惠券已放入钱包，注意查收");
                        showSentTicket();
                    } else {
                        ToastUtil.show(UserInfoExtraActivity.this, "完善成功!");
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Intent getIntent(Context ctx) {
        Intent it = new Intent(ctx, UserInfoExtraActivity.class);
        return it;
    }

    private void getUser() {
        if (SingleCurrentUser.userInfo != null) {
            bindDate2View();
            return;
        }
        setLoding(this, false);
        new BizDataAsyncTask<UserModel>() {
            @Override
            protected UserModel doExecute() throws ZYException, BizFailure {
                return userBiz.getUser();
            }

            @Override
            protected void onExecuteSucceeded(UserModel data) {
                closeLoding();
                SingleCurrentUser.userInfo = data;
                bindDate2View();


            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void bindDate2View() {
        if (StringUtils.isEmpty(SingleCurrentUser.userInfo.getID_CARD())) {
            needSend = true;
        }
        edRealname.setText(SingleCurrentUser.userInfo.getUSER_NAME());
        edCardId.setText(SingleCurrentUser.userInfo.getID_CARD());

        String carteFlg = SingleCurrentUser.userInfo.getCARTE_VITAL_FLG();
        String carteFlgDesc = carteVitalFlgItemsMapRev.get(carteFlg);
        int idx = 0;
        Utils.log("carteFlg:" + idx + "---" + carteFlg);
        if (!StringUtils.isEmpty(carteFlgDesc)) {
            idx = Arrays.asList(carteVitalFlgItems).indexOf(carteFlgDesc);
            if (idx < 0) idx = 0;
            if (idx > 1) idx = 1;
        }
        spnrCartevitalFlg.setSelection(idx);

        String strBuyed = SingleCurrentUser.userInfo.getOFTEN_BUY_TYPE();
        if (!StringUtils.isEmpty(strBuyed)) {
            String[] buyedTypes = strBuyed.split(",");
            if (buyedTypes != null) {
                for (String buyItem : buyedTypes) {
                    alwaysBuyDrugSet.add(buyItem);
                }
            }
        }

        if (medicineTypes == null) return;
        if (ryvBuyDrugAdapter == null) {
            ryvBuyDrugAdapter = new RecyclerView.Adapter<CheckViewHolder>() {
                @Override
                public CheckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = getLayoutInflater().inflate(R.layout.item_checkbox, parent, false);
                    return new CheckViewHolder(view);
                }

                @Override
                public void onBindViewHolder(CheckViewHolder holder, int position) {
                    MedicineTypeFirstModel item = medicineTypes.get(position);
                    holder.chk.setText(item.getCLASSNAME());
                    holder.chk.setTag(item.getCLASSCODE());
                    holder.data = item;
                    if (alwaysBuyDrugSet.contains(item.getCLASSCODE())) {
                        holder.chk.setChecked(true);
                    }
                }

                @Override
                public int getItemCount() {
                    if (medicineTypes == null)
                        return 0;
                    return medicineTypes.size();
                }
            };

            ryvAlwaysBuyDrug.setAdapter(ryvBuyDrugAdapter);
        } else {
            ryvBuyDrugAdapter.notifyDataSetChanged();
        }

    }

    private class CheckViewHolder extends RecyclerView.ViewHolder {
        MedicineTypeFirstModel data;//绑定的数据
        CheckBox chk;


        public CheckViewHolder(View itemView) {
            super(itemView);
            this.chk = (CheckBox) itemView;
            this.chk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data == null) return;
                    CheckBox chk = (CheckBox) v;
                    //将选中的类别记录，将不选的删除
                    if (chk.isChecked()) {
                        alwaysBuyDrugSet.add(data.getCLASSCODE());
                    } else {
                        alwaysBuyDrugSet.remove(data.getCLASSCODE());
                    }
                }
            });
        }
    }

    private void showSentTicket() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_send_ticket, (ViewGroup) findViewById(R.id.fragment_con));
        ImageView ivClose = (ImageView) layout.findViewById(R.id.fragment_send_ticket_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(UserInfoExtraActivity.this, "完善成功!");
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        ImageView sdv = (ImageView) layout.findViewById(R.id.fragment_send_ticket_sdv_show);
        Picasso.with(this).load(Uri.parse(AppConfig.IMAGE_PATH_LJT_ECOMERCE + "/ecommerce/images/mobileIndexImages/data_ok.png")).placeholder(R.drawable.img_adv_zhanwei).into(sdv);
        Dialog dialog = new Dialog(UserInfoExtraActivity.this, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.gongxi);


//        View btn = layout.findViewById(R.id.btn_sent_ticket);
        sdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.show(UserInfoExtraActivity.this, "完善成功!");
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        dialog.show();

    }

}
