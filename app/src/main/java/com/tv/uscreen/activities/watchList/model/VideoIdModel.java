package com.tv.uscreen.activities.watchList.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class VideoIdModel {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("debugMessage")
    @Expose
    private Object debugMessage;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Object getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(Object debugMessage) {
        this.debugMessage = debugMessage;
    }


    public class Datum {

        @SerializedName("dateCreated")
        @Expose
        private Long dateCreated;
        @SerializedName("lastUpdated")
        @Expose
        private Long lastUpdated;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("contentType")
        @Expose
        private String contentType;
        @SerializedName("keywords")
        @Expose
        private List<String> keywords = null;
        @SerializedName("premium")
        @Expose
        private Boolean premium;
        @SerializedName("sku")
        @Expose
        private String sku;
        @SerializedName("publishedDate")
        @Expose
        private Long publishedDate;
        @SerializedName("customData")
        @Expose
        private CustomData customData;
        @SerializedName("parentalRating")
        @Expose
        private Object parentalRating;
        @SerializedName("parentContent")
        @Expose
        private Object parentContent;
        @SerializedName("video")
        @Expose
        private Video video;

        @SerializedName("externalRefId")
        @Expose
        private String externalRefId;

        public String getExternalRefId() {
            return externalRefId;
        }

        public void setExternalRefId(String externalRefId) {
            this.externalRefId = externalRefId;
        }

        public Long getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(Long dateCreated) {
            this.dateCreated = dateCreated;
        }

        public Long getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(Long lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public Boolean getPremium() {
            return premium;
        }

        public void setPremium(Boolean premium) {
            this.premium = premium;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public Long getPublishedDate() {
            return publishedDate;
        }

        public void setPublishedDate(Long publishedDate) {
            this.publishedDate = publishedDate;
        }

        public CustomData getCustomData() {
            return customData;
        }

        public void setCustomData(CustomData customData) {
            this.customData = customData;
        }

        public Object getParentalRating() {
            return parentalRating;
        }

        public void setParentalRating(Object parentalRating) {
            this.parentalRating = parentalRating;
        }

        public Object getParentContent() {
            return parentContent;
        }

        public void setParentContent(Object parentContent) {
            this.parentContent = parentContent;
        }

        public Video getVideo() {
            return video;
        }

        public void setVideo(Video video) {
            this.video = video;
        }

        public class CustomData {

        }


        public class Video {

            @SerializedName("cuePoints")
            @Expose
            private Object cuePoints;
            @SerializedName("seasonNo")
            @Expose
            private Object seasonNo;
            @SerializedName("episodeNo")
            @Expose
            private Object episodeNo;
            @SerializedName("chapterNo")
            @Expose
            private Object chapterNo;
            @SerializedName("duration")
            @Expose
            private Integer duration;
            @SerializedName("videoType")
            @Expose
            private String videoType;
            @SerializedName("seasons")
            @Expose
            private List<Object> seasons = null;
            @SerializedName("offlineEnabled")
            @Expose
            private Object offlineEnabled;
            @SerializedName("drmDisabled")
            @Expose
            private Object drmDisabled;
            @SerializedName("textTracks")
            @Expose
            private Object textTracks;
            @SerializedName("images")
            @Expose
            private List<Image> images = null;

            public Object getCuePoints() {
                return cuePoints;
            }

            public void setCuePoints(Object cuePoints) {
                this.cuePoints = cuePoints;
            }

            public Object getSeasonNo() {
                return seasonNo;
            }

            public void setSeasonNo(Object seasonNo) {
                this.seasonNo = seasonNo;
            }

            public Object getEpisodeNo() {
                return episodeNo;
            }

            public void setEpisodeNo(Object episodeNo) {
                this.episodeNo = episodeNo;
            }

            public Object getChapterNo() {
                return chapterNo;
            }

            public void setChapterNo(Object chapterNo) {
                this.chapterNo = chapterNo;
            }

            public Integer getDuration() {
                return duration;
            }

            public void setDuration(Integer duration) {
                this.duration = duration;
            }

            public String getVideoType() {
                return videoType;
            }

            public void setVideoType(String videoType) {
                this.videoType = videoType;
            }

            public List<Object> getSeasons() {
                return seasons;
            }

            public void setSeasons(List<Object> seasons) {
                this.seasons = seasons;
            }

            public Object getOfflineEnabled() {
                return offlineEnabled;
            }

            public void setOfflineEnabled(Object offlineEnabled) {
                this.offlineEnabled = offlineEnabled;
            }

            public Object getDrmDisabled() {
                return drmDisabled;
            }

            public void setDrmDisabled(Object drmDisabled) {
                this.drmDisabled = drmDisabled;
            }

            public Object getTextTracks() {
                return textTracks;
            }

            public void setTextTracks(Object textTracks) {
                this.textTracks = textTracks;
            }

            public List<Image> getImages() {
                return images;
            }

            public void setImages(List<Image> images) {
                this.images = images;
            }


            public class Image {

                @SerializedName("id")
                @Expose
                private Integer id;
                @SerializedName("imageKey")
                @Expose
                private String imageKey;
                @SerializedName("src")
                @Expose
                private String src;
                @SerializedName("height")
                @Expose
                private Double height;
                @SerializedName("width")
                @Expose
                private Double width;
                @SerializedName("colorPalette")
                @Expose
                private List<String> colorPalette = null;
                @SerializedName("dominantColor")
                @Expose
                private String dominantColor;
                @SerializedName("tag")
                @Expose
                private Object tag;

                @SerializedName("isDefault")
                @Expose
                private boolean isDefault;

                public boolean isDefault() {
                    return isDefault;
                }

                public void setDefault(boolean aDefault) {
                    isDefault = aDefault;
                }

                public Integer getId() {
                    return id;
                }

                public void setId(Integer id) {
                    this.id = id;
                }

                public String getImageKey() {
                    return imageKey;
                }

                public void setImageKey(String imageKey) {
                    this.imageKey = imageKey;
                }

                public String getSrc() {
                    return src;
                }

                public void setSrc(String src) {
                    this.src = src;
                }

                public Double getHeight() {
                    return height;
                }

                public void setHeight(Double height) {
                    this.height = height;
                }

                public Double getWidth() {
                    return width;
                }

                public void setWidth(Double width) {
                    this.width = width;
                }

                public List<String> getColorPalette() {
                    return colorPalette;
                }

                public void setColorPalette(List<String> colorPalette) {
                    this.colorPalette = colorPalette;
                }

                public String getDominantColor() {
                    return dominantColor;
                }

                public void setDominantColor(String dominantColor) {
                    this.dominantColor = dominantColor;
                }

                public Object getTag() {
                    return tag;
                }

                public void setTag(Object tag) {
                    this.tag = tag;
                }

            }
        }

    }

}