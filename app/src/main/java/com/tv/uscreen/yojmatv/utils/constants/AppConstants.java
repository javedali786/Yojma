package com.tv.uscreen.yojmatv.utils.constants;

public interface AppConstants {
    String CONTENT_PREFERENCE = "contentPreference";
    String MY_VIPA_ENCRYPTION_KEY="MYMVHUB$KEY";
    String APP_CONTINUE_WATCHING = "CONTINUE WATCHING";
    String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String PLATFORM = "MOBILE";
    String PROFILE_FOLDER = "/180x180/filters:format(jpeg):max_bytes(400):quality(100)/app/user/profilePicture/";
    String GENRE_IMAGES_BASE_KEY = "/cms/images/taxonomy/genre/";
    String CAST_CREW_IMAGES_BASE_KEY = "/cms/images/taxonomy/castCrew/";
    String SERIES_IMAGES_BASE_KEY = "/cms/images/library/series/";
    String VIDEO_IMAGE_BASE_KEY = "/cms/images/library/video/";
    String FILTER = "/filters:";
    String SEARCH_GENRE_CONSTATNT = "genre:";
    String SEARCH_SORT_CONSTATNT = "title.keyword:";
    String FILTER_CUSTOM_FIELDS = "customFields.";
    String CF_IS_AUDIO_DESC = "is_audio_description";
    String CF_IS_CLOSE_CAPTION = "is_close_caption";
    String CF_IS_SOUND_TRACK = "is_sound_track";
    String CF_IS_SIGN_LANG = "is_signed_language_enabled";
    String CF_IS_4K = "is4k";
    String QUALITY = "quality";
    String VOD = "VIDEO";
    String SERIES = "SERIES";
    String PUBLISHED = "PUBLISHED";
    String NOT_FOR_SALE = "NOT_FOR_SALE";
    String CAST_AND_CREW = "CAST_AND_CREW";
    String GENRE = "GENRE";
    String SQUARE = "SQUARE";
    String CIRCLE = "CIRCLE";
    String LANDSCAPE = "WIDE_SCREEN_LANDSCAPE";
    String POTRAIT = "WIDE_SCREEN_PORTRAIT";
    String POSTER_LANDSCAPE = "POSTER_LANDSCAPE";
    String POSTER_POTRAIT = "POSTER_PORTRAIT";
    String Season = "SEASON";
    String Episode = "EPISODE";
    String Shows = "SHOWS";
    String Trailer = "TRAILER";
    String Series = "SERIES";
    String Movie = "MOVIE";
    String Movies = "MOVIES";
    String DOCUMENTARY = "DOCUMENTARY";
    String Documentaries = "DOCUMENTARIES";

    String Video = "VIDEO";
    String API_PARAM_NAME = "name";
    String API_PARAM_EMAIL = "email";
    String API_PARAM_PASSWORD = "password";
    String API_PARAM_STATUS = "status";
    String API_PARAM_PROFILE_PIC = "profilePicURL";
    String API_PARAM_GENDER = "gender";
    String API_PARAM_DOB = "dateOfBirth";
    String API_PARAM_IS_VERIFIED = "verified";
    String API_PARAM_VERIFICATION_DATE = "verificationDate";
    String API_PARAM_PHONE_NUMBER = "phoneNumber";
    String API_PARAM_EXPIRY_DATE = "expiryDate";
    String API_PARAM_PROFILE_STEP = "profileStep";
    String API_PARAM_NEW_PWD = "newPassword";
    String API_PARAM_FB_ID = "fbId";
    String API_PARAM_EMAIL_ID = "emailId";
    String API_PARAM_FB_TOKEN = "accessToken";
    String API_PARAM_IS_FB_EMAIL = "fbMail";
    String API_PARAM_FB_PIC = "profilePicUrl";
    String API_PARAM_WATCHLIST_ID = "watchListItemId";
    String API_PARAM_LIKE_ID = "id";
    String API_PARAM_LIKE_TYPE = "type";
    String API_PARAM_PAGE = "page";
    String API_PARAM_SIZE = "size";
    String API_PARAM_SERIES_ID = "seriesId";
    String API_PARAM_SEASON_ID = "seasonId";
    String API_PARAM_TYPE = "type";
    String API_PARAM_ID = "id";
    String API_PARAM_FETCH_DATA = "fetchData";
    String API_RESPONSE_CODE = "responseCode";
    String API_PARAM_USER_ASSET_LIST_DTO = "userAssetListDTOList";
    int RESPONSE_CODE_LOGOUT=401;
    int RESPONSE_CODE_ERROR = 500;
    String EXTRA_REGISTER_USER = "extraRegisterUser";
    String UNPUBLISHED = "UNPUBLISHED";
    String APP_PREF_PROFILE = "profileDetail";
    String APP_PREF_ACCESS_TOKEN = "accessToken";
    String APP_PREF_LOGIN_STATUS = "loginStatus";
    String FINAL_APP_PREF_LOGIN_STATUS = "status";
    String APP_PREF_GET_USER = "geLtUser";
    String APP_PREF_LOGIN_TYPE = "userLoginType";
    String APP_PREF_USER_ID = "userId";
    String APP_PREF_AVAILABLE_VERSION = "availableVersion";
    String APP_PREF_CFEP = "cloudFrontEndpoint";
    String APP_PREF_VIDEO_URL = "cloudFrontVideoUrl";
    String APP_PREF_CONFIG_VERSION = "configVersion";
    String APP_PREF_SERVER_BASE_URL = "serverBaseURL";
    String APP_PREF_CONFIG_RESPONSE = "Config_Response";
    String APP_PREF_LAST_CONFIG_HIT = "LastConfigTime";
    String APP_PREF_JUMP_BACK = "returnBack";
    String APP_PREF_IS_EPISODE = "isEpisode";
    String APP_PREF_JUMP_TO = "returnTo";
    String APP_PREF_VIDEO_POSITION = "videoPosition";
    String APP_PREF_GOTO_PURCHASE = "returnPurchase";
    String APP_PREF_ASSET_ID = "assetId";
    String APP_PREF_SELECTED_SEASON_ID = "seasonId";
    String APP_PREF_HAS_SELECTED_ID = "hasSelectedId";
    String APP_PREF_IS_RESTORE_STATE = "isRestoreState";
    String HOME_ENVEU = "0";
    String ORIGINAL_ENVEU = "1";
    String PREMIUM_ENVEU = "2";
    String SINETRON_ENVEU = "3";
    String WIDGET_TYPE_AD = "ADS";
    String WIDGET_TYPE_CONTENT = "CNT";
    String KEY_MREC = "MREC";
    String KEY_BANNER = "BANNER";
    int CAROUSEL_LDS_LANDSCAPE = 10;
    int CAROUSEL_LDS_BANNER = 11;
    int CAROUSEL_PR_POTRAIT = 12;
    int CAROUSEL_PR_POSTER = 13;
    int CAROUSEL_SQR_SQUARE = 14;
    int CAROUSEL_CIR_CIRCLE = 15;
    int CAROUSEL_CST_CUSTOM = 16;
    int HERO_LDS_LANDSCAPE = 20;
    int HERO_LDS_BANNER = 21;
    int HERO_PR_POTRAIT = 22;
    int HERO_PR_POSTER = 23;
    int HERO_SQR_SQUARE = 24;
    int HERO_CIR_CIRCLE = 25;
    int HERO_CST_CUSTOM = 26;
    int HORIZONTAL_LDS_LANDSCAPE = 31;
    int HORIZONTAL_PR_POTRAIT = 33;
    int HORIZONTAL_PR_POSTER = 34;
    int HORIZONTAL_SQR_SQUARE = 35;
    int HORIZONTAL_CIR_CIRCLE = 36;
    int ADS_BANNER = 41;
    int ADS_MREC = 42;
    int GRD_HORIZONTAL_LDS_LANDSCAPE = 43;
    int GRD_HORIZONTAL_PR_PORTRAIT = 44;
    int GRD_HORIZONTAL_PR_POSTER = 45;
    int GRD_HORIZONTAL_SQR_SQUARE = 46;
    int GRD_HORIZONTAL_CIR_CIRCLE = 47;

    String BUNDLE_VIDEO_ID_BRIGHTCOVE = "videoId";
    String BUNDLE_TAB_ID = "tabId";
    String BUNDLE_ASSET_ID = "assestId";
    String BUNDLE_SERIES_ID = "seriesId";
    String BUNDLE_IS_PREMIUM = "isPremium";
    String BUNDLE_DURATION = "duration";
    String BUNDLE_ASSET_BUNDLE = "assestIdBundle";
    String WEB_VIEW_HEADING = "WebViewHeading";
    String WEB_VIEW_URL = "WebVieweURl";
    String BUNDLE_BANNER_IMAGE = "bannerImage";
    String POSTER_URL = "posterUrl";
    String BUNDLE_SEASON_COUNT="seasonCount";
    String BUNDLE_SEASON_ARRAY="seasonArray";
    String BUNDLE_SEASON_NAME = "seasonName";
    String BUNDLE_TRAILER_REF_ID = "trailerRefId";
    String EXTRA_TRAILER_DETAILS = "extra_trailer_details";
    String EXTRA_SHOW_PRE_ROLL_VIDEO = "extra_show_pre_roll_video";
    String IS_SIGN_LANG_ENABLE = "signLangParentRefId";
    String AUDIO_TRACK_ITEM = "audioTracks";
    String SIGN_LANG_ID = "signLangId";
    String IS_PODCAST = "podcast";
    String BUNDLE_SELECTED_SEASON="selectedSeasonId";
    String BUNDLE_DETAIL_TYPE = "detailType";
    String BUNDLE_SERIES_DETAIL = "seriesDetail";
    String BOOKMARK_POSITION = "bookmarkPosition";
    String PLAYER_ASSET_TITLE = "playerAssetTitle";
    String PLAYER_ASSET_MEDIATYPE = "playerMediaType";
    int PAGE_SIZE = 20;
    String BUNDLE_ASSET_TYPE = "assetType";
    String BUNDLE_CURRENT_ASSET_ID = "currentAssetId";
    String SEARCH_TYPE_PROGRAM = "search_type_program";
    enum UserLoginType {
        Manual,
        FbLogin,
        GoogleLogin
    }

    enum UserStatus {
        Login,
        Logout
    }

    enum ContentType {
        VIDEO,
        MOVIE,
        SHOW,
        EPISODE,
        SERIES,
        SEASON,
        VOD,
        CONTINUE_WATCHING,
        MY_WATCHLIST,
        LIVE,
        ARTICLE
    }
    String USER_NAME="name";
    String USER_EMAIL="email";
    String EXPIRY_TIME="expirytime";
    String ACCOUNT_ID="accountid";
    String LIGHT_THEME = "LightTheme";
    String DARK_THEME = "DarkTheme";
    String FCM_TOKEN = "fcm_token";
    String HUNTER = "Hunter";
    String FISHERMAN = "FisherMan";
    String BOTH = "Both";
    String FROM = "From";
    String TITLE = "title";
    String MESSAGE = "message";
    String ACTION_BTN = "action_btn";
    String MOBILE_NUMBER="mobileNumber";
    String FB_MAIL = "fbMail";
    String PREF_ID="pref_id";
    String SPECIES_LIST = "speciesList";
    String INTENT_FROM_SETTING = "intentFromSetting";
    String PREFERENCE_PROFILE_ID = "profileId";
    String SIGN_IN_SUCCESS = "sign_in_success";
    String SIGN_UP_SUCCESS = "sign_UP_success";
    String LIVE_TV="LiveTv";
    String VOD_HOME="vod";
    String EXPENDITIONS="expedition";
    String HOME_TAG="homeTag";
    String MARKET_PLACES="marketplaces";
    String OFFERS="offers";
    String home="home";
    String vod="vod";
    String CAPS_VOD="VOD";
    String live="live";
    String expedition="expedition";
    String offers_offers="offers_offers";
    String offers_agency="offers_agency";
    String offers_species="offers_species";
    String marketPlace_product="marketPlace_product";
    String marketPlace_brand="marketPlace_brand";
    String marketPlace_categories="marketPlace_categories";
    String bigGameHunting="bigGameHunting";
    String smallGameHunting="smallGameHunting";
    String seaFishing="seaFishing";
    String continentalFishing="continentalFishing";
    String contest="contest";
    String news="news";
    String News="NEWS";
    String Event="EVENT";
    String Expedition="EXPEDITION";
    String novilites="novilites";
    String events="events";
    String series="series";
    String episode="EPISODE";
    String episodes="EPISODES";

    String INTENT_FROM = "intentFrom";
    String VIDEO = "VIDEO";
    String CUSTOM = "CUSTOM";
    String LIVE = "LIVE";
    String LIVE_TVV="liveTV";
    String AGENCY = "Agency";
    String IMAGE = "image";
    String ITEM = "ITEM";
    String ENROLLED = "ENROLLED";
    String TYPE = "type";
    String SPECIES= "species";
    String LOGGED_IN= "login";
    String USER_VERIFY= "verify";
    String OFFER = "OFFER";
 /*   String UAT_LICENSE_KEY = "akIWITZoyn2qrLa73aJqHWIGSTmnPWWdx8E+SswS6T8FMHMhoNpt0sdP700=";
    String PROD_LICENSE_KEY = "Gk8hWhpetk6hSe1kA2KyaiIbLtvEcGWrv4WFiArHpdk9c4MDR9VPMrUCCvs=";
*/
    String UAT_LICENSE_KEY = "dQU071to+hwjttPkhKGRLyX8g4fqezTr7jOi6Tqsr01bj+43";
    String PROD_LICENSE_KEY = "emo9NLiNMOlsFfEOb33tG5Xz6dEu0rO0CKICdewTPRiiAC1n";
    String QA_LICENSE_KEY = "C1sJKzeiVH2jvWyNf6Mdi6tKaVOGzdCffVTGSIclagHZyDHz";
    String APPLE = "APPLE_IAP";
    String GOOGLE_IAP = "GOOGLE_IAP";
    String TWO_C_TWO_P = "TWO_C_TWO_P";
    String AMAZON_IAP = "AMAZON_IAP";
    String STRIPE = "STRIPE";
    String PAYPAL = "PAYPAL";
    String VIDEO_COMPLETED = "VIDEO_COMPLETED";
    String VIDEO_STARTED = "VIDEO_STARTED";
    String VIDEO_PAUSED="VIDEO_PAUSED";
    String TAB_SCREEN_VIEWED = "screen_viewed";
    String JOIN_US = "Join US";
    String IGO = "iGO";
    String BIG_GAME_HUNTING="Big Game Hunting";
    String SMALL_GAME_HUNTING="Small Game Hunting";
    String SEA_FISHING="Sea Fishing";
    String CONTINENTAL_FISHING = "Continental Fishing";
    String MY_LIST="My List";
    String VIEW_PROFILE="View Profile";
    String UPDATE_PROFILE= "Update Profile";
    String SETTING_CHANGE_LAN="Settings - Change language";
    String STREAMING_SETTING="Settings - Streaming settings";
    String CONTENT_PREF_PROFILE="Content Perference - Profile";
    String CONTENT_PREF_SPECIES="Content Perference - Species";
    String CONTENT_PREF_TYPES="Content Perference - Types";
    String TERMS_CONDTIONS="Terms & Conditions";
    String PRIVACY_POLICY= "Privacy Policy";
    String FAQ="FAQ";
    String HELP_CENTER="Help Center";
    String LOGIN= "Login";
    String REGISTER="Register";
    String USER_VERIFICATION="User Verification";
    String CHANGE_PASSWORD="Change Password";
    String FORGOT_PASSWORD="Forgot Password";
    String SUBSCRIPTION="Subscription";
    String ORDER_HISTORY="Order History";
    String SEARCH_SEE_ALL="Search - See All";
    String SEARCH_TEXT = "Search text";
    String SEE_ALL_RAIL_TITTLE="See All Rail tittle";
    String CONTENT_DETAIL_TITTLE="Content Detail tittle";
    String ID ="id";
    String LANG = "lang";
    String APP_PLATFPRM = "platform";
    String ANDROID= "Android";
    String CONTENT_TYPE ="contentType";
    String SCREEN_NAME = "screen_name";
    String CONTENT_PLAYED = "content_played";
    String CONTENT_DURATION = "content_duration";
    String SEARCH_TERMS= "search_term";
    String CONTENT_PLAY = "content_play";
    String CONTENT_COMPLETED = "content_completed";
    String CONTENT_PAUSE = "content_pause";
    String SHARE_CONTENT= "share_content";
    String ADD_TO_WATCHLIST="add_to_watchlist";
    String REMOVE_WATCHLIST= "remove_watchlist";
    String SEARCH = "Search";
    String HOME = "home";
    String LOGOUT="logout";
    String GALLERY_SELECT= "gallery_select";
    String CONTENT_SELECT= "content_select";
    String RAIL_NAME = "category_name";
    String RAIL_ID = "category_id";
    String CONTENT_ID="content_id";
    String CONTENT_TITTLE="CONTENT_TITTLE";
    String CONTENT_EXIT= "content_exit";
    String LIVEACTIVITY = "LIVEACTIVITY";
    String EPISODEACTIVITY = "EpisodeActivity";
    String SERIESDEATILACTIVITY = "SeriesDetailActivity";
    String DETAILACTIVITY="DetailActivity";
    String FREE_RAIL="OnlyFreeUser";
    String HUNTING_HOME="hunting_home";
    String FISHING_HOME="fishing_home";
    String BOTH_HOME="both_home";
    String ENGLISH = "English";
    String SPANISH = "Spanish";
    String CONTENT_PREFERENCES= "content_preferences";
    String USER_PROFILE="user_profile";
    String REGISTER_DATE="register_date";
    String USERTYPE = "userType";
    String FREE_USER ="FreeUser";
    String PAID_USER = "PaidUser";
    String USER_SUBSCRIPTION="userSubscription";
    String CURRENCY = "currency";
    String PRICE= "price";
    String SUBSCRIPTION_TITTLE = "subscriptionTittle";
    String PAYMENT_METHOD = "paymentMethod";
    String GOOGLE="Google";
    String Podcast = "PODCAST";
    String Gaming = "GAMING";
    String Reel = "REEL";
    String SEARCH_RESULT= "Search result";
    String SPANISH_SEARCH_RESULT= "Resultado de búsqueda";
    String QA_YOUBORA_ACCOUNT_CODE = "enveudev";
    String PROD_YOUBORA_ACCOUNT_CODE = "enveu";
    String SETTINGS = "settings";







}
