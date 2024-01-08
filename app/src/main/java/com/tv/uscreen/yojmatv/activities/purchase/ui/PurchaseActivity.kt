package com.tv.uscreen.yojmatv.activities.purchase.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.activities.purchase.adapter.PurchaseAdapter
import com.tv.uscreen.yojmatv.activities.purchase.call_back.EntitlementStatus
import com.tv.uscreen.yojmatv.activities.purchase.in_app_billing.BillingProcessor
import com.tv.uscreen.yojmatv.activities.purchase.in_app_billing.InAppProcessListener
import com.tv.uscreen.yojmatv.activities.purchase.in_app_billing.PurchaseType
import com.tv.uscreen.yojmatv.activities.purchase.plans_layer.GetPlansLayer
import com.tv.uscreen.yojmatv.activities.purchase.purchase_model.PurchaseModel
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.EnterOTPActivity
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.membershipAndPlan.ResponseMembershipAndPlan
import com.tv.uscreen.yojmatv.databinding.ActivityPurchaseBinding

import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper


class PurchaseActivity : BaseBindingActivity<ActivityPurchaseBinding>(), CommonDialogFragment.EditDialogListener,
    InAppProcessListener, PurchaseAdapter.OnPurchaseItemClick{
    private val stringsHelper by lazy { StringsHelper }
    lateinit var bp : BillingProcessor
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityPurchaseBinding {
        return ActivityPurchaseBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBilling()
        //ThemeHandler.getInstance().applySelectPlan(this,binding)
        setClicks()
    }

    private fun initBilling() {
        binding?.progressBar?.visibility=View.VISIBLE
        bp = BillingProcessor(this@PurchaseActivity, this)
        bp.initializeBillingProcessor()
        val serviceIntent = Intent("com.android.vending.billing.InAppBillingService.BIND")
        serviceIntent.setPackage("com.android.vending")
    }

    private fun setClicks() {
        binding?.btnBuy?.setOnClickListener {
            clickedPlan?.let {
                bp.purchase(
                    this@PurchaseActivity,
                    clickedPlan!!.identifier,
                    "DEVELOPER PAYLOAD",
                    PurchaseType.SUBSCRIPTION.name
                )
            }?: run{
                Log.w("cardSelectiom","no card selected")
            }

        }
        binding?.rvPurchase?.layoutManager  = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm: FragmentManager = supportFragmentManager
        val commonDialogFragment = CommonDialogFragment.newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }

    override fun onActionBtnClicked() {
        val i = Intent(this@PurchaseActivity, EnterOTPActivity::class.java)
        startActivity(i)
    }
    override fun onDestroy() {
        super.onDestroy()
        // binding!!.planRecycleView.adapter =  AdapterPlan(imageArray,itemClickListener)
    }

    override fun onBillingInitialized() {
        callGetPlansApi()
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        commonDialog(
            stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.popup_payment_success.toString(),
                getString(R.string.popup_payment_success)
            ),
            stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.popup_payment_description.toString(),
                getString(R.string.popup_payment_description)
            ),
            stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                getString(R.string.popup_continue)
            )
        )
    }

    private lateinit var purchaseFinalList: List<PurchaseModel>
    var adapter : PurchaseAdapter? = null
    override fun onListOfSKUFetched(purchases: MutableList<SkuDetails>?) {
        purchaseFinalList = java.util.ArrayList()
        assert(purchases != null)
        Log.w("onListOfSKUFetched", purchases!!.size.toString() + "")
        runOnUiThread {
            binding?.progressBar?.visibility=View.GONE
            purchaseFinalList= AppCommonMethod.createPurchaseList(purchaseModel, purchases, purchaseFinalList as java.util.ArrayList<PurchaseModel?>,bp) as List<PurchaseModel>
            adapter= PurchaseAdapter(purchaseFinalList,this@PurchaseActivity,this@PurchaseActivity)
            binding?.rvPurchase?.adapter =  adapter
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
            override fun entitlementStatus(entitlementStatus: Boolean, apiStatus: Boolean,offerStatus:String,responseCode :Int) {

            }
            override fun getPlans(plans: ResponseMembershipAndPlan?, apiStatus: Boolean) {
                purchaseModel = AppCommonMethod.fetchRecSubscriptionModel("",plans!!, subSkuList as ArrayList<String>, productSkuList as ArrayList<String>)
                if (purchaseModel != null) {
                    bp.getAllSkuDetails(
                        purchaseModel!![0].subscriptionList,
                        purchaseModel!![0].productList
                    )
                }
            }
        })
    }

    var clickedPlan: PurchaseModel? = null
    @SuppressLint("NotifyDataSetChanged")
    override fun onPurchaseCardClick(click: Boolean, planName: PurchaseModel) {
        runOnUiThread {
            if (adapter != null) {
                clickedPlan = planName
                AppCommonMethod.activeBtn(binding,getColor(R.color.buy_now_pay_now_btn_text_color))
                adapter?.notifyDataSetChanged()
            }
        }
    }
}