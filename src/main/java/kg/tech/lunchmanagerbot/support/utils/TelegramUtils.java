package kg.tech.lunchmanagerbot.support.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.text.MessageFormat;

@Slf4j
@UtilityClass
public class TelegramUtils {

    public static String userToPrettyString(User user) {
        return MessageFormat.format("{0} {1} (@{2})", user.getFirstName(), user.getLastName(), user.getUserName());
    }

    public static String getChatIdByUpdate(Update update) {
        Long chatId = null;
        if (update.hasMessage()) chatId = update.getMessage().getChatId();
        if (update.hasCallbackQuery()) chatId = update.getCallbackQuery().getMessage().getChatId();

        return String.valueOf(chatId);
    }

    public static String getChatType(Update update) {
        if (update.hasMessage()) return update.getMessage().getChat().getType();
        if (update.hasCallbackQuery()) return update.getCallbackQuery().getMessage().getChat().getType();
        return "unknown_type";
    }

}
