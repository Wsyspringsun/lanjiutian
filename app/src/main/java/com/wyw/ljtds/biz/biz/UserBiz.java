package com.wyw.ljtds.biz.biz;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.wyw.ljtds.biz.biz.SoapProcessor.PropertyType;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.AreaModel;
import com.wyw.ljtds.model.BalanceRecord;
import com.wyw.ljtds.model.ChoJiangRec;
import com.wyw.ljtds.model.DianZiBiLog;
import com.wyw.ljtds.model.FavoriteModel;
import com.wyw.ljtds.model.FootprintModel;
import com.wyw.ljtds.model.MessageModel;
import com.wyw.ljtds.model.PointRecord;
import com.wyw.ljtds.model.UserDataModel;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.model.WalletModel;
import com.wyw.ljtds.model.Ticket;
import com.wyw.ljtds.ui.user.wallet.ChojiangRecActivity;
import com.wyw.ljtds.ui.user.wallet.DianZiBiListFragment;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20 0020.
 */

public class UserBiz extends BaseBiz {
    private static boolean logined;

    /**
     * 注册方法
     *
     * @param phoneNum     手机号
     * @param verifyCode   短信验证码
     * @param password     密码
     * @param invitor      推荐人手机号
     * @param terminalType android端传0
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static String register(String phoneNum, String verifyCode
            , String password, String invitor, String terminalType) throws BizFailure, ZYException {

        SoapProcessor ksoap2 = new SoapProcessor("Service", "regist",
                false);

        ksoap2.setProperty("phoneNumber", phoneNum, PropertyType.TYPE_STRING);
        ksoap2.setProperty("verifyCode", verifyCode, PropertyType.TYPE_STRING);
        ksoap2.setProperty("passWord", password, PropertyType.TYPE_STRING);
        ksoap2.setProperty("introducer", invitor, PropertyType.TYPE_STRING);
        ksoap2.setProperty("terminalType", terminalType, PropertyType.TYPE_STRING);

        return ksoap2.request().getAsString();
    }


    /**
     * 登陆方法
     *
     * @param userName     手机号
     * @param password     密码
     * @param terminalType android端传0
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static String login(String userName, String password, String terminalType) throws BizFailure, ZYException {

        SoapProcessor ksoap = new SoapProcessor("Service", "login", false);

        ksoap.setProperty("userName", userName, PropertyType.TYPE_STRING);
        ksoap.setProperty("passWord", password, PropertyType.TYPE_STRING);
        ksoap.setProperty("terminalType", terminalType, PropertyType.TYPE_STRING);
        return ksoap.request().getAsString();
    }


    /**
     * 申请验证码
     *
     * @param mobile
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static String VerificationCode(String mobile, String sendType) throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "getRegMobileCode", false);

        ksoap2.setProperty("mobile", mobile, PropertyType.TYPE_STRING);
        ksoap2.setProperty("sendType", sendType, PropertyType.TYPE_STRING);

        return ksoap2.request().getAsString();
    }


    /**
     * 找回密码
     *
     * @param mobile
     * @param verifyCode
     * @param passWord
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static boolean findPassWord(String mobile, String verifyCode, String passWord) throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "findPwd", false);

        ksoap2.setProperty("mobile", mobile, PropertyType.TYPE_STRING);
        ksoap2.setProperty("verifyCode", verifyCode, PropertyType.TYPE_STRING);
        ksoap2.setProperty("passWord", passWord, PropertyType.TYPE_STRING);

        return ksoap2.request().getAsBoolean();
    }


    /**
     * @param oldPwd
     * @param passWord
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static boolean modifyPwd(String oldPwd, String passWord) throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "modifyPwd", true);

        ksoap2.setProperty("oldPwd", oldPwd, PropertyType.TYPE_STRING);
        ksoap2.setProperty("passWord", passWord, PropertyType.TYPE_STRING);

        return ksoap2.request().getAsBoolean();
    }


    /**
     * 实名认证
     *
     * @param realName 姓名
     * @param cardId   身份证号
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static int realNameAuth(String realName, String cardId)
            throws BizFailure, ZYException {

        SoapProcessor ksoap = new SoapProcessor("Service", "realNameAuth", true);

        ksoap.setProperty("realName", realName, PropertyType.TYPE_STRING);
        ksoap.setProperty("cardId", cardId, PropertyType.TYPE_STRING);

        return ksoap.request().getAsInt();
    }

    /**
     * 获取省列表
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<AreaModel> getProvince() throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "province", false);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<AreaModel>> tt = new TypeToken<List<AreaModel>>() {
        };
        List<AreaModel> fs = gson.fromJson(element, tt.getType());
        List<AreaModel> bms = new ArrayList<>();
        bms.addAll(fs);
        return bms;
    }


    /**
     * 获取市列表
     *
     * @param parentId 省id
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<AreaModel> getcity(int parentId) throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "city", false);

        ksoap2.setProperty("parentId", parentId, PropertyType.TYPE_INTEGER);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<AreaModel>> tt = new TypeToken<List<AreaModel>>() {
        };
        List<AreaModel> fs = gson.fromJson(element, tt.getType());
        return fs;
    }

    public static List<AreaModel> getDistrict(int parentId) throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "district", false);

        ksoap2.setProperty("parentId", parentId, PropertyType.TYPE_INTEGER);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<AreaModel>> tt = new TypeToken<List<AreaModel>>() {
        };
        List<AreaModel> fs = gson.fromJson(element, tt.getType());
        return fs;
    }


    /**
     * 新增收货地址
     *
     * @param consigneeName     收货人
     * @param consigneeMobile   手机号
     * @param consigneeZipCode  邮编
     * @param consigneeProvince 省
     * @param consigneeCity     市
     * @param consigneeAddress  详细地址
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static int addUserAddress(AddressModel model) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "addUserAddress2", true);
        ksoap.setProperty("consigneeName", model.getCONSIGNEE_NAME(), PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeMobile", model.getCONSIGNEE_MOBILE(), PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeZipCode", "048000", PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeProvice", model.getCONSIGNEE_PROVINCE(), PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeCity", model.getCONSIGNEE_CITY(), PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeCounty", model.getCONSIGNEE_COUNTY(), PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeAddress", model.getCONSIGNEE_ADDRESS(), PropertyType.TYPE_STRING);
        ksoap.setProperty("addressLocation", model.getADDRESS_LOCATION(), PropertyType.TYPE_STRING);


        return ksoap.request().getAsInt();
    }


    /**
     * 修改收货地址
     *
     * @param addressId         地址id
     * @param consigneeName     收货人
     * @param consigneeMobile   手机号
     * @param consigneeZipCode  邮编
     * @param consigneeProvince 省
     * @param consigneeCity     市
     * @param consigneeAddress  详细地址
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static int updateUserAddress(AddressModel model) throws BizFailure, ZYException {

        SoapProcessor ksoap = new SoapProcessor("Service", "updateUserAddress2", false);

        ksoap.setProperty("addressId", model.getADDRESS_ID(), PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeName", model.getCONSIGNEE_NAME(), PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeMobile", model.getCONSIGNEE_MOBILE(), PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeZipCode", "048000", PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeProvice", model.getCONSIGNEE_PROVINCE(), PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeCity", model.getCONSIGNEE_CITY(), PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeCounty", model.getCONSIGNEE_COUNTY(), PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeAddress", model.getCONSIGNEE_ADDRESS(), PropertyType.TYPE_STRING);
        return ksoap.request().getAsInt();
    }


    /**
     * 删除收货地址
     *
     * @param addressId
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static int deleteUserAddress(String addressId) throws BizFailure, ZYException {

        SoapProcessor ksoap = new SoapProcessor("Service", "deleteUserAddress", false);

        ksoap.setProperty("addressId", addressId, PropertyType.TYPE_STRING);

        return ksoap.request().getAsInt();

    }


    public static int changeDefaultAddress(String addressId) throws BizFailure, ZYException {

        SoapProcessor ksoap = new SoapProcessor("Service", "changeDefaultAddress", true);

        ksoap.setProperty("addressId", addressId, PropertyType.TYPE_STRING);

        return ksoap.request().getAsInt();

    }

    /**
     * 获取收货地址
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<AddressModel> selectUserAddress() throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "selectUserAddress", true);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<AddressModel>> tt = new TypeToken<List<AddressModel>>() {
        };
        List<AddressModel> fs = gson.fromJson(element, tt.getType());
        List<AddressModel> bms = new ArrayList<AddressModel>();
        bms.addAll(fs);
        return bms;
    }


    /**
     * 获取个人账户信息
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static UserModel getUser() throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "getPerInformation", true);
        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(element, UserModel.class);

    }

    public static UserDataModel loadUserOrderNumber() throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "sumOrderAmount", true);
        ksoap.setProperty("token", PreferenceCache.getToken(), PropertyType.TYPE_STRING);
        JsonElement element = ksoap.request();

        Gson gson = new GsonBuilder().create();
        TypeToken<UserDataModel> tt = new TypeToken<UserDataModel>() {
        };
        UserDataModel fs = gson.fromJson(element, tt.getType());
        return fs;
    }

    /**
     * 获取钱包信息
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static WalletModel getWallet() throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "myWallet", true);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<WalletModel> tt = new TypeToken<WalletModel>() {
        };
        WalletModel fs = gson.fromJson(element, tt.getType());
        return fs;
    }


    /**
     * 获取站内信
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<MessageModel> getMessage(String msgType, String token, String firstIdx, String maxCount)
            throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "message", false);

        ksoap.setProperty("msgType", msgType, PropertyType.TYPE_STRING);
        ksoap.setProperty("token", token, PropertyType.TYPE_STRING);
        ksoap.setProperty("firstIdx", firstIdx, PropertyType.TYPE_STRING);
        ksoap.setProperty("maxCount", maxCount, PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<MessageModel>> tt = new TypeToken<List<MessageModel>>() {
        };
        List<MessageModel> fs = gson.fromJson(element, tt.getType());
        List<MessageModel> bms = new ArrayList<MessageModel>();
        bms.addAll(fs);
        return bms;
    }


    /**
     * 获取收藏夹列表
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<FavoriteModel> getFavorite() throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "favoritesGoods", true);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<FavoriteModel>> tt = new TypeToken<List<FavoriteModel>>() {
        };
        List<FavoriteModel> fs = gson.fromJson(element, tt.getType());
        List<FavoriteModel> bms = new ArrayList<FavoriteModel>();
        bms.addAll(fs);
        return bms;
    }

    /**
     * load user scan history
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<FavoriteModel> getHistory(int page) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "getClicksRanking", true);
        ksoap.setProperty("firstIdx", page, PropertyType.TYPE_INTEGER);
        ksoap.setProperty("maxCount", AppConfig.DEFAULT_PAGE_COUNT, PropertyType.TYPE_INTEGER);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<FavoriteModel>> tt = new TypeToken<List<FavoriteModel>>() {
        };
        List<FavoriteModel> fs = gson.fromJson(element, tt.getType());
        List<FavoriteModel> bms = new ArrayList<FavoriteModel>();
        bms.addAll(fs);
        return bms;
    }


    /**
     * 添加收藏
     *
     * @param commodityId id
     * @param goodsFlg    0是生活 1 是医药
     * @throws BizFailure
     * @throws ZYException
     */
    public static boolean addFavoritesGoods(String commodityId, String goodsFlg) throws BizFailure, ZYException {

        SoapProcessor ksoap = new SoapProcessor("Service", "addFavoritesGoods", true);

        ksoap.setProperty("commodityId", commodityId, PropertyType.TYPE_STRING);
        ksoap.setProperty("goodsFlg", goodsFlg, PropertyType.TYPE_STRING);

        return ksoap.request().getAsBoolean();

    }


    /**
     * 删除收藏
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static boolean deleteFavoritesGoods(String commodityId) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "delFavoritesGoods", true);

        ksoap.setProperty("commodityId", commodityId, PropertyType.TYPE_INTEGER);

        return ksoap.request().getAsBoolean();
    }


    /**
     * 批量删除收藏
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static boolean mulDelFavoritesGoods(String ids) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "mulDelFavoritesGoods", true);

        ksoap.setProperty("ids", ids, PropertyType.TYPE_INTEGER);

        return ksoap.request().getAsBoolean();
    }


    /**
     * 获取我的足迹列表
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<FootprintModel> getFootprint(String firstIdx, String maxCount) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "getClicksRanking", true);

        ksoap.setProperty("firstIdx", firstIdx, PropertyType.TYPE_STRING);
        ksoap.setProperty("maxCount", maxCount, PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<FootprintModel>> tt = new TypeToken<List<FootprintModel>>() {
        };
        List<FootprintModel> fs = gson.fromJson(element, tt.getType());
        List<FootprintModel> bms = new ArrayList<FootprintModel>();
        bms.addAll(fs);
        return bms;
    }


    /**
     * 添加我的足迹
     *
     * @return 1成功   0失败
     * @throws BizFailure
     * @throws ZYException
     */
    public static int clicksRanking(String commodityId, String token) throws BizFailure, ZYException {

        SoapProcessor ksoap = new SoapProcessor("Service", "clicksRanking", false);

        ksoap.setProperty("commodityId", commodityId, PropertyType.TYPE_STRING);
        ksoap.setProperty("token", PreferenceCache.getToken(), PropertyType.TYPE_STRING);

        return ksoap.request().getAsInt();

    }


    /**
     * 删除足迹
     *
     * @param clicksRankingId 足迹id
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static int deleteClicksRanking(int clicksRankingId) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "deleteClicksRanking", false);

        ksoap.setProperty("clicksRankingId", clicksRankingId, PropertyType.TYPE_INTEGER);

        return ksoap.request().getAsInt();
    }


    /**
     * 上传头像
     *
     * @param data
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static String putIcon(String data) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "myPhoto", true);

        ksoap.setProperty("data", data, SoapProcessor.PropertyType.TYPE_STRING);

        return ksoap.request().getAsString();
    }


    /**
     * 完善用户信息
     *
     * @param nickname 昵称
     * @param birthday 生日
     * @param sex      性别
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static int addPerInfomation(String nickname, String birthday, String sex) throws BizFailure, ZYException {
        SoapProcessor ksoap = new SoapProcessor("Service", "addPerInfomation", true);

        ksoap.setProperty("nickname", nickname, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("birthday", birthday, SoapProcessor.PropertyType.TYPE_STRING);
        ksoap.setProperty("sex", sex, SoapProcessor.PropertyType.TYPE_STRING);

        return ksoap.request().getAsInt();

    }

    public static List<BalanceRecord> readBalance(String m, String page, String count) throws BizFailure, ZYException {
        String method = "getBalance";
        SoapProcessor sp = new SoapProcessor("Service", method, true);
        sp.setProperty("method", m, PropertyType.TYPE_STRING);
        sp.setProperty("firstIdx", page, PropertyType.TYPE_STRING);
        sp.setProperty("maxCount", count, PropertyType.TYPE_STRING);

        JsonElement element = sp.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<BalanceRecord>> tt = new TypeToken<List<BalanceRecord>>() {
        };
        List<BalanceRecord> fs = gson.fromJson(element, tt.getType());
//        String data = "[ { \"ROWNUMBER\": 1, \"BANKCARDNO\": \"108888\", \"BUSNO\": \"000\", \"CARDHOLDER\": \"电商测试\", \"CARDADDRESS\": \"市医院\", \"CARDCOMPANY\": \"蓝九天电商\", \"CARDINVALIDATE\": 4102329600000, \"STATUS\": 1, \"CARDBALANCE\": 932.31, \"SEX\": \"男  \", \"TEL\": \"\", \"HANDSET\": \"15721669800\", \"CREATEUSER\": \"168\", \"CREATETIME\": 1498699546150, \"ADDNO\": \"1706290000004\", \"ADDMONEY_BUSNO\": \"000\", \"ADDMONEY_CREATEUSER\": \"168\", \"ADDMONEY_CREATETIME\": 1498699918803, \"ADDAMOUNT\": 400, \"ADDMONEY_CARDBALANCE\": 1000, \"ADDMONEY_NOTES\": \"测试\", \"ORGNAME\": \"山西蓝九天药业连锁有限公司\" }, { \"ROWNUMBER\": 2, \"BANKCARDNO\": \"108888\", \"BUSNO\": \"000\", \"CARDHOLDER\": \"电商测试\", \"CARDADDRESS\": \"市医院\", \"CARDCOMPANY\": \"蓝九天电商\", \"CARDINVALIDATE\": 4102329600000, \"STATUS\": 1, \"CARDBALANCE\": 932.31, \"SEX\": \"男  \", \"TEL\": \"\", \"HANDSET\": \"15721669800\", \"CREATEUSER\": \"168\", \"CREATETIME\": 1498699546150, \"ADDNO\": \"1706290000003\", \"ADDMONEY_BUSNO\": \"000\", \"ADDMONEY_CREATEUSER\": \"168\", \"ADDMONEY_CREATETIME\": 1498699885537, \"ADDAMOUNT\": 200, \"ADDMONEY_CARDBALANCE\": 600, \"ADDMONEY_NOTES\": \"测试\", \"ORGNAME\": \"山西蓝九天药业连锁有限公司\" }, { \"ROWNUMBER\": 3, \"BANKCARDNO\": \"108888\", \"BUSNO\": \"000\", \"CARDHOLDER\": \"电商测试\", \"CARDADDRESS\": \"市医院\", \"CARDCOMPANY\": \"蓝九天电商\", \"CARDINVALIDATE\": 4102329600000, \"STATUS\": 1, \"CARDBALANCE\": 932.31, \"SEX\": \"男  \", \"TEL\": \"\", \"HANDSET\": \"15721669800\", \"CREATEUSER\": \"168\", \"CREATETIME\": 1498699546150, \"ADDNO\": \"1706290000002\", \"ADDMONEY_BUSNO\": \"000\", \"ADDMONEY_CREATEUSER\": \"168\", \"ADDMONEY_CREATETIME\": 1498699821493, \"ADDAMOUNT\": 300, \"ADDMONEY_CARDBALANCE\": 400, \"ADDMONEY_NOTES\": \"测试\", \"ORGNAME\": \"山西蓝九天药业连锁有限公司\" }, { \"ROWNUMBER\": 4, \"BANKCARDNO\": \"108888\", \"BUSNO\": \"000\", \"CARDHOLDER\": \"电商测试\", \"CARDADDRESS\": \"市医院\", \"CARDCOMPANY\": \"蓝九天电商\", \"CARDINVALIDATE\": 4102329600000, \"STATUS\": 1, \"CARDBALANCE\": 932.31, \"SEX\": \"男  \", \"TEL\": \"\", \"HANDSET\": \"15721669800\", \"CREATEUSER\": \"168\", \"CREATETIME\": 1498699546150, \"ADDNO\": \"1706290000001\", \"ADDMONEY_BUSNO\": \"000\", \"ADDMONEY_CREATEUSER\": \"168\", \"ADDMONEY_CREATETIME\": 1498699807487, \"ADDAMOUNT\": 100, \"ADDMONEY_CARDBALANCE\": 100, \"ADDMONEY_NOTES\": \"测试\", \"ORGNAME\": \"山西蓝九天药业连锁有限公司\" } ]";
//        ArrayList<BalanceRecord> fs = GsonUtils.Json2ArrayList(data, BalanceRecord.class);
        return fs;

    }

    public static List<PointRecord> readPointRecord(String page, String count) throws BizFailure, ZYException {
        String method = "getPoint";
        SoapProcessor sp = new SoapProcessor("Service", method, true);
        sp.setProperty("firstIdx", page, PropertyType.TYPE_STRING);
        sp.setProperty("maxCount", count, PropertyType.TYPE_STRING);

        JsonElement element = sp.request();
        Gson gson = new GsonBuilder().create();
        TypeToken<List<PointRecord>> tt = new TypeToken<List<PointRecord>>() {
        };
        List<PointRecord> fs = gson.fromJson(element, tt.getType());
        return fs;

    }

    public static List<Ticket> readTicket(String type) throws BizFailure, ZYException {
        String method = "getRedPacketList";
        SoapProcessor sp = new SoapProcessor("Service", method, true);
        sp.setProperty("type", type, PropertyType.TYPE_STRING);
        JsonElement element = sp.request();
        Gson gson = new GsonBuilder().create();
        TypeToken<List<Ticket>> tt = new TypeToken<List<Ticket>>() {
        };
        List<Ticket> fs = gson.fromJson(element, tt.getType());
        return fs;
    }

    public static List<DianZiBiLog> readDianZiBiLog() throws BizFailure, ZYException {
        String method = "getRedPakcet";
        SoapProcessor sp = new SoapProcessor("Service", method, true);
        sp.setProperty("type", "0", PropertyType.TYPE_STRING);
        JsonElement element = sp.request();
        Gson gson = new GsonBuilder().create();
        TypeToken<List<DianZiBiLog>> tt = new TypeToken<List<DianZiBiLog>>() {
        };
        List<DianZiBiLog> fs = gson.fromJson(element, tt.getType());
        return fs;
    }

    public static boolean isLogined() {
        String token = PreferenceCache.getToken();
        Utils.log("" + !StringUtils.isEmpty(token));
        return !StringUtils.isEmpty(token);
    }

    public static List<ChoJiangRec> chojiangData() throws ZYException {
        String method = "getLingJiang";
        SoapProcessor sp = new SoapProcessor("Service", method, true);
        JsonElement element = sp.request();

        Gson gson = new GsonBuilder().create();
        TypeToken<List<ChoJiangRec>> tt = new TypeToken<List<ChoJiangRec>>() {
        };
        List<ChoJiangRec> fs = gson.fromJson(element, tt.getType());
        return fs;
    }
}
