package com.tv.activities.purchase.call_back;

import com.tv.beanModel.membershipAndPlan.ResponseMembershipAndPlan;
public interface EntitlementStatus {
    void entitlementStatus(boolean entitlementStatus,boolean apiStatus,int responseCode);
    default void getPlans(ResponseMembershipAndPlan plans, boolean apiStatus){

    }
}
