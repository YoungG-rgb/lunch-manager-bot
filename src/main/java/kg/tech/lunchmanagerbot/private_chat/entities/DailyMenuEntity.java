package kg.tech.lunchmanagerbot.private_chat.entities;

import kg.tech.lunchmanagerbot.group_chat.entities.TelegramGroupEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность, представляющая дневное меню, которое содержит набор блюд на определенный день.
 * @author Zhalaldin Toichubaev
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "daily_menus", indexes = @Index(name = "day_chat_id_pkey", columnList = "day,owner_chat_id"))
public class DailyMenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "daily_menus_seq")
    @SequenceGenerator(name = "daily_menus_seq", sequenceName = "daily_menus_seq", allocationSize = 1)
    Long id;

    @Column(name = "day")
    LocalDate day;

    @ManyToMany(fetch = FetchType.LAZY)
    Set<MenuItemEntity> items = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    Set<TelegramGroupEntity> telegramGroups = new HashSet<>();

    @Column(name = "owner_chat_id")
    String ownerChatId;

    @Column(length = 4000)
    String formattedMenuMessage;

}
