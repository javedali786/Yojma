package com.tv.uscreen.yojmatv

import android.content.Context
import android.content.pm.PackageManager
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.messaging.FirebaseMessaging
import com.tv.uscreen.yojmatv.dependencies.DaggerEnveuComponent
import com.tv.uscreen.yojmatv.dependencies.EnveuComponent
import com.tv.uscreen.yojmatv.dependencies.modules.UserPreferencesModule
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys

class OttApplication : MultiDexApplication() {
    var enveuComponent: EnveuComponent? = null
        get() {
            if (field == null) {
                field = DaggerEnveuComponent.builder()
                    .userPreferencesModule(UserPreferencesModule(this))
                    .build()
            }
            return field
        }
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        KsPreferenceKeys.getInstance()
        MultiDex.install(this)
        initializeFirebase()
        firebaseCrashlyticSetup()
        // setUpMoengage();
    }

    private fun initializeFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Logger.d("Fetching FCM registration token failed", task.exception.toString())
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                Logger.d("TokenIs", token!!)
                //    Toast.makeText(OttApplication.this, token, Toast.LENGTH_SHORT).show();
            })
    }

    /*  private void setUpMoengage() {
        MoEngageManager.INSTANCE.init(this);
        MoEFireBaseHelper.getInstance().addTokenListener(MoEngageManager.INSTANCE);
    }
*/
    private fun firebaseCrashlyticSetup() {
        if (KsPreferenceKeys.getInstance().appPrefLoginStatus.equals(
                AppConstants.UserStatus.Login.toString(),
                ignoreCase = true
            )
        ) {
            val userId = KsPreferenceKeys.getInstance().appPrefUserId
            FirebaseCrashlytics.getInstance().setUserId(userId)
        }
    }

    // Huh? Really?
    val version: Int
        get() {
            var v = 0
            try {
                v = packageManager.getPackageInfo(packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                // Huh? Really?
            }
            return v
        }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    // Huh? Really?
    val versionName: String
        get() {
            var v = ""
            try {
                v = packageManager.getPackageInfo(packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                // Huh? Really?
            }
            return v
        }

    companion object {
        var instance: OttApplication? = null
            private set
        val context: Context
            get() = instance!!.applicationContext

        fun getApplicationContext(context: Context): OttApplication {
            return context.applicationContext as OttApplication
        }
    }
}