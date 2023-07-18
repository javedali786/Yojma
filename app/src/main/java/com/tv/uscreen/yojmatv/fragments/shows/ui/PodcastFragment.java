package com.tv.uscreen.yojmatv.fragments.shows.ui;

import android.os.Bundle;

import com.tv.uscreen.yojmatv.beanModel.TabsBaseFragment;
import com.tv.uscreen.yojmatv.fragments.shows.viewModel.PodcastFragmentViewModel;

public class PodcastFragment extends TabsBaseFragment<PodcastFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(PodcastFragmentViewModel.class);
    }
}