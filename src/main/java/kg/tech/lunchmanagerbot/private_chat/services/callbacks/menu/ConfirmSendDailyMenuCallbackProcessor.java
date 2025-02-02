package kg.tech.lunchmanagerbot.private_chat.services.callbacks.menu;

import kg.tech.lunchmanagerbot.commons.models.TelegramResponse;
import kg.tech.lunchmanagerbot.commons.services.CallbackProcessor;
import kg.tech.lunchmanagerbot.commons.services.NotificationTaskService;
import kg.tech.lunchmanagerbot.group_chat.repositories.TelegramGroupRepository;
import kg.tech.lunchmanagerbot.private_chat.repositories.DailyMenuRepository;
import kg.tech.lunchmanagerbot.support.utils.StringUtils;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;

import static kg.tech.lunchmanagerbot.commons.enums.CallbackData.CONFIRM_SEND_DAILY_MENU;
import static kg.tech.lunchmanagerbot.commons.models.ExceptionMessageConstant.MENU_EXCEPTION;
import static kg.tech.lunchmanagerbot.support.utils.DateUtils.getDateOrToday;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmSendDailyMenuCallbackProcessor implements CallbackProcessor {
    private final DailyMenuRepository dailyMenuRepository;
    private final TelegramGroupRepository telegramGroupRepository;
    private final NotificationTaskService notificationTaskService;

    @Override
    public Optional<TelegramResponse> onUpdate(Update update) {
        if (!update.hasCallbackQuery()) return Optional.empty();
        String callbackData = update.getCallbackQuery().getData();

        if (callbackData.equals(CONFIRM_SEND_DAILY_MENU.name())) return this.distribute(update);
        return Optional.empty();
    }

    private Optional<TelegramResponse> distribute(Update update) {
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        TelegramResponse telegramResponse = new TelegramResponse();

        String message = dailyMenuRepository.findDailyMenuMessageByDayAndOwnerChatId(getDateOrToday(update), chatId);
        if (StringUtils.isEmpty(message)) {
            telegramResponse.setSendMessages(List.of( buildMessage(MENU_EXCEPTION, chatId) ));
            return Optional.of(telegramResponse);
        }

        telegramGroupRepository.findAllActive().forEach(group -> notificationTaskService.save(group.getChatId(), message));
        telegramResponse.setSendMessages(List.of( buildMessage("Нотификации будут отправлены в течение 3секунд", chatId) ));
        return Optional.of(telegramResponse);
    }

    @Override
    public boolean supports(String callbackData) {
        return CONFIRM_SEND_DAILY_MENU.name().equalsIgnoreCase(callbackData);
    }
}

