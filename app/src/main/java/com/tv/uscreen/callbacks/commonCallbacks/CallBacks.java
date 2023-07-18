package com.tv.uscreen.callbacks.commonCallbacks;


import com.tv.uscreen.beanModel.responseModels.VersionVerification.Result;

public interface CallBacks {
    void onCountryClick(Result country, int position);

    default void common(boolean status){

    }
}
