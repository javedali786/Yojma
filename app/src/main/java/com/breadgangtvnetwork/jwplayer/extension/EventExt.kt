package com.enveu.player.exo.extension

import com.google.android.exoplayer2.Player


fun Player.Events.getEventText(): String {
    val builder = StringBuilder()
    for (i in 0 until this.size()) {
        builder.append(getText(this[i]))
            .append(" ")
    }
    return builder.trim().replace(Regex(" "), ", ")
}

private fun getText(event: Int): String {
   // return when (event) {
//        Player.EVENT_TIMELINE_CHANGED ->
//            "Player.EVENT_TIMELINE_CHANGED"
//        Player.EVENT_MEDIA_ITEM_TRANSITION ->
//            "Player.EVENT_MEDIA_ITEM_TRANSITION"
//        Player.EVENT_TRACKS_CHANGED ->
//            "Player.EVENT_TRACKS_CHANGED"
//        Player.EVENT_IS_LOADING_CHANGED ->
//            "Player.EVENT_IS_LOADING_CHANGED"
//        Player.EVENT_PLAYBACK_STATE_CHANGED ->
//            "Player.EVENT_PLAYBACK_STATE_CHANGED"
//        Player.EVENT_PLAY_WHEN_READY_CHANGED ->
//            "Player.EVENT_PLAY_WHEN_READY_CHANGED"
//        Player.EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED ->
//            "Player.EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED"
//        Player.EVENT_IS_PLAYING_CHANGED ->
//            "Player.EVENT_IS_PLAYING_CHANGED"
//        Player.EVENT_REPEAT_MODE_CHANGED ->
//            "Player.EVENT_REPEAT_MODE_CHANGED"
//        Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED ->
//            "Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED"
//        Player.EVENT_PLAYER_ERROR ->
//            "Player.EVENT_PLAYER_ERROR"
//        Player.EVENT_POSITION_DISCONTINUITY ->
//            "Player.EVENT_POSITION_DISCONTINUITY"
//        Player.EVENT_PLAYBACK_PARAMETERS_CHANGED ->
//            "Player.EVENT_PLAYBACK_PARAMETERS_CHANGED"
//        Player.EVENT_AVAILABLE_COMMANDS_CHANGED ->
//            "Player.EVENT_AVAILABLE_COMMANDS_CHANGED"
//        Player.EVENT_MEDIA_METADATA_CHANGED ->
//            "Player.EVENT_MEDIA_METADATA_CHANGED"
//        Player.EVENT_PLAYLIST_METADATA_CHANGED ->
//            "Player.EVENT_PLAYLIST_METADATA_CHANGED"
//        Player.EVENT_SEEK_BACK_INCREMENT_CHANGED ->
//            "Player.EVENT_SEEK_BACK_INCREMENT_CHANGED"
//        Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED ->
//            "Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED"
//        Player.EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED ->
//            "Player.EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED"
//        Player.EVENT_TRACK_SELECTION_PARAMETERS_CHANGED ->
//            "Player.EVENT_TRACK_SELECTION_PARAMETERS_CHANGED"
//        Player.EVENT_AUDIO_ATTRIBUTES_CHANGED ->
//            "Player.EVENT_AUDIO_ATTRIBUTES_CHANGED"
//        Player.EVENT_AUDIO_SESSION_ID ->
//            "Player.EVENT_AUDIO_SESSION_ID"
//        Player.EVENT_VOLUME_CHANGED ->
//            "Player.EVENT_VOLUME_CHANGED"
//        Player.EVENT_SKIP_SILENCE_ENABLED_CHANGED ->
//            "Player.EVENT_SKIP_SILENCE_ENABLED_CHANGED"
//        Player.EVENT_SURFACE_SIZE_CHANGED ->
//            "Player.EVENT_SURFACE_SIZE_CHANGED"
//        Player.EVENT_VIDEO_SIZE_CHANGED ->
//            "Player.EVENT_VIDEO_SIZE_CHANGED"
//        Player.EVENT_RENDERED_FIRST_FRAME ->
//            "Player.EVENT_RENDERED_FIRST_FRAME"
//        Player.EVENT_CUES ->
//            "Player.EVENT_CUES"
//        Player.EVENT_METADATA ->
//            "Player.EVENT_METADATA"
//        Player.EVENT_DEVICE_INFO_CHANGED ->
//            "Player.EVENT_DEVICE_INFO_CHANGED"
//        Player.EVENT_DEVICE_VOLUME_CHANGED ->
//            "Player.EVENT_DEVICE_VOLUME_CHANGED"
//        else -> "Unknown event $event"
   // }
    return null.toString()
}