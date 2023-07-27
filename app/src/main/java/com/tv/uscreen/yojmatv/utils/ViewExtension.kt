package com.tv.uscreen.yojmatv.utils

import android.text.Html
import android.widget.TextView

fun TextView.htmlParseToString(htmlString:String){
    this.text = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT)
}