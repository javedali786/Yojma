package com.breadgangtvnetwork.activities.settings

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.breadgangtvnetwork.BuildConfig
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.databinding.FragmentDeveloperInformationBinding
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DeveloperInfoDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentDeveloperInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeveloperInformationBinding.inflate(inflater)
        dialog?.window?.setDimAmount(0.8f)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvUserId.text = getString(R.string.user_id, KsPreferenceKeys.getInstance().appPrefUserId)
        binding.tvAppVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)
        binding.tvModel.text = getString(R.string.device_model, getDeviceName())
        binding.tvOs.text = getString(R.string.device_os, "Android")
        binding.tvDeviceVersion.text = getString(R.string.device_os_version, Build.VERSION.SDK_INT)
        val platformType = when {
            isDirectToTV() -> "Android TV"
            resources.getBoolean(R.bool.isTablet) -> "Android Tab"
            else -> "Android Phone"
        }
        binding.tvPlatform.text = getString(R.string.device_platform, platformType)
        binding.tvDateTime.text = getString(R.string.date_time, getFormattedTime())
        binding.tvInternet.text = getString(R.string.internet)
    }

    private fun getDeviceName(): String =
        if (Build.MODEL.startsWith(Build.MANUFACTURER, ignoreCase = true)) {
            Build.MODEL
        } else {
            "${Build.MANUFACTURER} ${Build.MODEL}"
        }.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

    private fun isDirectToTV(): Boolean {
        return (context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_TELEVISION) == true
                || context?.packageManager?.hasSystemFeature(PackageManager.FEATURE_LEANBACK) == true)
    }

    private fun getFormattedTime(): String {
        val date = Date()
        val formatter = SimpleDateFormat("dd MMM yyyy 'at' hh:mm a", Locale.US)
        return formatter.format(date)
    }

    companion object {
        const val TAG = "DeveloperInfoDialogFragment"
    }
}