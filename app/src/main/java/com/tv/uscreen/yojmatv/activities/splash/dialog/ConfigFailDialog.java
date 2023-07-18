package com.tv.uscreen.yojmatv.activities.splash.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;


import com.tv.uscreen.yojmatv.OttApplication;

import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.DialogInterface;
import com.tv.uscreen.yojmatv.utils.Logger;

import java.util.Objects;

public class ConfigFailDialog {
    final Activity activity;
    DialogInterface dialogInterface;
    AlertDialog.Builder alertdialog;
    Dialog dialog;

    public ConfigFailDialog(Activity context) {
        this.activity = context;
    }

    public void showDialog(DialogInterface listner) {
        try {
            dialogInterface = listner;
            alertdialog = new AlertDialog.Builder(activity);
            AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(activity), R.style.AppAlertTheme);
            // = new AlertDialog.Builder(activity);
            LayoutInflater li = LayoutInflater.from(activity);
            View view = li.inflate(R.layout.config_fail_popup, null);
            alert.setView(view);

            if (dialog == null) {
                dialog = alert.create();
            }
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);


            TextView desc=view.findViewById(R.id.description_txt);
            TextView positive=view.findViewById(R.id.positive_txt);
            TextView negative=view.findViewById(R.id.negative_txt);

            desc.setText(OttApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
            positive.setText(OttApplication.getInstance().getResources().getString(R.string.retry_dms));
            negative.setText(OttApplication.getInstance().getResources().getString(R.string.cancel));

            LinearLayout negative_button = view.findViewById(R.id.negative_button);
            LinearLayout positive_button = view.findViewById(R.id.positive_button);


            negative_button.setOnClickListener(view12 -> {
                dialog.dismiss();
                dialogInterface.negativeAction();
            });

            positive_button.setOnClickListener(view1 -> {
                dialog.dismiss();
                dialogInterface.positiveAction();

            });

            dialog.show();

        } catch (Exception e) {
            Logger.e("MaterialDialog", "" + e);

        }

    }

    public void hide() {
        if (dialog.isShowing()) {
            dialog.hide();
        }
    }
}
