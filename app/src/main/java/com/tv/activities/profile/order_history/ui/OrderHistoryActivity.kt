package com.tv.activities.profile.order_history.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import app.doxzilla.activities.order_history.model.OrderHistoryModel
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties
import com.tv.R
import com.tv.activities.profile.order_history.adapter.OrderHistoryAdapter
import com.tv.activities.purchase.purchase_model.PurchaseModel
import com.tv.activities.purchase.ui.VodOfferType
import com.tv.activities.watchList.viewModel.WatchListViewModel
import com.tv.baseModels.BaseBindingActivity
import com.tv.databinding.OrderHistoryActivityBinding
import com.tv.utils.colorsJson.converter.ColorsHelper
import com.tv.utils.constants.AppConstants
import com.tv.utils.helpers.NetworkConnectivity
import com.tv.utils.helpers.RecyclerAnimator
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.utils.stringsJson.converter.StringsHelper
import com.tv.utils.stringsJson.converter.StringsHelper.stringParse

class OrderHistoryActivity : BaseBindingActivity<OrderHistoryActivityBinding?>() {
    private var token = ""
    private var viewModel: WatchListViewModel? = null
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var pastVisibleItems = 0
    private var mIsLoading = false
    private var counter = 0
    private var mScrollY = 0
    private var purchaseFinalList: MutableList<PurchaseModel> = ArrayList()
    var adapter: OrderHistoryAdapter? = null
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): OrderHistoryActivityBinding {
        return OrderHistoryActivityBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ThemeHandler.getInstance().applyOrderHistory(this, getBinding());
        parseColor()
        connectionValidation(NetworkConnectivity.isOnline(this))
        val properties = Properties()
        properties.addAttribute(AppConstants.ORDER_HISTORY, AppConstants.ORDER_HISTORY)
        MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties)
    }

    private fun parseColor() {
        binding?.colorsData = colorsHelper
        binding?.stringData = stringsHelper
        binding?.toolbar?.colorsData = colorsHelper
        binding?.toolbar?.stringData = stringsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            binding!!.noConnectionLayout.visibility = View.GONE
            binding!!.orderHistoryRecyclerView.visibility = View.VISIBLE
            binding!!.progressBar.visibility = View.VISIBLE
            initialization()
            RecyclerAnimator(this@OrderHistoryActivity).animate(binding!!.orderHistoryRecyclerView)
            (binding!!.orderHistoryRecyclerView.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false
            hitOrderHistoryAPI()
            setUpRecyclerView()
        } else {
            binding!!.noConnectionLayout.visibility = View.VISIBLE
            binding!!.orderHistoryRecyclerView.visibility = View.GONE
            binding!!.connection.retryTxt.setOnClickListener { connectionValidation(NetworkConnectivity.isOnline(this@OrderHistoryActivity)) }
        }
        setUi()
    }

    private fun setUi() {
        binding?.toolbar?.titleMid?.visibility = View.VISIBLE
        binding?.toolbar?.logoMain2?.visibility = View.GONE
        binding!!.toolbar.backLayout.setOnClickListener { onBackPressed() }
        binding!!.toolbar.titleSkip.visibility = View.GONE
        binding!!.toolbar.llSearchIcon.visibility = View.GONE
        binding!!.toolbar.titleMid.text = stringParse(
            stringsHelper.instance()?.data?.config?.order_history_title.toString(),
            getString(R.string.order_history_title)
        )
    }

    private fun initialization() {
        val preference = KsPreferenceKeys.getInstance()
        token = preference.appPrefAccessToken
        viewModel = ViewModelProvider(this@OrderHistoryActivity)[WatchListViewModel::class.java]
    }

    private fun setUpRecyclerView() {
        binding!!.orderHistoryRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (dy > 0) {
                    assert(layoutManager != null)
                    visibleItemCount = layoutManager!!.childCount
                    totalItemCount = layoutManager.itemCount
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if (mIsLoading) {
                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                            val adapterSize = binding!!.orderHistoryRecyclerView.adapter?.itemCount
                            if (adapterSize != null) {
                                if (adapterSize > 8) {
                                    mIsLoading = false
                                    counter++
                                    mScrollY += dy
                                    hitOrderHistoryAPI()
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    private fun setRail(orderHistoryModel: OrderHistoryModel?) {
        binding!!.progressBar.visibility = View.GONE
        if (orderHistoryModel != null && orderHistoryModel.data.items.isNotEmpty()) {
            mIsLoading = true
            for (j in orderHistoryModel.data.items.indices) {
                val (_, offerIdentifier, offerTitle, orderId, _, orderStatus, orderAmount, _, orderCurrency, _, subscriptionOfferType, _, createdDate, _, _, _, paymentProvider, _, entitlementState, _, _, nextChargeDate, currentExpiry, onTrial, offerDetails) = orderHistoryModel.data.items[j]
                val purchaseModel = PurchaseModel()
                purchaseModel.title = "" + offerTitle
                purchaseModel.price = "" + orderAmount
                purchaseModel.trialType = ""
                purchaseModel.trialDuration = 0
                if (subscriptionOfferType != null && !subscriptionOfferType.equals("", ignoreCase = true)) {
                    purchaseModel.subscriptionType = subscriptionOfferType
                }
                purchaseModel.createdDate = createdDate
                purchaseModel.isOnTrial = onTrial
                purchaseModel.isSelected = entitlementState != null && entitlementState.equals("ACTIVE", ignoreCase = true)
                purchaseModel.entitlementState = entitlementState != null && entitlementState.equals("ACTIVE", ignoreCase = true)
                purchaseModel.purchaseOptions = VodOfferType.RECURRING_SUBSCRIPTION.name
                purchaseModel.offerPeriod = VodOfferType.WEEKLY.name
                if (offerTitle != null) {
                    purchaseModel.title = offerTitle
                }
                if (offerIdentifier != null) {
                    purchaseModel.identifier = offerIdentifier
                }
                purchaseModel.customData = null
                purchaseModel.offerPeriod = ""
                purchaseModel.expiryDate = 0L
                if (paymentProvider != null) {
                    purchaseModel.paymentProvider = paymentProvider
                }
                if (paymentProvider != null) {
                    purchaseModel.paymentProvider = paymentProvider
                }
                if (orderId != null) {
                    purchaseModel.transactionID = orderId
                }
                if (orderStatus != null) {
                    purchaseModel.orderStatus = orderStatus
                }
                if (orderCurrency != null) {
                    purchaseModel.currency = orderCurrency
                }
                if (currentExpiry != null && currentExpiry > 0) {
                    purchaseModel.currentExpiryDate = currentExpiry
                }
                if (nextChargeDate != null && nextChargeDate > 0) {
                    purchaseModel.nextChargeDate = nextChargeDate
                }
                if (offerDetails != null) {
                    if (offerDetails.offerPeriod != null) {
                        purchaseModel.offerPeriod = offerDetails.offerPeriod
                    }
                    if (offerDetails.trialPeriod != null) {
                        if (offerDetails.trialPeriod.trialType != null) {
                            purchaseModel.trialType = offerDetails.trialPeriod.trialType
                        }
                        purchaseModel.trialDuration = offerDetails.trialPeriod.trialDuration
                    }
                }
                purchaseFinalList.add(purchaseModel)
            }
            if (adapter == null) {
                adapter = OrderHistoryAdapter(purchaseFinalList, currentLanguage)
                binding!!.orderHistoryRecyclerView.adapter = adapter
            } else {
                adapter!!.notifyDataSetChanged()
            }
            binding!!.orderHistoryRecyclerView.scrollToPosition(mScrollY)
        }
    }

    private fun hitOrderHistoryAPI() {
        viewModel!!.getOrderHistory(token, counter.toString(), AppConstants.PAGE_SIZE.toString()).observe(this) { orderHistoryModel: OrderHistoryModel? ->
            if (orderHistoryModel != null) {
                runOnUiThread { binding!!.noData.visibility = View.GONE }
                setRail(orderHistoryModel)
            } else {
                runOnUiThread {
                    binding!!.progressBar.visibility = View.GONE
                    binding!!.noData.visibility = View.VISIBLE
                }
            }
        }
    }
}