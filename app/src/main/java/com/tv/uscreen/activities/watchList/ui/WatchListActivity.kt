package com.tv.uscreen.activities.watchList.ui

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enveu.client.enums.ImageType
import com.enveu.client.watchHistory.beans.ResponseWatchHistoryAssetList
import com.google.gson.JsonObject

import com.tv.uscreen.Bookmarking.BookmarkingViewModel
import com.tv.uscreen.R
import com.tv.uscreen.activities.detail.ui.DetailActivity
import com.tv.uscreen.activities.detail.ui.EpisodeActivity
import com.tv.uscreen.activities.listing.callback.ItemClickListener
import com.tv.uscreen.activities.listing.listadapter.ListAdapter
import com.tv.uscreen.activities.series.ui.SeriesDetailActivity
import com.tv.uscreen.activities.watchList.adapter.WatchHistoryAdapter
import com.tv.uscreen.activities.watchList.adapter.WatchHistoryAdapter.WatchHistoryAdaperListener
import com.tv.uscreen.activities.watchList.adapter.WatchListAdapter
import com.tv.uscreen.activities.watchList.adapter.WatchListAdapter.DeleteWatchList
import com.tv.uscreen.activities.watchList.adapter.WatchListAdapter.WatchListAdaperListener
import com.tv.uscreen.activities.watchList.viewModel.WatchListViewModel
import com.tv.uscreen.adapters.CommonShimmerAdapter
import com.tv.uscreen.baseModels.BaseBindingActivity
import com.tv.uscreen.beanModel.emptyResponse.ResponseEmpty
import com.tv.uscreen.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.beanModel.watchHistory.ItemsItem
import com.tv.uscreen.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.databinding.WatchListActivityBinding
import com.tv.uscreen.fragments.dialog.AlertDialogFragment
import com.tv.uscreen.fragments.dialog.AlertDialogSingleButtonFragment
import com.tv.uscreen.utils.Logger
import com.tv.uscreen.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.utils.constants.AppConstants
import com.tv.uscreen.utils.cropImage.helpers.NetworkConnectivity
import com.tv.uscreen.utils.helpers.CheckInternetConnection
import com.tv.uscreen.utils.helpers.RailInjectionHelper
import com.tv.uscreen.utils.helpers.RecyclerAnimator
import com.tv.uscreen.utils.helpers.ToastHandler
import com.tv.uscreen.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.utils.stringsJson.converter.StringsHelper
import java.util.Objects

class WatchListActivity : BaseBindingActivity<WatchListActivityBinding?>(), WatchListAdaperListener, WatchHistoryAdaperListener, DeleteWatchList, AlertDialogFragment.AlertDialogListener,
    ItemClickListener, View.OnClickListener {
    private var counter = 0
    private var viewModel: WatchListViewModel? = null
    private var preference: KsPreferenceKeys? = null
    private var token: String? = null
    private var viewType: String? = null
    private var listAdapter: WatchListAdapter? = null
    private var historyAdapter: WatchHistoryAdapter? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var mLastClickTime: Long = 0
    private var loading = true
    private var deleteItem = false
    private var itemValue: com.tv.uscreen.beanModel.allWatchList.ItemsItem? = null
    private var adapterPurchase: CommonShimmerAdapter? = null
    private var isloggedout = false
    private var totalCount = 0
    private var isRailData = false
    private var counterLimit = 1
    private var bookmarkingViewModel: BookmarkingViewModel? = null
    private var railInjectionHelper: RailInjectionHelper? = null
    private var commonLandscapeAdapter: ListAdapter? = null
    private var mIsLoading = true
    private var isScrolling = false
    private var mScrollY = 0
    private var firstVisiblePosition = 0
    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var isWatchHistory = false
    private val colorsHelper by lazy { ColorsHelper }
    private val stringsHelper by lazy { StringsHelper }
    private fun callBinding() {
        viewModel = ViewModelProvider(this@WatchListActivity)[WatchListViewModel::class.java]
        bookmarkingViewModel = ViewModelProvider(this)[BookmarkingViewModel::class.java]
        railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
        binding!!.connection.retryTxt.setOnClickListener { connectionObserver() }
        binding!!.retryLoadData.setOnClickListener(this)
        modelCall()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callBinding()
    }

    private fun modelCall() {
        totalCount = 0
        loading = true
        listAdapter = WatchListAdapter(this@WatchListActivity, ArrayList(), this@WatchListActivity, this@WatchListActivity)
        historyAdapter = WatchHistoryAdapter(this@WatchListActivity, ArrayList(), this@WatchListActivity)
        setRecyclerProperties(binding!!.watchListRecycler)
        binding!!.toolbarWatchlist.backLayout.setOnClickListener { onBackPressed() }
        binding!!.toolbarWatchlist.backLayout.visibility = View.VISIBLE
        binding!!.toolbarWatchlist.titleMid.visibility = View.VISIBLE
        binding!!.toolbarWatchlist.titleMid.setText(R.string.my_history)
        connectionObserver()
    }

    private fun setRecyclerProperties(recyclerView: RecyclerView) {
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
    }

    private fun connectionObserver() {
        connectionValidation(NetworkConnectivity.isOnline(this))
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            bundleValue
        } else {
            noConnectionLayout()
        }
    }

    private val bundleValue: Unit
        private get() {
            if (intent.hasExtra("bundleId")) {
                var extras = intent.extras
                if (extras != null) {
                    extras = extras.getBundle("bundleId")
                    viewType = extras?.getString("viewType")
                    isWatchHistory = extras!!.getBoolean("isWatchHistory", false)
                }
            } else {
                throw IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All")
            }
            uiInitialisation()
        }

    private fun uiInitialisation() {
        counter = 0
        loading = true
        preference = KsPreferenceKeys.getInstance()
        token = preference?.appPrefAccessToken
        binding!!.noConnectionLayout.visibility = View.GONE
        binding!!.nodatafounmd.visibility = View.GONE
        //   getBinding().toolbar.screenText.setText(viewType);
        if (viewType.equals(resources.getString(R.string.my_history), ignoreCase = true)) {
            callShimmer(this@WatchListActivity, binding!!.watchListRecycler)
            watchHistoryList
        } else {
            callShimmer(this@WatchListActivity, binding!!.watchListRecycler)
            watchListData
        }
        binding!!.watchListRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                firstVisiblePosition = layoutManager?.findFirstVisibleItemPosition()!!
                if (dy > 0) {
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    if (mIsLoading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            val adapterSize = binding!!.watchListRecycler.adapter!!.itemCount
                            if (adapterSize > 8) {
                                mIsLoading = false
                                counter++
                                isScrolling = true
                                mScrollY += dy
                                watchHistoryList
                            }
                        }
                    }
                }
            }
        })
    }

    //
    private val watchListData: Unit
        private get() {
            try {
                token = preference!!.appPrefAccessToken
                if (counter <= counterLimit - 1) {
                    bookmarkingViewModel!!.getMywatchListData(token, counter, AppConstants.PAGE_SIZE).observe(this) { responseWatchHistoryAssetList: ResponseWatchHistoryAssetList? ->
                        if (responseWatchHistoryAssetList!!.isStatus) {
                            if (responseWatchHistoryAssetList.data != null) {
                                counterLimit = responseWatchHistoryAssetList.data.totalPages
                                var videoIds = ""
                                if (responseWatchHistoryAssetList.data != null) {
                                    val watchHistoryList = responseWatchHistoryAssetList.data.items
                                    for (historyItem: com.enveu.client.watchHistory.beans.ItemsItem in watchHistoryList) {
                                        videoIds = videoIds + historyItem.assetId.toString() + ","
                                    }
                                    railInjectionHelper!!.getWatchHistoryAssets(watchHistoryList, videoIds).observe(this, Observer { railCommonData ->
                                        if (railCommonData != null) {
                                            binding!!.watchListRecycler.visibility = View.VISIBLE
                                            setRail(railCommonData)
                                        } else {
                                            binding!!.watchListRecycler.visibility = View.GONE
                                            binding!!.nodatafounmd.visibility = View.VISIBLE
                                            binding!!.progressBar.visibility = View.GONE
                                        }
                                    })
                                } else {
                                    if (historyAdapter!!.itemCount <= 0) {
                                        binding!!.watchListRecycler.visibility = View.GONE
                                        binding!!.nodatafounmd.visibility = View.VISIBLE
                                        binding!!.progressBar.visibility = View.GONE
                                    }
                                }
                            } else {
                                //
                            }
                        } else {
                            if (responseWatchHistoryAssetList != null && responseWatchHistoryAssetList.responseCode == 4302) {
                                binding!!.watchListRecycler.visibility = View.GONE
                                binding!!.nodatafounmd.visibility = View.VISIBLE
                                binding!!.progressBar.visibility = View.GONE
                                logoutCall()
                            } else {
                                binding!!.watchListRecycler.visibility = View.GONE
                                binding!!.progressBar.visibility = View.GONE
                                binding!!.nodatafounmd.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }

    private fun logoutCall() {
        if (CheckInternetConnection.isOnline(Objects.requireNonNull(this))) {
            clearCredientials(preference)
            hitApiLogout(this, preference!!.appPrefAccessToken)
        } else {
            ToastHandler.getInstance().show(this@WatchListActivity, resources.getString(R.string.no_internet_connection))
        }
    }

    private fun noConnectionLayout() {
        binding!!.noConnectionLayout.visibility = View.VISIBLE
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

    override fun inflateBindingLayout(inflater: LayoutInflater): WatchListActivityBinding {
        return WatchListActivityBinding.inflate(inflater)
    }

    override fun onWatchHistoryItemClicked(itemValue: ItemsItem) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        try {
            if (itemValue.videoType.equals("SERIES", ignoreCase = true)) {
                ActivityLauncher.getInstance().detailScreen(this, DetailActivity::class.java, itemValue.id, "0", false)
            } else if (itemValue.videoType.equals("EPISODE", ignoreCase = true)) {
                ActivityLauncher.getInstance().episodeScreen(this, EpisodeActivity::class.java, itemValue.id, "0", false)
            } else {
                ActivityLauncher.getInstance().detailScreen(this, DetailActivity::class.java, itemValue.id, "0", false)
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    override fun onWatchListItemClicked(itemValue: com.tv.uscreen.beanModel.allWatchList.ItemsItem) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        if (itemValue.contentType.equals("SERIES", ignoreCase = true)) {
            ActivityLauncher.getInstance().seriesDetailScreen(this, SeriesDetailActivity::class.java, itemValue.contentId)
        } else {
            if (itemValue.assetType.equals("EPISODE", ignoreCase = true)) {
                ActivityLauncher.getInstance().episodeScreen(this, EpisodeActivity::class.java, itemValue.contentId, "0", false)
            } else {
                ActivityLauncher.getInstance().detailScreen(this, DetailActivity::class.java, itemValue.contentId, "0", false)
            }
        }
    }

    override fun onDeleteClick(itemValue: com.tv.uscreen.beanModel.allWatchList.ItemsItem) {
        try {
            deleteItem = true
            isloggedout = false
            this.itemValue = itemValue
            showDialog(
                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                resources.getString(R.string.delete_from_watchlist)
            )
        } catch (e: Exception) {
            Logger.i("WatchListAcivity", "less")
        }
    }

    private fun hitApiRemoveList(watchlistId: Int) {
        val requestParam = JsonObject()
        requestParam.addProperty(AppConstants.API_PARAM_WATCHLIST_ID, watchlistId)
        binding!!.progressBar.visibility = View.VISIBLE
        viewModel!!.hitApiRemoveWatchList(token, watchlistId.toString()).observe(this@WatchListActivity) { responseWatchList: ResponseEmpty ->
            binding!!.progressBar.visibility = View.GONE
            deleteItem = false
            if (Objects.requireNonNull(responseWatchList).isStatus) {
                listAdapter!!.deleteComment(itemValue!!.id)
                if (listAdapter!!.itemCount > 0) {
                } else {
                    binding!!.watchListRecycler.visibility = View.GONE
                    binding!!.nodatafounmd.visibility = View.VISIBLE
                }
                Logger.e("", "hitApiAddWatchList")
            } else {
                if (responseWatchList.responseCode == 401) {
                    isloggedout = true
                    deleteItem = false
                    showDialog(this@WatchListActivity.resources.getString(R.string.logged_out), resources.getString(R.string.you_are_logged_out))
                }
            }
        }
    }

    override fun onFinishDialog() {
        if (deleteItem) {
            hitApiRemoveList(itemValue!!.id)
        } else if (isloggedout) {
            forceLogout()
        }
    }

    private fun callShimmer(context: Context, recyclerView: RecyclerView) {
        adapterPurchase = CommonShimmerAdapter()
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapterPurchase
    }

    private fun forceLogout() {
        if (isloggedout) {
            isloggedout = false
            hitApiLogout(this@WatchListActivity, preference!!.appPrefAccessToken)
        }
    }

    val watchHistoryList: Unit
        get() {
            try {
                token = preference!!.appPrefAccessToken
                if (counter <= counterLimit - 1) {
                    bookmarkingViewModel!!.getWatchHistory(token, counter, AppConstants.PAGE_SIZE).observe(this) { responseWatchHistoryAssetList: ResponseWatchHistoryAssetList? ->
                        if (responseWatchHistoryAssetList!!.isStatus) {
                            if (responseWatchHistoryAssetList.data != null) {
                                counterLimit = responseWatchHistoryAssetList.data.totalPages
                                var videoIds = ""
                                if (responseWatchHistoryAssetList.data != null) {
                                    val watchHistoryList = responseWatchHistoryAssetList.data.items
                                    for (historyItem: com.enveu.client.watchHistory.beans.ItemsItem in watchHistoryList) {
                                        videoIds = videoIds + historyItem.assetId.toString() + ","
                                    }
                                    railInjectionHelper!!.getWatchHistoryAssets(watchHistoryList, videoIds).observe(
                                        this
                                    ) { railCommonData ->
                                        if (railCommonData != null) {
                                            binding!!.watchListRecycler.visibility = View.VISIBLE
                                            setRail(railCommonData)
                                        } else {
                                            binding!!.watchListRecycler.visibility = View.GONE
                                            binding!!.nodatafounmd.visibility = View.VISIBLE
                                            binding!!.progressBar.visibility = View.GONE
                                        }
                                    }
                                } else {
                                    if (historyAdapter!!.itemCount <= 0) {
                                        binding!!.watchListRecycler.visibility = View.GONE
                                        binding!!.nodatafounmd.visibility = View.VISIBLE
                                        binding!!.progressBar.visibility = View.GONE
                                    }
                                }
                            }
                        } else {
                            if (responseWatchHistoryAssetList != null && responseWatchHistoryAssetList.responseCode == 4302) {
                                binding!!.watchListRecycler.visibility = View.GONE
                                binding!!.nodatafounmd.visibility = View.VISIBLE
                                binding!!.progressBar.visibility = View.GONE
                                logoutCall()
                            } else {
                                binding!!.watchListRecycler.visibility = View.GONE
                                binding!!.progressBar.visibility = View.GONE
                                binding!!.nodatafounmd.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }
    private var railCommonData: RailCommonData? = null
    private fun setRail(playlistRailData: RailCommonData) {
        railCommonData = playlistRailData
        if (isScrolling) {
            setUiComponents(playlistRailData)
            binding!!.progressBar.visibility = View.GONE
        } else {
            binding!!.progressBar.visibility = View.GONE
            mIsLoading = true
            if (commonLandscapeAdapter == null) {
                RecyclerAnimator(this).animate(binding!!.watchListRecycler)
                commonLandscapeAdapter = ListAdapter(playlistRailData.enveuVideoItemBeans, this, AppCommonMethod.getListViewType(ImageType.LDS.name), this)
                if (viewType.equals(resources.getString(R.string.my_history), ignoreCase = true)) commonLandscapeAdapter!!.setWatchHistory() else commonLandscapeAdapter!!.setWatchList()
                binding!!.watchListRecycler.adapter = commonLandscapeAdapter
            } else {
                commonLandscapeAdapter!!.notifyAdapter(playlistRailData.enveuVideoItemBeans)
            }
            mIsLoading = playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
            binding!!.watchListRecycler.scrollToPosition(mScrollY)
            binding!!.progressBar.visibility = View.GONE
        }
    }

    private fun setUiComponents(playlistRailData: RailCommonData) {
        commonLandscapeAdapter!!.notifyAdapter(playlistRailData.enveuVideoItemBeans)
        mIsLoading = playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
    }

    fun setLoaderValue() {
        isRailData = true
        loading = counterLimit != counter
        counter += 1
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (viewModel != null) {
            viewModel!!.callCleared()
        }
    }

    override fun onRowItemClicked(itemValue: EnveuVideoItemBean, position: Int) {
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
            customContentType!!,
            videoType,
            trailerReferenceId,
            externalRefId!!,
            false,
            skuId,
            isParentContentNull,
            tittle,
            isHosted,
            externalUrl!!,
            itemValue.posterURL
        )
    }

    override fun onDeleteWatchHistoryClicked(assetId: Int, position: Int) {
        val fm = supportFragmentManager
        val alertDialog = AlertDialogFragment.newInstance(
            "",
            resources.getString(R.string.delete_from_watchhistory_message),
            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(), getString(R.string.popup_ok)),
            resources.getString(R.string.cancel)
        )
        alertDialog.setAlertDialogCallBack {
            binding!!.progressBar.visibility = View.VISIBLE
            binding!!.watchListRecycler.visibility = View.GONE
            bookmarkingViewModel!!.deleteFromWatchHistory(token, assetId).observe(
                this@WatchListActivity
            ) { bookmarkingResponse ->
                if (bookmarkingResponse != null) {
                    Toast.makeText(this@WatchListActivity, getString(R.string.delete_from_watchhistory), Toast.LENGTH_LONG).show()
                    counter = 0
                    commonLandscapeAdapter!!.clear()
                    watchHistoryList
                } else {
                    Toast.makeText(this@WatchListActivity, getString(R.string.delete_from_watchhistory_error), Toast.LENGTH_LONG).show()
                    binding!!.progressBar.visibility = View.GONE
                }
            }
        }
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert")
    }

    override fun onDeleteWatchListClicked(assetId: Int, tittle: String, position: Int) {
        val fm = supportFragmentManager
        val alertDialog = AlertDialogFragment.newInstance(
            tittle,
            resources.getString(R.string.delete_from_watchlist),
            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(), getString(R.string.popup_ok)),
            resources.getString(R.string.cancel)
        )
        alertDialog.setAlertDialogCallBack {
            binding!!.progressBar.visibility = View.VISIBLE
            bookmarkingViewModel!!.hitRemoveWatchlist(token, assetId).observe(
                this@WatchListActivity
            ) { responseEmpty ->
                if (responseEmpty != null && responseEmpty.isStatus) {
                    Toast.makeText(this@WatchListActivity, getString(R.string.delete_from_watch_list_message), Toast.LENGTH_LONG).show()
                    counter = 0
                    commonLandscapeAdapter!!.clear()
                    watchListData
                } else {
                    Toast.makeText(this@WatchListActivity, getString(R.string.delete_from_watch_list_message_error), Toast.LENGTH_LONG).show()
                    binding!!.progressBar.visibility = View.GONE
                }
            }
        }
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert")
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.retry_load_data -> {
                counter = 0
                counterLimit = 1
                binding!!.progressBar.visibility = View.VISIBLE
                connectionObserver()
            }
        }
    }
}