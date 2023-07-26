package com.tv.uscreen.yojmatv.beanModel


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
import com.enveu.client.baseCollection.baseCategoryModel.BaseCategory
import com.enveu.client.enums.LandingPageType
import com.enveu.client.enums.Layouts
import com.enveu.client.enums.ListingLayoutType
import com.enveu.client.enums.PDFTarget
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.SDKConfig
import com.tv.uscreen.yojmatv.activities.homeactivity.TabIdInterface
import com.tv.uscreen.yojmatv.activities.listing.listui.ListActivity
import com.tv.uscreen.yojmatv.activities.listing.ui.GridActivity
import com.tv.uscreen.yojmatv.activities.privacypolicy.ui.WebViewActivity
import com.tv.uscreen.yojmatv.activities.search.ui.ActivitySearch
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.ActivityLogin
import com.tv.uscreen.yojmatv.activities.watchList.ui.WatchListActivity
import com.tv.uscreen.yojmatv.adapters.commonRails.CommonAdapterNew
import com.tv.uscreen.yojmatv.adapters.shimmer.ShimmerAdapter
import com.tv.uscreen.yojmatv.baseModels.BaseBindingFragment
import com.tv.uscreen.yojmatv.baseModels.HomeBaseViewModel
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.CommonApiCallBack
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.CommonRailtItemClickListner
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.MoreClickListner
import com.tv.uscreen.yojmatv.databinding.FragmentHomeBinding
import com.tv.uscreen.yojmatv.fragments.home.viewModel.HomeFragmentViewModel
import com.tv.uscreen.yojmatv.fragments.movies.viewModel.MovieFragmentViewModel
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.ObjectHelper
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod.Companion.configResponse
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod.Companion.getCheckBCID
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod.Companion.heroAssetRedirections
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod.Companion.redirectionLogic
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod.Companion.trackFcmEvent
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.cropImage.helpers.ShimmerDataModel
import com.tv.uscreen.yojmatv.utils.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.RecyclerAnimator
import com.tv.uscreen.yojmatv.utils.helpers.ToastHandler
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KidsModeSinglton
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

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
        callShimmer()
        if (activity != null) {
            preference = KsPreferenceKeys.getInstance()
        }
        setTabId()
        connectionObserver()
    }

    private fun setTabId() {
        try {
            if (configResponse.data != null && configResponse.data.appConfig != null && configResponse.data.appConfig.navScreens != null) {
                if (viewModel is HomeFragmentViewModel) {
                    tabId = SDKConfig.getInstance().firstTabId
                } else if (viewModel is MovieFragmentViewModel) {
                    tabId = SDKConfig.getInstance().secondTabId
                }
            } else {
                tabId = if (viewModel is HomeFragmentViewModel) {
                    AppConstants.HOME_ENVEU
                }  else if (viewModel is MovieFragmentViewModel) {
                    AppConstants.ORIGINAL_ENVEU
                } else {
                    AppConstants.HOME_ENVEU
                }
            }
        } catch (ex: Exception) {
            tabId = if (viewModel is HomeFragmentViewModel) {
                AppConstants.HOME_ENVEU
            }  else if (viewModel is MovieFragmentViewModel) {
                AppConstants.ORIGINAL_ENVEU
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
                } else if (screenWidget.name != null) {
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