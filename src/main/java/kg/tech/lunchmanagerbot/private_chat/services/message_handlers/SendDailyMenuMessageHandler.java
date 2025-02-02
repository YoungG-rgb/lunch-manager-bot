package kg.tech.lunchmanagerbot.private_chat.services.message_handlers;

import kg.tech.lunchmanagerbot.commons.services.BaseMessageHandler;
import kg.tech.lunchmanagerbot.group_chat.repositories.TelegramGroupRepository;
import kg.tech.lunchmanagerbot.support.annotations.TypedMessageHandler;
import kg.tech.lunchmanagerbot.support.utils.ReplyKeyboardUtils;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Map;
import java.util.StringJoiner;

import static kg.tech.lunchmanagerbot.commons.enums.CallbackData.CONFIRM_SEND_DAILY_MENU;
import static kg.tech.lunchmanagerbot.commons.enums.MessageHandlerType.SEND_DAILY_MENU;

@Slf4j
@Service
@RequiredArgsConstructor
@TypedMessageHandler(SEND_DAILY_MENU)
public class SendDailyMenuMessageHandler extends BaseMessageHandler {
    private final TelegramGroupRepository telegramGroupRepository;

    @Override
    public SendMessage handle(Update update) {
        StringJoiner messageJoiner = new StringJoiner("\n");
        messageJoiner.add("Отправить ежедневное меню следующим группам?");
        telegramGroupRepository.findAllActive().forEach(group -> messageJoiner.add("\uD83D\uDCCD " + group.getName()));

        InlineKeyboardMarkup inlineKeyboardMarkup = ReplyKeyboardUtils.buildSingleRowKeyboard(Map.of(CONFIRM_SEND_DAILY_MENU.name(), CONFIRM_SEND_DAILY_MENU.getButtonText()));
        return buildMessage(messageJoiner.toString(), TelegramUtils.getChatIdByUpdate(update), inlineKeyboardMarkup);
    }

    @Override
    public boolean supports(Update update) {
        return update.getMessage().getText().equalsIgnoreCase(SEND_DAILY_MENU.getCommand());
    }
}
