package com.tv.uscreen.yojmatv.callbacks.commonCallbacks;


import com.tv.uscreen.yojmatv.beanModel.responseModels.VersionVerification.Result;

public interface CallBacks {
    void onCountryClick(Result country, int position);

    default void common(boolean status){

    }
}
