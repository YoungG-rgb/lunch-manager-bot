package kg.tech.lunchmanagerbot.communication;

import kg.tech.lunchmanagerbot.configs.props.TelegramBotProperties;
import kg.tech.lunchmanagerbot.commons.services.OrchestratorService;
import kg.tech.lunchmanagerbot.support.utils.TelegramUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBotClient extends TelegramLongPollingBot {
    private final TelegramBotProperties properties;
    private final OrchestratorService orchestratorService;

    @Override
    public String getBotUsername() {
        return properties.getBotName();
    }

    @Override
    public String getBotToken() {
        return properties.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        this.logIncoming(update);
        orchestratorService.orchestrate(update).ifPresent(telegramResponse -> {
            if (!telegramResponse.messagesEmpty()) telegramResponse.getSendMessages().forEach(this::sendMessage);
            if (telegramResponse.getSendSticker() != null) sendSticker(telegramResponse.getSendSticker());
        });
    }

    private void logIncoming(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText())
                log.info("Incoming message from chatId: {}, user: {}, text: {}", message.getChatId(), TelegramUtils.userToPrettyString(message.getFrom()), message.getText());
        }
    }

    public void sendMessage(SendMessage sendMessage) {
        try {
            execute(sendMessage);
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    public void sendSticker(SendSticker sendSticker) {
        try {
            execute(sendSticker);
        } catch (Exception exception) {
            log.error(exception.getMessage());
        }
    }
}
