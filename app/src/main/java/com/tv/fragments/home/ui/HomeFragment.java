package com.tv.fragments.home.ui;

import android.os.Bundle;

import com.tv.beanModel.TabsBaseFragment;
import com.tv.fragments.home.viewModel.HomeFragmentViewModel;

public class HomeFragment extends TabsBaseFragment<HomeFragmentViewModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setViewModel(HomeFragmentViewModel.class);

    }

}