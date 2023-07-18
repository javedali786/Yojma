package com.tv.uscreen.yojmatv.repository.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.enveu.client.baseCollection.baseCategoryServices.BaseCategoryServices;
import com.enveu.client.userManagement.bean.allSecondaryDetails.SwitchUserDetails;
import com.enveu.client.userManagement.callBacks.LogoutCallBack;
import com.enveu.client.userManagement.callBacks.SwitchUserCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tv.uscreen.yojmatv.beanModel.emptyResponse.ResponseEmpty;
import com.tv.uscreen.yojmatv.beanModel.responseModels.switchUserDetail.SwitchUser;
import com.tv.uscreen.yojmatv.networking.apiendpoints.ApiInterface;
import com.tv.uscreen.yojmatv.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.yojmatv.networking.intercepter.ErrorCodesIntercepter;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeRepository {

    private static HomeRepository instance;

    private HomeRepository() {
    }

    public static HomeRepository getInstance() {
        if (instance == null) {
            instance = new HomeRepository();
        }
        return (instance);
    }


    public LiveData<JsonObject> hitApiLogout(boolean session, String token) {
        final MutableLiveData<JsonObject> responseApi;
        {
            responseApi = new MutableLiveData<>();
            BaseCategoryServices.Companion.getInstance().logoutService(token, new LogoutCallBack() {
                @Override
                public void failure(boolean status, int errorCode, String message) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, 500);
                    responseApi.postValue(jsonObject);
                }

                @Override
                public void success(boolean status, Response<JsonObject> response) {
                        if (status){
                            try {
                                if (response.code() == 404) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                    responseApi.postValue(jsonObject);
                                }if (response.code() == 403) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                    responseApi.postValue(jsonObject);
                                }
                                else if (response.code() == 200) {
                                    Objects.requireNonNull(response.body()).addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                    responseApi.postValue(response.body());
                                } else if (response.code() == 401) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                    responseApi.postValue(jsonObject);
                                } else if (response.code() == 500) {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                    responseApi.postValue(jsonObject);
                                }
                            }catch (Exception e){
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                                responseApi.postValue(jsonObject);
                            }

                        }
                }
            });

        }
        return responseApi;
    }


    public LiveData<ResponseEmpty> hitApiVerifyUser(String token) {
        final MutableLiveData<ResponseEmpty> responseApi;
        {
            responseApi = new MutableLiveData<>();
            ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);


            Call<ResponseEmpty> call = endpoint.getVerifyUser();
            call.enqueue(new Callback<ResponseEmpty>() {
                @Override
                public void onResponse(@NonNull Call<ResponseEmpty> call, @NonNull Response<ResponseEmpty> response) {
                    if (response.code() == 200) {
                        ResponseEmpty empty = new ResponseEmpty();
                        empty.setResponseCode(response.code());
                        empty.setStatus(true);
                        responseApi.postValue(empty);
                    } else {

                        ResponseEmpty empty = new ResponseEmpty();

                        empty.setResponseCode(response.code());
                        responseApi.postValue(empty);
                    }


                }

                @Override
                public void onFailure(@NonNull Call<ResponseEmpty> call, @NonNull Throwable t) {
                    ResponseEmpty responseEmpty = new ResponseEmpty();
                    responseEmpty.setStatus(false);
                    responseApi.postValue(responseEmpty);
                }
            });
        }

        return responseApi;
    }

    public LiveData<SwitchUser> getSwitchUserAPIReponse(String token,String id) {
        final MutableLiveData<SwitchUser> responseApi;
        responseApi = new MutableLiveData<>();

        BaseCategoryServices.Companion.getInstance().switchUserService( token,id,new SwitchUserCallBack() {
            @Override
            public void success(boolean status, Response<SwitchUserDetails> response) {
                if (status) {
                    SwitchUserDetails cl;
                    if (response.body() != null) {
                        String token = response.headers().get("x-auth");
                        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
                        preference.setAppPrefAccessToken("");
                        preference.setAppPrefAccessToken(token);
                        Logger.d("AuthTokenSwithUser: " + preference.getAppPrefAccessToken());
                        Gson gson = new Gson();
                        String tmp = gson.toJson(response.body());
                        SwitchUser loginItemBean = gson.fromJson(tmp, SwitchUser.class);
                        responseApi.postValue(loginItemBean);
                    }
                    else {
                        SwitchUser responseModel = ErrorCodesIntercepter.getInstance().switchUserDetails(response);
                        responseApi.postValue(responseModel);
                    }
                }
            }

            @Override
            public void failure(boolean status, int errorCode, String errorMessage) {
                Logger.e("", "SwitchUser E" + errorMessage);
                SwitchUser cl = new SwitchUser();
                cl.setDebugMessage(errorMessage);
                responseApi.postValue(cl);
            }
        });

        return responseApi;
    }


}
