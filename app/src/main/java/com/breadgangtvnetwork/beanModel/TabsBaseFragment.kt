package com.breadgangtvnetwork.beanModel

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.SDKConfig
import com.breadgangtvnetwork.activities.homeactivity.TabIdInterface
import com.breadgangtvnetwork.activities.listing.listui.ListActivity
import com.breadgangtvnetwork.activities.listing.ui.GridActivity
import com.breadgangtvnetwork.activities.privacypolicy.ui.WebViewActivity
import com.breadgangtvnetwork.activities.search.ui.ActivitySearch
import com.breadgangtvnetwork.activities.usermanagment.ui.ActivityLogin
import com.breadgangtvnetwork.activities.watchList.ui.WatchListActivity
import com.breadgangtvnetwork.adapters.commonRails.CommonAdapterNew
import com.breadgangtvnetwork.adapters.shimmer.ShimmerAdapter
import com.breadgangtvnetwork.baseModels.BaseBindingFragment
import com.breadgangtvnetwork.baseModels.HomeBaseViewModel
import com.breadgangtvnetwork.beanModel.enveuCommonRailData.RailCommonData
import com.breadgangtvnetwork.callbacks.commonCallbacks.CommonApiCallBack
import com.breadgangtvnetwork.callbacks.commonCallbacks.CommonRailtItemClickListner
import com.breadgangtvnetwork.callbacks.commonCallbacks.MoreClickListner
import com.breadgangtvnetwork.databinding.FragmentHomeBinding
import com.breadgangtvnetwork.fragments.gaming.viewModel.GamingFragmentViewModel
import com.breadgangtvnetwork.fragments.home.viewModel.HomeFragmentViewModel
import com.breadgangtvnetwork.fragments.movies.viewModel.MovieFragmentViewModel
import com.breadgangtvnetwork.fragments.news.viewModel.ReelsFragmentViewModel
import com.breadgangtvnetwork.fragments.shows.viewModel.PodcastFragmentViewModel
import com.breadgangtvnetwork.utils.Logger
import com.breadgangtvnetwork.utils.ObjectHelper
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod.Companion.configResponse
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod.Companion.getCheckBCID
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod.Companion.heroAssetRedirections
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod.Companion.redirectionLogic
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod.Companion.trackFcmEvent
import com.breadgangtvnetwork.utils.constants.AppConstants
import com.breadgangtvnetwork.utils.cropImage.helpers.ShimmerDataModel
import com.breadgangtvnetwork.utils.helpers.NetworkConnectivity
import com.breadgangtvnetwork.utils.helpers.RailInjectionHelper
import com.breadgangtvnetwork.utils.helpers.RecyclerAnimator
import com.breadgangtvnetwork.utils.helpers.ToastHandler
import com.breadgangtvnetwork.utils.helpers.intentlaunchers.ActivityLauncher
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KidsModeSinglton
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.breadgangtvnetwork.utils.stringsJson.converter.StringsHelper
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory
import com.enveu.client.enums.LandingPageType
import com.enveu.client.enums.Layouts
import com.enveu.client.enums.ListingLayoutType
import com.enveu.client.enums.PDFTarget

open class TabsBaseFragment<T : HomeBaseViewModel?> : BaseBindingFragment<FragmentHomeBinding?>(), CommonRailtItemClickListner, MoreClickListner, TabIdInterface {
    private var viewModel: T? = null
    private var swipeToRefresh = 0
    private val mScrollY = 0
    private var preference: KsPreferenceKeys? = null
    private var tabId: String? = null
    private var railCommonDataList: MutableList<RailCommonData> = ArrayList()
    private var adapterNew: CommonAdapterNew? = null
    private var kidsMode = false
    private var intentFrom = ""
    var mActivity: Activity? = null
    private val tabsBaseBackgroundHideCallBack: TabsBaseBackgroundHideCallBack? = null
    protected fun setViewModel(viewModelClass: Class<out HomeBaseViewModel>) {
        viewModel = ViewModelProvider(this)[viewModelClass] as T
    }

    public override fun inflateBindingLayout(inflater: LayoutInflater): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }

    private fun modelCall() {
        mActivity = activity
        //        tabsBaseBackgroundHideCallBack = (TabsBaseBackgroundHideCallBack) mActivity;
//        tabsBaseBackgroundHideCallBack.tabBackgroundHide(true);
        callShimmer()
        if (activity != null) {
            preference = KsPreferenceKeys.getInstance()
        }
        setTabId()
        connectionObserver()
    }

    private fun setTabId() {
        try {
            if (configResponse != null && configResponse.data != null && configResponse.data.appConfig != null && configResponse.data.appConfig.navScreens != null) {
                if (viewModel is HomeFragmentViewModel) {
                    tabId = SDKConfig.getInstance().firstTabId
                } else if (viewModel is PodcastFragmentViewModel) {
                    tabId = SDKConfig.getInstance().secondTabId
                } else if (viewModel is MovieFragmentViewModel) {
                    tabId = SDKConfig.getInstance().thirdTabId
                } else if (viewModel is GamingFragmentViewModel) {
                    tabId = SDKConfig.getInstance().fourthTabId
                }
            } else {
                tabId = if (viewModel is HomeFragmentViewModel) {
                    AppConstants.HOME_ENVEU
                } else if (viewModel is ReelsFragmentViewModel) {
                    AppConstants.ORIGINAL_ENVEU
                } else if (viewModel is PodcastFragmentViewModel) {
                    AppConstants.PREMIUM_ENVEU
                } else if (viewModel is MovieFragmentViewModel) {
                    AppConstants.SINETRON_ENVEU
                } else {
                    AppConstants.HOME_ENVEU
                }
            }
        } catch (ex: Exception) {
            tabId = if (viewModel is HomeFragmentViewModel) {
                AppConstants.HOME_ENVEU
            } else if (viewModel is ReelsFragmentViewModel) {
                AppConstants.ORIGINAL_ENVEU
            } else if (viewModel is PodcastFragmentViewModel) {
                AppConstants.PREMIUM_ENVEU
            } else if (viewModel is MovieFragmentViewModel) {
                AppConstants.SINETRON_ENVEU
            } else {
                AppConstants.HOME_ENVEU
            }
            Logger.w(ex)
        }
    }

    private fun connectionObserver() {
        if (activity != null
            && NetworkConnectivity.isOnline(activity)
        ) {
            binding!!.noConnectionLayout.visibility = View.GONE
            adapterNew = null
            connectionValidation(true)
        } else {
            connectionValidation(false)
        }
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            binding!!.swipeContainer.isRefreshing = true
            uiInitialisation()
            loadDataFromModel()
        } else {
            noConnectionLayout()
            try {
                if (activity != null) {
                }
            } catch (ex: Exception) {
                //     getBinding().connection.btnMyDownloads.setVisibility(View.GONE);
                Logger.w(ex)
            }
        }
    }

    private fun uiInitialisation() {
        swipeToRefresh()
        callShimmer()
        //ThemeHandler.getInstance().applyThemeHomeTabOne(requireContext(), getBinding());
        binding!!.noResultFound.colorsData = ColorsHelper
        binding!!.connection.colorsData = ColorsHelper
        binding!!.noResultFound.stringData = StringsHelper
        binding!!.connection.stringData = StringsHelper
        binding!!.colorsData = ColorsHelper
        binding!!.noResultFound.retryAgain.setOnClickListener { view: View? ->
            binding!!.noResultLayout.visibility = View.GONE
            binding!!.noConnectionLayout.visibility = View.GONE
            binding!!.myRecyclerView.visibility = View.VISIBLE
            swipeToRefresh = 2
            connectionObserver()
        }
        binding!!.myRecyclerView.setHasFixedSize(true)
        binding!!.myRecyclerView.setItemViewCacheSize(20)
        binding!!.myRecyclerView.isNestedScrollingEnabled = false
        binding!!.myRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding!!.swipeContainer.isRefreshing = true
        binding!!.swipeContainer.visibility = View.VISIBLE
        binding!!.noResultLayout.visibility = View.GONE
        binding!!.noConnectionLayout.visibility = View.GONE
        binding!!.swipeContainer.setOnRefreshListener {
            if (NetworkConnectivity.isOnline(activity) && swipeToRefresh == 1) {
                swipeToRefresh = 2
                binding!!.swipeContainer.isRefreshing = true
                binding!!.swipeContainer.visibility = View.VISIBLE
                binding!!.noResultLayout.visibility = View.GONE
                baseCategories
            } else {
                swipeToRefreshCheck()
            }
        }
        baseCategories
    }

    private val baseCategories: Unit
        get() {
            railCommonDataList = ArrayList()
            Log.d("tabId", "tabId: $tabId")
            adapterNew = null
            val railInjectionHelper = ViewModelProvider(this).get(RailInjectionHelper::class.java)
            railInjectionHelper.getScreenWidgets(activity, tabId, false, object : CommonApiCallBack {
                override fun onSuccess(item: Any) {
                    if (item is RailCommonData) {
                        railCommonDataList.add(item)
                        binding!!.swipeContainer.isRefreshing = false
                        if (adapterNew == null) {
                            RecyclerAnimator(activity).animate(binding!!.myRecyclerView)
                            adapterNew = CommonAdapterNew(railCommonDataList, this@TabsBaseFragment, this@TabsBaseFragment)
                            binding!!.myRecyclerView.adapter = adapterNew
                        } else {
                            synchronized(railCommonDataList) {
                                adapterNew?.notifyItemChanged(railCommonDataList.size - 1)
                                binding!!.myRecyclerView.scrollToPosition(mScrollY + 500)
                            }
                        }
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    Logger.w(throwable)
                    if ("No Data".equals(throwable.message, ignoreCase = true)) {
                        binding!!.swipeContainer.isRefreshing = false
                        binding!!.myRecyclerView.visibility = View.GONE
                        binding!!.noResultLayout.visibility = View.VISIBLE
                    }
                }

                override fun onFinish() {
                    swipeToRefresh = 1
                    if (railCommonDataList.size <= 0) {
                        binding!!.swipeContainer.isRefreshing = false
                        binding!!.myRecyclerView.visibility = View.GONE
                        binding!!.noResultLayout.visibility = View.VISIBLE
                    }
                }
            })
        }

    private fun callShimmer() {
        val shimmerAdapter = ShimmerAdapter(
            activity, ShimmerDataModel(activity).getList(0), ShimmerDataModel(
                activity
            ).slides
        )
        binding!!.myRecyclerView.adapter = shimmerAdapter
    }

    private fun noConnectionLayout() {
        binding!!.noConnectionLayout.visibility = View.VISIBLE
        binding!!.connection.retryTxt.setOnClickListener { view: View? -> connectionObserver() }
    }

    private fun loadDataFromModel() {
        if (activity != null && swipeToRefresh != 1) {
            viewModel!!.allCategories.observe(requireActivity()) { assetCommonBean: List<BaseCategory?>? ->
                if (assetCommonBean == null) {
                    binding!!.myRecyclerView.visibility = View.GONE
                    binding!!.noResultLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun swipeToRefresh() {
        binding!!.swipeContainer.setOnRefreshListener {
            if (NetworkConnectivity.isOnline(baseActivity) && swipeToRefresh == 1) {
                swipeToRefresh = 2
                railCommonDataList.clear()
                connectionObserver()
            }
            swipeToRefreshCheck()
        }
    }

    private fun swipeToRefreshCheck() {
        if (binding!!.swipeContainer != null) {
            if (binding!!.swipeContainer.isRefreshing) {
                binding!!.swipeContainer.isRefreshing = false
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        kidsMode = KidsModeSinglton.getInstance().aBoolean
        Logger.d("BolleanTABBASEFRAGMENT $kidsMode")
        modelCall()
        viewModel!!.resetObject()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun railItemClick(railCommonData: RailCommonData, position: Int) {
        try {
            trackFcmEvent(railCommonData.enveuVideoItemBeans[position].title, railCommonData.enveuVideoItemBeans[position].assetType, activity, position)
        } catch (e: Exception) {
            Logger.w(e)
        }
        if (railCommonData.screenWidget.type != null && Layouts.HRO.name.equals(railCommonData.screenWidget.layout, ignoreCase = true)) {
            heroClickRedirection(railCommonData)
        } else {
            redirectionLogic(requireContext(), railCommonData, position)
        }
    }

    private fun heroClickRedirection(railCommonData: RailCommonData) {
        try {
            trackFcmEvent(railCommonData.enveuVideoItemBeans[0].title, railCommonData.enveuVideoItemBeans[0].assetType, activity, 0)
        } catch (e: Exception) {
            Logger.w(e)
        }
        val landingPageType = railCommonData.screenWidget.landingPageType
        if (landingPageType != null) {
            if (landingPageType == LandingPageType.DEF.name || landingPageType == LandingPageType.AST.name) {
                var videoId = 0L
                if (getCheckBCID(railCommonData.enveuVideoItemBeans[0].brightcoveVideoId)) {
                    videoId = railCommonData.enveuVideoItemBeans[0].brightcoveVideoId.toLong()
                }
                heroAssetRedirections(railCommonData, activity, videoId, railCommonData.screenWidget.landingPageAssetId!!.toInt(), "0", false)
            } else if (landingPageType == LandingPageType.HTM.name) {
                val webViewIntent = Intent(activity, WebViewActivity::class.java)
                webViewIntent.putExtra(AppConstants.WEB_VIEW_HEADING, railCommonData.screenWidget.landingPageTitle)
                webViewIntent.putExtra(AppConstants.WEB_VIEW_URL, railCommonData.screenWidget.htmlLink)
                startActivity(webViewIntent)
            } else if (landingPageType == LandingPageType.PDF.name) {
                if (railCommonData.screenWidget.landingPagetarget != null) {
                    if (railCommonData.screenWidget.landingPagetarget == PDFTarget.LGN.name) {
                        ActivityLauncher.getInstance().loginActivity(activity, ActivityLogin::class.java)
                    } else if (railCommonData.screenWidget.landingPagetarget == PDFTarget.SRH.name) {
                        ActivityLauncher.getInstance().searchActivity(activity, ActivitySearch::class.java)
                    }
                }
            } else if (landingPageType == LandingPageType.PLT.name) {
                Logger.e("MORE RAIL CLICK: $railCommonData")
                moreRailClick(railCommonData, 0, "")
            }
        }
    }

    override fun moreRailClick(data: RailCommonData, position: Int, multilingualTitle: String) {
        if (data.screenWidget != null) {
            val playListId: String?
            playListId = if (data.screenWidget.contentID != null) data.screenWidget.contentID else data.screenWidget.landingPagePlayListId
            if (playListId == null || playListId.equals("", ignoreCase = true)) {
                ToastHandler.getInstance().show(activity, requireActivity().resources.getString(R.string.something_went_wrong_at_our_end_please_try_later))
                return
            }
            val finalName = checkNull(data.screenWidget, multilingualTitle)
            // String continueWatchingTitle = OttApplication.getInstance().getResources().getString(R.string.continueWatchingTitle);
            if (data.screenWidget.name != null && data.screenWidget.referenceName != null && (data.screenWidget.referenceName.equals(
                    AppConstants.ContentType.CONTINUE_WATCHING.name,
                    ignoreCase = true
                ) || data.screenWidget.referenceName.equals("special_playlist", ignoreCase = true))
            ) {
                ActivityLauncher.getInstance().portraitListing(
                    activity, GridActivity::class.java,
                    playListId, finalName, 0, 0, data.screenWidget, data.isContinueWatching
                )
            } else if (data.screenWidget.name != null && data.screenWidget.referenceName != null && data.screenWidget.referenceName.equals(
                    AppConstants.ContentType.MY_WATCHLIST.name,
                    ignoreCase = true
                )
            ) {
                ActivityLauncher.getInstance().watchHistory(activity, WatchListActivity::class.java, finalName, false)
            } else {
                if (data.screenWidget.contentListinglayout != null && !data.screenWidget.contentListinglayout.equals("", ignoreCase = true) && data.screenWidget.contentListinglayout.equals(
                        ListingLayoutType.LST.name,
                        ignoreCase = true
                    )
                ) {
                    if (data.screenWidget.name != null) {
                        ActivityLauncher.getInstance().listActivity(
                            activity, ListActivity::class.java,
                            playListId, finalName, 0, 0, data.screenWidget
                        )
                    } else {
                        ActivityLauncher.getInstance().listActivity(
                            activity, ListActivity::class.java,
                            playListId, "", 0, 0, data.screenWidget
                        )
                    }
                } else if (data.screenWidget.contentListinglayout != null && !data.screenWidget.contentListinglayout.equals("", ignoreCase = true) && data.screenWidget.contentListinglayout.equals(
                        ListingLayoutType.GRD.name,
                        ignoreCase = true
                    )
                ) {
                    Logger.e("getRailData", "GRD")
                    if (data.screenWidget.name != null) {
                        ActivityLauncher.getInstance().portraitListing(
                            activity, GridActivity::class.java,
                            playListId, finalName, 0, 0, data.screenWidget, data.isContinueWatching
                        )
                    } else {
                        ActivityLauncher.getInstance().portraitListing(
                            activity, GridActivity::class.java,
                            playListId, "", 0, 0, data.screenWidget, data.isContinueWatching
                        )
                    }
                } else {
                    Logger.e("getRailData", "PDF")
                    if (data.screenWidget.name != null) {
                        ActivityLauncher.getInstance().portraitListing(
                            activity, GridActivity::class.java,
                            playListId, finalName, 0, 0, data.screenWidget, data.isContinueWatching
                        )
                    } else {
                        ActivityLauncher.getInstance().portraitListing(
                            activity, GridActivity::class.java,
                            playListId, "", 0, 0, data.screenWidget, data.isContinueWatching
                        )
                    }
                }
            }
        }
    }

    private fun checkNull(screenWidget: BaseCategory?, multilingualTitle: String): String {
        var name = ""
        try {
            if (multilingualTitle.equals("", ignoreCase = true)) {
                if (ObjectHelper.isSame(screenWidget!!.referenceName, AppConstants.ContentType.MY_WATCHLIST.name)) {
                    name = getString(R.string.my_watchlist)
                } else if (screenWidget != null && screenWidget.name != null) {
                    name = screenWidget.name.toString()
                }
            } else {
                name = multilingualTitle
            }
        } catch (ignored: Exception) {
        }
        return name
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (adapterNew != null) {
            binding!!.myRecyclerView.requestLayout()
            binding!!.myRecyclerView.adapter = null
            binding!!.myRecyclerView.layoutManager = null
            binding!!.myRecyclerView.adapter = adapterNew
            binding!!.myRecyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapterNew!!.notifyDataSetChanged()
        }
    }

    fun updateList() {
        if (preference != null && !preference!!.appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            for (i in railCommonDataList.indices) {
                if (railCommonDataList[i].isContinueWatching) {
                    railCommonDataList.removeAt(i)
                    adapterNew!!.notifyItemRemoved(i)
                }
            }
        }
    }

    fun updateAdList() {
        for (i in railCommonDataList.indices) {
            if (railCommonDataList[i].isAd) {
                Logger.w("isAdConfigurd$i  -->>", railCommonDataList[i].isAd.toString() + "")
                if (preference!!.entitlementStatus) {
                    railCommonDataList.removeAt(i)
                    adapterNew!!.notifyItemRemoved(i)
                }
            }
        }
    }

    override fun selectedTab(value: String) {
        intentFrom = value
        Log.d("intentFrom", "selectedTab: $intentFrom")
    }

    interface TabsBaseBackgroundHideCallBack {
        fun tabBackgroundHide(isBgHide: Boolean)
    }
}