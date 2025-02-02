package kg.tech.lunchmanagerbot.private_chat.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "menu_items", indexes = @Index(name = "idx_callback_data", columnList = "callback_data"))
public class MenuItemEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_items_seq")
    @SequenceGenerator(name = "menu_items_seq", sequenceName = "menu_items_seq", allocationSize = 1)
    Long id;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    Boolean isActive;

    /**
     * Категория/тип
     */
    String category;

    String buttonText;

    @Column(name = "callback_data", length = 64)
    String callbackData;

    public MenuItemEntity(String callbackData, String buttonText) {
        this.callbackData = callbackData;
        this.buttonText = buttonText;
    }
}
