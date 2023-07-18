package com.tv.activities.downloads


import android.view.LayoutInflater
import com.tv.baseModels.BaseBindingActivity
import com.tv.databinding.ActivityMyDownloadsBinding


class MyDownloads : BaseBindingActivity<ActivityMyDownloadsBinding>(){
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityMyDownloadsBinding {
        return ActivityMyDownloadsBinding.inflate(inflater)
    }


}