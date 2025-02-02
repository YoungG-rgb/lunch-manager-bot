package kg.tech.lunchmanagerbot.commons.services;

import kg.tech.lunchmanagerbot.commons.entities.NotificationTaskEntity;

public interface NotificationTaskService {
    NotificationTaskEntity save(String chatId, String message);
}
