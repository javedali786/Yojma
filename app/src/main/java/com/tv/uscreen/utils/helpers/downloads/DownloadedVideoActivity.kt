package com.tv.uscreen.utils.helpers.downloads

import android.view.LayoutInflater
import com.tv.uscreen.baseModels.BaseBindingActivity
import com.tv.uscreen.databinding.ActivityDownloadedVideoBinding


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class DownloadedVideoActivity : BaseBindingActivity<ActivityDownloadedVideoBinding>(){


    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityDownloadedVideoBinding {
        return ActivityDownloadedVideoBinding.inflate(inflater)

    }

}
