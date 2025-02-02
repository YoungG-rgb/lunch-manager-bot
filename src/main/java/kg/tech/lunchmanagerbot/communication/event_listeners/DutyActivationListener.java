package kg.tech.lunchmanagerbot.communication.event_listeners;

import kg.tech.lunchmanagerbot.commons.models.OrderPlacedEvent;
import kg.tech.lunchmanagerbot.group_chat.services.duty.AttendantUserService;
import kg.tech.lunchmanagerbot.group_chat.services.duty.DutyDayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DutyActivationListener {
    private final AttendantUserService attendantUserService;
    private final DutyDayService dutyDayService;

    @Async
    @EventListener
    public void handleOrderPlaced(OrderPlacedEvent orderPlacedEvent){
        log.info("Activating attendant user: {}", orderPlacedEvent.getTelegramUsername());
        attendantUserService.activateAttendant(orderPlacedEvent.getTelegramUsername());
        dutyDayService.refreshAttendant(orderPlacedEvent.getGroupChatId());
    }

}
