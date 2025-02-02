package kg.tech.lunchmanagerbot.configs.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "telegram")
public class TelegramBotProperties {
    private String botName;
    private String botToken;
}
