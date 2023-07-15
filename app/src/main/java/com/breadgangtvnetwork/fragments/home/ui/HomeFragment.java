package com.breadgangtvnetwork.fragments.home.ui;

import android.os.Bundle;

import com.breadgangtvnetwork.beanModel.TabsBaseFragment;
import com.breadgangtvnetwork.fragments.home.viewModel.HomeFragmentViewModel;

public class HomeFragment extends TabsBaseFragment<HomeFragmentViewModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setViewModel(HomeFragmentViewModel.class);

    }

}