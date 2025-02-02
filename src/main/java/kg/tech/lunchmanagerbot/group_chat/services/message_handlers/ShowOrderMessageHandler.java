package kg.tech.lunchmanagerbot.group_chat.services.message_handlers;

import kg.tech.lunchmanagerbot.commons.services.BaseMessageHandler;
import kg.tech.lunchmanagerbot.group_chat.entities.OrderEntity;
import kg.tech.lunchmanagerbot.group_chat.repositories.OrderRepository;
import kg.tech.lunchmanagerbot.support.annotations.TypedMessageHandler;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDate;
import java.util.List;
import java.util.StringJoiner;

import static kg.tech.lunchmanagerbot.commons.enums.MessageHandlerType.SHOW_ORDER;

@Slf4j
@Service
@RequiredArgsConstructor
@TypedMessageHandler(SHOW_ORDER)
public class ShowOrderMessageHandler extends BaseMessageHandler {
    private final OrderRepository orderRepository;

    @Override
    public SendMessage handle(Update update) {
        if (update.getMessage().getText().equals(SHOW_ORDER.getCommand())) return showMyOrder(update);
        return showAllOrder(update);
    }

    private SendMessage showMyOrder(Update update) {
        User fromUser = update.getMessage().getFrom();
        List<OrderEntity> userOrders = orderRepository.findByDayAndUserExternalId(LocalDate.now(), fromUser.getId());
        StringJoiner orderJoiner = new StringJoiner("\n");
        orderJoiner.add("Ваш заказ на сегодня:");
        userOrders.forEach(order -> orderJoiner.add(order.getMenuItemName() + " - " + order.getAmount()));

        return buildMessage(orderJoiner.toString(), TelegramUtils.getChatIdByUpdate(update));
    }

    private SendMessage showAllOrder(Update update) {
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        List<OrderEntity> groupedOrdersBy = orderRepository.findGroupedOrdersBy(LocalDate.now( ), chatId);

        StringJoiner orderJoiner = new StringJoiner("\n");
        orderJoiner.add("Заказ от " + update.getMessage().getChat().getTitle());
        groupedOrdersBy.forEach(order -> orderJoiner.add(order.getMenuItemName() + " - " + order.getAmount()));

        return buildMessage(orderJoiner.toString(), chatId);
    }

    @Override
    public boolean supports(Update update) {
        List<String> callbacks = List.of( SHOW_ORDER.getCommand(), "/show_order_all");
        return callbacks.stream().anyMatch(callback -> callback.equals(update.getMessage().getText()));
    }
}
