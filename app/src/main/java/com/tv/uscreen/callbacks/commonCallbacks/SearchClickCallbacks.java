package com.tv.uscreen.callbacks.commonCallbacks;

import com.tv.uscreen.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.uscreen.beanModel.popularSearch.ItemsItem;
import com.tv.uscreen.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;

public interface SearchClickCallbacks {
    void onEnveuItemClicked(EnveuVideoItemBean itemValue);
    void onShowAllItemClicked(RailCommonData itemValue,String header);
    void onShowAllProgramClicked(RailCommonData itemValue);
    void onPopularSearchItemClicked(ItemsItem itemValue);
}
