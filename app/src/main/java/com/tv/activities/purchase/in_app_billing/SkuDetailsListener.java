package com.tv.activities.purchase.in_app_billing;

import com.android.billingclient.api.SkuDetails;

public interface SkuDetailsListener {
    void response(SkuDetails skuDetails);
}
