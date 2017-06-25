package com.wyw.ljtds.biz.biz;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.wyw.ljtds.biz.biz.SoapProcessor.PropertyType;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.model.AddressModel;
import com.wyw.ljtds.model.CityModel;
import com.wyw.ljtds.model.FavoriteModel;
import com.wyw.ljtds.model.FootprintModel;
import com.wyw.ljtds.model.MessageModel;
import com.wyw.ljtds.model.ProvinceModel;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.model.WalletModel;

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

        SoapProcessor ksoap2 = new SoapProcessor( "Service", "regist",
                false );

        ksoap2.setProperty( "phoneNumber", phoneNum, PropertyType.TYPE_STRING );
        ksoap2.setProperty( "verifyCode", verifyCode, PropertyType.TYPE_STRING );
        ksoap2.setProperty( "passWord", password, PropertyType.TYPE_STRING );
        ksoap2.setProperty( "introducer", invitor, PropertyType.TYPE_STRING );
        ksoap2.setProperty( "terminalType", terminalType, PropertyType.TYPE_STRING );

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

        SoapProcessor ksoap = new SoapProcessor( "Service", "login", false );

        ksoap.setProperty( "userName", userName, PropertyType.TYPE_STRING );
        ksoap.setProperty( "passWord", password, PropertyType.TYPE_STRING );
        ksoap.setProperty( "terminalType", terminalType, PropertyType.TYPE_STRING );

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
        SoapProcessor ksoap2 = new SoapProcessor( "Service", "getRegMobileCode", false );

        ksoap2.setProperty( "mobile", mobile, PropertyType.TYPE_STRING );
        ksoap2.setProperty( "sendType", sendType, PropertyType.TYPE_STRING );

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
        SoapProcessor ksoap2 = new SoapProcessor( "Service", "findPwd", false );

        ksoap2.setProperty( "mobile", mobile, PropertyType.TYPE_STRING );
        ksoap2.setProperty( "verifyCode", verifyCode, PropertyType.TYPE_STRING );
        ksoap2.setProperty( "passWord", passWord, PropertyType.TYPE_STRING );

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
        SoapProcessor ksoap2 = new SoapProcessor( "Service", "modifyPwd", true );

        ksoap2.setProperty( "oldPwd", oldPwd, PropertyType.TYPE_STRING );
        ksoap2.setProperty( "passWord", passWord, PropertyType.TYPE_STRING );

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

        SoapProcessor ksoap = new SoapProcessor( "Service", "realNameAuth", true );

        ksoap.setProperty( "realName", realName, PropertyType.TYPE_STRING );
        ksoap.setProperty( "cardId", cardId, PropertyType.TYPE_STRING );

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
        SoapProcessor ksoap2 = new SoapProcessor( "Service", "province", false );

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<ProvinceModel>> tt = new TypeToken<List<ProvinceModel>>() {
        };
        List<ProvinceModel> fs = gson.fromJson( element, tt.getType() );
        List<ProvinceModel> bms = new ArrayList<ProvinceModel>();
        bms.addAll( fs );
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
        SoapProcessor ksoap2 = new SoapProcessor( "Service", "city", false );

        ksoap2.setProperty( "parentId", parentId, PropertyType.TYPE_INTEGER );

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<CityModel>> tt = new TypeToken<List<CityModel>>() {
        };
        List<CityModel> fs = gson.fromJson( element, tt.getType() );
        List<CityModel> bms = new ArrayList<CityModel>();
        bms.addAll( fs );
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

        SoapProcessor ksoap = new SoapProcessor( "Service", "addUserAddress", true );

        ksoap.setProperty( "consigneeName", consigneeName, PropertyType.TYPE_STRING );
        ksoap.setProperty( "consigneeMobile", consigneeMobile, PropertyType.TYPE_STRING );
        ksoap.setProperty( "consigneeZipCode", consigneeZipCode, PropertyType.TYPE_STRING );
        ksoap.setProperty( "consigneeProvice", consigneeProvince, PropertyType.TYPE_STRING );
        ksoap.setProperty( "consigneeCity", consigneeCity, PropertyType.TYPE_STRING );
        ksoap.setProperty( "consigneeAddress", consigneeAddress, PropertyType.TYPE_STRING );

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

        SoapProcessor ksoap = new SoapProcessor( "Service", "updateUserAddress", false );

        ksoap.setProperty( "addressId", addressId, PropertyType.TYPE_STRING );
        ksoap.setProperty( "consigneeName", consigneeName, PropertyType.TYPE_STRING );
        ksoap.setProperty( "consigneeMobile", consigneeMobile, PropertyType.TYPE_STRING );
        ksoap.setProperty( "consigneeZipCode", consigneeZipCode, PropertyType.TYPE_STRING );
        ksoap.setProperty( "consigneeProvice", consigneeProvince, PropertyType.TYPE_STRING );
        ksoap.setProperty( "consigneeCity", consigneeCity, PropertyType.TYPE_STRING );
        ksoap.setProperty( "consigneeAddress", consigneeAddress, PropertyType.TYPE_STRING );

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

        SoapProcessor ksoap = new SoapProcessor( "Service", "deleteUserAddress", false );

        ksoap.setProperty( "addressId", addressId, PropertyType.TYPE_STRING );

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
        SoapProcessor ksoap2 = new SoapProcessor( "Service", "selectUserAddress", true );

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<AddressModel>> tt = new TypeToken<List<AddressModel>>() {
        };
        List<AddressModel> fs = gson.fromJson( element, tt.getType() );
        List<AddressModel> bms = new ArrayList<AddressModel>();
        bms.addAll( fs );
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
        SoapProcessor ksoap2 = new SoapProcessor( "Service", "getPerInformation", true );
        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();
        return gson.fromJson( element, UserModel.class );

    }


    /**
     * 获取钱包信息
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<WalletModel> getWallet() throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor( "Service", "myWallet", true );

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<WalletModel>> tt = new TypeToken<List<WalletModel>>() {
        };
        List<WalletModel> fs = gson.fromJson( element, tt.getType() );
        List<WalletModel> bms = new ArrayList<WalletModel>();
        bms.addAll( fs );
        return bms;
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
        SoapProcessor ksoap = new SoapProcessor( "Service", "message", false );

        ksoap.setProperty( "msgType", msgType, PropertyType.TYPE_STRING );
        ksoap.setProperty( "token", token, PropertyType.TYPE_STRING );
        ksoap.setProperty( "firstIdx", firstIdx, PropertyType.TYPE_STRING );
        ksoap.setProperty( "maxCount", maxCount, PropertyType.TYPE_STRING );

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<MessageModel>> tt = new TypeToken<List<MessageModel>>() {
        };
        List<MessageModel> fs = gson.fromJson( element, tt.getType() );
        List<MessageModel> bms = new ArrayList<MessageModel>();
        bms.addAll( fs );
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
        SoapProcessor ksoap = new SoapProcessor( "Service", "favoritesGoods", true );

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<FavoriteModel>> tt = new TypeToken<List<FavoriteModel>>() {
        };
        List<FavoriteModel> fs = gson.fromJson( element, tt.getType() );
        List<FavoriteModel> bms = new ArrayList<FavoriteModel>();
        bms.addAll( fs );
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

        SoapProcessor ksoap = new SoapProcessor( "Service", "addFavoritesGoods", true );

        ksoap.setProperty( "commodityId", commodityId, PropertyType.TYPE_STRING );
        ksoap.setProperty( "goodsFlg", goodsFlg, PropertyType.TYPE_STRING );

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
        SoapProcessor ksoap = new SoapProcessor( "Service", "delFavoritesGoods", true );

        ksoap.setProperty( "commodityId", commodityId, PropertyType.TYPE_INTEGER );

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
        SoapProcessor ksoap = new SoapProcessor( "Service", "mulDelFavoritesGoods", true );

        ksoap.setProperty( "ids", ids, PropertyType.TYPE_INTEGER );

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
        SoapProcessor ksoap = new SoapProcessor( "Service", "getClicksRanking", true );

        ksoap.setProperty( "firstIdx", firstIdx, PropertyType.TYPE_STRING );
        ksoap.setProperty( "maxCount", maxCount, PropertyType.TYPE_STRING );

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<FootprintModel>> tt = new TypeToken<List<FootprintModel>>() {
        };
        List<FootprintModel> fs = gson.fromJson( element, tt.getType() );
        List<FootprintModel> bms = new ArrayList<FootprintModel>();
        bms.addAll( fs );
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

        SoapProcessor ksoap = new SoapProcessor( "Service", "clicksRanking", false );

        ksoap.setProperty( "commodityId", commodityId, PropertyType.TYPE_STRING );
        ksoap.setProperty( "token", PreferenceCache.getToken(), PropertyType.TYPE_STRING );

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
        SoapProcessor ksoap = new SoapProcessor( "Service", "deleteClicksRanking", false );

        ksoap.setProperty( "clicksRankingId", clicksRankingId, PropertyType.TYPE_INTEGER );

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
        SoapProcessor ksoap = new SoapProcessor( "Service", "myPhoto", true );

        ksoap.setProperty( "data", data, SoapProcessor.PropertyType.TYPE_STRING );

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
        SoapProcessor ksoap = new SoapProcessor( "Service", "addPerInfomation", true );

        ksoap.setProperty( "nickname", nickname, SoapProcessor.PropertyType.TYPE_STRING );
        ksoap.setProperty( "birthday", birthday, SoapProcessor.PropertyType.TYPE_STRING );
        ksoap.setProperty( "sex", sex, SoapProcessor.PropertyType.TYPE_STRING );

        return ksoap.request().getAsInt();

    }

}
