package kg.tech.lunchmanagerbot.private_chat.services.message_handlers;

import kg.tech.lunchmanagerbot.group_chat.repositories.TelegramGroupRepository;
import kg.tech.lunchmanagerbot.private_chat.repositories.DailyMenuRepository;
import kg.tech.lunchmanagerbot.support.BaseMessageHandler;
import kg.tech.lunchmanagerbot.support.annotations.TypedMessageHandler;
import kg.tech.lunchmanagerbot.support.domain.CallbackData;
import kg.tech.lunchmanagerbot.support.domain.MessageHandlerType;
import kg.tech.lunchmanagerbot.support.utils.ReplyKeyboardUtils;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import kg.tech.lunchmanagerbot.group_chat.entities.TelegramGroupEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kg.tech.lunchmanagerbot.support.domain.ExceptionMessageConstant.MENU_EXCEPTION;
import static kg.tech.lunchmanagerbot.support.utils.DateUtils.getDateOrToday;

@Slf4j
@Service
@RequiredArgsConstructor
@TypedMessageHandler(MessageHandlerType.MENU_DISTRIBUTION_SETUP)
public class MenuDistributionSetupMessageHandler extends BaseMessageHandler {
    private final TelegramGroupRepository telegramGroupRepository;
    private final DailyMenuRepository dailyMenuRepository;

    @Override
    public SendMessage handle(Update update) {
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        if (!dailyMenuRepository.existsByDayAndOwnerChatId(getDateOrToday(update), chatId)) {
            return buildMessage(MENU_EXCEPTION, chatId);
        }

        return buildMessage("Выберите группу:", chatId, getCustomKeyboard());
    }

    private ReplyKeyboard getCustomKeyboard() {
        Map<String, String> allTelegramGroups = telegramGroupRepository.findAllToggled(List.of("")).stream()
                .collect(Collectors.toMap(TelegramGroupEntity::getCallbackData, TelegramGroupEntity::getName));

        allTelegramGroups.put(CallbackData.DISTRIBUTE_DAILY_MENU.name(), CallbackData.DISTRIBUTE_DAILY_MENU.getButtonText( ));
        return ReplyKeyboardUtils.buildKeyboard(allTelegramGroups);
    }

    @Override
    public boolean supports(Update update) {
        return update.getMessage().getText().equalsIgnoreCase(MessageHandlerType.MENU_DISTRIBUTION_SETUP.getCommand());
    }

}
