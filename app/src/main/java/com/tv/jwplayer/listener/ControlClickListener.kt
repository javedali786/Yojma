package com.enveu.player.listener

import android.view.View

abstract class ControlClickListener {
    open fun onOutsideClicked(root: View) {}
    open fun onPlayPauseClicked() {}
    open fun onFullscreenClicked() {}
    open fun onBackClicked() {}
    open fun onSettingClicked() {}
    open fun onRewindClick() {}
    open fun onFastForwardClick() {}
    open fun onProgressDragStart() {}
    open fun onProgressDragStop(position: Long) {}
    open fun onPreviousClicked() {}
    open fun onMoreClicked() {}
    open fun onNextClicked() {}
    open fun onVideoQualitySelected(trackName: Int, selectedTrack: String) {}
}