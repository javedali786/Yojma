package com.breadgangtvnetwork.activities.search.adapter

import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.activities.search.adapter.RowSearchAdapter.RowSearchListener
import com.breadgangtvnetwork.beanModel.enveuCommonRailData.RailCommonData
import com.breadgangtvnetwork.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.breadgangtvnetwork.callbacks.commonCallbacks.SearchClickCallbacks
import com.breadgangtvnetwork.databinding.RowSearchCategoryBinding
import com.breadgangtvnetwork.utils.Logger
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod.Companion.launchDetailScreenFromSearch
import com.breadgangtvnetwork.utils.constants.AppConstants
import com.breadgangtvnetwork.utils.stringsJson.converter.StringsHelper


class CategoriedSearchAdapter(private val context: Context, private val list: List<RailCommonData>?, private val listener: SearchClickCallbacks) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    RowSearchListener {
    private val stringsHelper by lazy { StringsHelper }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: RowSearchCategoryBinding
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.row_search_category, parent, false
        )
        //ThemeHandler.getInstance().applySearchCategory(parent.getContext(),binding);
        Logger.d("ViewType :$viewType")
        return VideoTypeViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, pos: Int) {
        val singleSectionItems = list?.get(pos)?.enveuVideoItemBeans
        val totalCount = list?.get(pos)?.totalCount
        val itemListDataAdapter1 = RowSearchAdapter(context, singleSectionItems, true, this, totalCount!!, "", "")
        val videoTypeViewHolder = viewHolder as VideoTypeViewHolder
        setRecyclerProperties(videoTypeViewHolder.binding.recyclerView)
        viewHolder.binding.tvTitle.setTextColor(context.getColor(R.color.series_detail_description_text_color))
        viewHolder.binding.colorsData = ColorsHelper
        viewHolder.binding.stringData = StringsHelper

        val seeAll = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.search_see_all.toString(),
            context.getString(R.string.search_see_all)
        )
        val searchResults = stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.search_results.toString(), context.getString(R.string.search_results))
        videoTypeViewHolder.binding.showAll.text = seeAll
        videoTypeViewHolder.binding.recyclerView.adapter = itemListDataAdapter1
        var header = ""
        when (getItemViewType(pos)) {
            0 -> {
                val search = stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.search_result.toString(),
                    context.getString(R.string.search_result)
                )
                header = search
            }
        }
        videoTypeViewHolder.binding.tvTitle.text = header + " - " + totalCount + " " + searchResults
        if (list?.get(pos)?.totalCount!! < 0) {
            videoTypeViewHolder.binding.showAllSearch.visibility = View.GONE
        }
        val finalHeader = header
        videoTypeViewHolder.binding.showAllSearch.setOnClickListener { view: View? -> callResultActivity(list[pos], finalHeader) }
    }

    private fun callResultActivity(model: RailCommonData, header: String) {
        listener.onShowAllItemClicked(model, header)
    }

    override fun getItemViewType(position: Int): Int {
        return list?.get(position)?.layoutType!!
    }

    private fun setRecyclerProperties(recyclerView: RecyclerView) {
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    internal inner class VideoTypeViewHolder(val binding: RowSearchCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onRowItemClicked(itemValue: EnveuVideoItemBean, position: Int) {
        Log.d("javed", "onRowItemClicked: $itemValue")
        val isPremium: Boolean
        var isParentContentNull = false
        var isHosted = false
        var externalUrl: String? = ""
        isPremium = itemValue.isPremium
        var trailerReferenceId: String? = ""
        if (itemValue.trailerReferenceId != null) {
            trailerReferenceId = itemValue.trailerReferenceId
        }
        var tittle: String? = ""
        if (itemValue.title != null) {
            tittle = itemValue.title
        }
        var externalRefId: String? = ""
        if (itemValue.externalRefId != null) {
            externalRefId = itemValue.externalRefId
        }
        if (itemValue.parentContent == null) {
            isParentContentNull = true
        }
        var skuId: String? = ""
        if (itemValue.sku != null) {
            skuId = itemValue.sku
        }
        var customContentType: String? = ""
        val assetType = itemValue.assetType
        var videoType: String? = ""
        if (itemValue.assetType.equals(AppConstants.VIDEO, ignoreCase = true)) {
            videoType = itemValue.videoDetails.videoType
        } else if (itemValue.assetType.equals(AppConstants.LIVE, ignoreCase = true)) {
            if (java.lang.Boolean.TRUE == itemValue.liveContent.isHosted) {
                isHosted = true
            } else {
                if (itemValue.liveContent.externalUrl != null) {
                    externalUrl = itemValue.liveContent.externalUrl
                }
            }
        } else {
            if (itemValue.assetType.equals(AppConstants.CUSTOM, ignoreCase = true)) {
                customContentType = itemValue.customContent.customType
            }
        }
        launchDetailScreenFromSearch(
            context,
            assetType,
            itemValue.id,
            customContentType!!,
            videoType,
            trailerReferenceId,
            externalRefId!!,
            isPremium,
            skuId,
            isParentContentNull,
            tittle,
            isHosted,
            externalUrl!!,
            itemValue.posterURL
        )
    }
}