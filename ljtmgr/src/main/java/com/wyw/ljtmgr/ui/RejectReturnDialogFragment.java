package com.wyw.ljtmgr.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wyw.ljtmgr.R;
import com.wyw.ljtmgr.utils.StringUtils;

/***
 * wsy 2018-1-29
 * select default address
 * 拒绝退货原因
 */
public class RejectReturnDialogFragment extends DialogFragment {
    public static final String TAG_RETURN_MSG = "com.wyw.ljtmgr.ui.RejectReturnDialogFragment.TAG_RETURN_MSG";
    private EditText etMsg;
    private Button btnSubmit;
    private ImageView btnCancel;

    /*private void sendResult(int id) {
        if (getTargetFragment() == null) return;
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, it);
    }*/

    public static RejectReturnDialogFragment newInstance() {
        Bundle args = new Bundle();
        RejectReturnDialogFragment frag = new RejectReturnDialogFragment();
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.setCancelable(false);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog_rejectreturn_msg, null);
        initView(v);
        initEvent();
        initData();

        return new AlertDialog.Builder(getActivity()).setView(v).setCancelable(false).create();
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    private void initEvent() {
        View.OnClickListener btnListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fragment_dialog_rejectreturn_msg_btn_submit:
                        if (StringUtils.isEmpty(etMsg.getText())) {
                            Toast.makeText(getActivity(), "必须给个理由！", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (getTargetFragment() == null) return;
                        Intent it = new Intent();
                        it.putExtra(TAG_RETURN_MSG, etMsg.getText());
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, it);
                        RejectReturnDialogFragment.this.dismiss();
                        break;
                    case R.id.fragment_dialog_rejectreturn_cancel:
                        RejectReturnDialogFragment.this.dismiss();
                        break;
                }
            }
        };

        btnCancel.setOnClickListener(btnListner);
        btnSubmit.setOnClickListener(btnListner);
    }

    private void initData() {
    }

    private void initView(View v) {
        etMsg = (EditText) v.findViewById(R.id.fragment_dialog_rejectreturn_msg);
        btnSubmit = (Button) v.findViewById(R.id.fragment_dialog_rejectreturn_msg_btn_submit);
        btnCancel = (ImageView) v.findViewById(R.id.fragment_dialog_rejectreturn_cancel);
    }
}
