package com.tv.uscreen.yojmatv.utils.helpers;

import android.app.Activity;

import com.tv.uscreen.yojmatv.OttApplication;
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.DialogInterface;
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.VersionUpdateCallBack;
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.VersionValidator;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse.ConfigBean;
import com.tv.uscreen.yojmatv.utils.config.bean.dmsResponse.Version;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

public class ForceUpdateHandler {
    final Activity activity;
    final KsPreferenceKeys session;
    final MaterialDialog materialDialog;
    VersionValidator versionValidator;
    VersionUpdateCallBack versionUpdateCallBack;
    ConfigBean configBean;
    public static String FORCE="force";
    public static String RECOMMENDED="recommended";


    public ForceUpdateHandler(Activity context, ConfigBean configBean) {
        this.activity = context;
        session = KsPreferenceKeys.getInstance();
        materialDialog = new MaterialDialog(activity);
        this.configBean=configBean;
    }

    public void checkCurrentVersion(VersionValidator callBack) {
       // checkPlaystoreVersion(currentVersion, callBack);
        versionValidator = callBack;
        checkVersion(configBean);
    }

    OttApplication application;
    private void checkVersion(ConfigBean configBean) {
        if (configBean!=null){
           application= ((OttApplication) activity.getApplication());
           //configBean.getData().getAppConfig().getVersion().setForceUpdate(false);
               if(configBean.getData().getAppConfig()!=null){
                   Version version=configBean.getData().getAppConfig().getVersion();
                   if (version.isForceUpdate()){
                       String appversion = application.getVersionName().replace(".", "");
                       int appCurrentVersion = Integer.parseInt(appversion);
                       String configVersion= version.getUpdatedVersion();

                       if (!configVersion.equalsIgnoreCase("")) {
                           if (configVersion.contains(".")) {
                               configVersion = configVersion.replace(".", "");
                               if (!configVersion.equalsIgnoreCase("")) {
                                   int configAppCurrentVersion = Integer.parseInt(configVersion);
                                   versionValidator.version(
                                           appCurrentVersion < configAppCurrentVersion, appCurrentVersion, configAppCurrentVersion,FORCE);
                               }else {
                                   versionValidator.version(false, appCurrentVersion, 0,FORCE);
                               }
                           }
                           else {
                               versionValidator.version(false, appCurrentVersion, 0,FORCE);
                           }
                       }else {
                           versionValidator.version(false, appCurrentVersion, 0,FORCE);
                       }

                   }
                   else if (version.isRecommendedUpdate()){
                       String appversion = application.getVersionName().replace(".", "");
                       int appCurrentVersion = Integer.parseInt(appversion);
                       String configVersion= version.getUpdatedVersion();
                       if (!configVersion.equalsIgnoreCase("")) {
                           if (configVersion.contains(".")) {
                               configVersion = configVersion.replace(".", "");
                               if (!configVersion.equalsIgnoreCase("")) {
                                   int configAppCurrentVersion = Integer.parseInt(configVersion);
                                   versionValidator.version(
                                           appCurrentVersion < configAppCurrentVersion, appCurrentVersion, configAppCurrentVersion, RECOMMENDED);
                               } else {
                                   versionValidator.version(false, appCurrentVersion, 0, RECOMMENDED);
                               }
                           } else {
                               versionValidator.version(false, appCurrentVersion, 0, RECOMMENDED);
                           }
                       }
                   }else {
                       versionValidator.version(false, 0, 0, RECOMMENDED);
                   }
               }
               else {

               }



        }
    }

/*
    private void checkPlaystoreVersion(final int currentVersion, final VersionValidator callBack) {
        versionValidator = callBack;
        ApiInterface endpoint = RequestConfig.getClient().create(ApiInterface.class);


        Call<ResponseConfig> call = endpoint.getConfiguration("true");
        call.enqueue(new Callback<ResponseConfig>() {
            @Override
            public void onResponse(@NonNull Call<ResponseConfig> call, @NonNull Response<ResponseConfig> response) {
                if (response.body() != null) {
                    AppCommonMethod.urlPoints = response.body().getData().getImageTransformationEndpoint();
                    ResponseConfig cl = response.body();
                    KsPreferenceKeys ksPreferenceKeys = KsPreferenceKeys.getInstance();
                    Gson gson = new Gson();
                    String json = gson.toJson(cl);


                    AppCommonMethod.urlPoints = */
/*AppConstants.PROFILE_URL +*//*
 response.body().getData().getImageTransformationEndpoint();

                    ksPreferenceKeys.setAppPrefLastConfigHit(String.valueOf(System.currentTimeMillis()));
                    ksPreferenceKeys.setAppPrefConfigResponse(json);
                    ksPreferenceKeys.setAppPrefVideoUrl(response.body().getData().getCloudFrontVideoEndpoint());
                    ksPreferenceKeys.setAppPrefAvailableVersion(response.body().getData().getUpdateInfo().getAvailableVersion());
                    ksPreferenceKeys.setAppPrefCfep(AppCommonMethod.urlPoints);
                    ksPreferenceKeys.setAppPrefConfigVersion(String.valueOf(response.body().getData().getConfigVersion()));
                    ksPreferenceKeys.setAppPrefServerBaseUrl(response.body().getData().getServerBaseURL());
                  //  versionValidator.version(false, currentVersion, currentVersion);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseConfig> call, @NonNull Throwable t) {

            }
        });
    }
*/

    public void hideDialog() {
        materialDialog.hide();
    }

    public void typeHandle(String type,VersionUpdateCallBack callBack) {
        versionUpdateCallBack = callBack;
        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("spanish") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी") ){
            AppCommonMethod.updateLanguage("es", OttApplication.Companion.getInstance());
        } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")){
            AppCommonMethod.updateLanguage("en", OttApplication.Companion.getInstance());
        }
        materialDialog.showDialog(type, "", activity, new DialogInterface() {
            @Override
            public void positiveAction() {
                versionUpdateCallBack.selection(false);
            }

            @Override
            public void negativeAction() {
                versionUpdateCallBack.selection(true);
            }
        });

    }
}
