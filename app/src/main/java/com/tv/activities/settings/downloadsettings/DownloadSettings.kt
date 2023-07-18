package com.tv.activities.settings.downloadsettings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tv.R
import com.tv.activities.settings.downloadsettings.changequality.ui.ChangeDownloadQuality
import com.tv.baseModels.BaseBindingActivity
import com.tv.databinding.ActivityDownloadSettingsBinding
import com.tv.utils.constants.SharedPrefesConstants
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

class DownloadSettings : BaseBindingActivity<ActivityDownloadSettingsBinding>(), View.OnClickListener {
    companion object {
        const val CHANGE_QUALITY_REQUEST_CODE = 10001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  ThemeHandler.getInstance().applyDownloadsettingPage(applicationContext, binding)
        setupToolbar()

        binding.selectedQualityText.text = resources.getStringArray(R.array.download_quality)[KsPreferenceKeys.getInstance().getInt(
            SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 4)]
        binding.textDownloadQuality.setOnClickListener(this)
        binding.switchTheme.isChecked = KsPreferenceKeys.getInstance().downloadOverWifi == 1

        binding.switchTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                KsPreferenceKeys.getInstance().setInt(SharedPrefesConstants.DOWNLOAD_OVER_WIFI, 1)
            } else {
                KsPreferenceKeys.getInstance().setInt(SharedPrefesConstants.DOWNLOAD_OVER_WIFI, 0)
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbarDownload.backLayout.visibility = View.VISIBLE
        binding?.toolbarDownload!!.titleMid.visibility = View.VISIBLE
        binding?.toolbarDownload!!.titleMid.text = resources.getString(R.string.download_settings)
        binding.toolbarDownload.backLayout.setOnClickListener { onBackPressed() }
    }

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityDownloadSettingsBinding {
        return ActivityDownloadSettingsBinding.inflate(inflater)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.text_download_quality -> {
                startActivityForResult(Intent(this, ChangeDownloadQuality::class.java), CHANGE_QUALITY_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            CHANGE_QUALITY_REQUEST_CODE -> {
                binding.selectedQualityText.text = resources.getStringArray(R.array.download_quality)[KsPreferenceKeys.getInstance().getInt(
                    SharedPrefesConstants.DOWNLOAD_QUALITY_INDEX, 4)]
            }
        }
    }
}
