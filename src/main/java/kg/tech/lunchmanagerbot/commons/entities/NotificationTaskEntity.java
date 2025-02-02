package kg.tech.lunchmanagerbot.commons.entities;

import kg.tech.lunchmanagerbot.commons.enums.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notification_tasks", indexes = @Index(name = "status_pkey", columnList = "status"))
public class NotificationTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telegram_groups_seq")
    @SequenceGenerator(name = "telegram_groups_seq", sequenceName = "telegram_groups_seq", allocationSize = 1)
    Long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String message;

    String chatId;

    Integer currentRetries;

    Integer maxRetries;

    LocalDateTime nextRetryTime;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    NotificationStatus status;

    @CreationTimestamp
    LocalDateTime createdAt;

}
