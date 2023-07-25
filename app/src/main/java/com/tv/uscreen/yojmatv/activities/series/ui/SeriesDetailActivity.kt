package com.tv.uscreen.yojmatv.activities.series.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.jwplayer.PlayerActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.BaseOnTabSelectedListener
import com.google.gson.Gson
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.SDKConfig
import com.tv.uscreen.yojmatv.activities.detail.viewModel.DetailViewModel
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.ActivityLogin
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.EnterOTPActivity
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.PaymentDetailPage
import com.tv.uscreen.yojmatv.activities.usermanagment.viewmodel.RegistrationLoginViewModel
import com.tv.uscreen.yojmatv.adapters.player.EpisodeTabAdapter
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.entitle.ResponseEntitle
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModel.selectedSeason.SelectedSeasonModel
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.FirstEpisodeItem
import com.tv.uscreen.yojmatv.databinding.ActivitySeriesDetailBinding

import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogSingleButtonFragment
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment.Companion.newInstance
import com.tv.uscreen.yojmatv.fragments.player.ui.RelatedContentFragment
import com.tv.uscreen.yojmatv.fragments.player.ui.SeasonTabFragment
import com.tv.uscreen.yojmatv.fragments.player.ui.UserInteractionFragment
import com.tv.uscreen.yojmatv.networking.apistatus.APIStatus
import com.tv.uscreen.yojmatv.networking.responsehandler.ResponseModel
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.ObjectHelper
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.AppColors
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.cropImage.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class SeriesDetailActivity : BaseBindingActivity<ActivitySeriesDetailBinding?>(), AlertDialogFragment.AlertDialogListener, FirstEpisodeItem, CommonDialogFragment.EditDialogListener {
    private var seriesId = 0
    private var preference: KsPreferenceKeys? = null
    private var isLogin: String? = null
    private var shimmerCounter = 0
    private var seriesDetailBean: EnveuVideoItemBean? = null
    private var seasonTabFragment: SeasonTabFragment? = null
    private var relatedContentFragment: RelatedContentFragment? = null
    private var alertDialog: AlertDialog? = null
    private var episodeTabAdapter: EpisodeTabAdapter? = null
    private var newIntentCall = false
    @JvmField
    var isSeasonData = false
    @JvmField
    var isRailData = true
    private var userInteractionFragment: UserInteractionFragment? = null
    private var tabId: String? = null
    private var keyword: String? = ""
    private var isUserVerified: String? = null
    private var isUserNotVerify = false
    private var isUserNotEntitle = false
    private var isLoggedIn = false
    private var playbackUrl: String? = null
    private var viewModel: DetailViewModel? = null
    private var token: String? = null
    private var isPremium = false
    private var refId: String? = ""
    private var currentEpisodeId = 0
    private var tittle = ""
    private var posterUrl = ""
    private var assetType = ""
    private var sku = ""
    private var trailerUrl: String? = null
    private val isItemValueEmpty = false
    private val videoType = ""
    private val contentType = ""
    private val preferenceData = ""
    private val handler: Handler? = null
    private val runnable: Runnable? = null
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }
    private var registrationLoginViewModel: RegistrationLoginViewModel? = null
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivitySeriesDetailBinding {
        return ActivitySeriesDetailBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ThemeHandler.getInstance().applySeriesDetail(getApplicationContext(), getBinding());
        parserColor()
        window.setBackgroundDrawableResource(R.color.buy_now_pay_now_btn_text_color)
        shimmerCounter = 0
        preference = KsPreferenceKeys.getInstance()
        if (preference?.appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            isLoggedIn = true
        }
        tabId = if (SDKConfig.getInstance().seriesDetailId != null) {
            "10000"
        } else {
            SDKConfig.getInstance().seriesDetailId
        }
        isUserVerified = preference?.isVerified
        setupUI(binding!!.llParent)
        seriesId = intent.getIntExtra("seriesId", 0)
        onSeriesCreate()
        if (isLoggedIn) {
            hitUserProfileApi()
        }
        setClicks()
        binding?.metaDetails?.durationLl?.background = ColorsHelper.strokeBgDrawable(AppColors.detailPageHDBgColor(), AppColors.detailPageHDBrColor(), 10f)
    }

    private fun parserColor() {
        binding!!.stringData = stringsHelper
        binding!!.colorsData = colorsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
        binding!!.metaDetails.colorsData = colorsHelper
        binding!!.metaDetails.stringData = stringsHelper
    }

    override fun onPause() {
        dismissLoading(binding!!.progressBar)
        super.onPause()
    }

    private fun onSeriesCreate() {
        if (shimmerCounter == 0) {
            callShimmer()
        }
        connectionObserver()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        shimmerCounter = 0
        setupUI(binding!!.llParent)
        seriesId = intent.getIntExtra("seriesId", 0)
        newIntentCall = true
        onSeriesCreate()
        if (isLoggedIn) {
            hitUserProfileApi()
        }
    }

    private fun callShimmer() {
        shimmerCounter = 1
        binding!!.seriesShimmer.visibility = View.VISIBLE
        binding!!.mShimmer.colorsData = colorsHelper
        binding!!.mShimmer.seriesShimmerScroll1.isEnabled = false
        binding!!.mShimmer.seriesShimmerScroll2.isEnabled = false
        binding!!.mShimmer.seriesShimmerScroll2.isEnabled = false
        binding!!.noConnectionLayout.visibility = View.GONE
        binding!!.mShimmer.sfShimmer1.startShimmer()
        binding!!.mShimmer.sfShimmer2.startShimmer()
        binding!!.mShimmer.sfShimmer3.startShimmer()
        binding!!.mShimmer.flBackIconImage.bringToFront()
        binding!!.mShimmer.flBackIconImage.setOnClickListener { onBackPressed() }
    }

    fun stopShimmer() {
        if (isSeasonData) {
            isSeasonData = false
            // isRailData = false;
            binding!!.seriesShimmer.visibility = View.GONE
            binding!!.llParent.visibility = View.VISIBLE
            binding!!.noConnectionLayout.visibility = View.GONE
            binding!!.mShimmer.sfShimmer1.startShimmer()
            binding!!.mShimmer.sfShimmer2.startShimmer()
            binding!!.mShimmer.sfShimmer3.startShimmer()
            val aniFade = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
            binding!!.llParent.startAnimation(aniFade)
            //  setExpandable();
        }
    }

    private fun modelCall() {
        binding!!.noConnectionLayout.visibility = View.GONE
        binding!!.player.visibility = View.VISIBLE
        binding!!.playerFooter.visibility = View.VISIBLE
        binding!!.flBackIconImage.visibility = View.VISIBLE
        binding!!.backImage.bringToFront()
        binding!!.flBackIconImage.bringToFront()
        isLogin = preference?.appPrefLoginStatus
    }

    private fun connectionObserver() {
        connectionValidation(NetworkConnectivity.isOnline(this))
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (java.lang.Boolean.TRUE == aBoolean) {
            seriesDetail
        } else {
            noConnectionLayout()
        }
    }

    override fun onResume() {
        super.onResume()
        dismissLoading(binding!!.progressBar)
        AppCommonMethod.isSeriesPage = true
        if (NetworkConnectivity.isOnline(this)) {
            Logger.d("isOnline")
            setUserInteractionFragment(seriesId)
        } else {
            noConnectionLayout()
        }
        if (preference != null && userInteractionFragment != null) {
            AppCommonMethod.callSocialAction(preference!!, userInteractionFragment)
        }
    }

    var railInjectionHelper: RailInjectionHelper? = null
    private val seriesDetail: Unit
        private get() {
            modelCall()
            railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
            railInjectionHelper!!.getSeriesDetailsV2(seriesId.toString(), false).observe(this@SeriesDetailActivity) { response: ResponseModel<*>? ->
                if (response != null) {
                    if (response.status.equals(APIStatus.START.name, ignoreCase = true)) {
                    } else if (response.status.equals(APIStatus.SUCCESS.name, ignoreCase = true)) {
                        if (response.baseCategory != null) {
                            val enveuCommonResponse = response.baseCategory as RailCommonData
                            parseSeriesData(enveuCommonResponse)
                            val gson = Gson()
                            val json = gson.toJson(enveuCommonResponse)
                            Log.d("javed", "getSeriesDetail1: $json")
                        }
                    } else if (response.status.equals(APIStatus.ERROR.name, ignoreCase = true)) {
                        if (response.errorModel.errorCode != 0) {
                            if (response.errorModel.errorCode == AppConstants.RESPONSE_CODE_LOGOUT) {
                                if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                                    hitApiLogout()
                                }
                            } else {
                                showDialog(
                                    stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                                    stringsHelper.stringParse(
                                        stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong)
                                    )
                                )
                            }
                        }
                    } else if (response.status.equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                        showDialog(
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong)
                            )
                        )
                    }
                }
            }
            binding!!.flBackIconImage.setOnClickListener { onBackPressed() }
        }

    private fun parseSeriesData(enveuCommonResponse: RailCommonData?) {
        try {
            if (enveuCommonResponse != null && enveuCommonResponse.enveuVideoItemBeans.isNotEmpty()) {
                keyword = enveuCommonResponse.enveuVideoItemBeans[0].display_tags
                seriesDetailBean = enveuCommonResponse.enveuVideoItemBeans[0]
                seriesId = seriesDetailBean?.id!!
                setUserInteractionFragment(seriesId)
                setUiComponents(seriesDetailBean)
                setTabs()
                binding!!.tabLayout.isTabIndicatorFullWidth = true
                binding!!.tabLayout.tabMode = TabLayout.MODE_FIXED
                binding!!.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
                val params = binding!!.tabLayout.layoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
                binding!!.tabLayout.layoutParams = params
            } else {
                if (enveuCommonResponse != null && ObjectHelper.isNotEmpty(enveuCommonResponse.enveuVideoItemBeans)
                    && (enveuCommonResponse.enveuVideoItemBeans[0].responseCode
                            == AppConstants.RESPONSE_CODE_LOGOUT)
                ) {
                    if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                        hitApiLogout()
                    }
                } else {
                    showDialog(
                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong))
                    )
                }
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    private fun setUserInteractionFragment(id: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        val args = Bundle()
        args.putInt(AppConstants.BUNDLE_ASSET_ID, id)
        args.putSerializable(AppConstants.BUNDLE_SERIES_DETAIL, seriesDetailBean)
        // args.putString(AppConstants.BUNDLE_TRAILER_REF_ID, seriesDetailBean.getTrailerReferenceId());
        userInteractionFragment = UserInteractionFragment()
        userInteractionFragment!!.arguments = args
        transaction.replace(R.id.fragment_user_interaction, userInteractionFragment!!)
        transaction.addToBackStack(null)
        transaction.commit()
        // userInteractionFragment.setDownloadable(false);
    }

    private fun setTabs() {
        binding!!.tabLayout.setTabTextColors(
            AppColors.detailPageTabItemUnselectedTxtColor(),
            AppColors.detailPageTabItemSelectedTxtColor(),
        )
        if (newIntentCall) {
            newIntentCall = false
            val args = Bundle()
            args.putString(AppConstants.BUNDLE_TAB_ID, tabId)
            val bundleSeason = Bundle()
            bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, seriesId)
            bundleSeason.putParcelableArrayList(AppConstants.BUNDLE_SEASON_ARRAY, seriesDetailBean?.seasons as java.util.ArrayList<out Parcelable>?)
            bundleSeason.putString(AppConstants.BUNDLE_SEASON_NAME, seriesDetailBean!!.seasonName)
            bundleSeason.putInt(AppConstants.BUNDLE_SEASON_COUNT, seriesDetailBean!!.seasonCount)
            seasonTabFragment!!.arguments = bundleSeason

            //  forYouThisFragment = new ForYouThisFragment();
            val relatedContentArgs = Bundle()
            relatedContentArgs.putInt(AppConstants.ID, seriesId)
            relatedContentArgs.putString("videoType", AppConstants.SERIES)
            relatedContentArgs.putString("contentType", AppConstants.VIDEO)
            /*  relatedContentArgs.putString("preferenceData",preferenceData);
            Log.d("tag", "preferenceData-2: " + preferenceData);
*/relatedContentFragment!!.arguments = relatedContentArgs
            seasonTabFragment!!.resetAdapter()
            relatedContentFragment!!.resetAdapter()
            supportFragmentManager.beginTransaction().detach(seasonTabFragment!!).commit()
            supportFragmentManager.beginTransaction().detach(relatedContentFragment!!).commit()
            supportFragmentManager.beginTransaction().attach(seasonTabFragment!!).commit()
            supportFragmentManager.beginTransaction().attach(relatedContentFragment!!).commit()
            val tab = binding!!.tabLayout.getTabAt(0)
            tab!!.select()
        } else {
            seasonTabFragment = SeasonTabFragment()
            relatedContentFragment = RelatedContentFragment()
            binding!!.tabLayout.setSelectedTabIndicatorGravity(TabLayout.INDICATOR_GRAVITY_TOP)
            episodeTabAdapter = EpisodeTabAdapter(supportFragmentManager)
            val args = Bundle()
            args.putString(AppConstants.BUNDLE_TAB_ID, tabId)
            val bundleSeason = Bundle()
            bundleSeason.putInt(AppConstants.BUNDLE_ASSET_ID, seriesId)
            bundleSeason.putParcelableArrayList(AppConstants.BUNDLE_SEASON_ARRAY, seriesDetailBean?.seasons as java.util.ArrayList<out Parcelable>?)
            bundleSeason.putString(AppConstants.BUNDLE_SEASON_NAME, seriesDetailBean!!.seasonName)
            bundleSeason.putInt(AppConstants.BUNDLE_SEASON_COUNT, seriesDetailBean!!.seasonCount)
            seasonTabFragment!!.arguments = bundleSeason
            episodeTabAdapter!!.addFragment(
                seasonTabFragment,
                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.detail_page_episodes.toString(), getString(R.string.detail_page_episodes))
            )
            relatedContentFragment = RelatedContentFragment()
            val forYouArgs = Bundle()
            forYouArgs.putInt(AppConstants.ID, seriesId)
            forYouArgs.putString("videoType", AppConstants.SERIES)
            forYouArgs.putString("contentType", AppConstants.VIDEO)
            forYouArgs.putString("preferenceData", preferenceData)
            Log.d("tag", "preferenceData-2: $preferenceData")
            relatedContentFragment!!.arguments = forYouArgs
            episodeTabAdapter!!.addFragment(
                relatedContentFragment,
                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.detail_page_related_videos.toString(), getString(R.string.detail_page_related_videos))
            )
            binding!!.viewPager.adapter = episodeTabAdapter
            binding!!.viewPager.offscreenPageLimit = 10
            binding!!.tabLayout.setupWithViewPager(binding!!.viewPager)
        }
//        val borderClr = colorParser(colorsHelper.instance()?.data?.config?.series_detail_tab_unselected_border_color.toString(), R.color.series_detail_tab_unselected_border_color)
//        val bgClr = colorParser(colorsHelper.instance()?.data?.config?.app_bg_color.toString(), R.color.app_bg_color)

        binding?.metaDetails?.watchTrailer?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        binding?.tabLayout?.getTabAt(0)?.view?.background = ColorsHelper.strokeBgDrawable(AppColors.detailPageTabUnselectedBorderColor(), AppColors.detailPageTabUnselectedBorderColor(), 0f)
        if (episodeTabAdapter!!.count > 1) {
            binding?.tabLayout?.getTabAt(1)?.view?.background = ColorsHelper.strokeBgDrawable(AppColors.detailPageTabSelectedBorderColor(), AppColors.detailPageTabUnselectedBorderColor(), 0f)
        }
        binding!!.tabLayout.addOnTabSelectedListener(object : BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                showLoading(binding!!.progressBar, true)
                tab?.view?.background = ColorsHelper.strokeBgDrawable(AppColors.detailPageTabUnselectedBorderColor(), AppColors.detailPageTabUnselectedBorderColor(), 0f)
                Handler().postDelayed({ dismissLoading(binding!!.progressBar) }, 1500)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.view?.background = ColorsHelper.strokeBgDrawable(AppColors.detailPageTabSelectedBorderColor(), AppColors.detailPageTabUnselectedBorderColor(), 0f)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        binding!!.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                binding!!.viewPager.measure(binding!!.viewPager.measuredWidth, binding!!.viewPager.measuredHeight)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        changeTabsFont()
    }

    private fun changeTabsFont() {
        try {
            val vg = binding!!.tabLayout.getChildAt(0) as ViewGroup
            val tabsCount = vg.childCount
            for (j in 0 until tabsCount) {
                val vgTab = vg.getChildAt(j) as ViewGroup
                val tabChildsCount = vgTab.childCount
                for (i in 0 until tabChildsCount) {
                    val tabViewChild = vgTab.getChildAt(i)
                    if (tabViewChild is TextView) {
                        tabViewChild.setSingleLine()
                    }
                }
            }
        } catch (ignored: Exception) {
        }
    }

    fun seriesLoader() {
        showLoading(binding!!.progressBar, true)
    }

    fun removeTab(position: Int) {
        if (binding!!.tabLayout.tabCount >= 1 && position <= binding!!.tabLayout.tabCount) {
            episodeTabAdapter!!.removeTabPage(position)
            val params = binding!!.tabLayout.layoutParams
            params.width = resources.getDimension(R.dimen.tab_layout_single).toInt()
            binding!!.tabLayout.layoutParams = params
        }
    }

    private var seasonEpisodesList: MutableList<EnveuVideoItemBean>? = null
    fun episodesList(seasonEpisodes: List<EnveuVideoItemBean>?) {
        if (!seasonEpisodes.isNullOrEmpty()) {
            seasonEpisodesList = ArrayList()
            seasonEpisodesList?.addAll(seasonEpisodes)
            val episodes = stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.detail_page_episodes.toString(),
                getString(R.string.detail_page_episodes)
            ) + " " + seasonEpisodes.size + ""
            binding!!.metaDetails.playButton.visibility = View.VISIBLE
        } else {
            binding!!.metaDetails.playButton.visibility = View.INVISIBLE
            //  isItemValueEmpty = true;
            //  commonDialog(getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong), getResources().getString(R.string.continue));
        }
    }

    override fun onStop() {
        super.onStop()
        AppCommonMethod.isSeriesPage = false
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
            AppCommonMethod.seasonId = -1
            preference!!.appPrefAssetId = 0
            preference!!.appPrefJumpTo = ""
            preference!!.appPrefBranchIo = false
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    private fun noConnectionLayout() {
        stopShimmer()
        binding!!.llParent.visibility = View.GONE
        binding!!.noConnectionLayout.visibility = View.VISIBLE
        binding!!.connection.retryTxt.setOnClickListener {
            callShimmer()
            connectionObserver()
        }
    }

    private fun showDialog(title: String, message: String) {
        val fm = supportFragmentManager
        val alertDialog = AlertDialogSingleButtonFragment.newInstance(
            title,
            message,
            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(), getString(R.string.popup_ok))
        )
        alertDialog.isCancelable = false
        alertDialog.setAlertDialogCallBack(this)
        alertDialog.show(fm, "fragment_alert")
    }

    override fun onBackPressed() {
        if (preference!!.appPrefJumpBack) {
            preference!!.appPrefJumpBackId = 0
            preference!!.appPrefJumpBack = false
        }
        AppCommonMethod.seasonId = -1
        AppCommonMethod.isSeasonCount = false
        finish()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Logger.d("ORIENTATION_LANDSCAPE")
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Logger.d("ORIENTATION_PORTRAIT")
        }
    }

    override fun onFinishDialog() {
        if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            hitApiLogout()
        }
        finish()
    }

    fun hitApiLogout() {
        if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            hitApiLogout(this@SeriesDetailActivity, preference!!.appPrefAccessToken)
        }
    }

    fun openLogin() {
        preference!!.appPrefJumpTo = resources.getString(R.string.series)
        preference!!.appPrefJumpBack = true
        preference!!.appPrefJumpBackId = seriesId
        ActivityLauncher.getInstance().loginActivity(
            this@SeriesDetailActivity,
            ActivityLogin::class.java
        )
    }

    fun showSeasonList(list: ArrayList<SelectedSeasonModel>, selectedSeasonId: Int) {
        binding!!.transparentLayout.visibility = View.VISIBLE
        val listAdapter = SeasonListAdapter(list, selectedSeasonId)
        val builder = AlertDialog.Builder(this@SeriesDetailActivity)
        val inflater = LayoutInflater.from(this@SeriesDetailActivity)
        val content = inflater.inflate(R.layout.season_custom_dialog, null)
        builder.setView(content)
        val mRecyclerView = content.findViewById<RecyclerView>(R.id.my_recycler_view)
        val imageView = content.findViewById<ImageView>(R.id.close)
        imageView.setOnClickListener {
            alertDialog!!.cancel()
            binding!!.transparentLayout.visibility = View.GONE
        }

        //Creating Adapter to fill data in Dialog
        mRecyclerView.layoutManager = LinearLayoutManager(this@SeriesDetailActivity)
        mRecyclerView.adapter = listAdapter
        alertDialog = builder.create()
        alertDialog?.window!!.setBackgroundDrawable(ActivityCompat.getDrawable(this@SeriesDetailActivity, R.color.transparent))
        alertDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (alertDialog?.window != null) alertDialog?.window!!.attributes.windowAnimations = R.style.SlidingDialogAnimation
        alertDialog?.show()
        val lWindowParams = WindowManager.LayoutParams()
        lWindowParams.copyFrom(alertDialog?.window!!.attributes)
        lWindowParams.width = ViewGroup.LayoutParams.MATCH_PARENT // this is where the magic happens
        lWindowParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        alertDialog?.window!!.attributes = lWindowParams
    }

    internal inner class SeasonListAdapter(private val list: ArrayList<SelectedSeasonModel>, selectedPos: Int) : RecyclerView.Adapter<SeasonListAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.all_season_listing, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.season.text = list[position].list
            if (list[position].isSelected) {
                holder.season.setTextColor(
                    ContextCompat.getColor(
                        holder.season.context,
                        R.color.series_detail_description_text_color
                    )
                )
                val boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD)
                holder.season.typeface = boldTypeface
            } else {
                holder.season.setTextColor(
                    ContextCompat.getColor(
                        holder.season.context,
                        R.color.transparent
                    )
                )
                val boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                holder.season.typeface = boldTypeface
            }
            holder.season.setOnClickListener {
                alertDialog!!.cancel()
                binding!!.transparentLayout.visibility = View.GONE
                if (seasonTabFragment != null) {
                    seasonTabFragment!!.updateTotalPages()
                    seasonTabFragment!!.seasonAdapter = null
                    seasonTabFragment!!.selectedSeason = list[position].selectedId
                    showLoading(binding!!.progressBar, true)
                    seasonTabFragment!!.getSeasonEpisodes()
                }
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var season: TextView

            init {
                season = itemView.findViewById(R.id.season_name)
            }
        }
    }

    private var singleItem: EnveuVideoItemBean? = null
    override fun getFirstItem(itemValue: EnveuVideoItemBean) {
        singleItem = itemValue
        if (itemValue != null) {
            // setUserInteractionFragment(seriesId);
            isPremium = itemValue.isPremium
            currentEpisodeId = itemValue.id
            tittle = itemValue.title
            assetType = itemValue.assetType
            if (itemValue.externalRefId != null) {
                refId = itemValue.externalRefId
            }
            if (itemValue.sku != null) {
                sku = itemValue.sku
            }
            Log.d("getFirstItem", "getFirstItem2: $itemValue")
            //   setUiComponents(itemValue);
        }
    }

    private fun setUiComponents(seriesResponse: EnveuVideoItemBean?) {
        //  stopShimmer();
        Log.d("getFirstItem", "seriesResponse: $seriesResponse")
        if (seriesResponse != null) {
            val properties = Properties()
            properties.addAttribute(AppConstants.CONTENT_DETAIL_TITTLE, seriesResponse.title)
            MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties)
            if (seriesResponse.trailerReferenceId != null) {
                getTrailer(seriesResponse.trailerReferenceId)
            }
            setUserInteractionFragment(seriesId)
            // setCustomFields(itemValue, 0, getResources().getString(R.string.episode));
            binding!!.playlistItem = seriesResponse
            posterUrl = seriesResponse.posterURL
            ImageHelper.getInstance(this).loadListImage(binding!!.sliderImage, AppCommonMethod.getListLDSImage(seriesResponse.posterURL, this))
            //  ImageHelper.getInstance(SeriesDetailActivity.this).loadListImage(getBinding().sliderImage, AppCommonMethod.getListLDSImage(seriesResponse.getPosterURL(), SeriesDetailActivity.this));
            if (ObjectHelper.isEmpty(seriesResponse.description)) {
                binding!!.metaDetails.descriptionText.visibility = View.GONE
            }
            binding!!.responseApi = seriesResponse.description.trim { it <= ' ' }
            Logger.d("SeriesResponse: $seriesResponse")
            if (seriesResponse.title != null) {
                binding!!.metaDetails.tvTitle.text = seriesResponse.title
            } else {
                binding!!.metaDetails.tvTitle.visibility = View.GONE
            }
            if (seriesResponse.description != null) {
                binding!!.metaDetails.descriptionText.text = seriesResponse.description
            } else {
                binding!!.metaDetails.descriptionText.visibility = View.GONE
            }
            if (keyword != null) {
                keyword = keyword!!.replace(",".toRegex(), " \u25AA ")
                binding!!.metaDetails.time.text = keyword
            } else {
                binding!!.metaDetails.time.visibility = View.GONE
            }
            if (!ObjectHelper.isEmpty(seriesResponse.quality)) {
                binding!!.metaDetails.qualityPic.text = seriesResponse.quality
            } else {
                binding!!.metaDetails.durationLl.visibility = View.GONE
            }
            if (seriesResponse.producer != null) {
                var producer = seriesResponse.producer
                producer = producer.replace(",".toRegex(), " \u25AA ")
                binding!!.metaDetails.producedDescrption.text = producer
            } else {
                binding!!.metaDetails.producedDescrption.visibility = View.GONE
                binding!!.metaDetails.produced.visibility = View.GONE
            }
            if (seriesResponse.sponsors != null) {
                var sponsors = seriesResponse.sponsors
                sponsors = sponsors.replace(",".toRegex(), " \u25AA ")
                binding!!.metaDetails.sponseredDescription.text = sponsors
            } else {
                binding!!.metaDetails.sponseredDescription.visibility = View.GONE
                binding!!.metaDetails.sponsered.visibility = View.GONE
            }
            /* String duration1 = "";
            if (seriesResponse.getVideoDetails().getDuration()!=null) {
                duration1 = String.valueOf(seriesResponse.getVideoDetails().getDuration());
            }
            String properDuration = "";
            if (!StringUtils.isNullOrEmpty(duration1)) {
                properDuration = AppCommonMethod.convertToMinutes(Long.parseLong((duration1)))+" "+getResources().getString(R.string.minutes);
                setTextOrHide(getBinding().metaDetails.duration, properDuration);
            } else {
                getBinding().metaDetails.duration.setVisibility(View.GONE);
            }
*/
        }
    }

    private var resEntitle: ResponseEntitle? = null
    private fun setClicks() {
        token = preference!!.appPrefAccessToken
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        binding!!.metaDetails.watchTrailer.setOnClickListener {
            if (isLoggedIn) {
                if (isUserVerified.equals("true", ignoreCase = true)) {
                    startPlayer(trailerUrl, bingeWatchEnable = false, isTrailer = true)
                } else {
                    isUserNotVerify = true
                    commonDialog(
                        "",
                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_user_not_verify.toString(), getString(R.string.popup_user_not_verify)),
                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_verify.toString(), getString(R.string.popup_verify))
                    )
                }
            } else {
                ActivityLauncher.getInstance().loginActivity(this@SeriesDetailActivity, ActivityLogin::class.java)
            }
        }
        try {
            binding!!.metaDetails.playButton.setOnClickListener {
                if (isLoggedIn) {
                    if (!isPremium) {
                        if (isUserVerified.equals("true", ignoreCase = true)) {
                            if (null != refId && !refId.equals("", ignoreCase = true)) {
                                playbackUrl = SDKConfig.getInstance().playbacK_URL + refId + ".m3u8"
                                startPlayer(playbackUrl, KsPreferenceKeys.getInstance().bingeWatchEnable, false)
                            }
                        } else {
                            isUserNotVerify = true
                            commonDialog(
                                "",
                                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_user_not_verify.toString(), getString(R.string.popup_user_not_verify)),
                                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_verify.toString(), getString(R.string.popup_verify))
                            )
                        }
                    } else {
                        binding!!.progressBar.visibility = View.VISIBLE
                        viewModel!!.hitApiEntitlement(token, sku).observe(this@SeriesDetailActivity) { responseEntitle ->
                            binding!!.progressBar.visibility = View.GONE
                            if (responseEntitle != null && responseEntitle.data != null) {
                                resEntitle = responseEntitle
                                if (responseEntitle.data.entitled) {
                                    if (isUserVerified.equals("true", ignoreCase = true)) {
                                        if (null != responseEntitle.data.externalRefId && !responseEntitle.data.externalRefId.equals("", ignoreCase = true)) {
                                            playbackUrl = SDKConfig.getInstance().playbacK_URL + responseEntitle.data.externalRefId + ".m3u8"
                                            startPlayer(playbackUrl, KsPreferenceKeys.getInstance().bingeWatchEnable, false)
                                        }
                                    } else {
                                        isUserNotVerify = true
                                        commonDialog(
                                            "",
                                            stringsHelper.stringParse(
                                                stringsHelper.instance()?.data?.config?.popup_user_not_verify.toString(),
                                                getString(R.string.popup_user_not_verify)
                                            ),
                                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_verify.toString(), getString(R.string.popup_verify))
                                        )
                                    }
                                } else {
                                    isUserNotEntitle = true
                                    commonDialog(
                                        "",
                                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_select_plan.toString(), getString(R.string.popup_select_plan)),
                                        stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_purchase.toString(), getString(R.string.popup_purchase)
                                        )
                                    )
                                }
                            } else {
                                if (responseEntitle!!.responseCode != null && responseEntitle.responseCode == 4302) {
                                    clearCredientials(preference)
                                    ActivityLauncher.getInstance().loginActivity(this@SeriesDetailActivity, ActivityLogin::class.java)
                                } else {
                                    commonDialog(
                                        stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                                        stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong)
                                        ),
                                        stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)
                                        )
                                    )
                                }
                            }
                        }
                    }
                } else {
                    ActivityLauncher.getInstance().loginActivity(this@SeriesDetailActivity, ActivityLogin::class.java)
                }
            }
        } catch (e: Exception) {
            Logger.e(e)
        }
    }

    private fun startPlayer(playback_url: String?, bingeWatchEnable: Boolean, isTrailer: Boolean) {
        Log.d("playback_url", "startPlayer: $playback_url")
        ActivityLauncher.getInstance().launchPlayerActitivity(
            this@SeriesDetailActivity,
            PlayerActivity::class.java,
            playback_url,
            bingeWatchEnable,
            seasonEpisodesList,
            currentEpisodeId,
            tittle,
            assetType,
            isTrailer,
            false,
            posterUrl,
            AppConstants.SERIESDEATILACTIVITY
        )
    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm = supportFragmentManager
        val commonDialogFragment = newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }

    override fun onActionBtnClicked() {
        /* if (isItemValueEmpty){
            onBackPressed();
        }*/
        if (isUserNotVerify) {
            ActivityLauncher.getInstance().goToEnterOTP(this, EnterOTPActivity::class.java, "DetailPage")
        }
        if (isUserNotEntitle) {
            ActivityLauncher.getInstance().goToDetailPlanScreen(this, PaymentDetailPage::class.java, true, resEntitle)
        }
    }

    private fun getTrailer(trailerReferenceId: String) {
        railInjectionHelper = ViewModelProvider(this)[RailInjectionHelper::class.java]
        railInjectionHelper!!.getAssetDetailsV2(trailerReferenceId, this@SeriesDetailActivity).observe(this@SeriesDetailActivity) { assetResponse: ResponseModel<*>? ->
            if (assetResponse != null) {
                val gson = Gson()
                val json = gson.toJson(assetResponse.baseCategory)
                if (assetResponse.status.equals(APIStatus.START.name, ignoreCase = true)) {
                } else if (assetResponse.status.equals(APIStatus.SUCCESS.name, ignoreCase = true)) {
                    val enveuCommonResponse = assetResponse.baseCategory as RailCommonData
                    if (enveuCommonResponse != null && enveuCommonResponse.enveuVideoItemBeans.size > 0) {
                        // videoDetails = enveuCommonResponse.getEnveuVideoItemBeans().get(0);
                        if (!enveuCommonResponse.enveuVideoItemBeans[0].externalRefId.equals("", ignoreCase = true) && !enveuCommonResponse.enveuVideoItemBeans[0].externalRefId.equals(
                                null,
                                ignoreCase = true
                            )
                        ) {
                            binding!!.metaDetails.watchTrailer.visibility = View.VISIBLE
                            trailerUrl = SDKConfig.getInstance().playbacK_URL + enveuCommonResponse.enveuVideoItemBeans[0].externalRefId + ".m3u8"
                        }
                    }
                } else if (assetResponse.status.equals(APIStatus.ERROR.name, ignoreCase = true)) {
                    if (assetResponse.errorModel != null && assetResponse.errorModel.errorCode != 0) {
                        showDialog(
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong)
                            )
                        )
                    }
                } else if (assetResponse.status.equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                    // showDialog(SeriesDetailActivity.this.getResources().getString(R.string.error), getResources().getString(R.string.something_went_wrong));
                }
            }
        }
    }

    private fun hitUserProfileApi() {
        registrationLoginViewModel = ViewModelProvider(this)[RegistrationLoginViewModel::class.java]
        registrationLoginViewModel!!.hitUserProfile(this@SeriesDetailActivity, preference!!.appPrefAccessToken).observe(this@SeriesDetailActivity) { userProfileResponse ->
            dismissLoading(binding!!.progressBar)
            if (userProfileResponse != null) {
                if (userProfileResponse.data != null) {
                }
                if (userProfileResponse.status) {
                } else {
                    if (userProfileResponse.responseCode == 4302) {
                        clearCredientials(preference)
                        isLoggedIn = false
                    }
                }
            }
        }
    }
}