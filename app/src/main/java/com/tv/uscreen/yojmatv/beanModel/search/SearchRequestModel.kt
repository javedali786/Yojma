package com.tv.uscreen.yojmatv.beanModel.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class SearchRequestModel(): Parcelable {
    var keyword: String? = null
    var size = 4
    var offset = 0
    var filters = ArrayList<String>()
    var sort: String? = null


    fun addFilter(filter: String) {
        filters.add(filter)
    }

}
