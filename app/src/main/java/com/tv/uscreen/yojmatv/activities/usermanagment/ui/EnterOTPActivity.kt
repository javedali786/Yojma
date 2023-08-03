package com.tv.uscreen.yojmatv.activities.usermanagment.ui
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.activities.homeactivity.ui.HomeActivity
import com.tv.uscreen.yojmatv.activities.usermanagment.model.OtpResponse
import com.tv.uscreen.yojmatv.activities.usermanagment.viewmodel.RegistrationLoginViewModel
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.cms.HelpActivity
import com.tv.uscreen.yojmatv.databinding.ActivityEnterOtpBinding

import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import java.util.Locale
import java.util.Objects


class EnterOTPActivity : BaseBindingActivity<ActivityEnterOtpBinding?>(), CommonDialogFragment.EditDialogListener{
    private val colorsHelper by lazy { ColorsHelper }
    private val stringsHelper by lazy { StringsHelper }
    private var mCountDownTimer: CountDownTimer? = null
    private val mTimeLeftInMillis: Long = 300000
    private var pinOtp: String? = null
    private var otpSendCount = 0
    private var viewModel: RegistrationLoginViewModel? = null
    private var preference: KsPreferenceKeys? = null
    private var mLastClickTime: Long = 0
    private var token: String? = ""
    private val enterOTPActivity = "EnterOTPActivity"
    private var isOtpVerified: Boolean=false
    private var fromWhich: String? = ""

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityEnterOtpBinding {
        return ActivityEnterOtpBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fromWhich = intent.getStringExtra("fromWhich")
        parseColor()
        setupToolbar()
        setClick()
        connectionValidation(NetworkConnectivity.isOnline(this))
        val properties = Properties()
        properties.addAttribute(AppConstants.USER_VERIFICATION, AppConstants.USER_VERIFICATION)
        MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties)
    }

    private fun parseColor() {
        binding?.stringData = stringsHelper
        binding?.colorsData = colorsHelper
        binding?.toolbar?.colorsData = colorsHelper
        binding?.toolbar?.stringData = stringsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
    }

    private fun setupToolbar() {
        binding?.toolbar?.backLayout?.visibility = View.VISIBLE
        binding?.toolbar?.titleMid?.visibility = View.GONE
        binding?.toolbar?.logoMain2?.visibility = View.VISIBLE
        binding?.toolbar?.llSearchIcon?.visibility = View.GONE
        if(fromWhich!!.equals("DetailPage",ignoreCase = true)) {
            binding?.toolbar?.backLayout?.visibility=View.VISIBLE
        }else{
            binding?.toolbar?.backLayout?.visibility=View.VISIBLE
        }

        val otpSkip = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.skip.toString(),
            getString(R.string.skip)
        )
        binding?.toolbar?.titleSkip?.text = otpSkip
        binding?.toolbar?.titleSkip?.setOnClickListener{
            ActivityLauncher.getInstance().homeScreen(this, HomeActivity::class.java)
        }
        binding?.toolbar?.backLayout?.setOnClickListener{
            onBackPressed()
        }
    }

    private fun setClick(){
        binding?.contactUs?.setOnClickListener {
            Objects.requireNonNull(this).startActivity(
                Intent(
                    this,
                    HelpActivity::class.java
                ).putExtra("type", "3")
            )
        }
    }

    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            binding?.noConnectionLayout?.visibility = View.GONE
            binding?.otpLayout?.visibility = View.VISIBLE
            binding?.pinViewOtp?.requestFocus()
            openSoftKeyboard(binding?.pinViewOtp)
            initialization()
            updatedUI()
            handleClick()
            startCounter()
            generateOtp()
        } else {
            binding?.noConnectionLayout?.visibility = View.VISIBLE
            binding?.otpLayout?.visibility = View.GONE
            binding?.connection?.retryTxt?.setOnClickListener { v: View? ->
                connectionValidation(
                    NetworkConnectivity.isOnline(this@EnterOTPActivity)
                )
            }
        }
    }

    private fun initialization() {
        token = KsPreferenceKeys.getInstance().appPrefAccessToken
        Logger.e("token",token.toString())
        viewModel = ViewModelProvider(this@EnterOTPActivity).get(
            RegistrationLoginViewModel::class.java
        )
    }

    private fun updatedUI() {
        binding?.continueBtn?.isEnabled = false
        binding?.continueBtn?.background?.alpha = 128
        val email = KsPreferenceKeys.getInstance().appPrefUserEmail
        if (email != null && !email.equals("", ignoreCase = true)) {
            binding?.phoneNo?.text = email
        } else {
            Logger.w(enterOTPActivity, "email id is null")
        }
    }

    private fun handleClick() {
        binding?.toolbar?.backLayout?.setOnClickListener { view: View? -> onBackPressed() }
        binding?.resendOtp?.setOnClickListener { v: View? ->
            if (NetworkConnectivity.isOnline(this@EnterOTPActivity)) {
                if (otpSendCount < 3) {
                    otpSendCount++
                    binding?.pinViewOtp?.setText("")
                    binding?.resendOtp?.isEnabled = false
                    binding?.resendOtp?.setTextColor(resources.getColor(R.color.tph_hint_txt_color))
                    startCounter()
                    generateOtp()
                }
            } else {
                connectionValidation(NetworkConnectivity.isOnline(this))
            }
            binding?.resendOtp?.visibility=View.GONE
        }
        binding?.pinViewOtp?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!TextUtils.isEmpty(binding?.pinViewOtp?.text)) {
                    if (binding?.pinViewOtp?.selectionEnd == 6) {
                        binding?.continueBtn?.isEnabled = true
                        binding?.continueBtn?.background?.alpha = 250
//                        binding?.continueBtn?.setBackgroundResource(R.drawable.roundedcornerforbtn)
//                        val bgColor = if (!colorsHelper.instance()?.data?.config?.main_btn_selected_bg_color.isNullOrEmpty()) colorsHelper.instance()?.data?.config?.main_btn_selected_bg_color else getColor(R.color.main_btn_selected_bg_color)
//                        binding?.continueBtn?.background?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(bgColor as String?), BlendModeCompat.SRC_ATOP)
                        binding?.pinViewOtp?.isCursorVisible=false
                    } else {
                        binding?.continueBtn?.isEnabled = false
                        binding?.continueBtn?.background?.alpha = 128
//                        binding?.continueBtn?.setBackgroundResource(R.drawable.signup_btn_gredient)
//                        val bgColor = if (!colorsHelper.instance()?.data?.config?.main_btn_selected_bg_color.isNullOrEmpty()) colorsHelper.instance()?.data?.config?.main_btn_selected_bg_color else getColor(R.color.main_btn_selected_bg_color)
//                        binding?.continueBtn?.background?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(bgColor as String?), BlendModeCompat.SRC_ATOP)
                        binding?.pinViewOtp?.isCursorVisible=true
                    }
                } else {
                    binding?.continueBtn?.isEnabled = false
                    binding?.continueBtn?.background?.alpha = 128
//                    binding?.continueBtn?.setBackgroundResource(R.drawable.signup_btn_gredient)
//                    val bgColor = if (!colorsHelper.instance()?.data?.config?.main_btn_selected_bg_color.isNullOrEmpty()) colorsHelper.instance()?.data?.config?.main_btn_selected_bg_color else getColor(R.color.main_btn_selected_bg_color)
//                    binding?.continueBtn?.background?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(bgColor as String?), BlendModeCompat.SRC_ATOP)
                    binding?.pinViewOtp?.isCursorVisible=true
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        binding?.continueBtn?.setOnClickListener { v: View? ->
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1200) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            if (!TextUtils.isEmpty(binding?.pinViewOtp?.text)) {
                if (binding?.pinViewOtp?.selectionEnd == 6) {
                    if (otpSendCount >= 3) {
                     //   commonDialog(applicationContext.resources.getString(R.string.otp_limits),"",applicationContext.resources.getString(R.string.countinue))

                    } else {
                        pinOtp = binding?.pinViewOtp?.text.toString()
                        otpVerify(pinOtp!!.toInt())
                    }
                }
            }
        }
    }

    private fun startCounter() {
        if (mCountDownTimer != null) {
            mCountDownTimer!!.cancel()
        }
        binding?.resendOtp?.isEnabled = false
        binding?.resendOtp?.setTextColor(resources.getColor(R.color.tph_hint_txt_color))
        binding?.txtOtp?.visibility = View.VISIBLE
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateCountDownText(millisUntilFinished)
            }

            override fun onFinish() {
                binding?.txtOtp?.setText(R.string.otp_code_valid_00_min)
                binding?.txtOtp?.visibility = View.INVISIBLE
                if (otpSendCount == 3) {
                    hideSoftKeyboard(binding?.pinViewOtp)
                    binding?.resendOtp?.isEnabled = false
                    binding?.resendOtp?.setTextColor(resources.getColor(R.color.tph_hint_txt_color))

                } else {
//                    binding?.resendOtp?.isEnabled = true
//                    binding?.resendOtp?.visibility=View.VISIBLE
//                    binding?.resendOtp?.setTextColor(resources.getColor(R.color.series_detail_now_playing_title_color))
                }
            }
        }.start()
    }

    private fun updateCountDownText(millisUntilFinished: Long) {
        val minutes = millisUntilFinished / 1000 / 60
        val seconds = millisUntilFinished / 1000 % 60
        val timeLeftFormatted = String.format(Locale("en", "US"), "%02d:%02d", minutes, seconds)
        binding?.txtOtp?.text = String.format("%s %s%s", getString(R.string.otp_code_valid_for),timeLeftFormatted,getString(R.string.min)
        )
        if (millisUntilFinished <= 180000 && otpSendCount < 3) {
            binding?.resendOtp?.isEnabled = true
            binding?.resendOtp?.visibility=View.VISIBLE
            binding?.resendOtp?.setTextColor(resources.getColor(R.color.series_detail_now_playing_title_color))
        }

    }


    private fun generateOtp() {
        showLoading(binding?.progressBar, true)
        if (token != null && !token.equals("", ignoreCase = true)) {
            viewModel?.generateOTPCode(token)?.observe(this) { response: OtpResponse? ->
                dismissLoading(binding?.progressBar)
                if (response != null) {
                    if (response.responseCode != null) {
                        if (response.responseCode == 2000) {
                            Logger.w(enterOTPActivity, "OTP sent successfully")
                        }
                    } else {
                        if (response.debugMessage != null && response.debugMessage != "") {
                           commonDialog(
                               response.debugMessage.toString(),""
                               , stringsHelper.stringParse(
                                   stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                   getString(R.string.popup_continue)))
                        } else {
                            commonDialog(
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ),
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                    getString(R.string.something_went_wrong)
                                ),
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                    getString(R.string.popup_continue))
                            )
                        }
                        if (mCountDownTimer != null) {
                            mCountDownTimer?.cancel()
                            mCountDownTimer = null
                            binding?.txtOtp?.visibility = View.INVISIBLE
                        }
                    }
                } else {
                    Logger.w(enterOTPActivity, "response is null")
                }
            }
        } else {
            Logger.w(enterOTPActivity, "Token is null")
        }
    }

    private fun otpVerify(otp: Int) {
        showLoading(binding?.progressBar, true)
        if (token != null && !token.equals("", ignoreCase = true)) {
            viewModel!!.otpVerify(otp, token).observe(this) { response: OtpResponse? ->
                dismissLoading(binding?.progressBar)
                if (response != null) {
                    if (response.responseCode != null) {
                        if (response.responseCode == 2000) {
                            if (mCountDownTimer != null) {
                                mCountDownTimer?.cancel()
                            }
                            KsPreferenceKeys.getInstance().setIsVerified("true")
                            isOtpVerified=true
                            commonDialog(
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_your_account_has_been_verified.toString(),
                                    getString(R.string.popup_your_account_has_been_verified)
                                ), "",
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                    getString(R.string.popup_continue))
                            )

                            hideSoftKeyboard(binding?.pinViewOtp)
                            if (mCountDownTimer != null) {
                                mCountDownTimer?.cancel()
                                mCountDownTimer = null
                                binding?.txtOtp?.visibility = View.INVISIBLE
                            }
                        }
                    } else {
                        if (response.debugMessage != null && response.debugMessage != "") {
                            commonDialog(response.debugMessage.toString(), stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_enter_valid_otp.toString(),
                                getString(R.string.popup_enter_valid_otp)),
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                    getString(R.string.popup_continue)))
                        } else {
                            commonDialog(
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ),
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                    getString(R.string.something_went_wrong)
                                ),
                                stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue))
                            )

                        }
                    }
                } else {
                    Logger.w(enterOTPActivity, "response is null")
                }
            }
        } else {
            Logger.w(enterOTPActivity, "token is null")
        }
    }



    override fun onBackPressed() {
        if (fromWhich!!.equals("DetailPage", ignoreCase = true)) {
            super.onBackPressed()
        }else{
            super.onBackPressed()
        }
        hideSoftKeyboard(binding?.pinViewOtp)
        if (mCountDownTimer != null) {
            mCountDownTimer?.cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mCountDownTimer != null) {
            mCountDownTimer?.cancel()
        }
    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm: FragmentManager = supportFragmentManager
        val commonDialogFragment = CommonDialogFragment.newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }

    override fun onActionBtnClicked() {
     if (isOtpVerified) {
        /* ActivityLauncher.getInstance().goToPlanScreen(
             this@EnterOTPActivity,
             ActivitySelectSubscriptionPlan::class.java,
             "OTP")  */

         ActivityLauncher.getInstance().homeScreen(
             this@EnterOTPActivity,
             HomeActivity::class.java)
     }


    }
}