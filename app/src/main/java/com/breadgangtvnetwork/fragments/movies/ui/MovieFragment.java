package com.breadgangtvnetwork.fragments.movies.ui;

import android.os.Bundle;

import com.breadgangtvnetwork.beanModel.TabsBaseFragment;
import com.breadgangtvnetwork.fragments.movies.viewModel.MovieFragmentViewModel;

public class MovieFragment extends TabsBaseFragment<MovieFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(MovieFragmentViewModel.class);
    }
}