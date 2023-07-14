package com.example.jwplayer

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.breadgangtvnetwork.Bookmarking.BookmarkingViewModel
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.breadgangtvnetwork.databinding.ActivityPlayerBinding
import com.breadgangtvnetwork.jwplayer.utils.Logger
import com.breadgangtvnetwork.utils.constants.AppConstants
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import java.io.Serializable

class PlayerActivity : AppCompatActivity(), Serializable, JWPlayerFragment.OnPlayerInteractionListener {
    private lateinit var binding: ActivityPlayerBinding
    var  seasonEpisodesList : List<EnveuVideoItemBean>? = null
    var myFragment: JWPlayerFragment? = null
    private var playbackUrl : String? = null
    private var episodeId : Int? = 0
    private var isLogin: String? = null
    private var token: String? = null
    private var tittle: String? = null
    private var posterUrl: String? = null
    private var contentType: String? = null
    private var screenName: String? = null
    private var preference: KsPreferenceKeys? = null
    private var bookmarkingViewModel: BookmarkingViewModel? = null
    private var isBingeWatchEnable : Boolean? = false
    private var isTrailer : Boolean? = false
    private var isLive : Boolean? = false
    private var activity: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        window.setBackgroundDrawableResource(R.color.buy_now_pay_now_btn_text_color)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val bundle = intent.extras
        if (bundle != null) {
            playbackUrl = bundle.getString("playBackUrl")
            isBingeWatchEnable = bundle.getBoolean("binge_watch")
            episodeId = bundle.getInt("episodeId")
            isTrailer = bundle.getBoolean("isTrailer")
            isLive = bundle.getBoolean("isLive")
            tittle = bundle.getString("tittle")
            posterUrl = bundle.getString("posterUrl")
            contentType = bundle.getString("contentType")
            screenName = bundle.getString("screenName")
            activity = bundle.getString("activity")
            if (activity?.contains("HomeSliderActivity") == true) {
                screenName = "HomeSliderActivity"
            }
            try {
                if (bundle.getSerializable("episodeList") as List<EnveuVideoItemBean> != null) {
                    seasonEpisodesList =
                        bundle.getSerializable("episodeList") as List<EnveuVideoItemBean>
                }
            } catch (e: Exception) {
                Logger.w(e)
            }
        }
        bookmarkingViewModel = ViewModelProvider(this)[BookmarkingViewModel::class.java]
        preference = KsPreferenceKeys.getInstance()
        isLogin = preference?.appPrefLoginStatus
        token = preference?.appPrefAccessToken
        Logger.d("PlayerUrlToPlay",playbackUrl.toString())
        setContentView(binding.root)
        fitPlayerToFullScreen()
        val args = Bundle()
        args.putString("playBackUrl", playbackUrl)
        isTrailer?.let { args.putBoolean("isTrailer", it) }
        isLive?.let { args.putBoolean("isLive", it) }
        tittle?.let { args.putString("tittle",it) }
        posterUrl?.let { args.putString("posterUrl",it) }
        contentType?.let { args.putString("contentType",it) }
        screenName?.let { args.putString("screenName",it) }
        activity?.let { args.putString("activity",it) }
        episodeId?.let { args.putInt("episodeId", it) }
        if (null != seasonEpisodesList) {
            args.putSerializable("episodeList", seasonEpisodesList as Serializable)
        }

        isBingeWatchEnable?.let { args.putBoolean("binge_watch", it) }
        myFragment = JWPlayerFragment()
        myFragment!!.setArguments(args)
        supportFragmentManager
            .beginTransaction()
            .add(R.id.container_player, myFragment!!, "JWPlayerFragment")
            .commit()
    }
    private fun fitPlayerToFullScreen() {
        val params: ViewGroup.LayoutParams = binding.containerPlayer.getLayoutParams()
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.containerPlayer.requestLayout()
    }

    override fun onBookmarkCall(currentPosition: Double, assetId: Int) {
        var i: Int = currentPosition.toInt()
            if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            bookmarkingViewModel?.bookmarkVideo(token, assetId, i)
        }
    }

    override fun onBookmarkFinish(assetId: Int) {
        if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            bookmarkingViewModel?.finishBookmark(token, assetId)
        }
    }
}