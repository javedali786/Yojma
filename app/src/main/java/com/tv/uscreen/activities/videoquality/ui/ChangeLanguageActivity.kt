package com.tv.uscreen.activities.videoquality.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties

import com.tv.uscreen.R
import com.tv.uscreen.activities.homeactivity.ui.HomeActivity
import com.tv.uscreen.activities.videoquality.adapter.ChangeLanguageAdapter
import com.tv.uscreen.activities.videoquality.bean.LanguageItem
import com.tv.uscreen.activities.videoquality.callBack.ItemClick
import com.tv.uscreen.activities.videoquality.viewModel.VideoQualityViewModel
import com.tv.uscreen.baseModels.BaseBindingActivity
import com.tv.uscreen.databinding.VideoQualityActivityBinding
import com.tv.uscreen.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.fragments.dialog.CommonDialogFragment.Companion.newInstance
import com.tv.uscreen.utils.Logger
import com.tv.uscreen.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.utils.constants.AppConstants
import com.tv.uscreen.utils.helpers.NetworkConnectivity
import com.tv.uscreen.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.utils.stringsJson.converter.StringsHelper

class ChangeLanguageActivity : BaseBindingActivity<VideoQualityActivityBinding?>(), ItemClick, CommonDialogFragment.EditDialogListener {
    private var viewModel: VideoQualityViewModel? = null
    private var notificationAdapter: ChangeLanguageAdapter? = null
    var click = false
    private var lanName: String? = null
    private var langPos = 0
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): VideoQualityActivityBinding {
        return VideoQualityActivityBinding.inflate(inflater)
    }

    private var preference: KsPreferenceKeys? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = KsPreferenceKeys.getInstance()
        //ThemeHandler.getInstance().applyChangelanguge(this,getBinding());
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
        binding!!.colorsData = colorsHelper
        binding!!.stringData = stringsHelper
        binding!!.toolbar.colorsData = colorsHelper
        binding!!.toolbar.stringData = stringsHelper
        val config = Configuration(resources.configuration)
        Logger.d("Locale: " + config.locale.displayLanguage)
        callModel()
        connectionObserver()
        setToolBar()
        val properties = Properties()
        properties.addAttribute(AppConstants.SETTING_CHANGE_LAN, AppConstants.SETTING_CHANGE_LAN)
        MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties)
    }

    private fun setToolBar() {
        binding!!.toolbar.logoMain2.visibility = View.GONE
        binding!!.toolbar.backLayout.visibility = View.VISIBLE
        binding!!.toolbar.rlToolBar.visibility = View.GONE
        binding!!.toolbar.titleMid.visibility = View.VISIBLE
        val changeLangTile = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.lang_change_lang.toString(),
            getString(R.string.lang_change_lang)
        )
        binding!!.toolbar.titleMid.text = changeLangTile
        binding!!.toolbar.titleMid.setBackgroundResource(0)
        binding!!.toolbar.backLayout.setOnClickListener { onBackPressed() }
    }

    private fun callModel() {
        viewModel = ViewModelProvider(this)[VideoQualityViewModel::class.java]
    }

    private fun connectionObserver() {
        connectionValidation(NetworkConnectivity.isOnline(this))
    }

    private var arrayList: ArrayList<LanguageItem>? = null
    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            binding!!.noConnectionLayout.visibility = View.GONE
            val trackItems = ArrayList<LanguageItem>()
            Logger.e("LanguageList", getString(R.string.language_english))
            for (i in 0..2) {
                if (i == 1) {
                    val languageItem = LanguageItem()
                    val english = stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.lang_english.toString(),
                        getString(R.string.lang_english)
                    )
                    languageItem.languageName = english
                    languageItem.defaultLangageName = getString(R.string.language_english)
                    trackItems.add(languageItem)
                } else if (i == 0) {
                    val languageItem = LanguageItem()
                    val spanish = stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.lang_spanish.toString(),
                        getString(R.string.lang_spanish)
                    )
                    languageItem.languageName = spanish
                    languageItem.defaultLangageName = getString(R.string.language_spanish)
                    trackItems.add(languageItem)
                }
            }
            arrayList = trackItems
            Logger.e("LanguageList: $arrayList")
            uiInitialisation()
            setAdapter()
        } else {
            noConnectionLayout()
        }
    }

    private fun setAdapter() {
        notificationAdapter = ChangeLanguageAdapter(this@ChangeLanguageActivity, arrayList, this@ChangeLanguageActivity)
        binding!!.recyclerview.adapter = notificationAdapter
    }

    private fun uiInitialisation() {
        binding!!.recyclerview.hasFixedSize()
        binding!!.recyclerview.isNestedScrollingEnabled = false
        binding!!.recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun noConnectionLayout() {
        binding!!.noConnectionLayout.visibility = View.VISIBLE
        // getBinding().connection.closeButton.setOnClickListener(view -> onBackPressed());
        binding!!.connection.retryTxt.setOnClickListener { connectionObserver() }
    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm = supportFragmentManager
        val commonDialogFragment = newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }

    override fun itemClicked(name: String, position: Int) {
        if (position == 0) {
            lanName = "spanish"
        } else if (position == 1) {
            lanName = "English"
        }
        langPos = position
        if (preference!!.appLanguage.equals(getString(R.string.language_spanish_title), ignoreCase = true)) {
            AppCommonMethod.updateLanguage("es", this@ChangeLanguageActivity)
        } else if (preference!!.appLanguage.equals(getString(R.string.language_english_title), ignoreCase = true)) {
            AppCommonMethod.updateLanguage("en", this@ChangeLanguageActivity)
        }
        commonDialog(
            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_change_language_tittle.toString(), getString(R.string.popup_change_language_tittle)),
            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_do_you_want_to_change_lang.toString(), getString(R.string.popup_do_you_want_to_change_lang)),
            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue))
        )
    }

    override fun onActionBtnClicked() {
        preference!!.appLanguage = lanName
        preference!!.appPrefLanguagePos = langPos
        if (preference!!.appLanguage.equals(getString(R.string.language_spanish_title), ignoreCase = true)) {
            AppCommonMethod.updateLanguage("es", this@ChangeLanguageActivity)
        } else if (preference!!.appLanguage.equals(getString(R.string.language_english_title), ignoreCase = true)) {
            AppCommonMethod.updateLanguage("en", this@ChangeLanguageActivity)
        }
        ActivityLauncher.getInstance().homeActivity(this, HomeActivity::class.java)
    }
}