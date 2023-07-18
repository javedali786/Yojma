package com.tv.uscreen.yojmatv.utils.TrackerUtil

import android.app.Application
import com.moengage.core.DataCenter
import com.moengage.core.LogLevel
import com.moengage.core.MoEngage
import com.moengage.core.config.FcmConfig
import com.moengage.core.config.LogConfig
import com.moengage.core.config.NotificationConfig
import com.moengage.pushbase.MoEPushHelper
import com.moengage.pushbase.listener.TokenAvailableListener
import com.moengage.pushbase.model.Token
import com.tv.uscreen.yojmatv.BuildConfig
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.jwplayer.utils.Logger

object MoEngageManager : TokenAvailableListener {

    fun init(context: Application) {
        val moEngage = MoEngage.Builder(context, BuildConfig.MOENGAGE_APP_ID)
            .setDataCenter(DataCenter.DATA_CENTER_2)
            .configureNotificationMetaData(
                NotificationConfig(
                    R.mipmap.ic_launcher,
                    R.color.tph_hint_txt_color
                )
            )
            .configureFcm(FcmConfig(false))
            .configureLogs(LogConfig(LogLevel.VERBOSE, false))
            .build()
        MoEngage.initialiseDefaultInstance(moEngage)
        MoEPushHelper.getInstance().registerMessageListener(MoEPushMessageListener())
    }

    override fun onTokenAvailable(token: Token) {
        Logger.d("FirebaseToken",token.pushToken)
    }
}