package com.enveu.player.utils

import android.os.SystemClock

object ClickHandler {
    private var mLastClickTime: Long = 0
    fun allowClick(diff: Int = 400): Boolean {
        val currentTime = SystemClock.elapsedRealtime()
        val allow = currentTime - mLastClickTime > diff
        if (allow) {
            mLastClickTime = currentTime
        }
        return allow
    }

    fun disallowClick(diff: Int = 400) = !allowClick(diff)
}