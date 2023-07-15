package com.breadgangtvnetwork.activities.purchase.ui.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.breadgangtvnetwork.activities.layers.EntitlementLayer;
import com.breadgangtvnetwork.activities.purchase.purchase_model.PurchaseModel;
import com.breadgangtvnetwork.activities.purchase.purchase_model.PurchaseResponseModel;
import com.breadgangtvnetwork.activities.purchase.repository.PurchaseRepository;
import com.breadgangtvnetwork.beanModel.cancelPurchase.ResponseCancelPurchase;
import com.breadgangtvnetwork.beanModel.entitle.ResponseEntitle;
import com.breadgangtvnetwork.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import com.google.gson.JsonObject;


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
