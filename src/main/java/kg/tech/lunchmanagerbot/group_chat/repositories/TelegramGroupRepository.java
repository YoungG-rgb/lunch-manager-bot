package kg.tech.lunchmanagerbot.group_chat.repositories;

import kg.tech.lunchmanagerbot.group_chat.entities.TelegramGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TelegramGroupRepository extends JpaRepository<TelegramGroupEntity, Long> {
    Optional<TelegramGroupEntity> findByChatIdAndIsActiveTrue(String chatId);

    @Query("""
    select new kg.tech.lunchmanagerbot.group_chat.entities.TelegramGroupEntity(
    tg.callbackData, case when tg.callbackData in :selectedGroups then concat(tg.name, ' ✅') else concat(tg.name, ' ❌') end
    ) from TelegramGroupEntity tg where tg.isActive = true
    """)
    List<TelegramGroupEntity> findAllToggled(@Param("selectedGroups") List<String> selectedGroups);

    Optional<TelegramGroupEntity> findByCallbackData(String callbackData);

    @Query("""
    select new kg.tech.lunchmanagerbot.group_chat.entities.TelegramGroupEntity(
    tg.callbackData,
    case
        when tg.callbackData in (
            select tg_inner.callbackData from DailyMenuEntity d join d.telegramGroups tg_inner
            where d.day = :day and d.ownerChatId = :chatId
        )
        then concat(tg.name, ' ✅')
        else concat(tg.name, ' ❌')
    end
    ) from TelegramGroupEntity tg where tg.isActive = true
    """)
    List<TelegramGroupEntity> findAllToggled(@Param("day") LocalDate day, @Param("chatId") String ownerChatId);

}
