package com.tv.uscreen.yojmatv

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.moengage.firebase.MoEFireBaseHelper
import com.moengage.pushbase.MoEPushHelper
import com.tv.uscreen.yojmatv.OttApplication.Companion.context
import com.tv.uscreen.yojmatv.activities.splash.ui.ActivitySplash
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class OttMessagingService : FirebaseMessagingService() {
    var assetId: String? = null
    var assetType: String? = null
    var pendingIntent: PendingIntent? = null
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Logger.d("firebase onNewToken: $token")
      //  MoEFireBaseHelper.getInstance().passPushToken(OttApplication.instance!!, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // If the application is in the foreground handle or display both data and notification FCM messages here.
        // Here is where you can display your own notifications built from a received FCM message.
        super.onMessageReceived(remoteMessage)
        Logger.d("From: " + remoteMessage.from)
        Logger.d("Received message: " + remoteMessage.data)
        if (MoEPushHelper.getInstance().isFromMoEngagePlatform(remoteMessage.data) && MoEPushHelper.getInstance().isSilentPush(remoteMessage.data)) {
            Logger.d("MoEngage notification received")
            MoEFireBaseHelper.getInstance().passPushPayload(applicationContext, remoteMessage.data)
            return
        } else if (MoEPushHelper.getInstance().isFromMoEngagePlatform(remoteMessage.data)) {
            Logger.d("FCM_Payload: " + Gson().toJson(remoteMessage.data))
            MoEFireBaseHelper.getInstance().passPushPayload(applicationContext, remoteMessage.data)
        } else if (remoteMessage.data.isNotEmpty()) {
            val jsonData = (remoteMessage.data as Map<*, *>?)?.let { JSONObject(it) }
            Logger.d("FCM_Payload: " + Gson().toJson(remoteMessage))
            Logger.d("FCM_Payload: $jsonData")
            Logger.d("Message data payload: " + remoteMessage.data)
        }
        Logger.d("FCM_Payload: " + Gson().toJson(remoteMessage.notification))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && remoteMessage.notification != null) {
                val jsonData = (remoteMessage.data as Map<*, *>?)?.let { JSONObject(it) }
                Logger.d("FCM_Payload_final: $jsonData")
                if (jsonData!!.has("id")) {
                    assetId = jsonData.getString("mediaContentId")
                    if (jsonData.has("mediaType")) {
                        assetType = jsonData.getString("mediaType")
                    }
                    Logger.d("FCM_Payload_final: $assetId $assetType")
                    KsPreferenceKeys.getInstance().setNotificationPayload(assetId, jsonData)
                }

            Logger.d("Message Notification Body: " + Gson().toJson(remoteMessage.notification))
            displayNotification(remoteMessage.notification)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                displayNotification13(remoteMessage, remoteMessage.notification)
            }
        }
    }


    private fun displayNotification13(remoteMessage: RemoteMessage, notification: RemoteMessage.Notification?) {
        val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val intent = Intent(this, ActivitySplash::class.java)
        intent.putExtra("assetId", assetId)
        intent.putExtra("assetType", assetType)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        var pendingIntent: PendingIntent? = null
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        }
        val notificationBuilder: NotificationCompat.Builder
        notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(remoteMessage.data["gcm_title"]) //                    .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setContentInfo(remoteMessage.data["gcm_title"])
                .setLargeIcon(icon)
                .setColor(ContextCompat.getColor(this, R.color.tph_hint_txt_color))
                .setLights(ContextCompat.getColor(this, R.color.tph_hint_txt_color), 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)
        } else {
            NotificationCompat.Builder(this, "channel_id")
                .setContentTitle(notification!!.title)
                .setContentText(notification.body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setContentInfo(notification.title)
                .setLargeIcon(icon)
                .setColor(this.resources.getColor(R.color.tph_hint_txt_color))
                .setLights(this.resources.getColor(R.color.tph_hint_txt_color), 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher)
        }
        try {
            if (notification != null) {
                val picture_url = notification.imageUrl.toString()
                if (picture_url != null && "" != picture_url) {
                    val url = URL(picture_url)
                    val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    notificationBuilder.setStyle(
                        NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.body)
                    )
                }
            }
        } catch (e: IOException) {
            Logger.w(e)
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Notification Channel is required for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "channel description"
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }


    private fun displayNotification(notification: RemoteMessage.Notification?) {
        val context = context
        val icon = BitmapFactory.decodeResource(OttApplication.context.resources, R.mipmap.ic_launcher)
        val intent = Intent(context, ActivitySplash::class.java)
        intent.putExtra("assetId", assetId)
        intent.putExtra("assetType", assetType)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent: PendingIntent
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
        }
        val notificationBuilder: NotificationCompat.Builder
        notificationBuilder = NotificationCompat.Builder(context, "channel_id")
            .setContentTitle(notification!!.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setContentInfo(notification.title)
            .setLargeIcon(icon)
            .setColor(ContextCompat.getColor(context, R.color.tph_hint_txt_color))
            .setLights(ContextCompat.getColor(context, R.color.tph_hint_txt_color), 1000, 300)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.mipmap.ic_launcher)
        try {
            val pictureUrl = notification.imageUrl.toString()
            if (pictureUrl != null && "" != pictureUrl) {
                val url = URL(pictureUrl)
                val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.body)
                )
            }
        } catch (e: IOException) {
            Logger.w(e)
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "channel description"
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }
}