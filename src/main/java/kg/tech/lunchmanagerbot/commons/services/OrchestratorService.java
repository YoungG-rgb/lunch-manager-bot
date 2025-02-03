package kg.tech.lunchmanagerbot.commons.services;


import kg.tech.lunchmanagerbot.group_chat.repositories.TelegramGroupRepository;
import kg.tech.lunchmanagerbot.group_chat.repositories.TelegramUserRepository;
import kg.tech.lunchmanagerbot.commons.enums.MessageHandlerTypeGroup;
import kg.tech.lunchmanagerbot.commons.models.TelegramResponse;
import kg.tech.lunchmanagerbot.support.factories.CallbackProcessorFactory;
import kg.tech.lunchmanagerbot.support.factories.MessageHandlersFactory;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static kg.tech.lunchmanagerbot.support.utils.TelegramUtils.getChatIdByUpdate;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrchestratorService {
    private static final String STICKER_ID = "CAACAgIAAxkBAAMdZ5uo1dLAHYn7zoyTpwi_kHbagEIAAvxtAALKdohILk02YMPR-R82BA";
    private final TelegramGroupRepository telegramGroupRepository;
    private final TelegramUserRepository telegramUserRepository;
    private final CallbackProcessorFactory callbackProcessorFactory;
    private final MessageHandlersFactory messageHandlersFactory;

    public Optional<TelegramResponse> orchestrate(Update update) {
        String chatType = TelegramUtils.getChatType(update);

        if (update.hasMessage() || update.hasCallbackQuery()) {
            return switch (chatType) {
                case "private" -> processPrivateChat(update);
                case "supergroup" -> processSuperGroupChat(update);
                case "channel" -> {
                    log.warn("Received message from channel");
                    yield Optional.empty();
                }
                default -> {
                    log.warn("Received message from unknown chat type: {}", chatType);
                    yield Optional.empty();
                }
            };
        }

        return Optional.empty();
    }

    /**
     * @apiNote Обрабатывает события, отправленные в личном чате с ботом
     */
    private Optional<TelegramResponse> processPrivateChat(Update update) {
        String chatId = getChatIdByUpdate(update);
        if (telegramUserRepository.isMenuCreator(chatId)) {
            if (update.hasCallbackQuery()) {
                return callbackProcessorFactory.getCallbackProcessor(update.getCallbackQuery().getData())
                                .onUpdateReceived(update);
            }

            if (update.hasMessage()) return processMessageByGroup(MessageHandlerTypeGroup.PRIVATE_CHAT, update);
        }

        SendMessage sendMessage = SendMessage.builder().chatId(chatId).text("Ты кто?").build();
        SendSticker sendSticker = SendSticker.builder().chatId(chatId).sticker(new InputFile( STICKER_ID )).build();
        return Optional.of(TelegramResponse.of(List.of(sendMessage), sendSticker, null));
    }

    /**
     * @apiNote Обрабатывает события супер групп
     */
    private Optional<TelegramResponse> processSuperGroupChat(Update update) {
        if (update.hasMessage()) {
            return telegramGroupRepository.findByChatIdAndIsActiveTrue(getChatIdByUpdate(update))
                    .flatMap(tgGroup -> processMessageByGroup(tgGroup.getMessageHandlerTypeGroup(), update));
        }

        if (update.hasCallbackQuery()) {
            return callbackProcessorFactory.getCallbackProcessor(update.getCallbackQuery().getData())
                    .onUpdateReceived(update);
        }

        return Optional.empty();
    }

    /**
     * @param messageHandlerTypeGroup Группа message обработчиков
     * @param update Событие
     * @apiNote  Обрабатывает события определенной группы
     */
    private Optional<TelegramResponse> processMessageByGroup(MessageHandlerTypeGroup messageHandlerTypeGroup, Update update) {
        List<SendMessage> sendMessages = messageHandlersFactory.getAllMessageHandlersByGroup(messageHandlerTypeGroup)
                .stream()
                .filter(messageHandler -> messageHandler.supports(update))
                .map(messageHandler -> messageHandler.handle(update))
                .filter(Objects::nonNull)
                .toList();

        return Optional.of(TelegramResponse.of(sendMessages, null, null));
    }
}

