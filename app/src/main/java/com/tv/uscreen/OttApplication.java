package com.tv.uscreen;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.moengage.firebase.MoEFireBaseHelper;
import com.tv.uscreen.dependencies.DaggerEnveuComponent;
import com.tv.uscreen.dependencies.EnveuComponent;
import com.tv.uscreen.dependencies.modules.UserPreferencesModule;
import com.tv.uscreen.utils.Logger;
import com.tv.uscreen.utils.TrackerUtil.MoEngageManager;
import com.tv.uscreen.utils.constants.AppConstants;
import com.tv.uscreen.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
public class OttApplication extends MultiDexApplication {
    private EnveuComponent enveuComponent;
    private static OttApplication ottApplication;

    public static OttApplication getInstance() {
        return ottApplication;
    }
    public static Context getContext() {
              return getInstance().getApplicationContext();
          }
    @Override
    public void onCreate() {
        super.onCreate();
        ottApplication = this;
        KsPreferenceKeys.getInstance();
        MultiDex.install(this);
        initializeFirebase();
        firebaseCrashlyticSetup();
        setUpMoengage();

    }

    private void initializeFirebase() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Logger.d("Fetching FCM registration token failed", task.getException().toString());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Logger.d("TokenIs",token);
                    //    Toast.makeText(OttApplication.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void setUpMoengage() {
        MoEngageManager.INSTANCE.init(this);
        MoEFireBaseHelper.getInstance().addTokenListener(MoEngageManager.INSTANCE);
    }

    private void firebaseCrashlyticSetup() {
        if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus().equalsIgnoreCase(AppConstants.UserStatus.Login.toString())) {
            String userId = KsPreferenceKeys.getInstance().getAppPrefUserId();
            FirebaseCrashlytics.getInstance().setUserId(userId);
        }
    }



    public int getVersion() {
        int v = 0;
        try {
            v = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Huh? Really?
        }
        return v;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static OttApplication getApplicationContext(Context context) {
        return (OttApplication) context.getApplicationContext();
    }

    public EnveuComponent getEnveuComponent() {
        if (this.enveuComponent == null) {
            this.enveuComponent = DaggerEnveuComponent.builder()
                    .userPreferencesModule(new UserPreferencesModule(this))
                    .build();
        }
        return this.enveuComponent;
    }

    public String getVersionName() {
        String v = "";
        try {
            v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // Huh? Really?
        }
        return v;
    }



}
