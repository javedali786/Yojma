package com.breadgangtvnetwork.activities.listing.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.activities.detail.ui.EpisodeActivity
import com.breadgangtvnetwork.activities.series.ui.SeriesDetailActivity
import com.breadgangtvnetwork.baseModels.BaseBindingActivity
import com.breadgangtvnetwork.beanModel.enveuCommonRailData.RailCommonData
import com.breadgangtvnetwork.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.breadgangtvnetwork.databinding.MoreForyouActivityBinding
import com.breadgangtvnetwork.fragments.foryou.ui.ForYouAdapter
import com.breadgangtvnetwork.networking.apistatus.APIStatus
import com.breadgangtvnetwork.utils.Logger
import com.breadgangtvnetwork.utils.MediaTypeConstants
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod
import com.breadgangtvnetwork.utils.constants.AppConstants
import com.breadgangtvnetwork.utils.helpers.GridSpacingItemDecoration
import com.breadgangtvnetwork.utils.helpers.RailInjectionHelper
import com.breadgangtvnetwork.utils.helpers.RecyclerAnimator
import com.breadgangtvnetwork.utils.helpers.StringUtils
import com.breadgangtvnetwork.utils.stringsJson.converter.StringsHelper

class MoreForYouActivity : BaseBindingActivity<MoreForyouActivityBinding?>(), ForYouAdapter.EpisodeItemClick {
    private var contentType: String? = null
    var totalPages = 0
    private var railInjectionHelper: RailInjectionHelper? = null
    private val seriesId = 0
    private val context: Context? = null
    private val currentAssetId = 0
    private var forYouAdapter: ForYouAdapter? = null
    private var allEpiosdes: MutableList<EnveuVideoItemBean> = ArrayList()
    private var videoType: String? = ""
    private var tag: String? = ""
    private var id = 0
    private var railCommonData: RailCommonData? = null
    private val stringsHelper by lazy { StringsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): MoreForyouActivityBinding {
        return MoreForyouActivityBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentType = intent.getStringExtra("contentType")
        tag = intent.getStringExtra("preferenceData")
        videoType = intent.getStringExtra("videoType")
        id = intent.getIntExtra(AppConstants.ID, 0)
        binding!!.colorsData = ColorsHelper
        binding!!.toolbarGrid.colorsData = ColorsHelper
        binding!!.toolbarGrid.stringData = StringsHelper
        binding!!.toolbarGrid.backLayout.visibility = View.VISIBLE
      //  binding!!.toolbarGrid..setBackgroundColor(resources.getColor(R.color.buy_now_pay_now_btn_text_color))
        binding!!.toolbarGrid.backLayout.setOnClickListener { onBackPressed() }
        binding!!.toolbarGrid.titleSkip.visibility = View.GONE
        binding!!.toolbarGrid.titleMid.visibility = View.VISIBLE
        val relatedVideos = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.detail_page_related_videos.toString(),
            getString(R.string.detail_page_related_videos)
        )
        binding!!.toolbarGrid.titleMid.text = relatedVideos
        binding!!.progressBar.visibility = View.VISIBLE
        try {
            episodeList
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    private fun hideProgressBar() {}
    private val episodeList: Unit
        get() {
            binding!!.listMoreRecyclerview.addItemDecoration(GridSpacingItemDecoration(3, 6, true))
            railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
            forYouContent
        }// all episode view to set here

    //  hideProgressBar();
    private val forYouContent: Unit
        get() {
            railInjectionHelper!!.getRelatedContent(0, 50, contentType, id).observe(this) { response ->
                //  hideProgressBar();
                if (response != null) {
                    if (response.status.equals(APIStatus.START.name, ignoreCase = true)) {
                    } else if (response.status.equals(APIStatus.SUCCESS.name, ignoreCase = true)) {
                        if (response.baseCategory != null) {
                            val enveuCommonResponse = response.baseCategory as RailCommonData
                            parseSeriesData(enveuCommonResponse)
                            binding!!.progressBar.visibility = View.GONE
                            if (!StringUtils.isNullOrEmptyOrZero(enveuCommonResponse.seasonName)) {
                            } else {
                                // all episode view to set here
                                if (enveuCommonResponse.pageTotal - 1 > totalPages) {
                                }
                                if (forYouAdapter == null) {
                                    allEpiosdes = enveuCommonResponse.enveuVideoItemBeans
                                    if (contentType.equals(AppConstants.CUSTOM, ignoreCase = true)) {
                                        if (videoType.equals(MediaTypeConstants.getInstance().expedition, ignoreCase = true)) {
                                        }
                                    }
                                    RecyclerAnimator(this@MoreForYouActivity).animate(binding!!.listMoreRecyclerview)
                                    forYouAdapter = ForYouAdapter(this@MoreForYouActivity, allEpiosdes, seriesId, currentAssetId, this@MoreForYouActivity)
                                    updateSeasonEpisodeNumber(-1, forYouAdapter!!.currentEpisodeNumber)
                                    binding!!.listMoreRecyclerview.layoutManager = GridLayoutManager(this@MoreForYouActivity, 3)
                                    (binding!!.listMoreRecyclerview.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
                                    binding!!.listMoreRecyclerview.adapter = forYouAdapter
                                } else {
                                    allEpiosdes.addAll(enveuCommonResponse.enveuVideoItemBeans)
                                    forYouAdapter!!.notifyDataSetChanged()
                                }
                                hideProgressBar()
                                if (context is EpisodeActivity) {
                                    context.episodesList(allEpiosdes)
                                } else if (context is SeriesDetailActivity) {
                                    context.episodesList(allEpiosdes)
                                }
                            }
                        }
                    } else if (response.status.equals(APIStatus.ERROR.name, ignoreCase = true)) {
                        if (response.errorModel.errorCode != 0) {
                            binding!!.progressBar.visibility = View.GONE
                            binding!!.listMoreRecyclerview.visibility = View.GONE
                        }
                    } else if (response.status.equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                        binding!!.progressBar.visibility = View.GONE
                        binding!!.listMoreRecyclerview.visibility = View.GONE
                    }
                }
            }
        }

    private fun parseSeriesData(railCommonData: RailCommonData) {
        this.railCommonData = railCommonData
    }

    private fun updateSeasonEpisodeNumber(seasonNumber: Int, episodeNumber: Int) {
        Logger.d("seasonNumber: $seasonNumber | episodeNumber: $episodeNumber")
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
                    externalUrl = itemValue?.liveContent?.externalUrl
                }
            }
        } else {
            if (itemValue?.assetType.equals(AppConstants.CUSTOM, ignoreCase = true)) {
                customContentType = itemValue?.customContent?.customType
            }
        }
        AppCommonMethod.launchDetailScreenFromSearch(
            this,
            assetType!!,
            itemValue?.id!!,
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
            itemValue?.posterURL
        )
    }
}