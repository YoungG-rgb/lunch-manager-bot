package kg.tech.lunchmanagerbot.group_chat.services.message_handlers;

import kg.tech.lunchmanagerbot.commons.enums.MessageHandlerType;
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

@Slf4j
@Service
@RequiredArgsConstructor
@TypedMessageHandler(MessageHandlerType.SHOW_ORDER)
public class ShowOrderMessageHandler extends BaseMessageHandler {
    private final OrderRepository orderRepository;

    @Override
    public SendMessage handle(Update update) {
        User fromUser = update.getMessage().getFrom();
        List<OrderEntity> userOrders = orderRepository.findByDayAndUserExternalId(LocalDate.now(), fromUser.getId());
        StringJoiner orderJoiner = new StringJoiner("\n");
        orderJoiner.add("Ваш заказ на сегодня:");
        userOrders.forEach(order -> orderJoiner.add(order.getMenuItemName() + " - " + order.getAmount()));

        return buildMessage(orderJoiner.toString(), TelegramUtils.getChatIdByUpdate(update));
    }

    @Override
    public boolean supports(Update update) {
        return update.getMessage().getText().equalsIgnoreCase(MessageHandlerType.SHOW_ORDER.getCommand());
    }
}
