package com.tv.activities.usermanagment.ui

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties
import com.tv.R
import com.tv.activities.usermanagment.viewmodel.RegistrationLoginViewModel
import com.tv.baseModels.BaseBindingActivity
import com.tv.databinding.ChangePasswordNewBinding
import com.tv.fragments.dialog.CommonDialogFragment
import com.tv.utils.BindingUtils.FontUtil
import com.tv.utils.Logger
import com.tv.utils.colorsJson.converter.AppColors
import com.tv.utils.colorsJson.converter.ColorsHelper
import com.tv.utils.constants.AppConstants
import com.tv.utils.helpers.CheckInternetConnection
import com.tv.utils.helpers.NetworkConnectivity
import com.tv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.utils.stringsJson.converter.StringsHelper
import java.util.Objects

class ChangePasswordActivity : BaseBindingActivity<ChangePasswordNewBinding?>(),
    CommonDialogFragment.EditDialogListener {
    var font: Typeface? = null
    private var preference: KsPreferenceKeys? = null
    private var token: String? = null
    private  var newPass:String = ""
    private var viewModel: RegistrationLoginViewModel? = null
    private val mLastClickTime: Long = 0
    private var isloggedout = false
    private var isChnagePassword: Boolean=false
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }
    val regexPass:Regex=Regex("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=\\S+\$).{8,16}\$")
    override fun inflateBindingLayout(inflater: LayoutInflater): ChangePasswordNewBinding {
        return ChangePasswordNewBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ThemeHandler.getInstance().applyChangePasswordPage(this, binding);
        parseColor()
        setUi()
        setTextWatcher()
        setTextWatcherForNewPassword()
        connectionObserver()
        isloggedout = false
        viewModel = ViewModelProvider(this@ChangePasswordActivity)[RegistrationLoginViewModel::class.java]
        preference = KsPreferenceKeys.getInstance()

        val properties = Properties()
        properties.addAttribute(AppConstants.CHANGE_PASSWORD, AppConstants.CHANGE_PASSWORD)
        MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties)

    }
    private fun setUi() {
        token = KsPreferenceKeys.getInstance().appPrefAccessToken
        Logger.e("token",token.toString())
        setTextWatcher()
        setTextWatcherForNewPassword()
        val tempResponse = preference?.appPrefProfile
        binding?.password?.isLongClickable = false
        binding?.confNewPassword?.isLongClickable = false
        binding?.newPassword?.isLongClickable = false
        binding?.password?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding?.confNewPassword?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding?.newPassword?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        font = FontUtil.getNormal(this)
        binding?.password?.typeface = font
        binding?.confNewPassword?.typeface = font
        binding?.newPassword?.typeface = font
        binding?.confirmPasswordEye?.setOnCheckedChangeListener { compoundButton, b ->
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
                hideSoftKeyboard(binding?.confirmPasswordEye)
                binding?.password?.typeface = font
            }
        }

        binding?.newPasswordEye?.setOnCheckedChangeListener { compoundButton, b ->
            if (!b) {
                binding?.newPassword?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding?.newPassword?.typeface = font
            } else {
                binding?.newPassword?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding?.newPassword?.text?.let {
                    binding?.newPassword?.setSelection(
                        it.length
                    )
                }
                binding?.newPassword?.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                hideSoftKeyboard(binding?.newPasswordEye)
                binding?.newPassword?.typeface = font
            }
        }

        binding?.confNewPasswordEye?.setOnCheckedChangeListener { compoundButton, b ->
            if (!b) {
                binding?.confNewPassword?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding?.confNewPassword?.typeface = font
            } else {
                binding?.confNewPassword?.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding?.password?.text?.let {
                    binding?.confNewPassword?.setSelection(
                        it.length
                    )
                }
                binding?.confNewPassword?.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                hideSoftKeyboard(binding?.confNewPasswordEye)
                binding?.confNewPassword?.typeface = font
            }
        }
        binding?.updatePassword?.setOnClickListener{
            hideSoftKeyboard(binding?.updatePassword)
            if (CheckInternetConnection.isOnline(this@ChangePasswordActivity)) {
                if(validateEmptyPassword() &&  validateEmptyConfirmPassword() && confirmPasswordCheck(binding?.newPassword?.text.toString(),binding?.confNewPassword?.text.toString())){
                    changePasswordApi()
                }
            }else{
                commonDialog(
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "",
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                        getString(R.string.popup_continue)))

            }
        }
        binding?.toolbar?.backLayout?.setOnClickListener { onBackPressed() }
        val titleText = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.change_pwd_change_pwd.toString(),
            getString(R.string.change_pwd_change_pwd)
        )
        binding?.toolbar?.titleMid?.text = titleText
        binding?.toolbar?.titleSkip?.visibility = View.GONE
        binding?.toolbar?.titleMid?.visibility = View.VISIBLE
        binding?.toolbar?.logoMain2?.visibility = View.GONE
        binding?.toolbar?.llSearchIcon?.visibility = View.GONE

    }

    private fun parseColor() {
        binding?.stringData = stringsHelper
        binding?.colorsData = colorsHelper
        binding?.toolbar?.colorsData = colorsHelper
        binding?.toolbar?.stringData = stringsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper

        val textColorStates = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
            intArrayOf(AppColors.pwdSelectedEyeColor(), AppColors.pwdUnselectedEyeColor())
        )
        binding?.confirmPasswordEye?.buttonDrawable?.setTintList(textColorStates)
        binding?.newPasswordEye?.buttonDrawable?.setTintList(textColorStates)
        binding?.confNewPasswordEye?.buttonDrawable?.setTintList(textColorStates)

        binding?.password?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        binding?.newPassword?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        binding?.confNewPassword?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
    }

    private fun setTextWatcherForNewPassword() {
        binding?.newPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
//                editTextEmptyCheck()
            }
        })
    }

    private fun setTextWatcher() {
        binding?.confNewPassword?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
//                editTextEmptyCheck()
            }
        })
    }

//    private fun editTextEmptyCheck(): Boolean {
//        val newPass=binding?.newPassword?.text
//        val confirmPass=binding?.confNewPassword?.text
//        if(!newPass.isNullOrEmpty() && !confirmPass.isNullOrEmpty()) {
//            binding?.updatePassword?.isEnabled = true
//            binding?.updatePassword?.setBackgroundResource(R.drawable.roundedcornerforbtn)
//            val bgColor = if (!colorsHelper.instance()?.data?.config?.main_btn_selected_bg_color.isNullOrEmpty()) colorsHelper.instance()?.data?.config?.main_btn_selected_bg_color else getColor(R.color.main_btn_selected_bg_color)
//            binding?.updatePassword?.background?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(bgColor as String?), BlendModeCompat.SRC_ATOP)
//            return true
//        } else{
//            binding?.updatePassword?.isEnabled = false
//            binding?.updatePassword?.setBackgroundResource(R.drawable.signup_btn_gredient)
//            val bgColor = if (!colorsHelper.instance()?.data?.config?.main_btn_unselected_bg_color.isNullOrEmpty()) colorsHelper.instance()?.data?.config?.main_btn_unselected_bg_color else getColor(R.color.main_btn_unselected_bg_color)
//            binding?.updatePassword?.background?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(Color.parseColor(bgColor as String?), BlendModeCompat.SRC_ATOP)
//            return false
//        }
//    }

    private fun changePasswordApi() {
        showLoading(binding?.progressBar, true)
        newPass = binding?.newPassword?.text.toString()

        viewModel?.hitApiChangePwd(newPass, token, this)?.observe(this, Observer {
         if (it!=null){
             dismissLoading(binding?.progressBar)
             Logger.e("changePassRes", "response$it")
             if(it.responseCode===200 || it.responseCode===2000){
                 isChnagePassword=true
                 commonDialog(
                     stringsHelper.stringParse(
                         stringsHelper.instance()?.data?.config?.popup_pwd_pwd_changed.toString(),
                         getString(R.string.popup_pwd_pwd_changed)
                     ),
                     stringsHelper.stringParse(
                         stringsHelper.instance()?.data?.config?.popup_pwd_has_been_changed.toString(), getString(R.string.popup_pwd_has_been_changed))
                     ,
                     stringsHelper.stringParse(
                         stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                         getString(R.string.popup_continue))
                 )

                 clearEditView()
             } else if (it.responseCode === 401) {
                 isloggedout = true
                 logoutCall()
             } else if (it.responseCode === 403) {
                 isloggedout = true
                 logoutCall()
             }
         }
        })
    }

    private fun clearEditView() {
        binding?.confNewPassword?.setText("")
        binding?.password?.setText("")
        binding?.newPassword?.setText("")


    }

    private fun logoutCall() {
        if (CheckInternetConnection.isOnline(Objects.requireNonNull(this@ChangePasswordActivity))) {
            clearCredientials(preference)
            hitApiLogout(this@ChangePasswordActivity, preference?.appPrefAccessToken)
           ActivityLauncher.getInstance().goToLogin(this@ChangePasswordActivity, ActivityLogin::class.java)
        } else {
            commonDialog(
                stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "",
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue))
            )
        }
    }

    private fun validateEmptyPassword(): Boolean {
        var check = false
        if ((binding?.newPassword?.text.toString().trim().matches(regexPass))) {
            check = true
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
        if ((binding?.confNewPassword?.text.toString().trim().matches(regexPass))) {
            check = true
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

    private fun confirmPasswordCheck(password: String, confirmPassword: String?): Boolean {
        var check = false
        if (!password.equals(confirmPassword, ignoreCase = true)) {
            Logger.e("error pass", "$confirmPassword")
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_incorrect_pwd_tittle.toString(),
                    getString(R.string.popup_incorrect_pwd_tittle)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_change_pwd_your_pwd_does_not_match.toString(),
                    getString(R.string.popup_change_pwd_your_pwd_does_not_match)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue))
            )
        } else {
            check = true
        }
        return check
    }


    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm: FragmentManager = supportFragmentManager
        val commonDialogFragment = CommonDialogFragment.newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }
    override fun onActionBtnClicked() {
        if(isChnagePassword){
            onBackPressed()
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
                    ), "",
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)))
        }
    }
    private fun connectionValidation(aBoolean: Boolean) {
        if (aBoolean) {
            binding?.changePasswordLayout?.visibility = View.VISIBLE
            binding?.connection?.noConnectionLayout?.visibility = View.GONE

        } else {
            commonDialog(
                stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "",
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue))
            )
        }
    }
    private fun noConnectionLayout() {

        binding?.changePasswordLayout?.visibility = View.GONE
        binding?.connection?.noConnectionLayout?.visibility = View.VISIBLE
        binding?.connection?.retryTxt?.setOnClickListener { view -> connectionObserver() }
    }




}