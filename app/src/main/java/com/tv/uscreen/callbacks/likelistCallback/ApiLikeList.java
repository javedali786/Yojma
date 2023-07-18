package com.tv.uscreen.callbacks.likelistCallback;

import com.tv.uscreen.bean_model_v1_0.listAll.likeList.Response;

public interface ApiLikeList {
    public void onSuccess(boolean isStatus, Response likeListResponse);
    public void onFailure(boolean isStatus,String errorCode,String errorMessage);

}
