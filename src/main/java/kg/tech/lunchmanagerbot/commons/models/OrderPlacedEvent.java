package kg.tech.lunchmanagerbot.commons.models;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderPlacedEvent extends ApplicationEvent {
    private final String telegramUsername;
    private final String groupChatId;

    public OrderPlacedEvent(Object source, String groupChatId, String telegramUsername) {
        super(source);
        this.telegramUsername = telegramUsername;
        this.groupChatId = groupChatId;
    }
}
