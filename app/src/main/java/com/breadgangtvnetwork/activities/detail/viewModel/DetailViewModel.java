package com.breadgangtvnetwork.activities.detail.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.breadgangtvnetwork.activities.layers.EntitlementLayer;
import com.breadgangtvnetwork.beanModel.AssetHistoryContinueWatching.ResponseAssetHistory;
import com.breadgangtvnetwork.beanModel.addComment.ResponseAddComment;
import com.breadgangtvnetwork.beanModel.allComments.ResponseAllComments;
import com.breadgangtvnetwork.beanModel.deleteComment.ResponseDeleteComment;
import com.breadgangtvnetwork.beanModel.emptyResponse.ResponseEmpty;
import com.breadgangtvnetwork.beanModel.entitle.ResponseEntitle;
import com.breadgangtvnetwork.beanModel.isLike.ResponseIsLike;
import com.breadgangtvnetwork.beanModel.isWatchList.ResponseContentInWatchlist;
import com.breadgangtvnetwork.beanModel.like.ResponseAddLike;
import com.breadgangtvnetwork.beanModel.responseModels.detailPlayer.ResponseDetailPlayer;
import com.breadgangtvnetwork.beanModel.responseModels.landingTabResponses.CommonRailData;
import com.breadgangtvnetwork.beanModel.responseModels.series.SeriesResponse;
import com.breadgangtvnetwork.beanModel.responseModels.series.season.SeasonResponse;
import com.breadgangtvnetwork.beanModel.watchList.ResponseWatchList;
import com.breadgangtvnetwork.repository.bookmarking.BookmarkingRepository;
import com.breadgangtvnetwork.repository.detail.DetailRepository;
import com.breadgangtvnetwork.repository.home.HomeFragmentRepository;
import com.breadgangtvnetwork.utils.constants.AppConstants;
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.enveu.client.bookmarking.bean.GetBookmarkResponse;
import com.google.gson.JsonObject;

import java.util.List;

public class DetailViewModel extends DetailBaseViewModel {
    // private Context context;
    final DetailRepository detailRepository;


    public DetailViewModel(@NonNull Application application) {
        super(application);
        detailRepository = DetailRepository.getInstance();
    }

    @Override
    public LiveData<List<BaseCategory>> getAllCategories() {

        return HomeFragmentRepository.getInstance().getCategories(AppConstants.HOME_ENVEU);
    }

    public LiveData<ResponseDetailPlayer> hitApiDetailPlayer(boolean check, String token, int videoId) {
        return detailRepository.hitApiDetailPlayer(check, token, videoId);
    }

    public LiveData<List<CommonRailData>> hitApiYouMayLike(int videoId, int page, int size) {
        return detailRepository.hitApiYouMayLike(videoId, page, size);
    }

    public LiveData<ResponseWatchList> hitApiAddWatchList(String token, JsonObject data) {
        return detailRepository.hitApiAddToWatchList(token, data);
    }


    public LiveData<ResponseEmpty> hitApiRemoveWatchList(String token, String data) {
        return detailRepository.hitApiRemoveFromWatchList(token, data);
    }


    public LiveData<ResponseContentInWatchlist> hitApiIsWatchList(String token, JsonObject data) {
        return detailRepository.hitApiIsToWatchList(token, data);
    }


    public LiveData<ResponseIsLike> hitApiIsLike(String token, JsonObject data) {
        return detailRepository.hitApiIsLike(token, data);
    }

    public LiveData<ResponseAddLike> hitApiAddLike(String token, JsonObject data) {
        return detailRepository.hitApiAddLike(token, data);
    }

    public LiveData<ResponseEmpty> hitApiUnLike(String token, JsonObject data) {
        return detailRepository.hitApiUnLike(token, data);
    }

    public LiveData<ResponseEntitle> hitApiEntitlement(String token, String sku) {
        return EntitlementLayer.getInstance().hitApiEntitlement(token, sku);
    }


    public LiveData<ResponseAllComments> hitApiAllComents(String size, int page, JsonObject data) {
        return detailRepository.hitApiAllComment(size, page, data);
    }

    public LiveData<ResponseAddComment> hitApiAddComment(String token, JsonObject data) {
        return detailRepository.hitApiAddComment(token, data);
    }

    public LiveData<ResponseDeleteComment> hitApiDeleteComment(String token, String data) {
        return detailRepository.hitApiDeleteComment(token, data);
    }


    public LiveData<JsonObject> hitLogout(boolean session, String token) {
        return detailRepository.hitApiLogout(session, token);
    }

    public LiveData<SeriesResponse> getSeriesDetail(int seriesId) {
        return detailRepository.getSeriesDetail(seriesId);
    }

    public LiveData<SeasonResponse> getVOD(int seriesID, int pageNo, int length) {
        return detailRepository.getVOD(seriesID, pageNo, length);
    }

    public LiveData<List<SeasonResponse>> hitMultiRequestSeries(int size, SeriesResponse data, int railSize) {
        return detailRepository.multiRequestSeries(size, data, railSize);
    }


    public LiveData<SeasonResponse> singleRequestSeries(int id, int page, int size) {
        return detailRepository.singleRequestSeries(id, page, size);
    }

    public LiveData<ResponseEmpty> heartBeatApi(JsonObject assetRequest, String token) {
        return detailRepository.heartBeatApi(assetRequest, token);
    }

    public LiveData<ResponseAssetHistory> getMultiAssetHistory(String token, JsonObject data) {
        return detailRepository.getMultiAssetHistory(token, data);
    }

    @Override
    public void resetObject() {

    }

    public LiveData<GetBookmarkResponse> getBookMarkByVideoId(String token, int videoId) {
    return BookmarkingRepository.getInstance().getBookmarkByVideoId(token,videoId);
    }
}
