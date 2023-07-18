package com.tv.uscreen.yojmatv.utils;

import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData;

import java.util.Comparator;

public class SortSearchList implements Comparator<RailCommonData> {

    @Override
    public int compare(RailCommonData searchedKeywords, RailCommonData t1) {

        return (searchedKeywords.getLayoutType() - t1.getLayoutType());
    }
}
