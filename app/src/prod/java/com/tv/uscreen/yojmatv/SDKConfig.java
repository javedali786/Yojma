package com.tv.uscreen.yojmatv;


import com.tv.uscreen.yojmatv.utils.MediaTypeConstants;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.config.bean.AvatarImages;
import com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse.ConfigBean;

import java.util.ArrayList;
import java.util.List;

public class SDKConfig {

    private static SDKConfig sdkConfigInstance;
    boolean isTablet = OttApplication.Companion.getContext().getResources().getBoolean(R.bool.isTablet);
    ConfigBean configBean;

    private SDKConfig() {

    }

    public static SDKConfig getInstance() {
        if (sdkConfigInstance == null) {
            sdkConfigInstance = new SDKConfig();
        }
        return (sdkConfigInstance);
    }

   /* //QA BASED KEYS FOR PROD (PAYMENT TESTING)
    public static final String CONFIG_BASE_URL = "https://experience-manager-fe-api.beta.enveu.com/app/api/v1/config/";
    public static final String API_KEY_MOB = "vssatqjctyrqghzopdulzkleguurqqxzyvxsodqj";
    public static final String API_KEY_TAB = "fkzmmfinyqdwecanpzkvbrmclntmklthnmiarzfr";*/


    //PROD KEYS
    public static final String CONFIG_BASE_URL = "https://api-us.enveu.com/experience-manager-fe-api/app/api/v1/config/";
    public static final String API_KEY_MOB = "axkxaeobdrpqqvhfvnlrkvkrstnbyhyr";
    public static final String API_KEY_TAB = "tamvrupdkecgabywsnescbzrnsscpkvm";

    public static final int CONFIG_VERSION = 1;
    public static String ApplicationStatus = "disconnected";
    public static final String WEBP_QUALITY = "filters:format(webp):quality(60)/";
    public static final int DOWNLOAD_EXPIRY_DAYS=90;
    public static final String PUBNUB_KEY_PUBLISH = "pub-c-88bde132-793e-415b-a436-e7d9a6b83856";
    public static final String PUBNUB_KEY_SUBSCRIBE = "sub-c-ecb62526-738c-11ec-8c2d-5a0a4f87b172";


    public void setConfigObject(ConfigBean configResponse, boolean isTablet) {
        this.configBean = configResponse;
        MediaTypeConstants.getInstance().setConfigObject(configBean);
    }

    public String getBASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getBaseUrl();
    }

    public String getOVP_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getOvpBaseUrl();
    }

    public String getSEARCH_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getSearchBaseUrl();
    }

    public String getCLOUD_FRONT_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getCloudFrontBaseURL();
    }

    public String getSpanishLangCode() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getLanguageCodes().getSpanish();
    }

    public String getSUBSCRIPTION_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getSubscriptionBaseUrl();
    }

    public String getPAYMENT_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getPaymentBaseUrl();
    }
    public String getPARTICIPATION_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getParticipationBaseUrl();
    }

    public String getJwPlayerDiliveryBaseUrl() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getJwPlayerDiliveryBaseUrl();
    }
    public String getKidsModeId() {
        return null;
    }

    public String getCoupon_BASE_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getCouponBaseUrl();
    }

    public String getTermCondition_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getTermsConditionUrl();
    }

    public String getPrivay_Policy_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getPrivacyPolicyUrl();
    }

    public String getHELP_CENTER_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getHelpCenterUrl();
    }

    public String getFEEDBACK_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getFeedbackUrl();
    }
    public String getFAQ_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getFaqUrl();
    }
    public String getCONTACT_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getContactUsUrl();
    }
    public String getABOUT_US_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getAboutUsUrl();
    }

    public String getOvpApiKey() {
        if (isTablet) {
            return API_KEY_TAB;
        } else {
            return API_KEY_MOB;
        }
    }


    public String getFirstTabId() {
        return AppCommonMethod.getHomeTabId(configBean, "HOME");
    }

    public String getSecondTabId() {
        return AppCommonMethod.getHomeTabId(configBean, "MOVIE");
    }

    public String getThirdTabId() {
        return AppCommonMethod.getHomeTabId(configBean, "SERIES");
    }

    public String getFourthTabId() {
        return AppCommonMethod.getHomeTabId(configBean, "YOJMA KIDS");
    }

    public String getMovieDetailId() {
        return AppCommonMethod.getHomeTabId(configBean, "MOVIE DETAIL");
    }

    public String getShowDetailId() {
        return null;
       // return AppCommonMethod.getHomeTabId(configBean, "SHOW DETAIL");
    }

    public String getEpisodeDetailId() {
        return null;
      //  return AppCommonMethod.getHomeTabId(configBean, "EPISODE DETAIL");
    }

    public String getSeriesDetailId() {
        return null;
      //  return AppCommonMethod.getHomeTabId(configBean, "SERIES DETAIL");
    }

    public String getLiveDetailId() {
        return null;
      //  return AppCommonMethod.getHomeTabId(configBean, "LIVETV DETAIL");
    }

    public String getPopularSearchId() {
        return configBean == null ? "" : String.valueOf(configBean.getData().getAppConfig().getPopularSearchId());
    }

    public String getThaiLangCode() {
        return null;
      //  return configBean == null ? "" : configBean.getData().getAppConfig().getLanguageCodes().getThai();
    }

    public String getEnglishCode() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getLanguageCodes().getEnglish();
    }

    public List<String> getSupportedCurrencies() {
        return configBean == null ? new ArrayList<>() : configBean.getData().getAppConfig().getSupportedCurrencies();
    }

    public String getWebPUrl() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getImageTransformBaseURL();
    }

    public String getConfigVastTag() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getVastTagUrl()==null ? "" : configBean.getData().getAppConfig().getVastTagUrl();
    }

    public boolean getBingeWatchingEnabled() {
        return configBean != null && configBean.getData().getAppConfig().isBingeWatchingEnabled();
        // return configBean != null && configBean.getData().getAppConfig().getBingeWatchingEnabled();
    }

    public int getTimer() {
        return configBean == null ? 0 : configBean.getData().getAppConfig().getTimer();
    }

    public int getDownloadExpiryDays() {
        return DOWNLOAD_EXPIRY_DAYS;
    }

    public boolean isDownloadEnable() {
        return false;
       // return configBean != null && configBean.getData().getAppConfig().isDownloadEnable();
    }
//    public List<ContentPreference> getContentPreference() {
//        return configBean == null ? null : configBean.getData().getAppConfig().getContentPreference();
//    }
    public List<AvatarImages> getAvatarImages() {
        return configBean == null ? null : configBean.getData().getAppConfig().getAvatarImages();
       // return configBean == null ? null : configBean.getData().getAppConfig().getavatarImages();
    }
    public String getProfileFolder() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getProfilePictureBaseFolder();
    }
    public String getPLAYBACK_URL() {
        return configBean == null ? "" : configBean.getData().getAppConfig().getPlaybackBaseUrl();
    }

    public String getLivePlayBackUrl() {
        return  configBean == null ? "" : configBean.getData().getAppConfig().getLiveBaseURL();
    }

//    public boolean isLiveChatEnabled() {
//        return configBean != null && configBean.getData().getAppConfig().getPubnub() != null
//                && configBean.getData().getAppConfig().getPubnub().isLiveChatEnabled();
//    }

}