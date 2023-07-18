package com.tv.uscreen.yojmatv.activities.homeactivity.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.JsonObject;
import com.tv.uscreen.yojmatv.beanModel.emptyResponse.ResponseEmpty;
import com.tv.uscreen.yojmatv.beanModel.responseModels.switchUserDetail.SwitchUser;
import com.tv.uscreen.yojmatv.beanModel.userProfile.UserProfileResponse;
import com.tv.uscreen.yojmatv.repository.home.HomeRepository;
import com.tv.uscreen.yojmatv.repository.userManagement.RegistrationLoginRepository;


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
