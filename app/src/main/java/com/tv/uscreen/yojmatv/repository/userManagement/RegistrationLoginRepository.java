package com.tv.uscreen.yojmatv.repository.userManagement;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.enveu.client.baseCollection.baseCategoryServices.BaseCategoryServices;
import com.enveu.client.deleteAccount.DeleteAccountCallback;
import com.enveu.client.deleteAccount.DeleteAccountResponse;
import com.enveu.client.epgCallBacks.EpgCallBack;
import com.enveu.client.joinContest.joinContestCallBack.JoinContestCallback;
import com.enveu.client.userManagement.bean.allSecondaryDetails.AllSecondaryDetails;
import com.enveu.client.userManagement.bean.allSecondaryDetails.SecondaryUserDetails;
import com.enveu.client.userManagement.bean.registrationOtp.OtpResponseModel;
import com.enveu.client.userManagement.callBacks.AllListCallBack;
import com.enveu.client.userManagement.callBacks.AuthCallBack;
import com.enveu.client.userManagement.callBacks.ForgotPasswordCallBack;
import com.enveu.client.userManagement.callBacks.GetOTPCallback;
import com.enveu.client.userManagement.callBacks.LoginCallBack;
import com.enveu.client.userManagement.callBacks.SecondaryUserCallBack;
import com.enveu.client.userManagement.callBacks.UserProfileCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.tv.uscreen.yojmatv.OttApplication;
import com.tv.uscreen.yojmatv.R;
import com.tv.uscreen.yojmatv.activities.usermanagment.model.OtpResponse;
import com.tv.uscreen.yojmatv.beanModel.LoginDeviceModel.LoginDeviceModel;
import com.tv.uscreen.yojmatv.beanModel.connectFb.ResponseConnectFb;
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.uscreen.yojmatv.beanModel.forgotPassword.CommonResponse;
import com.tv.uscreen.yojmatv.beanModel.requestParamModel.RequestParamRegisterUser;
import com.tv.uscreen.yojmatv.beanModel.responseModels.LoginResponse.LoginResponseModel;
import com.tv.uscreen.yojmatv.beanModel.responseModels.RegisterSignUpModels.ResponseRegisteredSignup;
import com.tv.uscreen.yojmatv.beanModel.responseModels.SignUp.SignupResponseAccessToken;
import com.tv.uscreen.yojmatv.beanModel.responseModels.listAllAccounts.AllSecondaryAccountDetails;
import com.tv.uscreen.yojmatv.beanModel.responseModels.secondaryUserDetails.SecondaryUserDetailsJavaPojo;
import com.tv.uscreen.yojmatv.beanModel.userProfile.UserProfileResponse;
import com.tv.uscreen.yojmatv.beanModelV3.uiConnectorModelV2.EnveuVideoItemBean;
import com.tv.uscreen.yojmatv.bean_model_v1_0.authResponse.AuthResponse;
import com.tv.uscreen.yojmatv.networking.apiendpoints.ApiInterface;
import com.tv.uscreen.yojmatv.networking.apiendpoints.RequestConfig;
import com.tv.uscreen.yojmatv.networking.intercepter.ErrorCodesIntercepter;
import com.tv.uscreen.yojmatv.utils.Logger;
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod;
import com.tv.uscreen.yojmatv.utils.constants.AppConstants;
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils;
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistrationLoginRepository {


    private static RegistrationLoginRepository instance;

    private RegistrationLoginRepository() {
    }

    public static RegistrationLoginRepository getInstance() {
        if (instance == null) {
            instance = new RegistrationLoginRepository();
        }
        if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("spanish") || KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("हिंदी")) {
            AppCommonMethod.updateLanguage("es", OttApplication.getInstance());
        } else if (KsPreferenceKeys.getInstance().getAppLanguage().equalsIgnoreCase("English")) {
            AppCommonMethod.updateLanguage("en", OttApplication.getInstance());
        }
        return (instance);
    }


    public LiveData<LoginResponseModel> getLoginAPIResponse(String username, String pwd) {
        final MutableLiveData<LoginResponseModel> responseApi;
        responseApi = new MutableLiveData<>();
        final JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_EMAIL, username);
        requestParam.addProperty(AppConstants.API_PARAM_PASSWORD, pwd);

        BaseCategoryServices.Companion.getInstance().loginService(username.trim(), pwd.trim(), new LoginCallBack() {
            @Override
            public void success(boolean status, Response< com.enveu.client.userManagement.bean.LoginResponse.LoginResponseModel> response) {
                if (status) {
                    LoginResponseModel cl;
                    if (response.body() != null) {
                        String token = response.headers().get("x-auth");
                        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
                        preference.setAppPrefAccessToken(token);
                        //Logger.e("X_AUTHTOKEN",token);
                        Gson gson = new Gson();
                        String tmp = gson.toJson(response.body());
                        LoginResponseModel loginItemBean = gson.fromJson(tmp, LoginResponseModel.class);
                        responseApi.postValue(loginItemBean);
                    } else {
                        LoginResponseModel responseModel = ErrorCodesIntercepter.getInstance().Login(response);
                        responseApi.postValue(responseModel);
                    }
                }
            }

            @Override
            public void failure(boolean status, int errorCode, String errorMessage) {
                Logger.e("", "LoginResponseToUI E" + errorMessage);
                LoginResponseModel cl = new LoginResponseModel();
                cl.setStatus(false);
                responseApi.postValue(cl);
            }
        });

        return responseApi;
    }

    public LiveData<AuthResponse> getAuthResponse() {
        final MutableLiveData<AuthResponse> responseApi;
        responseApi = new MutableLiveData<>();

        BaseCategoryServices.Companion.getInstance().getAuthUrl(new AuthCallBack() {
            @Override
            public void success(boolean status, @NonNull Response<com.enveu.client.userManagement.bean.authResponse.AuthResponse> authResponse) {
                if (status) {
                    LoginResponseModel cl;
                    if (authResponse.body() != null) {
                        //Logger.e("X_AUTHTOKEN",token);
                        Gson gson = new Gson();
                        String tmp = gson.toJson(authResponse.body());
                        AuthResponse loginItemBean = gson.fromJson(tmp, AuthResponse.class);
                        responseApi.postValue(loginItemBean);
                    } else {
                        AuthResponse responseModel = ErrorCodesIntercepter.getInstance().Auth(authResponse);
                        responseApi.postValue(responseModel);
                    }
                }
            }

            @Override
            public void failure(boolean status, int errorCode, @NonNull String message) {
                AuthResponse cl = new AuthResponse();
                cl.setStatus(false);
                responseApi.postValue(cl);
            }
        });
        return responseApi;
    }
    public LiveData<LoginDeviceModel> getLoginDevice(String token) {

        final MutableLiveData<LoginDeviceModel> responseApi;
        responseApi = new MutableLiveData<>();

        try {

            Call<LoginDeviceModel> result =
                    RequestConfig.getUserInteration(token).create(ApiInterface.class)
                            .getLoginDeviceResponse(true);

            result.enqueue(new Callback<LoginDeviceModel>() {
                @Override
                public void onResponse(Call<LoginDeviceModel> call, Response<LoginDeviceModel> response) {

                    if  (response.body() != null){
                    Gson gson = new Gson();
                    String tmp = gson.toJson(response.body());
                    LoginDeviceModel loginItemBean = gson.fromJson(tmp, LoginDeviceModel.class);
                    responseApi.postValue(loginItemBean);
                    }
                    else {
                        responseApi.postValue(null);


                    }
                }

                @Override
                public void onFailure(Call<LoginDeviceModel> call, Throwable t) {
                    responseApi.postValue(null);
                }
            });

        } catch (Exception exception) {
        }

        return responseApi;
    }


    public LiveData<SignupResponseAccessToken> getSignupAPIResponse(String name, String email, String dob,String pwd, boolean isNotificationEnable) {
        String typeList = "";
        String speciesList = "";
        String profileId = KsPreferenceKeys.getInstance().getPreferenceProfileId();
        Logger.w("profileId",profileId);
        final MutableLiveData<SignupResponseAccessToken> responseApi;
        {
            responseApi = new MutableLiveData<>();
            if (null!= KsPreferenceKeys.getInstance().getTypeList()) {
                 typeList = AppCommonMethod.getTypeList();
            }
            if (null!= KsPreferenceKeys.getInstance().getSpeciesList()) {
                speciesList = AppCommonMethod.getSpeciesList();
            }


            final JsonObject requestParam = new JsonObject();
            requestParam.addProperty(AppConstants.API_PARAM_NAME, "");
            requestParam.addProperty(AppConstants.API_PARAM_EMAIL, "");
            requestParam.addProperty(AppConstants.API_PARAM_PASSWORD, pwd);

            BaseCategoryServices.Companion.getInstance().registerWithContentPreferences(name.trim(), email.trim(),dob.trim(), pwd.trim(),isNotificationEnable,profileId,speciesList,typeList, new LoginCallBack() {
                @Override
                public void success(boolean status, Response< com.enveu.client.userManagement.bean.LoginResponse.LoginResponseModel> response) {
                    if (status) {
                        try {
                            if (response != null && response.errorBody() == null) {
                                Logger.d("responseValue: " + response.code());
                            }
                        } catch (Exception e) {
                            Logger.w(e);
                        }
                        if (response!=null && response.body() != null){
                            if (response.code() == 200 ) {
                                Gson gson = new Gson();
                                String tmp = gson.toJson(response.body());
                                LoginResponseModel cl = gson.fromJson(tmp, LoginResponseModel.class);
                                cl.setResponseCode(200);
                                String token = response.headers().get("x-auth");
                                SignupResponseAccessToken responseModel = new SignupResponseAccessToken();
                                responseModel.setAccessToken(token);
                                responseModel.setResponseModel(cl);
                                Logger.e("manual", "nNontonToken" + token);

                                responseApi.postValue(responseModel);
                                Logger.e("", "REsponse" + response.body());
                            } else {
                                SignupResponseAccessToken responseModel = ErrorCodesIntercepter.getInstance().manualSignUp(response);
                                responseApi.postValue(responseModel);
                            }
                        }else {
                            SignupResponseAccessToken responseModel = ErrorCodesIntercepter.getInstance().manualSignUp(response);
                            responseApi.postValue(responseModel);
                        }

                    }
                }

                @Override
                public void failure(boolean status, int errorCode, String errorMessage) {
                    Logger.e("", "LoginResponseToUI E" + errorMessage);
                    LoginResponseModel cl = new LoginResponseModel();
                    cl.setResponseCode(400);
                    SignupResponseAccessToken responseModel = new SignupResponseAccessToken();
                    responseModel.setResponseModel(cl);
                    responseModel.setDebugMessage(OttApplication.getInstance().getResources().getString(R.string.server_error));
                    responseApi.postValue(responseModel);
                }
            });
        }

        return responseApi;
    }


    public LiveData<ResponseRegisteredSignup> getSignupAPIResponse(Context context, RequestParamRegisterUser userDetails) {

        final MutableLiveData<ResponseRegisteredSignup> responseApi;
        responseApi = new MutableLiveData<>();
        boolean check;
        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
        Logger.e("Token", "nNontonToken" + userDetails.getAccessToken());

        ApiInterface endpoint = RequestConfig.getClientInterceptor(userDetails.getAccessToken()).create(ApiInterface.class);
        final JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_NAME, userDetails.getName());
        requestParam.addProperty(AppConstants.API_PARAM_IS_VERIFIED, userDetails.isVerified());
        requestParam.addProperty(AppConstants.API_PARAM_VERIFICATION_DATE, userDetails.getVerificationDate());

        if (StringUtils.isNullOrEmptyOrZero(userDetails.getProfilePicURL())) {
            requestParam.add(AppConstants.API_PARAM_PROFILE_PIC, JsonNull.INSTANCE);

        } else {
            requestParam.addProperty(AppConstants.API_PARAM_PROFILE_PIC, userDetails.getProfilePicURL());
        }
        check = preference.getAppPrefDOB();
        if (check)
            requestParam.add(AppConstants.API_PARAM_DOB, JsonNull.INSTANCE);
        else
            requestParam.addProperty(AppConstants.API_PARAM_DOB, userDetails.getDateOfBirth());

        check = preference.getAppPrefHasNumberEmpty();
        if (check)
            requestParam.add(AppConstants.API_PARAM_PHONE_NUMBER, JsonNull.INSTANCE);
        else
            requestParam.addProperty(AppConstants.API_PARAM_PHONE_NUMBER, String.valueOf(userDetails.getPhoneNumber()));

        requestParam.addProperty(AppConstants.API_PARAM_STATUS, userDetails.getStatus());
        requestParam.addProperty(AppConstants.API_PARAM_EXPIRY_DATE, userDetails.getExpiryDate());
        requestParam.addProperty(AppConstants.API_PARAM_GENDER, userDetails.getGender());
        requestParam.addProperty(AppConstants.API_PARAM_PROFILE_STEP, "STEP_2");

        Call<ResponseRegisteredSignup> call = endpoint.getRegistrationStep(requestParam);
        call.enqueue(new Callback<ResponseRegisteredSignup>() {
            @Override
            public void onResponse(@NonNull Call<ResponseRegisteredSignup> call, @NonNull Response<ResponseRegisteredSignup> response) {
                // SignUpResponseModel cl = response.body();

                if (response.code() == 200) {
            ResponseRegisteredSignup temp = response.body();
                    temp.setStatus(true);
                    temp.setResponseCode(response.code());
                    responseApi.postValue(response.body());
                } else if (response.code() == 401) {
                    ResponseRegisteredSignup temp = new ResponseRegisteredSignup();
                    temp.setResponseCode(response.code());
                    temp.setStatus(false);
                    responseApi.postValue(temp);
                } else {
                    ResponseRegisteredSignup temp = new ResponseRegisteredSignup();
                    temp.setResponseCode(Objects.requireNonNull(response.code()));
                    temp.setStatus(false);
                    responseApi.postValue(temp);

                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseRegisteredSignup> call, @NonNull Throwable t) {
                Logger.e("error", "REsponse" + call);
                try {
                    if (call.execute().body() != null)
                        responseApi.postValue(call.execute().body());
                    else {

                        ResponseRegisteredSignup temp = new ResponseRegisteredSignup();
                        temp.setStatus(false);
                        temp.setResponseCode(500);

                    }
                } catch (IOException e) {
                    Logger.w(e);
                }

            }
        });

        return responseApi;
    }


    public LiveData<CommonResponse> getForgotPasswordAPIResponse(String email) {
        final MutableLiveData<CommonResponse> responseApi;
        {
            CommonResponse commonResponse = new CommonResponse();
            responseApi = new MutableLiveData<>();

            BaseCategoryServices.Companion.getInstance().forgotPasswordService(email, new ForgotPasswordCallBack() {
                @Override
                public void success(boolean status, Response<JsonObject> response) {

                    if (response.code() == 200) {
                        commonResponse.setCode(response.code());
                        responseApi.postValue(commonResponse);
                    } else {
                        String debugMessage = "";
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            int errorcode = jObjError.getInt("responseCode");
                            if (errorcode==4401){
                                commonResponse.setDebugMessage(OttApplication.getInstance().getString(R.string.popup_user_does_not_exists));
                                commonResponse.setCode(response.code());
                            }else {
                                commonResponse.setDebugMessage(debugMessage);
                                commonResponse.setCode(response.code());
                            }
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("RegistrationLoginRepo", "" + e);
                        }


                        responseApi.postValue(commonResponse);
                    }


                }

                @Override
                public void failure(boolean status, int errorCode, String message) {
                    commonResponse.setDebugMessage("");
                    commonResponse.setCode(500);
                    responseApi.postValue(commonResponse);
                }
            });

        }

        return responseApi;
    }


    public LiveData<LoginResponseModel> getChangePwdAPIResponse(String pwd, String token, Context context) {
        final MutableLiveData<LoginResponseModel> responseApi;
        responseApi = new MutableLiveData<>();
        final JsonObject requestParam = new JsonObject();
        requestParam.addProperty(AppConstants.API_PARAM_NEW_PWD, pwd);

        BaseCategoryServices.Companion.getInstance().changePasswordService(requestParam, token, new LoginCallBack() {
            @Override
            public void success(boolean status, Response< com.enveu.client.userManagement.bean.LoginResponse.LoginResponseModel> response) {
                if (status) {
                    LoginResponseModel cl;
                    if (response.code() == 500) {
                        cl = new LoginResponseModel();
                        cl.setResponseCode(Objects.requireNonNull(response.code()));
                        responseApi.postValue(cl);

                    } else if (response.code() == 401 || response.code() == 404) {
                        cl = new LoginResponseModel();
                        cl.setResponseCode(response.code());
                        String debugMessage = "";
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("RegistrationLoginRepo", "" + e);
                        }
                        cl.setDebugMessage(debugMessage);

                        responseApi.postValue(cl);
                    } else if (response.code() == 403) {
                        cl = new LoginResponseModel();
                        cl.setResponseCode(response.code());
                        String debugMessage = "";
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("RegistrationLoginRepo", "" + e);
                        }
                        cl.setDebugMessage(OttApplication.getInstance().getResources().getString(R.string.username_must_be_loggedin));

                        responseApi.postValue(cl);
                    } else if (response.body() != null ) {
                        Logger.e("", "LoginResponseModel" + response.body());
                        String token = response.headers().get("x-auth");
                        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
                        preference.setAppPrefAccessToken(token);
                        Gson gson = new Gson();
                        String tmp = gson.toJson(response.body());
                        LoginResponseModel loginItemBean = gson.fromJson(tmp, LoginResponseModel.class);
                        responseApi.postValue(loginItemBean);
                    }
                }
            }

            @Override
            public void failure(boolean status, int errorCode, String errorMessage) {
                Logger.e("", "LoginResponseToUI E" + errorMessage);
                LoginResponseModel cl = new LoginResponseModel();
                cl.setStatus(false);
                responseApi.postValue(cl);
            }
        });

           /* call.enqueue(new Callback<ResponseChangePassword>() {
                @Override
                public void onResponse(@NonNull Call<ResponseChangePassword> call, @NonNull ContinueWatchingModel<ResponseChangePassword> response) {
                    if (response.code() == 200) {
                        ResponseChangePassword model = response.body();
                        model.setAccessToken(response.headers().get("x-auth"));
                        model.setStatus(true);
                        responseApi.postValue(response.body());
                    } else {
                        ResponseChangePassword model = new ResponseChangePassword();
                        model.setStatus(false);
                        model.setResponseCode(Objects.requireNonNull(response.code()));
                        responseApi.postValue(model);

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseChangePassword> call, @NonNull Throwable t) {
                    ResponseChangePassword model = new ResponseChangePassword();
                    model.setStatus(false);
                    responseApi.postValue(model);

                }
            });*/

        return responseApi;
    }


    public LiveData<LoginResponseModel> getFbLogin(Context context, String email, String fbToken, String name, String fbId, String profilePic, boolean isEmail) {
        final MutableLiveData<LoginResponseModel> responseApi;
        {
            responseApi = new MutableLiveData<>();
            ApiInterface endpoint = RequestConfig.getEnveuSubscriptionClient().create(ApiInterface.class);
            final JsonObject requestParam = new JsonObject();
            requestParam.addProperty(AppConstants.API_PARAM_FB_ID, fbId);
            requestParam.addProperty(AppConstants.API_PARAM_NAME, name);
            requestParam.addProperty(AppConstants.API_PARAM_EMAIL_ID, email);
            requestParam.addProperty(AppConstants.API_PARAM_FB_TOKEN, fbToken);
            requestParam.addProperty(AppConstants.API_PARAM_IS_FB_EMAIL, isEmail);
            if (!profilePic.equalsIgnoreCase("")) {
                requestParam.addProperty(AppConstants.API_PARAM_FB_PIC, profilePic);
            }
            Call<LoginResponseModel> call = endpoint.getFbLogin(requestParam);

            BaseCategoryServices.Companion.getInstance().fbLoginService(requestParam, new LoginCallBack() {
                @Override
                public void success(boolean status, Response< com.enveu.client.userManagement.bean.LoginResponse.LoginResponseModel> response) {
                    if (status) {
                        LoginResponseModel cl;

                        if (response.body() != null ) {
                            String token = response.headers().get("x-auth");
                            KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
                            preference.setAppPrefAccessToken(token);
                            Gson gson = new Gson();
                            String tmp = gson.toJson(response.body());
                            LoginResponseModel loginItemBean = gson.fromJson(tmp, LoginResponseModel.class);

                            responseApi.postValue(loginItemBean);
                        } else {
                            LoginResponseModel responseModel = ErrorCodesIntercepter.getInstance().fbLogin(response);
                            responseApi.postValue(responseModel);
                        }

                    }
                }

                @Override
                public void failure(boolean status, int errorCode, String message) {
                    Logger.e("ResponseError", "getFbLogin" + call.toString());
                    try {
                        responseApi.postValue(call.execute().body());
                    } catch (IOException e) {
                        Logger.w(e);
                    }
                }
            });

        }
        return responseApi;
    }

    public LiveData<LoginResponseModel> getForceFbLogin(Context context, String email, String fbToken, String name, String fbId, boolean isEmail) {
        final MutableLiveData<LoginResponseModel> responseApi;
        {
            responseApi = new MutableLiveData<>();
            ApiInterface endpoint = RequestConfig.getEnveuSubscriptionClient().create(ApiInterface.class);
            final JsonObject requestParam = new JsonObject();
            requestParam.addProperty(AppConstants.API_PARAM_FB_ID, fbId);
            requestParam.addProperty(AppConstants.API_PARAM_NAME, name);
            requestParam.addProperty(AppConstants.API_PARAM_EMAIL_ID, email);
            requestParam.addProperty(AppConstants.API_PARAM_FB_TOKEN, fbToken);
            requestParam.addProperty(AppConstants.FB_MAIL, isEmail);
            Call<LoginResponseModel> call = endpoint.getFbLogin(requestParam);

            BaseCategoryServices.Companion.getInstance().fbLoginService(requestParam, new LoginCallBack() {
                @Override
                public void success(boolean status, Response< com.enveu.client.userManagement.bean.LoginResponse.LoginResponseModel> response) {
                    if (status) {
                        LoginResponseModel cl;

                        if (response.body() != null ) {
                            String token = response.headers().get("x-auth");
                            KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
                            preference.setAppPrefAccessToken(token);
                            Gson gson = new Gson();
                            String tmp = gson.toJson(response.body());
                            LoginResponseModel loginItemBean = gson.fromJson(tmp, LoginResponseModel.class);

                            responseApi.postValue(loginItemBean);
                        } else {
                            LoginResponseModel responseModel = ErrorCodesIntercepter.getInstance().fbLogin(response);
                            responseApi.postValue(responseModel);
                        }

                    }
                }

                @Override
                public void failure(boolean status, int errorCode, String message) {
                    Logger.e("ResponseError", "getFbLogin" + call.toString());
                    try {
                        responseApi.postValue(call.execute().body());
                    } catch (IOException e) {
                        Logger.w(e);
                    }
                }
            });

        }
        return responseApi;
    }


    public LiveData<LoginResponseModel> getForceFbLogin(Context context, String email, String fbToken, String name, String fbId, String profilePic, boolean isEmail) {

        final MutableLiveData<LoginResponseModel> responseApi;
        {
            responseApi = new MutableLiveData<>();
            ApiInterface endpoint = RequestConfig.getEnveuSubscriptionClient().create(ApiInterface.class);
            final JsonObject requestParam = new JsonObject();
            requestParam.addProperty(AppConstants.API_PARAM_FB_ID, fbId);
            requestParam.addProperty(AppConstants.API_PARAM_NAME, name);
            requestParam.addProperty(AppConstants.API_PARAM_EMAIL_ID, email);
            requestParam.addProperty(AppConstants.API_PARAM_FB_TOKEN, fbToken);
            requestParam.addProperty(AppConstants.API_PARAM_IS_FB_EMAIL, isEmail);
            if (!profilePic.equalsIgnoreCase("")) {
                requestParam.addProperty(AppConstants.API_PARAM_FB_PIC, profilePic);
            }
            Call<LoginResponseModel> call = endpoint.getForceFbLogin(requestParam);

            BaseCategoryServices.Companion.getInstance().fbForceLoginService(requestParam, new LoginCallBack() {
                @Override
                public void success(boolean status, Response< com.enveu.client.userManagement.bean.LoginResponse.LoginResponseModel> response) {
                    LoginResponseModel cl;
                    if (status) {

                        if (response.body() != null ) {
                            Logger.e("", "LoginResponseModel" + response.body());
                            String token = response.headers().get("x-auth");
                            KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
                            preference.setAppPrefAccessToken(token);
                            Gson gson = new Gson();
                            String tmp = gson.toJson(response.body());
                            LoginResponseModel loginItemBean = gson.fromJson(tmp, LoginResponseModel.class);

                            responseApi.postValue(loginItemBean);
                        } else {
                            LoginResponseModel responseModel = ErrorCodesIntercepter.getInstance().fbLogin(response);
                            responseApi.postValue(responseModel);
                        }

                    } else {
                        cl = new LoginResponseModel();
                        cl.setResponseCode(response.code());
                        String debugMessage = context.getResources().getString(R.string.server_error);
                        cl.setDebugMessage(debugMessage);
                    }
                }

                @Override
                public void failure(boolean status, int errorCode, String message) {
                    Logger.e("ResponseError", "getFbLogin" + call.toString());
                    try {
                        responseApi.postValue(call.execute().body());
                    } catch (IOException e) {
                        Logger.w(e);
                    }
                }
            });
        }
        return responseApi;
    }


    public LiveData<ResponseConnectFb> getConnectFb(Context context, String token, JsonObject requestParam) {
        final MutableLiveData<ResponseConnectFb> responseApi;
        {
            responseApi = new MutableLiveData<>();
            ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);
            endpoint.getConnectFb(requestParam).enqueue(new Callback<ResponseConnectFb>() {
                @Override
                public void onResponse(@NonNull Call<ResponseConnectFb> call, @NonNull Response<ResponseConnectFb> response) {
                    ResponseConnectFb model = new ResponseConnectFb();
                    if (response.code() == 200) {
                        model.setStatus(true);
                        model.setData(response.body().getData());
                        responseApi.postValue(model);
                        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
                        preference.setAppPrefAccessToken(response.headers().get("x-auth-token"));
                    } else if (response.code() == 400) {
                        String debugMessage;
                        int code = 0;
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            debugMessage = jObjError.getString("debugMessage");
                            code = jObjError.getInt("responseCode");

                            model.setResponseCode(400);
                            model.setDebugMessage(debugMessage);
                            model.setStatus(false);
                            responseApi.postValue(model);
                            Logger.e("", "" + jObjError.getString("debugMessage"));
                        } catch (Exception e) {
                            Logger.e("RegistrationLoginRepo", "" + e);
                        }
                    } else {
                        model.setStatus(false);
                        responseApi.postValue(model);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseConnectFb> call, @NonNull Throwable t) {
                    ResponseConnectFb model = new ResponseConnectFb();
                    model.setStatus(false);
                }
            });
            return responseApi;
        }
    }


    public LiveData<JsonObject> hitApiLogout(boolean session, String token) {
        final MutableLiveData<JsonObject> responseApi;
        {
            responseApi = new MutableLiveData<>();
            ApiInterface endpoint = RequestConfig.getClientInterceptor(token).create(ApiInterface.class);

            Call<JsonObject> call = endpoint.getLogout(session);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    try {

                    } catch (Exception e) {

                    }
                    if (response.code() == 404) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                        responseApi.postValue(jsonObject);
                    } else if (response.code() == 200) {
                        Objects.requireNonNull(response.body()).addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                        responseApi.postValue(response.body());
                    } else if (response.code() == 401) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                        responseApi.postValue(jsonObject);
                    } else if (response.code() == 500) {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppConstants.API_RESPONSE_CODE, response.code());
                        responseApi.postValue(jsonObject);
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });


        }

        return responseApi;
    }


    public LiveData<UserProfileResponse> getUserProfile(Context context, String token) {
        MutableLiveData<UserProfileResponse> mutableLiveData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().userProfileService(token, new UserProfileCallBack() {
            @Override
            public void success(boolean status, Response< com.enveu.client.userManagement.bean.UserProfile.UserProfileResponse> response) {
                UserProfileResponse cl;
                if (status) {
                    if (response != null) {
                        if (response.code() == 200) {
                            Gson gson = new Gson();
                            String tmp = gson.toJson(response.body());
                            UserProfileResponse profileItemBean = gson.fromJson(tmp, UserProfileResponse.class);
                            profileItemBean.setStatus(true);
                            mutableLiveData.postValue(profileItemBean);
                        } else {

                            cl = ErrorCodesIntercepter.getInstance().userProfile(response);
                            mutableLiveData.postValue(cl);

                        }
                    }


                }
            }

            @Override
            public void failure(boolean status, int errorCode, String message) {
                UserProfileResponse cl = new UserProfileResponse();
                cl.setStatus(false);
                mutableLiveData.postValue(cl);
            }
        });
        return mutableLiveData;
    }

    public LiveData<OtpResponse> getGenerateOTPResponse(String token) {
        final MutableLiveData<OtpResponse> responseApi;
        responseApi = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().generateOtpReq(token, new GetOTPCallback() {
            @Override
            public void success(boolean status, @NonNull Response<OtpResponseModel> response) {
                if (status) {
                    if (response.body() != null) {
                        Gson gson = new Gson();
                        String tmp = gson.toJson(response.body());
                        OtpResponse otpResponse = gson.fromJson(tmp, OtpResponse.class);
                        responseApi.postValue(otpResponse);
                    } else {
                        OtpResponse responseModel = ErrorCodesIntercepter.getInstance().otpResponse(response);
                        responseApi.postValue(responseModel);
                    }
                }
            }

            @Override
            public void failure(boolean status, int errorCode, @NonNull String errorMessage) {
                Logger.e("OtpResponse", "Error -> " + errorMessage);
                OtpResponse otpErrorResponse = new OtpResponse();
                otpErrorResponse.setDebugMessage(errorMessage);
                responseApi.postValue(otpErrorResponse);
            }
        });
        return responseApi;
    }

    public LiveData<OtpResponse> getOTPVerify(int otp, String token) {
        final MutableLiveData<OtpResponse> responseApi;
        responseApi = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().otpVerify(otp, token, new GetOTPCallback() {
            @Override
            public void success(boolean status, @NonNull Response<OtpResponseModel> response) {
                if (status) {
                    if (response.body() != null) {
                        Gson gson = new Gson();
                        String tmp = gson.toJson(response.body());
                        OtpResponse otpResponse = gson.fromJson(tmp, OtpResponse.class);
                        responseApi.postValue(otpResponse);
                    } else {
                        OtpResponse responseModel = ErrorCodesIntercepter.getInstance().otpResponse(response);
                        responseApi.postValue(responseModel);
                    }
                }
            }

            @Override
            public void failure(boolean status, int errorCode, @NonNull String errorMessage) {
                Logger.e("OtpResponse", "Error -> " + errorMessage);
                OtpResponse otpErrorResponse = new OtpResponse();
                otpErrorResponse.setDebugMessage(errorMessage);
                responseApi.postValue(otpErrorResponse);
            }
        });
        return responseApi;
    }



    public LiveData<UserProfileResponse> getUpdateProfile(Context context, String token, String name, String mobile, String spinnerValue, String dob, String address, String imageUrl, String via, String contentPreference, boolean isNotificationEnable, String pin, String city, String country, String profile, String species, String type) {

        MutableLiveData<UserProfileResponse> mutableLiveData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().userUpdateProfileServiceWithNoti(token, name,mobile,spinnerValue,dob,address,imageUrl,via,contentPreference,isNotificationEnable,pin,city,country,profile,species,type, new UserProfileCallBack() {
            @Override
            public void success(boolean status, Response< com.enveu.client.userManagement.bean.UserProfile.UserProfileResponse> response) {
                UserProfileResponse cl;
                if (status) {
                    if (response != null) {
                        if (response.code() == 200) {
                            Gson gson = new Gson();
                            String tmp = gson.toJson(response.body());
                            UserProfileResponse profileItemBean = gson.fromJson(tmp, UserProfileResponse.class);
                            profileItemBean.setStatus(true);
                            mutableLiveData.postValue(profileItemBean);
                        } else {
                            cl = ErrorCodesIntercepter.getInstance().userProfile(response);
                            mutableLiveData.postValue(cl);
                        }
                    }


                }
            }

            @Override
            public void failure(boolean status, int errorCode, String message) {
                UserProfileResponse cl = new UserProfileResponse();
                cl.setStatus(false);
                mutableLiveData.postValue(cl);
            }
        });
        return mutableLiveData;
    }


    private List<EnveuVideoItemBean> enveuVideoItemBeans;
    public LiveData<RailCommonData>getEpgListing(Context context, String channelId,Long startDate , Long endDate,int pageNumber,int pageSize,String languageCode) {
        MutableLiveData<RailCommonData> mutableLiveData = new MutableLiveData<>();

        BaseCategoryServices.Companion.getInstance().getEpgListing(channelId,startDate,endDate,pageNumber,pageSize,languageCode, new EpgCallBack() {
            @Override
            public void failure(boolean status, int errorCode, @NonNull String message) {
                mutableLiveData.postValue(null);
            }

            @Override
            public void success(boolean status, Response<com.enveu.client.epgListing.epgResponseNew.Response> response) {
                if (status) {
                    if (response != null) {
                        if (response.code() == 200) {
                            RailCommonData railCommonData = null;
                            railCommonData = new RailCommonData();

                            if (response.body()!=null) {
                                if (response.body().getData() !=null){
                                    List<com.enveu.client.epgListing.epgResponseNew.DataItem> dataItem = response.body().getData();
                                    enveuVideoItemBeans = new ArrayList<>();
                                    for (int i=0;i<dataItem.size();i++){
                                        com.enveu.client.epgListing.epgResponseNew.MediaContent mediaContent= dataItem.get(i).getMediaContent();
                                        Gson gson1 = new Gson();
                                        String tmp1 = gson1.toJson(mediaContent);
                                        EnveuVideoItemBean enveuVideoItemBean = new EnveuVideoItemBean(mediaContent);

                                        if (dataItem.get(i).getSchedules()!=null) {
                                            railCommonData.setSchedulesItemArrayList(dataItem.get(i).getSchedules());
                                            enveuVideoItemBeans.add(enveuVideoItemBean);
                                        }
                                    }
                                    railCommonData.setEnveuVideoItemBeans(enveuVideoItemBeans);
                                }

                            }
                            mutableLiveData.postValue(railCommonData);
                        } else {
                            mutableLiveData.postValue(null);
                        }
                    }
                }
                }

        });
        return mutableLiveData;
    }


    /////
    public LiveData<AllSecondaryAccountDetails>  getSecondaryAPIResponse(Context context,String token) {
        final MutableLiveData<AllSecondaryAccountDetails> responseApi;
        responseApi = new MutableLiveData<>();


        BaseCategoryServices.Companion.getInstance().AllListService( token,new AllListCallBack() {
            @Override
            public void success(boolean status, Response<AllSecondaryDetails> response) {
                if (status) {
                    AllSecondaryAccountDetails cl;
                    if (response.body() != null) {
                     /*   String token = response.headers().get("x-auth");
                        KsPreferenceKeys preference = KsPreferenceKeys.getInstance();
                        preference.setAppPrefAccessToken(token);*/
                        Gson gson = new Gson();
                        String tmp = gson.toJson(response.body());
                        AllSecondaryAccountDetails loginItemBean = gson.fromJson(tmp, AllSecondaryAccountDetails.class);
                        responseApi.postValue(loginItemBean);
                    } else {
                        AllSecondaryAccountDetails responseModel = ErrorCodesIntercepter.getInstance().allSecondaryAccountDetailsl(response);
                        responseApi.postValue(responseModel);
                    }
                }
            }

            @Override
            public void failure(boolean status, int errorCode, String errorMessage) {
                Logger.e("", "AllSecondaryResponse E" + errorMessage);
                AllSecondaryAccountDetails cl = new AllSecondaryAccountDetails();
                cl.setDebugMessage(errorMessage);
                responseApi.postValue(cl);
            }
        });

        return responseApi;
    }

    public LiveData<SecondaryUserDetailsJavaPojo> getSecondaryUserAPIReponse(String token,String userName) {
        final MutableLiveData<SecondaryUserDetailsJavaPojo> responseApi;
        responseApi = new MutableLiveData<>();

        BaseCategoryServices.Companion.getInstance().SecondaryUserService( token,userName,new SecondaryUserCallBack() {
            @Override
            public void success(boolean status, Response<SecondaryUserDetails> response) {
                if (status) {
                    SecondaryUserDetails cl;
                    if (response.body() != null) {
                        Gson gson = new Gson();
                        String tmp = gson.toJson(response.body());
                        SecondaryUserDetailsJavaPojo loginItemBean = gson.fromJson(tmp, SecondaryUserDetailsJavaPojo.class);
                        responseApi.postValue(loginItemBean);
                    } else {
                        SecondaryUserDetailsJavaPojo responseModel = ErrorCodesIntercepter.getInstance().secondaryUserDetails(response);
                        responseApi.postValue(responseModel);
                    }
                }
            }

            @Override
            public void failure(boolean status, int errorCode, String errorMessage) {
                Logger.e("", "SecondaryUser E" + errorMessage);
                SecondaryUserDetailsJavaPojo cl = new SecondaryUserDetailsJavaPojo();
                cl.setDebugMessage(errorMessage);
                responseApi.postValue(cl);
            }
        });

        return responseApi;
    }

    public LiveData<DeleteAccountResponse> deleteAccountReq(String token) {
        MutableLiveData<DeleteAccountResponse> deleteAccountResponseMutableLiveData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().deleteAccountReq(token,new DeleteAccountCallback(){

            @Override
            public void success(boolean status, @NotNull Response<DeleteAccountResponse> response) {
                try {
                    if (response.body()==null) {
                        if (response.errorBody()!=null) {
                            try{
                                JSONObject errorObject = new JSONObject(response.errorBody().string());
                                int responseCode = errorObject.getInt("responseCode");
                                String debugMessage = errorObject.getString("debugMessage");
                                if (responseCode !=0){
                                    DeleteAccountResponse deleteAccountResponse = new DeleteAccountResponse();
                                    if (responseCode == 4906) {
                                        deleteAccountResponse.setResponseCode(responseCode);
                                        deleteAccountResponse.setDebugMessage(debugMessage);
                                    } else {
                                        deleteAccountResponse.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                                    }
                                    deleteAccountResponseMutableLiveData.postValue(deleteAccountResponse);
                                } else {
                                    DeleteAccountResponse deleteAccountResponse = new DeleteAccountResponse();
                                    deleteAccountResponse.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                                    deleteAccountResponseMutableLiveData.postValue(deleteAccountResponse);
                                }
                            }
                            catch (Exception ignored){
                            }
                        } else {
                            DeleteAccountResponse deleteAccountResponse = new DeleteAccountResponse();
                            deleteAccountResponse.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                            deleteAccountResponseMutableLiveData.postValue(deleteAccountResponse);
                        }
                    } else {
                        if (response.isSuccessful()) {
                            int responseCode = response.body().getResponseCode();
                            if (responseCode !=0){
                                if (responseCode == 2001) {
                                    DeleteAccountResponse deleteAccountResponse = new DeleteAccountResponse();
                                    deleteAccountResponse.setResponseCode(responseCode);
                                    //deleteAccountResponse.setDebugMessage(debugMessage);
                                    deleteAccountResponseMutableLiveData.setValue(deleteAccountResponse);
                                }
                            }
                        } else {
                            DeleteAccountResponse deleteAccountResponse = new DeleteAccountResponse();
                            deleteAccountResponse.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                            deleteAccountResponseMutableLiveData.postValue(deleteAccountResponse);
                        }

                    }
                }
                catch (Exception e){
                    DeleteAccountResponse deleteAccountResponse = new DeleteAccountResponse();
                    deleteAccountResponse.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                    deleteAccountResponseMutableLiveData.postValue(deleteAccountResponse);
                }
            }
            @Override
            public void failure(boolean status, int errorCode, String message) {
                DeleteAccountResponse deleteAccountResponse = new DeleteAccountResponse();
                deleteAccountResponse.setResponseCode(AppConstants.RESPONSE_CODE_ERROR);
                deleteAccountResponseMutableLiveData.postValue(deleteAccountResponse);
            }
        });
        return deleteAccountResponseMutableLiveData;
    }


    public LiveData<com.enveu.client.joinContest.joinContestResponse.Response> checkUserContestApi(String token, int userId, int contestId, String languageCode) {
        MutableLiveData<com.enveu.client.joinContest.joinContestResponse.Response> joinContestMutableLiveData = new MutableLiveData<>();

        BaseCategoryServices.Companion.getInstance().checkContestForUser(token,userId,contestId,languageCode, new JoinContestCallback(){

            @Override
            public void failure(boolean status, int errorCode, @NonNull String message) {
                joinContestMutableLiveData.postValue(null);
            }

            @Override
            public void success(boolean status, @NonNull Response<com.enveu.client.joinContest.joinContestResponse.Response> joinContestResponse) {
                if (status && joinContestResponse.isSuccessful() && joinContestResponse.body().getData() != null) {
                    joinContestMutableLiveData.postValue(joinContestResponse.body());
                }else {
                    joinContestMutableLiveData.postValue(joinContestResponse.body());
                }
            }
        });

        return joinContestMutableLiveData;
    }

    public LiveData<com.enveu.client.joinContest.joinContestResponse.Response> joinContestForUser(String token, int userId, int contestId, String languageCode) {
        MutableLiveData<com.enveu.client.joinContest.joinContestResponse.Response> joinContestMutableLiveData = new MutableLiveData<>();

        BaseCategoryServices.Companion.getInstance().joinContestForUser(token,userId,contestId,languageCode, new JoinContestCallback(){

            @Override
            public void failure(boolean status, int errorCode, @NonNull String message) {
                joinContestMutableLiveData.postValue(null);
            }

            @Override
            public void success(boolean status, @NonNull Response<com.enveu.client.joinContest.joinContestResponse.Response> joinContestResponse) {
                if (status && joinContestResponse.isSuccessful() && joinContestResponse.body().getData() != null) {
                    joinContestMutableLiveData.postValue(joinContestResponse.body());
                }else {
                    joinContestMutableLiveData.postValue(joinContestResponse.body());
                }
            }
        });

        return joinContestMutableLiveData;
    }
}



