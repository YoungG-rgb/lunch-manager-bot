package kg.tech.lunchmanagerbot.configs;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BeanQualifiers {
    public static final String NOTIFICATION_PROCESSING_EXECUTOR = "notificationProcessingExecutor";
    public static final String NOTIFICATION_SCHEDULER_EXECUTOR = "notificationSchedulerExecutor";
    public static final String ATTENDANTS_DEACTIVATION_SCHEDULER_EXECUTOR = "attendantsDeactivationSchedulerExecutor";
}
