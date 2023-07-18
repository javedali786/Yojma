package com.tv.uscreen.activities.profile.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.google.gson.Gson
import com.moe.pushlibrary.MoEHelper
import com.moengage.core.Properties

import com.tv.uscreen.Bookmarking.BookmarkingViewModel
import com.tv.uscreen.R
import com.tv.uscreen.activities.profile.CountryListActivity
import com.tv.uscreen.activities.usermanagment.ui.ActivityLogin
import com.tv.uscreen.activities.usermanagment.viewmodel.RegistrationLoginViewModel
import com.tv.uscreen.baseModels.BaseBindingActivity
import com.tv.uscreen.beanModel.AppUserModel
import com.tv.uscreen.beanModel.userProfile.UserProfileResponse
import com.tv.uscreen.databinding.ProfileActivityNewBinding
import com.tv.uscreen.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.fragments.dialog.CommonDialogFragment.EditDialogListener
import com.tv.uscreen.utils.Logger
import com.tv.uscreen.utils.ObjectHelper
import com.tv.uscreen.utils.TrackerUtil.MoEUserTracker.setUserProperties
import com.tv.uscreen.utils.colorsJson.converter.AppColors
import com.tv.uscreen.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.utils.constants.AppConstants
import com.tv.uscreen.utils.helpers.CheckInternetConnection
import com.tv.uscreen.utils.helpers.NetworkConnectivity
import com.tv.uscreen.utils.helpers.StringUtils
import com.tv.uscreen.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.utils.stringsJson.converter.StringsHelper
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ProfileActivityNew : BaseBindingActivity<ProfileActivityNewBinding?>(), EditDialogListener {
    private var imageUrlId = ""
    private var countryName = ""
    private var mPreference: KsPreferenceKeys? = null
    private var via = ""
    private val transferUtility: TransferUtility? = null
    private var contentPreference = ""
    private var preference: KsPreferenceKeys? = null
    private var dateMilliseconds = ""
    private val encodePin = ""
    private var isLogin: String? = null
    private val mHandler = Handler(Looper.getMainLooper())
    private var isloggedout = false
    private var isDeletionRequired = false
    private var isInvalidResponseCode = false;
    private var isUpdateRecord: Boolean=false
    private val bookmarkingViewModel: BookmarkingViewModel? = null
    private var viewModel: RegistrationLoginViewModel? = null
    var saved: List<String>? = null
    val regex: Regex = Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}")
    private val SECOND_ACTIVITY_REQUEST_CODE = 0
    private val colorsHelper by lazy { ColorsHelper }
    private val stringsHelper by lazy { StringsHelper }


    override fun inflateBindingLayout(inflater: LayoutInflater): ProfileActivityNewBinding {
        return ProfileActivityNewBinding.inflate(inflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ThemeHandler.getInstance().applyProfilePage(this, binding)
        binding?.colorsData = colorsHelper
        binding?.stringData = stringsHelper
        binding?.toolbar?.colorsData = colorsHelper
        binding?.toolbar?.stringData = stringsHelper
        binding?.connection?.colorsData = ColorsHelper
        binding?.connection?.stringData = stringsHelper
        parseColor()
        inItView()
        connectionObserver()
        isLogin = KsPreferenceKeys.getInstance().appPrefLoginStatus
        modelcall()
        val properties = Properties()
        properties.addAttribute(AppConstants.VIEW_PROFILE, AppConstants.VIEW_PROFILE)
        MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties)
    }

    private fun parseColor() {
        binding?.titleLayout?.background = colorsHelper.strokeBgDrawable(AppColors.appBgColor(), AppColors.profileImageBorderColor(), 200f)
        binding?.userName?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        binding?.userEMail?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        binding?.countryId?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        binding?.userMobile?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        binding?.cityId?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        binding?.dOB?.background = ColorsHelper.strokeBgDrawable(AppColors.tphBgColor(), AppColors.tphBrColor(), 10f)
        ColorsHelper.textViewDrawableColor(binding!!.dOB, AppColors.dobIconColor())
        ColorsHelper.textViewDrawableColor(binding!!.countryId, AppColors.dropDownIconColor())
    }

    private fun modelcall() {
        if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            val tempResponse = KsPreferenceKeys.getInstance().appPrefUser
            if (!StringUtils.isNullOrEmptyOrZero(tempResponse)) {
                setVerify()
            } else {
                val tempResponseApi: String = KsPreferenceKeys.getInstance().appPrefProfile
                setVerifyApi(tempResponseApi)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun inItView() {
        binding?.tvDeleteAccount?.setOnClickListener {
            if (KsPreferenceKeys.getInstance().deleteAccountRequestStatus) {
                isDeletionRequired = false
                commonDialog(
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_under_review.toString(),
                        getString(R.string.popup_under_review))
                    ,
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_your_previous_account_already_review.toString(),
                        getString(R.string.popup_your_previous_account_already_review))
                    ,
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                        getString(R.string.popup_continue)))
            } else {
                isDeletionRequired = true
                commonDialog(
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_delete_account.toString(),
                        getString(R.string.popup_delete_account)
                    ),
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_want_to_delete_account.toString(),
                        getString(R.string.popup_want_to_delete_account)
                    ),
                    stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                        getString(R.string.popup_continue)))
            }
        }

        binding?.toolbar?.titleSkip?.visibility = View.GONE
        binding?.toolbar?.titleMid?.visibility = View.VISIBLE
        binding?.toolbar?.logoMain2?.visibility = View.GONE
        binding?.toolbar?.llSearchIcon?.visibility = View.GONE

        val myProfile = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.profile_my_profile.toString(),
            getString(R.string.profile_my_profile)
        )
        binding?.toolbar?.titleMid?.text = myProfile
        binding?.toolbar?.backLayout?.setOnClickListener { view -> onBackPressed() }
        binding?.llLogin?.setOnClickListener{
            hideSoftKeyboard(binding?.llLogin)
            if (CheckInternetConnection.isOnline(this@ProfileActivityNew)) {
                if(emptyEmail() && validateEmail()){
                    callUpdateApi(encodePin)
                }
            }else{
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
                this@ProfileActivityNew,
                { datepicker, selectedyear, selectedmonth, selectedday ->
                    mcurrentDate[Calendar.YEAR] = selectedyear
                    mcurrentDate[Calendar.MONTH] = selectedmonth
                    mcurrentDate[Calendar.DAY_OF_MONTH] = selectedday
                    val difference = mYear - selectedyear
                    if (difference >= 12) {
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
                        commonDialog(
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_date_difference.toString(),
                                getString(R.string.popup_date_difference)),
                            "",
                            stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                                getString(R.string.popup_continue)))
                    }
                }, mYear, mMonth, mDay
            )
            mDatePicker.show()
        })


        binding?.countryId?.setOnClickListener{
           // ActivityLauncher.getInstance().CountryListActivity(this@ProfileActivityNew, CountryListActivity::class.java)
            val intent = Intent(this, CountryListActivity::class.java)
            startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE)
        }

    }

    private fun deleteAccount(){
        if (NetworkConnectivity.isOnline(this@ProfileActivityNew)) {
            showLoading(binding!!.progressBar, true)
            val token = preference!!.appPrefAccessToken
            viewModel = ViewModelProvider(this@ProfileActivityNew).get(RegistrationLoginViewModel::class.java)
            viewModel!!.deleteAccount(token)
                .observe(this@ProfileActivityNew) { deleteAccountResponse ->
                    dismissLoading(binding!!.progressBar)
                    binding!!.progressBar.visibility = View.GONE
                    if (deleteAccountResponse != null) {
                        if (deleteAccountResponse.responseCode === 2001) {
                            Toast.makeText(this@ProfileActivityNew, getString(R.string.account_deletion_success_msg), Toast.LENGTH_SHORT).show()
                            KsPreferenceKeys.getInstance().deleteAccountRequestStatus = true
                            connectionValidation(true)

                            /*if (mHandler != null) {
                                mHandler.postDelayed(Runnable {
                                    onBackPressed();
                                }, 3000)
                            }*/
                        } else {
                            isInvalidResponseCode = true;
                            if (deleteAccountResponse.responseCode === 4906) {
                                commonDialog(
                                    stringsHelper.stringParse(
                                        stringsHelper.instance()?.data?.config?.popup_delete_account.toString(),
                                        getString(R.string.popup_delete_account)
                                    ),
                                    stringsHelper.stringParse(
                                        stringsHelper.instance()?.data?.config?.popup_your_previous_account_already_review.toString(),
                                        getString(R.string.popup_your_previous_account_already_review))
                                    ,
                                    stringsHelper.stringParse(
                                        stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue))
                                )
                            } else {
                                commonDialog(
                                    stringsHelper.stringParse(
                                        stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                        getString(R.string.popup_error)
                                    ), stringsHelper.stringParse(
                                        stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                        getString(R.string.something_went_wrong)
                                    ), stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue))
                                )
                            }
                        }
                    }
                }
        } else {
            Toast.makeText(
                this@ProfileActivityNew,
                getString(R.string.no_internet_connection),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun setVerifyApi(tempResponseApi: String?) {
        if (!StringUtils.isNullOrEmptyOrZero(tempResponseApi)) {
            binding?.usernameTv?.visibility = View.VISIBLE

            var isUserVerified = KsPreferenceKeys.getInstance().isVerified
            if (isUserVerified.equals("true")) {
               // binding?.toolbar?.verified?.visibility = View.VISIBLE
            } else {
                //binding?.toolbar?.verifyAccount?.visibility = View.VISIBLE
               // binding?.toolbar?.verified?.visibility = View.GONE
               /* binding?.toolbar?.verifyAccount?.setOnClickListener {
                    ActivityLauncher.getInstance().goToEnterOTP(this, EnterOTPActivity::class.java, "profile")
                }*/
            }

            var userName = KsPreferenceKeys.getInstance().appPrefUserName
            if (userName != null && !userName.equals("", ignoreCase = true) && !userName.equals("null", ignoreCase = true)) {
                setNameOrEmail(userName)
            }
        }


    }

    private fun setNameOrEmail(userName: String) {
        binding?.usernameTv?.text = userName
       // binding?.profileText != null

        var value = ""
        try {
            if (userName != null) {
                if (userName != "") {
                    value = userName.trim { it <= ' ' }.replace("\\s+".toRegex(), " ")
                    if (value.contains(" ")) {
                        val words: Array<String> = value.split(" ").toTypedArray()
                        if (words.isNotEmpty()) {
                            val firstWord = words[0][0].toString().uppercase(Locale.getDefault())
                            if (words.size == 1) {
                                value = firstWord
                                binding?.profileText?.text = value
                            } else {
                                val secondWord = words[1][0].toString().uppercase(Locale.getDefault())
                                value = firstWord + secondWord
                                binding?.profileText?.text = value

                            }
                        }
                    } else {
                        value = userName[0].toString().uppercase(Locale.getDefault()) + "" + userName[1].toString().uppercase(Locale.getDefault())
                        binding?.profileText?.text = value

                    }
                }
            }
        } catch (e: java.lang.Exception) {
            binding?.profileText?.text = value
        }
    }

    private fun setVerify() {
        val tempResponse = mPreference?.appPrefUser
        if (!StringUtils.isNullOrEmptyOrZero(tempResponse)) {
            val dataModel = Gson().fromJson(
                tempResponse,
                AppUserModel::class.java
            )
            if (dataModel != null) {
                var userName = dataModel.name
                var isUserVerified = dataModel.isVerified
                if (isUserVerified) {
                   // binding?.toolbar?.verified?.visibility = View.VISIBLE
                } else {
                  //  binding?.toolbar?.verifyAccount?.visibility = View.VISIBLE
                  //  binding?.toolbar?.verified?.visibility = View.GONE
                   /* binding?.toolbar?.verifyAccount?.setOnClickListener {
                        ActivityLauncher.getInstance().goToEnterOTP(this, EnterOTPActivity::class.java, "profile")
                    }*/
                }
                if (userName != null && !userName.equals("", ignoreCase = true) && !userName.equals("null", ignoreCase = true)) {
                    setNameOrEmail(userName)
                }
            }
        }

    }


    private fun callUpdateApi(encodePin: String) {
        showLoading(binding?.progressBar, true)
        var typeList: String? = ""
        var speciesList: String? = ""
        val profileId = KsPreferenceKeys.getInstance().preferenceProfileId
        if (null != KsPreferenceKeys.getInstance().speciesList) {
            typeList = AppCommonMethod.speciesList
        }
        if (null != KsPreferenceKeys.getInstance().typeList) {
            speciesList = AppCommonMethod.typeList
        }

        val token = KsPreferenceKeys.getInstance().appPrefAccessToken

        viewModel?.hitUpdateProfile(this,token,binding?.userName?.text.toString().trim(),binding?.userMobile?.text.toString().trim(),"",dateMilliseconds,"",imageUrlId,via,"",true,encodePin
            ,binding?.cityId?.text.toString().trim(),binding?.countryId?.text.toString(),profileId,speciesList,typeList)?.observe(this
        ) {
            if (it != null) {
                if (it.status) {
                    dismissLoading(binding?.progressBar)
                    val gson = Gson()
                    val userProfileData = gson.toJson(it)
                    KsPreferenceKeys.getInstance().userProfileData = userProfileData
                    isUpdateRecord=true
                    Logger.e("userdata",userProfileData)
                    commonDialog(
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_update.toString(),
                            getString(R.string.popup_update)),
                        stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_update_successfully.toString(),
                            getString(R.string.popup_update_successfully))
                        , stringsHelper.stringParse(
                            stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                            getString(R.string.popup_continue)))
                    updateUI(it)
                    val properties = Properties()
                    properties.addAttribute(AppConstants.UPDATE_PROFILE, AppConstants.UPDATE_PROFILE)
                    MoEHelper.getInstance(applicationContext).trackEvent(AppConstants.TAB_SCREEN_VIEWED, properties)
                }else{
                    if(it.responseCode===4302){
                        isloggedout = true
                        dismissLoading(binding?.progressBar)
                        logoutCall()
                        ActivityLauncher.getInstance().goToLogin(this@ProfileActivityNew, ActivityLogin::class.java)
                        try {
                            runOnUiThread { onBackPressed() }
                        } catch (e: java.lang.Exception) {
                            Logger.d(e.toString())
                        }
                    }else if(it.responseCode===4019){
                        commonDialog(it.debugMessage.toString(),"",stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
                    }else{
                        if (it.debugMessage!=null){
                            commonDialog(it.debugMessage.toString(),"",stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
                        }else{
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
            }else{
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

    var newObject: UserProfileResponse? = null
    private fun parseProfile() {
        try {
            saved = ArrayList()
            val gson = Gson()
            val json = KsPreferenceKeys.getInstance().userProfileData
            Logger.e("userprofiledata", KsPreferenceKeys.getInstance().userProfileData)
            newObject = gson.fromJson(json, UserProfileResponse::class.java)
            Logger.d("savedata3: " + newObject?.data?.customData?.contentPreferences)
            if (newObject?.data?.customData != null && newObject?.data?.customData?.contentPreferences != null) {
                contentPreference = newObject?.data?.customData?.contentPreferences!!
            }
        } catch (e: Exception) {
            Logger.w(e)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    private fun transferObserverListener(transferObserver: TransferObserver) {
        transferObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {
                    dismissLoading(binding?.progressBar)
                }
            }
            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {}
            override fun onError(id: Int, ex: Exception) {}
        })
    }
    override fun onStart() {
        super.onStart()
        AppCommonMethod.Url = ""
        AppCommonMethod.UriId = ""
    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm = supportFragmentManager
        val commonDialogFragment = CommonDialogFragment.newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }
    override fun onActionBtnClicked() {
        if (isDeletionRequired){
            deleteAccount()
        }
        if(isUpdateRecord){
            ActivityLauncher.getInstance().goToAccountSetting(this, AccountSettingActivity::class.java)
        }
    }

    private fun logoutCall() {
        if (CheckInternetConnection.isOnline(Objects.requireNonNull(this))) {
            clearCredientials(preference)
            hitApiLogout(this, KsPreferenceKeys.getInstance().appPrefAccessToken)
        }else{
            commonDialog(stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "", stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))

        }
    }

    private fun updateUI(it: UserProfileResponse) {
        try {
            var emailId = ""
            if (ObjectHelper.isNotEmpty(it.data.email)) {
                emailId = it?.data.email as String
            }
            var phoneNumber = ""
            if (ObjectHelper.isNotEmpty(it.data.phoneNumber)) {
                phoneNumber = it?.data.phoneNumber as String
            }
            var dob = 0.0
            if (ObjectHelper.isNotEmpty(it.data.dateOfBirth)) {
                dob = it?.data.dateOfBirth as Double
            }
            setUserProperties(applicationContext, it.data.id, it.data.name, emailId, phoneNumber,dob.toLong())
            binding?.userName?.setText(it.data.name)
            if (it.data.customData.country !=null) {
                binding?.countryId?.text = it.data.customData.country
            }
            if (it.data.customData.mobileNumber != null) {
                binding?.userMobile?.setText(
                    it.data.customData.mobileNumber as String
                )
            }
            binding?.userName?.text?.length?.let { it -> binding?.userName?.setSelection(it) }
            binding?.userEMail?.setText(it.data.email as String)
            binding?.userEMail?.isEnabled=false
            binding?.userEMail?.isFocusable = false
            if (it.data.customData.mobileNumber != null) {
                binding?.userMobile?.setText(
                    it.data.customData.mobileNumber.toString() + ""
                )
                binding?.userMobile?.isEnabled = true
                binding?.userMobile?.isFocusable = true
            } else {
                binding?.userMobile?.isEnabled = true
                binding?.userMobile?.isFocusable = true
            }
            if (it.data.dateOfBirth != null) {
                val df = DecimalFormat("#")
                df.maximumFractionDigits = 0
                val l = df.format(it.data.dateOfBirth).toLong()
                dateMilliseconds = l.toString()
                val dateString = DateFormat.format("dd MMMM yyyy", Date(l)).toString()
                binding?.dOB?.text = dateString
            }

            if (it.data.customData != null) {
                if (it.data.customData.city != null
                ) binding?.cityId?.setText(
                    it.data.customData.city
                )
            }

            KsPreferenceKeys.getInstance().appPrefUserName = (it.data.name as String)
            KsPreferenceKeys.getInstance().appPrefMobileNumber = (it.data.customData.mobileNumber.toString())

        } catch (e: java.lang.Exception) {
            Logger.w(e)
        }
    }

    private fun connectionObserver() {
        if (NetworkConnectivity.isOnline(this@ProfileActivityNew)) {
            preference = KsPreferenceKeys.getInstance()
            binding?.connection?.noConnectionLayout?.visibility = View.GONE
            binding?.updateProfileLayout?.visibility = View.VISIBLE
            connectionValidation(true)
        } else {
            noConnectionLayout()
            connectionValidation(false)
        }
    }

    private fun connectionValidation(connected : Boolean) {
        if (connected) {
            viewModel = ViewModelProvider(this@ProfileActivityNew).get(RegistrationLoginViewModel::class.java)
            val preference = KsPreferenceKeys.getInstance()
            val authToken = preference.appPrefAccessToken
            showLoading(binding?.progressBar,true)
            viewModel?.hitUserProfile(this@ProfileActivityNew,authToken)?.observe(this) {
                if (it != null) {
                    if (it.status) {
                        Logger.e("profileRes", it.toString())
                        if (it.data.deletionRequestStatus != null) {
                            var deletionRequestStatus: String = it.data.deletionRequestStatus
                            if (deletionRequestStatus.equals("UNDER_REVIEW")) {
                                KsPreferenceKeys.getInstance().deleteAccountRequestStatus = true
                            } else {
                                KsPreferenceKeys.getInstance().deleteAccountRequestStatus = false
                            }
                        }
                        val gson = Gson()
                        val userProfileData = gson.toJson(it)
                        Logger.e("userdata1",userProfileData)
                        updateUI(it)
                        dismissLoading(binding?.progressBar)
                    }
                    if (it.responseCode === 4302) {
                        isloggedout = true
                        logoutCall()
                        ActivityLauncher.getInstance().goToLogin(this@ProfileActivityNew, ActivityLogin::class.java)

                        try {
                            runOnUiThread { onBackPressed() }
                        } catch (e: java.lang.Exception) {
                        }
                    } else if (it.responseCode === 4019) {
                        commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ), it.debugMessage.toString(), stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
                    } else if (it.responseCode === 4901) {
                        commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ), it.debugMessage.toString(), stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
                    } else {
                    }


                }else{
                    commonDialog(stringsHelper.stringParse(
                                    stringsHelper.instance()?.data?.config?.popup_error.toString(),
                                    getString(R.string.popup_error)
                                ),stringsHelper.stringParse(
                                stringsHelper.instance()?.data?.config?.popup_something_went_wrong.toString(),
                                getString(R.string.something_went_wrong)
                            ) , stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))

                }
            }

        }else{
            commonDialog(stringsHelper.stringParse(
                        stringsHelper.instance()?.data?.config?.popup_no_internet_connection_found.toString(),
                        getString(R.string.popup_no_internet_connection_found)
                    ), "", stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(), getString(R.string.popup_continue)))
        }
    }

    fun noConnectionLayout() {
        binding?.updateProfileLayout?.visibility = View.GONE
        binding?.connection?.noConnectionLayout?.visibility = View.VISIBLE
        binding?.connection?.retryTxt?.setOnClickListener { view: View? -> connectionObserver() }
    }


    private fun emptyEmail(): Boolean {
        var check = false
        if (StringUtils.isNullOrEmptyOrZero(binding?.userEMail?.text.toString().trim())) {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_empty_email_tittle.toString(),
                    getString(R.string.popup_empty_email_tittle)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_empty_email_subtitle.toString(),
                    getString(R.string.popup_empty_email_subtitle)
                ),
                stringsHelper.stringParse(stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)))
        } else {
            check = true
        }
        return check
    }

//    private fun validateDate(): Boolean {
//        var check = false
//        if (StringUtils.isNullOrEmptyOrZero(binding?.dOB?.text.toString().trim())) {
//            commonDialog(applicationContext.resources.getString(R.string.empty_date),applicationContext.resources.getString(R.string.please_enter_a_valid_date), applicationContext.resources.getString(R.string.enter_date));
//        } else {
//            check = true
//        }
//        return check
//    }
    private fun validateEmail(): Boolean {
        var check = false
        if (binding?.userEMail?.text.toString().trim().matches(regex)) {
            check = true
        } else {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_invalid_email_tittle.toString(),
                    getString(R.string.popup_invalid_email_tittle)
                ), stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_empty_email_subtitle.toString(),
                    getString(R.string.popup_empty_email_subtitle)
                ), stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)
                )
            )
        }
        return check
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                countryName = data?.getStringExtra("countryName").toString()
                binding?.countryId?.text = countryName

            }
        }
    }
}