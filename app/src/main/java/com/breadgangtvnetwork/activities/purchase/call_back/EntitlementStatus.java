package com.breadgangtvnetwork.activities.purchase.call_back;

import com.breadgangtvnetwork.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
public interface EntitlementStatus {
    void entitlementStatus(boolean entitlementStatus,boolean apiStatus,int responseCode);
    default void getPlans(ResponseMembershipAndPlan plans, boolean apiStatus){

    }
}
