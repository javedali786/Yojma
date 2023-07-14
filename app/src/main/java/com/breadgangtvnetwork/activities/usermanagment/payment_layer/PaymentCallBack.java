package com.breadgangtvnetwork.activities.usermanagment.payment_layer;

import com.breadgangtvnetwork.activities.purchase.purchase_model.PurchaseResponseModel;

public interface PaymentCallBack {

     default void createOrderResponse(PurchaseResponseModel response, boolean status){

     }
     default void initiateOrderResponse(PurchaseResponseModel response,boolean status){

     }

     default void updateOrderResponse(PurchaseResponseModel response,boolean status){

     }
}
