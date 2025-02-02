package kg.tech.lunchmanagerbot.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CallbackData {
    MENU("\uD83D\uDCC5 Меню","Для владельца бизнеса"),
    INIT_DAILY_MENU("\uD83D\uDCC5 Создать меню на сегодня", "Для создания ежедневного меню"),
    DISTRIBUTE_DAILY_MENU("\uD83D\uDCE4 Начать рассылку", "Для отправки ежедневного меню"),
    SHOW_DAILY_MENU("\uD83D\uDD0D Показать меню на сегодня", "Для просмотра ежедневного меню"),
    CONFIRM_MENU("\uD83D\uDD04 Сохранить меню", "Для сохранения ежедневного меню"),

    MENU_ITEMS("\uD83C\uDF7D Позиции меню", "Для владельца бизнеса"),
    ;

    private final String buttonText;
    private final String description;

}
