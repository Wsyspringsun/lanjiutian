package com.wyw.ljtds.biz.biz;

import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;
import com.wyw.ljtds.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by wsy on 17-8-22.
 */

public class KeywordsStore {
    //    private static Set<String> coll;
    private static List<String> coll;
    private static Set<String> collMedicine;
    private static Integer type = 0;

    public static void add(String words) {
        if (coll == null) return;
        if (coll.indexOf(words) == 0)
            return;
        //已经存在 删除 旧的
        while (coll.remove(words)) {
        }
        coll.add(0, words);

        if (coll.size() > 20) {
            //药删除的内容
//            Object wd = coll.toArray()[0];
//            Utils.log(".........del:" + wd);
//            coll.remove(wd);
            coll.remove(coll.size() - 1);
        }
    }

    public static void initData(Integer type) {
        KeywordsStore.type = type;

//        coll = Collections.synchronizedSortedSet(new TreeSet());
        coll = Collections.synchronizedList(new LinkedList<String>());
        String data = "";
        if (AppConfig.MEDICINE == type) {
            data = PreferenceCache.getNearestKeywords4Medicine();
        } else {
            data = PreferenceCache.getNearestKeywords();
        }
        if (data == null || data.length() <= 0) return;
        Utils.log(".........initData:" + data);
        String[] arr = data.split(",");
        coll.addAll(Arrays.asList(arr));
    }

    public static void doStore() {
        if (coll == null || coll.size() <= 0)
            return;
        StringBuilder sb = new StringBuilder();
        String sep = ",";
        for (String obj : coll) {
            sb.append(obj).append(sep);
        }
        String rlt = sb.substring(0, sb.lastIndexOf(sep));
        Utils.log(".........result:" + rlt);
        if (AppConfig.MEDICINE == type) {
            PreferenceCache.putNearestKeywords4Medicine(rlt);
        } else {
            PreferenceCache.putNearestKeywords(rlt);
        }
    }

    public static List<String> getList() {
        if (coll == null) return null;
//        List<String> list = new ArrayList<>();
//        for (String k : coll) {
//            list.add(k);
//        }
//        return list;
        return coll;
    }

}
