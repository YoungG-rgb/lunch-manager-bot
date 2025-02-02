package kg.tech.lunchmanagerbot.commons.services;

import kg.tech.lunchmanagerbot.commons.models.TelegramResponse;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.Optional;

public interface CallbackProcessor {
    Optional<TelegramResponse> onUpdate(Update update);
    boolean supports(String callbackData);

    default SendMessage buildMessage(String text, String chatId) {
        return SendMessage.builder().chatId(chatId).text(text).build();
    }

    default SendMessage buildMessage(String text, String chatId, ReplyKeyboard keyboardMarkup) {
        SendMessage sendMessage = buildMessage(text, chatId);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }
}
