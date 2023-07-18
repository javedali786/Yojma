package com.tv.uscreen.fragments.dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


import com.tv.uscreen.R;

import java.util.Objects;

public class AlertDialogSingleButtonFragment extends DialogFragment {


    private AlertDialogFragment.AlertDialogListener alertDialogListener;


    public AlertDialogSingleButtonFragment() {
    }

    public static AlertDialogSingleButtonFragment newInstance(String title, String message, String positiveButtonText) {
        AlertDialogSingleButtonFragment frag = new AlertDialogSingleButtonFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putString("positiveButtonText", positiveButtonText);
        frag.setArguments(args);
        return frag;
    }

    public void setAlertDialogCallBack(AlertDialogFragment.AlertDialogListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder alertDialogBuilder;
        String title = requireArguments().getString("title");
        String message = getArguments().getString("message");
        String positiveButtonText = getArguments().getString("positiveButtonText");

        alertDialogBuilder = new AlertDialog.Builder(requireActivity(), R.style.AppAlertTheme);

        if (Objects.requireNonNull(title).equalsIgnoreCase("Error")) {
            alertDialogBuilder.setTitle("");
        } else {
            alertDialogBuilder.setTitle(title);
        }


        alertDialogBuilder.setMessage("" + message);
        alertDialogBuilder.setPositiveButton("" + positiveButtonText, (dialog, which) -> {
            // on success
//                AlertDialogListener alertDialogListener = (AlertDialogListener) getActivity();
            if (alertDialogListener != null) {
                alertDialogListener.onFinishDialog();
            }
            dialog.dismiss();
        });
        try {


            if (Objects.requireNonNull(message).contains("remove video")) {
                alertDialogBuilder.setNegativeButton("" + "Cancel", (dialog, which) -> {
                    // on success
                    dialog.dismiss();
                });
            }
        } catch (Exception e) {

        }
        return alertDialogBuilder.create();
    }


    // 1. Defines the listener interface with a method passing back data result.
    public interface AlertDialogListener {
        void onFinishDialog();
    }
}
