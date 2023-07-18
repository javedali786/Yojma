package com.tv.uscreen.activities.downloads


import android.view.LayoutInflater
import com.tv.uscreen.baseModels.BaseBindingActivity
import com.tv.uscreen.databinding.ActivityMyDownloadsBinding


class MyDownloads : BaseBindingActivity<ActivityMyDownloadsBinding>(){
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityMyDownloadsBinding {
        return ActivityMyDownloadsBinding.inflate(inflater)
    }


}