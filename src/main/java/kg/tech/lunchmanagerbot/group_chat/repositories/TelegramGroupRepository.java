package kg.tech.lunchmanagerbot.group_chat.repositories;

import kg.tech.lunchmanagerbot.group_chat.entities.TelegramGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface TelegramGroupRepository extends JpaRepository<TelegramGroupEntity, Long> {
    Optional<TelegramGroupEntity> findByChatIdAndIsActiveTrue(String chatId);

    @Query("""
    select new kg.tech.lunchmanagerbot.group_chat.entities.TelegramGroupEntity(
    tg.callbackData, case when tg.isActive = true then concat(tg.name, ' ✅') else concat(tg.name, ' ❌') end
    ) from TelegramGroupEntity tg
    """)
    List<TelegramGroupEntity> findAllToggled();

    @Modifying
    @Transactional
    @Query("""
    update TelegramGroupEntity tg
    set tg.isActive = case when tg.isActive = true then false else true end
    where tg.callbackData = :callbackData
    """)
    void toggleActive(@Param("callbackData") String callbackData);

    @Query("select new kg.tech.lunchmanagerbot.group_chat.entities.TelegramGroupEntity(tg.callbackData, tg.name, tg.chatId) from TelegramGroupEntity tg where tg.isActive = true")
    List<TelegramGroupEntity> findAllActive();

}
