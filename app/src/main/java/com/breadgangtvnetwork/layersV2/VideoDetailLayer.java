package com.breadgangtvnetwork.layersV2;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.breadgangtvnetwork.beanModel.enveuCommonRailData.RailCommonData;
import com.breadgangtvnetwork.beanModelV3.videoDetailsV2.EnveuVideoDetailsBean;
import com.breadgangtvnetwork.callbacks.apicallback.ApiResponseModel;
import com.breadgangtvnetwork.callbacks.commonCallbacks.CommonApiCallBack;
import com.breadgangtvnetwork.networking.apiendpoints.ApiInterface;
import com.breadgangtvnetwork.networking.apiendpoints.RequestConfig;
import com.breadgangtvnetwork.networking.errormodel.ApiErrorModel;
import com.breadgangtvnetwork.networking.servicelayer.APIServiceLayer;
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod;
import com.breadgangtvnetwork.utils.config.LanguageLayer;
import com.enveu.client.baseCollection.baseCategoryServices.BaseCategoryServices;
import com.enveu.client.videoDetail.callBacks.EnvVideoContentResponse;
import com.google.gson.Gson;

import retrofit2.Response;

public class VideoDetailLayer {

    private static VideoDetailLayer videoDetailLayerInstance;
    private static ApiInterface endpoint;
    ApiResponseModel callBack;
    private String languageCode;

    private VideoDetailLayer() {

    }

    public static VideoDetailLayer getInstance() {
        if (videoDetailLayerInstance == null) {
                endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
                videoDetailLayerInstance = new VideoDetailLayer();


        }
        return (videoDetailLayerInstance);
    }


    public void getVideoDetails(String manualImageAssetId, Context context, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode= LanguageLayer.getCurrentLanguageCode();
        Gson gson = new Gson();

        BaseCategoryServices.Companion.getInstance().getEnvVideoDetails(manualImageAssetId,languageCode,new EnvVideoContentResponse(){

            @Override
            public void success(boolean status, @NonNull Response<com.enveu.client.videoDetail.beanv1_0.EnvMediaDetail> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getData() != null) {
                        if (response.body().getResponseCode() == 2000) {
                            String json = gson.toJson(response.body());
                            Log.w("playlistCall-->>>>>",json);
                            EnveuVideoDetailsBean er=gson.fromJson(json, EnveuVideoDetailsBean.class);
                            RailCommonData railCommonData = new RailCommonData();
                            AppCommonMethod.getEnvAssetDetail(railCommonData, er);
                            callBack.onSuccess(railCommonData);
                        }
                    }
                }else {
                    ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
                    callBack.onError(errorModel);
                }
            }

            @Override
            public void failure(boolean status, int errorCode, @NonNull String message) {
                ApiErrorModel errorModel = new ApiErrorModel(500, message);
                callBack.onFailure(errorModel);
            }
        });


      /*  boolean isKidsMode  = KsPreferenceKeys.getInstance().getKidsMode();
        if (isKidsMode) {
            String parentalRating = AppCommonMethod.getParentalRating();
            endpoint.getEnvVideoDetailsPG(manualImageAssetId).enqueue(new Callback<EnveuVideoDetailsBean>() {
                @Override
                public void onResponse(Call<EnveuVideoDetailsBean> call, Response<EnveuVideoDetailsBean> response) {
                    if (response.isSuccessful()) {

                        if (response.body().getData() instanceof EnveuVideoDetails) {
                            RailCommonData railCommonData = new RailCommonData();
                            AppCommonMethod.getAssetDetail(railCommonData, response);
                            callBack.onSuccess(railCommonData);
                        }

                    } else {
                        ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
                        callBack.onError(errorModel);
                    }

                }

                @Override
                public void onFailure(Call<EnveuVideoDetailsBean> call, Throwable t) {
                    ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                    callBack.onFailure(errorModel);
                }
            });
        }else {

            endpoint.getEnvVideoDetailsPG(manualImageAssetId).enqueue(new Callback<EnveuVideoDetailsBean>() {
                @Override
                public void onResponse(Call<EnveuVideoDetailsBean> call, Response<EnveuVideoDetailsBean> response) {
                    if (response.isSuccessful()) {

                        if (response.body().getData() instanceof EnveuVideoDetails) {
                            RailCommonData railCommonData = new RailCommonData();
                            AppCommonMethod.getAssetDetail(railCommonData, response);
                            callBack.onSuccess(railCommonData);
                        }

                    } else {
                        ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
                        callBack.onError(errorModel);
                    }

                }

                @Override
                public void onFailure(Call<EnveuVideoDetailsBean> call, Throwable t) {
                    ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                    callBack.onFailure(errorModel);
                }
            });
        }*/
    }


    public void getAssetTypeHero(String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        APIServiceLayer.getInstance().getAssetTypeHero(manualImageAssetId, commonApiCallBack);
    }


}