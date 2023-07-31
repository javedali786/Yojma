package com.tv.uscreen.yojmatv.activities.homeactivity.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.databinding.ActivityMainBinding
import com.tv.uscreen.yojmatv.fragments.home.ui.HomeFragment
import com.tv.uscreen.yojmatv.fragments.more.ui.MoreFragment
import com.tv.uscreen.yojmatv.fragments.movies.ui.MovieFragment
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.AppColors
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.AnalyticsController
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils
import com.tv.uscreen.yojmatv.utils.helpers.ToolBarHandler
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.inAppUpdate.AppUpdateCallBack
import com.tv.uscreen.yojmatv.utils.inAppUpdate.ApplicationUpdateManager
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class HomeActivity : BaseBindingActivity<ActivityMainBinding?>(), AppUpdateCallBack {
    private var active: Fragment? = null
    private var preference: KsPreferenceKeys? = null
    private var homeFragment: Fragment? = null
    private var reelsFragment: Fragment? = null
    private var gamingFragment: Fragment? = null
    private var movieFragment: Fragment? = null
    private var podcastFragment: Fragment? = null
    private var moreFragment: Fragment? = null
    private var position = 0
    private var fragmentManager: FragmentManager? = null
    private val colorsHelper by lazy { ColorsHelper }
    private val stringsHelper by lazy { StringsHelper }

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val strCurrentTheme = KsPreferenceKeys.getInstance().currentTheme
        binding!!.toolbar.colorsData = colorsHelper
        binding!!.colorsData = colorsHelper
        val extras = intent
        if (extras != null) {
            position = intent.getIntExtra(AppConstants.HOME_TAG, 0)
        }
        Logger.d("CurrentThemeIs", strCurrentTheme)
        if (AppConstants.LIGHT_THEME.equals(strCurrentTheme, ignoreCase = true)) {
            setTheme(R.style.MyMaterialTheme_Base_Light)
        } else {
            setTheme(R.style.MyMaterialTheme_Base_Dark)
        }
        preference = KsPreferenceKeys.getInstance()
        callBinding()
        ApplicationUpdateManager.getInstance(applicationContext).setAppUpdateCallBack(this)
        ApplicationUpdateManager.getInstance(applicationContext).appUpdateManager.registerListener(listener)
        ApplicationUpdateManager.getInstance(applicationContext).isUpdateAvailable()
        ApplicationUpdateManager.getInstance(applicationContext).appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate()
                ApplicationUpdateManager.getInstance(applicationContext).appUpdateManager.unregisterListener(listener)
            }
        }
        AnalyticsController(this@HomeActivity).callAnalytics("home_activity", "Action", "Launch")
    }


    private fun callBinding() {
        callInitials()
    }

    private fun callInitials() {
        if (StringUtils.isNullOrEmptyOrZero(AppCommonMethod.urlPoints)) {
            AppCommonMethod.urlPoints = preference!!.appPrefCfep
        }
        initialFragment()
        ToolBarHandler.getInstance().setHomeAction(binding!!.toolbar, this@HomeActivity)
    }

    private val navigationItemListener = NavigationBarView.OnItemSelectedListener { item: MenuItem ->
        when (item.itemId) {
            R.id.navigation_home -> {
                if (active !is HomeFragment) {
                    if (homeFragment == null) {
                        homeFragment = HomeFragment()
                        fragmentManager!!.beginTransaction().add(R.id.content_frame, homeFragment as HomeFragment, "0").commit()
                    }
                    switchToHomeFragment()
                }
                return@OnItemSelectedListener true
            }

            R.id.navigation_movie -> {
                if (active !is MovieFragment) {
                    if (movieFragment == null) {
                        movieFragment = MovieFragment()
                        fragmentManager!!.beginTransaction().add(R.id.content_frame, movieFragment as MovieFragment, "2")
                            .hide(movieFragment as MovieFragment).commit()
                    }
                    switchToMovieFragment()
                }
                return@OnItemSelectedListener true
            }

            R.id.navigation_more -> {
                if (moreFragment != null) {
                    switchToMoreFragment()
                } else {
                    moreFragment = MoreFragment()
                    fragmentManager!!.beginTransaction().add(R.id.content_frame, moreFragment as MoreFragment, "4")
                        .hide(moreFragment as MoreFragment).commit()
                    switchToMoreFragment()
                    Handler(Looper.getMainLooper()).postDelayed({ (moreFragment as MoreFragment).clickEvent() }, 200)
                }
                return@OnItemSelectedListener true
            }

            else -> false
        }
    }

    private fun switchToHomeFragment() {
        binding!!.toolbar.llSearchIcon.visibility = View.VISIBLE
        fragmentManager!!.beginTransaction().hide(active!!).show(homeFragment!!).commit()
        active = homeFragment
    }

    private fun switchToReelsFragment() {
        binding!!.toolbar.llSearchIcon.visibility = View.VISIBLE
        fragmentManager!!.beginTransaction().hide(active!!).show(reelsFragment!!).commit()
        active = reelsFragment
    }

    private fun switchToGamingFragment() {
        binding!!.toolbar.llSearchIcon.visibility = View.VISIBLE
        fragmentManager!!.beginTransaction().hide(active!!).show(gamingFragment!!).commit()
        active = gamingFragment
    }

    private fun switchToPodCastFragment() {
        binding!!.toolbar.llSearchIcon.visibility = View.VISIBLE
        fragmentManager!!.beginTransaction().hide(active!!).show(podcastFragment!!).commit()
        active = podcastFragment
    }

    private fun switchToMoreFragment() {
        binding!!.toolbar.llSearchIcon.visibility = View.GONE
        binding!!.toolbar.clNotification.visibility = View.GONE
        fragmentManager!!.beginTransaction().hide(active!!).show(moreFragment!!).commit()
        active = moreFragment
    }

    private fun switchToMovieFragment() {
        binding!!.toolbar.llSearchIcon.visibility = View.VISIBLE
        fragmentManager!!.beginTransaction().hide(active!!).show(movieFragment!!).commit()
        active = movieFragment
    }

    private fun initialFragment() {
        homeFragment = HomeFragment()
        active = homeFragment
        fragmentManager = supportFragmentManager
        fragmentManager!!.beginTransaction().add(R.id.content_frame, homeFragment as HomeFragment, "1").hide(homeFragment as HomeFragment).commitAllowingStateLoss()
        fragmentManager!!.beginTransaction().hide(active!!).show(homeFragment as HomeFragment).commitAllowingStateLoss()
        uiInitialisation()
    }

    private fun uiInitialisation() {
        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        binding!!.toolbar.rlToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.buy_now_pay_now_btn_text_color))
        binding!!.toolbar.homeIconKids.visibility = View.GONE
        binding!!.toolbar.homeIcon.visibility = View.GONE
        binding!!.toolbar.backLayout.visibility = View.GONE

        bottomNavigationTextFromJson(navigation, R.id.navigation_home, stringsHelper.instance()?.data?.config?.home_tabbar.toString(), R.string.home_tabbar)
        bottomNavigationTextFromJson(navigation, R.id.navigation_movie, stringsHelper.instance()?.data?.config?.movie_tabbar.toString(), R.string.movie_tabbar)
        bottomNavigationTextFromJson(navigation, R.id.navigation_more, stringsHelper.instance()?.data?.config?.more_tabbar.toString(), R.string.more_tabbar)

        parseColor(navigation)
        navigation.setOnItemSelectedListener(navigationItemListener)
    }

    private fun parseColor(navigation: BottomNavigationView) {
        navigation.background = colorsHelper.strokeBgDrawable(AppColors.appBgColor(), AppColors.appBgColor(), 0f)

        val textColorStates = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(
                AppColors.bottomNavUnselectedTextColor(),
                AppColors.bottomNavSelectedTextColor()
            )
        )
        navigation.itemTextColor = textColorStates
        val iconColorStates = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)), intArrayOf(
                AppColors.bottomNavUnselectedIconColor(),
                AppColors.bottomNavSelectedIconColor(),
            )
        )
        navigation.itemIconTintList = iconColorStates
    }

    private fun bottomNavigationTextFromJson(navigation: BottomNavigationView, itemID: Int, jsonString: String, localString: Int) {
        val itemTitle = navigation.menu.findItem(itemID)
        val itemTabBar: String = if (jsonString != null && jsonString != "null" && jsonString != "") {
            jsonString
        } else {
            getString(localString)
        }
        itemTitle.title = itemTabBar
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        if (preference == null) preference = KsPreferenceKeys.getInstance()
        preference!!.appPrefIsRestoreState = true
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.beginTransaction().remove(fragment!!).commit()
        }
        callBinding()
    }

    var listener = InstallStateUpdatedListener { installState: InstallState ->
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            popupSnackbarForCompleteUpdate()
        }
    }

    override fun getAppUpdateCallBack(appUpdateInfo: AppUpdateInfo) {
        if (appUpdateInfo != null) {
            ApplicationUpdateManager.getInstance(applicationContext).startUpdate(appUpdateInfo, AppUpdateType.FLEXIBLE, this, ApplicationUpdateManager.APP_UPDATE_REQUEST_CODE)
        } else {
            Logger.w("InApp update", "NoUpdate available")
        }
    }

    /* Displays the snackbar notification and call to action. */
    private fun popupSnackbarForCompleteUpdate() {
        val snackbar = Snackbar.make(binding!!.blurredBackgroundImageView, resources.getString(R.string.update_has_downloaded), Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(resources.getString(R.string.restart)) { ApplicationUpdateManager.getInstance(applicationContext).appUpdateManager.completeUpdate() }
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.series_detail_episode_unselected_btn_txt_color))
        snackbar.show()
    }

    override fun onStart() {
        super.onStart()
        try {
            (homeFragment as HomeFragment?)!!.updateAdList()
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    override fun onResume() {
        super.onResume()
        Logger.d("onResume")
        AppCommonMethod.resetFilter(this@HomeActivity)
        if (preference == null) preference = KsPreferenceKeys.getInstance()
    }

    override fun onDestroy() {
        if (preference!!.appPrefIsRestoreState) {
            preference!!.appPrefIsRestoreState = false
        }
        super.onDestroy()
    }
}