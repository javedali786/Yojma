package com.tv.uscreen.yojmatv.networking.servicelayer;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.enveu.client.baseCollection.baseCategoryServices.BaseCategoryServices;
import com.enveu.client.bookmarking.bean.continuewatching.ContinueWatchingBookmark;
import com.enveu.client.callBacks.EnveuCallBacks;
import com.enveu.client.playlist.callBacks.EnvPlaylistResponse;
import com.enveu.client.watchHistory.beans.ItemsItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tv.uscreen.yojmatv.activities.watchList.model.VideoIdModel;
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.uscreen.yojmatv.beanModel.search.SearchRequestModel;
import com.tv.uscreen.yojmatv.beanModelV3.continueWatching.ContinueWatchingModel;
import com.tv.uscreen.yojmatv.beanModelV3.continueWatching.DataItem;
import com.tv.uscreen.yojmatv.beanModelV3.playListModelV2.EnveuCommonResponse;
import com.tv.uscreen.yojmatv.beanModelV3.searchV2.Data;
import com.tv.uscreen.yojmatv.beanModelV3.searchV2.ResponseSearch;
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.uscreen.yojmatv.beanModelV3.videoDetailsV2.EnveuVideoDetailsBean;
import com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.ListAllContent;
import com.tv.uscreen.yojmatv.bean_model_v1_0.videoDetailBean.EnvVideoDetailsBean;
import com.tv.uscreen.yojmatv.callbacks.RequestOffferCallBack.RequestOfferCallBack;
import com.tv.uscreen.yojmatv.callbacks.apicallback.ApiResponseModel;
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.CommonApiCallBack;
import com.tv.uscreen.yojmatv.callbacks.likelistCallback.ApiLikeList;
import com.tv.uscreen.yojmatv.networking.apiendpoints.ApiInterface;
import com.tv.uscreen.yojmatv.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.yojmatv.networking.errormodel.ApiErrorModel;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.MediaTypeConstants;
import com.tv.uscreen.yojmatv.utils.ObjectHelper;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.config.ImageLayer;
import com.tv.uscreen.yojmatv.utils.config.LanguageLayer;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIServiceLayer {

    private static APIServiceLayer projectRepository;
    private static ApiInterface endpoint;
    private ApiResponseModel callBack;
    private List<EnveuVideoItemBean> enveuVideoItemBeans;
    private static final String API_SERVICE_LAYER = "APIServiceLayer";


    public synchronized static APIServiceLayer getInstance() {
        if (projectRepository == null) {
            if (RequestConfig.getEnveuClient()!=null) {
                endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            }
            projectRepository = new APIServiceLayer();
        }
        return projectRepository;
    }


    public LiveData<List<BaseCategory>> getCategories(String screenId) {
        MutableLiveData<List<BaseCategory>> liveData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().categoryService("",screenId, new EnveuCallBacks() {
            @Override
            public void success(boolean status, List<BaseCategory> categoryList) {
                if (status) {
                    Collections.sort(categoryList, new Comparator<BaseCategory>() {
                        @Override
                        public int compare(BaseCategory o1, BaseCategory o2) {
                            return o1.getDisplayOrder().compareTo(o2.getDisplayOrder());
                        }
                    });
                    liveData.postValue(categoryList);
                }
            }

            @Override
            public void failure(boolean status, int errorCode, String errorMessage) {
                liveData.postValue(new ArrayList<>());
            }
        });
        return liveData;
    }

    String languageCode = "";

    public MutableLiveData<EnveuCommonResponse> getPlayListById(String playListId, int pageNumber, int pageSize) {

        MutableLiveData<EnveuCommonResponse> enveuCommonResponseMutableLiveData = new MutableLiveData<>();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        Gson gson = new Gson();
        BaseCategoryServices.Companion.getInstance().getEnvPlaylistDetailsById(playListId, languageCode, pageNumber, pageSize, new EnvPlaylistResponse() {
                    @Override
                    public void success(boolean status, @NonNull Response<com.enveu.client.playlist.beanv1_0.EnvPlaylistContents> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null && response.body().getData() != null && response.body().getData().getItems().size()>0) {
                                if (response.body().getResponseCode() == 2000){
                                    String json = gson.toJson(response.body());
                                    Log.w("playlistCall-->>>>>",json);
                                    EnveuCommonResponse er=gson.fromJson(json, EnveuCommonResponse.class);
                                    enveuCommonResponseMutableLiveData.postValue(er);
                                }
                                else {
                                    enveuCommonResponseMutableLiveData.postValue(null);
                                }
                            }
                            else {
                                enveuCommonResponseMutableLiveData.postValue(null);
                            }
                        } else {
                            enveuCommonResponseMutableLiveData.postValue(null);
                        }
                    }

                    @Override
                    public void failure(boolean status, int errorCode, @NonNull String message) {
                        enveuCommonResponseMutableLiveData.postValue(null);
                    }
                });


        return enveuCommonResponseMutableLiveData;
    }

    public void getAssetTypeHero(String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (endpoint!=null) {

            boolean isKidsMode  = KsPreferenceKeys.getInstance().getKidsMode();
            if (isKidsMode) {
                String parentalRating = AppCommonMethod.getParentalRating();
                endpoint.getVideoDetailsPG(manualImageAssetId, languageCode,parentalRating).enqueue(new Callback<EnveuVideoDetailsBean>() {
                    @Override
                    public void onResponse(Call<EnveuVideoDetailsBean> call, Response<EnveuVideoDetailsBean> response) {
                        if (response.isSuccessful()) {
                            commonApiCallBack.onSuccess(response.body().getData());
                        } else {
                            commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                        }
                    }

                    @Override
                    public void onFailure(Call<EnveuVideoDetailsBean> call, Throwable t) {
                        commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                    }
                });
            }else {

                endpoint.getVideoDetails(manualImageAssetId, languageCode).enqueue(new Callback<EnveuVideoDetailsBean>() {
                    @Override
                    public void onResponse(Call<EnveuVideoDetailsBean> call, Response<EnveuVideoDetailsBean> response) {
                        if (response.isSuccessful()) {
                            commonApiCallBack.onSuccess(response.body().getData());
                        } else {
                            commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                        }
                    }

                    @Override
                    public void onFailure(Call<EnveuVideoDetailsBean> call, Throwable t) {
                        commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                    }
                });
            }
        }
    }

    public void getPlayListByWithPagination(String playlistID,
                                            int pageNumber,
                                            int pageSize,
                                            BaseCategory screenWidget, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (endpoint!=null) {

            boolean isKidsMode  = KsPreferenceKeys.getInstance().getKidsMode();
            if (isKidsMode){
                String parentalRating = AppCommonMethod.getParentalRating();
                endpoint.getPlaylistDetailsByIdWithPG(playlistID, languageCode, pageNumber, pageSize,parentalRating).enqueue(new Callback<EnveuCommonResponse>() {
                    @Override
                    public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                        if (response.body() != null && response.body().getData() != null) {
                            RailCommonData railCommonData = new RailCommonData(response.body().getData(), screenWidget, true);
                            railCommonData.setStatus(true);
                            callBack.onSuccess(railCommonData);
                        } else {
                            ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
                            callBack.onError(errorModel);
                        }
                    }

                    @Override
                    public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {
                        ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                        callBack.onFailure(errorModel);
                    }
                });
            }else {

                endpoint.getPlaylistDetailsById(playlistID, languageCode, pageNumber, pageSize).enqueue(new Callback<EnveuCommonResponse>() {
                    @Override
                    public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                        if (response.body() != null && response.body().getData() != null) {
                            RailCommonData railCommonData = new RailCommonData(response.body().getData(), screenWidget, false);
                            railCommonData.setStatus(true);
                            callBack.onSuccess(railCommonData);
                        } else {
                            ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
                            callBack.onError(errorModel);
                        }
                    }

                    @Override
                    public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {
                        ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                        callBack.onFailure(errorModel);
                    }
                });
            }
        }

    }


    public void getSeasonEpisodesV2(int seriesId, int pageNumber,
                                    int size, int seasonNumber, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (endpoint!=null) {

            boolean isKidsMode  = KsPreferenceKeys.getInstance().getKidsMode();
            if (isKidsMode) {
                String parentalRating = AppCommonMethod.getParentalRating();
                endpoint.getRelatedContentPG(seriesId, seasonNumber, pageNumber, size, languageCode,parentalRating).enqueue(new Callback<EnveuCommonResponse>() {
                    @Override
                    public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                        parseResponseAsRailCommonData(response,false);
                    }

                    @Override
                    public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {
                        ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                        callBack.onFailure(errorModel);
                    }
                });
            }else {

                endpoint.getRelatedContent(seriesId, seasonNumber, pageNumber, size, languageCode).enqueue(new Callback<ListAllContent>() {
                    @Override
                    public void onResponse(@NonNull Call<ListAllContent> call, @NonNull Response<ListAllContent> response) {
                        parseEpisodeResponseAsRailCommonData(response,false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListAllContent> call, @NonNull Throwable t) {
                        ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                        callBack.onFailure(errorModel);
                    }
                });
            }
        }

    }

    private void parseEpisodeResponseAsRailCommonData(Response<ListAllContent> response,boolean isIntentFromForYou) {
        if (response.body() != null && response.body().getData() != null) {
            if (response.body().getData().getPageNumber() == 0 && response.body().getData().getTotalElements() == 0) {
                ApiErrorModel errorModel = new ApiErrorModel(500, "");
                callBack.onError(errorModel);
            } else {
                RailCommonData railCommonData = new RailCommonData(response.body().getData(),isIntentFromForYou);
                railCommonData.setStatus(true);
                try {
                    railCommonData.setTotalCount(response.body().getData().getTotalElements());
                    railCommonData.setPageTotal(response.body().getData().getTotalPages());
                } catch (Exception e) {
                    Log.e(API_SERVICE_LAYER, "parseEpisodeResponseAsRailCommonData: ", e );
                }
                callBack.onSuccess(railCommonData);
            }

        } else {
            ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
            callBack.onError(errorModel);
        }

    }


    public void getAllEpisodesV2(int seriesId, int pageNumber, int size, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (endpoint!=null) {
                endpoint.getRelatedContentWithoutSNo(seriesId, pageNumber, size, languageCode).enqueue(new Callback<ListAllContent>() {
                    @Override
                    public void onResponse(@NonNull Call<ListAllContent> call, @NonNull Response<ListAllContent> response) {
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        parseEpisodeResponseAsRailCommonData(response,false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListAllContent> call, @NonNull Throwable t) {
                        ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                        callBack.onFailure(errorModel);
                    }
                });
            }


    }

    public void getIsLikeGOI(String token ,int pageNumber, int size, ApiLikeList listener) {
        ApiInterface enveuSubscriptionEndPoint = RequestConfig.getEnveuSubscriptionClient(token).create(ApiInterface.class);
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (enveuSubscriptionEndPoint!=null) {
            enveuSubscriptionEndPoint.getIsLikeGOI(pageNumber, size, languageCode).enqueue(new Callback<com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.likeList.Response>() {
                    @Override
                    public void onResponse(@NonNull Call<com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.likeList.Response> call, @NonNull Response<com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.likeList.Response> response) {
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        if (response.isSuccessful() && response.body()!=null && response.body().getData()!=null) {
                            listener.onSuccess(true,response.body());
                        } else {
                            listener.onSuccess(false,response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.likeList.Response> call, @NonNull Throwable t) {
                       listener.onFailure(false,"0","");
                    }
                });
            }

    }


    public void getForYouContent( int pageNumber, int size,String tag,String contentType,String videoType, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (endpoint!=null) {
            if (contentType.equalsIgnoreCase(AppConstants.VIDEO)) {
                endpoint.getForYouContent(languageCode,pageNumber,size,tag,contentType,videoType).enqueue(new Callback<ListAllContent>() {
                    @Override
                    public void onResponse(@NonNull Call<ListAllContent> call, @NonNull Response<ListAllContent> response) {
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        parseEpisodeResponseAsRailCommonData(response,true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListAllContent> call, @NonNull Throwable t) {
                        ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                        callBack.onFailure(errorModel);
                    }
                });
            } else  {
                endpoint.getForYouCustomContent(languageCode,pageNumber,size,tag,contentType,videoType).enqueue(new Callback<ListAllContent>() {
                    @Override
                    public void onResponse(@NonNull Call<ListAllContent> call, @NonNull Response<ListAllContent> response) {
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        parseEpisodeResponseAsRailCommonData(response,true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<ListAllContent> call, @NonNull Throwable t) {
                        ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                        callBack.onFailure(errorModel);
                    }
                });
            }
            }
    }


    public void getCustomDetail( int pageNumber, int size,String tag,String contentType, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (endpoint!=null) {
            endpoint.getCustomDetail(languageCode,pageNumber,size,tag,contentType).enqueue(new Callback<ListAllContent>() {
                @Override
                public void onResponse(@NonNull Call<ListAllContent> call, @NonNull Response<ListAllContent> response) {
                    parseEpisodeResponseAsRailCommonData(response,false);
                }

                @Override
                public void onFailure(@NonNull Call<ListAllContent> call, @NonNull Throwable t) {
                    ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                    callBack.onFailure(errorModel);
                }
            });
        }


    }

    private void parseResponseAsRailCommonData(Response<EnveuCommonResponse> response,boolean intentFromRelatedContent ) {
        if (response.body() != null && response.body().getData() != null) {
            if (response.body().getData().getPageNumber() == 0 && response.body().getData().getTotalElements() == 0) {
                ApiErrorModel errorModel = new ApiErrorModel(500, "");
                callBack.onError(errorModel);
            } else {
                RailCommonData railCommonData = new RailCommonData(response.body().getData(),intentFromRelatedContent);
                railCommonData.setStatus(true);
                try {
                    railCommonData.setTotalCount(response.body().getData().getTotalElements());
                    railCommonData.setPageTotal(response.body().getData().getTotalPages());
                } catch (Exception ignore) {

                }
                callBack.onSuccess(railCommonData);
            }

        } else {
            ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
            callBack.onError(errorModel);
        }

    }

    public void getSeriesData(String assetID,boolean isIntentFromExpedition, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (endpoint!=null) {
                endpoint.getEnvVideoDetails(assetID, languageCode).enqueue(new Callback<EnvVideoDetailsBean>() {
                    @Override
                    public void onResponse(@NonNull Call<EnvVideoDetailsBean> call, @NonNull Response<EnvVideoDetailsBean> response) {
                        if (response.isSuccessful()) {
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            Log.d("Javed", "onResponse: " +  json);
                            RailCommonData railCommonData = new RailCommonData();
                            AppCommonMethod.getEpisodeAssetDetail(railCommonData, response, isIntentFromExpedition);
                            callBack.onSuccess(railCommonData);

                        } else {
                            ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
                            callBack.onError(errorModel);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<EnvVideoDetailsBean> call, @NonNull Throwable t) {
                        ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                        callBack.onFailure(errorModel);
                    }
                });
            }


    }

    public MutableLiveData<RailCommonData> getSearchPopularPlayList(String playlistID, int pageNumber, int pageSize, BaseCategory screenWidget) {

        MutableLiveData<RailCommonData> railCommonDataMutableLiveData = new MutableLiveData<>();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        Gson gson = new Gson();
        BaseCategoryServices.Companion.getInstance().getEnvPlaylistDetailsById(playlistID, languageCode, pageNumber, pageSize, new EnvPlaylistResponse() {
            @Override
            public void success(boolean status, @NonNull Response<com.enveu.client.playlist.beanv1_0.EnvPlaylistContents> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getData() != null && response.body().getData().getItems().size()>0) {
                        if (response.body() != null && response.body().getData() != null) {
                            String json = gson.toJson(response.body());
                            Log.w("playlistCall-->>>>>",json);
                            EnveuCommonResponse er=gson.fromJson(json, EnveuCommonResponse.class);
                            RailCommonData railCommonData = new RailCommonData(er.getData(), screenWidget, true);
                            railCommonData.setStatus(true);
                            railCommonDataMutableLiveData.postValue(railCommonData);
                        } else {
                            RailCommonData railCommonData = new RailCommonData();
                            railCommonData.setStatus(false);
                            railCommonDataMutableLiveData.postValue(railCommonData);
                        }
                    }
                    else {
                        RailCommonData railCommonData = new RailCommonData();
                        railCommonData.setStatus(false);
                        railCommonDataMutableLiveData.postValue(railCommonData);
                    }
                } else {
                    RailCommonData railCommonData = new RailCommonData();
                    railCommonData.setStatus(false);
                    railCommonDataMutableLiveData.postValue(railCommonData);
                }
            }

            @Override
            public void failure(boolean status, int errorCode, @NonNull String message) {
                RailCommonData railCommonData = new RailCommonData();
                railCommonData.setStatus(false);
                railCommonDataMutableLiveData.postValue(railCommonData);
            }
        });

      /*  MutableLiveData<RailCommonData> railCommonDataMutableLiveData = new MutableLiveData<>();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (endpoint!=null) {

            boolean isKidsMode  = KsPreferenceKeys.getInstance().getKidsMode();
            if (isKidsMode){
                String parentalRating = AppCommonMethod.getParentalRating();
                endpoint.getPlaylistDetailsByIdWithPG(playlistID, languageCode, pageNumber, pageSize,parentalRating).enqueue(new Callback<EnveuCommonResponse>() {
                    @Override
                    public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                        if (response.body() != null && response.body().getData() != null) {
                            RailCommonData railCommonData = new RailCommonData(response.body().getData(), screenWidget, true);
                            railCommonData.setStatus(true);
                            railCommonDataMutableLiveData.postValue(railCommonData);
                        } else {
                            RailCommonData railCommonData = new RailCommonData();
                            railCommonData.setStatus(false);
                            railCommonDataMutableLiveData.postValue(railCommonData);
                        }
                    }

                    @Override
                    public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {
                        RailCommonData railCommonData = new RailCommonData();
                        railCommonData.setStatus(false);
                        railCommonDataMutableLiveData.postValue(railCommonData);
                    }
                });
            }
            else {
                endpoint.getPlaylistDetailsById(playlistID, languageCode, pageNumber, pageSize).enqueue(new Callback<EnveuCommonResponse>() {
                    @Override
                    public void onResponse(Call<EnveuCommonResponse> call, Response<EnveuCommonResponse> response) {
                        if (response.body() != null && response.body().getData() != null) {
                            RailCommonData railCommonData = new RailCommonData(response.body().getData(), screenWidget, true);
                            railCommonData.setStatus(true);
                            railCommonDataMutableLiveData.postValue(railCommonData);
                        } else {
                            RailCommonData railCommonData = new RailCommonData();
                            railCommonData.setStatus(false);
                            railCommonDataMutableLiveData.postValue(railCommonData);
                        }
                    }

                    @Override
                    public void onFailure(Call<EnveuCommonResponse> call, Throwable t) {
                        RailCommonData railCommonData = new RailCommonData();
                        railCommonData.setStatus(false);
                        railCommonDataMutableLiveData.postValue(railCommonData);
                    }
                });
            }
        }*/
        return railCommonDataMutableLiveData;

    }

    public void getContinueWatchingVideos(List<ContinueWatchingBookmark> continueWatchingBookmarkList, String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        Gson gson = new Gson();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (endpoint!=null) {

            boolean isKidsMode  = KsPreferenceKeys.getInstance().getKidsMode();
            if (isKidsMode) {
                String parentalRating = AppCommonMethod.getParentalRating();
                endpoint.getVideosPG(manualImageAssetId, languageCode,parentalRating).enqueue(new Callback<ContinueWatchingModel>() {
                    @Override
                    public void onResponse(Call<ContinueWatchingModel> call, Response<ContinueWatchingModel> response) {
                        if (response.isSuccessful()) {
                            ArrayList<DataItem> enveuVideoDetailsArrayList = new ArrayList<>();
                            ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) response.body().getData();

                            for (ContinueWatchingBookmark continueWatchingBookmark : continueWatchingBookmarkList) {
                                for (DataItem enveuVideoDetail : enveuVideoDetails) {

                                    if (continueWatchingBookmark.getAssetId().intValue() == enveuVideoDetail.getId()) {
                                        if (continueWatchingBookmark.getPosition() != null)
                                            enveuVideoDetail.setPosition(continueWatchingBookmark.getPosition());
                                        enveuVideoDetailsArrayList.add(enveuVideoDetail);
                                    }
                                }
                            }
                            commonApiCallBack.onSuccess(enveuVideoDetailsArrayList);
                        } else {
                            commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                        }
                    }

                    @Override
                    public void onFailure(Call<ContinueWatchingModel> call, Throwable t) {
                        commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                    }
                });
            }else {
                endpoint.getVideoIdData(manualImageAssetId, languageCode).enqueue(new Callback<VideoIdModel>() {
                    @Override
                    public void onResponse(@NonNull Call<VideoIdModel> call, @NonNull Response<VideoIdModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            ArrayList<DataItem> enveuVideoDetailsArrayList = new ArrayList<>();
                            String json = gson.toJson(response.body().getData());
                          //  Log.w(PLAYLIST, json);
                            List<VideoIdModel.Datum> list = response.body().getData();
                            String arrayData = gson.toJson(list);
                            ArrayList<DataItem> enveuVideoDetails = gson.fromJson(arrayData, new TypeToken<List<DataItem>>() {}.getType());


                            for (ContinueWatchingBookmark item : continueWatchingBookmarkList) {
                                for (DataItem enveuVideoDetail : enveuVideoDetails) {
                                    if (item.getAssetId().intValue() == enveuVideoDetail.getId()) {
                                        if (item.getPosition() != null){
                                            enveuVideoDetail.setPosition(item.getPosition());
                                        }
                                        enveuVideoDetailsArrayList.add(enveuVideoDetail);
                                    }
                                }
                            }
                            commonApiCallBack.onSuccess(enveuVideoDetailsArrayList);
                        } else {
                            commonApiCallBack.onFailure(new Throwable("DETAILS_NOT_FOUND"));

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<VideoIdModel> call, @NonNull Throwable t) {
                        commonApiCallBack.onFailure(new Throwable("DETAILS_NOT_FOUND"));

                    }
                });

//                endpoint.getVideoIdData(manualImageAssetId, languageCode).enqueue(new Callback<ContinueWatchingModel>() {
//                    @Override
//                    public void onResponse(Call<ContinueWatchingModel> call, Response<ContinueWatchingModel> response) {
//                        if (response.isSuccessful()) {
//                            ArrayList<DataItem> enveuVideoDetailsArrayList = new ArrayList<>();
//                            ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) response.body().getData();
//
//                            for (ContinueWatchingBookmark continueWatchingBookmark : continueWatchingBookmarkList) {
//                                for (DataItem enveuVideoDetail : enveuVideoDetails) {
//
//                                    if (continueWatchingBookmark.getAssetId().intValue() == enveuVideoDetail.getId()) {
//                                        if (continueWatchingBookmark.getPosition() != null)
//                                            enveuVideoDetail.setPosition(continueWatchingBookmark.getPosition());
//                                        enveuVideoDetailsArrayList.add(enveuVideoDetail);
//                                    }
//                                }
//                            }
//                            commonApiCallBack.onSuccess(enveuVideoDetailsArrayList);
//                        } else {
//                            commonApiCallBack.onFailure(new Throwable("Details Not Found"));
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ContinueWatchingModel> call, Throwable t) {
//                        commonApiCallBack.onFailure(new Throwable("Details Not Found"));
//
//                    }
//                });
            }
        }
    }

    public void getWatchListVideos(List<ItemsItem> continueWatchingBookmarkList, String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        Gson gson = new Gson();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (endpoint!=null) {

            boolean isKidsMode  = KsPreferenceKeys.getInstance().getKidsMode();
            if (isKidsMode) {
                String parentalRating = AppCommonMethod.getParentalRating();
                endpoint.getVideosPG(manualImageAssetId, languageCode,parentalRating).enqueue(new Callback<ContinueWatchingModel>() {
                    @Override
                    public void onResponse(Call<ContinueWatchingModel> call, Response<ContinueWatchingModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            Logger.e("WATCH RESPONSE", new Gson().toJson(response.isSuccessful()));
                            Gson gson = new Gson();
                            String json = gson.toJson(response.body());
                            Log.d("myList", "onResponse: " + json);
                            ArrayList<DataItem> enveuVideoDetailsArrayList = new ArrayList<>();
                            ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) response.body().getData();

                            for (ItemsItem item :
                                    continueWatchingBookmarkList) {
                                for (DataItem enveuVideoDetail :
                                        enveuVideoDetails) {
                                    if (item.getAssetId() == enveuVideoDetail.getId()) {
                                        enveuVideoDetailsArrayList.add(enveuVideoDetail);
                                    }
                                }
                            }
                            commonApiCallBack.onSuccess(enveuVideoDetailsArrayList);
                        } else {
                            commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                        }
                    }

                    @Override
                    public void onFailure(Call<ContinueWatchingModel> call, Throwable t) {
                        commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                    }
                });
            }else {
                endpoint.getVideoIdData(manualImageAssetId, languageCode).enqueue(new Callback<VideoIdModel>() {
                    @Override
                    public void onResponse(Call<VideoIdModel> call, Response<VideoIdModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            ArrayList<DataItem> enveuVideoDetailsArrayList = new ArrayList<>();

//                            ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) response.body().getData();

                            String json = gson.toJson(response.body().getData());
                            Log.w("playlistCall-->>>>>", json);
                            List<VideoIdModel.Datum> list = response.body().getData();
                            String arrayData = gson.toJson(list);
                            ArrayList<DataItem> enveuVideoDetails = gson.fromJson(arrayData, new TypeToken<List<DataItem>>() {}.getType());


                            for (ItemsItem item : continueWatchingBookmarkList) {
                                for (DataItem enveuVideoDetail : enveuVideoDetails) {
                                    if (item.getAssetId() == enveuVideoDetail.getId()) {
                                        enveuVideoDetailsArrayList.add(enveuVideoDetail);
                                    }
                                }
                            }
                            commonApiCallBack.onSuccess(enveuVideoDetailsArrayList);
                        } else {
                            commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                        }
                    }

                    @Override
                    public void onFailure(Call<VideoIdModel> call, Throwable t) {
                        commonApiCallBack.onFailure(new Throwable("Details Not Found"));

                    }
                });
            }
        }

    }



    private List<RailCommonData> mModel;

    public LiveData<List<RailCommonData>> getSearchData(String keyword, int size, int page, boolean applyFilter, SearchRequestModel requestModel) {
        languageCode = LanguageLayer.getCurrentLanguageCode();
        List<String> filterGenreSavedListKeyForApi = KsPreferenceKeys.getInstance().getDataGenreListKeyValue();
        List<String> filterFeatureSavedListKeyForApi = KsPreferenceKeys.getInstance().getDataFeatureListKeyValue();
        List<String> filterSortSavedListKeyForApi = KsPreferenceKeys.getInstance().getDataSortListKeyValue();

        List<String> allFilters = new ArrayList<>();
        if (ObjectHelper.isNotEmpty(filterGenreSavedListKeyForApi))
            allFilters.addAll(filterGenreSavedListKeyForApi);
        if (ObjectHelper.isNotEmpty(filterFeatureSavedListKeyForApi))
            allFilters.addAll(filterFeatureSavedListKeyForApi);

        MutableLiveData<List<RailCommonData>> responsePopular = new MutableLiveData<>();
        try {


            ApiInterface endpoint = RequestConfig.getClientSearch().create(ApiInterface.class);
            String videoContentTypes = AppConstants.VIDEO;
            String StringMediaTypes = MediaTypeConstants.getInstance().getMovie() + "," + MediaTypeConstants.getInstance().getEpisode()  + "," + MediaTypeConstants.getInstance().getDocumentaries();

            Observable<ResponseSearch> allResults = null;
            Observable<ResponseSearch> episode = null;
            Observable<ResponseSearch> documentaries = null;


            allResults = endpoint.getSearchByFilters(keyword,
                            videoContentTypes, size, page, languageCode,StringMediaTypes, allFilters,
                            filterSortSavedListKeyForApi)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io());

            episode = endpoint.getSearchByFilters(keyword,
                            "Custom", size, page, languageCode,"", allFilters,
                            filterSortSavedListKeyForApi)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io());

          /*  documentaries = endpoint.getSearchByFilters(keyword,
                            videoContentTypes, size, page, languageCode,MediaTypeConstants.getInstance().getDocumentaries(), allFilters,
                            filterSortSavedListKeyForApi)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io());*/

            Observable<List<ResponseSearch>> combined = Observable.zip(allResults,episode, (allResult,episodes) -> {
                List<ResponseSearch> combinedList = new ArrayList<>();
                combinedList.add(allResult);
                combinedList.add(episodes);
                return combinedList;
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            combined.subscribe(new Observer<List<ResponseSearch>>() {

                @Override
                public void onSubscribe(Disposable d) {
                    Logger.d("on subscribe");
                }

                @Override
                public void onNext(@NonNull List<ResponseSearch> responseSearchList) {
                    Logger.d("response search: " + responseSearchList);
                    mModel = new ArrayList<>();
                    try {
                        final int dataSize = ObjectHelper.getSize(responseSearchList);
                        for (int i = 0; i < dataSize; i++) {
                            RailCommonData railCommonData = new RailCommonData();
                            final Data data = responseSearchList.get(i).getData();
                            if (data != null && data.getItems() != null) {
                                railCommonData.setStatus(true);
                                List<com.tv.uscreen.yojmatv.beanModelV3.searchV2.ItemsItem> searchItems = data.getItems();
                                List<EnveuVideoItemBean> enveuVideoItemBeans = new ArrayList<>();
                                for (com.tv.uscreen.yojmatv.beanModelV3.searchV2.ItemsItem videoItem : searchItems) {
                                    EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(videoItem);
                                    Gson gson = new Gson();
                                    String tmp = gson.toJson(enveuVideoItemBean);
                                    enveuVideoItemBeans.add(enveuVideoItemBean);
                                }
                                railCommonData.setEnveuVideoItemBeans(enveuVideoItemBeans);
                                railCommonData.setPageTotal(data.getPageInfo().getTotal());
                                railCommonData.setStatus(true);
                            } else {
                                railCommonData.setStatus(false);
                            }
                            mModel.add(railCommonData);
                        }
                    } catch (Exception e) {
                        RailCommonData railCommonData = new RailCommonData();
                        railCommonData.setStatus(false);
                        mModel.add(railCommonData);
                    }
                    responsePopular.postValue(mModel);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Logger.w(e);
                    responsePopular.postValue(new ArrayList<>());
                }

                @Override
                public void onComplete() {
                    Logger.d("completed");
                }
            });
        } catch (Exception e) {
            mModel = new ArrayList<>();
            RailCommonData railCommonData = new RailCommonData();
            railCommonData.setStatus(false);
            mModel.add(railCommonData);
            responsePopular.postValue(mModel);
        }
        return responsePopular;
    }

    public LiveData<RailCommonData> getSingleCategorySearch(String keyword, String type, int size, int page,boolean applyFilter,String customContentType,String videoType,String header) {
        MutableLiveData<RailCommonData> responsePopular;
        Call<ResponseSearch> call = null;

        responsePopular = new MutableLiveData<>();
        String StringMediaTypes = MediaTypeConstants.getInstance().getMovie() + "," + MediaTypeConstants.getInstance().getEpisode()  + "," + MediaTypeConstants.getInstance().getDocumentaries();

        {

            try {
                ApiInterface backendApi = RequestConfig.getClientSearch().create(ApiInterface.class);
                if (type.equalsIgnoreCase(AppConstants.VIDEO)) {
                    if (header.equalsIgnoreCase(AppConstants.SEARCH_RESULT) || header.equalsIgnoreCase(AppConstants.SPANISH_SEARCH_RESULT) ) {
                        call = backendApi.getVideoSearchResults(keyword, type, size, page, languageCode,StringMediaTypes);
                    } /*else if (header.equalsIgnoreCase(AppConstants.episodes)){
                        call = backendApi.getVideoSearchResults(keyword, type, size, page, languageCode,MediaTypeConstants.getInstance().getEpisode());
                    }  else if (header.equalsIgnoreCase(AppConstants.Documentaries)){
                        call = backendApi.getVideoSearchResults(keyword, type, size, page, languageCode,MediaTypeConstants.getInstance().getDocumentaries());
                    }*/
                }


                if (call != null) {
                    call.enqueue(new Callback<ResponseSearch>() {
                        @Override
                        public void onResponse(@NonNull Call<ResponseSearch> call, @NonNull Response<ResponseSearch> data) {
                            if (data.isSuccessful()){
                                if (data.code() == 200) {
                                    RailCommonData railCommonData = null;
                                    railCommonData = new RailCommonData();
                                    if (data.body().getData() != null && data.body().getData().getItems() != null) {
                                        railCommonData.setStatus(true);
                                        List<com.tv.uscreen.yojmatv.beanModelV3.searchV2.ItemsItem> itemsItem = data.body().getData().getItems();
                                        enveuVideoItemBeans = new ArrayList<>();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(itemsItem);
                                        for (com.tv.uscreen.yojmatv.beanModelV3.searchV2.ItemsItem videoItem : itemsItem) {
                                            EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(videoItem);
                                            if (type.equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries()) && videoItem.getSeasons() != null)
                                                enveuVideoItemBean.setSeasonCount(videoItem.getSeasons().size());

                                            enveuVideoItemBeans.add(enveuVideoItemBean);
                                        }

                                        railCommonData.setEnveuVideoItemBeans(enveuVideoItemBeans);
                                        railCommonData.setPageTotal(data.body().getData().getPageInfo().getTotal());
                                        railCommonData.setStatus(true);
                                        responsePopular.postValue(railCommonData);
                                    } else {
                                        railCommonData.setStatus(false);
                                        responsePopular.postValue(railCommonData);
                                    }

                                } else {
                                    responsePopular.postValue(new RailCommonData());
                                }

                            }

                        }

                        @Override
                        public void onFailure(@NonNull Call<ResponseSearch> call, @NonNull Throwable t) {
                            responsePopular.postValue(new RailCommonData());
                        }
                    });
                }

            } catch (Exception e) {
                responsePopular.postValue(new RailCommonData());
            }


        }
        return responsePopular;
    }

    public LiveData<RailCommonData> getProgramSearch(String keyword, int size, int page,boolean applyFilter) {
        MutableLiveData<RailCommonData> responsePopular;
        Call<ResponseSearch> call = null;
        responsePopular = new MutableLiveData<>();

        languageCode = LanguageLayer.getCurrentLanguageCode();
        try {
            ApiInterface backendApi = RequestConfig.getClientSearch().create(ApiInterface.class);

            Logger.d( "SearchValues-->>" + keyword + " " + size + " " + page);

            List<String> contentTypes = Arrays.asList(MediaTypeConstants.getInstance().getMovie(),
                    MediaTypeConstants.getInstance().getSeries(),
                    MediaTypeConstants.getInstance().getLive(),
                    MediaTypeConstants.getInstance().getShow());

            if (applyFilter) {
                List<String> filterGenreSavedListKeyForApi = KsPreferenceKeys.getInstance().getDataGenreListKeyValue();
                List<String> filterSortSavedListKeyForApi = KsPreferenceKeys.getInstance().getDataSortListKeyValue();
                if (filterGenreSavedListKeyForApi != null && filterGenreSavedListKeyForApi.size() > 0 || filterSortSavedListKeyForApi != null && filterSortSavedListKeyForApi.size() > 0) {
                    call = backendApi.getSearchResultsByFilters(keyword, contentTypes, size, page, languageCode, filterGenreSavedListKeyForApi, filterSortSavedListKeyForApi);
                }
            } else {
                call = backendApi.getSearchResults(keyword, contentTypes, size, page, languageCode);
            }

            if (call != null) call.enqueue(new Callback<ResponseSearch>() {
                @Override
                public void onResponse(@NonNull Call<ResponseSearch> call, @NonNull Response<ResponseSearch> response) {
                    if (response.code() == 200) {
                        RailCommonData railCommonData = null;
                        final ResponseSearch body = response.body();
                        if (body != null) {
                            railCommonData = new RailCommonData();
                            final Data data = body.getData();
                            if (data != null && data.getItems() != null) {
                                railCommonData.setStatus(true);
                                List<com.tv.uscreen.yojmatv.beanModelV3.searchV2.ItemsItem> itemsItem =
                                        data.getItems();
                                enveuVideoItemBeans = new ArrayList<>();
                                for (com.tv.uscreen.yojmatv.beanModelV3.searchV2.ItemsItem videoItem : itemsItem) {
                                    EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(videoItem);
                                    enveuVideoItemBean.setPosterURL(ImageLayer.getInstance().getPosterImageUrl(videoItem));
                                    if (videoItem.getSeasons() != null)
                                        enveuVideoItemBean.setSeasonCount(
                                                videoItem.getSeasons().size());

                                    enveuVideoItemBeans.add(enveuVideoItemBean);
                                }

                                railCommonData.setEnveuVideoItemBeans(enveuVideoItemBeans);
                                railCommonData.setPageTotal(data.getPageInfo().getTotal());
                                railCommonData.setStatus(true);
                            } else {
                                railCommonData.setStatus(false);
                            }
                        }
                        responsePopular.postValue(railCommonData);
                    } else {
                        responsePopular.postValue(new RailCommonData());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseSearch> call, @NonNull Throwable t) {
                    responsePopular.postValue(new RailCommonData());
                }
            });
            else Logger.e("Call not sent");

        } catch (Exception e) {
            responsePopular.postValue(new RailCommonData());
        }

        return responsePopular;
    }

    public void getRequestedOfferForUser(int page, int size, String token, int userId, String offers, String currentLanguageCode, RequestOfferCallBack requestOfferCallBack) {
        ApiInterface enveuSubscriptionEndPoint = RequestConfig.getRequestOfferClient(token).create(ApiInterface.class);
        if (enveuSubscriptionEndPoint != null) {
            enveuSubscriptionEndPoint.getRequestOfferForUser(userId,offers,page,size,currentLanguageCode).enqueue(new Callback<com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.RequestOfferList.Response>() {
                @Override
                public void onResponse(Call<com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.RequestOfferList.Response> call, Response<com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.RequestOfferList.Response> response) {
                    if (response.isSuccessful() && response.body()!=null && response.body().getData()!=null) {
                        requestOfferCallBack.success(true,response.body());
                    } else {
                        requestOfferCallBack.success(false,response.body());
                    }
                }

                @Override
                public void onFailure(Call<com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.RequestOfferList.Response> call, Throwable t) {
                    requestOfferCallBack.failure(false,"0","");
                }
            });
        }
    }


    public void getRelatedContent( int pageNumber, int size,String contentType,int id, ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        languageCode = LanguageLayer.getCurrentLanguageCode();
        if (endpoint!=null) {
                endpoint.getRelatedContentForVideo(id,languageCode,pageNumber,size,contentType).enqueue(new Callback<EnveuCommonResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<EnveuCommonResponse> call, @NonNull Response<EnveuCommonResponse> response) {
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        parseResponseAsRailCommonData(response,true);
                    }

                    @Override
                    public void onFailure(@NonNull Call<EnveuCommonResponse> call, @NonNull Throwable t) {
                        ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                        callBack.onFailure(errorModel);
                    }
                });
        }
    }

}
