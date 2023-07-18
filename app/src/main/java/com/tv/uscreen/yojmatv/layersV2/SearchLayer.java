package com.tv.uscreen.yojmatv.layersV2;

import androidx.lifecycle.LiveData;

import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.uscreen.yojmatv.beanModel.search.SearchRequestModel;
import com.tv.uscreen.yojmatv.callbacks.apicallback.ApiResponseModel;
import com.tv.uscreen.yojmatv.networking.apiendpoints.ApiInterface;
import com.tv.uscreen.yojmatv.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.yojmatv.networking.servicelayer.APIServiceLayer;

import java.util.List;

public class SearchLayer {

    private static SearchLayer searchLayerInstance;
    private static ApiInterface endpoint;
    ApiResponseModel callBack;

    private SearchLayer() {

    }

    public static SearchLayer getInstance() {
        if (searchLayerInstance == null) {
            endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            searchLayerInstance = new SearchLayer();
        }
        return (searchLayerInstance);
    }


    public LiveData<List<RailCommonData>> getSearchData(String keyword, int size, int page, boolean applyFilter, SearchRequestModel requestModel) {
        return APIServiceLayer.getInstance().getSearchData(keyword,size,page,applyFilter, requestModel);
    }

    public LiveData<RailCommonData> getSingleCategorySearch(String keyword, String type, int size, int page, boolean applyFilter,String customContentType,String videoType,String header) {
        return APIServiceLayer.getInstance().getSingleCategorySearch(keyword, type, size, page, applyFilter, customContentType,videoType,header);
    }

    public LiveData<RailCommonData> getProgramSearch(String keyword, int size, int page,boolean applyFilter) {
        return APIServiceLayer.getInstance().getProgramSearch(keyword, size, page, applyFilter);
    }


}
