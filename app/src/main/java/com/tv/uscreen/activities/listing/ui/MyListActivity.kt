package com.tv.uscreen.activities.listing.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enveu.client.enums.ImageType
import com.enveu.client.watchHistory.beans.ResponseWatchHistoryAssetList
import com.google.gson.Gson
import com.tv.uscreen.Bookmarking.BookmarkingViewModel
import com.tv.uscreen.R
import com.tv.uscreen.activities.listing.callback.ItemClickListener
import com.tv.uscreen.activities.listing.listadapter.ListAdapter
import com.tv.uscreen.adapters.CommonShimmerAdapter
import com.tv.uscreen.baseModels.BaseBindingActivity
import com.tv.uscreen.beanModel.emptyResponse.ResponseEmpty
import com.tv.uscreen.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.databinding.FragmentMyListBinding
import com.tv.uscreen.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.fragments.dialog.CommonDialogFragment.Companion.newInstance
import com.tv.uscreen.utils.Logger
import com.tv.uscreen.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.utils.constants.AppConstants
import com.tv.uscreen.utils.helpers.RailInjectionHelper
import com.tv.uscreen.utils.helpers.RecyclerAnimator
import com.tv.uscreen.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.utils.stringsJson.converter.StringsHelper

class MyListActivity : BaseBindingActivity<FragmentMyListBinding?>(), ItemClickListener, CommonDialogFragment.EditDialogListener {
    private var token: String? = null
    private var railInjectionHelper: RailInjectionHelper? = null
    private var bookmarkingViewModel: BookmarkingViewModel? = null
    private var counterLimit = 1
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisibleItems = 0
    private var mIsLoading = false
    private var counter = 0
    private var isScrolling = false
    private var mScrollY = 0
    private var commonLandscapeAdapter: ListAdapter? = null
    private var mPreference: KsPreferenceKeys? = null
    private var notReload = true
    private var adapterPurchase: CommonShimmerAdapter? = null
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding!!.noResultFound.stringData = stringsHelper
        binding!!.colorsData = colorsHelper
        binding!!.stringData = stringsHelper
        binding!!.noResultFound.toolbar.colorsData = colorsHelper
        binding!!.noResultFound.toolbar.stringData = stringsHelper
        binding!!.toolbar.colorsData = colorsHelper
        binding!!.toolbar.stringData = stringsHelper
        binding!!.noResultFound.stringData = stringsHelper
        binding!!.noResultFound.colorsData = colorsHelper
        setToolbar()
    }

    private fun setToolbar() {
        binding!!.toolbar.logoMain2.visibility = View.GONE
        binding!!.toolbar.titleMid.visibility = View.GONE
        binding!!.toolbar.rlToolBar.visibility = View.GONE
        binding!!.toolbar.backLayout.visibility = View.VISIBLE
        val myList = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.my_list.toString(),
            getString(R.string.my_list)
        )
        binding!!.toolbar.titleMid.text = myList
        binding!!.toolbar.searchIcon.visibility = View.GONE
        binding!!.toolbar.titleMid.setBackgroundResource(0)
        binding!!.toolbar.backLayout.setOnClickListener { onBackPressed() }
    }

    override fun inflateBindingLayout(inflater: LayoutInflater): FragmentMyListBinding {
        return FragmentMyListBinding.inflate(inflater)
    }

    public override fun onResume() {
        super.onResume()
        if (notReload) {
            reloadMyListData()
        }
        if (KsPreferenceKeys.getInstance().isWatchListButtonClick) {
            KsPreferenceKeys.getInstance().isWatchListButtonClick = false
            loadData()
        }
    }

    private fun loadData() {
        Handler().postDelayed({
            binding!!.watchListRecyclerView.visibility = View.VISIBLE
            binding!!.noResultLayout.visibility = View.GONE
            adapterPurchase = null
            commonLandscapeAdapter = null
            counter = 0
            counterLimit = 1
            initialize()
            callShimmer(this, binding!!.watchListRecyclerView)
            watchListData
            swipeToRefresh()
            binding!!.watchListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    if (dy > 0) {
                        assert(layoutManager != null)
                        visibleItemCount = layoutManager!!.childCount
                        totalItemCount = layoutManager.itemCount
                        pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                        if (mIsLoading) {
                            if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                                val adapterSize = binding!!.watchListRecyclerView.adapter?.itemCount
                                if (adapterSize != null) {
                                    if (adapterSize > 8) {
                                        mIsLoading = false
                                        counter++
                                        isScrolling = true
                                        mScrollY += dy
                                        watchListData
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }, 100)
    }

    private fun swipeToRefresh() {
        binding!!.swipeContainer.setOnRefreshListener {
            if (commonLandscapeAdapter != null) {
                commonLandscapeAdapter!!.clear()
            }
            KsPreferenceKeys.getInstance().setIsFirstTimeCame(false)
            reloadMyListData()
            swipeToRefreshCheck()
        }
    }

    private fun swipeToRefreshCheck() {
        if (binding!!.swipeContainer.isRefreshing) {
            binding!!.swipeContainer.isRefreshing = false
        }
    }

    private fun reloadMyListData() {
        notReload = false
        Handler().postDelayed({
            binding!!.watchListRecyclerView.visibility = View.VISIBLE
            binding!!.noResultLayout.visibility = View.GONE
            adapterPurchase = null
            commonLandscapeAdapter = null
            counter = 0
            counterLimit = 1
            initialize()
            callShimmer(this, binding!!.watchListRecyclerView)
            watchListData
            swipeToRefresh()
            binding!!.watchListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    if (dy > 0) {
                        assert(layoutManager != null)
                        visibleItemCount = layoutManager!!.childCount
                        totalItemCount = layoutManager.itemCount
                        pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                        if (mIsLoading) {
                            if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                                val adapterSize = binding!!.watchListRecyclerView.adapter?.itemCount
                                if (adapterSize != null) {
                                    if (adapterSize > 8) {
                                        mIsLoading = false
                                        counter++
                                        isScrolling = true
                                        mScrollY += dy
                                        watchListData
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }, 100)
    }

    private fun initialize() {
        mPreference = KsPreferenceKeys.getInstance()
        bookmarkingViewModel = ViewModelProvider(this)[BookmarkingViewModel::class.java]
        railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
        binding!!.watchListRecyclerView.setHasFixedSize(true)
    }

    private fun callShimmer(context: Context, recyclerView: RecyclerView) {
        adapterPurchase = CommonShimmerAdapter()
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapterPurchase
    }

    private val watchListData: Unit
        get() {
            try {
                token = mPreference!!.appPrefAccessToken
                if (counter <= counterLimit - 1) {
                    bookmarkingViewModel!!.getMywatchListData(token, counter, AppConstants.PAGE_SIZE).observe(this) { responseWatchHistoryAssetList: ResponseWatchHistoryAssetList ->
                        if (responseWatchHistoryAssetList.isStatus) {
                            if (responseWatchHistoryAssetList.data != null) {
                                counterLimit = responseWatchHistoryAssetList.data.totalPages
                                var videoIds = ""
                                if (responseWatchHistoryAssetList.data != null) {
                                    val watchHistoryList = responseWatchHistoryAssetList.data.items
                                    for (historyItem in watchHistoryList) {
                                        videoIds = videoIds + historyItem.assetId.toString() + ","
                                    }
                                    railInjectionHelper!!.getWatchHistoryAssets(watchHistoryList, videoIds).observe(this) { railCommonData: RailCommonData? ->
                                        notReload = true
                                        if (railCommonData != null) {
                                            binding!!.watchListRecyclerView.visibility = View.VISIBLE
                                            binding!!.progressBar.visibility = View.GONE
                                            binding!!.noResultLayout.visibility = View.GONE
                                            setRail(railCommonData)
                                            val gson = Gson()
                                            val railCommon = gson.toJson(railCommonData)
                                            Log.d("Javed_MyList", "getWatchListData: $railCommon")
                                        } else {
                                            hideRecyclerViewData()
                                        }
                                    }
                                } else {
                                    hideRecyclerViewData()
                                }
                            } else {
                                hideRecyclerViewData()
                            }
                        } else {
                            hideRecyclerViewData()
                        }
                    }
                }
            } catch (e: Exception) {
                Logger.w(e)
            }
        }

    private fun hideRecyclerViewData() {
        binding!!.watchListRecyclerView.visibility = View.GONE
        binding!!.progressBar.visibility = View.GONE
        binding!!.toolbar.root.visibility = View.GONE
        binding!!.noResultLayout.visibility = View.VISIBLE
        binding!!.noResultFound.toolbar.backLayout.visibility = View.VISIBLE
        binding!!.noResultFound.toolbar.backLayout.visibility = View.VISIBLE
        binding!!.noResultFound.toolbar.logoMain2.visibility = View.GONE
        binding!!.noResultFound.toolbar.rlToolBar.visibility = View.GONE
        binding!!.noResultFound.toolbar.titleMid.setText(R.string.mu_list_title)
        binding!!.noResultFound.toolbar.titleMid.setBackgroundResource(0)
        binding!!.noResultFound.toolbar.backLayout.setOnClickListener { onBackPressed() }
    }

    private var railCommonData: RailCommonData? = null
    private fun setRail(playlistRailData: RailCommonData) {
        railCommonData = playlistRailData
        if (isScrolling) {
            setUiComponents(playlistRailData)
        } else {
            binding!!.progressBar.visibility = View.GONE
            mIsLoading = true
            if (commonLandscapeAdapter == null) {
                RecyclerAnimator(this).animate(binding!!.watchListRecyclerView)
                commonLandscapeAdapter = ListAdapter(playlistRailData.enveuVideoItemBeans, this, AppCommonMethod.getListViewType(ImageType.LDS.name), this)
                commonLandscapeAdapter!!.setWatchList()
                binding!!.watchListRecyclerView.adapter = commonLandscapeAdapter
            } else {
                commonLandscapeAdapter!!.clear()
                commonLandscapeAdapter!!.notifyAdapter(playlistRailData.enveuVideoItemBeans)
            }
            mIsLoading = playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
            binding!!.watchListRecyclerView.scrollToPosition(mScrollY)
        }
        binding!!.progressBar.visibility = View.GONE
    }

    private fun setUiComponents(playlistRailData: RailCommonData) {
        commonLandscapeAdapter!!.notifyAdapter(playlistRailData.enveuVideoItemBeans)
        mIsLoading = playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
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
            if (itemValue.liveContent.isHosted == true) {
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

    override fun onDeleteWatchListClicked(assetID: Int, tittle: String, position: Int) {
        AppCommonMethod.assetId = assetID
        commonDialog(
            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_remove.toString(), getString(R.string.popup_remove))
                    + " " + tittle,
            applicationContext.getString(R.string.delete_hunting),
            applicationContext.getString(R.string.continue_but)
        )
    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm = supportFragmentManager
        val commonDialogFragment = newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }

    override fun onActionBtnClicked() {
        binding!!.progressBar.visibility = View.VISIBLE
        bookmarkingViewModel!!.hitRemoveWatchlist(token, AppCommonMethod.assetId).observe(this) { responseEmpty: ResponseEmpty? ->
            if (responseEmpty != null && responseEmpty.isStatus) {
                Toast.makeText(this, getString(R.string.remove_video_from_list), Toast.LENGTH_SHORT).show()
                counter = 0
                commonLandscapeAdapter!!.clear()
                watchListData
            } else {
                binding!!.progressBar.visibility = View.GONE
            }
        }
    }
}