package com.tv.networking.intercepter

import android.util.Log
import com.enveu.client.bookmarking.bean.continuewatching.GetContinueWatchingBean
import com.enveu.client.userManagement.bean.LoginResponse.LoginResponseModel
import com.enveu.client.userManagement.bean.UserProfile.UserProfileResponse
import com.enveu.client.userManagement.bean.allSecondaryDetails.AllSecondaryDetails
import com.enveu.client.userManagement.bean.allSecondaryDetails.SecondaryUserDetails
import com.enveu.client.userManagement.bean.allSecondaryDetails.SwitchUserDetails
import com.enveu.client.userManagement.bean.authResponse.AuthResponse
import com.enveu.client.userManagement.bean.registrationOtp.OtpResponseModel
import com.tv.OttApplication
import com.tv.R
import com.tv.activities.purchase.purchase_model.PurchaseResponseModel
import com.tv.activities.usermanagment.model.OtpResponse
import com.tv.beanModel.emptyResponse.ResponseEmpty
import com.tv.beanModel.entitle.ResponseEntitle
import com.tv.beanModel.membershipAndPlan.ResponseMembershipAndPlan
import com.tv.beanModel.responseGetWatchlist.ResponseGetIsWatchlist
import com.tv.beanModel.responseIsLike.ResponseIsLike
import com.tv.beanModel.responseModels.SignUp.SignupResponseAccessToken
import com.tv.beanModel.responseModels.listAllAccounts.AllSecondaryAccountDetails
import com.tv.beanModel.responseModels.secondaryUserDetails.SecondaryUserDetailsJavaPojo
import com.tv.beanModel.responseModels.switchUserDetail.SwitchUser
import com.tv.redeemcoupon.RedeemCouponResponseModel
import com.tv.repository.redeemCoupon.RedeemModel
import com.tv.utils.Logger
import com.tv.utils.commonMethods.AppCommonMethod
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.utils.stringsJson.converter.StringsHelper
import org.json.JSONObject
import retrofit2.Response

class ErrorCodesIntercepter private constructor() {
    private val stringsHelper by lazy { StringsHelper }
    fun manualSignUp(response: Response<LoginResponseModel?>): SignupResponseAccessToken? {
        var responseModel: SignupResponseAccessToken? = null
        if (response.code() == ErrorCode409) {
            val cl = com.tv.beanModel.responseModels.LoginResponse.LoginResponseModel()
            cl.responseCode = 4901
            var debugMessage = ""
            try {
                val jObjError = JSONObject(response.errorBody()!!.string())
                debugMessage = jObjError.getString("debugMessage")
                Logger.e("", "" + jObjError.getString("debugMessage"))
                responseModel = SignupResponseAccessToken()
                responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.user_already_exists))
            } catch (e: Exception) {
                Logger.e("RegistrationLoginRepo", "" + e)
            }
            if (responseModel != null) {
                responseModel.responseModel = cl
            }
        } else if (response.code() == ErrorCode400) {
            val cl = com.tv.beanModel.responseModels.LoginResponse.LoginResponseModel()
            cl.responseCode = 400
            responseModel = SignupResponseAccessToken()
            try {
                val errorObject = JSONObject(response.errorBody()!!.string())
                if (errorObject.getInt("responseCode") != 0) {
                    if (errorObject.getInt("responseCode") == 4003) {
                        responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.password_cannot_be_blank))
                    } else if (errorObject.getInt("responseCode") == 4004) {
                        responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.please_provide_valid_name))
                    } else if (errorObject.getInt("responseCode") == 4005) {
                        responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.email_id_cannot_be_blank))
                    } else if (errorObject.getInt("responseCode") == 4017) {
                        responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.invalid_email_id))
                    } else {
                        responseModel.setDebugMessage(
                            stringsHelper.stringParse(
                                (stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString()),
                                OttApplication.getInstance().resources.getString(R.string.popup_something_went_wrong)
                            )
                        )
                    }
                } else {
                    responseModel.setDebugMessage(
                        stringsHelper.stringParse(
                            (stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString()),
                            OttApplication.getInstance().resources.getString(R.string.popup_something_went_wrong)
                        )
                    )
                }
                responseModel.responseModel = cl
            } catch (e: Exception) {
                responseModel.setDebugMessage(
                    stringsHelper.stringParse(
                        (stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString()),
                        OttApplication.getInstance().resources.getString(R.string.popup_something_went_wrong)
                    )
                )
                responseModel.responseModel = cl
            }
        }
        return responseModel
    }

    fun fbLogin(response: Response<LoginResponseModel?>): com.tv.beanModel.responseModels.LoginResponse.LoginResponseModel? {
        var cl: com.tv.beanModel.responseModels.LoginResponse.LoginResponseModel? = null
        try {
            cl = com.tv.beanModel.responseModels.LoginResponse.LoginResponseModel()
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                if (errorObject.getInt("responseCode") == 4301) {
                    cl.responseCode = 403
                    cl.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.either_user_email_not_found))
                } else if (errorObject.getInt("responseCode") == 4103) {
                    cl.responseCode = 400
                    cl.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.user_deactivated))
                } else if (errorObject.getInt("responseCode") == 4901) {
                    cl.responseCode = 400
                    cl.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.user_already_exists))
                } else if (errorObject.getInt("responseCode") == 4007) {
                    cl.responseCode = 400
                    cl.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.fb_id_cannot_be_null_or_empty))
                } else if (errorObject.getInt("responseCode") == 500) {
                    cl.responseCode = 400
                    cl.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end))
                } else {
                    cl.responseCode = 400
                    cl.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end))
                }
            }
        } catch (e: Exception) {
            if (cl != null) {
                cl.responseCode = 400
                cl.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end))
            }
        }
        return cl
    }

    fun Login(response: Response<LoginResponseModel?>): com.tv.beanModel.responseModels.LoginResponse.LoginResponseModel? {
        var responseModel: com.tv.beanModel.responseModels.LoginResponse.LoginResponseModel? = null
        try {
            responseModel = com.tv.beanModel.responseModels.LoginResponse.LoginResponseModel()
            val errorObject = JSONObject(response.errorBody()!!.string())
            responseModel.responseCode = 400
            if (errorObject.getInt("responseCode") != 0) {
                if (errorObject.getInt("responseCode") != 0) {
                    if (errorObject.getInt("responseCode") == 4003) {
                        responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.password_cannot_be_blank))
                    } else if (errorObject.getInt("responseCode") == 4004) {
                        responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.please_provide_valid_name))
                    } else if (errorObject.getInt("responseCode") == 4401) {
                        responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.popup_user_does_not_exists))
                    } else if (errorObject.getInt("responseCode") == 4103) {
                        responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.user_deactivated))
                    } else if (errorObject.getInt("responseCode") == 4006) {
                        responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.user_can_not_login))
                    } else if (errorObject.getInt("responseCode") == 4002) {
                        responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.username_password_doest_match))
                    } else if (errorObject.getInt("responseCode") == 4005) {
                        responseModel.setDebugMessage(OttApplication.getInstance().resources.getString(R.string.email_id_cannot_be_blank))
                    } else {
                        responseModel.setDebugMessage(
                            stringsHelper.stringParse(
                                (stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString()),
                                OttApplication.getInstance().resources.getString(R.string.popup_something_went_wrong)
                            )
                        )
                    }
                } else {
                    responseModel.setDebugMessage(
                        stringsHelper.stringParse(
                            (stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString()),
                            OttApplication.getInstance().resources.getString(R.string.popup_something_went_wrong)
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("ErrorCode", "Login: $e")
        }
        return responseModel
    }

    fun Auth(response: Response<AuthResponse?>): com.tv.bean_model_v1_0.authResponse.AuthResponse? {
        var responseModel: com.tv.bean_model_v1_0.authResponse.AuthResponse? = null
        try {
            responseModel = com.tv.bean_model_v1_0.authResponse.AuthResponse()
            val errorObject = JSONObject(response.errorBody()!!.string())
            responseModel.responseCode = 400
            if (errorObject.getInt("responseCode") != 0) {
                if (errorObject.getInt("responseCode") != 0) {
                    if (errorObject.getInt("responseCode") == 4003) {
                        responseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.password_cannot_be_blank)
                    } else if (errorObject.getInt("responseCode") == 4004) {
                        responseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.please_provide_valid_name)
                    } else if (errorObject.getInt("responseCode") == 4401) {
                        responseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.popup_user_does_not_exists)
                    } else if (errorObject.getInt("responseCode") == 4103) {
                        responseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.user_deactivated)
                    } else if (errorObject.getInt("responseCode") == 4006) {
                        responseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.user_can_not_login)
                    } else if (errorObject.getInt("responseCode") == 4002) {
                        responseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.username_password_doest_match)
                    } else if (errorObject.getInt("responseCode") == 4005) {
                        responseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.email_id_cannot_be_blank)
                    } else {
                        responseModel.debugMessage = stringsHelper.stringParse(
                            (stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString()),
                            OttApplication.getInstance().resources.getString(R.string.popup_something_went_wrong)
                        )
                    }
                } else {
                    responseModel.debugMessage = stringsHelper.stringParse(
                        (stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString()),
                        OttApplication.getInstance().resources.getString(R.string.popup_something_went_wrong)
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("ErrorCode", "Login: $e")
        }
        return responseModel
    }

    fun checkEntitlement(response: Response<ResponseEntitle?>): ResponseEntitle {
        val responseEntitlement = ResponseEntitle()
        try {
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4406) {
                    responseEntitlement.debugMessage = OttApplication.getInstance().resources.getString(R.string.no_valid_offer)
                    responseEntitlement.status = false
                } else if (code == 4001) {
                    responseEntitlement.debugMessage = OttApplication.getInstance().resources.getString(R.string.no_Subscription_managment)
                    responseEntitlement.status = false
                } else if (code == 4302) {
                    responseEntitlement.responseCode = 4302
                    Logger.w("languageValeu-->>", OttApplication.getInstance().resources.getString(R.string.you_are_logged_out))
                    responseEntitlement.debugMessage = OttApplication.getInstance().resources.getString(R.string.you_are_logged_out)
                } else if (code == 500) {
                    responseEntitlement.debugMessage = OttApplication.getInstance().resources.getString(R.string.server_error)
                    responseEntitlement.status = false
                }
            } else {
                responseEntitlement.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
                responseEntitlement.status = false
            }
        } catch (ignored: Exception) {
            responseEntitlement.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
            responseEntitlement.status = false
        }
        return responseEntitlement
    }

    fun checkPlans(response: Response<ResponseMembershipAndPlan?>?, responseEntitlement: ResponseMembershipAndPlan?): ResponseMembershipAndPlan? {
        return null
    }

    fun userProfile(response: Response<UserProfileResponse?>): com.tv.beanModel.userProfile.UserProfileResponse {
        val empty = com.tv.beanModel.userProfile.UserProfileResponse()
        try {
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4302) {
                    empty.status = false
                    empty.responseCode = 4302
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.you_are_logged_out)
                } else if (code == 500) {
                    empty.status = false
                    empty.responseCode = 500
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
                } else if (code == 4019) {
                    empty.status = false
                    empty.responseCode = 4019
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.number_Cannot_change)
                } else if (code == 4901) {
                    empty.status = false
                    empty.responseCode = 4901
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.already_exist_number)
                }
            }
        } catch (ignored: Exception) {
            empty.responseCode = 500
            empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
        }
        return empty
    }

    fun addToWatchlisht(response: Response<ResponseEmpty?>): ResponseEmpty {
        val empty = ResponseEmpty()
        try {
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4904) {
                    empty.isStatus = false
                    empty.responseCode = 4904
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.user_already_in_watchlist)
                }
                if (code == 4408) {
                    empty.isStatus = false
                    empty.responseCode = 4408
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.user_not_already_in_watchlist)
                } else if (code == 4302) {
                    empty.isStatus = false
                    empty.responseCode = 4302
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.you_are_logged_out)
                } else if (code == 500) {
                    empty.isStatus = false
                    empty.responseCode = 500
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
                }
            }
        } catch (ignored: Exception) {
            empty.responseCode = 500
            empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
        }
        return empty
    }

    fun redeemCoupon(response: Response<RedeemCouponResponseModel?>): RedeemModel {
        val empty = RedeemModel()
        try {
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                when (errorObject.getInt("responseCode")) {
                    4903 -> {
                        empty.isStatus = false
                        empty.responseCode = 4903
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.subscribed_already)
                    }
                    4044 -> {
                        empty.isStatus = false
                        empty.responseCode = 4044
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.redeem_code_already_used)
                    }
                    4046 -> {
                        empty.isStatus = false
                        empty.responseCode = 4046
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.redeem_cancelled)
                    }
                    4045 -> {
                        empty.isStatus = false
                        empty.responseCode = 4045
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.redeem_code_invalid)
                    }
                    4047 -> {
                        empty.isStatus = false
                        empty.responseCode = 4047
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.redeem_code_expired)
                    }
                    4049 -> {
                        empty.isStatus = false
                        empty.responseCode = 4049
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.please_try)
                    }
                    4404 -> {
                        empty.isStatus = false
                        empty.responseCode = 4404
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.cannot_find_other_offer)
                    }
                    4405 -> {
                        empty.isStatus = false
                        empty.responseCode = 4405
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.not_find_any_offer)
                    }
                    4409 -> {
                        empty.isStatus = false
                        empty.responseCode = 4409
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.currency_not_supported)
                    }
                    4410 -> {
                        empty.isStatus = false
                        empty.responseCode = 4410
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.cannot_find_any_subs)
                    }
                    4302 -> {
                        empty.isStatus = false
                        empty.responseCode = 4302
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.you_are_logged_out)
                    }
                    500 -> {
                        empty.isStatus = false
                        empty.responseCode = 500
                        empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
                    }
                }
            }
        } catch (ignored: Exception) {
            empty.responseCode = 500
            empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
        }
        return empty
    }

    fun likeAsset(response: Response<ResponseEmpty?>): ResponseEmpty {
        val empty = ResponseEmpty()
        try {
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4902) {
                    empty.isStatus = false
                    empty.responseCode = 4902
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.user_already_liked)
                } else if (code == 4403) {
                    empty.isStatus = false
                    empty.responseCode = 4403
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.user_already_liked)
                } else if (code == 4302) {
                    empty.isStatus = false
                    empty.responseCode = 4302
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.you_are_logged_out)
                } else if (code == 500) {
                    empty.isStatus = false
                    empty.responseCode = 500
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
                }
            }
        } catch (ignored: Exception) {
            empty.responseCode = 500
            empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
        }
        return empty
    }

    fun isWatchlist(response: Response<ResponseGetIsWatchlist?>): ResponseGetIsWatchlist {
        val empty = ResponseGetIsWatchlist()
        try {
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4302) {
                    empty.isStatus = false
                    empty.responseCode = 4302
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.you_are_logged_out)
                } else if (code == 500) {
                    empty.isStatus = false
                    empty.responseCode = 500
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
                }
            }
        } catch (ignored: Exception) {
            empty.responseCode = 500
            empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
        }
        return empty
    }

    fun otpResponse(response: Response<OtpResponseModel?>): OtpResponse? {
        var responseModel: OtpResponse? = null
        try {
            responseModel = OtpResponse()
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4056) {
                    responseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.invalid_otp)
                } else if (code == 4401) {
                    responseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.user_with_this_email_id_does_not_exist)
                } else if (code == 4428) {
                    responseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.no_subscription_configuration_available)
                } else if (code == 4076) {
                    responseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.otp_has_been_expired)
                } else {
                    val errorMsg = errorObject.getString("debugMessage")
                    responseModel.debugMessage = errorMsg
                }
            }
        } catch (e: Exception) {
            Logger.e("Exception", e.toString())
        }
        return responseModel
    }

    fun isLike(response: Response<ResponseIsLike?>): ResponseIsLike {
        val empty = ResponseIsLike()
        try {
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4302) {
                    empty.isStatus = false
                    empty.responseCode = 4302
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.you_are_logged_out)
                } else if (code == 500) {
                    empty.isStatus = false
                    empty.responseCode = 500
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
                }
            }
        } catch (ignored: Exception) {
            empty.responseCode = 500
            empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
        }
        return empty
    }

    fun createNewOrder(response: Response<PurchaseResponseModel?>): PurchaseResponseModel {
        val purchaseResponseModel = PurchaseResponseModel()
        try {
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4414) {
                    purchaseResponseModel.responseCode = 4414
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.payment_config_not_found)
                } else if (code == 4404) {
                    purchaseResponseModel.responseCode = 4404
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.cannot_find_other_offer)
                } else if (code == 4405) {
                    purchaseResponseModel.responseCode = 4405
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.not_find_any_offer)
                } else if (code == 4409) {
                    purchaseResponseModel.responseCode = 4409
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.currency_not_supported)
                } else if (code == 4410) {
                    purchaseResponseModel.responseCode = 4410
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.not_find_any_offer)
                } else if (code == 4302) {
                    purchaseResponseModel.responseCode = 4302
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.you_are_logged_out)
                } else if (code == 500) {
                    purchaseResponseModel.responseCode = 500
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
                }
            }
        } catch (ignored: Exception) {
            purchaseResponseModel.responseCode = 500
            purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
        }
        return purchaseResponseModel
    }

    fun initiateOrder(response: Response<PurchaseResponseModel?>): PurchaseResponseModel {
        val purchaseResponseModel = PurchaseResponseModel()
        try {
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                when (4008) { //errorObject.getInt("responseCode");
                    4423 -> {
                        purchaseResponseModel.responseCode = 4423
                        purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.order_not_found)
                    }
                    4008 -> {
                        purchaseResponseModel.responseCode = 4008
                        purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.order_completed_or_failed)
                    }
                    4009 -> {
                        purchaseResponseModel.responseCode = 4009
                        purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.payment_provider_not_Supported)
                    }
                    4010 -> {
                        purchaseResponseModel.responseCode = 4010
                        purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.no_config_found)
                    }
                    4302 -> {
                        purchaseResponseModel.responseCode = 4302
                        purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.you_are_logged_out)
                    }
                    500 -> {
                        purchaseResponseModel.responseCode = 500
                        purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
                    }
                }
            }
        } catch (ignored: Exception) {
            purchaseResponseModel.responseCode = 500
            purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
        }
        return purchaseResponseModel
    }

    fun updateOrder(response: Response<PurchaseResponseModel?>): PurchaseResponseModel {
        val purchaseResponseModel = PurchaseResponseModel()
        try {
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4423) {
                    purchaseResponseModel.responseCode = 4423
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.order_not_found)
                } else if (code == 4008) {
                    purchaseResponseModel.responseCode = 4008
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.order_completed_or_failed)
                } else if (code == 4424) {
                    purchaseResponseModel.responseCode = 4424
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.payment_id_not_found)
                } else if (code == 4011) {
                    purchaseResponseModel.responseCode = 4011
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.purchase_token_required)
                } else if (code == 4013) {
                    purchaseResponseModel.responseCode = 4013
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.payment_validation_failed)
                } else if (code == 4302) {
                    purchaseResponseModel.responseCode = 4302
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.you_are_logged_out)
                } else if (code == 4012) {
                    purchaseResponseModel.responseCode = 4012
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.receipt_validation_url_not_avail)
                } else if (code == 4010) {
                    purchaseResponseModel.responseCode = 4010
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.no_config_found)
                } else if (code == 4009) {
                    purchaseResponseModel.responseCode = 4009
                    purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.payment_provider_not_Supported)
                }
            }
        } catch (ignored: Exception) {
            purchaseResponseModel.responseCode = 500
            purchaseResponseModel.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
        }
        return purchaseResponseModel
    }

    fun continueWatch(response: Response<GetContinueWatchingBean?>): GetContinueWatchingBean {
        val empty = GetContinueWatchingBean()
        try {
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4302) {
                    empty.isStatus = false
                    empty.responseCode = java.lang.Long.valueOf(4302)
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.you_are_logged_out)
                } else if (code == 500) {
                    empty.isStatus = false
                    empty.responseCode = java.lang.Long.valueOf(500)
                    empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
                } else {
                    empty.isStatus = false
                    empty.responseCode = 500L
                }
            }
        } catch (ignored: Exception) {
            empty.responseCode = java.lang.Long.valueOf(500)
            empty.debugMessage = OttApplication.getInstance().resources.getString(R.string.something_went_wrong_at_our_end)
        }
        return empty
    }

    fun allSecondaryAccountDetailsl(response: Response<AllSecondaryDetails?>): AllSecondaryAccountDetails? {
        var responseModel: AllSecondaryAccountDetails? = null
        try {
            responseModel = AllSecondaryAccountDetails()
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4302) {
                    //  responseModel.setStatus(false);
                    // responseModel.setResponseCode("");
                    responseModel.debugMessage = "User must be logged in"
                } else {
                    val errorMsg = errorObject.getString("debugMessage")
                    responseModel.debugMessage = errorMsg
                }
            }
        } catch (e: Exception) {
            Logger.e("Exception", e.toString())
        }
        return responseModel
    }

    fun secondaryUserDetails(response: Response<SecondaryUserDetails?>): SecondaryUserDetailsJavaPojo? {
        var responseModel: SecondaryUserDetailsJavaPojo? = null
        try {
            responseModel = SecondaryUserDetailsJavaPojo()
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4302) {
                    //  responseModel.setStatus(false);
                    // responseModel.setResponseCode("");
                    responseModel.debugMessage = "User must be logged in"
                } else {
                    val errorMsg = errorObject.getString("debugMessage")
                    responseModel.debugMessage = errorMsg
                }
            }
        } catch (e: Exception) {
            Logger.e("Exception", e.toString())
        }
        return responseModel
    }

    fun switchUserDetails(response: Response<SwitchUserDetails?>): SwitchUser? {
        var responseModel: SwitchUser? = null
        try {
            responseModel = SwitchUser()
            val errorObject = JSONObject(response.errorBody()!!.string())
            if (errorObject.getInt("responseCode") != 0) {
                val code = errorObject.getInt("responseCode")
                if (code == 4302) {
                    //  responseModel.setStatus(false);
                    // responseModel.setResponseCode("");
                    responseModel.debugMessage = "User must be logged in"
                } else {
                    val errorMsg = errorObject.getString("debugMessage")
                    responseModel.debugMessage = errorMsg
                }
            }
        } catch (e: Exception) {
            Logger.e("Exception", e.toString())
        }
        return responseModel
    }

    companion object {
        @JvmStatic
        var instance: ErrorCodesIntercepter? = null
            get() {
                if (field == null) {
                    field = ErrorCodesIntercepter()
                    appInstance = OttApplication.getInstance()
                }
                if (KsPreferenceKeys.getInstance().appLanguage.equals("spanish", ignoreCase = true) || KsPreferenceKeys.getInstance().appLanguage.equals("हिंदी", ignoreCase = true)) {
                    AppCommonMethod.updateLanguage("es", OttApplication.getInstance())
                } else if (KsPreferenceKeys.getInstance().appLanguage.equals("English", ignoreCase = true)) {
                    AppCommonMethod.updateLanguage("en", OttApplication.getInstance())
                }
                return field
            }
            private set
        var appInstance: OttApplication? = null
        var ErrorCode409 = 409
        var ErrorCode400 = 400
    }
}