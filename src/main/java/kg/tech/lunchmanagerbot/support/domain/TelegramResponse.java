package kg.tech.lunchmanagerbot.support.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramResponse {
    List<SendMessage> sendMessages;
    SendSticker sendSticker;

    public TelegramResponse(SendMessage sendMessages) {
        this.sendMessages = List.of( sendMessages );
    }

    public boolean messagesEmpty() {
        return sendMessages != null && sendMessages.isEmpty();
    }
}
