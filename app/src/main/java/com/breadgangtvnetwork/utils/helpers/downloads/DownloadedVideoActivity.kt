package com.breadgangtvnetwork.utils.helpers.downloads

import android.view.LayoutInflater
import com.breadgangtvnetwork.baseModels.BaseBindingActivity
import com.breadgangtvnetwork.databinding.ActivityDownloadedVideoBinding



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class DownloadedVideoActivity : BaseBindingActivity<ActivityDownloadedVideoBinding>(){


    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityDownloadedVideoBinding {
        return ActivityDownloadedVideoBinding.inflate(inflater)

    }

}
