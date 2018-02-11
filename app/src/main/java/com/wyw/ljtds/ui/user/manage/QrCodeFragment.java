package com.wyw.ljtds.ui.user.manage;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.model.UserModel;
import com.wyw.ljtds.ui.base.BaseFragment;
import com.wyw.ljtds.utils.GsonUtils;
import com.wyw.ljtds.utils.StringUtils;
import com.wyw.ljtds.utils.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by wsy on 17-7-28.
 */
@ContentView(R.layout.fragment_user_qrcode)
public class QrCodeFragment extends BaseFragment {
    private static final String ARG_USER = "current_user";
    @ViewInject(R.id.fragment_user_qrcode_photo)
    ImageView imgPhoto;
    @ViewInject(R.id.fragment_user_qrcode_idcode)
    ImageView imgIdcode;
    @ViewInject(R.id.fragment_user_qrcode_name)
    TextView txtName;

    private UserModel user;

    /**
     * @param cat 余额类型
     * @return
     */
    public static QrCodeFragment newInstance(String jsonUser) {
        QrCodeFragment fragment = new QrCodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER, jsonUser);
//        args.putInt(ARG_CAT, resId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (user == null) {
            String jsonUser = getArguments().getString(ARG_USER);
            Log.e(AppConfig.ERR_TAG, "jsonUser:" + jsonUser);
            user = GsonUtils.Json2Bean(jsonUser, UserModel.class);
            if (!StringUtils.isEmpty(user.getUSER_NAME())) {
                txtName.setText(user.getUSER_NAME());
            }
            if (!StringUtils.isEmpty(user.getUSER_ICON_FILE_ID())) {
                Picasso.with(getActivity()).load(Uri.parse(AppConfig.IMAGE_PATH_LJT + user.getUSER_ICON_FILE_ID())).into(imgPhoto);
            }
            String id = user.getOID_USER_ID();
            Bitmap bitmap = Utils.getQRCodeBitmap(getActivity(), id);
            if (bitmap != null) {
                imgIdcode.setImageBitmap(bitmap);
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
