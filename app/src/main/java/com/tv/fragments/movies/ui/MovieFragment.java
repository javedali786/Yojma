package com.tv.fragments.movies.ui;

import android.os.Bundle;

import com.tv.beanModel.TabsBaseFragment;
import com.tv.fragments.movies.viewModel.MovieFragmentViewModel;

public class MovieFragment extends TabsBaseFragment<MovieFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(MovieFragmentViewModel.class);
    }
}