package com.tv.uscreen.yojmatv.activities.usermanagment.payment_layer;

import com.tv.uscreen.yojmatv.activities.purchase.purchase_model.PurchaseResponseModel;

public interface PaymentCallBack {

     default void createOrderResponse(PurchaseResponseModel response, boolean status){

     }
     default void initiateOrderResponse(PurchaseResponseModel response,boolean status){

     }

     default void updateOrderResponse(PurchaseResponseModel response,boolean status){

     }
}
