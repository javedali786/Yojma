package com.breadgangtvnetwork.fragments.shows.ui;

import android.os.Bundle;

import com.breadgangtvnetwork.beanModel.TabsBaseFragment;
import com.breadgangtvnetwork.fragments.shows.viewModel.PodcastFragmentViewModel;

public class PodcastFragment extends TabsBaseFragment<PodcastFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(PodcastFragmentViewModel.class);
    }
}