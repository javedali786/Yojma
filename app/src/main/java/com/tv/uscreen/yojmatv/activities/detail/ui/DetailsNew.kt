package com.tv.uscreen.yojmatv.activities.detail.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tv.uscreen.yojmatv.R

import com.tv.uscreen.yojmatv.adapters.player.EpisodeTabAdapter
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean
import com.tv.uscreen.yojmatv.databinding.NewDetailsBinding

import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogSingleButtonFragment
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.fragments.foryou.ui.ForYouThisFragment
import com.tv.uscreen.yojmatv.networking.apistatus.APIStatus
import com.tv.uscreen.yojmatv.networking.responsehandler.ResponseModel
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.cropImage.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.ImageHelper
import com.tv.uscreen.yojmatv.utils.helpers.RailInjectionHelper
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class DetailsNew : BaseBindingActivity<NewDetailsBinding?>(), CommonDialogFragment.EditDialogListener, AlertDialogFragment.AlertDialogListener {
    private var railInjectionHelper: RailInjectionHelper? = null
    private var contentId = 0
    private val forYouThisFragment: ForYouThisFragment? = null
    private val episodeTabAdapter: EpisodeTabAdapter? = null
    private var seriesDetailBean: EnveuVideoItemBean? = null
    private val videoType = ""
    private var contentType = ""
    private val stringsHelper by lazy { StringsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): NewDetailsBinding {
        return NewDetailsBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        contentId = intent.getIntExtra("contentId", 0)
        Log.d("javed", "contentId: $contentId")
        setToolBar()
        connectionObserver()
    }

    private fun setToolBar() {
        binding!!.toolbar.logoMain2.visibility = View.GONE
        binding!!.toolbar.backLayout.visibility = View.VISIBLE
        binding!!.toolbar.titleMid.visibility = View.GONE
        binding!!.toolbar.rlToolBar.visibility = View.GONE
        binding!!.toolbar.backLayout.setOnClickListener { onBackPressed() }
    }

    private fun connectionObserver() {
        connectionValidation(NetworkConnectivity.isOnline(this))
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (java.lang.Boolean.TRUE == aBoolean) {
            customDetail
        } else {
            noConnectionLayout()
        }
    }

    private fun noConnectionLayout() {
        binding!!.progressBar.visibility = View.GONE
        binding!!.connection.noConnectionLayout.visibility = View.VISIBLE
        binding!!.connection.retryTxt.setOnClickListener { v: View? -> connectionObserver() }
    }

    private val customDetail: Unit
        get() {
            binding!!.progressBar.visibility = View.VISIBLE
            railInjectionHelper = ViewModelProvider(this).get(RailInjectionHelper::class.java)
            railInjectionHelper!!.getSeriesDetailsV2(contentId.toString(), false).observe(this@DetailsNew) { response: ResponseModel<*>? ->
                if (response != null) {
                    if (response.status.equals(APIStatus.START.name, ignoreCase = true)) {
                    } else if (response.status.equals(APIStatus.SUCCESS.name, ignoreCase = true)) {
                        binding!!.progressBar.visibility = View.GONE
                        if (response.baseCategory != null) {
                            val enveuCommonResponse = response.baseCategory as RailCommonData
                            parseSeriesData(enveuCommonResponse)
                            val gson = Gson()
                            val json = gson.toJson(enveuCommonResponse)
                            Log.d("javed", "getSeriesDetail1: $json")
                        }
                    } else if (response.status.equals(APIStatus.ERROR.name, ignoreCase = true)) {
                        binding!!.progressBar.visibility = View.GONE
                    } else if (response.status.equals(APIStatus.FAILURE.name, ignoreCase = true)) {
                        showDialog(
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_error.toString(), getString(R.string.popup_error)),
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(), getString(R.string.popup_something_went_wrong))
                        )
                        binding!!.progressBar.visibility = View.GONE
                    }
                }
            }
        }

    private fun parseSeriesData(enveuCommonResponse: RailCommonData?) {
        try {
            if (enveuCommonResponse != null && !enveuCommonResponse.enveuVideoItemBeans.isEmpty()) {
                if (enveuCommonResponse.enveuVideoItemBeans[0].keywords != null && !enveuCommonResponse.enveuVideoItemBeans[0].keywords.isEmpty()) {
                    var stringBuilder = StringBuilder()
                    for (i in enveuCommonResponse.enveuVideoItemBeans[0].keywords.indices) {
                        stringBuilder = if (i == enveuCommonResponse.enveuVideoItemBeans[0].keywords.size - 1) {
                            stringBuilder.append(enveuCommonResponse.enveuVideoItemBeans[0].keywords[i])
                        } else stringBuilder.append(enveuCommonResponse.enveuVideoItemBeans[0].keywords[i]).append(" \u25AA ")
                    }
                    // keyword = stringBuilder.toString();
                }
                seriesDetailBean = enveuCommonResponse.enveuVideoItemBeans[0]
                contentType = seriesDetailBean?.assetType!!
                setUiComponents(seriesDetailBean)
                //  setTabs();
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    private fun setUiComponents(seriesResponse: EnveuVideoItemBean?) {
        binding!!.progressBar.visibility = View.GONE
        binding!!.infoItemScroller.visibility = View.VISIBLE
        binding!!.detailTitleLayout.visibility = View.VISIBLE
        if (seriesResponse != null) {
            ImageHelper.getInstance(this@DetailsNew).loadImageTo(binding!!.bgImg, AppCommonMethod.getListPRImage(seriesResponse.posterURL, this@DetailsNew))
            binding!!.title.text = seriesResponse.title
            binding!!.title1.text = seriesResponse.description
            binding!!.title2.text = seriesResponse.description_two
            binding!!.title3.text = seriesResponse.description_three
        }
    }

    private fun showDialog(title: String, message: String) {
        val fm = supportFragmentManager
        val alertDialog = AlertDialogSingleButtonFragment.newInstance(
            title,
            message,
            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(), getString(R.string.popup_ok))
        )
        alertDialog.isCancelable = false
        alertDialog.setAlertDialogCallBack(this)
        alertDialog.show(fm, "fragment_alert")
    }

    override fun onActionBtnClicked() {}
    override fun onFinishDialog() {}
}