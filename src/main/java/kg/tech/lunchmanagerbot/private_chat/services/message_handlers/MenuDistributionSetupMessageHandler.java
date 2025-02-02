package kg.tech.lunchmanagerbot.private_chat.services.message_handlers;

import kg.tech.lunchmanagerbot.commons.enums.MessageHandlerType;
import kg.tech.lunchmanagerbot.commons.services.BaseMessageHandler;
import kg.tech.lunchmanagerbot.group_chat.entities.TelegramGroupEntity;
import kg.tech.lunchmanagerbot.group_chat.repositories.TelegramGroupRepository;
import kg.tech.lunchmanagerbot.support.annotations.TypedMessageHandler;
import kg.tech.lunchmanagerbot.support.utils.ReplyKeyboardUtils;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@TypedMessageHandler(MessageHandlerType.MENU_DISTRIBUTION_SETUP)
public class MenuDistributionSetupMessageHandler extends BaseMessageHandler {
    private final TelegramGroupRepository telegramGroupRepository;

    @Override
    public SendMessage handle(Update update) {
        Map<String, String> allTelegramGroups = telegramGroupRepository.findAllToggled().stream()
                .collect(Collectors.toMap(TelegramGroupEntity::getCallbackData, TelegramGroupEntity::getName));

        return buildMessage("Выберите группу:", TelegramUtils.getChatIdByUpdate(update),
                ReplyKeyboardUtils.buildSingleRowKeyboard(allTelegramGroups)
        );
    }

    @Override
    public boolean supports(Update update) {
        return update.getMessage().getText().equalsIgnoreCase(MessageHandlerType.MENU_DISTRIBUTION_SETUP.getCommand());
    }

}
