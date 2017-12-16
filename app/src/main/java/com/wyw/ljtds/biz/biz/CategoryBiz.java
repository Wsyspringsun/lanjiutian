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
import com.wyw.ljtds.model.CommodityListModel;
import com.wyw.ljtds.model.CommodityTypeFirstModel;
import com.wyw.ljtds.model.CommodityTypeSecondModel;
import com.wyw.ljtds.model.MedicineListModel;
import com.wyw.ljtds.model.MedicineTypeFirstModel;
import com.wyw.ljtds.model.MedicineTypeSecondModel;

import java.util.ArrayList;
import java.util.List;

import cn.xiaoneng.uiutils.XNUIUtils;

/**
 * Created by Administrator on 2017/3/1 0001.
 */

public class CategoryBiz extends BaseBiz {
    /**
     * 获取商品一级菜单
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<CommodityTypeFirstModel> getCommodityType() throws BizFailure, ZYException {

        SoapProcessor ksoap2 = new SoapProcessor("Service", "getCommodityTypeList", false);
        ksoap2.setProperty("parentId", "0", PropertyType.TYPE_STRING);
        ksoap2.setProperty("parentLvl", "1", PropertyType.TYPE_STRING);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<CommodityTypeFirstModel>> tt = new TypeToken<List<CommodityTypeFirstModel>>() {
        };
        List<CommodityTypeFirstModel> fs1 = gson.fromJson(element, tt.getType());
        List<CommodityTypeFirstModel> bms1 = new ArrayList<CommodityTypeFirstModel>();
        bms1.addAll(fs1);
        return bms1;

    }


    /**
     * 获取药品一级菜单
     *
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<MedicineTypeFirstModel> getMedicineType() throws BizFailure, ZYException {

        SoapProcessor ksoap2 = new SoapProcessor("Service", "getRootMedicineType", false);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<MedicineTypeFirstModel>> tt = new TypeToken<List<MedicineTypeFirstModel>>() {
        };
        List<MedicineTypeFirstModel> fs = gson.fromJson(element, tt.getType());
        List<MedicineTypeFirstModel> bms = new ArrayList<MedicineTypeFirstModel>();
        bms.addAll(fs);
        return bms;

    }


    /**
     * 获取商品二三级菜单
     *
     * @param parentId 一级分类id
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<CommodityTypeSecondModel> getCommodityTypeList(String parentId) throws BizFailure, ZYException {

        SoapProcessor ksoap = new SoapProcessor("Service", "getCommodityTypeList", false);

        ksoap.setProperty("parentId", parentId, PropertyType.TYPE_STRING);
        ksoap.setProperty("parentLvl", "2", PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<CommodityTypeSecondModel>> tt = new TypeToken<List<CommodityTypeSecondModel>>() {
        };
        List<CommodityTypeSecondModel> fs = gson.fromJson(element, tt.getType());
        List<CommodityTypeSecondModel> bms = new ArrayList<CommodityTypeSecondModel>();
        bms.addAll(fs);
        return bms;
    }


    /**
     * 获取药品二三级菜单
     *
     * @param parentId  药品一级typeid
     * @param parentLvl 药品1级
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<MedicineTypeSecondModel> getMedicineTypeList(String parentId, String parentLvl)
            throws BizFailure, ZYException {

        SoapProcessor ksoap = new SoapProcessor("Service", "getChildrenMedicineType", false);

        ksoap.setProperty("parentId", parentId, PropertyType.TYPE_STRING);
        ksoap.setProperty("parentLvl", parentLvl, PropertyType.TYPE_STRING);

        JsonElement element = ksoap.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<MedicineTypeSecondModel>> tt = new TypeToken<List<MedicineTypeSecondModel>>() {
        };
        List<MedicineTypeSecondModel> fs = gson.fromJson(element, tt.getType());
        List<MedicineTypeSecondModel> bms = new ArrayList<MedicineTypeSecondModel>();
        bms.addAll(fs);
//        Log.i(AppConfig.ERR_TAG, );
        return bms;
    }


    /**
     * 获取商品列表
     *
     * @param classId  id
     * @param startIdx 起始页数
     * @param pageSize 每页数量
     * @param orderby  排序方式
     * @param keyword  关键字
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<CommodityListModel> getCommodityList(String classId, String orderby, String keyword, String startIdx, String pageSize)
            throws BizFailure, ZYException {

        SoapProcessor ksoap2 = new SoapProcessor("Service", "getCommodityList", false);
        ksoap2.setProperty("classId", classId, PropertyType.TYPE_STRING);
        ksoap2.setProperty("orderby", orderby, PropertyType.TYPE_STRING);
        ksoap2.setProperty("keyword", keyword, PropertyType.TYPE_STRING);
        ksoap2.setProperty("startIdx", startIdx, PropertyType.TYPE_STRING);
        ksoap2.setProperty("pageSize", pageSize, PropertyType.TYPE_STRING);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<CommodityListModel>> tt = new TypeToken<List<CommodityListModel>>() {
        };
        List<CommodityListModel> fs = gson.fromJson(element, tt.getType());
        List<CommodityListModel> bms = new ArrayList<CommodityListModel>();
        bms.addAll(fs);
        return bms;

    }


    /**
     * 获取药品列表
     *
     * @param classId  id
     * @param startIdx 起始页数
     * @param pageSize 每页数量
     * @param orderby  排序方式
     * @param keyword  关键字
     * @return
     * @throws BizFailure
     * @throws ZYException
     */
    public static List<MedicineListModel> getMedicineList(String topFlg,String classId, String orderby, String keyword, String startIdx, String pageSize)
            throws BizFailure, ZYException {
        SoapProcessor ksoap2 = new SoapProcessor("Service", "getMedicineList", false);
        ksoap2.setProperty("topFlg", topFlg, PropertyType.TYPE_STRING);
        ksoap2.setProperty("classId", classId, PropertyType.TYPE_STRING);
        ksoap2.setProperty("orderby", orderby, PropertyType.TYPE_STRING);
        ksoap2.setProperty("keyword", keyword, PropertyType.TYPE_STRING);
        ksoap2.setProperty("startIdx", startIdx, PropertyType.TYPE_STRING);
        ksoap2.setProperty("pageSize", pageSize, PropertyType.TYPE_STRING);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<MedicineListModel>> tt = new TypeToken<List<MedicineListModel>>() {
        };
        List<MedicineListModel> fs = gson.fromJson(element, tt.getType());
        List<MedicineListModel> bms = new ArrayList<MedicineListModel>();
        bms.addAll(fs);
        return bms;
    }

    public static List<MedicineListModel> findMedicineList(String classId, String orderby, String startIdx, String pageSize)
            throws BizFailure, ZYException {

        SoapProcessor ksoap2 = new SoapProcessor("Service", "findMedicine", false);
        ksoap2.setProperty("classId", classId, PropertyType.TYPE_STRING);
        ksoap2.setProperty("orderby", orderby, PropertyType.TYPE_STRING);
        ksoap2.setProperty("firstIdx", startIdx, PropertyType.TYPE_STRING);
        ksoap2.setProperty("maxCount", pageSize, PropertyType.TYPE_STRING);

        JsonElement element = ksoap2.request();
        Gson gson = new GsonBuilder().create();

        TypeToken<List<MedicineListModel>> tt = new TypeToken<List<MedicineListModel>>() {
        };
        List<MedicineListModel> fs = gson.fromJson(element, tt.getType());
        List<MedicineListModel> bms = new ArrayList<MedicineListModel>();
        bms.addAll(fs);
        return bms;
    }

}
