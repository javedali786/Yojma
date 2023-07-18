package com.tv.uscreen.yojmatv.utils;

import com.tv.uscreen.yojmatv.beanModel.responseModels.series.SeasonsItem;

import java.util.Comparator;

public class SortSeasonList implements Comparator<SeasonsItem> {

    @Override
    public int compare(SeasonsItem searchedKeywords, SeasonsItem t1) {
        return (t1.getSeasonNo() - searchedKeywords.getSeasonNo());
    }
}

