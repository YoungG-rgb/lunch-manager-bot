package kg.tech.lunchmanagerbot.schedulers;

import kg.tech.lunchmanagerbot.group_chat.services.duty.AttendantUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static kg.tech.lunchmanagerbot.configs.BeanQualifiers.ATTENDANTS_DEACTIVATION_SCHEDULER_EXECUTOR;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnExpression(value = "'${schedulers.attendants-deactivation.is-running}' == 'true'")
public class AttendantsDeactivationScheduler {
    private final AttendantUserService attendantUserService;

    @Async(ATTENDANTS_DEACTIVATION_SCHEDULER_EXECUTOR)
    @Scheduled(cron = "${schedulers.attendants-deactivation.cron}")
    public void scheduleAttendantDeactivation(){
        log.info("Attendants deactivation starting...");
        attendantUserService.deactivateAttendants();
        log.info("Attendants deactivation ending...");
    }

}
