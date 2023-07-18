package com.tv.uscreen.yojmatv.utils;

import com.tv.uscreen.yojmatv.beanModel.responseModels.landingTabResponses.playlistResponse.PlaylistsItem;

import java.util.Comparator;

public class SortPlaylistId implements Comparator<PlaylistsItem> {

    @Override
    public int compare(PlaylistsItem listOne, PlaylistsItem listTwo) {

        return listOne.getId() - listTwo.getId();
    }
}