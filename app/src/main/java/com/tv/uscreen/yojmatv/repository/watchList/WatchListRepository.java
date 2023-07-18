package com.tv.uscreen.yojmatv.repository.watchList;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.tv.uscreen.yojmatv.beanModel.AssetHistoryContinueWatching.ResponseAssetHistory;
import com.tv.uscreen.yojmatv.beanModel.emptyResponse.ResponseEmpty;
import com.tv.uscreen.yojmatv.networking.apiendpoints.ApiInterface;
import com.tv.uscreen.yojmatv.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.yojmatv.networking.detailPlayer.APIDetails;
import com.tv.uscreen.yojmatv.userAssetList.ResponseUserAssetList;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;

import java.util.Objects;

import app.doxzilla.activities.order_history.model.OrderHistoryModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchListRepository {


    private static WatchListRepository instance;

    private WatchListRepository() {
    }

    public static WatchListRepository getInstance() {
        if (instance == null) {
            instance = new WatchListRepository();
        }
        return (instance);
    }
    public LiveData<ResponseEmpty> hitApiRemoveFromWatchList(String token, String data) {
        MutableLiveData<ResponseEmpty> responsePlayer = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getClientInterceptor(token).create(APIDetails.class);
        Call<ResponseEmpty> call = endpoint.getRemoveFromWatchList(data);
        call.enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(@NonNull Call<ResponseEmpty> call, @NonNull Response<ResponseEmpty> response) {
                //  Logger.e("", "" + response.body());
                ResponseEmpty responseEmpty = new ResponseEmpty();
                if (response.code() == 200) {
                    responseEmpty.setResponseCode(response.code());
                    responseEmpty.setStatus(true);
                    responsePlayer.postValue(responseEmpty);
                } else {

                    responseEmpty.setResponseCode(Objects.requireNonNull(response.code()));
                    responseEmpty.setStatus(false);
                    responsePlayer.postValue(responseEmpty);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseEmpty> call, @NonNull Throwable t) {
                ResponseEmpty responseEmpty = new ResponseEmpty();
                responseEmpty.setStatus(false);
                responsePlayer.postValue(responseEmpty);
            }
        });

        return responsePlayer;
    }


    public LiveData<ResponseAssetHistory> getAssetHistory(String token, int page, int size) {
        MutableLiveData<ResponseAssetHistory> assetContinue = new MutableLiveData<>();

        ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);

        final JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_PAGE, page);
        requestParam.addProperty(AppConstants.API_PARAM_SIZE, size);
        requestParam.add(AppConstants.API_PARAM_SERIES_ID, JsonNull.INSTANCE);
        requestParam.add(AppConstants.API_PARAM_SEASON_ID, JsonNull.INSTANCE);

        Logger.e("", "page " + page);

        Call<ResponseAssetHistory> call = endpoint.getAssetHistory(requestParam);

        call.enqueue(new Callback<ResponseAssetHistory>() {
            @Override
            public void onResponse(Call<ResponseAssetHistory> call, Response<ResponseAssetHistory> response) {
                if (response.code() == 200) {
                    response.body().setStatus(true);
                    assetContinue.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseAssetHistory> call, Throwable t) {
                assetContinue.postValue(new ResponseAssetHistory());
            }
        });
        return assetContinue;
    }


    public LiveData<ResponseUserAssetList> getAssetList(String token, ResponseAssetHistory list, boolean isFetchData) {
        ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);
        MutableLiveData<ResponseUserAssetList> mModel = new MutableLiveData<>();
        JsonObject jsonObj = new JsonObject();
        JsonArray jsonArr = new JsonArray();
        try {
            for (int i = 0; i < list.getData().getItems().size(); i++) {
                JsonObject pnObj = new JsonObject();
                pnObj.addProperty(AppConstants.API_PARAM_ID, list.getData().getItems().get(i).getId());
                pnObj.addProperty(AppConstants.API_PARAM_TYPE, list.getData().getItems().get(i).getType());
                jsonArr.add(pnObj);
            }
            jsonObj.addProperty(AppConstants.API_PARAM_FETCH_DATA, isFetchData);
            jsonObj.add("userAssetListDTOList", jsonArr);
        } catch (Exception e) {

        }

        Call<ResponseUserAssetList> call = endpoint.getAssetList(jsonObj);

        call.enqueue(new Callback<ResponseUserAssetList>() {
            @Override
            public void onResponse(Call<ResponseUserAssetList> call, Response<ResponseUserAssetList> response) {
                if (response.code() == 200) {
                    response.body().setStatus(true);
                    mModel.postValue(response.body());
                } else {
                    ResponseUserAssetList responseFail = new ResponseUserAssetList();
                    responseFail.setStatus(false);
                    mModel.postValue(responseFail);
                }
            }

            @Override
            public void onFailure(Call<ResponseUserAssetList> call, Throwable t) {
                ResponseUserAssetList response = new ResponseUserAssetList();
                response.setStatus(false);
                mModel.postValue(response);
            }
        });
        return mModel;
    }

    public LiveData<OrderHistoryModel> getOrderHistoryModel(String token, String page, String size) {

        MutableLiveData<OrderHistoryModel> orderHistory = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getClientOrderHistoryInterceptor(token).create(ApiInterface.class);
        Call<OrderHistoryModel> call = endpoint.getOrderHistory(page, size);

        call.enqueue(new Callback<OrderHistoryModel>() {
            @Override
            public void onResponse(@NonNull Call<OrderHistoryModel> call, @NonNull Response<OrderHistoryModel> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        response.body().setSuccessful(true);
                        orderHistory.postValue(response.body());
                    }else {
                        orderHistory.postValue(null);
                    }
                }else {
                    orderHistory.postValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderHistoryModel> call, @NonNull Throwable t) {
                OrderHistoryModel orderHistoryModel = new OrderHistoryModel();
                orderHistoryModel.setSuccessful(false);
                orderHistory.postValue(orderHistoryModel);
            }
        });
        return orderHistory;
    }


}
