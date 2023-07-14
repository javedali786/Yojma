package com.breadgangtvnetwork.fragments;

import android.os.Bundle;

import com.breadgangtvnetwork.beanModel.TabsBaseFragment;
import com.breadgangtvnetwork.fragments.home.viewModel.HomeFragmentViewModel;

public class ContestFragment extends TabsBaseFragment<HomeFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(ContestFragmentViewModel.class);
    }
}