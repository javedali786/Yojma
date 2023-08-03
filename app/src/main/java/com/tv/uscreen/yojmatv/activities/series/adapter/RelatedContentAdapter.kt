package com.tv.uscreen.yojmatv.activities.series.adapter


import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.databinding.RelatedEpisodeListBinding
import com.tv.uscreen.yojmatv.databinding.RowEpisodeListBinding
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper
import com.tv.uscreen.yojmatv.utils.helpers.downloads.OnDownloadClickInteraction
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class RelatedContentAdapter(
    private val context: Activity,
    private val videoItemBeans: MutableList<EnveuVideoItemBean>,
    private var id: Int,
    private var currentAssetId: Int,
    private val listner: EpisodeItemClick,
) : RecyclerView.Adapter<RelatedContentAdapter.SeasonViewHolder>() {
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
        val itemBinding: RelatedEpisodeListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.context),
            R.layout.related_episode_list, viewGroup, false
        )
        itemBinding.colorsData = ColorsHelper
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

    override fun getItemCount(): Int {
        return videoItemBeans!!.size
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.itemBinding.playlistItem = videoItemBeans[position]

        ImageHelper.getInstance(context).loadListImage(holder.itemBinding.itemImage, videoItemBeans[position]!!.posterURL)


        //  ImageHelper.getInstance(context).loadImageToListPortrait(holder.itemBinding.itemImage, videoItemBeans.get(position).getPosterURL());
        // Glide.with(context).load(videoItemBeans.get(position).getPosterURL()).into(holder.itemBinding.itemImage);
        holder.itemBinding.itemImage.setOnClickListener(View.OnClickListener {
            if (videoItemBeans[position].id === currentAssetId) {
                return@OnClickListener
            }
            listner.onItemClick(
                videoItemBeans[position],
                videoItemBeans[position].isPremium,
                position
            )
        })

       /* holder.itemBinding.itemImage.setOnClickListener { view: View? ->
            Logger.d("positionIs" + videoItemBeans[position])
            id = videoItemBeans[position]!!.id
            notifyDataSetChanged()
        }*/
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

    inner class SeasonViewHolder internal constructor(val itemBinding: RelatedEpisodeListBinding) : RecyclerView.ViewHolder(itemBinding.root)
}