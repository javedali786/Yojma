package com.tv.uscreen.yojmatv.utils.config;

import android.util.Log;

import com.tv.uscreen.yojmatv.beanModelV3.continueWatching.DataItem;
import com.tv.uscreen.yojmatv.beanModelV3.playListModelV2.VideosItem;
import com.tv.uscreen.yojmatv.beanModelV3.searchV2.ItemsItem;
import com.tv.uscreen.yojmatv.beanModelV3.videoDetailsV2.EnveuVideoDetails;
import com.tv.uscreen.yojmatv.bean_model_v1_0.listAll.Item;
import com.tv.uscreen.yojmatv.bean_model_v1_0.videoDetailBean.Data;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

public class ImageLayer {
    private static ImageLayer imageLayerInstance;

    private ImageLayer() {

    }

    public static ImageLayer getInstance() {
        if (imageLayerInstance == null) {
            imageLayerInstance = new ImageLayer();
        }
        return (imageLayerInstance);
    }


    public String getPosterImageUrl1(ItemsItem videoItem) {
        String finalUrl = "";
        try {

            if (videoItem.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (videoItem.getVideo().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getVideo().getImages().size(); i++) {
                        Log.d("JAVED", "getPosterImageUrl: " + videoItem.getVideo());
                        if (videoItem.getVideo().getImages().get(i).isDefault()) {
                            finalUrl = videoItem.getVideo().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }

            } else if (videoItem.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                if (videoItem.getCustomContent().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getCustomContent().getImages().size(); i++) {
                        Log.d("JAVED", "getPosterImageUrl: " + videoItem.getCustomContent());
                        if (videoItem.getCustomContent().getImages().get(i).isDefault()) {
                            finalUrl = videoItem.getCustomContent().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        }
        Log.w("imageURL", finalUrl);
        return finalUrl;
    }


    public String getPosterImageUrl(VideosItem videoItem, String imageIdentifier) {
        String finalUrl = "";
        try {
            if (videoItem.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (videoItem.getVideo().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getVideo().getImages().size(); i++) {
                        Log.d("JAVED", "getPosterImageUrl: " + videoItem.getVideo());
                        if (videoItem.getVideo().getImages().get(i).getTag().toString().equalsIgnoreCase(imageIdentifier)) {
                            finalUrl = videoItem.getVideo().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }

            } else if (videoItem.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                if (videoItem.getCustomContent().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getCustomContent().getImages().size(); i++) {
                        if (videoItem.getCustomContent().getImages().get(i).getTag().toString().equalsIgnoreCase(imageIdentifier)) {
                            finalUrl = videoItem.getCustomContent().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            }

        } catch (Exception ignored) {

        }
        Log.d("Images", "getPosterImageUrl: " + finalUrl);
        return finalUrl;
    }

    public String getProfilePosterUrl(VideosItem videoItem) {
        String finalUrl = "";
        try {
            if (videoItem.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                if (videoItem.getParentContent().getCustomContent().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getParentContent().getCustomContent().getImages().size(); i++) {
                        if (videoItem.getParentContent().getCustomContent().getImages().get(i).getTag().toString().equalsIgnoreCase("img4")) {
                            finalUrl = videoItem.getParentContent().getCustomContent().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            } else if (videoItem.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (videoItem.getParentContent().getVideo().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getParentContent().getVideo().getImages().size(); i++) {
                        if (videoItem.getParentContent().getVideo().getImages().get(i).getTag().toString().equalsIgnoreCase("img4")) {
                            finalUrl = videoItem.getParentContent().getVideo().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        }

        return finalUrl;
    }
    public String getProfilePosterUrl(Item videoItem) {
        String finalUrl = "";
        try {
            if (videoItem.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                if (videoItem.getParentContent().getCustomContent().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getParentContent().getCustomContent().getImages().size(); i++) {
                        if (videoItem.getParentContent().getCustomContent().getImages().get(i).getTag().toString().equalsIgnoreCase("img4")) {
                            finalUrl = videoItem.getParentContent().getCustomContent().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            } else if (videoItem.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (videoItem.getParentContent().getVideo().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getParentContent().getVideo().getImages().size(); i++) {
                        if (videoItem.getParentContent().getVideo().getImages().get(i).getTag().toString().equalsIgnoreCase("img4")) {
                            finalUrl = videoItem.getParentContent().getVideo().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        }

        return finalUrl;
    }


    public String getPopularSearchImageUrl(VideosItem videoItem) {
        String finalUrl = "";
        try {

            if (videoItem.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (videoItem.getVideo().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getVideo().getImages().size(); i++) {
                        Log.d("JAVED", "getPosterImageUrl: " + videoItem.getVideo());
                        if (videoItem.getVideo().getImages().get(i).isDefault()) {
                            finalUrl = videoItem.getVideo().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }

            } else if (videoItem.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                if (videoItem.getCustomContent().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getCustomContent().getImages().size(); i++) {
                        Log.d("JAVED", "getPosterImageUrl: " + videoItem.getCustomContent());
                        if (videoItem.getCustomContent().getImages().get(i).isDefault()) {
                            finalUrl = videoItem.getCustomContent().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }

            }


        } catch (Exception ignored) {

        }

        return finalUrl;
    }

    public String getLiveImageUrl(VideosItem videoItem) {
        String finalUrl = "";
            if (videoItem.getContentType().equalsIgnoreCase(AppConstants.LIVE)) {
                if (videoItem.getLiveContent().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getLiveContent().getImages().size(); i++) {
                        if (videoItem.getLiveContent().getImages().get(i).isDefault()) {
                            finalUrl = videoItem.getLiveContent().getImages().get(i).getSrc();
                            break;
                        }
                    }
                }
            }
        return finalUrl;
    }



    public String getPosterImageUrl(ItemsItem videoItem) {
        String finalUrl = "";
        try {
            if (videoItem.getVideo().getImages().size() > 0) {
                for (int i = 0; i < videoItem.getVideo().getImages().size(); i++) {
                    if (videoItem.getVideo().getImages().get(i).isDefault()) {
                        finalUrl = videoItem.getVideo().getImages().get(i).getSrc();
                        Log.w("imageURL", finalUrl);
                        break;
                    }
                }
            }


        } catch (Exception ignored) {

        }
        Log.w("imageURL", finalUrl);
        return finalUrl;
    }

    public String getPosterImageUrl(DataItem videoItem) {
        String finalUrl = "";
        try {

            if (videoItem.getVideo().getImages().size() > 0) {
                for (int i = 0; i < videoItem.getVideo().getImages().size(); i++) {
                    if (videoItem.getVideo().getImages().get(i).isDefault()) {
                        finalUrl = videoItem.getVideo().getImages().get(i).getSrc();
                        Log.w("imageURL", finalUrl);
                        break;
                    }
                }
            }
        } catch (Exception ignored) {

        }

        return finalUrl;
    }


    public String getThumbNailImageUrl(VideosItem videoItem) {
       /* String finalUrl="";
        try {

        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            if (videoItem.getImages()!=null && videoItem.getImages().getThumbnailEn()!=null && videoItem.getImages().getThumbnailEn().getSources()!=null
                    && videoItem.getImages().getThumbnailEn().getSources().size()>0){
                finalUrl=videoItem.getImages().getThumbnailEn().getSources().get(0).getSrc();
            }else {
                if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                        && videoItem.getImages().getThumbnail().getSources().size()>0){
                    finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
                }
            }
        }
        else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")){
            if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                    && videoItem.getImages().getThumbnail().getSources().size()>0){
                finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
            }
        }
        }catch (Exception ignored){

        }
        if(finalUrl.isEmpty()){
            if (videoItem.getImages()!=null && videoItem.getImages().getThumbnail()!=null && videoItem.getImages().getThumbnail().getSources()!=null
                    && videoItem.getImages().getThumbnail().getSources().size()>0){
                finalUrl=videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
            }
        }
        return finalUrl;*/

        String finalUrl = "";
        try {
      /*  if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            if (videoItem.getImages() != null && videoItem.getImages().getPosterEn() != null && videoItem.getImages().getPosterEn().getSources() != null
                    && videoItem.getImages().getPosterEn().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPosterEn().getSources().get(0).getSrc();
            }else {
                if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                        && videoItem.getImages().getPoster().getSources().size() > 0) {
                    finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
                }
            }
        }
        else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")){
            if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                    && videoItem.getImages().getPoster().getSources().size() > 0) {
                finalUrl= videoItem.getImages().getPoster().getSources().get(0).getSrc();
            }
        }*/

            finalUrl = videoItem.getVideo().getImages().get(0).getSrc();

        } catch (Exception ignored) {

        }
        if (finalUrl.isEmpty()) {
            if (videoItem.getImages() != null && videoItem.getImages().getPoster() != null && videoItem.getImages().getPoster().getSources() != null
                    && videoItem.getImages().getPoster().getSources().size() > 0) {
                finalUrl = videoItem.getImages().getPoster().getSources().get(0).getSrc();
            }
        }
        return finalUrl;
    }

    public String getThumbNailImageUrl(DataItem videoItem) {
        String finalUrl = "";
        try {

            if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                if (videoItem.getImages() != null && videoItem.getImages().getThumbnailEn() != null && videoItem.getImages().getThumbnailEn().getSources() != null
                        && videoItem.getImages().getThumbnailEn().getSources().size() > 0) {
                    finalUrl = videoItem.getImages().getThumbnailEn().getSources().get(0).getSrc();
                } else {
                    if (videoItem.getImages() != null && videoItem.getImages().getThumbnail() != null && videoItem.getImages().getThumbnail().getSources() != null
                            && videoItem.getImages().getThumbnail().getSources().size() > 0) {
                        finalUrl = videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
                    }
                }
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("Thai")) {
                if (videoItem.getImages() != null && videoItem.getImages().getThumbnail() != null && videoItem.getImages().getThumbnail().getSources() != null
                        && videoItem.getImages().getThumbnail().getSources().size() > 0) {
                    finalUrl = videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
                }
            }
        } catch (Exception ignored) {

        }
        if (finalUrl.isEmpty()) {
            if (videoItem.getImages() != null && videoItem.getImages().getThumbnail() != null && videoItem.getImages().getThumbnail().getSources() != null
                    && videoItem.getImages().getThumbnail().getSources().size() > 0) {
                finalUrl = videoItem.getImages().getThumbnail().getSources().get(0).getSrc();
            }
        }
        return finalUrl;
    }


    public String getPosterImageUrl(EnveuVideoDetails videoItem) {
        String finalUrl = "";
        try {
            if (videoItem.getVideo().getImages().size() > 0) {
                for (int i = 0; i < videoItem.getVideo().getImages().size(); i++) {
                    if (videoItem.getVideo().getImages().get(i).isDefault()) {
                        finalUrl = videoItem.getVideo().getImages().get(i).getSrc();
                        Log.w("imageURL", finalUrl);
                        break;
                    }
                }
            }


        } catch (Exception ignored) {

        }

        return finalUrl;
    }

    public String getEpisodePosterImageUrl(Item videoItem, String imageIdentifier) {
        String finalUrl = "";
        try {
            if (videoItem.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (videoItem.getVideo().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getVideo().getImages().size(); i++) {
                        if (videoItem.getVideo().getImages().get(i).isDefault()) {
                            finalUrl = videoItem.getVideo().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }

            } else if (videoItem.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                if (videoItem.getCustomContent().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getCustomContent().getImages().size(); i++) {
                        if (videoItem.getCustomContent().getImages().get(i).isDefault()) {
                            finalUrl = videoItem.getCustomContent().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            }

        } catch (Exception ignored) {

        }


        Log.w("imageURL", finalUrl);
        return finalUrl;
    }


    public String getEpisodePosterImageUrl(Data videoItem, String imageIdentifier) {
        String finalUrl = "";
        try {
            if (videoItem.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (videoItem.getVideo().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getVideo().getImages().size(); i++) {
                        if (videoItem.getVideo().getImages().get(i).isDefault()) {
                            finalUrl = videoItem.getVideo().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            } else if (videoItem.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                if (videoItem.getCustomContent().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getCustomContent().getImages().size(); i++) {
                        if (videoItem.getCustomContent().getImages().get(i).isDefault()) {
                            finalUrl = videoItem.getCustomContent().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.e(e);
        }

        Log.w("imageURL", finalUrl);
        return finalUrl;
    }

    public String getPosterImageUrl(com.enveu.client.epgListing.epgResponse.ItemsItem itemsItem) {
        String finalUrl = "";
        if (itemsItem.getProgram().getContent().getVideo().getImages().size() > 0) {
            for (int i = 0; i < itemsItem.getProgram().getContent().getVideo().getImages().size(); i++) {
                if (itemsItem.getProgram().getContent().getVideo().getImages().get(i).isIsDefault()) {
                    finalUrl = itemsItem.getProgram().getContent().getVideo().getImages().get(i).getSrc();
                    Log.w("imageURL", finalUrl);
                    break;
                }
            }
        }
        return finalUrl;
    }

    public String getForYouImageUrl(Item videoItem, String imageIdentifier) {
        String finalUrl = "";
        try {
            if (videoItem.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (videoItem.getVideo().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getVideo().getImages().size(); i++) {
                        Log.d("JAVED", "getPosterImageUrl: " + videoItem.getVideo());
                        if (videoItem.getVideo().getImages().get(i).getTag().toString().equalsIgnoreCase(imageIdentifier)) {
                            finalUrl = videoItem.getVideo().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }

            } else if (videoItem.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                if (videoItem.getCustomContent().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getCustomContent().getImages().size(); i++) {
                        if (videoItem.getCustomContent().getImages().get(i).getTag().toString().equalsIgnoreCase(imageIdentifier)) {
                            finalUrl = videoItem.getCustomContent().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            }


        } catch (Exception ignored) {

        }

        return finalUrl;
    }

    public String getRelatedContentImageUrl(VideosItem videoItem, String imageIdentifier) {
        String finalUrl = "";
        try {
            if (videoItem.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (videoItem.getVideo().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getVideo().getImages().size(); i++) {
                        Log.d("JAVED", "getPosterImageUrl: " + videoItem.getVideo());
                        if (videoItem.getVideo().getImages().get(i).getTag().toString().equalsIgnoreCase(imageIdentifier)) {
                            finalUrl = videoItem.getVideo().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }

            } else if (videoItem.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                if (videoItem.getCustomContent().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getCustomContent().getImages().size(); i++) {
                        if (videoItem.getCustomContent().getImages().get(i).getTag().toString().equalsIgnoreCase(imageIdentifier)) {
                            finalUrl = videoItem.getCustomContent().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            }


        } catch (Exception ignored) {

        }

        return finalUrl;
    }


    public String getExpeditionImageUrl(Data videoItem, String imageIdentifier) {
        String finalUrl = "";
        try {
            if (videoItem.getContentType().equalsIgnoreCase(AppConstants.VIDEO)) {
                if (videoItem.getVideo().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getVideo().getImages().size(); i++) {
                        Log.d("JAVED", "getPosterImageUrl: " + videoItem.getVideo());
                        if (videoItem.getVideo().getImages().get(i).getTag().toString().equalsIgnoreCase(imageIdentifier)) {
                            finalUrl = videoItem.getVideo().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }

            } else if (videoItem.getContentType().equalsIgnoreCase(AppConstants.CUSTOM)) {
                if (videoItem.getCustomContent().getImages().size() > 0) {
                    for (int i = 0; i < videoItem.getCustomContent().getImages().size(); i++) {
                        if (videoItem.getCustomContent().getImages().get(i).getTag().toString().equalsIgnoreCase(imageIdentifier)) {
                            finalUrl = videoItem.getCustomContent().getImages().get(i).getSrc();
                            Log.w("imageURL", finalUrl);
                            break;
                        }
                    }
                }
            }


        } catch (Exception ignored) {

        }

        return finalUrl;
    }


}
