package com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.enveu.client.epgListing.epgResponse.Program;
import com.enveu.client.playlist.beanv1_0.LiveContent;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import com.tv.uscreen.yojmatv.OttApplication;

import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.beanModelV3.continueWatching.DataItem;
import com.tv.uscreen.yojmatv.beanModelV3.playListModelV2.CustomContentData;
import com.tv.uscreen.yojmatv.beanModelV3.playListModelV2.VideosItem;
import com.tv.uscreen.yojmatv.beanModelV3.searchV2.ItemsItem;
import com.tv.uscreen.yojmatv.beanModelV3.videoDetailsV2.EnveuVideoDetailsBean;
import com.tv.uscreen.yojmatv.beanModelV3.videoDetailsV2.VideoDetails;
import com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.AudioTrackListItem;
import com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.Item;
import com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.ParentContent;
import com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.VideoItem;
import com.tv.uscreen.yojmatv.bean_model_v1_0.videoDetailBean.Data;
import com.tv.uscreen.yojmatv.bean_model_v1_0.videoDetailBean.DetailVideo;
import com.tv.uscreen.yojmatv.utils.CustomeFields;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.config.ImageLayer;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EnveuVideoItemBean implements Serializable {
    private ArrayList seasons;
    private String description;
    private String longDescription;
    private List<String> assetKeywords;
    private int likeCount;
    private String title;
    private long startTime;
    private Object svod;

    public String getDisplay_date() {
        return display_date;
    }

    public void setDisplay_date(String display_date) {
        this.display_date = display_date;
    }

    private String parentalSeriesId;
    private String contentProvider;
    private List<String> assetCast;
    private List<Object> keywords;
    private List<Object> video;
    private Boolean premium;
    private String posterURL;
    private String profilePosterUrl;
    private Object price;
    private List<String> assetGenres;
    private String season;
    private int id;
    private String externalRefId;
    private String sku;
    private boolean isNew;
    private Object tvod;
    private Object episodeNo;
    private String assetType;
    private int commentCount;
    private String uploadedAssetKey;
    private String brightcoveVideoId;
    private String series;
    private String seriesId;
    private Object plans;
    private long publishedDate;
    private String status;
    private int responseCode;
    private long duration;
    private String name;
    private int vodCount;
    private int seasonCount;
    private String thumbnailImage;
    private long videoPosition;
    private int contentOrder;
    private String seasonNumber;
    private String imageType;
    private String parentalRating;
    private String signedLangEnabled;
    private String isPodcast;
    private String is4k;
    private String isClosedCaption;
    private String isSoundTrack;
    private String isAudioDesc;
    private String seasonName;
    private CustomContentData customContent;
    private VideoDetails videoDetails;
    private LiveContent liveContent;
    private DetailVideo detailVideo;
    private Program program;
    private String customType;
    private VideoItem videoDetailsEpisodes;
    private String epgChannelId;
    private boolean isSelected;
    private List<AudioTrackListItem> audioTrackList;

    private com.tv.uscreen.yojmatv.bean_model_v1_0.videoDetailBean.ParentContent parentContent;
    private ParentContent parentContent2;


    public List<AudioTrackListItem> getAudioTrackList() {
        return audioTrackList;
    }
    public void setAudioTrackList(List<AudioTrackListItem> audioTrackList) {
        this.audioTrackList = audioTrackList;
    }

    public ParentContent getParentContent2() {
        return parentContent2;
    }

    public void setParentContent2(ParentContent parentContent2) {
        this.parentContent2 = parentContent2;
    }

    public com.tv.uscreen.yojmatv.bean_model_v1_0.videoDetailBean.ParentContent getParentContent() {
        return parentContent;
    }

    public void setParentContent(com.tv.uscreen.yojmatv.bean_model_v1_0.videoDetailBean.ParentContent parentContent) {
        this.parentContent = parentContent;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public LiveContent getLiveContent() {
        return liveContent;
    }

    public void setLiveContent(LiveContent liveContent) {
        this.liveContent = liveContent;
    }

    public String getEpgChannelId() {
        return epgChannelId;
    }

    public void setEpgChannelId(String epgChannelId) {
        this.epgChannelId = epgChannelId;
    }

    public VideoDetails getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(VideoDetails videoDetails) {
        this.videoDetails = videoDetails;
    }

    public List<Object> getVideo() {
        return video;
    }

    public void setVideo(List<Object> video) {
        this.video = video;
    }

    public String getIs4k() {
        return is4k;
    }


    public void setIs4k(String is4k) {
        this.is4k = is4k;
    }

    public String getIsClosedCaption() {
        return isClosedCaption;
    }

    public void setIsClosedCaption(String isClosedCaption) {
        this.isClosedCaption = isClosedCaption;
    }

    public List<Object> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Object> keywords) {
        this.keywords = keywords;
    }

    public String getIsSoundTrack() {
        return isSoundTrack;
    }

    public void setIsSoundTrack(String isSoundTrack) {
        this.isSoundTrack = isSoundTrack;
    }

    public String getIsAudioDesc() {
        return isAudioDesc;
    }

    public void setIsAudioDesc(String isAudioDesc) {
        this.isAudioDesc = isAudioDesc;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }

    public String getIscomingsoon() {
        return iscomingsoon;
    }

    public void setIscomingsoon(String iscomingsoon) {
        this.iscomingsoon = iscomingsoon;
    }

    public String getIsPodcast() {
        return isPodcast;
    }

    public void setIsPodcast(String isPodcast) {
        this.isPodcast = isPodcast;
    }

    public String getSignedLangEnabled() {
        return signedLangEnabled;
    }

    public void setSignedLangEnabled(String signedLangEnabled) {
        this.signedLangEnabled = signedLangEnabled;
    }


    public String getSignedLangParentRefId() {
        return signedLangParentRefId;
    }

    public void setSignedLangParentRefId(String signedLangParentRefId) {
        this.signedLangParentRefId = signedLangParentRefId;
    }

    private String signedLangParentRefId;
    private String signedLangRefId;

    public String getSignedLangRefId() {
        return signedLangRefId;
    }

    public void setSignedLangRefId(String signedLangRefId) {
        this.signedLangRefId = signedLangRefId;
    }

    public String getParentalSeriesId() {
        return parentalSeriesId;
    }

    public void setParentalSeriesId(String parentalSeriesId) {
        this.parentalSeriesId = parentalSeriesId;
    }

    public String getExternalRefId() {
        return externalRefId;
    }

    public void setExternalRefId(String externalRefId) {
        this.externalRefId = externalRefId;
    }

    private String iscomingsoon;
    private String widevineLicence;
    private String producer;
    private String quality;
    private String skipintro_endTime;
    private String skipintro_startTime;
    private String sponsors;
    private String display_tags;
    private String description_two;
    private String description_three;
    private String display_date;
    private String getWidevineURL;
    private String country;
    private String company;
    private String year;
    private String isNewS;
    private String isVIP;
    private String VastTag;
    private String trailerReferenceId;
    private String islivedrm = "false";
    private boolean isCurrentlyPlaying;


    public boolean isCurrentlyPlaying() {
        return isCurrentlyPlaying;
    }

    public void setCurrentlyPlaying(boolean currentlyPlaying) {
        isCurrentlyPlaying = currentlyPlaying;
    }

    public String getIslivedrm() {
        return islivedrm;
    }

    public void setIslivedrm(String islivedrm) {
        this.islivedrm = islivedrm;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public EnveuVideoItemBean() {
    }

    // Parsing EPG Details
    // Parsing EPG Details
    public EnveuVideoItemBean(com.enveu.client.epgListing.epgResponseNew.MediaContent itemsItem) {
        try {
            this.title = itemsItem.getTitle() == null ? "" : itemsItem.getTitle();
            this.description = itemsItem.getDescription() == null ? "" : itemsItem.getDescription();
            this.posterURL = ImageLayer.getInstance().getLivePosterImageUrl(itemsItem);
        } catch (Exception e) {
            Logger.w(e);
        }


    }


    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    //for asset details on episode page listing.......
    public EnveuVideoItemBean(Data details, boolean isIntentFromExpedition) {
        try {
            //Gson gson = new Gson();
            this.title = details.getTitle() == null ? "" : details.getTitle();
            this.description = details.getDescription() == null ? "" : details.getDescription().trim();
            this.longDescription = details.getLongDescription() == null ? "" : details.getLongDescription().trim();
            this.keywords = details.getKeywords() == null ? new ArrayList<>() : details.getKeywords();
            this.premium = details.getPremium();
            this.assetType = details.getContentType() == null ? "" : details.getContentType();
            this.audioTrackList=details.getVideo().getAudioTracks();

            if (details.getExternalRefId() != null && !details.getExternalRefId().equalsIgnoreCase("")) {
                this.externalRefId = details.getExternalRefId();
                this.setExternalRefId(externalRefId);
            }
            if (details.getParentContent() != null && details.getParentContent().getId() != null) {
                this.parentalSeriesId = String.valueOf(details.getParentContent().getId());
                Log.w("EpisodesError->", parentalSeriesId);
            }

            if (details.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (details.getVideo() != null) {
                    this.detailVideo = details.getVideo();
                }
            }

            if (isIntentFromExpedition) {
                this.posterURL = ImageLayer.getInstance().getExpeditionImageUrl(details, "img1");

            } else {
                this.posterURL = ImageLayer.getInstance().getEpisodePosterImageUrl(details, " ");

            }
            Log.w("getPlaylistVideo->", this.posterURL);


            Object customeFiled = details.getCustomData();
            LinkedTreeMap<Object, Object> t = (LinkedTreeMap) customeFiled;


            if (t.containsKey(CustomeFields.SKIP_INTRO_START)) {
                String skipintro_startTime = t.get((CustomeFields.SKIP_INTRO_START)).toString();
                this.skipintro_startTime = skipintro_startTime;

            }

            if (t.containsKey(CustomeFields.SKIP_INTRO_END)) {
                String skipintro_startTime = t.get((CustomeFields.SKIP_INTRO_END)).toString();
                this.skipintro_endTime = skipintro_startTime;

            }

            if (t.containsKey(CustomeFields.description_two)) {
                String description_two = t.get((CustomeFields.description_two)).toString();
                this.description_two = description_two;
            }
            if (t.containsKey(CustomeFields.description_three)) {
                String description_three = t.get((CustomeFields.description_three)).toString();
                this.description_three = description_three;
            }

            if (t.containsKey(CustomeFields.display_date)) {
                String description_three = t.get((CustomeFields.display_date)).toString();
                this.display_date = description_three;
            }

            if (t.containsKey(CustomeFields.display_tags)) {
                String display_tags = t.get((CustomeFields.display_tags)).toString();
                this.display_tags = display_tags;
            }

            if (t.containsKey(CustomeFields.sponsors)) {
                String sponsors = t.get((CustomeFields.sponsors)).toString();
                this.sponsors = sponsors;
            }

            if (t.containsKey(CustomeFields.Producer)) {
                String producer = t.get((CustomeFields.Producer)).toString();
                this.producer = producer;
            }


            if (t.containsKey(CustomeFields.trailerReferenceId)) {
                String trailerReferenceId = t.get((CustomeFields.trailerReferenceId)).toString();
                this.trailerReferenceId = trailerReferenceId;
            }

            if (t.containsKey(CustomeFields.quality)) {
                String quality = t.get((CustomeFields.quality)).toString();
                this.quality = quality;
            }


            this.season = "";
            this.id = details.getId();
            this.sku = details.getSku() == null ? "" : details.getSku();
            this.isNew = false;
            if (details.getVideo().getEpisodeNo() != null) {
                this.episodeNo = details.getVideo().getEpisodeNo() == null ? "" : details.getVideo().getEpisodeNo();
            }
            this.seasonNumber = details.getVideo().getSeasonNo() == null ? "" : String.valueOf(details.getVideo().getSeasonNo());

            //
            this.assetType = details.getVideo().getVideoType() == null ? "" : details.getVideo().getVideoType();
            Logger.d("assetType: Playlist" + assetType);

            this.series = "";
            if (imageType != null) {
                this.imageType = imageType;
            } else {
                this.imageType = "";
            }

            //series realated data
            if (details.getVideo().getSeasons() != null) {
                ArrayList arrayList = (ArrayList) details.getVideo().getSeasons();
                this.seasons = arrayList;
                this.seasonCount = arrayList.size();
            }


            this.duration = (long) details.getVideo().getDuration();

        } catch (Exception e) {
            Log.w("EpisodesError->", e.toString());
        }
    }

    public EnveuVideoItemBean(Item details, String imageType, String imageIdentifier, boolean isIntentFromForYou) {
        try {
            if (details.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (details.getVideo() != null) {
                    this.videoDetails = details.getVideo();
                }
            }
            this.keywords = details.getKeywords() == null ? new ArrayList<>() : details.getKeywords();
            this.title = details.getTitle() == null ? "" : details.getTitle();
            this.externalRefId = details.getExternalRefId() == null ? "" : details.getExternalRefId().trim();
            this.assetType = details.getContentType() == null ? "" : details.getContentType();

            this.description = details.getDescription() == null ? "" : details.getDescription().trim();
            this.premium = details.getPremium();
            this.parentContent2 = details.getParentContent();
            this.profilePosterUrl = ImageLayer.getInstance().getProfilePosterUrl(details);
            if (isIntentFromForYou) {
                this.posterURL = ImageLayer.getInstance().getForYouImageUrl(details, "img1");
            } else {
                this.posterURL = ImageLayer.getInstance().getEpisodePosterImageUrl(details, imageIdentifier);
            }
            this.season = "";
            this.id = details.getId();
            this.sku = details.getSku() == null ? "" : details.getSku();
            this.isNew = false;


            Object customeFiled = details.getCustomData();
            LinkedTreeMap<Object, Object> t = (LinkedTreeMap) customeFiled;


            if (t.containsKey(CustomeFields.SKIP_INTRO_START)) {
                String skipintro_startTime = t.get((CustomeFields.SKIP_INTRO_START)).toString();
                this.skipintro_startTime = skipintro_startTime;

            }

            if (t.containsKey(CustomeFields.SKIP_INTRO_END)) {
                String skipintro_endTime = t.get((CustomeFields.SKIP_INTRO_END)).toString();
                this.skipintro_endTime = skipintro_endTime;

            }

            if (t.containsKey(CustomeFields.sponsors)) {
                String sponsors = t.get((CustomeFields.sponsors)).toString();
                this.sponsors = sponsors;
            }

            if (t.containsKey(CustomeFields.description_two)) {
                String description_two = t.get((CustomeFields.description_two)).toString();
                this.description_two = description_two;
            }
            if (t.containsKey(CustomeFields.description_three)) {
                String description_three = t.get((CustomeFields.description_three)).toString();
                this.description_three = description_three;
            }
            if (t.containsKey(CustomeFields.display_date)) {
                String description_three = t.get((CustomeFields.display_date)).toString();
                this.display_date = description_three;
            }
            if (t.containsKey(CustomeFields.Producer)) {
                String producer = t.get((CustomeFields.Producer)).toString();
                this.producer = producer;
            }

            if (t.containsKey(CustomeFields.trailerReferenceId)) {
                String trailerReferenceId = t.get((CustomeFields.trailerReferenceId)).toString();
                this.trailerReferenceId = trailerReferenceId;
            }

            if (t.containsKey(CustomeFields.quality)) {
                String quality = t.get((CustomeFields.quality)).toString();
                this.quality = quality;
            }

            //
            /// this.assetType = details.getVideo().getVideoType() == null ? "" : details.getVideo().getVideoType();
            Logger.d("assetType: Playlist" + assetType);

            this.series = "";
            if (imageType != null) {
                this.imageType = imageType;
            } else {
                this.imageType = "";
            }

            //series realated data
            if (null != details.getVideo().getSeasons()) {
                ArrayList arrayList = (ArrayList) details.getVideo().getSeasons();
                this.seasonCount = arrayList.size();
            }


            this.duration = (long) details.getVideo().getDuration();
            this.episodeNo = details.getVideo().getEpisodeNo() == null ? "" : details.getVideo().getEpisodeNo();

        } catch (Exception e) {
            Log.w("EpisodesError->", e.toString());
        }
    }


    //description page - single asset parsing
    public EnveuVideoItemBean(EnveuVideoDetailsBean details) {
        try {
            this.title = details.getData().getTitle() == null ? "" : details.getData().getTitle();
            this.description = details.getData().getDescription() == null ? "" : details.getData().getDescription().trim();
            this.longDescription = details.getData().getLongDescription() == null ? "" : details.getData().getLongDescription().trim();
            this.externalRefId = details.getData().getExternalRefId() == null ? "" : details.getData().getExternalRefId().trim();
            this.assetGenres = details.getData().getGenres() == null ? new ArrayList<>() : details.getData().getGenres();
            this.assetCast = details.getData().getCast() == null ? new ArrayList<>() : details.getData().getCast();
            this.contentProvider = details.getData().getContentProvider() == null ? "" : details.getData().getContentProvider();
            this.premium = details.getData().getPremium();
            this.keywords = details.getData().getKeywords() == null ? new ArrayList<>() : details.getData().getKeywords();

            if (details.getData().getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (details.getData().getVideo() != null) {
                    this.videoDetails = details.getData().getVideo();
                }
            }
            this.posterURL = ImageLayer.getInstance().getPosterImageUrl(details.getData());
            Logger.e("duration", String.valueOf(duration));
            this.season = "";
            if (details.getData().getLinkedContent() != null) {
                this.seriesId = String.valueOf(details.getData().getLinkedContent().getId());
            }
            this.sku = details.getData().getSku() == null ? "" : details.getData().getSku();
            this.id = details.getData().getId();
            this.isNew = false;
            this.episodeNo = details.getData().getEpisodeNumber() == null ? "" : details.getData().getEpisodeNumber();
            this.assetType = details.getData().getContentType() == null ? "" : details.getData().getContentType();
            this.brightcoveVideoId = details.getData().getBrightcoveContentId() == null ? "" : details.getData().getBrightcoveContentId();
            this.series = String.valueOf(details.getData().getId());
            this.status = details.getData().getStatus() == null ? "" : details.getData().getStatus();


            Object customeFiled = details.getData().getCustomFields();
            LinkedTreeMap<Object, Object> t = (LinkedTreeMap) customeFiled;

            Object customedata = details.getData().getCustomData();
            Logger.e("customedata", customedata.toString());
            LinkedTreeMap<Object, Object> t1 = (LinkedTreeMap) customedata;

            if (t1.containsKey(CustomeFields.Producer)) {
                String producer = t1.get((CustomeFields.Producer)).toString();
                this.producer = producer;

            }
            if (t1.containsKey(CustomeFields.SKIP_INTRO_START)) {
                String skipintro_startTime = t1.get((CustomeFields.SKIP_INTRO_START)).toString();
                this.skipintro_startTime = skipintro_startTime;

            }

            if (t1.containsKey(CustomeFields.SKIP_INTRO_END)) {
                String skipintro_endTime = t1.get((CustomeFields.SKIP_INTRO_END)).toString();
                this.skipintro_endTime = skipintro_endTime;

            }

            if (t1.containsKey(CustomeFields.display_tags)) {
                String display_tags = t1.get((CustomeFields.display_tags)).toString();
                this.display_tags = display_tags;
            }

            if (t1.containsKey(CustomeFields.trailerReferenceId)) {
                String trailerRef = t1.get((CustomeFields.trailerReferenceId)).toString();
                this.trailerReferenceId = trailerRef;
            }

            if (t1.containsKey(CustomeFields.quality)) {
                String quality = t1.get((CustomeFields.quality)).toString();
                this.quality = quality;

            }

            if (t1.containsKey(CustomeFields.sponsors)) {
                String sponsors = t1.get((CustomeFields.sponsors)).toString();
                this.sponsors = sponsors;
            }

            if (t1.containsKey(CustomeFields.trailerReferenceId)) {
                String trailerRef = t1.get((CustomeFields.trailerReferenceId)).toString();
                this.trailerReferenceId = trailerRef;
            }
            this.longDescription = details.getData().getLongDescription() == null ? "" : details.getData().getLongDescription().toString().trim();

            //series realated data
            this.vodCount = 0;
            this.seasonNumber = details.getData().getSeasonNumber() == null ? "" : details.getData().getSeasonNumber().toString().replaceAll("\\.0*$", "");
            if (details.getData().getSeasons() != null) {
                ArrayList arrayList = (ArrayList) details.getData().getSeasons();
                this.seasons = arrayList;
                this.seasonCount = arrayList.size();
            }

            this.duration = details.getData().getVideo().getDuration();

        } catch (Exception e) {
            Logger.e("parsing error", e.getMessage());
            Logger.w(e);
        }

    }

    //for asset details.......
    public EnveuVideoItemBean(VideosItem details, int contentOrder, String imageType, String imageIdentifier, boolean type, boolean isIntentRelatedContent) {
        try {
            this.isSelected = false;
            if (details.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (details.getVideo() != null) {
                    this.videoDetails = details.getVideo();
                }
            } else {
                if (details.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                    if (details.getCustomContent() != null) {
                        this.customType = details.getCustomContent().getCustomType();
                    }
                }
            }

            if (details.getExternalRefId() != null && !details.getExternalRefId().equalsIgnoreCase("")) {
                this.externalRefId = details.getExternalRefId();
                this.setExternalRefId(externalRefId);
            }

            if (details.getContentType().equalsIgnoreCase(AppConstants.LIVE)) {
                if (details.getLiveContent() != null) {
                    this.liveContent = details.getLiveContent();
                    this.setLiveContent(liveContent);
                    //  this.epgChannelId = details.getLiveContent().getEpgChannelId();
                }

                this.posterURL = ImageLayer.getInstance().getLiveImageUrl(details);

            } else {
                if (isIntentRelatedContent) {
                    this.posterURL = ImageLayer.getInstance().getRelatedContentImageUrl(details, "img1");
                } else {
                    if (type == true) {
                        this.posterURL = ImageLayer.getInstance().getPopularSearchImageUrl(details);
                    } else {
                        this.posterURL = ImageLayer.getInstance().getPosterImageUrl(details, imageIdentifier);
                    }
                }

                Log.w("getPlaylistVideo->", this.posterURL);
            }
            //Gson gson = new Gson();
            this.title = details.getTitle() == null ? "" : details.getTitle();
            this.assetType = details.getContentType() == null ? "" : details.getContentType();
            this.parentContent = details.getParentContent();
            this.profilePosterUrl = ImageLayer.getInstance().getProfilePosterUrl(details);
            // String json = gson.toJson(details.getPlaylistVideoDetails().getImages());
            // Log.w("getPlaylistVideo->",json);
            this.description = details.getDescription() == null ? "" : details.getDescription().trim();
            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();
            //  this.svod = details.getSvod() == null ? "" : details.getSvod();
            this.contentProvider = details.getContentProvider() == null ? "" : details.getContentProvider();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();
            this.premium = details.getPremium();
            if (imageType != null) {
                this.imageType = imageType;
            } else {
            }

            Object customeFiled = details.getCustomFields();
            LinkedTreeMap<Object, Object> t = (LinkedTreeMap) customeFiled;

            Object customedata = details.getCustomData();
            LinkedTreeMap<Object, Object> t1 = (LinkedTreeMap) customedata;

            if (t1.containsKey(CustomeFields.SKIP_INTRO_START)) {
                String skipintro_startTime = t1.get((CustomeFields.SKIP_INTRO_START)).toString();
                this.skipintro_startTime = skipintro_startTime;

            }

            if (t1.containsKey(CustomeFields.SKIP_INTRO_END)) {
                String skipintro_endTime = t1.get((CustomeFields.SKIP_INTRO_END)).toString();
                this.skipintro_endTime = skipintro_endTime;

            }

            if (t1.containsKey(CustomeFields.display_tags)) {
                String display_tags = t1.get((CustomeFields.display_tags)).toString();
                this.display_tags = display_tags;
            }

            if (t1.containsKey(CustomeFields.Producer)) {
                String producer = t1.get((CustomeFields.Producer)).toString();
                this.producer = producer;

            }
            if (t1.containsKey(CustomeFields.trailerReferenceId)) {
                String trailerReferenceId = t1.get((CustomeFields.trailerReferenceId)).toString();
                this.trailerReferenceId = trailerReferenceId;
            }

            this.longDescription = details.getLongDescription() == null ? "" : details.getLongDescription().toString().trim();


            //series realated data
            if (details.getSeasons() != null) {
                ArrayList arrayList = (ArrayList) details.getSeasons();
                this.seasonCount = arrayList.size();
            }
            this.id = details.getId();
            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.season = "";
            this.sku = details.getSku() == null ? "" : details.getSku();
            this.isNew = false;
            this.episodeNo = details.getEpisodeNumber() == null ? "" : details.getEpisodeNumber();
            this.brightcoveVideoId = details.getBrightcoveContentId() == null ? "" : details.getBrightcoveContentId();
            this.series = "";
            this.status = details.getStatus() == null ? "" : details.getStatus();
            this.contentOrder = contentOrder;
            this.duration = (long) details.getVideo().getDuration();
        } catch (Exception e) {
            Logger.w(e);
        }
    }

    //for continue watching.......
    public EnveuVideoItemBean(DataItem details) {

        try {

            //  this.svod = details.getSvod() == null ? "" : details.getSvod();
            this.title = details.getTitle() == null ? "" : details.getTitle();
            this.description = details.getDescription() == null ? "" : details.getDescription().trim();
            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();
            this.assetType = details.getContentType() == null ? "" : details.getContentType();
            this.contentProvider = details.getContentProvider() == null ? "" : details.getContentProvider();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();
            this.premium = details.getPremium();

            if (details.getExternalRefId() != null && !details.getExternalRefId().equalsIgnoreCase("")) {
                this.externalRefId = details.getExternalRefId();
                this.setExternalRefId(externalRefId);
            }


            if (details.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (details.getVideo() != null) {
                    this.videoDetails = details.getVideo();
                    Gson gson = new Gson();
                    String tmp = gson.toJson(videoDetails);
                    this.setVideoDetails(videoDetails);
                }
            }
            this.posterURL = ImageLayer.getInstance().getPosterImageUrl(details);
            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.season = "";
            this.id = details.getId();
            this.sku = details.getSku() == null ? "" : details.getSku();
            this.isNew = false;
            this.episodeNo = details.getEpisodeNumber() == null ? "" : details.getEpisodeNumber();
            this.assetType = details.getContentType() == null ? "" : details.getContentType();
            this.brightcoveVideoId = details.getBrightcoveContentId() == null ? "" : details.getBrightcoveContentId();
            this.series = "";
            this.status = details.getStatus() == null ? "" : details.getStatus();
            if (details.getPosition() != null) {
                this.videoPosition = details.getPosition();
            }

            this.contentOrder = contentOrder;
            if (imageType != null) {
                this.imageType = imageType;
            } else {
                this.imageType = "";
            }

            Object customeFiled = details.getCustomFields();
            LinkedTreeMap<Object, Object> t = (LinkedTreeMap) customeFiled;


            this.longDescription = details.getLongDescription() == null ? "" : details.getLongDescription().toString().trim();

            if (details.getSeasons() != null) {
                ArrayList arrayList = (ArrayList) details.getSeasons();
                this.seasonCount = arrayList.size();
            }
            this.duration = (long) details.getVideo().getDuration();
            Logger.e("duration", String.valueOf(duration));
        } catch (Exception ignored) {
            Logger.e("ContinueWatching", ignored.getMessage());
        }

    }

    //for search data.......
    public EnveuVideoItemBean(ItemsItem details) {

        try {

            this.title = details.getTitle() == null ? "" : details.getTitle();
            this.description = details.getDescription() == null ? "" : details.getDescription().trim();
            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();

            this.contentProvider = details.getContentProvider() == null ? "" : details.getContentProvider();
            this.assetCast = details.getCast() == null ? new ArrayList<>() : details.getCast();

            if (details.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (details.getVideo() != null) {
                    this.videoDetails = details.getVideo();
                    this.setVideoDetails(videoDetails);
                }
            }  else {
                if (details.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                    if (details.getCustomContent() != null) {
                        this.customType = details.getCustomContent().getCustomType();
                    }
                }
            }

            if (details.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                this.posterURL = ImageLayer.getInstance().getPosterImageUrl1(details);

            } else if (details.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                this.posterURL = ImageLayer.getInstance().getPosterImageUrl1(details);
            }

            if (details.getExternalRefId() != null && !details.getExternalRefId().equalsIgnoreCase("")) {
                this.externalRefId = details.getExternalRefId();
                this.setExternalRefId(externalRefId);
            }
            this.premium = details.getPremium();

            this.assetGenres = details.getGenres() == null ? new ArrayList<>() : details.getGenres();
            this.season = "";
            this.id = details.getId();
            this.sku = details.getSku() == null ? "" : details.getSku();
            this.isNew = false;
            this.episodeNo = details.getEpisodeNumber() == null ? "" : details.getEpisodeNumber();
            this.assetType = details.getContentType() == null ? "" : details.getContentType();
            this.brightcoveVideoId = details.getBrightcoveContentId() == null ? "" : details.getBrightcoveContentId();
            this.series = "";
            this.status = details.getStatus() == null ? "" : details.getStatus();
            this.contentOrder = contentOrder;
            if (imageType != null) {
                this.imageType = imageType;
            }



            Object customeData = details.getCustomData();
            LinkedTreeMap<Object, Object> t = (LinkedTreeMap) customeData;


            if (t.containsKey(CustomeFields.sponsors)) {
                String sponsors = t.get((CustomeFields.sponsors)).toString();
                this.sponsors = sponsors;
            }

            if (t.containsKey(CustomeFields.Producer)) {
                String producer = t.get((CustomeFields.Producer)).toString();
                this.producer = producer;
            }


            if (t.containsKey(CustomeFields.trailerReferenceId)) {
                String trailerReferenceId = t.get((CustomeFields.trailerReferenceId)).toString();
                this.trailerReferenceId = trailerReferenceId;
            }

            if (t.containsKey(CustomeFields.quality)) {
                String quality = t.get((CustomeFields.quality)).toString();
                this.quality = quality;
            }

            this.longDescription = details.getLongDescription() == null ? "" : details.getLongDescription().toString().trim();

            //series realated data
            if (details.getSeasons() != null) {
                ArrayList arrayList = details.getSeasons();
                this.seasonCount = arrayList.size();
            }
            //   this.duration = (long) details.getVideo().getDuration();
        } catch (Exception e) {
            Logger.w("search" + e);
        }
    }

    public String getDisplay_tags() {
        return display_tags;
    }

    public void setDisplay_tags(String display_tags) {
        this.display_tags = display_tags;
    }

    public String getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public int getContentOrder() {
        return contentOrder;
    }

    public void setContentOrder(int contentOrder) {
        this.contentOrder = contentOrder;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public ArrayList getSeasons() {
        return seasons;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVodCount() {
        return vodCount;
    }

    public void setVodCount(int vodCount) {
        this.vodCount = vodCount;
    }

    public int getSeasonCount() {
        return seasonCount;
    }

    public void setSeasonCount(int seasonCount) {
        this.seasonCount = seasonCount;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

   /* public Object getVastTag() {
        return vastTag;
    }

    public void setVastTag(Object vastTag) {
        this.vastTag = vastTag;
    }*/

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<String> getAssetKeywords() {
        return assetKeywords;
    }

    public void setAssetKeywords(List<String> assetKeywords) {
        this.assetKeywords = assetKeywords;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getSvod() {
        return svod;
    }

    public void setSvod(Object svod) {
        this.svod = svod;
    }

    public String getContentProvider() {
        return contentProvider;
    }

    public void setContentProvider(String contentProvider) {
        this.contentProvider = contentProvider;
    }

    public List<String> getAssetCast() {
        return assetCast;
    }

    public void setAssetCast(List<String> assetCast) {
        this.assetCast = assetCast;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    public Object getPrice() {
        return price;
    }

    public void setPrice(Object price) {
        this.price = price;
    }

    public List<String> getAssetGenres() {
        return assetGenres;
    }

    public void setAssetGenres(List<String> assetGenres) {
        this.assetGenres = assetGenres;
    }

    public String getSeason() {
        return season;
    }

    public String getSkipintro_endTime() {
        return skipintro_endTime;
    }

    public String getSkipintro_startTime() {
        return skipintro_startTime;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public boolean isNew() {
        return isNew;
    }

    //	public void setThumbnailURL(String thumbnailURL){
//		this.thumbnailURL = thumbnailURL;
//	}
//
//	public String getThumbnailURL(){
//		return thumbnailURL;
//	}
//
    public void setNew(boolean aNew) {
        this.isNew = aNew;
    }

    public Object getTvod() {
        return tvod;
    }

    public void setTvod(Object tvod) {
        this.tvod = tvod;
    }

    public Object getEpisodeNo() {
        return episodeNo;
    }

    public void setEpisodeNo(Object episodeNo) {
        this.episodeNo = episodeNo;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getUploadedAssetKey() {
        return uploadedAssetKey;
    }

    public void setUploadedAssetKey(String uploadedAssetKey) {
        this.uploadedAssetKey = uploadedAssetKey;
    }

    public String getBrightcoveVideoId() {
        return brightcoveVideoId;
    }

    public void setBrightcoveVideoId(String brightcoveVideoId) {
        this.brightcoveVideoId = brightcoveVideoId;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }

    public Object getPlans() {
        return plans;
    }

    public void setPlans(Object plans) {
        this.plans = plans;
    }

    public long getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(long publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getVideoPosition() {
        return videoPosition;
    }

    public void setVideoPosition(long videoPosition) {
        this.videoPosition = videoPosition;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageType() {
        return imageType;
    }

    public String getParentalRating() {
        return parentalRating;
    }

    public void setParentalRating(String parentalRating) {
        this.parentalRating = parentalRating;
    }

    public String getComingSoon() {
        return iscomingsoon;
    }

    public void setComingSoon(String iscomingsoon) {
        this.iscomingsoon = iscomingsoon;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }


    public String getTrailerReferenceId() {
        return trailerReferenceId;
    }

    public void setTrailerReferenceId(String trailerReferenceId) {
        this.trailerReferenceId = trailerReferenceId;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getSponsors() {
        return sponsors;
    }

    public void setSponsors(String sponsors) {
        this.sponsors = sponsors;
    }

    public String getIsNewS() {
        return isNewS;
    }

    public void setIsNewS(String isNewS) {
        this.isNewS = isNewS;
    }

    public String getIsVIP() {
        return isVIP;
    }

    public void setIsVIP(String isVIP) {
        this.isVIP = isVIP;
    }

    public void setVastTag(String VastTag) {
        this.VastTag = VastTag;
    }

    public String getVastTag() {
        return VastTag;
    }

    public String getWidevineLicence() {
        return widevineLicence;
    }

    public void setWidevineLicence(String widevineLicence) {
        this.widevineLicence = widevineLicence;
    }

    public CustomContentData getCustomContent() {
        return customContent;
    }

    public void setCustomContent(CustomContentData customContent) {
        this.customContent = customContent;
    }

    public String getGetWidevineURL() {
        return getWidevineURL;
    }

    public void setGetWidevineURL(String getWidevineURL) {
        this.getWidevineURL = getWidevineURL;
    }

    public String getDescription_two() {
        return description_two;
    }

    public void setDescription_two(String description_two) {
        this.description_two = description_two;
    }

    public String getDescription_three() {
        return description_three;
    }

    public void setDescription_three(String description_three) {
        this.description_three = description_three;
    }

    public String getProfilePosterUrl() {
        return profilePosterUrl;
    }

    public void setProfilePosterUrl(String profilePosterUrl) {
        this.profilePosterUrl = profilePosterUrl;
    }

    @Override
    public String toString() {
        return
                "VideosItem{" +
                        "vastTag = '" + VastTag + '\'' +
                        ",description = '" + description + '\'' +
                        ",assetKeywords = '" + assetKeywords + '\'' +
                        ",likeCount = '" + likeCount + '\'' +
                        ",title = '" + title + '\'' +
                        ",skipintro_startTime = '" + skipintro_startTime + '\'' +
                        ",skipintro_endTime = '" + skipintro_endTime + '\'' +
                        ",svod = '" + svod + '\'' +
                        ",contentProvider = '" + contentProvider + '\'' +
                        ",assetCast = '" + assetCast + '\'' +
                        ",premium = '" + premium + '\'' +
                        ",isCurrentlyPlaying = '" + isCurrentlyPlaying + '\'' +
                        ",posterURL = '" + posterURL + '\'' +
                        ",profilePosterUrl = '" + profilePosterUrl + '\'' +
                        ",price = '" + price + '\'' +
                        ",assetGenres = '" + assetGenres + '\'' +
                        ",season = '" + season + '\'' +
                        ",id = '" + id + '\'' +
                        ",sku = '" + sku + '\'' +
                        ",new = '" + isNew + '\'' +
                        ",tvod = '" + tvod + '\'' +
                        ",episodeNo = '" + episodeNo + '\'' +
                        ",assetType = '" + assetType + '\'' +
                        ",commentCount = '" + commentCount + '\'' +
                        ",uploadedAssetKey = '" + uploadedAssetKey + '\'' +
                        ",brightcoveVideoId = '" + brightcoveVideoId + '\'' +
                        ",series = '" + series + '\'' +
                        ",plans = '" + plans + '\'' +
                        ",publishedDate = '" + publishedDate + '\'' +
                        ",startTime = '" + startTime + '\'' +
                        ",status = '" + status + '\'' +
                        ",trailerReferenceId = '" + trailerReferenceId + '\'' +
                        ",video = '" + videoDetails + '\'' +
                        ",video = '" + videoDetailsEpisodes + '\'' +
                        ",customContent = '" + customContent + '\'' +
                        ",customType = '" + customType + '\'' +
                        ",parentContent = '" + parentContent + '\'' +
                        ",parentContent2 = '" + parentContent2 + '\'' +
                        ",liveContent = '" + liveContent + '\'' +
                        ",externalRefId = '" + externalRefId + '\'' +
                        "}";
    }


    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public Drawable getVipImageDrawable() {
        OttApplication application = OttApplication.getInstance();
        Drawable drawable = null;
        try {
            //  drawable = ContextCompat.getDrawable(application, R.drawable.vip_icon_120);
        } catch (Exception e) {
            Logger.w(e);
        }
        return drawable;
    }

    public Drawable getNewSeriesImageDrawable() {
        OttApplication application = OttApplication.getInstance();
        try {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                return ContextCompat.getDrawable(application, R.drawable.series_icon_120);
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")) {
                return ContextCompat.getDrawable(application, R.drawable.series_thai_icon);
            } else {
                return ContextCompat.getDrawable(application, R.drawable.series_icon_120);
            }
        } catch (Exception e) {
            return ContextCompat.getDrawable(application, R.drawable.series_icon_120);
        }
    }

    public Drawable getEpisodeImageDrawable() {
        OttApplication application = OttApplication.getInstance();
        try {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                return ContextCompat.getDrawable(application, R.drawable.episode_icon_120);
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")) {
                return ContextCompat.getDrawable(application, R.drawable.episode_thai_icon);
            } else {
                return ContextCompat.getDrawable(application, R.drawable.episode_icon_120);
            }
        } catch (Exception e) {
            return ContextCompat.getDrawable(application, R.drawable.episode_icon_120);
        }
    }

    public Drawable getNewMoviesDrawable() {
        OttApplication application = OttApplication.getInstance();
        try {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                return ContextCompat.getDrawable(application, R.drawable.new_movie_120);
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")) {
                return ContextCompat.getDrawable(application, R.drawable.new_movie_thai120);
            } else {
                return ContextCompat.getDrawable(application, R.drawable.new_movie_120);
            }
        } catch (Exception e) {
            return ContextCompat.getDrawable(application, R.drawable.new_movie_120);
        }
    }

}