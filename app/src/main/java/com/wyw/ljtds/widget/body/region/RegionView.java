package com.wyw.ljtds.widget.body.region;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.ui.find.FragmentFind;
import com.wyw.ljtds.ui.goods.ActivityMedicineList;
import com.wyw.ljtds.widget.body.WaveEffectLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wyw.ljtds.widget.body.region.Region.EAR;
import static com.wyw.ljtds.widget.body.region.Region.EYE;
import static com.wyw.ljtds.widget.body.region.Region.FACE;
import static com.wyw.ljtds.widget.body.region.Region.HEAD;
import static com.wyw.ljtds.widget.body.region.Region.NOSEMOUTH;
import static com.wyw.ljtds.widget.body.region.Region.THROAT;

/**
 * Created by angelo on 2015/2/26.
 */
public class RegionView {
    public static final String REGION_TYPE = "regionType";
    public static final String REGION_ID = "regionID";
    private final WaveEffectLayout container;
    private final Context mContext;
    private AbsoluteLayout leftRegionLayout, rightRegionLayout;
    private LayoutInflater layoutInflater;
    private Region[] regions;
    private List<View> regionViews = new ArrayList<>();
    private static Map<Region, String> classIdMap = new HashMap<Region, String>();

    static {
        classIdMap.put(HEAD, "2738");
        classIdMap.put(Region.EYE, "2739");
        classIdMap.put(Region.FACE, "2740");
        classIdMap.put(Region.NOSEMOUTH, "2743");
        classIdMap.put(Region.EAR, "2742");
        classIdMap.put(Region.THROAT, "2741");
        classIdMap.put(Region.NECK, "2744");
        classIdMap.put(Region.SKIN, "2745");

        classIdMap.put(Region.CHEST, "2748");
        classIdMap.put(Region.ABDOMEN, "2749");
        classIdMap.put(Region.WAIST, "2750");
        classIdMap.put(Region.SHOULDER, "2746");
        classIdMap.put(Region.BACKBACK, "2763");
        classIdMap.put(Region.BACKANUSRECTUM, "2765");
        classIdMap.put(Region.PELVIC, "2761");

        classIdMap.put(Region.HAND, "2752");
        classIdMap.put(Region.FOOT, "2754");
        classIdMap.put(Region.LEG, "2753");
        classIdMap.put(Region.OTHER, "766");

    }

    public RegionView(WaveEffectLayout container, Context context) {
        this.container = container;
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(context);
        init();
    }

    private void init() {
        leftRegionLayout = (AbsoluteLayout) container.findViewById(R.id.left_region_layout);
        rightRegionLayout = (AbsoluteLayout) container.findViewById(R.id.right_region_layout);

    }

    public void setAdapter(int regionType) {
        regionViews.clear();
        if (-1 == regionType) {
            if (leftRegionLayout != null) {
                leftRegionLayout.removeAllViews();
            }
            if (rightRegionLayout != null) {
                rightRegionLayout.removeAllViews();
            }
            return;
        }

        regions = RegionParam.regionItems.get(regionType);
        for (Region region : regions) {
            regionViews.add(getItem(region));
        }

        regionViews.add(getItem(Region.SKIN));
        refresh();
    }

    private View getItem(final Region region) {
        View itemView = layoutInflater.inflate(R.layout.region_item, null);
        final TextView textView = (TextView) itemView.findViewById(R.id.text_view);
        textView.setText(region.getName());
        itemView.setTag(String.valueOf(region.getValue()));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classId = classIdMap.get(region);
                Intent it = new Intent(mContext, ActivityMedicineList.class);
                it.putExtra(ActivityMedicineList.TAG_LIST_FROM,FragmentFind. FIND_MTD_QUICK);
                it.putExtra(FragmentFind.TAG_MTD_QUICK_PARAM, classId);
                mContext.startActivity(it);
//                Log.e(AppConfig.ERR_TAG, "click " + classId);
            }
        });
        return itemView;
    }

    public void refresh() {

        if (leftRegionLayout == null || rightRegionLayout == null)
            return;

        leftRegionLayout.removeAllViews();
        rightRegionLayout.removeAllViews();
        int size = regionViews.size() - 1;
        if (size > 0) {
            int columnSize = size / 2 + size % 2;

            for (int i = 0; i < columnSize; i++) {

                leftRegionLayout.addView(regionViews.get(i), new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 0, regions[i].getDestinationY()));
            }

            for (int i = columnSize; i < size; i++) {

                rightRegionLayout.addView(regionViews.get(i), new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 0, regions[i].getDestinationY()));
            }

            // add skin region
            rightRegionLayout.addView(getItem(Region.SKIN), new AbsoluteLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 0, regions[size - 1].getDestinationY() + Region.SKIN.getDestinationY()));//RegionParam.OFFSET_Y
        }
    }
}

