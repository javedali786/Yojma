package com.tv.uscreen.beanModel.ContinueRailModel;

import com.tv.uscreen.beanModel.AssetHistoryContinueWatching.ItemsItem;
import com.tv.uscreen.userAssetList.ContentsItem;

public class CommonContinueRail {


    private int id;
    private ItemsItem userAssetStatus;
    private ContentsItem userAssetDetail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ItemsItem getUserAssetStatus() {
        return userAssetStatus;
    }

    public void setUserAssetStatus(ItemsItem userAssetStatus) {
        this.userAssetStatus = userAssetStatus;
    }

    public ContentsItem getUserAssetDetail() {
        return userAssetDetail;
    }

    public void setUserAssetDetail(ContentsItem userAssetDetail) {
        this.userAssetDetail = userAssetDetail;
    }

    @Override
    public String toString() {
        return "CommonContinueRail{" +
                "id=" + id +
                ", userAssetStatus=" + userAssetStatus +
                ", userAssetDetail=" + userAssetDetail +
                '}';
    }
}
