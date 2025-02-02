package kg.tech.lunchmanagerbot.private_chat.repositories;

import kg.tech.lunchmanagerbot.private_chat.entities.MenuItemEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {

    @Cacheable("all_menu_items")
    List<MenuItemEntity> findAllByIsActiveTrue();

    @Query("""
    select new kg.tech.lunchmanagerbot.private_chat.entities.MenuItemEntity(
        m.callbackData, case
                            when m.callbackData in :actualDailyMenuItems then concat(m.buttonText, ' ✅')
                            else concat(m.buttonText, ' ❌')
                        end)
        from MenuItemEntity m
    """)
    List<MenuItemEntity> findAllToggled(@Param("actualDailyMenuItems")List<String> actualDailyMenuItems);

    Optional<MenuItemEntity> findByCallbackData(String callbackData);

}
