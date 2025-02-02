package kg.tech.lunchmanagerbot.commons.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

public abstract class BaseMessageHandler {
    public abstract SendMessage handle(Update update);
    public abstract boolean supports(Update update);

    protected SendMessage buildMessage(String text, String chatId) {
        return SendMessage.builder().chatId(chatId).text(text).build();
    }

    protected SendMessage buildMessage(String text, String chatId, ReplyKeyboard keyboardMarkup) {
        SendMessage sendMessage = buildMessage(text, chatId);
        sendMessage.setReplyMarkup(keyboardMarkup);
        return sendMessage;
    }
}
