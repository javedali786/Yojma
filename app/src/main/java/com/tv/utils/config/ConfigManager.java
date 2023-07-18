package com.tv.utils.config;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.enveu.sdk.baseCategoryServices.ENSdkBaseCategoryServices;
import com.enveu.sdk.baseClient.ENSdkBaseClient;
import com.enveu.sdk.baseClient.ENSdkBaseConfiguration;
import com.enveu.sdk.baseClient.ENSdkBaseGateway;
import com.enveu.sdk.baseClient.ENSdkBasePlatform;
import com.enveu.sdk.callBack.ColorConfigEnveuCallBack;
import com.enveu.sdk.callBack.ConfigEnveuCallBack;
import com.enveu.sdk.logging.ENSdkEnveuLogs;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tv.BuildConfig;
import com.tv.R;
import com.tv.SDKConfig;
import com.tv.callbacks.apicallback.ApiResponseModel;
import com.tv.networking.apiendpoints.ApiInterface;
import com.tv.networking.apiendpoints.RequestConfig;
import com.tv.networking.errormodel.ApiErrorModel;
import com.tv.utils.Logger;
import com.tv.utils.commonMethods.AppCommonMethod;
import com.tv.utils.config.bean.dmsResponse.ConfigBean;
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.text.SimpleDateFormat;

public class ConfigManager {
    private static ConfigManager configManagerInstance;
    private static ApiInterface endpoint;
    private static boolean isDmsDataStored = false;
    ConfigBean configBean;
    ApiResponseModel callBack;
    private String API_KEY="";
    private boolean isTablet=false;

    private ConfigManager() {

    }

    public static ConfigManager getInstance() {
        if (configManagerInstance == null) {
            endpoint = RequestConfig.getConfigClient().create(ApiInterface.class);
            configManagerInstance = new ConfigManager();
        }
        return (configManagerInstance);
    }

    public boolean getConfig(Context context,ApiResponseModel listener) {
        this.callBack = listener;
        callBack.onStart();
        boolean isTablet = context.getResources().getBoolean(R.bool.isTablet);
        if (isTablet){
            API_KEY= SDKConfig.API_KEY_TAB;
            isTablet=true;
        }else {
            API_KEY=SDKConfig.API_KEY_MOB;
            isTablet=false;
        }

        ENSdkBaseClient client = new ENSdkBaseClient(ENSdkBaseGateway.ENVEU, SDKConfig.CONFIG_BASE_URL, API_KEY, SDKConfig.CONFIG_VERSION, ENSdkBasePlatform.android.name(), isTablet, getDeviceId(context.getContentResolver()));
        ENSdkBaseConfiguration.Companion.getInstance().clientSetup(client);

        String name= BuildConfig.VERSION_NAME;
        int code= BuildConfig.VERSION_CODE;

        if (  code == 136 ||code == 137 ||code == 138 || code == 139 || code == 140 || code == 141) {
            if (!KsPreferenceKeys.getInstance().getEncryptionUpdate()) {
                KsPreferenceKeys.getInstance().clear();
                KsPreferenceKeys.getInstance().setEncryptionUpdate(true);
            }
        }

        boolean _date = verifyDmsDate(KsPreferenceKeys.getInstance().getString("DMS_Date", "mDate"));
        Logger.w("configResponse", _date + "");
        if (_date) {
            ENSdkBaseCategoryServices.Companion.getInstance().configServiceV1_0(new ConfigEnveuCallBack() {

                @Override
                public void success(boolean status, @Nullable Object configResponse) {
                    if (status) {
                        if (configResponse!=null){
                         //   ENSdkEnveuLogs.INSTANCE.printWarning(Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(configResponse.getData()).getAppConfig()).getBaseUrl()));
                            Gson gson = new Gson();
                            String json = gson.toJson(configResponse);
                            Logger.w("configResponse str", json);
                            KsPreferenceKeys.getInstance().setString("DMS_Response", json);
                            KsPreferenceKeys.getInstance().setString("DMS_Date", "" + System.currentTimeMillis());
                            ConfigBean config=gson.fromJson(json,ConfigBean.class);
                            KsPreferenceKeys.getInstance().setSUBSCRIPTION_BASE_URL(config == null ? "" : config.getData().getAppConfig().getSubscriptionBaseUrl());
                            KsPreferenceKeys.getInstance().setPAYMENT_BASE_URL(config == null ? "" : config.getData().getAppConfig().getPaymentBaseUrl());
                            assert config != null;
                            Logger.w("configResponse str2", config.getData().getAppConfig().getNavScreens().get(0).getScreenName()+ "");
                            ENSdkBaseCategoryServices.Companion.getInstance().colorConfigService(2,2,context,new ColorConfigEnveuCallBack() {
                                @Override
                                public void colorConfigSuccess(boolean status, @Nullable JsonObject configBean) {
                                    Logger.w("colorConfigResponse str1", configBean+ "");
                                    assert configBean != null;
                                    //ThemeHandler.getInstance().setThemeObject(configBean);
                                }

                                @Override
                                public void colorConfigfailure(boolean status, int errorCode, @NonNull String message) {
                                    Logger.w("colorConfigResponse str2", status+ "");
                                }
                            });
                            callBack.onSuccess(config.getData());
                        }
                        }
                        else {
                        ENSdkEnveuLogs.INSTANCE.printWarning("errorOccurred f");

                        isDmsDataStored = checkPreviousDmsResponse();
                        if (isDmsDataStored) {
                            KsPreferenceKeys.getInstance().setString("DMS_Date", "" + System.currentTimeMillis());
                           // ThemeHandler.getInstance().checkColorConfig();
                            callBack.onSuccess(configBean);
                        } else {
                            ApiErrorModel errorModel = new ApiErrorModel(400, "");
                            callBack.onError(errorModel);
                            Logger.d("redirectionss inthree");
                        }
                    }
                }

                @Override
                public void failure(boolean status, int errorCode, String errorMessage) {
                    ENSdkEnveuLogs.INSTANCE.printWarning("errorOccurred ff");

                    isDmsDataStored = checkPreviousDmsResponse();
                    if (isDmsDataStored) {
                        KsPreferenceKeys.getInstance().setString("DMS_Date", "" + System.currentTimeMillis());
                        callBack.onSuccess(configBean);
                    } else {
                        ApiErrorModel errorModel = new ApiErrorModel(400, "");
                        callBack.onError(errorModel);
                        Logger.d("redirectionss inthree");
                    }
                }
            });


/*
            endpoint.getConfig(SDKConfig.CONFIG_VERSION).enqueue(new Callback<ConfigBean>() {
                @Override
                public void onResponse(Call<ConfigBean> call, Response<ConfigBean> response) {
                   // Logger.w("configResponse", response + "");
                    if (response.isSuccessful()) {

                       // Logger.e("configResponse", response.body().getData() + "");

                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                       // Logger.e("configResponse", json + "");
                        KsPreferenceKeys.getInstance().setString("DMS_Response", json);
                        KsPreferenceKeys.getInstance().setString("DMS_Date", "" + System.currentTimeMillis());
                        callBack.onSuccess(response.body());

                    } else {

                        isDmsDataStored = checkPreviousDmsResponse();
                        if (isDmsDataStored) {
                            KsPreferenceKeys.getInstance().setString("DMS_Date", "" + System.currentTimeMillis());
                            callBack.onSuccess(configBean);
                        } else {
                            ApiErrorModel errorModel = new ApiErrorModel(response.code(), response.message());
                            callBack.onError(errorModel);
                            Logger.d("redirectionss inthree");
                        }


                    }

                }

                @Override
                public void onFailure(Call<ConfigBean> call, Throwable t) {
                    Logger.w("configResponse--Error", t.getMessage() + "");
                    isDmsDataStored = checkPreviousDmsResponse();
                    if (isDmsDataStored) {
                        KsPreferenceKeys.getInstance().setString("DMS_Date", "" + System.currentTimeMillis());
                        callBack.onSuccess(configBean);
                    } else {
                        ApiErrorModel errorModel = new ApiErrorModel(500, t.getMessage());
                        callBack.onFailure(errorModel);
                    }
                }
            });
*/
        } else {
            Logger.d("redirectionss intwo");
           // ThemeHandler.getInstance().checkColorConfig();
            callBack.onSuccess(configBean);
        }

        return false;
    }

    private boolean checkPreviousDmsResponse() {
        return AppCommonMethod.getConfigResponse() != null;
    }

    private boolean verifyDmsDate(String storedDate) {
        boolean verifyDms = false;

        if (storedDate == null || storedDate.equalsIgnoreCase("mDate")) {
            verifyDms = true;
            return verifyDms;
        }

        String currentDate = getDateTimeStamp(System.currentTimeMillis());
        String temp = getDateTimeStamp(Long.parseLong(storedDate));
        verifyDms = !currentDate.equalsIgnoreCase(temp);

        return verifyDms;
    }

    private String getDateTimeStamp(Long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(timeStamp);
    }

    public static String getDeviceId(ContentResolver contentResolver) {
        return Settings.Secure.getString(contentResolver,
                Settings.Secure.ANDROID_ID);
    }

}
