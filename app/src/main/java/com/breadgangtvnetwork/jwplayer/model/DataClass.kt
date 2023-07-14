package com.enveu.player.model


data class Progress(var duration: Long = 0, var position: Double = 0.0) {
    fun getPercent(): Int {
        return if (duration == 0L) 0
        else ((position * 100.0) / duration).toInt()
    }
}

data class TrackItem(
    var trackName: String,
    var description: String? = null,
    var position: Int,
    var id: Int?
)