package com.breadgangtvnetwork.beanModel.enveuCommonRailData;

import android.app.Activity;
import android.util.Log;

import com.breadgangtvnetwork.beanModelV3.continueWatching.DataItem;
import com.breadgangtvnetwork.beanModelV3.playListModelV2.ItemsItem;
import com.breadgangtvnetwork.beanModelV3.playListModelV2.PlayListDetailsResponse;
import com.breadgangtvnetwork.beanModelV3.playListModelV2.VideosItem;
import com.breadgangtvnetwork.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.breadgangtvnetwork.beanModelV3.videoDetailsV2.EnveuVideoDetails;
import com.breadgangtvnetwork.bean_model_v1_0.listAll.Item;
import com.breadgangtvnetwork.bean_model_v1_0.listAll.ListAllData;
import com.breadgangtvnetwork.bean_model_v1_0.listAll.VideoItem;
import com.breadgangtvnetwork.callbacks.commonCallbacks.CommonApiCallBack;
import com.breadgangtvnetwork.layersV2.VideoDetailLayer;
import com.breadgangtvnetwork.utils.Logger;
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod;
import com.breadgangtvnetwork.utils.config.ImageLayer;
import com.breadgangtvnetwork.utils.constants.AppConstants;
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.enveu.client.enums.ImageSource;
import com.enveu.client.enums.ImageType;
import com.enveu.client.enums.LandingPageType;
import com.enveu.client.enums.Layouts;
import com.enveu.client.enums.WidgetImageType;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RailCommonData {

    private int maxContent;
    private String displayName;
    private String playlistType;
    private List<EnveuVideoItemBean> enveuVideoItemBeans = new ArrayList<>();
    private VideoItem videoItemDetail;
    private int seasonNumber;
    private String seasonName;
    private int railType;
    private BaseCategory screenWidget;
    private boolean status;
    private int pageTotal;
    private int layoutType = 0;
    private String searchKey;
    private int totalCount = 0;
    private String assetType;
    private boolean isSeries = false;
    private boolean isContinueWatching=false;
    private boolean isAd=false;


    // for playlist constructor
    public RailCommonData(PlayListDetailsResponse playListDetailsResponse, BaseCategory screenWidget, boolean type) {
        this.screenWidget = screenWidget;
        /*type = false calling for home playlist data -->> type = true calling from more listing*/
        if (!type) {
            setBrighcoveVideos(playListDetailsResponse.getItems(), screenWidget.getContentImageType(),screenWidget.getImageIdentifier(),type);
            isSeries = false;
            setRailType(screenWidget.getLayout(), screenWidget.getContentImageType());
        } else {
            setBrighcoveVideos(playListDetailsResponse.getItems(), ImageType.LDS.name(),"",type);
            isSeries = false;
        }
    }

    // for episode listing constructor
    public RailCommonData(PlayListDetailsResponse playListDetailsResponse,boolean isIntentRelatedContent){
        setEpisodesList(playListDetailsResponse.getItems(), ImageType.LDS.name(),isIntentRelatedContent);
        isSeries = false;
    }

    public RailCommonData(ListAllData playListDetailsResponse,boolean isIntentFromForYou){
        setListAllEpisodesList(playListDetailsResponse.getItems(), ImageType.LDS.name(),isIntentFromForYou);
        isSeries = false;
    }



    private void setListAllEpisodesList(List<Item> videos, String imageType,boolean isIntentFromForYou) {
        if (videos != null && !videos.isEmpty()) {
            final RailCommonData railCommonData = this;
            for (int i=0;i<videos.size();i++){
                Item videoItem= videos.get(i);

                EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(videoItem,imageType,"",isIntentFromForYou);
                try {
                    if (videoItem!=null){
                        if (videoItem.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                            if (videoItem.getVideo().getSeasonNo()!=null){
                                Double seasonNumber1= (Double) videoItem.getVideo().getSeasonNo();
                                int seasonNumber = seasonNumber1.intValue();
                                railCommonData.setSeasonNumber(seasonNumber);
                            }
                        }
                        enveuVideoItemBean.setVodCount(videos.size());
                        enveuVideoItemBeans.add(enveuVideoItemBean);
                    }

                } catch (Exception e){
               Logger.w(e);
                }
            }
            try {
                if (enveuVideoItemBeans!=null && !enveuVideoItemBeans.isEmpty()){
                    enveuVideoItemBeans.sort((o1, o2) -> {
                        if (o1.getEpisodeNo() != null && o1.getEpisodeNo() instanceof Double) {
                            return Double.compare((Double) o1.getEpisodeNo(), (Double) o2.getEpisodeNo());
                        } else {
                            return 0;
                        }
                    });
                }
            }catch (Exception e){
                Logger.w(e);
            }

        }
    }



    public RailCommonData(BaseCategory screenWidget) {
        this.screenWidget = screenWidget;
        setRailType(screenWidget.getLayout(), screenWidget.getContentImageType());
    }


    public RailCommonData() {

    }

    public void getHeroRailCommonData(BaseCategory screenWidget, Activity activity, CommonApiCallBack commonApiCallBack) {
        EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean();
        this.screenWidget = screenWidget;
        setRailType(screenWidget.getLayout(), screenWidget.getContentImageType());
        if (screenWidget.getImageSource() != null && screenWidget.getImageSource().equalsIgnoreCase(ImageSource.MNL.name())) {
            setManualTypeHero(enveuVideoItemBean, commonApiCallBack);
        } else {
            setAssetTypeHero(enveuVideoItemBean, commonApiCallBack);
        }
    }

    private void setManualTypeHero(EnveuVideoItemBean enveuVideoItemBean, CommonApiCallBack commonApiCallBack) {
        enveuVideoItemBean.setPosterURL(screenWidget.getImageURL());
        setRailType(screenWidget.getLayout(), screenWidget.getContentImageType());
        if (screenWidget.getLandingPageType().equals(LandingPageType.PDF.name()) || screenWidget.getLandingPageType().equals(LandingPageType.PLT.name()) || screenWidget.getLandingPageType().equals(LandingPageType.HTM.name())) {
            enveuVideoItemBeans.add(enveuVideoItemBean);
            commonApiCallBack.onSuccess(this);
        } else {
            VideoDetailLayer.getInstance().getAssetTypeHero(screenWidget.getLandingPageAssetId(), new CommonApiCallBack() {
                @Override
                public void onSuccess(Object item) {
                    if (item instanceof EnveuVideoDetails) {
                        EnveuVideoDetails enveuVideoDetails = (EnveuVideoDetails) item;
                        AppCommonMethod.createManualHeroItem(enveuVideoItemBean,enveuVideoDetails);
                    }
                    enveuVideoItemBeans.add(enveuVideoItemBean);
                    commonApiCallBack.onSuccess(RailCommonData.this);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    commonApiCallBack.onFailure(new Throwable("HERO ASSET NOT FOUND"));
                }

                @Override
                public void onFinish() {

                }


            });
        }


    }

    private void setAssetTypeHero(EnveuVideoItemBean enveuVideoItemBean, CommonApiCallBack commonApiCallBack) {
        final RailCommonData railCommonData = this;
        screenWidget.setLandingPageAssetId(screenWidget.getManualImageAssetId());
        VideoDetailLayer.getInstance().getAssetTypeHero(screenWidget.getManualImageAssetId(), new CommonApiCallBack() {
            @Override
            public void onSuccess(Object item) {
                if (item instanceof EnveuVideoDetails) {
                    EnveuVideoDetails enveuVideoDetails = (EnveuVideoDetails) item;
                    AppCommonMethod.createAssetHeroItem(enveuVideoItemBean,enveuVideoDetails,screenWidget);
                    enveuVideoItemBeans.add(enveuVideoItemBean);
                    railCommonData.setEnveuVideoItemBeans(enveuVideoItemBeans);
                    commonApiCallBack.onSuccess(railCommonData);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                commonApiCallBack.onFailure(new Throwable("HERO ASSET NOT FOUND"));

            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void setBrighcoveVideos(List<ItemsItem> videos, String imageType,String imageIdentifier, boolean type) {
       // Logger.d("RailCommonData---->>" + imageType);
        if (videos != null && videos.size() > 0) {
            final RailCommonData railCommonData = this;

            for (int i=0;i<videos.size();i++){
                VideosItem videoItem= videos.get(i).getContent();
                Gson gson = new Gson();
                String tmp = gson.toJson(videoItem);
                EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(videoItem,videos.get(i).getContentOrder(),imageType,imageIdentifier,type,false);
                if (videoItem!=null){
                    if (videoItem.getSeasonNumber()!=null && !videoItem.getSeasonNumber().equalsIgnoreCase("")){
                        int seasonNumber=Integer.parseInt(videoItem.getSeasonNumber());
                        railCommonData.setSeasonNumber(seasonNumber);
                    }
                    enveuVideoItemBeans.add(enveuVideoItemBean);
                }

            }

            if (enveuVideoItemBeans!=null && enveuVideoItemBeans.size()>0){
                Collections.sort(enveuVideoItemBeans, new Comparator<EnveuVideoItemBean>(){
                    public int compare(EnveuVideoItemBean o1, EnveuVideoItemBean o2){
                        return Integer.compare(o1.getContentOrder(), o2.getContentOrder());
                    }
                });
            }
        }
    }

    private void setEpisodesList(List<ItemsItem> videos, String imageType,boolean isIntentRelatedContent) {
        if (videos != null && videos.size() > 0) {
            final RailCommonData railCommonData = this;
            for (int i=0;i<videos.size();i++){
                VideosItem videoItem= videos.get(i).getContent();
                Gson gson = new Gson();
                String tmp = gson.toJson(videoItem);
                EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(videoItem,videos.get(i).getContentOrder(),imageType,"",true,isIntentRelatedContent);
                if (videoItem!=null){
                    if (videoItem.getSeasonNumber()!=null && !videoItem.getSeasonNumber().equalsIgnoreCase("")){
                        int seasonNumber=Integer.parseInt(videoItem.getSeasonNumber());
                        railCommonData.setSeasonNumber(seasonNumber);
                    }
                    enveuVideoItemBean.setVodCount(videos.size());
                    if (screenWidget != null && screenWidget.getWidgetImageType() != null && screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString()))
                    {
                        Logger.d("Screen WidgetType "+screenWidget.getWidgetImageType());
                        String imageUrl=ImageLayer.getInstance().getThumbNailImageUrl(videoItem);
                        enveuVideoItemBean.setPosterURL(imageUrl);

                    }
                    else{
                        if (!isIntentRelatedContent) {
                            String imageUrl=ImageLayer.getInstance().getPosterImageUrl(videoItem,"");
                            Logger.d("FinalImageUrl: " + imageUrl);
                            enveuVideoItemBean.setPosterURL(imageUrl);
                        }

                    }
                    enveuVideoItemBeans.add(enveuVideoItemBean);
                }

            }

            try {
                if (enveuVideoItemBeans!=null && enveuVideoItemBeans.size()>0){
                    Collections.sort(enveuVideoItemBeans, new Comparator<EnveuVideoItemBean>(){
                        public int compare(EnveuVideoItemBean o1, EnveuVideoItemBean o2){
                            if (o1.getEpisodeNo()!=null && o1.getEpisodeNo() instanceof Double){
                                return Double.compare((Double) o1.getEpisodeNo(), (Double) o2.getEpisodeNo());
                            }else {
                                return 0;
                            }
                        }
                    });
                }
            }catch (Exception ignored){

            }

        }
    }


    public void setBrightCoveSeries(List<SeriesItem> seriesItems, String name) {
        for (SeriesItem seriesItem :
                seriesItems) {
            Gson gson = new Gson();
            String tmp = gson.toJson(seriesItem);
            EnveuVideoItemBean enveuVideoItemBean = gson.fromJson(tmp, EnveuVideoItemBean.class);

            if (screenWidget != null && screenWidget.getWidgetImageType() != null && screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString()))
                enveuVideoItemBean.setPosterURL(seriesItem.getThumbnailImage());
            else
                enveuVideoItemBean.setPosterURL(seriesItem.getPosterImage());

            enveuVideoItemBean.setTitle(seriesItem.getName());
            enveuVideoItemBean.setBrightcoveVideoId(seriesItem.getBrightcoveSeriesId());
            enveuVideoItemBeans.add(enveuVideoItemBean);
        }
    }

    private void setRailType(String layoutType, String layoutImageType) {
        Log.d("layoutType", "setRailType: " + layoutType + layoutImageType);
        if (layoutType.equalsIgnoreCase(Layouts.CAR.name())) {
            if (layoutImageType.equalsIgnoreCase(ImageType.LDS.name())) {
                railType = AppConstants.CAROUSEL_LDS_LANDSCAPE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR1.name())) {
                railType = AppConstants.CAROUSEL_PR_POTRAIT;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.SQR.name())) {
                railType = AppConstants.CAROUSEL_SQR_SQUARE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.CIR.name())) {
                railType = AppConstants.CAROUSEL_CIR_CIRCLE;
            }
        } else if (layoutType.equalsIgnoreCase(Layouts.HOR.name())) {
            if (layoutImageType.equalsIgnoreCase(ImageType.LDS.name())) {
                railType = AppConstants.HORIZONTAL_LDS_LANDSCAPE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR1.name())) {
                railType = AppConstants.HORIZONTAL_PR_POTRAIT;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR2.name())) {
                railType = AppConstants.HORIZONTAL_PR_POSTER;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.SQR.name())) {
                railType = AppConstants.HORIZONTAL_SQR_SQUARE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.CIR.name())) {
                railType = AppConstants.HORIZONTAL_CIR_CIRCLE;
            }
        } else if (layoutType.equalsIgnoreCase(Layouts.GRD.name())) {
            Log.d("grid", "setRailType: 1 ");
            if (layoutImageType.equalsIgnoreCase(ImageType.LDS.name())) {
                railType = AppConstants.GRD_HORIZONTAL_LDS_LANDSCAPE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR1.name())) {
                railType = AppConstants.GRD_HORIZONTAL_PR_PORTRAIT;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR2.name())) {
                railType = AppConstants.GRD_HORIZONTAL_PR_POSTER;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.SQR.name())) {
                Log.d("grid", "setRailType: 1 " + layoutImageType);
                railType = AppConstants.GRD_HORIZONTAL_SQR_SQUARE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.CIR.name())) {
                railType = AppConstants.GRD_HORIZONTAL_CIR_CIRCLE;
            }
        } else if (layoutType.equalsIgnoreCase(Layouts.HRO.name())) {
            if (layoutImageType.equalsIgnoreCase(ImageType.LDS.name())) {
                railType = AppConstants.HERO_LDS_LANDSCAPE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR1.name())) {
                railType = AppConstants.HERO_PR_POTRAIT;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.PR2.name())) {
                railType = AppConstants.HERO_PR_POSTER;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.SQR.name())) {
                railType = AppConstants.HERO_SQR_SQUARE;
            } else if (layoutImageType.equalsIgnoreCase(ImageType.CIR.name())) {
                railType = AppConstants.HERO_CIR_CIRCLE;
            }
        } else if (layoutType.equalsIgnoreCase(Layouts.BAN.name())) {
            railType = AppConstants.ADS_BANNER;

        } else if (layoutType.equalsIgnoreCase(Layouts.MRC.name())) {
            railType = AppConstants.ADS_MREC;
        }
    }

    public void setContinueWatchingData(BaseCategory screenWidget,boolean isContinueWatching, ArrayList<DataItem> enveuVideoDetails, CommonApiCallBack commonApiCallBack) {
       // this.identifier = screenWidget.getReferenceName();
        this.displayName = (String) screenWidget.getName();
        this.screenWidget = screenWidget;
        final RailCommonData railCommonData = this;
        if (enveuVideoDetails != null && enveuVideoDetails.size() > 0) {
            for (DataItem enveuVideoDetails1 : enveuVideoDetails) {
                Gson gson = new Gson();
                String tmp = gson.toJson(enveuVideoDetails1);
                EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(enveuVideoDetails1);

                if (this.screenWidget != null && this.screenWidget.getWidgetImageType() != null && this.screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString()))
                {
                    Logger.d("Screen WidgetType "+screenWidget.getWidgetImageType());
                  //  String imageUrl=ImageLayer.getInstance().getThumbNailImageUrl(enveuVideoDetails1);
                   // enveuVideoItemBean.setPosterURL(imageUrl);

                }
                else{
                    String imageUrl=ImageLayer.getInstance().getPosterImageUrl(enveuVideoDetails1);
                    enveuVideoItemBean.setPosterURL(imageUrl);

                }

                enveuVideoItemBeans.add(enveuVideoItemBean);
            }
            setRailType(Layouts.HOR.name(), ImageType.LDS.name());
            railCommonData.setIsContinueWatching(isContinueWatching);
            commonApiCallBack.onSuccess(RailCommonData.this);
        } else {
            commonApiCallBack.onFailure(new Throwable("No Data Found"));
        }

    }
    public void setWatchHistoryData(ArrayList<DataItem> enveuVideoDetails, CommonApiCallBack commonApiCallBack) {

        if (enveuVideoDetails != null && enveuVideoDetails.size() > 0) {
            for (DataItem enveuVideoDetails1 : enveuVideoDetails) {
                Gson gson = new Gson();
                String tmp = gson.toJson(enveuVideoDetails1);
                EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(enveuVideoDetails1);

                if (this.screenWidget != null && this.screenWidget.getWidgetImageType() != null && this.screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString())){
                    Logger.d("Screen WidgetType "+screenWidget.getWidgetImageType());
                    String imageUrl=ImageLayer.getInstance().getThumbNailImageUrl(enveuVideoDetails1);
                    enveuVideoItemBean.setPosterURL(imageUrl);

                }
                else{
                    String imageUrl=ImageLayer.getInstance().getPosterImageUrl(enveuVideoDetails1);
                    enveuVideoItemBean.setPosterURL(imageUrl);

                }

                enveuVideoItemBeans.add(enveuVideoItemBean);
            }
            setRailType(Layouts.HOR.name(), ImageType.LDS.name());
            commonApiCallBack.onSuccess(RailCommonData.this);
        } else {
            commonApiCallBack.onFailure(new Throwable("No Data Found"));
        }

        /* if (enveuVideoDetails != null && enveuVideoDetails.size() > 0) {
            for (EnveuVideoDetails enveuVideoDetails1 :
                    enveuVideoDetails) {
                Gson gson = new Gson();
                String tmp = gson.toJson(enveuVideoDetails1);
                EnveuVideoItemBean enveuVideoItemBean = gson.fromJson(tmp, EnveuVideoItemBean.class);

                if (this.screenWidget != null && this.screenWidget.getWidgetImageType() != null && this.screenWidget.getWidgetImageType().equalsIgnoreCase(WidgetImageType.THUMBNAIL.toString()))
                    enveuVideoItemBean.setPosterURL(enveuVideoDetails1.getThumbnailImage());
                else
                    enveuVideoItemBean.setPosterURL(enveuVideoDetails1.getPosterImage());

                enveuVideoItemBean.setVideoPosition(0);
                enveuVideoItemBeans.add(enveuVideoItemBean);
            }
            setRailType(Layouts.HOR.name(), ImageType.LDS.name());
            commonApiCallBack.onSuccess(RailCommonData.this);
        } else {
            commonApiCallBack.onFailure(new Throwable("No Data Found"));
        }*/

    }
   /* public String getIdentifier() {
        return identifier;
    }*/

   /* public String getBrightcovePlaylistId() {
        return brightcovePlaylistId;
    }*/

    public void setIsContinueWatching(boolean continueWatching) {
        isContinueWatching = continueWatching;
    }

    public boolean isContinueWatching() {
        return isContinueWatching;
    }

    public void setIsAd(boolean ad) {
        isAd = ad;
    }

    public boolean isAd() {
        return isAd;
    }

    public int getMaxContent() {
        return maxContent;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPlaylistType() {
        return playlistType;
    }

    public List<EnveuVideoItemBean> getEnveuVideoItemBeans() {
        return enveuVideoItemBeans;
    }

    public void setEnveuVideoItemBeans(List<EnveuVideoItemBean> enveuVideoItemBeans) {
        this.enveuVideoItemBeans = enveuVideoItemBeans;
    }

   /* public long getId() {
        return id;
    }*/

    public int getRailType() {
        return railType;
    }

    public BaseCategory getScreenWidget() {
        return screenWidget;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int page) {
        this.pageTotal = page;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public VideoItem getEpisodeDetails() {
        return videoItemDetail;
    }

    public void setEpisodeDetails(VideoItem customContent) {
        this.videoItemDetail = customContent;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public boolean isSeries() {
        return isSeries;
    }

    public void setSeries(boolean series) {
        isSeries = series;
    }

    public int getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(int seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public String getSeasonName() {
        return seasonName;
    }

    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
    }


}
