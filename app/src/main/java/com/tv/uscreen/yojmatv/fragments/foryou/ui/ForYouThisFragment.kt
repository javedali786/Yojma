package com.tv.uscreen.yojmatv.fragments.foryou.ui


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.activities.listing.ui.MoreForYouActivity
import com.tv.uscreen.yojmatv.baseModels.BaseBindingFragment
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModel.selectedSeason.SelectedSeasonModel
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.databinding.ForYouFragmentLayoutBinding
import com.tv.uscreen.yojmatv.networking.apistatus.APIStatus
import com.tv.uscreen.yojmatv.networking.responsehandler.ResponseModel
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.RecyclerAnimator
import com.tv.uscreen.yojmatv.utils.helpers.SpacingItemDecoration
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class ForYouThisFragment : BaseBindingFragment<ForYouFragmentLayoutBinding?>(), ForYouAdapter.EpisodeItemClick, MoreItemClickListner {
    private var railInjectionHelper: RailInjectionHelper? = null
    private val seriesId = 0
    private val seasonCount = 0
    var selectedSeason = 0
    private val seasonArray: ArrayList<*>? = null
    private val seasonNameList = ArrayList<String>()
    private val bundle: Bundle? = null
    var isLoggedIn = false
    private var context: Context? = null
    private val seasonList: ArrayList<SelectedSeasonModel>? = null
    private val currentAssetId = 0
    var seasonAdapter: ForYouAdapter? = null
        private set
    private val seasonEpisodes: List<EnveuVideoItemBean>? = null
    private var allEpiosdes: MutableList<EnveuVideoItemBean> = ArrayList()
    private var videoType: String? = ""
    private var contentType: String? = ""
    private val mLastClickTime: Long = 0
    private var tag: String? = ""
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    var playListId: String? = null
    private var railCommonData: RailCommonData? = null
    private val stringsHelper by lazy { StringsHelper }
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

    override fun inflateBindingLayout(inflater: LayoutInflater): ForYouFragmentLayoutBinding {
        return ForYouFragmentLayoutBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.d("ON VIEW CREATED-----", "TRUE")
        binding!!.stringData = stringsHelper
        binding!!.colorsData = ColorsHelper
        val bundle = arguments
        if (bundle != null) {
            videoType = bundle.getString("videoType")
            contentType = bundle.getString("contentType")
            tag = bundle.getString("preferenceData")
            Log.d("tag", "preferenceData-3: $tag")
        }
        try {
            handler = Handler()
            runnable = object : Runnable {
                override fun run() {
                    if (!StringUtils.isNullOrEmpty(tag)) {
                        forYouList
                    } else {
                        binding!!.comingSoon.visibility = View.VISIBLE
                    }
                }
            }
            handler!!.postDelayed(runnable as Runnable, 2000)
        } catch (e: Exception) {
            Logger.w(e)
        }
        binding!!.moreText.setOnClickListener { view1: View? -> moreItemClick() }
    }

    fun hideProgressBar() {
        /* if (context instanceof SeriesDetailActivity) {
            ((SeriesDetailActivity) context).isSeasonData = true;
            ((SeriesDetailActivity) context).stopShimmer();

            ((SeriesDetailActivity) context).dismissLoading(((SeriesDetailActivity) context).getBinding().progressBar);

        } else if (context instanceof EpisodeActivity) {
            ((EpisodeActivity) context).dismissLoading(((EpisodeActivity) context).getBinding().progressBar);
            ((EpisodeActivity) context).isSeasonData = true;
            ((EpisodeActivity) context).stopShimmercheck();
        }*/
    }

    private val forYouList: Unit
        private get() {
            binding!!.seriesRecyclerView.addItemDecoration(
                SpacingItemDecoration(
                    8,
                    SpacingItemDecoration.HORIZONTAL
                )
            )
            if (activity != null) {
                railInjectionHelper = ViewModelProvider(this).get(RailInjectionHelper::class.java)
            }
            forYouContent
        }

    override fun onStop() {
        super.onStop()
        handler!!.removeCallbacks(runnable!!)
    }//                                    allEpiosdes.addAll(enveuCommonResponse.getEnveuVideoItemBeans());

    //                                    forYouAdapter.notifyDataSetChanged();
    //   updateSeasonEpisodeNumber(-1, forYouAdapter.getCurrentEpisodeNumber());
    //   ((SimpleItemAnimator) getBinding().seriesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    val forYouContent: Unit
        get() {
            railInjectionHelper!!.getForYouContent(0, 20, tag, contentType, videoType).observe(requireActivity(), object : Observer<ResponseModel<*>?> {
                override fun onChanged(response: ResponseModel<*>?) {
                    hideProgressBar()
                    if (response != null) {
                        if (response.status.equals(APIStatus.START.name, ignoreCase = true)) {
                        } else if (response.status.equals(APIStatus.SUCCESS.name, ignoreCase = true)) {
                            if (response.baseCategory != null) {
                                val enveuCommonResponse = response.baseCategory as RailCommonData
                                parseSeriesData(enveuCommonResponse)
                                binding!!.progressBar.visibility = View.GONE
                                if (!StringUtils.isNullOrEmptyOrZero(enveuCommonResponse.seasonName)) {
                                } else {
                                    binding!!.seasonHeader.visibility = View.VISIBLE
                                    binding!!.headerSeason.visibility = View.VISIBLE
                                    binding!!.seasonHeader.isEnabled = false
                                    if (seasonAdapter == null) {
                                        binding!!.moreText.visibility = View.VISIBLE
                                        allEpiosdes = enveuCommonResponse.enveuVideoItemBeans
                                        val relatedVideos = stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.detail_page_related_videos.toString(),
                                            getString(R.string.detail_page_related_videos)
                                        )
                                        binding!!.seasonHeader.text = relatedVideos
                                        binding!!.seasonHeader.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                                        RecyclerAnimator(activity).animate(binding!!.seriesRecyclerView)
                                        seasonAdapter = ForYouAdapter(requireActivity(), allEpiosdes, seriesId, currentAssetId, this@ForYouThisFragment)
                                        //   updateSeasonEpisodeNumber(-1, forYouAdapter.getCurrentEpisodeNumber());
                                        binding!!.seriesRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
                                        //   ((SimpleItemAnimator) getBinding().seriesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                                        binding!!.seriesRecyclerView.adapter = seasonAdapter
                                    } else {
//                                    allEpiosdes.addAll(enveuCommonResponse.getEnveuVideoItemBeans());
//                                    forYouAdapter.notifyDataSetChanged();
                                    }
                                    hideProgressBar()

                                }
                            }
                        } else if (response.status.equals(APIStatus.ERROR.name, ignoreCase = true)) {
                            if (response.errorModel.errorCode != 0) {
                                binding!!.headerSeason.visibility = View.VISIBLE
                                binding!!.seasonHeader.visibility = View.VISIBLE
                                binding!!.comingSoon.visibility = View.VISIBLE
                                binding!!.progressBar.visibility = View.GONE
                                binding!!.seriesRecyclerView.visibility = View.GONE
                            }
                        } else if (response.status.equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                            binding!!.seasonHeader.visibility = View.VISIBLE
                            binding!!.headerSeason.visibility = View.GONE
                            binding!!.comingSoon.visibility = View.VISIBLE
                            binding!!.progressBar.visibility = View.GONE
                            binding!!.seriesRecyclerView.visibility = View.GONE
                        }
                    }
                }
            })
        }
    var totalPages = 0
    private fun parseSeriesData(railCommonData: RailCommonData) {
        this.railCommonData = railCommonData
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
                    externalUrl = itemValue.liveContent?.externalUrl
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

    private fun updateSeasonEpisodeNumber(seasonNumber: Int, episodeNumber: Int) {
        Logger.d("seasonNumber: $seasonNumber | episodeNumber: $episodeNumber")
    }

    override fun moreItemClick() {
        ActivityLauncher.getInstance().listActivityForYou(activity, MoreForYouActivity::class.java, contentType, tag, videoType, 0)
    }

    fun resetAdapter() {
        seasonAdapter = null
        binding!!.seriesRecyclerView.adapter = null
    }
}