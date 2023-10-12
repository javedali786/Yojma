package com.tv.uscreen.yojmatv.activities.usermanagment.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.enveu.client.deleteAccount.DeleteAccountResponse;
import com.enveu.client.joinContest.joinContestResponse.Response;
import com.tv.uscreen.yojmatv.activities.usermanagment.model.OtpResponse;
import com.tv.uscreen.yojmatv.beanModel.LoginDeviceModel.LoginDeviceModel;
import com.tv.uscreen.yojmatv.beanModel.enveuCommonRailData.RailCommonData;
import com.tv.uscreen.yojmatv.beanModel.forgotPassword.CommonResponse;
import com.tv.uscreen.yojmatv.beanModel.responseModels.LoginResponse.LoginResponseModel;
import com.tv.uscreen.yojmatv.beanModel.responseModels.SignUp.SignupResponseAccessToken;
import com.tv.uscreen.yojmatv.beanModel.responseModels.listAllAccounts.AllSecondaryAccountDetails;
import com.tv.uscreen.yojmatv.beanModel.responseModels.secondaryUserDetails.SecondaryUserDetailsJavaPojo;
import com.tv.uscreen.yojmatv.beanModel.userProfile.UserProfileResponse;
import com.tv.uscreen.yojmatv.bean_model_v1_0.authResponse.AuthResponse;
import com.tv.uscreen.yojmatv.repository.userManagement.RegistrationLoginRepository;


public class RegistrationLoginViewModel extends AndroidViewModel {

    final RegistrationLoginRepository loginRepository;

    public RegistrationLoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = RegistrationLoginRepository.getInstance();
    }

    public LiveData<LoginResponseModel> hitLoginAPI( String userName, String pwd) {
        return loginRepository.getLoginAPIResponse(userName, pwd);
    }

    public LiveData<LoginDeviceModel> getLoginDevice(String token){
        return loginRepository.getLoginDevice(token);
    }
    public LiveData<AuthResponse> getAuthResponse() {
        return loginRepository.getAuthResponse();
    }
    public LiveData<AllSecondaryAccountDetails> hitAllSecondaryApi(Context context,String token) {
        return loginRepository.getSecondaryAPIResponse(context,token);
    }

    public LiveData<SecondaryUserDetailsJavaPojo> hitSecondaryUser(String token, String userName) {
        return loginRepository.getSecondaryUserAPIReponse(token,userName);
    }

    public LiveData<SignupResponseAccessToken> hitSignUpAPI(String name, String userName, String dob,String pwd, boolean isNotificationEnable) {
        return loginRepository.getSignupAPIResponse(name, userName,dob, pwd,isNotificationEnable);
    }
    public LiveData<CommonResponse> hitForgotPasswordApi(String email) {
        return loginRepository.getForgotPasswordAPIResponse(email);
    }

    public LiveData<LoginResponseModel> hitApiChangePwd(String email, String token,Context context) {
        return loginRepository.getChangePwdAPIResponse(email, token,context);
    }
    public LiveData<LoginResponseModel> hitFbLogin(Context context, String email, String fbToken, String name, String fbId,String pic, boolean isEmail) {
        return loginRepository.getFbLogin(context, email, fbToken, name, fbId,pic, isEmail);
    }

    public LiveData<LoginResponseModel> hitApiForceFbLogin(Context context, String email, String fbToken, String name, String fbId, String profilePic, boolean isEmail) {
        return loginRepository.getForceFbLogin(context, email, fbToken, name, fbId, profilePic, isEmail);
    }

    public LiveData<UserProfileResponse> hitUserProfile(Context context, String token) {
        return loginRepository.getUserProfile(context,token);
    }
    public LiveData<UserProfileResponse> hitUpdateProfile(Context context, String token, String name, String mobile, String spinnerValue, String dob, String address, String imageUrl, String via, String contentPreference, boolean isNotificationEnable,String pin,String city,String country,String profile,String species,String type) {
        return loginRepository.getUpdateProfile(context,token,name,mobile,spinnerValue,dob,address,imageUrl,via,contentPreference,isNotificationEnable,pin,city,country,profile,species,type);
    }

    public LiveData<RailCommonData> getEpgListing(Context context, String channelId, Long startDate , Long endDate, int pageNumber, int pageSize, String languageCode) {
        return loginRepository.getEpgListing(context,channelId,startDate,endDate,pageNumber,pageSize,languageCode);
    }

    public LiveData<OtpResponse> generateOTPCode(String token) {
        return loginRepository.getGenerateOTPResponse(token);

    }
    public LiveData<OtpResponse> otpVerify(int otp, String token) {
        return loginRepository.getInstance().getOTPVerify(otp, token);
    }


    public LiveData<DeleteAccountResponse> deleteAccount(String token) {
        return loginRepository.getInstance().deleteAccountReq(token);
    }

    public LiveData<Response> checkUserContestApi(String token, int userId, int contestId, String languageCode) {
        return loginRepository.getInstance().checkUserContestApi(token,userId,contestId,languageCode);
    }
    public LiveData<Response> joinContestForUSer(String token, int userId, int contestId, String languageCode) {
        return loginRepository.getInstance().joinContestForUser(token,userId,contestId,languageCode);
    }
}
