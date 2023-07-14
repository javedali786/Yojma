package com.breadgangtvnetwork.utils;


import com.breadgangtvnetwork.beanModel.KeywordList;

import java.util.Comparator;

class SortList implements Comparator<KeywordList> {

    @Override
    public int compare(KeywordList searchedKeywords, KeywordList t1) {

        return (int) (searchedKeywords.getTimeStamp() - t1.getTimeStamp());
    }
}
