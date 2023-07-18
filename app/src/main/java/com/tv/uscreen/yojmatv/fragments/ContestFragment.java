package com.tv.uscreen.yojmatv.fragments;

import android.os.Bundle;

import com.tv.uscreen.yojmatv.beanModel.TabsBaseFragment;
import com.tv.uscreen.yojmatv.fragments.home.viewModel.HomeFragmentViewModel;

public class ContestFragment extends TabsBaseFragment<HomeFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(ContestFragmentViewModel.class);
    }
}