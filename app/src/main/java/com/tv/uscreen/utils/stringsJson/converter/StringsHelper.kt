package com.tv.uscreen.utils.stringsJson.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tv.uscreen.OttApplication
import com.tv.uscreen.utils.helpers.SharedPrefHelper
import com.tv.uscreen.utils.stringsJson.model.StringsData
import java.nio.charset.StandardCharsets

object StringsHelper {

    fun loadDataFromJson(): StringsData? {
        var users: StringsData? = null
        val json: String = try {
            val `is` = OttApplication.getContext().assets.open("StringsData.json")
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