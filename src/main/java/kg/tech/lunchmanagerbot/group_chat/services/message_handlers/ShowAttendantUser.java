package kg.tech.lunchmanagerbot.group_chat.services.message_handlers;

import kg.tech.lunchmanagerbot.commons.enums.MessageHandlerType;
import kg.tech.lunchmanagerbot.commons.services.BaseMessageHandler;
import kg.tech.lunchmanagerbot.group_chat.entities.DutyDayEntity;
import kg.tech.lunchmanagerbot.group_chat.services.duty.DutyDayService;
import kg.tech.lunchmanagerbot.support.annotations.TypedMessageHandler;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.MessageFormat;

@Slf4j
@Service
@RequiredArgsConstructor
@TypedMessageHandler(MessageHandlerType.SHOW_ATTENDANT_USER)
public class ShowAttendantUser extends BaseMessageHandler {
    private final DutyDayService dutyDayService;

    @Override
    public SendMessage handle(Update update) {
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        DutyDayEntity todayDutyDay = dutyDayService.getTodayDutyDay(chatId);
        String message = MessageFormat.format("Внимание! \uD83D\uDEA8 @{0} сегодня держит дежурство!", todayDutyDay.getUsername( ));

        return buildMessage(message, chatId);
    }

    @Override
    public boolean supports(Update update) {
        return update.getMessage().getText().equalsIgnoreCase(MessageHandlerType.SHOW_ATTENDANT_USER.getCommand());
    }
}
