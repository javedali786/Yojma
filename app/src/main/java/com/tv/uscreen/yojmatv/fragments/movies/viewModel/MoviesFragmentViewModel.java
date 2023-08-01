package com.tv.uscreen.yojmatv.fragments.movies.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.tv.uscreen.yojmatv.baseModels.HomeBaseViewModel;
import com.tv.uscreen.yojmatv.repository.home.HomeFragmentRepository;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;

import java.util.List;


public class MoviesFragmentViewModel extends HomeBaseViewModel {

    public MoviesFragmentViewModel(@NonNull Application application) {
        super(application);

    }

    @Override
    public LiveData<List<BaseCategory>> getAllCategories() {

        return HomeFragmentRepository.getInstance().getCategories(AppConstants.ORIGINAL_ENVEU);
    }


    @Override
    public void resetObject() {

    }
}
