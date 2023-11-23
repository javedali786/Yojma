package com.tv.uscreen.yojmatv.fragments.player.ui


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.activities.detail.ui.EpisodeActivity
import com.tv.uscreen.yojmatv.activities.listing.ui.MoreForYouActivity
import com.tv.uscreen.yojmatv.activities.series.adapter.RelatedContentAdapter
import com.tv.uscreen.yojmatv.activities.series.ui.SeriesDetailActivity
import com.tv.uscreen.yojmatv.baseModels.BaseBindingFragment
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.databinding.RelatedContentFragmentLayoutBinding
import com.tv.uscreen.yojmatv.fragments.foryou.ui.MoreItemClickListner
import com.tv.uscreen.yojmatv.networking.apistatus.APIStatus
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
import java.util.Locale


class RelatedContentFragment : BaseBindingFragment<RelatedContentFragmentLayoutBinding?>(), RelatedContentAdapter.EpisodeItemClick, MoreItemClickListner {
    private var railInjectionHelper: RailInjectionHelper? = null
    private val seriesId = 0
    private var context: Context? = null
    private val currentAssetId = 0
    private var seasonAdapter: RelatedContentAdapter? = null
    private var allEpiosdes: MutableList<EnveuVideoItemBean> = ArrayList()
    private var videoType: String? = ""
    private var contentType: String? = ""
    private val tag = ""
    private var handler: Handler? = null
    private var runnable: Runnable? = null
    private var id = 0
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

    override fun inflateBindingLayout(inflater: LayoutInflater): RelatedContentFragmentLayoutBinding {
        return RelatedContentFragmentLayoutBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.stringData = stringsHelper
        binding!!.colorsData = ColorsHelper
        Logger.d("ON VIEW CREATED-----", "TRUE")
        val bundle = arguments
        if (bundle != null) {
            videoType = bundle.getString("videoType")
            contentType = bundle.getString("contentType")
            id = bundle.getInt(AppConstants.ID)
        }
        try {
            handler = Handler()
            runnable = Runnable { forYouList }
            handler!!.postDelayed(runnable as Runnable, 2000)
        } catch (e: Exception) {
            Logger.w(e)
        }
        binding!!.moreText.setOnClickListener { moreItemClick() }
    }

    private fun hideProgressBar() {
       /* if (context is SeriesDetailActivity) {
            (context as SeriesDetailActivity).isSeasonData = true
            (context as SeriesDetailActivity).stopShimmer()
            (context as SeriesDetailActivity).dismissLoading((context as SeriesDetailActivity).binding!!.progressBar)
        } else if (context is EpisodeActivity) {
            (context as EpisodeActivity).dismissLoading((context as EpisodeActivity).binding!!.progressBar)
            (context as EpisodeActivity).isSeasonData = true
            (context as EpisodeActivity).stopShimmercheck()
        }*/
    }

    private val forYouList: Unit
        get() {
            binding!!.seriesRecyclerView.addItemDecoration(
                SpacingItemDecoration(
                    8,
                    SpacingItemDecoration.HORIZONTAL
                )
            )
            if (activity != null) {
                railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
            }
            forYouContent
        }

    override fun onStop() {
        super.onStop()
        handler!!.removeCallbacks(runnable!!)
    }
    private val forYouContent: Unit
        get() {
            railInjectionHelper!!.getRelatedContent(0, 20, contentType, id).observe(
                requireActivity()
            ) { response ->
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
                                    seasonAdapter = RelatedContentAdapter(requireActivity(), allEpiosdes, seriesId, currentAssetId, this@RelatedContentFragment)
                                    //   updateSeasonEpisodeNumber(-1, relatedContentAdapter.getCurrentEpisodeNumber());
                                    binding!!.seriesRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
                                    //   ((SimpleItemAnimator) getBinding().seriesRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                                    binding!!.seriesRecyclerView.adapter = seasonAdapter
                                } else {
                                    //                                    allEpiosdes.addAll(enveuCommonResponse.getEnveuVideoItemBeans());
                                    //                                    relatedContentAdapter.notifyDataSetChanged();
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
        }
    var totalPages = 0
    private fun parseSeriesData(railCommonData: RailCommonData) {
        this.railCommonData = railCommonData
    }

    override fun onItemClick(enveuVideoItemBean: EnveuVideoItemBean?, isPremium: Boolean, position: Int) {
        val assetType = enveuVideoItemBean?.assetType?.uppercase(Locale.getDefault())
        AppCommonMethod.redirectionLogic(requireContext(), railCommonData!!, position)
    }

    private fun updateSeasonEpisodeNumber(seasonNumber: Int, episodeNumber: Int) {
        Logger.d("seasonNumber: $seasonNumber | episodeNumber: $episodeNumber")
    }

    override fun moreItemClick() {
      ActivityLauncher.getInstance().listActivityForYou(activity, MoreForYouActivity::class.java, contentType, tag, videoType, id)
    }

    fun resetAdapter() {
        seasonAdapter = null
        binding!!.seriesRecyclerView.adapter = null
    }
}