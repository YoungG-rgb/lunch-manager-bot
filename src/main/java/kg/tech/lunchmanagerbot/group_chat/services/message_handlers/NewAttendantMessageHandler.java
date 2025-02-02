package kg.tech.lunchmanagerbot.group_chat.services.message_handlers;

import kg.tech.lunchmanagerbot.group_chat.services.duty.AttendantUserService;
import kg.tech.lunchmanagerbot.commons.services.BaseMessageHandler;
import kg.tech.lunchmanagerbot.support.annotations.TypedMessageHandler;
import kg.tech.lunchmanagerbot.commons.enums.MessageHandlerType;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@TypedMessageHandler(MessageHandlerType.NEW_ATTENDANT)
public class NewAttendantMessageHandler extends BaseMessageHandler {
    private final AttendantUserService attendantUserService;

    @Override
    public SendMessage handle(Update update) {
        String chatId = TelegramUtils.getChatIdByUpdate(update);
        List<User> newChatMembers = update.getMessage().getNewChatMembers();
        Optional.ofNullable(newChatMembers).ifPresent(members -> members.forEach(member -> attendantUserService.saveIfAbsent(member, chatId)));
        return null;
    }

    @Override
    public boolean supports(Update update) {
        List<User> newChatMembers = update.getMessage().getNewChatMembers();
        return newChatMembers != null && !newChatMembers.isEmpty();
    }

}
