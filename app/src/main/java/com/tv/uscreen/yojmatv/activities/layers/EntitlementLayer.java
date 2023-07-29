package com.tv.uscreen.yojmatv.activities.layers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.tv.uscreen.yojmatv.beanModel.entitle.ResponseEntitle;
import com.tv.uscreen.yojmatv.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.yojmatv.networking.detailPlayer.APIDetails;
import com.tv.uscreen.yojmatv.networking.intercepter.ErrorCodesIntercepter;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntitlementLayer {

    private static EntitlementLayer entitlement;
    public synchronized static EntitlementLayer getInstance() {
        if (entitlement == null) {
            entitlement = new EntitlementLayer();
        }
        return entitlement;
    }

    public LiveData<ResponseEntitle> hitApiEntitlement(String token, String sku) {
        MutableLiveData<ResponseEntitle> responseOutput = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getUserInteration(token).create(APIDetails.class);
        Call<ResponseEntitle> call = endpoint.checkEntitlement(sku);
        call.enqueue(new Callback<ResponseEntitle>() {
            @Override
            public void onResponse(@NonNull Call<ResponseEntitle> call, @NonNull Response<ResponseEntitle> response) {
                if (response.code() == 200) {
                    ResponseEntitle responseEntitlement = new ResponseEntitle();

                    responseEntitlement.setResponseCode(response.code());
                    responseEntitlement.setStatus(true);
                    responseEntitlement.setData(Objects.requireNonNull(response.body()).getData());
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body().getData());
                    Log.d("Javed", "onResponse: " +  json);
                    responseOutput.postValue(responseEntitlement);
                } else {
                    ResponseEntitle responseModel = ErrorCodesIntercepter.getInstance().checkEntitlement(response);
                    responseOutput.postValue(responseModel);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseEntitle> call, @NonNull Throwable t) {
                ResponseEntitle responseEntitlement = new ResponseEntitle();
                responseEntitlement.setStatus(false);
                responseOutput.postValue(responseEntitlement);


            }
        });
        return responseOutput;
    }



    public LiveData<com.tv.uscreen.yojmatv.activities.detail.viewModel.Response> getGeoBlocking( String mediaContentId) {
        MutableLiveData<com.tv.uscreen.yojmatv.activities.detail.viewModel.Response> responseOutput = new MutableLiveData<>();

        APIDetails endpoint = RequestConfig.getGeoBlocking().create(APIDetails.class);
        Call<com.tv.uscreen.yojmatv.activities.detail.viewModel.Response> call = endpoint.getGeoBlocking(mediaContentId);
        call.enqueue(new Callback<com.tv.uscreen.yojmatv.activities.detail.viewModel.Response>() {
            @Override
            public void onResponse(@NonNull Call<com.tv.uscreen.yojmatv.activities.detail.viewModel.Response> call, @NonNull Response<com.tv.uscreen.yojmatv.activities.detail.viewModel.Response> response) {
                if (response.code() == 200) {
                    com.tv.uscreen.yojmatv.activities.detail.viewModel.Response response1 = new com.tv.uscreen.yojmatv.activities.detail.viewModel.Response();

                    response1.setResponseCode(response.code());
                    response1.setData(Objects.requireNonNull(response.body()).getData());
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body().getData());
                    Log.d("Javed", "onResponse: " +  json);
                    responseOutput.postValue(response1);
                } else {
                    responseOutput.postValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<com.tv.uscreen.yojmatv.activities.detail.viewModel.Response> call, @NonNull Throwable t) {
                com.tv.uscreen.yojmatv.activities.detail.viewModel.Response response = new com.tv.uscreen.yojmatv.activities.detail.viewModel.Response();
                response.setResponseCode(response.getResponseCode());
                responseOutput.postValue(response);
            }
        });
        return responseOutput;
    }



}
