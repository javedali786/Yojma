package com.breadgangtvnetwork.utils

import android.text.Editable
import android.text.TextWatcher


open class CustomTextWatcher: TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        Logger.d("$s $start $count $after")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Logger.d("$s $start $before $count")
    }

    override fun afterTextChanged(s: Editable?) {
        Logger.d("$s")
    }
}