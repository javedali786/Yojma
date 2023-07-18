package com.tv.uscreen.fragments.movies.ui;

import android.os.Bundle;

import com.tv.uscreen.beanModel.TabsBaseFragment;
import com.tv.uscreen.fragments.movies.viewModel.MovieFragmentViewModel;

public class MovieFragment extends TabsBaseFragment<MovieFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(MovieFragmentViewModel.class);
    }
}