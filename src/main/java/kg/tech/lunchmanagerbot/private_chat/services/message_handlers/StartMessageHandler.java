package kg.tech.lunchmanagerbot.private_chat.services.message_handlers;

import kg.tech.lunchmanagerbot.commons.services.BaseMessageHandler;
import kg.tech.lunchmanagerbot.support.annotations.TypedMessageHandler;
import kg.tech.lunchmanagerbot.commons.enums.MessageHandlerType;
import kg.tech.lunchmanagerbot.support.utils.ReplyKeyboardUtils;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import kg.tech.lunchmanagerbot.commons.enums.CallbackData;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kg.tech.lunchmanagerbot.commons.enums.CallbackData.MENU;
import static kg.tech.lunchmanagerbot.commons.enums.CallbackData.MENU_ITEMS;

@Slf4j
@Service
@RequiredArgsConstructor
@TypedMessageHandler(MessageHandlerType.START)
public class StartMessageHandler extends BaseMessageHandler {
    private static final Map<String, String> START_ACTIONS = Stream.of(MENU, MENU_ITEMS).collect(Collectors.toMap(Enum::name, CallbackData::getButtonText));

    @Override
    public SendMessage handle(Update update) {
        return SendMessage.builder().text("Добро пожаловать!")
                .chatId(TelegramUtils.getChatIdByUpdate(update))
                .replyMarkup(ReplyKeyboardUtils.buildKeyboard(START_ACTIONS))
                .build();
    }

    @Override
    public boolean supports(Update update) {
        return update.getMessage().getText().equalsIgnoreCase(MessageHandlerType.START.getCommand());
    }

}
