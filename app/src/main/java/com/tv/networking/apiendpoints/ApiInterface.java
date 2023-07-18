package com.tv.networking.apiendpoints;


import com.google.gson.JsonObject;
import com.tv.activities.watchList.model.VideoIdModel;
import com.tv.beanModel.AssetHistoryContinueWatching.ResponseAssetHistory;
import com.tv.beanModel.changePassword.ResponseChangePassword;
import com.tv.beanModel.configBean.ResponseConfig;
import com.tv.beanModel.connectFb.ResponseConnectFb;
import com.tv.beanModel.emptyResponse.ResponseEmpty;
import com.tv.beanModel.enveuCommonRailData.config.EnveuConfigResponse;
import com.tv.beanModel.mobileAds.ResponseMobileAds;
import com.tv.beanModel.popularSearch.ResponsePopularSearch;
import com.tv.beanModel.responseGetWatchlist.ResponseGetIsWatchlist;
import com.tv.beanModel.responseIsLike.ResponseIsLike;
import com.tv.beanModel.responseModels.LoginResponse.LoginResponseModel;
import com.tv.beanModel.responseModels.RegisterSignUpModels.ResponseRegisteredSignup;
import com.tv.beanModel.responseModels.SignUp.SignUpResponseModel;
import com.tv.beanModel.responseModels.landingTabResponses.playlistResponse.PlaylistResponses;
import com.tv.beanModel.responseModels.landingTabResponses.railData.PlaylistRailData;
import com.tv.beanModelV3.continueWatching.ContinueWatchingModel;
import com.tv.beanModelV3.playListModelV2.EnveuCommonResponse;
import com.tv.beanModelV3.searchV2.ResponseSearch;
import com.tv.beanModelV3.videoDetailsV2.EnveuVideoDetailsBean;
import com.tv.bean_model_v1_0.listAll.ListAllContent;
import com.tv.bean_model_v1_0.listAll.RequestOfferList.Response;
import com.tv.bean_model_v1_0.videoDetailBean.EnvVideoDetailsBean;
import com.tv.redeemcoupon.RedeemCouponResponseModel;
import com.tv.userAssetList.ResponseUserAssetList;
import com.tv.utils.config.bean.dmsResponse.ConfigBean;
import com.tv.utils.recoSense.bean.RecosenceResponse;

import org.json.JSONObject;

import java.util.List;

import app.doxzilla.activities.order_history.model.OrderHistoryModel;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers("x-platform: android")
    @GET("config")
    Call<ResponseConfig> getConfiguration(@Query("getLatest") String param1);

    @Headers("x-platform: android")
    @POST("user/login/manual")
    Call<LoginResponseModel> getLogin(@Body JsonObject apiLogin);

    @Headers("x-platform: android")
    @POST("user/register/manual")
    Call<SignUpResponseModel> getSignUp(@Body JsonObject apiSignUp);

    @Headers("x-platform: android")
    @PATCH("user/update/profile")
    Call<ResponseRegisteredSignup> getRegistrationStep(@Body JsonObject user);

    @GET("content/listAll")
    Call<EnveuCommonResponse> getLinkedContentList(@Query("linkedContentId") String seriesId, @Query("seasonNumber") int seasonNumber, @Query("page") int page, @Query("size") int size);

    @GET("user/forgotPassword")
    Call<JsonObject> getForgotPassword(@Query("email") String param1);

    @Headers("x-platform: android")
    @POST("user/changePassword")
    Call<ResponseChangePassword> getChangePassword(@Body JsonObject user);

    @Headers("x-platform: android")
    @POST("user/login/fb")
    Call<LoginResponseModel> getFbLogin(@Body JsonObject user);

    @Headers("x-platform: android")
    @POST("user/connectToFb")
    Call<ResponseConnectFb> getConnectFb(@Body JsonObject user);

    @Headers("x-platform: android")
    @POST("user/login/fb/force")
    Call<LoginResponseModel> getForceFbLogin(@Body JsonObject user);

    @Headers("x-platform: android")
    @GET("user/logout")
    Call<JsonObject> getLogout(@Query("removeAllSession") boolean param1);

    @Headers("x-platform: android")
    @POST("user/logout/false")
    Call<JsonObject> getUserLogout();


    @Headers("x-platform: android")
    @GET("user/sendVerificationEmail")
    Call<ResponseEmpty> getVerifyUser();


    @Headers("x-platform: android")
    @GET("popularSearch")
    Call<ResponsePopularSearch> getPopularSearch(@Query("size") int size, @Query("page") int page);

    @Headers("x-platform: android")
    @GET("searchSeries")
    Call<ResponsePopularSearch> getSearchKeyword(@Query("query") String keyword, @Query("size") int size, @Query("page") int page);

    @Headers("x-platform: android")
    @GET("homescreen/getConfig")
    Call<PlaylistResponses> getPlaylists(@Query("id") int Id);

    @Headers("x-platform: android")
    @GET("homescreen/getBaseCategories")
    Call<PlaylistRailData> getPlaylistsData(@Query("id") int id, @Query("pageNo") int pageNo, @Query("length") int length);


    @Headers("x-platform: android")
    @GET("homescreen/getBaseCategories")
    Observable<PlaylistRailData> getPlaylistsDataHome(@Query("id") int Id, @Query("pageNo") int pageNo, @Query("length") int length);


    @Headers("x-platform: android")
    @GET("ad/fetchAds")
    Call<ResponseMobileAds> getAdsData(@Query("navigationScreenId") int Id, @Query("platform") String platform);


    @POST("continueWatching/assetHistory")
    Call<ResponseAssetHistory> getAssetHistory(@Body JsonObject assetRequest);

    @POST("continueWatching/userAssetList")
    Call<ResponseUserAssetList> getAssetList(@Body JsonObject assetRequest);


    @GET("config")
    Call<EnveuConfigResponse> getConfig();


    //V2 changes applied in below APIs-->> Versioning moved to endpoints
    @GET("v1_0/mediaPlaylist/contents")
    Call<EnveuCommonResponse> getPlaylistDetailsById(@Query("playlistId") String playListId, @Query("locale") String locale, @Query("page") int pageNumber, @Query("size") int pageSize);


    @GET("v1_0/mediaPlaylist/contents")
    Call<EnveuCommonResponse> getPlaylistDetailsByIdWithPG(@Query("playlistId") String playListId, @Query("locale") String locale,
                                                           @Query("page") int pageNumber, @Query("size") int pageSize, @Query("parentalRating")
                                                                   String parentalRating);


    //V2 PI for getting asset details
    @GET("v3/content")
    Call<EnveuVideoDetailsBean> getVideoDetails(@Query("contentId") String manualImageAssetId, @Query("locale") String locale);

    @GET("v1_0/mediaContent")
    Call<EnveuVideoDetailsBean> getVideoDetailsPG(@Query("mediaContentId") String manualImageAssetId, @Query("locale") String locale,@Query("parentalRating") String parentalRating);

    @GET("v1_0/mediaContent")
    Call<EnveuVideoDetailsBean> getEnvVideoDetailsPG(@Query("mediaContentId") String manualImageAssetId);


    // v2 API for getting related content -->> all the season episodes
    @GET("v1_0/mediaContent/listAll")
    Call<ListAllContent> getRelatedContent(@Query("linkedContentId") int seriesId,
                                           @Query("seasonNumber") int seasonNumber,
                                           @Query("page") int pageNumber,
                                           @Query("size") int pageSize,
                                           @Query("locale") String locale);

    @GET("v3/content/listAll")
    Call<EnveuCommonResponse> getRelatedContentPG(@Query("linkedContentId") int seriesId,
                                                  @Query("seasonNumber") int seasonNumber,
                                                  @Query("page") int pageNumber,
                                                  @Query("size") int pageSize,
                                                  @Query("locale") String locale,
                                                  @Query("parentalRating") String parentalRating);

    @GET("v4/content/like/listAll")
    Call<com.tv.bean_model_v1_0.listAll.likeList.Response> getIsLikeGOI(@Query("page") int pageNumber,
                                                                        @Query("size") int pageSize,
                                                                        @Query("locale") String locale);

    @GET("participation/listAll")
    Call<Response> getRequestOfferForUser(@Query("userId") int userId,
                                          @Query("customType") String customType,
                                          @Query("page") int pageNumber,
                                          @Query("size") int pageSize,
                                          @Query("locale") String locale);



    // v2 API for getting related content -->> all the episodes
    @GET("v1_0/mediaContent/listAll")
    Call<ListAllContent> getRelatedContentWithoutSNo(@Query("linkedContentId") int seriesId,
                                                          @Query("page") int pageNumber,
                                                          @Query("size") int pageSize,
                                                          @Query("locale") String locale);

    // v2 API for getting related content -->> all the episodes
    @GET("v1_0/mediaContent/listAll")
    Call<ListAllContent> getForYouContent(@Query("locale") String locale,
                                                     @Query("page") int pageNumber,
                                                     @Query("size") int pageSize,
                                                     @Query("tag") String tag,
                                                     @Query("contentType") String contentType,
                                                     @Query("videoType") String videoType
    );





    @GET("v1_0/mediaContent/listAll")
    Call<ListAllContent> getForYouCustomContent(@Query("locale") String locale,
                                          @Query("page") int pageNumber,
                                          @Query("size") int pageSize,
                                          @Query("tag") String tag,
                                          @Query("contentType") String contentType,
                                          @Query("customType") String videoType
    );

    @GET("v1_0/mediaContent/{id}/getRelatedContents")
    Call<EnveuCommonResponse> getRelatedContentForVideo(@Path("id") int asset,@Query("locale") String locale,
                                                @Query("page") int pageNumber,
                                                @Query("size") int pageSize,
                                                @Query("contentType") String contentType
    );

    @GET("v1_0/mediaContent/3474/getRelatedContents")
    Call<EnveuCommonResponse> getRelatedContentForVideCustom(@Query("locale") String locale,
                                                   @Query("page") int pageNumber,
                                                   @Query("size") int pageSize,
                                                   @Query("contentType") String contentType
    );


    @GET("v1_0/mediaContent/listAll")
    Call<ListAllContent> getCustomDetail(@Query("locale") String locale,
                                          @Query("page") int pageNumber,
                                          @Query("size") int pageSize,
                                          @Query("tag") String tag,
                                          @Query("contentType") String contentType);


    @GET("v3/content/listAll")
    Call<EnveuCommonResponse> getRelatedContentWithoutSNoPG(@Query("linkedContentId") int seriesId,
                                                            @Query("page") int pageNumber,
                                                            @Query("size") int pageSize,
                                                            @Query("locale") String locale,
                                                            @Query("parentalRating") String parentalRating);

    //V2 PI for getting asset details
    @GET("v3/content")
    Call<ContinueWatchingModel> getVideos(@Query("contentId") String manualImageAssetId, @Query("locale") String locale);

//    @GET("v3/content")
    @GET("v1_0/mediaContent")
    Call<VideoIdModel> getVideoIdData(@Query("mediaContentId") String manualImageAssetId, @Query("locale") String locale);

    @GET("v3/content")
    Call<ContinueWatchingModel> getVideosPG(@Query("contentId") String manualImageAssetId, @Query("locale") String locale,@Query("parentalRating") String parentalRating);


    @Headers("x-platform: android")
    @GET("v1_0/search")
    io.reactivex.Observable<ResponseSearch> getSearch(@Query("keyword") String keyword,
                                                      @Query("contentType") String type,
                                                      @Query("size") int size,
                                                      @Query("page") int page,
                                                      @Query("locale") String locale,
                                                      @Query("videoType") String videoType);


    @Headers("x-platform: android")
    @GET("v1_0/search")
    io.reactivex.Observable<ResponseSearch> getSearchByFilters(@Query("keyword") String keyword, @Query("contentType") String type,
                                                               @Query("size") int size,
                                                               @Query("page") int page,
                                                               @Query("locale") String locale,
                                                               @Query("videoType") String videoType,
                                                               @Query("filters") List<String> filterGenreSavedListKeyForApi,
                                                               @Query("sort") List<String> filterSortSavedListKeyForApi);
    @Headers("x-platform: android")
    @GET("v1_0/search")
    io.reactivex.Observable<ResponseSearch> getSearchByCustomFilters(@Query("keyword") String keyword,
                                                               @Query("contentType") String type,
                                                               @Query("size") int size, @Query("page") int page, @Query("locale") String locale,
                                                               @Query("customType") String customType,
                                                               @Query("sort") List<String> filterSortSavedListKeyForApi);



    @Headers("x-platform: android")
    @GET("v1_0/search")
    Call<ResponseSearch> getSearchResults(@Query("keyword") String keyword,
                                          @Query("contentType") String type,
                                          @Query("size") int size, @Query("page") int page,
                                          @Query("locale") String locale,
                                          @Query("customType") String customType);
    @Headers("x-platform: android")
    @GET("v1_0/search")
    Call<ResponseSearch> getVideoSearchResults(@Query("keyword") String keyword,
                                          @Query("contentType") String type,
                                          @Query("size") int size, @Query("page") int page,
                                          @Query("locale") String locale,
                                          @Query("videoType") String videoType);
    @Headers("x-platform: android")
    @GET("v1_0/search")
    Call<ResponseSearch> getSearchResults(@Query("keyword") String keyword, @Query("contentType") List<String> type, @Query("size") int size, @Query("page") int page, @Query("locale") String locale);

    @Headers("x-platform: android")
    @GET("v1_0/search")
    Call<ResponseSearch> getSearchResultsByFilters(@Query("keyword") String keyword, @Query("contentType") String type, @Query("size") int size, @Query("page") int page, @Query("locale") String locale,
                                                   @Query("filters") List<String> filterGenreSavedListKeyForApi, @Query("sort") List<String> filterSortSavedListKeyForApi);

    @Headers("x-platform: android")
    @GET("v1_0/search")
    Call<ResponseSearch> getSearchResultsByFilters(@Query("keyword") String keyword, @Query("contentType") List<String> type, @Query("size") int size, @Query("page") int page, @Query("locale") String locale,
                                                   @Query("filters") List<String> filterGenreSavedListKeyForApi, @Query("sort") List<String> filterSortSavedListKeyForApi);
//till this

    @DELETE("v2/content/like/delete/{assetId}")
    Call<ResponseEmpty> deleteLike(@Path("assetId") int asset);

    @DELETE("v4/content/like/delete/{assetId}")
    Call<ResponseEmpty> deleteLikeGOI(@Path("assetId") int asset);

    @DELETE("v2/user/watchlist/delete/{assetId}")
    Call<ResponseEmpty> removeWatchlist(@Path("assetId") int asset);

    @DELETE("v4/user/watchlist/delete/{assetId}")
    Call<ResponseEmpty> removeWatchlistGOI(@Path("assetId") int asset);

    @GET("v2/user/watchlist/get/VOD/{assetId}")
    Call<ResponseGetIsWatchlist> getIsWatchList(@Path("assetId") int asset);


    @POST("v2/user/watchlist/add/VOD/{assetId}")
    Call<ResponseEmpty> addToWatchList(@Path("assetId") int asset);

    @POST("v2/user/watchlist/add/VOD/{assetId}")
    Call<ResponseEmpty> addToWatchListGOIVod(@Path("assetId") int asset);

    @POST("v2/user/watchlist/add/CUSTOM/{assetId}")
    Call<ResponseEmpty> addToWatchListGOICustom(@Path("assetId") int asset);


    @POST("v2/user/watchHistory/add/VOD")
    Call<ResponseEmpty> addToWatchHistory(@Query("") int asset);


    @POST("v2/content/like/VOD/{assetId}")
    Call<ResponseEmpty> addToLikeVod(@Path("assetId") int asset);


    @POST("v4/content/like/CUSTOM/{assetId}")
    Call<ResponseEmpty> addToLikeGOICustom(@Path("assetId") int asset);

    @POST("v4/content/like/VOD/{assetId}")
    Call<ResponseEmpty> addToLikeGOIVod(@Path("assetId") int asset);


    @GET("v2/content/like/VOD/{assetId}")
    Call<ResponseIsLike> getIsLikeVod(@Path("assetId") int asset);


    @GET("getConfig")
    Call<ConfigBean> getConfig(@Query("version") int manualImageAssetId);

    @POST("webhooks")
    Call<RecosenceResponse> getRecoClick(@Body JSONObject assetRequest);

    @GET("v1_0/mediaContent")
    Call<EnvVideoDetailsBean> getEnvVideoDetails(@Query("mediaContentId") String manualImageAssetId, @Query("locale") String locale);


    @POST("coupon/redeemCoupon")
    Call<RedeemCouponResponseModel> redeemCoupon(@Query("redeemCode") String redeemCode);

    @GET("v2/order/orderHistory")
    Call<OrderHistoryModel> getOrderHistory(@Query("page") String page, @Query("size") String size);
}

