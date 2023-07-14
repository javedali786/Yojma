package com.breadgangtvnetwork.layersV2;

import com.breadgangtvnetwork.callbacks.apicallback.ApiResponseModel;
import com.breadgangtvnetwork.callbacks.commonCallbacks.CommonApiCallBack;
import com.breadgangtvnetwork.networking.apiendpoints.ApiInterface;
import com.breadgangtvnetwork.networking.apiendpoints.RequestConfig;
import com.breadgangtvnetwork.networking.servicelayer.APIServiceLayer;
import com.enveu.client.bookmarking.bean.continuewatching.ContinueWatchingBookmark;
import com.enveu.client.watchHistory.beans.ItemsItem;

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
