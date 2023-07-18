package com.tv.uscreen.callbacks.RequestOffferCallBack;

import com.tv.uscreen.bean_model_v1_0.listAll.RequestOfferList.Response;

public interface RequestOfferCallBack {
    void success(boolean isStatus, Response response);
    void failure(boolean isStatus,String errorCode,String errorMessage);
}
