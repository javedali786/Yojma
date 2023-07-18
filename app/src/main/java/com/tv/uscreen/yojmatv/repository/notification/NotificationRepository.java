package com.tv.uscreen.yojmatv.repository.notification;

public class NotificationRepository {
    private static NotificationRepository instance;

    public synchronized static NotificationRepository getInstance() {
        if (instance == null) {
            instance = new NotificationRepository();
        }
        return instance;
    }

}
