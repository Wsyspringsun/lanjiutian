package com.wyw.ljtds.biz.biz;

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
import com.wyw.ljtds.model.BalanceRecord;
import com.wyw.ljtds.model.CityModel;
import com.wyw.ljtds.model.FavoriteModel;
import com.wyw.ljtds.model.FootprintModel;
import com.wyw.ljtds.model.MessageModel;
import com.wyw.ljtds.model.PointRecord;
import com.wyw.ljtds.model.ProvinceModel;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.model.WalletModel;
import com.wyw.ljtds.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20 0020.
 */

public class UserBiz extends BaseBiz {
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
    public static List<ProvinceModel> getProvince() throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "province", false);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<ProvinceModel>> tt = new TypeToken<List<ProvinceModel>>() {
        };
        List<ProvinceModel> fs = gson.fromJson(element, tt.getType());
        List<ProvinceModel> bms = new ArrayList<ProvinceModel>();
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
    public static List<CityModel> getcity(int parentId) throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "city", false);

        ksoap2.setProperty("parentId", parentId, PropertyType.TYPE_INTEGER);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<CityModel>> tt = new TypeToken<List<CityModel>>() {
        };
        List<CityModel> fs = gson.fromJson(element, tt.getType());
        List<CityModel> bms = new ArrayList<CityModel>();
        bms.addAll(fs);
        return bms;
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
    public static int addUserAddress(String consigneeName, String consigneeMobile, String consigneeZipCode
            , String consigneeProvince, String consigneeCity, String consigneeAddress) throws BizFailure, ZYException {

        SoapProcessor ksoap = new SoapProcessor("Service", "addUserAddress", true);

        ksoap.setProperty("consigneeName", consigneeName, PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeMobile", consigneeMobile, PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeZipCode", consigneeZipCode, PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeProvice", consigneeProvince, PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeCity", consigneeCity, PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeAddress", consigneeAddress, PropertyType.TYPE_STRING);

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
    public static int updateUserAddress(String addressId, String consigneeName
            , String consigneeMobile, String consigneeZipCode, String consigneeProvince, String consigneeCity, String consigneeAddress) throws BizFailure, ZYException {

        SoapProcessor ksoap = new SoapProcessor("Service", "updateUserAddress", false);

        ksoap.setProperty("addressId", addressId, PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeName", consigneeName, PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeMobile", consigneeMobile, PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeZipCode", consigneeZipCode, PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeProvice", consigneeProvince, PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeCity", consigneeCity, PropertyType.TYPE_STRING);
        ksoap.setProperty("consigneeAddress", consigneeAddress, PropertyType.TYPE_STRING);

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
//        String data = "[ { \"ROWNUMBER\": 1, \"CASHNO\": \"20170727155412343\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 7624.6500, \"INTEGRAL\": 600.0000, \"STATUS\": 1, \"EXECDATE\": 1501142178000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1501142178000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"生活馆消费抵用积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 2, \"CASHNO\": \"20170726180910598S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 8224.6500, \"INTEGRAL\": -3.0000, \"STATUS\": 1, \"EXECDATE\": 1501117191000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1501117191000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"订单交易完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 3, \"CASHNO\": \"20170726181050739S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 8221.6500, \"INTEGRAL\": -161.0000, \"STATUS\": 1, \"EXECDATE\": 1501117131000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1501117131000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"订单交易完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 4, \"CASHNO\": \"20170726180753448S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 8060.6500, \"INTEGRAL\": -196.0000, \"STATUS\": 1, \"EXECDATE\": 1501116771000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1501116771000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"订单交易完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 5, \"CASHNO\": \"20170726180950814S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 7864.6500, \"INTEGRAL\": -43.0000, \"STATUS\": 1, \"EXECDATE\": 1501116711000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1501116711000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"订单交易完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 6, \"CASHNO\": \"20170721155720994S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 97826.7300, \"INTEGRAL\": -5.0200, \"STATUS\": 1, \"EXECDATE\": 1501054816000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1501054816000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"生活馆订单完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 7, \"CASHNO\": \"20170721085942645\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 97821.7100, \"INTEGRAL\": 600.0000, \"STATUS\": 1, \"EXECDATE\": 1500598795000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500598795000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"生活馆消费抵用积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 8, \"CASHNO\": \"20170721083639547\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 98421.7100, \"INTEGRAL\": 600.0000, \"STATUS\": 1, \"EXECDATE\": 1500597412000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500597412000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"生活馆消费抵用积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 9, \"CASHNO\": \"20170721082946813\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 99021.7100, \"INTEGRAL\": 600.0000, \"STATUS\": 1, \"EXECDATE\": 1500597028000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500597028000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"生活馆消费抵用积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 10, \"CASHNO\": \"20170715090945378S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 7821.6500, \"INTEGRAL\": -1.0300, \"STATUS\": 1, \"EXECDATE\": 1500081955000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500081955000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"生活馆订单完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 11, \"CASHNO\": \"20170715090859525S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 7820.6200, \"INTEGRAL\": -1.0100, \"STATUS\": 1, \"EXECDATE\": 1500081236000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500081236000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"医药馆订单完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 12, \"CASHNO\": \"20170715090945378\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 7819.6100, \"INTEGRAL\": 600.0000, \"STATUS\": 1, \"EXECDATE\": 1500080993000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500080993000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"生活馆消费抵用积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 13, \"CASHNO\": \"20170715090859525\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 8419.6100, \"INTEGRAL\": 600.0000, \"STATUS\": 1, \"EXECDATE\": 1500080950000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500080950000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"医药馆消费抵用积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 14, \"CASHNO\": \"20170713181349034S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 9019.6100, \"INTEGRAL\": -3.0100, \"STATUS\": 1, \"EXECDATE\": 1500078959000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500078959000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"医药馆订单完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 15, \"CASHNO\": \"20170715082143467\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 9016.6000, \"INTEGRAL\": 600.0000, \"STATUS\": 1, \"EXECDATE\": 1500078114000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500078114000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"医药馆消费抵用积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 16, \"CASHNO\": \"20170712153417444S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 9616.6000, \"INTEGRAL\": -151.0000, \"STATUS\": 1, \"EXECDATE\": 1500025816000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500025816000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"医药馆订单完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 17, \"CASHNO\": \"20170712153652834S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 9465.6000, \"INTEGRAL\": -69.0000, \"STATUS\": 1, \"EXECDATE\": 1500025239000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500025239000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"医药馆订单完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 18, \"CASHNO\": \"20170712154146531S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 9396.6000, \"INTEGRAL\": -636.0000, \"STATUS\": 1, \"EXECDATE\": 1500025231000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1500025231000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"生活馆订单完成赠送积分。\", \"CARDHOLDER\": \"电商测 试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 19, \"CASHNO\": \"20170713152239567S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 8760.6000, \"INTEGRAL\": -1153.6000, \"STATUS\": 1, \"EXECDATE\": 1499935783000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1499935783000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"生活馆订单完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 20, \"CASHNO\": \"20170713151856066\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 7607.0000, \"INTEGRAL\": 600.0000, \"STATUS\": 1, \"EXECDATE\": 1499930345000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1499930345000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"医药馆消费抵用积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 21, \"CASHNO\": \"20170703205939169S\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 8207.0600, \"INTEGRAL\": -1.0600, \"STATUS\": 1, \"EXECDATE\": 1499087081000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1499087081000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"生活馆订单完成赠送积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 }, { \"ROWNUMBER\": 22, \"CASHNO\": \"20170703210119495C\", \"MEMCARDNO\": \"108888\", \"BUSNO\": \"987\", \"CARDLEVEL\": 1, \"INTEGRALA\": 8206.0000, \"INTEGRAL\": -600.0000, \"STATUS\": 1, \"EXECDATE\": 1499086934000, \"CREATEUSER\": \"987\", \"CREATETIME\": 1499086934000, \"MAXSALENO\": \"\", \"PSTID\": \"\", \"PSTNAME\": \"\", \"INTEGRAL_PST\": 0.0000, \"PSTQTY\": 0.0000, \"INTEGRALCASHNO\": \"\", \"CASHAMT\": 0.000000, \"NOTES\": \"生活馆订单取消退还积分。\", \"CARDHOLDER\": \"电商测试\", \"HANDSET\": \"15721669800\", \"USER_POINT\": 97629.7300 } ] ";
//        ArrayList<PointRecord> fs = GsonUtils.Json2ArrayList(data, PointRecord.class);
        return fs;

    }
}
