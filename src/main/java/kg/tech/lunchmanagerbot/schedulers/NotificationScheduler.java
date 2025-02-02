package kg.tech.lunchmanagerbot.schedulers;


import kg.tech.lunchmanagerbot.commons.entities.NotificationTaskEntity;
import kg.tech.lunchmanagerbot.commons.enums.NotificationStatus;
import kg.tech.lunchmanagerbot.commons.repositories.NotificationTaskRepository;
import kg.tech.lunchmanagerbot.schedulers.tasks.NotificationTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

import static kg.tech.lunchmanagerbot.configs.BeanQualifiers.NOTIFICATION_SCHEDULER_EXECUTOR;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnExpression(value = "'${schedulers.notification-processing.is-running}' == 'true'")
public class NotificationScheduler {
    private final NotificationTaskRepository notificationTaskRepository;
    private final ThreadPoolTaskExecutor notificationProcessingExecutor;
    private final ApplicationContext applicationContext;

    @Scheduled(cron = "${schedulers.notification-processing.cron}")
    @Async(NOTIFICATION_SCHEDULER_EXECUTOR)
    public void processNotifications(){
        notificationTaskRepository.findAllByStatus(NotificationStatus.NEW.name(), getFreeThreads(), LocalDateTime.now().plusSeconds(1))
                .forEach(notificationTaskEntity -> this.prepareTask(notificationTaskEntity).ifPresent(notificationProcessingExecutor::execute));
    }

    private int getFreeThreads(){
        return notificationProcessingExecutor.getMaxPoolSize() - notificationProcessingExecutor.getActiveCount();
    }

    private Optional<NotificationTask> prepareTask(NotificationTaskEntity notificationTaskEntity) {
        try {
            boolean isBlocked = notificationTaskRepository.safeUpdateStatus(notificationTaskEntity.getId(),
                    NotificationStatus.IN_PROGRESS, NotificationStatus.NEW) > 0;

            if (!isBlocked) {
                log.warn("NotificationTask {} isn`t blocked. Skipping...", notificationTaskEntity.getId());
                return Optional.empty();
            }

            notificationTaskEntity.setStatus(NotificationStatus.IN_PROGRESS);
            return Optional.of(applicationContext.getBean(NotificationTask.class, notificationTaskEntity));
        } catch (Exception exception) {
            log.error("NotificationTask {} not prepared.", notificationTaskEntity.getId(), exception);
            return Optional.empty();
        }
    }

}
