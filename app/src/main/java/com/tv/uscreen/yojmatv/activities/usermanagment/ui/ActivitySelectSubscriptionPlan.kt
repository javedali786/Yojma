package com.tv.uscreen.yojmatv.activities.usermanagment.ui


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.google.gson.Gson
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.activities.homeactivity.ui.HomeActivity
import com.tv.uscreen.yojmatv.activities.profile.adapter.AdapterManageSubscription
import com.tv.uscreen.yojmatv.activities.purchase.call_back.EntitlementStatus
import com.tv.uscreen.yojmatv.activities.purchase.in_app_billing.BillingProcessor
import com.tv.uscreen.yojmatv.activities.purchase.in_app_billing.InAppProcessListener
import com.tv.uscreen.yojmatv.activities.purchase.in_app_billing.PurchaseType
import com.tv.uscreen.yojmatv.activities.purchase.plans_layer.GetPlansLayer
import com.tv.uscreen.yojmatv.activities.purchase.purchase_model.PurchaseModel
import com.tv.uscreen.yojmatv.activities.purchase.purchase_model.PurchaseResponseModel
import com.tv.uscreen.yojmatv.activities.usermanagment.adapter.AdapterPlan
import com.tv.uscreen.yojmatv.activities.usermanagment.model.PlanSubscriptionModel
import com.tv.uscreen.yojmatv.activities.usermanagment.payment_layer.PaymentCallBack
import com.tv.uscreen.yojmatv.activities.usermanagment.payment_layer.PaymentCallsLayer
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.membershipAndPlan.ResponseMembershipAndPlan
import com.tv.uscreen.yojmatv.databinding.ActivitySelectSubscriptionPlanBinding
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.CommonRestoreDialog
import com.tv.uscreen.yojmatv.jwplayer.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper


class ActivitySelectSubscriptionPlan : BaseBindingActivity<ActivitySelectSubscriptionPlanBinding?>(),
    CommonDialogFragment.EditDialogListener, CommonRestoreDialog.EditDialogListener,
    InAppProcessListener, AdapterPlan.OnPurchaseItemClick , AdapterManageSubscription.OnPurchaseItemClick{
    private val stringsHelper by lazy { StringsHelper }
    private val imageArray: ArrayList<PlanSubscriptionModel> = ArrayList()
    lateinit var bp : BillingProcessor
    private var from: String? = ""
    private var signUp: String? = ""
    private var isUserVerified: String? = ""
    private var paymentProvider : String? = ""
    private var subscriptionTittle : String? = ""
    private var preference: KsPreferenceKeys? = null
    private var isPurchased = false
    private val colorsHelper by lazy { ColorsHelper }
    private val stringHelper by lazy { StringsHelper }

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivitySelectSubscriptionPlanBinding {
        return ActivitySelectSubscriptionPlanBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding?.stringData = stringHelper
        binding?.colorsData = colorsHelper
        binding?.toolbar?.colorsData = colorsHelper
        binding?.toolbar?.stringData = stringHelper
        val intent = intent
        from = intent.getStringExtra("intentFrom")
        initBilling()
        setClicks()

        preference = KsPreferenceKeys.getInstance()
        isUserVerified = preference?.isVerified
        binding?.toolbar!!.logoMain2.visibility=View.VISIBLE
        binding?.toolbar!!.llSearchIcon.visibility=View.GONE
        if(from!!.equals("settings",ignoreCase = true)) {
            binding?.toolbar!!.titleMid.visibility =View.VISIBLE
            binding?.toolbar!!.titleMid.text=resources.getString(R.string.manage_account)
            binding?.toolbar?.logoMain2?.visibility = View.GONE
            binding?.mainPaymentLayout!!.visibility = View.GONE
            binding?.bottomLay!!.visibility = View.GONE
            binding?.mainManageSubscriptionLayout!!.visibility = View.VISIBLE
            binding?.toolbar!!.titleSkip.visibility=View.GONE
        }else if(from!!.equals("Login",ignoreCase = true)){
            binding?.mainPaymentLayout!!.visibility = View.VISIBLE
            binding?.bottomLay!!.visibility = View.VISIBLE
            binding?.mainManageSubscriptionLayout!!.visibility = View.GONE
            binding?.toolbar!!.backLayout.visibility = View.GONE

        } else if (from!!.equals("OTP",ignoreCase = true) ){
            binding?.mainPaymentLayout!!.visibility = View.VISIBLE
            binding?.bottomLay!!.visibility = View.VISIBLE
            binding?.mainManageSubscriptionLayout!!.visibility = View.GONE
            binding?.toolbar!!.backLayout.visibility = View.GONE
            binding?.toolbar!!.titleSkip.visibility=View.VISIBLE
        }
        else{
            binding?.toolbar!!.titleSkip.visibility=View.GONE
            binding?.mainPaymentLayout!!.visibility = View.VISIBLE
            binding?.bottomLay!!.visibility = View.VISIBLE
            binding?.mainManageSubscriptionLayout!!.visibility = View.GONE
            binding?.toolbar!!.backLayout.visibility = View.VISIBLE
        }
    }

    private fun initBilling() {
        binding?.progressBar?.visibility=View.VISIBLE
        bp = BillingProcessor(this@ActivitySelectSubscriptionPlan, this)
        bp.initializeBillingProcessor()
        val serviceIntent = Intent("com.android.vending.billing.InAppBillingService.BIND")
        serviceIntent.setPackage("com.android.vending")
    }

    var orderID: String? = null
    private fun setClicks() {
        /*binding?.btnBuy?.setOnClickListener {
        binding?.toolbar?.logo?.visibility = View.VISIBLE
        binding?.btnBuy?.setOnClickListener {
            ActivityLauncher.getInstance().goToEnterOTP(this@ActivitySelectSubscriptionPlan, EnterOTPActivity::class.java)
        }*/

        binding?.toolbar?.titleSkip?.setOnClickListener{
            if (from!!.equals("SignUp",ignoreCase = true)) {
                ActivityLauncher.getInstance().homeActivity(this,HomeActivity::class.java)
            }else{
                ActivityLauncher.getInstance().homeActivity(this,HomeActivity::class.java)
            }
        }

        binding?.restoreLay?.setOnClickListener {
            restoreSubscription()
        }

        binding?.toolbar?.backLayout?.setOnClickListener {
            checkBackCondition()
        }
        binding?.toolbar?.backLayout?.setOnClickListener { checkBackCondition() }
        binding?.btnBuy?.setOnClickListener {
            clickedPlan?.let {
                Log.w("itemClick", clickedPlan!!.identifier.toString() + "")
                val token = KsPreferenceKeys.getInstance().appPrefAccessToken
                if (token != null && !token.equals("", ignoreCase = true)) {
                    binding?.progressBar?.visibility=View.VISIBLE
                    PaymentCallsLayer.getInstance()
                        .createOrder(token, clickedPlan, object : PaymentCallBack {
                            override
                            fun createOrderResponse(
                                response: PurchaseResponseModel,
                                status: Boolean
                            ) {
                                if (status) {
                                    orderID = response.data.orderId
                                    KsPreferenceKeys.getInstance().paymentorderid = orderID
                                    Log.w("orderIdOf", orderID.toString())
                                    callInitiateOrder(response)
                                } else {
                                    commonDialog(
                                        stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                            getString(R.string.popup_error)
                                        ),
                                        stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_payment_error.toString(),
                                            getString(R.string.popup_payment_error)
                                        )  + " " + SUPPORT,
                                         stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_ok.toString(),
                                            getString(R.string.popup_ok))
                                    )
                                }
                            }
                        })
                }
            }?: run{
             Log.w("cardSelectiom","no card selected")
        }

        }
        binding?.planRecycleView?.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


    }

    var paymentId: String? = null
    private fun callInitiateOrder(response: PurchaseResponseModel) {
        val token = KsPreferenceKeys.getInstance().appPrefAccessToken
        PaymentCallsLayer.getInstance()
            .callInitiatePayment(token, response.data.orderId, object : PaymentCallBack {
                override fun initiateOrderResponse(
                    response: PurchaseResponseModel,
                    status: Boolean
                ) {
                    if (status) {
                        paymentId = response.data.paymentId.toString()
                        buySubscription(paymentId)
                        Log.w("orderIdOf", paymentId.toString())
                    } else {
                        commonDialog(
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                getString(R.string.popup_error)
                            ),
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_payment_error.toString(),
                                getString(R.string.popup_payment_error)
                            ) + " " + SUPPORT,
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_ok.toString(),
                                getString(R.string.popup_ok))
                        )
                    }
                }
            })
    }

    private fun buySubscription(paymentId: String?) {
        if (paymentId != null && !paymentId.equals("", ignoreCase = true)) {
            binding!!.progressBar.visibility = View.GONE
            bp.purchase(
                this@ActivitySelectSubscriptionPlan,
                clickedPlan!!.customIdentifier,
                "DEVELOPER PAYLOAD",
                PurchaseType.SUBSCRIPTION.name
            )
        }
    }


    private fun commonDialog(title: String, description: String, actionBtn: String) {
        try {
            var fm: FragmentManager = this.supportFragmentManager
            var commonDialogFragment = CommonDialogFragment.newInstance(title, description, actionBtn)
            commonDialogFragment.setEditDialogCallBack(this)
            commonDialogFragment.show(fm, AppConstants.MESSAGE)
        }catch (e :Exception){
            e.printStackTrace()
        }

    }

    override fun onActionBtnClicked() {
        if (isPurchased) {
            if (from!!.equals("SignUp",ignoreCase = true)) {
                ActivityLauncher.getInstance().goToEnterOTP(this,
                    EnterOTPActivity::class.java,from)
            }else{
                if (from!!.equals("Login", ignoreCase = true)) {
                    if (isUserVerified.equals("true", ignoreCase = true)) {
                        ActivityLauncher.getInstance().homeScreen(this, HomeActivity::class.java)
                    }else{
                        ActivityLauncher.getInstance().goToEnterOTP(this,
                            EnterOTPActivity::class.java,from)
                    }
                }else {
                    ActivityLauncher.getInstance().homeScreen(this, HomeActivity::class.java)
                }
            }
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
       // binding!!.planRecycleView.adapter =  AdapterPlan(imageArray,itemClickListener)
    }

    override fun onBillingInitialized() {
        callGetPlansApi()
    }

    private val SUPPORT = "info@YojmaTv.net"
    private val BILLING_RESULT = "BillingResult"
    private val PURCHASED_SKU = "purchasedSKU"
    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        binding?.progressBar?.visibility=View.GONE
        val gson = Gson()
        val json = gson.toJson(billingResult)
        Log.w(BILLING_RESULT, json)
        Log.w(
            BILLING_RESULT,
            "new line"
        )
        val json2 = gson.toJson(purchases)
        Log.w(BILLING_RESULT, json2)

        binding!!.progressBar.visibility = View.VISIBLE
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            if (purchases[0].purchaseToken != null) {
                processPurchase(purchases)
            } else {
                updatePayment("FAILED", "inapp:com.enveu.demo:android.test.purchased", paymentId.toString())
            }
        }
    }

    var purchasedSKU = ""
    private fun handlePurchase(purchase: Purchase) {
        try {
            Log.w(
                PURCHASED_SKU,
                purchase.skus[0]
            )
            purchasedSKU = Base64.encodeToString(purchase.skus[0].toByteArray(), Base64.NO_WRAP)
            Log.w(
                PURCHASED_SKU,
                purchasedSKU
            )
            Log.w(
                PURCHASED_SKU,
                purchase.purchaseToken
            )
        } catch (e: java.lang.Exception) {
            Logger.e(e)
        }
        try {
            updatePayment("PAYMENT_DONE", purchase.purchaseToken, paymentId.toString())
        } catch (e: java.lang.Exception) {
            Logger.e(e)
        }
    }

    private fun updatePayment(paymentStatus: String, purchaseToken: String, paymentId: String) {
        Log.w("itemClick", clickedPlan!!.identifier.toString() + "")
        val token = KsPreferenceKeys.getInstance().appPrefAccessToken
        if (token != null && !token.equals("", ignoreCase = true)) {
            PaymentCallsLayer.getInstance().updatePurchase(
                "",
                paymentStatus,
                token,
                purchaseToken,
                paymentId,
                orderID,
                clickedPlan,
                purchasedSKU,
                object : PaymentCallBack {
                    override fun updateOrderResponse(
                        response: PurchaseResponseModel,
                        status: Boolean
                    ) {
                        binding!!.progressBar.visibility = View.GONE
                        if (status) {
                            if (response.data.orderStatus != null) {
                                if (response.data.orderStatus.equals("COMPLETED",ignoreCase = true)) {
                                    AppCommonMethod.isPurchase = true
                                    isPurchased = true
                                    commonDialog(
                                        "",
                                        stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_payment_success.toString(),
                                            getString(R.string.popup_payment_success))
                                                + " ", stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_ok.toString(),
                                            getString(R.string.popup_ok))
                                    )
                                } else {
                                    dismissLoading(binding!!.progressBar)
                                    commonDialog(
                                        stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                            getString(R.string.popup_error)
                                        ),
                                        stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_payment_error.toString(),
                                            getString(R.string.popup_payment_error)
                                        )
                                                + " " + SUPPORT,
                                         stringsHelper.stringParse(
                                            stringsHelper.instance()?.data?.config?.popup_ok.toString(),
                                            getString(R.string.popup_ok))
                                    )
                                }
                            }
                        } else {
                            commonDialog(
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ),
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_payment_error.toString(),
                                    getString(R.string.popup_payment_error)
                                ) + " " + SUPPORT,
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_ok.toString(),
                                    getString(R.string.popup_ok))
                            )
                        }
                    }
                })
        }
    }


    private fun processPurchase(purchases: List<Purchase>) {
        try {
            binding?.progressBar?.visibility=View.VISIBLE
            for (purchase in purchases) {
                Log.w(BILLING_RESULT,
                    "new line"
                )
                val gson = Gson()
                val json = gson.toJson(purchase)
                Log.w(BILLING_RESULT,
                    json
                )
                if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                    handlePurchase(purchase)
                } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
                }
            }
        } catch (ignored: Exception) {
        }
    }


    private lateinit var purchaseFinalList: List<PurchaseModel>
    var adapter : AdapterPlan? = null
    var manageAdapter : AdapterManageSubscription? = null
    override fun onListOfSKUFetched(purchases: MutableList<SkuDetails>?) {
        purchaseFinalList = java.util.ArrayList()
        assert(purchases != null)
        Log.w("onListOfSKUFetched", purchases!!.size.toString() + "")
        runOnUiThread {
            binding?.progressBar?.visibility=View.GONE
            if(from!!.equals("settings",ignoreCase = true)){
                purchaseFinalList= AppCommonMethod.createManagePurchaseList(purchaseModel, purchases,
                    purchaseFinalList as java.util.ArrayList<PurchaseModel?>,bp
                ) as List<PurchaseModel>
            }else{
                purchaseFinalList= AppCommonMethod.createPurchaseList(purchaseModel, purchases,
                    purchaseFinalList as java.util.ArrayList<PurchaseModel?>,bp
                ) as List<PurchaseModel>
            }

            if(from!!.equals("settings",ignoreCase = true)){
                binding?.mainPaymentLayout!!.visibility=View.GONE;
                binding?.bottomLay!!.visibility = View.GONE
                binding?.mainManageSubscriptionLayout!!.visibility=View.VISIBLE;
                manageAdapter= AdapterManageSubscription(purchaseFinalList,this@ActivitySelectSubscriptionPlan,this@ActivitySelectSubscriptionPlan)
                binding?.subscriptionRecycle?.adapter =  manageAdapter
            }else{
                binding?.mainPaymentLayout!!.visibility=View.VISIBLE;
                binding?.bottomLay!!.visibility = View.VISIBLE
                binding?.mainManageSubscriptionLayout!!.visibility=View.GONE;
                adapter= AdapterPlan(purchaseFinalList,this@ActivitySelectSubscriptionPlan,this@ActivitySelectSubscriptionPlan)
                binding?.planRecycleView?.adapter =  adapter
            }
        }
    }

    override fun onBillingError(error: BillingResult?) {

    }

    var subSkuList: MutableList<String>? = null
    var productSkuList: MutableList<String>? = null
    var purchaseModel: List<PurchaseModel>? = null
    private fun callGetPlansApi() {
        subSkuList = java.util.ArrayList<String>()
        productSkuList = java.util.ArrayList<String>()
        val token: String = KsPreferenceKeys.getInstance().appPrefAccessToken
        GetPlansLayer.getInstance().getPlansDetail(token, object : EntitlementStatus {
            override fun entitlementStatus(entitlementStatus: Boolean, apiStatus: Boolean,responseCode:Int) {

            }
            override fun getPlans(plans: ResponseMembershipAndPlan?, apiStatus: Boolean) {
                purchaseModel = AppCommonMethod.fetchRecSubscriptionModel(plans!!, subSkuList as java.util.ArrayList<String>, productSkuList as java.util.ArrayList<String>)
                if (purchaseModel != null && purchaseModel!!.isNotEmpty()) {
                    if(from!!.equals("settings",ignoreCase = true)) {
                        callManageSubscriptionAdapter(purchaseModel!!,plans);
                    }else{
                        bp.getAllSkuDetails(
                            purchaseModel!![0].subscriptionList,
                            purchaseModel!![0].productList
                        )
                    }

                }else{
                    commonDialog(
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_error.toString(),
                            getString(R.string.popup_error)
                        ),
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_payment_error.toString(),
                            getString(R.string.popup_payment_error)
                        ) + " " + SUPPORT,
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_ok.toString(),
                            getString(R.string.popup_ok))
                    )
                }
            }
        })
    }

    private fun callManageSubscriptionAdapter(
        purchaseModel: List<PurchaseModel>,
        plans: ResponseMembershipAndPlan?
    ) {
        purchaseFinalList = java.util.ArrayList()
        binding?.progressBar?.visibility=View.GONE
        runOnUiThread{
            purchaseFinalList= AppCommonMethod.createManagePurchaseListNew(
                purchaseModel,plans,
                purchaseFinalList as java.util.ArrayList<PurchaseModel?>
            ) as List<PurchaseModel>

            binding?.mainPaymentLayout!!.visibility=View.GONE;
            binding?.bottomLay!!.visibility = View.GONE
            binding?.mainManageSubscriptionLayout!!.visibility=View.VISIBLE;
            manageAdapter=
                AdapterManageSubscription(purchaseFinalList,this@ActivitySelectSubscriptionPlan,this@ActivitySelectSubscriptionPlan)
            binding?.subscriptionRecycle?.adapter =  manageAdapter
        }

    }

    var clickedPlan: PurchaseModel? = null
    @SuppressLint("NotifyDataSetChanged")
    override fun onPurchaseCardClick(click: Boolean, planName: PurchaseModel) {
        runOnUiThread {
            if (adapter != null) {
                clickedPlan = planName
                subscriptionTittle = clickedPlan!!.title
                binding?.descriptionLongText?.text = planName.description
                AppCommonMethod.activeBtn(binding,getColor(R.color.main_btn_txt_color));
                adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun setDescription(description: String?, planName: PurchaseModel) {
        clickedPlan = planName
        binding?.descriptionLongText?.text = description
    }

    override fun onBackPressed() {
        if (from!!.equals("Login", ignoreCase = true) || from!!.equals("Signup", ignoreCase = true)) {

        }else{
            super.onBackPressed()
        }

    }

    private fun checkBackCondition() {
        finish()
    }

    private fun restoreSubscription() {
        binding!!.progressBar.visibility = View.VISIBLE
        if (bp != null) {
            if (bp.isReady) {
                bp.queryPurchases { status, message ->
                    binding!!.progressBar.visibility = View.GONE
                    if (status) {
                        commonRestoreDialog("", message, 1)
                    } else {
                        if (message.contains(resources.getString(R.string.we_could_not))) {
                            commonRestoreDialog(resources.getString(R.string.error), message, 2)
                        } else {
                            commonRestoreDialog(resources.getString(R.string.error), message, 3)
                        }
                    }
                }
            }
        }
    }

    private fun commonRestoreDialog(error: String, message: String, type: Int) {
        val fm = supportFragmentManager
        val commonDialogFragment = CommonRestoreDialog.newInstance(error, message, type)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }

    override fun onRestoreActionBtnClicked(type: Int) {
            if(type==1){
                ActivityLauncher.getInstance().homeScreen(this@ActivitySelectSubscriptionPlan, HomeActivity::class.java)
            }
    }

}