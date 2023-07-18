package com.tv.uscreen.layersV2;

import com.enveu.client.bookmarking.bean.continuewatching.ContinueWatchingBookmark;
import com.enveu.client.watchHistory.beans.ItemsItem;
import com.tv.uscreen.callbacks.apicallback.ApiResponseModel;
import com.tv.uscreen.callbacks.commonCallbacks.CommonApiCallBack;
import com.tv.uscreen.networking.apiendpoints.ApiInterface;
import com.tv.uscreen.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.networking.servicelayer.APIServiceLayer;

import java.util.List;


public class ContinueWatchingLayer {

    private static ContinueWatchingLayer videoDetailLayerInstance;
    private static ApiInterface endpoint;
    ApiResponseModel callBack;

    private ContinueWatchingLayer() {

    }

    public static ContinueWatchingLayer getInstance() {
        if (videoDetailLayerInstance == null) {
            endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            videoDetailLayerInstance = new ContinueWatchingLayer();
        }
        return (videoDetailLayerInstance);
    }

    public void getContinueWatchingVideos(List<ContinueWatchingBookmark> continueWatchingBookmarkList, String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        APIServiceLayer.getInstance().getContinueWatchingVideos(continueWatchingBookmarkList, manualImageAssetId, commonApiCallBack);
    }

    public void getWatchHistoryVideos(List<ItemsItem> continueWatchingBookmarkList, String manualImageAssetId, CommonApiCallBack commonApiCallBack) {
        APIServiceLayer.getInstance().getWatchListVideos(continueWatchingBookmarkList, manualImageAssetId, commonApiCallBack);
    }
}
