package com.tv.uscreen.yojmatv.utils.commonMethods

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.SystemClock
import android.provider.Settings
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.billingclient.api.SkuDetails
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory
import com.enveu.client.enums.ImageType
import com.enveu.client.enums.RailCardType
import com.enveu.client.enums.WidgetImageType
import com.example.jwplayer.PlayerActivity
import com.facebook.login.LoginManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties

import com.tv.uscreen.yojmatv.BuildConstants
import com.tv.uscreen.yojmatv.OttApplication
import com.tv.uscreen.yojmatv.R

import com.tv.uscreen.yojmatv.SDKConfig
import com.tv.uscreen.yojmatv.activities.detail.ui.DetailActivity
import com.tv.uscreen.yojmatv.activities.detail.ui.EpisodeActivity
import com.tv.uscreen.yojmatv.activities.layers.EntitlementLayer
import com.tv.uscreen.yojmatv.activities.purchase.in_app_billing.BillingProcessor
import com.tv.uscreen.yojmatv.activities.purchase.purchase_model.PurchaseModel
import com.tv.uscreen.yojmatv.activities.purchase.ui.VodOfferType
import com.tv.uscreen.yojmatv.activities.series.ui.SeriesDetailActivity
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.ActivityLogin
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.EnterOTPActivity
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.PaymentDetailPage
import com.tv.uscreen.yojmatv.activities.usermanagment.viewmodel.RegistrationLoginViewModel
import com.tv.uscreen.yojmatv.baseModels.BaseActivity
import com.tv.uscreen.yojmatv.beanModel.configBean.ResponseConfig
import com.tv.uscreen.yojmatv.beanModel.entitle.ResponseEntitle
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModel.membershipAndPlan.ResponseMembershipAndPlan
import com.tv.uscreen.yojmatv.beanModel.responseModels.landingTabResponses.CommonRailData
import com.tv.uscreen.yojmatv.beanModel.responseModels.landingTabResponses.railData.PlaylistRailData
import com.tv.uscreen.yojmatv.beanModel.userProfile.UserProfileResponse
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.beanModelV3.videoDetailsV2.EnveuVideoDetails
import com.tv.uscreen.yojmatv.beanModelV3.videoDetailsV2.EnveuVideoDetailsBean
import com.tv.uscreen.yojmatv.bean_model_v1_0.videoDetailBean.Data
import com.tv.uscreen.yojmatv.bean_model_v1_0.videoDetailBean.EnvVideoDetailsBean
import com.tv.uscreen.yojmatv.databinding.ActivityPaymentDetailPagePlanBinding
import com.tv.uscreen.yojmatv.databinding.ActivityPurchaseBinding
import com.tv.uscreen.yojmatv.databinding.ActivitySelectSubscriptionPlanBinding


import com.tv.uscreen.yojmatv.fragments.dialog.DialogPlayer
import com.tv.uscreen.yojmatv.fragments.player.ui.UserInteractionFragment
import com.tv.uscreen.yojmatv.tarcker.EventConstant
import com.tv.uscreen.yojmatv.tarcker.FCMEvents
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.MediaTypeConstants
import com.tv.uscreen.yojmatv.utils.ObjectHelper
import com.tv.uscreen.yojmatv.utils.config.LanguageLayer
import com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse.ConfigBean
import com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse.SpeciesItem
import com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse.TypesItem
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.ActivityTrackers
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils
import com.tv.uscreen.yojmatv.utils.helpers.carousel.model.Slide
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import com.tv.uscreen.yojmatv.utils.stringsJson.model.StringsData
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONObject
import retrofit2.Response
import java.lang.ref.WeakReference
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class AppCommonMethod private constructor() : AppCompatActivity(), DialogPlayer.DialogListener {
    override fun onDialogFinish() {}

    companion object {
        @JvmField
        var Url = ""

        @JvmField
        var UriId = ""
        val adsRail: List<CommonRailData> = ArrayList<CommonRailData>()

        @JvmField
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.default_profile_pic)
            .error(R.drawable.default_profile_pic)
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        @JvmField
        val optionsSearch: RequestOptions = RequestOptions()
            .fitCenter()
            .placeholder(R.drawable.placeholder_landscape)
            .error(R.drawable.placeholder_landscape)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
        val optionsPlayer: RequestOptions = RequestOptions()
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
        val castOptions: RequestOptions = RequestOptions()
            .placeholder(R.drawable.placeholder_landscape)
            .error(R.drawable.placeholder_landscape)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
        var afterLogin = false

        @JvmField
        var urlPoints = ""
        var isPurchase = false
        var isDownloadDeleted = false
        var isDownloadIndex = -1

        @JvmField
        var isSeasonCount = false
        var isSeriesPage = false
        var isInternet = false
        var assetId = 0
        var seriesId = 0
        var seasonId = 0
        var getTimeStampDOB: Long = 0
        private const val sharingURL = ""
        private var mActivity: WeakReference<Activity>? = null
        private val activity: Activity? = null
        private var mLastClickTime: Long = 0
        private var isUserNotVerify = false
        private var isUserNotEntitle = false
        private var resEntitle: ResponseEntitle? = null

        @JvmStatic
        fun convertDpToPixel(dp: Int): Int {
            val metrics: DisplayMetrics = Resources.getSystem().displayMetrics
            return dp * (metrics.densityDpi / 160)
        }

        private const val FIT_IN_640_360 = "/fit-in/640x360"
        private const val FIT_IN_200_200 = "/fit-in/200x200"
        private const val FIT_IN_360_640 = "/fit-in/360x640"
        private const val IMG_FORMAT_SIZE = "(100):format(webP):maxbytes(400)"
        private const val FEATURE_SHARING = "sharing"
        private const val CHANNEL_NAME = "enveusharing"
        private const val CONTROL_PARAM_CONTENT_TYPE = "contentType"
        private const val CONTROL_PARAM_ID = "id"
        private const val CONTROL_PARAM_SERIES_ID = "seriesId"
        private const val CONTROL_PARAM_SEASON_NUMBER = "seasonNumber"

        @JvmStatic
        fun getImageUrl(contentType: String, shape: String): String {
            var url = ""
            val imageFrontEndUrl: String
            var resolution = ""
            imageFrontEndUrl = urlPoints
            if (shape.equals("LANDSCAPE", ignoreCase = true)) {
                resolution = FIT_IN_640_360
            } else if (shape.equals("SQUARE", ignoreCase = true)) {
                resolution = FIT_IN_200_200
            } else if (shape.equals("CIRCLE", ignoreCase = true)) {
                resolution = FIT_IN_200_200
            } else if (shape.equals("POTRAIT", ignoreCase = true)) {
                resolution = FIT_IN_360_640
            } else if (shape.equals("CROUSEL", ignoreCase = true)) {
                resolution = FIT_IN_640_360
            } else if (shape.equals("POSTER_POTRAIT", ignoreCase = true)) {
                resolution = FIT_IN_360_640
            } else if (shape.equals("POSTER_LANDSCAPE", ignoreCase = true)) {
                resolution = FIT_IN_640_360
            }
            if (contentType.equals(AppConstants.VOD, ignoreCase = true)) {
                url = (imageFrontEndUrl + resolution + AppConstants.FILTER + AppConstants.QUALITY
                        + IMG_FORMAT_SIZE + AppConstants.VIDEO_IMAGE_BASE_KEY)
            } else if (contentType.equals(AppConstants.SERIES, ignoreCase = true)) {
                url = (imageFrontEndUrl + resolution + AppConstants.FILTER + AppConstants.QUALITY
                        + IMG_FORMAT_SIZE + AppConstants.SERIES_IMAGES_BASE_KEY)
            } else if (contentType.equals(AppConstants.CAST_AND_CREW, ignoreCase = true)) {
                url = (imageFrontEndUrl + resolution + AppConstants.FILTER + AppConstants.QUALITY
                        + IMG_FORMAT_SIZE + AppConstants.CAST_CREW_IMAGES_BASE_KEY)
            } else if (contentType.equals(AppConstants.GENRE, ignoreCase = true)) {
                url = (imageFrontEndUrl + resolution + AppConstants.FILTER + AppConstants.QUALITY
                        + IMG_FORMAT_SIZE + AppConstants.GENRE_IMAGES_BASE_KEY)
            }
            return url
        }

        @Throws(NumberFormatException::class)
        fun parseTime(millis: Long): Long {
            return TimeUnit.MILLISECONDS.toSeconds(millis)
        }

        fun copyShareURL(activity: Activity?, title: String?, assetId: Int, assetType: String?, imgUrl: String?, seriesId: String?, seasonNumber: String?) {
//        mActivity = new WeakReference<>(activity);
//        BranchUniversalObject buo = new BranchUniversalObject()
//                .setTitle(title)
//                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
//                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
//                .setContentImageUrl(imgUrl);
//
//
//        LinkProperties lp = new LinkProperties()
//                .setFeature(FEATURE_SHARING)
//                .setChannel(CHANNEL_NAME)
//                .addControlParameter(CONTROL_PARAM_CONTENT_TYPE, assetType)
//                .addControlParameter(CONTROL_PARAM_ID, String.valueOf(assetId))
//                .addControlParameter(CONTROL_PARAM_SERIES_ID, String.valueOf(seriesId))
//                .addControlParameter(CONTROL_PARAM_SEASON_NUMBER, String.valueOf(seasonNumber));
//        buo.generateShortUrl(activity, lp, (url, error) -> {
//            if (error == null) {
//                sharingURL = url;
//                String completeURL = mActivity.get().getResources().getString(R.string.checkout) + " " + title + " " + mActivity.get().getResources().getString(R.string.on_enveu) + "\n" + sharingURL;
//                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mActivity.get().getSystemService(Context.CLIPBOARD_SERVICE);
//                    android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", completeURL);
//                    clipboard.setPrimaryClip(clip);
//                    ToastHandler.getInstance().show(activity, "Copied");
//                Logger.i("BRANCH SDK: got my Branch link to share: " + sharingURL);
//            } else {
//                Logger.e("BRANCH ERROR: " + error.getMessage());
//            }
//        });
        }

        ///// Create dynamic link object
        fun createDynamicLinkObject(id: String?, mediaType: String?): JSONObject {
            val jsonObject = JSONObject()
            try {
                jsonObject.put(CONTROL_PARAM_CONTENT_TYPE, mediaType)
                jsonObject.put("id", id)
            } catch (e: Exception) {
                Logger.w(e)
            }
            return jsonObject
        }

        var dynamicLinkUri: Uri? = null
        fun openShareFirebaseDynamicLinks(activity: Activity, title: String, assetId: Int, assetType: String, imgUrl: String, seriesId: String?, seasonNumber: String?) {
            try {
                //String imageURL = imgUrl + AppConstants.WIDTH + (int) activity.getResources().getDimension(R.dimen.width1) + AppConstants.HEIGHT + (int) activity.getResources().getDimension(R.dimen.height1) + AppConstants.QUALITY_IMAGE;
                //  Log.e("FinalUrl-->>in", imageURL);
                // Log.e("ImageUrl-->>in", imgUrl);
                val uri = createURI(title, assetId, assetType, imgUrl, activity)
                val shortLinkTask: Task<ShortDynamicLink> = FirebaseDynamicLinks.getInstance().createDynamicLink() // .setDomainUriPrefix("https://link.panteao.com")
                    .setLink(Uri.parse(uri))
                    .setDomainUriPrefix(BuildConstants.FIREBASE_DPLNK_PREFIX) //.setLink(Uri.parse(uri))
                    .setNavigationInfoParameters(
                        DynamicLink.NavigationInfoParameters.Builder().setForcedRedirectEnabled(true)
                            .build()
                    )
                    .setAndroidParameters(
                        DynamicLink.AndroidParameters.Builder(BuildConstants.FIREBASE_ANDROID_PACKAGE)
                            .build()
                    ) //TODO uncomment below line after getting ios package name.
                    //                    .setIosParameters(new DynamicLink.IosParameters.Builder(FirebaseConstants.FIREBASE_IOS_PACKAGE).build())
                    .setSocialMetaTagParameters(
                        DynamicLink.SocialMetaTagParameters.Builder()
                            .setTitle(title)
                            .setDescription(seasonNumber!!)
                            .setImageUrl(Uri.parse(imgUrl))
                            .build()
                    )
                    .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT)
                    .addOnCompleteListener(activity, OnCompleteListener<ShortDynamicLink> { task ->
                        if (task.isSuccessful) {
                            dynamicLinkUri = task.result.getShortLink()
                            val flowchartLink: Uri = task.result.getPreviewLink()!!
                            Log.e("dynamicUrl", dynamicLinkUri.toString() + flowchartLink)
                            // Log.e("flowchartLink", String.valueOf(flowchartLink));
                            try {
                                activity.runOnUiThread(Runnable {
                                    if (dynamicLinkUri != null) {
                                        val sharingIntent = Intent(Intent.ACTION_SEND)
                                        sharingIntent.setType("text/plain")
                                        sharingIntent.putExtra(
                                            Intent.EXTRA_TEXT,
                                            activity.getResources().getString(R.string.checkout) + " " + title + " " + activity.getResources()
                                                .getString(R.string.on_enveu) + "\n" + dynamicLinkUri.toString()
                                        )
                                        // sharingIntent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.checkout) + " " + asset.getName() + " " + activity.getResources().getString(R.string.on_Dialog) + "\n" + "https://stagingsott.page.link/?link="+dynamicLinkUri.toString()+"&apn=com.astro.stagingsott");
                                        activity.startActivity(Intent.createChooser(sharingIntent, activity.getResources().getString(R.string.detail_page_share)))
                                    }
                                })
                            } catch (e: Exception) {
                                Logger.e("openShareFirebaseDynamicLinks -> ", e.toString())
                            }
                        } else {
                            Logger.e("openShareFirebaseDynamicLinks -> ", task.exception.toString())
                        }
                    })
                shortLinkTask.toString()
            } catch (e: Exception) {
                Log.w("appcrashOnShare", e.message!!)
            }
        }

        private fun createURI(title: String, assetId: Int, assetType: String, imgUrl1: String, activity: Activity): String {
            var uri = ""
            uri = try {
                val assetId1 = assetId.toString() + ""
                val assetType1 = assetType + ""
                Uri.parse(BuildConstants.FIREBASE_DPLNK_URL)
                    .buildUpon()
                    .appendQueryParameter("id", assetId1)
                    .appendQueryParameter(CONTROL_PARAM_CONTENT_TYPE, assetType1)
                    .appendQueryParameter("image", imgUrl1)
                    .appendQueryParameter("name", title)
                    .appendQueryParameter("apn", BuildConstants.FIREBASE_ANDROID_PACKAGE)
                    .build().toString()
            } catch (ignored: Exception) {
                ""
            }
            return uri
        }

        fun getHomeRailData(commonRailData: List<PlaylistRailData>, adsRail: List<CommonRailData>, playlistsize: Int): List<CommonRailData> {
            val commonRailDataList: MutableList<CommonRailData> = ArrayList<CommonRailData>()
            val size = commonRailData.size
            var adsCounter = 0
            var listCounter = 0
            val loopSize: Int
            val listAds = commonRailData.size / 5
            loopSize = if (listAds > adsRail.size) {
                commonRailData.size + adsRail.size
            } else {
                commonRailData.size + listAds + 1
            }
            for (i in 0 until loopSize) {
                val commonData = CommonRailData()
                if (i == 0) {
                    val slides = ArrayList<Slide>()
                    val slideSize = Math.min(commonRailData[i].getData().getContents().size, 5)
                    for (j in 0 until slideSize) {
                        val slide = Slide()
                        commonData.setType(0)
                        slide.type = 1
                        if (commonRailData[i].getData().getContentType().equals(AppConstants.VOD, ignoreCase = true)) {
                            slide.imageFromUrl = getImageUrl(AppConstants.VOD, "CROUSEL") + commonRailData[i].getData().getContents().get(j).getLandscapeImage()
                        } else if (commonRailData[i].getData().getContentType().equals(AppConstants.SERIES, ignoreCase = true)) {
                            slide.imageFromUrl = getImageUrl(AppConstants.SERIES, "CROUSEL") + commonRailData[i].getData().getContents().get(j).getPicture()
                        } else if (commonRailData[i].getData().getContentType().equals(AppConstants.CAST_AND_CREW, ignoreCase = true)) {
                            slide.imageFromUrl = getImageUrl(AppConstants.CAST_AND_CREW, "CROUSEL") + commonRailData[i].getData().getContents().get(j).getPicture()
                        } else if (commonRailData[i].getData().getContentType().equals(AppConstants.GENRE, ignoreCase = true)) {
                            slide.imageFromUrl = getImageUrl(AppConstants.GENRE, "CROUSEL") + commonRailData[i].getData().getContents().get(j).getPicture()
                        }
                        slide.assetId = commonRailData[i].getData().getContents().get(j).getId()
                        slide.title = commonRailData[i].getData().getContents().get(j).getTitle()
                        slide.contents = commonRailData[i].getData().getContents().get(j)
                        slides.add(slide)
                    }
                    commonData.setSlides(slides)
                    commonData.setProgressType(playlistsize)
                    commonData.setRailData(commonRailData[i])
                    commonRailDataList.add(commonData)
                    listCounter = listCounter + 1
                } else {
                    if (i % 5 == 1) {
                        if (adsCounter < adsRail.size) {
                            adsRail[adsCounter].setType(4)
                            commonRailDataList.add(adsRail[adsCounter])
                            adsCounter = adsCounter + 1
                        } else {
                            if (listCounter < size) {
                                if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.SQUARE, ignoreCase = true)) {
                                    commonData.setType(1)
                                } else if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.POTRAIT, ignoreCase = true)) {
                                    commonData.setType(7) //default type common adapter
                                } else if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.CIRCLE, ignoreCase = true)) {
                                    commonData.setType(2)
                                } else if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.LANDSCAPE, ignoreCase = true)) {
                                    commonData.setType(3)
                                } else if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.POSTER_LANDSCAPE, ignoreCase = true)) {
                                    commonData.setType(5)
                                } else if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.POSTER_POTRAIT, ignoreCase = true)) {
                                    commonData.setType(6)
                                } else {
                                    commonData.setType(3)
                                }
                                commonData.setRailData(commonRailData[listCounter])
                                commonData.setProgressType(playlistsize)
                                commonRailDataList.add(commonData)
                                listCounter = listCounter + 1
                            }
                        }
                    } else {
                        if (listCounter < size) {
                            if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.SQUARE, ignoreCase = true)) {
                                commonData.setType(1)
                            } else if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.POTRAIT, ignoreCase = true)) {
                                commonData.setType(7) //default type
                            } else if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.CIRCLE, ignoreCase = true)) {
                                commonData.setType(2)
                            } else if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.LANDSCAPE, ignoreCase = true)) {
                                commonData.setType(3)
                            } //change for digital type 2:3
                            else if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.POSTER_LANDSCAPE, ignoreCase = true)) {
                                commonData.setType(5)
                            } else if (commonRailData[listCounter].getData().getContentImageType().equals(AppConstants.POSTER_POTRAIT, ignoreCase = true)) {
                                commonData.setType(6)
                            } else {
                                commonData.setType(3)
                            }
                            commonData.setRailData(commonRailData[listCounter])
                            commonData.setProgressType(playlistsize)
                            commonRailDataList.add(commonData)
                            listCounter = listCounter + 1
                        }
                    }
                }
            }
            return commonRailDataList
        }

        fun convertToMinutes(milliseconds: Long): String {
            var milliseconds = milliseconds
            var minutes = ""
            try {
                if (milliseconds % 1000 > 0) {
                    milliseconds = milliseconds + milliseconds % 1000
                }
                val minute = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
                Logger.w("episodeTiming $minute   ---   $milliseconds")
                minutes = String.format(Locale.getDefault(), "%02d", minute)
            } catch (ex: Exception) {
                Logger.w(ex)
            }
            return minutes
        }

        fun stringForTime(timeMs: Long): String {
            val formatBuilder = StringBuilder()
            val formatter = Formatter(formatBuilder, Locale.getDefault())
            val totalSeconds = (timeMs + 500) / 1000
            val seconds = totalSeconds % 60
            val minutes = totalSeconds / 60 % 60
            val hours = totalSeconds / 3600
            formatBuilder.setLength(0)
            return if (hours > 0) formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString() else formatter.format("%02d:%02d", minutes, seconds).toString()
        }

        fun calculateTime(milliseconds: Long): String {
            var milliseconds = milliseconds
            if (milliseconds % 1000 > 0) {
                milliseconds = milliseconds + milliseconds % 1000
            }
            val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
            val minute = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1)
            val second = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1)
            val strHour = String.format(Locale.getDefault(), "%02d", hours)
            val strMinute = String.format(Locale.getDefault(), "%02d", minute)
            val strSecond = String.format(Locale.getDefault(), "%02d", second)
            var showTime = "$minute:$strSecond"
            if (hours > 0) showTime = "$strHour:$strMinute:$strSecond" else if (minute > 0) showTime = "$strMinute:$strSecond" else if (second >= 0) showTime = "00:$strSecond"
            return showTime
        }

        fun getDuration(milliseconds: Long): String {
            var milliseconds = milliseconds
            if (milliseconds % 1000 > 0) {
                milliseconds = milliseconds + milliseconds % 1000
            }
            val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
            val minute = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % TimeUnit.HOURS.toMinutes(1)
            val second = TimeUnit.MILLISECONDS.toSeconds(milliseconds) % TimeUnit.MINUTES.toSeconds(1)
            val strHour = String.format(Locale.getDefault(), "%01d", hours)
            val strMinute = String.format(Locale.getDefault(), "%01d", minute)
            val strSecond = String.format(Locale.getDefault(), "%01d", second)
            var showTime = "$minute:$strSecond"
            if (hours > 0) showTime = strHour + "h " + strMinute + "m " + strSecond + "s" else if (minute > 0) showTime = strMinute + "m " + strSecond + "s" else if (second >= 0) showTime =
                "00:" + strSecond + "s"
            return showTime
        }

        @JvmStatic
        fun railBadgeVisibility(view: View, isVisible: Boolean) {
            if (isVisible) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.INVISIBLE
            }
        }

        fun setImage(oldUrl: String, imageSize: String): String {
            Logger.d("PRPosterImage-->>: $oldUrl $imageSize")
            val stringBuilder = StringBuilder()
            val urlImage = oldUrl.trim { it <= ' ' }
            val one: String = SDKConfig.getInstance().getWebPUrl()
            val two = imageSize + "/" + SDKConfig.WEBP_QUALITY
            stringBuilder.append(one).append(two).append(urlImage)
            Logger.d("ImageUrld-->>$one  $two $urlImage")
            Logger.d("-->>StringBilder$stringBuilder")
            return stringBuilder.toString()
        }

        fun callpreference(): ResponseConfig {
            val gson = Gson()
            val json: String = KsPreferenceKeys.getInstance().getAppPrefConfigResponse()
            return gson.fromJson<ResponseConfig>(json, ResponseConfig::class.java)
        }

        fun setProfilePic(preference: KsPreferenceKeys, context: Context?, key: String, imageView: CircleImageView?) {
            try {
                if (StringUtils.isNullOrEmptyOrZero(key)) {
                    ImageHelper.getInstance(context)
                        .loadImageToProfile(imageView, "")
                } else {
                    val stringBuilder = StringBuilder()
                    var url1: String = preference.getAppPrefCfep()
                    if (key.contains("http")) {
                        stringBuilder.append(url1).append("/").append(key)
                    } else {
                        if (StringUtils.isNullOrEmpty(url1)) {
                            url1 = urlPoints
                            preference.setAppPrefCfep(url1)
                        }
                        val url2: String = AppConstants.PROFILE_FOLDER
                        stringBuilder.append(url1).append(url2).append(key)
                    }
                    ImageHelper.getInstance(context)
                        .loadImageToProfile(imageView, stringBuilder.toString())
                }
            } catch (e: Exception) {
                Logger.e("AppCommonMEthod", "setProfilePic" + e.message)
            }
        }

        @JvmStatic
        @SuppressLint("HardwareIds")
        fun getDeviceId(contentResolver: ContentResolver?): String {
            return Settings.Secure.getString(
                contentResolver,
                Settings.Secure.ANDROID_ID
            )
        }

        @JvmStatic
        fun getGenre(videosItem: EnveuVideoItemBean): String {
            var stringBuilder = StringBuilder()
            if (videosItem.assetGenres != null && !videosItem.assetGenres.isEmpty()) {
                for (i in videosItem.assetGenres.indices) {
                    stringBuilder = if (i == videosItem.assetGenres.size - 1) {
                        stringBuilder.append(videosItem.assetGenres[i])
                    } else stringBuilder.append(videosItem.assetGenres[i]).append(", ")
                }
            }
            return stringBuilder.toString()
        }

        @JvmStatic
        val speciesList: String
            get() {
                var cast = ""
                var speciesBuilder = StringBuilder()
                for (i in KsPreferenceKeys.getInstance().getSpeciesList().indices) {
                    speciesBuilder = speciesBuilder.append(KsPreferenceKeys.getInstance().getSpeciesList().get(i)).append(",")
                }
                if (speciesBuilder.length > 0) {
                    cast = speciesBuilder.toString()
                    cast = cast.substring(0, cast.length - 1)
                } else {
                    cast = ""
                }
                Logger.w("speciesList", cast)
                return cast
            }

        fun getSpeciesListFromContentPreferences(list: List<String>, isType: Boolean): String {
            var cast = ""
            if (list.size > 0) {
                var speciesBuilder = StringBuilder()
                for (i in list.indices) {
                    if (KsPreferenceKeys.getInstance().getPreferenceProfileId().equals("hunt01", ignoreCase = true)) {
                        cast = if (isType) {
                            getContentPreferenceForTypes(list[i], configResponse.data.appConfig.contentPreferences.hunting.types)
                        } else {
                            getContentPreferenceForSpecies(list[i], configResponse.data.appConfig.contentPreferences.hunting.species)
                        }
                    } else if (KsPreferenceKeys.getInstance().getPreferenceProfileId().equals("fish01", ignoreCase = true)) {
                        cast = if (isType) {
                            getContentPreferenceForTypes(list[i], configResponse.data.appConfig.contentPreferences.fishing.types)
                        } else {
                            getContentPreferenceForSpecies(list[i], configResponse.data.appConfig.contentPreferences.fishing.species)
                        }
                    } else if (KsPreferenceKeys.getInstance().getPreferenceProfileId().equals("both01", ignoreCase = true)) {
                        cast = if (isType) {
                            getContentPreferenceForTypes(list[i], configResponse.data.appConfig.contentPreferences.both.types)
                        } else {
                            getContentPreferenceForSpecies(list[i], configResponse.data.appConfig.contentPreferences.both.species)
                        }
                    }
                    speciesBuilder = speciesBuilder.append(cast).append(",")
                    cast = speciesBuilder.toString()
                    //                if (speciesBuilder.length() > 0) {
//                    cast = speciesBuilder.toString();
//                   // cast = cast.substring(0, cast.length());
//                } else {
//                    cast = "";
//                }
                }
            }
            return cast
        }

        private fun getContentPreferenceForSpecies(speciesId: String, species: List<SpeciesItem>): String {
            var speciesList = ""
            try {
                val locale: String = LanguageLayer.getCurrentLanguageCode()
                for (i in species.indices) {
                    if (speciesId.equals(species[i].getId(), ignoreCase = true)) {
                        speciesList = if (locale.equals("en-US", ignoreCase = true)) {
                            species[i].getEnUS()
                        } else {
                            species[i].getEsES()
                        }
                    }
                }
            } catch (e: Exception) {
                Logger.w(e)
            }
            return speciesList
        }

        private fun getContentPreferenceForTypes(typesId: String, typesItems: List<TypesItem>): String {
            var typeList = ""
            try {
                val locale: String = LanguageLayer.getCurrentLanguageCode()
                for (i in typesItems.indices) {
                    if (typesId.equals(typesItems[i].getId(), ignoreCase = true)) {
                        typeList = if (locale.equals("en-US", ignoreCase = true)) {
                            typesItems[i].getEnUS()
                        } else {
                            typesItems[i].getEsES()
                        }
                    }
                }
            } catch (e: Exception) {
                Logger.w(e)
            }
            return typeList
        }

        fun getSpeciesListFromIntent(numberList: ArrayList<String?>): String {
            var cast = ""
            var speciesBuilder = StringBuilder()
            for (i in numberList.indices) {
                speciesBuilder = speciesBuilder.append(numberList[i]).append(",")
            }
            if (speciesBuilder.length > 0) {
                cast = speciesBuilder.toString()
                cast = cast.substring(0, cast.length - 1)
            } else {
                cast = ""
            }
            Logger.w("speciesList", cast)
            return cast
        }

        @JvmStatic
        val typeList: String
            get() {
                var cast = ""
                var speciesBuilder = StringBuilder()
                for (i in KsPreferenceKeys.getInstance().getTypeList().indices) {
                    speciesBuilder = speciesBuilder.append(KsPreferenceKeys.getInstance().getTypeList().get(i)).append(",")
                    Logger.e("dgdgdgddgdg", speciesBuilder.toString())
                }
                if (speciesBuilder.length > 0) {
                    cast = speciesBuilder.toString()
                    cast = cast.substring(0, cast.length - 1)
                } else {
                    cast = ""
                }
                Logger.w("speciesList", cast)
                return cast
            }

        fun getListViewType(contentImageType: String): Int {
            return if (contentImageType.equals(ImageType.CIR.name, ignoreCase = true)) {
                0
            } else if (contentImageType.equals(ImageType.LDS.name, ignoreCase = true)) {
                1
            } else if (contentImageType.equals(ImageType.PR1.name, ignoreCase = true)) {
                2
            } else if (contentImageType.equals(ImageType.PR2.name, ignoreCase = true)) {
                3
            } else if (contentImageType.equals(ImageType.SQR.name, ignoreCase = true)) {
                4
            } else {
                1
            }
        }

        fun getBranchUrl(posterURL: String, context: Context): String {
            Logger.d("PRPosterImage-->>$posterURL")
            val w = context.resources.getDimension(R.dimen.splash_width).toInt()
            val h = context.resources.getDimension(R.dimen.splash_width).toInt()
            return setImage(posterURL, w.toString() + "x" + h)
        }

        @JvmStatic
        fun getListPRImage(posterURL: String, context: Context): String {
            Logger.d("PRPosterImage-->>$posterURL")
            val w = context.resources.getInteger(R.integer.portrait_image_width)
            val h = context.resources.getInteger(R.integer.portrait_image_height)
            return setImage(posterURL, w.toString() + "x" + h)
        }

        @JvmStatic
        fun getListPRTwoImage(posterURL: String, context: Context): String {
            Logger.d("PRPosterImage-->>$posterURL")
            return setImage(
                posterURL, context.resources.getInteger(R.integer.portrait_image_width)
                    .toString() + "x" + context.resources.getInteger(R.integer.portrait_image_height)
            )
        }

        @JvmStatic
        fun getListCIRCLEImage(posterURL: String, context: Context): String {
            Logger.d("PRPosterImage-->>$posterURL")
            val w = context.resources.getDimension(R.dimen.circle_image_width).toInt()
            val h = context.resources.getDimension(R.dimen.circle_image_height).toInt()
            return setImage(posterURL, w.toString() + "x" + h)
        }

        @JvmStatic
        fun getListLDSImage(posterURL: String, context: Context): String {
            Logger.d("PRPosterImage-->>$posterURL")
            return setImage(
                posterURL, context.resources.getInteger(R.integer.landscape_image_width)
                    .toString() + "x" + context.resources.getInteger(R.integer.landscape_image_height)
            )
        }

        @JvmStatic
        fun getListSQRImage(posterURL: String, context: Context): String {
            Logger.d("PRPosterImage-->>$posterURL")
            val w = context.resources.getDimension(R.dimen.square_image_width).toInt()
            val h = context.resources.getDimension(R.dimen.square_image_height).toInt()
            return setImage(posterURL, w.toString() + "x" + h)
        }

        fun getListPRTwoImg(posterURL: String, context: Context): String {
            Logger.d("PRPosterImage-->>$posterURL")
            val w = context.resources.getDimension(R.dimen.search_toolbar_height_237dp).toInt()
            val h = context.resources.getDimension(R.dimen.search_toolbar_height_65dp).toInt()
            return setImage(posterURL, w.toString() + "x" + h)
        }

        @JvmStatic
        fun launchDetailScreen(context: Context?, videoId: Long?, screenType: String, id: Int, duration: String?, isPremium: Boolean) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                return
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            Logger.e("MediaTYpeIs", screenType)
            if (screenType.uppercase(Locale.getDefault()).equals(MediaTypeConstants.VIDEO, ignoreCase = true)) {
                ActivityLauncher.getInstance().detailScreenBrightCove(context as BaseActivity?, DetailActivity::class.java, id)
            } else {
                if (screenType.uppercase(Locale.getDefault()).equals(AppConstants.CUSTOM, ignoreCase = true)) {
                    ActivityLauncher.getInstance().detailScreenBrightCove(context as BaseActivity?, DetailActivity::class.java, id)
                }
            }
        }

        @JvmStatic
        fun launchDetailScreenFromSearch(
            context: Context,
            screenType: String,
            id: Int,
            customContentType: String,
            videoType: String?,
            trailerReferenceId: String?,
            externalRefId: String,
            isPremium: Boolean,
            skuId: String?,
            isParentContentNull: Boolean,
            tittle: String?,
            isHosted: Boolean,
            externalUrl: String,
            posterUrl: String
        ) {
            //checkLoginStatus(context);
            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                return
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            var screenFrom = ""
            screenFrom = context.toString()
            if (screenFrom.contains("HomeSliderActivity")) {
                screenFrom = "HomeSliderActivity"
            } else if (screenFrom.contains("ActivitySearch")) {
                screenFrom = "ActivitySearch"
            } else if (screenFrom.contains("WatchListActivity")) {
                screenFrom = "WatchListActivity"
            }
            MoEngageContentSelectTrack(OttApplication.getContext(), screenFrom, "", tittle, id.toString(), tittle, screenType, AppConstants.CONTENT_SELECT)
            Logger.e("MediaTYpeIs", screenType)
            if (screenType.uppercase(Locale.getDefault()).equals(MediaTypeConstants.VIDEO, ignoreCase = true)) {
                if (videoType != null && !videoType.equals("", ignoreCase = true)) {
                    if (videoType.equals(MediaTypeConstants.getInstance().getSeries(), ignoreCase = true)) {
                        ActivityLauncher.getInstance().seriesDetailScreen(context as BaseActivity, SeriesDetailActivity::class.java, id)
                    } else if (videoType.equals(MediaTypeConstants.getInstance().getMovie(), ignoreCase = true)) {
                        ActivityLauncher.getInstance().detailScreenBrightCove(context as BaseActivity, DetailActivity::class.java, id)
                    } else if (videoType.equals(MediaTypeConstants.getInstance().getEpisode(), ignoreCase = true)) {
                        ActivityLauncher.getInstance().episodeScreenBrightcove(context as BaseActivity, EpisodeActivity::class.java, id)
                    } else if (videoType.equals(MediaTypeConstants.getInstance().getShow(), ignoreCase = true)) {
                        showRedirection(context, externalRefId, isPremium, skuId, id, tittle, screenType, false, posterUrl)
                    } else if (videoType.equals(MediaTypeConstants.getInstance().getTrailer(), ignoreCase = true)) {
                        if (checkLoggedInAndUserVerifyCondition(AppConstants.LOGGED_IN)) {
                            verifyTrailerCondition(context, externalRefId, id, tittle, screenType, posterUrl)
                        } else {
                            ActivityLauncher.getInstance().loginActivity(context as BaseActivity, ActivityLogin::class.java)
                        }
                    }
                }
            } else if (screenType.equals(AppConstants.LIVE, ignoreCase = true)) {
                liveRedirection(context, externalRefId, isPremium, skuId, id, tittle, screenType, true, isHosted, externalUrl)
            } else {
                if (screenType.equals(AppConstants.CUSTOM, ignoreCase = true)) {
                    if (customContentType.equals(MediaTypeConstants.getInstance().getContest(), ignoreCase = true)) {
                        // ActivityLauncher.getInstance().goToContestDetail((BaseActivity) context, Detail.class,id,"fromContest");
                    } else if (customContentType.equals(MediaTypeConstants.getInstance().getEvent(), ignoreCase = true)) {
                        //  ActivityLauncher.getInstance().goToContestDetail((BaseActivity) context, Detail.class,id,"fromEvent");
                    } else if (customContentType.equals(MediaTypeConstants.getInstance().getAgency(), ignoreCase = true)) {
                        //  ActivityLauncher.getInstance().goToContestDetail((BaseActivity) context, Detail.class,id);
                    } else if (customContentType.equals(MediaTypeConstants.getInstance().getOffer(), ignoreCase = true)) {
                        //  ActivityLauncher.getInstance().goToContestDetail((BaseActivity) context, Detail.class,id);
                    } else if (customContentType.equals(MediaTypeConstants.getInstance().getNews(), ignoreCase = true)) {
                        //   ActivityLauncher.getInstance().goToNewsDetail((BaseActivity) context, DetailsNew.class,id);
                    } else if (customContentType.equals(MediaTypeConstants.getInstance().getExpedition(), ignoreCase = true)) {
//                    ActivityLauncher.getInstance().goToExpeditionDetail((BaseActivity) context, ExpeditionDetail.class,id,true);
                    }
                }
            }
        }

        private fun checkLoggedInAndUserVerifyCondition(loggedIn: String): Boolean {
            var isLoggedIn = false
            val preference: KsPreferenceKeys
            preference = KsPreferenceKeys.getInstance()
            if (loggedIn.equals(AppConstants.LOGGED_IN, ignoreCase = true)) {
                if (preference.getAppPrefLoginStatus().equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                    isLoggedIn = true
                }
            } else if (loggedIn.equals(AppConstants.USER_VERIFY, ignoreCase = true)) {
                isLoggedIn = java.lang.Boolean.parseBoolean(preference.isVerified())
            }
            return isLoggedIn
        }

        private fun showRedirection(
            context: Context,
            externalRefId: String,
            isPremium: Boolean,
            sku: String?,
            id: Int,
            tittle: String?,
            assetType: String,
            isIntentFromLive: Boolean,
            posterUrl: String
        ) {
            val stringsHelper = StringsHelper
            var playback_url = ""
            var isLoggedIn = false
            val preference: KsPreferenceKeys
            preference = KsPreferenceKeys.getInstance()
            val token: String
            token = preference.getAppPrefAccessToken()
            if (preference.getAppPrefLoginStatus().equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                isLoggedIn = true
            }
            val isUserVerified: String
            isUserVerified = preference.isVerified()
            if (isLoggedIn) {
                if (!isPremium) {
                    if (isUserVerified.equals("true", ignoreCase = true)) {
                        if (!StringUtils.isNullOrEmpty(externalRefId)) {
                            callMoEngageUserTypeSubscription(OttApplication.getContext(), AppConstants.PAID_USER)
                            playback_url = SDKConfig.getInstance().playbacK_URL + (externalRefId) + (".m3u8")
                            startPlayer(context, playback_url, false, id, isIntentFromLive, tittle, assetType, posterUrl)
                        }
                    } else {
                        isUserNotVerify = true
                        createShowDialog(
                            "",
                            stringsHelper.stringParse(
                                Objects.requireNonNull<StringsData>(stringsHelper.instance()).data.config.popup_user_not_verify,
                                context.getString(R.string.popup_user_not_verify)
                            ),
                            context.getString(R.string.ok),
                            context
                        )
                    }
                } else {
                    if (sku != null) {
                        EntitlementLayer.getInstance().hitApiEntitlement(token, sku).observe(context as LifecycleOwner, Observer<ResponseEntitle> { responseEntitle: ResponseEntitle? ->
                            if (null != responseEntitle && null != responseEntitle.getData()) {
                                var playback_url1 = ""
                                resEntitle = responseEntitle
                                if (responseEntitle.getData().getEntitled()) {
                                    if (isUserVerified.equals("true", ignoreCase = true)) {
                                        if (null != responseEntitle.getData().getExternalRefId()) {
                                            callMoEngageUserTypeSubscription(OttApplication.getContext(), AppConstants.PAID_USER)
                                            playback_url1 = SDKConfig.getInstance().getPLAYBACK_URL()+(responseEntitle.getData().getExternalRefId())+(".m3u8")
                                            startPlayer(context, playback_url1, false, id, isIntentFromLive, tittle, assetType, posterUrl)
                                        }
                                    } else {
                                        isUserNotVerify = true
                                        createShowDialog(
                                            "",
                                            stringsHelper.stringParse(
                                                Objects.requireNonNull<StringsData>(stringsHelper.instance()).data.config.popup_user_not_verify,
                                                context.getString(R.string.popup_user_not_verify)
                                            ),
                                            context.getString(R.string.ok),
                                            context
                                        )
                                    }
                                } else {
                                    callMoEngageUserTypeSubscription(OttApplication.getContext(), AppConstants.FREE_USER)
                                    isUserNotEntitle = true
                                    createShowDialog("", context.getString(R.string.select_plan), context.getString(R.string.purchase_option), context)
                                }
                            } else {
                                if (responseEntitle?.getResponseCode() != null && responseEntitle.getResponseCode() == 4302) {
                                    clearCredientials(preference, context)
                                    ActivityLauncher.getInstance().loginActivity(context as BaseActivity, ActivityLogin::class.java)
                                } else {
                                    createShowDialog("", context.getString(R.string.something_went_wrong), context.getString(R.string.countinue), context)
                                }
                            }
                        })
                    }
                }
            } else {
                ActivityLauncher.getInstance().loginActivity(context as BaseActivity, ActivityLogin::class.java)
            }
        }

        fun clearCredientials(preference: KsPreferenceKeys, context: Context) {
            try {
                val json: String = KsPreferenceKeys.getInstance().getString("DMS_Response", "")
                val isFacebook: String = preference.getAppPrefLoginType()
                if (isFacebook.equals(AppConstants.UserLoginType.FbLogin.toString(), ignoreCase = true)) {
                    LoginManager.getInstance().logOut()
                }
                val strCurrentTheme: String = KsPreferenceKeys.getInstance().getCurrentTheme()
                val strCurrentLanguage: String = KsPreferenceKeys.getInstance().getAppLanguage()
                val strSubscriptionURL: String = KsPreferenceKeys.getInstance().getSUBSCRIPTION_BASE_URL()
                val strPaymentURL: String = KsPreferenceKeys.getInstance().getPAYMENT_BASE_URL()
                val isBingeWatchEnable: Boolean = KsPreferenceKeys.getInstance().getBingeWatchEnable()
                preference.setAppPrefRegisterStatus(AppConstants.UserStatus.Logout.toString())
                preference.clear()
                KsPreferenceKeys.getInstance().setString("DMS_Response", json)
                KsPreferenceKeys.getInstance().setfirstTimeUserForKidsPIn(false)
                KsPreferenceKeys.getInstance().setSUBSCRIPTION_BASE_URL(strSubscriptionURL)
                KsPreferenceKeys.getInstance().setPAYMENT_BASE_URL(strPaymentURL)
                KsPreferenceKeys.getInstance().setCurrentTheme(strCurrentTheme)
                KsPreferenceKeys.getInstance().setAppLanguage(strCurrentLanguage)
                updateLanguage(strCurrentLanguage, context as BaseActivity)
                KsPreferenceKeys.getInstance().setBingeWatchEnable(isBingeWatchEnable)
                val preferenceProfileId = configResponse.data.appConfig.contentPreferences.both.id
                if (StringUtils.isNullOrEmpty(preferenceProfileId)) {
                    KsPreferenceKeys.getInstance().preferenceProfileId = preferenceProfileId
                } else {
                    KsPreferenceKeys.getInstance().setPreferenceProfileId("both01")
                }
            } catch (e: Exception) {
                Logger.w(e)
            }
        }

        private fun verifyTrailerCondition(context: Context, externalRefId: String, id: Int, tittle: String?, assetType: String, posterUrl: String) {
            val stringsHelper = StringsHelper
            var trailer_url = ""
            trailer_url = SDKConfig.getInstance().getPLAYBACK_URL()+(externalRefId)+(".m3u8")
            if (checkLoggedInAndUserVerifyCondition(AppConstants.USER_VERIFY)) {
                startPlayer(context, trailer_url, true, id, false, tittle, assetType, posterUrl)
            } else {
                isUserNotVerify = true
                createShowDialog(
                    "",
                    stringsHelper.stringParse(
                        Objects.requireNonNull<StringsData>(stringsHelper.instance()).data.config.popup_user_not_verify,
                        context.getString(R.string.popup_user_not_verify)
                    ),
                    context.getString(R.string.ok),
                    context
                )
            }
        }

        private fun startPlayer(context: Context, playback_url: String?, isTrailer: Boolean, id: Int, isIntentFromLive: Boolean, tittle: String?, assetType: String, posterUrl: String) {
            if (null != playback_url && !playback_url.isEmpty()) {
                if (isIntentFromLive) {
                    ActivityLauncher.getInstance()
                        .launchPlayerActitivity(context as Activity, PlayerActivity::class.java, playback_url, false, null, id, tittle, assetType, isTrailer, true, posterUrl, AppConstants.home)
                } else {
                    ActivityLauncher.getInstance()
                        .launchPlayerActitivity(context as Activity, PlayerActivity::class.java, playback_url, false, null, id, tittle, assetType, isTrailer, false, posterUrl, AppConstants.home)
                }
            } else {
                createShowDialog("", context.getString(R.string.something_went_wrong), context.getString(R.string.countinue), context)
            }
        }

        fun redirectOnVideoDetailPages(context: Context, railCommonData: RailCommonData, position: Int, screenType: String, posterUrl: String) {
            Logger.e("MediaTYpeIs", screenType)
            if (screenType.uppercase(Locale.getDefault()).equals(MediaTypeConstants.getInstance().getShow(), ignoreCase = true)) {
                val externalRefId: String = railCommonData.getEnveuVideoItemBeans().get(position).getExternalRefId()
                val isPremium: Boolean = railCommonData.getEnveuVideoItemBeans().get(position).isPremium()
                val skuId: String = railCommonData.getEnveuVideoItemBeans().get(position).getSku()
                showRedirection(
                    context,
                    externalRefId,
                    isPremium,
                    skuId,
                    railCommonData.getEnveuVideoItemBeans().get(position).getId(),
                    railCommonData.getEnveuVideoItemBeans().get(position).getTitle(),
                    railCommonData.getEnveuVideoItemBeans().get(position).getAssetType(),
                    false,
                    posterUrl
                )
            } else if (screenType.uppercase(Locale.getDefault()).equals(MediaTypeConstants.getInstance().getEpisode(), ignoreCase = true)) {
                ActivityLauncher.getInstance().episodeScreenBrightcove(context as BaseActivity, EpisodeActivity::class.java, railCommonData.getEnveuVideoItemBeans().get(position).getId())
            } else if (screenType.uppercase(Locale.getDefault()).equals(MediaTypeConstants.getInstance().getSeries(), ignoreCase = true)) {
                ActivityLauncher.getInstance().seriesDetailScreen(context as BaseActivity, SeriesDetailActivity::class.java, railCommonData.getEnveuVideoItemBeans().get(position).getId())
            } else if (screenType.uppercase(Locale.getDefault()).equals(MediaTypeConstants.getInstance().getMovie(), ignoreCase = true)) {
                ActivityLauncher.getInstance().detailScreenBrightCove(context as BaseActivity, DetailActivity::class.java, railCommonData.getEnveuVideoItemBeans().get(position).getId())
            } else if (screenType.uppercase(Locale.getDefault()).equals(MediaTypeConstants.getInstance().getTrailer(), ignoreCase = true)) {
                val externalRefId: String = railCommonData.getEnveuVideoItemBeans().get(position).getExternalRefId()
                if (checkLoggedInAndUserVerifyCondition(AppConstants.LOGGED_IN)) {
                    if (!StringUtils.isNullOrEmpty(externalRefId)) {
                        verifyTrailerCondition(
                            context,
                            externalRefId,
                            railCommonData.getEnveuVideoItemBeans().get(position).getId(),
                            railCommonData.getEnveuVideoItemBeans().get(position).getTitle(),
                            railCommonData.getEnveuVideoItemBeans().get(position).getAssetType(),
                            posterUrl
                        )
                    } else {
                        createShowDialog("", context.getString(R.string.something_went_wrong), context.getString(R.string.countinue), context)
                    }
                } else {
                    ActivityLauncher.getInstance().loginActivity(context as BaseActivity, ActivityLogin::class.java)
                }
            }
        }

        fun redirectOnDetailPages(context: Context, railCommonData: RailCommonData, position: Int, mediaType: String) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                return
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            try {
                if (mediaType.equals(AppConstants.VIDEO, ignoreCase = true)) {
                    redirectOnVideoDetailPages(
                        context,
                        railCommonData,
                        position,
                        railCommonData.getEnveuVideoItemBeans().get(position).getVideoDetails().getVideoType(),
                        railCommonData.getEnveuVideoItemBeans().get(position).getPosterURL()
                    )
                } else if (mediaType.equals(AppConstants.CUSTOM, ignoreCase = true)) {
                    redirectOnCustomDetailPages(context, railCommonData, position, railCommonData.getEnveuVideoItemBeans().get(position).getCustomContent().getCustomType())
                } else if (mediaType.equals(AppConstants.LIVE, ignoreCase = true)) {
                    redirectOnLivePages(context, railCommonData, position, mediaType)
                }
            } catch (e: Exception) {
                Logger.w(e)
            }
        }

        fun redirectOnLivePages(context: Context, railCommonData: RailCommonData, position: Int, screenType: String) {
            if (screenType.equals(AppConstants.LIVE, ignoreCase = true)) {
                var isHosted = false
                var externalUrl = ""
                var externalRefId = ""
                if (railCommonData.getEnveuVideoItemBeans().get(position).getExternalRefId() != null) {
                    externalRefId = railCommonData.getEnveuVideoItemBeans().get(position).getExternalRefId()
                }
                val isPremium: Boolean = railCommonData.getEnveuVideoItemBeans().get(position).isPremium()
                val skuId: String = railCommonData.getEnveuVideoItemBeans().get(position).getSku()
                if (java.lang.Boolean.TRUE == railCommonData.getEnveuVideoItemBeans().get(position).getLiveContent().isHosted) {
                    isHosted = true
                } else {
                    if (railCommonData.getEnveuVideoItemBeans().get(position).getLiveContent().externalUrl != null) {
                        externalUrl = railCommonData.getEnveuVideoItemBeans().get(position).getLiveContent().externalUrl!!
                    }
                }
                liveRedirection(
                    context,
                    externalRefId,
                    isPremium,
                    skuId,
                    railCommonData.getEnveuVideoItemBeans().get(position).getId(),
                    railCommonData.getEnveuVideoItemBeans().get(position).getTitle(),
                    railCommonData.getEnveuVideoItemBeans().get(position).getAssetType(),
                    true,
                    isHosted,
                    externalUrl
                )
            }
        }

        private fun liveRedirection(
            context: Context,
            externalRefId: String,
            isPremium: Boolean,
            sku: String?,
            id: Int,
            tittle: String?,
            assetType: String,
            isIntentFromLive: Boolean,
            isHosted: Boolean,
            externalUrl: String
        ) {
            val stringsHelper = StringsHelper
            var playback_url = ""
            var isLoggedIn = false
            val preference: KsPreferenceKeys
            preference = KsPreferenceKeys.getInstance()
            val token: String
            token = preference.getAppPrefAccessToken()
            if (preference.getAppPrefLoginStatus().equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                isLoggedIn = true
            }
            val isUserVerified: String
            isUserVerified = preference.isVerified()
            if (isLoggedIn) {
                if (!isPremium) {
                    if (isUserVerified.equals("true", ignoreCase = true)) {
                        if (isHosted) {
                            if (!StringUtils.isNullOrEmpty(externalRefId)) {
                                callMoEngageUserTypeSubscription(OttApplication.getContext(), AppConstants.PAID_USER)
                                playback_url = SDKConfig.getInstance().getLivePlayBackUrl()+(externalRefId)+(".m3u8")
                            }
                        } else {
                            playback_url = externalUrl
                        }
                        startPlayer(context, playback_url, false, id, isIntentFromLive, tittle, assetType, "")
                    } else {
                        isUserNotVerify = true
                        createShowDialog(
                            "",
                            stringsHelper.stringParse(
                                Objects.requireNonNull<StringsData>(stringsHelper.instance()).data.config.popup_user_not_verify,
                                context.getString(R.string.popup_user_not_verify)
                            ),
                            context.getString(R.string.ok),
                            context
                        )
                    }
                } else {
                    if (sku != null) {
                        EntitlementLayer.getInstance().hitApiEntitlement(token, sku).observe(context as LifecycleOwner, Observer<ResponseEntitle> { responseEntitle: ResponseEntitle? ->
                            if (null != responseEntitle && null != responseEntitle.getData()) {
                                var playback_url1 = ""
                                resEntitle = responseEntitle
                                if (responseEntitle.getData().getEntitled()) {
                                    if (isUserVerified.equals("true", ignoreCase = true)) {
                                        if (isHosted) {
                                            if (!responseEntitle.getData().getExternalRefId().equals("", ignoreCase = true)) {
                                                callMoEngageUserTypeSubscription(OttApplication.getContext(), AppConstants.PAID_USER)
                                                playback_url1 = SDKConfig.getInstance().getPLAYBACK_URL()+(responseEntitle.getData().getExternalRefId())+(".m3u8")
                                            }
                                        } else {
                                            if (responseEntitle.getData().getExternalUrl() != null) {
                                                playback_url1 = responseEntitle.getData().getExternalUrl()
                                            }
                                        }
                                        startPlayer(context, playback_url1, false, id, isIntentFromLive, tittle, assetType, "")
                                    } else {
                                        isUserNotVerify = true
                                        createShowDialog(
                                            "",
                                            stringsHelper.stringParse(
                                                Objects.requireNonNull<StringsData>(stringsHelper.instance()).data.config.popup_user_not_verify,
                                                context.getString(R.string.popup_user_not_verify)
                                            ),
                                            context.getString(R.string.ok),
                                            context
                                        )
                                    }
                                } else {
                                    callMoEngageUserTypeSubscription(OttApplication.getContext(), AppConstants.FREE_USER)
                                    isUserNotEntitle = true
                                    createShowDialog("", context.getString(R.string.select_plan), context.getString(R.string.purchase_option), context)
                                }
                            } else {
                                if (responseEntitle?.getResponseCode() != null && responseEntitle.getResponseCode() == 4302) {
                                    clearCredientials(preference, context)
                                    ActivityLauncher.getInstance().loginActivity(context as BaseActivity, ActivityLogin::class.java)
                                } else {
                                    createShowDialog("", context.getString(R.string.something_went_wrong), context.getString(R.string.countinue), context)
                                }
                            }
                        })
                    }
                }
            } else {
                ActivityLauncher.getInstance().loginActivity(context as BaseActivity, ActivityLogin::class.java)
            }
        }

        fun redirectOnCustomDetailPages(context: Context?, railCommonData: RailCommonData?, position: Int, customType: String) {
            if (customType.equals(MediaTypeConstants.getInstance().getContest(), ignoreCase = true)) {
                //   ActivityLauncher.getInstance().goToContestDetail((BaseActivity) context, Detail.class,railCommonData.getEnveuVideoItemBeans().get(position).getId(),"fromContest");
            } else if (customType.equals(MediaTypeConstants.getInstance().getEvent(), ignoreCase = true)) {
                //  ActivityLauncher.getInstance().goToContestDetail((BaseActivity) context, Detail.class,railCommonData.getEnveuVideoItemBeans().get(position).getId(),"fromEvent");
            } else if (customType.equals(MediaTypeConstants.getInstance().getAgency(), ignoreCase = true)) {
                // ActivityLauncher.getInstance().goToContestDetail((BaseActivity) context, Detail.class,railCommonData.getEnveuVideoItemBeans().get(position).getId(),"");
            } else if (customType.equals(MediaTypeConstants.getInstance().getOffer(), ignoreCase = true)) {
                //  ActivityLauncher.getInstance().goToContestDetail((BaseActivity) context, Detail.class,railCommonData.getEnveuVideoItemBeans().get(position).getId());
            } else if (customType.equals(MediaTypeConstants.getInstance().getNews(), ignoreCase = true)) {
                //   ActivityLauncher.getInstance().goToNewsDetail((BaseActivity) context, DetailsNew.class,railCommonData.getEnveuVideoItemBeans().get(position).getId());
            } else if (customType.equals(MediaTypeConstants.getInstance().getExpedition(), ignoreCase = true)) {
//            ActivityLauncher.getInstance().goToExpeditionDetail((BaseActivity) context, ExpeditionDetail.class,railCommonData.getEnveuVideoItemBeans().get(position).getId(),true);
            }
        }

        @JvmStatic
        fun trackFcmEvent(title: String?, assetType: String, activity: Context?, position: Int) {
            try {
                val requestParam = JsonObject()
                requestParam.addProperty(EventConstant.Name, title)
                if (assetType.uppercase(Locale.getDefault()).equals(MediaTypeConstants.getInstance().getMovie(), ignoreCase = true)) {
                    requestParam.addProperty(EventConstant.ContentType, assetType)
                } else if (assetType.uppercase(Locale.getDefault()).equals(MediaTypeConstants.getInstance().getShow(), ignoreCase = true)) {
                    requestParam.addProperty(EventConstant.ContentType, assetType)
                } else if (assetType.uppercase(Locale.getDefault()).equals(MediaTypeConstants.getInstance().getEpisode(), ignoreCase = true)) {
                    requestParam.addProperty(EventConstant.ContentType, assetType)
                } else if (assetType.uppercase(Locale.getDefault()).equals(MediaTypeConstants.getInstance().getSeries(), ignoreCase = true)) {
                    requestParam.addProperty(EventConstant.ContentType, assetType)
                } else {
                    requestParam.addProperty(EventConstant.ContentType, "")
                }

                // FCMEvents.getInstance().setContext(activity).trackEvent(3, requestParam);
            } catch (e: Exception) {
                Logger.w(e)
            }
        }

        fun getUserName(name: String?): String {
            var name = name
            var value = ""
            try {
                if (name != null) {
                    if (name != "") {
                        name = name.trim { it <= ' ' }.replace("\\s+".toRegex(), " ")
                        if (name.contains(" ")) {
                            val words = name.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            if (words.size != 0) {
                                val firstWord = words[0][0].toString().uppercase(Locale.getDefault())
                                value = if (words.size == 1) {
                                    firstWord
                                } else {
                                    val secondWord = words[1][0].toString().uppercase(Locale.getDefault())
                                    firstWord + secondWord
                                }
                            }
                        } else {
                            value = name[0].toString().uppercase(Locale.getDefault()) + "" + name[1].toString().uppercase(Locale.getDefault())
                        }
                    }
                }
            } catch (e: Exception) {
                Logger.w(e)
            }
            return value
        }

        fun trackSearchFcmEvent(activity: Context?, searchKeyword: String?) {
            try {
                val requestParam = JsonObject()
                requestParam.addProperty(EventConstant.SearchTitle, searchKeyword)
                FCMEvents.getInstance().setContext(activity).trackEvent(4, requestParam)
            } catch (e: Exception) {
                Logger.w(e)
            }
        }

        fun updateLanguage(context: Context) {
            if (KsPreferenceKeys.getInstance().getAppLanguage().equals("spanish", ignoreCase = true) || KsPreferenceKeys.getInstance().getAppLanguage().equals("हिंदी", ignoreCase = true)) {
                updateLanguage("es", context)
            } else if (KsPreferenceKeys.getInstance().getAppLanguage().equals("English", ignoreCase = true)) {
                updateLanguage("en", context)
            }
        }

        @JvmStatic
        fun updateLanguage(language: String?, context: Context) {
            Logger.w("selectedLang--in", language!!)
            val locale = Locale(language)
            Locale.setDefault(locale)
            val res = context.resources
            val config = Configuration(res.configuration)
            config.locale = locale
            res.updateConfiguration(config, res.displayMetrics)
        }

        fun getPushToken(activity: Activity) {
            mActivity = WeakReference<Activity>(activity)
            FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(OnCompleteListener<String?> { task: Task<String?> ->
                    if (!task.isSuccessful) {
                        //Could not get FirebaseMessagingToken
                        return@OnCompleteListener
                    }
                    if (null != task.result) {
                        //Got FirebaseMessagingToken
                        val firebaseMessagingToken = Objects.requireNonNull(task.result)
                        KsPreferenceKeys.getInstance().setAppPrefFcmToken(firebaseMessagingToken)
                        Logger.w("FCM_TOKEN", KsPreferenceKeys.getInstance().getAppPrefFcmToken())


                        // Log and toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        //Logger.d(TAG, msg);
                        //  Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                })
            /*  FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();
                        KsPreferenceKeys.getInstance().setAppPrefFcmToken(token);
                        Logger.w("FCM_TOKEN", KsPreferenceKeys.getInstance().getAppPrefFcmToken());


                    }
                });*/
        }

        fun showPopupMenu(context: Context?, view: View?, menuItems: Int, onMenuItemClickListener: PopupMenu.OnMenuItemClickListener) {
            val popup = PopupMenu(context!!, view!!)
            popup.setOnMenuItemClickListener { item: MenuItem? -> onMenuItemClickListener.onMenuItemClick(item) }
            popup.inflate(menuItems)
            popup.show()
        }

        @JvmStatic
        fun createManualHeroItem(enveuVideoItemBean: EnveuVideoItemBean, enveuVideoDetails: EnveuVideoDetails) {
            enveuVideoItemBean.brightcoveVideoId = enveuVideoDetails.brightcoveContentId
            enveuVideoItemBean.assetType = enveuVideoDetails.contentType
        }

        @JvmStatic
        fun createAssetHeroItem(enveuVideoItemBean: EnveuVideoItemBean, enveuVideoDetails: EnveuVideoDetails, screenWidget: BaseCategory) {
            enveuVideoItemBean.brightcoveVideoId = enveuVideoDetails.brightcoveContentId
            if (WidgetImageType.THUMBNAIL.toString().equals(screenWidget.widgetImageType, ignoreCase = true)) {
                Logger.d("Screen WidgetType " + screenWidget.widgetImageType)
                if (enveuVideoDetails.images != null && enveuVideoDetails.images.thumbnail != null && enveuVideoDetails.images.thumbnail.sources != null && !enveuVideoDetails.images.thumbnail.sources.isEmpty()) {
                    enveuVideoItemBean.posterURL = enveuVideoDetails.images.thumbnail.sources[0].src
                }
            } else {
                if (enveuVideoDetails.images != null && enveuVideoDetails.images.poster != null && enveuVideoDetails.images.poster.sources != null && !enveuVideoDetails.images.poster.sources.isEmpty()) {
                    enveuVideoItemBean.posterURL = enveuVideoDetails.images.poster.sources[0].src
                }
            }
            enveuVideoItemBean.assetType = enveuVideoDetails.contentType
        }

        @JvmStatic
        fun heroAssetRedirections(railCommonData: RailCommonData, activity: Context?, videoId: Long?, parseInt: Int, s: String?, b: Boolean) {
            try {
                var landingPageAssetId: String = railCommonData.getScreenWidget().landingPageAssetId.toString()
                if (ObjectHelper.isEmpty(landingPageAssetId)) {
                    landingPageAssetId = "0"
                }
                //            if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getEpisode())) {
//                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getEpisode(), Integer.parseInt(
//                        landingPageAssetId), "0", false);
//            } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getSeries())) {
//                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getSeries(), Integer.parseInt(
//                        landingPageAssetId), "0", false);
//            } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getMovie())) {
//                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getMovie(), Integer.parseInt(
//                        landingPageAssetId), "0", false);
//            } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getShow())) {
//                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getShow(), Integer.parseInt(
//                        landingPageAssetId), "0", false);
//            } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(MediaTypeConstants.getInstance().getLive())) {
//                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getLive(), Integer.parseInt(
//                        landingPageAssetId), "0", false);
//            } else if (railCommonData.getEnveuVideoItemBeans().get(0).getAssetType().equalsIgnoreCase(AppConstants.ContentType.ARTICLE.name())) {
//                AppCommonMethod.launchDetailScreen(activity, videoId, MediaTypeConstants.getInstance().getLive(), Integer.parseInt(
//                        landingPageAssetId), "0", false);
//            }
            } catch (ex: Exception) {
                Logger.w(ex)
            }
        }

        fun getAssetDetail(railCommonData: RailCommonData, response: Response<EnveuVideoDetailsBean>) {
            try {
                val enveuVideoDetailsBean = EnveuVideoDetailsBean()
                val enveuVideoDetails = response.body()!!.data
                enveuVideoDetailsBean.data = enveuVideoDetails
                val enveuVideoItemBean = EnveuVideoItemBean(enveuVideoDetailsBean)
                railCommonData.setEnveuVideoItemBeans(ArrayList<EnveuVideoItemBean>())
                railCommonData.getEnveuVideoItemBeans().add(enveuVideoItemBean)
            } catch (ex: Exception) {
                Logger.w(ex)
            }
        }

        @JvmStatic
        fun getEnvAssetDetail(railCommonData: RailCommonData, response: EnveuVideoDetailsBean) {
            try {
                val enveuVideoDetailsBean = EnveuVideoDetailsBean()
                val enveuVideoDetails = response.data
                enveuVideoDetailsBean.data = enveuVideoDetails
                val enveuVideoItemBean = EnveuVideoItemBean(enveuVideoDetailsBean)
                railCommonData.setEnveuVideoItemBeans(ArrayList<EnveuVideoItemBean>())
                railCommonData.getEnveuVideoItemBeans().add(enveuVideoItemBean)
            } catch (ex: Exception) {
                Logger.w(ex)
            }
        }

        fun callSocialAction(preference: KsPreferenceKeys, userInteractionFragment: UserInteractionFragment?) {
            try {
                if (preference.getAppPrefLoginStatus().equals(
                        AppConstants.UserStatus.Login.toString(), ignoreCase = true
                    ) && userInteractionFragment != null
                ) {
                    if (ActivityTrackers.LIKE.equals(ActivityTrackers.getInstance().action, ignoreCase = true)) {
                        userInteractionFragment.setToken(preference.getAppPrefAccessToken())
                        userInteractionFragment.setLikeForAsset(2)
                        ActivityTrackers.getInstance().setAction("")
                    } else if (ActivityTrackers.WATCHLIST.equals(
                            ActivityTrackers.getInstance().action, ignoreCase = true
                        )
                    ) {
                        userInteractionFragment.setToken(preference.getAppPrefAccessToken())
                        userInteractionFragment.setWatchListForAsset(2)
                        ActivityTrackers.getInstance().setAction("")
                    }
                }
            } catch (e: Exception) {
                Logger.w(e)
            }
        }

        var lang: String? = null

        @JvmStatic
        val currentTimeStamp: Long
            get() = System.currentTimeMillis() / 1000

        fun expiryDate(days: Int): String {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val currentDate = Date()
            Logger.d(dateFormat.format(currentDate))
            //Logger.d("DTGLogs", "expiryDays-->>" + dateFormat.format(currentDate));

            // convert date to calendar
            val c = Calendar.getInstance()
            c.time = currentDate

            // manipulate date
            c.add(Calendar.YEAR, 0)
            c.add(Calendar.MONTH, 0)
            c.add(Calendar.DATE, days) //same with c.add(Calendar.DAY_OF_MONTH, 1);
            c.add(Calendar.HOUR, 0)
            c.add(Calendar.MINUTE, 1)
            c.add(Calendar.SECOND, 1)

            // convert calendar to date
            val currentDatePlusOne = c.time
            Logger.d(dateFormat.format(currentDatePlusOne))

            //  Logger.d("DTGLogs", "expiryDays-->>" + dateFormat.format(currentDatePlusOne));
            return dateFormat.format(currentDatePlusOne)
        }

        fun getTodaysDifference(completionDate: String?): Int {
            var diff = -1
            try {
                val date = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).format(Date())
                val date1: Date
                val date2: Date
                val dates = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

                //Setting dates
                date1 = dates.parse(date)
                date2 = dates.parse(completionDate)
                diff = if (date1.before(date2)) {
                    //Logger.d("DTGLogs", "DBdayDifference-->>if" + date1 + "  " + date2);
                    -1
                } else {
                    // Logger.d("DTGLogs", "DBdayDifference-->>else" + date1 + "  " + date2);
                    1
                }
            } catch (e: Exception) {
                Logger.w(e)
            }
            return diff
        }

        @JvmStatic
        val configResponse: ConfigBean
            get() {
                val gson = Gson()
                val json: String = KsPreferenceKeys.getInstance().getString("DMS_Response", "")
                return gson.fromJson<ConfigBean>(json, ConfigBean::class.java)
            }

        fun setConfigConstant(configResponse: ConfigBean?, isTablet: Boolean) {
            //  Logger.w("configResponse", configResponse.getData().getAppConfig().getBaseUrl() + "  " + configResponse.getData().getAppConfig().getOvpBaseUrl());
            SDKConfig.getInstance().setConfigObject(configResponse, isTablet)
        }

        // home tab id
        //    public static String getHomeTabId(ConfigBean configBean, String name) {
        //        String screenId = "";
        //        if (configBean != null) {
        //            for (int i = 0; i < configBean.getData().getAppConfig().getNavScreens().size(); i++) {
        //                if (configBean.getData().getAppConfig().getNavScreens().get(i).getScreenName().equalsIgnoreCase(name)) {
        //                    screenId = String.valueOf(configBean.getData().getAppConfig().getNavScreens().get(i).getId());
        //                    break;
        //                }
        //            }
        //        }
        //        return screenId;
        //    }
        @JvmStatic
        fun getCheckBCID(brightcoveVideoId: String?): Boolean {
            return brightcoveVideoId != null && !"".equals(brightcoveVideoId, ignoreCase = true)
        }

        @JvmStatic
        fun handleTags(isVIPTag: String, isNewS: String, isVIP: FrameLayout, newSeries: FrameLayout, newEpisode: FrameLayout, newMovie: FrameLayout, assetType: String) {
            try {
                newEpisode.setVisibility(View.GONE)
                if (isVIPTag.equals("true", ignoreCase = true)) {
                    isVIP.setVisibility(View.VISIBLE)
                } else {
                    isVIP.setVisibility(View.GONE)
                }
                if (assetType.equals(MediaTypeConstants.getInstance().getSeries(), ignoreCase = true)) {
                    if (isNewS.equals("true", ignoreCase = true)) {
                        newSeries.setVisibility(View.VISIBLE)
                    } else {
                        newSeries.setVisibility(View.GONE)
                    }
                } else {
                    newSeries.setVisibility(View.GONE)
                }
                if (assetType.equals(MediaTypeConstants.getInstance().getMovie(), ignoreCase = true)) {
                    if (isNewS.equals("true", ignoreCase = true)) {
                        newMovie.setVisibility(View.VISIBLE)
                    } else {
                        newMovie.setVisibility(View.GONE)
                    }
                } else {
                    newMovie.setVisibility(View.GONE)
                }
                if (assetType.equals(MediaTypeConstants.getInstance().getEpisode(), ignoreCase = true)) {
                    if (isNewS.equals("true", ignoreCase = true)) {
                        newMovie.setVisibility(View.VISIBLE)
                    } else {
                        newMovie.setVisibility(View.GONE)
                    }
                } else {
                    newEpisode.setVisibility(View.GONE)
                }
            } catch (e: Exception) {
                Logger.w(e)
            }
        }

        fun createNotificationObject(notid: String?, assetType: String?): JSONObject {
            val jsonObject = JSONObject()
            try {
                jsonObject.put(CONTROL_PARAM_CONTENT_TYPE, assetType)
                jsonObject.put(CONTROL_PARAM_ID, notid)
            } catch (e: Exception) {
                Logger.w(e)
            }
            return jsonObject
        }

        fun openUrl(context: Context, url: String?) {
            val i = Intent(Intent.ACTION_VIEW)
            i.setData(Uri.parse(url))
            if (i.resolveActivity(context.packageManager) != null) {
                context.startActivity(i)
            }
        }

        @JvmStatic
        fun handleTitleDesc(titleLayout: RelativeLayout, tvTitle: TextView, tvDescription: TextView, baseCategory: BaseCategory?) {
            try {
                if (baseCategory != null) {
                    if (RailCardType.IMAGE_ONLY.name.equals(baseCategory.railCardType, ignoreCase = true)) {
                        titleLayout.setVisibility(View.GONE)
                    } else {
                        //titleLayout.setVisibility(View.VISIBLE);
                        if (RailCardType.IMAGE_TITLE.name.equals(baseCategory.railCardType, ignoreCase = true)) {
                            titleLayout.setVisibility(View.VISIBLE)
                            tvTitle.setVisibility(View.VISIBLE)
                        } else {
                            if (RailCardType.IMAGE_TITLE_DESC.name.equals(baseCategory.railCardType, ignoreCase = true)) {
                                titleLayout.setVisibility(View.VISIBLE)
                                tvTitle.setVisibility(View.VISIBLE)
                                tvDescription.setVisibility(View.VISIBLE)
                            } else {
                                titleLayout.setVisibility(View.GONE)
                                tvTitle.setVisibility(View.GONE)
                                tvDescription.setVisibility(View.GONE)
                            }
                        }
                    }
                }
            } catch (ignored: Exception) {
                titleLayout.visibility = View.GONE
                tvTitle.setVisibility(View.GONE)
                tvDescription.setVisibility(View.GONE)
            }
        }

        fun getProfileUserName(userProfileResponse: UserProfileResponse?): String {
            var name = ""
            if (userProfileResponse != null && userProfileResponse.data.name != null && !userProfileResponse.data.name.equals("", ignoreCase = true)) {
                name = userProfileResponse.data.name
            }
            return name
        }

        fun getProfileUserNumber(userProfileResponse: UserProfileResponse?): String? {
            var number = ""
            if (userProfileResponse != null && userProfileResponse.data.phoneNumber != null && !(userProfileResponse.data.phoneNumber as String).equals("", ignoreCase = true)) {
                number = userProfileResponse.data.phoneNumber as String
            }
            return number
        }

        fun getProfileUserGender(userProfileResponse: UserProfileResponse?): String? {
            var gender = ""
            if (userProfileResponse != null && userProfileResponse.data.gender != null && !(userProfileResponse.data.gender as String).equals("", ignoreCase = true)) {
                gender = userProfileResponse.data.gender as String
            }
            return gender
        }

        fun getProfileUserDOB(userProfileResponse: UserProfileResponse?): String {
            var dob = ""
            if (userProfileResponse != null && userProfileResponse.data.dateOfBirth != null) {
                val longVv = userProfileResponse.data.dateOfBirth as Double
                val df = DecimalFormat("#")
                df.maximumFractionDigits = 0
                val ll = df.format(longVv).toLong()
                dob = ll.toString()
            }
            return dob
        }

        fun getProfileUserAddress(userProfileResponse: UserProfileResponse?): String {
            var address = ""
            if (userProfileResponse != null && userProfileResponse.data.customData != null && userProfileResponse.data.customData.address != null && !userProfileResponse.data.customData.address.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                address = userProfileResponse.data.customData.address
            }
            return address
        }

        fun createPrefrenceList(newObject: UserProfileResponse): List<String?> {
            var strings: List<String?> = ArrayList()
            if (newObject.data.customData != null && newObject.data.customData.contentPreferences != null) {
                strings = Arrays.asList(*newObject.data.customData.contentPreferences.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            }
            return strings
        }

        fun check(identifier: String, saved: List<String>): Boolean {
            var contains = false
            for (i in saved.indices) {
                Logger.d("savedata6: " + saved[i] + "-" + identifier + "    " + i)
                if (saved[i].equals(identifier, ignoreCase = true)) {
                    contains = true
                    break
                } else {
                    contains = false
                }
            }
            return contains
        }

        @JvmStatic
        fun createFilterGenreList(selectedGenres: String?): List<String?> {
            var strings: List<String?> = ArrayList()
            if (selectedGenres != null && !selectedGenres.equals("", ignoreCase = true)) {
                strings = Arrays.asList(*selectedGenres.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            }
            return strings
        }

        fun createFilterSortList(selectedGenres: String?): List<String?> {
            var strings: List<String?> = ArrayList()
            if (selectedGenres != null && !selectedGenres.equals("", ignoreCase = true)) {
                strings = Arrays.asList(*selectedGenres.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            }
            return strings
        }

        @JvmStatic
        fun resetFilter(context: Context?) {
            KsPreferenceKeys.getInstance().saveDataGenre(null)
            KsPreferenceKeys.getInstance().saveDataGenreKeyValue(null)
            KsPreferenceKeys.getInstance().saveDataSort(null)
            KsPreferenceKeys.getInstance().saveDataSortKeyValue(null)
            KsPreferenceKeys.getInstance().saveDataFeature(null)
            KsPreferenceKeys.getInstance().saveDataFeatureKeyValue(null)
            KsPreferenceKeys.getInstance().setFilterApply("false")
            KsPreferenceKeys.getInstance().setFilterGenre(0)
        }

        @JvmStatic
        fun getMultilingualTitle(currentLang: String?, multilingualTitle: JsonObject, thaiCode: String?, englishCode: String?): String {
            var name = ""
            try {
                if (KsPreferenceKeys.getInstance().getAppLanguage().equals("Thai", ignoreCase = true) || KsPreferenceKeys.getInstance().getAppLanguage().equals("हिंदी", ignoreCase = true)) {
                    for ((key, value) in multilingualTitle.entrySet()) {
                        if (key.equals(thaiCode, ignoreCase = true)) {
                            name = value.getAsString()
                            break
                        }
                    }
                } else {
                    for ((key, value) in multilingualTitle.entrySet()) {
                        if (key.equals(englishCode, ignoreCase = true)) {
                            name = value.getAsString()
                            break
                        }
                    }
                }
            } catch (ignored: Exception) {
                name = ""
            }
            return name
        }

        //            if (configBean.getData().getAppConfig().getParentalControl().getRatings() != null) {
//                for (int i = 0; i < configBean.getData().getAppConfig().getParentalControl().getRatings().size(); i++) {
//                    if (currentLanguage.equalsIgnoreCase("Thai")) {
//                        if (configBean.getData().getAppConfig().getParentalControl().getRatings().get(i).getRatingValue() != null) {
//                            ratingValue = configBean.getData().getAppConfig().getParentalControl().getRatings().get(i).getRatingValue();
//                        }
//
//                    } else if (currentLanguage.equalsIgnoreCase("English")) {
//                        if (configBean.getData().getAppConfig().getParentalControl().getRatings().get(i).getRatingValue() != null) {
//
//                            ratingValue = configBean.getData().getAppConfig().getParentalControl().getRatings().get(i).getRatingValue();
//                        }
//
//                    }
//                }
//            }
        @JvmStatic
        val parentalRating: String
            get() {
                val ratingValue = ""
                val currentLanguage: String = KsPreferenceKeys.getInstance().getAppLanguage()
                val configBean = configResponse
                if (configBean != null) {
//            if (configBean.getData().getAppConfig().getParentalControl().getRatings() != null) {
//                for (int i = 0; i < configBean.getData().getAppConfig().getParentalControl().getRatings().size(); i++) {
//                    if (currentLanguage.equalsIgnoreCase("Thai")) {
//                        if (configBean.getData().getAppConfig().getParentalControl().getRatings().get(i).getRatingValue() != null) {
//                            ratingValue = configBean.getData().getAppConfig().getParentalControl().getRatings().get(i).getRatingValue();
//                        }
//
//                    } else if (currentLanguage.equalsIgnoreCase("English")) {
//                        if (configBean.getData().getAppConfig().getParentalControl().getRatings().get(i).getRatingValue() != null) {
//
//                            ratingValue = configBean.getData().getAppConfig().getParentalControl().getRatings().get(i).getRatingValue();
//                        }
//
//                    }
//                }
//            }
                }
                return ratingValue
            }

        fun getSpeciesList1(resSpeciesIds: String?): List<String>? {
            var items: List<String>? = null
            if (null != resSpeciesIds && resSpeciesIds != "") {
                items = Arrays.asList(*resSpeciesIds.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            }
            return items
        }

        fun getTypeList1(resTypesIds: String?): List<String>? {
            var items: List<String>? = null
            if (null != resTypesIds && resTypesIds != "") {
                items = Arrays.asList(*resTypesIds.split("\\s*,\\s*".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            }
            return items
        }

        fun getTabIdAsPerProfile(screenType: String): String {
            var tabId = "0"
            try {
                if (configResponse.data.appConfig.userProfile != null && !configResponse.data.appConfig.userProfile.isEmpty()) {
                    for (i in configResponse.data.appConfig.userProfile.indices) {
                        if (configResponse.data.appConfig.userProfile[i].id.equals(KsPreferenceKeys.getInstance().getPreferenceProfileId(), ignoreCase = true)) {
                            for (j in configResponse.data.appConfig.userProfile[i].navScreens.indices) {
                                var nav_Key = configResponse.data.appConfig.userProfile[i].navScreens[j].displayName.enUs
                                nav_Key = nav_Key.replace(" ", "")
                                val nav_Key1 = configResponse.data.appConfig.userProfile[i].navScreens[j].navScreenkey
                                if (nav_Key.equals(screenType, ignoreCase = true)) {
                                    for (k in configResponse.data.appConfig.navScreens.indices) {
                                        val screenName = configResponse.data.appConfig.navScreens[k].screenName
                                        if (screenName.equals(nav_Key1, ignoreCase = true)) {
                                            tabId = configResponse.data.appConfig.navScreens[k].id.toString()
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (screenType.equals(AppConstants.HUNTING_HOME, ignoreCase = true)) {
                        tabId = "0"
                    } else if (screenType.equals(AppConstants.FISHING_HOME, ignoreCase = true)) {
                        tabId = "1"
                    } else if (screenType.equals(AppConstants.BOTH_HOME, ignoreCase = true)) {
                        tabId = "2"
                    }
                }
                Log.d("tabId", "getTabIdAsPerProfile: $tabId")
            } catch (e: Exception) {
                if (screenType.equals(AppConstants.HUNTING_HOME, ignoreCase = true)) {
                    tabId = "0"
                } else if (screenType.equals(AppConstants.FISHING_HOME, ignoreCase = true)) {
                    tabId = "1"
                } else if (screenType.equals(AppConstants.BOTH_HOME, ignoreCase = true)) {
                    tabId = "2"
                }
            }
            return tabId
        }

        @JvmStatic
        fun redirectionLogic(context: Context, railCommonData: RailCommonData, position: Int) {
            // checkLoginStatus(context);
            var scrrenFrom = ""
            scrrenFrom = context.toString()
            val contentId: String = railCommonData.getEnveuVideoItemBeans().get(position).getId().toString()
            MoEngageContentSelectTrack(
                OttApplication.getContext(),
                scrrenFrom,
                "",
                railCommonData.getEnveuVideoItemBeans().get(position).getName(),
                contentId,
                railCommonData.getEnveuVideoItemBeans().get(position).getTitle(),
                railCommonData.getEnveuVideoItemBeans().get(position).getAssetType(),
                AppConstants.CONTENT_SELECT
            )
            if (railCommonData.getEnveuVideoItemBeans().get(position).getAssetType().equals(AppConstants.VIDEO, ignoreCase = true)) {
                redirectOnDetailPages(context, railCommonData, position, AppConstants.VIDEO)
            } else if (railCommonData.getEnveuVideoItemBeans().get(position).getAssetType().equals(AppConstants.CUSTOM, ignoreCase = true)) {
                redirectOnDetailPages(context, railCommonData, position, AppConstants.CUSTOM)
            } else if (railCommonData.getEnveuVideoItemBeans().get(position).getAssetType().equals(AppConstants.LIVE, ignoreCase = true)) {
                redirectOnDetailPages(context, railCommonData, position, AppConstants.LIVE)
            }
        }

        fun getDecodedJwt(jwt: String): String {
            val result = StringBuilder()
            val parts = jwt.split("[.]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            try {
                var index = 0
                for (part in parts) {
                    if (index >= 2) break
                    index++
                    val decodedBytes = Base64.decode(part.toByteArray(charset("UTF-8")), Base64.URL_SAFE)
                    if (index != 0) {
                        result.append(" " + String(decodedBytes, charset("UTF-8")))
                    }
                }
            } catch (e: Exception) {
                throw RuntimeException("Couldnt decode jwt", e)
            }
            return result.toString()
        }

        fun fetchRecSubscriptionModel(responseEntitlementModel: ResponseMembershipAndPlan, subSkuList: MutableList<String>, productSkuList: MutableList<String>): List<PurchaseModel> {
            val modelList: MutableList<PurchaseModel> = ArrayList<PurchaseModel>()
            try {
                if (responseEntitlementModel.getData() != null && !responseEntitlementModel.getData().isEmpty()) {
                    for (i in responseEntitlementModel.getData().indices) {
                        val model = PurchaseModel()
                        if (responseEntitlementModel.getData() != null && responseEntitlementModel.getData().get(i).getOfferType() != null && responseEntitlementModel.getData().get(i).getOfferType()
                                .contains(VodOfferType.RECURRING_SUBSCRIPTION.name)
                        ) {
                            model.setTitle(responseEntitlementModel.getData().get(i).getTitle())
                            val identifier: String = responseEntitlementModel.getData().get(i).getCustomData().getAndroidProductId()
                            model.setIdentifier(responseEntitlementModel.getData().get(i).getIdentifier())
                            model.setCustomIdentifier(identifier)
                            if (responseEntitlementModel.getData().get(i).getSubscriptionOrder() != null) {
                                model.setSubscriptionOrder(Math.toIntExact(responseEntitlementModel.getData().get(i).getSubscriptionOrder()))
                            }
                            model.setSubscriptionType(VodOfferType.RECURRING_SUBSCRIPTION.name)
                            subSkuList.add(identifier)
                            if (responseEntitlementModel.getData().get(i).getRecurringOffer() != null) {
                                if (responseEntitlementModel.getData().get(i).getRecurringOffer().getTrialPeriod() != null) {
                                    if (responseEntitlementModel.getData().get(i).getRecurringOffer().getTrialPeriod().getTrialType() != null && !responseEntitlementModel.getData().get(i)
                                            .getRecurringOffer().getTrialPeriod().getTrialType().equals("", ignoreCase = true)
                                    ) {
                                        model.setTrialType(responseEntitlementModel.getData().get(i).getRecurringOffer().getTrialPeriod().getTrialType())
                                    }
                                    if (responseEntitlementModel.getData().get(i).getRecurringOffer().getTrialPeriod().getTrialDuration() > 0) {
                                        model.setTrialDuration(responseEntitlementModel.getData().get(i).getRecurringOffer().getTrialPeriod().getTrialDuration())
                                    }
                                }
                                if (responseEntitlementModel.getData().get(i).getRecurringOffer().getOfferPeriod() != null) {
                                    if (responseEntitlementModel.getData().get(i).getRecurringOffer().getOfferPeriod().equals(VodOfferType.MONTHLY.name, ignoreCase = true)) {
                                        model.setOfferPeriod(VodOfferType.MONTHLY.name)
                                    } else if (responseEntitlementModel.getData().get(i).getRecurringOffer().getOfferPeriod().equals(VodOfferType.ANNUAL.name, ignoreCase = true)) {
                                        model.setOfferPeriod(VodOfferType.ANNUAL.name)
                                    }
                                }
                            }
                            model.setSubscriptionList(subSkuList)
                            if (responseEntitlementModel.getData().get(i).getExpiryDate() != null) {
                                model.setExpiryDate(responseEntitlementModel.getData().get(i).getExpiryDate())
                            }
                            model.setEntitlementState(responseEntitlementModel.getData().get(i).getEntitlementState() != null && responseEntitlementModel.getData().get(i).getEntitlementState())
                            if (responseEntitlementModel.getData().get(i).getCustomData() != null) {
                                model.setCustomData(responseEntitlementModel.getData().get(i).getCustomData())
                            }
                            if (responseEntitlementModel.getData().get(i).getCurrentExpiry() != null && responseEntitlementModel.getData().get(i).getCurrentExpiry() > 0) {
                                model.setCurrentExpiryDate(responseEntitlementModel.getData().get(i).getCurrentExpiry())
                            }
                            if (responseEntitlementModel.getData().get(i).getDescription() != null) {
                                model.setDescription(responseEntitlementModel.getData().get(i).getDescription())
                            }
                            if (responseEntitlementModel.getData().get(i).getNextChargeDate() != null && responseEntitlementModel.getData().get(i).getNextChargeDate() > 0) {
                                model.setNextChargeDate(responseEntitlementModel.getData().get(i).getNextChargeDate())
                            }
                            model.setOnTrial(responseEntitlementModel.getData().get(i).isOnTrial())
                            modelList.add(model)
                        } else {
                            val identifier: String = responseEntitlementModel.getData().get(i).getCustomData().getAndroidProductId()
                            model.setSubscriptionType("PRODUCT")
                            productSkuList.add(identifier)
                            if (responseEntitlementModel.getData().get(i).getRecurringOffer() != null) {
                                if (responseEntitlementModel.getData().get(i).getRecurringOffer().getTrialPeriod() != null) {
                                    if (responseEntitlementModel.getData().get(i).getRecurringOffer().getTrialPeriod().getTrialType() != null && !responseEntitlementModel.getData().get(i)
                                            .getRecurringOffer().getTrialPeriod().getTrialType().equals("", ignoreCase = true)
                                    ) {
                                        model.setTrialType(responseEntitlementModel.getData().get(i).getRecurringOffer().getTrialPeriod().getTrialType())
                                    }
                                    if (responseEntitlementModel.getData().get(i).getRecurringOffer().getTrialPeriod().getTrialDuration() > 0) {
                                        model.setTrialDuration(responseEntitlementModel.getData().get(i).getRecurringOffer().getTrialPeriod().getTrialDuration())
                                    }
                                }
                                if (responseEntitlementModel.getData().get(i).getRecurringOffer().getOfferPeriod() != null) {
                                    if (responseEntitlementModel.getData().get(i).getRecurringOffer().getOfferPeriod().equals(VodOfferType.MONTHLY.name, ignoreCase = true)) {
                                        model.setOfferPeriod(VodOfferType.MONTHLY.name)
                                    } else if (responseEntitlementModel.getData().get(i).getRecurringOffer().getOfferPeriod().equals(VodOfferType.ANNUAL.name, ignoreCase = true)) {
                                        model.setOfferPeriod(VodOfferType.ANNUAL.name)
                                    }
                                }
                            }
                            model.setEntitlementState(responseEntitlementModel.getData().get(i).getEntitlementState() != null && responseEntitlementModel.getData().get(i).getEntitlementState())
                            if (responseEntitlementModel.getData().get(i).getCustomData() != null) {
                                model.setCustomData(responseEntitlementModel.getData().get(i).getCustomData())
                            }
                            model.setSubscriptionList(productSkuList)
                            if (responseEntitlementModel.getData().get(i).getExpiryDate() != null) {
                                model.setExpiryDate(responseEntitlementModel.getData().get(i).getExpiryDate())
                            }
                            if (responseEntitlementModel.getData().get(i).getSubscriptionOrder() != null) {
                                model.setSubscriptionOrder(Math.toIntExact(responseEntitlementModel.getData().get(i).getSubscriptionOrder()))
                            }
                            if (responseEntitlementModel.getData().get(i).getDescription() != null) {
                                model.setDescription(responseEntitlementModel.getData().get(i).getDescription())
                            }
                            if (responseEntitlementModel.getData().get(i).getCurrentExpiry() != null && responseEntitlementModel.getData().get(i).getCurrentExpiry() > 0) {
                                model.setCurrentExpiryDate(responseEntitlementModel.getData().get(i).getCurrentExpiry())
                            }
                            if (responseEntitlementModel.getData().get(i).getNextChargeDate() != null && responseEntitlementModel.getData().get(i).getNextChargeDate() > 0) {
                                model.setNextChargeDate(responseEntitlementModel.getData().get(i).getNextChargeDate())
                            }
                            model.setOnTrial(responseEntitlementModel.getData().get(i).isOnTrial())
                            modelList.add(model)
                        }
                    }
                }
            } catch (e: Exception) {
                return modelList
            }
            return modelList
        }

        fun createPurchaseList(purchaseModelList: List<PurchaseModel>?, purchases: List<SkuDetails>, purchaseFinalList: ArrayList<PurchaseModel?>, bp: BillingProcessor): List<PurchaseModel?> {
            var skuDetails: SkuDetails
            for (i in purchases.indices) {
                val identifier: String = purchases[i].getSku()
                for (j in purchaseModelList!!.indices) {
                    if (identifier.equals(purchaseModelList[j].getCustomIdentifier(), ignoreCase = true)) {
                        val purchaseModel = PurchaseModel()
                        skuDetails = bp.getLocalSubscriptionSkuDetail(identifier)
                        purchaseModel.setPrice("" + skuDetails.getPrice())
                        purchaseModel.setTrialType("" + purchaseModelList[j].getTrialType())
                        purchaseModel.setTrialDuration(purchaseModelList[j].getTrialDuration())
                        purchaseModel.setSelected(false)
                        purchaseModel.setPurchaseOptions(VodOfferType.RECURRING_SUBSCRIPTION.name)
                        purchaseModel.setOfferPeriod(VodOfferType.WEEKLY.name)
                        purchaseModel.setTitle(purchaseModelList[j].getTitle())
                        purchaseModel.setIdentifier(purchaseModelList[j].getIdentifier())
                        purchaseModel.setCustomIdentifier(purchaseModelList[j].getCustomIdentifier())
                        purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode())
                        purchaseModel.setSubscriptionOrder(purchaseModelList[j].getSubscriptionOrder())
                        purchaseModel.setDescription(purchaseModelList[j].getDescription())
                        //purchaseModel.setDescription("" + skuDetails.getDescription());
                        purchaseFinalList.add(purchaseModel)
                    }
                }
            }
            return purchaseFinalList
        }

        fun activeBtn(binding: ActivityPurchaseBinding?, color: Int) {
            assert(binding != null)
            binding!!.btnBuy.setBackgroundResource(R.drawable.roundedcornerforbtn)
            binding.txtBtn.setTextColor(color)
        }

        fun activeBtn(binding: ActivitySelectSubscriptionPlanBinding?, color: Int) {
            assert(binding != null)
            binding!!.btnBuy.setBackgroundResource(R.drawable.roundedcornerforbtn)
            binding.txtBtn.setTextColor(color)
        }

        fun activeBtn(binding: ActivityPaymentDetailPagePlanBinding?, color: Int) {
            assert(binding != null)
            binding!!.btnBuy.setBackgroundResource(R.drawable.roundedcornerforbtn)
            binding.txtBtn.setTextColor(color)
        }

        @JvmStatic
        fun getEpisodeAssetDetail(railCommonData: RailCommonData, response: Response<EnvVideoDetailsBean>, isIntentFromExpedition: Boolean) {
            try {
                val enveuVideoDetailsBean = EnvVideoDetailsBean()
                val enveuVideoDetails: Data = response.body()!!.getData()
                enveuVideoDetailsBean.setData(enveuVideoDetails)
                val enveuVideoItemBean = EnveuVideoItemBean(enveuVideoDetailsBean.getData(), isIntentFromExpedition)
                railCommonData.setEnveuVideoItemBeans(ArrayList<EnveuVideoItemBean>())
                railCommonData.getEnveuVideoItemBeans().add(enveuVideoItemBean)
            } catch (ex: Exception) {
                Logger.w(ex)
            }
        }

        fun createShowDialog(title: String?, message: String?, actnBtn: String?, context: Context?) {
            val dialog = Dialog(context!!, R.style.FullScreenDialog)
            dialog.setContentView(R.layout.custom_popup)
            val layoutParams: WindowManager.LayoutParams = dialog.window!!.attributes
            dialog.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
            dialog.window!!.attributes = layoutParams
            val btu = dialog.findViewById<Button>(R.id.personalizeBtn)
            val titleText: TextView = dialog.findViewById<TextView>(R.id.popup_title)
            btu.text = actnBtn
            titleText.setText(title)
            val description: TextView = dialog.findViewById<TextView>(R.id.popup_discription)
            description.setText(message)
            dialog.show()
            btu.setOnClickListener {
                if (isUserNotVerify) {
                    dialog.dismiss()
                    ActivityLauncher.getInstance().goToEnterOTP(context as Activity?, EnterOTPActivity::class.java, "DetailPage")
                } else if (isUserNotEntitle) {
                    dialog.dismiss()
                    ActivityLauncher.getInstance().goToDetailPlanScreen(context as Activity?, PaymentDetailPage::class.java, false, resEntitle)
                    // ActivityLauncher.getInstance().goToPlanScreen((Activity)context, ActivitySelectSubscriptionPlan.class,"Navigation");
                } else {
                    dialog.dismiss()
                }
            }
        }

        @JvmStatic
        fun getDateFromTimeStamp(expiryDate: Double): String {
            val date: String
            val formatter = SimpleDateFormat("dd MMMM yyyy")
            date = formatter.format(Date(expiryDate.toLong()))
            Log.w("expiryDate", date)
            return date
        }

        fun createManagePurchaseList(purchaseModelList: List<PurchaseModel>?, purchases: List<SkuDetails>, purchaseFinalList: ArrayList<PurchaseModel?>, bp: BillingProcessor): List<PurchaseModel?> {
            var skuDetails: SkuDetails
            for (i in purchases.indices) {
                val identifier: String = purchases[i].getSku()
                if (purchaseModelList != null) {
                    for (j in purchaseModelList.indices) {
                        if (identifier.equals(purchaseModelList[j].getCustomIdentifier(), ignoreCase = true)) {
                            val purchaseModel = PurchaseModel()
                            skuDetails = bp.getLocalSubscriptionSkuDetail(identifier)
                            purchaseModel.setPrice("" + skuDetails.getPrice())
                            purchaseModel.setTrialType("" + purchaseModelList[j].getTrialType())
                            purchaseModel.setTrialDuration(purchaseModelList[j].getTrialDuration())
                            purchaseModel.setSelected(false)
                            purchaseModel.setPurchaseOptions(VodOfferType.RECURRING_SUBSCRIPTION.name)
                            purchaseModel.setOfferPeriod(VodOfferType.WEEKLY.name)
                            purchaseModel.setTitle(purchaseModelList[j].getTitle())
                            purchaseModel.setIdentifier(purchaseModelList[j].getIdentifier())
                            purchaseModel.setCustomIdentifier(purchaseModelList[j].getCustomIdentifier())
                            purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode())
                            purchaseModel.setCreatedDate(purchaseModelList[j].getCreatedDate())
                            purchaseModel.setDescription(purchaseModelList[j].getDescription())
                            purchaseModel.setSubscriptionOrder(purchaseModelList[j].getSubscriptionOrder())
                            if (purchaseModelList[j].getEntitlementState() != null && purchaseModelList[j].getEntitlementState() == true) {
                                purchaseModel.setEntitlementState(purchaseModelList[j].getEntitlementState())
                                purchaseFinalList.add(purchaseModel)
                            }
                        }
                    }
                }
            }
            return purchaseFinalList
        }

        fun fetchEntitleRecSubscriptionModel(responseEntitlementModel: ResponseEntitle, subSkuList: MutableList<String>, productSkuList: MutableList<String>): List<PurchaseModel> {
            val modelList: MutableList<PurchaseModel> = ArrayList<PurchaseModel>()
            if (responseEntitlementModel.getData() != null) {
                if (responseEntitlementModel.getData().getPurchaseAs() != null && !responseEntitlementModel.getData().getPurchaseAs().isEmpty()) {
                    for (i in responseEntitlementModel.getData().getPurchaseAs().indices) {
                        val model = PurchaseModel()
                        if (responseEntitlementModel.getData() != null && responseEntitlementModel.getData().getPurchaseAs().get(i).getOfferType() != null && responseEntitlementModel.getData()
                                .getPurchaseAs().get(i).getOfferType().contains(VodOfferType.RECURRING_SUBSCRIPTION.name)
                        ) {
                            model.setTitle(responseEntitlementModel.getData().getPurchaseAs().get(i).getTitle())
                            val identifier: String = responseEntitlementModel.getData().getPurchaseAs().get(i).getCustomData().getAndroidProductId()
                            model.setIdentifier(responseEntitlementModel.getData().getPurchaseAs().get(i).getIdentifier())
                            model.setCustomIdentifier(identifier)
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getSubscriptionOrder() != null) {
                                model.setSubscriptionOrder(Math.toIntExact(responseEntitlementModel.getData().getPurchaseAs().get(i).getSubscriptionOrder()))
                            }
                            model.setSubscriptionType(VodOfferType.RECURRING_SUBSCRIPTION.name)
                            subSkuList.add(identifier)
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer() != null) {
                                if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod() != null) {
                                    if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod().getTrialType() != null && !responseEntitlementModel.getData()
                                            .getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod().getTrialType().equals("", ignoreCase = true)
                                    ) {
                                        model.setTrialType(responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod().getTrialType())
                                    }
                                    if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod().getTrialDuration() > 0) {
                                        model.setTrialDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod().getTrialDuration())
                                    }
                                }
                                if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getOfferPeriod() != null) {
                                    if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getOfferPeriod().equals(VodOfferType.MONTHLY.name, ignoreCase = true)) {
                                        model.setOfferPeriod(VodOfferType.MONTHLY.name)
                                    } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getOfferPeriod().equals(VodOfferType.ANNUAL.name, ignoreCase = true)) {
                                        model.setOfferPeriod(VodOfferType.ANNUAL.name)
                                    }
                                }
                            }
                            model.setSubscriptionList(subSkuList)
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getExpiryDate() != null) {
                                model.setExpiryDate(responseEntitlementModel.getData().getPurchaseAs().get(i).getExpiryDate())
                            }
                            model.setEntitlementState(
                                responseEntitlementModel.getData().getPurchaseAs().get(i).getEntitlementState() != null && responseEntitlementModel.getData().getPurchaseAs().get(i)
                                    .getEntitlementState()
                            )
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getCustomData() != null) {
                                model.setCustomData(responseEntitlementModel.getData().getPurchaseAs().get(i).getCustomData())
                            }
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrentExpiry() != null && responseEntitlementModel.getData().getPurchaseAs().get(i)
                                    .getCurrentExpiry() > 0
                            ) {
                                model.setCurrentExpiryDate(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrentExpiry())
                            }
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getDescription() != null) {
                                model.setDescription(responseEntitlementModel.getData().getPurchaseAs().get(i).getDescription())
                            }
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getNextChargeDate() != null && responseEntitlementModel.getData().getPurchaseAs().get(i)
                                    .getNextChargeDate() > 0
                            ) {
                                model.setNextChargeDate(responseEntitlementModel.getData().getPurchaseAs().get(i).getNextChargeDate())
                            }
                            model.setOnTrial(responseEntitlementModel.getData().getPurchaseAs().get(i).isOnTrial())
                            modelList.add(model)
                        } else {
                            val identifier: String = responseEntitlementModel.getData().getPurchaseAs().get(i).getCustomData().getAndroidProductId()
                            model.setSubscriptionType("PRODUCT")
                            productSkuList.add(identifier)
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer() != null) {
                                if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod() != null) {
                                    if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod().getTrialType() != null && !responseEntitlementModel.getData()
                                            .getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod().getTrialType().equals("", ignoreCase = true)
                                    ) {
                                        model.setTrialType(responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod().getTrialType())
                                    }
                                    if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod().getTrialDuration() > 0) {
                                        model.setTrialDuration(responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getTrialPeriod().getTrialDuration())
                                    }
                                }
                                if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getOfferPeriod() != null) {
                                    if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getOfferPeriod().equals(VodOfferType.MONTHLY.name, ignoreCase = true)) {
                                        model.setOfferPeriod(VodOfferType.MONTHLY.name)
                                    } else if (responseEntitlementModel.getData().getPurchaseAs().get(i).getRecurringOffer().getOfferPeriod().equals(VodOfferType.ANNUAL.name, ignoreCase = true)) {
                                        model.setOfferPeriod(VodOfferType.ANNUAL.name)
                                    }
                                }
                            }
                            model.setEntitlementState(
                                responseEntitlementModel.getData().getPurchaseAs().get(i).getEntitlementState() != null && responseEntitlementModel.getData().getPurchaseAs().get(i)
                                    .getEntitlementState()
                            )
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getCustomData() != null) {
                                model.setCustomData(responseEntitlementModel.getData().getPurchaseAs().get(i).getCustomData())
                            }
                            model.setSubscriptionList(productSkuList)
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getExpiryDate() != null) {
                                model.setExpiryDate(responseEntitlementModel.getData().getPurchaseAs().get(i).getExpiryDate())
                            }
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getSubscriptionOrder() != null) {
                                model.setSubscriptionOrder(Math.toIntExact(responseEntitlementModel.getData().getPurchaseAs().get(i).getSubscriptionOrder()))
                            }
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getDescription() != null) {
                                model.setDescription(responseEntitlementModel.getData().getPurchaseAs().get(i).getDescription())
                            }
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrentExpiry() != null && responseEntitlementModel.getData().getPurchaseAs().get(i)
                                    .getCurrentExpiry() > 0
                            ) {
                                model.setCurrentExpiryDate(responseEntitlementModel.getData().getPurchaseAs().get(i).getCurrentExpiry())
                            }
                            if (responseEntitlementModel.getData().getPurchaseAs().get(i).getNextChargeDate() != null && responseEntitlementModel.getData().getPurchaseAs().get(i)
                                    .getNextChargeDate() > 0
                            ) {
                                model.setNextChargeDate(responseEntitlementModel.getData().getPurchaseAs().get(i).getNextChargeDate())
                            }
                            model.setOnTrial(responseEntitlementModel.getData().getPurchaseAs().get(i).isOnTrial())
                            modelList.add(model)
                        }
                    }
                }
            }
            return modelList
        }

        fun createManagePurchaseListNew(purchaseModelList: List<PurchaseModel>, plans: ResponseMembershipAndPlan?, purchaseFinalList: ArrayList<PurchaseModel?>): List<PurchaseModel?> {
            if (purchaseModelList != null) {
                for (j in purchaseModelList.indices) {
                    if (purchaseModelList[j].getEntitlementState() != null && purchaseModelList[j].getEntitlementState() == true) {
                        val purchaseModel = PurchaseModel()
                        purchaseModel.setPrice("" + plans?.getData()?.get(j)?.getPrices()?.get(0)?.getPrice())
                        purchaseModel.setCurrency("" + plans?.getData()?.get(j)?.getPrices()?.get(0)?.getCurrencyCode())
                        purchaseModel.setPaymentProvider("" + plans?.getData()?.get(j)?.getCustomData()?.getPaymentProvider())
                        purchaseModel.setTrialType("" + purchaseModelList[j].getTrialType())
                        purchaseModel.setTrialDuration(purchaseModelList[j].getTrialDuration())
                        purchaseModel.setSelected(false)
                        purchaseModel.setPurchaseOptions(VodOfferType.RECURRING_SUBSCRIPTION.name)
                        purchaseModel.setOfferPeriod(VodOfferType.WEEKLY.name)
                        purchaseModel.setTitle(purchaseModelList[j].getTitle())
                        purchaseModel.setIdentifier(purchaseModelList[j].getIdentifier())
                        purchaseModel.setCustomIdentifier(purchaseModelList[j].getCustomIdentifier())
                        // purchaseModel.setCurrency(purchaseModelList.get(j).getCurrency());
                        purchaseModel.setCreatedDate(purchaseModelList[j].getCreatedDate())
                        purchaseModel.setSubscriptionOrder(purchaseModelList[j].getSubscriptionOrder())
                        purchaseModel.setEntitlementState(purchaseModelList[j].getEntitlementState())
                        purchaseFinalList.add(purchaseModel)
                    }
                }
            }
            return purchaseFinalList
        }

        fun MoEngageEventTrack(context: Context?, screenName: String?, id: String?, tittle: String?, contentType: String?, eventType: String?, contentPlayed: String?, contentDuration: String?) {
            val properties = Properties()
            properties
                .addAttribute(AppConstants.SCREEN_NAME, screenName)
                .addAttribute(AppConstants.CONTENT_ID, id)
                .addAttribute(AppConstants.TITLE, tittle)
                .addAttribute(AppConstants.CONTENT_TYPE, contentType)
                .addAttribute(AppConstants.CONTENT_PLAYED, contentPlayed)
                .addAttribute(AppConstants.CONTENT_DURATION, contentDuration)
                .setNonInteractive()
            MoEHelper.getInstance(context!!).trackEvent(eventType.toString(), properties)
        }

        fun MoEngageShareEventTrack(context: Context?, screenName: String?, id: String?, tittle: String?, contentType: String?, eventType: String?) {
            val properties = Properties()
            properties
                .addAttribute(AppConstants.SCREEN_NAME, screenName)
                .addAttribute(AppConstants.CONTENT_ID, id)
                .addAttribute(AppConstants.CONTENT_TITTLE, tittle)
                .addAttribute(AppConstants.CONTENT_TYPE, contentType)
                .setNonInteractive()
            MoEHelper.getInstance(context!!).trackEvent(eventType.toString(), properties)
        }

        fun searchEventTrack(context: Context?, searchKeyword: String?) {
            val properties = Properties()
            properties
                .addAttribute(AppConstants.SCREEN_NAME, AppConstants.HOME)
                .addAttribute(AppConstants.SEARCH_TERMS, searchKeyword)
                .setNonInteractive()
            MoEHelper.getInstance(context!!).trackEvent(AppConstants.SEARCH, properties)
        }

        fun screenViewedTrack(context: Context?, eventType: String?, screenName: String?) {
            val properties = Properties()
            properties
                .addAttribute(AppConstants.SCREEN_NAME, screenName)
                .setNonInteractive()
            MoEHelper.getInstance(context!!).trackEvent(eventType.toString(), properties)
        }

        @JvmStatic
        fun MoEngageSeeAllEventTrack(context: Context?, screenName: String?, id: String?, railName: String?, eventType: String?) {
            val properties = Properties()
            properties
                .addAttribute(AppConstants.SCREEN_NAME, screenName)
                .addAttribute(AppConstants.RAIL_ID, id)
                .addAttribute(AppConstants.RAIL_NAME, railName)
                .setNonInteractive()
            MoEHelper.getInstance(context!!).trackEvent(eventType.toString(), properties)
        }

        fun MoEngageContentSelectTrack(
            context: Context?,
            screenName: String?,
            railId: String?,
            railName: String?,
            contentId: String?,
            contentTittle: String?,
            contentType: String?,
            eventType: String?
        ) {
            val properties = Properties()
            properties
                .addAttribute(AppConstants.SCREEN_NAME, screenName)
                .addAttribute(AppConstants.RAIL_ID, "")
                .addAttribute(AppConstants.RAIL_NAME, railName)
                .addAttribute(AppConstants.CONTENT_ID, contentId)
                .addAttribute(AppConstants.CONTENT_TITTLE, contentTittle)
                .addAttribute(AppConstants.CONTENT_TYPE, contentType)
                .setNonInteractive()
            MoEHelper.getInstance(context!!).trackEvent(eventType.toString(), properties)
        }

        fun hitUserProfileApi(context: Context?) {
            try {
                val preference: KsPreferenceKeys = KsPreferenceKeys.getInstance()
                val registrationLoginViewModel: RegistrationLoginViewModel
                registrationLoginViewModel = ViewModelProvider((context as BaseActivity?)!!).get<RegistrationLoginViewModel>(RegistrationLoginViewModel::class.java)
                registrationLoginViewModel.hitUserProfile(context as BaseActivity?, preference.getAppPrefAccessToken())
                    .observe(context as BaseActivity, Observer<UserProfileResponse?> { userProfileResponse ->
                        if (userProfileResponse != null) {
                            if (userProfileResponse.status) {
                            } else {
                                if (userProfileResponse.responseCode == 4302) {
                                    preference.setAppPrefRegisterStatus(AppConstants.UserStatus.Logout.toString())
                                }
                            }
                        }
                    })
            } catch (e: Exception) {
            }
        }

        fun checkLoginStatus(context: Context?) {
            val preference: KsPreferenceKeys
            preference = KsPreferenceKeys.getInstance()
            var isLoggedIn = false
            if (preference.getAppPrefLoginStatus().equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                isLoggedIn = true
            }
            if (isLoggedIn) {
                hitUserProfileApi(context)
            }
        }

        fun callMoEngageUserTypeSubscription(context: Context?, userType: String?) {
            val properties = Properties()
            properties.addAttribute(AppConstants.USERTYPE, userType)
            MoEHelper.getInstance(context!!).trackEvent(AppConstants.USERTYPE, properties)
        }

        @JvmStatic
        fun getHomeTabId(configBean: ConfigBean?, name: String?): String? {
            var screenId = ""
            if (configBean != null) {
                for (i in configBean.data.appConfig.navScreens.indices) {
                    if (configBean.data.appConfig.navScreens[i].screenName.equals(name, ignoreCase = true)) {
                        screenId = configBean.data.appConfig.navScreens[i].id.toString()
                        break
                    }
                }
            }
            return screenId
        }

    }

}