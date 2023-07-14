package com.breadgangtvnetwork.activities.usermanagment.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.breadgangtvnetwork.R
import com.breadgangtvnetwork.activities.homeactivity.ui.HomeActivity
import com.breadgangtvnetwork.activities.usermanagment.viewmodel.RegistrationLoginViewModel
import com.breadgangtvnetwork.baseModels.BaseBindingActivity
import com.breadgangtvnetwork.beanModel.responseModels.LoginResponse.Data
import com.breadgangtvnetwork.databinding.ActivityForgotPasswordBinding
import com.breadgangtvnetwork.fragments.dialog.CommonDialogFragment
import com.breadgangtvnetwork.utils.Logger
import com.breadgangtvnetwork.utils.colorsJson.converter.AppColors
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper
import com.breadgangtvnetwork.utils.colorsJson.converter.ColorsHelper.strokeBgDrawable
import com.breadgangtvnetwork.utils.constants.AppConstants
import com.breadgangtvnetwork.utils.helpers.CheckInternetConnection
import com.breadgangtvnetwork.utils.helpers.NetworkConnectivity
import com.breadgangtvnetwork.utils.helpers.intentlaunchers.ActivityLauncher
import com.breadgangtvnetwork.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.breadgangtvnetwork.utils.stringsJson.converter.StringsHelper
import com.google.gson.Gson
import java.util.Objects

class ActivityForgotPassword : BaseBindingActivity<ActivityForgotPasswordBinding?>(),
    CommonDialogFragment.EditDialogListener {
    val regex: Regex = Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,100}")
    private var viewModel: RegistrationLoginViewModel? = null
    private var errorDialog = false
    private lateinit var email:String
    private var preference: KsPreferenceKeys? = null
    private var name: String? = null
    private var fbId: String? = null
    private var accessToken: String? = null
    private var forceLogin: Boolean? = null
    private var isRequestSent: Boolean? = null
    private var modelLogin: Data? = null
    private val colorsHelper by lazy { ColorsHelper }
    private val stringsHelper by lazy { StringsHelper }

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityForgotPasswordBinding {
        return ActivityForgotPasswordBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ThemeHandler.getInstance().applyActivityForgotPassword(this,binding)
        parseColor()
        viewModel = ViewModelProvider(this@ActivityForgotPassword)[RegistrationLoginViewModel::class.java]
        callBinding()
        setClicks()
    }

    private fun parseColor() {
        binding?.colorsData = colorsHelper
        binding?.stringData = stringsHelper
        binding?.toolbar?.colorsData = colorsHelper
        binding?.toolbar?.stringData = stringsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
        binding?.llnumber?.background = strokeBgDrawable(AppColors.popupBgColor(), AppColors.popupBrColor(), 10f)
        binding?.etPasswordRecoveryEmail?.background = strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
    }

    fun callBinding() {
        if (CheckInternetConnection.isOnline(this@ActivityForgotPassword)) {
            val extra = intent.extras
            if (extra != null) {
                getBundleValue()
            }

        } else {
            commonDialog(stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "", stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
        }
    }

    fun getBundleValue() {
        if (intent.hasExtra(AppConstants.EXTRA_REGISTER_USER)) {
            var bundle: Bundle? = intent.extras
            if (bundle != null) {
                bundle = bundle.getBundle(AppConstants.EXTRA_REGISTER_USER)
                name = bundle?.getString("fbName")
                fbId = bundle?.getString("fbId")
                accessToken = bundle?.getString("fbToken")
                forceLogin = bundle?.getBoolean("forceLogin")

            }

        }

    }


    private fun setClicks() {
        binding?.toolbar?.logoMain2?.visibility =View.VISIBLE
        binding?.toolbar?.titleSkip?.visibility = View.GONE
        binding?.toolbar?.backLayout?.setOnClickListener { onBackPressed() }
//        setTextWatcher()
        binding?.continueBtnOne?.setOnClickListener{
            hideSoftKeyboard(binding?.continueBtnOne)
            if (CheckInternetConnection.isOnline(this@ActivityForgotPassword)) {
                if (validateEmail()){
                    if(forceLogin == true)
                        callForceFbApi()
                    else
                        callForgotApi()
                }
            }else{
                commonDialog(stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "", stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)))
            }

        }
    }


    fun callForceFbApi() {
        if (CheckInternetConnection.isOnline(this@ActivityForgotPassword)) {
            showLoading(binding?.progressBar, true)
            forceLogin?.let {
                viewModel?.hitApiForceFbLogin(this@ActivityForgotPassword, binding?.etPasswordRecoveryEmail?.text.toString().trim(),accessToken, name, fbId, "", it
                )?.observe(this@ActivityForgotPassword) { loginResponseModelResponse ->
                    if (Objects.requireNonNull(loginResponseModelResponse).responseCode == 2000) {
                        val gson = Gson()
                        modelLogin = loginResponseModelResponse.data
                        val stringJson = gson.toJson(loginResponseModelResponse.data)
                        Log.d("stringJson", stringJson)
                        saveUserDetails(stringJson,  false)
                        Log.d("stringJson", loginResponseModelResponse.data.isVerified.toString())
                        if (loginResponseModelResponse.data.isVerified){
                            ActivityLauncher.getInstance()
                                .homeScreen(this@ActivityForgotPassword, HomeActivity::class.java)
                        }else{
                            ActivityLauncher.getInstance().goToEnterOTP(this@ActivityForgotPassword,
                                EnterOTPActivity::class.java,"ForgotPassword")
                        }
                        // AppCommonMethod.trackFcmCustomEvent(applicationContext, AppConstants.SIGN_IN_SUCCESS, "", "", "", 0, " ", 0, "", 0, 0, "", "", loginResponseModelResponse.data.id.toString() + "", loginResponseModelResponse.data.name + ""

                    } else if (loginResponseModelResponse.responseCode == 403) {
                        dismissLoading(binding?.progressBar)
                        commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ),loginResponseModelResponse.debugMessage.toString(),
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(), getString(R.string.popup_ok)))
                    } else {
                        dismissLoading(binding?.progressBar)
                        commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ),loginResponseModelResponse.debugMessage.toString(),
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_ok.toString(), getString(R.string.popup_ok)))
                    }
                }
            }
        } else {
            commonDialog(stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "", stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
        }
    }


    private fun callForgotApi() {
        showLoading(binding?.progressBar,true)
        viewModel?.hitForgotPasswordApi(email)?.observe(this, Observer {
            if (it!=null){
                dismissLoading(binding?.progressBar)
                if (it.code===200){
                    errorDialog = false
                    isRequestSent = true;
                    commonDialog(
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_request_sent.toString(),
                            getString(R.string.popup_request_sent))
                        ,applicationContext.resources.getString(R.string.forgot_password_response),
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_check_your_email.toString(),
                            getString(R.string.popup_check_your_email)))
                    clearEditView()
                }else{
                    if (it.debugMessage != null && !it.debugMessage.equals("", ignoreCase = true)) {
                        errorDialog = true
                        commonDialog(it.debugMessage,"",
                            stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString()
                                , getString(R.string.popup_continue)))
                    } else {
                        errorDialog = false
                    }
                }

            }else{
                commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ),stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                getString(R.string.something_went_wrong)
                            ),
                    stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                        getString(R.string.popup_continue)))
            }
        })
    }

    private fun clearEditView() {
        binding?.etPasswordRecoveryEmail?.setText("")
    }


    private fun validateEmail(): Boolean {
        var check = false
        email= binding?.etPasswordRecoveryEmail?.text.toString()
        if (email.matches(regex)) {
            check = true
        } else {
            commonDialog(stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.popup_invalid_email_tittle.toString(),
                getString(R.string.popup_invalid_email_tittle)
            ), stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.popup_invalid_email_subtitle.toString(),
                getString(R.string.popup_invalid_email_subtitle)
            ), stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                getString(R.string.popup_continue)))
        }
        return check
    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm: FragmentManager = supportFragmentManager
        val commonDialogFragment = CommonDialogFragment.newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }
    override fun onActionBtnClicked()
    {
        if (isRequestSent == true) {
            onBackPressed()

        }
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            binding?.forgotLayout?.visibility = View.VISIBLE
            binding?.noConnectionLayout?.visibility = View.GONE
        } else {
            commonDialog(stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "", stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                getString(R.string.popup_continue)))
        }
    }

    private fun noConnectionLayout() {
        binding?.forgotLayout?.visibility = View.GONE
        binding?.noConnectionLayout?.visibility = View.VISIBLE
        binding?.connection?.retryTxt?.setOnClickListener { view -> connectionObserver() }
    }
    private fun connectionObserver() {
        connectionValidation(NetworkConnectivity.isOnline(this@ActivityForgotPassword))
    }

//    private fun setTextWatcher() {
//        binding?.etPasswordRecoveryEmail?.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
//            @RequiresApi(api = Build.VERSION_CODES.M)
//            override fun afterTextChanged(editable: Editable) {
//                if (binding?.etPasswordRecoveryEmail?.text.toString().trim { it <= ' ' }.isNotEmpty()) {
//                    binding?.continueBtnOne?.isEnabled = true
//                    binding?.continueBtnOne?.setBackgroundResource(R.drawable.roundedcornerforbtn)
//                    val bgColor = if (!colorsHelper.instance()?.data?.config?.main_btn_selected_bg_color.isNullOrEmpty()) colorsHelper.instance()?.data?.config?.main_btn_selected_bg_color else getColor(R.color.main_btn_selected_bg_color)
//                    binding?.continueBtnOne?.background?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(bgColor as String?), BlendModeCompat.SRC_ATOP)
//                } else {
//                    binding?.continueBtnOne?.isEnabled = false
//                    binding?.continueBtnOne?.setBackgroundResource(R.drawable.signup_btn_gredient)
//                    val bgColor = if (!colorsHelper.instance()?.data?.config?.main_btn_unselected_bg_color.isNullOrEmpty()) colorsHelper.instance()?.data?.config?.main_btn_unselected_bg_color else getColor(R.color.main_btn_unselected_bg_color)
//                    binding?.continueBtnOne?.background?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(bgColor as String?), BlendModeCompat.SRC_ATOP)
//                }
//            }
//        })
//    }

    private fun saveUserDetails(stringJson: String?, b: Boolean) {
        try {
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
            preference?.appPrefUserId =fbLoginData.id.toString()
            preference?.appPrefUserName = fbLoginData.name.toString()
            preference?.appPrefUserEmail = fbLoginData.email.toString()
            preference?.expirytime = fbLoginData.expiryTime.toString()
            preference?.setIsVerified(fbLoginData.isVerified.toString())
        } catch (e: Exception) {
            Logger.w(e)
        }

    }

}