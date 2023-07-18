package com.tv.uscreen.yojmatv.activities.listing.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory
import com.enveu.client.bookmarking.bean.continuewatching.ContinueWatchingBookmark
import com.enveu.client.bookmarking.bean.continuewatching.GetContinueWatchingBean
import com.enveu.client.enums.ImageType
import com.tv.uscreen.yojmatv.Bookmarking.BookmarkingViewModel
import com.tv.uscreen.yojmatv.R

import com.tv.uscreen.yojmatv.activities.homeactivity.ui.HomeActivity
import com.tv.uscreen.yojmatv.activities.listing.callback.ItemClickListener
import com.tv.uscreen.yojmatv.adapters.CommonListingAdapter
import com.tv.uscreen.yojmatv.adapters.commonRails.CommonCircleAdapter
import com.tv.uscreen.yojmatv.adapters.commonRails.CommonPosterLandscapeAdapter
import com.tv.uscreen.yojmatv.adapters.commonRails.CommonPosterPotraitAdapter
import com.tv.uscreen.yojmatv.adapters.commonRails.CommonPotraitAdapter
import com.tv.uscreen.yojmatv.adapters.commonRails.CommonPotraitTwoAdapter
import com.tv.uscreen.yojmatv.adapters.commonRails.LandscapeListingAdapter
import com.tv.uscreen.yojmatv.adapters.commonRails.SquareListingAdapter
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModelV3.continueWatching.DataItem
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.CommonApiCallBack
import com.tv.uscreen.yojmatv.databinding.ListingActivityBinding

import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogSingleButtonFragment
import com.tv.uscreen.yojmatv.layersV2.ContinueWatchingLayer
import com.tv.uscreen.yojmatv.networking.apistatus.APIStatus
import com.tv.uscreen.yojmatv.networking.responsehandler.ResponseModel
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.cropImage.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.cropImage.helpers.ShimmerDataModel
import com.tv.uscreen.yojmatv.utils.helpers.CheckInternetConnection
import com.tv.uscreen.yojmatv.utils.helpers.GridSpacingItemDecoration
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.RecyclerAnimator
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import java.util.Objects

class GridActivity : BaseBindingActivity<ListingActivityBinding?>(), ItemClickListener, AlertDialogFragment.AlertDialogListener {
    var playListId: String? = null
    var baseCategory: BaseCategory? = null
    var enveuLayoutType = 0
    private var counter = 0
    private var flag = 0
    private var firstVisiblePosition = 0
    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var commonCircleAdapter: CommonCircleAdapter? = null
    private var commonLandscapeAdapter: LandscapeListingAdapter? = null
    private val commonPotraitAdapter: CommonPotraitAdapter? = null
    private var commonPotraitTwoAdapter: CommonPotraitTwoAdapter? = null
    private var commonPosterLandscapeAdapter: CommonPosterLandscapeAdapter? = null
    private var commonPosterPotraitAdapter: CommonPosterPotraitAdapter? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var squareCommonAdapter: SquareListingAdapter? = null
    private var title: String? = null
    private var isContinueWatchingEnable = false
    private var mIsLoading = true
    private var isScrolling = false
    private var mScrollY = 0
    private var shimmerType = 0
    private var listData: RailCommonData? = null
    private var isloggedout = false
    private var preference: KsPreferenceKeys? = null
    var tabletSize = false
    private val stringsHelper by lazy { StringsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): ListingActivityBinding {
        return ListingActivityBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding!!.colorsData = ColorsHelper
        preference = KsPreferenceKeys.getInstance()
        title = intent.getStringExtra("title")
        isContinueWatchingEnable = intent.extras!!.getBoolean("isContinueWatching")
        playListId = intent.getStringExtra("playListId")
        flag = intent.getIntExtra("flag", 0)
        shimmerType = intent.getIntExtra("shimmerType", 0)
        baseCategory = intent.extras!!.getParcelable("baseCategory")
        tabletSize = this.resources.getBoolean(R.bool.isTablet)
        uiIntilization()
        connectionObserver()
        //ThemeHandler.getInstance().applyGridActivity(getApplicationContext(),getBinding());
    }

    private fun connectionObserver() {
        if (NetworkConnectivity.isOnline(this@GridActivity)) {
            binding!!.noConnectionLayout.visibility = View.GONE
            connectionValidation(true)
        } else {
            connectionValidation(false)
        }
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            if (counter == 0) callShimmer()
            setClicks()
            try {
                railData
            } catch (ignored: Exception) {
            }
        } else {
            noConnectionLayout()
        }
    }

    private fun callShimmer() {
        val shimmerAdapter = CommonListingAdapter(this@GridActivity)
        binding!!.listRecyclerview.hasFixedSize()
        binding!!.listRecyclerview.isNestedScrollingEnabled = false
        var num = 2
        if (baseCategory!!.contentImageType.equals(ImageType.CIR.name, ignoreCase = true) || baseCategory!!.contentImageType.equals(ImageType.PR1.name, ignoreCase = true)
            || baseCategory!!.contentImageType.equals(ImageType.PR2.name, ignoreCase = true)
            || baseCategory!!.contentImageType.equals(ImageType.SQR.name, ignoreCase = true)
        ) {
            val tabletSize = this@GridActivity.resources.getBoolean(R.bool.isTablet)
            num = 3
            if (tabletSize) {
                num = if (this@GridActivity.resources.configuration.orientation == 2) 5 else 4
            }
            shimmerAdapter.setDataList(ShimmerDataModel(this@GridActivity).getList(5))
        } else if (baseCategory!!.contentImageType.equals(ImageType.LDS.name, ignoreCase = true)
            || baseCategory!!.contentImageType.equals(ImageType.LDS2.name, ignoreCase = true)
        ) {
            val tabletSize = this@GridActivity.resources.getBoolean(R.bool.isTablet)
            num = 2
            if (tabletSize) {
                num = if (this@GridActivity.resources.configuration.orientation == 2) 4 else 3
            }
            shimmerAdapter.setDataList(ShimmerDataModel(this@GridActivity).getList(4))
        }
        binding!!.listRecyclerview.addItemDecoration(GridSpacingItemDecoration(num, 6, true))
        gridLayoutManager = GridLayoutManager(this@GridActivity, num)
        binding!!.listRecyclerview.layoutManager = gridLayoutManager
        binding!!.listRecyclerview.adapter = shimmerAdapter
        binding!!.listRecyclerview.visibility = View.VISIBLE
    }

    private fun uiIntilization() {
        binding!!.toolbarGrid.colorsData = ColorsHelper
        binding!!.toolbarGrid.stringData = StringsHelper
        binding!!.colorsData = ColorsHelper
        binding!!.toolbarGrid.backLayout.setOnClickListener { finish() }
        binding!!.toolbarGrid.titleMid.visibility = View.VISIBLE
        binding!!.toolbarGrid.logoMain2.visibility = View.GONE
        binding!!.toolbarGrid.llSearchIcon.visibility = View.VISIBLE
        if (isContinueWatchingEnable) {
            val loginStatus = preference!!.appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)
            if (loginStatus) {
                binding!!.toolbarGrid.titleMid.text = title
            } else {
                ActivityLauncher.getInstance().homeScreen(this@GridActivity, HomeActivity::class.java)
            }
        } else {
            binding!!.toolbarGrid.titleMid.text = title
        }
        //        getBinding().toolbarGrid.homeIcon.setVisibility(View.GONE);
//        getBinding().toolbar.mediaRouteButton.setVisibility(View.GONE);
        binding!!.toolbarGrid.backLayout.visibility = View.VISIBLE
        binding!!.toolbarGrid.titleSkip.visibility = View.GONE
        //    getBinding().toolbar.searchIcon.setVisibility(View.GONE);
        // getBinding().toolbarGrid.titleMidToolbar.setVisibility(View.VISIBLE);
        binding!!.listRecyclerview.hasFixedSize()
        binding!!.listRecyclerview.isNestedScrollingEnabled = false
        binding!!.listRecyclerview.addItemDecoration(GridSpacingItemDecoration(3, 5, true))
    }

    private fun setClicks() {
        binding!!.listRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                try {
                    if (enveuLayoutType == 0) {
                        val layoutManager = recyclerView.layoutManager as GridLayoutManager?
                        firstVisiblePosition = layoutManager?.findFirstVisibleItemPosition()!!
                        if (dy > 0) {
                            visibleItemCount = layoutManager.childCount
                            totalItemCount = layoutManager.itemCount
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                            if (mIsLoading) {
                                if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                                    // Logger.d("slidingValues"+getBinding().listRecyclerview.getAdapter().getItemCount()+" "+counter);
                                    val adapterSize = binding!!.listRecyclerview.adapter!!.itemCount
                                    if (adapterSize > 8) {
                                        mIsLoading = false
                                        counter++
                                        isScrolling = true
                                        mScrollY += dy
                                        connectionObserver()
                                    }
                                }
                            }
                        }
                    } else {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                        firstVisiblePosition = layoutManager?.findFirstVisibleItemPosition()!!
                        if (dy > 0) {
                            visibleItemCount = layoutManager.childCount
                            totalItemCount = layoutManager.itemCount
                            pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                            if (mIsLoading) {
                                if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                                    // Logger.d("slidingValues"+getBinding().listRecyclerview.getAdapter().getItemCount()+" "+counter);
                                    val adapterSize = binding!!.listRecyclerview.adapter!!.itemCount
                                    if (adapterSize > 8) {
                                        mIsLoading = false
                                        counter++
                                        isScrolling = true
                                        mScrollY += dy
                                        connectionObserver()
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Logger.e("ListingActivity", "" + e)
                }
            }
        })
        binding!!.transparentLayout.visibility = View.VISIBLE
        binding!!.transparentLayout.setOnClickListener { }
    }

    // showDialog(GridActivity.this.getResources().getString(R.string.error), GridActivity.this.getResources().getString(R.string.something_went_wrong));
    @get:RequiresApi(api = Build.VERSION_CODES.M)
    private val railData: Unit
        private get() {
            try {
                if (flag == 0) {
                    if (baseCategory!!.referenceName != null && (baseCategory!!.referenceName.equals(
                            AppConstants.ContentType.CONTINUE_WATCHING.name,
                            ignoreCase = true
                        ) || baseCategory!!.referenceName.equals("special_playlist", ignoreCase = true))
                    ) {
                        val preference = KsPreferenceKeys.getInstance()
                        val isLogin = preference.appPrefLoginStatus
                        if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                            val token = preference.appPrefAccessToken
                            val bookmarkingViewModel = ViewModelProvider(this)[BookmarkingViewModel::class.java]
                            bookmarkingViewModel.getContinueWatchingData(token, counter, AppConstants.PAGE_SIZE).observe(this) { getContinueWatchingBean: GetContinueWatchingBean? ->
                                binding!!.transparentLayout.visibility = View.GONE
                                if (getContinueWatchingBean != null) {
                                    if (getContinueWatchingBean.isStatus) {
                                        var videoIds = ""
                                        try {
                                            val continueWatchingBookmarkLists = getContinueWatchingBean.data.continueWatchingBookmarks
                                            val continueWatchingBookmarkList = removeDuplicates(continueWatchingBookmarkLists)
                                            for (continueWatchingBookmark: ContinueWatchingBookmark in continueWatchingBookmarkList) {
                                                videoIds = videoIds + continueWatchingBookmark.assetId.toString() + ","
                                            }
                                            Logger.w("assetIds", videoIds)
                                            ContinueWatchingLayer.getInstance().getContinueWatchingVideos(continueWatchingBookmarkList, videoIds, object : CommonApiCallBack {
                                                override fun onSuccess(item: Any) {
                                                    binding!!.transparentLayout.visibility = View.GONE
                                                    if (item is List<*>) {
                                                        val enveuVideoDetails = item as ArrayList<DataItem>
                                                        val railCommonData = RailCommonData()
                                                        railCommonData.setContinueWatchingData(baseCategory, true, enveuVideoDetails, object : CommonApiCallBack {
                                                            override fun onSuccess(item: Any) {
                                                                setRail(railCommonData)
                                                            }

                                                            override fun onFailure(throwable: Throwable) {}
                                                            override fun onFinish() {}
                                                        })
                                                    }
                                                }

                                                override fun onFailure(throwable: Throwable) {}
                                                override fun onFinish() {}
                                            })
                                        } catch (ignored: Exception) {
                                        }
                                    } else if (getContinueWatchingBean.responseCode == 4302L) {
                                        isloggedout = true
                                        logoutCall()
                                        try {
                                            runOnUiThread(Runnable { ActivityLauncher.getInstance().homeScreen(this@GridActivity, HomeActivity::class.java) })
                                        } catch (e: Exception) {
                                        }
                                    } else if (getContinueWatchingBean.responseCode == 500L) {
                                        // showDialog(GridActivity.this.getResources().getString(R.string.error), GridActivity.this.getResources().getString(R.string.something_went_wrong));
                                    }
                                }
                            }
                        }
                    } else {
                        val railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
                        railInjectionHelper.getPlayListDetailsWithPaginationV2(playListId, counter, AppConstants.PAGE_SIZE, baseCategory).observe(this) { playlistRailData: ResponseModel<*> ->
                            if (playlistRailData.status.equals(APIStatus.START.name, ignoreCase = true)) {
                            } else if (playlistRailData.status.equals(APIStatus.SUCCESS.name, ignoreCase = true)) {
                                if (Objects.requireNonNull(playlistRailData) != null) {
                                    if (playlistRailData.baseCategory != null) {
                                        val railCommonData = playlistRailData.baseCategory as RailCommonData
                                        try {
                                            if (title == null || title.equals("", ignoreCase = true)) {
                                                binding!!.toolbarGrid.titleMid.text = railCommonData.displayName
                                            }
                                        } catch (e: Exception) {
                                            Logger.w(e)
                                        }
                                        listData = railCommonData
                                        setRail(railCommonData)
                                        Logger.e("RAIL DATA", listData!!.isSeries.toString())
                                    }
                                }
                            } else if (playlistRailData.status.equals(APIStatus.ERROR.name, ignoreCase = true)) {
                            } else if (playlistRailData.status.equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                            }
                        }
                    }
                } else {
                }
            } catch (e: Exception) {
                Logger.w(e)
            }
        }

    private fun logoutCall() {
        if (CheckInternetConnection.isOnline(Objects.requireNonNull(this))) {
            clearCredientials(preference)
            hitApiLogout(this, preference!!.appPrefAccessToken)
        } else {
            // new ToastHandler(this).show(getResources().getString(stringsHelper.getStringDataFromJson(
            stringsHelper.stringParse(stringsHelper.instance()!!.data.config.popup_no_internet_connection_found, getString(R.string.popup_no_internet_connection_found))
        }
    }

    private fun removeDuplicates(continueWatchingBookmarkList: List<ContinueWatchingBookmark>): List<ContinueWatchingBookmark> {
        val noRepeat: MutableList<ContinueWatchingBookmark> = ArrayList()
        try {
            for (event in continueWatchingBookmarkList) {
                var isFound = false
                // check if the event name exists in noRepeat
                for (e in noRepeat) {
                    if (e.assetId == event.assetId || e == event) {
                        isFound = true
                        break
                    }
                }
                if (!isFound) noRepeat.add(event)
            }
        } catch (ignored: Exception) {
        }
        return noRepeat
    }

    var railCommonData: RailCommonData? = null
    private fun setRail(playlistRailData: RailCommonData?) {
        railCommonData = playlistRailData
        binding!!.transparentLayout.visibility = View.GONE
        if (isScrolling) {
            if (playlistRailData!!.enveuVideoItemBeans.isNotEmpty() && playlistRailData.enveuVideoItemBeans != null) {
                setUiComponents(playlistRailData)
            }
            binding!!.progressBar.visibility = View.GONE
        } else {
            binding!!.progressBar.visibility = View.GONE
            mIsLoading = true
            if (playlistRailData != null) {
                if (baseCategory!!.referenceName != null && (baseCategory!!.referenceName.equals(
                        AppConstants.ContentType.CONTINUE_WATCHING.name,
                        ignoreCase = true
                    ) || baseCategory!!.referenceName.equals("special_playlist", ignoreCase = true))
                ) {
                    if (commonPosterLandscapeAdapter == null) {
                        RecyclerAnimator(this).animate(binding!!.listRecyclerview)
                        commonPosterLandscapeAdapter = CommonPosterLandscapeAdapter(this, playlistRailData.enveuVideoItemBeans, ArrayList(), "VIDEO", ArrayList(), baseCategory, this)
                        binding!!.listRecyclerview.adapter = commonPosterLandscapeAdapter
                    }
                } else {
                    if (baseCategory!!.contentImageType.equals(ImageType.CIR.name, ignoreCase = true)) {
                        if (commonCircleAdapter == null) {
                            RecyclerAnimator(this).animate(binding!!.listRecyclerview)
                            commonCircleAdapter = CommonCircleAdapter(this, playlistRailData.enveuVideoItemBeans, "VIDEO", ArrayList(), this, playlistRailData)
                            binding!!.listRecyclerview.adapter = commonCircleAdapter
                        } else commonCircleAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                        mIsLoading = playlistRailData.maxContent != commonCircleAdapter!!.itemCount
                    } else if (baseCategory!!.contentImageType.equals(ImageType.SQR.name, ignoreCase = true)) {
                        if (squareCommonAdapter == null) {
                            RecyclerAnimator(this).animate(binding!!.listRecyclerview)
                            squareCommonAdapter = SquareListingAdapter(playlistRailData.enveuVideoItemBeans, "VIDEO", this)
                            binding!!.listRecyclerview.adapter = squareCommonAdapter
                        } else squareCommonAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                        mIsLoading = playlistRailData.maxContent != squareCommonAdapter!!.itemCount
                    } else if (baseCategory!!.contentImageType.equals(ImageType.LDS2.name, ignoreCase = true)) {
                        if (commonLandscapeAdapter == null) {
                            RecyclerAnimator(this).animate(binding!!.listRecyclerview)
                            commonLandscapeAdapter = LandscapeListingAdapter(this, playlistRailData.enveuVideoItemBeans, ArrayList(), "VIDEO", this, baseCategory, tabletSize)
                            binding!!.listRecyclerview.adapter = commonLandscapeAdapter
                        } else commonLandscapeAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                        mIsLoading = playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
                    } else if (baseCategory!!.contentImageType.equals(ImageType.PR2.name, ignoreCase = true)) {
                        if (commonPotraitTwoAdapter == null) {
                            RecyclerAnimator(this).animate(binding!!.listRecyclerview)
                            commonPotraitTwoAdapter = CommonPotraitTwoAdapter(this, playlistRailData.enveuVideoItemBeans, "VIDEO", ArrayList(), 0, this, baseCategory)
                            binding!!.listRecyclerview.adapter = commonPotraitTwoAdapter
                        } else commonPotraitTwoAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                        mIsLoading = playlistRailData.maxContent != commonPotraitTwoAdapter!!.itemCount
                    } else if (baseCategory!!.contentImageType.equals(ImageType.LDS.name, ignoreCase = true)) {
                        if (commonPosterLandscapeAdapter == null) {
                            RecyclerAnimator(this).animate(binding!!.listRecyclerview)
                            commonPosterLandscapeAdapter = CommonPosterLandscapeAdapter(this, playlistRailData.enveuVideoItemBeans, ArrayList(), "VIDEO", ArrayList(), baseCategory, this)
                            binding!!.listRecyclerview.adapter = commonPosterLandscapeAdapter
                        } else commonPosterLandscapeAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                        mIsLoading = playlistRailData.maxContent != commonPosterLandscapeAdapter!!.itemCount
                        if (commonLandscapeAdapter == null) {
                            RecyclerAnimator(this).animate(binding!!.listRecyclerview)
                            commonLandscapeAdapter = LandscapeListingAdapter(this, playlistRailData.enveuVideoItemBeans, ArrayList(), "VIDEO", this, baseCategory, tabletSize)
                            binding!!.listRecyclerview.adapter = commonLandscapeAdapter
                        } else commonLandscapeAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                        mIsLoading = playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
                    } else if (baseCategory!!.contentImageType.equals(ImageType.PR1.name, ignoreCase = true)) {
                        if (commonPosterPotraitAdapter == null) {
                            RecyclerAnimator(this).animate(binding!!.listRecyclerview)
                            commonPosterPotraitAdapter = CommonPosterPotraitAdapter(this, playlistRailData.enveuVideoItemBeans, ArrayList(), "VIDEO", ArrayList(), this, baseCategory, playlistRailData)
                            binding!!.listRecyclerview.adapter = commonPosterPotraitAdapter
                        } else commonPosterPotraitAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                        mIsLoading = playlistRailData.maxContent != commonPosterPotraitAdapter!!.itemCount
                    }
                    //                    else if (baseCategory.getContentImageType().equalsIgnoreCase(ImageType.PR1.name())) {
//                        if (commonPotraitAdapter == null) {
//                            new RecyclerAnimator(this).animate(getBinding().listRecyclerview);
//                            commonPotraitAdapter = new CommonPotraitAdapter(this, playlistRailData.getEnveuVideoItemBeans(), "VIDEO", new ArrayList<>(), 0, this, baseCategory, playlistRailData);
//                            getBinding().listRecyclerview.setAdapter(commonPotraitAdapter);
//                        } else
//                            commonPotraitAdapter.notifydata(playlistRailData.getEnveuVideoItemBeans());
//
//                        mIsLoading = playlistRailData.getMaxContent() != commonPotraitAdapter.getItemCount();
//                    }
                    binding!!.listRecyclerview.scrollToPosition(mScrollY)
                }
            }
        }
    }

    private fun setUiComponents(playlistRailData: RailCommonData?) {
        if (playlistRailData!!.enveuVideoItemBeans != null) {
            if (playlistRailData.enveuVideoItemBeans.isNotEmpty()) {
                if (Objects.requireNonNull(baseCategory!!.contentImageType).equals(ImageType.CIR.name, ignoreCase = true)) {
                    commonCircleAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                    mIsLoading = playlistRailData.maxContent != commonCircleAdapter!!.itemCount
                } else if (baseCategory!!.contentImageType.equals(ImageType.SQR.name, ignoreCase = true)) {
                    if (squareCommonAdapter != null) {
                        squareCommonAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                        mIsLoading = playlistRailData.maxContent != squareCommonAdapter!!.itemCount
                    }
                } else if (baseCategory!!.contentImageType.equals(ImageType.PR1.name, ignoreCase = true)) {
                    commonPosterPotraitAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                    mIsLoading = playlistRailData.maxContent != commonPosterPotraitAdapter!!.itemCount
                } else if (baseCategory!!.contentImageType.equals(ImageType.PR2.name, ignoreCase = true)) {
                    commonPotraitTwoAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                    mIsLoading = playlistRailData.maxContent != commonPotraitTwoAdapter!!.itemCount
                } else if (baseCategory!!.contentImageType.equals(ImageType.LDS.name, ignoreCase = true)) {
                    if (commonLandscapeAdapter != null) {
                        commonLandscapeAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                        mIsLoading = playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
                    }
                } else if (baseCategory!!.contentImageType.equals(ImageType.LDS2.name, ignoreCase = true)) {
                    commonLandscapeAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                    mIsLoading = playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
                } else {
                    commonLandscapeAdapter!!.notifydata(playlistRailData.enveuVideoItemBeans)
                    mIsLoading = playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
                }
            } else {
                mIsLoading = false
            }
        } else {
            mIsLoading = false
        }
    }

    private fun noConnectionLayout() {
        binding!!.noConnectionLayout.visibility = View.VISIBLE
        binding!!.connection.retryTxt.setOnClickListener { connectionObserver() }
    }

    override fun onRowItemClicked(itemValue: EnveuVideoItemBean, position: Int) {
        //AppCommonMethod.redirectionLogic(this,railCommonData,position);
        Log.d("javed", "onRowItemClicked: $itemValue")
        var trailerReferenceId: String? = ""
        var isParentContentNull = false
        var isHosted = false
        var externalUrl: String? = ""
        if (itemValue.trailerReferenceId != null) {
            trailerReferenceId = itemValue.trailerReferenceId
        }
        var tittle: String? = ""
        if (itemValue.title != null) {
            tittle = itemValue.title
        }
        if (itemValue.parentContent == null) {
            isParentContentNull = true
        }
        val isPremium: Boolean = itemValue.isPremium
        var externalRefId: String? = ""
        if (itemValue.externalRefId != null) {
            externalRefId = itemValue.externalRefId
        }
        var skuId: String? = ""
        if (itemValue.sku != null) {
            skuId = itemValue.sku
        }
        var customContentType: String? = ""
        val assetType = itemValue.assetType
        var videoType: String? = ""
        if (itemValue.assetType.equals(AppConstants.VIDEO, ignoreCase = true)) {
            videoType = itemValue.videoDetails.videoType
        } else if (itemValue.assetType.equals(AppConstants.LIVE, ignoreCase = true)) {
            if (java.lang.Boolean.TRUE == itemValue.liveContent.isHosted) {
                isHosted = true
            } else {
                if (itemValue.liveContent.externalUrl != null) {
                    externalUrl = itemValue.liveContent.externalUrl
                }
            }
        } else {
            if (itemValue.assetType.equals(AppConstants.CUSTOM, ignoreCase = true)) {
                customContentType = itemValue.customContent.customType
            }
        }
        AppCommonMethod.launchDetailScreenFromSearch(
            this,
            assetType,
            itemValue.id,
            customContentType.toString(),
            videoType,
            trailerReferenceId,
            externalRefId.toString(),
            isPremium,
            skuId,
            isParentContentNull,
            tittle,
            isHosted,
            externalUrl.toString(),
            itemValue.posterURL
        )
    }


    override fun onFinishDialog() {
        if (isloggedout) {
            logoutCall()
        } else {
            onBackPressed()
        }
    }

    private fun showDialog(title: String, message: String) {
        val fm = supportFragmentManager
        val alertDialog = AlertDialogSingleButtonFragment.newInstance(
            title,
            message,
            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(), getString(R.string.popup_ok))
        )
        alertDialog.isCancelable = false
        alertDialog.setAlertDialogCallBack(this)
        alertDialog.show(fm, "fragment_alert")
    }

}