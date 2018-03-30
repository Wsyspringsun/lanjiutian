package com.wyw.ljtds.model;

import android.content.Context;
import android.support.annotation.IntegerRes;

import com.wyw.ljtds.config.AppConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wsy on 18-3-27.
 */
public class MessageLib {
    private Context context;
    private static MessageLib single;
    private Map<String, Integer> labUnReadCount;
    private Map<String, XiaoNengData> labXnGroups;

    private MessageLib() {
    }

    private MessageLib(Context context) {
        this.context = context;
        this.labUnReadCount = new HashMap<>();
        this.labXnGroups = new HashMap<>();

    }

    /**
     * 获取单一实例
     *
     * @param context applicationContext
     * @return
     */
    public static MessageLib getInstance(Context context) {
        if (single == null)
            single = new MessageLib(context);
        return single;
    }

    public void saveUnreadMsgCount(String setttingId, int count) {
        labUnReadCount.put(setttingId, count);
    }

    public void saveXnGroup(String settingid, String groupName) {
        if (AppConfig.CHAT_XN_LJT_SETTINGID2.equals(settingid)) return;
        if (!labXnGroups.containsKey(settingid)) {
            XiaoNengData xn = new XiaoNengData();
            xn.setSettingid1(settingid);
            xn.setGroupName(groupName);
            labXnGroups.put(xn.getSettingid1(), xn);
        }
    }

    public Collection<XiaoNengData> readXnGroupList() {
        return labXnGroups.values();
    }

    public Integer readUnreadMsgCount(String setttingId) {
        return labUnReadCount.get(setttingId);
    }

    public void clearUnreadMsgCount(String settingId) {
        if (labUnReadCount.containsKey(settingId)) {
            labUnReadCount.put(settingId, null);
            labUnReadCount.remove(settingId);
        }
    }
}
