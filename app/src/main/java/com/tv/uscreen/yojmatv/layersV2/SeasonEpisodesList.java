package com.tv.uscreen.yojmatv.layersV2;

import com.tv.uscreen.yojmatv.callbacks.RequestOffferCallBack.RequestOfferCallBack;
import com.tv.uscreen.yojmatv.callbacks.apicallback.ApiResponseModel;
import com.tv.uscreen.yojmatv.callbacks.likelistCallback.ApiLikeList;
import com.tv.uscreen.yojmatv.networking.apiendpoints.ApiInterface;
import com.tv.uscreen.yojmatv.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.yojmatv.networking.servicelayer.APIServiceLayer;

public class SeasonEpisodesList {

    private static SeasonEpisodesList seasonEpisodesListInstance;
    private static ApiInterface endpoint;

    private SeasonEpisodesList() {

    }

    public static SeasonEpisodesList getInstance() {
        if (seasonEpisodesListInstance == null) {
            endpoint = RequestConfig.getEnveuClient().create(ApiInterface.class);
            seasonEpisodesListInstance = new SeasonEpisodesList();
        }
        return (seasonEpisodesListInstance);
    }

    public void getSeasonEpisodesV2(int seriesId, int pageNumber,
                                    int size, int seasonNumber, ApiResponseModel listener) {
        APIServiceLayer.getInstance().getSeasonEpisodesV2(seriesId, pageNumber, size, seasonNumber,listener);
    }

    public void getAllEpisodesV2(int seriesId, int pageNumber, int size, ApiResponseModel listener) {
        APIServiceLayer.getInstance().getAllEpisodesV2(seriesId, pageNumber, size,listener);
    }

    public void getIsLikeGOI(String token,int pageNumber, int size, ApiLikeList listener) {
        APIServiceLayer.getInstance().getIsLikeGOI(token, pageNumber, size,listener);
    }

    public void getForYouContent(int pageNumber, int size,String tag,String contentType,String videoType, ApiResponseModel listener) {
        APIServiceLayer.getInstance().getForYouContent(pageNumber,size,tag,contentType,videoType,listener);
    }

    public void getRelatedContent(int pageNumber, int size,String contentType,int id, ApiResponseModel listener) {
        APIServiceLayer.getInstance().getRelatedContent(pageNumber,size,contentType,id,listener);
    }

    public void getCustomDetail(int pageNumber, int size,String tag,String contentType, ApiResponseModel listener) {
        APIServiceLayer.getInstance().getCustomDetail(pageNumber,size,tag,contentType ,listener);
    }


    public void getRequestedOfferForUser(int page, int size, String token, int userId, String offers, String currentLanguageCode, RequestOfferCallBack requestOfferCallBack) {
        APIServiceLayer.getInstance().getRequestedOfferForUser(page, size, token,userId,offers,currentLanguageCode,requestOfferCallBack);
    }
}
