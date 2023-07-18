package com.tv.activities.purchase.in_app_billing;

import com.android.billingclient.api.Purchase;

import java.util.List;

public interface RestoreSubscriptionCallback {
    void subscriptionStatus(boolean status,String message);
   default void subscriptionHistory(boolean status, List<Purchase> purchases){

    }
}
