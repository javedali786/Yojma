package com.breadgangtvnetwork.utils;

import com.breadgangtvnetwork.beanModel.responseModels.landingTabResponses.railData.PlaylistRailData;

import java.util.Comparator;

public class SortRailsList implements Comparator<PlaylistRailData> {

    @Override
    public int compare(PlaylistRailData listOne, PlaylistRailData listTwo) {

        return listOne.getData().getIndex() - listTwo.getData().getIndex();
    }
}
