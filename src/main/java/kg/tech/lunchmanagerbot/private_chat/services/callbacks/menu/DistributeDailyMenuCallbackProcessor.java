package kg.tech.lunchmanagerbot.private_chat.services.callbacks.menu;

import kg.tech.lunchmanagerbot.private_chat.repositories.DailyMenuRepository;
import kg.tech.lunchmanagerbot.commons.services.CallbackProcessor;
import kg.tech.lunchmanagerbot.commons.models.TelegramResponse;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static kg.tech.lunchmanagerbot.commons.enums.CallbackData.DISTRIBUTE_DAILY_MENU;
import static kg.tech.lunchmanagerbot.commons.models.ExceptionMessageConstant.MENU_EXCEPTION;
import static kg.tech.lunchmanagerbot.support.utils.DateUtils.getDateOrToday;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistributeDailyMenuCallbackProcessor implements CallbackProcessor {
    private final DailyMenuRepository dailyMenuRepository;

    @Override
    public Optional<TelegramResponse> onUpdate(Update update) {
        if (!update.hasCallbackQuery()) return Optional.empty();
        String callbackData = update.getCallbackQuery().getData();

        if (callbackData.equals(DISTRIBUTE_DAILY_MENU.name())) return this.distribute(update);
        return Optional.empty();
    }

    // TODO переделать на отправку жобом
    private Optional<TelegramResponse> distribute(Update update) {
        TelegramResponse telegramResponse = new TelegramResponse();
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        LocalDate day = getDateOrToday(update);

        dailyMenuRepository.findByDayAndOwnerChatIdWithGroups(day, chatId).ifPresent(dailyMenu -> {
            if (dailyMenu.getFormattedMenuMessage() == null) {
                telegramResponse.setSendMessages(List.of( buildMessage(MENU_EXCEPTION, chatId)) );
            } else {
                telegramResponse.setSendMessages(dailyMenu.getTelegramGroups().stream()
                        .map(group -> buildMessage(dailyMenu.getFormattedMenuMessage(), group.getChatId())).toList()
                );
            }
        });

        return Optional.of(telegramResponse);
    }

    @Override
    public boolean supports(String callbackData) {
        return DISTRIBUTE_DAILY_MENU.name().equalsIgnoreCase(callbackData);
    }
}

