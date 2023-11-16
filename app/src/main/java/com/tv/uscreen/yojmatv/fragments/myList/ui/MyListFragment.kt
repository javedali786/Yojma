package com.tv.uscreen.yojmatv.fragments.myList.ui

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enveu.client.enums.ImageType
import com.enveu.client.watchHistory.beans.ResponseWatchHistoryAssetList
import com.tv.uscreen.yojmatv.Bookmarking.BookmarkingViewModel
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.activities.listing.callback.ItemClickListener
import com.tv.uscreen.yojmatv.activities.listing.listadapter.ListAdapter
import com.tv.uscreen.yojmatv.adapters.CommonShimmerAdapter
import com.tv.uscreen.yojmatv.baseModels.BaseBindingFragment
import com.tv.uscreen.yojmatv.beanModel.emptyResponse.ResponseEmpty
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.databinding.FragmentMyListBinding
import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogFragment
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.RecyclerAnimator
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import java.util.Objects

class MyListFragment : BaseBindingFragment<FragmentMyListBinding?>(), ItemClickListener {
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

    override fun inflateBindingLayout(inflater: LayoutInflater): FragmentMyListBinding {
        return FragmentMyListBinding.inflate(inflater)
    }

    override fun onResume() {
        super.onResume()
        if (notReload) {
            reloadMyListData()
        }
    }

    private fun reloadMyListData() {
        // make notReload before Handler
        notReload = false
        Handler().postDelayed({
            binding!!.watchListRecyclerView.visibility = View.VISIBLE
            binding!!.noResultLayout.visibility = View.GONE
            binding?.connection?.colorsData = colorsHelper
            binding?.connection?.stringData = stringsHelper
            binding!!.noResultFound.colorsData = ColorsHelper
            adapterPurchase = null
            commonLandscapeAdapter = null
            counter = 0
            counterLimit = 1
            //   ThemeHandler.getInstance().applyMyListing(requireContext(), getBinding(), null);
            initialize()
            callShimmer(requireContext(), binding!!.watchListRecyclerView)
            watchListData
            binding!!.watchListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                    //                    firstVisiblePosition = Objects.requireNonNull(layoutManager).findFirstVisibleItemPosition();
                    if (dy > 0) {
                        assert(layoutManager != null)
                        visibleItemCount = layoutManager!!.childCount
                        totalItemCount = layoutManager.itemCount
                        pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                        if (mIsLoading) {
                            if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                                val adapterSize = Objects.requireNonNull(binding!!.watchListRecyclerView.adapter).itemCount
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
        binding!!.noResultLayout.visibility = View.VISIBLE
    }

    private fun setRail(playlistRailData: RailCommonData) {
        if (isScrolling) {
            setUiComponents(playlistRailData)
        } else {
            binding!!.progressBar.visibility = View.GONE
            mIsLoading = true
            if (commonLandscapeAdapter == null) {
                RecyclerAnimator(requireActivity()).animate(binding!!.watchListRecyclerView)
                commonLandscapeAdapter = ListAdapter(playlistRailData.enveuVideoItemBeans, this, AppCommonMethod.getListViewType(ImageType.LDS.name), context)
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

    private var railCommonData: RailCommonData? = null
    private fun setUiComponents(playlistRailData: RailCommonData) {
        railCommonData = playlistRailData
        commonLandscapeAdapter!!.notifyAdapter(playlistRailData.enveuVideoItemBeans)
        mIsLoading = playlistRailData.maxContent != commonLandscapeAdapter!!.itemCount
    }

    override fun onRowItemClicked(itemValue: EnveuVideoItemBean, position: Int) {
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
        } else {
            if (itemValue.assetType.equals(AppConstants.CUSTOM, ignoreCase = true)) {
                customContentType = itemValue.customType
            }
        }
        AppCommonMethod.launchDetailScreenFromSearch(
            requireContext(),
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
            itemValue.posterURL,
            itemValue.videoDetails.audioTracks
        )
    }

    override fun onDeleteWatchListClicked(assetId: Int, tittle: String, position: Int) {
        val fm = requireActivity().supportFragmentManager
        val alertDialog = AlertDialogFragment.newInstance(
            tittle,
            resources.getString(R.string.delete_from_watchlist),
            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(), getString(R.string.popup_ok)),
            resources.getString(R.string.cancel)
        )
        alertDialog.setAlertDialogCallBack {
            binding!!.progressBar.visibility = View.VISIBLE
            bookmarkingViewModel!!.hitRemoveWatchlist(token, assetId).observe(requireActivity()) { responseEmpty: ResponseEmpty? ->
                if (responseEmpty != null && responseEmpty.isStatus) {
                    Toast.makeText(requireActivity(), getString(R.string.delete_from_watch_list_message), Toast.LENGTH_SHORT).show()
                    counter = 0
                    commonLandscapeAdapter!!.clear()
                    watchListData
                } else {
                    Toast.makeText(requireActivity(), getString(R.string.delete_from_watch_list_message_error), Toast.LENGTH_SHORT).show()
                    binding!!.progressBar.visibility = View.GONE
                }
            }
        }
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert")
    }
}