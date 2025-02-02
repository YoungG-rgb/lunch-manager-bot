package kg.tech.lunchmanagerbot.group_chat.repositories;

import kg.tech.lunchmanagerbot.group_chat.entities.DutyDayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

public interface DutyDayRepository extends JpaRepository<DutyDayEntity, Long> {

    Optional<DutyDayEntity> findByDay(LocalDate day);

    @Query(value = "select d.* from duty_days d where d.group_chat_id = :groupChatId order by d.day desc limit 1", nativeQuery = true)
    Optional<DutyDayEntity> getLatestDutyDayBy(@Param("groupChatId") String groupChatId);

    @Modifying
    @Transactional
    int deleteByDayAndGroupChatId(LocalDate day, String groupChatId);

}
