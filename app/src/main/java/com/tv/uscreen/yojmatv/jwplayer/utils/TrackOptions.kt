package com.enveu.player.utils

import android.content.Context
import com.enveu.player.model.TrackItem
import com.jwplayer.pub.api.media.adaptive.QualityLevel
import com.tv.uscreen.yojmatv.R


val RANGE_AUTO = 100_001..Int.MAX_VALUE
val RANGE_4K = 1_000_000..Int.MAX_VALUE
val RANGE_HIGH = 600_001..1_000_000   // FULL HD
val RANGE_MEDIUM = 450_001..600_000 // HD
val RANGE_LOW = 100_001..450_000   //LOW SD

object TrackOptions {

//    fun getVideoBitrate(@EnveuPlayer.Companion.VideoQuality videoQuality: Int) : Pair<Int, Int> {
//        return when (videoQuality) {
//           // EnveuPlayer.QUALITY_4K -> Pair(RANGE_4K.first, RANGE_4K.last)
//            EnveuPlayer.QUALITY_AUTO -> Pair(RANGE_AUTO.first, RANGE_AUTO.last)
//            EnveuPlayer.QUALITY_HIGH -> Pair(RANGE_HIGH.first, RANGE_HIGH.last)
//            EnveuPlayer.QUALITY_MEDIUM -> Pair(RANGE_MEDIUM.first, RANGE_MEDIUM.last)
//            EnveuPlayer.QUALITY_LOW -> Pair(RANGE_LOW.first, RANGE_LOW.last)
//            EnveuPlayer.QUALITY_NA -> Pair(0, 0)
//            else -> Pair(0,0)
//        }
//    }

    fun createVideoTracks(context: Context, trackList: MutableList<QualityLevel>): ArrayList<TrackItem> {
        var track1: Boolean? = false
        var track2: Boolean? = false
        var track3: Boolean? = false
        var track4: Boolean? = false
        val arrayList = ArrayList<TrackItem>()
        val trackMap = HashMap<String, TrackItem>()
        for (i in 0 until trackList.size) {
            val format = trackList[i].bitrate
            if (i==0 && !track1!!){
                track1 = true
                val trackItem = TrackItem(
                    trackName = context.getString(R.string.ep_video_auto),
                    description = context.getString(R.string.ep_video_auto_desc),
                    position = 0,
                    id = trackList[i].trackIndex
                )
                trackMap[context.getString(R.string.ep_video_auto)] = trackItem
                arrayList.add(trackItem)
            } else {
                if (format in RANGE_LOW && !track2!!) {
                    track2 = true
                    val trackItem = TrackItem(
                        trackName = context.getString(R.string.low),
                        description = context.getString(R.string.ep_video_low_desc),
                        position = 1,
                        id = trackList[i].trackIndex,
                    )
                   // trackMap[context.getString(R.string.ep_video_low)] = trackItem
                    arrayList.add(trackItem)
                } else {
                    if (format in RANGE_MEDIUM && !track3!!) {
                        track3 = true
                        val trackItem = TrackItem(
                            trackName = context.getString(R.string.medium),
                            description = context.getString(R.string.ep_video_medium_desc),
                            position = 2,
                            id = trackList[i].trackIndex,
                        )
                        arrayList.add(trackItem)
                        //trackMap[context.getString(R.string.ep_video_medium)] = trackItem
                    } else {
                        if (format in RANGE_HIGH && !track4!!) {
                            track4 = true
                            val trackItem = TrackItem(
                                trackName = context.getString(R.string.high),
                                description = context.getString(R.string.ep_video_high_desc),
                                position = 3,
                                id = trackList[i].trackIndex,
                            )
                            arrayList.add(trackItem)
                            //trackMap[context.getString(R.string.ep_video_high)] = trackItem
                        }
                    }
                }
            }
        }
        return arrayList
    }

//    fun createSubtitleTracks(context: Context,trackList: MutableList<TrackItem>, trackGroup: TrackGroup) {
//        for (i in 0 until trackGroup.length) {
//            val format = trackGroup.getFormat(i)
//            Logger.d("subtitle format: ${Format.toLogString(format)}")
//            trackList.add(TrackItem(
//                trackName = format.label ?: format.language ?: "NA",
//                description = null,
//                position = i,
//                id = format.id,
//                trackType = context.resources.getString(R.string.type_subtitle),
//            ))
//            Logger.d("subtitle size: ${trackList.size}")
//        }
//        Logger.d("subtitle: $trackList")
//    }
//
//    fun createAudioTracks(context: Context,trackList: MutableList<TrackItem>, trackGroup: TrackGroup) {
//        for (i in 0 until trackGroup.length) {
//            val format = trackGroup.getFormat(i)
//            Logger.d("audio format: ${Format.toLogString(format)}")
//            trackList.add(
//                TrackItem(
//                    trackName = format.label ?: format.language ?: "NA",
//                    description = null,
//                    position = i,
//                    id = format.id,
//                    trackType = context.resources.getString(R.string.type_audio),
//                )
//            )
//            Logger.d("audio size: ${trackList.size}")
//        }
//        Logger.d("audio: $trackList")
//    }
}