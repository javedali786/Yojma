package com.breadgangtvnetwork.activities.watchList.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.breadgangtvnetwork.beanModel.AssetHistoryContinueWatching.ResponseAssetHistory;
import com.breadgangtvnetwork.beanModel.emptyResponse.ResponseEmpty;
import com.breadgangtvnetwork.repository.watchList.WatchListRepository;

import app.doxzilla.activities.order_history.model.OrderHistoryModel;

public class WatchListViewModel extends AndroidViewModel {
    // private Context context;
    private final WatchListRepository watchListRepository;

    public WatchListViewModel(@NonNull Application application) {
        super(application);
        //  this.context = application;
        watchListRepository = WatchListRepository.getInstance();

    }
    public LiveData<ResponseEmpty> hitApiRemoveWatchList(String token, String data) {
        return watchListRepository.hitApiRemoveFromWatchList(token, data);
    }

    public LiveData<ResponseAssetHistory> getAssetHistory(String token, int page, int size) {
        return watchListRepository.getAssetHistory(token, page, size);
    }
    public LiveData<OrderHistoryModel> getOrderHistory(String token, String page, String size) {
        return watchListRepository.getOrderHistoryModel(token, page, size);
    }
    public void callCleared() {
        onCleared();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}

