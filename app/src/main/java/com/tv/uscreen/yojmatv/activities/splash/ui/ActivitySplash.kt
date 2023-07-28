package com.tv.uscreen.yojmatv.activities.splash.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.gson.Gson
import com.moengage.core.analytics.MoEAnalyticsHelper
import com.moengage.core.model.AppStatus
import com.tv.uscreen.yojmatv.OttApplication
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.SDKConfig
import com.tv.uscreen.yojmatv.activities.homeactivity.ui.HomeActivity
import com.tv.uscreen.yojmatv.activities.purchase.in_app_billing.BillingProcessor
import com.tv.uscreen.yojmatv.activities.purchase.in_app_billing.InAppProcessListener
import com.tv.uscreen.yojmatv.activities.purchase.in_app_billing.RestoreSubscriptionCallback
import com.tv.uscreen.yojmatv.activities.purchase.plans_layer.GetPlansLayer
import com.tv.uscreen.yojmatv.activities.splash.dialog.ConfigFailDialog
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.callbacks.apicallback.ApiResponseModel
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.DialogInterface
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.VersionUpdateCallBack
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.VersionValidator
import com.tv.uscreen.yojmatv.databinding.ActivitySplashBinding
import com.tv.uscreen.yojmatv.dependencies.providers.DTGPrefrencesProvider
import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogFragment
import com.tv.uscreen.yojmatv.networking.errormodel.ApiErrorModel
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.config.ConfigManager
import com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse.ConfigBean
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.AnalyticsController
import com.tv.uscreen.yojmatv.utils.helpers.ForceUpdateHandler
import com.tv.uscreen.yojmatv.utils.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.SharedPrefHelper
import com.tv.uscreen.yojmatv.utils.helpers.downloads.DownloadHelper
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject


class ActivitySplash : BaseBindingActivity<ActivitySplashBinding?>(), AlertDialogFragment.AlertDialogListener, InAppProcessListener {
    private val TAG = this.javaClass.simpleName

    @set:Inject
    public var dtgPrefrencesProvider: DTGPrefrencesProvider? = null
    private var forceUpdateHandler: ForceUpdateHandler? = null
    private var session: KsPreferenceKeys? = null
    private var viaIntent = false
    private var currentLanguage: String? = null
    private var configBean: ConfigBean? = null
    private var configCall = 1
    var clapanimation = 1
    private var notid: String? = ""
    private var notAssetType: String? = ""
    private var notificationAssetId = 0
    private var deepLinkObject: JSONObject? = null
    private val colorsHelper by lazy { ColorsHelper }
    private val stringsHelper by lazy { StringsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(inflater)
    }

    var downloadHelper: DownloadHelper? = null
    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult<String, Boolean>(
            ActivityResultContracts.RequestPermission(),
            ActivityResultCallback<Boolean> { isGranted: Boolean? -> })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        if (NetworkConnectivity.isOnline(this)) {
            initView()
            binding!!.noConnectionLayout.visibility = View.GONE
        } else {
            binding!!.noConnectionLayout.visibility = View.VISIBLE
        }
    }

    private fun initView() {
        if (TextUtils.isEmpty(KsPreferenceKeys.getInstance().qualityName)) {
            KsPreferenceKeys.getInstance().qualityName = "Auto"
            KsPreferenceKeys.getInstance().qualityPosition = 0
        }
        SharedPrefHelper.getInstance().setColorJson(ColorsHelper.loadDataFromJson())
        SharedPrefHelper.getInstance().setStringJson(StringsHelper.loadDataFromJson())
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
        binding?.colorsData = colorsHelper
        session = KsPreferenceKeys.getInstance()
        initBilling()
        AppCommonMethod.getPushToken(this)
        updateAndroidSecurityProvider(this@ActivitySplash)
        AnalyticsController(this@ActivitySplash).callAnalytics("splash_screen", "Action", "Launch")
        currentLanguage = KsPreferenceKeys.getInstance().appLanguage
        OttApplication.getApplicationContext(this).enveuComponent.inject(this)
        notificationCheck()
        connectionObserver()
        binding?.connection?.retryTxt?.setOnClickListener { connectionObserver() }
        // binding.noConnectionLayout.btnMyDownloads.setOnClickListener(view -> ActivityLauncher.getInstance().launchMyDownloads(ActivitySplash.this));
        Logger.d("IntentData: " + this.intent.data)
        printKeyHash()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermission()
        } else {
            Logger.e("First_Screen", "Device_below_Android_13")
        }
    }

    @RequiresApi(api = 33)
    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
        } else {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    var bp: BillingProcessor? = null
    private fun initBilling() {
        bp = BillingProcessor(this@ActivitySplash, this)
        bp!!.initializeBillingProcessor()
        val serviceIntent = Intent("com.android.vending.billing.InAppBillingService.BIND")
        serviceIntent.setPackage("com.android.vending")
    }

    private val dynamicLink: Unit
        get() {
            FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    try {
                        var deepLink: Uri? = null
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.link
                            Log.e(
                                "deepLink",
                                "in2" + pendingDynamicLinkData.link + " " + deepLink?.query
                            )
                            if (deepLink != null) {
                                val uri = Uri.parse(deepLink.toString())
                                var id: String? = null
                                var mediaType: String? = null
                                try {
                                    id = uri.getQueryParameter("id")
                                    mediaType = uri.getQueryParameter(MEDIA_TYPE)
                                } catch (e: Exception) {
                                    Logger.w(e)
                                }
                                try {
                                    if (mediaType != null) {
                                        if (!mediaType.equals(
                                                "",
                                                ignoreCase = true
                                            ) && !id.equals("", ignoreCase = true)
                                        ) {
                                            KsPreferenceKeys.getInstance().appPrefJumpTo = mediaType
                                            KsPreferenceKeys.getInstance().appPrefBranchIo = true
                                            KsPreferenceKeys.getInstance().appPrefJumpBackId =
                                                id!!.toInt()
                                            deepLinkObject =
                                                AppCommonMethod.createDynamicLinkObject(
                                                    id,
                                                    mediaType
                                                )
                                            redirections(deepLinkObject)
                                        }
                                    } else {
                                        redirectToHome()
                                    }
                                } catch (e: Exception) {
                                    redirectToHome()
                                }
                            }
                        } else {
                            onNewIntent(intent)
                        }
                    } catch (e: Exception) {
                        redirectToHome()
                        Logger.e("Catch", e.toString())
                    }
                }
                .addOnFailureListener(this, OnFailureListener { e ->
                    redirectToHome()
                    Logger.w(e)
                    Log.w(TAG, "getDynamicLink:onFailure", e)
                })
        }

    private fun printKeyHash(): String? {
        val packageInfo: PackageInfo
        var key: String? = null
        try {
            //getting application package name, as defined in manifest
            val packageName: String = this.packageName

            //Retrieving package info
            packageInfo = this.packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
            Log.e("Package Name=", this.applicationContext.packageName)
            for (signature in packageInfo.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                key = String(Base64.encode(md.digest(), 0))

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key)
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("Name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("No such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("Exception", e.toString())
        }
        return key
    }

    //        try {
    //            PackageInfo info = getPackageManager().getPackageInfo(
    //                    "com.terramedia.iberalia",
    //                    PackageManager.GET_SIGNATURES);
    //            for (Signature signature : info.signatures) {
    //                MessageDigest md = MessageDigest.getInstance("SHA");
    //                md.update(signature.toByteArray());
    //                Logger.w("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
    //            }
    //        } catch (PackageManager.NameNotFoundException e) {
    //            Logger.w("Exception", "" + e);
    //        } catch (NoSuchAlgorithmException e) {
    //            Logger.w("Exception", "" + e);
    //        }
    private fun callConfig(jsonObject: JSONObject?, updateType: String?) {
        ConfigManager.getInstance().getConfig(this@ActivitySplash, object : ApiResponseModel<Any> {
            override fun onStart() {}
            override fun onSuccess(response: Any?) {
                Log.w("identifiers", "in callConfig")
                Logger.e("Animation End", "Config Call Started")
                val isTablet: Boolean = resources.getBoolean(R.bool.isTablet)
                configBean = AppCommonMethod.configResponse
                val gson = Gson()
                val json: String = gson.toJson(configBean)
                Logger.d("configResponseLog: $json")
                AppCommonMethod.setConfigConstant(configBean, isTablet)
                setupBaseClient()
                updateLanguage(configBean!!.data.appConfig.primaryLanguage)
                KsPreferenceKeys.getInstance().ovpbaseurl = SDKConfig.getInstance().ovP_BASE_URL
                if (configBean != null) {
                    startClapAnimation(jsonObject, updateType, isTablet)
                } else {
                    configFailPopup()
                }
            }

            override fun onError(httpError: ApiErrorModel) {
                configFailPopup()
            }

            override fun onFailure(httpError: ApiErrorModel) {
                configFailPopup()
            }
        })
    }

    private fun startClapAnimation(
        jsonObject: JSONObject?,
        updateType: String?,
        isTablet: Boolean
    ) {
        val preference: KsPreferenceKeys
        val isUserVerified: String
        var isLoggedIn = false
        preference = KsPreferenceKeys.getInstance()
        if (preference.getAppPrefLoginStatus()
                .equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)
        ) {
            isLoggedIn = true
        }
        isUserVerified = preference.isVerified
        Logger.d("branchRedirectors onAnimationEnd1")
        if (jsonObject != null) {
            if (updateType != null && updateType.equals(
                    ForceUpdateHandler.RECOMMENDED,
                    ignoreCase = true
                )
            ) {
                branchRedirections(jsonObject)
            } else {
                val updateValue = getForceUpdateValue(jsonObject, 1)
                if (!updateValue) {
                    branchRedirections(jsonObject)
                }
            }
        } else {
            if (updateType != null && updateType.equals(
                    ForceUpdateHandler.RECOMMENDED,
                    ignoreCase = true
                )
            ) {
                Logger.d("branchRedirectors -->>config")
                // homeRedirection();
                Handler().postDelayed({
                    Logger.d("branchRedirectors -->>non")
                    //This logic is for now will update later
                            ActivityLauncher.getInstance()
                                .homeActivity(this@ActivitySplash, HomeActivity::class.java)
                    finish()
                }, 1)
            } else {
                val updateValue = getForceUpdateValue(null, 3)
                if (!updateValue) {
                    Logger.d("branchRedirectors -->>config")
                    // homeRedirection();
                    Handler().postDelayed({
                        Logger.d("branchRedirectors -->>non")
                                ActivityLauncher.getInstance()
                                    .homeActivity(this@ActivitySplash, HomeActivity::class.java)
                    }, 1)
                }
            }
        }
    }

    private fun notificationCheck() {
        Logger.w("notificationCheck", "in")
        if (intent != null) {
            Logger.w("notificationCheck", "notnull")
            if (intent.extras != null) {
                Logger.w("notificationCheck", "extra")
                val bundle: Bundle? = intent.extras
                if (bundle != null) {
                    notid = bundle.getString("id")
                    if (notid != null && !notid.equals("", ignoreCase = true)) {
                        notAssetType = bundle.getString("mediaType")
                        if (notAssetType != null && !notAssetType.equals("", ignoreCase = true)) {
                            parseNotification(notid, notAssetType)
                        } else {
                            onNewIntent(intent)
                        }
                    } else {
                        Logger.d("myApplication--->>>$intent")
                        onNewIntent(intent)
                    }
                } else {
                    onNewIntent(intent)
                }
            } else {
                Logger.w("notificationCheck", "nonextra")
                onNewIntent(intent)
            }
        } else {
            Logger.w("notificationCheck", "null")
        }
    }


    private fun loadAnimations() {
        var video: Uri? = null
        val isTablet = resources.getBoolean(R.bool.isTablet)
        video = if (isTablet) {
            Uri.parse("android.resource://$packageName" + "/" + R.raw.splashscreenmobile)
        } else {
            Uri.parse("android.resource://$packageName" + "/" + R.raw.splashscreenmobile)
        }
        binding!!.videoView.setVideoURI(video)
        binding!!.videoView.requestFocus()
        binding!!.videoView.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            callNextForRedirection()
            getDynamicLink()
        })
        binding!!.videoView.setOnErrorListener { mp, what, extra -> false }
        binding!!.videoView.start()
    }


    private fun getDynamicLink() {
        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(
                this
            ) { pendingDynamicLinkData ->
                try {
                    var deepLink: Uri? = null
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.link
                        Log.e(
                            "deepLink",
                            "in2" + pendingDynamicLinkData.link + " " + deepLink!!.query
                        )
                        if (deepLink != null) {
                            val uri = Uri.parse(deepLink.toString())
                            var id: String? = null
                            var mediaType: String? = null
                            try {
                                id = uri.getQueryParameter("id")
                                mediaType = uri.getQueryParameter(MEDIA_TYPE)
                            } catch (e: java.lang.Exception) {
                                Logger.w(e)
                            }
                            try {
                                if (mediaType != null) {
                                    if (!mediaType.equals("", ignoreCase = true) && !id.equals(
                                            "",
                                            ignoreCase = true
                                        )
                                    ) {
                                        KsPreferenceKeys.getInstance().appPrefJumpTo = mediaType
                                        KsPreferenceKeys.getInstance().appPrefBranchIo = true
                                        KsPreferenceKeys.getInstance().appPrefJumpBackId =
                                            id!!.toInt()
                                        deepLinkObject =
                                            AppCommonMethod.createDynamicLinkObject(id, mediaType)
                                        redirections(deepLinkObject)
                                    }
                                } else {
                                    redirectToHome()
                                }
                            } catch (e: java.lang.Exception) {
                                redirectToHome()
                            }
                        }
                    } else {
                        onNewIntent(intent)
                    }
                } catch (e: java.lang.Exception) {
                    redirectToHome()
                    Logger.e("Catch", e.toString())
                }
            }
            .addOnFailureListener(this) { e ->
                redirectToHome()
                Logger.w(e)
                Log.w(TAG, "getDynamicLink:onFailure", e)
            }
    }

    private fun callNextForRedirection() {
        if (viaIntent) {
            val notiVAlues: String = KsPreferenceKeys.getInstance().getNotificationPayload(notificationAssetId.toString() + "")
            try {
                Logger.e("Animation End", "Config Call")
                val jsonObject = JSONObject(notiVAlues)
                redirections(jsonObject)
            } catch (e: Exception) {
                if (notificationObject != null) {
                    redirections(notificationObject)
                } else {
                    redirections(null)
                }
            }
        } else {
            Logger.w("callNext-forRedirection", "else")
        }
    }

    private fun updateAndroidSecurityProvider(callingActivity: Activity) {
        try {
            ProviderInstaller.installIfNeeded(this)
        } catch (e: GooglePlayServicesRepairableException) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.connectionStatusCode, callingActivity, 0)
        } catch (e: GooglePlayServicesNotAvailableException) {
            Logger.e("SecurityException", "Google Play Services not available.")
        }
    }

    protected override fun onResume() {
        super.onResume()
       /* try {
            val isTablet: Boolean = this@ActivitySplash.resources.getBoolean(R.bool.isTablet)
            binding?.buildNumber?.visibility = View.VISIBLE
            if (!isTablet) binding?.buildNumber?.text = resources.getString(R.string.app_name) + "  V " + BuildConfig.VERSION_NAME
        } catch (ignored: Exception) {
        }*/
    }

    protected override fun onStart() {
        super.onStart()
        if (KsPreferenceKeys.getInstance().fromOnboard) {
            KsPreferenceKeys.getInstance().fromOnboard = false
            finish()
        }
    }

    private fun redirectToHome() {
        val updateValue = getForceUpdateValue(null, 2)
        if (!updateValue) {
            Logger.w("branchRedirectors homeRedirection")
            homeRedirection()
        }
    }

    private fun homeRedirection() {
        Logger.w("branchRedirectors $configCall")
        if (configCall == 1) {
            Logger.d("branchRedirectors $configCall")
            configCall = 2
            callConfig(null, null)
        }
    }

    var configRetry = false
    private fun configFailPopup() {
        if (KsPreferenceKeys.getInstance().appLanguage.equals("spanish", ignoreCase = true)) {
            AppCommonMethod.updateLanguage("es", OttApplication.getInstance())
        } else if (KsPreferenceKeys.getInstance().appLanguage.equals("English", ignoreCase = true)) {
            AppCommonMethod.updateLanguage("en", OttApplication.getInstance())
        }
        ConfigFailDialog(this@ActivitySplash).showDialog(object : DialogInterface {
            override fun positiveAction() {
                Handler().postDelayed({
                    binding?.progressBar?.visibility = View.VISIBLE
                    configRetry = true
                    callConfig(null, null)
                }, 200)
            }

            override fun negativeAction() {
                binding?.progressBar?.visibility = View.GONE
                finish()
            }
        })
    }

    private fun redirections(jsonObject: JSONObject?) {
        try {
            callConfig(jsonObject, null)
        } catch (e: Exception) {
        }
    }

    private fun connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true)
        } else {
            connectionValidation(false)
        }
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            binding?.connection?.noConnectionLayout?.visibility = View.GONE
            loadAnimations()
            callNextForRedirection()
            dynamicLink
        } else {
            binding?.connection?.noConnectionLayout?.visibility = View.VISIBLE
            binding?.connection?.noConnectionLayout?.bringToFront()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        if (intent.extras != null) {
            try {
                viaIntent = true
                notid = intent.getStringExtra("id")
                notAssetType = intent.getStringExtra("mediaType")
                if (notid == null && notAssetType == null) {
                    notid = intent.getStringExtra("assetId")
                    notAssetType = intent.getStringExtra("assetType")
                } else {
                    callConfig(null, null)
                }
                parseNotification(notid, notAssetType)
            } catch (e: Exception) {
                Logger.e(e)
            }
        } else {
            homeRedirection()
        }
    }

    private var notificationObject: JSONObject? = null
    private fun parseNotification(notid: String?, assetType: String?) {
        if (notid != null && !assetType.equals("", ignoreCase = true)) {
            notificationAssetId = notid.toInt()
            if (notificationAssetId > 0 && assetType != null && !assetType.equals("", ignoreCase = true)) {
                Logger.w("FCM_Payload_final --", notificationAssetId.toString() + "")
                notificationObject = AppCommonMethod.createNotificationObject(notid, assetType)
                viaIntent = true
            }
        } else {
            Logger.w("Parse_Notification", "else")
        }
    }

    override fun onFinishDialog() {
        connectionObserver()
    }

    protected override fun onPause() {
        super.onPause()
    }

    var forceUpdate = false
    private fun getForceUpdateValue(jsonObject: JSONObject?, type: Int): Boolean {
        Logger.d("branchRedirectors er forceupdate")
        if (KsPreferenceKeys.getInstance().appLanguage.equals("spanish", ignoreCase = true) || KsPreferenceKeys.getInstance().appLanguage.equals("हिंदी", ignoreCase = true)) {
            AppCommonMethod.updateLanguage("es", OttApplication.getInstance())
        } else if (KsPreferenceKeys.getInstance().appLanguage.equals("English", ignoreCase = true)) {
            AppCommonMethod.updateLanguage("en", OttApplication.getInstance())
        }
        forceUpdateHandler = ForceUpdateHandler(this@ActivitySplash, configBean)
        forceUpdateHandler!!.checkCurrentVersion(object : VersionValidator {
            override fun version(status: Boolean, currentVersion: Int, playstoreVersion: Int, updateType: String) {
                if (status) {
                    // For Existing user who has updated the app
                    MoEAnalyticsHelper.setAppStatus(application, AppStatus.UPDATE)
                    forceUpdate = true
                    forceUpdateHandler!!.typeHandle(updateType, VersionUpdateCallBack { selection: Boolean ->
                        if (updateType == ForceUpdateHandler.RECOMMENDED) {
                            if (!selection) {
                                binding?.progressBar?.visibility = View.VISIBLE
                                forceUpdateHandler!!.hideDialog()
                                clapanimation = 1
                                callConfig(null, updateType)
                            }
                        }
                    })
                } else {
                    forceUpdate = false
                }
            }
        })
        return forceUpdate
    }

    override fun onBillingInitialized() {
        Log.w("identifiers", "in onBillingInitialized")
        restorePurchase()
    }

    private fun restorePurchase() {
        if (KsPreferenceKeys.getInstance().appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            if (bp != null) {
                if (bp!!.isReady) {
                    bp!!.queryPurchases(object : RestoreSubscriptionCallback {
                        override fun subscriptionStatus(status: Boolean, message: String) {}
                        override fun subscriptionHistory(status: Boolean, purchases: List<Purchase>) {
                            if (status) {
                                GetPlansLayer.getInstance().getEntitlementStatus(
                                    KsPreferenceKeys.getInstance(),
                                    KsPreferenceKeys.getInstance().appPrefAccessToken
                                ) { entitlementStatus: Boolean, apiStatus: Boolean, responseCode: Int ->
                                    if (apiStatus) {
                                        if (entitlementStatus) {
                                        } else {
                                            if (responseCode == 100) {
                                                bp!!.checkPurchase(purchases)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }, 1)
                }
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {}
    override fun onListOfSKUFetched(purchases: List<SkuDetails>?) {}
    override fun onBillingError(error: BillingResult?) {}

    companion object {
        private const val MEDIA_TYPE = "contentType"
    }

}