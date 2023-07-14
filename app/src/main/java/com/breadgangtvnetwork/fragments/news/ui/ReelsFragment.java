package com.breadgangtvnetwork.fragments.news.ui;

import android.os.Bundle;

import com.breadgangtvnetwork.beanModel.TabsBaseFragment;
import com.breadgangtvnetwork.fragments.news.viewModel.ReelsFragmentViewModel;

public class ReelsFragment extends TabsBaseFragment<ReelsFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(ReelsFragmentViewModel.class);
    }
}