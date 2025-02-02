package kg.tech.lunchmanagerbot.commons.models;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderPlacedEvent extends ApplicationEvent {
    private final String telegramUsername;

    public OrderPlacedEvent(Object source, String telegramUsername) {
        super(source);
        this.telegramUsername = telegramUsername;
    }
}
