package kg.tech.lunchmanagerbot.group_chat.mappers;

import kg.tech.lunchmanagerbot.group_chat.entities.AttendantUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.telegram.telegrambots.meta.api.objects.User;

@Mapper
public interface AttendantUserMapper {

    @Mapping(target = "dutyActive", expression = "java( false )")
    AttendantUserEntity toEntity(User user);

}
