package kg.tech.lunchmanagerbot.commons.services;

import kg.tech.lunchmanagerbot.commons.entities.NotificationTaskEntity;
import kg.tech.lunchmanagerbot.commons.enums.NotificationStatus;

public interface NotificationTaskService {
    NotificationTaskEntity save(String chatId, String message);

    NotificationTaskEntity save(NotificationTaskEntity notificationTaskEntity);

    void rollbackIfRetryAvailable(NotificationTaskEntity notificationTaskEntity, NotificationStatus status);

}
