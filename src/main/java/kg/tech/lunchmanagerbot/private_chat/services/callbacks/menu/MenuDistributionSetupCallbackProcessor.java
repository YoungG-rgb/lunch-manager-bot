package kg.tech.lunchmanagerbot.private_chat.services.callbacks.menu;

import kg.tech.lunchmanagerbot.commons.models.TelegramResponse;
import kg.tech.lunchmanagerbot.commons.services.CallbackProcessor;
import kg.tech.lunchmanagerbot.group_chat.entities.TelegramGroupEntity;
import kg.tech.lunchmanagerbot.group_chat.repositories.TelegramGroupRepository;
import kg.tech.lunchmanagerbot.support.utils.ReplyKeyboardUtils;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuDistributionSetupCallbackProcessor implements CallbackProcessor {
    private final TelegramGroupRepository telegramGroupRepository;

    @Override
    public Optional<TelegramResponse> onUpdate(Update update) {
        if (!update.hasCallbackQuery()) return Optional.empty();
        telegramGroupRepository.toggleActive(update.getCallbackQuery().getData());
        Map<String, String> telegramGroups = telegramGroupRepository.findAllToggled( ).stream( )
                .collect(Collectors.toMap(TelegramGroupEntity::getCallbackData, TelegramGroupEntity::getName));

        SendMessage sendMessage = buildMessage("Выберите группу:", TelegramUtils.getChatIdByUpdate(update),
                ReplyKeyboardUtils.buildSingleRowKeyboard(telegramGroups));
        return Optional.of(new TelegramResponse( sendMessage ));
    }

    @Override
    public boolean supports(String callbackData) {
        return callbackData.startsWith("lunch_group_");
    }
}
