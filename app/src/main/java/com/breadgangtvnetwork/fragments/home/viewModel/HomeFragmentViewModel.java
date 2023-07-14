package com.breadgangtvnetwork.fragments.home.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.breadgangtvnetwork.baseModels.HomeBaseViewModel;
import com.breadgangtvnetwork.repository.home.HomeFragmentRepository;
import com.breadgangtvnetwork.utils.constants.AppConstants;
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;

import java.util.List;

public class HomeFragmentViewModel extends HomeBaseViewModel {

    public HomeFragmentViewModel(@NonNull Application application) {
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
