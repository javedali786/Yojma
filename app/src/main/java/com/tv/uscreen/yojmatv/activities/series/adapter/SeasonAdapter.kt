package com.tv.uscreen.yojmatv.activities.series.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.activities.series.ui.SeriesDetailActivity
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.FirstEpisodeItem
import com.tv.uscreen.yojmatv.databinding.RowEpisodeListBinding

import com.tv.uscreen.yojmatv.enums.DownloadStatus
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils
import com.tv.uscreen.yojmatv.utils.helpers.downloads.OnDownloadClickInteraction
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class SeasonAdapter(
    private val context: Activity,
    private val videoItemBeans: MutableList<EnveuVideoItemBean>,
    private var id: Int,
    private var currentAssetId: Int,
    private val listner: EpisodeItemClick,
    private val firstEpisodeItem: FirstEpisodeItem
) : RecyclerView.Adapter<SeasonAdapter.SeasonViewHolder>() {
    private val indexMap: HashMap<String, Int> = HashMap<String, Int>()
    private var onDownloadClickInteraction: OnDownloadClickInteraction? = null
    private var isFirstItemAdded = true
    private val stringsHelper by lazy { StringsHelper }
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
        val itemBinding: RowEpisodeListBinding
        itemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.row_episode_list, viewGroup, false
        )
        itemBinding.colorsData = ColorsHelper
        itemBinding.stringData = StringsHelper
        return SeasonViewHolder(itemBinding)
    }

    var clickBinding: RowEpisodeListBinding? = null

    init {
        if (context is OnDownloadClickInteraction) {
            onDownloadClickInteraction = context
        } else {
            Logger.w("$context does not implement OnDownloadClickInteraction")
        }
        buildIndexMap()
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (videoItemBeans!![position] != null) {
            holder.itemBinding.playlistItem = videoItemBeans[position]
        }
        if (isFirstItemAdded) {
            if (context is SeriesDetailActivity) {
                context.getFirstItem(videoItemBeans[0]!!)
            }
            isFirstItemAdded = false
        }
        if (AppCommonMethod.getCheckBCID(videoItemBeans[position]!!.brightcoveVideoId)) {
        }
        if (videoItemBeans[position]!!.episodeNo != null && videoItemBeans[position]!!.episodeNo is String && !(videoItemBeans[position]!!.episodeNo as String).equals("", ignoreCase = true)) {
            val episodeNum = videoItemBeans[position]!!.episodeNo as String
            val eNum = episodeNum.toInt()
            holder.itemBinding.titleWithSerialNo.text = eNum.toString() + ". " + videoItemBeans[position]!!.title
        } else {
            holder.itemBinding.titleWithSerialNo.text = videoItemBeans[position]!!.title
        }
        ImageHelper.getInstance(context).loadListImage(holder.itemBinding.episodeImage, videoItemBeans[position]!!.posterURL)
        holder.itemBinding.description.text = videoItemBeans[position]!!.description
        if (!StringUtils.isNullOrEmpty(videoItemBeans[position]!!.duration.toString())) {
            val d = videoItemBeans[position]!!.duration.toDouble()
            val x = d.toLong() // x = 1234
            val duration = AppCommonMethod.stringForTime(x) + " " + stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.popup_minutes.toString(),
                context.getString(R.string.popup_minutes)
            )
            holder.itemBinding.duration.text = duration
        } else {
            holder.itemBinding.duration.text = "00:00"
        }
        if (videoItemBeans[position]!!.id == currentAssetId) {
            holder.itemBinding.nowPlaying.visibility = View.VISIBLE
            holder.itemBinding.playIcon.visibility = View.GONE
        } else {
            holder.itemBinding.playIcon.visibility = View.VISIBLE
            holder.itemBinding.nowPlaying.visibility = View.GONE
        }
        holder.itemBinding.episodeImage.setOnClickListener(View.OnClickListener {
            if (videoItemBeans[position]!!.id == currentAssetId) {
                return@OnClickListener
            }
            listner.onItemClick(videoItemBeans[position], videoItemBeans[position]!!.isPremium, position)
        })
        holder.itemBinding.mainLay.setOnClickListener { view: View? ->
            Logger.d("positionIs" + videoItemBeans[position])
            id = videoItemBeans[position]!!.id
            notifyDataSetChanged()
        }
        holder.itemBinding.downloadVideo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                clickBinding = holder.itemBinding
                // Log.w("itemCliked",videoItemBeans.get(position).getAssetType()+"   "+videoItemBeans.get(position).getSeasonNumber());
                if (onDownloadClickInteraction != null) onDownloadClickInteraction!!.onDownloadClicked(videoItemBeans[position]!!.brightcoveVideoId, videoItemBeans[position]!!.episodeNo, this)
            }
        })
        holder.itemBinding.videoDownloaded.setOnClickListener { v -> deleteDownloadedVideo(v, videoItemBeans[position], position) }
        holder.itemBinding.videoDownloading.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                if (onDownloadClickInteraction != null) onDownloadClickInteraction!!.onProgressbarClicked(v, this, videoItemBeans[position]!!.brightcoveVideoId)
            }
        })
        holder.itemBinding.pauseDownload.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                holder.itemBinding.downloadStatus = DownloadStatus.REQUESTED
                if (onDownloadClickInteraction != null) onDownloadClickInteraction!!.onPauseClicked(videoItemBeans[position]!!.brightcoveVideoId, this)
            }
        })
    }

    fun holdHolder() {
        if (clickBinding != null) {
            clickBinding!!.downloadStatus = DownloadStatus.REQUESTED
        }
    }

    private fun deleteDownloadedVideo(view: View, enveuVideoItemBean: EnveuVideoItemBean?, position: Int) {}
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

    inner class SeasonViewHolder internal constructor(val itemBinding: RowEpisodeListBinding) : RecyclerView.ViewHolder(itemBinding.root)
}