package com.enveu.player.utils

import com.breadgangtvnetwork.jwplayer.utils.Logger
import java.util.Formatter
import java.util.Locale

object TimeUtils {
    fun formatDuration(duration: Long): String {
        if (duration < 0L) {
            return "00:00"
        }
        val formatBuilder = StringBuilder()
        val formatter = Formatter(formatBuilder, Locale.getDefault())

        val totalSeconds: Long = duration
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        formatBuilder.setLength(0)
        Logger.d("ProgressValueIs", seconds.toString())

        return if (hours > 0) formatter.format("%d:%02d:%02d", hours, minutes, seconds)
            .toString() else formatter.format("%02d:%02d", minutes, seconds).toString()
    }
}
