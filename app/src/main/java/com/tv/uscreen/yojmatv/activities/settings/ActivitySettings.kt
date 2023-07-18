package com.tv.uscreen.yojmatv.activities.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import com.tv.uscreen.yojmatv.BuildConfig


import com.tv.uscreen.yojmatv.activities.settings.downloadsettings.DownloadSettings
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.ActivityLogin
import com.tv.uscreen.yojmatv.activities.videoquality.ui.ChangeLanguageActivity
import com.tv.uscreen.yojmatv.activities.videoquality.ui.VideoQualityActivity
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.databinding.SettingsActivityBinding
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class ActivitySettings : BaseBindingActivity<SettingsActivityBinding?>(), View.OnClickListener {
    private var devInfoCounter = 0
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): SettingsActivityBinding {
        return SettingsActivityBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ThemeHandler.getInstance().applysttingsPage(this, getBinding());
        binding!!.stringData = stringsHelper
        binding!!.colorsData = colorsHelper
        binding!!.toolbar.colorsData = colorsHelper
        binding!!.toolbar.stringData = stringsHelper
        setToolBar()
        if (KsPreferenceKeys.getInstance().currentTheme.equals(AppConstants.LIGHT_THEME, ignoreCase = true)) {
            setTheme(R.style.MyMaterialTheme_Base_Light)
            binding!!.switchTheme.isChecked = false
        } else {
            setTheme(R.style.MyMaterialTheme_Base_Dark)
            binding!!.switchTheme.isChecked = true
        }
        try {
            val isTablet = this@ActivitySettings.resources.getBoolean(R.bool.isTablet)
            if (!isTablet) {
                val number: String
                if (BuildConfig.FLAVOR.equals("qa", ignoreCase = true)) {
                    number = this@ActivitySettings.resources.getString(R.string.app_name) + "(" + BuildConfig.VERSION_NAME + ")"
                    binding!!.buildNumber.text = number
                } else {
                    number = this@ActivitySettings.resources.getString(R.string.app_name) + "(" + BuildConfig.VERSION_NAME + ")"
                    binding!!.buildNumber.text = number
                }
            }
        } catch (ignored: Exception) {
        }
        setSwitchForBingeWatch()
        checkLanguage()
        if (KsPreferenceKeys.getInstance().appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
        } else {
            binding!!.downloadLayout.visibility = View.GONE
        }
        binding!!.rlKidsModePin.visibility = View.GONE
        binding!!.downloadLayout.setOnClickListener(this)
        binding!!.parentLayout.setOnClickListener {
            val intent = Intent(this@ActivitySettings, ChangeLanguageActivity::class.java)
            startActivity(intent)
        }
        binding!!.videoQuality.setOnClickListener {
            val intent = Intent(this@ActivitySettings, VideoQualityActivity::class.java)
            startActivity(intent)
        }
        binding!!.contentPerferencesLayout.setOnClickListener {
            if (KsPreferenceKeys.getInstance().appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
//                Intent intent = new Intent(ActivitySettings.this, ChoosePrefrenceActivity.class);
//                intent.putExtra(AppConstants.INTENT_FROM_SETTING, true);
//                startActivity(intent);
            } else {
                val intent = Intent(this@ActivitySettings, ActivityLogin::class.java)
                startActivity(intent)
            }
        }
        binding!!.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, _: Boolean ->
            if (binding!!.switchTheme.isChecked) {
                KsPreferenceKeys.getInstance().currentTheme = AppConstants.DARK_THEME
            } else {
                KsPreferenceKeys.getInstance().currentTheme = AppConstants.LIGHT_THEME
            }
            recreate()
        }
        binding!!.bingeSetting.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                KsPreferenceKeys.getInstance().bingeWatchEnable = true
                setSwitchForBingeWatch()
            } else {
                KsPreferenceKeys.getInstance().bingeWatchEnable = false
                setSwitchForBingeWatch()
            }
        }
        binding!!.switchTheme.setOnClickListener { }
        binding!!.buildNumber.setOnClickListener {
            if (KsPreferenceKeys.getInstance().appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                devInfoCounter++
                if (devInfoCounter == 3) {
                    binding!!.rlDeveloperInfo.visibility = View.VISIBLE
                    devInfoCounter = 0
                }
            }
        }
        binding!!.rlDeveloperInfo.setOnClickListener {
            DeveloperInfoDialogFragment().show(
                supportFragmentManager,
                DeveloperInfoDialogFragment.TAG
            )
        }
    }

    private fun setToolBar() {
        binding!!.toolbar.logoMain2.visibility = View.GONE
        binding!!.toolbar.backLayout.visibility = View.VISIBLE
        binding!!.toolbar.rlToolBar.visibility = View.VISIBLE
        binding!!.toolbar.titleMid.visibility = View.VISIBLE
        val settingsTile = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.settings_title.toString(),
            getString(R.string.settings_title)
        )
        binding!!.toolbar.titleMid.text = settingsTile
        binding!!.toolbar.searchIcon.visibility = View.GONE
        binding!!.toolbar.clNotification.visibility = View.GONE
        binding!!.toolbar.titleMid.setBackgroundResource(0)
        binding!!.toolbar.backLayout.setOnClickListener { onBackPressed() }
    }

    private fun setSwitchForBingeWatch() {
        binding!!.bingeSetting.isChecked = KsPreferenceKeys.getInstance().bingeWatchEnable
    }

    private fun checkLanguage() {
        val preference = KsPreferenceKeys.getInstance()
        val currentLanguage = preference.appLanguage
        if (currentLanguage.isEmpty()) {
            binding!!.languageText.text = "English"
        } else {
            if (currentLanguage.equals("English", ignoreCase = true)) {
                binding!!.languageText.text = currentLanguage
            } else {
                // getBinding().languageText.setText(getString(R.string.language_thai));
            }
        }
        val qualityName = preference.qualityName
        setQualityText(qualityName)
    }

    private fun setQualityText(qualityName: String) {
        if (qualityName.isEmpty()) {
            // getBinding().qualityText.setText(getString(R.string.auto));
            binding!!.qualityText.text = getString(R.string.ep_video_auto)
        } else {
            if (qualityName.equals("Auto", ignoreCase = true)) {
                // getBinding().qualityText.setText(getString(R.string.auto));
                binding!!.qualityText.text = getString(R.string.ep_video_auto)
            } else if (qualityName.equals("SD", ignoreCase = true)) {
                binding!!.qualityText.text = getString(R.string.low)
            } else if (qualityName.equals("HD", ignoreCase = true)) {
                binding!!.qualityText.text = getString(R.string.medium)
            } else if (qualityName.equals("Full HD", ignoreCase = true)) {
                binding!!.qualityText.text = getString(R.string.high)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (binding != null) {
            setQualityText(KsPreferenceKeys.getInstance().qualityName)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.downloadLayout -> {
                val intent = Intent(this@ActivitySettings, DownloadSettings::class.java)
                startActivity(intent)
            }

            R.id.parent_layout -> {
                val intent = Intent(this@ActivitySettings, ChangeLanguageActivity::class.java)
                startActivity(intent)
            }
        }
    }
}