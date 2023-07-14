package com.breadgangtvnetwork.fragments.more.adapter

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.breadgangtvnetwork.OttApplication
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.callbacks.commonCallbacks.MoreItemClickListener
import com.breadgangtvnetwork.databinding.MoreItemBinding
import com.breadgangtvnetwork.utils.Logger
import com.breadgangtvnetwork.utils.colorsJson.converter.AppColors
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper.imageViewDrawableBgColor
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod
import com.breadgangtvnetwork.utils.helpers.DrawableHelperAboutUs
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

class MoreListAdapter(private val mContext: Activity, private val itemsList: List<String>, call: MoreItemClickListener, islogin: Boolean) : RecyclerView.Adapter<MoreListAdapter.ViewHolder>() {
    val itemClickListener: MoreItemClickListener
    private val islogin: Boolean
    private var mLastClickTime: Long = 0

    init {
        val layoutInflater = LayoutInflater.from(mContext)
        itemClickListener = call
        this.islogin = islogin
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val moreItemBinding = DataBindingUtil.inflate<MoreItemBinding>(LayoutInflater.from(viewGroup.context), R.layout.more_item, viewGroup, false)
        moreItemBinding.colorsData = ColorsHelper
        return ViewHolder(moreItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.moreItemBinding.moreListTitle.text = "" + itemsList[i]
        setIcons(holder.moreItemBinding.moreListIcon, i, holder.moreItemBinding)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    private fun setIcons(v: ImageView, i: Int, binding: MoreItemBinding?) {
        when (i) {
            0 -> callDrawableHelper(mContext, R.drawable.ic_my_list, v)
            1 -> callDrawableHelper(mContext, R.drawable.ic_account, v)
            2 -> callDrawableHelper(mContext, R.drawable.ic_settings, v)
            3 -> callDrawableHelper(mContext, R.drawable.ic_buy_now, v)
            4 -> callDrawableHelper(mContext, R.drawable.ic_order_history, v)
            5 -> callDrawableHelper(mContext, R.drawable.ic_privacy, v)
            6 -> callDrawableHelper(mContext, R.drawable.ic_terms, v)
            else -> {}
        }
    }

    private fun callDrawableHelper(context: Context?, mDrawable: Int, imageView: ImageView) {
        imageView.background = AppCompatResources.getDrawable(context!!, mDrawable)
        imageViewDrawableBgColor(imageView, AppColors.itemLabelIconColor())

//        DrawableHelper
//                .withContext(context)
//                .withColor(mColor)
//                .withDrawable(mDrawable)
//                .tint()
//                .applyTo(imageView);
    }

    fun callDrawableHelperAboutUs(context: Context?, mDrawable: Int, imageView: ImageView?) {
        DrawableHelperAboutUs
            .withContext(context!!)
            .withDrawable(mDrawable)
            .tint()
            .applyTo(imageView!!)
    }

    inner class ViewHolder(val moreItemBinding: MoreItemBinding) : RecyclerView.ViewHolder(moreItemBinding.root) {
        init {
            moreItemBinding.root.setOnClickListener { view1: View? ->
                if (KsPreferenceKeys.getInstance().appLanguage.equals("spanish", ignoreCase = true)) {
                    AppCommonMethod.updateLanguage("es", OttApplication.getInstance())
                } else if (KsPreferenceKeys.getInstance().appLanguage.equals("English", ignoreCase = true)) {
                    AppCommonMethod.updateLanguage("en", OttApplication.getInstance())
                }
                Logger.e("Caption", itemsList[layoutPosition])
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                    return@setOnClickListener
                }
                mLastClickTime = SystemClock.elapsedRealtime()
                if (itemsList[layoutPosition].equals(mContext.resources.getString(R.string.my_list), ignoreCase = true)) {
                    itemClickListener.onClick(mContext.resources.getString(R.string.my_list))
                } else if (itemsList[layoutPosition].equals(mContext.resources.getString(R.string.more_account), ignoreCase = true)) {
                    itemClickListener.onClick(mContext.resources.getString(R.string.more_account))
                } else if (itemsList[layoutPosition].equals(mContext.resources.getString(R.string.more_settings), ignoreCase = true)) {
                    itemClickListener.onClick(mContext.resources.getString(R.string.more_settings))
                } else if (itemsList[layoutPosition].equals(mContext.resources.getString(R.string.more_buy_now), ignoreCase = true)) {
                    itemClickListener.onClick(mContext.resources.getString(R.string.more_buy_now))
                } else if (itemsList[layoutPosition].equals(mContext.resources.getString(R.string.manage_subscription), ignoreCase = true)) {
                    itemClickListener.onClick(mContext.resources.getString(R.string.manage_subscription))
                } else if (itemsList[layoutPosition].equals(mContext.resources.getString(R.string.more_order_history), ignoreCase = true)) {
                    itemClickListener.onClick(mContext.resources.getString(R.string.more_order_history))
                } else if (itemsList[layoutPosition].equals(mContext.resources.getString(R.string.privacy_policy), ignoreCase = true)) {
                    itemClickListener.onClick(mContext.resources.getString(R.string.privacy_policy))
                } else if (itemsList[layoutPosition].equals(mContext.resources.getString(R.string.more_term_condition), ignoreCase = true)) {
                    itemClickListener.onClick(mContext.resources.getString(R.string.more_term_condition))
                }
            }
        }
    }
}