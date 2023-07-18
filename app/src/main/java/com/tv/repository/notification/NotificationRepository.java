package com.tv.repository.notification;

public class NotificationRepository {
    private static NotificationRepository instance;

    public synchronized static NotificationRepository getInstance() {
        if (instance == null) {
            instance = new NotificationRepository();
        }
        return instance;
    }

}
