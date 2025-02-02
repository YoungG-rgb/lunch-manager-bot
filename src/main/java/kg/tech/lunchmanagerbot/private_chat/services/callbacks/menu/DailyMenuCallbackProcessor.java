package kg.tech.lunchmanagerbot.private_chat.services.callbacks.menu;

import kg.tech.lunchmanagerbot.private_chat.entities.DailyMenuEntity;
import kg.tech.lunchmanagerbot.private_chat.entities.MenuItemEntity;
import kg.tech.lunchmanagerbot.private_chat.mappers.DailyMenuMapper;
import kg.tech.lunchmanagerbot.private_chat.repositories.DailyMenuRepository;
import kg.tech.lunchmanagerbot.private_chat.repositories.MenuItemRepository;
import kg.tech.lunchmanagerbot.commons.services.CallbackProcessor;
import kg.tech.lunchmanagerbot.commons.enums.CallbackData;
import kg.tech.lunchmanagerbot.commons.models.TelegramResponse;
import kg.tech.lunchmanagerbot.support.utils.ReplyKeyboardUtils;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kg.tech.lunchmanagerbot.commons.enums.CallbackData.*;
import static kg.tech.lunchmanagerbot.commons.models.ExceptionMessageConstant.MENU_EXCEPTION;
import static kg.tech.lunchmanagerbot.support.utils.DateUtils.getDateOrToday;

@Service
@RequiredArgsConstructor
public class DailyMenuCallbackProcessor implements CallbackProcessor {
    private final DailyMenuRepository dailyMenuRepository;
    private final MenuItemRepository menuItemRepository;
    private final DailyMenuMapper dailyMenuMapper;
    private final Map<String, Function<Update, Optional<SendMessage>>> callbackRoutes = Map.of(
            MENU.name(), update -> Optional.of(getAvailableActions(update)),
            INIT_DAILY_MENU.name(), update -> Optional.of(toggleDailyMenu(update)),
            SHOW_DAILY_MENU.name(), update -> Optional.of(showDailyMenu(update)),
            CONFIRM_MENU.name(), update -> Optional.of(confirmDailyMenu(update))
    );

    @Override
    public boolean supports(String callback) {
        Stream<String> dynamicCallbacks = Stream.of("menu_item_");
        Stream<CallbackData> callbacks = Stream.of(MENU, INIT_DAILY_MENU, SHOW_DAILY_MENU, CONFIRM_MENU);
        return dynamicCallbacks.anyMatch(callback::startsWith) || callbacks.map(Enum::name).anyMatch(callbackData -> callbackData.equals(callback));
    }

    /**
     * @apiNote Метод обрабатывает динамические/заданные callback
     */
    @Override
    public Optional<TelegramResponse> onUpdate(Update update) {
        if (!update.hasCallbackQuery()) return Optional.empty();
        String callbackData = update.getCallbackQuery().getData();

        if (callbackRoutes.containsKey(callbackData))
            return callbackRoutes.get(callbackData).apply(update).flatMap(sendMessage -> Optional.of(new TelegramResponse(sendMessage)));

        return menuItemRepository.findByCallbackData(callbackData)
                .map(menuItemEntity -> processDailyMenu(update, menuItemEntity))
                .map(TelegramResponse::new);
    }

    /**
     * @apiNote Добавляет/удаляет блюдо в меню на текущий день. {@link DailyMenuEntity}
     */
    private SendMessage processDailyMenu(Update update, MenuItemEntity menuItemEntity){
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        DailyMenuEntity dailyMenu = dailyMenuRepository.findByDayAndOwnerChatIdWithItems(getDateOrToday(update), chatId)
                .orElseGet(() -> dailyMenuRepository.save( dailyMenuMapper.toEntity(update)) );

        if (!dailyMenu.getItems().remove(menuItemEntity)) {
            dailyMenu.getItems().add(menuItemEntity);
        }
        dailyMenuRepository.save(dailyMenu);
        return toggleDailyMenu(dailyMenu, update);
    }

    /**
     * @apiNote Формирует и возвращает список блюд в меню на текущий день с индикацией состояния.
     * <blockquote><pre>
     * ✅ — блюдо уже включено в меню.
     * ❌ — блюда нет в меню.
     * </pre></blockquote>
     */
    private SendMessage toggleDailyMenu(DailyMenuEntity dailyMenu, Update update) {
        List<String> actualDailyMenuItems = dailyMenu.getItems().stream().map(MenuItemEntity::getCallbackData).toList();
        Map<String, String> menuItems = menuItemRepository.findAllToggled(actualDailyMenuItems)
                .stream()
                .collect(Collectors.toMap(MenuItemEntity::getCallbackData, MenuItemEntity::getButtonText));

        menuItems.put(CONFIRM_MENU.name(), CONFIRM_MENU.getButtonText());
        return buildMessage("Настройте список блюд на сегодня.", TelegramUtils.getChatIdByUpdate(update),
                ReplyKeyboardUtils.buildKeyboard(menuItems));
    }

    private SendMessage toggleDailyMenu(Update update) {
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        DailyMenuEntity dailyMenu = dailyMenuRepository.findByDayAndOwnerChatIdWithItems(getDateOrToday(update), chatId)
                .orElseGet(() -> dailyMenuRepository.save( dailyMenuMapper.toEntity(update)) );

        return toggleDailyMenu(dailyMenu, update);
    }

    /**
     * Метод отдает стартовые кнопки
     */
    private SendMessage getAvailableActions(Update update) {
        Map<String, String> availableActions = Stream.of(INIT_DAILY_MENU, SHOW_DAILY_MENU)
                .collect(Collectors.toMap(Enum::name, CallbackData::getButtonText));

        return buildMessage("Выберите действие", TelegramUtils.getChatIdByUpdate(update),
                ReplyKeyboardUtils.buildSingleRowKeyboard(availableActions));
    }

    private SendMessage confirmDailyMenu(Update update){
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        DailyMenuEntity dailyMenu = dailyMenuRepository.findByDayAndOwnerChatIdWithItems(getDateOrToday(update), chatId)
                .orElseGet(() -> dailyMenuRepository.save( dailyMenuMapper.toEntity(update)) );

        if (dailyMenu.getItems() == null) return buildMessage(MENU_EXCEPTION, chatId);

        Map<String, List<MenuItemEntity>> dailyItems = dailyMenu.getItems().stream().collect(Collectors.groupingBy(MenuItemEntity::getCategory));
        StringJoiner messageJoiner = new StringJoiner("\n");
        messageJoiner.add("Здравствуйте \uD83C\uDF39\uD83C\uDF39\uD83C\uDF39, меню на сегодня:" + "\n");

        dailyItems.forEach((category, items) -> {
            messageJoiner.add(category + ": \n");
            items.forEach(item -> messageJoiner.add(item.getButtonText()));
        });

        dailyMenu.setFormattedMenuMessage(messageJoiner.toString());
        dailyMenuRepository.save(dailyMenu);
        return buildMessage(messageJoiner.toString(), chatId);
    }

    private SendMessage showDailyMenu(Update update) {
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        DailyMenuEntity dailyMenu = dailyMenuRepository.findByDayAndOwnerChatId(getDateOrToday(update), chatId)
                .orElseGet(() -> dailyMenuRepository.save( dailyMenuMapper.toEntity(update)) );

        return Optional.ofNullable(dailyMenu.getFormattedMenuMessage())
                .map(menuMessage -> buildMessage(menuMessage, chatId))
                .orElseGet(() -> buildMessage(MENU_EXCEPTION, chatId));
    }
}
