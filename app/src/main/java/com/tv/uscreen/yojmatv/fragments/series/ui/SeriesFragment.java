package com.tv.uscreen.yojmatv.fragments.series.ui;

import android.os.Bundle;

import com.tv.uscreen.yojmatv.beanModel.TabsBaseFragment;
import com.tv.uscreen.yojmatv.fragments.series.viewModel.SeriesFragmentViewModel;

public class SeriesFragment extends TabsBaseFragment<SeriesFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(SeriesFragmentViewModel.class);
    }
}