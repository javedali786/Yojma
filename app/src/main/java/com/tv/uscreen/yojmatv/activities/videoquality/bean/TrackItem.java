package com.tv.uscreen.yojmatv.activities.videoquality.bean;

public class TrackItem {


    private final String trackName; //Readable name of the track.
    private final String uniqueId; //Unique id, which should be passed to player in order to change track.
    private final String description;

    public TrackItem(String trackName, String uniqueId, String description) {
        this.trackName = trackName;
        this.uniqueId = uniqueId;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getUniqueId() {
        return uniqueId;
    }
}

