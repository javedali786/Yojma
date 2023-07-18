package com.tv.uscreen.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.google.android.material.internal.FlowLayout

@SuppressLint("RestrictedApi")
class CustomFlowLayout: FlowLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)
}