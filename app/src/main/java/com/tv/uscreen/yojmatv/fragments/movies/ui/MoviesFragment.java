package com.tv.uscreen.yojmatv.fragments.movies.ui;

import android.os.Bundle;

import com.tv.uscreen.yojmatv.beanModel.TabsBaseFragment;
import com.tv.uscreen.yojmatv.fragments.movies.viewModel.MoviesFragmentViewModel;

public class MoviesFragment extends TabsBaseFragment<MoviesFragmentViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(MoviesFragmentViewModel.class);
    }
}