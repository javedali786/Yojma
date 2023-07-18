package com.tv.uscreen.fragments.home.ui;

import android.os.Bundle;

import com.tv.uscreen.beanModel.TabsBaseFragment;
import com.tv.uscreen.fragments.home.viewModel.HomeFragmentViewModel;

public class HomeFragment extends TabsBaseFragment<HomeFragmentViewModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setViewModel(HomeFragmentViewModel.class);

    }

}