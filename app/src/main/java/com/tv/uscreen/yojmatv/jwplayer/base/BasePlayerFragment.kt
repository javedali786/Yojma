package com.enveu.player.base

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.enveu.player.controls.EnveuPlayerControlView
import com.enveu.player.receiver.ConnectivityReceiver
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadRequestData
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.MediaStatus
import com.google.android.gms.cast.MediaTrack
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.MediaQueue
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.images.WebImage
import com.google.gson.Gson
import com.tv.uscreen.yojmatv.SDKConfig
import com.tv.uscreen.yojmatv.activities.detail.viewModel.DetailViewModel
import com.tv.uscreen.yojmatv.beanModel.entitle.ResponseEntitle
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.callbacks.apicallback.EntitlementCallBack
import com.tv.uscreen.yojmatv.jwplayer.cast.ExpandedControlsActivity
import com.tv.uscreen.yojmatv.jwplayer.cast.TracksItem
import com.tv.uscreen.yojmatv.utils.Logger
import org.json.JSONObject

abstract class BasePlayerFragment : Fragment() {
    protected var currentPosition: Long = 0L
    var isUpdatedChromecastMedia = false
     var externalRefId: String? = null
    var playbackUrl: String? = null
    var isSeries: Boolean = false
    var currentPlayingIndex: Int? = -1
    var viewModel: DetailViewModel? = null
    var token: String? = null

    var seasonEpisodesList: List<EnveuVideoItemBean>? = null
    var subtitleTracks: List<TracksItem?>? = null
    var tittle: String? = null
    var chromecastPlaylist: ArrayList<MediaQueueItem>? = null
    var posterUrl: String? = null

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

    fun isCasting(videoUrl: String): Boolean {
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
                val intent = Intent(activity, ExpandedControlsActivity::class.java)
                startActivity(intent)
                if (isUpdatedChromecastMedia) {
                    remoteMediaClient?.unregisterCallback(this)
                }
            }
        })
        if (isSeries) {
            counter = currentPlayingIndex!!+1
           externalRefId?.let {
               val queueItem: MediaQueueItem =
                   MediaQueueItem.Builder(
                       buildMediaInfo(
                           seasonEpisodesList!![currentPlayingIndex!!],
                           it
                       )!!
                   )
                       .setAutoplay(true)
                       .setPreloadTime(20.0)
                       .build()
               val newItemArray: Array<MediaQueueItem> = arrayOf(queueItem)
               remoteMediaClient?.queueLoad(
                   newItemArray, 0,
                   MediaStatus.REPEAT_MODE_REPEAT_OFF, JSONObject()
               )
           }
            AsyncTask.execute { getChromecastList() }
        } else {
            remoteMediaClient?.load(
                MediaLoadRequestData.Builder()
                    .setMediaInfo(buildMediaInfo())
                    .setAutoplay(autoPlay)
                    .setCurrentTime(position)
                    .build()
            )
        }

    }

    // end the chromecast session
    fun endSession() {
        remoteMediaClient?.stop()
        mCastContext?.sessionManager?.endCurrentSession(true)
    }

    var counter: Int = 0
    fun getChromecastList() {
        if (counter < seasonEpisodesList?.size!! - 1 && isAdded) {
            getPlaylist(counter)
        } else {

        }
    }

    // to get the item count in the queue
    val count: Int
        get() {
            val queue: MediaQueue = mediaQueue ?: return 0
            return queue.itemCount
        }

    // will return the Media Queue
    val mediaQueue: MediaQueue?
        get() {
            val queue: MediaQueue? =
                if (remoteMediaClient == null) null else remoteMediaClient!!.mediaQueue
            return queue
        }

    private fun getPlaylist(i: Int) {
        var externalRefId = ""
        if (seasonEpisodesList!![i].isPremium) {
            val sku = seasonEpisodesList!![i].sku
            viewModel?.checkEntitlement(token, sku, object : EntitlementCallBack {
                override fun entitlementStatus(it: ResponseEntitle) {
                    if (it != null) {
                        if (it.data.entitled) {
                            externalRefId = it.data.externalRefId
                            val queueItem: MediaQueueItem =
                                MediaQueueItem.Builder(
                                    buildMediaInfo(
                                        seasonEpisodesList!![i],
                                        externalRefId
                                    )!!
                                )
                                    .setAutoplay(true)
                                    .setPreloadTime(20.0)
                                    .build()

                            if (count != 0) {
                                remoteMediaClient?.queueAppendItem(queueItem, JSONObject())

                            }

                        }
                    }
                    counter++
                    getChromecastList()
                }

            })
        } else {
            externalRefId = seasonEpisodesList!![i]?.externalRefId
            val queueItem: MediaQueueItem =
                MediaQueueItem.Builder(buildMediaInfo(seasonEpisodesList!![i], externalRefId)!!)
                    .setAutoplay(true)
                    .build()
            chromecastPlaylist?.add(queueItem)
            counter++
            getChromecastList()
        }

    }

    private fun buildMediaInfo(
        enveuVideoItemBean: EnveuVideoItemBean,
        externalRefid: String
    ): MediaInfo? {
        val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
        movieMetadata.putString(MediaMetadata.KEY_TITLE, enveuVideoItemBean.title ?: "")
        val tracks: MutableList<MediaTrack> = ArrayList<MediaTrack>()
        createSubtitleTracks(tracks)
        enveuVideoItemBean?.posterURL?.let { movieMetadata.addImage(WebImage(Uri.parse(it))) }
        return MediaInfo.Builder(
            SDKConfig.getInstance().playbacK_URL + externalRefid + ".m3u8" ?: ""
        )
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType("videos/mp4")
            .setMetadata(movieMetadata)
            .setMediaTracks(tracks)
            .setStreamDuration(0L)
            .build()
    }

    private fun buildMediaInfo(): MediaInfo? {
        val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE)
        movieMetadata.putString(MediaMetadata.KEY_TITLE, tittle ?: "")
        val tracks: MutableList<MediaTrack> = ArrayList<MediaTrack>()
        createSubtitleTracks(tracks)
        posterUrl?.let { movieMetadata.addImage(WebImage(Uri.parse(it))) }
        return MediaInfo.Builder(playbackUrl ?: "")
            .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
            .setContentType("videos/mp4")
            .setMetadata(movieMetadata)
            .setMediaTracks(tracks)
            .setStreamDuration(0L)
            .build()
    }

    private fun createSubtitleTracks(tracks: MutableList<MediaTrack>) {
        if (!subtitleTracks.isNullOrEmpty()) {
            subtitleTracks?.forEachIndexed { index, tracksItem ->
                if (tracksItem?.kind.equals("captions")) {
                    val subtitleTracks =
                        MediaTrack.Builder(index.toLong() /* ID */, MediaTrack.TYPE_TEXT)
                            .setName(tracksItem?.label)
                            .setSubtype(MediaTrack.SUBTYPE_SUBTITLES)
                            .setContentId(tracksItem?.file)
                            /* language is required for subtitle type but optional otherwise */
                            .setLanguage(tracksItem?.language ?: "")
                            .build()
                    tracks.add(subtitleTracks)
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = listener
        mCastSession?.castDevice?.let {
            enveuPlayerControlView.onCastInProgress(it.friendlyName)
            /*  play()*/
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
