package com.tv.activities.videoquality.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.tv.OttApplication
import com.tv.R
import com.tv.activities.videoquality.bean.TrackItem
import com.tv.activities.videoquality.callBack.NotificationItemClickListner
import com.tv.databinding.VideoQualityItemBinding
import com.tv.utils.colorsJson.converter.AppColors
import com.tv.utils.colorsJson.converter.ColorsHelper
import com.tv.utils.colorsJson.converter.ColorsHelper.strokeBgDrawable
import com.tv.utils.commonMethods.AppCommonMethod.Companion.updateLanguage
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.utils.stringsJson.converter.StringsHelper

class VideoQualityAdapter(activity: Activity?, private val inboxMessages: List<TrackItem>?, private val itemClickListener: NotificationItemClickListner) :
    RecyclerView.Adapter<VideoQualityAdapter.SingleItemRowHolder>() {
    private var pos: Int

    init {
        pos = KsPreferenceKeys.getInstance().qualityPosition
        if (KsPreferenceKeys.getInstance().appLanguage.equals("spanish", ignoreCase = true) || KsPreferenceKeys.getInstance().appLanguage.equals("हिंदी", ignoreCase = true)) {
            updateLanguage("es", OttApplication.getInstance())
        } else if (KsPreferenceKeys.getInstance().appLanguage.equals("English", ignoreCase = true)) {
            updateLanguage("en", OttApplication.getInstance())
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): SingleItemRowHolder {
        val itemBinding = DataBindingUtil.inflate<VideoQualityItemBinding>(
            LayoutInflater.from(viewGroup.context),
            R.layout.video_quality_item, viewGroup, false
        )
        //      ThemeHandler.getInstance().applyChangeLangeAdapter(viewGroup.getContext(),itemBinding);
        itemBinding.colorsData = ColorsHelper
        itemBinding.stringData = StringsHelper
        return SingleItemRowHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: SingleItemRowHolder, @SuppressLint("RecyclerView") position: Int) {
        if (pos == position) {
            viewHolder.notificationItemBinding.parentLayout.background = strokeBgDrawable(AppColors.itemLabelBgColor(),AppColors.itemLabelBgColor(), 0f)
        } else {
            viewHolder.notificationItemBinding.parentLayout.setBackgroundResource(0)
        }
        viewHolder.notificationItemBinding.titleText.text = inboxMessages?.get(position)?.trackName
        //  viewHolder.notificationItemBinding.secondTitleText.setText(inboxMessages.get(position).getDescription());
        viewHolder.notificationItemBinding.parentLayout.setOnClickListener { view: View? ->
            pos = position
            KsPreferenceKeys.getInstance().qualityPosition = pos
            KsPreferenceKeys.getInstance().qualityName = inboxMessages?.get(position)?.uniqueId
            itemClickListener.onClick("", "")
        }
    }

    override fun getItemCount(): Int {
        return inboxMessages?.size!!
    }

    inner class SingleItemRowHolder internal constructor(val notificationItemBinding: VideoQualityItemBinding) : RecyclerView.ViewHolder(
        notificationItemBinding.root
    )
}