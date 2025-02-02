package kg.tech.lunchmanagerbot.commons.services.impl;

import kg.tech.lunchmanagerbot.commons.entities.NotificationTaskEntity;
import kg.tech.lunchmanagerbot.commons.mappers.NotificationTaskEntityMapper;
import kg.tech.lunchmanagerbot.commons.repositories.NotificationTaskRepository;
import kg.tech.lunchmanagerbot.commons.services.NotificationTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationTaskServiceImpl implements NotificationTaskService {
    private final NotificationTaskRepository notificationTaskRepository;
    private final NotificationTaskEntityMapper notificationTaskMapper;

    @Override
    public NotificationTaskEntity save(String chatId, String message) {
        NotificationTaskEntity notificationTaskEntity = notificationTaskMapper.init(chatId, message);
        return notificationTaskRepository.save(notificationTaskEntity);
    }
}
