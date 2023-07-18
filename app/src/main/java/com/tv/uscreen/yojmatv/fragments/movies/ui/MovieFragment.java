package com.tv.uscreen.yojmatv.fragments.movies.ui;

import android.os.Bundle;

import com.tv.uscreen.yojmatv.beanModel.TabsBaseFragment;
import com.tv.uscreen.yojmatv.fragments.movies.viewModel.MovieFragmentViewModel;

public class MovieFragment extends TabsBaseFragment<MovieFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(MovieFragmentViewModel.class);
    }
}