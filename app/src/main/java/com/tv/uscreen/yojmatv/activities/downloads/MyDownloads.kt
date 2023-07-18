package com.tv.uscreen.yojmatv.activities.downloads


import android.view.LayoutInflater
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.databinding.ActivityMyDownloadsBinding


class MyDownloads : BaseBindingActivity<ActivityMyDownloadsBinding>(){
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityMyDownloadsBinding {
        return ActivityMyDownloadsBinding.inflate(inflater)
    }


}