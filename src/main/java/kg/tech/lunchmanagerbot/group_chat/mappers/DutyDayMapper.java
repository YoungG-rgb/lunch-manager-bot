package kg.tech.lunchmanagerbot.group_chat.mappers;

import kg.tech.lunchmanagerbot.group_chat.entities.AttendantUserEntity;
import kg.tech.lunchmanagerbot.group_chat.entities.DutyDayEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DutyDayMapper {

    @Mapping(target = "day", expression = "java( java.time.LocalDate.now() )")
    @Mapping(target = "userPriority", source = "priority")
    DutyDayEntity toTodayDutyDay(AttendantUserEntity nextDutyUser);

}

