package com.breadgangtvnetwork.utils.helpers;

import com.breadgangtvnetwork.beanModel.responseModels.series.SeasonsItem;

import java.util.Comparator;

public class SortSeasonNumber implements Comparator<SeasonsItem> {

    @Override
    public int compare(SeasonsItem searchedKeywords, SeasonsItem t1) {
        return (searchedKeywords.getSeasonNo() - t1.getSeasonNo());
    }
}