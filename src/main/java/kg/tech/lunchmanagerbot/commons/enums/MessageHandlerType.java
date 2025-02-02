package kg.tech.lunchmanagerbot.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageHandlerType {
    START("/start"),
    MENU_DISTRIBUTION_SETUP("/menu_distribution_setup"),

    NEW_USER("/new_user"),
//    ORDER("/order"),
//    MENU("/menu"),
    NEW_ATTENDANT("/new_attendant"),
    GROUPED_ORDER("/grouped_order")
    ;

    private final String command;
}
