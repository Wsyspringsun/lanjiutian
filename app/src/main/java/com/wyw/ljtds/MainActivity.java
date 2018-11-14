package com.wyw.ljtds;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wyw.ljtds.biz.biz.UserBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.MyLocation;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.cart.FragmentCart;
import com.wyw.ljtds.ui.category.FragmentCategory;
import com.wyw.ljtds.ui.find.ActivityFindCategort;
import com.wyw.ljtds.ui.find.FragmentFind;
import com.wyw.ljtds.ui.goods.ActivityGoodsList;
import com.wyw.ljtds.ui.goods.ActivityLifeGoodsInfo;
import com.wyw.ljtds.ui.goods.ActivityMedicineList;
import com.wyw.ljtds.ui.goods.ActivityMedicinesInfo;
import com.wyw.ljtds.ui.goods.LifeShopActivity;
import com.wyw.ljtds.ui.goods.PointShopLifeGoodsListActivity;
import com.wyw.ljtds.ui.goods.PointShopMedicineActivity;
import com.wyw.ljtds.ui.goods.ShopActivity;
import com.wyw.ljtds.ui.home.FragmentLifeIndex;
import com.wyw.ljtds.ui.home.FragmentUserIndex;
import com.wyw.ljtds.ui.home.HuoDongActivity;
import com.wyw.ljtds.ui.user.ActivityRegist;
import com.wyw.ljtds.ui.user.manage.UserInfoExtraActivity;
import com.wyw.ljtds.ui.user.wallet.PointShopActivity;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.EasyPermissions;


@ContentView(R.layout.activity_main)//setcontextview
public class MainActivity extends BaseActivity {
    protected static final int REQUEST_PERMS_LOCATION = 0;//请求Location权限

    public static String TAG_CMD = "";
    public static final String TAG_CMD_UserInfoExtraActivity = "com.wyw.ljtds.MainActivity.TAG_CMD_UserInfoExtraActivity";
    public static final String TAG_POSITION = "TAG_POSITION";

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    //    private IWXAPI wxApi;
    @ViewInject(R.id.home)
    private RelativeLayout home;
    @ViewInject(R.id.category)
    private RelativeLayout category;
    @ViewInject(R.id.find)
    private RelativeLayout find;
    @ViewInject(R.id.shopping_cart)
    private RelativeLayout cart;
    @ViewInject(R.id.user)
    private RelativeLayout user;
    @ViewInject(R.id.tv_home)
    private TextView tv_home;
    @ViewInject(R.id.tv_category)
    private TextView tv_category;
    @ViewInject(R.id.tv_find)
    private TextView tv_find;
    @ViewInject(R.id.tv_shopping_cart)
    private TextView tv_cart;
    @ViewInject(R.id.tv_user)
    private TextView tv_user;
    @ViewInject(R.id.iv_home)
    private ImageView iv_home;
    @ViewInject(R.id.iv_category)
    private ImageView iv_category;
    @ViewInject(R.id.iv_find)
    private ImageView iv_find;
    @ViewInject(R.id.iv_shopping_cart)
    private ImageView iv_cart;
    @ViewInject(R.id.iv_user)
    private ImageView iv_user;

    private FragmentLifeIndex fragmentHome;
    private FragmentCategory fragmentCategory;
    private FragmentFind fragmentFind;
    private FragmentCart fragmentCart;
    private FragmentUserIndex fragmentUser;

    private List<AddressModel> addrlist;
    private Bundle savedInstanceState;
    private FragmentTransaction fragmentTransaction;

    private UserBiz bizUser;


    @Event(value = {R.id.home, R.id.category, R.id.find, R.id.shopping_cart, R.id.user})
    private void setlect(View v) {
        int index = 0;
        switch (v.getId()) {
            case R.id.home:
                index = 0;
                break;
            case R.id.category:
                index = 1;
                break;
            case R.id.find:
                index = 2;
                break;
            case R.id.shopping_cart:
                index = 3;
                break;
            case R.id.user:
                index = 4;
                break;
        }
//        AppConfig.currSel = index;
        AppConfig.currSel = index;
        addFragmentToStack(AppConfig.currSel);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAG_POSITION, AppConfig.currSel);
    }

    public void initAddr(MyLocation loc) {
        Log.e(AppConfig.ERR_TAG, "initAddr");
        SingleCurrentUser.updateLocation(loc);
        if (fragmentHome != null && fragmentHome.isAdded()) {
            fragmentHome.setLocation();
        }
        if (fragmentFind != null && fragmentFind.isAdded()) {
            fragmentFind.setLocation();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        bizUser = UserBiz.getInstance(this);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            AppConfig.currSel = AppConfig.DEFAULT_INDEX_FRAGMENT;
            //默认
            switch (AppConfig.currSel) {
                case 0:
                    fragmentHome = FragmentLifeIndex.newInstance();
                    fragmentTransaction.replace(R.id.fragment_container, fragmentHome, fragmentHome.getClass().getName()).commit();
                    break;
                case 2:
                    fragmentFind = new FragmentFind();
                    fragmentTransaction.replace(R.id.fragment_container, fragmentFind, fragmentFind.getClass().getName()).commit();
                    break;
            }
        } else {
            AppConfig.currSel = savedInstanceState.getInt(TAG_POSITION);
        }


        final IWXAPI api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(AppConfig.WEIXIN_APP_ID);
        //注册微信
        StatService.start(this);

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (TAG_CMD_UserInfoExtraActivity.equals(MainActivity.TAG_CMD)) {
            startActivity(UserInfoExtraActivity.getIntent(MainActivity.this));
            MainActivity.TAG_CMD = "";
        }

        //other page point a fragment index
        if (AppConfig.currSel != -1) {
            addFragmentToStack(AppConfig.currSel);
        }

        if (SingleCurrentUser.location == null) {
//            loadDefaultLocation();
            if (UserBiz.isLogined()) {
                loadLoginerInfo();
                loadUserAddr();
                getUser();
            } else {
                loadUserLocation();
            }
        }

    }

    private void loadLoginerInfo() {

    }

    /**
     * 获取通知栏权限是否开启
     */
    public static boolean isNotificationEnabled(Context context) {
        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
      /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 添加fragment
     *
     * @param cur
     */
    void addFragmentToStack(int cur) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        hideFragments(fragmentTransaction);
        cur = cur % 5;
        switch (cur) {
            case 0:
                tv_home.setTextColor(getResources().getColor(R.color.base_bar));
                iv_home.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_home_yes));

                fragmentHome = (FragmentLifeIndex) getSupportFragmentManager().findFragmentByTag(FragmentLifeIndex.class.getName());
                if (fragmentHome == null) {
//                    fragmentHome = new FragmentHome();
                    fragmentHome = FragmentLifeIndex.newInstance();
                }
                selectFragment(fragmentHome, fragmentTransaction);
                break;
            case 1:
                tv_category.setTextColor(getResources().getColor(R.color.base_bar));
                iv_category.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_fenlei_yes));

                fragmentCategory = (FragmentCategory) getSupportFragmentManager().findFragmentByTag(FragmentCategory.class.getName());
                if (fragmentCategory == null) {
                    fragmentCategory = new FragmentCategory();
                }
                selectFragment(fragmentCategory, fragmentTransaction);

                break;
            case 2:
                tv_find.setTextColor(getResources().getColor(R.color.base_bar));
                iv_find.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_yiyao_yes));

                fragmentFind = (FragmentFind) getSupportFragmentManager().findFragmentByTag(FragmentFind.class.getName());
                if (fragmentFind == null) {
                    fragmentFind = new FragmentFind();
                }
                selectFragment(fragmentFind, fragmentTransaction);
                break;
            case 3:
                tv_cart.setTextColor(getResources().getColor(R.color.base_bar));
                iv_cart.setImageDrawable(getResources().getDrawable(R.drawable.icon_home_gouwuche_yes));

                fragmentCart = (FragmentCart) getSupportFragmentManager().findFragmentByTag(FragmentCart.class.getName());
                if (fragmentCart == null) {
                    fragmentCart = new FragmentCart();
                }
                selectFragment(fragmentCart, fragmentTransaction);

                break;
            case 4:
                tv_user.setTextColor(getResources().getColor(R.color.base_bar));
                iv_user.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_user_yes));

                fragmentUser = (FragmentUserIndex) getSupportFragmentManager().findFragmentByTag(FragmentUserIndex.class.getName());
                if (fragmentUser == null) {
                    fragmentUser = new FragmentUserIndex();
                }
                selectFragment(fragmentUser, fragmentTransaction);
                break;

        }
    }

    private void selectFragment(Fragment fragment, FragmentTransaction transaction) {
        if (!fragment.isAdded()) {
            transaction.add(R.id.fragment_container, fragment, fragment.getClass().getName());
        }
        transaction.show(fragment).commit();
    }

    /**
     * 重置底部按钮
     */
    private void reset() {
        tv_home.setTextColor(getResources().getColor(R.color.font_black2));
        tv_category.setTextColor(getResources().getColor(R.color.font_black2));
        tv_find.setTextColor(getResources().getColor(R.color.font_black2));
        tv_cart.setTextColor(getResources().getColor(R.color.font_black2));
        tv_user.setTextColor(getResources().getColor(R.color.font_black2));

        iv_home.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_home_no));
        iv_category.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_fenlei_no));
        iv_find.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_yiyao_no));
        iv_cart.setImageDrawable(getResources().getDrawable(R.drawable.icon_home_gouwuche_no));
        iv_user.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_user_no));
    }

    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        reset();
        if (fragmentHome != null) {
            fragmentTransaction.hide(fragmentHome);
        }
        if (fragmentCategory != null) {
            fragmentTransaction.hide(fragmentCategory);
        }
        if (fragmentFind != null) {
            fragmentTransaction.hide(fragmentFind);
        }
        if (fragmentCart != null) {
            fragmentTransaction.hide(fragmentCart);
        }
        if (fragmentUser != null) {
            fragmentTransaction.hide(fragmentUser);
        }
    }


    /**
     * 双击退出APP
     */
    long lastBackKeyDownTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastBackKeyDownTime > 2000) { // 两秒钟内双击返回键关闭主界面
            ToastUtil.show(this, getResources().getString(R.string.double_tap_to_exit));
            lastBackKeyDownTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    private void loadDefaultLocation() {
        MyLocation loc = MyLocation.newInstance(SingleCurrentUser.defaultLat, SingleCurrentUser.defaultLng, SingleCurrentUser.defaultAddrStr);
        initAddr(loc);
    }


    /**
     * 获取登录着的地址,获得成功则使用,否则使用Location
     */
    private void loadUserAddr() {
        setLoding(this, false);
        new BizDataAsyncTask<List<AddressModel>>() {
            @Override
            protected List<AddressModel> doExecute() throws ZYException, BizFailure {
                return bizUser.selectUserAddress();
            }

            @Override
            protected void onExecuteSucceeded(List<AddressModel> addressModels) {
                closeLoding();
                addrlist = addressModels;
                if (addrlist == null || addrlist.size() <= 0) {
                    //用户没有地址,使用location
                    loadUserLocation();
                } else {
                    //设置默认地址
                    AddressModel model = addrlist.get(0);
                    for (int i = 0; i < addrlist.size(); i++) {
                        if ("0".equals(addrlist.get(i).getDEFAULT_FLG())) {
                            model = addrlist.get(i);
                            break;
                        }
                    }
                    String addr = model.getADDRESS_LOCATION();
                    StringBuilder err = new StringBuilder();
                    MyLocation loc = AddressModel.parseLocation(err, addr);
                    if (err.length() > 0) {
                        loadUserLocation();
                    } else {
                        //upd addr
                        loc.setADDRESS_ID(model.getADDRESS_ID() + "");
                        initAddr(loc);
                    }
                }
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();

    }

    private void getUser() {
        new BizDataAsyncTask<UserModel>() {
            @Override
            protected UserModel doExecute() throws ZYException, BizFailure {
                return bizUser.getUser();
            }

            @Override
            protected void onExecuteSucceeded(UserModel data) {
                SingleCurrentUser.userInfo = data;
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }

    private void loadUserLocation() {
        //request location permission
        Log.e(AppConfig.ERR_TAG, "loadUserLocation");

        String[] perms = {android.Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            //没有权限，进行权限申请,使用默认定位地址
            loadDefaultLocation();
        } else {
            //有权限，直接使用
            regLocListener();
        }


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//        } else {
//            regLocListener();
//        }

    }

    /**
     * 注册位置监听
     */
    private void regLocListener() {
        MyLocation loc = null;
        if (SingleCurrentUser.bdLocation != null) {
            loc = MyLocation.newInstance(SingleCurrentUser.bdLocation.getLatitude(), SingleCurrentUser.bdLocation.getLongitude(), SingleCurrentUser.bdLocation.getAddrStr());
        } else {
            loc = MyLocation.newInstance(SingleCurrentUser.defaultLat, SingleCurrentUser.defaultLng, SingleCurrentUser.defaultAddrStr);
        }
        initAddr(loc);

    }

    public static Intent getIntent(Context ctx) {
        Intent it = new Intent(ctx, MainActivity.class);
        return it;
    }

    /***
     * YX：医药馆商品详情 LX：生活馆商品详情 LL：生活馆列表 YL：医药馆列表 Y
     * D：医药馆店铺 LD：生活馆店铺 DZ：电子币 CJ：抽奖 MZ：满赠 TJ：特价
     * MS：秒杀 KJ：砍价 FL：福利 LY：0元购
     * "name":"积分兑换","value":"JFDH",
     * "name":"医药馆积分商城","value":"JFYL",
     * "name":"生活馆积分商城","value":"JFLL"
     *
     * @param context
     * @param flg
     */
    public static void navPage(Context context, Map m) {

        Intent it = null;
        if (m == null) return;

        String flg = (String) m.get("flg");
        String headId = (String) m.get("headId");
        String advertizeShowName = (String) m.get("advertizeShowName");
        switch (flg) {
            case "ZC":
                if (UserBiz.isLogined()) {
                    if (!StringUtils.isEmpty(advertizeShowName)) {
                        ToastUtil.show(context, advertizeShowName);
                    }
                } else {
                    it = ActivityRegist.getIntent(context);
                }
                break;
            case "YX":
                it = ActivityMedicinesInfo.getIntent(context, headId, "");
                break;
            case "YL":
                it = ActivityMedicineList.getIntent(context, "0", "", headId, "");
                break;
            case "D":
                it = ShopActivity.getIntent(context, headId);
                break;
            case "DZ":
                //电子币
                it = HuoDongActivity.getIntent(context, HuoDongActivity.FLG_HUODONG_DIANZIBI);
                break;
            case "CJ":
                //抽奖
                it = HuoDongActivity.getIntent(context, HuoDongActivity.FLG_HUODONG_CHOJIANG);
                break;
            case "MZ":
                //满就送
                it = HuoDongActivity.getIntent(context, HuoDongActivity.FLG_HUODONG_MANZENG);
                break;
            case "TJ":
                //特价
                it = HuoDongActivity.getIntent(context, HuoDongActivity.FLG_HUODONG_TEJIA);
                break;
            case "MS":
                //秒杀
                it = HuoDongActivity.getIntent(context, HuoDongActivity.FLG_HUODONG_MIAOSHA);
                break;
            case "FL":
                //福利中心
                it = HuoDongActivity.getIntent(context, HuoDongActivity.FLG_HUODONG_LIST);
                break;
            case "ZY":
                //轻松找药
                it = new Intent(context, ActivityFindCategort.class);
                break;
            case "KJ":
                //砍价
                it = HuoDongActivity.getIntent(context, HuoDongActivity.FLG_HUODONG_KANJIA);
                break;
            case "LY":
                //零元购
                it = HuoDongActivity.getIntent(context, HuoDongActivity.FLG_HUODONG_LINGYUANGOU);
                break;
            case "ZX":
                //咨询
                ((BaseActivity) context).openChat("", "", AppConfig.CHAT_XN_LJT_SETTINGID2, AppConfig.CHAT_XN_LJT_TITLE2, false, "");
                break;
            case "JFDH":
                it = PointShopActivity.getIntent(context);
                break;
            case "JFYL":
                //积分商城
                it = PointShopMedicineActivity.getIntent(context);
                break;
            case "LX":
                it = ActivityLifeGoodsInfo.getIntent(context, headId);
                break;
            case "LL":
                it = ActivityGoodsList.getIntent(context, headId, "");
                break;
            case "LD":
                it = LifeShopActivity.getIntent(context, headId);
                break;
            case "JFLL":
                it = PointShopLifeGoodsListActivity.getIntent(context);
                break;
            default:
                break;
        }
        if (it != null)
            context.startActivity(it);
    }

}
