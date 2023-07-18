package com.tv.repository.userinteraction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.tv.beanModel.emptyResponse.ResponseEmpty;
import com.tv.beanModel.responseGetWatchlist.ResponseGetIsWatchlist;
import com.tv.beanModel.responseIsLike.ResponseIsLike;
import com.tv.networking.apiendpoints.ApiInterface;
import com.tv.networking.apiendpoints.RequestConfig;
import com.tv.networking.intercepter.ErrorCodesIntercepter;
import com.tv.utils.constants.AppConstants;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInterationRepository {

    private static UserInterationRepository instance;

    private UserInterationRepository() {
    }

    public static UserInterationRepository getInstance() {
        if (instance == null) {
            instance = new UserInterationRepository();
        }
        return (instance);
    }


    public LiveData<ResponseIsLike> hitApiIsLike(String token, int seriesId) {
        MutableLiveData<ResponseIsLike> responseData = new MutableLiveData<>();

        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.getIsLikeVod(seriesId).enqueue(new Callback<ResponseIsLike>() {
            @Override
            public void onResponse(Call<ResponseIsLike> call, Response<ResponseIsLike> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseIsLike empty = ErrorCodesIntercepter.getInstance().isLike(response);
                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<ResponseIsLike> call, Throwable t) {
                try {
                    ResponseIsLike empty = new ResponseIsLike();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }
    public LiveData<ResponseEmpty> hitApiDeleteLikeGOI(String token, int seriesId,String assetType) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.deleteLikeGOI(seriesId).enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseEmpty empty = ErrorCodesIntercepter.getInstance().likeAsset(response);
                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                try {
                    ResponseEmpty empty = new ResponseEmpty();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }

    public LiveData<ResponseEmpty> hitApiDeleteLike(String token, int seriesId) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.deleteLike(seriesId).enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseEmpty empty = ErrorCodesIntercepter.getInstance().likeAsset(response);
                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                try {
                    ResponseEmpty empty = new ResponseEmpty();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }

    public LiveData<ResponseEmpty> hitRemoveWatchlist(String token, int seriesId) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.removeWatchlist(seriesId).enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseEmpty empty = ErrorCodesIntercepter.getInstance().addToWatchlisht(response);
                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                try {
                    ResponseEmpty empty = new ResponseEmpty();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }

    public LiveData<ResponseEmpty> hitRemoveWatchlistGOI(String token, int seriesId) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.removeWatchlistGOI(seriesId).enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseEmpty empty = ErrorCodesIntercepter.getInstance().addToWatchlisht(response);
                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                try {
                    ResponseEmpty empty = new ResponseEmpty();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }


    public LiveData<ResponseEmpty> addToLikeGOI(String token, int seriesId,String assetType) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        if (assetType.equalsIgnoreCase(AppConstants.VIDEO)) {
            endpoint.addToLikeGOIVod(seriesId).enqueue(new Callback<ResponseEmpty>() {
                @Override
                public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                    try {
                        if (response.code() == 200) {
                            response.body().setStatus(true);
                            responseData.postValue(response.body());
                        } else {
                            ResponseEmpty empty = ErrorCodesIntercepter.getInstance().likeAsset(response);
                            responseData.postValue(empty);

                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                    try {
                        ResponseEmpty empty = new ResponseEmpty();
                        empty.setStatus(false);
                        empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                        empty.setDebugMessage(t.getMessage());
                        responseData.postValue(empty);
                    } catch (Exception e) {

                    }
                }
            });

        } else {
            endpoint.addToLikeGOICustom(seriesId).enqueue(new Callback<ResponseEmpty>() {
                @Override
                public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                    try {
                        if (response.code() == 200) {
                            response.body().setStatus(true);
                            responseData.postValue(response.body());
                        } else {
                            ResponseEmpty empty = ErrorCodesIntercepter.getInstance().likeAsset(response);
                            responseData.postValue(empty);

                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                    try {
                        ResponseEmpty empty = new ResponseEmpty();
                        empty.setStatus(false);
                        empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                        empty.setDebugMessage(t.getMessage());
                        responseData.postValue(empty);
                    } catch (Exception e) {

                    }
                }
            });

        }


        return responseData;
    }


    public LiveData<ResponseEmpty> hitApiAddLike(String token, int seriesId) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.addToLikeVod(seriesId).enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseEmpty empty = ErrorCodesIntercepter.getInstance().likeAsset(response);
                        responseData.postValue(empty);

                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                try {
                    ResponseEmpty empty = new ResponseEmpty();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }

    public LiveData<ResponseGetIsWatchlist> hitApiIsToWatchList(String token, int seriesId) {
        MutableLiveData<ResponseGetIsWatchlist> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.getIsWatchList(seriesId).enqueue(new Callback<ResponseGetIsWatchlist>() {
            @Override
            public void onResponse(Call<ResponseGetIsWatchlist> call, Response<ResponseGetIsWatchlist> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseGetIsWatchlist empty = ErrorCodesIntercepter.getInstance().isWatchlist(response);
                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<ResponseGetIsWatchlist> call, Throwable t) {
                try {
                    ResponseGetIsWatchlist empty = new ResponseGetIsWatchlist();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }


    public LiveData<ResponseEmpty> hitApiAddToWatchList(String token, int seriesId) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        endpoint.addToWatchList(seriesId).enqueue(new Callback<ResponseEmpty>() {
            @Override
            public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                try {
                    if (response.code() == 200) {
                        response.body().setStatus(true);
                        responseData.postValue(response.body());
                    } else {
                        ResponseEmpty empty = ErrorCodesIntercepter.getInstance().addToWatchlisht(response);
                        responseData.postValue(empty);
                    }

                } catch (Exception e) {

                }


            }

            @Override
            public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                try {
                    ResponseEmpty empty = new ResponseEmpty();
                    empty.setStatus(false);
                    empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    empty.setDebugMessage(t.getMessage());
                    responseData.postValue(empty);
                } catch (Exception e) {

                }
            }
        });


        return responseData;
    }


    public LiveData<ResponseEmpty> hitApiAddToWatchListGOI(String token, int seriesId,String assetType) {
        MutableLiveData<ResponseEmpty> responseData = new MutableLiveData<>();
        ApiInterface endpoint = RequestConfig.getUserInteration(token).create(ApiInterface.class);
        if(assetType.equalsIgnoreCase(AppConstants.VIDEO)){
            endpoint.addToWatchListGOIVod(seriesId).enqueue(new Callback<ResponseEmpty>() {
                @Override
                public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                    try {
                        if (response.code() == 200) {
                            response.body().setStatus(true);
                            responseData.postValue(response.body());
                        } else {
                            ResponseEmpty empty = ErrorCodesIntercepter.getInstance().addToWatchlisht(response);
                            responseData.postValue(empty);
                        }

                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                    try {
                        ResponseEmpty empty = new ResponseEmpty();
                        empty.setStatus(false);
                        empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                        empty.setDebugMessage(t.getMessage());
                        responseData.postValue(empty);
                    } catch (Exception e) {

                    }
                }
            });

        } else  {
            endpoint.addToWatchListGOICustom(seriesId).enqueue(new Callback<ResponseEmpty>() {
                @Override
                public void onResponse(Call<ResponseEmpty> call, Response<ResponseEmpty> response) {
                    try {
                        if (response.code() == 200) {
                            response.body().setStatus(true);
                            responseData.postValue(response.body());
                        } else {
                            ResponseEmpty empty = ErrorCodesIntercepter.getInstance().addToWatchlisht(response);
                            responseData.postValue(empty);
                        }

                    } catch (Exception e) {

                    }


                }

                @Override
                public void onFailure(Call<ResponseEmpty> call, Throwable t) {
                    try {
                        ResponseEmpty empty = new ResponseEmpty();
                        empty.setStatus(false);
                        empty.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                        empty.setDebugMessage(t.getMessage());
                        responseData.postValue(empty);
                    } catch (Exception e) {

                    }
                }
            });
        }

        return responseData;
    }


    public LiveData<JsonObject> hitApiLogout(boolean session, String token) {
        final MutableLiveData<JsonObject> responseApi;
        {
            responseApi = new MutableLiveData<>();
            ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);

            Call<JsonObject> call = endpoint.getLogout(session);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {

                    } catch (Exception e) {

                    }
                    if (response.code() == 404) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                        responseApi.postValue(jsonObject);
                    } else if (response.code() == 200) {
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
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });


        }

        return responseApi;
    }


}
