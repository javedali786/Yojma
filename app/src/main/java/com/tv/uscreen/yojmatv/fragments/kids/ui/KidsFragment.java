package com.tv.uscreen.yojmatv.fragments.kids.ui;

import android.os.Bundle;

import com.tv.uscreen.yojmatv.beanModel.TabsBaseFragment;
import com.tv.uscreen.yojmatv.fragments.kids.viewModel.KidsFragmentViewModel;

public class KidsFragment extends TabsBaseFragment<KidsFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(KidsFragmentViewModel.class);
    }
}