package com.tv.uscreen.yojmatv.fragments.more.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.tv.uscreen.yojmatv.OttApplication

import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.MoreItemClickListener
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.databinding.MoreItemBinding
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper.instance
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.helpers.DrawableHelperAboutUs
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

class AccountListAdapter(private val mContext: Activity, private val itemsList: List<String>, call: MoreItemClickListener, islogin: Boolean) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {
    val itemClickListener: MoreItemClickListener
    private val islogin: Boolean
    private var mLastClickTime: Long = 0
    private val colorsHelper by lazy { ColorsHelper }

    init {
        val layoutInflater = LayoutInflater.from(mContext)
        itemClickListener = call
        this.islogin = islogin
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val moreItemBinding = DataBindingUtil.inflate<MoreItemBinding>(LayoutInflater.from(viewGroup.context), R.layout.more_item, viewGroup, false)
        //        ThemeHandler.getInstance().applyMoreItemPage(viewGroup.getContext(),moreItemBinding);
        moreItemBinding.colorsData = colorsHelper
        return ViewHolder(moreItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.moreItemBinding.moreListTitle.text = "" + itemsList[i]
        setIcons(holder.moreItemBinding.moreListIcon, i, holder.moreItemBinding)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    fun setIcons(v: ImageView, i: Int, binding: MoreItemBinding?) {
        when (i) {
            0 -> callDrawableHelper(mContext, R.color.item_label_icon_color, R.drawable.ic_edit_user, v)
            1 -> callDrawableHelper(mContext, R.color.item_label_icon_color, R.drawable.ic_change_password, v)
            2 -> callDrawableHelper(mContext, R.color.item_label_icon_color, R.drawable.ic_logout, v)
            else -> {}
        }
    }

    private fun callDrawableHelper(context: Context?, mColor: Int, mDrawable: Int, imageView: ImageView) {
        imageView.background = AppCompatResources.getDrawable(context!!, mDrawable)
        val itemBackgroundColor = instance()?.data?.config?.item_label_icon_color
        val unwrappedDrawableTitle = imageView.background
        val wrappedDrawableTitle = DrawableCompat.wrap(unwrappedDrawableTitle)
        if (itemBackgroundColor != null && !itemBackgroundColor.equals("", ignoreCase = true)) {
            DrawableCompat.setTint(wrappedDrawableTitle, Color.parseColor(itemBackgroundColor))
        } else {
            DrawableCompat.setTint(wrappedDrawableTitle, ContextCompat.getColor(context, mColor))
        }

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
                if (itemsList[layoutPosition].equals(mContext.resources.getString(R.string.edit_profile), ignoreCase = true)) {
                    itemClickListener.onClick(mContext.resources.getString(R.string.edit_profile))
                } else if (itemsList[layoutPosition].equals(mContext.resources.getString(R.string.change_password), ignoreCase = true)) {
                    itemClickListener.onClick(mContext.resources.getString(R.string.change_password))
                } else if (itemsList[layoutPosition].equals(mContext.resources.getString(R.string.account_logout), ignoreCase = true)) {
                    itemClickListener.onClick(mContext.resources.getString(R.string.account_logout))
                }
            }
        }
    }
}