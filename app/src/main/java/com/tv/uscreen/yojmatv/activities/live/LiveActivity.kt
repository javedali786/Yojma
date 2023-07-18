package com.tv.uscreen.yojmatv.activities.live

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tv.uscreen.yojmatv.Bookmarking.BookmarkingViewModel
import com.tv.uscreen.yojmatv.R

import com.tv.uscreen.yojmatv.SDKConfig
import com.tv.uscreen.yojmatv.activities.listing.listui.ListActivity
import com.tv.uscreen.yojmatv.activities.purchase.planslayer.GetPlansLayer
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.ActivityLogin
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModel.pubnub.PNMessage
import com.tv.uscreen.yojmatv.beanModel.responseModels.detailPlayer.Data
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.MoreClickListner
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.NetworkChangeReceiver
import com.tv.uscreen.yojmatv.databinding.LiveDetailBinding

import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogSingleButtonFragment
import com.tv.uscreen.yojmatv.fragments.player.ui.LiveChatFragment
import com.tv.uscreen.yojmatv.fragments.player.ui.UserInteractionFragment
import com.tv.uscreen.yojmatv.networking.apistatus.APIStatus
import com.tv.uscreen.yojmatv.networking.responsehandler.ResponseModel
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.cropImage.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.CheckInternetConnection
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import com.tv.uscreen.yojmatv.utils.stringsJson.model.StringsData
import java.util.Objects

class LiveActivity : BaseBindingActivity<LiveDetailBinding?>(), AlertDialogFragment.AlertDialogListener, NetworkChangeReceiver.ConnectivityReceiverListener, AudioManager.OnAudioFocusChangeListener,
    MoreClickListner {
    private var isloggedout = false
    private var audioManager: AudioManager? = null
    private var focusRequest: AudioFocusRequest? = null
    private var videoDetails: EnveuVideoItemBean? = null
    private var preference: KsPreferenceKeys? = null
    private var assestId = 0
    private var token: String? = null
    private var isLogin: String? = null
    private var isHitPlayerApi = false
    private var brightCoveVideoId: Long = 0
    private val errorDialogShown = false
    private var bookmarkingViewModel: BookmarkingViewModel? = null
    private var userInteractionFragment: UserInteractionFragment? = null
    private var isLoggedIn = false
    private var liveChatFragment: LiveChatFragment? = null
    private val liveChatMessages: ArrayList<PNMessage> = ArrayList<PNMessage>()
    private val stringsHelper by lazy { StringsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): LiveDetailBinding {
        return LiveDetailBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setBackgroundDrawableResource(R.color.buy_now_pay_now_btn_text_color)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        val orientation: Int = this.getResources().getConfiguration().orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            // code for portrait mode
            showVideoDetail()
        } else {
            // code for landscape mode
            hideVideoDetail()
        }
        //basic settings and do not require internet
        preference = KsPreferenceKeys.getInstance()
        if (preference?.getAppPrefLoginStatus().equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            isLoggedIn = true
        }
        AppCommonMethod.isPurchase = false
        bookmarkingViewModel = ViewModelProvider(this).get<BookmarkingViewModel>(BookmarkingViewModel::class.java)
        setupUI(binding?.llParent)
        isHitPlayerApi = false
        if (intent.hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            var extras: Bundle? = getIntent().getExtras()
            if (extras != null) {
                extras = extras.getBundle(AppConstants.BUNDLE_ASSET_BUNDLE)
                assestId = Objects.requireNonNull<Bundle?>(extras).getInt(AppConstants.BUNDLE_ASSET_ID)
                brightCoveVideoId = Objects.requireNonNull<Bundle?>(extras).getLong(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE)
            }
        } else {
            throw IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All")
        }
        callBinding()
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
        binding?.seriesShimmer?.setVisibility(View.VISIBLE)
        binding!!.mShimmer.colorsData = ColorsHelper
        binding?.mShimmer?.seriesShimmerScroll1?.setEnabled(false)
        binding?.mShimmer?.seriesShimmerScroll2?.setEnabled(false)
        binding?.mShimmer?.seriesShimmerScroll2?.setEnabled(false)
        binding?.llParent?.setVisibility(View.GONE)
        binding?.noConnectionLayout?.setVisibility(View.GONE)
        binding?.mShimmer?.sfShimmer1?.startShimmer()
        binding?.mShimmer?.sfShimmer2?.startShimmer()
        binding?.mShimmer?.sfShimmer3?.startShimmer()
        binding?.mShimmer?.flBackIconImage?.bringToFront()
        binding?.mShimmer?.flBackIconImage?.setOnClickListener(View.OnClickListener { v: View? -> onBackPressed() })
    }

    private fun stopShimmer() {
        Logger.d(brightCoveVideoId.toString())
        binding?.seriesShimmer?.setVisibility(View.GONE)
        binding?.llParent?.setVisibility(View.VISIBLE)
        binding?.noConnectionLayout?.setVisibility(View.GONE)
        binding?.mShimmer?.sfShimmer1?.startShimmer()
        binding?.mShimmer?.sfShimmer2?.startShimmer()
        binding?.mShimmer?.sfShimmer3?.startShimmer()
    }

    fun playPlayerWhenShimmer() {
        val transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
        val bookmarkPosition = 0L
        val args = Bundle()
        args.putString(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, brightCoveVideoId.toString())
        args.putLong(AppConstants.BOOKMARK_POSITION, bookmarkPosition)
        args.putString("selected_track", KsPreferenceKeys.getInstance().getQualityName())
        args.putBoolean("ads_visibility", isAdShowingToUser)
        args.putString("selected_lang", KsPreferenceKeys.getInstance().getAppLanguage())
        if (videoDetails != null) {
            args.putString("vast_tag", videoDetails!!.vastTag)
        }
        if (videoDetails != null && videoDetails!!.assetType != null) {
            args.putString("assetType", videoDetails!!.assetType)
        }
        args.putString("config_vast_tag", SDKConfig.getInstance().getConfigVastTag())
        setArgsForEvent(args)
        if (videoDetails != null && videoDetails!!.isPremium && videoDetails!!.thumbnailImage != null) {
            args.putString(AppConstants.BUNDLE_BANNER_IMAGE, videoDetails!!.thumbnailImage)
        }
        /* playerFragment.setArguments(args);
        transaction.replace(R.id.player_frame, playerFragment, "PlayerFragment");
        transaction.addToBackStack(null);
        transaction.commit();*/binding?.pBar?.setVisibility(View.VISIBLE)
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

    protected override fun onResume() {
        super.onResume()
        requestAudioFocus()
        isloggedout = false
        dismissLoading(binding?.progressBar)
        if (AppCommonMethod.isPurchase) {
            AppCommonMethod.isPurchase = false
            isHitPlayerApi = false
            refreshDetailPage()
        }
        if (!isLoggedIn && preference?.getAppPrefLoginStatus().equals(
                AppConstants.UserStatus.Login.toString(), ignoreCase = true
            )
        ) {
            isLoggedIn = true
            AppCommonMethod.isPurchase = false
            isHitPlayerApi = false
            refreshDetailPage()
        }
        setBroadcast()
        if (preference != null && userInteractionFragment != null) {
            AppCommonMethod.callSocialAction(preference!!, userInteractionFragment)
        }
    }

    fun requestAudioFocus() {
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playbackAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            focusRequest = AudioFocusRequest.Builder(AudioManager.STREAM_MUSIC)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(AudioManager.OnAudioFocusChangeListener { focusChange: Int ->
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        Logger.d("AudioFocus", "Loss")
                    }
                })
                .build()
            audioManager?.requestAudioFocus(focusRequest!!)
        } else {
            audioManager?.requestAudioFocus(
                this, AudioManager.STREAM_VOICE_CALL,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }
    }

    fun releaseAudioFocusForMyApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager?.abandonAudioFocusRequest(focusRequest!!)
        }
    }

    fun openLiveChatFragment() {
        Logger.d("called")
        binding?.fragmentContainer?.setVisibility(View.VISIBLE)
        val fragmentTransaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
        liveChatFragment = LiveChatFragment()
        val args = Bundle()
        args.putParcelableArrayList("messages", liveChatMessages)
        liveChatFragment?.setArguments(args)
        fragmentTransaction.replace(R.id.fragment_container, liveChatFragment!!)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    fun removeLiveChatFragment() {
        val fragmentTransaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
        if (liveChatFragment != null) {
            fragmentTransaction.remove(liveChatFragment!!)
            fragmentTransaction.commit()
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            liveChatFragment = null
            binding?.rootScroll?.setVisibility(View.VISIBLE)
            binding?.fragmentContainer?.setVisibility(View.GONE)
        }
    }

    fun setBroadcast() {
        receiver = NetworkChangeReceiver()
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED")
        filter.addAction("android.net.wifi.STATE_CHANGE")
        this@LiveActivity.registerReceiver(receiver, filter)
        setConnectivityListener(this)
    }

    protected override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.hasExtra(AppConstants.BUNDLE_ASSET_BUNDLE)) {
            val extras: Bundle? = intent.getExtras()
            if (extras != null) {
                assestId = Objects.requireNonNull<Bundle>(intent.getExtras()!!.getBundle(AppConstants.BUNDLE_ASSET_BUNDLE)).getInt(AppConstants.BUNDLE_ASSET_ID)
                Logger.d("newintentCalled: $assestId")
                refreshDetailPage()
            }
        } else {
            throw IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All")
        }
    }

    fun refreshDetailPage() {
        callBinding()
    }

    private fun callBinding() {
        modelCall()
    }

    private fun modelCall() {
        binding?.connection?.retryTxt?.setOnClickListener(View.OnClickListener { view: View? ->
            binding?.llParent?.setVisibility(View.VISIBLE)
            binding?.noConnectionLayout?.setVisibility(View.GONE)
            connectionObserver()
        })
        binding?.backButton?.setOnClickListener(View.OnClickListener { view: View? -> onBackPressed() })
        connectionObserver()
    }

    fun openLoginPage() {
        preference?.setReturnTo(AppConstants.ContentType.VIDEO.toString())
        preference?.setAppPrefJumpBack(true)
        preference?.setAppPrefIsEpisode(false)
        preference?.setAppPrefJumpBackId(assestId)
        ActivityLauncher.getInstance().loginActivity(this@LiveActivity, ActivityLogin::class.java)
    }

    fun uiInitialisation() {
        callShimmer()
        binding?.setCasttext("")
        binding?.setCrewtext("")
        val videoItemBean = EnveuVideoItemBean()
        val data = Data()
        data.contentTitle = ""
        binding?.setResponseApi(videoItemBean)
        setupUI(binding?.llParent)
        //        if (Boolean.TRUE.equals(getBinding().metaDetails.descriptionText.isExpanded())) {
//            resetExpandable();
//        }

//        setExpandable();
        preference?.setAppPrefAssetId(assestId)
        isLogin = preference?.getAppPrefLoginStatus()
        token = preference?.getAppPrefAccessToken()
        binding?.noConnectionLayout?.setVisibility(View.GONE)
        Logger.d("newintentCalled", isHitPlayerApi.toString() + "")
        if (!isHitPlayerApi) {
            assetDetails
        }
    }

    // no impl
    val assetDetails: Unit
        get() {
            isHitPlayerApi = true
            val railInjectionHelper: RailInjectionHelper = ViewModelProvider(this).get<RailInjectionHelper>(
                RailInjectionHelper::class.java
            )
            binding?.pBar?.setVisibility(View.VISIBLE)
            railInjectionHelper.getAssetDetailsV2(assestId.toString(), this@LiveActivity).observe(
                this@LiveActivity, Observer<ResponseModel<*>> { assetResponse: ResponseModel<*>? ->
                    if (assetResponse != null) {
                        if (assetResponse.getStatus().equals(APIStatus.START.name, ignoreCase = true)) {
                            // no impl
                        } else if (assetResponse.getStatus().equals(
                                APIStatus.SUCCESS.name, ignoreCase = true
                            )
                        ) {
                            parseAssetDetails(assetResponse)
                        } else if (assetResponse.getStatus().equals(
                                APIStatus.ERROR.name, ignoreCase = true
                            )
                        ) {
                            if (assetResponse.getErrorModel() != null
                                && assetResponse.getErrorModel().getErrorCode() != 0
                            ) {
                                showDialog(
                                    stringsHelper.stringParse(Objects.requireNonNull<StringsData>(stringsHelper.instance()).data.config.popup_error, getString(R.string.popup_error)),
                                    stringsHelper.stringParse(
                                        Objects.requireNonNull<StringsData>(stringsHelper.instance()).data.config.popup_something_went_wrong,
                                        getString(R.string.popup_something_went_wrong)
                                    )
                                )
                            }
                        } else if (assetResponse.getStatus().equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                            showDialog(
                                stringsHelper.stringParse(Objects.requireNonNull<StringsData>(stringsHelper.instance()).data.config.popup_error, getString(R.string.popup_error)),
                                stringsHelper.stringParse(
                                    Objects.requireNonNull<StringsData>(stringsHelper.instance()).data.config.popup_something_went_wrong, getString(R.string.popup_something_went_wrong)
                                )
                            )
                        }
                    }
                })
        }
    var isAdShowingToUser = true
    private fun parseAssetDetails(assetResponse: ResponseModel<*>) {
        val enveuCommonResponse: RailCommonData = assetResponse.getBaseCategory() as RailCommonData
        if (enveuCommonResponse != null && !enveuCommonResponse.getEnveuVideoItemBeans().isEmpty()) {
            videoDetails = enveuCommonResponse.getEnveuVideoItemBeans().get(0)
            binding?.metaDetails?.descriptionText?.setEllipsize(TextUtils.TruncateAt.END)
            ImageHelper.getInstance(this@LiveActivity).loadListImage(binding?.playerImage, videoDetails!!.posterURL)
            binding?.pBar?.setVisibility(View.VISIBLE)
            if (AppCommonMethod.getCheckBCID(videoDetails!!.brightcoveVideoId)) {
                isLogin = preference?.getAppPrefLoginStatus()
                if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                    if (!preference?.getEntitlementStatus()!!) {
                        GetPlansLayer.getInstance().getEntitlementStatus(
                            preference, token
                        ) { entitlementStatus: Boolean, apiStatus: Boolean ->
                            binding?.pBar?.setVisibility(View.GONE)
                            if (entitlementStatus && apiStatus) {
                                isAdShowingToUser = false
                            }
                            brightCoveVideoId =
                                videoDetails!!.brightcoveVideoId.toLong()
                            playPlayerWhenShimmer()
                        }
                    } else {
                        binding?.pBar?.setVisibility(View.GONE)
                        brightCoveVideoId = videoDetails!!.brightcoveVideoId.toLong()
                        playPlayerWhenShimmer()
                    }
                } else {
                    binding?.pBar?.setVisibility(View.GONE)
                    brightCoveVideoId = videoDetails!!.brightcoveVideoId.toLong()
                    playPlayerWhenShimmer()
                }
            } else {
                binding?.pBar?.setVisibility(View.GONE)
            }
            setUserInteractionFragment(assestId)
            stopShimmer()
            setUI(videoDetails)
        }
    }

    fun setUserInteractionFragment(id: Int) {
        val transaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
        val args = Bundle()
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id)
        args.putSerializable(AppConstants.BUNDLE_SERIES_DETAIL, videoDetails)
        userInteractionFragment = UserInteractionFragment()
        userInteractionFragment?.setArguments(args)
        transaction.replace(R.id.fragment_user_interaction, userInteractionFragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun setUI(responseDetailPlayer: EnveuVideoItemBean?) {
        setDetails(responseDetailPlayer)
    }

    private fun setCustomFields(videoItemBean: EnveuVideoItemBean?) {
        try {
            binding?.metaDetails?.tvTitle?.setText(videoItemBean!!.title)
            //      setTextOrHide(getBinding().metaDetails.country, videoItemBean.getCountry());
            //setTextOrHide(getBinding().metaDetails.tvCompany, videoItemBean.getCompany());
            //   setTextOrHide(getBinding().metaDetails.year, videoItemBean.getYear());
//            getBinding().metaDetails.tvFeature.setVisibility(View.GONE);
//            getBinding().metaDetails.viewSeparatorDetail.setVisibility(View.GONE);
//            getBinding().metaDetails.viewSeparatorFeature.setVisibility(View.GONE);
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }

    fun logoutUser() {
        isloggedout = false
        if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)
            && CheckInternetConnection.isOnline(this@LiveActivity)
        ) {
            clearCredientials(preference)
            hitApiLogout(this@LiveActivity, preference?.getAppPrefAccessToken())
        }
    }

    private fun showDialog(title: String, message: String) {
        val fm: FragmentManager = getSupportFragmentManager()
        val alertDialog: AlertDialogSingleButtonFragment = AlertDialogSingleButtonFragment.newInstance(
            title,
            message,
            stringsHelper.stringParse(Objects.requireNonNull<StringsData>(stringsHelper.instance()).data.config.popup_ok, getString(R.string.popup_ok))
        )
        alertDialog.setCancelable(false)
        alertDialog.setAlertDialogCallBack(this)
        alertDialog.show(fm, "fragment_alert")
    }

    fun setDetails(videoItemBean: EnveuVideoItemBean?) {
        binding?.setResponseApi(videoItemBean)
        setCustomFields(videoItemBean)
        binding?.metaDetails?.descriptionText?.post(Runnable {
            val lineCount: Int = binding?.metaDetails?.descriptionText?.lineCount!!
            binding?.metaDetails?.descriptionText?.setMaxLines(2)
            if (lineCount >= 2) {
                binding?.metaDetails?.lessButton?.setVisibility(View.VISIBLE)
            } else {
                binding?.metaDetails?.lessButton?.setVisibility(View.INVISIBLE)
            }
        })
    }

    private fun noConnectionLayout() {
        binding?.llParent?.setVisibility(View.GONE)
        binding?.noConnectionLayout?.setVisibility(View.VISIBLE)
        //        getBinding().connection.btnMyDownloads.setOnClickListener(view -> {
//            boolean loginStatus = preference.getAppPrefLoginStatus().equalsIgnoreCase(AppConstants.UserStatus.Login.toString());
//            if (loginStatus)
//                ActivityLauncher.getInstance().launchMyDownloads(LiveActivity.this);
//            else
//                ActivityLauncher.getInstance().loginActivity(this, LoginActivity.class);
//        });
    }

    //    private void setExpandable() {
    //        getBinding().setExpandableText(getResources().getString(R.string.more));
    //        getBinding().metaDetails.descriptionText.setEllipsis("...");
    //        getBinding().metaDetails.lessButton.setOnClickListener(this::clickExpandable);
    //    }
    //
    //    public void clickExpandable(View view) {
    //        getBinding().metaDetails.descriptionText.toggle();
    //        getBinding().metaDetails.descriptionText.setEllipsis("...");
    //        if (getBinding().metaDetails.descriptionText.isExpanded()) {
    //            getBinding().metaDetails.descriptionText.setEllipsize(null);
    //        } else {
    //            getBinding().metaDetails.descriptionText.setMaxLines(2);
    //            getBinding().metaDetails.descriptionText.setEllipsize(TextUtils.TruncateAt.END);
    //        }
    //
    //        if (getBinding().metaDetails.descriptionText.isExpanded()) {
    //            getBinding().setExpandableText(getResources().getString(R.string.more));
    //
    //        } else {
    //            getBinding().setExpandableText(getResources().getString(R.string.less));
    //        }
    //
    //
    //    }
    //
    private var receiver: NetworkChangeReceiver? = null

    //
    //    public void resetExpandable() {
    //        getBinding().setExpandableText(getResources().getString(R.string.more));
    //        if (getBinding().metaDetails.descriptionText.isExpanded()) {
    //            getBinding().metaDetails.descriptionText.toggle();
    //            getBinding().metaDetails.descriptionText.setEllipsis("...");
    //        }
    //    }
    override fun onBackPressed() {
        if (liveChatFragment != null) {
            removeLiveChatFragment()
        } else {
            AppCommonMethod.isPurchase = true
            if (preference?.getAppPrefJumpBack() == true) {
                preference?.setAppPrefJumpBackId(0)
                preference?.setAppPrefVideoPosition(0.toString())
                preference?.setAppPrefJumpBack(false)
                preference?.setAppPrefGotoPurchase(false)
                preference?.setAppPrefIsEpisode(false)
            }
            preference?.setAppPrefAssetId(0)
            AppCommonMethod.seasonId = -1
            val orientation: Int = this.getResources().getConfiguration().orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                finish()
            } else {
            }
        }
    }

    protected override fun onPause() {
        Logger.d("True")
        releaseAudioFocusForMyApp()
        dismissLoading(binding?.progressBar)
        super.onPause()
    }

    protected override fun onStop() {
        super.onStop()
        try {
            if (receiver != null) {
                this.unregisterReceiver(receiver)
                NetworkChangeReceiver.connectivityReceiverListener = null
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    protected override fun onDestroy() {
        preference?.setAppPrefAssetId(0)
        preference?.setAppPrefJumpTo("")
        preference?.setAppPrefBranchIo(false)
        AppCommonMethod.seasonId = -1
        super.onDestroy()
    }

    override fun onFinishDialog() {
        if (isloggedout) logoutUser()
        if (isPlayerError) {
            binding?.playerImage?.setVisibility(View.VISIBLE)
            ImageHelper.getInstance(this@LiveActivity).loadListImage(
                binding?.playerImage,
                videoDetails!!.posterURL
            )
            isPlayerError = false
        } else {
            finish()
        }
    }

    private fun setConnectivityListener(listener: NetworkChangeReceiver.ConnectivityReceiverListener?) {
        NetworkChangeReceiver.connectivityReceiverListener = listener
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        // need to handle reconnection
    }

    override fun onAudioFocusChange(focusChange: Int) {
        val manager: AudioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (manager.isMusicActive()) {
            when (focusChange) {
                AudioManager.AUDIOFOCUS_GAIN -> Logger.i("AUDIOFOCUS_GAIN")
                AudioManager.AUDIOFOCUS_LOSS -> Logger.i("AUDIOFOCUS_LOSS")
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> Logger.i("AUDIOFOCUS_LOSS_TRANSIENT")
                AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> Logger.i("AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK")
                else ->                     //
                    Logger.i("default case")
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == 2) {
            hideVideoDetail()
        } else {
            showVideoDetail()
        }
    }

    private fun showVideoDetail() {
        binding?.rootScroll?.visibility = View.VISIBLE
    }

    private fun hideVideoDetail() {
        binding?.rootScroll?.visibility = View.GONE
    }

    override fun moreRailClick(data: RailCommonData, position: Int, multilingualTitle: String) {
        Logger.d(data.screenWidget.contentID + "  " + data.screenWidget.landingPageTitle + " " + 0 + " " + 0)
        if (data.screenWidget != null && data.screenWidget.contentID != null) {
            val playListId: String = data.screenWidget.contentID!!
            if (data.screenWidget.name != null) {
                ActivityLauncher.getInstance().listActivity(
                    this@LiveActivity, ListActivity::class.java, playListId, data.screenWidget.name.toString(), 0, 0,
                    data.screenWidget
                )
            } else {
                ActivityLauncher.getInstance().listActivity(this@LiveActivity, ListActivity::class.java, playListId, "", 0, 0, data.screenWidget)
            }
        }
    }

    protected override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (supportsPiPMode()) {
            try {
                //PictureInPictureManager.getInstance().onUserLeaveHint();
            } catch (ex: Exception) {
                Logger.w(ex)
            }
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (supportsPiPMode()) {
        }
    }

    private fun supportsPiPMode(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    private var isPlayerError = false
}