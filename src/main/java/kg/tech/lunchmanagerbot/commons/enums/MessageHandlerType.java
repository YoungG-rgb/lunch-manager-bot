package kg.tech.lunchmanagerbot.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageHandlerType {
    START("/start"),
    MENU_DISTRIBUTION_SETUP("/menu_distribution_setup"),
    SEND_DAILY_MENU("/send_daily_menu"),

    NEW_ATTENDANT("/new_attendant"),
    SHOW_ATTENDANT_USER("/show_attendant_user"),
    ORDER("/order")
    ;

    private final String command;
}
