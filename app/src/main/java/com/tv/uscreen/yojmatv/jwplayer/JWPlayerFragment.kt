package com.example.jwplayer

//import com.enveu.player.utils.TrackOptions


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.enveu.client.bookmarking.bean.GetBookmarkResponse
import com.enveu.player.base.BasePlayerFragment
import com.enveu.player.listener.ControlClickListener
import com.enveu.player.model.Progress
import com.enveu.player.model.TrackItem
import com.enveu.player.utils.Network
import com.enveu.player.utils.TrackOptions
import com.example.PlayerListener
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.util.Util
import com.google.android.gms.cast.framework.CastContext
import com.google.gson.Gson
import com.jwplayer.pub.api.JWPlayer
import com.jwplayer.pub.api.PlayerState
import com.jwplayer.pub.api.UiGroup
import com.jwplayer.pub.api.configuration.PlayerConfig
import com.jwplayer.pub.api.configuration.UiConfig
import com.jwplayer.pub.api.events.AudioTracksEvent
import com.jwplayer.pub.api.events.BufferEvent
import com.jwplayer.pub.api.events.CaptionsListEvent
import com.jwplayer.pub.api.events.CompleteEvent
import com.jwplayer.pub.api.events.EventType
import com.jwplayer.pub.api.events.IdleEvent
import com.jwplayer.pub.api.events.MetaEvent
import com.jwplayer.pub.api.events.PlayEvent
import com.jwplayer.pub.api.events.ReadyEvent
import com.jwplayer.pub.api.events.TimeEvent
import com.jwplayer.pub.api.license.LicenseUtil
import com.jwplayer.pub.api.media.audio.AudioTrack
import com.jwplayer.pub.api.media.captions.Caption
import com.jwplayer.pub.api.media.playlists.PlaylistItem
import com.jwplayer.pub.view.JWPlayerView
import com.npaw.youbora.lib6.jwplayer.JWPlayerAdapter
import com.npaw.youbora.lib6.jwplayer.JWPlayerAdsAdapter
import com.npaw.youbora.lib6.plugin.Options
import com.npaw.youbora.lib6.plugin.Plugin
import com.tv.uscreen.yojmatv.BuildConfig
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.SDKConfig
import com.tv.uscreen.yojmatv.activities.detail.viewModel.DetailViewModel
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.ActivityLogin
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.databinding.FragmentJWPlayerBinding
import com.tv.uscreen.yojmatv.fragments.dialog.DialogPlayer
import com.tv.uscreen.yojmatv.jwplayer.cast.PlayDetailResponse
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection

class JWPlayerFragment : BasePlayerFragment(), PlayerListener, DialogPlayer.DialogListener {
    private var mPlayer: JWPlayer? = null
    private var mListener: OnPlayerInteractionListener? = null
    private var mActivity: Activity? = null
    private var externalRefId: String? = null
    private var isChromcastConnected = false
    private var screenName: String? = null
    private var skipIntroStartTime: String = ""
    private var skipIntroEndTime: String = ""
    private var contentPlayed: String? = null
    private var contentDuration: String? = null
    private var contentType: String? = null
    private var id: Int? = null
    private var episodeId: Int? = 0
    private var currentPlayingIndex: Int? = -1
    private var isBingeWatchEnable: Boolean? = false
    var videoTracks: ArrayList<TrackItem>? = ArrayList<TrackItem>()
    var seasonEpisodesList: List<EnveuVideoItemBean>? = null
    var selectedVideoTrack = "Auto"
    private var preference: KsPreferenceKeys? = null
    var isLoggedIn = false
    var isUserVerified = ""
    private var token: String? = null
    private var RefId = ""
    private var sku = ""
    private var viewModel: DetailViewModel? = null
    private var captionList: ArrayList<Caption>? = null
    private var audioList: ArrayList<AudioTrack>? = null

    // protected override lateinit var enveuPlayerControlView: EnveuPlayerControlView
    private var controlClickListener: ControlClickListener? = null
    private var mCastContext: CastContext? = null
    var total: Int? = 0
    private var isInitialised = false
    private var isNetworkConnected = true
    private var lastBookmarkTime = 0L
    private val progress = Progress()
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var isTrailer: Boolean? = false
    private var isLive: Boolean? = false
    private var isBookMarkUpdated: Boolean? = false
    private var bookmarkPosition = 0.0
    private var plugin : Plugin? = null
    private var userId: String? = ""
    private var contentId: String? = null
    private var genre: String? = ""
    private var seriesTittle: String? = ""







    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentJWPlayerBinding.inflate(layoutInflater)
    }

    override fun checkNetworkConnectivity(isConnected: Boolean) {
        this.isNetworkConnected = isConnected
        var playerState = false
        viewBinding.seriesDetailAllEpisodeTxtColors.updateNetworkState(isConnected)
        if (isConnected) {
            mPlayer?.play()
            playerState = true
            // eachSecondHandler.postDelayed(playPositionRunnable, PLAYER_UPDATE_INTERVAL)
            viewBinding.seriesDetailAllEpisodeTxtColors.updatePlayPauseIcon(playerState)
            isLive?.let {
                viewBinding.seriesDetailAllEpisodeTxtColors.toggleControlVisibility(
                    123,
                    it
                )
            }
        } else {
            mPlayer?.pause()
            // eachSecondHandler.removeCallbacksAndMessages(null)
            viewBinding.seriesDetailAllEpisodeTxtColors.hidePlayerControls()
        }
    }

    override fun seekPlayerTo(position: Double) {
        mPlayer?.seek(position)
    }

    override fun seekAfterDisconnected() {
        if (mPlayer != null) {
            mPlayer?.position?.let { mPlayer?.seek(it) }
        }
    }

    override fun isChromeCastConnected(isConnected: Boolean) {
        isChromcastConnected = isConnected
        posterUrl?.let {
            viewBinding.seriesDetailAllEpisodeTxtColors.chromeCastStatus(isConnected, it)
        }
    }

    override fun pause() {
        var playerState = false
        if (mPlayer?.state == PlayerState.PLAYING) {
            mPlayer?.pause()
            playerState = false
        } else if (mPlayer?.state == PlayerState.PAUSED) {
            mPlayer?.play()
            playerState = true
        }
        viewBinding.seriesDetailAllEpisodeTxtColors.updatePlayPauseIcon(playerState)
    }

    override fun play() {
        if (mPlayer?.state == PlayerState.COMPLETE) {
            // restart video
            seekPlayerTo(0.0)
        }
        mPlayer?.play()
        viewBinding.seriesDetailAllEpisodeTxtColors.updatePlayPauseIcon(true)
    }

    private val playerControlClickListener = object : ControlClickListener() {
        override fun onPlayPauseClicked() {
            super.onPlayPauseClicked()
            var playerState = false
            if (mPlayer?.state == PlayerState.PLAYING) {
                mPlayer?.pause()
                playerState = false
            } else if (mPlayer?.state == PlayerState.PAUSED) {
                mPlayer?.play()
                playerState = true
            }
            viewBinding.seriesDetailAllEpisodeTxtColors.updatePlayPauseIcon(playerState)
        }

        override fun onFullscreenClicked() {
            super.onFullscreenClicked()
//            toggleOrientation()
            //  playerListener?.onFullscreenClick()
        }

        override fun onNextClicked() {
            super.onNextClicked()
            onVideoComplete()
        }

        override fun onBackClicked() {
            super.onBackClicked()
            if (activity != null) {
                activity?.finish()
                activity?.onBackPressed()
            }
        }

        override fun onSettingClicked() {
            try {
                super.onSettingClicked()
                if (KsPreferenceKeys.getInstance().appLanguage.equals(
                        "spanish",
                        ignoreCase = true
                    )
                ) {
                    activity?.let { AppCommonMethod.updateLanguage("es", it) }
                } else if (KsPreferenceKeys.getInstance().appLanguage.equals(
                        "English",
                        ignoreCase = true
                    )
                ) {
                    activity?.let { AppCommonMethod.updateLanguage("en", it) }
                }
                var settingList = ArrayList<String>()
                if (!audioList.isNullOrEmpty() && audioList?.size!! > 1)
                    settingList.add(resources.getString(R.string.ep_settings_audio))
                if (!captionList.isNullOrEmpty() && captionList?.size!! > 1)
                    settingList.add(resources.getString(R.string.ep_settings_subtitle))

                if (TrackOptions.createVideoTracks(activity!!, mPlayer?.qualityLevels!!)
                        .isNotEmpty()
                ) {
                    videoTracks =
                        TrackOptions.createVideoTracks(activity!!, mPlayer?.qualityLevels!!)
                }
                if (!videoTracks.isNullOrEmpty())
                    settingList.add(resources.getString(R.string.ep_settings_video))
                viewBinding.seriesDetailAllEpisodeTxtColors.setSettingAdapter(settingList)
            } catch (e: java.lang.Exception) {

            }

        }

        override fun onVideoQualitySelected(trackName: Int, selectedTrack: String) {
            super.onVideoQualitySelected(trackName, selectedTrack)

            if (mPlayer != null)
                mPlayer?.currentQuality = trackName
            selectedVideoTrack = selectedTrack
        }

        override fun onSettingSelected(selectedSetting: String) {
            super.onSettingSelected(selectedSetting)
            when (selectedSetting) {
                resources.getString(R.string.ep_settings_audio) -> {
                    viewBinding.seriesDetailAllEpisodeTxtColors.setAudioAdapter(
                        audioList,
                        selectedVideoTrack
                    )
                }

                resources.getString(R.string.ep_settings_subtitle) -> {
                    viewBinding.seriesDetailAllEpisodeTxtColors.setCaptionAdapter(
                        captionList,
                        selectedVideoTrack
                    )
                }

                resources.getString(R.string.ep_settings_video) -> {
                    if (mPlayer?.qualityLevels != null) {
                        viewBinding.seriesDetailAllEpisodeTxtColors.setVideoAdapter(
                            videoTracks,
                            selectedVideoTrack
                        )
                    }
                }
            }

        }

        override fun onCaptionSelected(trackIndex: Int) {
            super.onCaptionSelected(trackIndex)
            mPlayer?.currentCaptions = trackIndex
        }

        override fun onAudioSelected(trackIndex: Int) {
            super.onAudioSelected(trackIndex)
            mPlayer?.currentAudioTrack = trackIndex
        }

        override fun onSkipIntroClicked() {
            super.onSkipIntroClicked()
            if (!skipIntroEndTime.isNullOrEmpty())
                seekPlayerTo(skipIntroEndTime.toDouble())
        }

        override fun onRewindClick() {
            super.onRewindClick()
            mPlayer.let {
                val back10Ms = it?.position?.minus(10)
                val backMs = if (back10Ms!! > 0) {
                    back10Ms
                } else {
                    0
                }
                seekPlayerTo(backMs.toDouble())
            }
        }

        override fun onFastForwardClick() {
            super.onFastForwardClick()
            mPlayer.let {
                val fwd10Ms = it?.position?.plus(10)
                val fwdMs = if (fwd10Ms!! > 0) {
                    fwd10Ms
                } else {
                    0
                }
                seekPlayerTo(fwdMs.toDouble())
            }
        }

        override fun onProgressDragStop(position: Long) {
            super.onProgressDragStop(position)
            seekPlayerTo(position.toDouble())
        }

        override fun onOutsideClicked(root: View) {
            super.onOutsideClicked(root)
            isLive?.let {
                viewBinding.seriesDetailAllEpisodeTxtColors.toggleControlVisibility(
                    123,
                    it
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = KsPreferenceKeys.getInstance().appPrefUserId
        val bundle = arguments
        if (bundle != null) {
            playbackUrl = bundle.getString("playBackUrl")
            externalRefId = bundle.getString("externalRefId")
            isBingeWatchEnable = bundle.getBoolean("binge_watch")
            episodeId = bundle.getInt("episodeId")
            isTrailer = bundle.getBoolean("isTrailer")
            isLive = bundle.getBoolean("isLive")
            tittle = bundle.getString("tittle")
            posterUrl = bundle.getString("posterUrl")
            contentType = bundle.getString("contentType")
            screenName = bundle.getString("screenName")
            seriesTittle = bundle.getString("seriesTittle")
            genre = bundle.getString("tag")
            contentId = episodeId.toString()


            bundle.getString("skipIntroStartTime")?.let {
                skipIntroStartTime = it
            }
            bundle.getString("skipIntroEndTime")?.let {
                skipIntroEndTime = it
            }
            if (screenName == null) {
                screenName = AppConstants.SCREEN_NAME
            }
            try {
                if (bundle.getSerializable("episodeList") as ArrayList<EnveuVideoItemBean> != null) {
                    seasonEpisodesList =
                        bundle.getSerializable("episodeList") as ArrayList<EnveuVideoItemBean>
                }
            } catch (e: Exception) {
                Logger.w(e)
            }

            if (null != seasonEpisodesList) {
                currentPlayingIndex = getCurrentPlayingIndex(seasonEpisodesList!!)
            }
        }
        preference = KsPreferenceKeys.getInstance()
        if (preference?.appPrefLoginStatus
                .equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)
        ) {
            isLoggedIn = true
        }
        isUserVerified = preference?.isVerified.toString()
        token = preference?.appPrefAccessToken
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        callBookMarkApi()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        controlClickListener?.let { viewBinding.seriesDetailAllEpisodeTxtColors.attachListener(it) }
        viewBinding.seriesDetailAllEpisodeTxtColors.attachListener(playerControlClickListener)
        enveuPlayerControlView = viewBinding.seriesDetailAllEpisodeTxtColors
        mActivity = activity
        mListener = mActivity as OnPlayerInteractionListener
        UIInitialization()
        setUpCast()
        initializaPlayer()
        mPlayer?.isCaptionsRendering = true
    }

    fun initializePlugin(context: Activity,userId:String?,tittle:String?,isLive:Boolean?,seriesTittle:String?,contentId:String?,contentType:String?,genre:String?) {
        plugin =null
        val options = Options()
        options.accountCode = AppConstants.YOUBORA_ACCOUNT_CODE

        options.username = userId
        options.contentTitle = tittle
        //  options.contentDuration = contentDurationInLong
        options.contentIsLive = isLive
        options.contentTvShow = seriesTittle
        options.contentSeason = seriesTittle
        options.contentEpisodeTitle = tittle
        options.contentChannel = tittle
        options.contentId = contentId
        options.contentType = contentType
        options.contentGenre= genre
        options.appName = "Yojma TV"
        options.appReleaseVersion = BuildConfig.VERSION_NAME
        options.contentCustomDimension8= "Yojma TV"

        if (isLive == true) {
            options.contentChannel = tittle
        } else {
            options.contentChannel = ""
        }

        plugin = Plugin(options, context as Context)
        plugin?.activity = context // Activity where the player is
        plugin?.fireInit()
        android.util.Log.w("FirebasePerformance","InitCalled")
        /*  plugin?.adapter = JWPlayerAdapter(mPlayer)
          plugin?.adsAdapter = JWPlayerAdsAdapter(mPlayer)*/

    }
    private fun setJwPlayerAdapter(mPlayer: JWPlayer?){
        plugin?.adapter = JWPlayerAdapter(mPlayer)
        plugin?.adsAdapter = JWPlayerAdsAdapter(mPlayer)
    }
    private fun stopPlugin(){
        plugin?.fireStop()
        plugin?.removeAdapter()
    }


    private fun callBookMarkApi() {
        if (isTrailer == true && isLive == true) {
            return
        }
        activity?.let {
            episodeId?.let { it1 ->
                viewModel!!.getBookMarkByVideoId(token, it1).observe(
                    it
                ) { getBookmarkResponse: GetBookmarkResponse? ->
                    if (getBookmarkResponse != null && getBookmarkResponse.bookmarks != null) {
                        bookmarkPosition = getBookmarkResponse.bookmarks[0].position.toDouble()
                        contentPlayed = bookmarkPosition.toString()
                    }
                }
            }
        }
    }


    private fun getCurrentPlayingIndex(seasonEpisodesList: List<EnveuVideoItemBean>): Int? {
        var currnetPlayingIndex: Int? = 0
        val total = seasonEpisodesList?.size
        for (i in 0 until total!!) {
            val id = seasonEpisodesList!![i].id
            if (id == episodeId) {
                currnetPlayingIndex = i
                break
            }
        }
        return currnetPlayingIndex
    }

    private fun UIInitialization() {
        viewBinding.seriesDetailAllEpisodeTxtColors.initializeRecycler()
    }

    private fun initializaPlayer() {
        isInitialised = true
        if (isCasting()) {
            isChromeCastConnected(true)
            return
        }
        isBookMarkUpdated = false
        viewBinding.seriesDetailAllEpisodeTxtColors.updateBufferingState(true)
        if (BuildConfig.FLAVOR.equals("qa", ignoreCase = true)) {
            LicenseUtil().setLicenseKey(
                requireContext(),
                AppConstants.QA_LICENSE_KEY
            )
        } else {
            LicenseUtil().setLicenseKey(
                requireContext(),
                AppConstants.PROD_LICENSE_KEY
            )
        }
        KsPreferenceKeys.getInstance().caption = ""
        KsPreferenceKeys.getInstance().audioName = ""


        val hideJwControlUiConfig = UiConfig.Builder()
            .hide(UiGroup.CONTROLBAR)
            .build()
        val playerView: JWPlayerView = viewBinding.jwplayer
        playerView.player.also { mPlayer = it }
        // mPlayer.controls = false
        val playlistItem = PlaylistItem.Builder()
            .file(playbackUrl)
            .build()
        val playlist: MutableList<PlaylistItem> = ArrayList()
        playlist.add(playlistItem)
        if (!SDKConfig.getInstance().jwPlayerDiliveryBaseUrl.isNullOrEmpty()) {
            val config = PlayerConfig.Builder()
                .playlistUrl("${SDKConfig.getInstance().jwPlayerDiliveryBaseUrl}$externalRefId")
                .uiConfig(hideJwControlUiConfig)
                .build()
            mPlayer?.setup(config)
            setJwPlayerAdapter(mPlayer)
            startBookmarking()
            tittle?.let { viewBinding.seriesDetailAllEpisodeTxtColors.setTittle(it) }
            viewBinding.seriesDetailAllEpisodeTxtColors.addHideHandler()

            addListener()
            if (isBingeWatchEnable == true && currentPlayingIndex!! < seasonEpisodesList?.size!! - 1) {
                viewBinding.seriesDetailAllEpisodeTxtColors.shouldShowNext(true)
            } else {
                viewBinding.seriesDetailAllEpisodeTxtColors.shouldShowNext(false)
            }
        }
        callPlayDetails()
    }

    private fun callPlayDetails() {
        activity?.let {
            viewModel!!.getPlayDetail("${SDKConfig.getInstance().jwPlayerDiliveryBaseUrl}$externalRefId")
                .observe(
                    it
                ) { playDetailResponse: PlayDetailResponse? ->
                    if (!playDetailResponse?.playlist?.get(0)?.tracks.isNullOrEmpty()) {
                        subtitleTracks = playDetailResponse?.playlist?.get(0)?.tracks
                    }
                }
        }

    }

    private fun addListener() {
        mPlayer?.addListener(EventType.PLAY, this)
        mPlayer?.addListener(EventType.IDLE, this)
        mPlayer?.addListener(EventType.READY, this)
        mPlayer?.addListener(EventType.META, this)
        mPlayer?.addListener(EventType.READY, this)
        mPlayer?.addListener(EventType.BUFFER, this)
        mPlayer?.addListener(EventType.CAPTIONS_LIST, this)
        mPlayer?.addListener(EventType.AUDIO_TRACKS, this)
        mPlayer?.addListener(EventType.TIME, this)
        mPlayer?.addListener(EventType.COMPLETE, this)
        mPlayer?.play()
    }

    //CallBacks From Player
    override fun onPlay(p0: PlayEvent?) {
        viewBinding.seriesDetailAllEpisodeTxtColors.updateBufferingState(false)
        videoTracks =
            mPlayer?.qualityLevels?.let { TrackOptions.createVideoTracks(requireContext(), it) }

    }

    override fun onBuffer(p0: BufferEvent?) {
        if (!isChromcastConnected) {
            viewBinding.seriesDetailAllEpisodeTxtColors.updateBufferingState(true)
        }
    }

    override fun onIdle(p0: IdleEvent?) {

    }

    override fun onReady(p0: ReadyEvent?) {

    }

    override fun onMeta(p0: MetaEvent?) {

    }

    override fun onCaptionsList(captionsListEvent: CaptionsListEvent?) {
        if (captionsListEvent != null) {
            captionList = ArrayList()
            captionList?.addAll(captionsListEvent?.captions)
        }
    }

    override fun onAudioTracks(audioTrackEvent: AudioTracksEvent?) {
        if (audioTrackEvent != null) {
            audioList = ArrayList()
            audioList?.addAll(audioTrackEvent?.audioTracks)
        }
    }

    override fun onTime(p0: TimeEvent?) {
        if (!isTrailer!! && !isLive!!) {
            if (!isBookMarkUpdated!!) {
                isBookMarkUpdated = true
                val fwd10Ms = mPlayer?.position?.plus(bookmarkPosition)
                if (fwd10Ms != null) {
                    seekPlayerTo(fwd10Ms)
                }
            }
        }

        if (!skipIntroStartTime.isNullOrEmpty() && !skipIntroEndTime.isNullOrEmpty()) {
            if (mPlayer?.position!! >= skipIntroStartTime.toDouble() && mPlayer?.position!! <= skipIntroEndTime.toDouble()) {
                viewBinding.seriesDetailAllEpisodeTxtColors.toggleSkipIntroVisibility(View.VISIBLE)
            } else {
                viewBinding.seriesDetailAllEpisodeTxtColors.toggleSkipIntroVisibility(View.GONE)
            }

        }
        contentDuration = mPlayer?.duration.toString()
        mPlayer?.duration?.let { viewBinding.seriesDetailAllEpisodeTxtColors.updateDuration(it) }
        mPlayer?.position?.let { viewBinding.seriesDetailAllEpisodeTxtColors.updateProgress(it) }
    }

    private fun startBookmarking() {
        if (isTrailer == true && isLive == true) {
            return
        }
        handler = Handler(Looper.getMainLooper())
        runnable = object : Runnable {
            override fun run() {
                if (mPlayer != null) {
                    val totalDuration: Double = mPlayer!!.duration
                    val currentPosition: Double = mPlayer!!.position
                    val percentagePlayed = currentPosition / totalDuration * 100L
                    if (percentagePlayed > 1 && percentagePlayed <= 95) {
                        if (mListener != null) {
                            mListener = mActivity as OnPlayerInteractionListener
                            episodeId?.let { mListener!!.onBookmarkCall(mPlayer!!.position, it) }
                        }
                        Logger.d("PercentagePlayed $percentagePlayed")
                        Logger.d("PercentagePlayed", "Start")
                        handler?.postDelayed(this, 10000)
                    } else if (percentagePlayed > 95) {
                        if (mListener != null) {
                            mListener = mActivity as OnPlayerInteractionListener
                            episodeId?.let { mListener!!.onBookmarkFinish(it) }
                        }
                        Logger.d("PercentagePlayed $percentagePlayed")
                        Logger.d("PercentagePlayed", "Finish")
                    } else {
                        handler?.postDelayed(this, 10000)
                    }
                }
            }
        }
        handler!!.postDelayed(runnable as Runnable, 10000)
    }


    override fun onComplete(p0: CompleteEvent?) {
        onVideoComplete()
        handler!!.removeCallbacksAndMessages(runnable)
    }

    private fun onVideoComplete() {
        if (shouldShowBingeWatch(seasonEpisodesList?.size) == true && isBingeWatchEnable == true) {
            if (seasonEpisodesList != null && seasonEpisodesList!!.isNotEmpty()) {
                total = seasonEpisodesList!!.size
                for (i in 0 until total!!) {
                    val id = seasonEpisodesList!![i].id
                    if (total != null) {
                        if (id == episodeId && i < total!! - 1) {
                            Logger.d("id: $id")
                            val nextEpisodeItem = seasonEpisodesList?.get(i + 1)
                            Logger.d("seasonEpisodesList: $seasonEpisodesList")
                            this.tittle = nextEpisodeItem?.title
                            this.episodeId = nextEpisodeItem?.id

                            this.skipIntroStartTime = nextEpisodeItem?.skipintro_startTime ?: ""
                            this.skipIntroEndTime = nextEpisodeItem?.skipintro_endTime ?: ""
                            Logger.d("seasonEpisodesList: $skipIntroStartTime  $skipIntroEndTime")


                            //currentPlayingIndex = i + 1
                            mPlayer?.stop()
                            stopPlugin()
                            viewBinding.seriesDetailAllEpisodeTxtColors.hidePlayerControls()
                            // removeListener()
                            sku = nextEpisodeItem?.sku.toString()
                            if (nextEpisodeItem != null) {
                                playNextEpisode(
                                    nextEpisodeItem.isPremium,
                                    nextEpisodeItem.externalRefId
                                )
                            }
                            break
                        }
                    }
                }
            }
        } else {
            mPlayer?.stop()
            viewBinding.seriesDetailAllEpisodeTxtColors.hidePlayerControls()
            removeListener()
            activity?.finish()
            activity?.onBackPressed()
        }
    }

    private fun shouldShowBingeWatch(size: Int?): Boolean? {
        var shouldBingeWatchShow: Boolean? = false
        currentPlayingIndex = currentPlayingIndex!! + 1
        if (size != null) {
            shouldBingeWatchShow = currentPlayingIndex!! < size
        }

        if (!shouldBingeWatchShow!!) {
            isBingeWatchEnable = false
        }

        return shouldBingeWatchShow
    }

    private fun removeListener() {
        viewBinding.seriesDetailAllEpisodeTxtColors.removeListener()
        mPlayer?.removeListener(EventType.PLAY, this)
        mPlayer?.removeListener(EventType.IDLE, this)
        mPlayer?.removeListener(EventType.READY, this)
        mPlayer?.removeListener(EventType.META, this)
        mPlayer?.removeListener(EventType.READY, this)
        mPlayer?.removeListener(EventType.CAPTIONS_LIST, this)
        mPlayer?.removeListener(EventType.AUDIO_TRACKS, this)
        mPlayer?.removeListener(EventType.BUFFER, this)
        mPlayer?.removeListener(EventType.TIME, this)
    }

    private fun playNextEpisode(isPremimum: Boolean?, RefId: String) {
        if (isLoggedIn) {
            if (!isPremimum!!) {
                if (isUserVerified.equals("true", ignoreCase = true)) {
                    this.playbackUrl = SDKConfig.getInstance().playbacK_URL + RefId + ".m3u8"
                    // startPlayer(playback_url)
                    activity?.let { initializePlugin(it,userId,tittle,isLive,seriesTittle,contentId,contentType,genre) }

                    externalRefId = RefId
                    initializaPlayer()
                } else {
                    showDialog(
                        "",
                        resources.getString(R.string.user_verify_player),
                        resources.getString(R.string.ok)
                    )
                    //activity?.finish()
                    // activity?.onBackPressed()
                }
            } else {
                viewBinding.seriesDetailAllEpisodeTxtColors.updateBufferingState(true)
                viewModel?.hitApiEntitlement(token, sku)?.observe(this) {
                    if (it != null) {
                        viewBinding.seriesDetailAllEpisodeTxtColors.updateBufferingState(false)
                        if (it.data.entitled) {
                            if (isUserVerified.equals("true", ignoreCase = true)) {
                                this.playbackUrl =
                                    SDKConfig.getInstance().playbacK_URL + it.data.externalRefId + ".m3u8"
                                externalRefId = it.data.externalRefId
                                activity?.let { initializePlugin(it,userId,tittle,isLive,seriesTittle,contentId,contentType,genre)}
                                initializaPlayer()
                            } else {
                                showDialog(
                                    "",
                                    resources.getString(R.string.user_verify_player),
                                    resources.getString(R.string.ok)
                                )
                            }
                        } else {
                            showDialog(
                                "",
                                resources.getString(R.string.select_plan_player),
                                resources.getString(R.string.ok)
                            )
                        }
                    } else {
                        showDialog(
                            resources.getString(R.string.error),
                            resources.getString(R.string.something_went_wrong),
                            resources.getString(R.string.countinue)
                        )

                    }
                }
            }
        } else {
            ActivityLauncher.getInstance().loginActivity(activity, ActivityLogin::class.java)
        }
    }

    override fun onPause() {
        // mPlayer.pause()
        super.onPause()
        pause()
    }

    override fun onResume() {
        //  mPlayer.play()
        super.onResume()
        if (!isCasting() && (Util.SDK_INT <= 23 || mPlayer == null) && (isNetworkConnected || Network.isInternetAvailable(
                requireActivity()
            ))
        ) {
            if (isInitialised) {
                checkNetworkConnectivity(true)
                play()
            } else {
                initializaPlayer()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!isCasting() && Util.SDK_INT > 23 && (isNetworkConnected || Network.isInternetAvailable(
                requireActivity()
            ))
        ) {
            if (isInitialised) {
                checkNetworkConnectivity(true)
                play()
            } else {
                initializaPlayer()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.seriesDetailAllEpisodeTxtColors.removeListener()
        mPlayer?.removeListener(EventType.PLAY, this)
        mPlayer?.removeListener(EventType.IDLE, this)
        mPlayer?.removeListener(EventType.READY, this)
        mPlayer?.removeListener(EventType.META, this)
        mPlayer?.removeListener(EventType.READY, this)
        mPlayer?.removeListener(EventType.BUFFER, this)
        mPlayer?.removeListener(EventType.TIME, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    private fun showDialog(title: String, message: String, actionBtn: String) {
        val fm = parentFragmentManager
        val alertDialog = DialogPlayer.newInstance(title, message, actionBtn)
        alertDialog.isCancelable = false
        alertDialog.setAlertDialogCallBack(this)
        alertDialog.show(fm, "fragment_alert")
    }

    override fun onDialogFinish() {
        activity?.onBackPressed()
    }


    interface OnPlayerInteractionListener {
        fun onBookmarkCall(currentPosition: Double, episodeId: Int)
        fun onBookmarkFinish(episodeId: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (mActivity != null && mActivity !is OnPlayerInteractionListener) {
            try {
                throw java.lang.Exception("Activity ($mActivity) must implement OnPlayerInteractionListener")
            } catch (e: java.lang.Exception) {
                Logger.w(e)
            }
        }
    }

    override fun onDetach() {
        try {
            super.onDetach()
        } catch (ex: java.lang.Exception) {
            Logger.w(ex)
        }
        mListener = null
    }


}