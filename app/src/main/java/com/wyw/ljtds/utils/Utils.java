package com.wyw.ljtds.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class Utils {
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
        DecimalFormat df = new DecimalFormat("0.00");
        BigDecimal bVal = new BigDecimal(val);
        return df.format(bVal);
    }
}
