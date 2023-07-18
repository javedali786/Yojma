package com.tv.uscreen.yojmatv.utils.helpers;

import com.tv.uscreen.yojmatv.beanModel.responseModels.series.SeasonsItem;

import java.util.Comparator;

public class SortSeasonNumber implements Comparator<SeasonsItem> {

    @Override
    public int compare(SeasonsItem searchedKeywords, SeasonsItem t1) {
        return (searchedKeywords.getSeasonNo() - t1.getSeasonNo());
    }
}