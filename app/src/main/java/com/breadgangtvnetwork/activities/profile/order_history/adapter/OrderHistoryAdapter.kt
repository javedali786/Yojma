package com.breadgangtvnetwork.activities.profile.order_history.adapter

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.activities.purchase.purchase_model.PurchaseModel
import com.breadgangtvnetwork.activities.purchase.ui.VodOfferType
import com.breadgangtvnetwork.databinding.OrderHistoryItemBinding
import com.breadgangtvnetwork.utils.colorsJson.converter.AppColors
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper
import com.breadgangtvnetwork.utils.commonMethods.AppCommonMethod
import com.breadgangtvnetwork.utils.constants.AppConstants
import com.breadgangtvnetwork.utils.stringsJson.converter.StringsHelper

class OrderHistoryAdapter(private val list: List<PurchaseModel>, private val currentLanguage: String) : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {
    private var context: Activity? = null
    private val colorsHelper by lazy { ColorsHelper }
    private var paymentMode: String? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        context = viewGroup.context as Activity
        val itemOrderHistoryBinding = DataBindingUtil.inflate<OrderHistoryItemBinding>(LayoutInflater.from(context), R.layout.order_history_item, viewGroup, false)
        itemOrderHistoryBinding.colorsData = colorsHelper
        itemOrderHistoryBinding.stringData = StringsHelper
        return ViewHolder(itemOrderHistoryBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.llLayout.background = colorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        val model = list[position]
        if (model.title != null && !model.title.equals("", ignoreCase = true) && !model.title.equals("null", ignoreCase = true)) {
            // holder.binding.textView.setText(model.getTitle());
        }
        if (model.price != null && !model.price.equals("", ignoreCase = true) && !model.price.equals("null", ignoreCase = true) && model.currency != null && !model.currency.equals(
                "",
                ignoreCase = true
            ) && !model.currency.equals("null", ignoreCase = true)
        ) {
            holder.binding.packageName.text = String.format("%s %s", model.price, model.currency)
        }
        if (model.entitlementState) {
            holder.binding.activeExpired.visibility = View.VISIBLE
            holder.binding.st.visibility = View.VISIBLE
            holder.binding.activeExpired.setText(R.string.active_text)
        } else {
            holder.binding.st.visibility = View.GONE
            holder.binding.activeExpired.visibility = View.GONE
        }
        if (model.createdDate != null && list[position].createdDate > 0) {
            holder.binding.orderDateEndId.text = AppCommonMethod.getDateFromTimeStamp(model.createdDate.toDouble())
        }
        if (model.subscriptionType != null && !model.subscriptionType.equals("", ignoreCase = true)) {
            holder.binding.orderPurchaseId.text = model.subscriptionType
        } else {
            holder.binding.orderPurchaseId.text = "NA"
        }
        if (model.isOnTrial != null) {
            val trial = model.isOnTrial as Boolean
            if (trial) {
                if (model.currentExpiryDate != null && list[position].currentExpiryDate > 0) {
                    val expireText =
                        String.format("%s%s", context!!.getString(R.string.trial_period_expiring_on) + " ", AppCommonMethod.getDateFromTimeStamp(list[position].currentExpiryDate.toDouble()))
                    holder.binding.expireOn.text = expireText
                } else {
                    if (model.nextChargeDate != null && list[position].nextChargeDate > 0) {
                        val expireText = String.format("%s%s", context!!.getString(R.string.renewal_on) + " ", AppCommonMethod.getDateFromTimeStamp(list[position].nextChargeDate.toDouble()))
                        holder.binding.expireOn.text = expireText
                    }
                }
            } else {
                if (model.currentExpiryDate != null && list[position].currentExpiryDate > 0) {
                    val expireText = String.format("%s%s", context!!.getString(R.string.expire_on) + " ", AppCommonMethod.getDateFromTimeStamp(list[position].currentExpiryDate.toDouble()))
                    holder.binding.expireOn.text = expireText
                } else {
                    if (model.nextChargeDate != null && list[position].nextChargeDate > 0) {
                        val expireText = String.format("%s%s", context!!.getString(R.string.renewal_on) + " ", AppCommonMethod.getDateFromTimeStamp(list[position].nextChargeDate.toDouble()))
                        holder.binding.expireOn.text = expireText
                    }
                }
            }
        } else {
            if (model.currentExpiryDate != null && list[position].currentExpiryDate > 0) {
                val expireText = String.format("%s%s", context!!.getString(R.string.expire_on) + " ", AppCommonMethod.getDateFromTimeStamp(list[position].currentExpiryDate.toDouble()))
                holder.binding.expireOn.text = expireText
            } else {
                if (model.nextChargeDate != null && list[position].nextChargeDate > 0) {
                    val expireText = String.format("%s%s", context!!.getString(R.string.renewal_on) + " ", AppCommonMethod.getDateFromTimeStamp(list[position].nextChargeDate.toDouble()))
                    holder.binding.expireOn.text = expireText
                }
            }
        }
        if (position == 1) {
            val unwrappedDrawable = holder.binding.activeExpired.background
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable)
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor("#EC1C24"))
            holder.binding.activeExpired.setText(R.string.expired)
        }
        if (list[position].offerPeriod != null && !list[position].offerPeriod.equals("", ignoreCase = true) && list[position].offerPeriod.equals(VodOfferType.MONTHLY.name, ignoreCase = true)) {
            val duration = context!!.getString(R.string.one_month)
            holder.binding.textView.text = list[position].offerPeriod
            holder.binding.duration.text = duration
        } else if (list[position].offerPeriod != null && !list[position].offerPeriod.equals("", ignoreCase = true) && list[position].offerPeriod.equals(VodOfferType.ANNUAL.name, ignoreCase = true)) {
            val duration = " " + context!!.getString(R.string.one_year)
            holder.binding.textView.text = list[position].offerPeriod
            holder.binding.duration.text = duration
        } else {
            holder.binding.duration.visibility = View.GONE
        }
        if (model.paymentProvider != null && !model.paymentProvider.equals("", ignoreCase = true) && !model.paymentProvider.equals("null", ignoreCase = true)) {
            when (model.paymentProvider) {
                AppConstants.APPLE -> setTextView("Apple", holder)
                AppConstants.GOOGLE_IAP -> setTextView("Google", holder)
                AppConstants.TWO_C_TWO_P -> setTextView("Apple", holder)
                AppConstants.AMAZON_IAP -> setTextView("Amazon", holder)
                AppConstants.STRIPE -> setTextView("Stripe", holder)
                AppConstants.PAYPAL -> setTextView("Paypal", holder)
                else -> {
                    paymentMode = model.paymentProvider
                    paymentMode = paymentMode?.replace("_", " ")
                    setTextView(paymentMode!!, holder)
                }
            }
        } else {
            paymentMode = "NA"
        }
        val paymentStatus: String = if (model.orderStatus != null && !model.orderStatus.equals("", ignoreCase = true) && !model.orderStatus.equals("null", ignoreCase = true)) {
            model.orderStatus
        } else {
            "NA"
        }
        holder.binding.paymentStatus.text = paymentStatus
        val transactionId: String = if (model.transactionID != null && !model.transactionID.equals("", ignoreCase = true) && !model.transactionID.equals("null", ignoreCase = true)) {
            model.transactionID
        } else {
            "NA"
        }
        holder.binding.transactionId.text = transactionId
        holder.binding.transactionId.setOnClickListener {
            val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Copied Text", holder.binding.transactionId.text.toString())
            clipboard.setPrimaryClip(clip)
        }
    }

    private fun setTextView(apple: String, holder: ViewHolder) {
        holder.binding.paymentMode.text = apple
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: OrderHistoryItemBinding) : RecyclerView.ViewHolder(binding.root)
}