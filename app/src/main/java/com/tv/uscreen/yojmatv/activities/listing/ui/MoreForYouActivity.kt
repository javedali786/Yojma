package com.tv.uscreen.yojmatv.activities.listing.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.activities.series.adapter.RelatedContentAdapter
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.databinding.MoreForyouActivityBinding
import com.tv.uscreen.yojmatv.networking.apistatus.APIStatus
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.MediaTypeConstants
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.GridSpacingItemDecoration
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.RecyclerAnimator
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class MoreForYouActivity : BaseBindingActivity<MoreForyouActivityBinding?>(), RelatedContentAdapter.EpisodeItemClick {
    private var contentType: String? = null
    var totalPages = 0
    private var railInjectionHelper: RailInjectionHelper? = null
    private val seriesId = 0
    private val context: Context? = null
    private val currentAssetId = 0
    private var RelatedContentAdapter: RelatedContentAdapter? = null
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
        binding!!.toolbarGrid.searchIcon.visibility = View.GONE
        binding!!.toolbarGrid.backLayout.visibility = View.VISIBLE
      //  binding!!.toolbarGrid..setBackgroundColor(resources.getColor(R.color.buy_now_pay_now_btn_text_color))
        binding!!.toolbarGrid.backLayout.setOnClickListener { onBackPressed() }
        binding!!.toolbarGrid.titleSkip.visibility = View.GONE
        binding!!.toolbarGrid.titleMid.visibility = View.VISIBLE
        binding!!.toolbarGrid.logoMain2.visibility = View.GONE

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
            binding!!.listMoreRecyclerview.addItemDecoration(GridSpacingItemDecoration(2, 4, true))
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
                                if (RelatedContentAdapter == null) {
                                    allEpiosdes = enveuCommonResponse.enveuVideoItemBeans
                                    if (contentType.equals(AppConstants.CUSTOM, ignoreCase = true)) {
                                        if (videoType.equals(MediaTypeConstants.getInstance().expedition, ignoreCase = true)) {
                                        }
                                    }
                                    RecyclerAnimator(this@MoreForYouActivity).animate(binding!!.listMoreRecyclerview)
                                    RelatedContentAdapter = RelatedContentAdapter(this@MoreForYouActivity, allEpiosdes, seriesId, currentAssetId, this@MoreForYouActivity)
                                    updateSeasonEpisodeNumber(-1, RelatedContentAdapter!!.currentEpisodeNumber)
                                     binding!!.listMoreRecyclerview.layoutManager = GridLayoutManager(this@MoreForYouActivity, 2)
                                    (binding!!.listMoreRecyclerview.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
                                    binding!!.listMoreRecyclerview.adapter = RelatedContentAdapter
                                } else {
                                    allEpiosdes.addAll(enveuCommonResponse.enveuVideoItemBeans)
                                    RelatedContentAdapter!!.notifyDataSetChanged()
                                }
                                hideProgressBar()
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
        var trailerReferenceId: String? = ""
        var isParentContentNull = false
        val isHosted = false
        val externalUrl: String = ""
        if (itemValue?.trailerReferenceId != null) {
            trailerReferenceId = itemValue.trailerReferenceId
        }
        var tittle: String? = ""
        if (itemValue?.title != null) {
            tittle = itemValue.title
        }
        if (itemValue?.parentContent == null) {
            isParentContentNull = true
        }
        var externalRefId: String? = ""
        if (itemValue?.externalRefId != null) {
            externalRefId = itemValue.externalRefId
        }
        var skuId: String? = ""
        if (itemValue?.sku != null) {
            skuId = itemValue.sku
        }
        var customContentType: String? = ""
        val assetType = itemValue?.assetType
        var videoType: String? = ""
        if (itemValue?.assetType.equals(AppConstants.VIDEO, ignoreCase = true)) {
            videoType = itemValue?.videoDetails?.videoType
        } else {
            if (itemValue?.assetType.equals(AppConstants.CUSTOM, ignoreCase = true)) {
                customContentType = itemValue?.customType
            }
        }
        AppCommonMethod.launchDetailScreenFromSearch(
            this,
            assetType!!,
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
    }
}