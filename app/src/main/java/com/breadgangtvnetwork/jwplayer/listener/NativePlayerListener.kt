package com.enveu.player.exo.listener

import com.enveu.player.base.EnveuPlayerListener
import com.google.android.exoplayer2.DeviceInfo
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.metadata.Metadata
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters
import com.google.android.exoplayer2.video.VideoSize

abstract class NativePlayerListener : EnveuPlayerListener() {
    open fun onPlaybackStateChanged(playbackState: Int) {}
    open fun onIsPlayingChanged(isPlaying: Boolean) {}
    open fun onEvents(player: Player, events: Player.Events) {}
    open fun onTimelineChanged(timeline: Timeline, reason: Int) {}
    open fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {}

    //    open fun onTracksChanged(tracks: Tracks) {}
    open fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {}
    open fun onPlaylistMetadataChanged(mediaMetadata: MediaMetadata) {}
    open fun onIsLoadingChanged(isLoading: Boolean) {}
    open fun onAvailableCommandsChanged(availableCommands: Player.Commands) {}
    open fun onTrackSelectionParametersChanged(parameters: TrackSelectionParameters) {}
    open fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) {}
    open fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) {}
    open fun onRepeatModeChanged(repeatMode: Int) {}
    open fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
    open fun onPlayerError(error: PlaybackException) {}
    open fun onPlayerErrorChanged(error: PlaybackException?) {}
    open fun onPositionDiscontinuity(
        oldPosition: Player.PositionInfo,
        newPosition: Player.PositionInfo,
        reason: Int
    ) {
    }

    open fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
    open fun onSeekBackIncrementChanged(seekBackIncrementMs: Long) {}
    open fun onSeekForwardIncrementChanged(seekForwardIncrementMs: Long) {}
    open fun onMaxSeekToPreviousPositionChanged(maxSeekToPreviousPositionMs: Long) {}
    open fun onAudioSessionIdChanged(audioSessionId: Int) {}
    open fun onAudioAttributesChanged(audioAttributes: AudioAttributes) {}
    open fun onVolumeChanged(volume: Float) {}
    open fun onSkipSilenceEnabledChanged(skipSilenceEnabled: Boolean) {}
    open fun onDeviceInfoChanged(deviceInfo: DeviceInfo) {}
    open fun onDeviceVolumeChanged(volume: Int, muted: Boolean) {}
    open fun onVideoSizeChanged(videoSize: VideoSize) {}
    open fun onSurfaceSizeChanged(width: Int, height: Int) {}
    open fun onRenderedFirstFrame() {}

    //    open fun onCues(cueGroup: CueGroup) {}
    open fun onMetadata(metadata: Metadata) {}
}