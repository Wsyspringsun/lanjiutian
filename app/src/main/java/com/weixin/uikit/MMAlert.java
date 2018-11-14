package com.weixin.uikit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.wyw.ljtds.R;
import com.wyw.ljtds.config.AppConfig;
import com.wyw.ljtds.widget.dialog.BottomDialog;

import java.util.ArrayList;
import java.util.List;

public final class MMAlert {

    public interface OnAlertSelectId {
        void onClick(int whichButton);
    }

    private MMAlert() {

    }

    public static AlertDialog showAlert(final Context context, final String msg, final String title) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setIcon(R.drawable.ic_dialog_alert);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final int msgId, final int titleId) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setIcon(R.drawable.ic_dialog_alert);
        builder.setTitle(titleId);
        builder.setMessage(msgId);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final int msgId, final int titleId, final DialogInterface.OnClickListener l) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setIcon(R.drawable.ic_dialog_alert);
        builder.setTitle(titleId);
        builder.setMessage(msgId);
        builder.setPositiveButton(R.string.ok, l);
        // builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final String msg, final String title, final DialogInterface.OnClickListener l) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setIcon(R.drawable.ic_dialog_alert);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ok, l);
        // builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final int msgId, final int titleId, final DialogInterface.OnClickListener lOk, final DialogInterface.OnClickListener lCancel) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setIcon(R.drawable.ic_dialog_alert);
        builder.setTitle(titleId);
        builder.setMessage(msgId);
        builder.setPositiveButton(R.string.ok, lOk);
        builder.setNegativeButton(R.string.alert_quxiao, lCancel);
        // builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final int msg, final int title, final int yes, final int no, final DialogInterface.OnClickListener lOk,
                                        final DialogInterface.OnClickListener lCancel) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setIcon(R.drawable.ic_dialog_alert);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(yes, lOk);
        builder.setNegativeButton(no, lCancel);
        // builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final String msg, final String title, final DialogInterface.OnClickListener lOk, final DialogInterface.OnClickListener lCancel) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setIcon(R.drawable.ic_dialog_alert);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.alert_queding, lOk);
        builder.setNegativeButton(R.string.alert_quxiao, lCancel);
        // builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final String msg, final String title, final String yes, final String no, final DialogInterface.OnClickListener lOk,
                                        final DialogInterface.OnClickListener lCancel) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setIcon(R.drawable.ic_dialog_alert);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(yes, lOk);
        builder.setNegativeButton(no, lCancel);
        // builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final String title, final View view, final DialogInterface.OnClickListener lOk) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setView(view);
        builder.setPositiveButton(R.string.alert_queding, lOk);
        // builder.setCancelable(true);
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final String title, final View view, final String ok, final String cancel, final DialogInterface.OnClickListener lOk,
                                        final DialogInterface.OnClickListener lCancel) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setView(view);
        builder.setPositiveButton(ok, lOk);
        builder.setNegativeButton(cancel, lCancel);
        // builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final String title, final String ok, final View view, final DialogInterface.OnClickListener lOk) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setView(view);
        builder.setPositiveButton(ok, lOk);
        // builder.setCancelable(true);
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final String title, final String msg, final View view, final DialogInterface.OnClickListener lOk,
                                        final DialogInterface.OnClickListener lCancel) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setView(view);
        builder.setPositiveButton(R.string.alert_queding, lOk);
        builder.setNegativeButton(R.string.alert_quxiao, lCancel);
        // builder.setCancelable(true);
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                if (lCancel != null) {
                    lCancel.onClick(dialog, 0);
                }
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static AlertDialog showAlert(final Context context, final String title, final View view, final OnCancelListener lCancel) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return null;
        }

        final Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setView(view);
        // builder.setCancelable(true);
        builder.setOnCancelListener(lCancel);
        final AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    public static Dialog showAlert(final Context context, final String title, final String[] items, String exit, final OnAlertSelectId alertDo) {
        return showMyAlert(context, title, items, exit, alertDo);
//        return showAlert(context, title, items, exit, alertDo, null);
    }

    private static Dialog showMyAlert(Context context, String title, String[] items, String exit, final OnAlertSelectId alertDo) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_list, null);


//        final Dialog alert = new BottomSheetDialog(context, layout).setCancelable(true);
        final Dialog alert = new Dialog(context, R.style.BottomDialog);
        alert.setContentView(layout);
        WindowManager windowManager = alert.getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = alert.getWindow().getAttributes();
//        params.height = params.height;
        params.width = display.getWidth() * 1;
        params.dimAmount = 0.2f;
        alert.getWindow().setAttributes(params);
        alert.getWindow().setGravity(Gravity.BOTTOM);
        alert.getWindow().setWindowAnimations(R.style.popWindow_anim_style);
        alert.show();

        RecyclerView rv = (RecyclerView) layout.findViewById(R.id.dialog_list_recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);//设置方向滑动
        rv.setLayoutManager(lm);
        IconListAdapter adapter = new IconListAdapter(context, items, alertDo);
        adapter.setItemClickListener(new IconListAdapter.ItemClickListener() {
            @Override
            public void onClick(int position) {
                alert.dismiss();
                alertDo.onClick(position);
            }
        });
        rv.setAdapter(adapter);

        return alert;
    }

    public static ProgressDialog showProgressDlg(final Context context, final String title, final String message, final boolean indeterminate, final boolean cancelable, final OnCancelListener lCancel) {

        return ProgressDialog.show(context, title, message, indeterminate, cancelable, new OnCancelListener() {

            @Override
            public void onCancel(final DialogInterface dialog) {
                if (lCancel != null) {
                    lCancel.onCancel(dialog);
                }
            }
        });
    }

    /**
     * @param context Context.
     * @param title   The title of this AlertDialog can be null .
     * @param items   button name list.
     * @param alertDo methods call Id:Button + cancel_Button.
     * @param exit    Name can be null.It will be Red Color
     * @return A AlertDialog
     */
    public static Dialog showAlert(final Context context, final String title, final String[] items, String exit, final OnAlertSelectId alertDo, OnCancelListener cancelListener) {
        String cancel = context.getString(R.string.alert_quxiao);
        final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.alert_dialog_menu_layout, null);
        final int cFullFillWidth = 10000;
        layout.setMinimumWidth(cFullFillWidth);
        final ListView list = (ListView) layout.findViewById(R.id.content_list);
        AlertAdapter adapter = new AlertAdapter(context, title, items, exit, cancel);
        list.setAdapter(adapter);
        list.setDividerHeight(0);

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!(title == null || title.equals("")) && position - 1 >= 0) {
                    alertDo.onClick(position - 1);
                    dlg.dismiss();
                    list.requestFocus();
                } else {
                    alertDo.onClick(position);
                    dlg.dismiss();
                    list.requestFocus();
                }

            }
        });
        // set a large value put it in bottom
        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        if (cancelListener != null) {
            dlg.setOnCancelListener(cancelListener);
        }
        dlg.setContentView(layout);
        dlg.show();
        return dlg;
    }

}

class AlertAdapter extends BaseAdapter {
    // private static final String TAG = "AlertAdapter";
    public static final int TYPE_BUTTON = 0;
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_EXIT = 2;
    public static final int TYPE_CANCEL = 3;
    private List<String> items;
    private int[] types;
    // private boolean isSpecial = false;
    private boolean isTitle = false;
    // private boolean isExit = false;
    private Context context;

    public AlertAdapter(Context context, String title, String[] items, String exit, String cancel) {
        if (items == null || items.length == 0) {
            this.items = new ArrayList<String>();
        } else {
            this.items = stringsToList(items);
        }
        this.types = new int[this.items.size() + 3];
        this.context = context;
        if (title != null && !title.equals("")) {
            types[0] = TYPE_TITLE;
            this.isTitle = true;
            this.items.add(0, title);
        }

        if (exit != null && !exit.equals("")) {
            // this.isExit = true;
            types[this.items.size()] = TYPE_EXIT;
            this.items.add(exit);
        }

        if (cancel != null && !cancel.equals("")) {
            // this.isSpecial = true;
            types[this.items.size()] = TYPE_CANCEL;
            this.items.add(cancel);
        }
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0 && isTitle) {
            return false;
        } else {
            return super.isEnabled(position);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String textString = (String) getItem(position);
        ViewHolder holder;
        int type = types[position];
        if (convertView == null || ((ViewHolder) convertView.getTag()).type != type) {
            holder = new ViewHolder();
            if (type == TYPE_CANCEL) {
                convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout_cancel, null);
            } else if (type == TYPE_BUTTON) {
                convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout, null);
            } else if (type == TYPE_TITLE) {
                convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout_title, null);
            } else if (type == TYPE_EXIT) {
                convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout_special, null);
            }

            // holder.view = (LinearLayout) convertView.findViewById(R.id.popup_layout);
            holder.text = (TextView) convertView.findViewById(R.id.popup_text);
            holder.type = type;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(textString);
        return convertView;
    }

    static class ViewHolder {
        // LinearLayout view;
        TextView text;
        int type;
    }

    public static List<String> stringsToList(final String[] src) {
        if (src == null || src.length == 0) {
            return null;
        }
        final List<String> result = new ArrayList<String>();
        for (int i = 0; i < src.length; i++) {
            result.add(src[i]);
        }
        return result;
    }


}


/**
 * 微信分享界面
 */
class IconListViewHolder extends RecyclerView.ViewHolder {
    ImageView sdvIcon;

    public IconListViewHolder(View itemView, MMAlert.OnAlertSelectId alertDo) {
        super(itemView);
        sdvIcon = (ImageView) itemView.findViewById(R.id.dialog_menu_icon);
    }
}

class IconListAdapter extends RecyclerView.Adapter<IconListViewHolder> {
    interface ItemClickListener {
        void onClick(int position);
    }

    MMAlert.OnAlertSelectId alertDo;
    String[] list;
    Context context;

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    ItemClickListener itemClickListener;

    public IconListAdapter(Context context, String[] items, MMAlert.OnAlertSelectId alertDo) {
        this.list = items;
        this.context = context;
        this.alertDo = alertDo;
    }

    @Override
    public IconListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_menu_list, parent, false);

        IconListViewHolder vh = new IconListViewHolder(view, this.alertDo);
        return vh;
    }

    @Override
    public void onBindViewHolder(final IconListViewHolder holder, final int position) {
        String data = list[position];
        holder.sdvIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(position);
            }
        });
        switch (position) {
            case 0:
                holder.sdvIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.weixin));
                break;
            case 1:
                holder.sdvIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.pengyouquan));
                break;
            case 2:
                holder.sdvIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.shoucang));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.length;
    }
}
