package kg.tech.lunchmanagerbot.group_chat.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "telegram_users", indexes =
@Index(name = "idx_chat_id", columnList = "chat_id", unique = true))
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telegram_users_seq")
    @SequenceGenerator(name = "telegram_users_seq", sequenceName = "telegram_users_seq", allocationSize = 1)
    Long id;

    @Column(name = "chat_id")
    String chatId;

    String name;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    Boolean isMenuCreator;

    String callbackData;

}
