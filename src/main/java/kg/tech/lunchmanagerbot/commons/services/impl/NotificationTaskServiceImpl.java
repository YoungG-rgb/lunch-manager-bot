package kg.tech.lunchmanagerbot.commons.services.impl;

import kg.tech.lunchmanagerbot.commons.entities.NotificationTaskEntity;
import kg.tech.lunchmanagerbot.commons.enums.NotificationStatus;
import kg.tech.lunchmanagerbot.commons.mappers.NotificationTaskEntityMapper;
import kg.tech.lunchmanagerbot.commons.repositories.NotificationTaskRepository;
import kg.tech.lunchmanagerbot.commons.services.NotificationTaskService;
import kg.tech.lunchmanagerbot.support.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationTaskServiceImpl implements NotificationTaskService {
    private static final String DEFAULT_RETRY_DURATION = "PT3S, PT5S";
    private final NotificationTaskRepository notificationTaskRepository;
    private final NotificationTaskEntityMapper notificationTaskMapper;

    @Override
    public NotificationTaskEntity save(String chatId, String message) {
        NotificationTaskEntity notificationTaskEntity = notificationTaskMapper.init(chatId, message);
        return notificationTaskRepository.save(notificationTaskEntity);
    }

    @Override
    public NotificationTaskEntity save(NotificationTaskEntity notificationTaskEntity) {
        return notificationTaskRepository.save(notificationTaskEntity);
    }

    @Override
    public void rollbackIfRetryAvailable(NotificationTaskEntity notificationTaskEntity, NotificationStatus status) {
        if (notificationTaskEntity.getCurrentRetries() >= notificationTaskEntity.getMaxRetries()) {
            notificationTaskEntity.setStatus(NotificationStatus.FAIL);
        } else {
            notificationTaskEntity.setNextRetryTime(calculateNextTryTime(notificationTaskEntity));
            notificationTaskEntity.setStatus(NotificationStatus.NEW);
        }

        notificationTaskRepository.save(notificationTaskEntity);
    }

    /**
     * @apiNote Вычисляет следующее время обработки таски на основе заданного временного интервала.
     */
    private LocalDateTime calculateNextTryTime(NotificationTaskEntity processingDefinition) {
        List<Duration> backoffDurations = DateUtils.parseSemicolonSeparatedDurations(DEFAULT_RETRY_DURATION);

        Duration nextDuration = backoffDurations.size() > processingDefinition.getCurrentRetries()
                ? backoffDurations.get(processingDefinition.getCurrentRetries())
                : backoffDurations.get(backoffDurations.size() - 1);

        return LocalDateTime.now().plus(nextDuration);
    }
}
