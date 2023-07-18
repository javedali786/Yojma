package com.tv.uscreen.fragments.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


import com.tv.uscreen.R;
import com.tv.uscreen.utils.constants.AppConstants;

import java.util.Objects;

public class DialogPlayer extends DialogFragment {
    private DialogPlayer.DialogListener alertDialogListener;
    public DialogPlayer() {
    }
    public static DialogPlayer newInstance(String title, String message, String actnBtn) {
        DialogPlayer frag = new DialogPlayer();
        Bundle args = new Bundle();
        args.putString(AppConstants.TITLE, title);
        args.putString(AppConstants.MESSAGE, message);
        args.putString(AppConstants.ACTION_BTN, actnBtn);
        frag.setArguments(args);
        return frag;
    }

    public void setAlertDialogCallBack(DialogPlayer.DialogListener alertDialogListener) {
        this.alertDialogListener = alertDialogListener;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_popup, container);
        //ThemeHandler.getInstance().applyPopUp(requireContext(),view);
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().setCancelable(false);
            String title_value = requireArguments().getString(AppConstants.TITLE);
            String message = getArguments().getString(AppConstants.MESSAGE);
            String actnBtn = getArguments().getString(AppConstants.ACTION_BTN);
            Button btnOk = view.findViewById(R.id.personalizeBtn);
            Button cancel = view.findViewById(R.id.cancelButton);
            TextView description = view.findViewById(R.id.popup_discription);
            TextView title = view.findViewById(R.id.popup_title);
            title.setText(title_value);
            if (title_value.isEmpty()) {
                title.setVisibility(View.GONE);
            }
            description.setText(message);
            btnOk.setText(actnBtn);
            btnOk.setOnClickListener(v -> {
                if (alertDialogListener != null)
                    alertDialogListener.onDialogFinish();
                dismiss();
            });
            cancel.setOnClickListener(v -> {
                dismiss();
            });
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
        return view;
    }
    public void onResume() {
        if (Objects.requireNonNull(getDialog()).getWindow() != null)
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        super.onResume();
    }
    public interface DialogListener {
        void onDialogFinish();
    }
}
