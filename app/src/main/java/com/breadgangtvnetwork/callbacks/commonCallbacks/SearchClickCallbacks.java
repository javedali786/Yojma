package com.breadgangtvnetwork.callbacks.commonCallbacks;

import com.breadgangtvnetwork.beanModel.enveuCommonRailData.RailCommonData;
import com.breadgangtvnetwork.beanModel.popularSearch.ItemsItem;
import com.breadgangtvnetwork.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;

public interface SearchClickCallbacks {
    void onEnveuItemClicked(EnveuVideoItemBean itemValue);
    void onShowAllItemClicked(RailCommonData itemValue,String header);
    void onShowAllProgramClicked(RailCommonData itemValue);
    void onPopularSearchItemClicked(ItemsItem itemValue);
}
