package com.tv.uscreen.yojmatv.activities.videoquality.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.activities.videoquality.adapter.VideoQualityAdapter
import com.tv.uscreen.yojmatv.activities.videoquality.bean.TrackItem
import com.tv.uscreen.yojmatv.activities.videoquality.callBack.NotificationItemClickListner
import com.tv.uscreen.yojmatv.activities.videoquality.viewModel.VideoQualityViewModel
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.databinding.VideoQualityActivityBinding

import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class VideoQualityActivity : BaseBindingActivity<VideoQualityActivityBinding?>(), NotificationItemClickListner {
    private var viewModel: VideoQualityViewModel? = null
    private var notificationAdapter: VideoQualityAdapter? = null
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): VideoQualityActivityBinding {
        return VideoQualityActivityBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
        binding!!.colorsData = colorsHelper
        binding!!.stringData = stringsHelper
        binding!!.toolbar.colorsData = colorsHelper
        binding!!.toolbar.stringData = stringsHelper
        callModel()
        connectionObserver()
        setToolBar()
        val properties = Properties()
        properties.addAttribute(AppConstants.STREAMING_SETTING, AppConstants.STREAMING_SETTING)
        MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties)
    }

    private fun setToolBar() {
        binding!!.toolbar.logoMain2.visibility = View.GONE
        binding!!.toolbar.backLayout.visibility = View.VISIBLE
        binding!!.toolbar.titleMid.visibility = View.GONE
        val streamingTile = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.streaming_settings_title.toString(),
            getString(R.string.streaming_settings_title)
        )
        binding!!.toolbar.titleMid.text = streamingTile
        binding!!.toolbar.titleMid.setBackgroundResource(0)
        binding!!.toolbar.backLayout.setOnClickListener { onBackPressed() }
    }

    private fun callModel() {
        viewModel = ViewModelProvider(this)[VideoQualityViewModel::class.java]
    }

    private fun connectionObserver() {
        connectionValidation(NetworkConnectivity.isOnline(this))
    }

    private var arrayList: ArrayList<TrackItem>? = null
    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            binding!!.noConnectionLayout.visibility = View.GONE
            arrayList = viewModel!!.getQualityList(resources)
            uiInitialisation()
            setAdapter()
        } else {
            noConnectionLayout()
        }
    }

    private fun setAdapter() {
        notificationAdapter = VideoQualityAdapter(this@VideoQualityActivity, arrayList, this@VideoQualityActivity)
        binding!!.recyclerview.adapter = notificationAdapter
    }

    private fun uiInitialisation() {
        binding!!.recyclerview.hasFixedSize()
        binding!!.recyclerview.isNestedScrollingEnabled = false
        binding!!.recyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun noConnectionLayout() {
        binding!!.noConnectionLayout.visibility = View.VISIBLE
    }

    override fun onClick(id: String, status: String) {
        notificationAdapter!!.notifyDataSetChanged()
    }
}