package kg.tech.lunchmanagerbot.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static kg.tech.lunchmanagerbot.commons.enums.MessageHandlerType.*;

@Getter
@AllArgsConstructor
public enum MessageHandlerTypeGroup {
    SERV_TEAM_LUNCH(List.of(NEW_ATTENDANT, SHOW_ATTENDANT_USER, ORDER, SHOW_ORDER)),
    NURTELECOM_COMPANY(Collections.emptyList()),
    PRIVATE_CHAT(List.of(START, MENU_DISTRIBUTION_SETUP, SEND_DAILY_MENU))
    ;

    private final List<MessageHandlerType> messageHandlers;

}
