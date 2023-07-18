package com.tv.fragments.shows.ui;

import android.os.Bundle;

import com.tv.beanModel.TabsBaseFragment;
import com.tv.fragments.shows.viewModel.PodcastFragmentViewModel;

public class PodcastFragment extends TabsBaseFragment<PodcastFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(PodcastFragmentViewModel.class);
    }
}