package com.enveu.player.base

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.enveu.player.controls.EnveuPlayerControlView
import com.enveu.player.receiver.ConnectivityReceiver
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.gson.Gson
import com.tv.uscreen.yojmatv.utils.Logger

abstract class BasePlayerFragment : Fragment() {
    protected var currentPosition: Long = 0L
    var isUpdatedChromecastMedia = false
    var videoMetadata: VideoMetadata? = null
    abstract fun play()
    abstract fun pause()
    abstract fun checkNetworkConnectivity(isConnected: Boolean)
    abstract fun seekPlayerTo(position: Double)
    abstract fun seekAfterDisconnected()
    abstract fun isChromeCastConnected(isConnected: Boolean)
  //  private var mCastSession: CastSession? = null
    private val mSessionManagerListener: SessionManagerListener<CastSession> =
        SessionManagerListenerImpl()


    //
    private var remoteMediaClient: RemoteMediaClient? = null
    private var mCastContext: CastContext? = null
    private var mCastSession: CastSession? = null
    protected lateinit var enveuPlayerControlView: EnveuPlayerControlView
    private var listener: ConnectivityReceiver.ConnectivityReceiverListener? =
        object : ConnectivityReceiver.ConnectivityReceiverListener {
            override fun onNetworkConnectionChanged(isConnected: Boolean) {
                enveuPlayerControlView.updateNetworkState(isConnected)
                checkNetworkConnectivity(isConnected)
            }

        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.registerReceiver(
            ConnectivityReceiver(),
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    protected fun setUpCast() {
        enveuPlayerControlView.initCast()
        mCastContext = CastContext.getSharedInstance()
       // mCastSession = mCastContext?.sessionManager?.currentCastSession
    }
    fun isCasting(): Boolean = mCastSession?.castDevice != null

    fun isCasting(videoUrl: String) : Boolean  {
        Logger.d("video url: $videoUrl")
        Logger.d("cast content id: ${mCastSession?.remoteMediaClient?.currentItem?.media?.contentId}")
        return mCastSession?.remoteMediaClient?.currentItem?.media?.contentId == videoUrl
    }

    //
    private inner class SessionManagerListenerImpl : SessionManagerListener<CastSession> {
        override fun onSessionStarted(session: CastSession, sessionId: String) {
            Logger.d("$session | $sessionId")
            onConnected(session)
        }

        override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
            Logger.d("$session | $wasSuspended")
            onConnected(session)
        }

        override fun onSessionEnded(session: CastSession, error: Int) {
            Logger.d("$session | $error")
            onDisconnected()
        }

        override fun onSessionResumeFailed(session: CastSession, error: Int) {
            Logger.d("$session | $error")
            onDisconnected()
        }

        override fun onSessionStartFailed(session: CastSession, error: Int) {
            Logger.d("$session | $error")
            onDisconnected()
        }

        override fun onSessionStarting(session: CastSession) {
            Logger.d("$session")
        }

        override fun onSessionResuming(session: CastSession, sessionId: String) {
            Logger.d("$session | $sessionId")
        }

        override fun onSessionEnding(session: CastSession) {
            Logger.d("$session")
        }

        override fun onSessionSuspended(session: CastSession, reason: Int) {
            Logger.d("$session | $reason")
        }

        private fun onConnected(session: CastSession) {
            mCastSession = session
            loadRemoteMedia(currentPosition, true)
            val deviceName = session.castDevice?.friendlyName ?: ""
            enveuPlayerControlView.onCastInProgress(deviceName)
            isChromeCastConnected(true)
            pause()
            enveuPlayerControlView.updateBufferingState(false)
        }

        private fun onDisconnected() {
            remoteMediaClient?.removeProgressListener(mediaProgressListener)
            enveuPlayerControlView.onCastDisconnected()
            enveuPlayerControlView
            seekAfterDisconnected()
            isChromeCastConnected(false)
            play()
        }
    }

    protected fun loadRemoteMedia(position: Long, autoPlay: Boolean) {
        isUpdatedChromecastMedia = false
        val castSession = mCastSession ?: return
        remoteMediaClient = (castSession.remoteMediaClient ?: return)

        remoteMediaClient?.addProgressListener(mediaProgressListener, 100)

        remoteMediaClient?.registerCallback(object : RemoteMediaClient.Callback() {
            override fun onStatusUpdated() {
                val chromecastPlayingData = remoteMediaClient?.mediaStatus?.mediaInfo?.mediaTracks;
                val chromeCastDataGson = Gson().toJson(chromecastPlayingData)
                if (!isUpdatedChromecastMedia) {
                    if (chromecastPlayingData != null) {
                        isUpdatedChromecastMedia = true;
                        Log.d(tag, "onStatusUpdated: $chromeCastDataGson")
//                        for (i in chromecastPlayingData) {
//                            if (i.type == MediaTrack.TYPE_AUDIO) {
//                                val chromeCastTrackName = i.name
//                                if (chromeCastTrackName != null && chromeCastTrackName != "") {
//                                    if (chromeCastTrackName.contains(applicationDefaultLanguage!!)){
//                                        audioID = i.id
//                                    }
//                                }
//                                setActiveTrackForChromeCast(audioID)
//                            } else if (i.type == MediaTrack.TYPE_TEXT) {
//                                val chromecastCaptionName = i.name
//                                if (captionTrackName != null) {
//                                    if (chromecastCaptionName != null) {
//                                        if (chromecastCaptionName.contains(captionTrackName!!)){
//                                            captionID = i.id
//                                        }
//                                    }
//                                }
//                                setActiveTrackForChromeCast(captionID)
//                            }
//                        }
                    }
                }

//                val intent = Intent(activity, ExpandedControlsActivity::class.java)
//                startActivity(intent)
                if (isUpdatedChromecastMedia) {
                    remoteMediaClient?.unregisterCallback(this)
                }
            }
        })

//        val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
//
//        movieMetadata.putString(MediaMetadata.KEY_TITLE, "ABC")
//        movieMetadata.putString(MediaMetadata.KEY_SUBTITLE, "ABC")
//        //movieMetadata.addImage(WebImage(posterUrl))
//       // movieMetadata.addImage(WebImage(Uri.parse("https://resources-eu1.enveu.tv/IberaliaGo_1666014283572-proj-1666014298392/main-app/contents/img_1676888820270.jpg")))
//
//        val mediaInfo = MediaInfo.Builder("https://cdn.jwplayer.com/manifests/bJeozcfa.m3u8")
//            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
//            .setContentType("videos/mp4")
//            .setMetadata(movieMetadata)
//          //  .setStreamDuration(mSelectedMedia.getDuration() * 1000)
//            .build()
//        val remoteMediaClient = mCastSession!!.remoteMediaClient
//        remoteMediaClient?.load(MediaLoadRequestData.Builder().setMediaInfo(mediaInfo).setAutoplay(true).build())
//        remoteMediaClient?.load(
//            MediaLoadRequestData.Builder()
//                .setMediaInfo(buildMediaInfo())
//                .setAutoplay(true)
//                .setCurrentTime(position)
//                .build()
//        )
    }

    private fun buildMediaInfo(): MediaInfo? {
        val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
        movieMetadata.putString(MediaMetadata.KEY_TITLE, "Playing from SDK")
        Logger.d("videoMetadata: $videoMetadata")
        videoMetadata?.title?.let { movieMetadata.putString(MediaMetadata.KEY_TITLE, it) }
      //  videoMetadata?.posterUrl?.let { movieMetadata.addImage(WebImage(Uri.parse(it))) }

        return videoMetadata?.videoUrl?.let {
            MediaInfo.Builder(it)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("videos/mp4")
                .setMetadata(movieMetadata)
                .build()
        }
    }



    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = listener
        mCastSession?.castDevice?.let {
            enveuPlayerControlView.onCastInProgress(it.friendlyName)
            play()
           // pause()
        } ?: enveuPlayerControlView.onCastDisconnected()
    }

    override fun onStop() {
        super.onStop()
        ConnectivityReceiver.connectivityReceiverListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mCastContext?.sessionManager?.removeSessionManagerListener(
            mSessionManagerListener,
            CastSession::class.java
        )
    }

    private val mediaProgressListener = RemoteMediaClient.ProgressListener { progressMs, _ ->
        currentPosition = progressMs
    }

    override fun onStart() {
        super.onStart()
        mCastContext?.sessionManager?.addSessionManagerListener(
            mSessionManagerListener,
            CastSession::class.java
        )
    }

    data class VideoMetadata(
       // var bcoveVideoId: String? = null,
      //  var posterUrl: String = "",
        var title: String = "ABC",
        var videoUrl: String = "https://cdn.jwplayer.com/manifests/bJeozcfa.m3u8",
        var mediaType: Int = 123
    )

}
