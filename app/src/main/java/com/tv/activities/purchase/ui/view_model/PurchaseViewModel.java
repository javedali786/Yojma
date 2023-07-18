package com.tv.activities.purchase.ui.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.JsonObject;
import com.tv.activities.layers.EntitlementLayer;
import com.tv.activities.purchase.purchase_model.PurchaseModel;
import com.tv.activities.purchase.purchase_model.PurchaseResponseModel;
import com.tv.activities.purchase.repository.PurchaseRepository;
import com.tv.beanModel.cancelPurchase.ResponseCancelPurchase;
import com.tv.beanModel.entitle.ResponseEntitle;
import com.tv.beanModel.membershipAndPlan.ResponseMembershipAndPlan;


public class PurchaseViewModel extends AndroidViewModel {

    public PurchaseViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<PurchaseResponseModel> createNewPurchaseRequest(String token, JsonObject data, PurchaseModel model, String sku) {
        return PurchaseRepository.getInstance().createNewPurchaseRequest(token, data,model,sku);
    }

    public LiveData<PurchaseResponseModel> callInitiatePaymet(String token, String orderId) {
        return PurchaseRepository.getInstance().callInitiatePaymet(token, orderId);
    }


    public LiveData<ResponseMembershipAndPlan> getPlans(String token) {
        return PurchaseRepository.getInstance().getPlans(token);
    }

    public LiveData<ResponseMembershipAndPlan> getNewPlans(String token) {
        return PurchaseRepository.getInstance().getNewPlans(token);
    }

    public LiveData<ResponseCancelPurchase> cancelPlan(String token) {
        return PurchaseRepository.getInstance().cancelPlan(token);
    }

    public LiveData<PurchaseResponseModel> updatePurchase(String billingError,String paymentStatus,String token,String purchaseToken,String paymentId,String orderId,PurchaseModel purchaseModel,String purchasedSKU) {
        return PurchaseRepository.getInstance().updatePurchase(billingError,paymentStatus,token,purchaseToken,paymentId,orderId,purchaseModel,purchasedSKU);
    }

    public LiveData<ResponseEntitle> hitApiEntitlement(String token, String sku) {

        return EntitlementLayer.getInstance().hitApiEntitlement(token, sku);
    }



}
