package kg.tech.lunchmanagerbot.group_chat.entities;

import kg.tech.lunchmanagerbot.commons.enums.MessageHandlerTypeGroup;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "telegram_groups", indexes = @Index(name = "idx_chat_id", columnList = "chat_id", unique = true))
public class TelegramGroupEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telegram_groups_seq")
    @SequenceGenerator(name = "telegram_groups_seq", sequenceName = "telegram_groups_seq", allocationSize = 1)
    Long id;

    @Column(unique = true)
    String callbackData;

    String name;

    @Column(name = "chat_id")
    String chatId;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    Boolean isActive;

    @Enumerated(EnumType.STRING)
    MessageHandlerTypeGroup messageHandlerTypeGroup;

    public TelegramGroupEntity(String callbackData, String name) {
        this.callbackData = callbackData;
        this.name = name;
    }
}
