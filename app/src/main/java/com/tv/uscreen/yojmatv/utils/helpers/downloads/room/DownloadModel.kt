package com.tv.uscreen.yojmatv.utils.helpers.downloads.room

class DownloadModel {
    var downloadVideos = ArrayList<DownloadedVideo>()
    var episodeMap = HashMap<String, ArrayList<DownloadedEpisodes>>()
    var status : Boolean = false
}