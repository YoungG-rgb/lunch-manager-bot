package kg.tech.lunchmanagerbot.private_chat.services.callbacks.menu;

import kg.tech.lunchmanagerbot.group_chat.entities.TelegramGroupEntity;
import kg.tech.lunchmanagerbot.group_chat.repositories.TelegramGroupRepository;
import kg.tech.lunchmanagerbot.private_chat.entities.DailyMenuEntity;
import kg.tech.lunchmanagerbot.private_chat.repositories.DailyMenuRepository;
import kg.tech.lunchmanagerbot.support.CallbackProcessor;
import kg.tech.lunchmanagerbot.support.domain.CallbackData;
import kg.tech.lunchmanagerbot.support.domain.TelegramResponse;
import kg.tech.lunchmanagerbot.support.utils.ReplyKeyboardUtils;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static kg.tech.lunchmanagerbot.support.utils.DateUtils.getDateOrToday;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuDistributionSetupCallbackProcessor implements CallbackProcessor {
    private final TelegramGroupRepository telegramGroupRepository;
    private final DailyMenuRepository dailyMenuRepository;

    @Override
    public Optional<TelegramResponse> onUpdate(Update update) {
        if (!update.hasCallbackQuery()) return Optional.empty();
        String chatId = TelegramUtils.getChatIdByUpdate(update);

        Optional<TelegramGroupEntity> telegramGroupOptional = telegramGroupRepository.findByCallbackData(update.getCallbackQuery().getData());
        if (telegramGroupOptional.isPresent()) {
            DailyMenuEntity dailyMenu = dailyMenuRepository.findByDayAndOwnerChatIdWithGroups(getDateOrToday(update), chatId).get();
            if (!dailyMenu.getTelegramGroups().remove(telegramGroupOptional.get())) {
                dailyMenu.getTelegramGroups().add(telegramGroupOptional.get());
            }
            dailyMenuRepository.save(dailyMenu);
            return Optional.of(new TelegramResponse( toggleTelegramGroups(dailyMenu, chatId) ));
        }

        return Optional.empty();
    }

    private SendMessage toggleTelegramGroups(DailyMenuEntity dailyMenu, String chatId) {
        List<String> selectedGroups = dailyMenu.getTelegramGroups().stream().map(TelegramGroupEntity::getCallbackData).toList();
        Map<String, String> allTelegramGroups = telegramGroupRepository.findAllToggled(selectedGroups).stream()
                .collect(Collectors.toMap(TelegramGroupEntity::getCallbackData, TelegramGroupEntity::getName));

        allTelegramGroups.put(CallbackData.DISTRIBUTE_DAILY_MENU.name(), CallbackData.DISTRIBUTE_DAILY_MENU.getButtonText());
        return buildMessage("Выберите группу:", chatId, ReplyKeyboardUtils.buildSingleRowKeyboard(allTelegramGroups));
    }

    @Override
    public boolean supports(String callbackData) {
        return callbackData.startsWith("lunch_group_");
    }
}
