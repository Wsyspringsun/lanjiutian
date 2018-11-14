package com.wyw.ljtds.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.weixin.uikit.MMAlert;
import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.SoapProcessor;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.MyApplication;
import com.wyw.ljtds.ui.user.manage.UserInfoExtraActivity;
import com.wyw.ljtds.ui.user.order.LogisticTraceActivity;

import junit.framework.Assert;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class Utils {
    private static final int MMAlertSelect1 = 0; //选择分享的渠道
    private static final int MMAlertSelect2 = 1;
    private static final int MMAlertSelect3 = 2;


    public static final String RESPONSE_STATUS = "status";
    public static final String RESPONSE_MESSAGE = "message";
    public static final String RESPONSE_RESULT = "result";
    public static final String RESPONSE_STATUSCODE = "statusCode";

    private static String REG_PATTERN_PHONE = "\\d{7}|0\\d{10}|13\\d{9}|14\\d{9}|15\\d{9}|17\\d{9}|18\\d{9}";
    private static final String TAG = "SDK_Sample.Util";
    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public final static String CoorType_GCJ02 = "gcj02";
    public final static String CoorType_BD09LL = "bd09ll";
    public final static String CoorType_BD09MC = "bd09";
    /***
     * 61 ： GPS定位结果，GPS定位成功。
     * 62 ： 无法获取有效定位依据，定位失败，请检查运营商网络或者wifi网络是否正常开启，尝试重新请求定位。
     * 63 ： 网络异常，没有成功向服务器发起请求，请确认当前测试手机网络是否通畅，尝试重新请求定位。
     * 65 ： 定位缓存的结果。
     * 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
     * 67 ： 离线定位失败。通过requestOfflineLocaiton调用时对应的返回结果。
     * 68 ： 网络连接失败时，查找本地离线定位时对应的返回结果。
     * 161： 网络定位结果，网络定位定位成功。
     * 162： 请求串密文解析失败。
     * 167： 服务端定位失败，请您检查是否禁用获取位置信息权限，尝试重新请求定位。
     * 502： key参数错误，请按照说明文档重新申请KEY。
     * 505： key不存在或者非法，请按照说明文档重新申请KEY。
     * 601： key服务被开发者自己禁用，请按照说明文档重新申请KEY。
     * 602： key mcode不匹配，您的ak配置过程中安全码设置有问题，请确保：sha1正确，“;”分号是英文状态；且包名是您当前运行应用的包名，请按照说明文档重新申请KEY。
     * 501～700：key验证失败，请按照说明文档重新申请KEY。
     */

    public static float[] EARTH_WEIGHT = {0.1f, 0.2f, 0.4f, 0.6f, 0.8f}; // 推算计算权重_地球
    //public static float[] MOON_WEIGHT = {0.0167f,0.033f,0.067f,0.1f,0.133f};
    //public static float[] MARS_WEIGHT = {0.034f,0.068f,0.152f,0.228f,0.304f};


    /**
     * 保留后两位小数点
     *
     * @param number
     * @param style  1：直接去掉  2：四舍五入
     * @return
     */
    public static BigDecimal DoubleFormat(String number, int style) {

        BigDecimal bigDecimal = new BigDecimal(number);
        if (style == 1) {
            bigDecimal.setScale(2, BigDecimal.ROUND_DOWN);
        } else if (style == 2) {
            bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        return bigDecimal;
    }

    //银联支付验签
    public static boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 在左边拼接字符，达到制定总长度
     *
     * @param sH 要进行拼接的字符串
     * @param s  拼接的字符
     * @param i  最终结果的总长度
     * @return
     */
    public static String leftPad(String sH, String s, int i) {
        if (sH == null) return "";
        String temp = sH;
        while (temp.length() < i) {
            temp = s + temp;
        }
        return temp;
    }

    /**
     * 图片保存为文件
     *
     * @param filename
     * @param bitmap
     */
    public static void saveFile(String filename, Bitmap bitmap) {
        File f = new File(filename);
        FileOutputStream fos = null;
        try {
            f.createNewFile();
            fos = new FileOutputStream(f);
            Bitmap.CompressFormat cprsFormat = Bitmap.CompressFormat.PNG;
            bitmap.compress(cprsFormat, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showErrMsg(Activity context, String err) {
        ToastUtil.show(context, err);
    }

    public static Integer[] range(int start, int end) {
        Integer[] arr = new Integer[end - start];
        int i = 0, x = start;
        while (x < end) {
            arr[i] = x;
            x++;
            i++;
        }
        return arr;
    }

    public static <T> String join(T[] arr, String s) {
        if (arr == null) return "";
        if (arr.length <= 0) return "";
        StringBuilder sb = new StringBuilder();
        for (T i : arr) {
            sb.append(String.valueOf(i)).append(s);
        }
        if (sb.lastIndexOf(s) == sb.length() - 1)
            return sb.substring(0, sb.length());
        return sb.toString();
    }


    public interface HttpResCallback {
        void handle(BufferedReader reader);

        void preRequest(HttpURLConnection httpConnection);
    }

    public static void getHttpResponse(String urlPath, HttpResCallback callback) {
        URL infoUrl = null;
        InputStream inStream = null;
        HttpURLConnection httpConnection = null;
        try {
            infoUrl = new URL(urlPath);
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;

            if (callback != null) {
                callback.preRequest(httpConnection);
            }
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));
                callback.handle(reader);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
                if (httpConnection != null)
                    httpConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] getBytesFromUrl(String urlPath) throws IOException {
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(conn.getResponseMessage() + " url:" + urlPath);
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream is = conn.getInputStream();

            int readCnt = -1;
            byte[] buffer = new byte[1024];
            while ((readCnt = is.read(buffer)) > 0) {
                bos.write(buffer, 0, readCnt);
            }
            bos.close();
            return bos.toByteArray();
        } finally {
            conn.disconnect();
        }
    }


    public static Bitmap getBitMapFromUrl(String url) {
        try {
            byte[] data = getBytesFromUrl(url);
            Bitmap bitMap = BitmapFactory.decodeByteArray(data, 0, data.length);
            return bitMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 480);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }


    public static String formatFee(String val) {
        if (StringUtils.isEmpty(val))
            return "";
        DecimalFormat df = new DecimalFormat("0.##");
        BigDecimal bVal = new BigDecimal(val);
        return df.format(bVal);
    }

    public static boolean validPhoneNum(String phone) {
        return Pattern.matches(REG_PATTERN_PHONE, phone);
    }

    public static void error(String content) {
//        if (!AppConfig.test) return;
        StackTraceElement[] trace = new Throwable().getStackTrace();
        if (trace != null && trace.length > 1) {
            int segmentSize = 3 * 1024;
            long length = content.length();
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.e(AppConfig.ERR_TAG, trace[1].getClassName() + "#" + trace[1].getMethodName() + " > " + trace[1].getLineNumber() + " :" + content);
            } else {
                Log.e(AppConfig.ERR_TAG, trace[1].getClassName() + "#" + trace[1].getMethodName() + " > " + trace[1].getLineNumber() + " :");
                while (content.length() > segmentSize) {// 循环分段打印日志
                    String logContent = content.substring(0, segmentSize);
                    content = content.replace(logContent, "");
                    Log.e(AppConfig.ERR_TAG, "> " + logContent);
                }
                Log.e(AppConfig.ERR_TAG, " > " + content);
            }
        }
    }

    public static void log(String content) {
//        if (!AppConfig.test) return;
        StackTraceElement[] trace = new Throwable().getStackTrace();
        if (trace != null && trace.length > 1) {
            int segmentSize = 3 * 1024;
            if (StringUtils.isEmpty(content)) return;
            long length = content.length();
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.i(AppConfig.ERR_TAG, trace[1].getClassName() + "#" + trace[1].getMethodName() + " > " + trace[1].getLineNumber() + " :" + content);
            } else {
                Log.i(AppConfig.ERR_TAG, trace[1].getClassName() + "#" + trace[1].getMethodName() + " > " + trace[1].getLineNumber() + " :");
                while (content.length() > segmentSize) {// 循环分段打印日志
                    String logContent = content.substring(0, segmentSize);
                    content = content.replace(logContent, "");
                    Log.i(AppConfig.ERR_TAG, "> " + logContent);
                }
                Log.i(AppConfig.ERR_TAG, " > " + content);
            }
        }
    }

    public static JsonElement parseResponse(String response) throws ZYException, BizFailure {
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(response).getAsJsonObject();
        JsonElement resultElement;

        boolean status = jsonObject.get(RESPONSE_STATUS).getAsBoolean();
        if (!status) {
            String message = jsonObject.get(RESPONSE_MESSAGE).getAsString();
            throw new BizFailure(message);
        }

        resultElement = jsonObject.get(RESPONSE_RESULT);
        return resultElement;
    }

    public static void debug(String content) {
//        if (!AppConfig.test) return;
        StackTraceElement[] trace = new Throwable().getStackTrace();
        if (trace != null && trace.length > 1) {
            int segmentSize = 3 * 1024;
            long length = content.length();
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.d(AppConfig.ERR_TAG, trace[1].getClassName() + "#" + trace[1].getMethodName() + " > " + trace[1].getLineNumber() + " :" + content);
            } else {
                Log.d(AppConfig.ERR_TAG, trace[1].getClassName() + "#" + trace[1].getMethodName() + " > " + trace[1].getLineNumber() + " :");
                while (content.length() > segmentSize) {// 循环分段打印日志
                    String logContent = content.substring(0, segmentSize);
                    content = content.replace(logContent, "");
                    Log.d(AppConfig.ERR_TAG, "> " + logContent);
                }
                Log.d(AppConfig.ERR_TAG, " > " + content);
            }
        }
    }

    public static byte[] bmpToByteArray(final Bitmap.CompressFormat cprsFormat, final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
//        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        bmp.compress(cprsFormat, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            Utils.error(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Utils.error(e.getMessage());
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);

        return data;
    }

    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            Log.i(TAG, "readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        Log.d(TAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

        if (offset < 0) {
            Log.e(TAG, "readFromFile invalid offset:" + offset);
            return null;
        }
        if (len <= 0) {
            Log.e(TAG, "readFromFile invalid len:" + len);
            return null;
        }
        if (offset + len > (int) file.length()) {
            Log.e(TAG, "readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len]; // 创建合适文件大小的数组
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            Log.e(TAG, "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        return null;
    }

    public static Bitmap getQRCodeBitmap(Activity context, String content) {
        int width = 200;
        int height = 200;
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);// 生成矩阵

            int bitMatrixWidth = bitMatrix.getWidth();

            int bitMatrixHeight = bitMatrix.getHeight();

            int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

            for (int y = 0; y < bitMatrixHeight; y++) {
                int offset = y * bitMatrixWidth;

                for (int x = 0; x < bitMatrixWidth; x++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        pixels[offset + x] = bitMatrix.get(x, y) ? context.getColor(R.color.black) : context.getColor(R.color.white);
                    } else {
                        pixels[offset + x] = bitMatrix.get(x, y) ? context.getResources().getColor(R.color.black) : context.getResources().getColor(R.color.white);
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
            bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static void setIconText(Context context, TextView tv, String txt) {
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
        tv.setTypeface(iconfont);
        tv.setText(txt);
    }

    /**
     * 获取设备唯一编号
     *
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        String imei;
        try {
            imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } catch (Exception e) {
            imei = System.currentTimeMillis() + "";
        }
        return imei;
    }

    public static String trim(String val) {
        if (StringUtils.isEmpty(val)) return "";
        return val.trim();
    }

    /**
     * 将数据存入    * @param context
     *
     * @param cacheFileName
     * @param mode
     * @param content
     */
    public static void setCache2File(Context context, String cacheFileName, int mode, String content) {
        FileOutputStream fos = null;
        try {
            //打开文件输出流，接收参数是文件名和模式
            fos = context.openFileOutput(cacheFileName, mode);
            fos.write(content.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从 File 读取
     *
     * @param context
     * @param cacheFileName
     * @return
     */
    public static String getCache2File(Context context, String cacheFileName) {
        FileInputStream fis = null;
        StringBuffer sBuf = new StringBuffer();
        try {
            fis = context.openFileInput(cacheFileName);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = fis.read(buf)) != -1) {
                sBuf.append(new String(buf, 0, len));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (sBuf != null) {
            return sBuf.toString();
        }
        return null;
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            Utils.log("data:" + inetAddress.getHostAddress());
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
            ToastUtil.show(context, "当前无网络连接");
        }
        return "";
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static PayUtil getPayUtilInstance(Activity context, Map data) {
        PayUtil instance = new PayUtil(context, data);
        return instance;
    }

    public static void wechatShare(final Context context, final String title, final String description, final String imgUrl, final String url) {
        final IWXAPI wxApi = ((MyApplication) ((Activity) context).getApplication()).wxApi;
        Utils.log("wechatShare:" + title + "-" + description + "-" + imgUrl + "-" + url);
        MMAlert.showAlert(context, "分享", context.getResources().getStringArray(R.array.send_webpage_item),
                null, new MMAlert.OnAlertSelectId() {
                    @Override
                    public void onClick(int whichButton) {
                        Utils.log("whichButton:" + whichButton + ";imgUrl:" + imgUrl);
                        WXMediaMessage msg = null;
                        SendMessageToWX.Req req = null;

                        WXWebpageObject webpage = new WXWebpageObject();
                        webpage.webpageUrl = url;
                        msg = new WXMediaMessage(webpage);
                        msg.title = title;
                        msg.description = description;
                        //默认图片
                        Bitmap bmpDefault = BitmapFactory.decodeResource(context.getResources(), R.drawable.send_music_thumb);
                        Bitmap.CompressFormat cprsFormatDefault = Bitmap.CompressFormat.PNG;
                        msg.thumbData = Utils.bmpToByteArray(cprsFormatDefault, bmpDefault, true);

                        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.send_music_thumb);
                        Bitmap.CompressFormat cprsFormat = Bitmap.CompressFormat.PNG;
                        if (!StringUtils.isEmpty(imgUrl)) {
                            int fixIdx = imgUrl.lastIndexOf('.');
                            if (fixIdx > 0) {
                                String fix = imgUrl.substring(fixIdx);
                                Utils.log("fix:" + fix);
                                if ("jpg".equals(fix.toLowerCase())) {
                                    cprsFormat = Bitmap.CompressFormat.JPEG;
                                }
                                try {
                                    bmp = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }


                        }
                        if (bmp != null && !bmp.isRecycled()) {
                            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, AppConfig.THUMB_SIZE, AppConfig.THUMB_SIZE, true);
                            msg.thumbData = Utils.bmpToByteArray(cprsFormat, thumbBmp, true);
                            bmp.recycle();
                        }


                        req = new SendMessageToWX.Req();
                        req.transaction = buildTransaction("webpage");
                        req.message = msg;

                        switch (whichButton) {
                            case MMAlertSelect1:
                                req.scene = SendMessageToWX.Req.WXSceneSession;
                                break;
                            case MMAlertSelect2:
                                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                                break;
                            case MMAlertSelect3:
                                req.scene = SendMessageToWX.Req.WXSceneFavorite;
                                break;
                        }

                        wxApi.sendReq(req);
                    }
                });
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public static void loadNetTime(Context context, LocationListener listner) {
//        通过网络或者GPS的方式。
        LocationManager locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //获取最近一次知道的时间
        //        long networkTS = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getTime();

        //        或者实时的获取时间：
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listner); //获取当前时间
    }
}
