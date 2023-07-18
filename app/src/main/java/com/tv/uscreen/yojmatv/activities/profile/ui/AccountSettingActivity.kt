package com.tv.uscreen.yojmatv.activities.profile.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tv.uscreen.yojmatv.R


import com.tv.uscreen.yojmatv.activities.homeactivity.ui.HomeActivity
import com.tv.uscreen.yojmatv.activities.purchase.plans_layer.GetPlansLayer
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.ActivityLogin
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.ChangePasswordActivity
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.beanModel.AppUserModel
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.MoreItemClickListener
import com.tv.uscreen.yojmatv.databinding.AccountSettingActivityBinding
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment.Companion.newInstance
import com.tv.uscreen.yojmatv.fragments.more.adapter.AccountListAdapter
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.commonMethods.AppCommonMethod
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.CheckInternetConnection
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import java.util.Locale

class AccountSettingActivity : BaseBindingActivity<AccountSettingActivityBinding?>(), CommonDialogFragment.EditDialogListener, MoreItemClickListener {
    private var mPreference: KsPreferenceKeys? = null
    private var isLogin: String? = null
    private var mListLogin: MutableList<String>? = null
    private var token = ""
    private var userState = ""
    private var hasEntitlement = false
    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }
    override fun inflateBindingLayout(inflater: LayoutInflater): AccountSettingActivityBinding {
        return AccountSettingActivityBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding!!.colorsData = ColorsHelper
        binding!!.stringData = stringsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
        binding!!.toolbar.colorsData = colorsHelper
        setToolbar()
        userState = KsPreferenceKeys.getInstance().appPrefLoginStatus
        Logger.e("userState", userState)
        mPreference = KsPreferenceKeys.getInstance()
        setUI()
        callGetPlans()
    }

    private fun setToolbar() {
        binding!!.toolbar.logoMain2.visibility = View.GONE
        binding!!.toolbar.rlToolBar.visibility = View.GONE
        binding!!.toolbar.backLayout.visibility = View.VISIBLE
        binding!!.toolbar.titleMid.visibility = View.VISIBLE
        val account = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_account.toString(),
            getString(R.string.more_account)
        )
        binding!!.toolbar.titleMid.text = account
        binding!!.toolbar.titleMid.setBackgroundResource(0)
        binding!!.toolbar.backLayout.setOnClickListener { onBackPressed() }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        userState = KsPreferenceKeys.getInstance().appPrefLoginStatus
        Logger.e("userState", userState)
        mPreference = KsPreferenceKeys.getInstance()
        setUI()
        callGetPlans()
    }

    private fun setUI() {
        token = KsPreferenceKeys.getInstance().appPrefAccessToken
        isLogin = mPreference!!.appPrefLoginStatus
        if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            val tempResponse = KsPreferenceKeys.getInstance().appPrefUser
            if (!StringUtils.isNullOrEmptyOrZero(tempResponse)) {
                setVerify()
            } else {
                val tempResponseApi = mPreference!!.appPrefProfile
                setVerifyApi(tempResponseApi)
            }
        }
    }

    private fun callGetPlans() {
        binding!!.progressBar.visibility = View.VISIBLE
        GetPlansLayer.getInstance().getEntitlementStatus(KsPreferenceKeys.getInstance(), token) { entitlementStatus: Boolean, apiStatus: Boolean, responseCode: Int ->
            hasEntitlement = if (apiStatus) {
                entitlementStatus
            } else {
                if (responseCode == 403) {
                    hitApiLogout(this, token)
                    clearCredientials(mPreference)
                    ActivityLauncher.getInstance().homeActivity(this, HomeActivity::class.java)
                }
                false
            }
            modelCall(hasEntitlement)
        }
    }

    private fun modelCall(hasEntitlement: Boolean) {
        binding!!.progressBar.visibility = View.GONE
//        val label1 = this.resources.getStringArray(R.array.account_option)

        val editProfile = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.account_edit_profile.toString(),
            getString(R.string.account_edit_profile)
        )
         val changePassword = stringsHelper.stringParse(
             stringsHelper.instance()?.data?.config?.account_change_password.toString(),
             getString(R.string.account_change_password)
         )
         val logout =stringsHelper.stringParse(
             stringsHelper.instance()?.data?.config?.account_logout.toString(),
             getString(R.string.account_logout)
         )

        val label1 = arrayOf(editProfile, changePassword, logout)

        val accountOptions: List<String> = ArrayList(listOf(*label1))
        mListLogin = ArrayList()
        mListLogin?.addAll(listOf(*label1))
        mPreference = KsPreferenceKeys.getInstance()
        setUIComponets(accountOptions, false)
    }

    private fun setVerifyApi(tempResponse: String?) {
        if (!StringUtils.isNullOrEmptyOrZero(tempResponse)) {
            binding!!.usernameTv.visibility = View.VISIBLE
            val userName = mPreference!!.appPrefUserName
            if (userName != null && !userName.equals("", ignoreCase = true) && !userName.equals("null", ignoreCase = true)) {
                setNameOrEmail(userName)
            }
        }
    }

    private fun setVerify() {
        val tempResponse = mPreference!!.appPrefUser
        if (!StringUtils.isNullOrEmptyOrZero(tempResponse)) {
            val dataModel = Gson().fromJson(tempResponse, AppUserModel::class.java)
            if (dataModel != null) {
                val userName = dataModel.name
                if (userName != null && !userName.equals("", ignoreCase = true) && !userName.equals("null", ignoreCase = true)) {
                    setNameOrEmail(userName)
                }
            }
        }
    }

    private fun setNameOrEmail(userName: String?) {
        binding!!.usernameTv.text = userName
        var value = ""
        try {
            if (userName != null) {
                if (userName != "") {
                    value = userName.trim { it <= ' ' }.replace("\\s+".toRegex(), " ")
                    if (value.contains(" ")) {
                        val words = value.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (words.isNotEmpty()) {
                            val firstWord = words[0][0].toString().uppercase(Locale.getDefault())
                            if (words.size == 1) {
                                value = firstWord
                                binding!!.profileText.text = value
                            } else {
                                val secondWord = words[1][0].toString().uppercase(Locale.getDefault())
                                value = firstWord + secondWord
                                binding!!.profileText.text = value
                            }
                        }
                    } else {
                        value = userName[0].toString().uppercase(Locale.getDefault()) + "" + userName[1].toString().uppercase(Locale.getDefault())
                        binding!!.profileText.text = value
                    }
                }
            }
        } catch (e: Exception) {
            binding!!.profileText.text = value
        }
    }

    private fun setUIComponets(mList: List<String>, isLogin: Boolean) {
        val adapter = AccountListAdapter(this, mList, this, isLogin)
        binding!!.moreFragmentsRv.hasFixedSize()
        binding!!.moreFragmentsRv.isNestedScrollingEnabled = false
        binding!!.moreFragmentsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding!!.moreFragmentsRv.adapter = adapter
    }

    private fun commonDialog(title: String, description: String, actionBtn: String) {
        val fm = supportFragmentManager
        val commonDialogFragment = newInstance(title, description, actionBtn)
        commonDialogFragment.setEditDialogCallBack(this)
        commonDialogFragment.show(fm, AppConstants.MESSAGE)
    }

    override fun onActionBtnClicked() {
        if (CheckInternetConnection.isOnline(this)) {
            hitApiLogout(this, token)
            AppCommonMethod.screenViewedTrack(applicationContext, AppConstants.LOGOUT, "AccountSettingActivity")
            clearCredientials(mPreference)
            ActivityLauncher.getInstance().loginActivity(this, ActivityLogin::class.java)
        }
    }

    override fun onClick(caption: String) {
        val loginStatus = KsPreferenceKeys.getInstance().appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)
        if (caption == getString(R.string.edit_profile)) {
            if (loginStatus) {
                ActivityLauncher.getInstance().ProfileActivityNew(this, ProfileActivityNew::class.java)
            } else {
                ActivityLauncher.getInstance().loginActivity(this, ActivityLogin::class.java)
            }
        } else if (caption == getString(R.string.change_password)) {
            if (loginStatus) {
                ActivityLauncher.getInstance().changePassword(this, ChangePasswordActivity::class.java)
            } else {
                ActivityLauncher.getInstance().loginActivity(this, ActivityLogin::class.java)
            }
        } else if (caption == getString(R.string.account_logout)) {
            commonDialog(
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_logout.toString(),
                    getString(R.string.popup_logout)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_logout_you_want_to_logout.toString(),
                    getString(R.string.popup_logout_you_want_to_logout)
                ),
                stringsHelper.stringParse(
                    stringsHelper.instance()?.data?.config?.popup_continue.toString(),
                    getString(R.string.popup_continue)
                )
            )
        }
    }
}