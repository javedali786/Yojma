package com.breadgangtvnetwork.activities.search.ui

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.activities.detail.ui.DetailActivity
import com.breadgangtvnetwork.activities.search.adapter.CommonSearchAdapter.CommonSearchListener
import com.breadgangtvnetwork.activities.search.adapter.RowSearchAdapter
import com.breadgangtvnetwork.activities.search.adapter.RowSearchAdapter.RowSearchListener
import com.breadgangtvnetwork.activities.series.ui.SeriesDetailActivity
import com.breadgangtvnetwork.adapters.CommonShimmerAdapter
import com.breadgangtvnetwork.baseModels.BaseBindingActivity
import com.breadgangtvnetwork.beanModel.enveuCommonRailData.RailCommonData
import com.breadgangtvnetwork.beanModel.popularSearch.ItemsItem
import com.breadgangtvnetwork.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.breadgangtvnetwork.databinding.ActivityResultBinding
import com.breadgangtvnetwork.utils.Logger
import com.breadgangtvnetwork.utils.MediaTypeConstants
import com.breadgangtvnetwork.utils.ObjectHelper
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod
import com.breadgangtvnetwork.utils.constants.AppConstants
import com.breadgangtvnetwork.utils.helpers.NetworkConnectivity
import com.breadgangtvnetwork.utils.helpers.RailInjectionHelper
import com.breadgangtvnetwork.utils.helpers.RecyclerAnimator
import com.breadgangtvnetwork.utils.helpers.intentlaunchers.ActivityLauncher
import com.breadgangtvnetwork.utils.stringsJson.converter.StringsHelper
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties

class ActivityResults : BaseBindingActivity<ActivityResultBinding?>(), CommonSearchListener, RowSearchListener {
    private var searchKeyword: String? = null
    private var searchType: String? = null
    private var customContentType: String? = null
    private var videoType: String? = null
    private var header: String? = null
    private var mLayoutManager: LinearLayoutManager? = null
    private var loading = true
    private var singleSectionItems: List<EnveuVideoItemBean>? = null
    private var itemListDataAdapter1: RowSearchAdapter? = null
    private val railCommonData1: RailCommonData? = null
    private var counter = 0
    private var isLastPage = false
    private var totalCount = 0
    private var mLastClickTime: Long = 0
    private var firstVisiblePosition = 0
    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var railInjectionHelper: RailInjectionHelper? = null
    private var applyFilter = false
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ThemeHandler.getInstance().applySearchResult(getApplicationContext(),getBinding());
        binding?.toolbar?.colorsData = colorsHelper
        binding?.toolbar?.colorsData = colorsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
        bundleValue
        modelCall()
        val properties = Properties()
        properties.addAttribute(AppConstants.SEARCH_SEE_ALL, AppConstants.SEARCH_SEE_ALL)
        MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties)
    }

    private fun modelCall() {
        connectionObserver()
    }

    private val bundleValue: Unit
        private get() {
            mLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            if (intent.hasExtra("SearchResult")) {
                var extras = intent.extras
                if (extras != null) {
                    try {
                        extras = extras.getBundle("SearchResult")
                        searchType = extras?.getString("assetType")
                        customContentType = extras?.getString("customContentType")
                        header = extras?.getString("header")
                        Log.d("main", "getBundleValue: $searchType $customContentType")
                        videoType = extras?.getString("videoType")
                        totalCount = extras!!.getInt("Total_Result")
                        applyFilter = extras.getBoolean("apply_filter")
                        searchKeyword = extras.getString("Search_Key")
                    } catch (e: NullPointerException) {
                        Logger.w(e)
                    }
                }
            } else {
                throw IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All")
            }
            setHeader()
        }

    private fun setHeader() {
        if (searchType.equals(AppConstants.VIDEO, ignoreCase = true)) {
            if (header.equals(getString(R.string.search_result), ignoreCase = true)) {
                binding!!.toolbar.titleMid.text = getString(R.string.search_result) + "-" + totalCount + " " + getString(R.string.search_results)
            }
        }
    }

    private fun connectionObserver() {
        connectionValidation(NetworkConnectivity.isOnline(this))
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            uiInitialisation()
        } else {
            noConnectionLayout()
        }
    }

    private fun uiInitialisation() {
        railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
        binding!!.noConnectionLayout.visibility = View.GONE
        binding!!.root.visibility = View.VISIBLE
        binding!!.toolbar.logoMain2.visibility =View.GONE
        binding!!.toolbar.logoMain2.visibility =View.GONE
        binding!!.toolbar.titleMid.visibility =View.VISIBLE
        binding!!.toolbar.llSearchIcon.visibility =View.GONE
        binding!!.progressBar.visibility = View.VISIBLE
        counter = 0
        loading = true
        singleSectionItems = ArrayList()
        itemListDataAdapter1 = RowSearchAdapter(this@ActivityResults, singleSectionItems, false, this, totalCount, customContentType, videoType)
        setRecyclerProperties(binding!!.resultRecycler)
        callShimmer(binding!!.resultRecycler)
        hitApiSearchKeyword(searchKeyword, searchType, applyFilter, customContentType, videoType, header)
        recyclerViewScroll()
        localizeHeader(searchType)
        binding!!.toolbar.backLayout.setOnClickListener { onBackPressed() }
    }

    private fun localizeHeader(searchType: String?) {
        binding!!.toolbar.titleMid.isAllCaps = true
        if (searchType.equals(MediaTypeConstants.getInstance().episode, ignoreCase = true)) {
            binding!!.toolbar.titleMid.text =
                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.detail_page_episodes.toString(), getString(R.string.detail_page_episodes))
        } else if (ObjectHelper.isSame(searchType, AppConstants.SEARCH_TYPE_PROGRAM)) {
            binding!!.toolbar.titleMid.text = getString(R.string.heading_program)
        }
    }

    private fun recyclerViewScroll() {
        binding!!.resultRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                try {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    firstVisiblePosition = layoutManager?.findFirstVisibleItemPosition()!!
                    if (dy > 0) {
                        visibleItemCount = layoutManager.childCount
                        totalItemCount = layoutManager.itemCount
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                        if (loading) {
                            if (!isLastPage) {
                                loading = false
                                counter += AppConstants.PAGE_SIZE
                                binding!!.progressBar.visibility = View.VISIBLE
                                hitApiSearchKeyword(searchKeyword, searchType, applyFilter, customContentType, videoType, header)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Logger.e("ListActivity", "" + e)
                }
            }
        })
    }

    private fun setRecyclerProperties(recyclerView: RecyclerView) {
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(true)
        mLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = mLayoutManager
    }

    private var railCommonData: RailCommonData? = null
    private fun hitApiSearchKeyword(keyword: String?, type: String?, applyFilter: Boolean, customContentType: String?, videoType: String?, header: String?) {
        railInjectionHelper!!.getSearchSingleCategory(
            keyword, type, AppConstants.PAGE_SIZE,
            counter, applyFilter, customContentType, videoType, header
        ).observe(this@ActivityResults) { data: RailCommonData? ->
            if (data != null) {
                if (counter == 0) {
                    RecyclerAnimator(this).animate(binding!!.resultRecycler)
                    binding!!.resultRecycler.adapter = itemListDataAdapter1
                }
                railCommonData = data
                singleSectionItems = data.enveuVideoItemBeans
                itemListDataAdapter1!!.notifyAdapter(singleSectionItems)
                loading = true
                if (itemListDataAdapter1!!.itemCount == totalCount) isLastPage = true
            }
            binding!!.progressBar.visibility = View.GONE
        }
    }

    private fun callShimmer(recyclerView: RecyclerView) {
        val adapterPurchase = CommonShimmerAdapter()
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = adapterPurchase
    }

    private fun noConnectionLayout() {
        binding!!.root.visibility = View.GONE
        binding!!.noConnectionLayout.visibility = View.VISIBLE
        binding!!.connection.retryTxt.setOnClickListener { connectionObserver() }
    }

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityResultBinding {
        return ActivityResultBinding.inflate(inflater)
    }

    override fun onItemClicked(itemValue: ItemsItem) {
        if (itemValue.type.equals(MediaTypeConstants.VIDEO, ignoreCase = true)) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            ActivityLauncher.getInstance().seriesDetailScreen(this@ActivityResults, SeriesDetailActivity::class.java, itemValue.id)
        } else if (itemValue.type.equals(AppConstants.CUSTOM, ignoreCase = true)) {
            Logger.e("ActivityResults", "Season click")
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            ActivityLauncher.getInstance().seriesDetailScreen(this@ActivityResults, SeriesDetailActivity::class.java, itemValue.id)
        } else {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            ActivityLauncher.getInstance().detailScreen(this@ActivityResults, DetailActivity::class.java, itemValue.id, "0", false)
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
            customContentType!!,
            videoType,
            trailerReferenceId,
            externalRefId!!,
            isPremium,
            skuId,
            isParentContentNull,
            tittle,
            isHosted,
            externalUrl!!,
            itemValue.posterURL
        )

        // AppCommonMethod.redirectionLogic(this,railCommonData,position);
    }


}