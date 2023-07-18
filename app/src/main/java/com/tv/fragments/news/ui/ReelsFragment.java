package com.tv.fragments.news.ui;

import android.os.Bundle;

import com.tv.beanModel.TabsBaseFragment;
import com.tv.fragments.news.viewModel.ReelsFragmentViewModel;

public class ReelsFragment extends TabsBaseFragment<ReelsFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(ReelsFragmentViewModel.class);
    }
}