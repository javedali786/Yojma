package com.tv.uscreen.yojmatv.networking.detailPlayer;

import com.google.gson.JsonObject;
import com.tv.uscreen.yojmatv.activities.detail.viewModel.Response;
import com.tv.uscreen.yojmatv.activities.purchase.purchase_model.PurchaseResponseModel;
import com.tv.uscreen.yojmatv.beanModel.AssetHistoryContinueWatching.ResponseAssetHistory;
import com.tv.uscreen.yojmatv.beanModel.addComment.ResponseAddComment;
import com.tv.uscreen.yojmatv.beanModel.allComments.ResponseAllComments;
import com.tv.uscreen.yojmatv.beanModel.allWatchList.ResponseAllWatchList;
import com.tv.uscreen.yojmatv.beanModel.cancelPurchase.ResponseCancelPurchase;
import com.tv.uscreen.yojmatv.beanModel.deleteComment.ResponseDeleteComment;
import com.tv.uscreen.yojmatv.beanModel.emptyResponse.ResponseEmpty;
import com.tv.uscreen.yojmatv.beanModel.entitle.ResponseEntitle;
import com.tv.uscreen.yojmatv.beanModel.isLike.ResponseIsLike;
import com.tv.uscreen.yojmatv.beanModel.isWatchList.ResponseContentInWatchlist;
import com.tv.uscreen.yojmatv.beanModel.like.ResponseAddLike;
import com.tv.uscreen.yojmatv.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import com.tv.uscreen.yojmatv.beanModel.responseModels.detailPlayer.ResponseDetailPlayer;
import com.tv.uscreen.yojmatv.beanModel.responseModels.series.SeriesResponse;
import com.tv.uscreen.yojmatv.beanModel.responseModels.series.season.SeasonResponse;
import com.tv.uscreen.yojmatv.beanModel.watchHistory.ResponseWatchHistory;
import com.tv.uscreen.yojmatv.beanModel.watchList.ResponseWatchList;
import com.tv.uscreen.yojmatv.jwplayer.cast.PlayDetailResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIDetails {

    @Headers("x-platform: android")
    @GET("detail/videoAsset/{Id}")
    Call<ResponseDetailPlayer> getPlayerDetails(@Path("Id") int id);


    @Headers("x-platform: android")
    @GET("homescreen/getYouMayLikeContent")
    Call<SeasonResponse> getYouMayLike(@Query("contentTypeId") int contentTypeId, @Query("pageNo") int pageNo, @Query("length") int length);


    @Headers("x-platform: android")
    @GET("detail/series/{seriesId}")
    Call<SeriesResponse> getSeriesDetails(@Path("seriesId") int seriesId);

    @Headers("x-platform: android")
    @GET("season/vodBySeason")
    Call<SeasonResponse> getSeasonEpisode(@Query("seasonId") int seasonId, @Query("pageNo") int pageNo, @Query("length") int length);


    @Headers("x-platform: android")
    @GET("season/vodBySeason")
    io.reactivex.Observable<SeasonResponse> getSeasonEpisodeMulti(@Query("seasonId") int seasonId, @Query("pageNo") int pageNo, @Query("length") int length);


    @Headers("x-platform: android")
    @GET("season/vodBySeason")
    Call<SeasonResponse> getSeasonEpisodeSingle(@Query("seasonId") int seasonId, @Query("pageNo") int pageNo, @Query("length") int length);


    @Headers("x-platform: android")
    @GET("series/vodBySeries")
    Call<SeasonResponse> getVOD(@Query("seriesId") int seriesId, @Query("pageNo") int pageNo, @Query("length") int length);


    @Headers("x-platform: android")
    @POST("watchList/addToWatchList")
    Call<ResponseWatchList> getAddToWatchList(@Body JsonObject data);

    @Headers("x-platform: android")
    @POST("watchList/isContentPresentOnWatchList")
    Call<ResponseContentInWatchlist> getIsContentWatchList(@Body JsonObject data);


    @Headers("x-platform: android")
    @POST("like/islike")
    Call<ResponseIsLike> getIsLike(@Body JsonObject data);


    @Headers("x-platform: android")
    @POST("like/likes")
    Call<ResponseAddLike> getAddLike(@Body JsonObject data);


    @Headers("x-platform: android")
    @POST("like/delete")
    Call<ResponseEmpty> getUnLike(@Body JsonObject data);


    @Headers("x-platform: android")
    @POST("watchList/removeFromWatchList")
    Call<ResponseEmpty> getRemoveFromWatchList(@Query("watchListItemId") String id);

    @Headers("x-platform: android")
    @GET("watchList/getUsersWatchList")
    Call<ResponseAllWatchList> getAllWatchList(@Query("pageNo") int pageNo, @Query("length") int length);


    @Headers("x-platform: android")
    @GET("watchHistory/getUsersWatchHistory")
    Call<ResponseWatchHistory> getWatchHistory(@Query("pageNo") int pageNo, @Query("length") int length);

    @Headers("x-platform: android")
    @GET("v5/subscription/checkEntitlement")
    Call<ResponseEntitle> checkEntitlement(@Query("vodSKU") String sku);


    @GET
    Call<PlayDetailResponse> getSubtitle(@Url String url);
    @Headers("x-platform: android")
    @GET("v1_0/mediaContent/geo/check?")
    Call<Response> getGeoBlocking(@Query("mediaContentId") String mediaContentId);

    @Headers("x-platform: android")
    @POST("comment/listComments")
    Call<ResponseAllComments> getAllComments(@Query("size") String size, @Query("page") int page, @Body JsonObject data);


    @Headers("x-platform: android")
    @POST("comment/addComment")
    Call<ResponseAddComment> getAddComments(@Body JsonObject data);

    @Headers("x-platform: android")
    @POST("comment/delete")
    Call<ResponseDeleteComment> getDeleteComment(@Query("commentId") String commentId);


    @Headers("x-platform: android")
    @POST("order/new")
    Call<PurchaseResponseModel> getCreateNewPurchase(@Body JsonObject data);

    @Headers("x-platform: android")
    @POST("payment/initiate")
    Call<PurchaseResponseModel> initiatePurchase(@Body JsonObject data);

    @POST("v2/content/like/VOD/{assetId}")
    Call<ResponseEmpty> addToLikeVod(@Path("assetId") int asset);

    @Headers("x-platform: android")
    @POST("payment/{paymentId}")
    Call<PurchaseResponseModel> updatePurchase(@Path("paymentId") String paymentId , @Body JsonObject data);

    @Headers("x-platform: android")
    @GET("v5/subscription/getPlans")
    Call<ResponseMembershipAndPlan> getPlans(@Query("subscriptionOfferType") String a, @Query("subscriptionCheckEntitlement") boolean b);

    @Headers("x-platform: android")
    @GET("v2/purchases/cancelPurchase")
    Call<ResponseCancelPurchase> cancelPurchase();


    @Headers("x-platform: android")
    @POST("continueWatching/addBookmark")
    Call<ResponseEmpty> getBookMark(@Body JsonObject assetRequest);


    @POST("continueWatching/assetHistory")
    Call<ResponseAssetHistory> getAssetHistory(@Body JsonObject assetRequest);


}
