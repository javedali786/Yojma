package com.tv.uscreen.yojmatv.activities.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.databinding.ManageSubscriptionNewBinding

import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class ManageSubscriptionAccount : BaseBindingActivity<ManageSubscriptionNewBinding?>(){

    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }

    override fun inflateBindingLayout(inflater: LayoutInflater): ManageSubscriptionNewBinding {
        return ManageSubscriptionNewBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding!!.stringData = stringsHelper
        binding!!.colorsData = colorsHelper
        setUi()
    }
    private fun setUi() {
        binding!!.backIcon.setOnClickListener { view: View? -> onBackPressed() }
    }
}