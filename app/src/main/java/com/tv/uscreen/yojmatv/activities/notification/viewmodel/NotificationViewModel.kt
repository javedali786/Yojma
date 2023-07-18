package com.tv.uscreen.yojmatv.activities.notification.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tv.uscreen.yojmatv.repository.notification.NotificationRepository

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    private val notificationRepository: NotificationRepository = NotificationRepository.getInstance()

}