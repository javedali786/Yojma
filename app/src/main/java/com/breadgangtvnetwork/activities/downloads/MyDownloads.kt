package com.breadgangtvnetwork.activities.downloads


import android.view.LayoutInflater
import com.breadgangtvnetwork.baseModels.BaseBindingActivity
import com.breadgangtvnetwork.databinding.ActivityMyDownloadsBinding


class MyDownloads : BaseBindingActivity<ActivityMyDownloadsBinding>(){
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityMyDownloadsBinding {
        return ActivityMyDownloadsBinding.inflate(inflater)
    }


}