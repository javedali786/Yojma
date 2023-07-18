package com.tv.uscreen.fragments.shows.ui;

import android.os.Bundle;

import com.tv.uscreen.beanModel.TabsBaseFragment;
import com.tv.uscreen.fragments.shows.viewModel.PodcastFragmentViewModel;

public class PodcastFragment extends TabsBaseFragment<PodcastFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(PodcastFragmentViewModel.class);
    }
}