package com.tv.uscreen.yojmatv.fragments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.tv.uscreen.yojmatv.baseModels.HomeBaseViewModel;
import com.tv.uscreen.yojmatv.repository.home.HomeFragmentRepository;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;

import java.util.List;


public class ContestFragmentViewModel extends HomeBaseViewModel {

    public ContestFragmentViewModel(@NonNull Application application) {
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
