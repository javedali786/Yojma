package com.tv.uscreen.fragments.shows.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.tv.uscreen.baseModels.HomeBaseViewModel;
import com.tv.uscreen.repository.home.HomeFragmentRepository;
import com.tv.uscreen.utils.constants.AppConstants;

import java.util.List;

public class PodcastFragmentViewModel extends HomeBaseViewModel {
    public PodcastFragmentViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    public LiveData<List<BaseCategory>> getAllCategories() {

        return HomeFragmentRepository.getInstance().getCategories(AppConstants.HOME_ENVEU);
    }


    @Override
    public void resetObject() {

    }
}
