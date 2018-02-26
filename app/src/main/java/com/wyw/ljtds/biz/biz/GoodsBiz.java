package com.wyw.ljtds.biz.biz;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.CommodityDetailsModel;
import com.wyw.ljtds.model.CreatOrderModel;
import com.wyw.ljtds.model.KeyValue;
import com.wyw.ljtds.model.LogisticsModel;
import com.wyw.ljtds.model.MedicineDetailsEvaluateModel;
import com.wyw.ljtds.model.MedicineDetailsModel;
import com.wyw.ljtds.model.MedicineListModel;
import com.wyw.ljtds.model.MedicineShop;
import com.wyw.ljtds.model.OnlinePayModel;
import com.wyw.ljtds.model.OrderModelInfoMedicine;
import com.wyw.ljtds.model.OrderModelMedicine;
import com.wyw.ljtds.model.ShopImg;
import com.wyw.ljtds.model.ShopModel;
import com.wyw.ljtds.model.ShoppingCartModel;
import com.wyw.ljtds.ui.goods.ActivityGoodsSubmit;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/17 0017.
 */

public class GoodsBiz extends BaseBiz {
    public static String getGoodsIdByBarcode(String barcode) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "getMedicineByBarcode", false);
        ksoap.setProperty("barcode", barcode, SoapProcessor.PropertyType.TYPE_STRING);
        return ksoap.request().getAsString();
    }

    /**
     * 获取商品详情
     *
     * @param commodityId
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static CommodityDetailsModel getGoods(String commodityId) throws BizFailure, ZYException {
        SoapProcessor ksoap;
        if (!StringUtils.isEmpty(PreferenceCache.getToken())) {
            ksoap = new SoapProcessor("Service", "getCommodityDetails", true);
        } else {
            ksoap = new SoapProcessor("Service", "getCommodityDetails", false);
            ksoap.setProperty("token", "", SoapProcessor.PropertyType.TYPE_STRING);
        }


        ksoap.setProperty("commodityId", commodityId, SoapProcessor.PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(element, CommodityDetailsModel.class);
    }


    /**
     * 获取药品详情
     *
     * @param id id
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static MedicineDetailsModel getMedicine(String id, String logiId, String lat, String lng, String addrId) throws BizFailure, ZYException {
        SoapProcessor ksoap = null;
        if ("x".equals(logiId)) {
            if (!StringUtils.isEmpty(PreferenceCache.getToken())) {
                ksoap = new SoapProcessor("Service", "getMedicineByBarcode", true);
            } else {
                ksoap = new SoapProcessor("Service", "getMedicineByBarcode", false);
                ksoap.setProperty("token", "", SoapProcessor.PropertyType.TYPE_STRING);
            }
            ksoap.setProperty("barcode", id, SoapProcessor.PropertyType.TYPE_STRING);
            ksoap.setProperty("lat", lat, SoapProcessor.PropertyType.TYPE_STRING);
            ksoap.setProperty("lng", lng, SoapProcessor.PropertyType.TYPE_STRING);
            ksoap.setProperty("addressId", addrId, SoapProcessor.PropertyType.TYPE_STRING);
            Utils.log("barcode:" + id);
        } else {
            if (!StringUtils.isEmpty(PreferenceCache.getToken())) {
                ksoap = new SoapProcessor("Service", "getMedicine", true);
            } else {
                ksoap = new SoapProcessor("Service", "getMedicine", false);
                ksoap.setProperty("token", "", SoapProcessor.PropertyType.TYPE_STRING);
            }
            ksoap.setProperty("wareId", id, SoapProcessor.PropertyType.TYPE_STRING);
            ksoap.setProperty("logisticsCompanyId", logiId, SoapProcessor.PropertyType.TYPE_STRING);
            ksoap.setProperty("lat", lat, SoapProcessor.PropertyType.TYPE_STRING);
            ksoap.setProperty("lng", lng, SoapProcessor.PropertyType.TYPE_STRING);
            ksoap.setProperty("addressId", addrId, SoapProcessor.PropertyType.TYPE_STRING);
            Utils.log("wareId:" + id);
        }
        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(element, MedicineDetailsModel.class);
    }


    /**
     * 获取药品评价
     *
     * @param id
     * @param startIdx
     * @param pageSize
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<MedicineDetailsEvaluateModel> getEvaluate(String id, String startIdx, String pageSize) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "getEvaluate", false);

        ksoap.setProperty("wareId", id, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("startIdx", startIdx, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("pageSize", pageSize, SoapProcessor.PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<MedicineDetailsEvaluateModel>> tt = new TypeToken<List<MedicineDetailsEvaluateModel>>() {
        };
        List<MedicineDetailsEvaluateModel> fs = gson.fromJson(element, tt.getType());
        List<MedicineDetailsEvaluateModel> bms = new ArrayList<MedicineDetailsEvaluateModel>();
        bms.addAll(fs);
        return bms;
    }


    /**
     * 创建order
     *
     * @param data json样式的string字符串
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static CreatOrderModel getOrderShow(String data, String op) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "order", true);

        ksoap.setProperty("op", op, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(element, CreatOrderModel.class);

    }


    /**
     * 提交订单
     *
     * @param data
     * @param op
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static String submitOrder(String data, String op) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "order", true);

        ksoap.setProperty("op", op, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);

        return ksoap.request().getAsString();
    }


    /**
     * 在线支付
     *
     * @param data 支付方式和订单id
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static String onlinePay(String data) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "order", true);

        ksoap.setProperty("op", "onlinePay", SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);
        return ksoap.requestStr();
//        JsonElement element = ksoap.request();
//        Gson gson = new GsonBuilder().create();
//        return gson.fromJson(element, OnlinePayModel.class);
    }


    /**
     * 发送在线支付成功与否的回执
     *
     * @param data
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static String onlinePayResult(String data) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "order", true);

        ksoap.setProperty("op", "onlinePayResult", SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);

        return ksoap.request().getAsString();

    }


    /**
     * 添加购物车
     *
     * @param data json样式的string字符串
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static String shoppingCart(String data, String op) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "shoppingcart", true);

        ksoap.setProperty("op", op, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);

        return ksoap.request().getAsString();
    }


    /**
     * 显示购物车
     *
     * @param data json样式的string字符串
     * @param op   操作
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static ShoppingCartModel showCart(String data, String op) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "shoppingcart", true);

        ksoap.setProperty("op", op, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);
        Log.e(AppConfig.ERR_TAG, "data:" + data);
        JsonElement el = ksoap.request();
        Gson gson = new GsonBuilder().create();
        TypeToken<ShoppingCartModel> tt = new TypeToken<ShoppingCartModel>() {
        };
        ShoppingCartModel fs = gson.fromJson(el, tt.getType());
        return fs;
    }


    /**
     * 药品订单
     *
     * @param data
     * @param op
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<OrderModelMedicine> getUserOrder(String data, String op) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "order", true);

        ksoap.setProperty("op", op, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<OrderModelMedicine>> tt = new TypeToken<List<OrderModelMedicine>>() {
        };
        List<OrderModelMedicine> fs = gson.fromJson(element, tt.getType());
        List<OrderModelMedicine> bms = new ArrayList<OrderModelMedicine>();
        bms.addAll(fs);
        return bms;
    }


    /**
     * 订单详情
     *
     * @param data
     * @param op
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static OrderModelInfoMedicine getOrderInfo(String data) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "order", true);

        ksoap.setProperty("op", "orderDetail", SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(element, OrderModelInfoMedicine.class);
    }


    /**
     * 写评价
     *
     * @param data
     * @param op
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static String writeEvaluate(String data, String op) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "evaluate", true);

        ksoap.setProperty("op", op, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);
        JsonElement req = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(req, String.class);
    }

    public static String publishEvaluate(String data) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "evaluate", true);
        ksoap.setProperty("op", "create", SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);
        JsonElement req = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(req, String.class);
    }


    /**
     * 查询物流信息
     *
     * @param data
     * @param op
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static LogisticsModel getLogistics(String data, String op) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "order", true);

        ksoap.setProperty("op", op, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(element, LogisticsModel.class);
    }


    /**
     * 订单操作
     *
     * @param data
     * @param op
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static int orderOperation(String data, String op) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "order", true);

        ksoap.setProperty("op", op, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);

        return ksoap.request().getAsInt();
    }


    public static Object returnGoodsHanding(String data, String op) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "returnGoodsHanding", true);

        ksoap.setProperty("op", op, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);

        return ksoap.request();
    }

    public static List<KeyValue> readReturnReasonList() throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "returnReasonList", true);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<KeyValue>> tt = new TypeToken<List<KeyValue>>() {
        };
        List<KeyValue> fs = gson.fromJson(element, tt.getType());
        return fs;
    }

    public static List<ShopImg> readShopPage(String shopid) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "getShopPage", false);
        ksoap.setProperty("oidAdminUserId", shopid, SoapProcessor.PropertyType.TYPE_STRING);
        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        TypeToken<List<ShopImg>> tt = new TypeToken<List<ShopImg>>() {
        };
        List<ShopImg> fs = gson.fromJson(element, tt.getType());
        return fs;
    }

    public static List<MedicineShop> readMedicineShop(String wareId, String lat, String lng) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "getGroupList", false);
        ksoap.setProperty("wareId", wareId, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("lat", lat, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("lng", lng, SoapProcessor.PropertyType.TYPE_STRING);
        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        TypeToken<List<MedicineShop>> tt = new TypeToken<List<MedicineShop>>() {
        };
        List<MedicineShop> fs = gson.fromJson(element, tt.getType());
        return fs;
    }

    public static List<MedicineListModel> loadGoodsOfShop(String data) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "shop", false);
        ksoap.setProperty("op", "shopMedicineList", SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);
        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        TypeToken<List<MedicineListModel>> tt = new TypeToken<List<MedicineListModel>>() {
        };
        List<MedicineListModel> fs = gson.fromJson(element, tt.getType());
        return fs;
    }

    /**
     * 获取全店推荐商品列表
     *
     * @param data
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<MedicineListModel> loadGoodsOfHot(String data) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "shop", false);
        ksoap.setProperty("op", "shopHotMedicineList", SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);
        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        TypeToken<List<MedicineListModel>> tt = new TypeToken<List<MedicineListModel>>() {
        };
        List<MedicineListModel> fs = gson.fromJson(element, tt.getType());
        return fs;
    }

    public static ShopModel loadShopInfo(String data) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "shop", false);
        ksoap.setProperty("op", "shopParameters", SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);
        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();
        TypeToken<ShopModel> tt = new TypeToken<ShopModel>() {
        };
        ShopModel fs = gson.fromJson(element, tt.getType());
        return fs;
    }
}
