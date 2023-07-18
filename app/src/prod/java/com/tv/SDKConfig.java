package com.tv;



import com.tv.utils.MediaTypeConstants;
import com.tv.utils.config.bean.AvatarImages;
import com.tv.utils.config.bean.dmsResponse.ConfigBean;

import java.util.ArrayList;
import java.util.List;

public class SDKConfig {

    private static SDKConfig sdkConfigInstance;
    boolean isTablet = OttApplication.getInstance().getResources().getBoolean(R.bool.isTablet);
    ConfigBean configBean;

    private SDKConfig() {

    }

    public static SDKConfig getInstance() {
        if (sdkConfigInstance == null) {
            sdkConfigInstance = new SDKConfig();
        }
        return (sdkConfigInstance);
    }

    /* UAT keys*//*
     */
   /* public static final String CONFIG_BASE_URL = "https://api-eu1.enveu.com/experience-manager-fe-api/app/api/v1/config/";
    public static final String API_KEY_MOB = "xsswbnbjcmfpvjnufsskjzgqwkhbvbvpniivahdl";
    public static final String API_KEY_TAB = "obgbedzdgzcjsxwhzvtvdzegzgtkhvnxtmeraxlt";*/
    //Prod config

    //PROD KEYS
    public static final String CONFIG_BASE_URL = "https://api-eu1.enveu.com/experience-manager-fe-api/app/api/v1/config/";
    public static final String API_KEY_MOB = "ubjoadpovgmagoxqikaerdqpbowqlddivfbvtsxp";
    public static final String API_KEY_TAB = "huwlvryczkqciosjcdepvcgjliuefimvhruieucy";


//    public static final String CONFIG_BASE_URL = "https://api-eu1.enveu.com/experience-manager-fe-api/app/api/v1/config/";
//    public static final String API_KEY_MOB = "jyeubkiluqkpsmsiszzfazpcqidydarzoblqcmxh";
//    public static final String API_KEY_TAB = "kumnqiowcgouixukvlnufaptcjzmhulhtyqbbflr";

    /* UAT keys*/
   /* public static String CONFIG_BASE_URL = "https://experience-manager-fe-api.uat.enveu.com/app/api/v1/config/";
    public static String API_KEY_MOB = "spkuaohqsngqcrvfrgforegkooveiobspgwwbmce";
    public static String API_KEY_TAB = "unwtftwilxazacjururwsdtwuepymqqxlvamahqf";*/
    public static final int CONFIG_VERSION = 1;
    public static String ApplicationStatus = "disconnected";
    public static final String WEBP_QUALITY = "filters:format(webp):quality(60)/";
    public static final int DOWNLOAD_EXPIRY_DAYS=90;
    public static final String PUBNUB_KEY_PUBLISH = "pub-c-88bde132-793e-415b-a436-e7d9a6b83856";
    public static final String PUBNUB_KEY_SUBSCRIBE = "sub-c-ecb62526-738c-11ec-8c2d-5a0a4f87b172";

    /*uat keys*//*
    public static String CONFIG_BASE_URL = "https://experience-manager-fe-api.uat.enveu.com/app/api/v1/config/";
    public static String API_KEY_MOB = "zzSQzVoXQl9oA6d75OPCJ5UHowG2upc6520dJ3zs";
    public static String API_KEY_TAB = "2hpaUCvN395jJA6AMaamk8wyOQbvAGhNQaIuH2Nf";
    public static int CONFIG_VERSION = 1;
    public static String ApplicationStatus = "disconnected";*/

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
       // return AppCommonMethod.getHomeTabId(configBean, "HOME");
        return null;
    }

    public String getSecondTabId() {
        return null;
        //return AppCommonMethod.getHomeTabId(configBean, "PROGRAM");
    }

    public String getThirdTabId() {
        return null;
       // return AppCommonMethod.getHomeTabId(configBean, "PODCAST");
    }

    public String getFourthTabId() {
        return null;
        //return AppCommonMethod.getHomeTabId(configBean, "LIVE");
    }

    public String getMovieDetailId() {
        return null;
        //return AppCommonMethod.getHomeTabId(configBean, "MOVIE DETAIL");
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