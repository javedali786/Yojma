package com.breadgangtvnetwork.activities.purchase.planslayer;

import com.breadgangtvnetwork.activities.purchase.callBack.EntitlementStatus;
import com.breadgangtvnetwork.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
import com.breadgangtvnetwork.networking.apiendpoints.RequestConfig;
import com.breadgangtvnetwork.networking.detailPlayer.APIDetails;
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

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
                public void onResponse(Call<ResponseMembershipAndPlan> call, Response<ResponseMembershipAndPlan> response) {

                    boolean entitlementState=false;
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    if (response.code() == 200) {
                        purchaseResponseModel.setStatus(true);
//                    purchaseResponseModel.setDebugMessage(response.body().getDebugMessage());
                        purchaseResponseModel.setData(response.body().getData());
                        if (purchaseResponseModel.getData().size()>0){
                            for (int i = 0; i < purchaseResponseModel.getData().size(); i++) {
                                if (purchaseResponseModel.getData().get(i).getEntitlementState()) {
                                    entitlementState=true;
                                    preferenceKeys.setEntitlementState(true);
                                    callBack.entitlementStatus(true,true);
                                    break;
                                }else {
                                    entitlementState=false;
                                }
                            }

                            if (!entitlementState){
                                preferenceKeys.setEntitlementState(false);
                                callBack.entitlementStatus(false,false);

                            }
                        }else {
                            preferenceKeys.setEntitlementState(false);
                            callBack.entitlementStatus(false,false);
                        }
                    } else {
                        purchaseResponseModel.setStatus(false);
                        preferenceKeys.setEntitlementState(false);
                        callBack.entitlementStatus(false,false);


                    }

                }

                @Override
                public void onFailure(Call<ResponseMembershipAndPlan> call, Throwable t) {
                    ResponseMembershipAndPlan purchaseResponseModel = new ResponseMembershipAndPlan();
                    purchaseResponseModel.setStatus(false);
                    preferenceKeys.setEntitlementState(false);
                    callBack.entitlementStatus(false,false);


                }
            });

        }catch (Exception e){
            callBack.entitlementStatus(false,false);
            preferenceKeys.setEntitlementState(false);
        }

    }
}
