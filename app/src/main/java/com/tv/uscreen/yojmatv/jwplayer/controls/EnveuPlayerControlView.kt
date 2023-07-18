package com.enveu.player.controls

import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enveu.player.extension.percent
import com.enveu.player.extension.percentOf
import com.enveu.player.listener.ControlClickListener
import com.enveu.player.model.TrackItem
import com.enveu.player.utils.ClickHandler
import com.enveu.player.utils.TimeUtils
import com.example.jwplayer.VideoQualityAdapter.CustomAdapter
import com.google.android.gms.cast.framework.CastButtonFactory
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.databinding.EnveuPlayerControlViewBinding


import com.tv.uscreen.yojmatv.jwplayer.utils.Logger
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper

class EnveuPlayerControlView : FrameLayout {

    private var binding: EnveuPlayerControlViewBinding
    private var dragging: Boolean = false
    private var controlClickListener: ControlClickListener? = null
    private var showNext: Boolean = false
    private var showMore: Boolean = false
    private var showPrevious: Boolean = false
    private var controlTimeoutAt: Long = 10_000L
    private var videoDuration: Long = 0
    private var castInProgress: Boolean = false
    private var mediaType: Boolean? = false
    private var visibilityHandler = Handler(Looper.getMainLooper())

//    @EnveuPlayer.Companion.MediaType
//    private var mediaType: Int = EnveuPlayer.VOD
    private var visibilityRunnable = { updateControlVisibility(View.GONE) }

    private val itemClick = object : CustomAdapter.ItemClick{
        override fun itemClick(trackName: Int, selectedTrackName: String) {
            if (controlClickListener != null) {
                updateRecyclerViewVisibility()
                controlClickListener?.onVideoQualitySelected(trackName,selectedTrackName)
                addHideHandler()
            }
        }

    }

    private fun updateRecyclerViewVisibility() {
        if (binding.rvQuality.visibility == View.VISIBLE) {
            binding.rvQuality.visibility = View.GONE
        }else{
            binding.rvQuality.visibility = View.VISIBLE
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        binding = EnveuPlayerControlViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun attachListener(controlClickListener: ControlClickListener) {
        this.controlClickListener = controlClickListener

        binding.root.setOnClickListener{
            if (ClickHandler.allowClick()) {
                controlClickListener.onOutsideClicked(binding.root)
            }
        }

        binding.ivEpBack.setOnClickListener {
            controlClickListener.onBackClicked()
        }

        binding.ivEpSetting.setOnClickListener {
            controlClickListener.onSettingClicked()
        }
        binding.ivEpRewind.setOnClickListener {
            controlClickListener.onRewindClick()
        }
        binding.ivEpPlayPause.setOnClickListener {
            togglePlayPauseUi()
            controlClickListener.onPlayPauseClicked()
        }
        binding.ivEpFastFwd.setOnClickListener {
            controlClickListener.onFastForwardClick()
        }
        binding.ivEpFullscreen.setOnClickListener {
            controlClickListener.onFullscreenClicked()
        }
        binding.tvPreviousEpisode.setOnClickListener{
            controlClickListener.onPreviousClicked()
        }
        binding.tvMoreEpisodes.setOnClickListener {
            controlClickListener.onMoreClicked()
        }
        binding.tvNextEpisode.setOnClickListener {
            controlClickListener.onNextClicked()
        }

        binding.epTimeBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                binding.epTimeBar.progress = progress
                binding.tvEpPosition.text = TimeUtils.formatDuration(progress.toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                dragging = true
                controlClickListener.onProgressDragStart()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                dragging = false
                controlClickListener.onProgressDragStop(seekBar.progress.percentOf(videoDuration))
            }
        })
    }

    fun removeListener() {
        this.controlClickListener = null
    }

    fun updateBufferingState(buffering: Boolean) {
        binding.pbEpProgress.visibility = if (buffering) View.VISIBLE else View.GONE
    }

    fun updateDuration(duration: Double) {
        videoDuration = duration.toLong()
        binding.epTimeBar.max = 100
        binding.tvEpDuration.text = TimeUtils.formatDuration(duration.toLong())
    }

    fun updateProgress(progress: Double) {
        binding.epTimeBar.progress = progress.toLong().percent(videoDuration)
        binding.tvEpPosition.text = TimeUtils.formatDuration(progress.toLong())
    }
    fun updateSeekBar() {
        binding.epTimeBar.progress = 0
        binding.tvEpPosition.text = "00:00"
        binding.tvEpDuration.text = "00:00"
    }

    fun isDragging(): Boolean = dragging

    private fun togglePlayPauseUi() {
        val isPlaying = binding.ivEpPlayPause.tag
        if (isPlaying is Boolean) {
            val iconResId = if (isPlaying) {
                R.drawable.ic_ep_pause
            } else {
                R.drawable.ic_ep_play
            }
            binding.ivEpPlayPause.setImageResource(iconResId)
            binding.ivEpPlayPause.tag = !isPlaying
        }
    }

    fun updatePlayPauseIcon(isPlaying: Boolean) {
        binding.ivEpPlayPause.tag = isPlaying
        togglePlayPauseUi()
    }

    fun controlTimeout(timeoutMs: Long) {
        if (timeoutMs > 0) {
            this.controlTimeoutAt = timeoutMs
        }
    }

    fun addHideHandler() {
        visibilityHandler.postDelayed(visibilityRunnable, controlTimeoutAt)
    }

    fun toggleControlVisibility(orientation: Int,mediaType: Boolean) {
        visibilityHandler.removeCallbacksAndMessages(null)
        this.mediaType = mediaType
        val visibility = if (binding.seriesDetailAllEpisodeTxtColorsBackground.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
        val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE
        Logger.d("isLandscape: $isLandscape | orientation: $orientation")
        updateControlVisibility(visibility, isLandscape)
        addHideHandler()
    }

    private fun updateControlVisibility(visibility: Int, isLandscape: Boolean = false) {
        if (mediaType != true){
            controlsVisibilityForVod(visibility,isLandscape)
        }else{
            controlsVisibilityForLive(visibility,isLandscape)
        }
    }

    private fun controlsVisibilityForLive(visibility: Int, isLandscape: Boolean = false) {
        binding.seriesDetailAllEpisodeTxtColorsBackground.visibility = visibility
        binding.tvTittle.visibility = visibility
        binding.ivEpBack.visibility = visibility
        binding.ivEpSetting.visibility = View.INVISIBLE
        binding.ivEpRewind.visibility = View.GONE
        binding.ivEpPlayPause.visibility = View.GONE
        binding.ivEpFastFwd.visibility = View.GONE
        binding.tvEpPosition.visibility = View.GONE
        binding.epTimeBar.visibility = View.GONE
        binding.tvEpDuration.visibility = View.GONE
        binding.ivEpFullscreen.visibility = visibility
        binding.tvEpLiveTag.visibility = visibility

        if (visibility == View.VISIBLE) {
            binding.tvNextEpisode.visibility =
                if (isLandscape && showNext) View.VISIBLE else View.GONE
            binding.tvMoreEpisodes.visibility =
                if (isLandscape && showMore) View.VISIBLE else View.GONE
            binding.tvPreviousEpisode.visibility =
                if (isLandscape && showPrevious) View.VISIBLE else View.GONE
        } else {
            binding.tvNextEpisode.visibility = View.GONE
            binding.tvMoreEpisodes.visibility = View.GONE
            binding.tvPreviousEpisode.visibility = View.GONE
        }
    }

    private fun controlsVisibilityForVod(visibility: Int, isLandscape: Boolean = false) {
        binding.seriesDetailAllEpisodeTxtColorsBackground.visibility = visibility
        binding.ivEpBack.visibility = visibility
        binding.tvTittle.visibility = visibility
        binding.ivEpSetting.visibility = visibility
        binding.ivEpRewind.visibility = visibility
        binding.ivEpPlayPause.visibility = visibility
        binding.ivEpFastFwd.visibility = visibility
        binding.tvEpPosition.visibility = visibility
        binding.epTimeBar.visibility = visibility
        binding.tvEpDuration.visibility = visibility
        binding.ivEpFullscreen.visibility = visibility
        binding.mrb.visibility = visibility
        binding.tvEpLiveTag.visibility = View.GONE
        binding.rvQuality.visibility = View.GONE

        if (visibility == View.VISIBLE) {
            binding.tvNextEpisode.visibility =
                if (showNext) View.VISIBLE else View.GONE
            binding.tvMoreEpisodes.visibility =
                if (isLandscape && showMore) View.VISIBLE else View.GONE
            binding.tvPreviousEpisode.visibility =
                if (isLandscape && showPrevious) View.VISIBLE else View.GONE
        } else {
            binding.tvNextEpisode.visibility = View.GONE
            binding.tvMoreEpisodes.visibility = View.GONE
            binding.tvPreviousEpisode.visibility = View.GONE
        }
    }

    fun shouldShowNext(show: Boolean) {
        this.showNext = show
    }

    fun shouldShowMore(show: Boolean) {
        this.showMore = show
    }

    fun shouldShowPrevious(show: Boolean) {
        this.showPrevious = show
    }

    fun onCastInProgress(deviceName: String) {
        castInProgress = true
        binding.tvInternetConnection.text =
            binding.tvInternetConnection.context.getString(R.string.ep_cast_message, deviceName)
        binding.tvInternetConnection.visibility = View.VISIBLE
        hidePlayerControls()
        visibilityHandler.removeCallbacksAndMessages(null)
    }

    fun onCastDisconnected() {
        castInProgress = false
        binding.tvInternetConnection.text = ""
        binding.tvInternetConnection.visibility = View.INVISIBLE
        binding.mrb.visibility = View.INVISIBLE
        addHideHandler()
    }

    fun updateNetworkState(isConnected: Boolean) {
        if (isConnected) {
            binding.tvInternetConnection.visibility = View.INVISIBLE
        } else {
            binding.tvInternetConnection.setText(R.string.no_internet_connection)
            binding.tvInternetConnection.visibility = View.VISIBLE
        }
    }

    fun hidePlayerControls() {
        visibilityHandler.removeCallbacksAndMessages(null)
        binding.seriesDetailAllEpisodeTxtColorsBackground.visibility = View.GONE
        binding.tvTittle.visibility = View.GONE
        binding.ivEpBack.visibility = View.VISIBLE
        binding.ivEpSetting.visibility = View.GONE
        binding.mrb.visibility = View.GONE
        binding.ivEpRewind.visibility = View.GONE
        binding.ivEpPlayPause.visibility = View.GONE
        binding.ivEpFastFwd.visibility = View.GONE
        binding.tvEpPosition.visibility = View.GONE
        binding.epTimeBar.visibility = View.GONE
        binding.tvEpDuration.visibility = View.GONE
        binding.ivEpFullscreen.visibility = View.GONE
        binding.tvNextEpisode.visibility = View.GONE
        binding.tvInternetConnection.visibility = View.VISIBLE
    }

    fun initializeRecycler() {
        binding.rvQuality.hasFixedSize()
        binding.rvQuality.setNestedScrollingEnabled(false)
        binding.rvQuality.setLayoutManager(
            LinearLayoutManager(
                binding.rvQuality.context,
                RecyclerView.VERTICAL,
                false
            )
        )
    }

    fun setVideoQualityAdapter(videoTracks: ArrayList<TrackItem>?, selectedVideoTrack: String) {
        if (videoTracks!=null) {
            updateRecyclerViewVisibility()
            visibilityHandler.removeCallbacksAndMessages(null)
            val adapter = CustomAdapter(videoTracks,itemClick,selectedVideoTrack)
            binding.rvQuality.adapter = adapter
        }
    }

    fun initCast() {
        CastButtonFactory.setUpMediaRouteButton(
            this@EnveuPlayerControlView.context.applicationContext,
            binding.mrb
        )
    }

    fun chromeCastStatus(connected: Boolean, posterUrl: String) {
        if (connected) {
            binding.playerImage.visibility = View.VISIBLE
            ImageHelper.getInstance(context).loadListImage(
                binding.playerImage,
                AppCommonMethod.getListLDSImage(posterUrl, context)
            )
        }else{
            binding.playerImage.visibility = View.GONE
        }
    }

    fun setTittle(tittle: String) {
        binding.tvTittle.text = tittle
    }

}