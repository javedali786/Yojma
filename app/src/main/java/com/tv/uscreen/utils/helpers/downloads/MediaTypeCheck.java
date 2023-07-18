package com.tv.uscreen.utils.helpers.downloads;

import com.tv.uscreen.utils.MediaTypeConstants;

public class MediaTypeCheck {


    public static boolean isMediaTypeSupported(String assetType) {
        boolean supported= MediaTypeConstants.getInstance().getMovie().equalsIgnoreCase(assetType)
                || MediaTypeConstants.getInstance().getEpisode().equalsIgnoreCase(assetType)
                || MediaTypeConstants.getInstance().getShow().equalsIgnoreCase(assetType);
        return supported;
    }
}
