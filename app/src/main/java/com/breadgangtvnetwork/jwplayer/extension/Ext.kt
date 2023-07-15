package com.enveu.player.extension

import android.os.Build
import android.widget.TextView

fun Int.percentOf(num: Long) : Long {
    return if (num > 0) {
        ((this * num) / 100.0f).toLong()
    } else {
        0L
    }
}

fun Long.percent(num: Long) : Int {
    return if (num > 0) {
        ((this * 100.0f) / num).toInt()
    } else {
        0
    }
}

fun TextView.updateAppearance(style: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.setTextAppearance(style)
    } else {
        this.setTextAppearance(this.context, style)
    }
}