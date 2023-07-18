package com.tv.uscreen.activities.notification.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tv.uscreen.repository.notification.NotificationRepository

class NotificationViewModel(application: Application) : AndroidViewModel(application) {
    private val notificationRepository: NotificationRepository = NotificationRepository.getInstance()

}