package com.tv.uscreen.yojmatv.activities.notification.ui

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.moengage.inbox.core.model.InboxMessage
import com.tv.uscreen.yojmatv.R

import com.tv.uscreen.yojmatv.activities.notification.adapter.NotificationListAdapter
import com.tv.uscreen.yojmatv.activities.notification.viewmodel.NotificationViewModel
import com.tv.uscreen.yojmatv.baseModels.BaseBindingActivity
import com.tv.uscreen.yojmatv.databinding.ActivityNotificationBinding

import com.tv.uscreen.yojmatv.fragments.dialog.AlertDialogFragment
import com.tv.uscreen.yojmatv.utils.Logger
import com.tv.uscreen.yojmatv.utils.ObjectHelper
import com.tv.uscreen.yojmatv.utils.TrackerUtil.MoEngageNotificationManager
import com.tv.uscreen.yojmatv.utils.colorsJson.converter.ColorsHelper
import com.tv.uscreen.yojmatv.utils.constants.AppConstants
import com.tv.uscreen.yojmatv.utils.cropImage.helpers.NetworkConnectivity
import com.tv.uscreen.yojmatv.utils.helpers.ksPreferenceKeys.KsPreferenceKeys
import com.tv.uscreen.yojmatv.utils.stringsJson.converter.StringsHelper
import org.json.JSONObject
import java.util.Objects


class NotificationActivity : BaseBindingActivity<ActivityNotificationBinding>()  {

    private val stringsHelper by lazy { StringsHelper }
    private var viewModel: NotificationViewModel? = null
    private var notificationAdapter: NotificationListAdapter? = null

    private var itemClickListener: NotificationListAdapter.OnItemClickListener? =
        object : NotificationListAdapter.OnItemClickListener {
            override fun onItemClicked(inboxMessage: InboxMessage) {
                MoEngageNotificationManager.markAsRead(inboxMessage)
                notificationAdapter?.updateItem(inboxMessage)

                Logger.d("clicked on : $inboxMessage")
                val assetType: String? = inboxMessage.payload.optString("mediaType")
                val assetId: String? = inboxMessage.payload.optString("id")
                val seriesId: String? = inboxMessage.payload.optString("seriesId")
                val seasonNumber: String? = inboxMessage.payload.optString("seasonNumber")

                Logger.d("assetType: $assetType | assetId: $assetId | seriesId: $seriesId | seasonNumber: $seasonNumber")

                val json = JSONObject()
                json.apply {
                    put("mediaType", assetType)
                    put("id", assetId)
                    put("seriesId", seriesId)
                    put("seasonNumber", seasonNumber)
                }

                branchRedirections(json)
            }

            override fun onDeleteClicked(inboxMessage: InboxMessage) {
                showAlertDialog(
                    message = getString(R.string.delete_notification_single),
                    positiveLabel = getString(R.string.ok),
                    positiveAction = {
                        MoEngageNotificationManager.deleteNotification(inboxMessage)
                        notificationAdapter?.deleteItem(inboxMessage)
                    },
                    negativeLabel = getString(R.string.cancel),
                )
            }
        }

    override fun inflateBindingLayout(inflater: LayoutInflater): ActivityNotificationBinding {
        return ActivityNotificationBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding?.toolbar?.logoMain2?.visibility = View.GONE
        binding?.stringData = stringsHelper
        binding.connection.colorsData = ColorsHelper
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]
        notificationAdapter = NotificationListAdapter()

        setupToolbarIcons()
        setupListener()

        if (KsPreferenceKeys.getInstance().currentTheme.equals(
                AppConstants.LIGHT_THEME,
                ignoreCase = true
            )
        ) {
            binding.tvNoData.setTextColor(ContextCompat.getColor(binding.tvNoData.context, R.color.series_detail_description_text_color))
        } else {
            binding.tvNoData.setTextColor(ContextCompat.getColor(binding.tvNoData.context, R.color.tph_hint_txt_color))
        }

        if (NetworkConnectivity.isOnline(this@NotificationActivity)) {
            getNotifications()
        } else {
            showNoNetwork()
        }

    }

    fun getNotifications() {
        MoEngageNotificationManager.getAllNotifications().observe(this) { list ->
            binding?.noConnectionLayout?.visibility = View.GONE

            val hasData = ObjectHelper.isNotEmpty(list)
            initNoData(hasData)

            if (hasData) {
                initRecyclerView(list.inboxMessages)
            }
        }
    }


    private fun setupListener() {
        binding?.toolbar?.clDelete?.setOnClickListener {
            notificationAdapter?.getDataList()?.let { dataList ->
                showAlertDialog(
                    message = getString(R.string.delete_notification_all),
                    positiveLabel = getString(R.string.ok),
                    positiveAction = {
                        MoEngageNotificationManager.deleteAllNotifications(dataList)
                        notificationAdapter?.clearData()
                        initNoData(false)
                    },
                    negativeLabel = getString(R.string.cancel),
                )
            }
        }
    }


    override fun onDestroy() {
        itemClickListener = null
        notificationAdapter = null
        binding?.toolbar?.clDelete?.setOnClickListener(null)
        super.onDestroy()
    }

    private fun setupToolbarIcons() {
        binding?.toolbar?.let {
            val notificationTitle = stringsHelper.stringParse(
                stringsHelper.instance()?.data?.config?.noti_title.toString(),
                getString(R.string.noti_title)
            )
            it.screenText.text = notificationTitle
            it.titleText.visibility = View.VISIBLE
            it.backLayout.visibility = View.VISIBLE
            it.clNotification.visibility = View.GONE
            it.searchIcon.visibility = View.GONE
            it.backLayout.setOnClickListener { onBackPressed() }
        }
    }

    private fun showNoNetwork() {
        binding?.noConnectionLayout?.visibility = View.VISIBLE
        binding?.noConnectionLayout?.findViewById<View>(R.id.retry_txt)
            ?.setOnClickListener { getNotifications() }
    }

    private fun initNoData(hasData: Boolean) {
        if (hasData) {
            binding?.grpNoData?.visibility = View.GONE
            binding?.rvNotification?.visibility = View.VISIBLE
            binding?.toolbar?.clDelete?.visibility = View.VISIBLE
        } else {
            binding?.grpNoData?.visibility = View.VISIBLE
            binding?.rvNotification?.visibility = View.GONE
            binding?.toolbar?.clDelete?.visibility = View.GONE
        }
    }

    private fun initRecyclerView(list: List<InboxMessage>) {
        notificationAdapter?.setupData(list)
        notificationAdapter?.listener = itemClickListener

        binding?.rvNotification?.let { rv ->
            val linearLayoutManager = LinearLayoutManager(rv.context)
            rv.layoutManager = linearLayoutManager
            val dividerItemDecoration = DividerItemDecoration(
                rv.context,
                linearLayoutManager.orientation
            )
            dividerItemDecoration.setDrawable(
                ColorDrawable(ContextCompat.getColor(rv.context, R.color.order_history_title_color))
            )
            rv.addItemDecoration(dividerItemDecoration)
            rv.adapter = notificationAdapter
        }
    }

    private fun showAlertDialog(
        message: String,
        positiveLabel: String,
        positiveAction: () -> Unit,
        negativeLabel: String,
    ) {
        val alertDialog = AlertDialogFragment.newInstance("", message, positiveLabel, negativeLabel)
        alertDialog.setAlertDialogCallBack {
            positiveAction()
            initNoData((notificationAdapter?.itemCount ?: 0) > 0)
        }
        alertDialog.show(Objects.requireNonNull(supportFragmentManager), "fragment_notification")
    }



}