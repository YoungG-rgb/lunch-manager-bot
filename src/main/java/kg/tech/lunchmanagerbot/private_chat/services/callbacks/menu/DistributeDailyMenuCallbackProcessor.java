package kg.tech.lunchmanagerbot.private_chat.services.callbacks.menu;

import kg.tech.lunchmanagerbot.support.CallbackProcessor;
import kg.tech.lunchmanagerbot.support.domain.TelegramResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

import static kg.tech.lunchmanagerbot.support.domain.CallbackData.DISTRIBUTE_DAILY_MENU;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributeDailyMenuCallbackProcessor implements CallbackProcessor {

    @Override
    public Optional<TelegramResponse> onUpdate(Update update) {
        if (!update.hasCallbackQuery()) return Optional.empty();
        String callbackData = update.getCallbackQuery().getData();

        if (callbackData.equals(DISTRIBUTE_DAILY_MENU.name())) return this.distribute(update);
        return Optional.empty();
    }

    private Optional<TelegramResponse> distribute(Update update) {
        return Optional.empty();
    }

    @Override
    public boolean supports(String callbackData) {
        return DISTRIBUTE_DAILY_MENU.name().equalsIgnoreCase(callbackData);
    }
}

