package com.tv.uscreen.Bookmarking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.enveu.client.bookmarking.bean.BookmarkingResponse;
import com.enveu.client.bookmarking.bean.GetBookmarkResponse;
import com.enveu.client.bookmarking.bean.continuewatching.GetContinueWatchingBean;
import com.enveu.client.watchHistory.beans.ResponseWatchHistoryAssetList;
import com.google.gson.JsonObject;
import com.tv.uscreen.beanModel.emptyResponse.ResponseEmpty;
import com.tv.uscreen.beanModel.responseGetWatchlist.ResponseGetIsWatchlist;
import com.tv.uscreen.beanModel.responseIsLike.ResponseIsLike;
import com.tv.uscreen.repository.bookmarking.BookmarkingRepository;
import com.tv.uscreen.repository.userManagement.RegistrationLoginRepository;
import com.tv.uscreen.repository.userinteraction.UserInterationRepository;

public class BookmarkingViewModel extends AndroidViewModel {
    public BookmarkingViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BookmarkingResponse> bookmarkVideo(String token, int assestId, int currentPosition) {
        return BookmarkingRepository.getInstance().bookmarkVideo(token,assestId,currentPosition);
    }

    public MutableLiveData<GetBookmarkResponse> finishBookmark(String token, int assestId) {
        return BookmarkingRepository.getInstance().finishBookmark(token,assestId);
    }

    public MutableLiveData<GetContinueWatchingBean> getContinueWatchingData(String token, int pageNumber, int pageSize) {
        return BookmarkingRepository.getInstance().getContinueWatchingData(token,pageNumber,pageSize);
}

    public LiveData<ResponseWatchHistoryAssetList> getWatchHistory(String token,
                                                                   int page, int size) {
        return BookmarkingRepository.getInstance().hitApiGetWatchHistoryList(token, page, size);
    }

    public void addToWatchHistory(String token, int assestId) {
        BookmarkingRepository.getInstance().addToWatchHistory(token,assestId);
    }

    public LiveData<BookmarkingResponse>  deleteFromWatchHistory(String token, int assetId) {
        return BookmarkingRepository.getInstance().deleteFromWatchHistory(token,assetId);
    }

    public LiveData<ResponseGetIsWatchlist> hitApiIsWatchList(String token, int seriesId) {
        return UserInterationRepository.getInstance().hitApiIsToWatchList(token, seriesId);
    }

    public LiveData<ResponseEmpty> hitApiAddWatchList(String token, int seriesId) {
        return UserInterationRepository.getInstance().hitApiAddToWatchList(token, seriesId);
    }

    public LiveData<ResponseEmpty> hitApiAddWatchListGOI(String token, int seriesId,String assetId) {
        return UserInterationRepository.getInstance().hitApiAddToWatchListGOI(token, seriesId,assetId);
    }
    public LiveData<ResponseIsLike> hitApiIsLike(String token, int assetId) {
        return UserInterationRepository.getInstance().hitApiIsLike(token, assetId);
    }

    public LiveData<ResponseEmpty> hitApiDeleteLike(String token, int assetId) {
        return UserInterationRepository.getInstance().hitApiDeleteLike(token, assetId);
    }

    public LiveData<ResponseEmpty> hitApiDeleteLikeGOI(String token, int assetId,String assetType) {
        return UserInterationRepository.getInstance().hitApiDeleteLikeGOI(token, assetId,assetType);
    }

    public LiveData<ResponseEmpty> hitRemoveWatchlist(String token, int assetId) {
        return UserInterationRepository.getInstance().hitRemoveWatchlist(token, assetId);
    }
    public LiveData<ResponseEmpty> hitRemoveWatchlistGOI(String token, int assetId) {
        return UserInterationRepository.getInstance().hitRemoveWatchlistGOI(token, assetId);
    }
    public LiveData<ResponseEmpty> hitApiAddLike(String token, int assetId) {
        return UserInterationRepository.getInstance().hitApiAddLike(token, assetId);
    }

    public LiveData<ResponseEmpty> addToLikeGOI(String token, int assetId,String assetType) {
        return UserInterationRepository.getInstance().addToLikeGOI(token, assetId,assetType);
    }

    public LiveData<JsonObject> hitLogout(boolean session, String token) {
        return RegistrationLoginRepository.getInstance().hitApiLogout(session, token);
    }

    public LiveData<ResponseWatchHistoryAssetList> getMywatchListData(String token, int pageNumber, int pageSize) {
        return BookmarkingRepository.getInstance().getMyWatchListData(token,pageNumber,pageSize);
    }

}
