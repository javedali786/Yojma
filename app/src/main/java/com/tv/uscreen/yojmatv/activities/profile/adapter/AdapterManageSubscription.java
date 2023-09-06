package com.tv.uscreen.yojmatv.activities.profile.adapter;

import static com.tv.uscreen.yojmatv.R.layout;
import static com.tv.uscreen.yojmatv.R.string;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tv.uscreen.yojmatv.activities.purchase.purchase_model.PurchaseModel;
import com.tv.uscreen.yojmatv.activities.purchase.ui.VodOfferType;
import com.tv.uscreen.yojmatv.databinding.ManageSubscriptionItemBinding;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;

import java.util.List;

;

public class AdapterManageSubscription extends RecyclerView.Adapter<AdapterManageSubscription.ItemHolder> {

    List<PurchaseModel> items;
    AdapterManageSubscription.OnPurchaseItemClick mListener;
    Context context;
    String paymentMode;
    public AdapterManageSubscription(List<PurchaseModel> items, AdapterManageSubscription.OnPurchaseItemClick mListener, Context context) {
        this.items = items;
        this.mListener = mListener;
        this.context=context;
    }

    @NonNull
    @Override
    public AdapterManageSubscription.ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        ManageSubscriptionItemBinding manageSubscriptionItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layout.manage_subscription_item, viewGroup, false);
        return new AdapterManageSubscription.ItemHolder(manageSubscriptionItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterManageSubscription.ItemHolder holder, int position) {
        PurchaseModel model = items.get(position);
        if (model.getTitle() != null && !model.getTitle().equalsIgnoreCase("") && !model.getTitle().equalsIgnoreCase("null")) {
            holder.binding.annuallyId.setText(model.getTitle().toUpperCase());
        }

        if (model.getPrice() != null && !model.getPrice().equalsIgnoreCase("") && !model.getPrice().equalsIgnoreCase("null")
                && model.getCurrency()!= null && !model.getCurrency().equalsIgnoreCase("") && !model.getCurrency().equalsIgnoreCase("null")) {
            holder.binding.paymentId.setText(String.format("%s %s", model.getPrice(), model.getCurrency()));
        }

        if (model.getOfferPeriod() != null && !model.getOfferPeriod().equalsIgnoreCase("") && model.getOfferPeriod().equalsIgnoreCase(VodOfferType.MONTHLY.name())) {

            String duration = context.getString(string.one_month);
           // holder.binding.annuallyId.setText(model.getOfferPeriod());
            //holder.binding.duration.setText(duration);
        } else if (model.getOfferPeriod() != null && !model.getOfferPeriod().equalsIgnoreCase("") && model.getOfferPeriod().equalsIgnoreCase(VodOfferType.ANNUAL.name())) {

            String duration = " " + context.getString(string.one_year);
            holder.binding.annuallyId.setText(model.getOfferPeriod());

            //holder.binding.duration.setText(duration);
        } else {
            //holder.binding.duration.setVisibility(View.GONE);
        }

        if (model.getCreatedDate() != null && model.getCreatedDate() > 0) {
            holder.binding.cbPurchase.setText(AppCommonMethod.getDateFromTimeStamp(model.getCreatedDate()));
        }

        if (model.getEntitlementState()) {
            holder.binding.activeId.setVisibility(View.VISIBLE);
            holder.binding.activeId.setText(string.active_text);
        } else {
            holder.binding.activeId.setText("");
            /*holder.binding.st.setVisibility(View.GONE);
            holder.binding.activeExpired.setVisibility(View.GONE);*/
        }

        try {
            if (model.isCancelled()) {
                holder.binding.cancelSubscriptionId.setVisibility(View.GONE);
                holder.binding.llCancellationDate.setVisibility(View.VISIBLE);
                String date = AppCommonMethod.expiryDate(Math.toIntExact(model.getExpiryDate()));
                holder.binding.cancellationDate.setText(date);
            } else {
                holder.binding.llCancellationDate.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Logger.e(e);
        }

        if (model.getPaymentProvider() != null && !model.getPaymentProvider().equalsIgnoreCase("") && !model.getPaymentProvider().equalsIgnoreCase("null") ) {
            switch (model.getPaymentProvider()){
                case AppConstants.APPLE:
                    setTextView("Apple",holder);
                    break;
                case AppConstants.GOOGLE_IAP:
                    setTextView("Google",holder);
                    holder.binding.cancelSubscriptionId.setVisibility(View.VISIBLE);
                    break;
                case AppConstants.TWO_C_TWO_P:
                    setTextView("Apple",holder);
                    break;
                case AppConstants.AMAZON_IAP:
                    setTextView("Amazon",holder);
                    break;
                case AppConstants.STRIPE:
                    setTextView("Stripe",holder);
                    break;
                case AppConstants.PAYPAL:
                    setTextView("Paypal",holder);
                    break;
                default:
                    holder.binding.cancelSubscriptionId.setVisibility(View.GONE);
                    paymentMode = model.getPaymentProvider();
                    paymentMode=paymentMode.replace("_"," ");
                    setTextView(paymentMode,holder);
            }
        }else {
            paymentMode = "NA";
        }
//        paymentMode=paymentMode.replace("_"," ");
//
//        holder.binding.appleId.setText(paymentMode);

        if (model.getCreatedDate() != null && model.getCreatedDate() > 0) {
            holder.binding.cbPurchaseDate.setText(AppCommonMethod.getDateFromTimeStamp(model.getCreatedDate()));
        }

        holder.binding.cancelSubscriptionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = context.getPackageName();
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/account/subscriptions?package=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

    }

    private void setTextView(String paymentType, ItemHolder holder) {
        holder.binding.appleId.setText(paymentType);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        final ManageSubscriptionItemBinding binding;

        public ItemHolder(ManageSubscriptionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public interface OnPurchaseItemClick {
        void onPurchaseCardClick(boolean click, PurchaseModel planName);
        void setDescription(String description,PurchaseModel planName);
    }

}
