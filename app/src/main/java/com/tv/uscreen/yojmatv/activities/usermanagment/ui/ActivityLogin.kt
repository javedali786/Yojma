package com.tv.uscreen.yojmatv.activities.usermanagment.ui


import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookAuthorizationException
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.Gson
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.activities.homeactivity.ui.HomeActivity
import com.tv.uscreen.yojmatv.activities.purchase.plans_layer.GetPlansLayer
import com.tv.uscreen.yojmatv.activities.usermanagment.viewmodel.RegistrationLoginViewModel
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.responseModels.LoginResponse.Data
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.AppleSignInListener
import com.tv.uscreen.yojmatv.databinding.ActivityLoginBinding
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.AppColors
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper.strokeBgDrawable
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.AppleSignInManager
import com.tv.uscreen.yojmatv.utils.helpers.CheckInternetConnection
import com.tv.uscreen.yojmatv.utils.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import org.json.JSONException
import org.json.JSONObject
import java.util.Arrays
import java.util.Objects


class ActivityLogin : BaseBindingActivity<ActivityLoginBinding?>(), CommonDialogFragment.EditDialogListener,
    AppleSignInListener {
    private val permissionNeeds = Arrays.asList("email", "public_profile")
    var font: Typeface? = null
    private var viewModel: RegistrationLoginViewModel? = null
    private var email: String? = null
    private var modelLogin: Data? = null
    private var preference: KsPreferenceKeys? = null
    private lateinit var password: String
    private var accessTokenFB: String? = null
    private var callbackManager: CallbackManager? = null
    var hasFbEmail = false
    var isFbLoginClick = false
    private var mLastClickTime: Long = 0
    private var name = ""
    private var id = ""
    private var jwtToken = ""
    private var type = ""
    private var fromSplash:Boolean? = false
    private var socialLoginToken = ""
    val regexPass: Regex = Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=\\S+\$).{8,16}\$")
    val regex: Regex = Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,100}")
    private var isSubscriptionPurchased = true
    private val colorsHelper by lazy { ColorsHelper }
    private val stringsHelper by lazy { StringsHelper }

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding?.stringData = stringsHelper
        binding?.colorsData = colorsHelper
        binding?.toolbar?.colorsData = colorsHelper
        binding?.toolbar?.stringData = stringsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
        parseColor()
        binding?.signIn?.paintFlags = binding!!.signIn.paintFlags or Paint.UNDERLINE_TEXT_FLAG
       /* val bundle = intent.extras
        fromSplash = intent.getBooleanExtra("fromSplash",false)
        if (fromSplash!!){
            binding?.toolbar!!.backLayout.visibility = View.GONE
        }*/
        setClicks()
        connectionObserver()
        viewModel = ViewModelProvider(this@ActivityLogin).get(
            RegistrationLoginViewModel::class.java
        )
        preference = KsPreferenceKeys.getInstance()
    }

    private fun setClicks() {
        callbackManager = create()
        FacebookSdk.fullyInitialize();
        binding?.fbButton?.setReadPermissions(permissionNeeds);
        binding?.confirmPasswordEye?.isChecked = false
        binding?.toolbar?.backLayout?.visibility = View.VISIBLE
        binding?.toolbar?.titleSkip?.visibility = View.GONE
        binding?.toolbar?.logoMain2?.visibility = View.VISIBLE
        binding?.toolbar?.searchIcon?.visibility = View.GONE

        binding?.toolbar?.backLayout?.setOnClickListener {
            onBackPressed()
        }
        binding?.signIn?.setOnClickListener {
            clearEditView()
            val i = Intent(this@ActivityLogin, ActivitySignUp::class.java)
            startActivity(i)

        }

        binding?.forgetPassword?.setOnClickListener {
            clearEditView()
            val i = Intent(this@ActivityLogin, ActivityForgotPassword::class.java)
            startActivity(i)
        }
        binding?.signUp?.setOnClickListener {
            Log.d("Melody", "setClicks: ")
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            hideSoftKeyboard(binding?.signUp)
            if (CheckInternetConnection.isOnline(this@ActivityLogin)) {
                if (validateEmptyEmail() && validateEmail() && validateEmptyPassword()) {
                    callLoginApi()

                }
            } else {
                commonDialog(
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "", stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                        getString(R.string.popup_continue)
                    )
                )

            }
        }

        binding?.appleImg?.setOnClickListener {
            showLoading(binding?.progressBar, true)
            viewModel?.authResponse?.observe(this, Observer {
                if (it != null) {
                    dismissLoading(binding?.progressBar)
                    Log.d("LoginResponse", Gson().toJson(it))
                    try {
                        if (it.responseCode == 2000 && it.data.authURL != null) {
                            AppleSignInManager.setContext(this)
                            AppleSignInManager.setUrl(it.data.authURL)
                            AppleSignInManager.setupAppleWebViewDialog()
                        } else if (it.debugMessage != null) {
                            commonDialog(
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ), it.debugMessage.toString()
                                , stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                    getString(R.string.popup_continue)
                                )
                            )
                        } else {
                            commonDialog(
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ), stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                    getString(R.string.popup_something_went_wrong)
                                ), stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                    getString(R.string.popup_continue)
                                )
                            )
                        }

                    } catch (e: Exception) {
                        Logger.w(e)
                    }
                } else {
                    commonDialog(
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_error.toString(),
                            getString(R.string.popup_error)
                        ), stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                            getString(R.string.popup_something_went_wrong)
                        ), stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                            getString(R.string.popup_continue)
                        )
                    )

                }
            })
        }

        binding?.facebookImg?.setOnClickListener {
            hideSoftKeyboard(binding?.facebookImg)
            if (CheckInternetConnection.isOnline(this@ActivityLogin)) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                    return@setOnClickListener
                }
                mLastClickTime = SystemClock.elapsedRealtime()
                clearEditView()
                isFbLoginClick = true
                binding?.fbButton?.performClick()

            } else {
                commonDialog(
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), ""
                    , stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                        getString(R.string.popup_continue)
                    )
                )

            }
        }

        binding?.fbButton?.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onCancel() {
                println("onCancel")
            }

            override fun onError(error: FacebookException) {
                if (error is FacebookAuthorizationException) LoginManager.getInstance().logOut()
            }

            override fun onSuccess(result: LoginResult?) {
                println("onSuccess")
                if (result != null) {
                    accessTokenFB = result.accessToken.token
                }
                val request = GraphRequest.newMeRequest(
                    result?.accessToken
                ) { `object`: JSONObject?, response: GraphResponse? ->
                    Logger.i(
                        "LoginActivity", response.toString()
                    )
                    try {
                        id = `object`!!.getString("id")
                        name = `object`.getString("name")
                        if (`object`.has("email")) {
                            email = `object`.getString("email")
                            hasFbEmail = true
                        } else hasFbEmail = false
                        showHideProgress(getBinding()!!.progressBar)
                        hitApiFBLogin()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                val parameters = Bundle()
                parameters.putString(
                    "fields",
                    "id,name,email"
                )
                request.parameters = parameters
                request.executeAsync()
            }
        })
//        setTextWatcher()
//        setTextWatcherForEmail()
        binding?.confirmPasswordEye?.setOnCheckedChangeListener { compoundButton, b ->
            if (!b) {
                binding?.password?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding?.password?.typeface = font
            } else {
                binding?.password?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding?.password?.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                hideSoftKeyboard(binding?.confirmPasswordEye)
                binding?.password?.typeface = font
            }
        }
    }

    private fun parseColor() {
        val textColorStates = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
            intArrayOf(AppColors.pwdSelectedEyeColor(), AppColors.pwdUnselectedEyeColor())
        )
        binding?.confirmPasswordEye?.buttonDrawable?.setTintList(textColorStates)
        binding?.name?.background = strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        binding?.password?.background = strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        binding?.forgetPassword?.background = strokeBgDrawable(AppColors.clearColor(), AppColors.clearColor(), 10f)
    }

    private fun setTextWatcherForEmail() {
        binding?.name?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
//                editTextEmptyCheck()
            }
        })
    }


    private fun callLoginApi() {
        showLoading(binding?.progressBar, true)
        preference?.appPrefAccessToken = ""
        viewModel?.hitLoginAPI(binding?.name?.text.toString(), binding?.password?.text.toString())?.observe(this, Observer {
            if (it != null) {
                dismissLoading(binding?.progressBar)
                Log.d("LoginResponse", Gson().toJson(it))

                try {
                    if (it.responseCode == 2000) {
                        val gson = Gson()
                        val loginData: Data = it.data
                        modelLogin = it.data
                        val stringJson = gson.toJson(loginData)
                         callLoginDevice(stringJson)
                    } else if (it.debugMessage != null) {
                        commonDialog(
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                getString(R.string.popup_error)
                            ), stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_username_pwd_does_not_match.toString(),
                                getString(R.string.popup_username_pwd_does_not_match)
                            ), stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                getString(R.string.popup_continue)
                            )
                        )
                    } else {
                        commonDialog(
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                getString(R.string.popup_error)
                            ), stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                getString(R.string.something_went_wrong)
                            ), stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                getString(R.string.popup_continue)
                            )
                        )
                    }
                } catch (e: NullPointerException) {
                    Logger.w(e)
                }
            } else {
                commonDialog(
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_error.toString(),
                        getString(R.string.popup_error)
                    ), stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                        getString(R.string.something_went_wrong)
                    ), stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                        getString(R.string.popup_continue)
                    )
                )
            }
        })

    }

    fun hitApiFBLogin() {
        if (CheckInternetConnection.isOnline(this@ActivityLogin)) {
            showLoading(getBinding()?.progressBar, true)
            viewModel?.hitFbLogin(
                this@ActivityLogin, email, accessTokenFB, name, id, "", hasFbEmail
            )?.observe(this@ActivityLogin) { loginResponseModelResponse ->
                if (Objects.requireNonNull(loginResponseModelResponse).responseCode == 2000) {
                    val gson = Gson()
                    modelLogin = loginResponseModelResponse.data
                    val stringJson = gson.toJson(loginResponseModelResponse.data)
                    Log.d("stringJson", stringJson)
                    saveUserDetails(stringJson, false)
                    Log.d("stringJson", loginResponseModelResponse.data.isVerified.toString())


                } else if (loginResponseModelResponse.responseCode === 403) {
                    ActivityLauncher.getInstance().forceLogin(this@ActivityLogin, ActivityForgotPassword::class.java, accessTokenFB, id, name, "", true)
                } else {
                    dismissLoading(getBinding()?.progressBar)
                    commonDialog(stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                getString(R.string.popup_error)
                            )
                        , loginResponseModelResponse.debugMessage.toString(),
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_ok.toString(),
                            getString(R.string.popup_ok)))
                }
            }
        } else {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                    getString(R.string.popup_no_internet_connection_found)
                ), "", stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)
                )
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        } catch (e: java.lang.Exception) {
            Logger.e("LoginActivity", "" + e.toString())
        }
    }


    private fun saveUserDetails(stringJson: String?, b: Boolean) {
        val fbLoginData: Data = Gson().fromJson(stringJson, Data::class.java)
        val gson = Gson()
        val stringJson = gson.toJson(fbLoginData)
        if (b) {
            preference?.appPrefLoginType = AppConstants.UserLoginType.Manual.toString()
        } else {
            preference?.appPrefLoginType = AppConstants.UserLoginType.FbLogin.toString()
        }
        AppConstants.UserLoginType.FbLogin.toString()
        preference?.appPrefProfile = stringJson
        preference?.appPrefLoginStatus = AppConstants.UserStatus.Login.toString()
        preference?.appPrefUserId = fbLoginData.id.toString()
        preference?.appPrefUserEmail = fbLoginData.email.toString()
        preference?.setIsVerified(fbLoginData.isVerified.toString())
        preference?.expirytime = fbLoginData.expiryTime.toString()
        if (fbLoginData.name != null) {
            preference?.appPrefUserName = fbLoginData.name.toString()
        }

        if (fbLoginData.isVerified) {
            ActivityLauncher.getInstance()
                .homeActivity(this@ActivityLogin, HomeActivity::class.java)
        } else{
            ActivityLauncher.getInstance()
                .goToEnterOTP(this@ActivityLogin, EnterOTPActivity::class.java,"login")
        }

      //  checkPlansForUser()
    }

    private fun checkPlansForUser() {
        showLoading(binding?.progressBar, true)
        val token: String = KsPreferenceKeys.getInstance().appPrefAccessToken
        GetPlansLayer.getInstance().getEntitlementStatus(KsPreferenceKeys.getInstance(), token) { entitlementStatus, apiStatus, _ ->
            dismissLoading(binding?.progressBar)
            if (apiStatus) {
                if (entitlementStatus) {
                    if (KsPreferenceKeys.getInstance().isVerified.equals("true", ignoreCase = true)) {
                        ActivityLauncher.getInstance()
                            .homeScreen(this@ActivityLogin, HomeActivity::class.java)
                    } else {
                        ActivityLauncher.getInstance().goToEnterOTP(
                            this,
                            EnterOTPActivity::class.java, "Login"
                        )
                    }
                } else {
                    ActivityLauncher.getInstance().goToPlanScreen(
                        this@ActivityLogin,
                        ActivitySelectSubscriptionPlan::class.java,
                        "Login"
                    )
                }
            } else {
                ActivityLauncher.getInstance()
                    .homeScreen(this@ActivityLogin, HomeActivity::class.java)
            }
        }
    }

    private fun validateEmptyEmail(): Boolean {
        var check = false
        email = binding?.name?.text.toString()
        if (StringUtils.isNullOrEmptyOrZero(email)) {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_empty_email_tittle.toString(),
                    getString(R.string.popup_empty_email_tittle)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_empty_email_subtitle.toString(),
                    getString(R.string.popup_empty_email_subtitle)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)
                )
            )
        } else {
            check = true
        }
        return check
    }

    private fun validateEmail(): Boolean {
        var check = false
        if (binding?.name?.text.toString().trim().matches(regex)) {
            check = true
        } else {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_invalid_email_tittle.toString(),
                    getString(R.string.popup_invalid_email_tittle)
                ), stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_invalid_email_subtitle.toString(),
                    getString(R.string.popup_invalid_email_subtitle)
                ), stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)
                )
            )
        }
        return check
    }

    private fun validateEmptyPassword(): Boolean {
        var check = false
        if (binding?.password?.text.toString().trim() != "") {
            password = binding?.password?.text.toString().trim().matches(regexPass).toString()
            if ((binding?.password?.text.toString().trim().matches(regexPass))) {
                check = true
            } else {
                commonDialog(
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_incorrect_pwd_tittle.toString(),
                        getString(R.string.popup_incorrect_pwd_tittle)
                    ), stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_pwd_must_be_8_to_16_char.toString(),
                        getString(R.string.popup_pwd_must_be_8_to_16_char)
                    ), stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                        getString(R.string.popup_continue)
                    )
                )
            }
        }else{
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_empty_Password.toString(),
                    getString(R.string.popup_empty_Password)
                ), stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_pwd_must_be_8_to_16_char.toString(),
                    getString(R.string.popup_pwd_must_be_8_to_16_char)
                ), stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)
                )
            )
        }
        return check
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            binding?.loginMainFrame?.visibility = View.VISIBLE
            binding?.toolbar?.rlToolBar?.visibility = View.VISIBLE
            binding?.connection?.noConnectionLayout?.visibility = View.GONE
        } else {
            noConnectionLayout()
        }
    }

    private fun connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true)
        } else {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                    getString(R.string.popup_no_internet_connection_found)
                ), "", stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)
                )
            )
        }
    }

    private fun noConnectionLayout() {
        binding?.toolbar?.rlToolBar?.visibility = View.GONE
        binding?.loginMainFrame?.visibility = View.GONE
        binding?.connection?.noConnectionLayout?.visibility = View.VISIBLE
        binding?.connection?.retryTxt?.setOnClickListener { view -> connectionObserver() }
    }


    private fun setTextWatcher() {
        binding?.password?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            @RequiresApi(api = Build.VERSION_CODES.M)
            override fun afterTextChanged(editable: Editable) {
//                editTextEmptyCheck()
            }
        })
    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm: FragmentManager = supportFragmentManager
        val commonDialogFragment = CommonDialogFragment.newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }

    private fun callLoginDevice(stringJson:String){
        viewModel!!.getLoginDevice(KsPreferenceKeys.getInstance().appPrefAccessToken)?.observe(this){
            if(it.data!=null && it !=null) {
                if(it.responseCode == 2000) {
                    clearEditView()
                    saveUserDetails(stringJson, true)
                } else if (it.debugMessage != null) {
                    commonDialog(
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_error.toString(),
                            getString(R.string.popup_error)
                        ), stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_username_pwd_does_not_match.toString(),
                            getString(R.string.popup_username_pwd_does_not_match)
                        ), stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                            getString(R.string.popup_continue)
                        )
                    )
                } else {
                    commonDialog(
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_error.toString(),
                            getString(R.string.popup_error)
                        ), stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                            getString(R.string.something_went_wrong)
                        ), stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                            getString(R.string.popup_continue)
                        )
                    )
                }
            }


        }
    }

    override fun onActionBtnClicked() {
//        val i = Intent(this@ActivityLogin, ActivitySelectSubscriptionPlan::class.java)
//        startActivity(i)

    }

    private fun clearEditView() {
        binding?.name?.setText("")
        binding?.password?.setText("")
    }

    override fun onAppSignInSuccess(
        jwt_token: String
    ) {
        runOnUiThread {
            try {
                jwtToken = jwt_token
                val token: String = jwt_token
                val preference = KsPreferenceKeys.getInstance()
                preference.appPrefAccessToken = token
                var signInResponse = AppCommonMethod.getDecodedJwt(jwt_token)
                val separated: Array<String> = signInResponse.split("}").toTypedArray()
                var firstIndex = separated[0]
                var lastIndex = separated[1]
                if (lastIndex != null) {
                    saveUserDetails(lastIndex + "}", true)
                    //ActivityLauncher.getInstance().homeslider(this@ActivityLogin, HomeSliderActivity::class.java)
                }

            } catch (e: Exception) {
                Logger.w(e)
            }

        }
    }

    override fun onAppSignInError() {
        runOnUiThread {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                    getString(R.string.popup_error)
                ), stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                    getString(R.string.something_went_wrong)
                ), stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)
                )
            )
        }
    }
}