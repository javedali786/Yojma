package com.breadgangtvnetwork.adapters

import android.app.Activity
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.beanModel.beanModel.SectionDataModel
import com.breadgangtvnetwork.databinding.LandscapeShimmerBinding
import com.breadgangtvnetwork.databinding.PotraitShimmerBinding
import com.breadgangtvnetwork.databinding.SquareShimmerBinding
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper
import com.facebook.shimmer.ShimmerFrameLayout

class CommonListingAdapter(activity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dataList: ArrayList<SectionDataModel>? = null

    init {
        val displaymetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaymetrics)
        //if you need three fix imageview in width
        var num = 3
        val tabletSize = activity.resources.getBoolean(R.bool.isTablet)
        if (tabletSize) {
            //landscape
            num = if (activity.resources.configuration.orientation == 2) 6 else 5
        }
    }

    fun setDataList(demoList: ArrayList<SectionDataModel>?) {
        dataList = demoList
    }

    override fun getItemViewType(position: Int): Int {
        return dataList!![0].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val squareShimmerBinding = DataBindingUtil.inflate<SquareShimmerBinding>(LayoutInflater.from(parent.context), R.layout.square_shimmer, parent, false)
                squareShimmerBinding.colorsData = ColorsHelper
                SquareHolder(squareShimmerBinding)
            }
            2 -> {
                val potraitShimmerBinding = DataBindingUtil.inflate<PotraitShimmerBinding>(LayoutInflater.from(parent.context), R.layout.potrait_shimmer, parent, false)
                potraitShimmerBinding.colorsData = ColorsHelper
                PortrateHolder(potraitShimmerBinding)
            }
            3 -> {
                val landscapeShimmerBinding =
                    DataBindingUtil.inflate<LandscapeShimmerBinding>(LayoutInflater.from(parent.context), R.layout.landscape_shimmer, parent, false)
                landscapeShimmerBinding.colorsData = ColorsHelper
                LandscapeHolder(landscapeShimmerBinding)
            }
            else -> {
                val squareShimmerBinding = DataBindingUtil.inflate<SquareShimmerBinding>(LayoutInflater.from(parent.context), R.layout.square_shimmer, parent, false)
                squareShimmerBinding.colorsData = ColorsHelper
                SquareHolder(squareShimmerBinding)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        when (viewHolder) {
            is SquareHolder -> {
                viewHolder.shimmerFrameLayout.startShimmer()
            }
            is LandscapeHolder -> {
                viewHolder.shimmerFrameLayout.startShimmer()
            }
            is PortrateHolder -> {
                viewHolder.shimmerFrameLayout.startShimmer()
            }
        }
    }

    override fun getItemCount(): Int {
        return 18
    }

    internal inner class SquareHolder(squareRecyclerItemBinding: SquareShimmerBinding) : RecyclerView.ViewHolder(squareRecyclerItemBinding.root) {
        val shimmerFrameLayout: ShimmerFrameLayout

        init {
            shimmerFrameLayout = squareRecyclerItemBinding.sfShimmer1
        }
    }

    internal inner class LandscapeHolder(landscapeRecyclerItemBinding: LandscapeShimmerBinding) : RecyclerView.ViewHolder(
        landscapeRecyclerItemBinding.root
    ) {
        val shimmerFrameLayout: ShimmerFrameLayout

        init {
            shimmerFrameLayout = landscapeRecyclerItemBinding.sfShimmer1
        }
    }

    internal inner class PortrateHolder(potraitRecyclerItemBinding: PotraitShimmerBinding) : RecyclerView.ViewHolder(
        potraitRecyclerItemBinding.root
    ) {
        val shimmerFrameLayout: ShimmerFrameLayout
        val shimmerImage: View

        init {
            shimmerFrameLayout = potraitRecyclerItemBinding.sfShimmer1
            shimmerImage = potraitRecyclerItemBinding.shimmerViewPotrait
        }
    }
}