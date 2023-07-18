package com.tv.uscreen.activities.purchase.in_app_billing;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.gson.Gson;
import com.tv.uscreen.activities.purchase.BillingConstants;
import com.tv.uscreen.activities.purchase.PrintLogging;
import com.tv.uscreen.utils.Logger;
import com.tv.uscreen.utils.constants.AppConstants;
import com.tv.uscreen.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BillingProcessor implements PurchasesUpdatedListener {
    private final WeakReference<Activity> mActivity;
    private BillingClient myBillingClient=null;
    private final InAppProcessListener inAppProcessListener;
    String tagSku = "skuDetails";

    public BillingProcessor(Activity activity,InAppProcessListener billingCallBacks) {
        mActivity = new WeakReference<>(activity);
        inAppProcessListener=billingCallBacks;
    }

    public static boolean isIabServiceAvailable(Activity context) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentServices(getBindServiceIntent(), 0);
        return list != null && !list.isEmpty();
    }

    private static Intent getBindServiceIntent()
    {
        Intent intent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        intent.setPackage("com.android.vending");
        return intent;
    }

    /** A reference to BillingClient */
    public void initializeBillingProcessor() {
        myBillingClient =
                BillingClient.newBuilder(mActivity.get())
                        .enablePendingPurchases()
                        .setListener(this)
                        .build();
        // clears billing manager when the jvm exits or gets terminated.
        Runtime.getRuntime().addShutdownHook(new Thread(this::destroy));
        // starts play billing service connection
        connectToPlayBillingService();
    }

    /** Initiates Google Play Billing Service. */
    private void connectToPlayBillingService() {
        PrintLogging.printLog(TAG, "connectToPlayBillingService");
        if (!myBillingClient.isReady()) {
            startServiceConnection(
                    () -> {
                        // IAB is fully set up. Now, let's get an inventory of stuff we own.
                        PrintLogging.printLog(TAG, "Setup successful. Querying inventory.");
                        if (inAppProcessListener!=null){
                            inAppProcessListener.onBillingInitialized();
                        }
                    });
        }
    }

    private void querySkuDetails() {
        Map<String, SkuDetails> skuResultMap = new HashMap<>();
        List<String> subscriptionSkuList = BillingConstants.getSkuList(BillingClient.SkuType.SUBS);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(subscriptionSkuList).setType(BillingClient.SkuType.SUBS);
        querySkuDetailsAsync(
                skuResultMap,
                params,
                BillingClient.SkuType.SUBS,
                () -> {
                    List<String> inAppSkuList = BillingConstants.getSkuList(BillingClient.SkuType.INAPP);
                    SkuDetailsParams.Builder params1 = SkuDetailsParams.newBuilder();
                    params1.setSkusList(inAppSkuList).setType(BillingClient.SkuType.INAPP);
                    querySkuDetailsAsync(skuResultMap, params1, BillingClient.SkuType.INAPP, null);
                });
    }

    /**
     * Makes connection with BillingClient.
     *
     * @param executeOnSuccess A runnable implementation.
     */
    private void startServiceConnection(Runnable executeOnSuccess) {
        myBillingClient.startConnection(
                new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                        // The billing client is ready. You can query purchases here.
                        PrintLogging.printLog(TAG, "Setup finished");
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && executeOnSuccess != null) {
                            executeOnSuccess.run();
                        }
                        logErrorType(billingResult);
                    }

                    @Override
                    public void onBillingServiceDisconnected() {
                        // Try to restart the connection on the next request to
                        // Google Play by calling the startConnection() method.
                    }
                });
    }

    /**
     * Logs Billing Client Success, Failure and error responses.
     *
     * @param billingResult to identify the states of Billing Client Responses.
     * @see <a
     *     href="https://developer.android.com/google/play/billing/billing_reference.html">Google
     *     Play InApp Purchase Response Types Guide</a>
     */
    public static final String TAG = BillingProcessor.class.getName();
    private void logErrorType(BillingResult billingResult) {
        switch (billingResult.getResponseCode()) {
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
            case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
                PrintLogging.printLog(
                        TAG,
                        "Billing unavailable. Make sure your Google Play app is setup correctly");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.OK:
                PrintLogging.printLog(TAG, "Setup successful!");
                break;
            case BillingClient.BillingResponseCode.USER_CANCELED:
                PrintLogging.printLog(TAG, "User has cancelled Purchase!");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
                PrintLogging.printLog(TAG, "Product is not available for purchase");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.ERROR:
                PrintLogging.printLog(TAG, "fatal error during API action");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                PrintLogging.printLog(TAG, "Failure to purchase since item is already owned");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
                PrintLogging.printLog(TAG, "Failure to consume since item is not owned");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
                PrintLogging.printLog(TAG, "Billing feature is not supported on your device");
                inAppProcessListener.onBillingError(billingResult);
                break;
            case BillingClient.BillingResponseCode.SERVICE_TIMEOUT:
                PrintLogging.printLog(TAG, "Billing service timeout occurred");
                inAppProcessListener.onBillingError(billingResult);
                break;
            default:
                PrintLogging.printLog(TAG, "Billing unavailable. Please check your device");
                inAppProcessListener.onBillingError(billingResult);
                break;
        }
    }

    private void destroy() {
        PrintLogging.printLog(TAG, "Destroying the billing manager.");
        if (myBillingClient.isReady()) {
            myBillingClient.endConnection();
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> purchases) {
        int v=billingResult.getResponseCode();
        Log.w("onPurchaseUpdate",v+"");
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            if (inAppProcessListener != null) {
                try {
                    if (purchases.get(0).getPurchaseToken() != null) {
                        for (Purchase purchase : purchases) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && productType != null && !productType.equalsIgnoreCase("") && productType.equalsIgnoreCase(PurchaseType.PRODUCT.name())) {
                                handleConsumablePurchasesAsync(purchase);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.w("productType", e.toString());
                }

                inAppProcessListener.onPurchasesUpdated(billingResult, purchases);
            }

            try {
                if (myBillingClient != null && myBillingClient.isReady() && billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases.get(0).getPurchaseToken() != null) {
                    for (Purchase purchase : purchases) {
                        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                            Log.d(TAG, "onPurchasesUpdated: ");
                        }
                    }
                }
            } catch (Exception e) {
                Logger.e(e);
            }
        } else {
            // Handle any other error codes.
            logErrorType(billingResult);
        }
    }

    private void handleConsumablePurchasesAsync(Purchase purchase) {
        try {
            // Generating Consume Response listener
            final ConsumeResponseListener listener =
                    (billingResult, purchaseToken) -> {
                        // If billing service was disconnected, we try to reconnect 1 time
                        // (feel free to introduce your retry policy here).
                        try {
                            Log.w("tvod",billingResult.getDebugMessage()+" "+billingResult.getResponseCode());
                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                Log.w("tvod","consumed");
                            } else {
                                Log.w("tvod","failed");
                            }
                        }catch (Exception e){
                            Logger.e(e);
                        }

                    };
            // Consume the purchase async
            final ConsumeParams consumeParams =
                    ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
            // Creating a runnable from the request to use it inside our connection retry policy below
            executeServiceRequest(() -> myBillingClient.consumeAsync(consumeParams, listener));
        }catch (Exception e){
            Logger.e(e);
        }
    }


    /**
     * Queries SKU Details from Google Play Remote Server of SKU Types (InApp and Subscription).
     *
     * @param skuResultLMap contains SKU ID and Price Details returned by the sku details query.
     * @param params contains list of SKU IDs and SKU Type (InApp or Subscription).
     * @param billingType InApp or Subscription.
     * @param executeWhenFinished contains query for InApp SKU Details that will be run after
     */
    SkuDetails skuDetails=null;
    private void querySkuDetailsAsync(
            Map<String, SkuDetails> skuResultLMap,
            SkuDetailsParams.Builder params,
            @BillingClient.SkuType String billingType,
            Runnable executeWhenFinished) {
        final SkuDetailsResponseListener listener =
                (billingResult, skuDetailsList) -> {
                    // Process the result.
                    if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                        PrintLogging.printLog(
                                TAG,
                                "Unsuccessful query for type: "
                                        + billingType
                                        + ". Error code: "
                                        + billingResult.getResponseCode());
                    } else if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                        for (SkuDetails sku : skuDetailsList) {
                            Log.w(tagSku,sku.getPrice()+"-->>"+sku.getPriceCurrencyCode());
                            this.skuDetails=sku;
                            skuResultLMap.put(sku.getSku(), sku);
                        }
                    }
                    if (executeWhenFinished != null) {
                        executeWhenFinished.run();
                        return;
                    }
                    if (skuResultLMap.size() == 0) {
                        PrintLogging.printLog(
                                TAG, "sku error: " + "nosku");
                    } else {
                        PrintLogging.printLog(TAG, "storing sku list locally");
                    }
                };
        // Creating a runnable from the request to use it inside our connection retry policy below
        executeServiceRequest(() -> myBillingClient.querySkuDetailsAsync(params.build(), listener));
    }

    private void executeServiceRequest(Runnable runnable) {
        if (myBillingClient.isReady()) {
            runnable.run();
        } else {
            // If billing service was disconnected, we try to reconnect 1 time.
            // (feel free to introduce your retry policy here).
            startServiceConnection(runnable);
        }
    }



    /**
     * Stores Purchased Items, consumes consumable items, acknowledges non-consumable items.
     *
     * @param purchases list of Purchase Details returned from the queries.
     */
    private void processPurchases(@NonNull List<Purchase> purchases) {
        if (!purchases.isEmpty()) {
            PrintLogging.printLog(TAG, "purchase list size: " + purchases.size());
        }
        for (Purchase purchase : purchases) {
            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
                Log.w("purchaseToken-->>>",purchase.getPurchaseToken());
            } else if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) {
                PrintLogging.printLog(
                        TAG, "Received a pending purchase of SKU: " + purchase.getSkus().get(0));
                // handle pending purchases, e.g. confirm with users about the pending
                // purchases, prompt them to complete it, etc.
                // TODO: 8/24/2020 handle this in the next release.
            }
        }
        for (Purchase purchase : purchases) {
            if (purchase.getSkus().get(0).equals(BillingConstants.SKU_BUY_APPLE)) {
                Log.d(TAG, "processPurchases: ");
            } else {
                 acknowledgeNonConsumablePurchasesAsync(purchase);
            }
        }
    }

    public void acknowledgeNonConsumablePurchasesAsync(Purchase purchase) {
        final AcknowledgePurchaseParams params =
                AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.getPurchaseToken())
                        .build();
        final AcknowledgePurchaseResponseListener listener =
                billingResult -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        PrintLogging.printLog(TAG, "onAcknowledgePurchaseResponse: " + billingResult.getResponseCode());
                    } else {
                        PrintLogging.printLog(TAG, "onAcknowledgePurchaseResponse: " + billingResult.getDebugMessage());
                    }
                };
        executeServiceRequest(() -> myBillingClient.acknowledgePurchase(params, listener));
    }


    BillingFlowParams purchaseParams;
    public void initiatePurchaseFlow(@NonNull Activity activity, @NonNull SkuDetails skuDetails) {
        try {
            if (skuDetails.getType().equals(BillingClient.SkuType.SUBS) && areSubscriptionsSupported()
                    || skuDetails.getType().equals(BillingClient.SkuType.INAPP)) {
                if (KsPreferenceKeys.getInstance().getAppPrefUserId()!=null && !KsPreferenceKeys.getInstance().getAppPrefUserId().equalsIgnoreCase("")){
                    purchaseParams =
                            BillingFlowParams.newBuilder().setSkuDetails(skuDetails).setObfuscatedAccountId(KsPreferenceKeys.getInstance().getAppPrefUserId()).setObfuscatedProfileId(KsPreferenceKeys.getInstance().getPAYMENTORDERID()).build();
                }else {
                    purchaseParams  =
                            BillingFlowParams.newBuilder().setSkuDetails(skuDetails).setObfuscatedProfileId(KsPreferenceKeys.getInstance().getPAYMENTORDERID()).build();
                }

                executeServiceRequest(
                        () -> {
                            PrintLogging.printLog(TAG, "Launching in-app purchase flow.");
                            myBillingClient.launchBillingFlow(activity, purchaseParams);
                        });
            }

        }catch (Exception e){
            Logger.e(e);
        }
    }

    private boolean areSubscriptionsSupported() {
        final BillingResult billingResult =
                myBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
            PrintLogging.printLog(
                    TAG,
                    "areSubscriptionsSupported() got an error response: "
                            + billingResult.getResponseCode());
            if (inAppProcessListener!=null){
                inAppProcessListener.onBillingError(billingResult);
            }
        }
        return billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK;
    }

    String purchaseType="";
    String productType="";
    public void purchase(Activity activity, String sku, String developerPayload, String purchaseType) {
        this.purchaseType="";
        this.productType="";
        Log.d(TAG, "purchase: " + developerPayload);
        if (purchaseType.equalsIgnoreCase(PurchaseType.PRODUCT.name())){
            this.purchaseType=PurchaseType.PRODUCT.name();
            this.productType=PurchaseType.PRODUCT.name();
            if (myBillingClient!=null && myBillingClient.isReady()){
                getProductSkuDetails(activity,sku);
            }
        }else {
            if (myBillingClient!=null && myBillingClient.isReady()){
                this.productType=PurchaseType.SUBSCRIPTION.name();
                getSubscriptionSkuDetails(activity,sku);
            }
        }
    }

    public void getProductSkuDetails(Activity activity, String sku) {
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        myBillingClient.querySkuDetailsAsync(params.build(),
                (billingResult, skuDetailsList) -> {
                    if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                        for (SkuDetails details : skuDetailsList) {
                            Log.w(tagSku, details.getPrice() + "-->>" + details.getPriceCurrencyCode());
                            if (details.getSku().equalsIgnoreCase(sku)){
                                initiatePurchaseFlow(activity,details);
                            }
                        }
                    }
                });
    }

    public void getSubscriptionSkuDetails(Activity activity, String sku) {
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                (billingResult, skuDetailsList) -> {
                    if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                        for (SkuDetails details : skuDetailsList) {
                            Log.w(tagSku, details.getPrice() + "-->>" + details.getPriceCurrencyCode());
                            if (details.getSku().equalsIgnoreCase(sku)){
                                initiatePurchaseFlow(activity,details);
                            }
                        }
                    }
                });
    }

    public SkuDetails getProductSkuDetail(String sku) {
        final SkuDetails[] skuDetail=new SkuDetails[1];
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        myBillingClient.querySkuDetailsAsync(params.build(),
                (billingResult, skuDetailsList) -> {
                    if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                        for (SkuDetails details : skuDetailsList) {
                            Log.w(tagSku, details.getPrice() + "-->>" + details.getPriceCurrencyCode());
                            if (details.getSku().equalsIgnoreCase(sku)){
                                skuDetail[0]=details;
                            }
                        }
                    }
                });
        return skuDetails;
    }


    public SkuDetails getSubscriptionSkuDetail(String sku) {
        final SkuDetails[] skuDetail=new SkuDetails[1];
        List<String> skuList = new ArrayList<>();
        skuList.add(sku);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                (billingResult, skuDetailsList) -> {
                    if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                        for (SkuDetails details : skuDetailsList) {
                            Log.w(tagSku, details.getPrice() + "-->>" + details.getPriceCurrencyCode());
                            Log.w(tagSku,  sku+"-->>" + details.getSku());
                            if (details.getSku().equalsIgnoreCase(sku)){
                                skuDetail[0]=details;
                            }
                        }
                    }
                });
       return skuDetails;
    }


    public boolean isReady() {
        if (myBillingClient!=null){
            return myBillingClient.isReady();
        }else {
            return false;
        }

    }

    public void endConnection() {
        if (myBillingClient!=null && myBillingClient.isReady()){
            myBillingClient.endConnection();
        }
    }
    List<SkuDetails> listOfllSkus;
    public void getAllSkuDetails(List<String> subSkuList, List<String> productSkuList) {
        try{
            if (subSkuList!=null) {
                listOfllSkus = new ArrayList<>();
                SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                params.setSkusList(subSkuList).setType(BillingClient.SkuType.SUBS);
                myBillingClient.querySkuDetailsAsync(params.build(),
                        (billingResult, skuDetailsList) -> {
                            Log.w(tagSku, billingResult.getDebugMessage() + " " + billingResult.getResponseCode() + " " + billingResult);
                            if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                                for (SkuDetails details : skuDetailsList) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(details);
                                    Log.w(tagSku, json);
                                    listOfllSkus.add(details);
                                }
                            }
                            fetchAllProducts(productSkuList, listOfllSkus);
                        });
            }

        }catch (Exception e){
            Logger.e(e);
        }
    }

    private void fetchAllProducts(List<String> productSkuList, List<SkuDetails> listOfllSkus) {
        if (productSkuList!=null){
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(productSkuList).setType(BillingClient.SkuType.INAPP);
            myBillingClient.querySkuDetailsAsync(params.build(),
                    (billingResult, skuDetailsList) -> {
                        Log.w(tagSku,  billingResult.getDebugMessage()+" "+billingResult.getResponseCode()+" "+ billingResult);
                        if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                            for (SkuDetails details : skuDetailsList) {
                                Log.w(tagSku, details.getPrice() + "-->>" + details.getPriceCurrencyCode());
                                listOfllSkus.add(details);
                                inAppProcessListener.onListOfSKUFetched(listOfllSkus);
                            }
                        }else {
                            inAppProcessListener.onListOfSKUFetched(listOfllSkus);
                        }
                    });
        }else {
            inAppProcessListener.onListOfSKUFetched(listOfllSkus);
        }

    }

    public List<SkuDetails> getListOfllSkus() {
        return listOfllSkus;
    }

    public SkuDetails getLocalSubscriptionSkuDetail(String identifier) {
        try {
            if (getListOfllSkus()!=null && !getListOfllSkus().isEmpty()){
                for (int i=0;i<getListOfllSkus().size();i++){
                    if (identifier.equalsIgnoreCase(getListOfllSkus().get(i).getSku())){
                        return getListOfllSkus().get(i);
                    }
                }
            }
        }catch (Exception e){
            Log.e(TAG, "getLocalSubscriptionSkuDetail: ", e );
        }

        return null;
    }

    public void getAllSkuSubscriptionDetails(List<String> subSkuList) {
        listOfllSkus=new ArrayList<>();
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(subSkuList).setType(BillingClient.SkuType.SUBS);
        myBillingClient.querySkuDetailsAsync(params.build(),
                (billingResult, skuDetailsList) -> {
                    if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                        for (SkuDetails sku : skuDetailsList) {
                            Log.w(tagSku, sku.getPrice() + "-->>" + sku.getPriceCurrencyCode());
                            listOfllSkus.add(sku);
                        }
                    }
                    inAppProcessListener.onListOfSKUFetched(listOfllSkus);
                });
    }

    public boolean isOneTimePurchaseSupported() {
        final BillingResult billingResult= myBillingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        return billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK;
    }


    String purchasedSKU="";
    String purchasedToken="";
    RestoreSubscriptionCallback callback;
    public void queryPurchases(RestoreSubscriptionCallback calling) {
        try {
            this.callback=calling;
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus().equalsIgnoreCase(AppConstants.UserStatus.Login.toString()) && myBillingClient != null) {
                final Purchase.PurchasesResult purchasesResult =
                        myBillingClient.    queryPurchases(BillingClient.SkuType.SUBS);

                final List<Purchase> purchases = new ArrayList<>();
                if (purchasesResult.getPurchasesList() != null) {
                    purchases.addAll(purchasesResult.getPurchasesList());
                }
                if (!purchases.isEmpty()) {
                    purchasedSKU = purchases.get(0).getSkus().get(0);
                    purchasedToken = purchases.get(0).getPurchaseToken();
                }


                PurchaseHandler.getInstance().checkPurchaseHistory(purchases, myBillingClient, callback);
            }
        }catch (Exception e){
            Log.w("crashHap",e.toString());
        }


    }

    public void queryPurchases(RestoreSubscriptionCallback calling,int type) {
        try {
            Log.w("identifiers","in queryPurchases");
            if (KsPreferenceKeys.getInstance().getAppPrefLoginStatus().equalsIgnoreCase(AppConstants.UserStatus.Login.toString()) && myBillingClient != null) {
                final Purchase.PurchasesResult purchasesResult =
                        myBillingClient.queryPurchases(BillingClient.SkuType.SUBS);

                final List<Purchase> purchases = new ArrayList<>();
                if (purchasesResult.getPurchasesList() != null) {
                    purchases.addAll(purchasesResult.getPurchasesList());
                }
                if (!purchases.isEmpty()) {
                    purchasedSKU = purchases.get(0).getSkus().get(0);
                    purchasedToken = purchases.get(0).getPurchaseToken();
                    calling.subscriptionHistory(true,purchases);
                }

                Gson gson = new Gson();
                String json = gson.toJson(purchases);
                Log.w("BillingResult_purchases", json);
                Log.w("identifiers","in queryPurchases 2");

            }
        }catch (Exception e){
            Log.w("crashHap",e.toString());
        }
    }

    public void checkPurchase(List<Purchase> purchases) {
        PurchaseHandling.getInstance().checkPurchaseHistory(purchases, myBillingClient);
    }
}
