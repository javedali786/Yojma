package com.tv.uscreen.yojmatv.activities.listing.listui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory
import com.tv.uscreen.yojmatv.activities.listing.callback.ItemClickListener
import com.tv.uscreen.yojmatv.activities.listing.listadapter.ListAdapter
import com.tv.uscreen.yojmatv.adapters.CommonShimmerAdapter
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.databinding.ListingActivityBinding

import com.tv.uscreen.yojmatv.networking.apistatus.APIStatus
import com.tv.uscreen.yojmatv.networking.responsehandler.ResponseModel
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.cropImage.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.RecyclerAnimator
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import java.util.Objects

class ListActivity : BaseBindingActivity<ListingActivityBinding?>(), ItemClickListener {
    var playListId: String? = null
    var baseCategory: BaseCategory? = null
    private var counter = 0
    private var flag = 0
    private var firstVisiblePosition = 0
    private var pastVisiblesItems = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var commonLandscapeAdapter: ListAdapter? = null
    private var gridLayoutManager: LinearLayoutManager? = null
    private var title: String? = null
    private var mIsLoading = true
    private var isScrolling = false
    private var mScrollY = 0
    private var listData: RailCommonData? = null
    override fun inflateBindingLayout(inflater: LayoutInflater): ListingActivityBinding {
        return ListingActivityBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding!!.toolbarGrid.colorsData = ColorsHelper
        binding!!.toolbarGrid.stringData = StringsHelper
        binding!!.colorsData = ColorsHelper
        title = intent.getStringExtra("title")
        playListId = intent.getStringExtra("playListId")
        flag = intent.getIntExtra("flag", 0)
        val shimmerType = intent.getIntExtra("shimmerType", 0)
        baseCategory = intent.extras!!.getParcelable("baseCategory")
        Logger.d("ValueForImageTYpe " + baseCategory!!.widgetImageType)
        uiIntilization()
        connectionObserver()
    }

    private fun connectionObserver() {
        if (NetworkConnectivity.isOnline(this@ListActivity)) {
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
            railData
        } else {
            noConnectionLayout()
        }
    }

    private fun callShimmer() {
        val adapterPurchase = CommonShimmerAdapter()
        gridLayoutManager = LinearLayoutManager(this)
        binding!!.listRecyclerview.itemAnimator = DefaultItemAnimator()
        binding!!.listRecyclerview.layoutManager = gridLayoutManager
        binding!!.listRecyclerview.adapter = adapterPurchase
        //SeparatorDecoration decoration = new SeparatorDecoration(this, getResources().getColor(R.color.home_screen_separator), 0.9f);
        //getBinding().listRecyclerview.addItemDecoration(decoration);
        binding!!.listRecyclerview.visibility = View.VISIBLE
    }

    private fun uiIntilization() {
        binding!!.toolbarGrid.backLayout.setOnClickListener { onBackPressed() }
        binding!!.listRecyclerview.layoutManager = gridLayoutManager
        binding!!.toolbarGrid.titleMid.visibility = View.VISIBLE
        binding!!.toolbarGrid.logoMain2.visibility = View.GONE
        binding!!.toolbarGrid.searchIcon.visibility = View.GONE
        binding!!.toolbarGrid.titleMid.text = title
        binding!!.toolbarGrid.backLayout.visibility = View.VISIBLE
        binding!!.toolbarGrid.titleMid.visibility = View.VISIBLE
        binding!!.toolbarGrid.titleSkip.visibility = View.GONE
        binding!!.listRecyclerview.hasFixedSize()
        binding!!.listRecyclerview.isNestedScrollingEnabled = false
    }

    private fun setClicks() {
        binding!!.listRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                try {
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
                } catch (e: Exception) {
                    Logger.e("ListActivity", "" + e)
                }
            }
        })
    }

    @get:RequiresApi(api = Build.VERSION_CODES.M)
    private val railData: Unit
        private get() {
            if (flag == 0) {
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
            } else {
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
                RecyclerAnimator(this).animate(binding!!.listRecyclerview)
                commonLandscapeAdapter = ListAdapter(playlistRailData.enveuVideoItemBeans, this, AppCommonMethod.getListViewType(baseCategory!!.contentImageType.toString()), this)
                binding!!.listRecyclerview.adapter = commonLandscapeAdapter
            } else commonLandscapeAdapter!!.notifyAdapter(playlistRailData.enveuVideoItemBeans)
            mIsLoading = playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
            binding!!.listRecyclerview.scrollToPosition(mScrollY)
        }
    }

    private fun setUiComponents(playlistRailData: RailCommonData) {
        mIsLoading = if (playlistRailData.enveuVideoItemBeans.size > 0) {
            commonLandscapeAdapter!!.notifyAdapter(playlistRailData.enveuVideoItemBeans)
            playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
        } else {
            false
        }
    }

    private fun noConnectionLayout() {
        binding!!.noConnectionLayout.visibility = View.VISIBLE
        binding!!.connection.retryTxt.setOnClickListener { connectionObserver() }
    }

    override fun onRowItemClicked(itemValue: EnveuVideoItemBean, position: Int) {
        try {
            AppCommonMethod.trackFcmEvent(itemValue.title, itemValue.assetType, this@ListActivity, position)
        } catch (e: Exception) {
            Logger.d("onRowItemClicked $e")
        }
        if (railCommonData != null) {
            AppCommonMethod.redirectionLogic(this, railCommonData!!, position)
        }
    }
}