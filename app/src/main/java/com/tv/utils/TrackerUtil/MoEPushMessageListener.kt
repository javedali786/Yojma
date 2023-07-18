package com.tv.utils.TrackerUtil

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.moengage.pushbase.push.PushMessageListener
import com.tv.activities.splash.ui.ActivitySplash
import com.tv.utils.Logger

class MoEPushMessageListener: PushMessageListener() {
    override fun onNotificationClick(activity: Activity, payload: Bundle) {
        super.onNotificationClick(activity, payload)


        Logger.d("payload: $payload")

        val assetType: String? = payload.getString("mediaType")
        val assetId: String? = payload.getString("id")
        val seriesId: String? = payload.getString("seriesId")
        val seasonNumber: String? = payload.getString("seasonNumber")

        val intent = Intent(activity, ActivitySplash::class.java).apply {
            putExtra("assetId", assetId)
            putExtra("assetType", assetType)
            putExtra("seriesId", seriesId)
            putExtra("seasonNumber", seasonNumber)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        activity.startActivity(intent)
    }

    override fun onNotificationReceived(context: Context, payload: Bundle) {
        super.onNotificationReceived(context, payload)
        MoEngageNotificationManager.getAllNotifications()
    }
}