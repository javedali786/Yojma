package com.tv.fragments.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.tv.R;


public class AlertDialogFragment extends DialogFragment {


    private AlertDialogListener alertDialogListener;
    private AlertDialogDeleteListener alertDialogDeleteListener;

    public AlertDialogFragment() {
    }
    public void setAlertDialogDeleteCallBack(AlertDialogDeleteListener alertDialogDeleteListener) {
        this.alertDialogDeleteListener = alertDialogDeleteListener;
    }

    public static AlertDialogFragment newInstance(String title, String message,String positiveButtonText,String negativeButtonText) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("positiveButtonText", positiveButtonText);
        args.putString("negativeButtonText", negativeButtonText);
        frag.setArguments(args);
        return frag;
    }

    public void setAlertDialogCallBack(AlertDialogListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = requireArguments().getString("title");
        String message = getArguments().getString("message");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireActivity(), R.style.AppAlertTheme);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage("" + message);
        alertDialogBuilder.setPositiveButton(getArguments().getString("positiveButtonText"), (dialog, which) -> {
            if (alertDialogDeleteListener != null){
                alertDialogDeleteListener.onFinishDeleteDialog();
            }else {
                alertDialogListener.onFinishDialog();
            }
            dialog.dismiss();
        });
        alertDialogBuilder.setNegativeButton(getArguments().getString("negativeButtonText"), (dialog, which) -> {
            dialog.dismiss();
        });

        return alertDialogBuilder.create();

    }

    // 1. Defines the listener interface with a method passing back data result.
    public interface AlertDialogListener {
        void onFinishDialog();
    }
    public interface AlertDialogDeleteListener {
        void onFinishDeleteDialog();
    }
}
