package com.tv.uscreen.activities.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.tv.uscreen.baseModels.BaseBindingActivity
import com.tv.uscreen.databinding.ManageSubscriptionNewBinding
import com.tv.uscreen.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.utils.stringsJson.converter.StringsHelper

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