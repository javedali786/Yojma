package com.tv.uscreen.yojmatv.activities.usermanagment.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.*
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
import com.tv.uscreen.yojmatv.databinding.ActivitySignupBinding
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.utils.BindingUtils.FontUtil
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.AppColors
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ActivitySignUp : BaseBindingActivity<ActivitySignupBinding?>(), CommonDialogFragment.EditDialogListener,
    AppleSignInListener {
    var font: Typeface? = null
    private var viewModel: RegistrationLoginViewModel? = null
    private val permissionNeeds = listOf("email", "public_profile")
    private var isNotificationEnable = false
    private val ifCheckboxIsChecked = true
    private lateinit var name:String
    private lateinit var email:String
    private var dob:String = ""
    private var dateMilliseconds = ""
    private var accessTokenFB: String? = null
    private var callbackManager: CallbackManager? = null
    var hasFbEmail = false
    var isFbLoginClick = false
    private var mLastClickTime: Long = 0
    private var modelLogin: Data? = null
    private var id = ""
    private var preference: KsPreferenceKeys? = null
    private lateinit var password:String
    private var jwtToken = ""
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }
    val regexPass:Regex=Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=\\S+\$).{8,16}\$")
    val regex: Regex = Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,100}")
    override fun inflateBindingLayout(inflater: LayoutInflater): ActivitySignupBinding {
        return ActivitySignupBinding.inflate(inflater)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ThemeHandler.getInstance().applyActivitySignUp(this,binding)
        parseColor()
        binding?.signIn?.paintFlags = binding!!.signIn.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        viewModel = ViewModelProvider(this@ActivitySignUp).get(
            RegistrationLoginViewModel::class.java
        )
        preference= KsPreferenceKeys.getInstance()
        uiCall()
        setClicks()
        connectionObserver()
       /* setTextWatcher()
        emailTextWatcher()
        passwordTextWatcher()*/
    }

    private fun parseColor() {
        binding?.colorsData = colorsHelper
        binding?.stringData = stringsHelper
        binding?.toolbar?.colorsData = colorsHelper
        binding?.toolbar?.stringData = stringsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper

        val textColorStates = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
            intArrayOf(AppColors.pwdSelectedEyeColor(), AppColors.pwdUnselectedEyeColor())
        )
        binding?.radioPasswordEye?.buttonDrawable?.setTintList(textColorStates)
        binding?.confirmPasswordEye?.buttonDrawable?.setTintList(textColorStates)

        binding?.name?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor() , 10f)
        binding?.email?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor() , 10f)
        binding?.dOB?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor() , 10f)
        ColorsHelper.textViewDrawableColor(binding!!.dOB, AppColors.dobIconColor())
        binding?.password?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor() , 10f)
        binding?.confPassword?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor() , 10f)
    }

    private fun passwordTextWatcher() {
        binding?.password?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }
            override fun afterTextChanged(editable: Editable) {
               editTextEmptyCheck()
            }
        })
    }

    private fun emailTextWatcher() {
        binding?.email?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }
            override fun afterTextChanged(editable: Editable) {
                editTextEmptyCheck()
            }
        })
    }


    private fun editTextEmptyCheck(): Boolean {
        val email=binding?.email?.text
        val pass=binding?.password?.text
        val cnfPass=binding?.confPassword?.text
        if(!email.isNullOrEmpty() && !pass.isNullOrEmpty() && !cnfPass.isNullOrEmpty()&& binding?.checkBox?.isChecked == true) {
            binding?.signUp?.isEnabled = true
            binding?.signUp?.setBackgroundResource(R.drawable.roundedcornerforbtn)
            binding?.signUp?.setTextColor(getColor(R.color.main_btn_txt_color))
            return true
        } else{
            binding?.signUp?.isEnabled = false
            binding?.signUp?.setBackgroundResource(R.drawable.signup_btn_gredient)
            binding?.signUp?.setTextColor(getColor(R.color.main_btn_txt_color))
            return false
        }
    }


    private fun setClicks() {
        binding?.toolbar?.backLayout?.setOnClickListener { onBackPressed() }

        callbackManager = CallbackManager.Factory.create()
        FacebookSdk.fullyInitialize();
        binding?.fbButton?.setReadPermissions(permissionNeeds);

        binding?.facebookImg?.setOnClickListener{
            hideSoftKeyboard(binding?.facebookImg)
            if (CheckInternetConnection.isOnline(this@ActivitySignUp)) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                    return@setOnClickListener
                }
                mLastClickTime = SystemClock.elapsedRealtime()
                clearEditView()
                isFbLoginClick = true
                binding?.fbButton?.performClick()

            }else{
             //   connectionValidation(false)
                commonDialog(stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "", stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))



            }
        }

        binding?.dOB?.setOnClickListener(View.OnClickListener {
            val mcurrentDate = Calendar.getInstance()
            val mYear = mcurrentDate[Calendar.YEAR]
            val mMonth = mcurrentDate[Calendar.MONTH]
            val mDay = mcurrentDate[Calendar.DAY_OF_MONTH]
            val mDatePicker = DatePickerDialog(
                this@ActivitySignUp,
                { datepicker, selectedyear, selectedmonth, selectedday ->
                    mcurrentDate[Calendar.YEAR] = selectedyear
                    mcurrentDate[Calendar.MONTH] = selectedmonth
                    mcurrentDate[Calendar.DAY_OF_MONTH] = selectedday
                    val difference = mYear - selectedyear
                    if (difference >= 18) {
                        val sdf = SimpleDateFormat("dd MMMM yyyy ", Locale.getDefault())
                        binding?.dOB?.text = sdf.format(mcurrentDate.time)
                        try {
                            val d = sdf.parse(binding?.dOB?.text.toString())
                            dateMilliseconds = d.time.toString()
                        } catch (e: ParseException) {
                            Logger.w(e)
                        }
                    } else {
                        binding?.dOB?.text = ""
                        dateMilliseconds = ""
                        commonDialog( stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_invalid_dob_tittle.toString(),
                            getString(R.string.popup_invalid_dob_tittle)),
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_date_difference.toString(),
                                getString(R.string.popup_date_difference)),
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                getString(R.string.popup_continue)))
                    }
                }, mYear, mMonth, mDay
            )
            mDatePicker.show()
        })


        binding?.appleImg?.setOnClickListener {
            showLoading(binding?.progressBar,true)
            viewModel?.authResponse?.observe(this, Observer {
                if (it!=null) {
                    dismissLoading(binding?.progressBar)
                    Log.d("LoginResponse", Gson().toJson(it))
                    try {
                        if (it.responseCode === 2000 && it.data.authURL != null) {
                            AppleSignInManager.setContext(this)
                            AppleSignInManager.setUrl(it.data.authURL)
                            AppleSignInManager.setupAppleWebViewDialog()
                        } else if(it.debugMessage!=null){
                            commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ),it.debugMessage.toString(),stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
                        }else{
                            commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ), stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                getString(R.string.something_went_wrong)
                            ),stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
                        }

                    } catch (e: Exception) {
                        Logger.w(e)
                    }
                }else{
                    commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ), stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                getString(R.string.something_went_wrong)
                            ),stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))

                }
            })
        }

        binding?.fbButton?.registerCallback(callbackManager,object :
            FacebookCallback<LoginResult?> {
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
                        }
                        else hasFbEmail = false
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
        } )


    }


    fun hitApiFBLogin() {
        if (CheckInternetConnection.isOnline(this@ActivitySignUp)) {
            showLoading(getBinding()?.progressBar, true)
            viewModel?.hitFbLogin(
                this@ActivitySignUp, email, accessTokenFB, name, id, "", hasFbEmail)?.observe(this@ActivitySignUp) { loginResponseModelResponse ->
                if (Objects.requireNonNull(loginResponseModelResponse).responseCode == 2000) {
                    val gson = Gson()
                    modelLogin = loginResponseModelResponse.data
                    val stringJson = gson.toJson(loginResponseModelResponse.data)
                    Log.d("stringJson", stringJson)
                    saveUserDetails(stringJson,  false)
                    Log.d("stringJson", loginResponseModelResponse.data.isVerified.toString())
                        ActivityLauncher.getInstance().goToPlanScreen(this@ActivitySignUp,
                            ActivitySelectSubscriptionPlan::class.java,"SignUp")
//                    if (loginResponseModelResponse.data.isVerified){
//                        ActivityLauncher.getInstance().homeslider(this@ActivitySignUp, HomeSliderActivity::class.java)
//                    }else{
//                        ActivityLauncher.getInstance().goToPlanScreen(this@ActivitySignUp,ActivitySelectSubscriptionPlan::class.java,true)
//                    }
                    // AppCommonMethod.trackFcmCustomEvent(applicationContext, AppConstants.SIGN_IN_SUCCESS, "", "", "", 0, " ", 0, "", 0, 0, "", "", loginResponseModelResponse.data.id.toString() + "", loginResponseModelResponse.data.name + ""

                } else if (loginResponseModelResponse.responseCode === 403) {
                    ActivityLauncher.getInstance().forceLogin(this@ActivitySignUp, ActivityForgotPassword::class.java, accessTokenFB, id, name, "", true)
                } else {
                    dismissLoading(binding?.progressBar)
                    commonDialog(stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                getString(R.string.popup_error)
                            ),loginResponseModelResponse.debugMessage.toString(),
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_ok.toString(),
                            getString(R.string.popup_ok)))
                }
            }
        } else {
            commonDialog(stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "", stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
        }
    }



    private fun uiCall() {
        binding?.signIn?.setOnClickListener {
            ActivityLauncher.getInstance().goToLogin(this@ActivitySignUp, ActivityLogin::class.java)
            clearEditView()
        }

        binding?.toolbar?.titleSkip?.visibility = View.GONE

        val signupTermConditionText = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.signup_term_condition.toString(),
            getString(R.string.signup_term_condition)
        )

        setClickableString(getString(R.string.term), getString(R.string.agree),getString(R.string.privacy) , getString(R.string.and_the))


        binding?.checkBox?.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            if (b) {
                binding?.checkBox?.setBackgroundResource(R.drawable.checked_state)
                isNotificationEnable=ifCheckboxIsChecked

               binding?.checkBox?.background = strokeCheckBoxBgDrawable(AppColors.radioBtnCheckedColor(), AppColors.radioBtnUncheckedColor(), 5,0f)
//                editTextEmptyCheck()

            } else {
                //binding?.checkBox?.setBackgroundResource(R.drawable.unchecked_state)
                isNotificationEnable=!ifCheckboxIsChecked

                binding?.checkBox?.background = strokeCheckBoxBgDrawable(AppColors.appBgColor(), AppColors.radioBtnUncheckedColor(), 5,0f)
//                editTextEmptyCheck()
            }
            //   KsPreferenceKeys.getInstance().saveNotificationEnable(isNotificationEnable)
        }



        binding?.password?.isLongClickable = false
        binding?.confPassword?.isLongClickable = false
        binding?.password?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding?.confPassword?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        font = FontUtil.getNormal(this)
        binding?.password?.typeface = font
        binding?.confPassword?.typeface = font
        binding?.radioPasswordEye?.setOnCheckedChangeListener { compoundButton, b ->
            if (!b) {
                binding?.password?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding?.password?.typeface = font
            } else {
                binding?.password?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding?.password?.text?.let {
                    binding?.password?.setSelection(
                       it.length
                    )
                }
                binding?.password?.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                hideSoftKeyboard(binding?.radioPasswordEye)
                binding?.password?.typeface = font
            }
        }

        binding?.confirmPasswordEye?.setOnCheckedChangeListener { compoundButton, b ->
            if (!b) {
                binding?.confPassword?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding?.confPassword?.typeface = font
            } else {
                binding?.confPassword?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding?.password?.text?.let {
               //     binding?.confPassword?.setSelection(it.length)
                }
                binding?.confPassword?.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                hideSoftKeyboard(binding?.confirmPasswordEye)
                binding?.confPassword?.typeface = font
            }
        }
        binding?.signUp?.setOnClickListener{
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            hideSoftKeyboard(binding?.signUp)
            if (CheckInternetConnection.isOnline(this@ActivitySignUp)) {
                if(validateEmptyEmail() && validateEmail() && validateEmptyPassword() && validateEmptyConfirmPassword() && confirmPasswordCheck(binding?.password?.text.toString(),binding?.confPassword?.text.toString()) && binding?.checkBox?.isChecked == true){
                    hitSignUpApi()
                }

            }else{
                commonDialog(stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "", stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))

            }
        }

    }

    private fun strokeCheckBoxBgDrawable(tphBgColor: Int, tphBorderColor: Int, strokeWidth: Int, cornerRadius: Float): GradientDrawable {
        val drawable = GradientDrawable()
        drawable.shape = GradientDrawable.RECTANGLE
        drawable.cornerRadius = cornerRadius
        drawable.setStroke(strokeWidth, tphBorderColor)
        drawable.setColor(tphBgColor)
        return drawable
    }

    private fun clearEditView() {
        binding?.name?.setText("")
        binding?.email?.setText("")
        binding?.dOB?.setText("")
        binding?.password?.setText("")
        binding?.confPassword?.setText("")

    }


    private fun hitSignUpApi() {
        showLoading(binding?.progressBar,true)
        preference?.appPrefAccessToken = ""
        viewModel?.hitSignUpAPI(binding?.name?.text.toString(),binding?.email?.text.toString(),dateMilliseconds,binding?.password?.text.toString(),true)?.observe(this, Observer {
          if (it!=null) {
              dismissLoading(binding?.progressBar)
              Log.d("SIgnupResponse", Gson().toJson(it))
             try {
                  if (it.responseModel.responseCode === 200) {
                      val gson = Gson()
                      KsPreferenceKeys.getInstance().appPrefAccessToken=it.accessToken
                      val signUpData: Data = it.responseModel.data
                      val stringJson = gson.toJson(signUpData)
                      callSignupDevice(stringJson)
                      saveUserDetails(stringJson, true)
                     /* try {
                          setUserProperties(this,signUpData.id,signUpData.name,signUpData.email,signUpData.phoneNumber,signUpData.dateOfBirth)
                      } catch (e: NullPointerException ) {
                        Logger.w(e)
                      }*/

                     /* val properties = Properties()
                      properties.addAttribute(AppConstants.REGISTER, AppConstants.REGISTER)
                      MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties)
                      val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                      val date = Date()
                      System.out.println(dateFormat.format(date))
                      properties.addAttribute(AppConstants.REGISTER_DATE, date)
                      MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.REGISTER_DATE, properties)
                      AppCommonMethod.screenViewedTrack(applicationContext, AppConstants.SIGN_UP_SUCCESS, "ActivitySignUp")*/

                      clearEditView()
                  }else if(it.responseModel.responseCode==4901){
                      commonDialog(it.debugMessage.toString(),"",stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
                  }else if (it.responseModel.responseCode==400){
                      commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ),it.debugMessage.toString(),stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
                  }else{
                      commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ), stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                getString(R.string.something_went_wrong)
                            ),stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
                  }
              } catch (e: NullPointerException) {
                  Logger.w(e)
              }
          }else{
              commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ), stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                getString(R.string.something_went_wrong)
                            ),stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
          }
        })
    }

    private fun saveUserDetails(stringJson: String?, b: Boolean) {
        val fbLoginData: Data = Gson().fromJson(stringJson, Data::class.java)
        Logger.d("signupResponseIs",fbLoginData.accountId)
        Logger.d("signupResponseIs",fbLoginData.expiryTime)
        val gson = Gson()
        val stringJson = gson.toJson(fbLoginData)
        preference?.setfirstTimeUserForKidsPIn(true)

        if (b) {
            preference?.appPrefLoginType = AppConstants.UserLoginType.Manual.toString()
        } else {
            preference?.appPrefLoginType = AppConstants.UserLoginType.FbLogin.toString()
        }
        preference?.appPrefProfile = stringJson
        preference?.appPrefLoginStatus = AppConstants.UserStatus.Login.toString()
        preference?.appPrefUserId = fbLoginData.id.toString()
        preference?.appPrefLoginType = AppConstants.UserStatus.Login.toString()
        Logger.e("loginvalue", preference?.appPrefLoginType.toString())
        preference?.appPrefUserName = fbLoginData.name.toString()
        preference?.appPrefUserEmail =fbLoginData.email.toString()
        preference?.expirytime = java.lang.String.valueOf(fbLoginData.expiryTime)

        ActivityLauncher.getInstance()
            .goToEnterOTP(this@ActivitySignUp, EnterOTPActivity::class.java,"signUp")

       // checkPlansForUser()
    }

    private fun checkPlansForUser() {
        val token: String = KsPreferenceKeys.getInstance().appPrefAccessToken
        GetPlansLayer.getInstance().getEntitlementStatus(
            KsPreferenceKeys.getInstance(),token
        ) { entitlementStatus, apiStatus, _ ->
            if (apiStatus) {
                if (entitlementStatus) {
                    if (KsPreferenceKeys.getInstance().isVerified.equals("true",ignoreCase = true)){
                        ActivityLauncher.getInstance().goToPlanScreen(
                            this@ActivitySignUp,
                            ActivitySelectSubscriptionPlan::class.java,
                            "SignUp"
                        )
                    }else{
                        ActivityLauncher.getInstance().goToEnterOTP(this,
                            EnterOTPActivity::class.java,"SignUp")
                    }
                }else{
                    ActivityLauncher.getInstance().goToPlanScreen(
                        this@ActivitySignUp,
                        ActivitySelectSubscriptionPlan::class.java,
                        "SignUp"
                    )
                }
            }else{
                ActivityLauncher.getInstance().homeScreen(this@ActivitySignUp, HomeActivity::class.java)
            }
        }
    }



    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            binding?.loginMainFrame?.visibility = View.VISIBLE
            binding?.toolbar?.logoMain2?.visibility = View.VISIBLE
            binding?.toolbar?.searchIcon?.visibility = View.GONE
            binding?.connection?.noConnectionLayout?.visibility = View.GONE
        } else {
            noConnectionLayout()
        }
    }

    private fun connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true)
        } else {
            commonDialog(stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "", stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
        }
    }

    private fun noConnectionLayout() {
        binding?.loginMainFrame?.visibility = View.GONE
        binding?.connection?.noConnectionLayout?.visibility = View.VISIBLE
        binding?.connection?.retryTxt?.setOnClickListener { view -> connectionObserver() }
    }


    private fun confirmPasswordCheck(password: String, confirmPassword: String?): Boolean {
        var check = false
        if (!password.equals(confirmPassword, ignoreCase = true)) {
            Logger.e("error pass", "$confirmPassword")
            commonDialog(stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.popup_change_pwd_incorrect_confirm_pwd.toString(),
                getString(R.string.popup_change_pwd_incorrect_confirm_pwd)), resources.getString(R.string.sorry_your_password_doesn_t_match_please_check_your_password_again), resources.getString(R.string.forgot_continue))
        } else {
            check = true
        }
        return check
    }


    private fun validateEmptyPassword(): Boolean {
        var check = false
        password=binding?.password?.text.toString().trim().matches(regexPass).toString()
        if ((binding?.password?.text.toString().trim().matches(regexPass))) {
            check = true
        } else if (binding?.password?.text.toString().trim() == "") {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_empty_Password.toString(),
                    getString(R.string.popup_empty_Password)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_pwd_must_be_8_to_16_char.toString(),
                    getString(R.string.popup_pwd_must_be_8_to_16_char)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue))
            )
        } else {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_incorrect_pwd_tittle.toString(),
                    getString(R.string.popup_incorrect_pwd_tittle)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_pwd_must_be_8_to_16_char.toString(),
                    getString(R.string.popup_pwd_must_be_8_to_16_char)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue))
            )

        }
        return check
    }

    private fun validateEmptyConfirmPassword(): Boolean {
        var check = false
        if (StringUtils.isNullOrEmptyOrZero(binding?.confPassword?.text.toString().trim())) {
            commonDialog(stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.popup_empty_Confirm_Password.toString(),
                getString(R.string.popup_confirm_pwd)),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_pwd_must_be_8_to_16_char.toString(),
                    getString(R.string.popup_pwd_must_be_8_to_16_char)
                ),
                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)))
        } else {
            check = true
        }
        return check
    }

    private fun validateEmptyEmail(): Boolean {
        var check = false
        email=binding?.email?.text.toString().trim()
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

    private fun validateEmptyDob(): Boolean {
        var check = false
        dob=binding?.dOB?.text.toString().trim()
        if (StringUtils.isNullOrEmptyOrZero(dob)) {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_empty_dob_tittle.toString(),
                    getString(R.string.popup_empty_dob_tittle)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_empty_dob_subtitle.toString(),
                    getString(R.string.popup_empty_dob_subtitle)
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
        if (binding?.email?.text.toString().trim().matches(regex)) {
            check = true
        } else {
            commonDialog(stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_invalid_email_tittle.toString(), getString(R.string.popup_invalid_email_tittle)),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_invalid_email_subtitle.toString(),
                    getString(R.string.popup_invalid_email_subtitle)
                ), stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue))
            )
        }
        return check
    }

    private fun setClickableString(
        clickableValue: String,
        wholeValue: String,
        clickableValue1: String,
        wholeValue1: String
    ) {
        val spannableString = SpannableString(wholeValue)
        val startIndex = wholeValue.indexOf(clickableValue)
        val endIndex = startIndex + clickableValue.length
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://watch.yojma.tv/tyc"))
                startActivity(browserIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = AppColors.termsAndConditionTextColor()
                ds.isUnderlineText = false
            }
        }, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val spannableString1 = SpannableString(wholeValue1)
        val startIndex1 = wholeValue1.indexOf(clickableValue1)
        val endIndex1 = startIndex1 + clickableValue1.length
        spannableString1.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://watch.yojma.tv/privacidad"))
                startActivity(browserIntent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = AppColors.termsAndConditionTextColor()
                ds.isUnderlineText = false
            }
        }, startIndex1, endIndex1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding?.termstext?.text = TextUtils.concat(spannableString, " ", spannableString1)
        binding?.termstext?.movementMethod =
            LinkMovementMethod.getInstance() // <-- important, onClick in ClickableSpan won't work without this
    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm: FragmentManager = supportFragmentManager
        val commonDialogFragment = CommonDialogFragment.newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }

    private fun callSignupDevice(stringJson:String){
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
            }  else {
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


    override fun onActionBtnClicked()
    {
    }
   private fun setTextWatcher() {
        binding?.confPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
            }
            override fun afterTextChanged(editable: Editable) {
               editTextEmptyCheck()
            }
        })
    }

    override fun onAppSignInSuccess(jwt_token: String) {
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
                    saveUserDetails(lastIndex+"}",true)
                    //ActivityLauncher.getInstance().homeslider(this@ActivitySignUp, HomeSliderActivity::class.java)
                    ActivityLauncher.getInstance().goToPlanScreen(
                        this@ActivitySignUp,
                        ActivitySelectSubscriptionPlan::class.java,
                        "SignUp"
                    )
                }

            } catch (e: Exception) {
                Logger.w(e)
            }

        }
    }

    override fun onAppSignInError() {
        runOnUiThread {
            commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ), stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                getString(R.string.something_went_wrong)
                            ),stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
        }
    }

}