package kg.tech.lunchmanagerbot.support.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

import static kg.tech.lunchmanagerbot.support.domain.MessageHandlerType.*;

@Getter
@AllArgsConstructor
public enum MessageHandlerTypeGroup {
    DEFAULT(List.of(NEW_USER)),
    SERV_TEAM_LUNCH(List.of(NEW_ATTENDANT, GROUPED_ORDER, NEW_USER)),
    NURTELECOM_COMPANY(Collections.emptyList()),
    PRIVATE_CHAT(List.of(START, MENU_DISTRIBUTION_SETUP))
    ;

    private final List<MessageHandlerType> messageHandlers;

}
