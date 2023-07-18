package com.tv.utils.helpers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.enveu.client.bookmarking.bean.continuewatching.ContinueWatchingBookmark;
import com.enveu.client.enums.Layouts;
import com.enveu.client.enums.PlaylistType;
import com.enveu.client.watchHistory.beans.ItemsItem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tv.Bookmarking.BookmarkingViewModel;
import com.tv.ObservableRxList;
import com.tv.activities.homeactivity.ui.HomeActivity;
import com.tv.beanModel.emptyResponse.ResponseEmpty;
import com.tv.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.beanModel.responseGetWatchlist.ResponseGetIsWatchlist;
import com.tv.beanModel.responseIsLike.ResponseIsLike;
import com.tv.beanModel.search.SearchRequestModel;
import com.tv.beanModelV3.continueWatching.DataItem;
import com.tv.bean_model_v1_0.listAll.RequestOfferList.Response;
import com.tv.callbacks.RequestOffferCallBack.RequestOfferCallBack;
import com.tv.callbacks.apicallback.ApiResponseModel;
import com.tv.callbacks.commonCallbacks.CommonApiCallBack;
import com.tv.callbacks.likelistCallback.ApiLikeList;
import com.tv.layersV2.ContinueWatchingLayer;
import com.tv.layersV2.ListPaginationDataLayer;
import com.tv.layersV2.SearchLayer;
import com.tv.layersV2.SeasonEpisodesList;
import com.tv.layersV2.SeriesDataLayer;
import com.tv.layersV2.VideoDetailLayer;
import com.tv.networking.apistatus.APIStatus;
import com.tv.networking.errormodel.ApiErrorModel;
import com.tv.networking.responsehandler.ResponseModel;
import com.tv.networking.servicelayer.APIServiceLayer;
import com.tv.repository.home.HomeFragmentRepository;
import com.tv.repository.userManagement.RegistrationLoginRepository;
import com.tv.utils.ObjectHelper;
import com.tv.utils.constants.AppConstants;
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.ArrayList;
import java.util.List;


public class RailInjectionHelper extends AndroidViewModel {
    private final MutableLiveData<RailCommonData> mutableRailCommonData = new MutableLiveData<>();
    private final ObservableRxList<RailCommonData> observableList = new ObservableRxList<>();
    private int i = 0;
    private Boolean finalTag;
    private List<BaseCategory> baseCategories;
    private KsPreferenceKeys preference;
    private String isLogin;
    private boolean entittleStatus = false;

    public RailInjectionHelper(@NonNull Application application) {
        super(application);
    }

    public void getScreenWidgets(Activity activity, String screenId, Boolean tag, CommonApiCallBack commonApiCallBack) {
        finalTag = tag;
        if(screenId!=null){
            APIServiceLayer.getInstance().getCategories(screenId).observe((LifecycleOwner) activity, baseCategoriesList -> {
                baseCategories = baseCategoriesList;
                Gson gson = new Gson();
                String json = gson.toJson(baseCategories);
                Log.w("screenData-->>", json);
                i = 0;
                if (ObjectHelper.isNotEmpty(baseCategories)) {
                    getScreenListing(activity, commonApiCallBack);
                } else {
                    commonApiCallBack.onFailure(new Throwable("No Data"));
                }
            });
        }
        }


    private void getScreenListing(Activity activity, CommonApiCallBack commonApiCallBack) {
        if (i < baseCategories.size()) {
            BaseCategory screenWidget = baseCategories.get(i);
            String type = "";
            if (screenWidget.getType() != null)
                type = screenWidget.getType();
            if (type.equalsIgnoreCase(AppConstants.WIDGET_TYPE_CONTENT)) {

                if (activity instanceof HomeActivity) {

                    isLogin = KsPreferenceKeys.getInstance().getAppPrefLoginStatus();
                    entittleStatus = KsPreferenceKeys.getInstance().getEntitlementStatus();
                    if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
                        if (entittleStatus && baseCategories.get(i).getShowRail().equalsIgnoreCase(AppConstants.FREE_RAIL)) {
                        i++;
                        getScreenListing(activity, commonApiCallBack);
                        } else {
                            if (finalTag) {
                                if (i == 0) {
                                    getRailDetails(activity, screenWidget, commonApiCallBack);
                                }
                            } else {
                                getRailDetails(activity, screenWidget, commonApiCallBack);
                            }
                        }
                    } else {
                        if (finalTag) {
                            if (i == 0) {
                                getRailDetails(activity, screenWidget, commonApiCallBack);
                            }
                        } else {
                            getRailDetails(activity, screenWidget, commonApiCallBack);
                        }
                    }
                }
            } else if (type.equalsIgnoreCase(AppConstants.WIDGET_TYPE_AD)) {
                getAdsDetails(activity, screenWidget, commonApiCallBack);
            }
        } else {
            commonApiCallBack.onFinish();
        }
    }


    private void getAdsDetails(Activity activity, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        if (!KsPreferenceKeys.getInstance().getEntitlementStatus()) {
            RailCommonData railCommonData = new RailCommonData(screenWidget);
            railCommonData.setIsAd(true);
            commonApiCallBack.onSuccess(railCommonData);
            i++;
            getScreenListing(activity, commonApiCallBack);
        } else {
            i++;
            getScreenListing(activity, commonApiCallBack);
        }

    }

    private void getRailDetails(Activity activity, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        if (screenWidget.getLayout() != null && screenWidget.getLayout().equalsIgnoreCase(Layouts.HRO.name())) {
            getHeroDetails(activity, screenWidget, commonApiCallBack);
        } else {
            if (screenWidget.getContentPlayListType() != null && (screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.BVC.name()) || screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.EN_OVP.name()))) {
                getPlaylistDetailsById(activity, screenWidget, commonApiCallBack);
            } else if (screenWidget.getContentPlayListType() != null && screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.K_PDF.name())) {
                // Get Playlist data from Predefined Kaltura
            } else if (screenWidget.getContentPlayListType() != null && screenWidget.getContentPlayListType().equalsIgnoreCase(PlaylistType.KTM.name())) {
                // Get Playlist data from Kaltura
            }
        }
    }

    private void getHeroDetails(Activity activity, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        RailCommonData railCommonData = new RailCommonData();
        railCommonData.getHeroRailCommonData(screenWidget, activity, new CommonApiCallBack() {
            @Override
            public void onSuccess(Object item) {
                commonApiCallBack.onSuccess(item);
                i++;
                getScreenListing(activity, commonApiCallBack);
            }

            @Override
            public void onFailure(Throwable throwable) {
                i++;
                getScreenListing(activity, commonApiCallBack);
            }

            @Override
            public void onFinish() {

            }
        });

    }


    private void getPlaylistDetailsById(Activity activity, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        int contentSize = 0;
        if (screenWidget.getContentSize() != null) {
            contentSize = (int) screenWidget.getContentSize();
        }
        if (screenWidget.getName() != null && screenWidget.getReferenceName() != null && (screenWidget.getReferenceName().equalsIgnoreCase("special_playlist") || screenWidget.getReferenceName().equalsIgnoreCase(AppConstants.ContentType.CONTINUE_WATCHING.name()))) {
            injectContinueWatchingRail(activity, contentSize, screenWidget, commonApiCallBack);
        } else if (screenWidget.getName() != null && screenWidget.getReferenceName() != null && screenWidget.getReferenceName().equalsIgnoreCase(AppConstants.ContentType.MY_WATCHLIST.name())) {
            injectWatchlistRail(activity, contentSize, screenWidget, commonApiCallBack);
        } else {
            APIServiceLayer.getInstance().getPlayListById(screenWidget.getContentID(), 0, contentSize).observe((LifecycleOwner) activity, enveuCommonResponse -> {
                if (enveuCommonResponse != null && enveuCommonResponse.getData() != null) {
                    RailCommonData railCommonData = new RailCommonData(enveuCommonResponse.getData(), screenWidget, false);
                    commonApiCallBack.onSuccess(railCommonData);
                    i++;
                    getScreenListing(activity, commonApiCallBack);
                } else {
                    i++;
                    getScreenListing(activity, commonApiCallBack);
                }
            });
        }
    }

    private void injectWatchlistRail(Activity activity, int contentSize, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        if (preference == null)
            preference = KsPreferenceKeys.getInstance();
        String isLogin = preference.getAppPrefLoginStatus();
        if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
            String token = preference.getAppPrefAccessToken();
            BookmarkingViewModel bookmarkingViewModel = new ViewModelProvider((FragmentActivity) activity).get(BookmarkingViewModel.class);
            bookmarkingViewModel.getMywatchListData(token, 0, contentSize).observe((LifecycleOwner) activity, getContinueWatchingBean -> {
                String videoIds = "";
                if (getContinueWatchingBean != null && getContinueWatchingBean.getData() != null) {
                    List<ItemsItem> continueWatchingBookmarkList = getContinueWatchingBean.getData().getItems();
                    for (ItemsItem continueWatchingBookmark : continueWatchingBookmarkList
                    ) {
                        videoIds = videoIds.concat(String.valueOf(continueWatchingBookmark.getAssetId())).concat(",");
                    }
                    ContinueWatchingLayer.getInstance().getWatchHistoryVideos(continueWatchingBookmarkList, videoIds, new CommonApiCallBack() {
                        @Override
                        public void onSuccess(Object item) {
                            if (item instanceof List) {
                                ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) item;
                                RailCommonData railCommonData = new RailCommonData();
                                railCommonData.setContinueWatchingData(screenWidget, false, enveuVideoDetails, new CommonApiCallBack() {
                                    @Override
                                    public void onSuccess(Object item) {
                                        commonApiCallBack.onSuccess(railCommonData);
                                        i++;
                                        getScreenListing(activity, commonApiCallBack);
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        i++;
                                        getScreenListing(activity, commonApiCallBack);
                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            commonApiCallBack.onFailure(new Throwable("ASSET NOT FOUND"));
                            i++;
                            getScreenListing(activity, commonApiCallBack);
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
                } else {
                    i++;
                    getScreenListing(activity, commonApiCallBack);
                }

            });
        } else {
            i++;
            getScreenListing(activity, commonApiCallBack);
        }
    }


    private void injectContinueWatchingRail(Activity activity, int contentSize, BaseCategory screenWidget, CommonApiCallBack commonApiCallBack) {
        if (preference == null)
            preference = KsPreferenceKeys.getInstance();
        String isLogin = preference.getAppPrefLoginStatus();
        if (isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
            String token = preference.getAppPrefAccessToken();
            BookmarkingViewModel bookmarkingViewModel = new ViewModelProvider((FragmentActivity) activity).get(BookmarkingViewModel.class);
            bookmarkingViewModel.getContinueWatchingData(token, 0, contentSize).observe((LifecycleOwner) activity, getContinueWatchingBean -> {
                String videoIds = "";
                if (getContinueWatchingBean != null && getContinueWatchingBean.getData() != null) {
                    List<ContinueWatchingBookmark> continueWatchingBookmarkLists = getContinueWatchingBean.getData().getContinueWatchingBookmarks();
                    List<ContinueWatchingBookmark> continueWatchingBookmarkList = removeDuplicates(continueWatchingBookmarkLists);
                    for (ContinueWatchingBookmark continueWatchingBookmark : continueWatchingBookmarkList) {
                        videoIds = videoIds.concat(String.valueOf(continueWatchingBookmark.getAssetId())).concat(",");
                    }
                    ContinueWatchingLayer.getInstance().getContinueWatchingVideos(continueWatchingBookmarkList, videoIds, new CommonApiCallBack() {
                        @Override
                        public void onSuccess(Object item) {
                            if (item instanceof List) {
                                ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) item;
                                RailCommonData railCommonData = new RailCommonData();
                                railCommonData.setContinueWatchingData(screenWidget, true, enveuVideoDetails, new CommonApiCallBack() {
                                    @Override
                                    public void onSuccess(Object item) {
                                        commonApiCallBack.onSuccess(railCommonData);
                                        i++;
                                        getScreenListing(activity, commonApiCallBack);
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        i++;
                                        getScreenListing(activity, commonApiCallBack);
                                    }

                                    @Override
                                    public void onFinish() {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            commonApiCallBack.onFailure(new Throwable("ASSET NOT FOUND"));
                            i++;
                            getScreenListing(activity, commonApiCallBack);
                        }

                        @Override
                        public void onFinish() {

                        }
                    });
                } else {
                    i++;
                    getScreenListing(activity, commonApiCallBack);
                }

            });
        } else {
            i++;
            getScreenListing(activity, commonApiCallBack);
        }
    }

    private List<ContinueWatchingBookmark> removeDuplicates(List<ContinueWatchingBookmark> continueWatchingBookmarkList) {
        List<ContinueWatchingBookmark> noRepeat = new ArrayList<>();
        try {
            for (ContinueWatchingBookmark event : continueWatchingBookmarkList) {
                boolean isFound = false;
                // check if the event name exists in noRepeat
                for (ContinueWatchingBookmark e : noRepeat) {
                    if (e.getAssetId().equals(event.getAssetId()) || (e.equals(event))) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) noRepeat.add(event);
            }
        } catch (Exception ignored) {

        }


        return noRepeat;
    }


    public MutableLiveData<RailCommonData> getPlayListDetailsWithPagination(String playlistID, int pageNumber, int pageSize, BaseCategory screenWidget) {
        return APIServiceLayer.getInstance().getSearchPopularPlayList(playlistID, pageNumber, pageSize, screenWidget);
    }

    public LiveData<List<RailCommonData>> getSearch(String keyword, int size, int page, boolean applyFilter, SearchRequestModel requestModel) {
        return SearchLayer.getInstance().getSearchData(keyword, size, page, applyFilter, requestModel);
    }

    public LiveData<RailCommonData> getSearchSingleCategory(String keyword, String type, int size, int page, boolean applyFilter,String customContentType,String videoType,String header) {
        return SearchLayer.getInstance().getSingleCategorySearch(keyword, type, size, page, applyFilter,customContentType,videoType,header);
    }

    public LiveData<RailCommonData> getSearchProgram(String keyword, int size, int page, boolean applyFilter) {
        return SearchLayer.getInstance().getProgramSearch(keyword, size, page, applyFilter);
    }

    public LiveData<ResponseGetIsWatchlist> hitApiIsWatchList(String token, int seriesId) {
        return HomeFragmentRepository.getInstance().hitApiIsToWatchList(token, seriesId);
    }

    public LiveData<ResponseEmpty> hitApiAddWatchList(String token, int seriesId) {
        return HomeFragmentRepository.getInstance().hitApiAddToWatchList(token, seriesId);
    }

    public LiveData<ResponseEmpty> hitApiAddWatchHistory(String token, int assetId) {
        return HomeFragmentRepository.getInstance().hitApiAddToWatchHistory(token, assetId);
    }


    public LiveData<ResponseIsLike> hitApiIsLike(String token, int assetId) {
        return HomeFragmentRepository.getInstance().hitApiIsLike(token, assetId);
    }

    public LiveData<ResponseEmpty> hitApiDeleteLike(String token, int assetId) {
        return HomeFragmentRepository.getInstance().hitApiDeleteLike(token, assetId);
    }

    public LiveData<ResponseEmpty> hitRemoveWatchlist(String token, int assetId) {
        return HomeFragmentRepository.getInstance().hitRemoveWatchlist(token, assetId);
    }

    public LiveData<ResponseEmpty> hitApiAddLike(String token, int assetId) {
        return HomeFragmentRepository.getInstance().hitApiAddLike(token, assetId);
    }


    public LiveData<RailCommonData> getWatchHistoryAssets(List<ItemsItem> watchHistoryList, String videoIds) {
        MutableLiveData<RailCommonData> railCommonDataMutableLiveData = new MutableLiveData<>();

        ContinueWatchingLayer.getInstance().getWatchHistoryVideos(watchHistoryList, videoIds, new CommonApiCallBack() {
            @Override
            public void onSuccess(Object item) {
                if (item instanceof List) {
                    ArrayList<DataItem> enveuVideoDetails = (ArrayList<DataItem>) item;
                    RailCommonData railCommonData = new RailCommonData();
                    railCommonData.setWatchHistoryData(enveuVideoDetails, new CommonApiCallBack() {
                        @Override
                        public void onSuccess(Object item) {
                            railCommonDataMutableLiveData.postValue(railCommonData);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            railCommonDataMutableLiveData.postValue(null);
                        }

                        @Override
                        public void onFinish() {
                        }
                    });
                } else {
                    railCommonDataMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                railCommonDataMutableLiveData.postValue(null);
            }

            @Override
            public void onFinish() {
            }
        });
        return railCommonDataMutableLiveData;
    }

    public LiveData<JsonObject> hitLogout(boolean session, String token) {
        return RegistrationLoginRepository.getInstance().hitApiLogout(session, token);
    }


    public LiveData<ResponseModel> getCatData(Activity activity, String screenId) {
        MutableLiveData<ResponseModel> liveData = new MutableLiveData<>();
        HomeFragmentRepository homeFragmentRepository = new HomeFragmentRepository();

        homeFragmentRepository.getCat(screenId, new ApiResponseModel<BaseCategory>() {
            @Override
            public void onStart() {
                liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
            }

            @Override
            public void onSuccess(List<BaseCategory> t) {
                liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), t, null));
            }

            @Override
            public void onError(ApiErrorModel apiError) {
                liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
            }
        });
        return liveData;
    }

    public MutableLiveData<ResponseModel> getPlayListDetailsWithPaginationV2(String playlistID,
                                                                             int pageNumber, int pageSize,
                                                                             BaseCategory screenWidget) {
        MutableLiveData liveData = new MutableLiveData();
        ListPaginationDataLayer.getInstance().getPlayListByWithPagination(playlistID, pageNumber, pageSize, screenWidget, new ApiResponseModel<RailCommonData>() {
            @Override
            public void onStart() {
                liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
            }

            @Override
            public void onSuccess(RailCommonData response) {
                liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
            }

            @Override
            public void onError(ApiErrorModel apiError) {
                liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
            }
        });

        return liveData;
    }

    public LiveData<ResponseModel> getSeriesDetailsV2(String asseetID ,boolean isIntentFromExpedition) {
        MutableLiveData liveData = new MutableLiveData();
        SeriesDataLayer.getInstance().getSeriesData(asseetID,isIntentFromExpedition, new ApiResponseModel<RailCommonData>() {
            @Override
            public void onStart() {
                liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
            }

            @Override
            public void onSuccess(RailCommonData response) {
                liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
            }

            @Override
            public void onError(ApiErrorModel apiError) {
                liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
            }
        });

        return liveData;
    }

    public LiveData<com.tv.bean_model_v1_0.listAll.likeList.Response> getIsLikeGOI(String token, int pageNo, int size) {
        MutableLiveData<com.tv.bean_model_v1_0.listAll.likeList.Response> liveData = new MutableLiveData();
         SeasonEpisodesList.getInstance().getIsLikeGOI(token, pageNo, size, new ApiLikeList() {
             @Override
             public void onSuccess(boolean isStatus, com.tv.bean_model_v1_0.listAll.likeList.Response likeListResponse) {
                 liveData.postValue(likeListResponse);
             }

             @Override
             public void onFailure(boolean isStatus, String errorCode, String errorMessage) {
                 liveData.postValue(null);
             }
         });
        return liveData;
    }


    public LiveData<ResponseModel> getEpisodeNoSeasonV2(int seriesId, int pageNo, int size, int seasonNumber) {
        MutableLiveData liveData = new MutableLiveData();
        if (seasonNumber == -1) {
            SeasonEpisodesList.getInstance().getAllEpisodesV2(seriesId, pageNo, size, new ApiResponseModel<RailCommonData>() {
                @Override
                public void onStart() {
                    liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
                }

                @Override
                public void onSuccess(RailCommonData response) {
                    liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
                }

                @Override
                public void onError(ApiErrorModel apiError) {
                    liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
                }

                @Override
                public void onFailure(ApiErrorModel httpError) {
                    liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
                }
            });


        } else {
            SeasonEpisodesList.getInstance().getSeasonEpisodesV2(seriesId, pageNo, size, seasonNumber, new ApiResponseModel<RailCommonData>() {
                @Override
                public void onStart() {
                    liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
                }

                @Override
                public void onSuccess(RailCommonData response) {
                    liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
                }

                @Override
                public void onError(ApiErrorModel apiError) {
                    liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
                }

                @Override
                public void onFailure(ApiErrorModel httpError) {
                    liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
                }
            });

        }
        return liveData;
    }

    public LiveData<ResponseModel> getAssetDetailsV2(String asseetID, Context context) {
        MutableLiveData liveData = new MutableLiveData();
        VideoDetailLayer.getInstance().getVideoDetails(asseetID, context, new ApiResponseModel<RailCommonData>() {
            @Override
            public void onStart() {
                liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
            }

            @Override
            public void onSuccess(RailCommonData response) {
                liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
            }

            @Override
            public void onError(ApiErrorModel apiError) {
                liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
            }
        });

        return liveData;

    }

    public LiveData<ResponseModel> getForYouContent(int pageNumber, int size,String tag,String contentType,String videoType) {
        MutableLiveData liveData = new MutableLiveData();
            SeasonEpisodesList.getInstance().getForYouContent(pageNumber,size,tag,contentType,videoType, new ApiResponseModel<RailCommonData>() {
                @Override
                public void onStart() {
                    liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
                }

                @Override
                public void onSuccess(RailCommonData response) {
                    liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
                }

                @Override
                public void onError(ApiErrorModel apiError) {
                    liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
                }

                @Override
                public void onFailure(ApiErrorModel httpError) {
                    liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
                }
            });

        return liveData;
    }


    public LiveData<ResponseModel> getRelatedContent(int pageNumber, int size,String contentType,int id) {
        MutableLiveData liveData = new MutableLiveData();
        SeasonEpisodesList.getInstance().getRelatedContent(pageNumber,size,contentType,id, new ApiResponseModel<RailCommonData>() {
            @Override
            public void onStart() {
                liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
            }

            @Override
            public void onSuccess(RailCommonData response) {
                liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
            }

            @Override
            public void onError(ApiErrorModel apiError) {
                liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
            }
        });

        return liveData;
    }



    public LiveData<ResponseModel> getCustomDetail(int pageNumber, int size,String tag,String contentType) {
        MutableLiveData liveData = new MutableLiveData();
        SeasonEpisodesList.getInstance().getCustomDetail(pageNumber,size,tag,contentType,new ApiResponseModel<RailCommonData>() {
            @Override
            public void onStart() {
                liveData.postValue(new ResponseModel(APIStatus.START.name(), null, null));
            }

            @Override
            public void onSuccess(RailCommonData response) {
                liveData.postValue(new ResponseModel(APIStatus.SUCCESS.name(), response, null));
            }

            @Override
            public void onError(ApiErrorModel apiError) {
                liveData.postValue(new ResponseModel(APIStatus.ERROR.name(), null, apiError));
            }

            @Override
            public void onFailure(ApiErrorModel httpError) {
                liveData.postValue(new ResponseModel(APIStatus.FAILURE.name(), null, httpError));
            }
        });

        return liveData;
    }

    public LiveData<Response> getRequstedOfferForUser(int page, int size, String token, int userId, String offers, String currentLanguageCode) {
        MutableLiveData<Response> responseMutableLiveData = new MutableLiveData<>();

        SeasonEpisodesList.getInstance().getRequestedOfferForUser(page,size,token,userId,offers,currentLanguageCode, new RequestOfferCallBack() {
            @Override
            public void success(boolean isStatus, Response response) {
                    responseMutableLiveData.postValue(response);
            }

            @Override
            public void failure(boolean isStatus, String errorCode, String errorMessage) {
                responseMutableLiveData.postValue(null);
            }
        });
        return responseMutableLiveData;
    }
}
