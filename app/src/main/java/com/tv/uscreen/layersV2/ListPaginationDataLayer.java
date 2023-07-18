package com.tv.uscreen.layersV2;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.tv.uscreen.callbacks.apicallback.ApiResponseModel;
import com.tv.uscreen.networking.apiendpoints.ApiInterface;
import com.tv.uscreen.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.networking.servicelayer.APIServiceLayer;

public class ListPaginationDataLayer {

    private static ListPaginationDataLayer listPaginationDataLayerInstance;
    private static ApiInterface endpoint;
    private ApiResponseModel callBack;

    private ListPaginationDataLayer() {

    }

    public static ListPaginationDataLayer getInstance() {
        if (listPaginationDataLayerInstance == null) {
            endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            listPaginationDataLayerInstance = new ListPaginationDataLayer();
        }
        return (listPaginationDataLayerInstance);
    }


    public void getPlayListByWithPagination(String playlistID,
                                            int pageNumber,
                                            int pageSize,
                                            BaseCategory screenWidget, ApiResponseModel listener) {
        APIServiceLayer.getInstance().getPlayListByWithPagination(playlistID, pageNumber, pageSize, screenWidget,listener);
    }
}
