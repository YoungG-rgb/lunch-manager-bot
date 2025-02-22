package kg.tech.lunchmanagerbot.private_chat.services.callbacks.menu_items;

import kg.tech.lunchmanagerbot.commons.services.CallbackProcessor;
import kg.tech.lunchmanagerbot.commons.models.TelegramResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static kg.tech.lunchmanagerbot.commons.enums.CallbackData.MENU_ITEMS;

@Service
@RequiredArgsConstructor
public class MenuItemCallbackProcessor implements CallbackProcessor {

    @Override
    public Optional<TelegramResponse> onUpdate(Update update) {
        return Optional.empty();
    }

    @Override
    public boolean supports(String callbackData) {
        return MENU_ITEMS.name().equals(callbackData);
    }
}
