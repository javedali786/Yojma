package com.tv.uscreen.yojmatv.fragments.news.ui;

import android.os.Bundle;

import com.tv.uscreen.yojmatv.beanModel.TabsBaseFragment;
import com.tv.uscreen.yojmatv.fragments.news.viewModel.ReelsFragmentViewModel;

public class ReelsFragment extends TabsBaseFragment<ReelsFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(ReelsFragmentViewModel.class);
    }
}