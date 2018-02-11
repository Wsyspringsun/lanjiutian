package com.wyw.ljtds.ui;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jauker.widget.BadgeView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.weixin.uikit.MMAlert;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.model.OnlinePayModel;
import com.wyw.ljtds.ui.base.BaseActivity;
import com.wyw.ljtds.ui.goods.SelDefaultAddressFragment;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.Utils;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestActivity extends BaseActivity {
    private static final String DIALOG_SEL_ADDR = "DIALOG_SEL_ADDR";
    TextView tv;
    private IWXAPI api;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pay_result);
        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
        btn = (Button) findViewById(R.id.pay_result_btn_click);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://wxpay.wxutil.com/pub_v2/app/app_pay.php";
                v.setEnabled(false);
                try {
                    byte[] buf = Utils.getHtmlByteArray(url);
                    if (buf != null && buf.length > 0) {
                        String content = new String(buf);
                        Utils.log("get server pay params:" + content);
                        JSONObject json = new JSONObject(content);
                        if (null != json && !json.has("retcode")) {
                            PayReq req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId = json.getString("appid");
                            req.partnerId = json.getString("partnerid");
                            req.prepayId = json.getString("prepayid");
                            req.nonceStr = json.getString("noncestr");
                            req.timeStamp = json.getString("timestamp");
                            req.packageValue = json.getString("package");
                            req.sign = json.getString("sign");
                            req.extData = "app data"; // optional
                            Toast.makeText(TestActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                            api.sendReq(req);
                        } else {
                            Utils.log("PAY_GET" + "返回错误" + json.getString("retmsg"));
                            Toast.makeText(TestActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Utils.log("PAY_GET" + "服务器请求错误");
                        Toast.makeText(TestActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utils.error("异常：" + e.getMessage());
                }
                v.setEnabled(true);
            }
        });

    }


    public void getIp() {
        new BizDataAsyncTask<String>() {

            @Override
            protected String doExecute() throws ZYException, BizFailure {
                final StringBuilder sbIp = new StringBuilder();
                Utils.getHttpResponse("http://2017.ip138.com/ic.asp", new Utils.HttpResCallback() {
                    @Override
                    public void handle(BufferedReader reader) {
                        String line = null;
                        StringBuilder sb = new StringBuilder();
                        try {
                            while ((line = reader.readLine()) != null)
                                sb.append(line + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Utils.log(sb.toString());
                        Pattern pattern = Pattern
                                .compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                        Matcher matcher = pattern.matcher(sb.toString());
                        if (matcher.find()) {
                            sbIp.append(matcher.group());
                        }
                    }

                    @Override
                    public void preRequest(HttpURLConnection httpConnection) {

                    }
                });
                return sbIp.toString();
            }

            @Override
            protected void onExecuteSucceeded(String s) {
                tv.setText(s);
            }

            @Override
            protected void OnExecuteFailed() {

            }
        }.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Path path = FileSystems.getDefault().getPath(filePath, fileName);
//        MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像
        //testWeixinAlert();




        /*
         * lp.x与lp.y表示相对于原始位置的偏移.
         * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
         * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
         * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
         * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
         * 当参数值包含Gravity.CENTER_HORIZONTAL时
         * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
         * 当参数值包含Gravity.CENTER_VERTICAL时
         * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
         * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
         * Gravity.CENTER_VERTICAL.
         *
         * 本来setGravity的参数值为Gravity.LEFT | Gravity.TOP时对话框应出现在程序的左上角,但在
         * 我手机上测试时发现距左边与上边都有一小段距离,而且垂直坐标把程序标题栏也计算在内了,
         * Gravity.LEFT, Gravity.TOP, Gravity.BOTTOM与Gravity.RIGHT都是如此,据边界有一小段距离
         */
//        lp.width = 300; // 宽度
//        lp.height = 800; // 高度
    }

    /*
        测试微信弹框
     */
    private void testWeixinAlert() {
        //微信接口分享方式
        MMAlert.showAlert(this, getString(R.string.send_webpage),
                this.getResources().getStringArray(R.array.send_webpage_item),
                null, null);
    }

    void testBadgeView() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.fragment_send_ticket, (ViewGroup) findViewById(R.id.fragment_con));
//        final Dialog dialog = new AlertDialog.Builder(this)).setView(layout).create();
        SimpleDraweeView sdv = (SimpleDraweeView) layout.findViewById(R.id.fragment_send_ticket_sdv_show);
        sdv.setImageURI(Uri.parse(AppConfig.IMAGE_PATH_LJT + "/.appinit/regist_ok.png"));

        BadgeView badge = new BadgeView(this);
        badge.setTargetView(sdv);
        badge.setBadgeCount(42);

        Dialog dialog = new Dialog(TestActivity.this, R.style.Theme_AppCompat_Dialog);
//        dialog.setContentView(R.layout.fragment_send_ticket);
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        dialog.setTitle(R.string.gongxi);

//        Window dialogWindow = dialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
//        lp.alpha = 0.9f; // 透明度
        dialog.show();

        View view = layout.findViewById(R.id.btn_sent_ticket);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
