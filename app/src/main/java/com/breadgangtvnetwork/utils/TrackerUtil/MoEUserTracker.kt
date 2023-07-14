package com.breadgangtvnetwork.utils.TrackerUtil

import android.content.Context
import android.text.format.DateFormat
import com.breadgangtvnetwork.OttApplication
import com.breadgangtvnetwork.utils.Logger
import com.breadgangtvnetwork.utils.ObjectHelper
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.model.UserGender
import java.text.DecimalFormat
import java.util.Date

object MoEUserTracker {

    private fun getNonNullContext(context: Context?): Context {
        return context ?: OttApplication.getInstance()
    }

    private fun getMoEHelper(context: Context?): MoEHelper {
        return MoEHelper.getInstance(getNonNullContext(context))
    }

    fun logout(context: Context?) {
        Logger.d("Logging out user")
        getMoEHelper(context).logoutUser()
    }

    fun setUniqueId(context: Context?, id: String?) {
        Logger.d("setting unique id: $id")
        id?.let { _id -> getMoEHelper(context).setUniqueId(_id) }
    }

    fun setEmail(context: Context?, email: String?) {
        Logger.d("setting email: $email")
        email?.let { _email -> getMoEHelper(context).setEmail(_email) }
    }

    fun setUsername(context: Context?, username: String?) {
        Logger.d("setting username: $username")
        username?.let { _username -> getMoEHelper(context).setFullName(_username) }
    }

    private fun setPhone(context: Context?, phone: String?) {
        Logger.d("setting phone: $phone")
        phone?.let { _phone -> getMoEHelper(context).setNumber(_phone) }
    }

    private fun setGender(context: Context?, gender: String?) {
        Logger.d("setting gender: $gender")
        gender?.let { _gender ->
            when {
                ObjectHelper.isSame(_gender, "male") ->
                    getMoEHelper(context).setGender(UserGender.MALE)
                ObjectHelper.isSame(_gender, "female") ->
                    getMoEHelper(context).setGender(UserGender.FEMALE)
                ObjectHelper.isSame(_gender, "others") ->
                    getMoEHelper(context).setGender(UserGender.OTHER)
            }
        }
    }

    private fun setDob(context: Context?, dob: Long?) {
        Logger.d("setting dob: $dob")
        dob?.let { _dob ->
            val decimalFormat = DecimalFormat("#")
            decimalFormat.maximumFractionDigits = 0
            val longDate = decimalFormat.format(_dob).toLong()
            Logger.d("dob (long): $longDate")
            val dateString = DateFormat.format("MMMM dd,yyyy", Date(longDate)).toString()
            Logger.d("dob: $dateString")
            getMoEHelper(context).setBirthDate(Date(longDate))
        }
    }

    fun setUserProperties(context: Context?,userId : Int,username: String?,email: String?,phone: String?,dob: Long?) {
        Logger.d("called from : ${Logger.getTag()}")
        val json = KsPreferenceKeys.getInstance().userProfileData
        Logger.d("user data: $json")
       /* val response: UserProfileResponse? = Gson().fromJson(json, UserProfileResponse::class.java)
        response?.data?.let { user ->}*/
            setUsername(context, username)
            setEmail(context, email.toString())
            setPhone(context, phone.toString())
            setUniqueId(context,userId.toString())
            if (ObjectHelper.isNotEmpty(dob)) {
                setDob(context, dob)
            }

    }


}