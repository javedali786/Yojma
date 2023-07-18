package com.tv.callbacks.commonCallbacks;

import com.tv.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.beanModel.popularSearch.ItemsItem;
import com.tv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;

public interface SearchClickCallbacks {
    void onEnveuItemClicked(EnveuVideoItemBean itemValue);
    void onShowAllItemClicked(RailCommonData itemValue,String header);
    void onShowAllProgramClicked(RailCommonData itemValue);
    void onPopularSearchItemClicked(ItemsItem itemValue);
}
