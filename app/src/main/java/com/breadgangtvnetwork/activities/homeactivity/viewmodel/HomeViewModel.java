package com.breadgangtvnetwork.activities.homeactivity.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.breadgangtvnetwork.beanModel.emptyResponse.ResponseEmpty;
import com.breadgangtvnetwork.beanModel.responseModels.switchUserDetail.SwitchUser;
import com.breadgangtvnetwork.beanModel.userProfile.UserProfileResponse;
import com.breadgangtvnetwork.repository.home.HomeRepository;
import com.breadgangtvnetwork.repository.userManagement.RegistrationLoginRepository;
import com.google.gson.JsonObject;


public class HomeViewModel extends AndroidViewModel {

    final HomeRepository homeRepository;
    final RegistrationLoginRepository loginRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);

        homeRepository = HomeRepository.getInstance();
        loginRepository = RegistrationLoginRepository.getInstance();
    }

    public LiveData<JsonObject> hitLogout(boolean session, String token) {
        return homeRepository.hitApiLogout(session, token);
    }

    public LiveData<ResponseEmpty> hitVerify(String token) {
        return homeRepository.hitApiVerifyUser(token);
    }

    public LiveData<UserProfileResponse> hitUserProfile(Context context, String token) {
        return loginRepository.getUserProfile(context,token);
    }

    public LiveData<SwitchUser> hitSwitchUser(String token, String  id) {
        return homeRepository.getSwitchUserAPIReponse(token,id);
    }
}
