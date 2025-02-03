package kg.tech.lunchmanagerbot.commons.services;

import kg.tech.lunchmanagerbot.commons.models.TelegramResponse;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
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

    default Optional<TelegramResponse> onUpdateReceived(Update update) {
        var result = this.onUpdate(update);
        result.ifPresent(telegramResponse -> {
            if (update.hasCallbackQuery()) {
                AnswerCallbackQuery acceptCallback = new AnswerCallbackQuery();
                acceptCallback.setShowAlert(false);
                acceptCallback.setCallbackQueryId(String.valueOf(update.getCallbackQuery().getId()));
                acceptCallback.setCacheTime(1000);
                telegramResponse.setAction(acceptCallback);
            }
        });
        return result;
    }

    default SendMessage buildMessage(String text, String chatId, ReplyKeyboard keyboardMarkup) {
        SendMessage sendMessage = buildMessage(text, chatId);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }
}
