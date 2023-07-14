package com.breadgangtvnetwork.utils.helpers.downloads;

import com.breadgangtvnetwork.utils.MediaTypeConstants;

public class MediaTypeCheck {


    public static boolean isMediaTypeSupported(String assetType) {
        boolean supported= MediaTypeConstants.getInstance().getMovie().equalsIgnoreCase(assetType)
                || MediaTypeConstants.getInstance().getEpisode().equalsIgnoreCase(assetType)
                || MediaTypeConstants.getInstance().getShow().equalsIgnoreCase(assetType);
        return supported;
    }
}
