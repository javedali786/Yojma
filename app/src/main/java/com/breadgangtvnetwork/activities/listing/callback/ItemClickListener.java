package com.breadgangtvnetwork.activities.listing.callback;

import com.breadgangtvnetwork.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;

public interface ItemClickListener {
    void onRowItemClicked(EnveuVideoItemBean itemValue, int position);

    default void onDeleteWatchListClicked(int assetId, String tittle,int position){

    }
    default void onDeleteWatchHistoryClicked(int assetId, int position){

    }
}
