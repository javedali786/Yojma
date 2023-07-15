package com.breadgangtvnetwork.layersV2;

import com.breadgangtvnetwork.callbacks.apicallback.ApiResponseModel;
import com.breadgangtvnetwork.networking.apiendpoints.ApiInterface;
import com.breadgangtvnetwork.networking.apiendpoints.RequestConfig;
import com.breadgangtvnetwork.networking.servicelayer.APIServiceLayer;

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
