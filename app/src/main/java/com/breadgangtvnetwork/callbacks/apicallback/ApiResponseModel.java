package com.breadgangtvnetwork.callbacks.apicallback;

import com.breadgangtvnetwork.networking.errormodel.ApiErrorModel;
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;

import java.util.List;

public interface ApiResponseModel<T> {
    void onStart();

    default void onSuccess(T response){

    }
    default void onSuccess(List<BaseCategory> catList){

    }
    default void onError(ApiErrorModel apiError){

    }

    default void onFailure(ApiErrorModel httpError){

    }
}