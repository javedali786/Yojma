package com.tv.uscreen.yojmatv.fragments.player.ui

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.activities.detail.ui.EpisodeActivity
import com.tv.uscreen.yojmatv.activities.series.adapter.SeasonAdapter
import com.tv.uscreen.yojmatv.activities.series.ui.SeriesDetailActivity
import com.tv.uscreen.yojmatv.baseModels.BaseBindingFragment
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModel.selectedSeason.SelectedSeasonModel
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.FirstEpisodeItem
import com.tv.uscreen.yojmatv.databinding.SeasonFragmentLayoutBinding

import com.tv.uscreen.yojmatv.networking.apistatus.APIStatus
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.ObjectHelper
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.RecyclerAnimator
import com.tv.uscreen.yojmatv.utils.helpers.SpacingItemDecoration
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import java.util.Arrays

class SeasonTabFragment : BaseBindingFragment<SeasonFragmentLayoutBinding?>(), SeasonAdapter.EpisodeItemClick, FirstEpisodeItem {
    private val stringsHelper by lazy { StringsHelper }
    private var railInjectionHelper: RailInjectionHelper? = null
    private var seriesId = 0
    private var seasonCount = 0
    var selectedSeason = 0
    private var seasonArray: ArrayList<*>? = null
    private val seasonNameList = ArrayList<String?>()
    private val bundle: Bundle? = null
    private var context: Context? = null
    private var seasonList: ArrayList<SelectedSeasonModel>? = null
    private var currentAssetId = 0
    var seasonAdapter: SeasonAdapter? = null
    private var seasonEpisodes: MutableList<EnveuVideoItemBean> = ArrayList()
    private var allEpiosdes: MutableList<EnveuVideoItemBean> = ArrayList()
    private var mLastClickTime: Long = 0
    private var railCommonData: RailCommonData? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onDetach() {
        super.onDetach()
        context = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun inflateBindingLayout(inflater: LayoutInflater): SeasonFragmentLayoutBinding {
        return SeasonFragmentLayoutBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.stringData = StringsHelper
        binding!!.colorsData = ColorsHelper
        Logger.d("ON VIEW CREATED-----", "TRUE")
        val bundle = arguments
        seasonArray = ArrayList<Any?>()
        val seasonArray1: ArrayList<*>? = bundle!!.getParcelableArrayList<Parcelable>(AppConstants.BUNDLE_SEASON_ARRAY)
        if (seasonArray1 != null) {
            for (i in seasonArray1.indices) {
                val value = seasonArray1[i] as Double
                Logger.d("seasonArray: $value")
                val v = value.toInt()
                Logger.d("seasonArray: $v")
                (seasonArray as ArrayList<Any?>).add(v)
            }
        }
        Logger.d("seasonArray: $seasonArray")
        val seasonName = bundle.getString(AppConstants.BUNDLE_SEASON_NAME)
        if (ObjectHelper.isNotEmpty(seasonName)) {
            seasonNameList.addAll(Arrays.asList(*seasonName!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()))
        }
        Logger.d("season name: $seasonNameList")
        try {
            getVideoRails(bundle)
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    fun getVideoRails(bundle: Bundle?) {
        if (bundle != null) {
            binding!!.seasonHeader.visibility = View.GONE
            seriesId = bundle.getInt(AppConstants.BUNDLE_ASSET_ID)
            seasonCount = bundle.getInt(AppConstants.BUNDLE_SEASON_COUNT)
            currentAssetId = bundle.getInt(AppConstants.BUNDLE_CURRENT_ASSET_ID)
            if (seasonCount > 0) {
                seasonList = ArrayList()
                selectedSeason = 1
                val tempSeaon = bundle.getInt(AppConstants.BUNDLE_SELECTED_SEASON)
                if (context is EpisodeActivity && tempSeaon > 0) selectedSeason = tempSeaon
                if (context is SeriesDetailActivity) {
                    if (seasonArray != null && !seasonArray!!.isEmpty()) {
                        selectedSeason = seasonArray!![0] as Int
                    }
                }
            }
            binding!!.seasonHeader.isEnabled = false
            if (seriesId == -1) {
                binding!!.seasonHeader.visibility = View.VISIBLE
                binding!!.headerSeason.visibility = View.VISIBLE
                val allEpisodesText = stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.detail_page_all_episode.toString(),
                    getString(R.string.detail_page_all_episode)
                )
                binding!!.seasonHeader.text = allEpisodesText
                binding!!.seasonHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                binding!!.comingSoon.visibility = View.VISIBLE
                binding!!.seriesRecyclerView.visibility = View.GONE
                binding!!.seasonMore.visibility = View.GONE
                hideProgressBar()
            } else {
                episodeList
            }
        }
        binding!!.seasonMore.setOnClickListener { v: View? ->
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
                return@setOnClickListener
            }
            binding!!.seasonMore.visibility = View.GONE
            binding!!.progressBar.visibility = View.VISIBLE
            mLastClickTime = SystemClock.elapsedRealtime()
            totalPages++
            if (seasonCount > 0) {
                getSeasonEpisodes()
            } else {
                //here -1 indicates not to send seasonNumber key
                allEpisodes
            }
        }
        binding!!.seasonHeader.setOnClickListener { v: View? ->
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            if (seasonList != null) seasonList!!.clear()
            if (seasonList == null) {
                seasonList = ArrayList()
            }
            for (i in seasonArray!!.indices) {
                val isSelected = selectedSeason == seasonArray!![i] as Int
                val seasonName = getSeasonName(i)
                if (ObjectHelper.isEmpty(seasonName)) {
                    seasonList!!.add(SelectedSeasonModel(resources.getString(R.string.season) + " " + seasonArray!![i], seasonArray!![i] as Int, isSelected))
                } else {
                    seasonList!!.add(SelectedSeasonModel(seasonName, seasonArray!![i] as Int, isSelected))
                }
            }
            if (seasonArray!!.size > 1) {
                if (context is SeriesDetailActivity) {
                    (context as SeriesDetailActivity).showSeasonList(seasonList!!, selectedSeason + 1)
                } else if (context is EpisodeActivity) {
                    (context as EpisodeActivity).showSeasonList(seasonList!!, selectedSeason + 1)
                }
            }
        }
    }

    private fun getSeasonName(index: Int): String? {
        return if (index > -1 && ObjectHelper.isNotEmpty(seasonNameList) && ObjectHelper.getSize(seasonNameList) > index
        ) {
            seasonNameList[index]
        } else null
    }

    fun hideProgressBar() {
        if (context is SeriesDetailActivity) {
            (context as SeriesDetailActivity).isSeasonData = true
            (context as SeriesDetailActivity).stopShimmer()
            (context as SeriesDetailActivity).dismissLoading((context as SeriesDetailActivity).binding!!.progressBar)
            /*if (seasonAdapter != null) {
                ((SeriesDetailActivity) context).numberOfEpisodes(seasonAdapter.getItemCount());
            }*/
        } else if (context is EpisodeActivity) {
            (context as EpisodeActivity).dismissLoading((context as EpisodeActivity).binding!!.progressBar)
            (context as EpisodeActivity).isSeasonData = true
            (context as EpisodeActivity).stopShimmercheck()
        }
    }

    //here -1 indicates not to send seasonNumber key
    private val episodeList: Unit
        private get() {
            binding!!.seriesRecyclerView.addItemDecoration(
                SpacingItemDecoration(
                    8,
                    SpacingItemDecoration.HORIZONTAL
                )
            )
            railInjectionHelper = ViewModelProvider(this).get(RailInjectionHelper::class.java)
            Logger.d("seasonCount-->> $seasonCount")
            if (seasonCount > 0) {
                getSeasonEpisodes()
            } else {
                //here -1 indicates not to send seasonNumber key
                allEpisodes
            }
        }

    // all episode view to set here
    val allEpisodes: Unit
        get() {
            railInjectionHelper!!.getEpisodeNoSeasonV2(seriesId, totalPages, 50, -1).observe(requireActivity()) { response ->
                hideProgressBar()
                if (response != null) {
                    if (response.status.equals(APIStatus.START.name, ignoreCase = true)) {
                    } else if (response.status.equals(APIStatus.SUCCESS.name, ignoreCase = true)) {
                        if (response.baseCategory != null) {
                            val enveuCommonResponse = response.baseCategory as RailCommonData
                            railCommonData = enveuCommonResponse
                            binding!!.comingSoon.visibility = View.GONE
                            binding!!.progressBar.visibility = View.GONE
                            if (!StringUtils.isNullOrEmptyOrZero(enveuCommonResponse.seasonName)) {
                            } else {
                                // all episode view to set here
                                if (enveuCommonResponse.pageTotal - 1 > totalPages) {
                                    binding!!.seasonMore.visibility = View.VISIBLE
                                } else {
                                    binding!!.seasonMore.visibility = View.GONE
                                }
                                binding!!.seasonHeader.visibility = View.VISIBLE
                                binding!!.headerSeason.visibility = View.VISIBLE
                                binding!!.seasonHeader.isEnabled = false
                                if (seasonAdapter == null) {
                                    allEpiosdes = enveuCommonResponse.enveuVideoItemBeans
                                    val allEpisodesText = stringsHelper.stringParse(
                                        stringsHelper.instance()?.data?.config?.detail_page_all_episode.toString(),
                                        getString(R.string.detail_page_all_episode)
                                    )
                                    binding!!.seasonHeader.text = allEpisodesText
                                    binding!!.seasonHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                                    RecyclerAnimator(activity).animate(binding!!.seriesRecyclerView)
                                    seasonAdapter = SeasonAdapter(requireActivity(), allEpiosdes, seriesId, currentAssetId, this@SeasonTabFragment, this@SeasonTabFragment)
                                    updateSeasonEpisodeNumber(-1, seasonAdapter!!.currentEpisodeNumber)
                                    binding!!.seriesRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                                    (binding!!.seriesRecyclerView.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
                                    binding!!.seriesRecyclerView.adapter = seasonAdapter
                                } else {
                                    allEpiosdes.addAll(enveuCommonResponse.enveuVideoItemBeans)
                                    seasonAdapter!!.notifyDataSetChanged()
                                }
                                hideProgressBar()
                                if (context is EpisodeActivity) {
                                    (context as EpisodeActivity).episodesList(allEpiosdes)
                                } else if (context is SeriesDetailActivity) {
                                    (context as SeriesDetailActivity).episodesList(allEpiosdes)
                                }
                            }
                        }
                    } else if (response.status.equals(APIStatus.ERROR.name, ignoreCase = true)) {
                        if (response.errorModel.errorCode != 0) {
                            if (context is SeriesDetailActivity) {
                                (context as SeriesDetailActivity).episodesList(null)
                            }
                            binding!!.seasonHeader.visibility = View.GONE
                            binding!!.headerSeason.visibility = View.GONE
                            binding!!.progressBar.visibility = View.GONE
                            binding!!.comingSoon.visibility = View.VISIBLE
                            binding!!.seriesRecyclerView.visibility = View.GONE
                            binding!!.seasonMore.visibility = View.GONE
                        }
                    } else if (response.status.equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                        binding!!.seasonHeader.visibility = View.GONE
                        binding!!.headerSeason.visibility = View.GONE
                        binding!!.comingSoon.visibility = View.VISIBLE
                        binding!!.progressBar.visibility = View.GONE
                        binding!!.seriesRecyclerView.visibility = View.GONE
                        binding!!.seasonMore.visibility = View.GONE
                    }
                }
            }
        }

    fun getSeasonEpisodes() {
        binding!!.seasonHeader.isEnabled = false
        (binding!!.seriesRecyclerView.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
        railInjectionHelper!!.getEpisodeNoSeasonV2(seriesId, totalPages, 50, selectedSeason).observe(requireActivity()) { response ->
            hideProgressBar()
            if (response != null) {
                if (response.status.equals(APIStatus.START.name, ignoreCase = true)) {
                } else if (response.status.equals(APIStatus.SUCCESS.name, ignoreCase = true)) {
                    if (response.baseCategory != null) {
                        binding!!.progressBar.visibility = View.GONE
                        binding!!.seriesRecyclerView.visibility = View.VISIBLE
                        val enveuCommonResponse = response.baseCategory as RailCommonData
                        parseSeriesData(enveuCommonResponse)
                    }
                } else if (response.status.equals(APIStatus.ERROR.name, ignoreCase = true)) {
                    if (response.errorModel.errorCode != 0) {
                        if (context is SeriesDetailActivity) {
                            (context as SeriesDetailActivity).episodesList(null)
                        }
                        binding!!.seasonHeader.visibility = View.GONE
                        binding!!.headerSeason.visibility = View.GONE
                        binding!!.progressBar.visibility = View.GONE
                        binding!!.comingSoon.visibility = View.VISIBLE
                        binding!!.seriesRecyclerView.visibility = View.GONE
                        binding!!.seasonMore.visibility = View.GONE
                        parseSeriesData(null)
                    }
                } else if (response.status.equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                    binding!!.seasonHeader.visibility = View.GONE
                    binding!!.headerSeason.visibility = View.GONE
                    binding!!.comingSoon.visibility = View.VISIBLE
                    binding!!.progressBar.visibility = View.GONE
                    binding!!.seriesRecyclerView.visibility = View.GONE
                    binding!!.seasonMore.visibility = View.GONE
                }
            } else {
                if (context is SeriesDetailActivity) {
                    (context as SeriesDetailActivity).episodesList(null)
                }
            }
        }
    }

    var totalPages = 0
    private fun parseSeriesData(railCommonData: RailCommonData?) {
        this.railCommonData = railCommonData
        if (railCommonData != null) {
            if (!railCommonData.enveuVideoItemBeans.isEmpty()) {
                if (railCommonData.pageTotal - 1 > totalPages) {
                    binding!!.seasonMore.visibility = View.VISIBLE
                } else {
                    binding!!.seasonMore.visibility = View.GONE
                }
                binding!!.seasonHeader.visibility = View.VISIBLE
                binding!!.headerSeason.visibility = View.VISIBLE
                binding!!.seasonHeader.isEnabled = true
                if (seasonAdapter == null) {
                    seasonEpisodes = railCommonData.enveuVideoItemBeans
                    val seasonName = getSeasonName(railCommonData.seasonNumber - 1)
                    if (ObjectHelper.isEmpty(seasonName)) {
                        val seasonText = stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.detail_page_season.toString(),
                            getString(R.string.detail_page_season)
                        )
                        val seasonTxt = "$seasonText ${railCommonData.seasonNumber}"
                        binding!!.seasonHeader.text = seasonTxt
                    } else {
                        binding!!.seasonHeader.text = seasonName
                    }
                    if (seasonArray!!.size > 1) {
                        binding!!.seasonHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
                    }
                    binding!!.comingSoon.visibility = View.GONE
                    RecyclerAnimator(activity).animate(binding!!.seriesRecyclerView)
                    seasonAdapter = SeasonAdapter(requireActivity(), seasonEpisodes, seriesId, currentAssetId, this, this)
                    updateSeasonEpisodeNumber(railCommonData.seasonNumber, seasonAdapter!!.currentEpisodeNumber)
                    binding!!.seriesRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                    (binding!!.seriesRecyclerView.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
                    binding!!.seriesRecyclerView.adapter = seasonAdapter
                } else {
                    seasonEpisodes!!.addAll(railCommonData.enveuVideoItemBeans)
                    seasonAdapter!!.notifyDataSetChanged()
                }
                if (context is EpisodeActivity) {
                    (context as EpisodeActivity).episodesList(seasonEpisodes)
                }
                if (context is SeriesDetailActivity) {
                    (context as SeriesDetailActivity).episodesList(seasonEpisodes)
                }
            } else {
                binding!!.seasonHeader.visibility = View.GONE
                binding!!.headerSeason.visibility = View.GONE
                binding!!.comingSoon.visibility = View.VISIBLE
                binding!!.seriesRecyclerView.visibility = View.GONE
                binding!!.seasonMore.visibility = View.GONE
            }
        } else {
            if (selectedSeason > 0) {
                if (seasonArray!!.size > 1) {
                    binding!!.seasonHeader.isEnabled = true
                    val seasonText = stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.detail_page_season.toString(),
                        getString(R.string.detail_page_season)
                    )
                    val seasonTxt = "$seasonText $selectedSeason"
                    binding!!.seasonHeader.text = seasonTxt
                    binding!!.seasonHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0)
                }
            }
        }
        hideProgressBar()
    }

    override fun onItemClick(itemValue: EnveuVideoItemBean?, isPremium: Boolean, position: Int) {
        Log.d("javed", "onRowItemClicked: $itemValue")
        var trailerReferenceId: String? = ""
        var isParentContentNull = false
        var isHosted = false
        var externalUrl: String? = ""
        if (itemValue?.trailerReferenceId != null) {
            trailerReferenceId = itemValue?.trailerReferenceId
        }
        var tittle: String? = ""
        if (itemValue?.title != null) {
            tittle = itemValue?.title
        }
        if (itemValue?.parentContent == null) {
            isParentContentNull = true
        }
        var externalRefId: String? = ""
        if (itemValue?.externalRefId != null) {
            externalRefId = itemValue?.externalRefId
        }
        var skuId: String? = ""
        if (itemValue?.sku != null) {
            skuId = itemValue?.sku
        }
        var customContentType: String? = ""
        val assetType = itemValue?.assetType
        var videoType: String? = ""
        if (itemValue?.assetType.equals(AppConstants.VIDEO, ignoreCase = true)) {
            videoType = itemValue?.videoDetails?.videoType
        } else if (itemValue?.assetType.equals(AppConstants.LIVE, ignoreCase = true)) {
            if (java.lang.Boolean.TRUE == itemValue?.liveContent?.isHosted) {
                isHosted = true
            } else {
                if (itemValue?.liveContent?.externalUrl != null) {
                    externalUrl = itemValue?.liveContent.externalUrl
                }
            }
        } else {
            if (itemValue?.assetType.equals(AppConstants.CUSTOM, ignoreCase = true)) {
                customContentType = itemValue?.customContent?.customType
            }
        }
        AppCommonMethod.launchDetailScreenFromSearch(
            requireContext(),
            assetType!!,
            itemValue!!.id,
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
    }

    fun updateCurrentAsset(id: Int) {
        currentAssetId = id
        if (seasonAdapter != null) {
            seasonAdapter!!.updateCurrentId(currentAssetId)
            seasonAdapter!!.notifyDataSetChanged()
        }
    }

    fun updateTotalPages() {
        totalPages = 0
    }

    fun updateStatus() {
        if (seasonAdapter != null) {
            seasonAdapter!!.holdHolder()
        }
    }

    private fun updateSeasonEpisodeNumber(seasonNumber: Int, episodeNumber: Int) {
        Logger.d("seasonNumber: $seasonNumber | episodeNumber: $episodeNumber")
    }

    override fun getFirstItem(itemValue: EnveuVideoItemBean) {
        Log.d("getFirstItem", "getFirstItem1: $itemValue")
    }

    fun resetAdapter() {
        seasonAdapter = null
        binding!!.seriesRecyclerView.adapter = null
    }
}