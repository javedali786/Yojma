package com.tv.uscreen.yojmatv.fragments.player.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModelProvider
import com.enveu.client.utils.ClickHandler.disallowClick
import com.google.gson.JsonObject

import com.tv.uscreen.yojmatv.Bookmarking.BookmarkingViewModel
import com.tv.uscreen.yojmatv.R

import com.tv.uscreen.yojmatv.activities.detail.ui.DetailActivity
import com.tv.uscreen.yojmatv.activities.detail.ui.EpisodeActivity
import com.tv.uscreen.yojmatv.activities.live.LiveActivity
import com.tv.uscreen.yojmatv.activities.series.ui.SeriesDetailActivity
import com.tv.uscreen.yojmatv.baseModels.BaseBindingFragment
import com.tv.uscreen.yojmatv.beanModel.emptyResponse.ResponseEmpty
import com.tv.uscreen.yojmatv.beanModel.responseGetWatchlist.ResponseGetIsWatchlist
import com.tv.uscreen.yojmatv.beanModel.responseIsLike.ResponseIsLike
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.databinding.DetailWatchlistLikeShareViewBinding

import com.tv.uscreen.yojmatv.enums.DownloadStatus
import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogSingleButtonFragment
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.MediaTypeConstants
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.AppColors
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper.imageViewDrawableBgColor
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.ActivityTrackers
import com.tv.uscreen.yojmatv.utils.helpers.CheckInternetConnection
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils
import com.tv.uscreen.yojmatv.utils.helpers.ToastHandler
import com.tv.uscreen.yojmatv.utils.helpers.downloads.DownloadedVideoActivity
import com.tv.uscreen.yojmatv.utils.helpers.downloads.OnDownloadClickInteraction
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import java.util.Objects

class UserInteractionFragment : BaseBindingFragment<DetailWatchlistLikeShareViewBinding?>(), AlertDialogFragment.AlertDialogListener, View.OnClickListener {
    private var assetId = 0
    private var preference: KsPreferenceKeys? = null
    private var token: String? = null
    private var watchListCounter = 0
    private var likeCounter = 0
    private var seriesDetailBean: EnveuVideoItemBean? = null
    private var trailerRefId: String? = null
    private var bookmarkingViewModel: BookmarkingViewModel? = null
    private var isLoggedOut = false
    private var onDownloadClickInteraction: OnDownloadClickInteraction? = null
    private var isLogin: String? = null
    private var kidsMode = false
    private val stringsHelper by lazy { StringsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): DetailWatchlistLikeShareViewBinding {
        return DetailWatchlistLikeShareViewBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //        ThemeHandler.getInstance().applyUserInterationPage(view.getContext(),getBinding());
        binding!!.stringData = StringsHelper
        binding!!.colorsData = ColorsHelper
        try {
            getAssetId()
            hitApiIsLike()
            hitApiIsWatchList()
            isLoggedOut = false
            if (activity is SeriesDetailActivity) {
                binding!!.watchList.visibility = View.GONE
            }
            if (activity is LiveActivity) {
                binding!!.watchList.visibility = View.GONE
                binding!!.llLike.visibility = View.GONE
            }
            kidsMode = KsPreferenceKeys.getInstance().kidsMode
            isLogin = preference!!.appPrefLoginStatus
            setKidsMode(kidsMode)
            if (!isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true) && kidsMode) {
                binding!!.watchList.visibility = View.GONE
                binding!!.llLike.visibility = View.GONE
            }

        } catch (e: Exception) {
            Logger.w(e)
        }
        setClickListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (activity is OnDownloadClickInteraction) {
            onDownloadClickInteraction = activity as OnDownloadClickInteraction?
        } else {
            Logger.w(activity.toString() + " does not implement OnDownloadClickInteraction")
        }
    }

    private fun setClickListeners() {
        binding!!.shareWith.setOnClickListener(this)
        //        getBinding().showComments.setOnClickListener(this);
//        getBinding().downloadVideo.setOnClickListener(this);
//        getBinding().videoDownloaded.setOnClickListener(this);
//        getBinding().videoDownloading.setProgress(0);
//        getBinding().videoDownloading.setOnClickListener(this);
//        getBinding().pauseDownload.setOnClickListener(this);
        binding!!.downloadStatus = DownloadStatus.START
        if (activity is SeriesDetailActivity) {
//            getBinding().download.setVisibility(View.GONE);
        }

    }

    private fun getAssetId() {
        val bundle = arguments
        if (bundle != null) {
            likeCounter = 0
            assetId = bundle.getInt(AppConstants.BUNDLE_ASSET_ID)
            seriesDetailBean = bundle.getSerializable(AppConstants.BUNDLE_SERIES_DETAIL) as EnveuVideoItemBean?
            trailerRefId = bundle.getString(AppConstants.BUNDLE_TRAILER_REF_ID)
        }
        if (activity != null && preference == null) preference = KsPreferenceKeys.getInstance()
        bookmarkingViewModel = ViewModelProvider(this)[BookmarkingViewModel::class.java]
        if (preference!!.appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            token = preference!!.appPrefAccessToken
        } else {
            resetLike()
            resetWatchList()
        }
        uiInitialisation()
    }

    fun uiInitialisation() {
        likeClick()
        watchListClick()
        binding!!.shareWith.setOnTouchListener { _: View?, motionEvent: MotionEvent? -> gestureDetector.onTouchEvent(motionEvent!!) }

        // setTrailerColor();
        //    getBinding().llTrailer.setOnClickListener(v -> showTrailer());

//        setWhiteColorOnShareButton(getBinding().addIcon, getBinding().tvWatch);
//        setWhiteColorOnShareButton(getBinding().likeIcon, getBinding().tvLike);
//        setWhiteColorOnShareButton(getBinding().shareImg, getBinding().shareTxt);
    }

    //    private void setWhiteColorOnShareButton(ImageView imageView, TextView textView) {
    //        Drawable unwrappedDrawable = imageView.getBackground();
    //        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
    //        int iconColor = ColorsHelper.INSTANCE.colorParser(colorsHelper.instance()).etData?().getConfig?().getSeries_detail_share_unselected_color?(), R.toString().color.series_detail_share_unselected_color);
    //        DrawableCompat.setTint(wrappedDrawable, iconColor);
    //        textView.setTextColor(iconColor);
    //    }
    private fun showTrailer() {
        val args = Bundle()
        args.putString(AppConstants.BUNDLE_VIDEO_ID_BRIGHTCOVE, trailerRefId.toString())
        args.putBoolean("from_binge", false)
        args.putBoolean("", true)
        args.putInt("from", 1)
        args.putString("selected_lang", KsPreferenceKeys.getInstance().appLanguage)
        args.putString("selected_track", KsPreferenceKeys.getInstance().qualityName)
        val intent = Intent(activity, DownloadedVideoActivity::class.java)
        intent.putExtra(AppConstants.EXTRA_TRAILER_DETAILS, args)
        startActivity(intent)
    }

    private var gestureDetector = GestureDetector(activity, object : SimpleOnGestureListener() {
        override fun onLongPress(event: MotionEvent) {
            super.onLongPress(event)
            copyShareURL()
        }

        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            openShareDialogue()
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return true
        }
    })

    private fun copyShareURL() {
        var imgUrl = seriesDetailBean!!.thumbnailImage
        var id = seriesDetailBean!!.id
        var title = seriesDetailBean!!.name
        var assetType: String? = ""
        when (activity) {
            is SeriesDetailActivity -> {
                imgUrl = seriesDetailBean!!.posterURL
                id = seriesDetailBean!!.id
                title = seriesDetailBean!!.title
                assetType = MediaTypeConstants.getInstance().series
                (activity as SeriesDetailActivity?)!!.seriesLoader()
            }

            is EpisodeActivity -> {
                imgUrl = seriesDetailBean!!.posterURL
                id = seriesDetailBean!!.id
                title = seriesDetailBean!!.title
                assetType = MediaTypeConstants.getInstance().episode
            }

            is DetailActivity -> {
                imgUrl = seriesDetailBean!!.posterURL
                id = seriesDetailBean!!.id
                title = seriesDetailBean!!.title
                assetType = seriesDetailBean!!.assetType
            }

            is LiveActivity -> {
                imgUrl = seriesDetailBean!!.posterURL
                id = seriesDetailBean!!.id
                title = seriesDetailBean!!.title
                assetType = MediaTypeConstants.getInstance().live
            }
        }
        if (disallowClick()) {
            return
        }
        //imgUrl = AppCommonMethod.getBranchUrl(imgUrl,getActivity());
        AppCommonMethod.copyShareURL(activity, title, id, assetType, imgUrl, if (seriesDetailBean!!.seriesId == null) "" else seriesDetailBean!!.seriesId, seriesDetailBean!!.season)
        Handler().postDelayed({
            if (activity is SeriesDetailActivity) {
                (activity as SeriesDetailActivity?)!!.dismissLoading((activity as SeriesDetailActivity?)!!.binding!!.progressBar)
            }
        }, 2000)
    }

    private fun goToLogin() {
        when (activity) {
            is SeriesDetailActivity -> {
                ActivityTrackers.getInstance().setLauncherActivity("SeriesDetailActivity")
                (activity as SeriesDetailActivity?)!!.openLogin()
            }

            is EpisodeActivity -> {
                (activity as EpisodeActivity?)!!.openLoginPage(resources.getString(R.string.please_login_play))
                ActivityTrackers.getInstance().setLauncherActivity("EpisodeActivity")
            }

            is DetailActivity -> {
                ActivityTrackers.getInstance().setLauncherActivity("DetailActivity")
                (activity as DetailActivity?)!!.openLoginPage(resources.getString(R.string.please_login_play))
            }
        }
    }

    private fun watchListClick() {
        binding!!.watchList.setOnClickListener {
            if (binding!!.wProgressBar.visibility != View.VISIBLE) {
                val isLogin = preference!!.appPrefLoginStatus
                if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                    setWatchListForAsset(1)
                } else {
                    ActivityTrackers.getInstance().setAction(ActivityTrackers.WATCHLIST)
                    goToLogin()
                }
            }
        }
    }

    fun setWatchListForAsset(from: Int) {
        binding!!.wProgressBar.visibility = View.VISIBLE
        binding!!.addIcon.visibility = View.GONE
        val id = seriesDetailBean!!.id.toString()
        if (watchListCounter == 0) {
            hitApiAddWatchList(from)
        } else {
            hitApiRemoveList()
        }
    }

    private fun hitApiRemoveList() {
        bookmarkingViewModel!!.hitRemoveWatchlist(token, assetId).observe(viewLifecycleOwner) { responseEmpty: ResponseEmpty ->
            binding!!.wProgressBar.visibility = View.GONE
            binding!!.addIcon.visibility = View.VISIBLE
            if (Objects.requireNonNull(responseEmpty).isStatus) {
                resetWatchList()
            } else {
                if (responseEmpty.responseCode == 4302) {
                    isLoggedOut = true
                    logoutCall()
                } else if (responseEmpty.responseCode == 500) {
                    showDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                }
            }
        }
    }

    private fun likeClick() {
        binding!!.llLike.setOnClickListener { setLikeForAsset(1) }
    }

    fun setToken(token: String?) {
        this.token = token
    }

    fun setLikeForAsset(from: Int) {
        if (binding!!.lProgressBar.visibility != View.VISIBLE) {
            if (disallowClick()) {
                return
            }
            if (preference!!.appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                binding!!.lProgressBar.visibility = View.VISIBLE
                binding!!.likeIcon.visibility = View.GONE
                if (likeCounter == 0) hitApiAddLike(from) else hitApiRemoveLike()
            } else {
                ActivityTrackers.getInstance().setAction(ActivityTrackers.LIKE)
                goToLogin()
            }
        }
    }

    private fun hitApiAddLike(from: Int) {
        bookmarkingViewModel!!.hitApiAddLike(token, assetId).observe(viewLifecycleOwner) { responseEmpty: ResponseEmpty ->
            binding!!.lProgressBar.visibility = View.GONE
            binding!!.likeIcon.visibility = View.VISIBLE
            if (Objects.requireNonNull(responseEmpty).isStatus) {
                setLike()
            } else {
                if (responseEmpty.responseCode == 4302) {
                    isLoggedOut = true
                    logoutCall()
                } else if (responseEmpty.responseCode == 4902) {
                    setLike()
                    val debugMessage = responseEmpty.debugMessage
                    //from value will bedepends on how user click of watchlist icon-->>if loggedout=2 else=2
                    if (from == 1) {
                        showDialog(getString(R.string.error), debugMessage)
                    }
                } else if (responseEmpty.responseCode == 500) {
                    showDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                } else {
                    showDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                }
            }
        }
    }

    private fun hitApiRemoveLike() {
        bookmarkingViewModel!!.hitApiDeleteLike(token, assetId).observe(viewLifecycleOwner) { responseEmpty: ResponseEmpty ->
            binding!!.lProgressBar.visibility = View.GONE
            binding!!.likeIcon.visibility = View.VISIBLE
            if (Objects.requireNonNull(responseEmpty).isStatus) {
                resetLike()
            } else {
                if (responseEmpty.responseCode == 4302) {
                    isLoggedOut = true
                    showDialog(getString(R.string.logged_out), resources.getString(R.string.you_are_logged_out))
                } else if (responseEmpty.responseCode == 500) {
                    showDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                }
            }
        }
    }

    private fun hitApiIsLike() {
        if (preference!!.appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            val requestParam = JsonObject()
            requestParam.addProperty(AppConstants.API_PARAM_LIKE_ID, assetId)
            requestParam.addProperty(AppConstants.API_PARAM_LIKE_TYPE, MediaTypeConstants.getInstance().series)
            bookmarkingViewModel!!.hitApiIsLike(token, assetId).observe(viewLifecycleOwner) { responseEmpty: ResponseIsLike ->
                if (Objects.requireNonNull(responseEmpty).isStatus) {
                    if (StringUtils.isNullOrEmptyOrZero(responseEmpty.data.id)) {
                        resetLike()
                    } else {
                        setLike()
                    }
                } else {
                    if (responseEmpty.responseCode == 4302) {
                        isLoggedOut = true
                        logoutCall()
                    } else if (responseEmpty.responseCode == 500) {
                        showDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                    }
                }
            }
        }
    }

    private fun hitApiIsWatchList() {
        if (preference!!.appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            bookmarkingViewModel!!.hitApiIsWatchList(token, assetId).observe(viewLifecycleOwner) { responseEmpty: ResponseGetIsWatchlist ->
                if (Objects.requireNonNull(responseEmpty).isStatus) {
                    setWatchList()
                } else {
                    if (responseEmpty.responseCode == 4302) {
                        isLoggedOut = true
                        logoutCall()
                    } else if (responseEmpty.responseCode == 500) {
                        showDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                    }
                }
            }
        }
    }

    private fun hitApiAddWatchList(from: Int) {
        if (activity is SeriesDetailActivity) {
            (activity as SeriesDetailActivity?)!!.seriesLoader()
        }
        bookmarkingViewModel!!.hitApiAddWatchList(token, assetId).observe(viewLifecycleOwner) { responseEmpty: ResponseEmpty ->
            binding!!.wProgressBar.visibility = View.GONE
            binding!!.addIcon.visibility = View.VISIBLE
            if (Objects.requireNonNull(responseEmpty).isStatus) {
                setWatchList()
            } else {
                if (responseEmpty.responseCode == 4302) {
                    isLoggedOut = true
                    logoutCall()
                } else if (responseEmpty.responseCode == 4904) {
                    setWatchList()
                    val debugMessage = responseEmpty.debugMessage
                    //from value will bedepends on how user click of watchlist icon-->>if loggedout=2 else=2
                    if (from == 1) {
                        showDialog(getString(R.string.error), debugMessage)
                    }
                } else if (responseEmpty.responseCode == 500) {
                    showDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                } else {
                    showDialog(getString(R.string.error), getString(R.string.something_went_wrong))
                }
            }
        }
    }

    private fun openShareDialogue() {
        var imgUrl = seriesDetailBean!!.thumbnailImage
        var id = seriesDetailBean!!.id
        var title = seriesDetailBean!!.name
        var assetType = "" // seriesDetailBean.getVideoDetails().getVideoType();
        if (activity is SeriesDetailActivity) {
            imgUrl = seriesDetailBean!!.posterURL
            id = seriesDetailBean!!.id
            title = seriesDetailBean!!.title
            assetType = MediaTypeConstants.getInstance().series
            (activity as SeriesDetailActivity?)!!.seriesLoader()
        } else if (activity is EpisodeActivity) {
            imgUrl = seriesDetailBean!!.posterURL
            id = seriesDetailBean!!.id
            title = seriesDetailBean!!.title
            assetType = MediaTypeConstants.getInstance().episode
        }
        if (activity is DetailActivity) {
            imgUrl = seriesDetailBean!!.posterURL
            id = assetId
            title = seriesDetailBean!!.title
            assetType = MediaTypeConstants.getInstance().movie
        } else if (activity is LiveActivity) {
            imgUrl = seriesDetailBean!!.posterURL
            id = seriesDetailBean!!.id
            title = seriesDetailBean!!.title
            assetType = MediaTypeConstants.getInstance().live
        }
        val ids: String = seriesDetailBean!!.id.toString()
        if (disallowClick()) {
            return
        }
        imgUrl = AppCommonMethod.getBranchUrl(imgUrl, requireContext())
        //    AppCommonMethod.openShareDialog(getActivity(), title, id, assetType, imgUrl, seriesDetailBean.getSeriesId()  == null ? "" : seriesDetailBean.getSeriesId(), seriesDetailBean.getSeason());
        AppCommonMethod.openShareFirebaseDynamicLinks(requireActivity(), title, id, assetType, imgUrl, seriesDetailBean!!.series, seriesDetailBean!!.season)
        Handler().postDelayed({
            if (activity is SeriesDetailActivity) {
                (activity as SeriesDetailActivity?)!!.dismissLoading((activity as SeriesDetailActivity?)!!.binding!!.progressBar)
            }
        }, 2000)
    }

    private fun setLike() {
        binding!!.lProgressBar.visibility = View.GONE
        binding!!.likeIcon.visibility = View.VISIBLE
        likeCounter = 1
        setLikeProperty()
    }

    private fun setLikeProperty() {
        binding!!.likeIcon.background = ContextCompat.getDrawable(binding!!.likeIcon.context, R.drawable.ic_liked_detail_page)
        imageViewDrawableBgColor(binding!!.likeIcon, AppColors.detailPageLikeSelectedColor())
        binding!!.tvLike.setTextColor(AppColors.detailPageLikeSelectedColor())
        ImageViewCompat.setImageTintList(binding!!.likeIcon, ColorStateList.valueOf(AppColors.detailPageLikeSelectedColor()))
    }

    private fun resetLike() {
        binding!!.lProgressBar.visibility = View.GONE
        binding!!.likeIcon.visibility = View.VISIBLE
        binding!!.likeIcon.visibility = View.VISIBLE
        likeCounter = 0
        binding!!.likeIcon.background = ContextCompat.getDrawable(binding!!.likeIcon.context, R.drawable.ic_like_icon)
        imageViewDrawableBgColor(binding!!.likeIcon, AppColors.detailPageLikeUnselectedColor())
        binding!!.tvLike.setTextColor(AppColors.detailPageLikeUnselectedColor())
    }

    //    public void setTrailerColor() {
    //        Logger.d(seriesDetailBean.getTitle() + " | trailer_reference_id: " + trailerRefId);
    //        if (ObjectHelper.isEmpty(trailerRefId)) {
    //            getBinding().llTrailer.setVisibility(View.GONE);
    //        } else {
    //            getBinding().llTrailer.setVisibility(View.VISIBLE);
    //        }
    //
    //        getBinding().tvTrailer.setTextColor(
    //                ContextCompat.getColor(getBinding().tvTrailer.getContext(), R.color.more_text_color_dark));
    //        ImageViewCompat.setImageTintList(getBinding().ivTrailer,
    //                ColorStateList.valueOf(ContextCompat.getColor(getBinding().ivTrailer.getContext(), R.color.more_text_color_dark)));
    //    }
    //
    //    private void setLiveChatColor() {
    //        getBinding().tvChat.setTextColor(ContextCompat.getColor(getBinding().tvChat.getContext(), R.color.more_text_color_dark));
    //        ImageViewCompat.setImageTintList(getBinding().ivChat, ColorStateList.valueOf(ContextCompat.getColor(getBinding().ivChat.getContext(), R.color.more_text_color_dark)));
    //    }
    private fun setWatchList() {
        if (activity is SeriesDetailActivity) {
            (activity as SeriesDetailActivity?)!!.dismissLoading((activity as SeriesDetailActivity?)!!.binding!!.progressBar)
        }
        binding!!.wProgressBar.visibility = View.GONE
        binding!!.addIcon.visibility = View.VISIBLE
        watchListCounter = 1
        setTextColor()
    }

    private fun setTextColor() {
        binding!!.addIcon.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_addedlist)
        imageViewDrawableBgColor(binding!!.addIcon, AppColors.detailPageMyListSelectedColor())
        binding!!.tvWatch.setTextColor(AppColors.detailPageMyListSelectedColor())
    }

    private fun resetWatchList() {
        binding!!.wProgressBar.visibility = View.GONE
        binding!!.addIcon.visibility = View.VISIBLE
        watchListCounter = 0
        imageViewDrawableBgColor(binding!!.addIcon, AppColors.detailPageMyListUnselectedColor())
        binding!!.tvWatch.setTextColor(AppColors.detailPageMyListUnselectedColor())
    }

    private fun showDialog(title: String, message: String) {
        val fm = requireActivity().supportFragmentManager
        val alertDialog = AlertDialogSingleButtonFragment.newInstance(
            title,
            message,
            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(), getString(R.string.popup_ok))
        )
        alertDialog.isCancelable = false
        alertDialog.setAlertDialogCallBack(this)
        alertDialog.show(fm, "fragment_alert")
    }

    override fun onFinishDialog() {
        if (isLoggedOut) {
            logoutCall()
        }
    }

    private fun logoutCall() {
        if (CheckInternetConnection.isOnline(requireActivity())) {
            clearCredientials(preference)
            hitApiLogout(baseActivity, preference!!.appPrefAccessToken)
        } else {
            ToastHandler.getInstance().show(activity, getString(R.string.no_internet_connection))
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.shareWith -> {
                openShareDialogue()
            }

            R.id.llLike -> {
                likeClick()
            }

            R.id.watchList -> {
                watchListClick()
            }

            R.id.download_video -> {
                if (onDownloadClickInteraction != null) onDownloadClickInteraction!!.onDownloadClicked(null, 0, this)
            }

            R.id.video_downloaded -> {
                if (onDownloadClickInteraction != null) onDownloadClickInteraction!!.onDownloadCompleteClicked(view, this, null)
            }

            R.id.video_downloading -> {
                if (onDownloadClickInteraction != null) onDownloadClickInteraction!!.onProgressbarClicked(view, this, null)
            }

            R.id.pause_download -> {
                Logger.w("pauseClicked", "in")
                if (onDownloadClickInteraction != null) onDownloadClickInteraction!!.onPauseClicked(null, this)
            }
        }
    }

    //    public void setDownloadStatus(DownloadStatus downloadStatus) {
    //        if (getBinding() != null) {
    //            getBinding().setDownloadStatus(downloadStatus);
    //            if (downloadStatus.equals(com.terramedia.iberalia.enums.DownloadStatus.DOWNLOADING)) {
    //                getBinding().downloadText.setText(getString(R.string.downloading));
    //            } else if (downloadStatus.equals(com.terramedia.iberalia.enums.DownloadStatus.DOWNLOADED)) {
    //                getBinding().downloadText.setText(getString(R.string.downloaded));
    //            } else {
    //                getBinding().downloadText.setText(getString(R.string.download));
    //            }
    //        }
    //    }
    //
    //    public void setDownloadable(boolean isDownloadable) {
    //        if (getBinding() != null){
    //            getBinding().setIsDownloadable(isDownloadable);
    //            if(isDownloadable && isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString()) && !kidsMode){
    //                getBinding().download.setVisibility(View.VISIBLE);
    //            }
    //            else if(isDownloadable && !isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString()) && !kidsMode ){
    //                getBinding().download.setVisibility(View.VISIBLE);
    //            }
    //            else if(isDownloadable && isLogin.equalsIgnoreCase(AppConstants.UserStatus.Login.toString()) && kidsMode ){
    //                getBinding().download.setVisibility(View.VISIBLE);
    //            }
    //            else {
    //                getBinding().download.setVisibility(View.GONE);
    //
    //            }
    //
    //        }
    //
    //    }
    private fun setKidsMode(isKidsMode: Boolean) {
        if (binding != null) binding!!.isKidsMode = isKidsMode
    } //    public void setDownloadProgress(float progress) {
    //        if (getBinding() != null)
    //            getBinding().videoDownloading.setProgress(progress);
    //    }
}