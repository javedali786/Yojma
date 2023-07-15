package com.breadgangtvnetwork.layersV2;

import com.breadgangtvnetwork.callbacks.apicallback.ApiResponseModel;
import com.breadgangtvnetwork.networking.apiendpoints.ApiInterface;
import com.breadgangtvnetwork.networking.apiendpoints.RequestConfig;
import com.breadgangtvnetwork.networking.servicelayer.APIServiceLayer;
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;

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
