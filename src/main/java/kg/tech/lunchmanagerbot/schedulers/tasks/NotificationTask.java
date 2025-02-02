package kg.tech.lunchmanagerbot.schedulers.tasks;

import kg.tech.lunchmanagerbot.commons.entities.NotificationTaskEntity;
import kg.tech.lunchmanagerbot.commons.enums.NotificationStatus;
import kg.tech.lunchmanagerbot.commons.services.NotificationTaskService;
import kg.tech.lunchmanagerbot.communication.TelegramBotClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Slf4j
@Component
@Scope(SCOPE_PROTOTYPE)
public class NotificationTask implements Runnable {
    private final NotificationTaskEntity notificationTaskEntity;
    @Autowired private TelegramBotClient telegramBotClient;
    @Autowired private NotificationTaskService notificationTaskService;

    public NotificationTask(NotificationTaskEntity notificationTaskEntity) {
        this.notificationTaskEntity = notificationTaskEntity;
    }

    @Override
    public void run() {
        try {
            telegramBotClient.sendMessage(notificationTaskEntity.getMessage(), notificationTaskEntity.getChatId());
            notificationTaskEntity.setStatus(NotificationStatus.SUCCESS);
            notificationTaskService.save(notificationTaskEntity);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);

            notificationTaskEntity.setCurrentRetries( notificationTaskEntity.getCurrentRetries() + 1);
            notificationTaskService.rollbackIfRetryAvailable(notificationTaskEntity, NotificationStatus.NEW);
        }
    }
}
