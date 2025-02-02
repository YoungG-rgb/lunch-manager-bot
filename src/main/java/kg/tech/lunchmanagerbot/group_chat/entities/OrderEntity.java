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
@Table(name = "orders", indexes = @Index(name = "day_user_id_pkey", columnList = "day, user_external_id"))
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_seq")
    @SequenceGenerator(name = "orders_seq", sequenceName = "orders_seq", allocationSize = 1)
    Long id;

    Integer amount;

    String menuItemName;

    String fromUsername;

    @Column(name = "user_external_id")
    Long userExternalId;

    @Column(name = "day")
    LocalDate day;

}
