package com.wyw.ljtds.ui.user.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.gxz.PagerSlidingTabStrip;
import com.wyw.ljtds.R;
import com.wyw.ljtds.adapter.MyFrPagerAdapter;
import com.wyw.ljtds.ui.base.BaseFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BalanceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BalanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@ContentView(R.layout.fragment_categorys)
public class BalanceFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @ViewInject(R.id.tabs)
    private PagerSlidingTabStrip tabs;
    @ViewInject(R.id.pager)
    private ViewPager pager;

    private ArrayList<String> titles = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    public BalanceFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BalanceFragment.
     */
    public static BalanceFragment newInstance(String param1, String param2) {
        BalanceFragment fragment = new BalanceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fragmentList.add(BalanceListFragment.newInstance(R.string.balance_in));
        fragmentList.add(BalanceListFragment.newInstance(R.string.balance_out));

        titles.add(getResources().getString(R.string.balance_in));
        titles.add(getResources().getString(R.string.balance_out));

//        iconList.add(R.drawable.arrow);
//        iconList.add(R.drawable.arrow);

        MyFrPagerAdapter pAdapter = new MyFrPagerAdapter(getActivity().getSupportFragmentManager(), titles, fragmentList);
//        pAdapter.setIcons(iconList);

        pager.setAdapter(pAdapter);
        tabs.setViewPager(pager);
        pager.setCurrentItem(0);
    }
}
