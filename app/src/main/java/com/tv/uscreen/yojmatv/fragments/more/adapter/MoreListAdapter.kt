package com.tv.uscreen.yojmatv.fragments.more.adapter

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

import com.tv.uscreen.yojmatv.OttApplication
import com.tv.uscreen.yojmatv.R

import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.MoreItemClickListener
import com.tv.uscreen.yojmatv.databinding.MoreItemBinding

import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.AppColors
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper.imageViewDrawableBgColor
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.helpers.DrawableHelperAboutUs
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class MoreListAdapter(private val mContext: Activity, private val itemsList: List<String>, call: MoreItemClickListener, islogin: Boolean) : RecyclerView.Adapter<MoreListAdapter.ViewHolder>() {
    val itemClickListener: MoreItemClickListener
    private val islogin: Boolean
    private var mLastClickTime: Long = 0
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }
    private var gaming= ""
    private var myList= ""
    private var account= ""
    private var settings= ""
    private var buyNow= ""
    private var orderHistory= ""
    private var termsCondition= ""
    private var manageSubscription= ""
    private var privacyPolicy= ""

    init {
        val layoutInflater = LayoutInflater.from(mContext)
        itemClickListener = call
        this.islogin = islogin
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val moreItemBinding = DataBindingUtil.inflate<MoreItemBinding>(LayoutInflater.from(viewGroup.context), R.layout.more_item, viewGroup, false)
        moreItemBinding.colorsData = ColorsHelper
        stringsHelper.instance()?.data?.config?.more_gaming.toString()

        mContext.getString(R.string.more_gaming)
        myList = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.my_list.toString(),
            mContext.getString(R.string.my_list)
        )
        account = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_account.toString(),
            mContext.getString(R.string.more_account)
        )
        settings = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_settings.toString(),
            mContext.getString(R.string.more_settings)
        )
        buyNow = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_buy_now.toString(),
            mContext.getString(R.string.more_buy_now)
        )
        manageSubscription = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_manage_subscription.toString(),
            mContext.getString(R.string.more_manage_subscription)
        )
        orderHistory = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_order_history.toString(),
            mContext.getString(R.string.more_order_history)
        )
        privacyPolicy = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_privacy_policy.toString(),
            mContext.getString(R.string.more_privacy_policy)
        )
        termsCondition = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_term_condition.toString(),
           mContext.getString(R.string.more_term_condition)
        )
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
           // 3 -> callDrawableHelper(mContext, R.drawable.ic_buy_now, v)
           // 4 -> callDrawableHelper(mContext, R.drawable.ic_order_history, v)
            3 -> callDrawableHelper(mContext, R.drawable.ic_privacy, v)
            4 -> callDrawableHelper(mContext, R.drawable.ic_terms, v)
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
                if (itemsList[layoutPosition] == myList) {
                    itemClickListener.onClick(myList)
                } else if (itemsList[layoutPosition] == account) {
                    itemClickListener.onClick(account)
                } else if (itemsList[layoutPosition] == settings) {
                    itemClickListener.onClick(settings)

                }/* else if (itemsList[layoutPosition]== buyNow) {
                    itemClickListener.onClick(buyNow)
                } else if (itemsList[layoutPosition] == manageSubscription) {
                    itemClickListener.onClick(manageSubscription)
                } else if (itemsList[layoutPosition] == orderHistory) {
                    itemClickListener.onClick(orderHistory)
                }*/
                else if (itemsList[layoutPosition]==privacyPolicy) {
                    itemClickListener.onClick(privacyPolicy)
                } else if (itemsList[layoutPosition] ==termsCondition) {
                    itemClickListener.onClick(termsCondition)
                }
            }
        }
    }
}