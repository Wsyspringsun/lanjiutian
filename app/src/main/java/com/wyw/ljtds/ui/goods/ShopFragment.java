package com.wyw.ljtds.ui.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.biz.biz.GoodsBiz;
import com.wyw.ljtds.biz.exception.BizFailure;
import com.wyw.ljtds.biz.exception.ZYException;
import com.wyw.ljtds.biz.task.BizDataAsyncTask;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.ShopModel;
import com.wyw.ljtds.model.SingleCurrentUser;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.GsonUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_shopgoods)
public class ShopFragment extends BaseFragment {
    private static final String ARG_SHOPID = "param1";
    private static final String ARG_PARAM2 = "param2";

    @ViewInject(R.id.fragment_shopinfo_data)
    TextView tvInfo;

    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BalanceFragment.
     */
    public static ShopFragment newInstance(String shopid, String shoptel) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SHOPID, shopid);
        args.putString(ARG_PARAM2, shoptel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isAdded())
            return;

        loadData();

    }

    private void loadData() {
        setLoding(getActivity(), false);
        new BizDataAsyncTask<ShopModel>() {
            @Override
            protected ShopModel doExecute() throws ZYException, BizFailure {
                closeLoding();
//                String shopId = "001";
                String shopId = getArguments().getString(ARG_SHOPID);
                Map<String, String> data = new HashMap<>();
                data.put("busno", shopId);
                data.put("lat", SingleCurrentUser.location.getLatitude() + "");
                data.put("lng", SingleCurrentUser.location.getLongitude() + "");
                return GoodsBiz.loadShopInfo(GsonUtils.Bean2Json(data));
            }

            @Override
            protected void onExecuteSucceeded(ShopModel shop) {
                closeLoding();
                String shopname = shop.getLOGISTICS_COMPANY();
                String shopTel = AppConfig.LJG_TEL;
                tvInfo.setText("店铺信息：" + shopname + "\n" + "联系我们：" + shopTel);
            }

            @Override
            protected void OnExecuteFailed() {
                closeLoding();
            }
        }.execute();
    }
}
