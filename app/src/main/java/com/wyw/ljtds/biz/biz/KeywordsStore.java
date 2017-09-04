package com.wyw.ljtds.biz.biz;

import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.config.PreferenceCache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by wsy on 17-8-22.
 */

public class KeywordsStore {
    private static Set<String> coll;
    private static Set<String> collMedicine;
    private static Integer type = 0;

    public static void add(String words) {
        coll.add(words);
        if (coll.size() > 10) coll.remove(coll.toArray()[0]);
    }

    public static void initData(Integer type) {
        KeywordsStore.type = type;

        coll = Collections.synchronizedSortedSet(new TreeSet());
        String data = "";
        if (AppConfig.MEDICINE == type) {
            data = PreferenceCache.getNearestKeywords4Medicine();
        } else {
            data = PreferenceCache.getNearestKeywords();
        }
        if (data == null || data.length() <= 0) return;
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
        if (AppConfig.MEDICINE == type) {
            PreferenceCache.putNearestKeywords4Medicine(rlt);
        } else {
            PreferenceCache.putNearestKeywords(rlt);
        }
    }

    public static List<String> getList() {
        if (coll == null) return null;
        List<String> list = new ArrayList<>();
        for (String k : coll) {
            list.add(k);
        }
        return list;
    }

}
