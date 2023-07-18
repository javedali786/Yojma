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
import com.tv.uscreen.yojmatv.databinding.PortaritForyouListBinding
import com.tv.uscreen.yojmatv.databinding.PotraitItemLargeBinding


import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper
import com.tv.uscreen.yojmatv.utils.helpers.downloads.OnDownloadClickInteraction

class ForYouAdapter(
    private val context: Activity,
    private val videoItemBeans: MutableList<EnveuVideoItemBean>,
    private val id: Int,
    private var currentAssetId: Int,
    private val listner: EpisodeItemClick
) : RecyclerView.Adapter<ForYouAdapter.SeasonViewHolder>() {
    private val indexMap: HashMap<String, Int> = HashMap<String, Int>()
    private var onDownloadClickInteraction: OnDownloadClickInteraction? = null
    private fun buildIndexMap() {
        indexMap.clear()
        if (videoItemBeans.size > 0) {
            for ((index, videoItemBean) in videoItemBeans.withIndex()) {
                if (!videoItemBean.brightcoveVideoId.isNullOrEmpty()) {
                    indexMap[videoItemBean.brightcoveVideoId] = index
                }
            }
            notifyDataSetChanged()
        }
    }

    private fun notifyVideoChanged(videoId: String) {
        if (indexMap.containsKey(videoId)) {
            val index = indexMap[videoId] as Int
            for (videoItemBean in videoItemBeans!!) {
                if (videoItemBean!!.brightcoveVideoId == videoId) {
                    videoItemBeans[index] = videoItemBean
                    notifyItemChanged(index)
                }
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SeasonViewHolder {
        val itemBinding: PotraitItemLargeBinding
        itemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.potrait_item_large, viewGroup, false
        )
        return SeasonViewHolder(itemBinding)
    }

    var clickBinding: PortaritForyouListBinding? = null

    init {
        if (context is OnDownloadClickInteraction) {
            onDownloadClickInteraction = context
        } else {
            Logger.w("$context does not implement OnDownloadClickInteraction")
        }
        // buildIndexMap();
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (videoItemBeans!![position] != null) {
            holder.itemBinding.playlistItem = videoItemBeans[position]
        }


//        Picasso.with(holder.itemBinding.itemImage.getContext())
//                .load(videoItemBeans.get(position).getPosterURL())
//                .placeholder(R.drawable.placeholder_potrait)
//                .fit()
//                .into(holder.itemBinding.itemImage);
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

        //  ImageHelper.getInstance(context).loadImageToListPortrait(holder.itemBinding.itemImage, videoItemBeans.get(position).getPosterURL());
        // Glide.with(context).load(videoItemBeans.get(position).getPosterURL()).into(holder.itemBinding.itemImage);
        holder.itemBinding.itemImage.setOnClickListener(View.OnClickListener {
            if (videoItemBeans[position]!!.id == currentAssetId) {
                return@OnClickListener
            }
            listner.onItemClick(videoItemBeans[position], videoItemBeans[position]!!.isPremium, position)
        })
    }

    private fun deleteDownloadedVideo(view: View, enveuVideoItemBean: EnveuVideoItemBean, position: Int) {}
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

    fun updateCurrentId(id: Int) {
        currentAssetId = id
    }

    interface EpisodeItemClick {
        fun onItemClick(assetId: EnveuVideoItemBean?, isPremium: Boolean, position: Int)
    }

    inner class SeasonViewHolder internal constructor(val itemBinding: PotraitItemLargeBinding) : RecyclerView.ViewHolder(itemBinding.root)
}