package com.breadgangtvnetwork.activities.profile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.breadgangtvnetwork.baseModels.BaseBindingActivity
import com.breadgangtvnetwork.databinding.ManageSubscriptionNewBinding
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper
import com.breadgangtvnetwork.utils.stringsJson.converter.StringsHelper

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