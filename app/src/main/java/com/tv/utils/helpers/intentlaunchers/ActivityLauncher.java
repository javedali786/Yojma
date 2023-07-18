package com.tv.utils.helpers.intentlaunchers;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;

import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory;
import com.example.jwplayer.PlayerActivity;
import com.tv.activities.Novelties.ui.NoveltiesActivity;
import com.tv.activities.detail.ui.DetailActivity;
import com.tv.activities.detail.ui.DetailsNew;
import com.tv.activities.detail.ui.EpisodeActivity;
import com.tv.activities.downloads.MyDownloads;
import com.tv.activities.homeactivity.ui.HomeActivity;
import com.tv.activities.listing.listui.ListActivity;
import com.tv.activities.listing.ui.GridActivity;
import com.tv.activities.listing.ui.MoreForYouActivity;
import com.tv.activities.listing.ui.MyListActivity;
import com.tv.activities.live.LiveActivity;
import com.tv.activities.notification.ui.NotificationActivity;
import com.tv.activities.profile.CountryListActivity;
import com.tv.activities.profile.order_history.ui.OrderHistoryActivity;
import com.tv.activities.profile.ui.AccountSettingActivity;
import com.tv.activities.profile.ui.ManageSubscriptionAccount;
import com.tv.activities.profile.ui.ProfileActivityNew;
import com.tv.activities.purchase.ui.PurchaseActivity;
import com.tv.activities.search.ui.ActivityResults;
import com.tv.activities.search.ui.ActivitySearch;
import com.tv.activities.series.ui.SeriesDetailActivity;
import com.tv.activities.settings.ActivitySettings;
import com.tv.activities.splash.ui.ActivitySplash;
import com.tv.activities.usermanagment.ui.ActivityForgotPassword;
import com.tv.activities.usermanagment.ui.ActivityLogin;
import com.tv.activities.usermanagment.ui.ActivitySelectSubscriptionPlan;
import com.tv.activities.usermanagment.ui.ActivitySignUp;
import com.tv.activities.usermanagment.ui.ChangePasswordActivity;
import com.tv.activities.usermanagment.ui.EnterOTPActivity;
import com.tv.activities.usermanagment.ui.PaymentDetailPage;
import com.tv.activities.watchList.ui.WatchListActivity;
import com.tv.beanModel.entitle.ResponseEntitle;
import com.tv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.utils.Logger;
import com.tv.utils.constants.AppConstants;
import com.tv.utils.helpers.ADHelper;
import com.tv.utils.helpers.StringUtils;
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.io.Serializable;
import java.util.List;


public final class ActivityLauncher {
    private static ActivityLauncher instance;

    private ActivityLauncher() {
    }

    public static ActivityLauncher getInstance() {
        if (instance == null) {
            instance = new ActivityLauncher();
        }
        return instance;
    }

    public void activitySignUp(Activity source, Class<ActivitySignUp> destination) {
        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
        source.finish();
    }

    /*public void liveTvActivity(Activity source, Class<ActivityLiveTv> destination) {
        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
    }*/
    public void homeActivity(Activity source, Class<HomeActivity> destination) {
        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
        source.finish();
    }

    public void ProfileActivityNew(Activity source, Class<ProfileActivityNew> destination) {
        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
    }

    public void CountryListActivity(Activity source, Class<CountryListActivity> destination) {
        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
    }



    public void AccountSettingActivity(Activity source, Class<AccountSettingActivity> destination) {

        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
    }

    public void manageAccount(Activity source, Class<ManageSubscriptionAccount> destination) {

        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
    }
    public void goToPlanSubscription(Activity source, Class<ActivitySelectSubscriptionPlan> destination) {
        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
    }
    public void goToPurchaseActivity(Activity source, Class<PurchaseActivity> destination) {

        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
    }


    public void loginActivity(Activity source, Class<ActivityLogin> destination) {
        if (source instanceof HomeActivity){
            Intent intent = new Intent(source, destination);
            intent.putExtra("loginFrom","home");
            source.startActivity(intent);
            source.finish();
        }else {
            Intent intent = new Intent(source, destination);
            intent.putExtra("loginFrom","");
            source.startActivity(intent);
            source.finish();
        }


    }

    public void homeActivityWithParams(Activity source, Class<HomeActivity> destination,int postion) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.HOME_TAG,postion);
        source.startActivity(intent);
    }
    public void launchPlayerActitivity(Activity source, Class<PlayerActivity> destination, String playbackurl, boolean IsBingeWatchEnable, List<EnveuVideoItemBean> seasonEpisodesList, int currentEpisodeId,String tittle,String contentType,Boolean isTrailer,Boolean isLive, String posterUrl,String screenName) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("activity", String.valueOf(source));
        intent.putExtra("playBackUrl",playbackurl);
        intent.putExtra("binge_watch",IsBingeWatchEnable);
        intent.putExtra("episodeId",currentEpisodeId);
        intent.putExtra("tittle",tittle);
        intent.putExtra("contentType",contentType);
        intent.putExtra("episodeList", (Serializable) seasonEpisodesList);
        intent.putExtra("isTrailer",isTrailer);
        intent.putExtra("isLive",isLive);
        intent.putExtra("posterUrl",posterUrl);
        intent.putExtra("screenName",screenName);
        source.startActivity(intent);
    }

    public void homeActivityWithIntent(Activity source, Class<HomeActivity> destination,int postion,String intentFrom) {
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.HOME_TAG,postion);
        intent.putExtra(AppConstants.INTENT_FROM , intentFrom);
        source.startActivity(intent);
    }

    public void forceLogin(Activity source, Class<ActivityForgotPassword> destination, String token, String fid, String name, String picUrl,boolean forceLogin) {
        Bundle args = new Bundle();
        args.putString("fbName", name);
        args.putString("fbToken", token);
        args.putString("fbId", fid);
        args.putString("fbProfilePic", picUrl);
        args.putBoolean("forceLogin",forceLogin);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.EXTRA_REGISTER_USER, args);
        source.startActivity(intent);
    }


    public void searchActivity(Activity source, Class<ActivitySearch> destination) {
        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
    }


    public void homeScreen(Activity source, Class<HomeActivity> destination) {
        Intent intent = new Intent(source, destination);
        //intent.putExtra( AppConstants.KIDS_MODE,kidsMode);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        TaskStackBuilder.create(source).addNextIntentWithParentStack(intent).startActivities();
    }

    public void splashScreenKill(Activity source, Class<ActivitySplash> destination) {
        Intent intent = new Intent(source, destination);
        //intent.putExtra( AppConstants.KIDS_MODE,kidsMode);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        TaskStackBuilder.create(source).addNextIntentWithParentStack(intent).startActivities();
    }

    public void detailScreen(Activity source, Class<DetailActivity> destination, int id, String duration, boolean isPremium) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putInt(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, id);

        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        preference.setAppPrefAssetId(0);
        if (ADHelper.getInstance(source).getPipAct()!=null){
            ADHelper.getInstance(source).getPipAct().moveTaskToBack(false);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        source.startActivity(intent);
    }
    public void detailScreenBrightCove(Activity source, Class<DetailActivity> destination,int id) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
       // args.putLong(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, "");

//        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
//        args.putString(AppConstants.BUNDLE_DETAIL_TYPE, detailType);
//
//        //this is for non-series launcher
//        if (StringUtils.isNullOrEmpty(duration))
//            args.putString(AppConstants.BUNDLE_DURATION, "0");
//        else
//            args.putString(AppConstants.BUNDLE_DURATION, duration);

        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        preference.setAppPrefAssetId(0);
        Logger.d("JSON SENT: " + args);
        if (ADHelper.getInstance(source).getPipAct()!=null){
            ADHelper.getInstance(source).getPipAct().moveTaskToBack(false);
            ADHelper.getInstance(source).getPipAct().finish();
            //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        source.startActivity(intent);


    }

    public void liveScreenBrightCove(Activity source, Class<LiveActivity> destination, Long videoId, int id, String duration, boolean isPremium, String detailType) {
        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putLong(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, videoId);

        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        args.putString(AppConstants.BUNDLE_DETAIL_TYPE, detailType);

        //this is for non-series launcher
        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);

        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        preference.setAppPrefAssetId(0);
        Logger.d("JSON SENT: " + args);
        source.startActivity(intent);
    }



    public void episodeScreenBrightcove(Activity source, Class<EpisodeActivity> destination,int id) {
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();

        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
//      args.putLong(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, videoId);
//        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        args.putBoolean(AppConstants.EXTRA_SHOW_PRE_ROLL_VIDEO, !(source instanceof EpisodeActivity));
//        args.putString(AppConstants.IS_SIGN_LANG_ENABLE, signedLangEnabled);
//        args.putString(AppConstants.SIGN_LANG_ID, signedLangRefId);

//        if (StringUtils.isNullOrEmpty(duration))
//            args.putString(AppConstants.BUNDLE_DURATION, "0");
//        else
//            args.putString(AppConstants.BUNDLE_DURATION, duration);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        preference.setAppPrefAssetId(0);
        if (ADHelper.getInstance(source).getPipAct()!=null){
            ADHelper.getInstance(source).getPipAct().moveTaskToBack(false);
            ADHelper.getInstance(source).getPipAct().finish();
            //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        source.startActivity(intent);
    }


    public void episodeScreen(Activity source, Class<EpisodeActivity> destination, int id, String duration, boolean isPremium) {
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();

        Bundle args = new Bundle();
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id);
        args.putBoolean(AppConstants.BUNDLE_IS_PREMIUM, isPremium);
        if (StringUtils.isNullOrEmpty(duration))
            args.putString(AppConstants.BUNDLE_DURATION, "0");
        else
            args.putString(AppConstants.BUNDLE_DURATION, duration);
        Intent intent = new Intent(source, destination);
        intent.putExtra(AppConstants.BUNDLE_ASSET_BUNDLE, args);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        preference.setAppPrefAssetId(0);
        if (ADHelper.getInstance(source).getPipAct()!=null){
            ADHelper.getInstance(source).getPipAct().moveTaskToBack(false);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        }
        source.startActivity(intent);
    }

    public void portraitListing(Activity source, Class<GridActivity> destination, String i, String title, int flag, int type, BaseCategory baseCategory, boolean continueWatching) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("playListId", i);
        intent.putExtra("title", title);
        intent.putExtra("flag", flag);
        intent.putExtra("shimmerType", type);
        intent.putExtra("baseCategory", baseCategory);
        intent.putExtra("isContinueWatching",continueWatching);
        source.startActivity(intent);
    }

    public void listActivity(Activity source, Class<ListActivity> destination, String i, String title, int flag, int type, BaseCategory baseCategory) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("playListId", i);
        intent.putExtra("title", title);
        intent.putExtra("flag", flag);
        intent.putExtra("shimmerType", type);
        intent.putExtra("baseCategory", baseCategory);
        source.startActivity(intent);
    }
    public void listActivityForYou(Activity source, Class<MoreForYouActivity> destination, String contentType,String tag,String videoType,int id) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("contentType", contentType);
        intent.putExtra("preferenceData", tag);
        intent.putExtra("videoType", videoType);
        intent.putExtra(AppConstants.ID,id);
        source.startActivity(intent);
    }

    public void notificationActivity(Activity source, Class<NotificationActivity> destination) {
        Intent intent = new Intent(source, destination);
        source.startActivity(intent);
    }


    public void goToEnterOTP(Activity source, Class<EnterOTPActivity> destination, String screenName){
        Intent intent = new Intent(source, destination);
        intent.putExtra("fromWhich",screenName);
        source.startActivity(intent);
        source.finish();

    }

    public void goToPlanScreen(Activity source, Class<ActivitySelectSubscriptionPlan> destination, String screenName){
        Intent intent = new Intent(source, destination);
        intent.putExtra("fromWhich",screenName);
        source.startActivity(intent);
    }

//    public void goToPlanScreenFrmSetting(Activity source, Class<ActivitySelectSubscriptionPlan> destination, boolean isFrom,String screenName){
//        Intent intent = new Intent(source, destination);
//        intent.putExtra("fromWhich",isFrom);
//        source.startActivity(intent);
//    }
    public void changePassword(Activity activity, Class<ChangePasswordActivity> destination){
        activity.startActivity(new Intent(activity, destination));
    }


    public void orderHistroy(Activity activity, Class<OrderHistoryActivity> destination){
        activity.startActivity(new Intent(activity, destination));
    }

    public void goToLogin(Activity activity, Class<ActivityLogin> destination){
        activity.startActivity(new Intent(activity, destination));
    }

    public void goToLoginFromSplash(Activity activity, Class<ActivityLogin> destination,boolean fromSplash){
        Intent intent = new Intent(activity, destination);
        intent.putExtra("fromSplash", fromSplash);
        activity.startActivity(intent);
        activity.finish();

    }

    public void gotoList(Activity activity, Class<MyListActivity> destination){
        activity.startActivity(new Intent(activity, destination));
    }


    public void goToNovelties(Activity activity, Class<NoveltiesActivity> destination){
        activity.startActivity(new Intent(activity, destination));
    }
    public void goToAccountSetting(Activity activity, Class<AccountSettingActivity> destination){
        activity.startActivity(new Intent(activity, destination));
    }
    public void goToSetting(Activity activity, Class<ActivitySettings> destination){
        activity.startActivity(new Intent(activity, destination));
    }

    public void goToNewsDetail(Activity activity, Class<DetailsNew> destination,int Id){
        Intent intent = new Intent(activity, destination);
        intent.putExtra("contentId",Id);
        activity.startActivity(intent);
    }

    public void goToEventDetail(Activity activity, Class<DetailsNew> destination){
        activity.startActivity(new Intent(activity, destination));
    }

//    public void goToExpeditionDetail(Activity activity, Class<ExpeditionDetail> destination,int Id,boolean IsFrom){
//        Intent intent = new Intent(activity, destination);
//        intent.putExtra("contentId",Id);
//        intent.putExtra("isFrom",IsFrom);
//        activity.startActivity(intent);
//    }

    public void resultActivityBundle(Activity source, Class<ActivityResults> destination, String assetType, String searchKey, int total,boolean applyFilter,String customContentType, String videoType,String header) {
        Bundle args = new Bundle();
        args.putString("assetType", assetType);
        args.putString("Search_Key", searchKey);
        args.putInt("Total_Result", total);
        args.putBoolean("apply_filter", applyFilter);
        args.putString("customContentType",customContentType);
        args.putString("videoType",videoType);
        args.putString("header",header);
        Intent intent = new Intent(source, destination);
        intent.putExtra("SearchResult", args);
        source.startActivity(intent);
    }



    public void seriesDetailScreen(Activity source, Class<SeriesDetailActivity> destination, int seriesId) {
        Intent intent = new Intent(source, destination);
        intent.putExtra("seriesId", seriesId);
        source.startActivity(intent);
    }



    public void watchHistory(Activity source, Class<WatchListActivity> destination, String type, boolean isWatchHistory) {
        Bundle args = new Bundle();
        args.putString("viewType", type);
        Intent intent = new Intent(source, destination);
        intent.putExtra("bundleId", args);
        intent.putExtra("isWatchHistory", isWatchHistory);
        source.startActivity(intent);
    }
    public void launchMyDownloads(Activity source){
        source.startActivity(new Intent(source, MyDownloads.class));
    }

    public void goToDetailPlanScreen(Activity source, Class<PaymentDetailPage> destination, boolean isFrom, ResponseEntitle responseEntitle){
        Intent intent = new Intent(source, destination);
        intent.putExtra("fromWhich",isFrom);
        intent.putExtra("responseEntitle", (Serializable) responseEntitle);
        source.startActivity(intent);
    }
}
