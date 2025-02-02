package kg.tech.lunchmanagerbot.group_chat.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendant_users", indexes = @Index(name = "idx_username", columnList = "username", unique = true))
public class AttendantUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendant_users_seq")
    @SequenceGenerator(name = "attendant_users_seq", sequenceName = "attendant_users_seq", allocationSize = 1)
    Long id;

    @Column(unique = true, nullable = false)
    String username;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    Boolean dutyActive;

    Integer priority;

}
