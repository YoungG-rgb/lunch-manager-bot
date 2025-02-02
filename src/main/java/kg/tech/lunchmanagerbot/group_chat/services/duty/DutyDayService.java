package kg.tech.lunchmanagerbot.group_chat.services.duty;

import kg.tech.lunchmanagerbot.group_chat.entities.DutyDayEntity;

import java.time.LocalDate;
import java.util.Optional;

public interface DutyDayService {
    Optional<DutyDayEntity> findByDay(LocalDate day, String groupChatId);
    DutyDayEntity getTodayDutyDay(String groupChatId);
    DutyDayEntity save(DutyDayEntity dutyDay);

}
