package kg.tech.lunchmanagerbot.group_chat.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "duty_days", indexes = {
        @Index(name = "idx_day", columnList = "day", unique = true),
        @Index(name = "day_group_chat_id_pkey", columnList = "day,group_chat_id")
})
public class DutyDayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "duty_days_seq")
    @SequenceGenerator(name = "duty_days_seq", sequenceName = "duty_days_seq", allocationSize = 1)
    Long id;

    @Column(name = "day")
    LocalDate day;

    String username;

    Integer userPriority;

    @Column(name = "group_chat_id")
    String groupChatId;

}
