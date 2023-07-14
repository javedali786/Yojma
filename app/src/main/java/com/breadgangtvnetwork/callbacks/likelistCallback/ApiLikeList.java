package com.breadgangtvnetwork.callbacks.likelistCallback;

import com.breadgangtvnetwork.bean_model_v1_0.listAll.likeList.Response;

public interface ApiLikeList {
    public void onSuccess(boolean isStatus, Response likeListResponse);
    public void onFailure(boolean isStatus,String errorCode,String errorMessage);

}
