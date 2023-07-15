package com.breadgangtvnetwork.utils.helpers.downloads.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.breadgangtvnetwork.enums.DownloadStatus

@Entity(tableName = "downloadedVideos", primaryKeys = ["videoId", "seasonNumber","parentalRating"])
data class DownloadedVideo(
        val videoId: String,
        val downloadType: String,
        val seriesId: String,
        var parentalRating: String,
        var seasonNumber: String = "",
        var episodeCount: String = "",
        var seriesName: String = "",
        var expiryDate: String = "") {

    var id: Long = 0
    @ColumnInfo(name = "created_at")
    var createdAt = System.currentTimeMillis()
    var downloadStatus = DownloadStatus.START
}