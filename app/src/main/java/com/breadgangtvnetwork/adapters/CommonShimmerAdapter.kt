package com.breadgangtvnetwork.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.databinding.CardRowShimmerBinding
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper
import com.facebook.shimmer.ShimmerFrameLayout

class CommonShimmerAdapter : RecyclerView.Adapter<CommonShimmerAdapter.PurchaseViewHolder> {
    private var isHeader = false
    private var list: MutableList<String>? = null

    constructor() {
        dummyList()
    }

    constructor(isHeader: Boolean) {
        this.isHeader = isHeader
    }

    private fun dummyList() {
        list = ArrayList()
        (list as ArrayList<String>).add("one")
        (list as ArrayList<String>).add("one")
        (list as ArrayList<String>).add("one")
        (list as ArrayList<String>).add("one")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val itemView = DataBindingUtil.inflate<CardRowShimmerBinding>(LayoutInflater.from(parent.context), R.layout.card_row_shimmer, parent, false)
        itemView.colorsData = ColorsHelper
        return PurchaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, i: Int) {
        holder.shimmerFrameLayout.startShimmer()
        if (i == 0) {
            if (isHeader) {
                holder.header.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        dummyList()
        return list!!.size
    }

    inner class PurchaseViewHolder internal constructor(binding: CardRowShimmerBinding) : RecyclerView.ViewHolder(binding.root) {
        val shimmerFrameLayout: ShimmerFrameLayout
        val header: View

        init {
            shimmerFrameLayout = binding.sflMockContent
            header = binding.header
        }
    }
}