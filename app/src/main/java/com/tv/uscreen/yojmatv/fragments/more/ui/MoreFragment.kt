package com.tv.uscreen.yojmatv.fragments.more.ui


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tv.uscreen.yojmatv.R
import com.tv.uscreen.yojmatv.activities.homeactivity.ui.HomeActivity
import com.tv.uscreen.yojmatv.activities.homeactivity.viewmodel.HomeViewModel
import com.tv.uscreen.yojmatv.activities.listing.ui.MyListActivity
import com.tv.uscreen.yojmatv.activities.profile.ui.AccountSettingActivity
import com.tv.uscreen.yojmatv.activities.purchase.plans_layer.GetPlansLayer
import com.tv.uscreen.yojmatv.activities.settings.ActivitySettings
import com.tv.uscreen.yojmatv.activities.usermanagment.ui.ActivityLogin
import com.tv.uscreen.yojmatv.baseModels.BaseBindingFragment
import com.tv.uscreen.yojmatv.beanModel.AppUserModel
import com.tv.uscreen.yojmatv.beanModel.userProfile.UserProfileResponse
import com.tv.uscreen.yojmatv.callbacks.commonCallbacks.MoreItemClickListener
import com.tv.uscreen.yojmatv.cms.HelpActivity
import com.tv.uscreen.yojmatv.databinding.FragmentMoreBinding
import com.tv.uscreen.yojmatv.fragments.dialog.CommonDialogFragment
import com.tv.uscreen.yojmatv.fragments.more.adapter.MoreListAdapter
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.ObjectHelper
import com.tv.uscreen.yojmatv.utils.TrackerUtil.MoEUserTracker.setUserProperties
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.helpers.CheckInternetConnection
import com.tv.uscreen.yojmatv.utils.helpers.StringUtils
import com.tv.uscreen.yojmatv.utils.helpers.intentlaunchers.ActivityLauncher
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper

class MoreFragment : BaseBindingFragment<FragmentMoreBinding?>(), CommonDialogFragment.EditDialogListener, MoreItemClickListener {
    private var mPreference: KsPreferenceKeys? = null
    private var isLogin: String? = null
    private var mListLogin: MutableList<String>? = null
    private val viewModel: HomeViewModel? = null
    private var hasEntitlement = false
    private var token = ""
    private var userState = ""
    private var gaming= ""
    private var myList= ""
    private var account= ""
    private var settings= ""
    private var buyNow= ""
    private var orderHistory= ""
    private var termsCondition= ""
    private var manageSubscription= ""
    private var privacyPolicy= ""
    private var contactUs= ""


    private val stringsHelper by lazy { StringsHelper }
    private val colorsHelper by lazy { ColorsHelper }
    public override fun inflateBindingLayout(inflater: LayoutInflater): FragmentMoreBinding {
        return FragmentMoreBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userState = KsPreferenceKeys.getInstance().appPrefLoginStatus
        parseColor()
        Logger.e("userState", userState)
        mPreference = KsPreferenceKeys.getInstance()
        setUI()
    }

    private fun parseColor() {
        binding!!.stringData = stringsHelper
        binding!!.colorsData = colorsHelper
        binding?.connection?.colorsData = colorsHelper
        binding?.connection?.stringData = stringsHelper
       // binding?.titleLayout?.background = colorsHelper.strokeBgDrawable(AppColors.appBgColor(), AppColors.profileImageBorderColor(), 200f)
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

    private fun setVerifyApi(tempResponse: String?) {
        if (!StringUtils.isNullOrEmptyOrZero(tempResponse)) {
            binding!!.usernameTv.visibility = View.VISIBLE
            val userName = mPreference!!.appPrefUserName
            if (userName != null && !userName.equals("", ignoreCase = true) && !userName.equals("null", ignoreCase = true)) {
                setNameOrEmail(userName)
            }
            // setUIComponets(mListLogin, true);
        }
    }

    private fun modelCall(hasEntitlement: Boolean) {
        binding!!.progressBar.visibility = View.GONE
        gaming = stringsHelper.stringParse(
        stringsHelper.instance()?.data?.config?.more_gaming.toString(),
        getString(R.string.more_gaming)
        )
         myList = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.my_list.toString(),
            getString(R.string.my_list)
        )
         account = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_account.toString(),
            getString(R.string.more_account)
        )
         settings = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_settings.toString(),
            getString(R.string.more_settings)
        )
        /* buyNow = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_buy_now.toString(),
            getString(R.string.more_buy_now)
        )
         manageSubscription = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_manage_subscription.toString(),
            getString(R.string.more_manage_subscription)
        )*/
        /* orderHistory = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_order_history.toString(),
            getString(R.string.more_order_history)
        )*/
         privacyPolicy = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_privacy_policy.toString(),
            getString(R.string.more_privacy_policy)
        )
         termsCondition = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_term_condition.toString(),
            getString(R.string.more_term_condition)
        )

        contactUs = stringsHelper.stringParse(
            stringsHelper.instance()?.data?.config?.more_contact_us.toString(),
            getString(R.string.more_contact_us)
        )

        val label3 = arrayOf(myList, account, settings,privacyPolicy, termsCondition,contactUs)
        val mListLogOut: List<String> = ArrayList(listOf(*label3))
        val mListWithSub: List<String> = ArrayList(listOf(*label3))
        mListLogin = ArrayList()
        (mListLogin as ArrayList<String>).addAll(listOf(*label3))
        if (isLogin.equals("Login", ignoreCase = true)) {
            if (hasEntitlement) {
                setUIComponets(mListLogin as ArrayList<String>)
            } else {
                setUIComponets(mListWithSub)
            }
        } else {
            setUIComponets(mListLogOut)
        }
    }

    private fun setUIComponets(mList: List<String>) {
        val adapter = MoreListAdapter(requireActivity(), mList, this, false)
        binding!!.moreFragmentsRv.hasFixedSize()
        binding!!.moreFragmentsRv.isNestedScrollingEnabled = false
        binding!!.moreFragmentsRv.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
        binding!!.moreFragmentsRv.adapter = adapter
    }

    fun clickEvent() {
        callGetPlans()
        try {
            isLogin = mPreference?.appPrefLoginStatus
            if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
                val token = mPreference?.appPrefAccessToken
                viewModel!!.hitUserProfile(context, token).observe(requireActivity()) { userProfileResponse: UserProfileResponse? ->
                    if (userProfileResponse != null && userProfileResponse.status) {
                        updateUI(userProfileResponse)
                    }
                }
            } else {
                binding!!.titleLayout.visibility = View.VISIBLE
            }
        } catch (ex: Exception) {
            Logger.w(ex)
        }
    }

    private fun callGetPlans() {
        binding!!.progressBar.visibility = View.VISIBLE
        GetPlansLayer.getInstance().getEntitlementStatus(KsPreferenceKeys.getInstance(), token) { entitlementStatus: Boolean, apiStatus: Boolean, responseCode: Int ->
            hasEntitlement = if (apiStatus) {
                entitlementStatus
            } else {
                if (responseCode == 403) {
                    logoutCall()
                    clearCredientials(mPreference)
                    ActivityLauncher.getInstance().homeActivity(requireActivity(), HomeActivity::class.java)
                }
                false
            }
            modelCall(hasEntitlement)
        }
    }

    private fun updateUI(userProfileResponse: UserProfileResponse) {
        try {
            mPreference!!.appPrefUserName = mPreference!!.appPrefUserName
            mPreference!!.appPrefUserEmail = userProfileResponse.data.email.toString()
            var userName = mPreference!!.appPrefUserName
            if (userName != null && !userName.equals("", ignoreCase = true)) {
                setNameOrEmail(userName)
            } else {
                userName = mPreference!!.appPrefUserEmail
                if (userName != null && !userName.equals("", ignoreCase = true)) {
                    setNameOrEmail(userName)
                }
            }
            binding!!.titleLayout.visibility = View.VISIBLE
            //TODO uncomment this below line after updated API
//            setUserImage(userProfileResponse);
            val gson = Gson()
            val userProfileData = gson.toJson(userProfileResponse)
            KsPreferenceKeys.getInstance().userProfileData = userProfileData
            Logger.d("savedata2 " + KsPreferenceKeys.getInstance().userProfileData)
            val json = KsPreferenceKeys.getInstance().userProfileData
            val newObject = gson.fromJson(json, UserProfileResponse::class.java)
            Logger.d("savedata3 $newObject")
            var emailId = ""
            if (ObjectHelper.isNotEmpty(newObject.data.email)) {
                emailId = newObject.data.email as String
            }
            var phoneNumber = ""
            if (ObjectHelper.isNotEmpty(newObject.data.phoneNumber)) {
                phoneNumber = newObject.data.phoneNumber as String
            }
            var dob = 0L
            if (ObjectHelper.isNotEmpty(newObject.data.dateOfBirth)) {
                dob = newObject.data.dateOfBirth as Long
            }
            setUserProperties(activity, newObject.data.id, newObject.data.name, emailId, phoneNumber, dob)
        } catch (e: Exception) {
            Logger.w(e)
        }
    }

    private fun setNameOrEmail(userName: String) {
        binding?.usernameTv?.text = userName
        binding?.profileText?.text = userName.substring(0, 2)
    }

    fun updateAppSync() {
        isLogin = mPreference!!.appPrefLoginStatus
        if (isLogin.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)) {
            setVerify()
        }
    }

    private fun setVerify() {
        val tempResponse = mPreference!!.appPrefUser
        if (!StringUtils.isNullOrEmptyOrZero(tempResponse)) {
            val dataModel = Gson().fromJson(tempResponse, AppUserModel::class.java)
            //getBinding().loginBtn.setVisibility(View.GONE);
            if (dataModel != null) {
                val userName = dataModel.name
                if (userName != null && !userName.equals("", ignoreCase = true) && !userName.equals("null", ignoreCase = true)) {
                    setNameOrEmail(userName)
                } else {
                  //  binding!!.profileText.setBackgroundResource(R.drawable.profile)
                }
            }
        }
    }

    override fun onClick(caption: String) {
        val loginStatus = KsPreferenceKeys.getInstance().appPrefLoginStatus.equals(AppConstants.UserStatus.Login.toString(), ignoreCase = true)
       if (caption == myList) {
            if (loginStatus) {
                ActivityLauncher.getInstance().gotoList(requireActivity(), MyListActivity::class.java)
            } else {
                ActivityLauncher.getInstance().goToLogin(requireActivity(), ActivityLogin::class.java)
            }
        } else if (caption == account) {
            if (loginStatus) {
                ActivityLauncher.getInstance().goToAccountSetting(requireActivity(), AccountSettingActivity::class.java)
            } else {
                ActivityLauncher.getInstance().goToLogin(requireActivity(), ActivityLogin::class.java)
            }
        } else if (caption == settings) {
            ActivityLauncher.getInstance().goToSetting(requireActivity(), ActivitySettings::class.java)
        }
      /* else if (caption == buyNow) {
            if (loginStatus) {
                if (hasEntitlement) {
                    ActivityLauncher.getInstance().manageAccount(requireActivity(), ManageSubscriptionAccount::class.java)
                } else {
                    ActivityLauncher.getInstance().goToPlanScreen(requireActivity(), ActivitySelectSubscriptionPlan::class.java, "")
                }
            } else {
                ActivityLauncher.getInstance().goToLogin(requireActivity(), ActivityLogin::class.java)
            }
        }
       else if (caption ==manageSubscription) {
            if (loginStatus) {
                ActivityLauncher.getInstance().goToPlanScreen(requireActivity(), ActivitySelectSubscriptionPlan::class.java, "moreFragment")
            } else {
                ActivityLauncher.getInstance().goToLogin(requireActivity(), ActivityLogin::class.java)
            }
        }
       else if (caption == orderHistory) {
            if (loginStatus) {
                ActivityLauncher.getInstance().orderHistroy(requireActivity(), OrderHistoryActivity::class.java)
            } else {
                ActivityLauncher.getInstance().goToLogin(requireActivity(), ActivityLogin::class.java)
            }
        }*/
       else if (caption == privacyPolicy) {
            requireActivity().startActivity(Intent(requireActivity(), HelpActivity::class.java).putExtra("type", "2"))
        } else if (caption == termsCondition) {
            requireActivity().startActivity(Intent(requireActivity(), HelpActivity::class.java).putExtra("type", "1"))
        }
       else if (caption == contactUs) {
           requireActivity().startActivity(Intent(requireActivity(), HelpActivity::class.java).putExtra("type", "3"))
       }
    }

    inner class AppSyncBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                updateAppSync()
            } catch (e: Exception) {
                Logger.w(e)
            }
        }
    }

    private fun logoutCall() {
        if (CheckInternetConnection.isOnline(requireActivity())) {
            clearCredientials(mPreference)
            hitApiLogout(activity, mPreference!!.appPrefAccessToken)
        }
    }

    override fun onActionBtnClicked() {
        if (CheckInternetConnection.isOnline(requireActivity())) {
            logoutCall()
            clearCredientials(mPreference)
            ActivityLauncher.getInstance().homeActivity(requireActivity(), HomeActivity::class.java)
        }
    }
}