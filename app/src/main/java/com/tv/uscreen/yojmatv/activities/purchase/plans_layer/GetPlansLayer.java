package com.tv.uscreen.yojmatv.activities.purchase.plans_layer;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tv.uscreen.yojmatv.activities.purchase.call_back.EntitlementStatus;
import com.tv.uscreen.yojmatv.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import com.tv.uscreen.yojmatv.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.yojmatv.networking.detailPlayer.APIDetails;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetPlansLayer {

    private static GetPlansLayer plansLayerInstance;

    private GetPlansLayer() {

    }

    public static GetPlansLayer getInstance() {
        if (plansLayerInstance == null) {
            plansLayerInstance = new GetPlansLayer();
        }
        return (plansLayerInstance);
    }

    public void getEntitlementStatus(KsPreferenceKeys preferenceKeys, String token, EntitlementStatus callBack) {
        try {

            APIDetails endpoint = RequestConfig.getUserInteration(token).create(APIDetails.class);
            Call<ResponseMembershipAndPlan> call = endpoint.getPlans("RECURRING_SUBSCRIPTION",true);
            call.enqueue(new Callback<ResponseMembershipAndPlan>() {
                @Override
                public void onResponse(@NonNull Call<ResponseMembershipAndPlan> call, @NonNull Response<ResponseMembershipAndPlan> response) {
                    boolean entitlementState=false;
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
                        purchaseResponseModel.setData(response.body().getData());
                        if (!purchaseResponseModel.getData().isEmpty()){
                            for (int i = 0; i < purchaseResponseModel.getData().size(); i++) {
                                if (purchaseResponseModel.getData().get(i).getEntitlementState()) {
                                    entitlementState=true;
                                    preferenceKeys.setEntitlementState(true);
                                    callBack.entitlementStatus(true,true,purchaseResponseModel.getData().get(i).getOfferStatus(),0);
                                    break;
                                }else {
                                    entitlementState=false;
                                }
                            }

                            if (!entitlementState){
                                preferenceKeys.setEntitlementState(false);
                                callBack.entitlementStatus(false,true,"false",100);

                            }
                        }else {
                            preferenceKeys.setEntitlementState(false);
                            callBack.entitlementStatus(false,false,"false",0);
                        }
                    } else if(response.code() == 403){
                        preferenceKeys.setEntitlementState(false);
                        callBack.entitlementStatus(false,false,"false",403);
                    }else {
                        purchaseResponseModel.setStatus(false);
                        preferenceKeys.setEntitlementState(false);
                        callBack.entitlementStatus(false,false,"false",0);


                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResponseMembershipAndPlan> call, @NonNull Throwable t) {
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    purchaseResponseModel.setStatus(false);
                    preferenceKeys.setEntitlementState(false);
                    callBack.entitlementStatus(false,false,"false",0);


                }
            });

        }catch (Exception e){
            callBack.entitlementStatus(false,false,"false",0);
            preferenceKeys.setEntitlementState(false);
        }

    }

    public void getPlansDetail(String token, EntitlementStatus callBack) {
        try {

            APIDetails endpoint = RequestConfig.getUserInteration(token).create(APIDetails.class);
            Call<ResponseMembershipAndPlan> call = endpoint.getPlans("RECURRING_SUBSCRIPTION",true);
            call.enqueue(new Callback<ResponseMembershipAndPlan>() {
                @Override
                public void onResponse(@NonNull Call<ResponseMembershipAndPlan> call, @NonNull Response<ResponseMembershipAndPlan> response) {
                    boolean entitlementState=false;
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
                        if (response.body() != null) {
                            Gson json = new Gson();
                            String jsonStr = json.toJson(response.body().getData());
                            Log.d("planResponse", "onResponse: "+jsonStr);
                            purchaseResponseModel.setData(response.body().getData());
                        }
                        if (!purchaseResponseModel.getData().isEmpty()){
                            callBack.getPlans(purchaseResponseModel,true);
                        }else {
                            callBack.getPlans(null,false);
                        }
                    } else {
                        purchaseResponseModel.setStatus(false);
                        callBack.getPlans(null,false);
                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResponseMembershipAndPlan> call, @NonNull Throwable t) {
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    purchaseResponseModel.setStatus(false);
                    callBack.getPlans(null,false);


                }
            });

        }catch (Exception e){
            ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
            purchaseResponseModel.setStatus(false);
            callBack.getPlans(null,false);
        }

    }

}
