package com.enveu.player.base

import com.breadgangtvnetwork.jwplayer.utils.Logger
import com.enveu.player.model.Progress


abstract class EnveuPlayerListener {
    open fun onProgress(progress: Progress) {
//        Logger.d("progress: $progress")
    }

    open fun onSmartDownload() {
        Logger.d("smart download")
    }

    open fun onBookmark(progress: Progress) {
        Logger.d("bookmark $progress")
    }

    open fun onBookmarkFinish(progress: Progress) {
        Logger.d("bookmark finish $progress")
    }

    open fun onFullscreenClick() {
        Logger.d("on full screen click")
    }

    open fun onPlay() {
        Logger.d("play")
    }

    open fun onPause() {
        Logger.d("pause")
    }

    open fun onRestart() {
        Logger.d("restart")
    }

    open fun onNextClick() {
        Logger.d("next clicked")
    }

    open fun onMoreClick() {
        Logger.d("more clicked")
    }

    open fun onPreviousClick() {
        Logger.d("previous clicked")
    }

    open fun onBackClick() {
        Logger.d("back button clicked")
    }
}