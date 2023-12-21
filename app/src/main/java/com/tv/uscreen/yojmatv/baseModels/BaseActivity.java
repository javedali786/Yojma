package com.tv.uscreen.yojmatv.baseModels;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.enveu.client.baseClient.BaseClient;
import com.enveu.client.baseClient.BaseConfiguration;
import com.enveu.client.baseClient.BaseDeviceType;
import com.enveu.client.baseClient.BaseGateway;
import com.enveu.client.baseClient.BasePlatform;
import com.enveu.client.baseCollection.baseCategoryServices.BaseCategoryServices;
import com.enveu.client.userManagement.callBacks.LogoutCallBack;
import com.enveu.client.utils.ClickHandler;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.gson.JsonObject;
import com.tv.uscreen.yojmatv.BuildConfig;


import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.SDKConfig;
import com.tv.uscreen.yojmatv.activities.detail.ui.DetailActivity;
import com.tv.uscreen.yojmatv.activities.detail.ui.EpisodeActivity;
import com.tv.uscreen.yojmatv.activities.homeactivity.ui.HomeActivity;
import com.tv.uscreen.yojmatv.activities.live.LiveActivity;
import com.tv.uscreen.yojmatv.activities.series.ui.SeriesDetailActivity;
import com.tv.uscreen.yojmatv.activities.splash.ui.ActivitySplash;
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.uscreen.yojmatv.utils.BaseActivityAlertDialog;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.MediaTypeConstants;
import com.tv.uscreen.yojmatv.utils.ObjectHelper;
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper;
import com.tv.uscreen.yojmatv.utils.colorsJson.model.ColorsModel;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;
import com.tv.uscreen.yojmatv.utils.helpers.SharedPrefHelper;
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils;
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;
import com.tv.uscreen.yojmatv.utils.inAppUpdate.ApplicationUpdateManager;
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper;
import com.tv.uscreen.yojmatv.utils.stringsJson.model.StringsData;

import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Response;

public class BaseActivity extends AppCompatActivity implements BaseActivityAlertDialog.AlertDialogListener {
    private static final int PERMISSION_REQUEST_CODE = 1;

    boolean tabletSize;
    protected String currentLanguage = "";
    private String strCurrentTheme = "";
    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void openSoftKeyboard(View view) {
        view.requestFocus();
        view.postDelayed(() -> {
            InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.showSoftInput(view, 0);
        }, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public void hitApiLogout(Context context, String token) {

        String isFacebook = KsPreferenceKeys.getInstance().getAppPrefLoginType();

        if (isFacebook.equalsIgnoreCase(AppConstants.UserLoginType.FbLogin.toString())) {
            LoginManager.getInstance().logOut();
        }
        BaseCategoryServices.Companion.getInstance().logoutService(token, new LogoutCallBack() {
            @Override
            public void failure(boolean status, int errorCode, String message) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, 500);
            }
            @Override
            public void success(boolean status, Response<JsonObject> response) {
                if (status) {
                    try {
                        if (response.code() == 404) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                        }
                        if (response.code() == 403) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());

                        } else if (response.code() == 200) {
                            Objects.requireNonNull(response.body()).addProperty(AppConstants.API_RESPONSE_CODE, response.code());

                        } else if (response.code() == 401) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());

                        } else if (response.code() == 500) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());

                        }
                    } catch (Exception e) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());

                    }

                }
            }
        });


    }

    public void clearCredientials(KsPreferenceKeys preference) {
        try {
            String json = KsPreferenceKeys.getInstance().getString("DMS_Response", "");
            String isFacebook = preference.getAppPrefLoginType();
            if (isFacebook.equalsIgnoreCase(AppConstants.UserLoginType.FbLogin.toString())) {
                LoginManager.getInstance().logOut();
            }
            String strCurrentTheme = KsPreferenceKeys.getInstance().getCurrentTheme();
            boolean encrypt = preference.getEncryptionUpdate();
            String strCurrentLanguage = KsPreferenceKeys.getInstance().getAppLanguage();
            String strSubscriptionURL = KsPreferenceKeys.getInstance().getSUBSCRIPTION_BASE_URL();
            String strPaymentURL = KsPreferenceKeys.getInstance().getPAYMENT_BASE_URL();
            boolean isBingeWatchEnable = KsPreferenceKeys.getInstance().getBingeWatchEnable();
            preference.setAppPrefRegisterStatus(AppConstants.UserStatus.Logout.toString());
            ColorsModel colorsModel = ColorsHelper.INSTANCE.loadDataFromJson();
            StringsData stringsHelper = StringsHelper.INSTANCE.loadDataFromJson();
            preference.clear();
            SharedPrefHelper.getInstance().setColorJson(colorsModel);
            SharedPrefHelper.getInstance().setStringJson(stringsHelper);
            preference.setEncryptionUpdate(encrypt);
            KsPreferenceKeys.getInstance().setString("DMS_Response", json);
            KsPreferenceKeys.getInstance().setfirstTimeUserForKidsPIn(false);

            KsPreferenceKeys.getInstance().setSUBSCRIPTION_BASE_URL(strSubscriptionURL);
            KsPreferenceKeys.getInstance().setPAYMENT_BASE_URL(strPaymentURL);

            KsPreferenceKeys.getInstance().setCurrentTheme(strCurrentTheme);
            KsPreferenceKeys.getInstance().setAppLanguage(strCurrentLanguage);
            AppCommonMethod.updateLanguage(strCurrentLanguage, this);
            KsPreferenceKeys.getInstance().setBingeWatchEnable(isBingeWatchEnable);
            String preferenceProfileId = AppCommonMethod.getConfigResponse().getData().getAppConfig().getContentPreferences().getBoth().getId();
            if(StringUtils.isNullOrEmpty(preferenceProfileId)){
                KsPreferenceKeys.getInstance().setPreferenceProfileId(preferenceProfileId);
            }else {
                KsPreferenceKeys.getInstance().setPreferenceProfileId("both01");
            }
        } catch (Exception e) {
            Logger.w(e);
        }
    }

    public void showHideProgress(ProgressBar progressBar) {
        showLoading(progressBar, false);
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissLoading(progressBar);
            }
        }, 3000);

    }

    protected void showLoading(ProgressBar progressBar, boolean val) {
        if (val) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.bringToFront();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    public void dismissLoading(ProgressBar progressBar) {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            // checkAutoRotation();
        } else {
            if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        strCurrentTheme = KsPreferenceKeys.getInstance().getCurrentTheme();
        if (KsPreferenceKeys.getInstance().getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
            setTheme(R.style.MyMaterialTheme_Base_Light);
        } else {
            setTheme(R.style.MyMaterialTheme_Base_Dark);
        }
        currentLanguage = KsPreferenceKeys.getInstance().getAppLanguage();
        updateLanguage(currentLanguage);


    }

    public void branchRedirections(JSONObject jsonObject) {
        try {
            Logger.d("branchRedirections: " + jsonObject);
            if (jsonObject != null && jsonObject.has("contentType") && jsonObject.has("id")) {
                int assetId = 0;
                String contentType = jsonObject.getString("contentType");
                String id = jsonObject.optString("id");
                if (!id.equalsIgnoreCase("")) {
                    assetId = Integer.parseInt(id);
                    if (contentType.equalsIgnoreCase(
                            MediaTypeConstants.getInstance().getSeries())) {
                        if (ClickHandler.disallowClick()) {
                            return;
                        }
                        ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                        ActivityLauncher.getInstance().seriesDetailScreen(
                                BaseActivity.this, SeriesDetailActivity.class, assetId);
                        finish();
                    } else if (contentType.equalsIgnoreCase(AppConstants.ContentType.VIDEO.name())
                            || contentType.equalsIgnoreCase(
                            MediaTypeConstants.getInstance().getMovie())) {
                        if (ClickHandler.disallowClick()) {
                            return;
                        }
                        ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                        ActivityLauncher.getInstance().detailScreen(BaseActivity.this,
                                DetailActivity.class, assetId, "0", false);
                        finish();
                    } else if (contentType.equalsIgnoreCase(
                            MediaTypeConstants.getInstance().getShow())) {
                        if (ClickHandler.disallowClick()) {
                            return;
                        }
                        ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                        ActivityLauncher.getInstance().detailScreen(BaseActivity.this,
                                DetailActivity.class, assetId, "0", false);
                        finish();
                    } else if (contentType.equalsIgnoreCase(
                            MediaTypeConstants.getInstance().getLive())) {
                        if (ClickHandler.disallowClick()) {
                            return;
                        }
                        ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                        ActivityLauncher.getInstance().liveScreenBrightCove(
                                BaseActivity.this, LiveActivity.class, 0l, assetId, "0", false,
                                SDKConfig.getInstance().getLiveDetailId());
                        finish();
                    } else if (contentType.equalsIgnoreCase("CUSTOM") || contentType.equalsIgnoreCase("EXPEDITION")) {
                        if (ClickHandler.disallowClick()) {
                            return;
                        }
                        ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
//                        ActivityLauncher.getInstance().goToExpeditionDetail(
//                                BaseActivity.this, ExpeditionDetail.class, assetId, true);
//                        finish();
                    } else if (contentType.equalsIgnoreCase(
                            MediaTypeConstants.getInstance().getEpisode())) {
                        if (ClickHandler.disallowClick()) {
                            return;
                        }
                        ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                        ActivityLauncher.getInstance().episodeScreen(BaseActivity.this,
                                EpisodeActivity.class, assetId, "0", false);
                        finish();
                    } else if (contentType.equalsIgnoreCase(
                            AppConstants.ContentType.ARTICLE.toString())) {
                        if (ClickHandler.disallowClick()) {
                            return;
                        }
                        ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                        finish();
                    }else {
                        ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                        finish();
                    }
                } else {
                    ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                    finish();
                }
            }else {
               try {
                   if (jsonObject != null && jsonObject.has("mediaType") && jsonObject.has("id")) {
                       int assetId = 0;
                       String contentType = jsonObject.getString("mediaType");
                       String id = jsonObject.optString("id");
                       if (!id.equalsIgnoreCase("")) {
                           assetId = Integer.parseInt(id);
                           if (contentType.equalsIgnoreCase(
                                   MediaTypeConstants.getInstance().getSeries())) {
                               if (ClickHandler.disallowClick()) {
                                   return;
                               }
                               ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                               ActivityLauncher.getInstance().seriesDetailScreen(
                                       BaseActivity.this, SeriesDetailActivity.class, assetId);
                               finish();
                           } else if (contentType.equalsIgnoreCase(AppConstants.ContentType.VIDEO.name())
                                   || contentType.equalsIgnoreCase(
                                   MediaTypeConstants.getInstance().getMovie())) {
                               if (ClickHandler.disallowClick()) {
                                   return;
                               }
                               ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                               ActivityLauncher.getInstance().detailScreen(BaseActivity.this,
                                       DetailActivity.class, assetId, "0", false);
                               finish();
                           } else if (contentType.equalsIgnoreCase(
                                   MediaTypeConstants.getInstance().getShow())) {
                               if (ClickHandler.disallowClick()) {
                                   return;
                               }
                               ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                               ActivityLauncher.getInstance().detailScreen(BaseActivity.this,
                                       DetailActivity.class, assetId, "0", false);
                               finish();
                           } else if (contentType.equalsIgnoreCase(
                                   MediaTypeConstants.getInstance().getLive())) {
                               if (ClickHandler.disallowClick()) {
                                   return;
                               }
                               ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                               ActivityLauncher.getInstance().liveScreenBrightCove(
                                       BaseActivity.this, LiveActivity.class, 0l, assetId, "0", false,
                                       SDKConfig.getInstance().getLiveDetailId());
                               finish();
                           } else if (contentType.equalsIgnoreCase("CUSTOM") || contentType.equalsIgnoreCase("EXPEDITION")) {
                               if (ClickHandler.disallowClick()) {
                                   return;
                               }
                               ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
//                               ActivityLauncher.getInstance().goToExpeditionDetail(
//                                       BaseActivity.this, ExpeditionDetail.class, assetId, true);
//                               finish();
                           } else if (contentType.equalsIgnoreCase(
                                   MediaTypeConstants.getInstance().getEpisode())) {
                               if (ClickHandler.disallowClick()) {
                                   return;
                               }
                               ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                               ActivityLauncher.getInstance().episodeScreen(BaseActivity.this,
                                       EpisodeActivity.class, assetId, "0", false);
                               finish();
                           } else if (contentType.equalsIgnoreCase(
                                   AppConstants.ContentType.ARTICLE.toString())) {
                               if (ClickHandler.disallowClick()) {
                                   return;
                               }
                               ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                               finish();
                           }else {
                               ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                               finish();
                           }
                       } else {
                           ActivityLauncher.getInstance().homeScreen(this, HomeActivity.class);
                           finish();
                       }

                   }

               }catch (Exception e){
                   Logger.w(e);
               }
               }
        } catch (Exception e) {
            Logger.w(e);
        }
    }

    public void updateLanguage(String currentLanguage) {
        try {
            if (currentLanguage.equalsIgnoreCase("")) {
                KsPreferenceKeys.getInstance().setAppLanguage("spanish");
                KsPreferenceKeys.getInstance().setAppPrefLanguagePos(0);
                AppCommonMethod.updateLanguage("es", this);
            } else {
                if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("spanish")) {
                    AppCommonMethod.updateLanguage("es", this);
                } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
                    AppCommonMethod.updateLanguage("en", this);
                }
            }

            //Logger.e("currentLanguage", "" + currentLanguage);

        } catch (Exception e) {

        }
    }


    protected boolean checkPermissionGranted(@NonNull String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private OnPermissionResult onPermissionResult;

    protected void requestPermission(@NonNull String permission, OnPermissionResult onPermissionResult) {
        this.onPermissionResult = onPermissionResult;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + BuildConfig.APPLICATION_ID));
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (onPermissionResult != null) {
            onPermissionResult.performAction(permissions, grantResults);
        }
    }

    public interface OnPermissionResult {
        void performAction(@NonNull String[] permissions, @NonNull int[] grantResults);
    }

    protected void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideSoftKeyboard(view);
                return false;
            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }

    }

    @Override
    public void onDialogFinished() {
        String token = KsPreferenceKeys.getInstance().getAppPrefAccessToken();
        hitApiLogout(getApplicationContext(), token);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!currentLanguage.equalsIgnoreCase(KsPreferenceKeys.getInstance().getAppLanguage())) {
//          Recreate needs to be invoked to recreate the activity so that the new theme can be applied on the current screen.
        }

        if (!strCurrentTheme.equalsIgnoreCase(KsPreferenceKeys.getInstance().getCurrentTheme())) {

//          Recreate needs to be invoked to recreate the activity so that the new theme can be applied on the current screen.
            recreate();
        }

        ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                popupSnackbarForCompleteUpdate();
            }
        });

        if (!(this instanceof ActivitySplash)
                && BaseConfiguration.Companion.getInstance().getClients() == null) {
            setupBaseClient();
        }
    }

    protected void setupBaseClient() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        String API_KEY = "";
        String DEVICE_TYPE = "";
        if (isTablet) {
            API_KEY = SDKConfig.API_KEY_TAB;
            DEVICE_TYPE = BaseDeviceType.tablet.name();
        } else {
            API_KEY = SDKConfig.API_KEY_MOB;
            DEVICE_TYPE = BaseDeviceType.mobile.name();
        }
        BaseClient client = new BaseClient(BaseGateway.ENVEU,
                SDKConfig.getInstance().getBASE_URL(),
                SDKConfig.getInstance().getOVP_BASE_URL(),
                "",
                DEVICE_TYPE,BuildConfig.VERSION_NAME, API_KEY,
                BasePlatform.android.name(), isTablet,
                AppCommonMethod.getDeviceId(getContentResolver()),SDKConfig.getInstance().getSUBSCRIPTION_BASE_URL());
        BaseConfiguration.Companion.getInstance().clientSetup(client);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.update_has_downloaded), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getResources().getString(R.string.restart), view -> ApplicationUpdateManager.getInstance(getApplicationContext()).getAppUpdateManager().completeUpdate());
        snackbar.setActionTextColor(getResources().getColor(R.color.series_detail_episode_unselected_btn_txt_color));
        snackbar.show();
    }

    public void setTextOrHide(TextView textView, String data) {
        Logger.d(Logger.getTag(), " setTextOrHide called " + textView + " " + data);
        if (ObjectHelper.isEmpty(data)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setText(data);
            textView.setVisibility(View.VISIBLE);
        }
    }

    public void updateVisibility(View view, String value) {
        try {
            Logger.d(Logger.getTag(), "updateVisibility called " + view + " " + value);
            if (ObjectHelper.isNotEmpty(value)) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            Logger.w(ex);
        }
    }

    public int getFeatureCount(EnveuVideoItemBean seriesDetailBean) {
        int featureCount = 0;
        if (seriesDetailBean == null) {
            return featureCount;
        }
        if (ObjectHelper.isNotEmpty(seriesDetailBean.getIs4k())) {
            featureCount++;
        }
        if (ObjectHelper.isNotEmpty(seriesDetailBean.getSignedLangEnabled())) {
            featureCount++;
        }
        if (ObjectHelper.isNotEmpty(seriesDetailBean.getIsAudioDesc())) {
            featureCount++;
        }
        if (ObjectHelper.isNotEmpty(seriesDetailBean.getIsClosedCaption())) {
            featureCount++;
        }
        if (ObjectHelper.isNotEmpty(seriesDetailBean.getIsSoundTrack())) {
            featureCount++;
        }
        return featureCount;
    }
}
