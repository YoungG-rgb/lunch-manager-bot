package kg.tech.lunchmanagerbot.group_chat.services.callbacks;

import kg.tech.lunchmanagerbot.commons.models.OrderPlacedEvent;
import kg.tech.lunchmanagerbot.commons.models.TelegramResponse;
import kg.tech.lunchmanagerbot.commons.services.CallbackProcessor;
import kg.tech.lunchmanagerbot.group_chat.mappers.OrderMapper;
import kg.tech.lunchmanagerbot.group_chat.repositories.OrderRepository;
import kg.tech.lunchmanagerbot.private_chat.entities.DailyMenuEntity;
import kg.tech.lunchmanagerbot.private_chat.entities.MenuItemEntity;
import kg.tech.lunchmanagerbot.private_chat.repositories.DailyMenuRepository;
import kg.tech.lunchmanagerbot.private_chat.repositories.MenuItemRepository;
import kg.tech.lunchmanagerbot.support.utils.ReplyKeyboardUtils;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kg.tech.lunchmanagerbot.commons.enums.CallbackData.ORDER_ITEM_PREFIX;
import static kg.tech.lunchmanagerbot.commons.enums.CallbackData.OWNER_PREFIX;
import static kg.tech.lunchmanagerbot.commons.models.ExceptionMessageConstant.MENU_EXCEPTION;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCallbackProcessor implements CallbackProcessor {
    private final Map<String, Function<Update, Optional<TelegramResponse>>> callbackRoutes = Map.of(
            OWNER_PREFIX.getCallback(), update -> Optional.of(getDailyMenuItems(update)),
            ORDER_ITEM_PREFIX.getCallback(), update -> Optional.of(orderDailyMenuItem(update))
    );

    private final DailyMenuRepository dailyMenuRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public Optional<TelegramResponse> onUpdate(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        for (Map.Entry<String, Function<Update, Optional<TelegramResponse>>> functionEntry : callbackRoutes.entrySet()) {
            if (callbackData.startsWith(functionEntry.getKey())) return functionEntry.getValue().apply(update);
        }

        return Optional.empty( );
    }

    @Override
    public boolean supports(String callbackData) {
        Stream<String> dynamicCallbacks = Stream.of(ORDER_ITEM_PREFIX.getCallback(), OWNER_PREFIX.getCallback());
        return dynamicCallbacks.anyMatch(callbackData::startsWith);
    }

    private TelegramResponse orderDailyMenuItem(Update update) {
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        User fromUser = update.getCallbackQuery().getFrom();
        String menuItemName = menuItemRepository.getNameByOrderCallbackData(update.getCallbackQuery().getData());
        TelegramResponse telegramResponse = new TelegramResponse();

        eventPublisher.publishEvent(new OrderPlacedEvent(this, chatId, fromUser.getUserName()));
        if (orderRepository.existsBy(LocalDate.now(), fromUser.getId(), menuItemName)) {
            orderRepository.updateAmount(LocalDate.now(), fromUser.getId(), menuItemName);
            return telegramResponse.withMessage(buildMessage(menuItemName + " принят", chatId));
        }

        orderRepository.save( orderMapper.toNewEntity(fromUser, menuItemName, chatId) );
        return telegramResponse.withMessage(buildMessage(menuItemName + " принят", chatId));
    }

    private TelegramResponse getDailyMenuItems(Update update) {
        TelegramResponse telegramResponse = new TelegramResponse();
        String chatId = TelegramUtils.getChatIdByUpdate(update);

        String ownerChatId = update.getCallbackQuery().getData().substring(OWNER_PREFIX.getCallback().length());
        Optional<DailyMenuEntity> dailyMenuEntity = dailyMenuRepository.findByDayAndOwnerChatIdWithItems(LocalDate.now( ), ownerChatId);
        if (dailyMenuEntity.isEmpty()) return telegramResponse.withMessage( buildMessage(MENU_EXCEPTION, chatId) );

        Map<String, String> dailyMenuItems = dailyMenuEntity.get().getItems().stream().collect(Collectors.toMap(MenuItemEntity::getOrderCallbackData, MenuItemEntity::getButtonText));
        return telegramResponse.withMessage( buildMessage("Выберите блюдо:", chatId, ReplyKeyboardUtils.buildKeyboard(dailyMenuItems)) );
    }

}
