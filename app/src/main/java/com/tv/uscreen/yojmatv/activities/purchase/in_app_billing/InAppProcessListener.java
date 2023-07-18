package com.tv.uscreen.yojmatv.activities.purchase.in_app_billing;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;

import java.util.List;

public interface InAppProcessListener {
    void onBillingInitialized();
    void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases);
    void onListOfSKUFetched(@Nullable List<SkuDetails> purchases);
    void onBillingError(@Nullable BillingResult error);
}
