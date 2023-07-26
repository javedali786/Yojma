package com.tv.uscreen.yojmatv.fragments.foryou.ui


import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.databinding.PotraitItemLargeBinding
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper

class ForYouAdapter(private val context: Activity, private val videoItemBeans: MutableList<EnveuVideoItemBean>, private val id: Int, private var currentAssetId: Int, private val listner: EpisodeItemClick) : RecyclerView.Adapter<ForYouAdapter.SeasonViewHolder>() {
    private val itemWidth = 0
    private val itemHeight = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SeasonViewHolder {
        val itemBinding: PotraitItemLargeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.potrait_item_large, viewGroup, false

        )
        return SeasonViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, @SuppressLint("RecyclerView") position: Int) {
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

    inner class SeasonViewHolder internal constructor(val itemBinding: PotraitItemLargeBinding) : RecyclerView.ViewHolder(itemBinding.root)
}