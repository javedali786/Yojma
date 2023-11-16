package com.tv.uscreen.yojmatv.activities.detail.ui

import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.enveu.client.enums.Layouts
import com.example.jwplayer.PlayerActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
import com.google.gson.Gson
import com.tv.uscreen.yojmatv.Bookmarking.BookmarkingViewModel
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.SDKConfig
import com.tv.uscreen.yojmatv.activities.detail.viewModel.DetailViewModel
import com.tv.uscreen.yojmatv.activities.detail.viewModel.Response
import com.tv.uscreen.yojmatv.activities.downloads.NetworkHelper.isWifiEnabled
import com.tv.uscreen.yojmatv.activities.listing.listui.ListActivity
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.ActivityLogin
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.EnterOTPActivity
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.PaymentDetailPage
import com.tv.uscreen.yojmatv.activities.usermanagment.viewmodel.RegistrationLoginViewModel
import com.tv.uscreen.yojmatv.adapters.player.EpisodeTabAdapter
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.entitle.ResponseEntitle
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModel.responseModels.detailPlayer.Data
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.CommonRailtItemClickListner
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.MoreClickListner
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.NetworkChangeReceiver
import com.tv.uscreen.yojmatv.databinding.DetailScreenBinding
import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment.Companion.newInstance
import com.tv.uscreen.yojmatv.fragments.player.ui.RelatedContentFragment
import com.tv.uscreen.yojmatv.fragments.player.ui.UserInteractionFragment
import com.tv.uscreen.yojmatv.networking.apistatus.APIStatus
import com.tv.uscreen.yojmatv.networking.responsehandler.ResponseModel
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.AppColors
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper.strokeBgDrawable
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.cropImage.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.CheckInternetConnection
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.htmlParseToString
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class DetailActivity : BaseBindingActivity<DetailScreenBinding?>(),
    AlertDialogFragment.AlertDialogListener, NetworkChangeReceiver.ConnectivityReceiverListener,
    OnAudioFocusChangeListener,
    CommonRailtItemClickListner, MoreClickListner, CommonDialogFragment.EditDialogListener {
    private var isLoggedOut = false
    private var audioManager: AudioManager? = null
    private var focusRequest: AudioFocusRequest? = null
    private var videoDetails: EnveuVideoItemBean? = null
    private var viewModel: DetailViewModel? = null
    private var preference: KsPreferenceKeys? = KsPreferenceKeys.getInstance()
    private var episodeTabAdapter: EpisodeTabAdapter? = null
    private var assetId = 0
    private var token: String? = null
    private var isLogin: String? = null
    private var isHitPlayerApi = false
    private val brightCoveVideoId: Long = 0
    private var bookmarkingViewModel: BookmarkingViewModel? = null
    private var userInteractionFragment: UserInteractionFragment? = null
    private var isLoggedIn = false
    private var isUserVerified: String? = null
    private var isUserNotVerify = false
    private var isGeoBlocking = false
    private var isUserNotEntitle = false
    private var keyword: String? = ""
    private val videoType = ""
    private val contentType = ""
    private var playbackUrl: String? = null
    private var relatedContentFragment: RelatedContentFragment? = null
    private var trailerUrl: String? = null
    private var trailerExternalRefId: String? = null
    private var registrationLoginViewModel: RegistrationLoginViewModel? = null
    private val preferenceData = ""
    private val speciesList: List<String> = ArrayList()
    private val typeList: List<String> = ArrayList()
    private val colorsHelper by lazy { ColorsHelper }
    private val stringsHelper by lazy { StringsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): DetailScreenBinding {
        return DetailScreenBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseColor()
        window.setBackgroundDrawableResource(R.color.app_bg_color)
        preference
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setupPortraitView()
        } else {
            setupLandscapeView()
        }
        if (preference?.appPrefLoginStatus.equals(
                AppConstants.UserStatus.Login.toString(),
                ignoreCase = true
            )
        ) {
            isLoggedIn = true
        }
        isUserVerified = preference?.isVerified
        AppCommonMethod.isPurchase = false
        viewModel = ViewModelProvider(this@DetailActivity)[DetailViewModel::class.java]
        bookmarkingViewModel = ViewModelProvider(this)[BookmarkingViewModel::class.java]
        token = preference?.appPrefAccessToken
        setupUI(binding!!.llParent)
        isHitPlayerApi = false
        if (intent.hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            var extras = intent.extras
            if (extras != null) {
                extras = extras.getBundle(AppConstants.BUNDLE_ASSET_BUNDLE)
                assetId = extras!!.getInt(AppConstants.BUNDLE_ASSET_ID)
            }
        } else {
            throw IllegalArgumentException("Activity cannot find extras Search_Show_All")
        }
        /* if (isLoggedIn) {
             hitUserProfileApi()
         }*/
        checkGeoBlocking()
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

    private fun hitUserProfileApi() {
        registrationLoginViewModel = ViewModelProvider(this)[RegistrationLoginViewModel::class.java]
        registrationLoginViewModel!!.hitUserProfile(this@DetailActivity, token)
            .observe(this@DetailActivity) { userProfileResponse ->
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

    private fun callShimmer() {
        binding!!.seriesShimmer.visibility = View.VISIBLE
        binding!!.mShimmer.colorsData = colorsHelper
        binding!!.mShimmer.seriesShimmerScroll1.isEnabled = false
        binding!!.mShimmer.seriesShimmerScroll2.isEnabled = false
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
        Logger.e("stopShimmer: $brightCoveVideoId")
        binding!!.seriesShimmer.visibility = View.GONE
        binding!!.llParent.visibility = View.VISIBLE
        binding!!.noConnectionLayout.visibility = View.GONE
        binding!!.mShimmer.sfShimmer1.startShimmer()
        binding!!.mShimmer.sfShimmer2.startShimmer()
        binding!!.mShimmer.sfShimmer3.startShimmer()
    }

    private fun playPlayerWhenShimmer() {
        binding!!.pBar.visibility = View.GONE
        viewModel!!.getBookMarkByVideoId(token, videoDetails!!.id)
            .observe(this) { binding!!.backButton.visibility = View.VISIBLE }
        playerFragment()
    }

    private fun playerFragment() {
        val transaction = supportFragmentManager.beginTransaction()
    }

    private fun setTabs(id: Int) {
        binding?.tabLayout?.setTabTextColors(
            AppColors.detailPageTabItemUnselectedTxtColor(),
            AppColors.detailPageTabItemSelectedTxtColor(),
        )
        relatedContentFragment = RelatedContentFragment()
        val args = Bundle()
        args.putString("videoType", AppConstants.Movie)
        args.putString("contentType", AppConstants.VIDEO)
        args.putInt(AppConstants.ID, id)

        relatedContentFragment!!.arguments = args
        binding!!.tabLayout.setSelectedTabIndicatorGravity(TabLayout.INDICATOR_GRAVITY_TOP)
        episodeTabAdapter = EpisodeTabAdapter(supportFragmentManager)
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
            strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        binding?.tabLayout?.getTabAt(0)?.view?.background = strokeBgDrawable(
            AppColors.detailPageTabUnselectedBorderColor(),
            AppColors.detailPageTabUnselectedBorderColor(),
            0f
        )
        if (episodeTabAdapter!!.count > 1) {
            binding?.tabLayout?.getTabAt(1)?.view?.background = strokeBgDrawable(
                AppColors.detailPageTabSelectedBorderColor(),
                AppColors.detailPageTabUnselectedBorderColor(),
                0f
            )
        }
        binding?.tabLayout?.addOnTabSelectedListener(object :
            BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                showLoading(binding!!.progressBar, true)
                tab?.view?.background = strokeBgDrawable(
                    AppColors.detailPageTabUnselectedBorderColor(),
                    AppColors.detailPageTabUnselectedBorderColor(),
                    0f
                )
                Handler().postDelayed({ dismissLoading(binding!!.progressBar) }, 1500)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.view?.background = strokeBgDrawable(
                    AppColors.detailPageTabSelectedBorderColor(),
                    AppColors.detailPageTabUnselectedBorderColor(),
                    0f
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
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
    }

    private fun changeTabsFont() {
        try {
            val vg = binding?.tabLayout?.getChildAt(0) as ViewGroup
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
            Logger.w("Exception", ignored.message.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        requestAudioFocus()
        isLoggedOut = false
        dismissLoading(binding!!.progressBar)
        if (AppCommonMethod.isPurchase) {
            AppCommonMethod.isPurchase = false
            isHitPlayerApi = false
            refreshDetailPage()
        }
        if (!isLoggedIn) {
            if (preference!!.appPrefLoginStatus.equals(
                    AppConstants.UserStatus.Login.toString(),
                    ignoreCase = true
                )
            ) {
                isLoggedIn = true
                AppCommonMethod.isPurchase = false
                isHitPlayerApi = false
                refreshDetailPage()
            }
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

            // AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            focusRequest = AudioFocusRequest.Builder(AudioManager.STREAM_MUSIC)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener { focusChange: Int ->
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        Logger.d("AudioFocus", "Loss")
                    }
                }
                .build()
            audioManager!!.requestAudioFocus(focusRequest!!)
        } else {
            audioManager!!.requestAudioFocus(
                this, AudioManager.STREAM_VOICE_CALL,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }
    }

    private fun releaseAudioFocusForMyApp() {
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
        this@DetailActivity.registerReceiver(receiver, filter)
        setConnectivityListener(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            val extras = intent.extras
            if (extras != null) {
                //extras = extras.getBundle("assestIdBundle");
                assetId = intent.extras!!.getBundle(AppConstants.BUNDLE_ASSET_BUNDLE)!!
                    .getInt(AppConstants.BUNDLE_ASSET_ID)
                Logger.d("newintentCalled", assetId.toString() + "")
                refreshDetailPage()
            }
        } else {
            throw IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All")
        }
    }

    private fun refreshDetailPage() {
        callBinding()
    }

    private fun callBinding() {
        modelCall()
    }
    private var response1: Response? = null
    private fun checkGeoBlocking() {
        viewModel!!.getGeoBlocking(assetId.toString())
            .observe(this@DetailActivity) { response ->
                if (response != null && response.data != null) {
                    if(response.data.isIsBlocked) {
                        isGeoBlocking = true
                    }
                } else {
                    if (response!!.responseCode != null && response.responseCode == 4302) {

                    } else {
                        commonDialog(
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                getString(R.string.popup_error)
                            ),
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                getString(R.string.popup_something_went_wrong)
                            ),
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                getString(R.string.popup_continue)
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

    fun openLoginPage(context: String?) {
        preference!!.setReturnTo(AppConstants.ContentType.VIDEO.toString())
        preference!!.appPrefJumpBack = true
        preference!!.appPrefIsEpisode = false
        preference!!.appPrefJumpBackId = assetId
        ActivityLauncher.getInstance().loginActivity(this@DetailActivity, ActivityLogin::class.java)
    }

    fun uiInitialisation() {
        callShimmer()
        binding!!.casttext = ""
        binding!!.crewtext = ""
        val player = EnveuVideoItemBean()
        val data = Data()
        data.contentTitle = ""
        if (player.description != null && player.description.equals("", ignoreCase = true)) {
            binding!!.metaDetails.descriptionText.visibility = View.GONE
        }
        binding!!.responseApi = player
        setupUI(binding!!.llParent)
        if (binding!!.metaDetails.expandableLayout.isExpanded) // resetExpandable();
        //   setExpandable();
            preference!!.appPrefAssetId = assetId
        isLogin = preference!!.appPrefLoginStatus
        binding!!.noConnectionLayout.visibility = View.GONE
        Logger.d("new intent Called: $isHitPlayerApi")
        setClicks()
        if (!isHitPlayerApi) {
            assetDetails
        }
    }

    private var resEntitle: ResponseEntitle? = null
    private fun setClicks() {
        binding!!.metaDetails.watchTrailer.setOnClickListener {
            startPlayer(trailerUrl, true, trailerExternalRefId ?: "")
            /*if (isLoggedIn) {
                if (isUserVerified.equals("true", ignoreCase = true)) {

                } else {
                    isUserNotVerify = true
                    commonDialog("", stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_user_not_verify.toString(), getString(R.string.popup_user_not_verify)), stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_verify.toString(), getString(R.string.popup_verify)))
                }
            } else {
                ActivityLauncher.getInstance()
                    .loginActivity(this@DetailActivity, ActivityLogin::class.java)
            }*/
        }
        binding!!.metaDetails.playButton.setOnClickListener {
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
                            if (null != videoDetails!!.externalRefId && !videoDetails!!.externalRefId.equals(
                                    "",
                                    ignoreCase = true
                                )
                            ) {
                                playbackUrl =
                                    SDKConfig.getInstance().playbacK_URL + videoDetails!!.externalRefId + ".m3u8"
                                startPlayer(playbackUrl, false, videoDetails!!.externalRefId)
                            }
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
                        binding!!.progressBar.visibility = View.VISIBLE
                        viewModel!!.hitApiEntitlement(token, videoDetails!!.sku)
                            .observe(this@DetailActivity) { responseEntitle ->
                                binding!!.progressBar.visibility = View.GONE
                                if (responseEntitle != null && responseEntitle.data != null) {
                                    resEntitle = responseEntitle
                                    if (responseEntitle.data.entitled) {
                                        if (isUserVerified.equals("true", ignoreCase = true)) {
                                            if (null != responseEntitle.data.externalRefId && !responseEntitle.data.externalRefId.equals(
                                                    "",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                playbackUrl =
                                                    SDKConfig.getInstance().playbacK_URL + responseEntitle.data.externalRefId + ".m3u8"
                                                startPlayer(
                                                    playbackUrl,
                                                    false,
                                                    responseEntitle.data.externalRefId
                                                )
                                            }
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
                                        isUserNotEntitle = true
                                        commonDialog(
                                            stringsHelper.stringParse(
                                                stringsHelper.instance()?.data?.config?.popup_notEntitled.toString(),
                                                getString(R.string.popup_notEntitled)
                                                ),
                                            stringsHelper.stringParse(
                                                stringsHelper.instance()?.data?.config?.popup_select_plan.toString(),
                                                getString(R.string.popup_select_plan)
                                            ),
                                            stringsHelper.stringParse(
                                                stringsHelper.instance()?.data?.config?.popup_purchase.toString(),
                                                getString(R.string.popup_purchase)
                                            )
                                        )
                                    }
                                } else {
                                    if (responseEntitle!!.responseCode != null && responseEntitle.responseCode == 4302) {
                                        clearCredientials(preference)
                                        ActivityLauncher.getInstance().loginActivity(
                                            this@DetailActivity,
                                            ActivityLogin::class.java
                                        )
                                    } else {
                                        commonDialog(
                                            stringsHelper.stringParse(
                                                stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                                getString(R.string.popup_error)
                                            ),
                                            stringsHelper.stringParse(
                                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                                getString(R.string.popup_something_went_wrong)
                                            ),
                                            stringsHelper.stringParse(
                                                stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                                getString(R.string.popup_continue)
                                            )
                                        )
                                    }
                                }
                            }
                    }
                }
            } else {
                ActivityLauncher.getInstance()
                    .loginActivity(this@DetailActivity, ActivityLogin::class.java)
            }
        }
    }

    private fun startPlayer(playback_url: String?, isTrailer: Boolean, externalRefId: String) {
        ActivityLauncher.getInstance().launchPlayerActitivity(
            this@DetailActivity,
            PlayerActivity::class.java,
            playback_url,
            false,
            null,
            videoDetails!!.id,
            "",
            videoDetails!!.title,
            videoDetails!!.assetType,
            isTrailer,
            false,
            videoDetails!!.posterURL,
            AppConstants.DETAILACTIVITY,externalRefId,videoDetails?.skipintro_startTime?:"",videoDetails?.skipintro_endTime?:"",keyword,
            videoDetails?.audioTrackList
        )
    }

    var railInjectionHelper: RailInjectionHelper? = null
    private val assetDetails: Unit
        get() {
            isHitPlayerApi = true
            railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
            railInjectionHelper!!.getAssetDetailsV2(assetId.toString(), this@DetailActivity)
                .observe(this@DetailActivity) { assetResponse: ResponseModel<*>? ->
                    if (assetResponse != null) {
                        val gson = Gson()
                        val json = gson.toJson(assetResponse.baseCategory)
                        if (assetResponse.status.equals(APIStatus.START.name, ignoreCase = true)) {
                        } else if (assetResponse.status.equals(
                                APIStatus.SUCCESS.name,
                                ignoreCase = true
                            )
                        ) {
                            parseAssetDetails(assetResponse)
                        } else if (assetResponse.status.equals(
                                APIStatus.ERROR.name,
                                ignoreCase = true
                            )
                        ) {
                            if (assetResponse.errorModel != null && assetResponse.errorModel.errorCode != 0) {
                                commonDialog(
                                    stringsHelper.stringParse(
                                        stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                        getString(R.string.popup_error)
                                    ),
                                    stringsHelper.stringParse(
                                        stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                        getString(R.string.popup_something_went_wrong)
                                    ),
                                    stringsHelper.stringParse(
                                        stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                        getString(R.string.popup_continue)
                                    )
                                )
                            }
                        } else if (assetResponse.status.equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                            commonDialog(
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ),
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                    getString(R.string.popup_something_went_wrong)
                                ),
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                    getString(R.string.popup_continue)
                                )
                            )
                        }
                    }
                }
        }
    var isAdShowingToUser = true
    private fun parseAssetDetails(assetResponse: ResponseModel<*>) {
        val enveuCommonResponse = assetResponse.baseCategory as RailCommonData
        if (enveuCommonResponse != null && enveuCommonResponse.enveuVideoItemBeans.size > 0) {
            videoDetails = enveuCommonResponse.enveuVideoItemBeans[0]
            setUserInteractionFragment(assetId)
            binding!!.metaDetails.descriptionText.ellipsize = TextUtils.TruncateAt.END
            keyword = enveuCommonResponse.enveuVideoItemBeans[0].display_tags
            val id = enveuCommonResponse.enveuVideoItemBeans[0].id
            setTabs(id)
            if (videoDetails?.trailerReferenceId != null) {
                videoDetails?.trailerReferenceId?.let { getTrailer(it) }
            }
            ImageHelper.getInstance(this@DetailActivity).loadListImage(
                binding!!.playerImage,
                AppCommonMethod.getListLDSImage(
                    videoDetails?.posterURL.toString(),
                    this@DetailActivity
                )
            )
            // ImageHelper.getInstance(DetailActivity.this).loadListImage(getBinding().playerImage, videoDetails?.getPosterURL());
            setUserInteractionFragment(assetId)
            Logger.w("videoType", videoType)
            stopShimmer()
            setUI(videoDetails)
            Logger.e("video", videoDetails?.duration.toString())
        }
    }

    private fun getTrailer(trailerReferenceId: String) {
        railInjectionHelper!!.getAssetDetailsV2(trailerReferenceId, this@DetailActivity)
            .observe(this@DetailActivity) { assetResponse: ResponseModel<*>? ->
                if (assetResponse != null) {
                    val gson = Gson()
                    val json = gson.toJson(assetResponse.baseCategory)
                    if (assetResponse.status.equals(APIStatus.START.name, ignoreCase = true)) {
                        Log.d(DETAIL_ACTIVITY, "getAssetDetails")
                    } else if (assetResponse.status.equals(
                            APIStatus.SUCCESS.name,
                            ignoreCase = true
                        )
                    ) {
                        val enveuCommonResponse = assetResponse.baseCategory as RailCommonData
                        if (enveuCommonResponse != null && enveuCommonResponse.enveuVideoItemBeans.size > 0) {
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
                    } else if (assetResponse.status.equals(APIStatus.ERROR.name, ignoreCase = true)) {
                        if (assetResponse.errorModel != null && assetResponse.errorModel.errorCode != 0) {
                            commonDialog(
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ),
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                    getString(R.string.popup_something_went_wrong)
                                ),
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                    getString(R.string.popup_continue)
                                )
                            )
                        }
                    } else if (assetResponse.status.equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                        commonDialog(
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                getString(R.string.popup_error)
                            ),
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                getString(R.string.popup_something_went_wrong)
                            ),
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                getString(R.string.popup_continue)
                            )
                        )
                    }
                }
            }
    }

    private fun setUserInteractionFragment(id: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        val args = Bundle()
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id)
        args.putSerializable(AppConstants.BUNDLE_SERIES_DETAIL, videoDetails)
        userInteractionFragment = UserInteractionFragment()
        userInteractionFragment!!.arguments = args
        transaction.replace(R.id.fragment_user_interaction, userInteractionFragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setUI(responseDetailPlayer: EnveuVideoItemBean?) {
        if (responseDetailPlayer!!.assetCast.size > 0 && !responseDetailPlayer.assetCast[0].equals(
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
        if (responseDetailPlayer.assetGenres.size > 0 && !responseDetailPlayer.assetGenres[0].equals(
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
        binding?.metaDetails?.durationLl?.background =
            strokeBgDrawable(AppColors.detailPageHDBgColor(), AppColors.detailPageHDBrColor(), 10f)
    }

    private fun setCustomFields(videoItemBean: EnveuVideoItemBean?, duration: String?) {
        Log.d("duration", "setCustomFields: $duration")
        try {

            if (videoItemBean?.title != null) {
                binding!!.metaDetails.tvTitle.text = videoItemBean.title
            } else {
                binding!!.metaDetails.tvTitle.visibility = View.GONE
            }
            if (videoItemBean?.longDescription != null) {
                binding!!.metaDetails.descriptionText.htmlParseToString(videoItemBean.longDescription)
                // binding!!.metaDetails.descriptionText.text = videoItemBean.longDescription
            } else {
                binding!!.metaDetails.descriptionText.visibility = View.GONE
            }
            if (keyword != null) {
                keyword = keyword!!.replace(",".toRegex(), " \u25AA ")
                binding!!.metaDetails.time.text = keyword
            } else {
                binding!!.metaDetails.time.visibility = View.GONE
            }
            if (duration != null) {
                setTextOrHide(binding!!.metaDetails.duration, duration)
            } else {
                binding!!.metaDetails.duration.visibility = View.INVISIBLE
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
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    private fun logoutUser() {
        isLoggedOut = false
        if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)
            && CheckInternetConnection.isOnline(this@DetailActivity)
        ) {
            clearCredientials(preference)
            hitApiLogout(this@DetailActivity, preference!!.appPrefAccessToken)
        }
    }


    private fun setDetails(responseDetailPlayer: EnveuVideoItemBean?) {
        if (responseDetailPlayer!!.assetType != null && responseDetailPlayer.duration > 0) {
            val durationInMinutes =
                (AppCommonMethod.stringForTime(responseDetailPlayer.duration) + " "
                        + stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_minutes.toString(),
                    getString(R.string.popup_minutes)
                ))
            setCustomFields(responseDetailPlayer, durationInMinutes)
        } else {
            setCustomFields(responseDetailPlayer, "")
        }
        binding!!.responseApi = responseDetailPlayer
        if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            addToWatchHistory()
        }
    }

    private fun addToWatchHistory() {
        bookmarkingViewModel!!.addToWatchHistory(token, assetId)
    }

    private fun noConnectionLayout() {
        binding!!.llParent.visibility = View.GONE
        binding!!.noConnectionLayout.visibility = View.VISIBLE
        binding!!.connection.retryTxt.setOnClickListener { connectionObserver() }
    }

    private var receiver: NetworkChangeReceiver? = null
    override fun onBackPressed() {
        AppCommonMethod.isPurchase = true
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
        Logger.d("here")
        releaseAudioFocusForMyApp()
        dismissLoading(binding!!.progressBar)
        super.onPause()
    }

    override fun onStop() {
        Logger.d("here")
        try {
            if (receiver != null) {
                unregisterReceiver(receiver)
                NetworkChangeReceiver.connectivityReceiverListener = null
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
        super.onStop()
    }

    override fun onDestroy() {
        Logger.d("here")
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
            //  ImageHelper.getInstance(DetailActivity.this).loadListImage(getBinding().playerImage, videoDetails.getPosterURL());
            isPlayerError = false
        } else {
            finish()
        }
    }

    private fun setConnectivityListener(listener: NetworkChangeReceiver.ConnectivityReceiverListener?) {
        NetworkChangeReceiver.connectivityReceiverListener = listener
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (KsPreferenceKeys.getInstance().downloadOverWifi == 1) {
            isWifiEnabled(this)
        } else {
            NetworkConnectivity.isOnline(this)
        }
    }

    override fun onAudioFocusChange(focusChange: Int) {
        val manager = this.getSystemService(AUDIO_SERVICE) as AudioManager
        if (manager.isMusicActive) {
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> Logger.i("AUDIOFOCUS_GAIN")
                AudioManager.AUDIOFOCUS_LOSS -> Logger.i("AUDIOFOCUS_LOSS")
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> Logger.i("AUDIOFOCUS_LOSS_TRANSIENT")
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> Logger.i("AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
                else -> {}
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setupLandscapeView()
        } else {
            setupPortraitView()
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
                this@DetailActivity,
                item.screenWidget.landingPageType,
                Toast.LENGTH_LONG
            ).show()
        } else {
            if (AppCommonMethod.getCheckBCID(item.enveuVideoItemBeans[position].brightcoveVideoId)) {
                val getVideoId = item.enveuVideoItemBeans[position].brightcoveVideoId.toLong()
                AppCommonMethod.redirectionLogic(this, item, position)
                //AppCommonMethod.launchDetailScreen(this, getVideoId, AppConstants.Video, item.getEnveuVideoItemBeans().get(position).getId(), "0", false);
            }
        }
    }

    override fun moreRailClick(data: RailCommonData, position: Int, multilingualTitle: String) {
        Logger.d(data.screenWidget.contentID + "  " + data.screenWidget.landingPageTitle + " " + 0 + " " + 0)
        if (data.screenWidget != null && data.screenWidget.contentID != null) {
            val playListId = data.screenWidget.contentID
            if (data.screenWidget.name != null) {
                ActivityLauncher.getInstance().listActivity(
                    this@DetailActivity,
                    ListActivity::class.java,
                    playListId,
                    data.screenWidget.name.toString(),
                    0,
                    0,
                    data.screenWidget
                )
            } else {
                ActivityLauncher.getInstance().listActivity(
                    this@DetailActivity,
                    ListActivity::class.java,
                    playListId,
                    "",
                    0,
                    0,
                    data.screenWidget
                )
            }
        }
    }

    private var isPlayerError = false
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        return true
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
        } else if (isUserNotEntitle) {
            ActivityLauncher.getInstance()
                .goToDetailPlanScreen(this, PaymentDetailPage::class.java, true, resEntitle)
        }
    }

    companion object {
        private const val DETAIL_ACTIVITY = "DetailActivity"
    }
}