package kg.tech.lunchmanagerbot.group_chat.services.duty.impl;

import kg.tech.lunchmanagerbot.group_chat.entities.AttendantUserEntity;
import kg.tech.lunchmanagerbot.group_chat.entities.DutyDayEntity;
import kg.tech.lunchmanagerbot.group_chat.mappers.DutyDayMapper;
import kg.tech.lunchmanagerbot.group_chat.repositories.DutyDayRepository;
import kg.tech.lunchmanagerbot.group_chat.services.duty.AttendantUserService;
import kg.tech.lunchmanagerbot.group_chat.services.duty.DutyDayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DutyDayServiceImpl implements DutyDayService {
    private final DutyDayRepository dutyDayRepository;
    private final DutyDayMapper dutyDayMapper;
    private final AttendantUserService userService;

    @Override
    public Optional<DutyDayEntity> findByDay(LocalDate day, String groupChatId) {
        return dutyDayRepository.findByDay(day);
    }

    @Override
    public DutyDayEntity getTodayDutyDay(String groupChatId) {
        return this.findByDay(LocalDate.now( ), groupChatId).orElseGet(( ) -> this.initDutyDay(groupChatId));
    }

    @Override
    public DutyDayEntity save(DutyDayEntity dutyDay) {
        return dutyDayRepository.save(dutyDay);
    }

    @Override
    public void refreshAttendant(String groupChatId) {
        if (dutyDayRepository.deleteByDayAndGroupChatId(LocalDate.now(), groupChatId) > 0) {
            this.initDutyDay(groupChatId);
        }
    }

    private DutyDayEntity initDutyDay(String groupChatId) {
        AttendantUserEntity dutyUser = dutyDayRepository.getLatestDutyDayBy(groupChatId)
                .map(latestDutyDay -> userService.findFirstNextDutyUser(latestDutyDay.getUserPriority(), groupChatId))
                .orElse(userService.findFirstDutyUser(groupChatId));

        if (dutyUser == null) throw new IllegalStateException("duty user not found");

        DutyDayEntity todayDutyDay = dutyDayMapper.toTodayDutyDay(dutyUser);
        return dutyDayRepository.save(todayDutyDay);
    }

}


