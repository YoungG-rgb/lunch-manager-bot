package kg.tech.lunchmanagerbot.group_chat.mappers;

import kg.tech.lunchmanagerbot.group_chat.entities.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.telegram.telegrambots.meta.api.objects.User;

@Mapper
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userExternalId", source = "fromUser.id")
    @Mapping(target = "amount", expression = "java( 1 )")
    @Mapping(target = "day", expression = "java( java.time.LocalDate.now() )")
    @Mapping(target = "fromUsername", expression = "java( kg.tech.lunchmanagerbot.support.utils.StringUtils.joinNonEmptyStrings(\" \",fromUser.getFirstName(), fromUser.getLastName()) )")
    @Mapping(target = "menuItemName", source = "menuItemName")
    OrderEntity toNewEntity(User fromUser, String menuItemName);

}
