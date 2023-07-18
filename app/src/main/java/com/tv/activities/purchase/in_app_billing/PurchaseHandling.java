package com.tv.activities.purchase.in_app_billing;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tv.OttApplication;
import com.tv.R;
import com.tv.SDKConfig;
import com.tv.activities.purchase.purchase_model.PurchaseModel;
import com.tv.activities.purchase.purchase_model.PurchaseResponseModel;
import com.tv.activities.purchase.ui.VodOfferType;
import com.tv.beanModel.membershipAndPlan.DataItem;
import com.tv.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import com.tv.networking.apiendpoints.RequestConfig;
import com.tv.networking.detailPlayer.APIDetails;
import com.tv.networking.intercepter.ErrorCodesIntercepter;
import com.tv.utils.Logger;
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseHandling {

    private static PurchaseHandling projectRepository;
    BillingClient myBillingClient;
    boolean getPlans=false;
    public synchronized static PurchaseHandling getInstance() {
        if (projectRepository == null) {
            projectRepository = new PurchaseHandling();
        }
        return projectRepository;
    }

    public void checkPurchaseHistory(List<Purchase> purchases, BillingClient billingClient) {
        if (purchases!=null && !purchases.isEmpty()){
            try {
                myBillingClient=billingClient;
                if (purchases.get(0).getPurchaseState()==Purchase.PurchaseState.PURCHASED){
                    JSONObject jsonObject=new JSONObject(purchases.get(0).getOriginalJson());
                    if (jsonObject.has("purchaseToken")){
                        Log.w("purchaseToken--2",jsonObject.getString("purchaseToken"));
                        Log.w("purchaseToken--2",purchases.get(0).getSkus().get(0));
                        if (jsonObject.has("obfuscatedAccountId")){
                            Log.w("purchaseToken--2",jsonObject.getString("obfuscatedAccountId"));
                            Log.w("purchaseToken--2", KsPreferenceKeys.getInstance().getAppPrefUserId());
                            if (jsonObject.getString("obfuscatedAccountId").equalsIgnoreCase(KsPreferenceKeys.getInstance().getAppPrefUserId())){
                                Log.w("purchaseToken--2","conditionTrue");
                                Log.w("identifiers","in queryPurchases");
                                callGetPlansAPI(purchases);
                            }
                        }else {
                            callGetPlansAPI(purchases);
                        }

                    }
                }

            } catch (Exception e) {
                Logger.e(e);
            }
        }else {
            Log.w("purchaseToken--2","no purchase available");
        }
    }

    private void callGetPlansAPI(List<Purchase> purchases) {
        if (getPlans){
            return;
        }
        try {
            getPlans=true;
            Log.w("identifiers","in callGetPlansAPI"+" -->"+SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL()+" -->"+KsPreferenceKeys.getInstance().getSUBSCRIPTION_BASE_URL());
            APIDetails endpoint = RequestConfig.getUserInteration(KsPreferenceKeys.getInstance().getAppPrefAccessToken()).create(APIDetails.class);
            Call<ResponseMembershipAndPlan> call = endpoint.getPlans("RECURRING_SUBSCRIPTION",true);
            call.enqueue(new Callback<ResponseMembershipAndPlan>() {
                @Override
                public void onResponse(@NonNull Call<ResponseMembershipAndPlan> call, @NonNull Response<ResponseMembershipAndPlan> response) {
                    Log.w("getPlansRes","in");
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
                        purchaseResponseModel.setData(response.body().getData());
                        checkEntitlementState(purchaseResponseModel,purchases);
                    } else {
                        purchaseResponseModel.setStatus(false);
                        getPlans=false;
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResponseMembershipAndPlan> call, @NonNull Throwable t) {
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    purchaseResponseModel.setStatus(false);
                    getPlans=false;
                }
            });

        }catch (Exception ignored){
            getPlans=false;
            Log.w("identifiers","in callGetPlansAPI error"+ignored.toString());
        }


    }

    final int count=0;
    String purchasedSKU="";
    String newSKU="";
    String finalSKU="";
    String playstoreSKU="";
    boolean initiatePurchase=false;
    private void checkEntitlementState(ResponseMembershipAndPlan purchaseResponseModel, List<Purchase> purchases) {
        try {
            Log.w("identifiers","in checkEntitlementState");
            if (purchaseResponseModel!=null && !purchaseResponseModel.getData().isEmpty() && purchaseResponseModel.isStatus()){
                for (int i = 0; i < purchaseResponseModel.getData().size(); i++) {
                    if (!purchaseResponseModel.getData().get(i).getEntitlementState()) {
                        Purchase purchase=purchases.get(count);
                        Log.w("identifiers",purchase.getSkus().get(0));
                        Log.w("identifiers",purchaseResponseModel.getData().get(i).getIdentifier());
                        playstoreSKU=purchase.getSkus().get(0);
                        newSKU=playstoreSKU;
                        finalSKU=newSKU;
                        Log.w("finalSKU",finalSKU+"<---->"+purchaseResponseModel.getData().get(i).getCustomData().getAndroidProductId());

                        Log.w("identifiers",finalSKU);
                        if (finalSKU.equalsIgnoreCase(purchaseResponseModel.getData().get(i).getCustomData().getAndroidProductId())){
                            Log.w("identifiers",purchaseResponseModel.getData().get(i).getIdentifier());
                            Log.w("identifiers",purchase.getSkus().get(0));
                            Log.w("identifiers","initiatePurchaseflow");
                            initiatePurchaseFlow(purchase,purchaseResponseModel.getData().get(i));
                            break;
                        }else {
                            getPlans=false;
                        }
                    }
                }
            }else {
                getPlans=false;
            }
        }catch (Exception ignored){
            getPlans=false;
        }

    }

    private void initiatePurchaseFlow(Purchase purchase, DataItem dataItem) {
        try {
            Log.w("identifiers","in initiatePurchaseFlow");
            PurchaseModel purchaseModel=new PurchaseModel();
            List<String> subSkuList=new ArrayList<>();
            subSkuList.add(purchase.getSkus().get(0));
            SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
            params.setSkusList(subSkuList).setType(BillingClient.SkuType.SUBS);
            myBillingClient.querySkuDetailsAsync(params.build(),
                    (billingResult, skuDetailsList) -> {
                        if (skuDetailsList != null && !skuDetailsList.isEmpty()) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                purchaseModel.setTitle("" + dataItem.getTitle());
                                purchaseModel.setPurchaseOptions(VodOfferType.RECURRING_SUBSCRIPTION.name());
                                purchaseModel.setIdentifier(dataItem.getIdentifier());
                                purchaseModel.setCurrency(skuDetails.getPriceCurrencyCode());
                            }
                            callCreateNewPurchase(purchaseModel,purchase,dataItem);
                        }else {
                            getPlans=false;
                        }
                    });

        }catch (Exception e){
            Log.w("billingProcess",e.toString());
            getPlans=false;
        }
    }

    private void callCreateNewPurchase(PurchaseModel model, Purchase purchase, DataItem dataItem) {
        try {
            Log.w("identifiers","in callCreateNewPurchase");
            JSONObject jsonObject=new JSONObject();
            JSONObject jsonObject1=new JSONObject();

            try {
                if (model.getPurchaseOptions().contains(VodOfferType.PERPETUAL.name()) || model.getPurchaseOptions().contains(VodOfferType.RENTAL.name())){
                    jsonObject1.put("enveuSMSPlanName", model.getIdentifier());
                    jsonObject1.put("enveuSMSPlanTitle", model.getTitle());
                    jsonObject1.put("enveuSMSOfferType", model.getPurchaseOptions());
                    jsonObject1.put("enveuSMSPurchaseCurrency", model.getCurrency());
                    jsonObject1.put("enveuSMSOfferContentSKU", purchase.getSkus().get(0));
                }else {
                    jsonObject1.put("enveuSMSPlanName", model.getIdentifier());
                    jsonObject1.put("enveuSMSPlanTitle", model.getTitle());
                    jsonObject1.put("enveuSMSSubscriptionOfferType", model.getPurchaseOptions());
                    jsonObject1.put("enveuSMSPurchaseCurrency", model.getCurrency());
                }


                jsonObject.put("notes",jsonObject1);
            } catch (Exception e) {
                Logger.e(e);
            }

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());


            String planName=model.getIdentifier()+"_PLAN/";
            String paymentURL;
            if (SDKConfig.getInstance().getPAYMENT_BASE_URL()!=null && !SDKConfig.getInstance().getPAYMENT_BASE_URL().equalsIgnoreCase("")){
                paymentURL = SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/offer/"+planName;
            }else {
                paymentURL= KsPreferenceKeys.getInstance().getPAYMENT_BASE_URL()+"v2/offer/"+planName;
            }

            APIDetails endpoint = RequestConfig.paymentClient(KsPreferenceKeys.getInstance().getAppPrefAccessToken(),paymentURL).create(APIDetails.class);
            Call<PurchaseResponseModel> call = endpoint.getCreateNewPurchase(gsonObject);
            call.enqueue(new Callback<PurchaseResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
                        purchaseResponseModel.setData(response.body().getData());
                        String orderId = purchaseResponseModel.getData().getOrderId();
                        callInitiatePaymentApi(orderId,purchase,dataItem,model);
                    } else {
                        Log.w("identifiers","in callCreateNewPurchase e");
                        PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().createNewOrder(response);
                        getPlans=false;
                    }

                }

                @Override
                public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                    Log.w("identifiers","in callCreateNewPurchase e2");
                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    purchaseResponseModel.setStatus(false);
                    purchaseResponseModel.setResponseCode(500);
                    purchaseResponseModel.setDebugMessage(OttApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                    getPlans=false;
                }
            });
        }catch (Exception ignored){
            Log.w("identifiers","in callCreateNewPurchase"+" "+ignored.toString());
            getPlans=false;
        }

    }

    private void callInitiatePaymentApi(String orderID, Purchase purchase, DataItem dataItem, PurchaseModel model) {

        try {
            Log.w("identifiers","in callInitiatePaymentApi");
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("paymentProvider","GOOGLE_IAP");
            } catch (Exception e) {
                Logger.e(e);
            }

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());
            String initiateURL;
            if (SDKConfig.getInstance().getPAYMENT_BASE_URL()!=null && !SDKConfig.getInstance().getPAYMENT_BASE_URL().equalsIgnoreCase("")){
                initiateURL = SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/order/"+orderID+"/";
            }else {
                 initiateURL= KsPreferenceKeys.getInstance().getPAYMENT_BASE_URL()+"v2/order/"+orderID+"/";
            }


            APIDetails endpoint = RequestConfig.paymentClient(KsPreferenceKeys.getInstance().getAppPrefAccessToken(),initiateURL).create(APIDetails.class);
            Call<PurchaseResponseModel> call = endpoint.initiatePurchase(gsonObject);
            call.enqueue(new Callback<PurchaseResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
                        purchaseResponseModel.setData(response.body().getData());
                        String paymentId = purchaseResponseModel.getData().getPaymentId().toString();
                        updatePayment(purchase.getPurchaseToken(), paymentId,purchase.getOrderId(),purchase,model,orderID);
                    } else {
                        PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().initiateOrder(response);
                        getPlans=false;
                    }

                }

                @Override
                public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    purchaseResponseModel.setStatus(false);
                    purchaseResponseModel.setResponseCode(500);
                    purchaseResponseModel.setDebugMessage(OttApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                    getPlans=false;
                }
            });
        }catch (Exception ignored){
            getPlans=false;
        }


    }

    private void updatePayment(String purchaseToken, String paymentId, String playstoreOrderId, Purchase purchase, PurchaseModel purchaseModel, String orderId) {
        try {
            Log.w("identifiers","in updatePayment");
            JSONObject jsonObject=new JSONObject();
            JSONObject jsonObject1=new JSONObject();

            try {
                /* "purchasePrice": "20",    "purchaseCurrency": "INR"*/
                if ("PAYMENT_DONE".equalsIgnoreCase("FAILED")){
                    jsonObject1.put("googleIAPPurchaseToken", "Could not connect to the play store");
                    jsonObject1.put("exception", "");
                }else {
                    jsonObject1.put("googleIAPPurchaseToken", purchaseToken);
                    jsonObject1.put("playStoreProductId", Base64.encodeToString(purchase.getSkus().get(0).getBytes(),Base64.NO_WRAP));
                }

                if (purchaseModel!=null){
                    jsonObject1.put("purchasePrice", purchaseModel.getPrice());
                    jsonObject1.put("purchaseCurrency", purchaseModel.getCurrency());
                }else {
                    jsonObject1.put("purchasePrice", "");
                    jsonObject1.put("purchaseCurrency", "");
                }
                jsonObject.put("paymentStatus", "PAYMENT_DONE");
                jsonObject.put("notes",jsonObject1);
            }catch (Exception ignored){
                Log.w("purchasedSKU", "PAYMENT_DONE");
            }

            JsonParser jsonParser = new JsonParser();
            JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());

            String initiateURL;
            if (SDKConfig.getInstance().getPAYMENT_BASE_URL()!=null && !SDKConfig.getInstance().getPAYMENT_BASE_URL().equalsIgnoreCase("")){
                initiateURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/order/"+orderId+"/";
            }else {
                initiateURL= KsPreferenceKeys.getInstance().getPAYMENT_BASE_URL()+"v2/order/"+orderId+"/";
            }

            APIDetails endpoint = RequestConfig.paymentClient(KsPreferenceKeys.getInstance().getAppPrefAccessToken(),initiateURL).create(APIDetails.class);
            Call<PurchaseResponseModel> call = endpoint.updatePurchase(paymentId,gsonObject);
            call.enqueue(new Callback<PurchaseResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    if (response.code() == 200) {
                        Log.w("identifiers","in success");
                        purchaseResponseModel.setStatus(true);
                        purchaseResponseModel.setData(response.body().getData());
                    } else {
                        Log.w("identifiers","in failed");
                        PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().updateOrder(response);
                    }
                    getPlans=false;


                }

                @Override
                public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                    PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                    purchaseResponseModel.setStatus(false);
                    purchaseResponseModel.setResponseCode(500);
                    purchaseResponseModel.setDebugMessage(OttApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                    getPlans=false;
                }
            });
        }catch (Exception ignored){
            getPlans=false;
        }

    }

}
