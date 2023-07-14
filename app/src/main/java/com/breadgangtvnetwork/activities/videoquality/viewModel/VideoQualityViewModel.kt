package com.breadgangtvnetwork.activities.videoquality.viewModel

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.activities.videoquality.bean.LanguageItem
import com.breadgangtvnetwork.activities.videoquality.bean.TrackItem
import com.breadgangtvnetwork.utils.Logger
import com.breadgangtvnetwork.utils.stringsJson.converter.StringsHelper

class VideoQualityViewModel(application: Application) : AndroidViewModel(application) {

    private val stringsHelper by lazy { StringsHelper }

    val languageList: ArrayList<LanguageItem>
        get() {
            Logger.e("Locale", getApplication<Application>().getString(R.string.language_english))
            val trackItems = ArrayList<LanguageItem>()
            for (i in 0..2) {
                if (i == 0) {
                    val languageItem = LanguageItem()
                    languageItem.languageName = getApplication<Application>().getString(R.string.language_english)
                    trackItems.add(languageItem)
                } else if (i == 1) {
                    val languageItem = LanguageItem()
                    languageItem.languageName = getApplication<Application>().getString(R.string.language_spanish)
                    trackItems.add(languageItem)
                }
            }
            return trackItems
        }

    fun getQualityList(resources: Resources): ArrayList<TrackItem> {
        val trackItems = ArrayList<TrackItem>()
        for (i in 0..3) {
            when (i) {
                0 -> {
                    val auto = stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.streaming_auto.toString(),
                        "Auto"
                    )
                    trackItems.add(TrackItem(auto, resources.getString(R.string.ep_video_auto), resources.getString(R.string.description_auto)))
                }
                1 -> {
                    val sd = stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.streaming_sd.toString(),
                        "SD"
                    )
                    trackItems.add(TrackItem(sd, resources.getString(R.string.low), resources.getString(R.string.description_sd)))
                }
                2 -> {
                    val hd = stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.streaming_hd.toString(),
                        "HD"
                    )
                    trackItems.add(TrackItem(hd, resources.getString(R.string.medium), resources.getString(R.string.description_hd)))
                }
                3 -> {
                    val fullHd = stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.streaming_full_hd.toString(),
                        "Full HD"
                    )
                    trackItems.add(TrackItem(fullHd, resources.getString(R.string.high), resources.getString(R.string.description_full_hd)))
                }
            }
        }
        return trackItems
    }
}