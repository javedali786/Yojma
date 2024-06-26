package com.tv.uscreen.yojmatv.jwplayer.cast


import android.content.Context
import com.google.android.gms.cast.CastMediaControlIntent
import com.google.android.gms.cast.LaunchOptions
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import java.util.Locale

class CastOptionsProvider : OptionsProvider {
    override fun getCastOptions(context: Context): CastOptions {

        val launchOptions = LaunchOptions.Builder()
            .setLocale(Locale.getDefault())
            .build()

        return CastOptions.Builder()
            .setReceiverApplicationId(CastMediaControlIntent.DEFAULT_MEDIA_RECEIVER_APPLICATION_ID)
            .setLaunchOptions(launchOptions)
            .build()
//        val notificationOptions = NotificationOptions.Builder()
//            .setTargetActivityClassName(ExpandedControlsActivity::class.java.name)
//            .build()
//        val mediaOptions = CastMediaOptions.Builder()
//            .setNotificationOptions(notificationOptions)
//            .setExpandedControllerActivityClassName(ExpandedControlsActivity::class.java.name)
//            .build()
//        val launchOptions = LaunchOptions.Builder()
//            .setAndroidReceiverCompatible(true)
////            .setCredentialsData(credentialsData)
//            .build()
//        return CastOptions.Builder()
//            .setLaunchOptions(launchOptions)
//            .setReceiverApplicationId(context.getString(R.string.cast_receiver_app_id))
//            .setStopReceiverApplicationWhenEndingSession(true)
//            .setCastMediaOptions(mediaOptions)
//            .build()
    }

    override fun getAdditionalSessionProviders(context: Context): List<SessionProvider>? {
        return null
    }
}