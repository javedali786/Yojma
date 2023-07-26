package com.tv.uscreen.yojmatv.fragments.foryou.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.databinding.MorePotraitItemLargeBinding
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper

class MoreForYouAdapter(private val context: Activity, private val videoItemBeans: MutableList<EnveuVideoItemBean>, private val id: Int, private var currentAssetId: Int, private val listner: EpisodeItemClick) : RecyclerView.Adapter<MoreForYouAdapter.SeasonViewHolder>() {
    private var itemWidth = 0
    private var itemHeight = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SeasonViewHolder {
        val itemBinding: MorePotraitItemLargeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.more_potrait_item_large, viewGroup, false



        )
        return SeasonViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, @SuppressLint("RecyclerView") position: Int) {
        var num : Int = 3
        val tabletSize = context.resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            num = if (context.resources.configuration.orientation == 2) 5 else 4
        }
        val displaymetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displaymetrics)
        //if you need three fix imageview in width
        //if you need three fix imageview in width
        itemWidth = displaymetrics.widthPixels / num
        itemHeight = itemWidth * 3 / 2


        holder.itemBinding.playlistItem = videoItemBeans[position]

        try {
            if (videoItemBeans[position]!!.posterURL != null && !videoItemBeans[position]!!.posterURL.equals("", ignoreCase = true)) {
                ImageHelper.getInstance(holder.itemBinding.itemImage.context).loadPortraitImage(
                    holder.itemBinding.itemImage, AppCommonMethod.getListPRImage(
                        videoItemBeans[position]!!.posterURL, holder.itemBinding.itemImage.context
                    )
                )
                holder.itemBinding.itemImage.scaleType = ScaleType.FIT_XY
            }
        } catch (ignored: Exception) {
        }

        holder.itemBinding.itemImage.setOnClickListener(View.OnClickListener {
            if (videoItemBeans[position]!!.id == currentAssetId) {
                return@OnClickListener
            }
            listner.onItemClick(videoItemBeans[position], videoItemBeans[position]!!.isPremium, position)
        })
    }

    override fun getItemCount(): Int {
        return videoItemBeans!!.size
    }

    val currentEpisodeNumber: Int
        get() {
            var episodeNo = -1
            var found = false
            for (enveuVideoItemBean in videoItemBeans!!) {
                episodeNo++
                if (enveuVideoItemBean!!.id == currentAssetId) {
                    found = true
                    break
                }
            }
            return if (found) episodeNo else -1
        }

    interface EpisodeItemClick {
        fun onItemClick(assetId: EnveuVideoItemBean?, isPremium: Boolean, position: Int)
    }

    inner class SeasonViewHolder internal constructor(val itemBinding: MorePotraitItemLargeBinding) : RecyclerView.ViewHolder(itemBinding.root)
}