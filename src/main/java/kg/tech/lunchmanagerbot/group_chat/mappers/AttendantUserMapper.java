package kg.tech.lunchmanagerbot.group_chat.mappers;

import kg.tech.lunchmanagerbot.group_chat.entities.AttendantUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.telegram.telegrambots.meta.api.objects.User;

@Mapper
public interface AttendantUserMapper {

    @Mappings({
            @Mapping(target = "dutyActive", expression = "java( false )"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "priority", source = "priority"),
            @Mapping(target = "groupChatId", source = "groupChatId"),
            @Mapping(target = "username", source = "user.userName")
    })
    AttendantUserEntity toNewEntity(User user, int priority, String groupChatId);

}
