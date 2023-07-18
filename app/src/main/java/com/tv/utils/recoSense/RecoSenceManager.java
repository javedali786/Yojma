package com.tv.utils.recoSense;

import com.tv.OttApplication;
import com.tv.SDKConfig;
import com.tv.networking.apiendpoints.ApiInterface;
import com.tv.networking.apiendpoints.RequestConfig;
import com.tv.networking.errormodel.ApiErrorModel;
import com.tv.utils.Logger;
import com.tv.utils.commonMethods.AppCommonMethod;
import com.tv.utils.recoSense.bean.RecosenceResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecoSenceManager {

    private static RecoSenceManager recoSenceManagerInstance;
    private static ApiInterface endpoint;
    private RecoSenceManager() {

    }

    public static RecoSenceManager getInstance() {
        if (recoSenceManagerInstance == null) {
            endpoint = RequestConfig.getRecoClickClient().create(ApiInterface.class);
            recoSenceManagerInstance = new RecoSenceManager();
        }
        return (recoSenceManagerInstance);
    }


    public void sendClickEvent(String screenType, int id) {
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("action_type",RecoSenceActionTypes.VIEW);
            jsonObject.put("app_ver","1");
            jsonObject.put("channel",RecoSenceActionTypes.CHANNEL);
            jsonObject.put("client_id","cpcqnahvyzlvclzr4wxa");


            JSONObject jsonObject1=new JSONObject();
            jsonObject1.put("language", SDKConfig.getInstance().getSpanishLangCode());
            jsonObject.put("context",jsonObject1);

            jsonObject.put("device_id", AppCommonMethod.getDeviceId(OttApplication.getInstance().getContentResolver()));
            jsonObject.put("item_id", String.valueOf(id));
            jsonObject.put("timestamp", AppCommonMethod.getCurrentTimeStamp());
            jsonObject.put("user_id", AppCommonMethod.getDeviceId(OttApplication.getInstance().getContentResolver()));


            /*DateFormat df = DateFormat.getTimeInstance();
            df.setTimeZone(TimeZone.getTimeZone("gmt"));
            String gmtTime = df.format(new Date());
*/
            Logger.w("recoObject  ",jsonObject+"");

          //  Logger.w("recoObject---->>",gmtTime+"");
            endpoint.getRecoClick(jsonObject).enqueue(new Callback<RecosenceResponse>() {
                @Override
                public void onResponse(Call<RecosenceResponse> call, Response<RecosenceResponse> response) {
                    Logger.w("configResponse",response+"");

                }

                @Override
                public void onFailure(Call<RecosenceResponse> call, Throwable t) {
                    Logger.w("configResponse--",t.getMessage()+"");
                    ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());

                }
            });


        }catch (Exception e){

        }
    }
}
