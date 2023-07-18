package com.tv.uscreen.fragments.gaming.ui;

import android.os.Bundle;

import com.tv.uscreen.beanModel.TabsBaseFragment;
import com.tv.uscreen.fragments.gaming.viewModel.GamingFragmentViewModel;

public class GamingFragment extends TabsBaseFragment<GamingFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(GamingFragmentViewModel.class);
    }
}