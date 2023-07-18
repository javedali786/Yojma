package com.tv.uscreen.yojmatv.adapters.commonRails

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.beanModel.ContinueRailModel.CommonContinueRail
import com.tv.uscreen.yojmatv.beanModel.responseModels.landingTabResponses.railData.ContentsItem
import com.tv.uscreen.yojmatv.beanModel.responseModels.series.season.ItemsItem
import com.tv.uscreen.yojmatv.databinding.LandscapeShimmerItemBinding

import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

class CommonLandscapeAdapter(
    context: Activity,
    private val itemsList: List<ContentsItem>,
    private val seasonItems: List<ItemsItem>,
    private val contentType: String,
    private val continuelist: ArrayList<CommonContinueRail>
) : RecyclerView.Adapter<CommonLandscapeAdapter.SingleItemRowHolder>() {
    private val preference: KsPreferenceKeys
    private val isLogin: String
    private val itemWidth: Int
    private val itemHeight: Int

    init {
        preference = KsPreferenceKeys.getInstance()
        isLogin = preference.appPrefLoginStatus
        var num = 2
        val tabletSize = context.resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            //landscape
            num = if (context.resources.configuration.orientation == 2) 4 else 3
        }
        val displaymetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displaymetrics)
        //if you need three fix imageview in width
        itemWidth = (displaymetrics.widthPixels - 80) / num
        itemHeight = itemWidth * 9 / 16
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): SingleItemRowHolder {
        val binding = DataBindingUtil.inflate<LandscapeShimmerItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.landscape_shimmer_item, parent, false
        )
        binding.colorsData = ColorsHelper
        return SingleItemRowHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleItemRowHolder, i: Int) {}
    override fun getItemCount(): Int {
        var itemCount = 0
        if (itemsList.size > 0) {
            itemCount = itemsList.size
        } else if (seasonItems.size > 0) {
            itemCount = seasonItems.size
        } else if (continuelist.size > 0) itemCount = continuelist.size
        return itemCount
    }

    inner class SingleItemRowHolder internal constructor(val landscapeItemBinding: LandscapeShimmerItemBinding) : RecyclerView.ViewHolder(landscapeItemBinding.root)
}