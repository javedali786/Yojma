package com.tv.uscreen.yojmatv.activities.detail.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.Typeface
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.enveu.client.bookmarking.bean.GetBookmarkResponse
import com.enveu.client.enums.Layouts
import com.example.jwplayer.PlayerActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.Gson
import com.tv.uscreen.yojmatv.Bookmarking.BookmarkingViewModel
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.SDKConfig
import com.tv.uscreen.yojmatv.activities.detail.viewModel.DetailViewModel
import com.tv.uscreen.yojmatv.activities.listing.listui.ListActivity
import com.tv.uscreen.yojmatv.activities.purchase.planslayer.GetPlansLayer
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.ActivityLogin
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.EnterOTPActivity
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.PaymentDetailPage
import com.tv.uscreen.yojmatv.activities.usermanagment.viewmodel.RegistrationLoginViewModel
import com.tv.uscreen.yojmatv.adapters.player.EpisodeTabAdapter
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.entitle.ResponseEntitle
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModel.responseModels.detailPlayer.Data
import com.tv.uscreen.yojmatv.beanModel.selectedSeason.SelectedSeasonModel
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.CommonRailtItemClickListner
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.MoreClickListner
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.NetworkChangeReceiver
import com.tv.uscreen.yojmatv.databinding.EpisodeScreenBinding
import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment.Companion.newInstance
import com.tv.uscreen.yojmatv.fragments.player.ui.RelatedContentFragment
import com.tv.uscreen.yojmatv.fragments.player.ui.SeasonTabFragment
import com.tv.uscreen.yojmatv.fragments.player.ui.UserInteractionFragment
import com.tv.uscreen.yojmatv.networking.apistatus.APIStatus
import com.tv.uscreen.yojmatv.networking.responsehandler.ResponseModel
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.MediaTypeConstants
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.AppColors
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.config.LanguageLayer
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.cropImage.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.CheckInternetConnection
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils
import com.tv.uscreen.yojmatv.utils.helpers.downloads.DownloadHelper
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.htmlParseToString
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class EpisodeActivity : BaseBindingActivity<EpisodeScreenBinding?>(),
    AlertDialogFragment.AlertDialogListener, NetworkChangeReceiver.ConnectivityReceiverListener,
    OnAudioFocusChangeListener,
    CommonRailtItemClickListner, MoreClickListner, CommonDialogFragment.EditDialogListener {
    private var viewModel: DetailViewModel? = null
    private var preference: KsPreferenceKeys? = null
    private var assetId = 0
    private var seriesId = 0
    private var token: String? = null
    private var isLogin: String? = null
    private var isLoggedOut = false
    private var isHitPlayerApi = false
    private val tabId: String? = null
    private var brightCoveVideoId: Long? = null
    private var signLangParentRefId = ""
    private var isGeoBlocking = false
    private var signLangRefId = ""
    private var isPodcast = ""
    private var railInjectionHelper: RailInjectionHelper? = null
    private var audioManager: AudioManager? = null
    private var focusRequest: AudioFocusRequest? = null
    private var seasonTabFragment: SeasonTabFragment? = null
    private var seriesDetailBean: EnveuVideoItemBean? = null
    private var keyword: String? = ""
    private var alertDialog: AlertDialog? = null
    private var episodeTabAdapter: EpisodeTabAdapter? = null

    @JvmField
    var isSeasonData = false

    @JvmField
    var isRailData = true
    private val errorDialogShown = false
    private var videoDetails: EnveuVideoItemBean? = null
    private val downloadHelper: DownloadHelper? = null
    private var userInteractionFragment: UserInteractionFragment? = null
    private val isOfflineAvailable = false
    private var isLoggedIn = false
    private var id = 0
    private var isUserVerified: String? = null
    private var isUserNotVerify = false
    private var isUserNotEntitle = false
    private var playbackUrl: String? = null
    private var trailerUrl: String? = null
    private var trailerExternalRefId: String? = null
    private var currentEpisodeId = 0
    private var relatedContentFragment: RelatedContentFragment? = null
    private val preferenceData = ""
    private var registrationLoginViewModel: RegistrationLoginViewModel? = null
    private val colorsHelper by lazy { ColorsHelper }
    private val stringsHelper by lazy { StringsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): EpisodeScreenBinding {
        return EpisodeScreenBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ThemeHandler.getInstance().applyepisodedetailPage(getApplicationContext(),getBinding());
        parseColor()
        window.setBackgroundDrawableResource(R.color.app_bg_color)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        val kidsMode = KsPreferenceKeys.getInstance().kidsMode
        var parentalRating: String? = ""
        parentalRating = if (kidsMode) {
            AppCommonMethod.parentalRating
        } else {
            "Other"
        }
        preference = KsPreferenceKeys.getInstance()
        if (preference?.appPrefLoginStatus.equals(
                AppConstants.UserStatus.Login.toString(),
                ignoreCase = true
            )
        ) {
            isLoggedIn = true
        }
        isUserVerified = preference?.isVerified
        viewModel = ViewModelProvider(this@EpisodeActivity)[DetailViewModel::class.java]
        setupUI(binding!!.llParent)
        isHitPlayerApi = false
        AppCommonMethod.isPurchase = false
        if (intent.hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            var extras = intent.extras
            if (extras != null) {
                extras = extras.getBundle(AppConstants.BUNDLE_ASSET_BUNDLE)
                assetId = extras?.getInt(AppConstants.BUNDLE_ASSET_ID)!!
                brightCoveVideoId = extras.getLong(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE)
            }
        }
        setClicks()
        checkGeoBlocking()
        if (isLoggedIn) {
          //  hitUserProfileApi()
        }
        callBinding()
    }

    private fun parseColor() {
        binding!!.stringData = stringsHelper
        binding!!.colorsData = colorsHelper
        binding!!.metaDetails.colorsData = colorsHelper
        binding!!.metaDetails.stringData = stringsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
    }

    private fun callBinding() {
        modelCall()
    }


    private fun checkGeoBlocking() {
        viewModel!!.getGeoBlocking(assetId.toString())
            .observe(this@EpisodeActivity) { response ->
                if (response != null && response.data != null) {
                    if(response.data.isIsBlocked) {
                        isGeoBlocking = true
                    }
                } else {
                    if (response!!.responseCode != null && response.responseCode == 4302) {

                    } else {
                        commonDialog(stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong)),
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)
                            )
                        )
                    }
                }
            }
    }


    private fun modelCall() {
        binding!!.connection.retryTxt.setOnClickListener {
            binding!!.llParent.visibility = View.VISIBLE
            binding!!.noConnectionLayout.visibility = View.GONE
            connectionObserver()
        }
        binding!!.backButton.setOnClickListener { onBackPressed() }
        connectionObserver()
    }

    private var resEntitle: ResponseEntitle? = null
    private fun setClicks() {
        binding!!.metaDetails.watchTrailer.setOnClickListener {
            startPlayer(
                trailerUrl,
                bingeWatchEnable = false,
                isTrailer = true,
                trailerExternalRefId ?: ""
            )
           /* if (isLoggedIn) {
                if (isUserVerified.equals("true", ignoreCase = true)) {

                } else {
                    isUserNotVerify = true
                    commonDialog(
                        "",
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_user_not_verify.toString(),
                            getString(R.string.popup_user_not_verify)
                        ),
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_verify.toString(),
                            getString(R.string.popup_verify)
                        )
                    )
                }
            } else {
                ActivityLauncher.getInstance()
                    .loginActivity(this@EpisodeActivity, ActivityLogin::class.java)
            }*/
        }
        binding!!.metaDetails.playButton.setOnClickListener {
            currentEpisodeId = videoDetails!!.id
            if (isLoggedIn) {
                if (isGeoBlocking) {
                    commonDialog(
                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.geo_blocking_title.toString(),
                            getString(R.string.geo_blocking_title)),
                        "",
                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(),getString(R.string.ok)
                        )
                    )
                } else {
                    if (!videoDetails!!.isPremium) {
                        if (isUserVerified.equals("true", ignoreCase = true)) {
                            if (null != videoDetails!!.externalRefId && !videoDetails!!.externalRefId.equals("", ignoreCase = true)) {
                                playbackUrl = SDKConfig.getInstance().playbacK_URL + videoDetails!!.externalRefId + ".m3u8"
                                startPlayer(playbackUrl, KsPreferenceKeys.getInstance().bingeWatchEnable, false,videoDetails!!.externalRefId)
                            }
                        } else {
                            isUserNotVerify = true
                            commonDialog(
                                "",
                                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_user_not_verify.toString(), getString(R.string.popup_user_not_verify)),
                                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_verify.toString(), getString(R.string.popup_verify))
                            )
                        }
                    } else {
                        binding!!.progressBar.visibility = View.VISIBLE
                        viewModel!!.hitApiEntitlement(token, videoDetails!!.sku).observe(this@EpisodeActivity) { responseEntitle ->
                            binding!!.progressBar.visibility = View.GONE
                            if (null != responseEntitle && null != responseEntitle.data) {
                                resEntitle = responseEntitle
                                if (responseEntitle.data.entitled) {
                                    if (isUserVerified.equals("true", ignoreCase = true)) {
                                        if (null != responseEntitle.data.externalRefId && !responseEntitle.data.externalRefId.equals("", ignoreCase = true)) {
                                            playbackUrl = SDKConfig.getInstance().playbacK_URL + responseEntitle.data.externalRefId + ".m3u8"
                                            startPlayer(playbackUrl, KsPreferenceKeys.getInstance().bingeWatchEnable, false,responseEntitle.data.externalRefId)
                                        }
                                    } else {
                                        isUserNotVerify = true
                                        //TODO show dialog when user is not verified for now showing entitlement dialog
                                        commonDialog(
                                            "",
                                            stringsHelper.stringParse(
                                                stringsHelper.instance()?.data?.config?.popup_user_not_verify.toString(),
                                                getString(R.string.popup_user_not_verify)
                                            ),
                                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_verify.toString(), getString(R.string.popup_verify))
                                        )
                                    }
                                } else {
                                    isUserNotEntitle = true
                                    commonDialog(
                                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_notEntitled.toString(), getString(R.string.popup_notEntitled)),
                                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_select_plan.toString(), getString(R.string.popup_select_plan)),
                                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_purchase.toString(), getString(R.string.popup_purchase))
                                    )
                                }
                            } else {
                                if (responseEntitle!!.responseCode != null && responseEntitle.responseCode == 4302) {
                                    clearCredientials(preference)
                                    ActivityLauncher.getInstance().loginActivity(this@EpisodeActivity, ActivityLogin::class.java)
                                } else {
                                    commonDialog(
                                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                                        stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong)
                                        ),
                                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue))
                                    )
                                }
                            }
                        }
                    }
                }

            } else {
                ActivityLauncher.getInstance()
                    .loginActivity(this@EpisodeActivity, ActivityLogin::class.java)
            }
        }
    }

    private fun startPlayer(
        playback_url: String?,
        bingeWatchEnable: Boolean,
        isTrailer: Boolean,
        externalRefId: String
    ) {
        ActivityLauncher.getInstance().launchPlayerActitivity(
            this@EpisodeActivity,
            PlayerActivity::class.java,
            playback_url,
            bingeWatchEnable,
            seasonEpisodesList,
            currentEpisodeId,
            videoDetails!!.title,
            videoDetails!!.assetType,
            isTrailer,
            false,
            videoDetails!!.posterURL,
            AppConstants.EPISODEACTIVITY,
            externalRefId,
            videoDetails?.skipintro_startTime ?: "",
            videoDetails?.skipintro_endTime ?: ""
        )
    }

    private fun connectionObserver() {
        connectionValidation(NetworkConnectivity.isOnline(this))
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            uiInitialisation()
        } else {
            noConnectionLayout()
        }
    }

    private fun setTabs(seasonNumber: String, noEpisode: Boolean) {
        binding?.tabLayout?.setTabTextColors(
            AppColors.detailPageTabItemUnselectedTxtColor(),
            AppColors.detailPageTabItemSelectedTxtColor(),
        )
        Logger.d("season: $seasonNumber | episode: $noEpisode")
        binding!!.tabLayout.setSelectedTabIndicatorGravity(TabLayout.INDICATOR_GRAVITY_TOP)
        if (episodeTabAdapter == null) {
            episodeTabAdapter = EpisodeTabAdapter(supportFragmentManager)
            val args = Bundle()
            args.putString(AppConstants.BUNDLE_TAB_ID, tabId)
            seasonTabFragment = SeasonTabFragment()
            relatedContentFragment = RelatedContentFragment()
            val forYouArgs = Bundle()
            forYouArgs.putString("videoType", AppConstants.Episode)
            forYouArgs.putString("contentType", AppConstants.VIDEO)
            forYouArgs.putInt(AppConstants.ID, id)
            relatedContentFragment!!.arguments = forYouArgs
            val bundleSeason = Bundle()
            if (noEpisode) {
                bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, seriesId)
                bundleSeason.putInt(AppConstants.BUNDLE_CURRENT_ASSET_ID, assetId)
                bundleSeason.putParcelableArrayList(
                    AppConstants.BUNDLE_SEASON_ARRAY,
                    seriesDetailBean!!.seasons as java.util.ArrayList<out Parcelable>?
                )
                bundleSeason.putString(
                    AppConstants.BUNDLE_SEASON_NAME,
                    seriesDetailBean!!.seasonName
                )
                bundleSeason.putInt(
                    AppConstants.BUNDLE_SEASON_COUNT,
                    seriesDetailBean!!.seasonCount
                )
                if (seriesDetailBean!!.seasonCount > 0) bundleSeason.putInt(
                    AppConstants.BUNDLE_SELECTED_SEASON,
                    seasonNumber.toInt()
                )
            } else {
                bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, -1)
            }
            seasonTabFragment!!.arguments = bundleSeason
            episodeTabAdapter!!.addFragment(
                seasonTabFragment,
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.detail_page_episodes.toString(),
                    getString(R.string.detail_page_episodes)
                )
            )
            episodeTabAdapter!!.addFragment(
                relatedContentFragment,
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.detail_page_related_videos.toString(),
                    getString(R.string.detail_page_related_videos)
                )
            )
            binding!!.viewPager.adapter = episodeTabAdapter
            binding!!.viewPager.offscreenPageLimit = 2
            binding!!.tabLayout.setupWithViewPager(binding!!.viewPager)

            binding?.metaDetails?.watchTrailer?.background =
                ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
            binding?.tabLayout?.getTabAt(0)?.view?.background = ColorsHelper.strokeBgDrawable(
                AppColors.detailPageTabUnselectedBorderColor(),
                AppColors.detailPageTabUnselectedBorderColor(),
                0f
            )
            if (episodeTabAdapter!!.count > 1) {
                binding?.tabLayout?.getTabAt(1)?.view?.background = ColorsHelper.strokeBgDrawable(
                    AppColors.detailPageTabSelectedBorderColor(),
                    AppColors.detailPageTabUnselectedBorderColor(),
                    0f
                )
            }
            binding!!.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    showLoading(binding!!.progressBar, true)
                    tab.view.background = ColorsHelper.strokeBgDrawable(
                        AppColors.detailPageTabUnselectedBorderColor(),
                        AppColors.detailPageTabUnselectedBorderColor(),
                        0f
                    )
                    Handler().postDelayed({ dismissLoading(binding!!.progressBar) }, 1500)
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {
                    tab.view.background = ColorsHelper.strokeBgDrawable(
                        AppColors.detailPageTabSelectedBorderColor(),
                        AppColors.detailPageTabUnselectedBorderColor(),
                        0f
                    )
                }

                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
            binding!!.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    binding!!.viewPager.measure(
                        binding!!.viewPager.measuredWidth,
                        binding!!.viewPager.measuredHeight
                    )
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
            changeTabsFont()
        } else {
            val bundleSeason = Bundle()
            if (noEpisode) {
                bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, seriesId)
                bundleSeason.putInt(AppConstants.BUNDLE_CURRENT_ASSET_ID, assetId)
                bundleSeason.putParcelableArrayList(
                    AppConstants.BUNDLE_SEASON_ARRAY,
                    seriesDetailBean!!.seasons as java.util.ArrayList<out Parcelable>?
                )
                bundleSeason.putString(
                    AppConstants.BUNDLE_SEASON_NAME,
                    seriesDetailBean!!.seasonName
                )
                bundleSeason.putInt(
                    AppConstants.BUNDLE_SEASON_COUNT,
                    seriesDetailBean!!.seasonCount
                )
                if (seriesDetailBean!!.seasonCount > 0) bundleSeason.putInt(
                    AppConstants.BUNDLE_SELECTED_SEASON,
                    seasonNumber.toInt()
                )
            } else {
                bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, -1)
            }
            val args = Bundle()
            args.putString(AppConstants.BUNDLE_TAB_ID, tabId)
            seasonTabFragment!!.getVideoRails(bundleSeason)
        }
    }

    private fun changeTabsFont() {
        try {
            val vg = binding!!.tabLayout.getChildAt(0) as ViewGroup
            val tabsCount = vg.childCount
            for (j in 0 until tabsCount) {
                val vgTab = vg.getChildAt(j) as ViewGroup
                val tabChildsCount = vgTab.childCount
                for (i in 0 until tabChildsCount) {
                    val tabViewChild = vgTab.getChildAt(i)
                    if (tabViewChild is TextView) {
                        tabViewChild.setSingleLine()
                    }
                }
            }
        } catch (ignored: Exception) {
        }
    }

    fun stopShimmercheck() {
        if (isSeasonData) {
            isSeasonData = false
            isRailData = true
            Handler().postDelayed({ stopShimmer() }, 1000)
        }
    }

    fun updateSeasonEpisodeNumber(seasonNumber: Int, episodeNumber: Int) {}
    fun removeTab(position: Int) {
        if (binding!!.tabLayout.tabCount >= 1 && position <= binding!!.tabLayout.tabCount) {
            episodeTabAdapter!!.removeTabPage(position)
            val params = binding!!.tabLayout.layoutParams
            params.width = resources.getDimension(WindowManager.LayoutParams.MATCH_PARENT).toInt()
            binding!!.tabLayout.layoutParams = params
        }
    }

    private fun callShimmer() {
        binding!!.seriesShimmer.visibility = View.VISIBLE
        binding!!.mShimmer.colorsData = ColorsHelper
        binding!!.mShimmer.seriesShimmerScroll1.isEnabled = false
        binding!!.mShimmer.seriesShimmerScroll2.isEnabled = false
        binding!!.llParent.visibility = View.GONE
        binding!!.noConnectionLayout.visibility = View.GONE
        binding!!.mShimmer.sfShimmer1.startShimmer()
        binding!!.mShimmer.sfShimmer2.startShimmer()
        binding!!.mShimmer.sfShimmer3.startShimmer()
        binding!!.mShimmer.flBackIconImage.bringToFront()
        binding!!.mShimmer.flBackIconImage.setOnClickListener { onBackPressed() }
    }

    private fun stopShimmer() {
        binding!!.seriesShimmer.visibility = View.GONE
        binding!!.llParent.visibility = View.VISIBLE
        binding!!.noConnectionLayout.visibility = View.GONE
        binding!!.mShimmer.sfShimmer1.startShimmer()
        binding!!.mShimmer.sfShimmer2.startShimmer()
        binding!!.mShimmer.sfShimmer3.startShimmer()
    }

    private fun playPlayerWhenShimmer() {
        if (videoDetails != null) {
            val id = videoDetails!!.id
            binding!!.pBar.visibility = View.GONE
            viewModel!!.getBookMarkByVideoId(token, id).observe(
                this
            ) { getBookmarkResponse: GetBookmarkResponse? ->
                binding!!.backButton.visibility = View.VISIBLE
                var bookmarkPosition = 0L
                if (getBookmarkResponse != null && getBookmarkResponse.bookmarks != null) {
                    bookmarkPosition = getBookmarkResponse.bookmarks[0].position
                }
                Logger.e(isOfflineAvailable.toString())
                if (isOfflineAvailable) {
                    val bookmarkPosition2 = bookmarkPosition
                }
            }
        }
    }

    private fun setArgsForEvent(args: Bundle) {
        try {
            if (videoDetails != null) {
                if (videoDetails!!.name != null) {
                    args.putString(AppConstants.PLAYER_ASSET_TITLE, videoDetails!!.name)
                }
                if (videoDetails!!.assetType != null) {
                    args.putString(AppConstants.PLAYER_ASSET_MEDIATYPE, videoDetails!!.assetType)
                }
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    override fun onResume() {
        super.onResume()
        Logger.e("EpisodeActivity", "onResume")
        requestAudioFocus()
        isLoggedOut = false
        dismissLoading(binding!!.progressBar)
        if (AppCommonMethod.isPurchase) {
            AppCommonMethod.isPurchase = false
            seriesId = AppCommonMethod.seriesId
            isHitPlayerApi = false
            refreshDetailPage(assetId)
        }
        if (!isLoggedIn && preference!!.appPrefLoginStatus.equals(
                AppConstants.UserStatus.Login.toString(), ignoreCase = true
            )
        ) {
            isLoggedIn = true
            AppCommonMethod.isPurchase = false
            seriesId = AppCommonMethod.seriesId
            isHitPlayerApi = false
            fromBingWatch = false
            seasonTabFragment!!.seasonAdapter = null
            refreshDetailPage(assetId)
        }
        setBroadcast()
        if (preference != null && userInteractionFragment != null) {
            AppCommonMethod.callSocialAction(preference!!, userInteractionFragment)
        }
    }

    private fun requestAudioFocus() {
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playbackAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            focusRequest = AudioFocusRequest.Builder(AudioManager.STREAM_MUSIC)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener { focusChange: Int -> }
                .build()
            audioManager!!.requestAudioFocus(
                this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        } else {
            audioManager!!.requestAudioFocus(
                null, AudioManager.STREAM_VOICE_CALL,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }
    }

    private fun releaseAudioFocusForMyApp(context: Context?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager!!.abandonAudioFocusRequest(focusRequest!!)
        }
    }

    private fun setBroadcast() {
        receiver = NetworkChangeReceiver()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED")
        filter.addAction("android.net.wifi.STATE_CHANGE")
        this@EpisodeActivity.registerReceiver(receiver, filter)
        setConnectivityListener(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            isHitPlayerApi = false
            val extras = intent.extras
            if (extras != null) {
                assetId = intent.extras!!.getBundle(AppConstants.BUNDLE_ASSET_BUNDLE)
                    ?.getInt(AppConstants.BUNDLE_ASSET_ID)!!
                AppCommonMethod.seasonId = -1
                refreshDetailPage(assetId)
            }
        } else {
            throw IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All")
        }
    }

    private fun refreshDetailPage(assestId: Int) {
        assetId = assestId
        if (preference!!.appPrefHasSelectedId) {
            preference!!.appPrefHasSelectedId = false
            val tempId = preference!!.appPrefSelectodSeasonId
            if (tempId != -1) {
                preference!!.appPrefSelectodSeasonId = -1
            }
        }

//        if (getBinding().metaDetails.expandableLayout.isExpanded())
        //    clickExpandable(getBinding().metaDetails.lessButton);

//        final Bundle bundle = getIntent().getExtras().getBundle(AppConstants.BUNDLE_ASSET_BUNDLE);
//        bundle.remove(AppConstants.EXTRA_SHOW_PRE_ROLL_VIDEO);
        callBinding()
    }

    fun openLoginPage(message: String?) {
        preference!!.appPrefJumpTo = MediaTypeConstants.getInstance().episode
        preference!!.appPrefJumpBack = true
        preference!!.appPrefIsEpisode = true
        preference!!.appPrefJumpBackId = assetId
        preference!!.appPrefHasSelectedId = true
        ActivityLauncher.getInstance()
            .loginActivity(this@EpisodeActivity, ActivityLogin::class.java)
    }

    fun uiInitialisation() {
        if (!fromBingWatch) {
            callShimmer()
        }
        railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
        binding!!.casttext = ""
        binding!!.crewtext = ""
        val player = EnveuVideoItemBean()
        val data = Data()
        data.contentTitle = ""
        binding!!.responseApi = player
        setupUI(binding!!.llParent)
        preference!!.appPrefAssetId = assetId
        isLogin = preference!!.appPrefLoginStatus
        token = preference!!.appPrefAccessToken
        Logger.e("APP_PREF_ACCESS_TOKEN$token")
        showLoading(binding!!.progressBar, false)
        binding!!.noConnectionLayout.visibility = View.GONE
        if (!isHitPlayerApi) {
            episodeDetails
        }
        binding?.metaDetails?.durationLl?.background = ColorsHelper.strokeBgDrawable(
            AppColors.detailPageHDBgColor(),
            AppColors.detailPageHDBrColor(),
            10f
        )
    }

    private var isAdShowingToUser = true
    private val episodeDetails: Unit
        private get() {
            isHitPlayerApi = true
            if (fromBingWatch) {
                parseVideoDetails(nextEpisode)
            } else {
                railInjectionHelper!!.getSeriesDetailsV2(assetId.toString(), false).observe(
                    this@EpisodeActivity
                ) { response: ResponseModel<*>? ->
                    if (response != null) {
                        if (response.status.equals(APIStatus.START.name, ignoreCase = true)) {
                        } else if (response.status.equals(
                                APIStatus.SUCCESS.name,
                                ignoreCase = true
                            )
                        ) {
                            if (response.baseCategory != null) {
                                val enveuCommonResponse = response.baseCategory as RailCommonData
                                if (enveuCommonResponse.enveuVideoItemBeans.size > 0 && enveuCommonResponse.enveuVideoItemBeans[0] != null) {
                                    keyword =
                                        enveuCommonResponse.enveuVideoItemBeans[0].display_tags
                                    videoDetails = enveuCommonResponse.enveuVideoItemBeans[0]
                                    id = enveuCommonResponse.enveuVideoItemBeans[0].id
                                    if (videoDetails?.trailerReferenceId != null) {
                                        getTrailer(videoDetails?.trailerReferenceId!!)
                                    }
                                    Log.d("javed", "getEpisodeDetails: $videoDetails")
                                    parseVideoDetails(videoDetails)
                                }
                            }
                        } else if (response.status.equals(
                                APIStatus.ERROR.name,
                                ignoreCase = true
                            )
                        ) {
                            if (response.errorModel.errorCode != 0) {
                                stopShimmer()
                            }
                        } else if (response.status.equals(
                                APIStatus.FAILURE.name,
                                ignoreCase = true
                            )
                        ) {
                            stopShimmer()
                        }
                    }
                }
            }
        }

    private fun getTrailer(trailerReferenceId: String) {
        railInjectionHelper!!.getAssetDetailsV2(trailerReferenceId, this@EpisodeActivity)
            .observe(this@EpisodeActivity) { assetResponse: ResponseModel<*>? ->
                if (assetResponse != null) {
                    val gson = Gson()
                    val json = gson.toJson(assetResponse.baseCategory)
                    if (assetResponse.status.equals(APIStatus.START.name, ignoreCase = true)) {
                    } else if (assetResponse.status.equals(
                            APIStatus.SUCCESS.name,
                            ignoreCase = true
                        )
                    ) {
                        val enveuCommonResponse = assetResponse.baseCategory as RailCommonData
                        if (enveuCommonResponse != null && enveuCommonResponse.enveuVideoItemBeans.size > 0) {
                            // videoDetails = enveuCommonResponse.getEnveuVideoItemBeans().get(0);
                            if (!enveuCommonResponse.enveuVideoItemBeans[0].externalRefId.equals(
                                    "",
                                    ignoreCase = true
                                ) && !enveuCommonResponse.enveuVideoItemBeans[0].externalRefId.equals(
                                    null,
                                    ignoreCase = true
                                )
                            ) {
                                binding!!.metaDetails.watchTrailer.visibility = View.VISIBLE
                                trailerExternalRefId =
                                    enveuCommonResponse.enveuVideoItemBeans[0].externalRefId
                                trailerUrl =
                                    SDKConfig.getInstance().playbacK_URL + enveuCommonResponse.enveuVideoItemBeans[0].externalRefId + ".m3u8"
                            }
                        }
                    } else if (assetResponse.status.equals(
                            APIStatus.ERROR.name,
                            ignoreCase = true
                        )
                    ) {
                        if (assetResponse.errorModel != null && assetResponse.errorModel.errorCode != 0) {
                            commonDialog(stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong)),
                                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)
                                )
                            )                        }
                    } else if (assetResponse.status.equals(
                            APIStatus.FAILURE.name,
                            ignoreCase = true
                        )
                    ) {
                        commonDialog(stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong)),
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)
                            )
                        )                    }
                }
            }
    }

    private fun parseVideoDetails(videoDetails: EnveuVideoItemBean?) {
        dismissLoading(binding!!.progressBar)
        if (videoDetails!!.signedLangParentRefId != null) {
            signLangParentRefId = videoDetails.signedLangParentRefId
        }
        if (videoDetails.signedLangRefId != null) {
            signLangRefId = videoDetails.signedLangRefId
        }
        if (videoDetails.isPodcast != null) {
            isPodcast = videoDetails.isPodcast
            KsPreferenceKeys.getInstance().setPodId(videoDetails.brightcoveVideoId, true)
        }
        ImageHelper.getInstance(this).loadListImage(
            binding!!.playerImage,
            AppCommonMethod.getListLDSImage(videoDetails.posterURL, this)
        )
        // ImageHelper.getInstance(EpisodeActivity.this).loadListImage(getBinding().playerImage, videoDetails.getPosterURL());
        Logger.d("getBrightcoveVideoId " + videoDetails.brightcoveVideoId)
        if (fromBingWatch) {
            if (AppCommonMethod.getCheckBCID(videoDetails.brightcoveVideoId)) {
                isLogin = preference!!.appPrefLoginStatus
                if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                    if (!preference!!.entitlementStatus) {
                        GetPlansLayer.getInstance().getEntitlementStatus(
                            preference, token
                        ) { entitlementStatus: Boolean, apiStatus: Boolean ->
                            binding!!.pBar.visibility = View.GONE
                            if (entitlementStatus && apiStatus) {
                                isAdShowingToUser = false
                            }
                            brightCoveVideoId = videoDetails.brightcoveVideoId.toLong()
                            playPlayerWhenShimmer()
                        }
                    } else {
                        binding!!.pBar.visibility = View.GONE
                        brightCoveVideoId = videoDetails.brightcoveVideoId.toLong()
                        playPlayerWhenShimmer()
                    }
                } else {
                    brightCoveVideoId = videoDetails.brightcoveVideoId.toLong()
                    playPlayerWhenShimmer()
                }
            }
        }
        seriesId = if (StringUtils.isNullOrEmptyOrZero(videoDetails.parentalSeriesId)) {
            -1
        } else {
            if (videoDetails.parentalSeriesId != null) {
                videoDetails.parentalSeriesId.toInt()
            } else {
                -1
            }
        }
        if (!fromBingWatch) {
            if (videoDetails.seasonNumber != null && !videoDetails.seasonNumber.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                getSeriesDetail(seriesId, videoDetails.seasonNumber)
            } else {
                getSeriesDetail(seriesId, "1")
            }
        } else {
            if (seasonTabFragment != null) {
                seasonTabFragment!!.updateCurrentAsset(videoDetails.id)
            }
        }
        setUI(videoDetails)
    }

    private fun getSeriesDetail(seriesId: Int, seasonNumber: String) {
        Logger.d("season: $seasonNumber | seriesId: $seriesId")
        if (seriesId == -1) {
            setTabs(seasonNumber, false)
            setUserInteractionFragment(assetId, null)
        } else {
            railInjectionHelper!!.getSeriesDetailsV2(seriesId.toString(), false).observe(
                this@EpisodeActivity
            ) { response: ResponseModel<*>? ->
                if (response != null) {
                    if (response.status.equals(APIStatus.START.name, ignoreCase = true)) {
                    } else if (response.status.equals(APIStatus.SUCCESS.name, ignoreCase = true)) {
                        if (response.baseCategory != null) {
                            val enveuCommonResponse = response.baseCategory as RailCommonData
                            seriesDetailBean = enveuCommonResponse.enveuVideoItemBeans[0]
                            id = enveuCommonResponse.enveuVideoItemBeans[0].id
                            setTabs(seasonNumber, true)
                            setUserInteractionFragment(assetId, seriesDetailBean)
                        }
                    } else if (response.status.equals(APIStatus.ERROR.name, ignoreCase = true)) {
                        if (response.errorModel.errorCode != 0) {
                            stopShimmer()
                            commonDialog(stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong)),
                                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)
                                )
                            )                        }
                    } else if (response.status.equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                        stopShimmer()
                        commonDialog(stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong)),
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)
                            )
                        )                    }
                }
            }
        }
    }

    private fun setUserInteractionFragment(id: Int, seriesDetailBean: EnveuVideoItemBean?) {

        // String seriesVideoId = seriesDetailBean.getBrightcoveVideoId();
        val transaction = supportFragmentManager.beginTransaction()
        val args = Bundle()
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id)
        args.putSerializable(AppConstants.BUNDLE_SERIES_DETAIL, videoDetails)
        //args.putString(AppConstants.BUNDLE_SERIES_ID, seriesVideoId);
        //  args.putString(AppConstants.BUNDLE_TRAILER_REF_ID, seriesDetailBean.getTrailerReferenceId());
        userInteractionFragment = UserInteractionFragment()
        userInteractionFragment!!.arguments = args
        transaction.replace(R.id.fragment_user_interaction, userInteractionFragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setUI(responseDetailPlayer: EnveuVideoItemBean?) {
        Log.d("Javed", "setUI: $responseDetailPlayer")
        if (responseDetailPlayer!!.assetCast != null && !responseDetailPlayer.assetCast[0].equals(
                "",
                ignoreCase = true
            )
        ) {
            var stringBuilder = StringBuilder()
            for (i in responseDetailPlayer.assetCast.indices) {
                stringBuilder = if (i == responseDetailPlayer.assetCast.size - 1) {
                    stringBuilder.append(responseDetailPlayer.assetCast[i])
                } else stringBuilder.append(responseDetailPlayer.assetCast[i]).append(", ")
            }
            binding!!.casttext = " $stringBuilder"
        } else {
            binding!!.metaDetails.llCastView.visibility = View.GONE
        }
        if (responseDetailPlayer.assetGenres != null && !responseDetailPlayer.assetGenres[0].equals(
                "",
                ignoreCase = true
            )
        ) {
            var stringBuilder = StringBuilder()
            for (i in responseDetailPlayer.assetGenres.indices) {
                stringBuilder = if (i == responseDetailPlayer.assetGenres.size - 1) {
                    stringBuilder.append(responseDetailPlayer.assetGenres[i])
                } else stringBuilder.append(responseDetailPlayer.assetGenres[i]).append(", ")
            }
            binding!!.crewtext = " $stringBuilder"
        } else {
            binding!!.metaDetails.llCrewView.visibility = View.GONE
        }
        setDetails(responseDetailPlayer)
    }

    private fun logoutUser() {
        isLoggedOut = false
        if (AppConstants.UserStatus.Login.toString().equals(isLogin, ignoreCase = true)
            && CheckInternetConnection.isOnline(this@EpisodeActivity)
        ) {
            clearCredientials(preference)
            hitApiLogout(this@EpisodeActivity, preference!!.appPrefAccessToken)
        }
    }



    private fun setDetails(responseDetailPlayer: EnveuVideoItemBean?) {
        if (responseDetailPlayer!!.assetType != null && responseDetailPlayer.duration > 0) {
            val durationInMinutes =
                AppCommonMethod.stringForTime(responseDetailPlayer.duration) + " " + stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_minutes.toString(),
                    getString(R.string.popup_minutes)
                )
            setCustomFields(responseDetailPlayer, durationInMinutes)
        } else {
            setCustomFields(responseDetailPlayer, "")
        }
        if (responseDetailPlayer.description != null && responseDetailPlayer.description.equals(
                "",
                ignoreCase = true
            )
        ) {
            binding!!.metaDetails.descriptionText.visibility = View.GONE
        }
        binding!!.responseApi = responseDetailPlayer
        if (isLogin.equals(
                AppConstants.UserStatus.Login.toString(),
                ignoreCase = true
            )
        ) addToWatchHistory()
    }

    private fun setCustomFields(videoItemBean: EnveuVideoItemBean?, duration: String) {
        try {

            if (videoItemBean?.title != null) {
                binding!!.metaDetails.tvTitle.text = videoItemBean.title
            } else {
                binding!!.metaDetails.tvTitle.visibility = View.GONE
            }
            if (videoItemBean?.longDescription != null) {
                binding!!.metaDetails.descriptionText.htmlParseToString(videoItemBean.longDescription)
            } else {
                binding!!.metaDetails.descriptionText.visibility = View.GONE
            }
            val duration1 = videoItemBean?.duration.toString()
            var properDuration = ""
            properDuration = if (!StringUtils.isNullOrEmpty(duration1)) {
                AppCommonMethod.convertToMinutes(duration1.toLong()) + " " + stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_minutes.toString(),
                    getString(R.string.popup_minutes)
                )
            } else {
                "00:00"
            }
            if (properDuration != null) {
                setTextOrHide(binding!!.metaDetails.duration, duration)
            } else {
                binding!!.metaDetails.duration.visibility = View.INVISIBLE
            }
            if (keyword != null) {
                keyword = keyword!!.replace(",".toRegex(), " \u25AA ")
                binding!!.metaDetails.time.text = keyword
            } else {
                binding!!.metaDetails.time.visibility = View.GONE
            }
            if (videoItemBean?.quality != null) {
                binding!!.metaDetails.qualityPic.text = videoItemBean.quality
            } else {
                binding!!.metaDetails.durationLl.visibility = View.GONE
            }
            if (videoItemBean?.producer != null) {
                var producer = videoItemBean.producer
                producer = producer.replace(",".toRegex(), " \u25AA ")
                binding!!.metaDetails.producedDescrption.text = producer
            } else {
                binding!!.metaDetails.producedDescrption.visibility = View.GONE
                binding!!.metaDetails.produced.visibility = View.GONE
            }
            if (videoItemBean?.sponsors != null) {
                var sponsors = videoItemBean.sponsors
                sponsors = sponsors.replace(",".toRegex(), " \u25AA ")
                binding!!.metaDetails.sponseredDescription.text = sponsors
            } else {
                binding!!.metaDetails.sponseredDescription.visibility = View.GONE
                binding!!.metaDetails.sponsered.visibility = View.GONE
            }
            if ("true".equals(videoItemBean?.comingSoon, ignoreCase = true)) {
                binding!!.backButton.visibility = View.VISIBLE
            } else {
                binding!!.backButton.visibility = View.VISIBLE
                binding!!.pBar.visibility = View.GONE
                if (!AppCommonMethod.getCheckBCID(videoDetails!!.brightcoveVideoId)) {
                    isLogin = preference!!.appPrefLoginStatus
                    if (isLogin.equals(
                            AppConstants.UserStatus.Login.toString(),
                            ignoreCase = true
                        )
                    ) {
                        if (!preference!!.entitlementStatus) {
                            GetPlansLayer.getInstance().getEntitlementStatus(
                                preference, token
                            ) { entitlementStatus: Boolean, apiStatus: Boolean ->
                                binding!!.pBar.visibility = View.GONE
                                if (entitlementStatus && apiStatus) {
                                    isAdShowingToUser = false
                                }
                                if (videoDetails!!.brightcoveVideoId != null && !videoDetails!!.brightcoveVideoId.equals(
                                        "",
                                        ignoreCase = true
                                    )
                                ) {
                                    brightCoveVideoId = videoDetails!!.brightcoveVideoId.toLong()
                                }
                                playPlayerWhenShimmer()
                            }
                        } else {
                            binding!!.pBar.visibility = View.GONE
                            brightCoveVideoId = videoDetails!!.brightcoveVideoId.toLong()
                            playPlayerWhenShimmer()
                        }
                    } else {
                        binding!!.pBar.visibility = View.GONE
                        brightCoveVideoId = 0L
                        playPlayerWhenShimmer()
                    }
                } else {
                    binding!!.pBar.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    private fun showEpisodeNumber(videoItemBean: EnveuVideoItemBean) {}
    private fun addToWatchHistory() {
        val bookmarkingViewModel = ViewModelProvider(this)[BookmarkingViewModel::class.java]
        bookmarkingViewModel.addToWatchHistory(token, assetId)
    }

    private fun noConnectionLayout() {
        binding!!.llParent.visibility = View.GONE
        binding!!.noConnectionLayout.visibility = View.VISIBLE
        binding!!.connection.retryTxt.setOnClickListener { connectionObserver() }
    }

    private var receiver: NetworkChangeReceiver? = null
    override fun onBackPressed() {
        if (preference!!.appPrefJumpBack) {
            preference!!.appPrefJumpBackId = 0
            preference!!.appPrefVideoPosition = 0.toString()
            preference!!.appPrefJumpBack = false
            preference!!.appPrefGotoPurchase = false
            preference!!.appPrefIsEpisode = false
        }
        preference!!.appPrefAssetId = 0
        AppCommonMethod.seasonId = -1
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        if (receiver != null) {
            unregisterReceiver(receiver)
            if (NetworkChangeReceiver.connectivityReceiverListener != null) NetworkChangeReceiver.connectivityReceiverListener =
                null
        }
        releaseAudioFocusForMyApp(this@EpisodeActivity)
    }

    override fun onDestroy() {
        preference!!.appPrefAssetId = 0
        preference!!.appPrefJumpTo = ""
        preference!!.appPrefBranchIo = false
        AppCommonMethod.seasonId = -1
        super.onDestroy()
    }

    override fun onFinishDialog() {
        Logger.w("onfinishdialog", "episode")
        if (isLoggedOut) logoutUser()
        if (isPlayerError) {
            binding!!.playerImage.visibility = View.VISIBLE
            ImageHelper.getInstance(this@EpisodeActivity)
                .loadListImage(binding!!.playerImage, videoDetails!!.posterURL)
            isPlayerError = false
        } else {
            finish()
        }
    }

    private fun setConnectivityListener(listener: NetworkChangeReceiver.ConnectivityReceiverListener?) {
        NetworkChangeReceiver.connectivityReceiverListener = listener
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {}
    override fun onAudioFocusChange(focusChange: Int) {
        val manager = this.getSystemService(AUDIO_SERVICE) as AudioManager
        if (manager.isMusicActive) {
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> Logger.i("AUDIOFOCUS_GAIN")
                AudioManager.AUDIOFOCUS_LOSS -> Logger.e("AUDIOFOCUS_LOSS")
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> Logger.e("AUDIOFOCUS_LOSS_TRANSIENT")
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> Logger.e("AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
                else -> {}
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        try {
            Logger.d("configuration changed: " + newConfig.orientation)
            Logger.d("language: " + LanguageLayer.getCurrentLanguageCode())
            val isCastConnected = false
            if (!isCastConnected) {
                super.onConfigurationChanged(newConfig)
                if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setupLandscapeView()
                } else {
                    setupPortraitView()
                }
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    private fun setupPortraitView() {
        val params = binding!!.playerFrame.layoutParams as ConstraintLayout.LayoutParams
        params.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        params.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
        binding!!.playerFrame.layoutParams = params
        val set = ConstraintSet()
        set.clone(binding!!.llParent)
        set.connect(
            R.id.player_frame,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        set.connect(
            R.id.player_frame,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        set.connect(
            R.id.player_frame,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        set.setDimensionRatio(R.id.player_frame, "H,16:9")
        set.applyTo(binding!!.llParent)
        binding!!.rootScroll.visibility = View.VISIBLE
    }

    private fun setupLandscapeView() {
        binding!!.rootScroll.visibility = View.GONE
        val params = binding!!.playerFrame.layoutParams as ConstraintLayout.LayoutParams
        params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
        params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
        binding!!.playerFrame.layoutParams = params
    }

    override fun railItemClick(item: RailCommonData, position: Int) {
        if (item.screenWidget.type != null && Layouts.HRO.name.equals(
                item.screenWidget.layout,
                ignoreCase = true
            )
        ) {
            Toast.makeText(
                this@EpisodeActivity,
                item.screenWidget.landingPageType,
                Toast.LENGTH_LONG
            ).show()
        } else {
            if (AppCommonMethod.getCheckBCID(item.enveuVideoItemBeans[position].brightcoveVideoId)) {
                val getVideoId = item.enveuVideoItemBeans[position].brightcoveVideoId.toLong()
                AppCommonMethod.redirectionLogic(this, item, position)
                // AppCommonMethod.launchDetailScreen(this, getVideoId, AppConstants.Video, item.getEnveuVideoItemBeans().get(position).getId(), "0", false);
            }
        }
    }

    override fun moreRailClick(data: RailCommonData, position: Int, multilingualTitle: String) {
        if (data.screenWidget != null && data.screenWidget.contentID != null) {
            val playListId = data.screenWidget.contentID
            var screenName: String? = ""
            if (data.screenWidget.name != null) {
                screenName = data.screenWidget.name as String?
            }
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("playListId", playListId)
            intent.putExtra("title", screenName)
            intent.putExtra("flag", 0)
            intent.putExtra("shimmerType", 0)
            intent.putExtra("baseCategory", data.screenWidget)
            startActivityForResult(intent, 1001)
        }
    }

    private var fromBingWatch = false
    private var nextEpisode: EnveuVideoItemBean? = null
    private var seasonEpisodesList: MutableList<EnveuVideoItemBean>? = null
    fun episodesList(seasonEpisodes: List<EnveuVideoItemBean>?) {
        try {
            if (seasonEpisodes != null) {
                seasonEpisodesList = ArrayList()
                seasonEpisodesList?.addAll(seasonEpisodes)
                //                int size =0;
//                for (int i = 0; i < size; i++) {
//                    if (seasonEpisodes.get(i).getBrightcoveVideoId() != null) {
//                        String id = seasonEpisodes.get(i).getBrightcoveVideoId();
//                        if (id.equalsIgnoreCase(String.valueOf(brightCoveVideoId))) {
//                            seasonEpisodesList.addAll(seasonEpisodes);
//                            break;
//                        }
//                    }
//                }
            }
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }

    fun showSeasonList(list: ArrayList<SelectedSeasonModel>, selectedSeasonId: Int) {
        binding!!.transparentLayout.visibility = View.VISIBLE
        val listAdapter: SeasonListAdapter = SeasonListAdapter(list, selectedSeasonId)
        val builder = AlertDialog.Builder(this@EpisodeActivity)
        val inflater = LayoutInflater.from(this@EpisodeActivity)
        val content = inflater.inflate(R.layout.season_custom_dialog, null)
        builder.setView(content)
        val mRecyclerView = content.findViewById<RecyclerView>(R.id.my_recycler_view)
        val imageView = content.findViewById<ImageView>(R.id.close)
        imageView.setOnClickListener {
            alertDialog!!.cancel()
            binding!!.transparentLayout.visibility = View.GONE
        }
        mRecyclerView.layoutManager = LinearLayoutManager(this@EpisodeActivity)
        mRecyclerView.adapter = listAdapter
        alertDialog = builder.create()
        alertDialog?.window!!.setBackgroundDrawable(
            ContextCompat.getDrawable(this@EpisodeActivity, R.color.transparent)
        )
        alertDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (alertDialog?.window != null) alertDialog?.window!!.attributes.windowAnimations =
            R.style.SlidingDialogAnimation
        alertDialog?.show()
        val lWindowParams = WindowManager.LayoutParams()
        lWindowParams.copyFrom(alertDialog?.window!!.attributes)
        lWindowParams.width = ViewGroup.LayoutParams.MATCH_PARENT // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT
        alertDialog?.window!!.attributes = lWindowParams
    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm = supportFragmentManager
        val commonDialogFragment = newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }

    override fun onActionBtnClicked() {
        if (isUserNotVerify) {
            ActivityLauncher.getInstance()
                .goToEnterOTP(this, EnterOTPActivity::class.java, "DetailPage")
        }
        if (isUserNotEntitle) {
           ActivityLauncher.getInstance().goToDetailPlanScreen(this, PaymentDetailPage::class.java, true, resEntitle)
        }
    }

    internal inner class SeasonListAdapter(
        private val list: ArrayList<SelectedSeasonModel>,
        private val selectedPos: Int
    ) : RecyclerView.Adapter<SeasonListAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.all_season_listing, parent, false)
            return ViewHolder(view)
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.season.text = list[position].list
            if (list[position].isSelected) {
                holder.season.setTextColor(getColor(R.color.series_detail_description_text_color))
                val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                holder.season.typeface = boldTypeface
            } else {
                holder.season.setTextColor(getColor(R.color.transparent))
                val boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                holder.season.typeface = boldTypeface
            }
            holder.season.setOnClickListener {
                alertDialog!!.cancel()
                binding!!.transparentLayout.visibility = View.GONE
                if (seasonTabFragment != null) {
                    seasonTabFragment!!.updateTotalPages()
                    seasonTabFragment!!.seasonAdapter = null
                    seasonTabFragment!!.selectedSeason = list[position].selectedId
                    showLoading(binding!!.progressBar, true)
                    seasonTabFragment!!.updateTotalPages()
                    seasonTabFragment!!.getSeasonEpisodes()
                }
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var season: TextView

            init {
                season = itemView.findViewById(R.id.season_name)
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        supportsPiPMode()
    }

    private fun supportsPiPMode() {}
    private var isPlayerError = false
    private fun hitUserProfileApi() {
        registrationLoginViewModel = ViewModelProvider(this)[RegistrationLoginViewModel::class.java]
        registrationLoginViewModel!!.hitUserProfile(
            this@EpisodeActivity,
            preference!!.appPrefAccessToken
        ).observe(this@EpisodeActivity) { userProfileResponse ->
            dismissLoading(binding!!.progressBar)
            if (userProfileResponse != null) {
                if (userProfileResponse.data != null) {
                }
                if (userProfileResponse.status) {
                } else {
                    if (userProfileResponse.responseCode == 4302) {
                        clearCredientials(preference)
                        isLoggedIn = false
                    }
                }
            }
        }
    }
}