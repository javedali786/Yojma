package com.tv.uscreen.yojmatv.layersV2;

import com.tv.uscreen.yojmatv.callbacks.apicallback.ApiResponseModel;
import com.tv.uscreen.yojmatv.networking.apiendpoints.ApiInterface;
import com.tv.uscreen.yojmatv.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.yojmatv.networking.servicelayer.APIServiceLayer;

public class SeriesDataLayer {

    private static SeriesDataLayer seriesDataLayerInstance;
    private static ApiInterface endpoint;

    private SeriesDataLayer() {

    }

    public static SeriesDataLayer getInstance() {
        if (seriesDataLayerInstance == null) {
            if (RequestConfig.getEnveuClient()!= null) {
                endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            }
            seriesDataLayerInstance = new SeriesDataLayer();
        }
        return (seriesDataLayerInstance);
    }


    public void getSeriesData(String assetID,boolean isIntentFromExpedition, ApiResponseModel listener) {
        APIServiceLayer.getInstance().getSeriesData(assetID, isIntentFromExpedition,listener);
    }
}
