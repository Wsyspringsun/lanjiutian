package com.wyw.ljtds;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.UpdateAppModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.cart.FragmentCart;
import com.wyw.ljtds.ui.category.FragmentCategory;
import com.wyw.ljtds.ui.find.FragmentFind;
import com.wyw.ljtds.ui.home.FragmentLifeIndex;
import com.wyw.ljtds.ui.user.FragmentUser;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.ToastUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;


@ContentView(R.layout.activity_main)//setcontextview
public class MainActivity extends BaseActivity {
    private static final String TAG_POSITION = "TAG_POSITION";
    private IWXAPI wxApi;
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


    //fragment相关
    public static int index = AppConfig.DEFAULT_INDEX_FRAGMENT;
    private FragmentManager fragmentManager;
    //    private FragmentHome fragmentHome;
    private FragmentLifeIndex fragmentHome;
    private FragmentCategory fragmentCategory;
    private FragmentFind fragmentFind;
    private FragmentCart fragmentCart;
    private FragmentUser fragmentUser;

    private UpdateAppModel updateAppModel;

    @Event(value = {R.id.home, R.id.category, R.id.find, R.id.shopping_cart, R.id.user})
    private void setlect(View v) {
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
        addFragmentToStack(index);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAG_POSITION, index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final IWXAPI api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(AppConfig.WEIXIN_APP_ID);
        Log.e(AppConfig.ERR_TAG, "registerApp  WEIXIN_APP_ID...................");
        //注册微信
        StatService.start(this);


        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            //默认
            index = AppConfig.DEFAULT_INDEX_FRAGMENT;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (index) {
                case 0:
                    fragmentHome = FragmentLifeIndex.newInstance();
                    fragmentTransaction.replace(R.id.fragment_container, fragmentHome, fragmentHome.getClass().getName()).commit();
                    break;
                case 2:
                    fragmentFind = new FragmentFind();
                    fragmentTransaction.replace(R.id.fragment_container, fragmentFind, fragmentFind.getClass().getName()).commit();
                    break;
                default:
                    index = 0;
                    break;
            }
        } else {
            index = savedInstanceState.getInt(TAG_POSITION);
            addFragmentToStack(index);
        }
        callback();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        addFragmentToStack(index);
    }

    /**
     * 添加fragment
     *
     * @param cur
     */
    private void addFragmentToStack(int cur) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        cur = cur % 5;
        switch (cur) {
            case 0:
                tv_home.setTextColor(getResources().getColor(R.color.base_bar));
                iv_home.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_home_yes));

//                fragmentHome = (FragmentHome) fragmentManager.findFragmentByTag(FragmentHome.class.getName());
                fragmentHome = (FragmentLifeIndex) fragmentManager.findFragmentByTag(FragmentLifeIndex.class.getName());
                if (fragmentHome == null) {
//                    fragmentHome = new FragmentHome();
                    fragmentHome = FragmentLifeIndex.newInstance();
                }
                selectFragment(fragmentHome, fragmentTransaction);
                break;
            case 1:
                tv_category.setTextColor(getResources().getColor(R.color.base_bar));
                iv_category.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_fenlei_yes));

                fragmentCategory = (FragmentCategory) fragmentManager.findFragmentByTag(FragmentCategory.class.getName());
                if (fragmentCategory == null) {
                    fragmentCategory = new FragmentCategory();
                }
                selectFragment(fragmentCategory, fragmentTransaction);

                break;
            case 2:
                tv_find.setTextColor(getResources().getColor(R.color.base_bar));
                iv_find.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_yiyao_yes));

                fragmentFind = (FragmentFind) fragmentManager.findFragmentByTag(FragmentFind.class.getName());
                if (fragmentFind == null) {
                    fragmentFind = new FragmentFind();
                }

                selectFragment(fragmentFind, fragmentTransaction);
                break;
            case 3:
                tv_cart.setTextColor(getResources().getColor(R.color.base_bar));
                iv_cart.setImageDrawable(getResources().getDrawable(R.drawable.icon_home_gouwuche_yes));

                fragmentCart = (FragmentCart) fragmentManager.findFragmentByTag(FragmentCart.class.getName());
                if (fragmentCart == null) {
                    fragmentCart = new FragmentCart();
                }
                selectFragment(fragmentCart, fragmentTransaction);

                break;
            case 4:
                tv_user.setTextColor(getResources().getColor(R.color.base_bar));
                iv_user.setImageDrawable(getResources().getDrawable(R.mipmap.icon_home_user_yes));

                fragmentUser = (FragmentUser) fragmentManager.findFragmentByTag(FragmentUser.class.getName());
                if (fragmentUser == null) {
                    fragmentUser = new FragmentUser();
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


    /**
     * 更新版本
     */
    private void callback() {
        RequestParams params = new RequestParams(AppConfig.APP_UPDATE_URL);
        Callback.Cancelable cancelable = x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                updateAppModel = GsonUtils.Json2Bean(result, UpdateAppModel.class);
                PackageManager packageManager = getPackageManager();
                // getPackageName()是你当前类的包名，0代表是获取版本信息
                PackageInfo packInfo = null;
                try {
                    packInfo = packageManager.getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String version = packInfo.versionName;
                Log.e(AppConfig.ERR_TAG, version + "; " + updateAppModel.getAndroid());
//                if (!version.equals(updateAppModel.getAndroid())) {
                if (version.compareTo(updateAppModel.getAndroid()) < 0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(
                            MainActivity.this).setTitle("下载更新").setMessage(updateAppModel.getAndroid_update_message())
                            .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    downloadFile(updateAppModel.getAndroid_download_link(), AppConfig.CACHE_ROOT_NAME);
                                }
                            });
                    if (!updateAppModel.getAndroid_force_update().equals("1")) {
                        dialog.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                    }
                    dialog.setCancelable(false).show();

                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

        });
    }

    private void downloadFile(final String url, String path) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path);
        x.http().get(requestParams, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMessage("亲，努力下载中。。。");
                progressDialog.show();
                progressDialog.setMax((int) total);
                progressDialog.setProgress((int) current);
            }

            @Override
            public void onSuccess(File result) {

                progressDialog.dismiss();
                File apkFile = new File(AppConfig.CACHE_ROOT_NAME);
                if (!apkFile.exists()) {
                    return;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ex.printStackTrace();
                ToastUtil.show(MainActivity.this, "下载失败，请检查网络和SD卡");
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
}
