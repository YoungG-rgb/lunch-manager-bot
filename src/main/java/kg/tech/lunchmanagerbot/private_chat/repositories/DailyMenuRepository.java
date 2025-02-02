package kg.tech.lunchmanagerbot.private_chat.repositories;

import kg.tech.lunchmanagerbot.private_chat.entities.DailyMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyMenuRepository extends JpaRepository<DailyMenuEntity, Long> {
    @Query("select d from DailyMenuEntity d left join fetch d.items where d.day = :day and d.ownerChatId = :chatId")
    Optional<DailyMenuEntity> findByDayAndOwnerChatIdWithItems(@Param("day") LocalDate day, @Param("chatId") String ownerChatId);

    Optional<DailyMenuEntity> findByDayAndOwnerChatId(LocalDate day, String ownerChatId);

    @Query("select m.formattedMenuMessage from DailyMenuEntity m where m.day = :day and m.ownerChatId = :chatId")
    String findDailyMenuMessageByDayAndOwnerChatId(@Param("day") LocalDate day, @Param("chatId") String ownerChatId);

    boolean existsByDayAndOwnerChatId(LocalDate day, String ownerChatId);

}
