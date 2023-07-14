package com.breadgangtvnetwork.activities.manageAccounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.breadgangtvnetwork.R;
import com.breadgangtvnetwork.activities.profile.ui.ProfileActivityNew;
import com.breadgangtvnetwork.baseModels.BaseBindingActivity;
import com.breadgangtvnetwork.databinding.ActivityManageAccountBinding;
import com.breadgangtvnetwork.utils.helpers.intentlaunchers.ActivityLauncher;

public class ManageAccount extends BaseBindingActivity<ActivityManageAccountBinding> {

    @Override
    public ActivityManageAccountBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityManageAccountBinding.inflate(inflater);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolBar();
        getBinding().myProfile.setOnClickListener(v -> ActivityLauncher.getInstance().ProfileActivityNew(ManageAccount.this, ProfileActivityNew.class));
    }
    private void toolBar() {
        getBinding().toolbarMange.backLayout.setOnClickListener(view -> onBackPressed());
        getBinding().toolbarMange.backLayout.setVisibility(View.VISIBLE);
        getBinding().toolbarMange.titleMid.setVisibility(View.VISIBLE);
        getBinding().toolbarMange.titleMid.setText(R.string.manage_account);
    }
}