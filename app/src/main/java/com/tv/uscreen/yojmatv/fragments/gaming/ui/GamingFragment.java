package com.tv.uscreen.yojmatv.fragments.gaming.ui;

import android.os.Bundle;

import com.tv.uscreen.yojmatv.beanModel.TabsBaseFragment;
import com.tv.uscreen.yojmatv.fragments.gaming.viewModel.GamingFragmentViewModel;

public class GamingFragment extends TabsBaseFragment<GamingFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(GamingFragmentViewModel.class);
    }
}