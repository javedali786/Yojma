package com.breadgangtvnetwork.utils.TrackerUtil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.breadgangtvnetwork.OttApplication
import com.moengage.inbox.core.MoEInboxHelper
import com.moengage.inbox.core.model.InboxData
import com.moengage.inbox.core.model.InboxMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

object MoEngageNotificationManager {
    private val mUnreadNotificationCountLiveData: MutableLiveData<Int> = MutableLiveData()
    private val mNotificationsLiveData: MutableLiveData<InboxData> = MutableLiveData()


    fun getUnreadCount(): LiveData<Int> {
        CoroutineScope(Dispatchers.IO).launch {
            getUnreadNotificationCount(this.coroutineContext)
        }
        return mUnreadNotificationCountLiveData
    }

    private suspend fun getUnreadNotificationCount(coroutineContext: CoroutineContext) =
        withContext(coroutineContext) {
            val inboxData = MoEInboxHelper.getInstance()
                .fetchAllMessages(OttApplication.getInstance())
            mUnreadNotificationCountLiveData.postValue(inboxData?.inboxMessages?.size)
        }

    private fun refreshNotificationCount() {
        getUnreadCount()
    }

    fun getAllNotifications(): LiveData<InboxData> {
        CoroutineScope(Dispatchers.IO).launch {
            getAllNotificationList(this.coroutineContext)
            getUnreadNotificationCount(this.coroutineContext)
        }
        return mNotificationsLiveData
    }

    private suspend fun getAllNotificationList(coroutineContext: CoroutineContext) =
        withContext(coroutineContext) {
            mNotificationsLiveData.postValue(
                MoEInboxHelper.getInstance().fetchAllMessages(OttApplication.getInstance())
            )

        }

    fun markAsRead(inboxMessage: InboxMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            MoEInboxHelper.getInstance().trackMessageClicked(OttApplication.getInstance(), inboxMessage)
            refreshNotificationCount()
        }
    }

    fun deleteNotification(inboxMessage: InboxMessage) {
        CoroutineScope(Dispatchers.IO).launch {
            MoEInboxHelper.getInstance().deleteMessage(OttApplication.getInstance(), inboxMessage)
            refreshNotificationCount()
        }
    }

    fun deleteAllNotifications(inboxMessages: List<InboxMessage>) {
        CoroutineScope(Dispatchers.IO).launch {
            for (message in inboxMessages) {
                MoEInboxHelper.getInstance().deleteMessage(OttApplication.getInstance(), message)
            }
            refreshNotificationCount()
        }
    }
}