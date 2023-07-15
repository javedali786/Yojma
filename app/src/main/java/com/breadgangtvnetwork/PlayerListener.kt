package com.example

import com.jwplayer.pub.api.events.listeners.VideoPlayerEvents

interface PlayerListener : VideoPlayerEvents.OnPlayListener,
    VideoPlayerEvents.OnBufferListener,
    VideoPlayerEvents.OnIdleListener,
    VideoPlayerEvents.OnReadyListener,
    VideoPlayerEvents.OnMetaListener,
    VideoPlayerEvents.OnTimeListener,
    VideoPlayerEvents.OnCompleteListener{
}