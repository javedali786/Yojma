package com.tv.uscreen.yojmatv.activities.usermanagment.payment_layer;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.tv.uscreen.yojmatv.OttApplication;

import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.SDKConfig;
import com.tv.uscreen.yojmatv.activities.purchase.purchase_model.PurchaseModel;
import com.tv.uscreen.yojmatv.activities.purchase.purchase_model.PurchaseResponseModel;
import com.tv.uscreen.yojmatv.activities.purchase.ui.VodOfferType;
import com.tv.uscreen.yojmatv.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.yojmatv.networking.detailPlayer.APIDetails;
import com.tv.uscreen.yojmatv.networking.intercepter.ErrorCodesIntercepter;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentCallsLayer {

    private static PaymentCallsLayer paymentCallsLayer;

    public synchronized static PaymentCallsLayer getInstance() {
        if (paymentCallsLayer == null) {
            paymentCallsLayer = new PaymentCallsLayer();
        }
        return paymentCallsLayer;
    }

    PaymentCallBack paymentCallBack;
    public void createOrder(String token, PurchaseModel model, PaymentCallBack callBack) {
        paymentCallBack=callBack;
        JSONObject jsonObject=new JSONObject();
        JSONObject jsonObject1=new JSONObject();

        Log.w("PlanValues",model.getIdentifier()+" "+
                model.getTitle()+" "+
                model.getPurchaseOptions()+" "+
                model.getCurrency());

       if (paymentCallBack!=null){
       }

        try {
            if (model.getPurchaseOptions().contains(VodOfferType.PERPETUAL.name()) || model.getPurchaseOptions().contains(VodOfferType.RENTAL.name())){
                jsonObject1.put("enveuSMSPlanName", model.getIdentifier());
                jsonObject1.put("enveuSMSPlanTitle", model.getTitle());
                jsonObject1.put("enveuSMSOfferType", model.getPurchaseOptions());
                jsonObject1.put("enveuSMSPurchaseCurrency", model.getCurrency());
                jsonObject1.put("enveuSMSOfferContentSKU", model.getIdentifier());
            }else {
                jsonObject1.put("enveuSMSPlanName", model.getIdentifier());
                jsonObject1.put("enveuSMSPlanTitle", model.getTitle());
                jsonObject1.put("enveuSMSSubscriptionOfferType", model.getPurchaseOptions());
                jsonObject1.put("enveuSMSPurchaseCurrency",  model.getCurrency());
                jsonObject1.put("enveuSMSOfferContentSKU", model.getIdentifier());

            }


            jsonObject.put("notes",jsonObject1);
        }catch (Exception ignored){

        }

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());


        String planName=model.getIdentifier()+"_PLAN/";
        String paymentURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/offer/"+planName;
        APIDetails endpoint = RequestConfig.paymentClient(token,paymentURL).create(APIDetails.class);
        Call<PurchaseResponseModel> call = endpoint.getCreateNewPurchase(gsonObject);
        call.enqueue(new Callback<PurchaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                if (response.code() == 200) {
                    purchaseResponseModel.setStatus(true);
                    purchaseResponseModel.setData(response.body().getData());
                    paymentCallBack.createOrderResponse(purchaseResponseModel,true);

                } else {

                    PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().createNewOrder(response);
                    paymentCallBack.createOrderResponse(purchaseResponseModel2,false);

                }


            }

            @Override
            public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                purchaseResponseModel.setStatus(false);
                purchaseResponseModel.setResponseCode(500);
                purchaseResponseModel.setDebugMessage(OttApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                paymentCallBack.createOrderResponse(purchaseResponseModel,false);
            }
        });

    }

    public void callInitiatePayment(String token, String orderID,PaymentCallBack callBack) {

        JSONObject jsonObject=new JSONObject();


        try {
            jsonObject.put("paymentProvider","GOOGLE_IAP");
        }catch (Exception ignored){

        }

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());

        String initiateURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/order/"+orderID+"/";

        APIDetails endpoint = RequestConfig.paymentClient(token,initiateURL).create(APIDetails.class);
        Call<PurchaseResponseModel> call = endpoint.initiatePurchase(gsonObject);
        call.enqueue(new Callback<PurchaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                if (response.code() == 200) {
                    purchaseResponseModel.setStatus(true);
                    purchaseResponseModel.setData(response.body().getData());
                    callBack.initiateOrderResponse(purchaseResponseModel,true);
                } else {
                    PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().initiateOrder(response);
                    callBack.initiateOrderResponse(purchaseResponseModel2,false);
                }


            }

            @Override
            public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                purchaseResponseModel.setStatus(false);
                purchaseResponseModel.setResponseCode(500);
                purchaseResponseModel.setDebugMessage(OttApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                callBack.initiateOrderResponse(purchaseResponseModel,false);
            }
        });

    }

    public void updatePurchase(String billingError,String paymentStatus,String token,String purchaseToken,String paymentId,
                                                          String orderId,PurchaseModel purchaseModel,String purchasedSKU,PaymentCallBack callBack) {


        JSONObject jsonObject=new JSONObject();
        JSONObject jsonObject1=new JSONObject();

        try {
            /* "purchasePrice": "20",    "purchaseCurrency": "INR"*/
            if (paymentStatus.equalsIgnoreCase("FAILED")){
                jsonObject1.put("googleIAPPurchaseToken", "Could not connect to the play store");
                jsonObject1.put("exception",billingError);
            }else {
                jsonObject1.put("googleIAPPurchaseToken", purchaseToken);
                jsonObject1.put("playStoreProductId",purchasedSKU);
            }

            if (purchaseModel!=null){
                jsonObject1.put("purchasePrice", purchaseModel.getPrice());
                jsonObject1.put("purchaseCurrency", purchaseModel.getCurrency());
            }else {
                jsonObject1.put("purchasePrice", "");
                jsonObject1.put("purchaseCurrency", "");
            }
            jsonObject.put("paymentStatus", paymentStatus);
            jsonObject.put("notes",jsonObject1);
        }catch (Exception ignored){

        }

        JsonParser jsonParser = new JsonParser();
        JsonObject gsonObject = (JsonObject)jsonParser.parse(jsonObject.toString());


        String initiateURL= SDKConfig.getInstance().getPAYMENT_BASE_URL()+"v2/order/"+orderId+"/";

        APIDetails endpoint = RequestConfig.paymentClient(token,initiateURL).create(APIDetails.class);
        Call<PurchaseResponseModel> call = endpoint.updatePurchase(paymentId,gsonObject);
        call.enqueue(new Callback<PurchaseResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<PurchaseResponseModel> call, @NonNull Response<PurchaseResponseModel> response) {

                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                if (response.code() == 200) {
                    purchaseResponseModel.setStatus(true);
                    purchaseResponseModel.setData(response.body().getData());
                    callBack.updateOrderResponse(purchaseResponseModel,true);
                } else {
                    PurchaseResponseModel purchaseResponseModel2 = ErrorCodesIntercepter.getInstance().updateOrder(response);
                    callBack.updateOrderResponse(purchaseResponseModel2,false);
                }


            }

            @Override
            public void onFailure(@NonNull Call<PurchaseResponseModel> call, @NonNull Throwable t) {
                PurchaseResponseModel purchaseResponseModel = new PurchaseResponseModel();
                purchaseResponseModel.setStatus(false);
                purchaseResponseModel.setResponseCode(500);
                purchaseResponseModel.setDebugMessage(OttApplication.getInstance().getResources().getString(R.string.something_went_wrong_at_our_end));
                callBack.updateOrderResponse(purchaseResponseModel,false);
            }
        });
    }


}
