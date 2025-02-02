package kg.tech.lunchmanagerbot.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CallbackData {
    MENU("\uD83D\uDCC5 Меню", "MENU","Для владельца бизнеса"),
    INIT_DAILY_MENU("\uD83D\uDCC5 Создать меню на сегодня","INIT_DAILY_MENU", "Для создания ежедневного меню"),
    CONFIRM_SEND_DAILY_MENU("\uD83D\uDCE4 Начать рассылку", "CONFIRM_SEND_DAILY_MENU","Для отправки ежедневного меню"),
    SHOW_DAILY_MENU("\uD83D\uDD0D Показать меню на сегодня", "SHOW_DAILY_MENU","Для просмотра ежедневного меню"),
    CONFIRM_MENU("\uD83D\uDD04 Сохранить меню","CONFIRM_MENU" ,"Для сохранения ежедневного меню"),
    MENU_ITEMS("\uD83C\uDF7D Позиции меню", "MENU_ITEMS","Для владельца бизнеса"),

    /**
     * Dynamics
     */
    OWNER_PREFIX("", "menu_owner_","Для выбора меню овнера"),
    ORDER_ITEM_PREFIX("", "order_item_","Для выбора блюд в группе"),
    MENU_ITEM_PREFIX("", "menu_item_","Для создания блюд в ежедневное меню")
    ;

    private final String buttonText;
    private final String callback;
    private final String description;

}
