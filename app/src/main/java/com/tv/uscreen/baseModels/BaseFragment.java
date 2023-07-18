package com.tv.uscreen.baseModels;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.enveu.client.baseCollection.baseCategoryServices.BaseCategoryServices;
import com.enveu.client.userManagement.callBacks.LogoutCallBack;
import com.facebook.login.LoginManager;
import com.google.gson.JsonObject;
import com.tv.uscreen.OttApplication;
import com.tv.uscreen.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.utils.constants.AppConstants;
import com.tv.uscreen.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import java.util.Objects;

import retrofit2.Response;

public class BaseFragment extends Fragment {

    @Nullable
    protected BaseBindingActivity getBaseActivity() {
        return (BaseBindingActivity) getActivity();
    }

    protected void showLoading(ProgressBar progressBar, boolean val, Activity context) {
        if (val) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.bringToFront();
        }
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public void clearCredientials(KsPreferenceKeys preference) {
        try {
            String isFacebook = preference.getAppPrefLoginType();
            if (isFacebook.equalsIgnoreCase(AppConstants.UserLoginType.FbLogin.toString())) {
                LoginManager.getInstance().logOut();
            }
            String strCurrentTheme = KsPreferenceKeys.getInstance().getCurrentTheme();
            String strCurrentLanguage = KsPreferenceKeys.getInstance().getAppLanguage();
            preference.clear();
            KsPreferenceKeys.getInstance().setCurrentTheme(strCurrentTheme);
            KsPreferenceKeys.getInstance().setAppLanguage(strCurrentLanguage);
            if (strCurrentLanguage.equalsIgnoreCase("spanish") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
                AppCommonMethod.updateLanguage("es", OttApplication.getInstance());
            } else if (strCurrentLanguage.equalsIgnoreCase("English")) {
                AppCommonMethod.updateLanguage("en", OttApplication.getInstance());
            }
            //AppCommonMethod.updateLanguage(strCurrentLanguage,getBaseActivity());
            // ActivityLauncher.getInstance()`.homeScreen(getBaseActivity(), HomeActivity.class);
        } catch (Exception e) {
            //ActivityLauncher.getInstance()`.homeScreen(getBaseActivity(), HomeActivity.class);

        }
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

    protected void dismissLoading(ProgressBar progressBar, Activity context) {
        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}
