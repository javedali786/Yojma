package com.breadgangtvnetwork.fragments.gaming.ui;

import android.os.Bundle;

import com.breadgangtvnetwork.beanModel.TabsBaseFragment;
import com.breadgangtvnetwork.fragments.gaming.viewModel.GamingFragmentViewModel;
import com.breadgangtvnetwork.fragments.news.viewModel.ReelsFragmentViewModel;

public class GamingFragment extends TabsBaseFragment<GamingFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(GamingFragmentViewModel.class);
    }
}