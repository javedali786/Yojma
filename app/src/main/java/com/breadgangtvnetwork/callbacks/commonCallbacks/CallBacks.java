package com.breadgangtvnetwork.callbacks.commonCallbacks;


import com.breadgangtvnetwork.beanModel.responseModels.VersionVerification.Result;

public interface CallBacks {
    void onCountryClick(Result country, int position);

    default void common(boolean status){

    }
}
