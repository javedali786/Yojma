package com.tv.uscreen.yojmatv.utils.colorsJson.converter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tv.uscreen.yojmatv.OttApplication
import com.tv.uscreen.yojmatv.utils.colorsJson.model.ColorsModel
import com.tv.uscreen.yojmatv.utils.helpers.SharedPrefHelper
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

object ColorsHelper {

    fun loadDataFromJson(): ColorsModel? {
        var users: ColorsModel? = null
        val json: String = try {
            val `is` = OttApplication.getContext().assets.open("ColorsData.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            String(buffer, StandardCharsets.UTF_8)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return null
        }

        val listType = object : TypeToken<ColorsModel?>() {}.type
        if (null != listType) {
            users = Gson().fromJson(json, listType)
        }
        return users
    }

    fun instance(): ColorsModel? {
        var colorModel: ColorsModel? = null

        val jsonData = SharedPrefHelper.getInstance().colorJson

        val listType = object : TypeToken<ColorsModel?>() {}.type
        if (null != listType) {
            colorModel = Gson().fromJson(jsonData, listType)
        }
        return colorModel
    }

    fun colorParser(colorsHelper: String, localStringColor: Int): Int {
        val colorCode: Int = if (colorsHelper != null && colorsHelper != "null" && colorsHelper != "") {
            if (hexValidator(colorsHelper)) {
                Color.parseColor(colorsHelper)
            }else{
                Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(OttApplication.getInstance(), localStringColor)))
            }
        } else {
            Color.parseColor("#" + Integer.toHexString(ContextCompat.getColor(OttApplication.getInstance(), localStringColor)))
        }
        return colorCode
    }

    private fun hexValidator(str: String?): Boolean {
        val regex = "^#([A-Fa-f0-9]{6,8}|[A-Fa-f0-9]{3})$"
        val p = Pattern.compile(regex)
        if (str == null) {
            return false
        }
        val m = p.matcher(str)
        return m.matches()
    }

    fun strokeBgDrawable(tphBgColor: Int, tphBorderColor: Int, cornerRadius: Float): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = cornerRadius
        drawable.setStroke(3, tphBorderColor)
        drawable.setColor(tphBgColor)
        return drawable
    }

    fun imageViewDrawableBgColor(imageView: ImageView, mColor: Int) {
        val unwrappedDrawableTitle = imageView.background
        val wrappedDrawableTitle = DrawableCompat.wrap(unwrappedDrawableTitle)
        DrawableCompat.setTint(wrappedDrawableTitle, mColor)
    }

    fun textViewDrawableColor(textView: TextView, color: Int) {
        for (drawable in textView.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
            }else{
                for (drawableRight in textView.compoundDrawablesRelative) {
                    if (drawableRight != null) {
                        drawableRight.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
                    }
                }
            }
        }
    }
}
