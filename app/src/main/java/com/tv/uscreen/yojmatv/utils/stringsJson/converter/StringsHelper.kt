package com.tv.uscreen.yojmatv.utils.stringsJson.converter

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tv.uscreen.yojmatv.OttApplication
import com.tv.uscreen.yojmatv.utils.helpers.SharedPrefHelper
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.model.StringsData
import java.io.InputStream
import java.nio.charset.StandardCharsets

object StringsHelper {

    fun loadDataFromJson(): StringsData? {
        var users: StringsData? = null
        val json: String = try {
            var `is`:InputStream? = null
            Log.d("loadDataFromJson", "loadDataFromJson: " + KsPreferenceKeys.getInstance().appLanguage)
            `is` = if (KsPreferenceKeys.getInstance().appLanguage == "English") {
                OttApplication.getContext().assets.open("StringsData.json")
            } else {
                OttApplication.getContext().assets.open("SpanishStringsData.json")
            }

            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return null
        }

        val listType = object : TypeToken<StringsData?>() {}.type
        if (null != listType) {
            users = Gson().fromJson(json, listType)
        }
        return users
    }

    fun instance(): StringsData? {
        var stringData: StringsData? = null

        val jsonData = SharedPrefHelper.getInstance().stringJson

        val listType = object : TypeToken<StringsData?>() {}.type
        if (null != listType) {
            stringData = Gson().fromJson(jsonData, listType)
        }
        return stringData
    }

    fun stringParse(jsonKey: String, localKey: String): String {
        val value: String = if (jsonKey != null && jsonKey != "null" && jsonKey != "") {
            jsonKey
        } else {
            localKey
        }
        return value
    }
}