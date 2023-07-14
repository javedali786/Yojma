package com.breadgangtvnetwork.activities.Novelties.ui;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.breadgangtvnetwork.activities.Novelties.ui.Adapter.AdapterViewPage;
import com.breadgangtvnetwork.baseModels.BaseBindingActivity;
import com.breadgangtvnetwork.databinding.NoveltiesActivityBinding;
import com.google.android.material.tabs.TabLayout;


public class NoveltiesActivity extends BaseBindingActivity<NoveltiesActivityBinding> {
    AdapterViewPage mAdapterViewPage;
    @Override
    public NoveltiesActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return NoveltiesActivityBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUiInit();
    }

    void setUiInit() {
        mAdapterViewPage = new AdapterViewPage(getSupportFragmentManager());
        getBinding().backIcon.setOnClickListener(view -> onBackPressed());
        getBinding().viewPagerTv.setAdapter(mAdapterViewPage);
        getBinding().tabLayoutTv.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getBinding().viewPagerTv.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
