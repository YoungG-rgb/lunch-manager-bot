package kg.tech.lunchmanagerbot.group_chat.services.message_handlers;

import kg.tech.lunchmanagerbot.commons.enums.MessageHandlerType;
import kg.tech.lunchmanagerbot.commons.services.BaseMessageHandler;
import kg.tech.lunchmanagerbot.group_chat.entities.TelegramUserEntity;
import kg.tech.lunchmanagerbot.group_chat.repositories.TelegramUserRepository;
import kg.tech.lunchmanagerbot.support.annotations.TypedMessageHandler;
import kg.tech.lunchmanagerbot.support.utils.ReplyKeyboardUtils;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@TypedMessageHandler(MessageHandlerType.ORDER)
public class OrderMessageHandler extends BaseMessageHandler {
    private final TelegramUserRepository telegramUserRepository;

    @Override
    public SendMessage handle(Update update) {
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        Map<String, String> allMenuCreators = telegramUserRepository.findAllByIsMenuCreatorTrue( )
                .stream().collect(Collectors.toMap(TelegramUserEntity::getCallbackData, TelegramUserEntity::getName));

        return buildMessage("Выберите меню-овнера", chatId, ReplyKeyboardUtils.buildSingleRowKeyboard(allMenuCreators));
    }

    @Override
    public boolean supports(Update update) {
        return update.getMessage().getText().equalsIgnoreCase(MessageHandlerType.ORDER.getCommand());
    }
}
