package com.tv.callbacks.commonCallbacks;

public interface CommonApiCallBack {
    void onSuccess(Object item);

    void onFailure(Throwable throwable);

    void onFinish();

}
