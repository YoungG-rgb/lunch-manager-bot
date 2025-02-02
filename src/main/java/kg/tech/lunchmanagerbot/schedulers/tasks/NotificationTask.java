package kg.tech.lunchmanagerbot.schedulers.tasks;

import kg.tech.lunchmanagerbot.commons.entities.NotificationTaskEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Slf4j
@Component
@Scope(SCOPE_PROTOTYPE)
public class NotificationTask implements Runnable {
    private final NotificationTaskEntity notificationTaskEntity;

    public NotificationTask(NotificationTaskEntity notificationTaskEntity) {
        this.notificationTaskEntity = notificationTaskEntity;
    }

    @Override
    public void run() {

    }
}
